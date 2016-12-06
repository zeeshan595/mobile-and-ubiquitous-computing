package com.traffic.zeeshan.scotlandtraffic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Animation extends AppCompatActivity {

    Drawing myDrawing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDrawing = new Drawing(this);
        setContentView(myDrawing);

        final Animation myContext = this;
        myDrawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(myContext, MainActivity.class));
            }
        });
    }
}