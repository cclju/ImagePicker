package com.lzy.imagepicker.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
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

/**
 * 加载相册 图片和视屏 的RecyclerView适配器
 *
 * 增加 Head 测试 利用 Decoration
 *
 *
 */

@Deprecated
public class CommonImageRecyclerAdapterWithHead2 extends RecyclerView.Adapter<ViewHolder> {

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
            this.images.clear();
            this.images.addAll(images);
        }
        notifyDataSetChanged();
    }

    /**
     * 构造方法
     */
    public CommonImageRecyclerAdapterWithHead2(Activity activity, ArrayList<ImageItem> images) {
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
        return new ImageViewHolder(mInflater.inflate(R.layout.picker_adapter_common_image_list_item, parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof ImageViewHolder){
            ((ImageViewHolder)holder).bind(position);
        }
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
        return images == null ? null : images.get(position);
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
                    if (listener != null) listener.onImageItemClick(rootView, imageItem, position);
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
                        imagePicker.addSelectedImageItem(position, imageItem, cbCheck.isChecked());
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

    public boolean hasHeaderText(int position) {
        ImageItem imageItem = images.get(position);
        return imageItem.groupPos == 0;
    }

    public boolean hasHeader(int position) {

        ImageItem item = getItem(position);
        if (item == null) {
            return false;
        }

        String curDateYM = TimeUtil.getDateYM(item.timestamp * 1000);
        long now = System.currentTimeMillis();
        if (position == 0) {

            if (TimeUtil.isSameWeek(now, item.timestamp * 1000)) {
                item.timeDisplay = "本周";
            } else {
                item.timeDisplay = curDateYM + "  1234567890";
            }

            item.groupId = 0;
            item.groupPos = 0;
            return true;
        }

        if (TimeUtil.isSameWeek(now, item.timestamp * 1000)) {
            item.timeDisplay = "本周";
        } else {
            item.timeDisplay = curDateYM + "  1234567890";
        }

        ImageItem itemPre = getItem(position - 1);
        if (!itemPre.timeDisplay.equals(item.timeDisplay)) {
            item.groupId = itemPre.groupId + 1;
            item.groupPos = 0;
            Log.d("Picker", "display: " + item.timeDisplay);
            return true;
        } else {
            item.groupId = itemPre.groupId;
            item.groupPos = itemPre.groupPos + 1;
            if (item.groupPos <= 3) {
                return true;
            }
        }

        return false;
    }

    //采用xml方式来实现ItemDecoration，可以更方便的定制ItemDecoration的内容，生成head布局
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderHolder(mInflater.inflate(R.layout.picker_adapter_common_item_decoration, parent, false));
    }

    //绑定head的数据
    public void onBindHeaderViewHolder(HeaderHolder viewholder, int position) {
        ImageItem imageItem = images.get(position);
        Log.d("Picker", "imageItem.groupPos: " + imageItem.groupPos + "  imageItem.timeDisplay: " + imageItem.timeDisplay);
        if (hasHeaderText(position)) {
            viewholder.tvTime.setText(imageItem.timeDisplay);
        } else {
            viewholder.tvTime.setText("");
        }
    }

    //获取每条数据属于哪一分组
    public int getHeaderId(int position) {
        return images.get(position).groupId;
    }


    public  class HeaderHolder extends RecyclerView.ViewHolder {
        TextView tvTime;

        public HeaderHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }


}
