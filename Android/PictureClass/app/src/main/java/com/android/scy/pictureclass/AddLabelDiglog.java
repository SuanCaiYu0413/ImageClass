package com.android.scy.pictureclass;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddLabelDiglog extends AppCompatActivity {
    Point pil;
    @BindView(R.id.add_ok) Button btnOk;
    @BindView(R.id.add_cancel) Button btnCancel;
    @BindView(R.id.add_edit) EditText editAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_label_diglog);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        int x = intent.getIntExtra("x",0);
        int y = intent.getIntExtra("y",0);
        pil = new Point(x,y);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("label",editAdd.getText().toString().trim());
                intent.putExtra("x",pil.x);
                intent.putExtra("y",pil.y);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED,null);
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
