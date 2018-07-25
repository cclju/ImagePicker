package com.lzy.imagepicker.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.R;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.util.TimeUtil;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.SuperCheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载相册 图片和视屏 的RecyclerView适配器
 *
 * 增加 Head 测试，需要预先设置数据
 *
 * GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
 * layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
        if (mRecyclerAdapter.isHeader(position)) {
        return 4;
        }
        return 1;
        }
    });
 *
 */

public class CommonImageRecyclerAdapterWithHead extends RecyclerView.Adapter<ViewHolder> {


    public static final int ITEM_TYPE_HEADER = 1;  //第一个条目是时间
    public static final int ITEM_TYPE_NORMAL = 2;  //第一个条目不是时间
    private ImagePicker imagePicker;
    private Activity mActivity;
    private ArrayList<ImageItem> images;       //当前需要显示的所有的图片数据
    private ArrayList<ImageItem> mSelectedImages; //全局保存的已经选中的图片数据
    private int mImageSize;               //每个条目的大小
    private LayoutInflater mInflater;
    private OnImageItemClickListener listener;   //图片被点击的监听

    public void setOnImageItemClickListener(OnImageItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnImageItemClickListener {
        void onImageItemClick(View view, ImageItem imageItem, int position);
    }

    public void refreshData(ArrayList<ImageItem> images) {
        if (images == null || images.size() == 0) {
            this.images = new ArrayList<>();
        } else {

            // 原始数据作转换，便于展示
            // 默认传进来时间从过去到现在排序的，这里添加 type head 数据来调整数据源
            List<ImageItem> handleImages = new ArrayList<>();
            long now = System.currentTimeMillis();

            // for only one
            if (images.size() == 1) {

                ImageItem item = images.get(0);
                String curDateYM = TimeUtil.getDateYM(item.timestamp * 1000);

                // add header
                if (TimeUtil.isSameWeek(now, item.timestamp * 1000)) {

                    curDateYM = "本周";
                }
                ImageItem head = new ImageItem();
                head.viewType = ITEM_TYPE_HEADER;
                head.timeDisplay = curDateYM;
                handleImages.add(head);

                // add item
                item.viewType = ITEM_TYPE_NORMAL;
                item.groupId = 0;
                handleImages.add(item);

            } else {

                // for more than one
                ImageItem itemPre = null;
                for (int i = 0; i < images.size(); i++) {

                    ImageItem item = images.get(i);
                    String curDateYM = TimeUtil.getDateYM(item.timestamp * 1000);
                    if (i == 0) {

                        // add header
                        if (TimeUtil.isSameWeek(now, item.timestamp * 1000)) {

                            curDateYM = "本周";
                        }
                        ImageItem head = new ImageItem();
                        head.viewType = ITEM_TYPE_HEADER;
                        head.timeDisplay = curDateYM;
                        handleImages.add(head);

                        // add item
                        item.viewType = ITEM_TYPE_NORMAL;
                        item.groupId = 0;
                        handleImages.add(item);

                        itemPre = item;

                        continue;
                    }

                    String preDateYM = TimeUtil.getDateYM(itemPre.timestamp * 1000);
                    if (!preDateYM.equals(curDateYM)) {

                        if (TimeUtil.isSameWeek(now, item.timestamp * 1000)) {

                            curDateYM = "本周";
                        }

                        // add header
                        ImageItem head = new ImageItem();
                        head.viewType = ITEM_TYPE_HEADER;
                        head.timeDisplay = curDateYM;
                        handleImages.add(head);

                        item.groupId = itemPre.groupId + 1;
                    } else {

                        item.groupId = itemPre.groupId;
                    }

                    // add item
                    item.viewType = ITEM_TYPE_NORMAL;
                    handleImages.add(item);

                    itemPre = item;
                }

            }

            this.images.clear();
            this.images.addAll(handleImages);
        }
        notifyDataSetChanged();
    }

    /**
     * 构造方法
     */
    public CommonImageRecyclerAdapterWithHead(Activity activity, ArrayList<ImageItem> images) {
        this.mActivity = activity;
        if (images == null || images.size() == 0) this.images = new ArrayList<>();
        else this.images = images;

        mImageSize = Utils.getImageItemWidth(mActivity);
        imagePicker = ImagePicker.getInstance();
        mSelectedImages = imagePicker.getSelectedImages();
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER){
            return new HeaderHolder(mInflater.inflate(R.layout.picker_adapter_common_item_decoration, parent, false));
        }
        return new ImageViewHolder(mInflater.inflate(R.layout.picker_adapter_common_image_list_item, parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder){
            ((HeaderHolder)holder).bind(position);
        }else if (holder instanceof ImageViewHolder){
            ((ImageViewHolder)holder).bind(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return hasHeader(position) ? ITEM_TYPE_HEADER : ITEM_TYPE_NORMAL;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    public ImageItem getItem(int position) {
        return images.get(position);
    }

    // 返回原始数据对应的位置
    public int getOriginPosition(int position) {
        if (images == null || images.size() == 0) {
            return -1;
        }

        int groupSize = images.get(position).groupId + 1;
        return position - groupSize;
    }

    private class ImageViewHolder extends ViewHolder{

        View rootView;
        ImageView ivThumb;
        ImageView ivVideoPlay;
        View mask;
        View checkView;
        SuperCheckBox cbCheck;


        ImageViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            ivThumb = (ImageView) itemView.findViewById(R.id.iv_thumb);
            ivVideoPlay = (ImageView) itemView.findViewById(R.id.iv_video_play);
            mask = itemView.findViewById(R.id.mask);
            checkView=itemView.findViewById(R.id.checkView);
            cbCheck = (SuperCheckBox) itemView.findViewById(R.id.cb_check);
            itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize)); //让图片是个正方形
        }

        void bind(final int position){
            final ImageItem imageItem = getItem(position);
            ivThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // listener thumb click
                    if (listener != null) listener.onImageItemClick(rootView, imageItem, getOriginPosition(position));
                }
            });

