package com.timejh.tagmemo_java.group;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.timejh.tagmemo_java.R;
import com.timejh.tagmemo_java.model.Group;
import com.timejh.tagmemo_java.model.GroupMemo;
import com.timejh.tagmemo_java.model.Memo;
import com.timejh.tagmemo_java.util.Database;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class GroupActivity extends AppCompatActivity implements GroupFragment.Listener {

    private FrameLayout contentView;

    private FragmentManager manager;

    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        initView();

        initFragmentSettings();

        initValue();
    }

    private void initView() {
        contentView = (FrameLayout) findViewById(R.id.contentView);
    }

    private void initFragmentSettings() {
        manager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();

        fragmentList.add(new GroupFragment(""));

        showContentFragment();
    }

    private void initValue() {
        // 테스트용 DB저장
        Realm.getDefaultInstance().executeTransaction(realm -> {
            {
                Group group1 = realm.createObject(Group.class, Database.createID(Group.class));
                group1.parentId = "";
                group1.title = "그룹1";
                group1.last_date = Database.getCurrentDate();

                GroupMemo groupMemo_group1 = realm.createObject(GroupMemo.class, Database.createID(GroupMemo.class));
                groupMemo_group1.type = GroupMemo.TYPE_GROUP;
                groupMemo_group1.parentId = "";
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
                group2.parentId = "";
                group2.title = "그룹2";
                group2.last_date = Database.getCurrentDate();

                GroupMemo groupMemo_group2 = realm.createObject(GroupMemo.class, Database.createID(GroupMemo.class));
                groupMemo_group2.parentId = "";
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

                        Memo group2_group1_memo2 = realm.createObject(Memo.class, Database.createID(Memo.class));
                        group2_group1_memo2.title = "그룹2-그룹1-메모2";
                        group2_group1_memo2.content = "그룹2-그룹1-메모2";
                        group2_group1_memo2.parentId = group2_group1.id;
                        group2_group1_memo2.position = 1;
                        group2_group1_memo2.last_date = Database.getCurrentDate();

                        GroupMemo groupMemo_group2_group1_memo2 = realm.createObject(GroupMemo.class, Database.createID(GroupMemo.class));
                        groupMemo_group2_group1_memo2.type = GroupMemo.TYPE_MEMO;
                        groupMemo_group2_group1_memo2.memo = group2_group1_memo1;
                        groupMemo_group2_group1_memo2.parentId = group2_group1.id;
                        groupMemo_group2_group1_memo2.last_date = Database.getCurrentDate();
                        groupMemo_group2_group1_memo2.position = 1;

                        Group group2_group1_group1 = realm.createObject(Group.class, Database.createID(Group.class));
                        group2_group1_group1.title = "그룹2-그룹1-그룹1";
                        group2_group1_group1.parentId = group2_group1.id;
                        group2_group1_group1.position = 2;
                        group2_group1_group1.last_date = Database.getCurrentDate();

                        GroupMemo groupMemo_group2_group1_group1 = realm.createObject(GroupMemo.class, Database.createID(GroupMemo.class));
                        groupMemo_group2_group1_group1.type = GroupMemo.TYPE_GROUP;
                        groupMemo_group2_group1_group1.group = group2_group1_group1;
                        groupMemo_group2_group1_group1.parentId = group2_group1.id;
                        groupMemo_group2_group1_group1.last_date = Database.getCurrentDate();
                        groupMemo_group2_group1_group1.position = 2;

                        {
                            Memo group2_group1_group1_memo1 = realm.createObject(Memo.class, Database.createID(Memo.class));
                            group2_group1_group1_memo1.title = "그룹2-그룹1-그룹1-메모1";
                            group2_group1_group1_memo1.content = "그룹2-그룹1-그룹1-메모1";
                            group2_group1_group1_memo1.parentId = group2_group1_group1.id;
                            group2_group1_group1_memo1.position = 0;
                            group2_group1_group1_memo1.last_date = Database.getCurrentDate();

                            GroupMemo groupMemo_group2_group1_group_1memo1 = realm.createObject(GroupMemo.class, Database.createID(GroupMemo.class));
                            groupMemo_group2_group1_group_1memo1.type = GroupMemo.TYPE_MEMO;
                            groupMemo_group2_group1_group_1memo1.memo = group2_group1_group1_memo1;
                            groupMemo_group2_group1_group_1memo1.parentId = group2_group1_group1.id;
                            groupMemo_group2_group1_group_1memo1.last_date = Database.getCurrentDate();
                            groupMemo_group2_group1_group_1memo1.position = 0;

                            Memo group2_group1_group1_memo2 = realm.createObject(Memo.class, Database.createID(Memo.class));
                            group2_group1_group1_memo2.title = "그룹2-그룹1-그룹1-메모2";
                            group2_group1_group1_memo2.content = "그룹2-그룹1-그룹1-메모2";
                            group2_group1_group1_memo2.parentId = group2_group1_group1.id;
                            group2_group1_group1_memo2.position = 1;
                            group2_group1_group1_memo2.last_date = Database.getCurrentDate();

                            GroupMemo groupMemo_group2_group1_group1_memo2 = realm.createObject(GroupMemo.class, Database.createID(GroupMemo.class));
                            groupMemo_group2_group1_group1_memo2.type = GroupMemo.TYPE_MEMO;
                            groupMemo_group2_group1_group1_memo2.memo = group2_group1_group1_memo2;
                            groupMemo_group2_group1_group1_memo2.parentId = group2_group1_group1.id;
                            groupMemo_group2_group1_group1_memo2.last_date = Database.getCurrentDate();
                            groupMemo_group2_group1_group1_memo2.position = 1;
                        }
                    }
                }
            }
        });
    }

    public void showContentFragment(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.contentView, fragment)
                .addToBackStack(fragment.getTag())
                .commit();
    }

    public void showContentFragment(int position) {
        showContentFragment(fragmentList.get(position));
    }

    public void pushContentFragment(Fragment fragment) {
        fragmentList.add(fragment);
        showContentFragment();
    }

    public void popContentFragment() {
        if (fragmentList.size() > 1) {
            fragmentList.remove(fragmentList.size() - 1);
        }
    }

    public void showContentFragment() {
        if (fragmentList.size() > 0) {
            showContentFragment(fragmentList.get(fragmentList.size() - 1));
        }
    }

    public void popshowContentFragment() {
        popContentFragment();
        showContentFragment();
    }

    public boolean isPopable() {
        return fragmentList.size() > 1;
    }

    private void startGroupManager(int mode, String group_id) {
        startActivity(new Intent(this, GroupManageActivity.class)
                .putExtra("mode", mode)
                .putExtra("group_id", group_id));
    }

    @Override
    public void onBackPressed() {
        if (isPopable()) {
            popshowContentFragment();
            return;
        }
        finish();
    }

    @Override
    public void onClickGroup(String group_id) {
        Log.d("here", group_id);
        fragmentList.add(new GroupFragment(group_id));
        showContentFragment();
    }

    @Override
    public void onClickMemo(String memo_id) {

    }

    @Override
    public void onClickAddGroup(String group_id) {
        startGroupManager(GroupManageActivity.MODE_CREATE, group_id);
    }

    @Override
    public void onClickAddMemo(String group_id) {

    }
}
