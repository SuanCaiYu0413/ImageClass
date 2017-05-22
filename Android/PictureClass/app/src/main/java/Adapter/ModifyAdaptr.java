package Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.android.scy.pictureclass.R;

import java.util.List;

import Model.ResultItem;

/**
 * Created by Administrator on 2017/5/9.
 */

public class ModifyAdaptr extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<ResultItem> dataList;
    final public static int IMG_FLAG = 0;
    final public static int PLACEHOLDER = 1;
    final public static int EDIT_FLAG = 2;
    public ModifyAdaptr(Context context,List<ResultItem> datalist) {
        this.context = context;
        this.dataList = datalist;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ResultItem ri = dataList.get(i);
        switch (ri.getType()){
            case EDIT_FLAG:
                ViewHolderEdit viewHolderEdit;
                if(view == null){
                    viewHolderEdit = new ModifyAdaptr.ViewHolderEdit();
                    view = inflater.inflate(R.layout.item_modify_name,null);
                    viewHolderEdit.editText = (EditText) view.findViewById(R.id.item_modify_edit);
                    viewHolderEdit.textView = (TextView) view.findViewById(R.id.item_modify_title);
                    view.setTag(viewHolderEdit);
                }else{
                    viewHolderEdit = (ModifyAdaptr.ViewHolderEdit) view.getTag();
                }
                Log.d("debug",ri.getTitle());
                viewHolderEdit.textView.setText(ri.getTitle());
                break;
            default:
                break;
        }
        return view;
    }

    class ViewHolderEdit{
        public EditText editText;
        public TextView textView;
    }
}
