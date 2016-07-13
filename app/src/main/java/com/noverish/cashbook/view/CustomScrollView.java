package com.noverish.cashbook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Noverish on 2016-01-26.
 */
public class CustomScrollView extends ScrollView {
    private boolean scrollable = true;
    private Thread up, down;
    private boolean upping = false, downing = false;
    public static final int deltaScroll = 20;
    public static final int deltaMilliSec = 20;

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollable(boolean scrollable) {
        //Log.e("scrollable",scrollable + "");
        this.scrollable = scrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // if we can scroll pass the event to the superclass
                if (scrollable)
                    return super.onTouchEvent(ev);
                // only continue to handle the touch event if scrolling enabled
                return false; // scrollable is always false at this point
            default:
                //Log.e("scroll",getScrollY() + "");
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        if (!scrollable) return false;
        else return super.onInterceptTouchEvent(ev);
    }

    public void startUp() {
        if(!upping) {
            up = getUp();
            up.start();
            upping = true;
        }
    }

    public void stopUp() {
        if(up != null) {
            up.interrupt();
            up = null;
            upping = false;
        }
    }

    public void startDown() {
        if(!downing) {
            down = getDown();
            down.start();
            downing = true;
        }
    }

    public void stopDown() {
        if(down != null) {
            down.interrupt();
            down = null;
            downing = false;
        }
    }

    private Thread getUp() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(!Thread.currentThread().isInterrupted()) {
                        scrollBy(0, -deltaScroll);
                        Thread.sleep(deltaMilliSec);
                    }
                } catch (InterruptedException inter) {
                    Log.e("ScrollUp", "interrupted");
                }
                Log.e("over","up");
            }
        });
    }
    private Thread getDown() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(!Thread.currentThread().isInterrupted()) {
                        scrollBy(0, deltaScroll);
                        Thread.sleep(deltaMilliSec);
                    }
                } catch (InterruptedException inter) {
                    Log.e("ScrollDown", "interrupted");
                }
                Log.e("over","down");
            }
        });
    }
}
