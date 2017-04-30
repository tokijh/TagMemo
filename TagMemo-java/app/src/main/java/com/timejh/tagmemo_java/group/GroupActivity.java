package com.timejh.tagmemo_java.group;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.timejh.tagmemo_java.R;
import com.timejh.tagmemo_java.group.adapter.GroupListAdapter;
import com.timejh.tagmemo_java.model.Group;
import com.timejh.tagmemo_java.model.GroupMemo;
import com.timejh.tagmemo_java.model.HashTag;
import com.timejh.tagmemo_java.model.Memo;
import com.timejh.tagmemo_java.util.Database;

import io.realm.Realm;

public class GroupActivity extends AppCompatActivity implements GroupListAdapter.Callback {

    private RecyclerView rv_group;

    private FloatingActionMenu fab;
    private FloatingActionButton fab_edit, fab_add;

    private GroupListAdapter groupListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        initView();

        initAdapter();

        initManager();

        initListener();

        initValue();
    }

    private void initView() {
        rv_group = (RecyclerView) findViewById(R.id.rv_group);

        fab = (FloatingActionMenu) findViewById(R.id.fab);
        fab_edit = (FloatingActionButton) findViewById(R.id.fab_edit);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
    }

    private void initAdapter() {
        groupListAdapter = new GroupListAdapter(Realm.getDefaultInstance().where(GroupMemo.class).isNull("parentId").findAllSorted("position"), this);
        rv_group.setAdapter(groupListAdapter);
    }

    private void initManager() {
        rv_group.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initListener() {
        fab_edit.setOnClickListener(onClickListener);
        fab_add.setOnClickListener(onClickListener);
    }

    private void initValue() {
        // 테스트용 DB저장
        Realm.getDefaultInstance().executeTransaction(realm -> {
            {
                Group group1 = realm.createObject(Group.class, Database.createID(Group.class));
                group1.title = "그룹1";
                group1.last_date = Database.getCurrentDate();

                GroupMemo groupMemo_group1 = realm.createObject(GroupMemo.class, Database.createID(GroupMemo.class));
                groupMemo_group1.type = GroupMemo.TYPE_GROUP;
                groupMemo_group1.group = group1;
                groupMemo_group1.last_date = Database.getCurrentDate();
                groupMemo_group1.position = realm.where(GroupMemo.class).max("position").longValue() + 1;

                {
                    Memo group1_memo1 = realm.createObject(Memo.class, Database.createID(Memo.class));
                    group1_memo1.title = "그룹1-메모1";
                    group1_memo1.content = "그룹1-메모1";
                    group1_memo1.parentId = group1.id;
                    group1_memo1.position = 0;
                    group1_memo1.last_date = Database.getCurrentDate();

                    GroupMemo groupMemo_group1_memo1 = realm.createObject(GroupMemo.class, Database.createID(GroupMemo.class));
                    groupMemo_group1_memo1.type = GroupMemo.TYPE_MEMO;
                    groupMemo_group1_memo1.memo = group1_memo1;
                    groupMemo_group1_memo1.parentId = group1.id;
                    groupMemo_group1_memo1.last_date = Database.getCurrentDate();
                    groupMemo_group1_memo1.position = 0;

                    Memo group1_memo2 = realm.createObject(Memo.class, Database.createID(Memo.class));
                    group1_memo2.title = "그룹1-메모2";
                    group1_memo2.content = "그룹1-메모2";
                    group1_memo2.parentId = group1.id;
                    group1_memo2.position = 1;
                    group1_memo2.last_date = Database.getCurrentDate();

                    GroupMemo groupMemo_group1_memo2 = realm.createObject(GroupMemo.class, Database.createID(GroupMemo.class));
                    groupMemo_group1_memo2.type = GroupMemo.TYPE_MEMO;
                    groupMemo_group1_memo2.memo = group1_memo2;
                    groupMemo_group1_memo2.parentId = group1.id;
                    groupMemo_group1_memo2.last_date = Database.getCurrentDate();
                    groupMemo_group1_memo2.position = 1;

                    Group group1_group1 = realm.createObject(Group.class, Database.createID(Group.class));
                    group1_group1.title = "그룹1-그룹1";
                    group1_group1.parentId = group1.id;
                    group1_group1.position = 2;
                    group1_group1.last_date = Database.getCurrentDate();

                    GroupMemo groupMemo_group1_group1 = realm.createObject(GroupMemo.class, Database.createID(GroupMemo.class));
                    groupMemo_group1_group1.type = GroupMemo.TYPE_GROUP;
                    groupMemo_group1_group1.group = group1_group1;
                    groupMemo_group1_group1.parentId = group1.id;
                    groupMemo_group1_group1.last_date = Database.getCurrentDate();
                    groupMemo_group1_group1.position = 2;

                    {
                        Memo group1_group1_memo1 = realm.createObject(Memo.class, Database.createID(Memo.class));
                        group1_group1_memo1.title = "그룹1-그룹1-메모1";
                        group1_group1_memo1.content = "그룹1-그룹1-메모1";
                        group1_group1_memo1.parentId = group1_group1.id;
                        group1_group1_memo1.position = 0;
                        group1_group1_memo1.last_date = Database.getCurrentDate();

                        GroupMemo groupMemo_group1_group1_memo1 = realm.createObject(GroupMemo.class, Database.createID(GroupMemo.class));
                        groupMemo_group1_group1_memo1.type = GroupMemo.TYPE_MEMO;
                        groupMemo_group1_group1_memo1.memo = group1_group1_memo1;
                        groupMemo_group1_group1_memo1.parentId = group1_group1.id;
                        groupMemo_group1_group1_memo1.last_date = Database.getCurrentDate();
                        groupMemo_group1_group1_memo1.position = 0;
                    }
                }

                Group group2 = realm.createObject(Group.class, Database.createID(Group.class));
                group2.title = "그룹2";
                group2.last_date = Database.getCurrentDate();

                GroupMemo groupMemo_group2 = realm.createObject(GroupMemo.class, Database.createID(GroupMemo.class));
                groupMemo_group2.type = GroupMemo.TYPE_GROUP;
                groupMemo_group2.group = group2;
                groupMemo_group2.last_date = Database.getCurrentDate();
                groupMemo_group2.position = realm.where(GroupMemo.class).max("position").longValue() + 1;

                {
                    Memo group2_memo1 = realm.createObject(Memo.class, Database.createID(Memo.class));
                    group2_memo1.title = "그룹2-메모1";
                    group2_memo1.content = "그룹2-메모1";
                    group2_memo1.parentId = group2.id;
                    group2_memo1.position = 0;
                    group2_memo1.last_date = Database.getCurrentDate();

                    GroupMemo groupMemo_group2_memo1 = realm.createObject(GroupMemo.class, Database.createID(GroupMemo.class));
                    groupMemo_group2_memo1.type = GroupMemo.TYPE_MEMO;
                    groupMemo_group2_memo1.memo = group2_memo1;
                    groupMemo_group2_memo1.parentId = group2.id;
                    groupMemo_group2_memo1.last_date = Database.getCurrentDate();
                    groupMemo_group2_memo1.position = 0;

                    Memo group2_memo2 = realm.createObject(Memo.class, Database.createID(Memo.class));
                    group2_memo2.title = "그룹2-메모2";
                    group2_memo2.content = "그룹2-메모2";
                    group2_memo2.parentId = group2.id;
                    group2_memo2.position = 1;
                    group2_memo2.last_date = Database.getCurrentDate();

                    GroupMemo groupMemo_group2_memo2 = realm.createObject(GroupMemo.class, Database.createID(GroupMemo.class));
                    groupMemo_group2_memo2.type = GroupMemo.TYPE_MEMO;
                    groupMemo_group2_memo2.memo = group2_memo2;
                    groupMemo_group2_memo2.parentId = group2.id;
                    groupMemo_group2_memo2.last_date = Database.getCurrentDate();
                    groupMemo_group2_memo2.position = 1;

                    Group group2_group1 = realm.createObject(Group.class, Database.createID(Group.class));
                    group2_group1.title = "그룹2-그룹1";
                    group2_group1.parentId = group2.id;
                    group2_group1.position = 2;
                    group2_group1.last_date = Database.getCurrentDate();

                    GroupMemo groupMemo_group2_group1 = realm.createObject(GroupMemo.class, Database.createID(GroupMemo.class));
                    groupMemo_group2_group1.type = GroupMemo.TYPE_GROUP;
                    groupMemo_group2_group1.group = group2_group1;
                    groupMemo_group2_group1.parentId = group2.id;
                    groupMemo_group2_group1.last_date = Database.getCurrentDate();
                    groupMemo_group2_group1.position = 2;

                    {
                        Memo group2_group1_memo1 = realm.createObject(Memo.class, Database.createID(Memo.class));
                        group2_group1_memo1.title = "그룹2-그룹1-메모1";
                        group2_group1_memo1.content = "그룹2-그룹1-메모1";
                        group2_group1_memo1.parentId = group2_group1.id;
                        group2_group1_memo1.position = 0;
                        group2_group1_memo1.last_date = Database.getCurrentDate();

                        GroupMemo groupMemo_group2_group1_memo1 = realm.createObject(GroupMemo.class, Database.createID(GroupMemo.class));
                        groupMemo_group2_group1_memo1.type = GroupMemo.TYPE_MEMO;
                        groupMemo_group2_group1_memo1.memo = group2_group1_memo1;
                        groupMemo_group2_group1_memo1.parentId = group2_group1.id;
                        groupMemo_group2_group1_memo1.last_date = Database.getCurrentDate();
                        groupMemo_group2_group1_memo1.position = 0;
                    }
                }
            }
        });
    }

    private void startGroupManager(int mode, String group_id) {
        startActivity(new Intent(this, GroupManageActivity.class)
                .putExtra("mode", mode)
                .putExtra("group_id", group_id));
    }

    private View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.fab_edit:
                Log.e("TAGG", Realm.getDefaultInstance().where(HashTag.class).findAll().toString());
                Log.e("TAGGA", Realm.getDefaultInstance().where(Group.class).findAll().toString());
                Log.e("TAGGB", Realm.getDefaultInstance().where(GroupMemo.class).findAll().toString());
                break;
            case R.id.fab_add:
                startGroupManager(GroupManageActivity.MODE_CREATE, null);
                break;
        }
        fab.close(true);
    };

    @Override
    public void onGroupClicked(int position) {
        startGroupManager(GroupManageActivity.MODE_VIEW, groupListAdapter.get(position).id);
    }

    @Override
    public void onMemoClicked(int position) {

    }
}
