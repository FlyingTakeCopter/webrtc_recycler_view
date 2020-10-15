package com.gcssloop.pagelayoutmanager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class MemberVideoView extends RelativeLayout {
    private static final String TAG = MemberVideoView.class.getSimpleName();

    private Context context;

    /**
     * video rect
     */
    RelativeLayout rlVideoView;
    /**
     * 除了视频以外其他的
     */
    RelativeLayout rlCover;
    /**
     * 无视频头像
     */
    RelativeLayout rlHead;
    /**
     * loading
     */
    ProgressBar loading;

    private boolean isBigView;

    RenderData data;

//    JRTCVideoView videoView;

    public MemberVideoView(Context context) {
        super(context);
        init(context);
    }

    public MemberVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MemberVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MemberVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_member_view, this, true);
        rlVideoView = findViewById(R.id.rl_video_view);
        rlCover = findViewById(R.id.rl_cover);
        rlHead = findViewById(R.id.rl_head);
        loading = findViewById(R.id.progressBar);
    }

    public void update(RenderData data) {
        this.data = data;
        bindVideo();
        // 视频
        if (data.hasVideo()) {
            // 有视频 没有第一帧
            if (data.isRenderFirstFrame()) {
                rlHead.setVisibility(GONE);
                loading.setVisibility(GONE);
            } else {
                rlHead.setVisibility(VISIBLE);
                loading.setVisibility(VISIBLE);
            }
        } else {
            // 没有视频
            rlHead.setVisibility(VISIBLE);
            loading.setVisibility(GONE);
        }
    }

    public void recycle() {

    }

    public void setBigView(boolean bigView) {
        isBigView = bigView;
    }

    public void bindVideo() {
        try {
            if (data == null) {
                return;
            }
//            SurfaceView videoView = data.getVideoView();
//            if (videoView.getParent() != null) {
//                if (videoView.getParent() == rlVideoView) {
//                    // 绑定的就是当前
//                    return;
//                }
//                // 清除自己之前绑定过的holder
//                ((ViewGroup) videoView.getParent()).removeView(videoView);
//            }
//            // 清空当前holder绑定的video
//            rlVideoView.removeAllViews();
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            rlVideoView.addView(videoView, 0, params);
//            if (!isBigView) {
//                videoView.setZOrderOnTop(true);
//                videoView.setZOrderMediaOverlay(true);
//            } else {
//                videoView.setZOrderMediaOverlay(false);
//            }
//            rlCover.bringToFront();
        } catch (Exception e) {
            Log.e(TAG, "bindVideo error " + e.toString());
        }
    }

    public void unBindVideo(){
//        try {
//            if (data == null){
//                return;
//            }
//            rlVideoView.removeAllViews();
//        }catch (Exception e){
//            LogUtil.e(TAG, "bindVideo error " + e.toString());
//        }
    }
}
