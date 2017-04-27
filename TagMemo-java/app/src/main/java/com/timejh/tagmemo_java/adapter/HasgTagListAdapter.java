package com.timejh.tagmemo_java.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timejh.tagmemo_java.R;
import com.timejh.tagmemo_java.model.HashTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tokijh on 2017. 4. 27..
 */

public class HasgTagListAdapter extends RecyclerView.Adapter {

    private Context context;
    private Callback callback;

    private List<HashTag> hashTags;

    public HasgTagListAdapter(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;

        hashTags = new ArrayList<>();
    }

    public void add(HashTag hashTag) {
        hashTags.add(hashTag);
        this.notifyDataSetChanged();
    }

    public void set(List<HashTag> hashTags) {
        this.hashTags = hashTags;
        this.notifyDataSetChanged();
    }

    public void remove(int position) {
        hashTags.remove(position);
        this.notifyDataSetChanged();
    }

    public HashTag get(int position) {
        return hashTags.get(position);
    }

    private void setHashTags(ItemHashTag itemHashTag, int position) {
        HashTag hashTag = hashTags.get(position);

        itemHashTag.tv_tag.setText(hashTag.tag);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHashTag(LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setHashTags((ItemHashTag) holder, position);
    }

    @Override
    public int getItemCount() {
        return hashTags.size();
    }

    private class ItemHashTag extends RecyclerView.ViewHolder {

        int position;

        TextView tv_tag;

        public ItemHashTag(View itemView) {
            super(itemView);

            initView();

            initListener();
        }

        private void initView() {
            tv_tag = (TextView) itemView.findViewById(R.id.tv_tag);
        }

        private void initListener() {
            itemView.setOnClickListener(itemHashTagClickListener);
        }

        private View.OnClickListener itemHashTagClickListener = v -> callback.onItemClicked(position);
    }

    public interface Callback {
        void onItemClicked(int position);
    }
}
