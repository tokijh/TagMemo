package com.timejh.tagmemo_java.group.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timejh.tagmemo_java.R;
import com.timejh.tagmemo_java.model.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tokijh on 2017. 4. 27..
 */

public class GroupListAdapter extends RecyclerView.Adapter {

    private static final int ITEM_GROUP = 0;

    private List<GroupExtend> groupExtends;

    private Context context;
    private Callback callback;

    public GroupListAdapter(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
        groupExtends = new ArrayList<>();
    }

    public void add(Group group) {
        groupExtends.add(new GroupExtend(ITEM_GROUP, group));
        this.notifyDataSetChanged();
    }

    public void set(List<Group> groups) {
        groupExtends.clear();
        for (Group group : groups) {
            add(group);
        }
        this.notifyDataSetChanged();
    }

    private void setItemGroup(ItemGroup itemGroup, int position) {
        Group group = groupExtends.get(position).group;
        itemGroup.tv_title.setText(group.title);
        itemGroup.tv_count.setText(group.count + "");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_GROUP:
                return new ItemGroup(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_group_item, parent, false));
        }
        throw new RuntimeException("There is no type that matches");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case ITEM_GROUP :
                setItemGroup((ItemGroup) holder, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return groupExtends.size();
    }

    @Override
    public int getItemViewType(int position) {
        return groupExtends.get(position).type;
    }

    private class ItemGroup extends RecyclerView.ViewHolder {

        int position;

        TextView tv_title;
        TextView tv_count;

        public ItemGroup(View itemView) {
            super(itemView);

            initView();

            initListener();
        }

        private void initView() {
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
        }

        private void initListener() {
            itemView.setOnClickListener(itemGroupClickListener);
        }

        View.OnClickListener itemGroupClickListener =  v -> callback.onItemClicked(position);
    }

    private class GroupExtend {
        int type;
        Group group;

        GroupExtend(int type, Group group) {
            this.type = type;
            this.group = group;
        }
    }

    public interface Callback {
        void onItemClicked(int position);
    }
}
