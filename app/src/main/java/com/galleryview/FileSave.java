package com.galleryview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by USER on 2016-10-18.
 */
public class FileSave {
    public static final String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static void saveView(View view, String name) {
        Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        // Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        if (b != null) {
            File folder = new File(storagePath + "/DCIM");
            folder.mkdir();
            try {
                File file = new File(storagePath + "/DCIM/" + name + ".png");
                Canvas c = new Canvas(b);
                view.draw(c);
                FileOutputStream fos = new FileOutputStream(file);
                if (fos != null) {
                    b.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                    Toast.makeText(view.getContext(), "save : " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                }
                //setWallpaper( b );
            } catch (Exception e) {
                Log.e("testSaveView", "Exception: " + e.toString());
            }
        }
    }
    //        public void viewSave(View v, String filename){
//
//            String StoragePath =
//                    Environment.getExternalStorageDirectory().getAbsolutePath();
//            String savePath = StoragePath + "/DCIM";
//            File f = new File(savePath);
//            if (!f.isDirectory()) f.mkdirs();
//
//            v.buildDrawingCache();
//
//            // Bitmap bitmap = v.getDrawingCache();
//            BitmapDrawable bd = (BitmapDrawable) v.getBackground();
//            Bitmap bitmap = bd.getBitmap();
//
//            FileOutputStream fos;
//            try{
//                fos = new FileOutputStream(savePath + "/" + filename);
//                bitmap.compress(Bitmap.CompressFormat.JPEG,80,fos);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//            Toast.makeText(context, "save", Toast.LENGTH_SHORT).show();
//        }
}
