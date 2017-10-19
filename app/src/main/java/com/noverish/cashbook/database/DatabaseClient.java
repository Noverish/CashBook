package com.noverish.cashbook.database;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Noverish on 2017-10-19.
 */

public class DatabaseClient {
    public static void insert(RealmObject item) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(item);
        realm.commitTransaction();
    }

    public static <T extends RealmObject> long getPrimaryKey(Class<? extends T> t) {
        Realm realm = Realm.getDefaultInstance();
        Number maxid = realm.where(t).max("id");
        return (maxid == null) ? 1 : ((long) maxid + 1);
    }

    public static <T extends RealmObject> ArrayList<T> getAll(Class<? extends T> t) {
        Realm realm = Realm.getDefaultInstance();
        return new ArrayList<>(realm.where(t).findAll());
    }


}
