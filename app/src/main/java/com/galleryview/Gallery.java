package com.galleryview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by USER on 2016-10-17.
 */
public class Gallery extends AppCompatActivity {
    public Context mContext = null;
    static final int resIds[] = {
            R.drawable.mov01, R.drawable.mov02, R.drawable.mov03,
            R.drawable.mov04, R.drawable.mov05, R.drawable.mov06,
            R.drawable.mov07, R.drawable.mov08, R.drawable.mov09,
            R.drawable.mov10, R.drawable.mov11,
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        final GridView gv = (GridView) findViewById(R.id.imgGrid);
        // BitmapDrawable bd=(BitmapDrawable) this.getResources().getDrawable(resIds[0]);
        gv.post(new Runnable() {
            @Override
            public void run() {
                int height = (resIds.length / 3 + 1) * 232;
                Log.d("d", "height : " + height);
                LinearLayout.LayoutParams gvLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                gv.setLayoutParams(gvLp);
            }
        });
        final ImageAdapter imageAdapter = new ImageAdapter(this);
        gv.setAdapter(imageAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                imageAdapter.callImageViewer(position);
            }
        });
    }

    class ImageAdapter extends BaseAdapter {
        public ImageAdapter(Context c) {
            mContext = c;
        }

        public final void callImageViewer(int selectedIndex) {
            Intent i = new Intent(mContext, ImagePopup.class);
            i.putExtra("resCnt", selectedIndex);
            startActivityForResult(i, 1);
        }

        public int getCount() {
            return resIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return resIds[position];
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(159, 230));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setPadding(2, 2, 2, 2);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(resIds[position]);
            return imageView;
        }
    }
}
