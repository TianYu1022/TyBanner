package com.tianyu.banner.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.viewpager2.widget.ViewPager2;

import com.tianyu.banner.R;
import com.tianyu.banner.banner.adapter.BannerAdapter;
import com.tianyu.banner.banner.entity.IBannerData;
import com.tianyu.banner.banner.indicator.CircleIndicator;
import com.tianyu.banner.banner.indicator.Indicator;
import com.tianyu.banner.banner.indicator.ProgressIndicator;
import com.tianyu.banner.banner.utils.BannerUtil;

import java.util.ArrayList;


/**
 * @describe：自定义banner
 * @author：TianYu
 */
public class TyBanner extends ConstraintLayout implements LifecycleObserver {

    private static final int DEFAULT_INTERVAL_TIME = 3000;
    private static final int DEFAULT_SCROLL_TIME = 1000;

    private static final int DEFAULT_INDICATOR_END_MARGIN_DP = 15;
    private static final int DEFAULT_INDICATOR_RADIU_DP = 3;
    private static final int DEFAULT_INDICATOR_MARGIN_DP = 8;

    private static final int DEFAULT_TITLE_START_MARGIN_DP = 12;
    private static final int DEFAULT_TITLE_MARGIN_TOP_DP = 10;
    private static final int DEFAULT_TITLE_MARGIN_BOTTOM_DP = 10;
    private static final int DEFAULT_TITLE_SIZE_DP = 14;

    private static final int SCROLL_MODE_HORIZONTAL = 0;
    private static final int SCROLL_MODE_VERTICAL = 1;

    private static final int INDICATOR_STYLE_CIRCLE = 0;
    private static final int INDICATOR_STYLE_PROGRESS = 1;

    private static final int TITLE_MODE_MARQUEE = 0;
    private static final int TITLE_MODE_END = 1;
    private static final int TITLE_MODE_MIDDLE = 2;
    private static final int TITLE_MODE_START = 3;

    /************************************************************************/
    private boolean mIsNeedShowBackGround;  //是否显示半透明背景
    private boolean mIsAutoLoop; // 是否自动循环
    private boolean mIsShowTitleByView; //是否显示title

    private int mIndicatorEndMargin;  //指示器前后边距
    private int mIndicatorMargin; //指示器之间间隙距离
    private int mTitleTextStartMargin; //title距离屏幕开始的距离 默认12dp
    private int mIndicatorRadiu; //指示器半径

    private int mTitleTextSize; //title字体大小
    private int mTitleTextMarginTop; //title 距离半透明背景上边边距
    private int mTitleTextMarginBottom; //title 距离半透明背景下边边距

    private int mIntervalTime;  // 切换间隔时间
    private int mScrollTime; //滑动时间

    private int mIndicatorSelectColor; //用户选中之后指示器颜色
    private int mIndicatorUnSelectColor; //用户未选中指示器颜色
    private int mTitleTextColor; //title颜色

    private int mBannerPageScrollMode; //banner滚动的方式 切换模式，垂直或者水平方向
    private int mTitleMode; //title过长处理的方式
    private int mIndicatorStyle;//Indicator的样式
    /************************************************************************/
    private ImageView mask;
    private ViewPager2 mPager;
    private TextView mTitle;
    private Indicator mIndicator;
    private TypedArray array;
    private LifecycleOwner mLifecycleOwner;

    private ArrayList<? extends IBannerData> mDatas;

    private int mIds = 0X1000;

    public TyBanner(Context context) {
        super(context);
    }

