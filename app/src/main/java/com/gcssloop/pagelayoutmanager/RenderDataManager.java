package com.gcssloop.pagelayoutmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 数据管理
 * @author lqk
 */
public class RenderDataManager {

    private MyAdapter adapter;
    /**
     * 原始数据 服务器下发
     */
    private List<RenderData> renderData = new ArrayList<>();

    public RenderDataManager(MyAdapter adapter) {
        this.adapter = adapter;
    }

    public void add(RenderData data){
        if (renderData.isEmpty()){
            renderData.add(data);
            updateAdapter();
            return;
        }

        renderData.add(data);

        if (canJoinTwoMember()){
            renderData.get(0).setRemoteData(data);
        }

        updateAdapter();
    }

    /**
     * 用户离开房间不需要做取消订阅 直接删除就好
     * @param peerId
     */
    public void remove(int peerId){
        if (renderData.size() <= 1){
            return;
        }
        RenderData removeData = null;
        for (RenderData data : renderData) {
            if (data.getPeerId() == peerId){
                removeData = data;
                renderData.remove(data);
                break;
            }
        }
        // 没找到
        if (removeData == null){
            return;
        }

        if (isInTwoMember(removeData)){
            if (renderData.size() > 1){
                renderData.get(0).setRemoteData(renderData.get(1));
                // TODO 去订阅
            }else {
                renderData.get(0).setRemoteData(null);
            }
        }
        updateAdapter();
    }

    public void setStreamId(int peerId, String streamId, int type){
        for (int i = 0; i < renderData.size(); i++){
            RenderData data = renderData.get(i);
            if (data.getPeerId() == peerId){
                renderData.get(i).setStreamId(streamId, type);
                if (data.isLocal()){
                    // 本地
                }else if (data.isShow()){
                    // TODO remote当前在显示中，来了videoStream 去订阅
                }
                updateAdapter();
                break;
            }
        }
    }

    public void renderFirstFrame(int peerId, String streamId){
        for (int i = 0; i < renderData.size(); i++){
            RenderData data = renderData.get(i);
            if (data.getPeerId() == peerId){
                renderData.get(i).renderFirstFrame();
                updateAdapter();
                break;
            }
        }
    }


    private boolean isInTwoMember(RenderData data){
        return !renderData.isEmpty() && Objects.equals(renderData.get(0).getRemoteData(), data);
    }

    private boolean canJoinTwoMember(){
        return !renderData.isEmpty() && renderData.get(0).getRemoteData() == null;
    }


    private void updateAdapter(){
        adapter.updateData(renderData);
    }
}
