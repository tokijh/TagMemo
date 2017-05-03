package com.timejh.tagmemo_java.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by tokijh on 2017. 4. 30..
 */

public class GroupMemo extends RealmObject {

    public static final int TYPE_GROUP = 0;
    public static final int TYPE_MEMO = 1;

    public String id;

    public int type;
    public String parentGroupId;
    public String type_id; // TYPE에 맞는 ID(Group 또는 Memo)
    public Group group;
    public Memo memo;

    public String last_date;
    public boolean isValidated;
}
