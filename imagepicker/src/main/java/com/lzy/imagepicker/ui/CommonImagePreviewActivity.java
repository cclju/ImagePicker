package com.lzy.imagepicker.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.R;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.dialog.MenuAlertDialog;
import com.lzy.imagepicker.event.MenuClickEvent;
import com.lzy.imagepicker.view.CustomJZVideoPlayerStandard;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZUserActionStandard;
import cn.jzvd.JZVideoPlayer;

/**
 *  ViewPager 自定义展示图片和视屏
 */
public class CommonImagePreviewActivity extends CommonImagePreviewBaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final String TAG = "CommonImagePreview";

    private static final String EXTRA_IS_FROM_GRID_VIEW = "extra_is_from_grid_view";

    private boolean isFromGridView; // 标记是从 Gird 跳转过来的，大图行为需要特殊处理，如：点击图片直接关闭

    private boolean isDeleted; // 标记是否做了删除操作

    public static void startActivity(Activity activity, List<ImageItem> itemList, int position) {
        Intent intentPreview = new Intent(activity, CommonImagePreviewActivity.class);

        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) itemList);
        intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);

        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);

        activity.startActivity(intentPreview);
    }

    public static void startForResult(Activity activity, List<ImageItem> itemList, int position,
                                     int requestCode) {
        Intent intentPreview = new Intent(activity, CommonImagePreviewActivity.class);

        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) itemList);
        intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);

        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);

        intentPreview.putExtra(EXTRA_IS_FROM_GRID_VIEW, true);

        activity.startActivityForResult(intentPreview, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagePicker.setShowCamera(false);
        imagePicker.setCrop(false);

        isFromGridView = getIntent().getBooleanExtra(EXTRA_IS_FROM_GRID_VIEW, false);

//        topBar.findViewById(R.id.btn_back).setOnClickListener(this);
        topLLBack.setOnClickListener(this);
        findViewById(R.id.top_view_all).setOnClickListener(this);
        bottomViewMore = findViewById(R.id.bottom_view_more);
        bottomViewMore.setOnClickListener(this);
        bottomViewSave = findViewById(R.id.bottom_view_save);
        bottomViewSave.setOnClickListener(this);
//
//        topBar.setVisibility(isFromGridView ? View.GONE : View.VISIBLE);

//        mTitleCount.setText(getString(R.string.ip_preview_image_count, mCurrentPosition + 1, mImageItems.size()));
        //滑动ViewPager的时候，根据外界的数据改变当前的选中状态和当前的图片的位置描述文本
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
//                mTitleCount.setText(getString(R.string.ip_preview_image_count, mCurrentPosition + 1, mImageItems.size()));
            }
        });

//        NavigationBarChangeListener.with(this, NavigationBarChangeListener.ORIENTATION_HORIZONTAL)
//                .setListener(new NavigationBarChangeListener.OnSoftInputStateChangeListener() {
//                    @Override
//                    public void onNavigationBarShow(int orientation, int height) {
//                        topBar.setPadding(0, 0, height, 0);
//                    }
//
//                    @Override
//                    public void onNavigationBarHide(int orientation) {
//                        topBar.setPadding(0, 0, 0, 0);
//                    }
//                });

        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.top_ll_back) {

            onBackPressed();

        } else if (id == R.id.top_view_all) {

            if (isFromGridView) {
                onBackPressed();
            } else {
                // 调用自定义相册（图片和视屏展示）
                CommonImageGridActivity.startWithFinishActivity(this, mImageItems);
            }

        } else if (id == R.id.bottom_view_more) {

            MenuAlertDialog alertDialog = new MenuAlertDialog(this, R.style.MMTheme_DataSheet,
                    new MenuAlertDialog.OnClickDo() {
                @Override
                public void onClick(int whichButton) {
                    if (whichButton == R.id.popup_choose_forward) {

                        Log.d(TAG, "ImagePicker, Menu onClick forward and position: " + mCurrentPosition);
                        EventBus.getDefault().post(new MenuClickEvent(MenuClickEvent.TYPE_FORWARD,
                                mCurrentPosition, CommonImagePreviewActivity.this));


                    } else if (whichButton == R.id.popup_choose_favorite) {

                        Log.d(TAG, "ImagePicker, Menu onClick favorite and position: " + mCurrentPosition);
                        EventBus.getDefault().post(new MenuClickEvent(MenuClickEvent.TYPE_FAVORITE,
                                mCurrentPosition, CommonImagePreviewActivity.this));

                    } else if (whichButton == R.id.popup_choose_delete) {

                        Log.d(TAG, "ImagePicker, Menu onClick delete and position: " + mCurrentPosition);
                        EventBus.getDefault().post(new MenuClickEvent(MenuClickEvent.TYPE_DELETE,
                                mCurrentPosition, CommonImagePreviewActivity.this));

                        //移除当前图片刷新界面
                        mImageItems.remove(mCurrentPosition);
                        isDeleted = true;
                        if (mImageItems.size() > 0) {
                            mAdapter.setData(mImageItems);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            onBackPressed();
                        }

                    }
                }
            });
            alertDialog.show();

        } else if (id == R.id.bottom_view_save) {

            Log.d(TAG, "ImagePicker, save and position: " + mCurrentPosition);
            EventBus.getDefault().post(new MenuClickEvent(MenuClickEvent.TYPE_SAVE,
                    mCurrentPosition, CommonImagePreviewActivity.this));
        }
    }

    /** 是否删除此张图片 */
