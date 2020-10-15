package com.gcssloop.pagelayoutmanager;

import android.content.Context;
import android.text.TextUtils;
import android.view.SurfaceView;

import java.util.Objects;

/**
 * 次数据修改一定要在 一个子线程中
 */
public class RenderData {
    public static final int STREAM_VIDEO = 0;
    public static final int STREAM_AUDIO = 1;
    /**
     * peer
     */
    private int peerId;
    /**
     * 视频流ID
     */
    private String streamIdV;
    /**
     * 音频流ID
     */
    private String streamIdA;
    /**
     * 是否是本地视频
     */
    private boolean isLocal;
    /**
     * local 持有一个远端的用户数据,用于大小屏显示,远端用户为null
     */
    private RenderData remoteData;
    /**
     * nick
     */
    private String name;

    private boolean isShow;

    private SurfaceView videoView;
    public int voice;

    private boolean renderFirstFrame;

    public static final int UNSUBSCRIBE = 0;
    public static final int SUBSCRIBED = 1;
    public static final int SUBSCRIBING = 2;

    /**
     * 订阅状态
     */
    private int videoState = UNSUBSCRIBE;

    public RenderData(Context context, int peerId, String nickName, boolean isLocal) {
        this.peerId = peerId;
        this.name = nickName;
        this.isLocal = isLocal;
        videoView = new SurfaceView(context);
    }

    public void release() {
//        if (videoView != null) {
//            videoView.release();
//            videoView = null;
//        }
    }

    public int getPeerId() {
        return peerId;
    }

    public String getName() {
        return name;
    }

    public String getVideoStreamId() {
        return streamIdV;
    }

    public String getAudioStreamId() {
        return streamIdA;
    }

    public void setStreamId(String streamId, int type) {
        if (type == STREAM_VIDEO) {
            this.streamIdV = streamId;
            renderFirstFrame = false;
            videoState = UNSUBSCRIBE;
//            muteVideo = !TextUtils.isEmpty(streamId);
        } else if (type == STREAM_AUDIO) {
            this.streamIdA = streamId;
//            muteAudio = !TextUtils.isEmpty(streamId);
        } else {
            throw new RuntimeException("unknow stream type");
        }

    }

    public SurfaceView getVideoView() {
        return videoView;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public boolean isRenderFirstFrame() {
        return renderFirstFrame;
    }

    public void setSubscribeState(int state){
        videoState = state;
    }

    public void renderFirstFrame() {
        // 只有处于订阅中的状态时，才能设置true
        if (isLocal){
            renderFirstFrame = true;
        }else if (videoState == SUBSCRIBING){
            renderFirstFrame = true;
            videoState = SUBSCRIBED;
        }
    }

    public boolean hasVideo(){
        return !TextUtils.isEmpty(streamIdV);
    }

    public boolean hasAudio(){
        return !TextUtils.isEmpty(streamIdA);
    }

    public int getSubscribeState() {
        return videoState;
    }

    public RenderData getRemoteData() {
        return remoteData;
    }

    public void setRemoteData(RenderData remoteData) {
        this.remoteData = remoteData;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenderData that = (RenderData) o;
        return peerId == that.peerId &&
                voice == that.voice &&
                renderFirstFrame == that.renderFirstFrame &&
                videoState == that.videoState &&
                Objects.equals(streamIdV, that.streamIdV) &&
                Objects.equals(streamIdA, that.streamIdA) &&
                Objects.equals(remoteData, that.remoteData) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(peerId, streamIdV, streamIdA, remoteData, name, voice, renderFirstFrame, videoState);
    }
}

