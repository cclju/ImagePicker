package com.lzy.imagepickerdemo.wxdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.CommonImagePreviewActivity;
import com.lzy.imagepickerdemo.R;
import com.lzy.imagewatcher.ImageWatcherHelper;
import com.lzy.imagewatcher.util.CustomDotIndexProvider;
import com.lzy.imagewatcher.util.SimpleLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ================================================
 * https://github.com/iielse/ImageWatcher
 *
 * 使用本地资源来模拟聊天界面的图片、视屏预览测试
 * ================================================
 */
public class WxDemoWithImageWatcherActivity extends Activity {


    private List<ImageItem> dataList = new ArrayList<>();
    private SparseArray<ImageView> mapping;

    private ImageWatcherHelper iwHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        boolean isTranslucentStatus = false;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//            isTranslucentStatus = true;
//        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_watcher);

        String u1 = "/storage/emulated/0/testcjw/test01.jpg";
        ImageItem item01 = new ImageItem();
        item01.path = u1;
        item01.timestamp = 1532501644;

        String u2 = "/storage/emulated/0/testcjw/test02.jpg";
        ImageItem item02 = new ImageItem();
        item02.path = u2;
        item02.timestamp = 1532501644;

        String u3 = "/storage/emulated/0/testcjw/test03.jpg";
        ImageItem item03 = new ImageItem();
        item03.path = u3;
        item03.timestamp = 1532501644;

        String u4 = "/storage/emulated/0/testcjw/test04.jpg";
        ImageItem item04 = new ImageItem();
        item04.path = u4;
        item04.timestamp = 1532501644;

        String u5 = "/storage/emulated/0/testcjw/test05.jpg";
        ImageItem item05 = new ImageItem();
        item05.path = u5;
        item05.timestamp = 1532501644;

        String u6 = "/storage/emulated/0/testcjw/test06.jpg";
        ImageItem item06 = new ImageItem();
        item06.path = u6;
        item06.timestamp = 1533501644;

        String u7 = "/storage/emulated/0/testcjw/test07.jpg";
        ImageItem item07 = new ImageItem();
        item07.path = u7;
        item07.timestamp = 1534958457;

        String u8 = "/storage/emulated/0/testcjw/test08.jpg";
        ImageItem item08 = new ImageItem();
        item08.path = u8;
        item08.timestamp = 1535958457;

        dataList.add(item01);
        dataList.add(item02);
        dataList.add(item03);
        dataList.add(item04);
        dataList.add(item05);
        dataList.add(item06);
        dataList.add(item07);
        dataList.add(item08);

        Glide.with(this).load(new File(u1)).into(((ImageView) findViewById(R.id.v1)));
        Glide.with(this).load(new File(u2)).into(((ImageView) findViewById(R.id.v2)));
        Glide.with(this).load(new File(u3)).into(((ImageView) findViewById(R.id.v3)));
        Glide.with(this).load(new File(u4)).into(((ImageView) findViewById(R.id.v4)));
        Glide.with(this).load(new File(u5)).into(((ImageView) findViewById(R.id.v5)));
        Glide.with(this).load(new File(u6)).into(((ImageView) findViewById(R.id.v6)));
        Glide.with(this).load(new File(u7)).into(((ImageView) findViewById(R.id.v7)));
        Glide.with(this).load(new File(u8)).into(((ImageView) findViewById(R.id.v8)));

        mapping = new SparseArray<>();
        mapping.put(0, (ImageView) findViewById(R.id.v1));
        mapping.put(1, (ImageView) findViewById(R.id.v2));
        mapping.put(2, (ImageView) findViewById(R.id.v3));
        mapping.put(3, (ImageView) findViewById(R.id.v4));
        mapping.put(4, (ImageView) findViewById(R.id.v5));
        mapping.put(5, (ImageView) findViewById(R.id.v6));
        mapping.put(6, (ImageView) findViewById(R.id.v7));
        mapping.put(7, (ImageView) findViewById(R.id.v8));

        View.OnClickListener showListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show((ImageView) v);
            }
        };
        findViewById(R.id.v1).setOnClickListener(showListener);
        findViewById(R.id.v2).setOnClickListener(showListener);
        findViewById(R.id.v3).setOnClickListener(showListener);
        findViewById(R.id.v4).setOnClickListener(showListener);
        findViewById(R.id.v5).setOnClickListener(showListener);
        findViewById(R.id.v6).setOnClickListener(showListener);
        findViewById(R.id.v7).setOnClickListener(showListener);
        findViewById(R.id.v8).setOnClickListener(showListener);

        //  **************  动态 addView   **************

        iwHelper = ImageWatcherHelper.with(this, new SimpleLoader()) // 一般来讲， ImageWatcher 需要占据全屏的位置
                .setIndexProvider(new CustomDotIndexProvider()); // 自定义index

//        Utils.fitsSystemWindows(isTranslucentStatus, findViewById(R.id.v_fit));
    }

    private void show(ImageView clickedImage) {
        // 注意，下标当然是从0开始的
//        iwHelper.show(clickedImage, mapping, dataList);

        // 图片、视频 大图预览
        CommonImagePreviewActivity.startActivity(this, dataList, 1);
    }

    @Override
    public void onBackPressed() {
        if (!iwHelper.handleBackPressed()) {
            super.onBackPressed();
        }
    }

    // 模拟聊天界面：图片、视频混合预览测试
    private void testChat() {

        List<ImageItem> imageItemList = new ArrayList<>();

        for (int i = 10; i > 0; i--) {

            long timestamp = 1532335011 - 30 * 24 * 3600 * i;
            int itemNum = new Random().nextInt(8) + 1; // 1-12
            Log.d("Picker", "itemNum: " + itemNum);
            for (int j = 0; j < itemNum; j++) {
                ImageItem item01 = new ImageItem();
                item01.path = "/storage/emulated/0/testcjw/test01.jpg";
                item01.timestamp = timestamp;
                imageItemList.add(item01);
            }

            ImageItem item03 = new ImageItem();
            item03.path = "/storage/emulated/0/testcjw/test05.jpg";
            item03.videoPath = "/storage/emulated/0/testcjw/test05video.mp4";
            item03.timestamp = timestamp;
            imageItemList.add(item03);

        }

        for (int i = 3; i > 0; i--) {
            ImageItem item01 = new ImageItem();
            item01.path = "/storage/emulated/0/testcjw/test01.jpg";
            item01.timestamp = 1532501644;
            imageItemList.add(item01);
        }


        int itemPos = 1;

        // 图片、视频 大图预览
        CommonImagePreviewActivity.startActivity(this, imageItemList, itemPos);
    }

}
