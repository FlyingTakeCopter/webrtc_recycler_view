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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gcssloop.pagelayoutmanager.gridpagersnaphelper.GridPagerSnapHelper;
//import com.gcssloop.widget.PagerConfig;
import com.gcssloop.widget.PagerGridLayoutManager;
//import com.gcssloop.widget.PagerGridSnapHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PagerGridLayoutManager
        .PageListener, RadioGroup.OnCheckedChangeListener {

    private int mRows = 2;
    private int mColumns = 2;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RenderDataManager dataManager;
//    private PagerGridLayoutManager mLayoutManager;
    private RadioGroup mRadioGroup;
    private TextView mPageTotal;        // 总页数
    private TextView mPageCurrent;      // 当前页数

    private int mTotal = 0;
    private int mCurrent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("GCS", "onCreate");

        mRadioGroup = (RadioGroup) findViewById(R.id.orientation_type);
        mRadioGroup.setOnCheckedChangeListener(this);

        mPageTotal = (TextView) findViewById(R.id.page_total);
        mPageCurrent = (TextView) findViewById(R.id.page_current);

//        mLayoutManager = new PagerGridLayoutManager(mRows, mColumns, PagerGridLayoutManager
//                .HORIZONTAL);


        // 系统带的 RecyclerView，无需自定义
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // 水平分页布局管理器
//        mLayoutManager.setPageListener(this);    // 设置页面变化监听器
//        mRecyclerView.setLayoutManager(mLayoutManager);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0){
                    return 2;
                }
                return 1;
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);

        // 设置滚动辅助工具

        GridPagerSnapHelper gridPagerSnapHelper = new GridPagerSnapHelper(2, 2);
//        gridPagerSnapHelper.setRow(2).setColumn(2);
        gridPagerSnapHelper.attachToRecyclerView(mRecyclerView);
//        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
//        linearSnapHelper.attachToRecyclerView(mRecyclerView);
//        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
//        pagerSnapHelper.attachToRecyclerView(mRecyclerView);
//        MyGridSnapHelper gridPagerSnapHelper = new MyGridSnapHelper(2, 2);
//        gridPagerSnapHelper.attachToRecyclerView(mRecyclerView);
//        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
//        pageSnapHelper.attachToRecyclerView(mRecyclerView);

        // 如果需要查看调试日志可以设置为true，一般情况忽略即可
//        PagerConfig.setShowLog(true);
//        PagerConfig.setFlingThreshold(2000);
//        PagerConfig.setMillisecondsPreInch(100f);
// 显示监听
        ItemVisibleHelper itemVisibleHelper = new ItemVisibleHelper();
        itemVisibleHelper.setOnScrollStatusListener(new ItemVisibleHelper.OnScrollStatusListener() {
            @Override
            public void onEnter(int position) {
                Log.i("itemVisibleHelper", "enter: " + position);

            }

            @Override
            public void onExit(int position) {
                Log.i("itemVisibleHelper", "exit: " + position);
            }
        });
        itemVisibleHelper.attachToRecycler(mRecyclerView);

        // 使用原生的 Adapter 即可
        mAdapter = new MyAdapter();
        dataManager = new RenderDataManager(mAdapter);
        dataManager.add(new RenderData(this, -1, "local", true));
//        mAdapter.setData(mAdapter.getData());
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override public void onChanged() {
                super.onChanged();
                int count = mAdapter.getItemCount();

            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(null);
//        mRecyclerView.addOnScrollListener(recyclerScrollListener);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                LinearLayoutManager
//                recyclerView.getLayoutManager().find
//            }
//        });
    }

//    private RecyclerView.OnScrollListener recyclerScrollListener=new RecyclerView.OnScrollListener(){
//        //RecyclerVew
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
//        }
//
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//            LinearLayoutManager layoutManager= (LinearLayoutManager) recyclerView.getLayoutManager();
//            if (layoutManager!=null){
//                int firstVisible = layoutManager.findFirstVisibleItemPosition();
//                int lastVisible = layoutManager.findLastVisibleItemPosition();
//
//                int visibleItemCount = lastVisible - firstVisible;
//                if (lastVisible == 0) {
//                    visibleItemCount = 0;
//                }
//                if (visibleItemCount != 0) {
//                    Log.i("visibleItemCount", "first = " + firstVisible + " last = " + lastVisible);
////                    dealScrollEvent(firstVisible, lastVisible);
//                }
//            }
//        }
//    };

    @Override public void onPageSizeChanged(int pageSize) {
        mTotal = pageSize;
        Log.e("TAG", "总页数 = " + pageSize);
        mPageTotal.setText("共 " + pageSize + " 页");
    }

    @Override public void onPageSelect(int pageIndex) {
        mCurrent = pageIndex;
        Log.e("TAG", "选中页码 = " + pageIndex);
        mPageCurrent.setText("第 " + (pageIndex + 1) + " 页");
    }

    int num = 1;
    public void addOne(View view) {
        dataManager.add(new RenderData(this, num, num + "", false));
        num++;
    }

    public void removeOne(View view) {
        dataManager.remove(1);
    }

    public void addMore(View view) {
        dataManager.setStreamId(1, "123", RenderData.STREAM_VIDEO);
    }

    @SuppressLint("WrongConstant")
    @Override public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//        int type = -1;
//        if (checkedId == R.id.type_horizontal) {
//            type = mLayoutManager.setOrientationType(PagerGridLayoutManager.HORIZONTAL);
//        } else if (checkedId == R.id.type_vertical) {
//            type = mLayoutManager.setOrientationType(PagerGridLayoutManager.VERTICAL);
//        } else {
//            throw new RuntimeException("不支持的方向类型");
//        }

//        Log.i("GCST", "type == " + type);
    }

    public void prePage(View view) {
//        mLayoutManager.prePage();
    }

    public void nextPage(View view) {
//        mLayoutManager.nextPage();
    }

    public void smoothPrePage(View view) {
//        mLayoutManager.smoothPrePage();
    }

    public void smoothNextPage(View view) {
//        mLayoutManager.smoothNextPage();
    }

    public void firstPage(View view) {
        mRecyclerView.smoothScrollToPosition(0);
    }

    public void lastPage(View view) {
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount()-1);
    }

}
