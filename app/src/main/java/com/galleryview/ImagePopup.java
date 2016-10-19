package com.galleryview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by USER on 2016-10-18.
 */
public class ImagePopup extends AppCompatActivity {
    Can can = null;
    LinearLayout ll = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        int resCnt = extras.getInt("resCnt", 0);
        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        can = new Can(this, resCnt);
        can.setBackgroundColor(Color.BLACK);
        LinearLayout.LayoutParams canLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        canLp.weight = 0.9f;
        can.setLayoutParams(canLp);
        ll.addView(can);
        Button btn = new Button(this);
        btn.setText("저장");
        LinearLayout.LayoutParams bntLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        bntLp.weight = 0.1f;
        btn.setLayoutParams(bntLp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nowDateTime = new SimpleDateFormat("yyyy-MM-dd-HHmmss").format(new Date());
                FileSave.saveView(can, nowDateTime);
            }
        });
        ll.addView(btn);
        setContentView(ll);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // can.destroy();
    }

    protected class Can extends ImageView {
        float imgX, imgY;
        float imgW, imgH;
        float bx, by;
        float scale = 1;
        int resCnt = 0;
        Context context = null;
        boolean isReady = false;
        int cvWidth, cvHeight;
        //        int[] resIds = {
//                R.drawable.mov01, R.drawable.mov02, R.drawable.mov03,
//                R.drawable.mov04, R.drawable.mov05, R.drawable.mov06,
//                R.drawable.mov07, R.drawable.mov08, R.drawable.mov09,
//                R.drawable.mov10, R.drawable.mov11,
//        };
        boolean isTchEnable = true;

        public Can(Context context, int resCnt) {
            super(context);
            this.context = context;
            // setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            // paint.setColor(Color.BLACK);
            this.resCnt = resCnt;
            bmp = BitmapFactory.decodeResource(context.getResources(), Gallery.resIds[resCnt]);
            isReady = false;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    cvWidth = getWidth();
                    cvHeight = getHeight();
                    imgW = bmp.getWidth();
                    imgH = bmp.getHeight();
                    imgX = (cvWidth / 2);
                    bx = imgX;
                    imgY = (cvHeight / 2);
                    by = imgY;
                    isReady = true;
                    alpha = 255;
                    scale = 1;
                    invalidate();
                }
            }, 500);
        }

        public void reLoad(int resCnt) {
            Log.d("d", "reLoad()");
            if (bmp != null) bmp.recycle();
            bmp = BitmapFactory.decodeResource(context.getResources(), Gallery.resIds[resCnt]);
            isSlide = false;
            isTryLeft = false;
            isTryRight = false;

            postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgW = bmp.getWidth();
                    imgH = bmp.getHeight();
                    imgX = (cvWidth / 2);
                    bx = imgX;
                    imgY = (cvHeight / 2);
                    by = imgY;
                    isReady = true;
                    // alpha = 255;
                    scale = 1;
                    slideCnt = 0;
                    invalidate();
                    alpha = 0;
                    Runnable run = new Runnable() {
                        @Override
                        public void run() {
                            alpha += 12;
                            if (slideCnt++ < 20) new Handler().postDelayed(this, 60);
                            else {
                                slideCnt = 0;
                                alpha = 255;
                                isTchEnable = true;
                            }
                            invalidate();
                        }
                    };
                    new Handler().post(run);
                }
            }, 500);
        }

        public void recycle() {
            bmp.recycle();
        }

        Paint paint = new Paint();
        Rect dstRect = null;
        Bitmap bmp;

        boolean isTryLeft = false;
        boolean isTryRight = false;

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (!isReady) return;
            float px = (imgW * scale / 2);
            float py = (imgH * scale / 2);
            float minX, maxX;
            float minY, maxY;
            if (!isSlide) {
                if ((imgW * scale) < cvWidth) {
                    minX = -((imgW * scale)) / 2 + px;
                    maxX = cvWidth - ((imgW * scale)) / 2 + px;
                    minY = -((imgH * scale)) / 2 + py;
                    maxY = cvHeight - ((imgH * scale)) / 2 + py;
                } else {
                    minX = cvWidth - (imgW * scale) + px;
                    maxX = px;
                    minY = cvHeight - (imgH * scale) + py;
                    maxY = py;
                }
                if (imgX < minX) {
                    imgX = minX;
                    isTryLeft = true;
                }
                else if (imgX > maxX) {
                    imgX = maxX;
                    isTryRight = true;
                }
                if (imgY < minY) imgY = minY;
                else if (imgY > maxY) imgY = maxY;
            }
            dstRect = new Rect((int) (imgX - px), (int) (imgY - py), (int) (imgX + (imgW * scale) - px), (int) (imgY + (imgH * scale) - py));
            paint.setAlpha(alpha);
            canvas.drawBitmap(bmp, null, dstRect, paint);

        }

        int slideCnt = 0;
        boolean isSlide = false;

        public void slideLeft() {
            if (!isTryLeft) return;

            Log.d("d", "resCnt : " + resCnt);
            if (resCnt >= Gallery.resIds.length - 1) {
                Toast.makeText(context, "마지막 이미지입니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            isTchEnable = false;
            isSlide = true;
            Log.d("d", "slideLeft()");
            Toast.makeText(context, "slideLeft", Toast.LENGTH_SHORT).show();
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    alpha -= 12;
                    imgX -= 8;
                    invalidate();
                    if (slideCnt++ < 20) new Handler().postDelayed(this, 60);
                    else {
                        resCnt++;
                        reLoad(resCnt);
                    }
                }
            };
            new Handler().post(run);
        }

        public void slideRight() {

            if (!isTryRight) return;

            if (resCnt <= 0) {
                Toast.makeText(context, "첫번째 이미지입니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            isTchEnable = false;
            isSlide = true;
            Log.d("d", "slideRight()");
            Toast.makeText(context, "slideRight", Toast.LENGTH_SHORT).show();
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    alpha -= 12;
                    imgX += 8;
                    invalidate();
                    if (slideCnt++ < 20) new Handler().postDelayed(this, 60);
                    else {
                        resCnt--;
                        reLoad(resCnt);
                    }
                }
            };
            new Handler().post(run);
        }

        int alpha = 255;
        float downX, downY;
        float downX2, downY2;
        float moveX, moveY;
        boolean isMult = false;
        float beforeDist;
        // int cenX, cenY;
        float tchCenX, tchCenY;
        ArrayList<Float> tmpX = new ArrayList<Float>();
        // float[] tmpX = new float[4], tmpDy = new float[3];
        int tmpCnt = 0;

        public void calcCenXY(float x1, float y1, float x2, float y2) {
            tchCenX = (x1 + x2) / 2;
            tchCenY = (y1 + y2) / 2;
            Log.d("d", "tchCenX,Y : " + tchCenX + ", " + tchCenY);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (!isTchEnable) return false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    downX = event.getX(0);
                    downY = event.getY(0);
                    tmpX.clear();
                    for (int i = 0; i < 5; i++) tmpX.add(downX);
                    break;
                case MotionEvent.ACTION_MOVE:
                    moveX = event.getX(0);
                    moveY = event.getY(0);

                    if (isTryLeft) {
                        float slideValue = Math.abs(tmpX.get(tmpX.size() - 1) - tmpX.get(0));
                        if(slideValue > 0) isTryLeft = false;
                    } else if (isTryRight) {
                        float slideValue = Math.abs(tmpX.get(tmpX.size() - 1) - tmpX.get(0));
                        if(slideValue < 0) isTryRight = false;
                    }


                    Log.d("d", "move_x, y : " + moveX + ", " + moveX);
                    if (isMult) {
                        downX2 = event.getX(1);
                        downY2 = event.getY(1);
                        float dist = (float) Math.sqrt(Math.pow(moveX - downX2, 2) + Math.pow(moveY - downY2, 2));
                        float calcDist = dist - beforeDist;
                        scale += calcDist / 100f;
                        if (scale < 0.5f) scale = 0.5f;
                        if (scale > 5.0f) scale = 5.0f;
                        Log.d("d", "calcDist : " + calcDist);
                        beforeDist = dist;
                    } else {
                        float dx = moveX - downX;
                        float dy = moveY - downY;
                        imgX = bx + dx;
                        imgY = by + dy;
                        tmpX.remove(0);
                        tmpX.add(moveX);
                    }
                    break;
                case MotionEvent.ACTION_POINTER_2_DOWN:
                    downX2 = event.getX(1);
                    downY2 = event.getY(1);
                    calcCenXY(downX, downY, downX2, downY2);
                    beforeDist = (float) Math.sqrt(Math.pow(downX - downX2, 2) + Math.pow(downY - downY2, 2));
                    isMult = true;
                    break;
                case MotionEvent.ACTION_UP:
                    bx = imgX;
                    by = imgY;
                    isMult = false;
                    float slideRange = cvWidth / 7;
                    float slideValue = Math.abs(tmpX.get(tmpX.size() - 1) - tmpX.get(0));
                    if (slideValue > slideRange) {
                        if (tmpX.get(tmpX.size() - 1) < tmpX.get(0)) slideLeft();
                        else slideRight();
                    }
                    Log.d("d", "slideValue : " + slideValue);
                    Log.d("d", "slideRange : " + slideRange);
                    break;
            }
            invalidate();
            return true;
        }
    }
}