    public TyBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initValue(attrs);
    }

    public TyBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initValue(attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * @param attrs 属性集
     */
    private void initValue(AttributeSet attrs) {
        //获取样式化属性  可样式化
        array = getContext().obtainStyledAttributes(attrs, R.styleable.TyBanner);
        mIsNeedShowBackGround = array.getBoolean(R.styleable.TyBanner_bannerIsNeedShowBackGround, true);
        mIsAutoLoop = array.getBoolean(R.styleable.TyBanner_bannerIsAutoLoop, true);
        mIsShowTitleByView = array.getBoolean(R.styleable.TyBanner_bannerIsShowTitleByView, true);

        mIntervalTime = array.getInt(R.styleable.TyBanner_bannerIntervalTime, DEFAULT_INTERVAL_TIME);
        mScrollTime = array.getInt(R.styleable.TyBanner_bannerScrollTime, DEFAULT_SCROLL_TIME);

        //getDimensionPixelSize 将dp直接转为px值 获取尺寸像素大小
        mIndicatorEndMargin = array.getDimensionPixelSize(R.styleable.TyBanner_bannerIndicatorEndMargin, BannerUtil.dip2px(getContext(), DEFAULT_INDICATOR_END_MARGIN_DP));
        mIndicatorMargin = array.getDimensionPixelSize(R.styleable.TyBanner_bannerIndicatorMargin, BannerUtil.dip2px(getContext(), DEFAULT_INDICATOR_MARGIN_DP));
        mIndicatorRadiu = array.getDimensionPixelSize(R.styleable.TyBanner_bannerIndicatorRadiu, BannerUtil.dip2px(getContext(), DEFAULT_INDICATOR_RADIU_DP));
        mTitleTextStartMargin = array.getDimensionPixelSize(R.styleable.TyBanner_bannerTitleTextStartMargin, BannerUtil.dip2px(getContext(), DEFAULT_TITLE_START_MARGIN_DP));
        mTitleTextSize = array.getDimensionPixelSize(R.styleable.TyBanner_bannerTitleTextSize, BannerUtil.dip2px(getContext(), DEFAULT_TITLE_SIZE_DP));
        mTitleTextMarginTop = array.getDimensionPixelSize(R.styleable.TyBanner_bannerTitleTextMarginTop, BannerUtil.dip2px(getContext(), DEFAULT_TITLE_MARGIN_TOP_DP));
        mTitleTextMarginBottom = array.getDimensionPixelSize(R.styleable.TyBanner_bannerTitleTextMarginBottom, BannerUtil.dip2px(getContext(), DEFAULT_TITLE_MARGIN_BOTTOM_DP));

        mTitleTextColor = array.getColor(R.styleable.TyBanner_bannerTitleTextColor, Color.WHITE);
        mIndicatorSelectColor = array.getColor(R.styleable.TyBanner_bannerIndicatorSelectColor, Color.RED);
        mIndicatorUnSelectColor = array.getColor(R.styleable.TyBanner_bannerIndicatorUnSelectColor, Color.WHITE);

        mBannerPageScrollMode = array.getInt(R.styleable.TyBanner_bannerPageScrollMode, SCROLL_MODE_HORIZONTAL);
        mTitleMode = array.getInt(R.styleable.TyBanner_bannerTitleMode, TITLE_MODE_MARQUEE);
        mIndicatorStyle = array.getInt(R.styleable.TyBanner_bannerIndicatorStyle, INDICATOR_STYLE_CIRCLE);

        //回收
        array.recycle();
    }

    private void initView() {

        //step 1：添加一个 ViewPager2
        mPager = new ViewPager2(getContext());
        //设置ID
        mPager.setId(mIds++);
        addView(mPager);
        //当移动到所选页面上时
        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                position = position % mDatas.size();
                if (!mIsShowTitleByView) {
                    mask.setVisibility(INVISIBLE);
                    mTitle.setVisibility(INVISIBLE);
                }

                mTitle.setText(mDatas.get(position).getTitle());
                mIndicator.setCurrent(position);
            }
        });

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);

        constraintSet.connect(mPager.getId(), constraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(mPager.getId(), constraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(mPager.getId(), constraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(mPager.getId(), constraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.constrainWidth(mPager.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(mPager.getId(), ConstraintSet.MATCH_CONSTRAINT);

        //Step 2：添加一个 title 背景半透明
        mask = new ImageView(getContext());

        if (mIsNeedShowBackGround) {
            mask.setBackgroundColor(Color.parseColor("#40000000"));
        } else {
            mask.setBackgroundColor(Color.parseColor("#01000000"));
        }
        mask.setId(mIds++);
        addView(mask);

        constraintSet.connect(mask.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(mask.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(mask.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.constrainWidth(mask.getId(), ConstraintSet.MATCH_CONSTRAINT);

        mTitle = new TextView(getContext());
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        mTitle.setSingleLine(true);
        mTitle.measure(MeasureSpec.makeMeasureSpec(getResources().getDisplayMetrics().widthPixels, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(getResources().getDisplayMetrics().heightPixels, MeasureSpec.AT_MOST));

        int titleHeight = mTitle.getMeasuredHeight();
        constraintSet.constrainHeight(mask.getId(), (titleHeight + mTitleTextMarginTop + mTitleTextMarginBottom));
        // step3 添加 Indicator
        if (mIndicatorStyle == INDICATOR_STYLE_CIRCLE) {
            mIndicator = new CircleIndicator(getContext());
            mIndicator.setMargin(mIndicatorMargin);
        } else {
            mIndicator = new ProgressIndicator(getContext());
        }


        mIndicator.setIndicatorUnSelectColor(mIndicatorUnSelectColor);
        mIndicator.setIndicatorSelectColor(mIndicatorSelectColor);
        mIndicator.setRadiu(mIndicatorRadiu);
        mIndicator.setId(mIds++);
        addView((View) mIndicator);


        if (mIndicatorStyle == INDICATOR_STYLE_CIRCLE) {
            constraintSet.connect(mIndicator.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, mIndicatorEndMargin);
            constraintSet.connect(mIndicator.getId(), ConstraintSet.BOTTOM, mask.getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(mIndicator.getId(), ConstraintSet.TOP, mask.getId(), ConstraintSet.TOP);

            constraintSet.constrainWidth(mIndicator.getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainHeight(mIndicator.getId(), ConstraintSet.WRAP_CONTENT);
        } else {

            constraintSet.connect(mIndicator.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, mIndicatorEndMargin);
            constraintSet.connect(mIndicator.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, mIndicatorEndMargin);
            constraintSet.connect(mIndicator.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            constraintSet.connect(mIndicator.getId(), ConstraintSet.TOP, mask.getId(), ConstraintSet.BOTTOM);

            constraintSet.constrainWidth(mIndicator.getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainHeight(mIndicator.getId(), ConstraintSet.WRAP_CONTENT);
        }


        //Step 4：添加 title

        mTitle.setId(mIds++);
        mTitle.setTextColor(mTitleTextColor);
        //设置一行显示
        mTitle.setSingleLine(true);
        // TypedValue.COMPLEX_UNIT_PX 添加的dp已经是 px 了
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        switch (mTitleMode) {
            case TITLE_MODE_MARQUEE:
                mTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                mTitle.setSelected(true);
                //设置重复选取框动画的次数。仅在 TextView 已启用选取框的情况下应用。设置为-1可无限期重复
                mTitle.setMarqueeRepeatLimit(-1);
                break;
            case TITLE_MODE_END:
                mTitle.setEllipsize(TextUtils.TruncateAt.END);
                break;
            case TITLE_MODE_MIDDLE:
                mTitle.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                break;
            case TITLE_MODE_START:
                mTitle.setEllipsize(TextUtils.TruncateAt.START);
                break;
        }
        addView(mTitle);

        constraintSet.connect(mTitle.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, mTitleTextStartMargin);
        if (mIndicatorStyle == INDICATOR_STYLE_CIRCLE) {
            constraintSet.connect(mTitle.getId(), ConstraintSet.END, mIndicator.getId(), ConstraintSet.START, mIndicatorEndMargin);
        } else {
            constraintSet.connect(mTitle.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, mIndicatorEndMargin);
        }
        constraintSet.connect(mTitle.getId(), ConstraintSet.BOTTOM, mask.getId(), ConstraintSet.BOTTOM, mTitleTextMarginBottom);
        constraintSet.connect(mTitle.getId(), ConstraintSet.TOP, mask.getId(), ConstraintSet.TOP, mTitleTextMarginTop);

        constraintSet.constrainWidth(mTitle.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(mTitle.getId(), ConstraintSet.WRAP_CONTENT);

        constraintSet.applyTo(this);
    }

    /**
     * 当用户按下banner中的一张图片时拦截事件，并停止循环
     * 当用户松手时拦截事件，并重新开始循环
     * 触摸事件分发
     * 事件分发拦截器
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                stopLoop();
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                if (mBannerPageScrollMode == SCROLL_MODE_HORIZONTAL) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            }

            case MotionEvent.ACTION_UP: {
                startLoop();
            }

        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 设置生命周期所有者
     * 让 banner 的自动轮播跟随生命周期
     * 在使用时可以设置
     * {@link #setLifecycleOwner(LifecycleOwner)}
     * banner.setLifecycleOwner(this);
     *
     * @param lifecycleOwner
     */
    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.mLifecycleOwner = lifecycleOwner;

        mLifecycleOwner.getLifecycle().addObserver(this);
    }


    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setData(BannerAdapter adapter) {
        mDatas = adapter.getDataList();
        if (mDatas == null || mDatas.size() == 0) {
            return;
        }
        //适配器
        mPager.setAdapter(adapter);
        //设置启用用户输入
        mPager.setUserInputEnabled(true);
        //设定方向
        int mBannerPageScrollMode = this.mBannerPageScrollMode;
        //更改滚动方式
        if (mBannerPageScrollMode == SCROLL_MODE_HORIZONTAL) {
            mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        } else if (mBannerPageScrollMode == SCROLL_MODE_VERTICAL) {
            mPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        }

        ScrollSpeedManger.reflectLayoutManager(this);
        int initPosition = Integer.MAX_VALUE / 2;
        initPosition = initPosition - (initPosition % mDatas.size());

        //设置当前选定的页面。 如果smoothScroll = true
        // 将从目前的View进行平滑的动画到新的View 如果适配器未设置或清空忽略
        mPager.setCurrentItem(initPosition, false);
        mIndicator.setCount(mDatas.size());
        mIndicator.setCurrent(initPosition % mDatas.size());

        startLoop();
    }

    /**
     * 可见性更改
     *
     * @param changedView
     * @param visibility
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            startLoop();
        } else {
            stopLoop();
        }
    }

    private Runnable mLoopTask = new Runnable() {
        @Override
        public void run() {
            int cIndex = mPager.getCurrentItem();
            //smoothScroll 平滑的
            mPager.setCurrentItem(++cIndex, true);
            getHandler().postDelayed(this, mIntervalTime);
        }
    };

    private boolean isOnResume = false;

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        isOnResume = true;
        startLoop();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        isOnResume = false;
        stopLoop();
    }

    public void startLoop() {
        stopLoop();
        if (mIsAutoLoop && (mDatas != null && mDatas.size() > 1) && (getVisibility() == VISIBLE) && isOnResume && getHandler() != null) {
            getHandler().postDelayed(mLoopTask, mIntervalTime);
        }
    }

    /**
     * 停止循环
     */
    public void stopLoop() {
        if (getHandler() != null) {
            getHandler().removeCallbacks(mLoopTask);
        }
    }

    /*private int getPosition() {
        if (mDatas != null && mDatas.size() > 1) {
            int i = Integer.MAX_VALUE / 2;//取最大值的中间值
            int j = i % mDatas.size(); //中间值 取余
            if (j != 0) {//如果余数不等于0
                // 用size - 余数，求出还是多少才能除size 取余等于0 ，
                // 然后再加到中间值，目的是为了让中间值除size 取余等于0
                i = (mDatas.size() - j) + i;
            }
            return i;
        }
        return 0;
    }*/

    /**
     * 获取滑动时间
     *
     * @return
     */
    public int getScrollTime() {
        return mScrollTime;
    }

    public ViewPager2 getViewPager2() {
        return mPager;
    }

    /**
     * 当页面销毁的时候 将viewpager2解注册,并且停止循环
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 从 window 移除
        stopLoop();
    }
}
