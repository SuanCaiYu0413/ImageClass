package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.scy.pictureclass.R;
import com.bumptech.glide.Glide;

import java.util.List;

import Model.HistoryLabel;

/**
 * Created by Administrator on 2017/5/2.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> implements View.OnClickListener {
    private List<HistoryLabel> pictures;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener = null;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_list,parent,false);
        ViewHolder holder = new ViewHolder(v);
        if(mContext==null) {
            mContext = parent.getContext();
        }
        v.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryLabel picInfo = pictures.get(position);
        Glide.with(mContext).load(picInfo.getUrl()).into(holder.pic);
        holder.name.setText(picInfo.getImgname());
        String labels = "";
        for(int i=0;i<picInfo.getLabel().length;i++){
            labels += "#" + picInfo.getLabel()[i] + " ";
        }
        holder.labels.setText(labels);
        holder.itemView.setTag(position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    @Override
    public int getItemCount() {
        return pictures.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pic;
        TextView name;
        TextView labels;
        public ViewHolder(View v){
            super(v);
            pic = (ImageView) v.findViewById(R.id.img_history);
            name = (TextView) v.findViewById(R.id.name_history);
            labels = (TextView) v.findViewById(R.id.labels_history);
        }
    }
    public HistoryAdapter(List<HistoryLabel> pics){
        pictures = pics;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
