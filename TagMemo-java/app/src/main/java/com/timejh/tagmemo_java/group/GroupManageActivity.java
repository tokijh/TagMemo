package com.timejh.tagmemo_java.group;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.WordsLayoutManager;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.timejh.tagmemo_java.R;
import com.timejh.tagmemo_java.adapter.HashTagListAdapter;
import com.timejh.tagmemo_java.model.Group;
import com.timejh.tagmemo_java.model.GroupMemo;
import com.timejh.tagmemo_java.model.HashTag;
import com.timejh.tagmemo_java.util.Database;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;
import io.realm.RealmList;

public class GroupManageActivity extends AppCompatActivity {

    public static final int MODE_CREATE = 0;
    public static final int MODE_EDIT = 1;

    private String parentGroupId = null;
    private String group_id = null;
    private Group group = null;
    private GroupMemo groupMemo = null;

    private EditText ed_title;
    private AutoCompleteTextView actv_tag;
    private Button bt_tag_add;
    private RecyclerView rv_tag;

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

        initIntentValue();

        initValue();

        initObserable();
    }

    private void initIntentValue() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("mode")) {
                switch (bundle.getInt("mode")) {
                    case MODE_CREATE:
                        initIntentValueCreate(bundle);
                        break;
                    case MODE_EDIT:
                        initIntentValueEdit(bundle);
                        break;
                }
            }
        }
    }

    private void initIntentValueCreate(Bundle bundle) {
        if (bundle.containsKey("parentGroupId")) {
            this.parentGroupId = bundle.getString("parentGroupId");
        }
        Realm realm = Realm.getDefaultInstance();
        Group group = realm.where(Group.class)
                .equalTo("id", this.parentGroupId)
                .findFirst();
        if (group != null) {
            hashTagListAdapter.set(group.tags);
        }
    }

    private void initIntentValueEdit(Bundle bundle) {
        if (bundle.containsKey("parentGroupId")) {
            this.parentGroupId = bundle.getString("parentGroupId");
        }
        if (bundle.containsKey("group_id")) {
            this.group_id = bundle.getString("group_id");
        }
    }

    private void initView() {
        ed_title = (EditText) findViewById(R.id.ed_title);
        actv_tag = (AutoCompleteTextView) findViewById(R.id.actv_tag);
        bt_tag_add = (Button) findViewById(R.id.bt_tag_add);
        rv_tag = (RecyclerView) findViewById(R.id.rv_tag);
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
    }

    private void initObserable() {
        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(
                RxTextView.textChangeEvents(actv_tag)
                        .map(t -> t.text().length() > 0)
                        .subscribe(bt_tag_add::setEnabled)
        );

        compositeDisposable.add(
                RxTextView.textChanges(ed_title)
                        .map(t -> parentGroupId != null)
                        .subscribe(result -> {
                            if (result) {
                                saveAfterCheck();
                            }
                        })
        );
    }

    private void initValue() {
        if (group_id != null && !"".equals(group_id)) {
            modeEdit();
        }
    }

    private void modeEdit() {
        Group group = Realm.getDefaultInstance()
                .where(Group.class)
                .equalTo("id", group_id)
                .findFirst();

        if (group == null) {
            Toast.makeText(this, "Error on Loading", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        this.group = group;

        ed_title.setText(this.group.title);
        hashTagListAdapter.set(this.group.tags);
    }

    private void save(@Nullable String title, @Nullable List<HashTag> tags) {
        Realm realm = Realm.getDefaultInstance();
        if (this.group == null && (this.group_id == null || "".equals(this.group_id))) {
            this.group = createGroup();
            this.group_id = this.group.id;
        } else if (this.group == null) {
            this.group = realm.where(Group.class)
                    .equalTo("id", this.group_id)
                    .findFirst();
        }
        realm.beginTransaction();

        if (title != null) {
            this.group.title = title;
        }
        if (tags != null) {
            this.group.tags.clear();
            for (HashTag hashTag : tags) {
                this.group.tags.add(saveTag(realm, hashTag.tag));
            }
        }
        this.group.last_date = Database.getCurrentDate();
        this.group.isValidated = false;

        realm.commitTransaction();

        saveGroupMemo();
    }

    private void saveGroupMemo() {
        boolean isCretated = false;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        if (this.groupMemo == null) {
            this.groupMemo = realm.where(GroupMemo.class)
                    .equalTo("type_id", this.group_id)
                    .findFirst();
            if (groupMemo == null) {
                this.groupMemo = realm.createObject(GroupMemo.class);
                this.groupMemo.id = Database.createID(GroupMemo.class);
                this.groupMemo.parentGroupId = this.parentGroupId;
                this.groupMemo.type = GroupMemo.TYPE_GROUP;
                this.groupMemo.type_id = this.group_id;
                this.groupMemo.group = this.group;

                isCretated = true;
            }
        }

        this.groupMemo.last_date = Database.getCurrentDate();
        this.groupMemo.isValidated = false;

        realm.commitTransaction();

        if (isCretated) {
            refleshParentGroup(groupMemo);
        }
    }

    private void saveAfterCheck() {
        String title = ed_title.getText().toString();
        List<HashTag> hashTags = hashTagListAdapter.get();
        if (this.group == null) {
            save(title, hashTags);
        } else {
            if (title.equals(this.group.title)) {
                title = null;
            }
            if (hashTags.size() == this.group.tags.size()) {
                boolean isChanged = false;
                for (int i = 0; i < this.group.tags.size(); i++) {
                    if (!this.group.tags.get(i).tag.equals(hashTags.get(i).tag)) {
                        isChanged = true;
                    }
                }
                if (!isChanged) {
                    hashTags = null;
                }
            }
            if (title != null || hashTags != null) {
                save(title, hashTags);
            }
        }
    }

    private Group createGroup() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        Group group = realm.createObject(Group.class);
        group.id = Database.createID(Group.class);
        group.parentGroupId = this.parentGroupId;
        group.tags = new RealmList<>();

        realm.commitTransaction();

        return group;
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
        hashTagListAdapter.sort();
    }

    private View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.bt_tag_add:
                addTag();
                break;
        }
    };

    private HashTagListAdapter.Callback hashTagListAdapterCallback = new HashTagListAdapter.Callback() {

        @Override
        public void onItemClicked(int position) {
            hashTagListAdapter.remove(position);
        }

        @Override
        public void onDataChanged() {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
