/*
 * Copyright 2017 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-09-18 23:47:01
 *
 * GitHub: https://github.com/GcsSloop
 * WeiBo: http://weibo.com/GcsSloop
 * WebSite: http://www.gcssloop.com
 */

package com.gcssloop.pagelayoutmanager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<AbstractMeetingHolder> {

    private static final String TAG = MyAdapter.class.getSimpleName();

    private static final int ITEM_TWO_MEMBER = 0;
    private static final int ITEM_TOP_LEFT = 1;
    private static final int ITEM_TOP_RIGHT = 2;
    private static final int ITEM_BOTTOM_LEFT = 3;
    private static final int ITEM_BOTTOM_RIGHT = 4;

    private static final int MARGIN_TOP_PX = UiUtils.dip2px(66, BaseApplication.getInstance());
    private static final int MARGIN_H_PX = UiUtils.dip2px(15, BaseApplication.getInstance());
    private static final int MARGIN_BOTTOM_PX = UiUtils.dip2px(66, BaseApplication.getInstance());
    private static final int MARGIN_SPAC_PX = UiUtils.dip2px(3, BaseApplication.getInstance());

    private List<RenderData> renderData = new ArrayList<>();

    private List<RenderData> fillEmptyData = new ArrayList<>();

    public List<RenderData> getData() {
        return renderData;
    }

    private void fillEmptyData(List<RenderData> dataList){
        fillEmptyData = new ArrayList<>(dataList);

        if (dataList.size() <= 1){
            return;
        }

        int pageSize = 4;
        // 远端用户数量
        int remoteSize = dataList.size() - 1;
        int temp = remoteSize % 4;
        if (temp != 0){
            for (int i = 0; i < pageSize - temp; i++){
                fillEmptyData.add(null);
            }
        }
    }

    public void setData(List<RenderData> data) {
        this.renderData = data;

        fillEmptyData(renderData);
    }

    @Override
    public int getItemViewType(int position) {
        int res = 0;
        if (position == 0) {
            res = ITEM_TWO_MEMBER;
        } else {
            int loc = (position - 1) % 4;
            Log.i(TAG, "getItemViewType:" + loc);
            switch (loc) {
                case 0:
                    res = ITEM_TOP_LEFT;
                    break;
                case 1:
                    res = ITEM_BOTTOM_LEFT;
                    break;
                case 2:
                    res = ITEM_TOP_RIGHT;
                    break;
                case 3:
                    res = ITEM_BOTTOM_RIGHT;
                    break;
                default:
                    break;
            }
        }
        return res;
    }

    @Override
    public AbstractMeetingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder:" + viewType);
        if (viewType == ITEM_TWO_MEMBER) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_two_member, parent, false);
            return new MeetingTwoMemberHolder(view);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.layout_tile_cell, parent, false);
            switch (viewType) {
                case ITEM_TOP_LEFT:
                    ((RecyclerView.LayoutParams) view.getLayoutParams()).setMargins(MARGIN_H_PX, MARGIN_TOP_PX, MARGIN_SPAC_PX, MARGIN_SPAC_PX);
                    break;
                case ITEM_TOP_RIGHT:
                    ((RecyclerView.LayoutParams) view.getLayoutParams()).setMargins(MARGIN_SPAC_PX, MARGIN_TOP_PX, MARGIN_H_PX, MARGIN_SPAC_PX);
                    break;
                case ITEM_BOTTOM_LEFT:
                    ((RecyclerView.LayoutParams) view.getLayoutParams()).setMargins(MARGIN_H_PX, MARGIN_SPAC_PX, MARGIN_SPAC_PX, MARGIN_BOTTOM_PX);
                    break;
                case ITEM_BOTTOM_RIGHT:
                    ((RecyclerView.LayoutParams) view.getLayoutParams()).setMargins(MARGIN_SPAC_PX, MARGIN_SPAC_PX, MARGIN_H_PX, MARGIN_BOTTOM_PX);
                    break;
                default:
                    break;
            }

            int width = (parent.getWidth() - (MARGIN_H_PX + MARGIN_SPAC_PX) * 2) / 2;
            int heiht = (parent.getHeight() - MARGIN_TOP_PX - MARGIN_BOTTOM_PX) / 2;
            view.getLayoutParams().width = width;
            view.getLayoutParams().height = heiht;

            return new MeetingTileCellHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final AbstractMeetingHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder = " + position);
        holder.update(fillEmptyData.get(position));
    }

    @Override
    public int getItemCount() {
        return fillEmptyData.size();
    }

}
