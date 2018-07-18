package com.lzy.imagepicker.event;

import android.app.Activity;

/**
 * Created by jarvis on 2018/7/11.
 */

public class MenuClickEvent {

    public static final int TYPE_FORWARD = 1;
    public static final int TYPE_FAVORITE = 2;
    public static final int TYPE_DELETE = 3;
    public static final int TYPE_SAVE = 4;

    private int type;
    private int position;
    private Activity activity;

    public MenuClickEvent(int type, int position, Activity activity) {
        this.type = type;
        this.position = position;
        this.activity = activity;
    }

    public int getType() {
        return type;
    }

    public int getPosition() {
        return position;
    }

    public Activity getActivity() {
        return activity;
    }
}
