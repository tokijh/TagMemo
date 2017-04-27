package com.timejh.tagmemo_java.group;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.timejh.tagmemo_java.R;
import com.timejh.tagmemo_java.group.adapter.GroupListAdapter;

public class GroupListActivity extends AppCompatActivity implements GroupListAdapter.Callback {

    private RecyclerView rv_group;
    private GroupListAdapter groupListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        initView();

        initAdapter();

        initManager();

        startActivity(new Intent(this, GroupManageActivity.class));
    }

    private void initView() {
        rv_group = (RecyclerView) findViewById(R.id.rv_group);
    }

    private void initAdapter() {
        groupListAdapter = new GroupListAdapter(this, this);
        rv_group.setAdapter(groupListAdapter);
    }

    private void initManager() {
        rv_group.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemClicked(int position) {

    }
}
