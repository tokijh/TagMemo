package com.timejh.tagmemo_java.group;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.WordsLayoutManager;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.timejh.tagmemo_java.R;
import com.timejh.tagmemo_java.adapter.HasgTagListAdapter;
import com.timejh.tagmemo_java.model.HashTag;

public class GroupManageActivity extends AppCompatActivity implements HasgTagListAdapter.Callback {

    private EditText ed_title;
    private AutoCompleteTextView actv_tag;
    private Button bt_tag_add;
    private RecyclerView rv_tag;

    private HasgTagListAdapter hasgTagListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manage);

        initView();

        initAdapter();

        initManager();

        initListener();
    }

    private void initView() {
        ed_title = (EditText) findViewById(R.id.ed_title);
        actv_tag = (AutoCompleteTextView) findViewById(R.id.actv_tag);
        bt_tag_add = (Button) findViewById(R.id.bt_tag_add);
        rv_tag = (RecyclerView) findViewById(R.id.rv_tag);
    }

    private void initAdapter() {
        hasgTagListAdapter = new HasgTagListAdapter(this, this);
        rv_tag.setAdapter(hasgTagListAdapter);
    }

    private void initManager() {
        rv_tag.setLayoutManager(new WordsLayoutManager(this));
    }

    private void initListener() {
        bt_tag_add.setOnClickListener(onClickListener);
    }

    private void addTag() {
        HashTag hashTag = new HashTag();
        hashTag.tag = actv_tag.getText().toString();
        hasgTagListAdapter.add(hashTag);
    }

    private View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.bt_tag_add:
                addTag();
                break;
        }
    };

    @Override
    public void onItemClicked(int position) {

    }
}
