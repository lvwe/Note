package com.example.administrator.note;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> implements View.OnClickListener{

//    private LayoutInflater mLayoutInflater;
//    private Context mContext;
    private List<Note> mNoteList;
    private OnItemClickListener mItemClickListener = null;

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null){
            mItemClickListener.OnItemClick(v,(int)v.getTag());
        }
    }

    //    public enum ITEM_TYPE{
//        ITEM1,
//        ITEM2
//    }
    static class ViewHolder extends RecyclerView.ViewHolder{
    ImageView mImg;
    TextView mTitle;
    TextView mCategory;
    ImageView mStar;
    ImageView mClock;
    TextView mReTime;
    ImageView mImgBar;


    public ViewHolder(View itemView) {
        super(itemView);
        mImg = (ImageView) itemView.findViewById(R.id.id_img);
        mTitle = (TextView) itemView.findViewById(R.id.id_title);
        mCategory = (TextView) itemView.findViewById(R.id.id_category);
        mStar = (ImageView) itemView.findViewById(R.id.id_star);
        mClock = (ImageView) itemView.findViewById(R.id.id_clock);
        mReTime = (TextView) itemView.findViewById(R.id.id_category);
        mImgBar = (ImageView) itemView.findViewById(R.id.id_bar);

    }
}
    public  RecycleViewAdapter (List<Note> mNoteList){
        this.mNoteList = mNoteList;
//        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = mNoteList.get(position);
//        holder.mImg.setImageResource(note.getImgId());
        holder.mImg.setImageBitmap(note.getImgId());
        holder.mTitle.setText(note.getTitle());
        holder.mCategory.setText(note.getCategory());
        holder.mClock.setImageBitmap(note.getImgClock());
        holder.mStar.setImageBitmap(note.getImgStar());
        holder.mReTime.setText(note.getReTime());
        holder.mImgBar.setBackgroundColor(Color.WHITE);
        holder.itemView.setTag(position);
    }

    public void setItemClickListener(OnItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }
}
