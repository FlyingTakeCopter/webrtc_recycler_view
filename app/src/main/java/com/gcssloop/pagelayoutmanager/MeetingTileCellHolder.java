package com.gcssloop.pagelayoutmanager;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * tile cell holder
 *
 * @author lqk
 */
public class MeetingTileCellHolder extends AbstractMeetingHolder {
    private static final String TAG = MeetingTileCellHolder.class.getSimpleName();
    /**
     * video rect
     */
    private RelativeLayout rlVideoView;
    /**
     * 除了视频以外其他的
     */
    private RelativeLayout rlCover;
    /**
     * 无视频头像
     */
    private RelativeLayout rlHead;
    /**
     * loading
     */
    private ProgressBar loading;
    /**
     * nick
     */
    private TextView tvNickName;
    /**
     * audio
     */
    private ImageView ivAudio;

    private RenderData data;

    MeetingTileCellHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void init(View view) {
        rlVideoView = view.findViewById(R.id.rl_video_view);
        rlCover = view.findViewById(R.id.rl_cover);
        rlHead = view.findViewById(R.id.rl_head);
        loading = view.findViewById(R.id.progressBar);
        tvNickName = view.findViewById(R.id.tv_nick);
        ivAudio = view.findViewById(R.id.iv_audio);
    }

    @Override
    public void update(RenderData data) {
        if (data.isLocal()) {
            throw new RuntimeException("MeetingTileCellHolder update error: is local");
        }
        this.data = data;
        bindView();
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

        // 音频
        if (data.hasAudio()) {
            ivAudio.setBackground(itemView.getResources().getDrawable(R.mipmap.rtc_audio));
        } else {
            ivAudio.setBackground(itemView.getResources().getDrawable(R.mipmap.rtc_audio_unaviable));
        }
        // 音量

        // 昵称
        tvNickName.setText(data.getName());
    }

    @Override
    public void recycle() {

    }

    @Override
    public void bindView() {
        try {
            if (data == null) {
                throw new NullPointerException("holder 不存在");
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
//            videoView.setZOrderOnTop(true);
//            videoView.setZOrderMediaOverlay(true);
//            rlCover.bringToFront();
        } catch (Exception e) {
            Log.e(TAG, "addView error " + e.toString());
        }
    }

    @Override
    public void unBindView() {
//        try {
//            if (data == null) {
//                return;
//            }
//            rlVideoView.removeAllViews();
//        } catch (Exception e) {
//            LogUtil.e(TAG, "bindVideo error " + e.toString());
//        }
    }
}
