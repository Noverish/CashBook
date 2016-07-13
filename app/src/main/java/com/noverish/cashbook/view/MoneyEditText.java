package com.noverish.cashbook.view;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by Noverish on 2016-01-30.
 */
public class MoneyEditText extends EditText implements TextWatcher{

    private int cursorPosition;
    private int commaNumber;

    public MoneyEditText(Context context) {
        super(context);
        set(0);
    }

    public MoneyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        set(0);
    }

    public MoneyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        set(0);
    }

    public MoneyEditText(Context context, long number) {
        super(context);
        set(number);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //commaNumber = getCommaNumber(s);

        Log.e("before", s + " " + start + " " + after + " " + count);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        Log.e("    on", s + " " + start + " " + before + " " + count);

        /*if(count == 1)
            cursorPosition = start + 1;

        setSelection(cursorPosition);*/

    }

    @Override
    public void afterTextChanged(Editable s) {

        Log.e(" after", s.toString());

        /*this.removeTextChangedListener(this);

        setText(numberWithComma(s.toString()));
        if(commaNumber != getCommaNumber(getText())) {
            commaNumber = getCommaNumber(getText());
            setSelection(cursorPosition + 1);
        }

        this.addTextChangedListener(this);*/
    }


    public Editable getText() {
        String number = getText().toString();
        number = number.replace(",","");
        return new SpannableStringBuilder(number);
    }




    private int getCommaNumber(CharSequence s) {
        String tmp = s.toString();
        return tmp.split("[,]").length - 1;
    }

    private void set(long number) {
        setText(String.valueOf(number));
        addTextChangedListener(this);
        setSelection(getText().length());
    }






}
