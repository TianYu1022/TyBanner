package com.tianyu.banner.banner.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.tianyu.banner.banner.entity.IBannerData;

import java.util.ArrayList;

/**
 * @describe：banner可扩展适配器
 * @author：TianYu
 */
public abstract class BannerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    public abstract ArrayList<? extends IBannerData> getDataList();
}
