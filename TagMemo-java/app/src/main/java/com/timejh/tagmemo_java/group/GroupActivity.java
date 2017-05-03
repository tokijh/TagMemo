package com.timejh.tagmemo_java.group;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.timejh.tagmemo_java.R;
import com.timejh.tagmemo_java.memo.MemoManageActivity;
import com.timejh.tagmemo_java.model.Group;
import com.timejh.tagmemo_java.model.GroupMemo;
import com.timejh.tagmemo_java.util.Database;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class GroupActivity extends AppCompatActivity implements GroupFragment.Listener {

    private FragmentManager manager;

    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        initFragmentSettings();

        initRootGroup();
    }

    private void initFragmentSettings() {
        manager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();

        fragmentList.add(new GroupFragment("/"));

        showContentFragment();
    }

    private void initRootGroup() {
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(Group.class).findAll().size() == 0) {
            realm.beginTransaction();

            Group group = realm.createObject(Group.class);
            group.id = "/";
            group.isValidated = false;
            group.last_date = Database.getCurrentDate();

            GroupMemo groupMemo = realm.createObject(GroupMemo.class);
            groupMemo.id = "/";
            groupMemo.parentGroupId = "/";
            groupMemo.group = group;
            groupMemo.isValidated = false;
            groupMemo.last_date = Database.getCurrentDate();

            realm.commitTransaction();
        }
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

    private void startGroupManager(int mode, String parentGroupId, String group_id) {
        startActivity(new Intent(this, GroupManageActivity.class)
                .putExtra("mode", mode)
                .putExtra("parentGroupId", parentGroupId)
                .putExtra("group_id", group_id));
    }

    private void startMemoManager(String parentGroupId, String memo_id) {
        startActivity(new Intent(this, MemoManageActivity.class)
                .putExtra("parentGroupId", parentGroupId)
                .putExtra("memo_id", memo_id));
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
    public void onClickGroup(String parentGroupId, String group_id) {
        fragmentList.add(new GroupFragment(group_id));
        showContentFragment();
    }

    @Override
    public void onClickMemo(String parentGroupId, String memo_id) {
        startMemoManager(parentGroupId, memo_id);
    }

    @Override
    public void onClickAddGroup(String parentGroupId) {
        startGroupManager(GroupManageActivity.MODE_CREATE, parentGroupId, null);
    }

    @Override
    public void onClickAddMemo(String parentGroupId) {
        startMemoManager(parentGroupId, null);
    }
}
