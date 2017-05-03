package com.timejh.tagmemo_java.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by tokijh on 2017. 4. 30..
 */

public class Memo extends RealmObject {

    public String id;

    public String parentId;
    public String title;
    public String content;
    public RealmList<HashTag> tags;

    public String last_date;
    public boolean isValidated;
}
