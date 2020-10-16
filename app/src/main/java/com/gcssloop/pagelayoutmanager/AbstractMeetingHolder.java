package com.gcssloop.pagelayoutmanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * meeting adapter base holder
 * @author lqk
 */
public abstract class AbstractMeetingHolder extends RecyclerView.ViewHolder {
    AbstractMeetingHolder(@NonNull View itemView) {
        super(itemView);
        init(itemView);
    }

    /**
     * init ui
     * @param view view
     */
    public abstract void init(View view);

    /**
     * 更新数据
     * @param data data
     */
    public abstract void update(RenderData data);

    /**
     * 回收video view
     */
    public abstract void recycle();

    public abstract void bindView();

    public abstract void unBindView();

}
