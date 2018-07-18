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

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.R;
import com.lzy.imagepicker.adapter.CommonImageRecyclerAdapter;
import com.lzy.imagepicker.adapter.CommonImageRecyclerAdapter.OnImageItemClickListener;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.lzy.imagepicker.view.item.ItemIconWithText;

import java.util.ArrayList;
import java.util.List;

/**
 * GridView 展示 图片和视屏
 */
public class CommonImageGridActivity extends ImageBaseActivity implements OnImageItemClickListener, ImagePicker.OnImageSelectedListener, View.OnClickListener {

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
    private CommonImageRecyclerAdapter mRecyclerAdapter;

    private ArrayList<ImageItem> imageItemList;


    public static void startWithFinishActivity(Activity activity, List<ImageItem> itemList) {
        Intent intentPreview = new Intent(activity, CommonImageGridActivity.class);
        intentPreview.putExtra(EXTRAS_IMAGES, (ArrayList<ImageItem>) itemList);
        activity.startActivity(intentPreview);
        activity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_activity_common_image_grid);

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

        mRecyclerAdapter = new CommonImageRecyclerAdapter(this, null);

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

        } else if (id == R.id.bottom_view_favorite) {
            Log.d(TAG, "ImagePicker, onClick favorite");

        } else if (id == R.id.bottom_view_delete) {
            Log.d(TAG, "ImagePicker, onClick delete");

        } else if (id == R.id.bottom_view_save) {
            Log.d(TAG, "ImagePicker, onClick save");

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
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, Utils.dp2px(this, 2), false));
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void onImageItemClick(View view, ImageItem imageItem, int position) {
        //根据是否有相机按钮确定位置
        position = imagePicker.isShowCamera() ? position - 1 : position;
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

        for (int i = imagePicker.isShowCamera() ? 1 : 0; i < mRecyclerAdapter.getItemCount(); i++) {
            ImageItem imageItem = mRecyclerAdapter.getItem(i);

            String imageItemPath = imageItem.isVideo() ? imageItem.videoPath : imageItem.path;
            String itemPath = item.isVideo() ? item.videoPath : item.path;

            if (imageItemPath != null && imageItemPath.equals(itemPath)) {
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

//        if (data != null && data.getExtras() != null) {
//            if (resultCode == ImagePicker.RESULT_CODE_BACK) {
////                isOrigin = data.getBooleanExtra(ImagePreviewActivity.ISORIGIN, false);
//            } else {
//                //从拍照界面返回
//                //点击 X , 没有选择照片
//                if (data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) == null) {
//                    //什么都不做 直接调起相机
//                } else {
//                    //说明是从裁剪页面过来的数据，直接返回就可以
//                    setResult(ImagePicker.RESULT_CODE_ITEMS, data);
//                }
//                finish();
//            }
//        } else {
//            //如果是裁剪，因为裁剪指定了存储的Uri，所以返回的data一定为null
//            if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_TAKE) {
//                //发送广播通知图片增加了
//                ImagePicker.galleryAddPic(this, imagePicker.getTakeImageFile());
//
//                /**
//                 * 2017-03-21 对机型做旋转处理
//                 */
//                String path = imagePicker.getTakeImageFile().getAbsolutePath();
////                int degree = BitmapUtil.getBitmapDegree(path);
////                if (degree != 0){
////                    Bitmap bitmap = BitmapUtil.rotateBitmapByDegree(path,degree);
////                    if (bitmap != null){
////                        File file = new File(path);
////                        try {
////                            FileOutputStream bos = new FileOutputStream(file);
////                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
////                            bos.flush();
////                            bos.close();
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
////                    }
////                }
//
//                ImageItem imageItem = new ImageItem();
//                imageItem.path = path;
//                imagePicker.clearSelectedImages();
//                imagePicker.addSelectedImageItem(0, imageItem, true);
//                if (imagePicker.isCrop()) {
//                    Intent intent = new Intent(CommonImageGridActivity.this, ImageCropActivity.class);
//                    startActivityForResult(intent, ImagePicker.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
//                } else {
//                    Intent intent = new Intent();
//                    intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
//                    setResult(ImagePicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
//                    finish();
//                }
//            }
//        }
    }

}