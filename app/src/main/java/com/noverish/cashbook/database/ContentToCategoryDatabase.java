package com.noverish.cashbook.database;

import android.content.Context;
import android.util.Log;

import com.noverish.cashbook.other.MoneyUsageItem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Noverish on 2016-04-28.
 */
public class ContentToCategoryDatabase {
    private Context context;
    private HashMap<String, Integer> database;

    private static final String DATABASE_FILE_NAME = "database";

    private final String TAG = getClass().getSimpleName();

    private static ContentToCategoryDatabase contentToCategoryDatabase;
    public static ContentToCategoryDatabase getContentToCategoryDatabase(Context context) {
        if(contentToCategoryDatabase == null)
            contentToCategoryDatabase = new ContentToCategoryDatabase(context);
        return contentToCategoryDatabase;
    }

    private ContentToCategoryDatabase(Context context) {
        this.context = context;

        database = readFromInternalStorage();

        if(database == null) {
            saveToInternalStorage(new HashMap<String, Integer>());
            database = readFromInternalStorage();
        }
    }


    private void saveToInternalStorage(HashMap<String, Integer> map) {
        context.deleteFile(DATABASE_FILE_NAME);
        try {
            FileOutputStream fos = context.openFileOutput(DATABASE_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(map);
            of.flush();
            of.close();
            fos.close();
        }
        catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }
    }

    private HashMap<String, Integer> readFromInternalStorage() {
        HashMap<String, Integer> toReturn = null;

        try {
            FileInputStream fis;
            fis = context.openFileInput(DATABASE_FILE_NAME);
            ObjectInputStream oi = new ObjectInputStream(fis);
            toReturn = (HashMap<String, Integer>) oi.readObject();
            oi.close();
        } catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }
        return toReturn;
    }

    public void clearDatabase() {
        context.deleteFile(DATABASE_FILE_NAME);
    }


    public void putContentCategory(String content, int categoryID) {
        database.put(content, categoryID);
        saveToInternalStorage(database);
    }

    public int getCategoryIDFromContent(String content) {
        if(database.containsKey(content))
            return database.get(content);
        else
            return CategoryDBManager.DEFAULT_CATEOGRY_ID;
    }


    public void printAllDatabase() {
        if(database.keySet().size() > 0)
            for(String key : database.keySet())
                Log.e(TAG,"key : " + key + " - value : " + database.get(key));
        else
            Log.e(TAG,"Database is empty");
    }

    public ArrayList<String> getDatabase() {
        ArrayList<String> ans = new ArrayList<>();
        CategoryDBManager categoryDBManager = CategoryDBManager.getCategoryManager(context);

        for(String content : database.keySet()) {
            int categoryId = database.get(content);
            String[] set = categoryDBManager.getCategoryNameSetFromID(MoneyUsageItem.EXPENDITURE, categoryId);
            ans.add(content + " : " + set[0] + "-" + set[1]);
        }

        return ans;
    }

    public void write(String str) {
        try {
            FileOutputStream stream = context.openFileOutput(DATABASE_FILE_NAME, Context.MODE_PRIVATE);
            stream.write(str.getBytes());
            stream.close();
        } catch (IOException io) {
            Log.e(TAG, "IOException occurred");
        }
    }

    public String read() {
        String tmp = null;

        try {
            FileInputStream stream = context.openFileInput(DATABASE_FILE_NAME);

            int c;
            tmp = "";
            while( (c = stream.read()) != -1){
                tmp = tmp + Character.toString((char)c);
            }

            //string temp contains all the data of the file.
            stream.close();
        } catch (IOException id) {
            Log.e(TAG, "IOException occurred");
        }

        return tmp;
    }

}
