package com.noverish.cashbook.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.noverish.cashbook.R;

import java.util.Calendar;

/**
 * Created by Noverish on 2016-05-03.
 */
public class DateTimeSelector extends LinearLayout {

    private TextView dateTextView, timeTextView;

    private Context ctx;
    private Calendar calendar;

    private int padding;

    public DateTimeSelector(Context context) {
        super(context);

        if(!isInEditMode())
            init(context);
    }

    public DateTimeSelector(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(!isInEditMode())
            init(context);
    }

    private void init(Context context) {
        this.ctx = context;

        calendar = Calendar.getInstance();
        padding = (int) getResources().getDimension(R.dimen.date_time_selector_text_view_padding);

        dateTextView = new TextView(context);
        dateTextView.setText(millisToDateString(calendar.getTimeInMillis()));
        dateTextView.setPadding(padding, padding, padding, padding);
        dateTextView.setTypeface(null, Typeface.BOLD);;
        dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.date_time_selector_text_size));
        dateTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        setTimeInMillis(calendar.getTimeInMillis());
                    }
                };

                new DatePickerDialog(ctx, dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        addView(dateTextView);

        timeTextView = new TextView(context);
        timeTextView.setText(millisToTimeString(calendar.getTimeInMillis()));
        timeTextView.setPadding(padding, padding, padding, padding);
        timeTextView.setTypeface(null, Typeface.BOLD);
        timeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.date_time_selector_text_size));
        timeTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        setTimeInMillis(calendar.getTimeInMillis());
                    }
                };

                new TimePickerDialog(ctx, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        });
        addView(timeTextView);
    }

    public long getTimeInMillis() {
        return calendar.getTimeInMillis();
    }

    public void setTimeInMillis(long millis) {
        calendar.setTimeInMillis(millis);

        dateTextView.setText(millisToDateString(millis));
        timeTextView.setText(millisToTimeString(millis));
    }

    public static String millisToDateString(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String[] days = {"일", "월", "화", "수", "목", "금", "토"};

        return String.format("%d년 %d월 %d일 %s요일",year, month+1, date, days[day-1]);
    }

    public static String millisToTimeString(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        int hour = calendar.get(Calendar.HOUR);
        hour = (hour == 0) ? 12 : hour;

        int minute = calendar.get(Calendar.MINUTE);
        String am_pm;

        if(calendar.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "오전";
        else
            am_pm = "오후";

        return String.format("%s %d시 %d분", am_pm, hour, minute);
    }


}
