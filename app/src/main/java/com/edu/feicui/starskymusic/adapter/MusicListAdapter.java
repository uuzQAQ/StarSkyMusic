package com.edu.feicui.starskymusic.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edu.feicui.starskymusic.R;
import com.edu.feicui.starskymusic.entity.MusicBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2017/1/11.
 */

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MyViewHolder> {


    private Context context;
    private List<MusicBean> list = new ArrayList<>();
    private boolean isShowTail = false;

    public MusicListAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<MusicBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_one_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        ButterKnife.bind(viewHolder, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.mTvSongName.setText(list.get(position).getTitle());
        holder.mTvSongerName.setText(list.get(position).getArtist());
        holder.mIvTailItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowTail == false){
                    holder.mLlTail.setVisibility(View.VISIBLE);
                    isShowTail = true;
                }else{
                    holder.mLlTail.setVisibility(View.GONE);
                    isShowTail = false;
                }
            }
        });

        //监听用于 显示全选等东西
        //监听item点击监听
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_check_item)
        ImageView mIvCheckItem;
        @BindView(R.id.tv_song_name)
        TextView mTvSongName;
        @BindView(R.id.tv_songer_name)
        TextView mTvSongerName;
        @BindView(R.id.iv_tail_item)
        ImageView mIvTailItem;
        @BindView(R.id.tv_tail_delete)
        TextView mTvTailDelete;
        @BindView(R.id.tv_tail_download)
        TextView mTvTailDownload;
        @BindView(R.id.tv_tail_collect)
        TextView mTvTailCollect;
        @BindView(R.id.tv_tail_share)
        TextView mTvTailShare;
        @BindView(R.id.ll_tail)
        LinearLayout mLlTail;


        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
