package com.tianyu.banner.banner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.lang.reflect.Field;


/**
 * 改变 LinearLayoutManager 的切换速度
 * 运用反射
 */
public class ScrollSpeedManger extends LinearLayoutManager {
    private TyBanner banner;

    public ScrollSpeedManger(TyBanner banner, LinearLayoutManager linearLayoutManager) {
        super(banner.getContext(), linearLayoutManager.getOrientation(), false);
        this.banner = banner;
    }

    /**
     * 重写滑动的监听
     *
     * @param recyclerView
     * @param state
     * @param position
     */
    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            protected int calculateTimeForDeceleration(int dx) {
                return banner.getScrollTime();
            }
        };
        //设置滑动的时间
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    public static void reflectLayoutManager(TyBanner banner) {
        if (banner.getScrollTime() < 100) return;
        try {
            //先找到 RecycleView
            ViewPager2 viewPager2 = banner.getViewPager2();
            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
            //OVER_SCROLL_NEVER 不允许边界回弹
            recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

            ScrollSpeedManger speedManger = new ScrollSpeedManger(banner, (LinearLayoutManager) recyclerView.getLayoutManager());
            recyclerView.setLayoutManager(speedManger);


            Field LayoutMangerField = ViewPager2.class.getDeclaredField("mLayoutManager");
            //将其中属性设为可访问
            LayoutMangerField.setAccessible(true);
            LayoutMangerField.set(viewPager2, speedManger);

            Field pageTransformerAdapterField = ViewPager2.class.getDeclaredField("mPageTransformerAdapter");
            pageTransformerAdapterField.setAccessible(true);
            //页面切换的动画效果
            Object mPageTransformerAdapter = pageTransformerAdapterField.get(viewPager2);
            if (mPageTransformerAdapter != null) {
                Class<?> aClass = mPageTransformerAdapter.getClass();
                Field layoutManager = aClass.getDeclaredField("mLayoutManager");
                layoutManager.setAccessible(true);
                layoutManager.set(mPageTransformerAdapter, speedManger);
            }
            Field scrollEventAdapterField = ViewPager2.class.getDeclaredField("mScrollEventAdapter");
            scrollEventAdapterField.setAccessible(true);
            Object mScrollEventAdapter = scrollEventAdapterField.get(viewPager2);
            if (mScrollEventAdapter != null) {
                Class<?> aClass = mScrollEventAdapter.getClass();
                Field layoutManager = aClass.getDeclaredField("mLayoutManager");
                layoutManager.setAccessible(true);
                layoutManager.set(mScrollEventAdapter, speedManger);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
