package com.tianyu.banner.banner.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.tianyu.banner.banner.entity.IBannerData;

import java.util.ArrayList;

/**
 * @describe：如果没有特殊效果，使用默认适配器，实现基础效果
 * @author：TianYu
 */
public abstract class SimpleBannerAdapter extends BannerAdapter<SimpleBannerAdapter.SimpleViewHolder> {

    private static int mCount =0;
    private ArrayList<? extends IBannerData> mDatas;

    public SimpleBannerAdapter(ArrayList<? extends IBannerData> mDatas) {
        this.mDatas = mDatas;
    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setTag(mCount);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return new SimpleViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        position = position % mDatas.size();
        bindData((ImageView) holder.itemView,mDatas.get(position));
    }

    public abstract void bindData(ImageView view, IBannerData data);

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public ArrayList<? extends IBannerData> getDataList() {
        return mDatas;
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder{

        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private void setData(int id){
            itemView.setBackgroundResource(id);
        }
    }
}
