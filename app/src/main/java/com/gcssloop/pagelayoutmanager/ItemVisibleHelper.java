package com.gcssloop.pagelayoutmanager;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * recyclerView item 显示监听
 * @author lqk
 */
class ItemVisibleHelper {
    private OnScrollStatusListener listener;

    void setOnScrollStatusListener(OnScrollStatusListener listener) {
        this.listener = listener;
    }

    public interface OnScrollStatusListener{
        /**
         * 进入屏幕
         * @param position pos
         */
        void onEnter(int position);

        /**
         * 离开屏幕
         * @param position pos
         */
        void onExit(int position);
    }

    void attachToRecycler(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(recyclerScrollListener);
    }
    
    private RecyclerView.OnScrollListener recyclerScrollListener=new RecyclerView.OnScrollListener(){
        List<Integer> visibleList = new ArrayList<>(6);
        List<Integer> temp = new ArrayList<>(6);

        //RecyclerVew
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager= (LinearLayoutManager) recyclerView.getLayoutManager();

            if (layoutManager!=null){
                int childCount = layoutManager.getChildCount();
                Log.i("OnScrollListener", "childCount = " + childCount);
                // 统计出新的visible列表
                temp.clear();
                for (int i = 0; i < childCount; i++){
                    final View child = layoutManager.getChildAt(i);
                    temp.add(layoutManager.getPosition(child));
                }
                // 找出离开的
                for (Integer index : visibleList) {
                    if (!temp.contains(index)){
                        if (listener != null){
                            listener.onExit(index);
                        }
                    }
                }
                // 找出新加入的
                for (Integer index : temp) {
                    if (!visibleList.contains(index)){
                        if (listener != null){
                            listener.onEnter(index);
                        }
                    }
                }

                visibleList.clear();
                visibleList.addAll(temp);
            }
        }
    };
}
