package com.tianyu.banner.banner.utils;

import android.content.Context;

/**
 * @describe：Banner dp/sp 转 px
 * @author：TianYu
 */
public class BannerUtil {
    /**
     * dp  转 px
     *
     * @param context
     * @param dp      传入多少dp
     * @return 返回dp 换算成px 后的值
     */
    public static int dip2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int sp2px(Context context, float sp) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }
}
