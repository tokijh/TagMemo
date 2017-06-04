package com.timejh.tagmemo_java.adapter;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timejh.tagmemo_java.R;
import com.timejh.tagmemo_java.model.HashTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tokijh on 2017. 4. 27..
 */

public class HashTagListAdapter extends RecyclerView.Adapter {

    private Callback callback;

    private List<HashTag> hashTags;

    public HashTagListAdapter(Callback callback) {
        this.callback = callback;
        hashTags = new ArrayList<>();
    }

    public void add(HashTag hashTag) {
        hashTags.add(hashTag);
        notifyDataChanged();
    }

    public void set(List<HashTag> hashTags) {
        this.hashTags.clear();
        for (HashTag hashTag : hashTags) {
            this.hashTags.add(hashTag);
        }
        notifyDataChanged();
    }

    public void remove(int position) {
        hashTags.remove(position);
        notifyDataChanged();
    }

    public void sort() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            hashTags.sort((o1, o2) -> o1.tag.compareTo(o2.tag));
        } else {
            Collections.sort(hashTags, (o1, o2) -> o1.tag.compareTo(o2.tag));
        }
        notifyDataChanged();
    }

    public List<HashTag> get() {
        return hashTags;
    }

    public HashTag get(int position) {
        return hashTags.get(position);
    }

    public boolean isContain(HashTag hashTag) {
        for (HashTag listHashTag : hashTags) {
            if (listHashTag.tag.equals(hashTag.tag))
                return true;
        }
        return false;
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

    private void notifyDataChanged() {
        callback.onDataChanged();
        this.notifyDataSetChanged();
    }

    public interface Callback {
        void onItemClicked(int position);
        void onDataChanged();
    }
}
