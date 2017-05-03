package com.timejh.tagmemo_java.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by tokijh on 2017. 4. 27..
 */

public class Group extends RealmObject {

    public String id;

    public String parentGroupId;
    public String title;
    public RealmList<HashTag> tags;
    public RealmList<GroupMemo> groupMemos;

    public String last_date;
    public boolean isValidated;
}
