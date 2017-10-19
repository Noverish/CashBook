package com.noverish.cashbook.database;

import android.support.annotation.NonNull;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

/**
 * Created by Noverish on 2017-10-20.
 */

public class DatabaseMigration implements RealmMigration {
    @Override
    public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {

    }
}
