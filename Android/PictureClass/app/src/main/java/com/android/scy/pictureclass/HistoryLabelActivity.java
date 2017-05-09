package com.android.scy.pictureclass;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import Adapter.HistoryAdapter;
import Model.HistoryLabel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryLabelActivity extends AppCompatActivity {
    List<HistoryLabel> pics = new ArrayList<>();
    @BindView(R.id.history_toolbar) Toolbar toolbar;
    @BindView(R.id.history_recyclerview) RecyclerView mrv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_label);
        ButterKnife.bind(this);
        initPics();
        initActivity();
    }

    private void initPics() {
        for(int i=0;i<10;i++){
            HistoryLabel a = new HistoryLabel();
            a.setImgname("图1_"+i);
            a.setUrl("http://img1.juimg.com/141115/330821-1411151GU142.jpg");
            a.setLabel(new String[]{"asd","fsd","as3g"});
            pics.add(a);
            HistoryLabel b = new HistoryLabel();
            b.setImgname("图2_"+i);
            b.setUrl("http://www.dabaoku.com/sucaidatu/dongwu/chongwujingling/804838.JPG");
            b.setLabel(new String[]{"哈哈哈","嘿嘿","axxxx"});
            pics.add(b);
            HistoryLabel c = new HistoryLabel();
            c.setImgname("图3_"+i);
            c.setUrl("http://img.pconline.com.cn/images/upload/upc/tx/photoblog/1112/29/c2/10087294_10087294_1325133605031_mthumb.jpg");
            c.setLabel(new String[]{"哈哈哈","嘿嘿","axxxx"});
            pics.add(c);
            HistoryLabel d = new HistoryLabel();
            d.setImgname("图4_"+i);
            d.setUrl("http://mvimg2.meitudata.com/561086d22691c8272.jpg");
            d.setLabel(new String[]{"哈哈哈","嘿嘿","axxxx"});
            pics.add(d);
        }
    }

    private void initActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode().setDuration(500));
            getWindow().setReturnTransition(new Explode().setDuration(500));
        }
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrv.setLayoutManager(layoutManager);
        HistoryAdapter adapter = new HistoryAdapter(pics);
        mrv.setAdapter(adapter);
    }
}
