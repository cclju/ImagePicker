package com.lzy.imagepicker.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.R;
import com.lzy.imagepicker.adapter.CommonImageRecyclerAdapterWithHead;
import com.lzy.imagepicker.adapter.decoration.GridSpacingItemWithHeadDecoration;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.item.ItemIconWithText;

import java.util.ArrayList;
import java.util.List;

/**
 * GridView 展示 图片和视屏 with Header
 */
public class CommonImageGridWithHeadActivity extends ImageBaseActivity implements CommonImageRecyclerAdapterWithHead.OnImageItemClickListener,
        ImagePicker.OnImageSelectedListener, View.OnClickListener {

    private static final String TAG = "CommonImageGridActivity";

    public static final String EXTRAS_IMAGES = "IMAGES";

    private ImagePicker imagePicker;

    private TextView tvLeft;
    private TextView tvRight;
    private LinearLayout llFooterBar;
    private ItemIconWithText bottomViewForward;
    private ItemIconWithText bottomViewFavorite;
    private ItemIconWithText bottomViewDelete;
    private ItemIconWithText bottomViewSave;

    private RecyclerView mRecyclerView;
//    private CommonImageRecyclerAdapter mRecyclerAdapter;
    private CommonImageRecyclerAdapterWithHead mRecyclerAdapter;

    private ArrayList<ImageItem> imageItemList;


    public static void startWithFinishActivity(Activity activity, List<ImageItem> itemList) {
        Intent intentPreview = new Intent(activity, CommonImageGridWithHeadActivity.class);
        intentPreview.putExtra(EXTRAS_IMAGES, (ArrayList<ImageItem>) itemList);
        activity.startActivity(intentPreview);
        activity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_activity_common_image_grid_with_head);

        imagePicker = ImagePicker.getInstance();

        imagePicker.setMultiMode(false);
        imagePicker.clear();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);

        findViewById(R.id.iv_back).setOnClickListener(this);
        tvLeft = findViewById(R.id.tv_left);
        tvRight = findViewById(R.id.tv_right);
        tvRight.setOnClickListener(this);

        llFooterBar = findViewById(R.id.ll_footer_bar);
        llFooterBar.setVisibility(View.GONE);

        bottomViewForward = findViewById(R.id.bottom_view_forward);
        bottomViewFavorite = findViewById(R.id.bottom_view_favorite);
        bottomViewDelete = findViewById(R.id.bottom_view_delete);
        bottomViewSave = findViewById(R.id.bottom_view_save);

        mRecyclerAdapter = new CommonImageRecyclerAdapterWithHead(this, null);

