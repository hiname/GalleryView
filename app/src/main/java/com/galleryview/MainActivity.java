package com.galleryview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvProfile, tvGallery, tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvProfile = (TextView) findViewById(R.id.btnProfile);
        tvProfile.setOnClickListener(this);

        tvGallery = (TextView) findViewById(R.id.btnGallery);
        tvGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tvProfile) {
            startActivity(new Intent(this, Profile.class));
        } else if (v == tvGallery) {
            startActivity(new Intent(this, Gallery.class));
        }
    }
}
