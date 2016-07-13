package com.noverish.cashbook.other;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

import com.noverish.cashbook.R;

import java.util.HashMap;

/**
 * Created by Noverish on 2016-05-14.
 */
public class IconManager {
    public HashMap<String, Drawable> icons;

    public static String meal = "meal";
    public static String snack = "snack";
    public static String beverage = "beverage";
    public static String drinks = "drinks";
    public static String appliance = "appliance";
    public static String cloth = "cloth";
    public static String communication = "communication";
    public static String furniture = "furniture";
    public static String haircut = "haircut";
    public static String culture = "culture";
    public static String housing = "housing";
    public static String gathering = "gathering";
    public static String laundry = "laundry";
    public static String medical = "medical";
    public static String others = "others";
    public static String study = "study";
    public static String transportation = "transportation";

    private static IconManager manger;
    public static IconManager getIconManager(Context context) {
        if(manger == null)
            manger = new IconManager(context);
        return manger;
    }

    private IconManager(Context context) {
        icons = new HashMap<>();

        icons.put(meal, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_meal, null));
        icons.put(snack, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_snack, null));
        icons.put(beverage, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_beverage, null));
        icons.put(drinks, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_drink, null));
        icons.put(appliance, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_appliance, null));
        icons.put(cloth, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_cloth, null));
        icons.put(communication, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_communication, null));
        icons.put(furniture, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_furniture, null));
        icons.put(haircut, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_haircut, null));
        icons.put(culture, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_culture, null));
        icons.put(housing, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_housing, null));
        icons.put(gathering, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_gathering, null));
        icons.put(laundry, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_laundry, null));
        icons.put(medical, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_medical, null));
        icons.put(others, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_others, null));
        icons.put(study, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_study, null));
        icons.put(transportation, ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_transportation, null));
    }

    public Drawable getDrawable(String key) {
        return icons.get(key);
    }

    public String getKey(Drawable drawable) {
        for(String key : icons.keySet())
            if(icons.get(key).equals(drawable))
                return key;

        return null;
    }
}
