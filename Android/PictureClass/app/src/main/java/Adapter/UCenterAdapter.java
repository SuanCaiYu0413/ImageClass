package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.scy.pictureclass.R;
import com.bumptech.glide.Glide;

import java.util.List;

import Model.ResultItem;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Administrator on 2017/5/3.
 */

public class UCenterAdapter extends BaseAdapter {
    private Context context;
    private List<ResultItem> datalist;
    private final int TYPE_ONE = 0;
    private LayoutInflater inflater;
    private final int TYPE_TWO = 1;
    private final int TYPE_THERE = 2;
    public UCenterAdapter(Context context,List<ResultItem> datalist){
        this.context = context;
        this.datalist = datalist;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int i) {
        return datalist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ResultItem ri = datalist.get(i);
        if(getItemViewType(i) == TYPE_ONE){
            ViewHolderOne viewHolderOne;
            if(view == null){
                viewHolderOne = new ViewHolderOne();
                view = inflater.inflate(R.layout.me_item_ucenter_fragment,null);
                viewHolderOne.imageView = (CircleImageView) view.findViewById(R.id.header_pic_ucenter);
                viewHolderOne.textView = (TextView) view.findViewById(R.id.uname_ucenter);
                view.setTag(viewHolderOne);
            }else{
                viewHolderOne = (ViewHolderOne) view.getTag();
            }
            Glide.with(context).load(ri.getImg()).into(viewHolderOne.imageView);
            viewHolderOne.textView.setText(ri.getTitle());
        }else if(getItemViewType(i) == TYPE_TWO){
            ViewHolderTwo viewHolderTwo;
            if(view == null){
                viewHolderTwo = new ViewHolderTwo();
                view = inflater.inflate(R.layout.item_ucenter_fragment,null);
                viewHolderTwo.imageView = (ImageView) view.findViewById(R.id.item_ucenter_img);
                viewHolderTwo.textView = (TextView) view.findViewById(R.id.item_ucenter_title);
                view.setTag(viewHolderTwo);
            }else{
                viewHolderTwo = (ViewHolderTwo) view.getTag();
            }
            Glide.with(context).load(ri.getImg()).into(viewHolderTwo.imageView);
            viewHolderTwo.textView.setText(ri.getTitle());
        }else if(getItemViewType(i) == TYPE_THERE){
            ViewHolderThere viewHolderThere;
            if(view == null){
                viewHolderThere = new ViewHolderThere();
                view = inflater.inflate(R.layout.placeholder_ucenter_fragment,null);
                view.setTag(viewHolderThere);
            }else{
                viewHolderThere = (ViewHolderThere) view.getTag();
            }
        }
        return view;
    }

    class ViewHolderOne {
        public CircleImageView imageView;
        public TextView textView;
    }
    class ViewHolderThere {

    }
    class ViewHolderTwo {
        public ImageView imageView;
        public TextView textView;
    }

    @Override
    public int getItemViewType(int position) {
        ResultItem ri = datalist.get(position);
        switch (ri.getType()){
            case TYPE_ONE:
                return TYPE_ONE;
            case TYPE_TWO:
                return TYPE_TWO;
            case TYPE_THERE:
                return TYPE_THERE;
            default:
                break;
        }
        return -1;
    }
}
