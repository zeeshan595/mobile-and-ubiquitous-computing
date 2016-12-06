package com.traffic.zeeshan.scotlandtraffic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Drawing extends View {

    Bitmap blue_ball;
    float changingY;

    public Drawing(Context context){
        super(context);

        blue_ball = BitmapFactory.decodeResource(getResources(), R.mipmap.blue_ball);
        changingY = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Setup Canvas
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        //Draw Text
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        canvas.drawText("click anywhere to go back", (canvas.getWidth() / 2) - 250, (canvas.getHeight() / 2) - 25, paint);

        //Draw Ball
        canvas.drawBitmap(blue_ball, canvas.getWidth() / 2, changingY, null);
        if (changingY < canvas.getHeight()) {
            changingY += 10;
        }
        else {
            changingY = 0;
        }
        invalidate();
    }
}
