package com.timejh.tagmemo_java.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by tokijh on 2017. 4. 30..
 */

public class GroupMemo extends RealmObject {

    public static final int TYPE_GROUP = 0;
    public static final int TYPE_MEMO = 1;

    @PrimaryKey
    public String id;

    public int type;
    public String parentId;
    public Group group;
    public Memo memo;
    public long position;

    public String last_date;
    public boolean isValidated;
}
