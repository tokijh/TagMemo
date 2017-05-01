package com.timejh.tagmemo_java.group;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import io.realm.Realm;

public class GroupFragment extends Fragment implements GroupListAdapter.Callback {

    private RecyclerView rv_groupmemo;

    private FloatingActionMenu fab;
    private FloatingActionButton fab_edit, fab_add_group, fab_add_memo;

    private GroupListAdapter groupListAdapter;

    private View view;

    private Listener listener;

    private String group_id;

    public GroupFragment(String group_id) {
        this.group_id = group_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.fragment_group, container, false);

        initView();

        initAdapter();

        initManager();

        initListener();

        return view;
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
                getContext(),
                Realm.getDefaultInstance()
                        .where(GroupMemo.class)
                        .equalTo("parentId", group_id)
                        .findAllSorted("position"),
                this);
        rv_groupmemo.setAdapter(groupListAdapter);
    }

    private void initManager() {
        rv_groupmemo.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initListener() {
        fab_edit.setOnClickListener(onClickListener);
        fab_add_group.setOnClickListener(onClickListener);
        fab_add_memo.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.fab_edit:
                Log.e("TAGG", Realm.getDefaultInstance().where(HashTag.class).findAll().toString());
                Log.e("TAGGA", Realm.getDefaultInstance().where(Group.class).findAll().toString());
                Log.e("TAGGB", Realm.getDefaultInstance().where(GroupMemo.class).findAll().toString());
                break;
            case R.id.fab_add_group:
                listener.onClickAddGroup(group_id);
                break;
            case R.id.fab_add_memo:
                listener.onClickAddMemo(group_id);
                break;
        }
        fab.close(true);
    };

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
        listener.onClickGroup(groupMemo.group.id);
    }

    @Override
    public void onMemoClicked(int position) {
        GroupMemo groupMemo = groupListAdapter.get(position);
        listener.onClickMemo(groupMemo.memo.id);
    }

    public interface Listener {
        void onClickGroup(String group_id);

        void onClickMemo(String memo_id);

        void onClickAddGroup(String group_id);

        void onClickAddMemo(String group_id);
    }
}
