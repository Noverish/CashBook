package com.noverish.cashbook.database;

import android.content.Context;
import android.util.Log;

import com.noverish.cashbook.other.IconManager;
import com.noverish.cashbook.other.MoneyUsageItem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class CategoryDBManager {

    private IconManager iconManager;
    private Context context;
    private HashMap<Integer, BigCategory> expenseBigCategory, incomeBigCategory;
    private HashMap<Integer, SmallCategory> expenseSmallCategory, incomeSmallCategory;

    private static final String EXPENSE_BIG_CATEGORY_DATABASE = "expense_big";
    private static final String EXPENSE_SMALL_CATEGORY_DATABASE = "expense_small";
    private static final String INCOME_BIG_CATEGORY_DATABASE = "income_big";
    private static final String INCOME_SMALL_CATEGORY_DATABASE = "income_small";

    private static CategoryDBManager categoryDatabase;
    public static CategoryDBManager getCategoryManager(Context context) {
        if(categoryDatabase == null) {
            categoryDatabase = new CategoryDBManager(context);
        }
        return categoryDatabase;
    }

    private final String TAG = getClass().getSimpleName();

    public static final int DEFAULT_CATEOGRY_ID = 0;//308743  1433117

    private CategoryDBManager(Context context) {
        this.context = context;
        iconManager = IconManager.getIconManager(context);

        loadDatabase();
    }


    private void saveDatabase() {
        try {
            FileOutputStream fos = context.openFileOutput(EXPENSE_BIG_CATEGORY_DATABASE, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(expenseBigCategory);
            of.flush();
            of.close();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException - save expenseBigCategory failed");
            e.printStackTrace();
        } catch (IOException io) {
            Log.e(TAG, "IOException - save expenseBigCategory failed");
            io.printStackTrace();
        }


        try {
            FileOutputStream fos = context.openFileOutput(EXPENSE_SMALL_CATEGORY_DATABASE, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(expenseSmallCategory);
            of.flush();
            of.close();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException - save expenseSmallCategory failed");
            e.printStackTrace();
        } catch (IOException io) {
            Log.e(TAG, "IOException - save expenseSmallCategory failed");
            io.printStackTrace();
        }


        try {
            FileOutputStream fos = context.openFileOutput(INCOME_BIG_CATEGORY_DATABASE, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(incomeBigCategory);
            of.flush();
            of.close();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException - save incomeBigCategory failed");
            e.printStackTrace();
        } catch (IOException io) {
            Log.e(TAG, "IOException - save incomeBigCategory failed");
            io.printStackTrace();
        }


        try {
            FileOutputStream fos = context.openFileOutput(INCOME_SMALL_CATEGORY_DATABASE, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(incomeSmallCategory);
            of.flush();
            of.close();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException - save incomeSmallCategory failed");
            e.printStackTrace();
        } catch (IOException io) {
            Log.e(TAG, "IOException - save incomeSmallCategory failed");
            io.printStackTrace();
        }
    }

    private void loadDatabase() {
        try {
            FileInputStream fis;
            fis = context.openFileInput(EXPENSE_BIG_CATEGORY_DATABASE);
            ObjectInputStream oi = new ObjectInputStream(fis);
            expenseBigCategory = (HashMap<Integer, BigCategory>) oi.readObject();
            oi.close();

        } catch (FileNotFoundException ex) {
            Log.e(TAG, "FileNotFoundException - load expenseBigCategory failed");
            ex.printStackTrace();
        } catch (ClassNotFoundException cl) {
            Log.e(TAG, "ClassNotFoundException - load expenseBigCategory failed");
            cl.printStackTrace();
        } catch (IOException io) {
            Log.e(TAG, "IOException - load expenseBigCategory failed");
            io.printStackTrace();
        }

        if(expenseBigCategory == null)
            setDefaultExpenseBigCategory();


        try {
            FileInputStream fis;
            fis = context.openFileInput(EXPENSE_SMALL_CATEGORY_DATABASE);
            ObjectInputStream oi = new ObjectInputStream(fis);
            expenseSmallCategory = (HashMap<Integer, SmallCategory>) oi.readObject();
            oi.close();

        } catch (FileNotFoundException ex) {
            Log.e(TAG, "FileNotFoundException - load expenseSmallCategory failed");
            ex.printStackTrace();
        } catch (ClassNotFoundException cl) {
            Log.e(TAG, "ClassNotFoundException - load expenseSmallCategory failed");
            cl.printStackTrace();
        } catch (IOException io) {
            Log.e(TAG, "IOException - load expenseSmallCategory failed");
            io.printStackTrace();
        }

        if(expenseSmallCategory == null)
            setDefaultExpenseSmallCategory();


        try {
            FileInputStream fis;
            fis = context.openFileInput(INCOME_BIG_CATEGORY_DATABASE);
            ObjectInputStream oi = new ObjectInputStream(fis);
            incomeBigCategory = (HashMap<Integer, BigCategory>) oi.readObject();
            oi.close();

        } catch (FileNotFoundException ex) {
            Log.e(TAG, "FileNotFoundException - load incomeBigCategory failed");
            ex.printStackTrace();
        } catch (ClassNotFoundException cl) {
            Log.e(TAG, "ClassNotFoundException - load incomeBigCategory failed");
            cl.printStackTrace();
        } catch (IOException io) {
            Log.e(TAG, "IOException - load incomeBigCategory failed");
            io.printStackTrace();
        }

        if(incomeBigCategory == null)
            setDefaultIncomeBigCategory();


        try {
            FileInputStream fis;
            fis = context.openFileInput(INCOME_SMALL_CATEGORY_DATABASE);
            ObjectInputStream oi = new ObjectInputStream(fis);
            incomeSmallCategory = (HashMap<Integer, SmallCategory>) oi.readObject();
            oi.close();

        } catch (FileNotFoundException ex) {
            Log.e(TAG, "FileNotFoundException - load incomeSmallCategory failed");
            ex.printStackTrace();
        } catch (ClassNotFoundException cl) {
            Log.e(TAG, "ClassNotFoundException - load incomeSmallCategory failed");
            cl.printStackTrace();
        } catch (IOException io) {
            Log.e(TAG, "IOException - load incomeSmallCategory failed");
            io.printStackTrace();
        }

        if(incomeSmallCategory == null)
            setDefaultIncomeSmallCategory();
    }

    private void resetDatabase() {
        context.deleteFile(EXPENSE_BIG_CATEGORY_DATABASE);
        context.deleteFile(EXPENSE_SMALL_CATEGORY_DATABASE);
        context.deleteFile(INCOME_BIG_CATEGORY_DATABASE);
        context.deleteFile(INCOME_SMALL_CATEGORY_DATABASE);

        setDefaultExpenseBigCategory();
        setDefaultExpenseSmallCategory();
        setDefaultIncomeBigCategory();
        setDefaultIncomeSmallCategory();
    }


    private void setDefaultExpenseBigCategory() {
        expenseBigCategory = new HashMap<>();

        insertBigCategory(MoneyUsageItem.EXPENDITURE, "식비", IconManager.meal);
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "주거/통신", IconManager.housing);
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "생활용품", IconManager.appliance);
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "의복/미용", IconManager.cloth);
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "건강/문화", IconManager.culture);
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "교육/육아", IconManager.study);
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "교통/차량", IconManager.transportation);
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "경조사/회비", IconManager.gathering);
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "세금/이자", IconManager.others);
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "기타", IconManager.others);
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "신용카드", IconManager.others);
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "저축/보험", IconManager.others);
    }

    private void setDefaultExpenseSmallCategory() {
        expenseSmallCategory = new HashMap<>();

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "식비", "주식");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "식비", "부식/간식", IconManager.snack);
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "식비", "커피/음료", IconManager.beverage);
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "식비", "술/유흥", IconManager.drinks);
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "식비", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "주거/통신", "관리비");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "주거/통신", "공과금");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "주거/통신", "이동통신", IconManager.communication);
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "주거/통신", "인터넷", IconManager.communication);
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "주거/통신", "월세");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "주거/통신", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "생활용품", "가구/가전", IconManager.furniture);
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "생활용품", "주방/욕식");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "생활용품", "잡화소모");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "생활용품", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "의복/미용", "의류비");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "의복/미용", "패션잡화");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "의복/미용", "헤어/뷰티", IconManager.haircut);
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "의복/미용", "세탁수선비", IconManager.laundry);
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "의복/미용", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "건강/문화", "운동/레저");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "건강/문화", "문화생활");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "건강/문화", "여행");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "건강/문화", "병원비", IconManager.medical);
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "건강/문화", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "교육/육아", "등록금");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "교육/육아", "학원/교재비");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "교육/육아", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "교통/차량", "대중교통비");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "교통/차량", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "경조사/회비", "경조사비");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "경조사/회비", "모임회비");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "경조사/회비", "데이트");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "경조사/회비", "선물");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "경조사/회비", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "세금/이자", "세금");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "세금/이자", "대출이자");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "세금/이자", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "기타", "기타");
    }

    private void setDefaultIncomeBigCategory() {
        incomeBigCategory = new HashMap<>();

        insertBigCategory(MoneyUsageItem.INCOME, "주수입", null);
        insertBigCategory(MoneyUsageItem.INCOME, "부수입", null);
        insertBigCategory(MoneyUsageItem.INCOME, "기타", null);
    }

    private void setDefaultIncomeSmallCategory() {
        incomeSmallCategory = new HashMap<>();

        insertSmallCategory(MoneyUsageItem.INCOME, "주수입", "급여");
        insertSmallCategory(MoneyUsageItem.INCOME, "주수입", "용돈");
        insertSmallCategory(MoneyUsageItem.INCOME, "주수입", "상여");
        insertSmallCategory(MoneyUsageItem.INCOME, "주수입", "사업소득");

        insertSmallCategory(MoneyUsageItem.INCOME, "부수입", "이자");
        insertSmallCategory(MoneyUsageItem.INCOME, "부수입", "기타");
        insertSmallCategory(MoneyUsageItem.INCOME, "부수입", "빌려준 돈");
    }


    private void insertBigCategory(int classification, String name, String icon) {
        HashMap<Integer, BigCategory> bigCategories = (classification == MoneyUsageItem.EXPENDITURE) ? expenseBigCategory : incomeBigCategory;

        int id;
        try {
            id = Collections.max(bigCategories.keySet()) + 1;
        } catch (NoSuchElementException ex) {
            id = 0;
        }

        bigCategories.put(id, new BigCategory(name, icon));
        saveDatabase();
    }

    public void insertSmallCategory(int classification, String bigCategoryName, String name) {
        insertSmallCategory(classification, getBigCategoryID(classification, bigCategoryName), name, getBigCategoryByName(classification, bigCategoryName).icon);
        saveDatabase();
    }

    public void insertSmallCategory(int classification, String bigCategoryName, String name, String icon) {
        HashMap<Integer, SmallCategory> smallCategories = (classification == MoneyUsageItem.EXPENDITURE) ? expenseSmallCategory : incomeSmallCategory;

        int id;
        try {
            id = Collections.max(smallCategories.keySet()) + 1;
        } catch (NoSuchElementException ex) {
            id = 0;
        }

        smallCategories.put(id, new SmallCategory(getBigCategoryByName(classification, bigCategoryName), name, icon));
        saveDatabase();
    }

    public void insertSmallCategory(int classification, int bigCategoryNameId, String name, String icon) {
        insertSmallCategory(classification, getBigCategoryNameById(classification, bigCategoryNameId), name, icon);
        saveDatabase();
    }


    public BigCategory getBigCategoryById(int classification, int bigCategoryID) {
        HashMap<Integer, BigCategory> bigCategories = (classification == MoneyUsageItem.EXPENDITURE) ? expenseBigCategory : incomeBigCategory;

        return bigCategories.get(bigCategoryID);
    }

    public SmallCategory getSmallCategoryById(int classification, int smallCategoryID) {
        HashMap<Integer, SmallCategory> smallCategories = (classification == MoneyUsageItem.EXPENDITURE) ? expenseSmallCategory : incomeSmallCategory;

        return smallCategories.get(smallCategoryID);
    }

    public BigCategory getBigCategoryByName(int classification, String bigCategoryName) {
        HashMap<Integer, BigCategory> bigCategories = (classification == MoneyUsageItem.EXPENDITURE) ? expenseBigCategory : incomeBigCategory;

        for(BigCategory big : bigCategories.values())
            if(big.name.equals(bigCategoryName))
                return big;

        return null;
    }


    public ArrayList<String> getAllSmallCategory(int classification, int bigCategoryID) {
        HashMap<Integer, BigCategory> bigCategories = (classification == MoneyUsageItem.EXPENDITURE) ? expenseBigCategory : incomeBigCategory;
        HashMap<Integer, SmallCategory> smallCategories = (classification == MoneyUsageItem.EXPENDITURE) ? expenseSmallCategory : incomeSmallCategory;

        ArrayList<String> arrayList = new ArrayList<>();
        for(SmallCategory smallCategory : smallCategories.values())
            if (bigCategories.get(bigCategoryID).name.equals(smallCategory.bigCategory.name))
                arrayList.add(smallCategory.name);


        return arrayList;
    }

    public ArrayList<String> getAllSmallCategory(int classification, String bigCategoryName) {
        return getAllSmallCategory(classification, getBigCategoryID(classification, bigCategoryName));
    }

    public ArrayList<String> getAllBigCategory(int classification) {
        HashMap<Integer, BigCategory> bigCategories = (classification == MoneyUsageItem.EXPENDITURE) ? expenseBigCategory : incomeBigCategory;

        ArrayList<String> arrayList = new ArrayList<>();
        for(BigCategory bigCategory : bigCategories.values())
            arrayList.add(bigCategory.name);

        return arrayList;
    }

    public ArrayList<Integer> getAllBigCategoryId(int classification) {
        HashMap<Integer, BigCategory> bigCategories = (classification == MoneyUsageItem.EXPENDITURE) ? expenseBigCategory : incomeBigCategory;

        return new ArrayList<>(bigCategories.keySet());
    }


    public String getBigCategoryNameById(int classification, int id) {
        HashMap<Integer, BigCategory> bigCategories = (classification == MoneyUsageItem.EXPENDITURE) ? expenseBigCategory : incomeBigCategory;

        return bigCategories.get(id).name;
    }

    public String getSmallCategoryNameById(int classification, int id) {
        HashMap<Integer, SmallCategory> smallCategories = (classification == MoneyUsageItem.EXPENDITURE) ? expenseSmallCategory : incomeSmallCategory;

        return smallCategories.get(id).name;
    }


    public int getBigCategoryID(int classification, String bigCategoryName) {
        HashMap<Integer, BigCategory> bigCategories = (classification == MoneyUsageItem.EXPENDITURE) ? expenseBigCategory : incomeBigCategory;

        for(int id : bigCategories.keySet()) {
            if(bigCategories.get(id).name.equals(bigCategoryName))
                return id;
        }

        return -1;
    }

    public int getSmallCategoryID(int classification, String bigCategoryName, String name) {
        HashMap<Integer, BigCategory> bigCategories = (classification == MoneyUsageItem.EXPENDITURE) ? expenseBigCategory : incomeBigCategory;
        HashMap<Integer, SmallCategory> smallCategories = (classification == MoneyUsageItem.EXPENDITURE) ? expenseSmallCategory : incomeSmallCategory;

        for(int id : smallCategories.keySet()) {
            SmallCategory small = smallCategories.get(id);
            if(small.bigCategory.name.equals(bigCategoryName) && small.name.equals(name))
                return id;
        }

        return -1;
    }

    public int getSmallCategoryID(int classification, int bigCategoryId, String name) {
        return getSmallCategoryID(classification, getBigCategoryNameById(classification, bigCategoryId), name);
    }

    public String[] getCategoryNameSetFromID(int classification, int id) {
        HashMap<Integer, SmallCategory> smallCategories = (classification == MoneyUsageItem.EXPENDITURE) ? expenseSmallCategory : incomeSmallCategory;
        SmallCategory item = smallCategories.get(id);

        return new String[]{item.bigCategory.name, item.name};
    }


    public int getBigCategoryIdFromSmallCategoryId(int classification, int smallCategoryId) {
        HashMap<Integer, SmallCategory> smallCategories = (classification == MoneyUsageItem.EXPENDITURE) ? expenseSmallCategory : incomeSmallCategory;

        return getBigCategoryID(classification, smallCategories.get(smallCategoryId).bigCategory.name);
    }

    private void printAllDatabase() {
        for(int id : expenseBigCategory.keySet())
            Log.e(TAG,"id : " + id + " - " + expenseBigCategory.get(id));

        for(int id : expenseSmallCategory.keySet())
            Log.e(TAG,"id : " + id + " - " + expenseSmallCategory.get(id));

        for(int id : incomeBigCategory.keySet())
            Log.e(TAG,"id : " + id + " - " + incomeBigCategory.get(id));

        for(int id : incomeSmallCategory.keySet())
            Log.e(TAG,"id : " + id + " - " + incomeSmallCategory.get(id));
    }


    public static class BigCategory implements Serializable {
        public String icon;
        public String name;
        public int frequency;

        private BigCategory(String name, String icon) {
            this.name = name;
            this.icon = icon;
            frequency = 0;
        }

        public void increaseFrequency() {
            frequency++;
        }

        @Override
        public String toString() {
            return "BigCategory{" +
                    "icon='" + icon + '\'' +
                    ", name='" + name + '\'' +
                    ", frequency=" + frequency +
                    '}';
        }
    }

    public static class SmallCategory implements Serializable{
        public String icon;
        public BigCategory bigCategory;
        public String name;
        public int frequency;

        private SmallCategory(BigCategory bigCategory, String name, String icon) {
            this.icon = icon;
            this.bigCategory = bigCategory;
            this.name = name;
            this.frequency = 0;
        }

        public void increaseFrequency() {
            frequency++;
        }

        @Override
        public String toString() {
            return "SmallCategory{" +
                    "icon='" + icon + '\'' +
                    ", bigCategory=" + bigCategory +
                    ", name='" + name + '\'' +
                    ", frequency=" + frequency +
                    '}';
        }
    }


    private void comment() {
        /*
    private CategoryDBManager(Context context) {
        //context.deleteDatabase(DATABASE_NAME);

        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

        database.execSQL("create table if not exists " + EXPENSE_BIG_CATEGORY_TABLE + "(" +
                " _id integer PRIMARY KEY autoincrement, " +
                "name text, " +
                "frequency integer);");

        database.execSQL("create table if not exists " + INCOME_BIG_CATEGORY_TABLE + "(" +
                " _id integer PRIMARY KEY autoincrement, " +
                "name text, " +
                "frequency integer);");

        database.execSQL("create table if not exists " + EXPENSE_SMALL_CATEGORY_TABLE + "(" +
                " _id integer PRIMARY KEY autoincrement, " +
                "big_category_index integer, " +
                "name text, " +
                "frequency integer);");

        database.execSQL("create table if not exists " + INCOME_SMALL_CATEGORY_TABLE + "(" +
                " _id integer PRIMARY KEY autoincrement, " +
                "big_category_index integer, " +
                "name text, " +
                "frequency integer);");



        //setDefaultCategory();

        //printAllDatabase();
    }

    */






    /*
    private void setDefaultCategory() {
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "식비");
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "주거/통신");
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "생활용품");
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "의복/미용");
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "건강/문화");
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "교육/육아");
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "교통/차량");
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "경조사/회비");
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "세금/이자");
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "기타");
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "신용카드");
        insertBigCategory(MoneyUsageItem.EXPENDITURE, "저축/보험");

        insertBigCategory(MoneyUsageItem.INCOME, "주수입");
        insertBigCategory(MoneyUsageItem.INCOME, "부수입");
        insertBigCategory(MoneyUsageItem.INCOME, "기타");


        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "식비", "주식");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "식비", "부식/간식");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "식비", "커피/음료");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "식비", "술/유흥");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "식비", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "주거/통신", "관리비");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "주거/통신", "공과금");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "주거/통신", "이동통신");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "주거/통신", "인터넷");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "주거/통신", "월세");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "주거/통신", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "생활용품", "가구/가전");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "생활용품", "주방/욕식");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "생활용품", "잡화소모");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "생활용품", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "의복/미용", "의류비");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "의복/미용", "패션잡화");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "의복/미용", "헤어/뷰티");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "의복/미용", "세탁수선비");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "의복/미용", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "건강/문화", "운동/레저");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "건강/문화", "문화생활");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "건강/문화", "여행");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "건강/문화", "병원비");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "건강/문화", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "교육/육아", "등록금");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "교육/육아", "학원/교재비");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "교육/육아", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "교통/차량", "대중교통비");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "교통/차량", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "경조사/회비", "경조사비");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "경조사/회비", "모임회비");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "경조사/회비", "데이트");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "경조사/회비", "선물");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "경조사/회비", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "세금/이자", "세금");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "세금/이자", "대출이자");
        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "세금/이자", "기타");

        insertSmallCategory(MoneyUsageItem.EXPENDITURE, "기타", "기타");

        //================================================

        insertSmallCategory(MoneyUsageItem.INCOME, "주수입", "급여");
        insertSmallCategory(MoneyUsageItem.INCOME, "주수입", "용돈");
        insertSmallCategory(MoneyUsageItem.INCOME, "주수입", "상여");
        insertSmallCategory(MoneyUsageItem.INCOME, "주수입", "사업소득");

        insertSmallCategory(MoneyUsageItem.INCOME, "부수입", "이자");
        insertSmallCategory(MoneyUsageItem.INCOME, "부수입", "기타");
        insertSmallCategory(MoneyUsageItem.INCOME, "부수입", "빌려준 돈");
    }


    public int getIDFromSmallCategoryName(int classification, String bicCategoryName, String smallCategoryName) {
        String bigTableName = "";
        String smallTableName = "";

        if(classification == MoneyUsageItem.EXPENDITURE) {
            bigTableName = EXPENSE_BIG_CATEGORY_TABLE;
            smallTableName = EXPENSE_SMALL_CATEGORY_TABLE;
        } else if(classification == MoneyUsageItem.INCOME) {
            bigTableName = INCOME_BIG_CATEGORY_TABLE;
            smallTableName = INCOME_SMALL_CATEGORY_TABLE;
        }

        String query = "SELECT * FROM " + bigTableName + " WHERE name = '" + bicCategoryName + "'";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToNext();
        int bigCategoryID = cursor.getInt(0);

        query = "SELECT * FROM " + smallTableName + " WHERE name = '" + smallCategoryName + "'" + " AND big_category_index = " + bigCategoryID;
        cursor = database.rawQuery(query, null);

        cursor.moveToNext();
        int ans = cursor.getInt(0);
        cursor.close();

        return ans;
    }

    public String[] getCategoryNameSetFromID(int classification, int id) {
        String[] set = new String[2];

        String tableName = "";

        if(classification == MoneyUsageItem.EXPENDITURE) {
            tableName = EXPENSE_SMALL_CATEGORY_TABLE;
        } else if(classification == MoneyUsageItem.INCOME) {
            tableName = INCOME_SMALL_CATEGORY_TABLE;
        } else {
            String[] ans = {"",""};
            return ans;
        }


        String query = "SELECT * FROM " + tableName + " WHERE _id = " + id;
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToNext();
        cursor.getInt(0);

        set[0] = getBigCategoryNameById(classification, cursor.getInt(1));
        set[1] = cursor.getString(2);



        cursor.close();

        return set;
    }



    private int insertBigCategory(int classification, String name) {
        String tableName = null;

        if(classification == MoneyUsageItem.EXPENDITURE) {
            tableName = EXPENSE_BIG_CATEGORY_TABLE;
        } else if(classification == MoneyUsageItem.INCOME) {
            tableName = INCOME_BIG_CATEGORY_TABLE;
        }

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("frequency", 0);

        return (int) database.insert(tableName, null, values);
    }

    public int insertSmallCategory(int classification, String bigCategory, String name) {
        String tableName = null;

        if(classification == MoneyUsageItem.EXPENDITURE) {
            tableName = EXPENSE_BIG_CATEGORY_TABLE;
        } else if(classification == MoneyUsageItem.INCOME) {
            tableName = INCOME_BIG_CATEGORY_TABLE;
        }

        String query = "SELECT * FROM " + tableName + " WHERE name = '" + bigCategory + "'";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToNext();

        int index = cursor.getInt(0);

        cursor.close();

        return insertSmallCategory(classification, index, name);
    }

    public int insertSmallCategory(int classification, int bigCategoryNameId, String name) {
        String tableName = null;

        if(classification == MoneyUsageItem.EXPENDITURE) {
            tableName = EXPENSE_SMALL_CATEGORY_TABLE;
        } else if(classification == MoneyUsageItem.INCOME) {
            tableName = INCOME_SMALL_CATEGORY_TABLE;
        }

        ContentValues recordValues = new ContentValues();
        recordValues.put("big_category_index", bigCategoryNameId);
        recordValues.put("name", name);
        recordValues.put("frequency", 0);

        return (int) database.insert(tableName, null, recordValues);
    }


    public ArrayList<String> getAllSmallCategory(int classification, int bigCategoryID) {
        ArrayList<String> ans = new ArrayList<>();
        String tableName;

        if(classification == MoneyUsageItem.EXPENDITURE) {
            tableName = EXPENSE_SMALL_CATEGORY_TABLE;
        } else if(classification == MoneyUsageItem.INCOME) {
            tableName = INCOME_SMALL_CATEGORY_TABLE;
        } else {
            Log.e(TAG,"Wrong getSmallCategoryById although classification is Transfer");
            return ans;
        }

        String query = "SELECT * FROM " + tableName + " WHERE big_category_index = " + bigCategoryID;
        Cursor cursor = database.rawQuery(query, null);

        int cnt = cursor.getCount();

        for(int i = 0;i<cnt;i++) {
            cursor.moveToNext();

            ans.add(cursor.getString(2));
        }
        cursor.close();

        return ans;
    }

    public ArrayList<String> getAllSmallCategory(int classification, String bigCategoryName) {
        int bigCategoryID = getBigCategoryID(classification, bigCategoryName);
        return getAllSmallCategory(classification, bigCategoryID);
    }

    public ArrayList<String> getAllBigCategory(int classification) {
        ArrayList<String> ans = new ArrayList<>();
        String tableName;

        if(classification == MoneyUsageItem.EXPENDITURE) {
            tableName = EXPENSE_BIG_CATEGORY_TABLE;
        } else if(classification == MoneyUsageItem.INCOME) {
            tableName = INCOME_BIG_CATEGORY_TABLE;
        } else {
            Log.e(TAG,"Wrong getBigCategoryById although classification is Transfer");
            return ans;
        }

        String query = "SELECT * FROM " + tableName;
        Cursor cursor = database.rawQuery(query, null);

        int cnt = cursor.getCount();

        for(int i = 0;i<cnt;i++) {
            cursor.moveToNext();

            ans.add(cursor.getString(1));
        }

        cursor.close();

        return ans;
    }


    private String getBigCategoryNameById(int classification, int id) {
        String tableName = "";

        if(classification == MoneyUsageItem.EXPENDITURE) {
            tableName = EXPENSE_BIG_CATEGORY_TABLE;
        } else if(classification == MoneyUsageItem.INCOME) {
            tableName = INCOME_BIG_CATEGORY_TABLE;
        }

        String query = "SELECT * FROM " + tableName + " WHERE _id = " + id;
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToNext();

        cursor.getInt(0);
        String ans = cursor.getString(1);

        cursor.close();

        return ans;
    }

    public String getSmallCategoryNameById(int classification, int id) {
        String tableName = "";

        if(classification == MoneyUsageItem.EXPENDITURE) {
            tableName = EXPENSE_SMALL_CATEGORY_TABLE;
        } else if(classification == MoneyUsageItem.INCOME) {
            tableName = INCOME_SMALL_CATEGORY_TABLE;
        }

        String query = "SELECT * FROM " + tableName + " WHERE _id = " + id;
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToNext();

        cursor.getInt(0);
        String ans = cursor.getString(1);

        cursor.close();

        return ans;
    }


    public int getBigCategoryID(int classification, String name) {
        String tableName = "";

        if(classification == MoneyUsageItem.EXPENDITURE) {
            tableName = EXPENSE_BIG_CATEGORY_TABLE;
        } else if(classification == MoneyUsageItem.INCOME) {
            tableName = INCOME_BIG_CATEGORY_TABLE;
        }

        String query = "SELECT * FROM " + tableName + " WHERE name = '" + name + "'";
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToNext();

        int ans = cursor.getInt(0);

        cursor.close();

        return ans;
    }

    public int getSmallCategoryID(int classification, String name) {
        String tableName = "";

        if(classification == MoneyUsageItem.EXPENDITURE) {
            tableName = EXPENSE_SMALL_CATEGORY_TABLE;
        } else if(classification == MoneyUsageItem.INCOME) {
            tableName = INCOME_SMALL_CATEGORY_TABLE;
        }

        String query = "SELECT * FROM " + tableName + " WHERE name = '" + name + "'";
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToNext();

        int ans = cursor.getInt(0);

        cursor.close();

        return ans;
    }

    public int printAllDatabase() {
        String countQuery = "SELECT * FROM " + EXPENSE_BIG_CATEGORY_TABLE;
        Cursor cursor = database.rawQuery(countQuery, null);
        int cnt = cursor.getCount();

        for(int i = 0;i < cnt;i++) {
            cursor.moveToNext();

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int frequency = cursor.getInt(2);

            Log.e(TAG, "ID : " + id + " name : " + name + " frequency : " + frequency);
        }

        cursor.close();


        countQuery = "SELECT * FROM " + INCOME_BIG_CATEGORY_TABLE;
        cursor = database.rawQuery(countQuery, null);
        cnt = cursor.getCount();

        for(int i = 0;i < cnt;i++) {
            cursor.moveToNext();

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int frequency = cursor.getInt(2);

            Log.e(TAG,  "ID : " + id + " name : " + name + " frequency" + frequency);
        }

        cursor.close();


        countQuery = "SELECT * FROM " + EXPENSE_SMALL_CATEGORY_TABLE;
        cursor = database.rawQuery(countQuery, null);
        cnt = cursor.getCount();

        for(int i = 0;i < cnt;i++) {
            cursor.moveToNext();

            int id = cursor.getInt(0);
            int bigId = cursor.getInt(1);
            String name = cursor.getString(2);
            int frequency = cursor.getInt(3);

            Log.e(TAG, id + " " + bigId + " " + name + " " + frequency);
        }

        cursor.close();


        countQuery = "SELECT * FROM " + INCOME_SMALL_CATEGORY_TABLE;
        cursor = database.rawQuery(countQuery, null);
        cnt = cursor.getCount();

        for(int i = 0;i < cnt;i++) {
            cursor.moveToNext();

            int id = cursor.getInt(0);
            int bigId = cursor.getInt(1);
            String name = cursor.getString(2);
            int frequency = cursor.getInt(3);

            Log.e(TAG, id + " " + bigId + " " + name + " " + frequency);
        }

        cursor.close();
        return cnt;
    }*/

    }
}
