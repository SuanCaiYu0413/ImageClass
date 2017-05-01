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

public class AddLabelDiglog extends AppCompatActivity {
    Point pil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int x = intent.getIntExtra("x",0);
        int y = intent.getIntExtra("y",0);
        pil = new Point(x,y);
        setContentView(R.layout.activity_add_label_diglog);
        Button btn_ok = (Button) findViewById(R.id.add_ok);
        final EditText edit_add = (EditText) findViewById(R.id.add_edit);
        Button btn_cancel = (Button) findViewById(R.id.add_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("label",edit_add.getText().toString().trim());
                intent.putExtra("x",pil.x);
                intent.putExtra("y",pil.y);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
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
