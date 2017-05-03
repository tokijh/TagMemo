package com.timejh.tagmemo_java.memo;

import android.support.annotation.Nullable;
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
import com.timejh.tagmemo_java.model.Memo;
import com.timejh.tagmemo_java.util.Database;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;
import io.realm.RealmList;

public class MemoManageActivity extends AppCompatActivity {

    private String parentGroupId = null;
    private String memo_id = null;
    private Memo memo = null;
    private GroupMemo groupMemo = null;

    private EditText ed_title, ed_content;
    private AutoCompleteTextView actv_tag;
    private LinearLayout layout_tag_add;
    private Button bt_tag_add;
    private RecyclerView rv_tag;

    private FloatingActionMenu fab;

    private FloatingActionButton fab_delete, fab_camera, fab_gallery;

    private HashTagListAdapter hashTagListAdapter;

    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_manage);

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
            if (bundle.containsKey("parentGroupId")) {
                this.parentGroupId = bundle.getString("parentGroupId");
            }
            if (bundle.containsKey("memo_id")) {
                this.memo_id = bundle.getString("memo_id");
            }
        }
    }

    private void initView() {
        ed_title = (EditText) findViewById(R.id.ed_title);
        ed_content = (EditText) findViewById(R.id.ed_content);
        actv_tag = (AutoCompleteTextView) findViewById(R.id.actv_tag);
        layout_tag_add = (LinearLayout) findViewById(R.id.layout_tag_add);
        bt_tag_add = (Button) findViewById(R.id.bt_tag_add);
        rv_tag = (RecyclerView) findViewById(R.id.rv_tag);

        fab = (FloatingActionMenu) findViewById(R.id.fab);

        fab_delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        fab_camera = (FloatingActionButton) findViewById(R.id.fab_camera);
        fab_gallery = (FloatingActionButton) findViewById(R.id.fab_gallery);
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
        fab_delete.setOnClickListener(onClickListener);
        fab_camera.setOnClickListener(onClickListener);
        fab_gallery.setOnClickListener(onClickListener);
    }

    private void initObserable() {
        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(
                RxTextView.textChanges(actv_tag)
                        .map(t -> t.length() > 0)
                        .subscribe(bt_tag_add::setEnabled)
        );

        compositeDisposable.add(
                Observable.combineLatest(
                        RxTextView.textChanges(ed_title).map(t -> true),
                        RxTextView.textChanges(ed_content).map(t -> true),
                        (title, content) -> parentGroupId != null && (title || content))
                        .subscribe(result -> {
                            if (result) {
                                saveAfterCheck();
                            }
                        })
        );
    }

    private void initValue() {
        if (memo_id != null && !"".equals(memo_id)) {
            modeEdit();
        }
    }

    private void modeEdit() {
        Memo memo = Realm.getDefaultInstance()
                .where(Memo.class)
                .equalTo("id", memo_id)
                .findFirst();

        if (memo == null) {
            Toast.makeText(this, "Error on Loading", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        this.memo = memo;

        ed_title.setText(this.memo.title);
        ed_content.setText(this.memo.content);
        hashTagListAdapter.set(this.memo.tags);
    }

    private void save(@Nullable String title, @Nullable String content, @Nullable List<HashTag> tags) {
        Realm realm = Realm.getDefaultInstance();
        if (this.memo == null && (this.memo_id == null || "".equals(this.memo_id))) {
            this.memo = createMemo();
            this.memo_id = this.memo.id;
        } else if (this.memo == null) {
            this.memo = realm.where(Memo.class)
                    .equalTo("id", this.memo_id)
                    .findFirst();
        }
        realm.beginTransaction();

        if (title != null) {
            this.memo.title = title;
        }
        if (content != null) {
            this.memo.content = content;
        }
        if (tags != null) {
            this.memo.tags.clear();
            for (HashTag hashTag : tags) {
                this.memo.tags.add(saveTag(realm, hashTag.tag));
            }
        }
        this.memo.last_date = Database.getCurrentDate();
        this.memo.isValidated = false;

        realm.commitTransaction();

        saveGroupMemo();
    }

    private void saveGroupMemo() {
        boolean isCretated = false;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        if (this.groupMemo == null) {
            this.groupMemo = realm.where(GroupMemo.class)
                    .equalTo("type_id", this.memo_id)
                    .findFirst();
            if (groupMemo == null) {
                this.groupMemo = realm.createObject(GroupMemo.class);
                this.groupMemo.id = Database.createID(GroupMemo.class);
                this.groupMemo.parentGroupId = this.parentGroupId;
                this.groupMemo.type = GroupMemo.TYPE_MEMO;
                this.groupMemo.type_id = this.memo_id;
                this.groupMemo.memo = this.memo;

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
        String content = ed_content.getText().toString();
        List<HashTag> hashTags = hashTagListAdapter.get();
        if (this.memo == null) {
            save(title, content, hashTags);
        } else {
            if (title.equals(this.memo.title)) {
                title = null;
            }
            if (content.equals(this.memo.content)) {
                content = null;
            }
            if (hashTags.size() == this.memo.tags.size()) {
                boolean isChanged = false;
                for (int i = 0; i < this.memo.tags.size(); i++) {
                    if (!this.memo.tags.get(i).tag.equals(hashTags.get(i).tag)) {
                        isChanged = true;
                    }
                }
                if (!isChanged) {
                    hashTags = null;
                }
            }
            if (title != null || content != null || hashTags != null) {
                save(title, content, hashTags);
            }
        }
    }

    private Memo createMemo() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        Memo memo = realm.createObject(Memo.class);
        memo.id = Database.createID(Memo.class);
        memo.parentGroupId = this.parentGroupId;
        memo.tags = new RealmList<>();

        realm.commitTransaction();

        return memo;
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
                saveAfterCheck();
                break;
        }
    };

    private HashTagListAdapter.Callback hashTagListAdapterCallback = position -> {

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
