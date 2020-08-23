package com.tianyu.banner.banner.indicator;

/**
 * @describe：设置指示器接口，指示器需要实现这个接口
 * @author：TianYu
 */
public interface Indicator {
    //半径
    void setRadiu(int radiu);
    //边距
    void setMargin(int margin);

    //指示器数量
    void setCount(int count);
    //当前被选中的下标位置
    void setCurrent(int index);

    //设置指示器颜色
    void setIndicatorSelectColor(int color);
    void setIndicatorUnSelectColor(int color);

    void setId(int id);
    int getId();
}
