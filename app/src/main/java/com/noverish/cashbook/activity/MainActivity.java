package com.noverish.cashbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.noverish.cashbook.R;
import com.noverish.cashbook.account.AccountManageActivity;
import com.noverish.cashbook.database.AccountDBManager;
import com.noverish.cashbook.database.CashBookDBManager;
import com.noverish.cashbook.database.CategoryDBManager;
import com.noverish.cashbook.database.ContentToCategoryDatabase;
import com.noverish.cashbook.other.MoneyUsageItem;
import com.noverish.cashbook.other.NotificationsReadService;
import com.noverish.cashbook.view.CurrentPropertyView;
import com.noverish.cashbook.view.MoneyUsageListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private CashBookDBManager cashBookManager;

    public static final int REQUEST_CODE_MONEY_USAGE_ADD = 1001;
    public static final int REQUEST_CODE_MONEY_USAGE_MODIFY = 1002;
    public static final int RESULT_CODE_CASHBOOK_DB_MODIFIED = 1003;
    public static final int RESULT_CODE_ACCOUNT_DB_MODIFIED = 1004;
    public static final int NULL = 0;

    private CurrentPropertyView currentPropertyView;
    private MoneyUsageListView moneyUsageListView;

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        init();

        AccountDBManager.getAccountManager(this);
        CashBookDBManager.getCashBookDBManager(this);
        CategoryDBManager.getCategoryManager(this);
        ContentToCategoryDatabase.getContentToCategoryDatabase(this);

        currentPropertyView = (CurrentPropertyView) findViewById(R.id.main_activity_current_property);
        moneyUsageListView = (MoneyUsageListView) findViewById(R.id.main_activity_money_usage_list);

        /*Intent intent = new Intent(this, NotificationsReadService.class);
        startService(intent);*/

        NotificationsReadService.checkNotificationAccess(this);
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(getApplicationContext(), CashBookAddActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MONEY_USAGE_ADD);
            }
        });

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.account_manager_button) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }*/
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivityForResult(intent, NULL);
        } else if(id == R.id.main_setting_account_manage) {
            Intent intent = new Intent(getApplicationContext(), AccountManageActivity.class);
            startActivityForResult(intent, NULL);
        } else if(id == R.id.main_setting_card_manage) {
            Intent intent = new Intent(getApplicationContext(), CardManageActivity.class);
            startActivityForResult(intent, NULL);
        } else if(id == R.id.main_setting_category_manage) {

        } else if(id == R.id.main_setting_notification_manage) {
            Intent intent = new Intent(getApplicationContext(), NotificationListenerActivity.class);
            startActivityForResult(intent, NULL);
        } else if(id == R.id.main_setting_current_property_manage) {

        } else if(id == R.id.main_setting_message_manage) {
            Intent intent = new Intent(getApplicationContext(), ExtractSMSActivity.class);
            startActivityForResult(intent, NULL);
        }

        return super.onOptionsItemSelected(item);
    }



    private void asdf() {
        String data = "";
        InputStream inputStream = getResources().openRawResource(R.raw.text);

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
                total.append("\n");
            }

            data = total.toString();

        } catch (IOException ex) {

        }

        String[] tmps = data.split("\n");

        for(String tmp : tmps) {

            String[] items = tmp.split("\t");

            Calendar calendar = Calendar.getInstance();

            String[] date = items[0].split("-");
            String[] time = items[1].split(":");
            calendar.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]), Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));

            long dateLong = calendar.getTimeInMillis();
            int classification = Integer.parseInt(items[2]);
            String content = items[3];
            int amount = Integer.parseInt(items[4]);
            int account = Integer.parseInt(items[5]);
            int category = Integer.parseInt(items[6]);

            MoneyUsageItem moneyUsageItem = new MoneyUsageItem(dateLong, classification, amount, content, content, account, category, "");

            Log.e(TAG,moneyUsageItem + "");

            //cashBookManager.insert(moneyUsageItem, false);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        Log.e(TAG,"onActivityResult result code - " + resultCode);

        if(resultCode == RESULT_CODE_CASHBOOK_DB_MODIFIED) {
            moneyUsageListView.refresh();
        }
        currentPropertyView.refresh();
    }
}