//        onImageSelected(0, null, false);

        // TODO 据说这样会导致大量图片崩溃
        imageItemList = (ArrayList<ImageItem>) getIntent().getSerializableExtra(EXTRAS_IMAGES);

        onImagesLoaded(imageItemList);
    }

    @Override
    protected void onDestroy() {
        imagePicker.removeOnImageSelectedListener(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_right) {
            // 选择 切换
            if (imagePicker.isMultiMode()) {

                imagePicker.setMultiMode(false);
                imagePicker.clear();

                tvRight.setText("选择");
                tvLeft.setText("图片和视频");

                llFooterBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
                llFooterBar.setVisibility(View.GONE);

                setFooterBarEnable(false);

            } else {

                imagePicker.setMultiMode(true);
                imagePicker.setSelectLimit(9);
                imagePicker.addOnImageSelectedListener(this);

                tvRight.setText("取消");
                tvLeft.setText(String.format("已选择%s个文件", "" + imagePicker.getSelectImageCount()));

                llFooterBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
                llFooterBar.setVisibility(View.VISIBLE);

            }

            mRecyclerAdapter.notifyDataSetChanged();

        } else if (id == R.id.iv_back) {
            //点击返回按钮
            finish();
        } else if (id == R.id.bottom_view_forward) {
            Log.d(TAG, "ImagePicker, onClick forward");
            Toast.makeText(this, "转发点击", Toast.LENGTH_LONG).show();

        } else if (id == R.id.bottom_view_favorite) {
            Log.d(TAG, "ImagePicker, onClick favorite");
            Toast.makeText(this, "收藏点击", Toast.LENGTH_LONG).show();

        } else if (id == R.id.bottom_view_delete) {
            Log.d(TAG, "ImagePicker, onClick delete");
            Toast.makeText(this, "删除点击", Toast.LENGTH_LONG).show();

        } else if (id == R.id.bottom_view_save) {
            Log.d(TAG, "ImagePicker, onClick save");
            Toast.makeText(this, "保存点击", Toast.LENGTH_LONG).show();

        }
    }

    private void setFooterBarEnable(boolean isEnable) {

        Log.d(TAG, "ImagePicker, setFooterBarEnable: " + isEnable);

        if (isEnable) {

            bottomViewForward.setIcon(ContextCompat.getDrawable(this, R.drawable.picker_icon_forward));
            bottomViewForward.setItemTextColor(ContextCompat.getColor(this, R.color.color_222222));

            bottomViewFavorite.setIcon(ContextCompat.getDrawable(this, R.drawable.picker_icon_favorite));
            bottomViewFavorite.setItemTextColor(ContextCompat.getColor(this, R.color.color_222222));

            bottomViewDelete.setIcon(ContextCompat.getDrawable(this, R.drawable.picker_icon_delete));
            bottomViewDelete.setItemTextColor(ContextCompat.getColor(this, R.color.color_222222));

            bottomViewSave.setIcon(ContextCompat.getDrawable(this, R.drawable.picker_icon_save));
            bottomViewSave.setItemTextColor(ContextCompat.getColor(this, R.color.color_222222));

            bottomViewForward.setOnClickListener(this);
            bottomViewFavorite.setOnClickListener(this);
            bottomViewDelete.setOnClickListener(this);
            bottomViewSave.setOnClickListener(this);

        } else {

            bottomViewForward.setIcon(ContextCompat.getDrawable(this, R.drawable.picker_icon_forward_disable));
            bottomViewForward.setItemTextColor(ContextCompat.getColor(this, R.color.color_999999));

            bottomViewFavorite.setIcon(ContextCompat.getDrawable(this, R.drawable.picker_icon_favorite_disable));
            bottomViewFavorite.setItemTextColor(ContextCompat.getColor(this, R.color.color_999999));

            bottomViewDelete.setIcon(ContextCompat.getDrawable(this, R.drawable.picker_icon_delete_disable));
            bottomViewDelete.setItemTextColor(ContextCompat.getColor(this, R.color.color_999999));

            bottomViewSave.setIcon(ContextCompat.getDrawable(this, R.drawable.picker_icon_save_disable));
            bottomViewSave.setItemTextColor(ContextCompat.getColor(this, R.color.color_999999));

            bottomViewForward.setOnClickListener(null);
            bottomViewFavorite.setOnClickListener(null);
            bottomViewDelete.setOnClickListener(null);
            bottomViewSave.setOnClickListener(null);
        }
    }

    public void onImagesLoaded(ArrayList<ImageItem> itemList) {
        if (itemList == null || itemList.size() == 0) {
            mRecyclerAdapter.refreshData(null);
        } else {
            mRecyclerAdapter.refreshData(itemList);
        }
        mRecyclerAdapter.setOnImageItemClickListener(this);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mRecyclerAdapter.hasHeader(position)) {
                    return 4;
                }
                return 1;
            }
        });

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemWithHeadDecoration(4,
                Utils.dp2px(this, 1), false));
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { // 静止

                    if (!recyclerView.canScrollVertically(1)) {
                        // onScrollToBottom
                    }
                    if (!recyclerView.canScrollVertically(-1)) {
                        // onScrollToTop
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING
                        || newState == RecyclerView.SCROLL_STATE_SETTLING) { // 滚动中

                    // onScrolling
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // onScrollDown
                } else {
                    // onScrollUp
                }
            }
        });
    }

    @Override
    public void onImageItemClick(View view, ImageItem imageItem, int position) {
        // 图片点击 预览图片或视屏
        CommonImagePreviewActivity.startForResult(this, imageItemList, position,
                ImagePicker.REQUEST_CODE_GRID_TO_PREVIEW);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onImageSelected(int position, ImageItem item, boolean isAdd) {
        // 右上角 checkbox 点击

        int selectImageCount = imagePicker.getSelectImageCount();
        tvLeft.setText(String.format("已选择%s个文件", "" + selectImageCount));

        setFooterBarEnable(selectImageCount > 0);

        for (int i = 0; i < mRecyclerAdapter.getItemCount(); i++) {
            ImageItem imageItem = mRecyclerAdapter.getItem(i);

            String imageItemPath = imageItem.isVideo() ? imageItem.videoPath : imageItem.path;
            String itemPath = item.isVideo() ? item.videoPath : item.path;

            if (imageItemPath != null && !"".equals(imageItemPath) && imageItemPath.equals(itemPath)) {
                mRecyclerAdapter.notifyItemChanged(i);
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getExtras() != null) {
            if (requestCode == ImagePicker.REQUEST_CODE_GRID_TO_PREVIEW
                    && resultCode == ImagePicker.RESULT_CODE_BACK) {

                // need to refresh
                imageItemList = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (imageItemList != null && imageItemList.size() > 0) {

                    Log.d(TAG, "ImagePicker, preview view back to grid view refresh data");
                    mRecyclerAdapter.refreshData(imageItemList);

                } else {
                    Log.d(TAG, "ImagePicker, preview view back to grid view finish");
                    finish();
                }
            }
        }

    }

}