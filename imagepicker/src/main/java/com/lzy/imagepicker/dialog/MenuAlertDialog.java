package com.lzy.imagepicker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.lzy.imagepicker.R;


public class MenuAlertDialog extends Dialog implements OnClickListener {

    private Context context;

    private LinearLayout popupChooseForward;
    private LinearLayout popupChooseFavorite;
    private LinearLayout popupChooseDelete;

    private LinearLayout popupChooseCancel;

    private OnClickDo onClickDo;

    public MenuAlertDialog(Context context, int theme, OnClickDo onClickDo) {
        super(context, theme);
        this.context = context;
        this.onClickDo = onClickDo;
    }

    public interface OnClickDo {
        void onClick(int whichButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_dialog_menu_alert);

        initView();

        setListeners();
    }

    public void initView(){
        popupChooseForward = (LinearLayout) findViewById(R.id.popup_choose_forward);
        popupChooseFavorite = (LinearLayout) findViewById(R.id.popup_choose_favorite);
        popupChooseDelete = (LinearLayout) findViewById(R.id.popup_choose_delete);
        popupChooseCancel = (LinearLayout) findViewById(R.id.popup_choose_cancel);
    }

    public void setListeners(){
        popupChooseForward.setOnClickListener(this);
        popupChooseFavorite.setOnClickListener(this);
        popupChooseDelete.setOnClickListener(this);
        popupChooseCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (onClickDo != null) {
            onClickDo.onClick(id);
        }
        dismiss();
    }


}