            ivVideoPlay.setVisibility(imageItem.isVideo() ? View.VISIBLE : View.GONE);

            checkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cbCheck.setChecked(!cbCheck.isChecked());
                    int selectLimit = imagePicker.getSelectLimit();
                    if (cbCheck.isChecked() && mSelectedImages.size() >= selectLimit) {
                        Toast.makeText(mActivity.getApplicationContext(), mActivity.getString(R.string.ip_select_limit, selectLimit), Toast.LENGTH_SHORT).show();
                        cbCheck.setChecked(false);
                        mask.setVisibility(View.GONE);
                    } else {
                        // listener check box click
                        imagePicker.addSelectedImageItem(getOriginPosition(position), imageItem, cbCheck.isChecked());
                        mask.setVisibility(View.VISIBLE);
                    }
                }
            });
            //根据是否多选，显示或隐藏checkbox
            if (imagePicker.isMultiMode()) {
                cbCheck.setVisibility(View.VISIBLE);
                checkView.setVisibility(View.VISIBLE);
                boolean checked = mSelectedImages.contains(imageItem);
                if (checked) {
                    mask.setVisibility(View.VISIBLE);
                    cbCheck.setChecked(true);
                } else {
                    mask.setVisibility(View.GONE);
                    cbCheck.setChecked(false);
                }
            } else {
                cbCheck.setChecked(false);
                mask.setVisibility(View.GONE);
                checkView.setVisibility(View.GONE);
            }
            imagePicker.getImageLoader().displayImage(mActivity, imageItem.path, ivThumb, mImageSize, mImageSize); //显示图片
        }

    }

    public boolean hasHeader(int position) {
        if (images == null || images.size() == 0) {
            return false;
        }

        return images.get(position).viewType == ITEM_TYPE_HEADER;
    }

    public  class HeaderHolder extends ViewHolder {

        View mItemView;
        TextView tvTime;

        public HeaderHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }

        void bind(int position) {
            ImageItem imageItem = getItem(position);
            tvTime.setText(imageItem.timeDisplay);
        }
    }
}
