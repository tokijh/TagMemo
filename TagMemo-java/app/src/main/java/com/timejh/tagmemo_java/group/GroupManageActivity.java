package com.timejh.tagmemo_java.group;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.WordsLayoutManager;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.timejh.tagmemo_java.R;
import com.timejh.tagmemo_java.adapter.HashTagListAdapter;
import com.timejh.tagmemo_java.model.Group;
import com.timejh.tagmemo_java.model.GroupMemo;
import com.timejh.tagmemo_java.model.HashTag;
import com.timejh.tagmemo_java.util.Database;
import com.timejh.tagmemo_java.util.Dialog;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;
import io.realm.RealmList;

public class GroupManageActivity extends AppCompatActivity {

    public static final int MODE_CREATE = 0;
    public static final int MODE_VIEW = 1;
    public static final int MODE_EDIT = 2;

    private int mode;
    private String parentGroupId;
    private String group_id;

    private EditText ed_title;
    private AutoCompleteTextView actv_tag;
    private LinearLayout layout_tag_add;
    private Button bt_tag_add;
    private RecyclerView rv_tag;

    private FloatingActionMenu fab_view;
    private FloatingActionMenu fab_edit;

    private FloatingActionButton fab_view_delete, fab_view_edit;
    private FloatingActionButton fab_edit_delete, fab_edit_save, fab_edit_cancel;

