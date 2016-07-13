package com.noverish.cashbook.z_deprecated;

/**
 * Created by Noverish on 2016-05-04.
 */
public class CustomDatabase {
/*
    private Context context;
    private ArrayList<BigCategory> expenseBigCategory, incomeBigCategory;
    private ArrayList<SmallCategory> expenseSmallCategory, incomeSmallCategory;

    private static final String EXPENSE_BIG_CATEGORY_DATABASE = "expense_big";
    private static final String EXPENSE_SMALL_CATEGORY_DATABASE = "expense_small";
    private static final String INCOME_BIG_CATEGORY_DATABASE = "income_big";
    private static final String INCOME_SMALL_CATEGORY_DATABASE = "income_small";

    private static CustomDatabase customDatabase;
    public static CustomDatabase getCustomDatabase(Context context) {
        if(customDatabase == null) {
            customDatabase = new CustomDatabase(context);
        }
        return customDatabase;
    }

    private final String TAG = getClass().getSimpleName();

    private CustomDatabase(Context context) {
        this.context = context;

        loadDatabase();

        printAllDatabase();
    }

    private void saveDatabase() {
        context.deleteFile(EXPENSE_BIG_CATEGORY_DATABASE);
        try {
            FileOutputStream fos = context.openFileOutput(EXPENSE_BIG_CATEGORY_DATABASE, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(expenseBigCategory);
            of.flush();
            of.close();
            fos.close();
        }
        catch (Exception e) {
            Log.e(TAG, "save expenseBigCategory failed");
        }

        context.deleteFile(EXPENSE_SMALL_CATEGORY_DATABASE);
        try {
            FileOutputStream fos = context.openFileOutput(EXPENSE_SMALL_CATEGORY_DATABASE, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(expenseSmallCategory);
            of.flush();
            of.close();
            fos.close();
        }
        catch (Exception e) {
            Log.e(TAG, "save expenseSmallCategory failed");
        }

        context.deleteFile(INCOME_BIG_CATEGORY_DATABASE);
        try {
            FileOutputStream fos = context.openFileOutput(INCOME_BIG_CATEGORY_DATABASE, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(incomeBigCategory);
            of.flush();
            of.close();
            fos.close();
        }
        catch (Exception e) {
            Log.e(TAG, "save incomeBigCategory failed");
            e.printStackTrace();
        }

        context.deleteFile(INCOME_SMALL_CATEGORY_DATABASE);
        try {
            FileOutputStream fos = context.openFileOutput(INCOME_SMALL_CATEGORY_DATABASE, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(incomeSmallCategory);
            of.flush();
            of.close();
            fos.close();
        }
        catch (Exception e) {
            Log.e(TAG, "save incomeSmallCategory failed");
        }
    }

    private void loadDatabase() {
        try {
            FileInputStream fis;
            fis = context.openFileInput(EXPENSE_BIG_CATEGORY_DATABASE);
            ObjectInputStream oi = new ObjectInputStream(fis);
            expenseBigCategory = (ArrayList<BigCategory>) oi.readObject();
            oi.close();

        } catch (Exception e) {
            Log.e(TAG, "load expenseBigCategory failed");
        }

        if(expenseBigCategory == null)
            setDefaultExpenseBigCategory();


        try {
            FileInputStream fis;
            fis = context.openFileInput(EXPENSE_SMALL_CATEGORY_DATABASE);
            ObjectInputStream oi = new ObjectInputStream(fis);
            expenseSmallCategory = (ArrayList<SmallCategory>) oi.readObject();
            oi.close();

        } catch (Exception e) {
            Log.e(TAG, "load expenseSmallCategory failed");
        }

        if(expenseSmallCategory == null)
            setDefaultExpenseSmallCategory();


        try {
            FileInputStream fis;
            fis = context.openFileInput(INCOME_BIG_CATEGORY_DATABASE);
            ObjectInputStream oi = new ObjectInputStream(fis);
            incomeBigCategory = (ArrayList<BigCategory>) oi.readObject();
            oi.close();

        } catch (Exception e) {
            Log.e(TAG, "load incomeBigCategory failed");
        }

        if(incomeBigCategory == null)
            setDefaultIncomeBigCategory();


        try {
            FileInputStream fis;
            fis = context.openFileInput(INCOME_SMALL_CATEGORY_DATABASE);
            ObjectInputStream oi = new ObjectInputStream(fis);
            incomeSmallCategory = (ArrayList<SmallCategory>) oi.readObject();
            oi.close();

        } catch (Exception e) {
            Log.e(TAG, "load incomeSmallCategory failed");
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
        expenseBigCategory = new ArrayList<>();

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
    }

    private void setDefaultExpenseSmallCategory() {
        expenseSmallCategory = new ArrayList<>();

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
    }

    private void setDefaultIncomeBigCategory() {
        incomeBigCategory = new ArrayList<>();

        insertBigCategory(MoneyUsageItem.INCOME, "주수입");
        insertBigCategory(MoneyUsageItem.INCOME, "부수입");
        insertBigCategory(MoneyUsageItem.INCOME, "기타");
    }

    private void setDefaultIncomeSmallCategory() {
        incomeSmallCategory = new ArrayList<>();

        insertSmallCategory(MoneyUsageItem.INCOME, "주수입", "급여");
        insertSmallCategory(MoneyUsageItem.INCOME, "주수입", "용돈");
        insertSmallCategory(MoneyUsageItem.INCOME, "주수입", "상여");
        insertSmallCategory(MoneyUsageItem.INCOME, "주수입", "사업소득");

        insertSmallCategory(MoneyUsageItem.INCOME, "부수입", "이자");
        insertSmallCategory(MoneyUsageItem.INCOME, "부수입", "기타");
        insertSmallCategory(MoneyUsageItem.INCOME, "부수입", "빌려준 돈");
    }


    public int getIDFromSmallCategoryName(int classification, String bigCategoryName, String smallCategoryName) {
        ArrayList<BigCategory> bigCategories = (classification == 0) ? expenseBigCategory : incomeBigCategory;
        ArrayList<SmallCategory> smallCategories = (classification == 0) ? expenseSmallCategory : incomeSmallCategory;

        for(SmallCategory smallCategory : smallCategories)
            if(smallCategory.name.equals(smallCategoryName))
                if(bigCategories.get(getBigCategoryID(classification, bigCategoryName)).name.equals(bigCategoryName))
                    return smallCategories.indexOf(smallCategory);

        return -1;
    }

    public String[] getCategoryNameSetFromID(int classification, int id) {
        ArrayList<SmallCategory> smallCategories = (classification == 0) ? expenseSmallCategory : incomeSmallCategory;
        SmallCategory item = smallCategories.get(id);

        return new String[]{item.bigCategory.name, item.name};
    }


    private void insertBigCategory(int classification, String name) {
        ArrayList<BigCategory> bigCategories = (classification == 0) ? expenseBigCategory : incomeBigCategory;
        bigCategories.add(new BigCategory(name));
        saveDatabase();
    }

    public void insertSmallCategory(int classification, String bigCategoryName, String name) {
        insertSmallCategory(classification, getBigCategoryID(classification, bigCategoryName), name);
        saveDatabase();
    }

    public void insertSmallCategory(int classification, int bigCategoryNameId, String name) {
        ArrayList<BigCategory> bigCategories = (classification == 0) ? expenseBigCategory : incomeBigCategory;
        ArrayList<SmallCategory> smallCategories = (classification == 0) ? expenseSmallCategory : incomeSmallCategory;

        BigCategory bigCategory = bigCategories.get(bigCategoryNameId);
        smallCategories.add(new SmallCategory(bigCategory, name));
        saveDatabase();
    }


    public ArrayList<String> getAllSmallCategory(int classification, int bigCategoryID) {
        ArrayList<SmallCategory> smallCategories = (classification == 0) ? expenseSmallCategory : incomeSmallCategory;

        ArrayList<String> arrayList = new ArrayList<>();
        for(SmallCategory smallCategory : smallCategories)
            arrayList.add(smallCategory.name);

        return arrayList;
    }

    public ArrayList<String> getAllSmallCategory(int classification, String bigCategoryName) {
        return getAllSmallCategory(classification, getBigCategoryID(classification, bigCategoryName));
    }

    public ArrayList<String> getAllBigCategory(int classification) {
        ArrayList<BigCategory> bigCategories = (classification == 0) ? expenseBigCategory : incomeBigCategory;

        ArrayList<String> arrayList = new ArrayList<>();
        for(BigCategory bigCategory : bigCategories)
            arrayList.add(bigCategory.name);

        return arrayList;
    }


    private String getBigCategoryNameById(int classification, int id) {
        ArrayList<BigCategory> bigCategories = (classification == 0) ? expenseBigCategory : incomeBigCategory;

        return bigCategories.get(id).name;
    }

    public String getSmallCategoryNameById(int classification, int id) {
        ArrayList<SmallCategory> smallCategories = (classification == 0) ? expenseSmallCategory : incomeSmallCategory;

        return smallCategories.get(id).name;
    }


    public int getBigCategoryID(int classification, String bigCategoryName) {
        ArrayList<BigCategory> bigCategories = (classification == 0) ? expenseBigCategory : incomeBigCategory;

        for(BigCategory bigCategory : bigCategories)
            if(bigCategory.name.equals(bigCategoryName))
                return bigCategories.indexOf(bigCategory);


        return -1;
    }

    public int getSmallCategoryID(int classification, String bigCategoryName, String name) {
        return getSmallCategoryID(classification, getBigCategoryID(classification, bigCategoryName), name);
    }

    public int getSmallCategoryID(int classification, int bigCategoryId, String name) {
        ArrayList<BigCategory> bigCategories = (classification == 0) ? expenseBigCategory : incomeBigCategory;
        ArrayList<SmallCategory> smallCategories = (classification == 0) ? expenseSmallCategory : incomeSmallCategory;

        for(SmallCategory smallCategory : smallCategories)
            if(smallCategory.name.equals(name))
                if(bigCategories.get(bigCategoryId).equals(smallCategory.bigCategory))
                    return smallCategories.indexOf(smallCategory);

        return -1;
    }


    private void printAllDatabase() {
        for(BigCategory expenseBig : expenseBigCategory)
            Log.e(TAG,"expenseBigCategory : " + expenseBig.name);

        for(SmallCategory expenseSmall : expenseSmallCategory)
            Log.e(TAG,"expenseSmallCategory : " + expenseSmall.name);

        for(BigCategory incomeBig : incomeBigCategory)
            Log.e(TAG,"expenseBigCategory : " + incomeBig.name);

        for(SmallCategory incomeSmall : incomeSmallCategory)
            Log.e(TAG,"expenseSmallCategory : " + incomeSmall.name);
    }


    static class BigCategory implements Serializable {
        String name;
        int frequency;

        private BigCategory(String name) {
            this.name = name;
            frequency = 0;
        }

        public void increaseFrequency() {
            frequency++;
        }

    }

    static class SmallCategory implements Serializable{
        BigCategory bigCategory;
        String name;
        int frequency;

        private SmallCategory(BigCategory bigCategory, String name) {
            this.bigCategory = bigCategory;
            this.name = name;
            this.frequency = 0;
        }

        public void increaseFrequency() {
            frequency++;
        }
    }

*/
}
