package com.timejh.tagmemo_java.group.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timejh.tagmemo_java.R;
import com.timejh.tagmemo_java.model.Group;
import com.timejh.tagmemo_java.model.GroupMemo;
import com.timejh.tagmemo_java.model.Memo;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

import static com.timejh.tagmemo_java.model.GroupMemo.TYPE_GROUP;
import static com.timejh.tagmemo_java.model.GroupMemo.TYPE_MEMO;

/**
 * Created by tokijh on 2017. 4. 27..
 */

public class GroupListAdapter extends RealmRecyclerViewAdapter {

    private Context context;
    private Callback callback;

    public GroupListAdapter(Context context, OrderedRealmCollection<GroupMemo> orderedRealmCollection, Callback callback) {
        super(orderedRealmCollection, true);
        this.context = context;
        this.callback = callback;
    }

    private void setItemGroup(GroupHolder holder, int position) {
        Group group = ((GroupMemo) getItem(position)).group;

        holder.group_id = group.id;
        holder.initAdapter();
        holder.tv_title.setText(group.title);
        holder.tv_count.setText(group.tags.size() + "");
    }

    private void setItemMemo(MemoHolder holder, int position) {
        Memo memo = ((GroupMemo) getItem(position)).memo;

        holder.tv_title.setText(memo.title);
    }

    public GroupMemo get(int position) {
        return (GroupMemo) getItem(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_GROUP:
                return new GroupHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false));
            case TYPE_MEMO:
                return new MemoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo, parent, false));
        }
        throw new RuntimeException("There is no type that matches");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_GROUP:
                setItemGroup((GroupHolder) holder, position);
                break;
            case TYPE_MEMO:
                setItemMemo((MemoHolder) holder, position);
                break;
        }
    }

    @Override
    public long getItemId(int index) {
        return super.getItemId(index);
    }

    @Override
    public int getItemViewType(int position) {
        return ((GroupMemo) getItem(position)).type;
    }

    class GroupHolder extends RecyclerView.ViewHolder {

        int position;

        String group_id;

        TextView tv_title;
        TextView tv_count;
        RecyclerView rv_groupmemo;

        GroupListAdapter groupListAdapter;

        public GroupHolder(View itemView) {
            super(itemView);

            initView();

//            initAdapter();

            initManager();

            initListener();
        }

        private void initView() {
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            rv_groupmemo = (RecyclerView) itemView.findViewById(R.id.rv_groupmemo);
        }

        private void initAdapter() {
            groupListAdapter = new GroupListAdapter(
                    context,
                    Realm.getDefaultInstance()
                            .where(GroupMemo.class)
                            .equalTo("parentId", group_id)
                            .findAllSorted("position"),
                    callback
            );
            rv_groupmemo.setAdapter(groupListAdapter);
        }

        private void initManager() {
            rv_groupmemo.setLayoutManager(new LinearLayoutManager(context));
        }

        private void initListener() {
            itemView.setOnClickListener(groupClickListener);
        }

        Callback callback = new Callback() {
            @Override
            public void onGroupClicked(int position) {

            }

            @Override
            public void onMemoClicked(int position) {

            }
        };

        View.OnClickListener groupClickListener =  v -> callback.onGroupClicked(position);
    }

    class MemoHolder extends RecyclerView.ViewHolder {

        int position;

        TextView tv_title;

        public MemoHolder(View itemView) {
            super(itemView);

            initView();

            initListener();
        }

        private void initView() {
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }

        private void initListener() {
            itemView.setOnClickListener(memoClickListener);
        }

        View.OnClickListener memoClickListener =  v -> callback.onMemoClicked(position);
    }

    public interface Callback {
        void onGroupClicked(int position);
        void onMemoClicked(int position);
    }
}
