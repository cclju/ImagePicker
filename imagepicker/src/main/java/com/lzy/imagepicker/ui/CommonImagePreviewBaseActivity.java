package com.lzy.imagepicker.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.lzy.imagepicker.DataHolder;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.R;
import com.lzy.imagepicker.adapter.CommonImagePageAdapter;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.ViewPagerFixed;

import java.util.ArrayList;

/**
 *  展示图片和视屏
 */
public abstract class CommonImagePreviewBaseActivity extends ImageBaseActivity {

    protected ImagePicker imagePicker;
    protected ArrayList<ImageItem> mImageItems;      //跳转进ImagePreviewFragment的图片文件夹
    protected int mCurrentPosition = 0;              //跳转进ImagePreviewFragment时的序号，第几个图片
//    protected TextView mTitleCount;                  //显示当前图片的位置  例如  5/31
    protected ArrayList<ImageItem> selectedImages;   //所有已经选中的图片
    protected View content;
//    protected View topBar;

    protected View topLLBack;
    protected View topViewAll;
    protected View bottomViewMore;
    protected View bottomViewSave;

    protected ViewPagerFixed mViewPager;
    protected CommonImagePageAdapter mAdapter;
    protected boolean isFromItems = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_activity_common_image_preview);

        mCurrentPosition = getIntent().getIntExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
        isFromItems = getIntent().getBooleanExtra(ImagePicker.EXTRA_FROM_ITEMS, false);

        if (isFromItems) {
            // 据说这样会导致大量图片崩溃
            mImageItems = (ArrayList<ImageItem>) getIntent().getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
        } else {
            // 下面采用弱引用会导致预览崩溃
            mImageItems = (ArrayList<ImageItem>) DataHolder.getInstance().retrieve(DataHolder.DH_CURRENT_IMAGE_FOLDER_ITEMS);
        }

        imagePicker = ImagePicker.getInstance();
        selectedImages = imagePicker.getSelectedImages();

        //初始化控件
        content = findViewById(R.id.content);

        //因为状态栏透明后，布局整体会上移，所以给头部加上状态栏的margin值，保证头部不会被覆盖
//        topBar = findViewById(R.id.top_bar);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topBar.getLayoutParams();
//            params.topMargin = Utils.getStatusHeight(this);
//            topBar.setLayoutParams(params);
//        }
//        topBar.findViewById(R.id.btn_ok).setVisibility(View.GONE);
//        topBar.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        topLLBack = findViewById(R.id.top_ll_back);
        topViewAll = findViewById(R.id.top_view_all);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams paramsBack = (RelativeLayout.LayoutParams) topLLBack.getLayoutParams();
            paramsBack.topMargin = Utils.getStatusHeight(this);
            topLLBack.setLayoutParams(paramsBack);

            RelativeLayout.LayoutParams paramsAll = (RelativeLayout.LayoutParams) topViewAll.getLayoutParams();
            paramsAll.topMargin = Utils.getStatusHeight(this);
            topViewAll.setLayoutParams(paramsAll);
        }

//        mTitleCount = (TextView) findViewById(R.id.tv_des);

        mViewPager = (ViewPagerFixed) findViewById(R.id.viewpager);
        mAdapter = new CommonImagePageAdapter(this, mImageItems);
        mAdapter.setPhotoViewClickListener(new CommonImagePageAdapter.PhotoViewClickListener() {
            @Override
            public void OnPhotoTapListener(View view, float v, float v1) {
                onImageSingleTap();
            }

            @Override
            public void OnVideoTapListener(int userAction) {
                onVideoTap(userAction);
            }
        });
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPosition, false);

        //初始化当前页面的状态
//        mTitleCount.setText(getString(R.string.ip_preview_image_count, mCurrentPosition + 1, mImageItems.size()));
    }

    /** 单击时，隐藏头和尾 */
    public abstract void onImageSingleTap();

    /** 单击时，隐藏头和尾 */
    public abstract void onVideoTap(int userAction);

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ImagePicker.getInstance().restoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ImagePicker.getInstance().saveInstanceState(outState);
    }
}