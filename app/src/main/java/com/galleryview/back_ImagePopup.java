//package com.galleryview;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Matrix;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ImageView;
//
//import java.util.Arrays;
//
///**
// * Created by USER on 2016-10-17.
// */
//public class ImagePopup extends Activity implements View.OnClickListener, View.OnTouchListener {
//    private final int imgWidth = 198;
//    private final int imgHeight = 288;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.image_popup);
//        /** 전송메시지 */
//        Intent i = getIntent();
//        Bundle extras = i.getExtras();
//        int selRes = extras.getInt("selRes", 0);
//        /** 완성된 이미지 보여주기  */
//        iv = (ImageView) findViewById(R.id.imageView);
//        iv.setImageResource(selRes);
//        iv.setOnTouchListener(this);
//        // matrix.setTranslate(100,100);
//        iv.setImageMatrix(matrix);
//        matValues[0] = 1.0f;
//        matValues[4] = 1.0f;
//    }
//
//    ImageView iv = null;
//
//    public void onClick(View v) {}
//
//    float downX1, downY1;
//    float downX2, downY2;
//    float moveX1, moveY1;
//    float startDistance = 0;
//    Matrix matrix = new Matrix();
//    boolean isMutiTch = false;
//    float beforeScale = 1;
//    float calcScale;
//    float beforeX = 0;
//    float beforeY = 0;
//    float[] matValues = new float[9];
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        // Log.d("d", "event.getPointerCount() : " + event.get
//        // if (event.getPointerCount() <= 1) return false;
//        matrix.getValues(matValues);
////        beforeX = matValues[2];
////        beforeY = matValues[5];
////        Log.d("d", "beforeX,Y : " + beforeX + ", " + beforeY);
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                downX1 = event.getX(0);
//                downY1 = event.getY(0);
//                Log.d("d", "action_down : " + downX1 + ", " + downY1);
//                break;
//            case MotionEvent.ACTION_POINTER_1_DOWN:
//                downX1 = event.getX(0);
//                downY1 = event.getY(0);
//                Log.d("d", "p1_down : " + downX1 + ", " + downY1);
//                break;
//            case MotionEvent.ACTION_POINTER_2_DOWN:
//                isMutiTch = true;
//                downX2 = event.getX(1);
//                downY2 = event.getY(1);
//                Log.d("d", "p2_down : " + downX2 + ", " + downY2);
//                startDistance = getDist(downX1, downY1, downX2, downY2);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                moveX1 = event.getX(0);
//                moveY1 = event.getY(0);
//                if (isMutiTch) {
//                    downX2 = event.getX(1);
//                    downY2 = event.getY(1);
//                    float distance = getDist(moveX1, moveY1, downX2, downY2);
//                    Log.d("d", "distance : " + distance);
//                    calcScale = beforeScale + (distance - startDistance) / 200f;
//                    if (calcScale < 0.3f)
//                        calcScale = 0.3f;
//                    if (calcScale > 5.0f)
//                        calcScale = 5.0f;
//                    // matrix.setScale(calcScale, calcScale);
//                    // matrix.setScale(calcScale, calcScale, 200, 400);
//                    // matrix.setScale(calcScale, calcScale, (beforeX + (imgWidth / 2)), (beforeY + (imgHeight / 2)));
//                    matrix.getValues(matValues);
//                    matValues[0] = calcScale;
//                    matValues[4] = calcScale;
//                    matrix.setValues(matValues);
//                    iv.setImageMatrix(matrix);
//                    Log.d("d", "calcScale : " + calcScale);
//                } else {
//                    float movePointX = beforeX + (moveX1 - downX1);
//                    float movePointY = beforeY + (moveY1 - downY1);
//                    if (movePointX < -((imgWidth * calcScale) / 2))
//                        movePointX = -((imgWidth * calcScale) / 2);
//                    else if (movePointX > 400 - ((imgWidth * calcScale) / 2))
//                        movePointX = 400 - ((imgWidth * calcScale) / 2);
//                    if (movePointY < -((imgHeight * calcScale) / 2))
//                        movePointY = -((imgHeight * calcScale) / 2);
//                    else if (movePointY > 800 - ((imgHeight * calcScale) / 2))
//                        movePointY = 800 - ((imgHeight * calcScale) / 2);
//                    // matrix.setTranslate(beforeX  + moveValueX, beforeY + moveValueY);
//                    matrix.getValues(matValues);
//                    matValues[2] = movePointX;
//                    matValues[5] = movePointY;
//                    matrix.setValues(matValues);
//                    iv.setImageMatrix(matrix);
//                }
//                matrix.getValues(matValues);
//                Log.d("d", "matValues : " + Arrays.toString(matValues));
//                break;
//            case MotionEvent.ACTION_UP:
//                beforeScale = calcScale;
//                isMutiTch = false;
//                Log.d("d", "action_up => beforeScale, isMutiTch : " + beforeScale + ", " + isMutiTch);
//                // matrix.getValues(matValues);
//                beforeX = matValues[2];
//                beforeY = matValues[5];
//                Log.d("d", "beforeX,Y : " + beforeX + ", " + beforeY);
//                break;
//        }
//        return true;
//    }
//
//    public float getDist(float x1, float y1, float x2, float y2) {
////        float distX12 = Math.abs(downX1 - downX2);
////        float distY12 = Math.abs(downY1 - downY2);
//        float distX21 = (x2 - x1);
//        float distY21 = (y2 - y1);
//        return (float) Math.sqrt(Math.pow(distX21, 2) + Math.pow(distY21, 2));
//    }
//
//}
