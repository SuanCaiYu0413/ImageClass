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
import Model.PictureInfo;

/**
 * Created by Administrator on 2017/5/2.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<HistoryLabel> Pictures;
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_list,parent,false);
        ViewHolder holder = new ViewHolder(v);
        if(mContext==null) {
            mContext = parent.getContext();
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryLabel pic_info = Pictures.get(position);
        Glide.with(mContext).load(pic_info.getUrl()).into(holder.pic);
        holder.name.setText(pic_info.getImgname());
        String labels = "";
        for(int i=0;i<pic_info.getLabel().length;i++){
            labels += "#" + pic_info.getLabel()[i] + " ";
        }
        holder.labels.setText(labels);
    }

    @Override
    public int getItemCount() {
        return Pictures.size();
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
        Pictures = pics;
    }
}