    private HashTagListAdapter hashTagListAdapter;

    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manage);

        initView();

        initAdapter();

        initManager();

        initListener();

        initObserable();

        initIntentValue();

        setMode();
    }

    private void initIntentValue() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("mode")) {
                this.mode = bundle.getInt("mode");
                if (bundle.containsKey("parentGroupId")) {
                    this.parentGroupId = bundle.getString("parentGroupId");
                }
                if (bundle.containsKey("group_id")) {
                    this.group_id = bundle.getString("group_id");
                }
                return;
            }
        }
        this.mode = MODE_CREATE;
    }

    private void initView() {
        ed_title = (EditText) findViewById(R.id.ed_title);
        actv_tag = (AutoCompleteTextView) findViewById(R.id.actv_tag);
        layout_tag_add = (LinearLayout) findViewById(R.id.layout_tag_add);
        bt_tag_add = (Button) findViewById(R.id.bt_tag_add);
        rv_tag = (RecyclerView) findViewById(R.id.rv_tag);

        fab_view = (FloatingActionMenu) findViewById(R.id.fab_view);
        fab_edit = (FloatingActionMenu) findViewById(R.id.fab_edit);

        fab_view_delete = (FloatingActionButton) findViewById(R.id.fab_view_delete);
        fab_view_edit = (FloatingActionButton) findViewById(R.id.fab_view_edit);
        fab_edit_delete = (FloatingActionButton) findViewById(R.id.fab_edit_delete);
        fab_edit_save = (FloatingActionButton) findViewById(R.id.fab_edit_save);
        fab_edit_cancel = (FloatingActionButton) findViewById(R.id.fab_edit_cancel);
    }

    private void initAdapter() {
        hashTagListAdapter = new HashTagListAdapter(hashTagListAdapterCallback);
        rv_tag.setAdapter(hashTagListAdapter);
    }

    private void initManager() {
        rv_tag.setLayoutManager(new WordsLayoutManager(this));
    }

    private void initListener() {
        bt_tag_add.setOnClickListener(onClickListener);
        fab_view_delete.setOnClickListener(onClickListener);
        fab_view_edit.setOnClickListener(onClickListener);
        fab_edit_delete.setOnClickListener(onClickListener);
        fab_edit_save.setOnClickListener(onClickListener);
        fab_edit_cancel.setOnClickListener(onClickListener);
    }

    private void initObserable() {
        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(
                RxTextView.textChangeEvents(actv_tag)
                        .map(t -> t.text().length() > 0)
                        .subscribe(bt_tag_add::setEnabled)
        );
    }

    private void setMode() {
        switch (this.mode) {
            case MODE_CREATE:
                modeCreate();
                break;
            case MODE_VIEW:
                modeView();
                break;
            case MODE_EDIT:
                modeEdit();
                break;
            default: throw new RuntimeException("There is no support mode");
        }
    }

    private void modeCreate() {
        fab_view.setVisibility(View.GONE);
        fab_edit.setVisibility(View.VISIBLE);
        fab_edit_delete.setVisibility(View.GONE);
        layout_tag_add.setVisibility(View.VISIBLE);
        ed_title.setEnabled(true);
    }

    private void modeView() {
        fab_view.setVisibility(View.VISIBLE);
        fab_edit.setVisibility(View.GONE);
        layout_tag_add.setVisibility(View.GONE);
        ed_title.setEnabled(false);

        Group group = Realm.getDefaultInstance()
                .where(Group.class)
                .equalTo("id", group_id)
                .findFirst();

        if (group == null) {
            Toast.makeText(this, "Error on Loading", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ed_title.setText(group.title);
        hashTagListAdapter.set(group.tags);
    }

    private void modeEdit() {
        modeView();

        fab_view.setVisibility(View.GONE);
        fab_edit.setVisibility(View.VISIBLE);
        fab_edit_delete.setVisibility(View.VISIBLE);
        layout_tag_add.setVisibility(View.VISIBLE);
        ed_title.setEnabled(true);
    }

    private void setModeView() {
        this.mode = MODE_VIEW;
        setMode();
    }

    private void setModeEdit() {
        this.mode = MODE_EDIT;
        setMode();
    }

    private void saveGroup() {
        Dialog.show(this);

        Group group = null;

        switch (this.mode) {
            case MODE_CREATE:
                group = createGroup();
                break;
            case MODE_EDIT:
                group = editGroup();
                break;
        }

        refleshParentGroup(saveGroupMemo(group));
        this.group_id = group.id;

        Dialog.dismiss();

        setModeView();
    }

    private Group createGroup() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        Group group = realm.createObject(Group.class);

        group.id = Database.createID(Group.class);
        group.title = ed_title.getText().toString();
        group.parentGroupId = this.parentGroupId;
        group.tags = new RealmList<>();
        for (HashTag hashTag : hashTagListAdapter.get()) {
            group.tags.add(saveTag(realm, hashTag.tag));
        }
        group.tags.sort("tag");
        group.isValidated = false;
        group.last_date = Database.getCurrentDate();

        realm.commitTransaction();
        return group;
    }

    private Group editGroup() {
        Realm realm = Realm.getDefaultInstance();

        Group group = realm.where(Group.class)
                .equalTo("id", group_id)
                .findFirst();
        if (group == null) { // CREATE 로 전환
            mode = MODE_CREATE;
            return createGroup();
        }

        realm.beginTransaction();
        group.title = ed_title.getText().toString();
        group.tags.clear();
        for (HashTag hashTag : hashTagListAdapter.get()) {
            group.tags.add(saveTag(realm, hashTag.tag));
        }
        group.tags.sort("tag");
        group.isValidated = false;
        group.last_date = Database.getCurrentDate();

        realm.commitTransaction();

        return group;
    }

    private GroupMemo saveGroupMemo(Group group) {
        Realm realm = Realm.getDefaultInstance();

        GroupMemo groupMemo = realm.where(GroupMemo.class)
                .equalTo("type_id", group.id)
                .findFirst();
        realm.beginTransaction();
        if (groupMemo == null) {
            groupMemo = realm.createObject(GroupMemo.class);
            groupMemo.id = Database.createID(GroupMemo.class);
            groupMemo.parentGroupId = this.parentGroupId;
            groupMemo.type_id = group.id;
            groupMemo.group = group;
        }
        groupMemo.type = GroupMemo.TYPE_GROUP;
        groupMemo.isValidated = false;
        groupMemo.last_date = Database.getCurrentDate();

        realm.commitTransaction();

        return groupMemo;
    }

    /**
     * 부모의 Group의 GroupMemo를 갱신한다.
     *
     * @param groupMemo
     */
    private void refleshParentGroup(GroupMemo groupMemo) {
        Realm realm = Realm.getDefaultInstance();

        Group group = realm.where(Group.class)
                .equalTo("id", groupMemo.parentGroupId)
                .findFirst();

        realm.beginTransaction();

        if (!group.groupMemos.contains(groupMemo)) {
            group.groupMemos.add(groupMemo);
        }

        realm.commitTransaction();
    }

    private HashTag saveTag(Realm realm, String tag) {
        HashTag hashTag = realm.where(HashTag.class)
                .equalTo("tag", tag)
                .findFirst();
        if (hashTag == null) {
            hashTag = realm.createObject(HashTag.class);
            hashTag.id = Database.createID(HashTag.class);
            hashTag.tag = tag;
            hashTag.last_date = Database.getCurrentDate();
        }
        return hashTag;
    }

    private void addTag() {
        String str_actv_tag = actv_tag.getText().toString();
        actv_tag.setText("");
        for (String tag : str_actv_tag.split(" ")) {
            HashTag hashTag = new HashTag();
            hashTag.tag = tag;
            if ("".equals(tag) || hashTagListAdapter.isContain(hashTag)) {
                continue;
            }
            hashTagListAdapter.add(hashTag);
        }
    }

    private View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.bt_tag_add:
                addTag();
                break;
            case R.id.fab_view_delete:
                break;
            case R.id.fab_view_edit:
                setModeEdit();
                break;
            case R.id.fab_edit_delete:
                break;
            case R.id.fab_edit_save:
                saveGroup();
                break;
            case R.id.fab_edit_cancel:
                fab_view.close(false);
                if (this.mode == MODE_CREATE) {
                    finish();
                    return;
                }
                setModeView();
                break;
        }
        fab_view.close(true);
        fab_edit.close(true);
    };

    private HashTagListAdapter.Callback hashTagListAdapterCallback = position -> {

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
