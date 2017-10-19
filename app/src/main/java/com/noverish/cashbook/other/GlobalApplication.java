package com.noverish.cashbook.other;

import android.app.Application;

import com.noverish.cashbook.database.DatabaseMigration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Noverish on 2017-10-19.
 */

public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration(new DatabaseMigration())
                .build();

        Realm.setDefaultConfiguration(config);
    }
}
