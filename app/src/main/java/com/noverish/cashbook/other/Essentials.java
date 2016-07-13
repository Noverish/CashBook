package com.noverish.cashbook.other;

/**
 * Created by Noverish on 2016-05-24.
 */
public class Essentials {
    public static String numberWithComma(int number) {
        return numberWithComma(String.valueOf(number));
    }

    public static String numberWithComma(long number) {
        return numberWithComma(String.valueOf(number));
    }

    public static String numberWithComma(String number) {
        String tmp = number.replace(",", "");

        for (int i = tmp.length() - 3; i >= 1; i -= 3) {
            tmp = tmp.substring(0, i) + "," + tmp.substring(i);
        }

        return tmp;
    }


}