//    private void showDeleteDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("提示");
//        builder.setMessage("要删除这张照片吗？");
//        builder.setNegativeButton("取消", null);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //移除当前图片刷新界面
//                mImageItems.remove(mCurrentPosition);
//                if (mImageItems.size() > 0) {
//                    mAdapter.setData(mImageItems);
//                    mAdapter.notifyDataSetChanged();
//                    mTitleCount.setText(getString(R.string.ip_preview_image_count, mCurrentPosition + 1, mImageItems.size()));
//                } else {
//                    onBackPressed();
//                }
//            }
//        });
//        builder.show();
//    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            Log.d(TAG, "ImagePicker, JZVideoPlayer.backPress");
            return;
        }

        Log.d(TAG, "ImagePicker, onBackPressed");

        if (isFromGridView && isDeleted) { // 带回最新数据，需要刷新前一个页面的数据
            Intent intent = new Intent();
            intent.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, mImageItems);
            setResult(ImagePicker.RESULT_CODE_BACK, intent);
            finish();
        }

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    /** 单击时，隐藏头和尾 */
    @Override
    public void onImageSingleTap() {

        if (isFromGridView) {
            onBackPressed();
        } else {
            Log.d("cjwtest", "onImageSingleTap -----------");
            handleUI();
        }
    }

    @Override
    public void onVideoTap(int userAction) {
        // 满足条件再切换
        Log.d("cjwtest", "onVideoTap -----------");
        if (userAction == JZUserActionStandard.ON_CLICK_START_THUMB) {

            // hide
            if (topLLBack.getVisibility() == View.VISIBLE) {
                Log.d("cjwtest", "onVideoTap hide icons");
                handleUI();
            }

        } else if (userAction == JZUserActionStandard.ON_AUTO_COMPLETE) {

            // show
            if (topLLBack.getVisibility() != View.VISIBLE) {
                Log.d("cjwtest", "onVideoTap show icons");
                handleUI();
            }

        } else if (userAction == JZUserActionStandard.ON_CLICK_BLANK) {

            // auto hide or show
            handleUI();

        } else if (userAction == CustomJZVideoPlayerStandard.ON_CONTROL_VIEW_DISMISS) {

            // hide
            if (topLLBack.getVisibility() == View.VISIBLE) {
                Log.d("cjwtest", "onVideoTap hide icons when control view dismiss");
                handleUI();
            }
        }

    }

    /**
     * 操作按钮显示隐藏 切换
     *
     */
    private void handleUI() {

        if (topLLBack.getVisibility() == View.VISIBLE) {
//          topLLBack.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_out));
            topLLBack.setVisibility(View.GONE);

//          topViewAll.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_out));
            topViewAll.setVisibility(View.GONE);
            bottomViewMore.setVisibility(View.GONE);
            bottomViewSave.setVisibility(View.GONE);

            tintManager.setStatusBarTintResource(Color.TRANSPARENT);//通知栏所需颜色
        } else {
//          topLLBack.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_in));
            topLLBack.setVisibility(View.VISIBLE);

//          topViewAll.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_in));
            topViewAll.setVisibility(View.VISIBLE);
            bottomViewMore.setVisibility(View.VISIBLE);
            bottomViewSave.setVisibility(View.VISIBLE);

            tintManager.setStatusBarTintResource(R.color.ip_color_primary_dark);//通知栏所需颜色
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}