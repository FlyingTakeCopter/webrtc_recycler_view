package com.gcssloop.pagelayoutmanager.gridpagersnaphelper;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Created by hanhailong on 2017/8/20.
 */

public class GridPagerSnapHelper extends SnapHelper {
    private static final String TAG = GridPagerSnapHelper.class.getSimpleName();

    private static final int DEFAULT_ROW = 1;
    private static final int DEFAULT_COLUMN = 1;

    private int mRow = DEFAULT_ROW;
    private int mColumn = DEFAULT_COLUMN;
    private int mPageCount = mRow * mColumn;

    private static final int MAX_SCROLL_ON_FLING_DURATION = 100; // ms

    public GridPagerSnapHelper(int mRow, int mColumn) {
        this.mRow = mRow;
        this.mColumn = mColumn;
        mPageCount = mRow * mColumn;
    }

    // Orientation helpers are lazily created per LayoutManager.
    @Nullable
    private OrientationHelper mVerticalHelper;
    @Nullable
    private OrientationHelper mHorizontalHelper;

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        int[] out = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToCenter(layoutManager, targetView,
                    getHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }

        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToCenter(layoutManager, targetView,
                    getVerticalHelper(layoutManager));
        } else {
            out[1] = 0;
        }
        Log.i("calculateDisToFinalSnap", out[0] + "_" + out[1]);
        return out;
    }

    private int getPageStartByItemIndex(int index){
        if (index == 0){
            return 0;
        }
        return ((pageIndex(index) - 1) * mPageCount) + 1;
    }

    private int distanceToCenter(@NonNull RecyclerView.LayoutManager layoutManager,
                                 @NonNull View targetView, OrientationHelper helper) {
        if (layoutManager.canScrollHorizontally()) {
            int totalWidth = mRecyclerView.getWidth();

            int columnWidth = totalWidth / mColumn;

            int position = layoutManager.getPosition(targetView);
//            int pageIndex = pageIndex(position);
//
//            int currentPageStart = pageIndex * countOfpage(position);
            int currentPageStart = getPageStartByItemIndex(position);

            int distance = ((position - currentPageStart) / mRow) * columnWidth;

            final int childStart = helper.getDecoratedStart(targetView);
            return childStart - distance;
        } else {//数值方向
            int totalHeight = mRecyclerView.getHeight();

            int rowHeight = totalHeight / mRow;

            int position = layoutManager.getPosition(targetView);
//            int pageIndex = pageIndex(position);
//
//            int currentPageStart = pageIndex * countOfpage(position);

            int currentPageStart = getPageStartByItemIndex(position);

            int distance = ((position - currentPageStart) / mColumn) * rowHeight;

            final int childStart = helper.getDecoratedStart(targetView);
            return childStart - distance;
        }
    }


    @Nullable
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return findCenterView(layoutManager, getVerticalHelper(layoutManager));
        } else if (layoutManager.canScrollHorizontally()) {
            return findCenterView(layoutManager, getHorizontalHelper(layoutManager));
        }
        return null;
    }

    /**
     * get the page of position
     *
     * @param position
     * @return
     */
    private int pageIndex(int position) {
        if (position == 0){
            return 0;
        }
        return ((position - 1) / mPageCount) + 1;
    }

    /**
     * the total count of per page
     *
     * @return
     */
    private int countOfpage(int position) {
        if (position == 0){
            return 1;
        }
        return mPageCount;
    }

    /**
     * 获取手指抬起后要对齐的view
     * Return the child view that is currently closest to the center of this parent.
     *
     * @param layoutManager The {@link RecyclerView.LayoutManager} associated with the attached
     *                      {@link RecyclerView}.
     * @param helper        The relevant {@link android.support.v7.widget.OrientationHelper} for the attached {@link RecyclerView}.
     * @return the child view that is currently closest to the center of this parent.
     */
    @Nullable
    private View findCenterView(RecyclerView.LayoutManager layoutManager,
                                OrientationHelper helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }

        View closestChild = null;
        final int center;
        if (layoutManager.getClipToPadding()) {
            center = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
        } else {
            center = helper.getEnd() / 2;
        }

        int absClosest = Integer.MAX_VALUE;

        for (int i = 0; i < childCount; i++) {
            final View child = layoutManager.getChildAt(i);
            int childCenter = helper.getDecoratedStart(child)
                    + (helper.getDecoratedMeasurement(child) / 2);
            // 第一个显示超过一半直接切换
            if (layoutManager.getPosition(child) == 0 && childCenter > 0){
                closestChild = child;
                break;
            }
            int absDistance = Math.abs(childCenter - center);

            /** if child center is closer than previous closest, set it as closest  **/
            if (absDistance < absClosest) {
                absClosest = absDistance;
                closestChild = child;
            }
        }

        if (closestChild == null){
            return layoutManager.getChildAt(0);
        }
        int childPosition = layoutManager.getPosition(closestChild);
        int pageStartIndex = getPageStartByItemIndex(childPosition);
        Log.i(TAG, "findCenterView:" + pageStartIndex);
        return layoutManager.getChildAt(pageStartIndex);
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX,
                                      int velocityY) {
        final int itemCount = layoutManager.getItemCount();
        if (itemCount == 0) {
            return RecyclerView.NO_POSITION;
        }

        View mStartMostChildView = null;
        if (layoutManager.canScrollVertically()) {
            mStartMostChildView = findStartView(layoutManager, getVerticalHelper(layoutManager));
        } else if (layoutManager.canScrollHorizontally()) {
            mStartMostChildView = findStartView(layoutManager, getHorizontalHelper(layoutManager));
        }

        if (mStartMostChildView == null) {
            return RecyclerView.NO_POSITION;
        }
        final int centerPosition = layoutManager.getPosition(mStartMostChildView);
        if (centerPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION;
        }

        final boolean forwardDirection;
        if (layoutManager.canScrollHorizontally()) {
            forwardDirection = velocityX > 0;
        } else {
            forwardDirection = velocityY > 0;
        }
        boolean reverseLayout = false;
        if ((layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            RecyclerView.SmoothScroller.ScrollVectorProvider vectorProvider =
                    (RecyclerView.SmoothScroller.ScrollVectorProvider) layoutManager;
            PointF vectorForEnd = vectorProvider.computeScrollVectorForPosition(itemCount - 1);
            if (vectorForEnd != null) {
                reverseLayout = vectorForEnd.x < 0 || vectorForEnd.y < 0;
            }
        }

//        int pageIndex = pageIndex(centerPosition);

        int currentPageStart = getPageStartByItemIndex(centerPosition);
        int countOfPage = countOfpage(centerPosition);
//
//        int currentPageStart = pageIndex * countOfPage;

        return reverseLayout
                ? (forwardDirection ? currentPageStart - countOfPage : currentPageStart)
                : (forwardDirection ? currentPageStart + countOfPage : (currentPageStart + countOfPage - 1));
    }

    @Nullable
    private View findStartView(RecyclerView.LayoutManager layoutManager,
                               OrientationHelper helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }

        View closestChild = null;
        int startest = Integer.MAX_VALUE;

        for (int i = 0; i < childCount; i++) {
            final View child = layoutManager.getChildAt(i);
            int childStart = helper.getDecoratedStart(child);

            /** if child is more to start than previous closest, set it as closest  **/
            if (childStart < startest) {
                startest = childStart;
                closestChild = child;
            }
        }

        return closestChild;
    }

    @Override
    protected LinearSmoothScroller createSnapScroller(final RecyclerView.LayoutManager layoutManager) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return null;
        }
        return new LinearSmoothScroller(mRecyclerView.getContext()) {
            @Override
            protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
                int[] snapDistances = calculateDistanceToFinalSnap(mRecyclerView.getLayoutManager(),
                        targetView);
                final int dx = snapDistances[0];
                final int dy = snapDistances[1];
                final int time = calculateTimeForDeceleration(Math.max(Math.abs(dx), Math.abs(dy)));
                if (time > 0) {
                    action.update(dx, dy, time, mDecelerateInterpolator);
                }
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }

            @Override
            protected int calculateTimeForScrolling(int dx) {
                return Math.min(MAX_SCROLL_ON_FLING_DURATION, super.calculateTimeForScrolling(dx));
            }

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return null;
            }
        };
    }

    @NonNull
    private OrientationHelper getVerticalHelper(@NonNull RecyclerView.LayoutManager layoutManager) {
        if (mVerticalHelper == null || mVerticalHelper.mLayoutManager != layoutManager) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return mVerticalHelper;
    }

    @NonNull
    private OrientationHelper getHorizontalHelper(
            @NonNull RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null || mHorizontalHelper.mLayoutManager != layoutManager) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }
}
