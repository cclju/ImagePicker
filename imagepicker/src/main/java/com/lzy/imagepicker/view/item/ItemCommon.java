package com.lzy.imagepicker.view.item;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;


public abstract class ItemCommon extends FrameLayout {

    protected Context context;

    protected abstract void initAttrs(AttributeSet attrs);
    protected abstract int getLayoutRes();
    protected abstract void initView();

    public ItemCommon(@NonNull Context context) {
        this(context, null);
    }

    public ItemCommon(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        initAttrs(attrs);

        LayoutInflater.from(context).inflate(getLayoutRes(), this, true);

        initView();
    }


}
