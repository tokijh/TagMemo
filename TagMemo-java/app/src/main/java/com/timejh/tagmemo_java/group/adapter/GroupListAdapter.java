package com.timejh.tagmemo_java.group.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timejh.tagmemo_java.R;
import com.timejh.tagmemo_java.adapter.HashTagListAdapter;
import com.timejh.tagmemo_java.model.Group;
import com.timejh.tagmemo_java.model.GroupMemo;
import com.timejh.tagmemo_java.model.HashTag;
import com.timejh.tagmemo_java.model.Memo;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

import static com.timejh.tagmemo_java.model.GroupMemo.TYPE_GROUP;
import static com.timejh.tagmemo_java.model.GroupMemo.TYPE_MEMO;

/**
 * Created by tokijh on 2017. 4. 27..
 */

public class GroupListAdapter extends RealmRecyclerViewAdapter {

    private Callback callback;

    public GroupListAdapter(OrderedRealmCollection<GroupMemo> orderedRealmCollection, Callback callback) {
        super(orderedRealmCollection, true);
        this.callback = callback;
    }

    private void setItemGroup(GroupHolder holder, int position) {
        Group group = ((GroupMemo) getItem(position)).group;

        holder.position = position;
        holder.group_id = group.id;
        holder.tv_title.setText(group.title);
        holder.tv_content_count.setText(group.groupMemos.size() + "");
        if (group.tags.size() > 0) {
            holder.rv_tag.setVisibility(View.VISIBLE);
            holder.hashTagListAdapter.set(group.tags);
        } else {
            holder.rv_tag.setVisibility(View.GONE);
        }
    }

    private void setItemMemo(MemoHolder holder, int position) {
        Memo memo = ((GroupMemo) getItem(position)).memo;

        holder.position = position;
        holder.tv_title.setText(memo.title);
        if (memo.tags.size() > 0) {
            holder.rv_tag.setVisibility(View.VISIBLE);
            holder.hashTagListAdapter.set(memo.tags);
        } else {
            holder.rv_tag.setVisibility(View.GONE);
        }
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
        TextView tv_content_count;
        RecyclerView rv_tag;

        HashTagListAdapter hashTagListAdapter;

        public GroupHolder(View itemView) {
            super(itemView);

            initView();

            initAdapter();

            initManager();

            initListener();
        }

        private void initView() {
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content_count = (TextView) itemView.findViewById(R.id.tv_content_count);
            rv_tag = (RecyclerView) itemView.findViewById(R.id.rv_tag);
        }

        private void initListener() {
            itemView.setOnClickListener(groupClickListener);
        }

        private void initAdapter() {
            hashTagListAdapter = new HashTagListAdapter(hashTahCallback);
            rv_tag.setAdapter(hashTagListAdapter);
        }

        private void initManager() {
            rv_tag.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }

        View.OnClickListener groupClickListener =  v -> callback.onGroupClicked(position);

        HashTagListAdapter.Callback hashTahCallback = new HashTagListAdapter.Callback() {
            @Override
            public void onItemClicked(int position) {
                hashTagListAdapter.remove(position);
            }

            @Override
            public void onDataChanged() {

            }
        };
    }

    class MemoHolder extends RecyclerView.ViewHolder {

        int position;

        TextView tv_title;
        RecyclerView rv_tag;

        HashTagListAdapter hashTagListAdapter;

        public MemoHolder(View itemView) {
            super(itemView);

            initView();

            initAdapter();

            initManager();

            initListener();
        }

        private void initView() {
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            rv_tag = (RecyclerView) itemView.findViewById(R.id.rv_tag);
        }

        private void initListener() {
            itemView.setOnClickListener(memoClickListener);
        }

        private void initAdapter() {
            hashTagListAdapter = new HashTagListAdapter(hashTahCallback);
            rv_tag.setAdapter(hashTagListAdapter);
        }

        private void initManager() {
            rv_tag.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }

        View.OnClickListener memoClickListener =  v -> callback.onMemoClicked(position);

        HashTagListAdapter.Callback hashTahCallback = new HashTagListAdapter.Callback() {
            @Override
            public void onItemClicked(int position) {
                hashTagListAdapter.remove(position);
            }

            @Override
            public void onDataChanged() {

            }
        };
    }

    public interface Callback {
        void onGroupClicked(int position);
        void onMemoClicked(int position);
    }
}
