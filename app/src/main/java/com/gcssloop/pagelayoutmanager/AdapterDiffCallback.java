package com.gcssloop.pagelayoutmanager;

import android.support.v7.util.DiffUtil;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

public class AdapterDiffCallback extends DiffUtil.Callback {
    private static final String TAG = AdapterDiffCallback.class.getSimpleName();

    List<RenderData> mOldData;
    List<RenderData> mNewData;

    public AdapterDiffCallback(List<RenderData> mOldData, List<RenderData> mNewList) {
        this.mOldData = mOldData;
        this.mNewData = mNewList;
    }

    @Override
    public int getOldListSize() {
        return mOldData.size();
    }

    @Override
    public int getNewListSize() {
        return mNewData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        RenderData oldRender = mOldData.get(oldItemPosition);
        RenderData newRender = mNewData.get(newItemPosition);

        if (oldRender == null && newRender == null){
            return true;
        }

        if (oldRender == null || newRender == null){
            return false;
        }

        return oldRender.getPeerId() == newRender.getPeerId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        RenderData oldRender = mOldData.get(oldItemPosition);
        RenderData newRender = mNewData.get(newItemPosition);
        if (oldRender == null && newRender == null){
            return true;
        }

        if (oldRender == null || newRender == null){
            return false;
        }
        return oldRender.equals(newRender);
    }
}
