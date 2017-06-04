package com.timejh.tagmemo_java.group;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.timejh.tagmemo_java.R;
import com.timejh.tagmemo_java.group.adapter.GroupListAdapter;
import com.timejh.tagmemo_java.model.Group;
import com.timejh.tagmemo_java.model.GroupMemo;
import com.timejh.tagmemo_java.model.HashTag;
import com.timejh.tagmemo_java.model.Memo;

import io.realm.Realm;

public class GroupFragment extends Fragment implements GroupListAdapter.Callback {

    private RecyclerView rv_groupmemo;

    private FloatingActionMenu fab;
    private FloatingActionButton fab_edit, fab_add_group, fab_add_memo;

    private GroupListAdapter groupListAdapter;

    private View view;

    private Listener listener;

    private String parentGroupId;
    private Group group;

    public GroupFragment(String parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.fragment_group, container, false);

        initGroup();

        initView();

        initAdapter();

        initManager();

        initListener();

        return view;
    }

    private void initGroup() {
        this.group = Realm.getDefaultInstance()
                .where(Group.class)
                .equalTo("id", parentGroupId)
                .findFirst();
    }

    private void initView() {
        rv_groupmemo = (RecyclerView) view.findViewById(R.id.rv_groupmemo);

        fab = (FloatingActionMenu) view.findViewById(R.id.fab);
        fab_edit = (FloatingActionButton) view.findViewById(R.id.fab_edit);
        fab_add_group = (FloatingActionButton) view.findViewById(R.id.fab_add_group);
        fab_add_memo = (FloatingActionButton) view.findViewById(R.id.fab_add_memo);
    }

    private void initAdapter() {
        groupListAdapter = new GroupListAdapter(
                this.group.groupMemos,
                this);
        rv_groupmemo.setAdapter(groupListAdapter);
    }

    private void initManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_groupmemo.setLayoutManager(linearLayoutManager);
        rv_groupmemo.addItemDecoration(new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation()));
    }

    private void initListener() {
        fab_edit.setOnClickListener(onClickListener);
        fab_add_group.setOnClickListener(onClickListener);
        fab_add_memo.setOnClickListener(onClickListener);
    }

    private void initToolbar() {
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolBar);
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);

        // Set default title in toolbar
        if (group.parentGroupId != null) {
            toolbar.setTitle(this.group.title);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            toolbar.setTitle(R.string.toolbar_title);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        // TODO use SearchView
//        SearchView searchView = (SearchView) toolbar.getMenu().findItem(R.id.menu_search).getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
    }

    private View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.fab_edit:
                // Realm 데이터를 보기 위한 임시 로직
                Log.e("HashTag", Realm.getDefaultInstance().where(HashTag.class).findAll().toString());
                Log.e("Group", Realm.getDefaultInstance().where(Group.class).findAll().toString());
                Log.e("GroupMemo", Realm.getDefaultInstance().where(GroupMemo.class).findAll().toString());
                Log.e("Memo", Realm.getDefaultInstance().where(Memo.class).findAll().toString());
                break;
            case R.id.fab_add_group:
                listener.onClickAddGroup(parentGroupId);
                break;
            case R.id.fab_add_memo:
                listener.onClickAddMemo(parentGroupId);
                break;
        }
        fab.close(true);
    };

    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = (item) -> {
        switch (item.getItemId()) {
            case R.id.add_folder:
                break;
            case R.id.add_memo:
                break;
            case R.id.edit:
                break;
        }
        return true;
    };

    @Override
    public void onResume() {
        super.onResume();

        initToolbar();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onGroupClicked(int position) {
        GroupMemo groupMemo = groupListAdapter.get(position);
        listener.onClickGroup(parentGroupId, groupMemo.group.id);
    }

    @Override
    public void onMemoClicked(int position) {
        GroupMemo groupMemo = groupListAdapter.get(position);
        listener.onClickMemo(parentGroupId, groupMemo.memo.id);
    }

    public interface Listener {
        void onClickGroup(String parentGroupId, String group_id);

        void onClickMemo(String parentGroupId, String memo_id);

        void onClickAddGroup(String parentGroupId);

        void onClickAddMemo(String parentGroupId);

        void onBackPress();
    }
}
