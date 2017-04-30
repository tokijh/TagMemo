package com.timejh.tagmemo_java.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by tokijh on 2017. 4. 27..
 */

public class HashTag extends RealmObject {
    @PrimaryKey
    public String id;

    public String tag;

    public String last_date;
    public boolean isValidated;
}
