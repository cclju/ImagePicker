package com.lzy.imagepicker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.lzy.imagepicker.R;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * 自定义播放器
 * 这里可以监听到视频播放的生命周期和播放状态
 * 所有关于视频的逻辑都应该写在这里
 */
public class CustomJZVideoPlayerStandard extends JZVideoPlayerStandard {

    public static final int ON_CONTROL_VIEW_DISMISS = 1001;

    public CustomJZVideoPlayerStandard(Context context) {
        super(context);
    }

    public CustomJZVideoPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private OnVideoTapListener listener;

    public interface OnVideoTapListener {

        void onVideoTap(int userAction);
    }

    public void setOnVideoTapListener(OnVideoTapListener listener) {
        this.listener = listener;
    }

    @Override
    public void init(Context context) {
        super.init(context);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
//        if (i == cn.jzvd.R.id.fullscreen) {
//            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
//                //click quit fullscreen
//            } else {
//                //click goto fullscreen
//            }
//        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.picker_custom_jz_layout_standard;
    }

    @Override
    public void onEvent(int type) {
        super.onEvent(type);

        Log.d("cjwtest", "onEvent type: " + type);
        if (listener != null) {
            listener.onVideoTap(type);
        }
    }

    @Override
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro, int thumbImg, int bottomPro, int retryLayout) {
        super.setAllControlsVisiblity(topCon, bottomCon, startBtn, loadingPro, thumbImg, bottomPro, retryLayout);

        bottomProgressBar.setVisibility(GONE);
    }

    @Override
    public void dissmissControlView() {
//        super.dissmissControlView();

        Log.d("cjwtest", "dissmissControlView");

        if (currentState != CURRENT_STATE_NORMAL
                && currentState != CURRENT_STATE_ERROR
                && currentState != CURRENT_STATE_AUTO_COMPLETE) {
            post(new Runnable() {
                @Override
                public void run() {
                    bottomContainer.setVisibility(View.INVISIBLE);
                    topContainer.setVisibility(View.INVISIBLE);
                    startButton.setVisibility(View.INVISIBLE);
                    if (clarityPopWindow != null) {
                        clarityPopWindow.dismiss();
                    }
//                    if (currentScreen != SCREEN_WINDOW_TINY) {
//                        bottomProgressBar.setVisibility(View.VISIBLE);
//                    }

                    listener.onVideoTap(ON_CONTROL_VIEW_DISMISS);
                }
            });
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouch(v, event);
    }

    @Override
    public void startVideo() {
        super.startVideo();
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
    }

    @Override
    public void onStateError() {
        super.onStateError();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
    }

    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();
    }

    @Override
    public void startWindowTiny() {
        super.startWindowTiny();
    }

}
