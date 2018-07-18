package com.lzy.imagepicker.view.item;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.imagepicker.R;


public class ItemIconWithText extends ItemCommon {

    private String text;
    private float textSize;
    private int textColor;

    private Drawable icon;
    private float iconSize;
    private float iconWidth;
    private float iconHeight;

    private TextView tvText;
    private ImageView ivIcon;

    public ItemIconWithText(@NonNull Context context) {
        super(context);
    }

    public ItemIconWithText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initAttrs(AttributeSet attrs) {
        TypedArray a = this.context.obtainStyledAttributes(attrs, R.styleable.ItemIconWithText);

        text = a.getString(R.styleable.ItemIconWithText_text);
        textSize = a.getDimension(R.styleable.ItemIconWithText_text_size, 0);
        textColor = a.getColor(R.styleable.ItemIconWithText_text_color,
                ContextCompat.getColor(context, R.color.color_999999));

        icon = a.getDrawable(R.styleable.ItemIconWithText_icon);
        iconSize = a.getDimension(R.styleable.ItemIconWithText_icon_size, 0);
        iconWidth = a.getDimension(R.styleable.ItemIconWithText_icon_width, 0);
        iconHeight = a.getDimension(R.styleable.ItemIconWithText_icon_height, 0);

        a.recycle();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.picker_item_icon_with_text;
    }

    @Override
    protected void initView() {
        tvText = findViewById(R.id.tv_text);
        ivIcon = findViewById(R.id.iv_icon);

        setItemText(text);
        setItemTextColor(textColor);
        setItemTextSize(textSize);

        setIcon(icon);

        if (iconSize > 0) {
            setIconSize(iconSize);
        }
        if (iconWidth > 0 && iconHeight > 0) {
            setIconWidthHeight(iconWidth, iconHeight);
        }
    }

    public void setItemText(String text) {
        if (tvText != null && text != null) {
            tvText.setText(text);
        }
    }

    public void setItemTextColor(int textColor) {
        if (tvText != null) {
            tvText.setTextColor(textColor);
        }
    }

    public void setItemTextSize(float textSize) {
        if (tvText != null && textSize > 0) {
            tvText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    public void setIcon(Drawable drawable) {
        if (ivIcon != null && drawable != null) {
            ivIcon.setImageDrawable(drawable);
        }
    }

    public void setIconSize(float iconSize) {
        if (ivIcon != null && iconSize > 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivIcon.getLayoutParams();
            layoutParams.width = (int) iconSize;
            layoutParams.height = (int) iconSize;
            ivIcon.setLayoutParams(layoutParams);
        }
    }

    public void setIconWidthHeight(float iconWidth, float iconHeight) {
        if (ivIcon != null && iconWidth > 0 && iconHeight > 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivIcon.getLayoutParams();
            layoutParams.width = (int) iconWidth;
            layoutParams.height = (int) iconHeight;
            ivIcon.setLayoutParams(layoutParams);
        }
    }


}
