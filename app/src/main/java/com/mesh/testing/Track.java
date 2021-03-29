package com.mesh.testing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Track extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        MapView mapView = new MapView(this);
    }
}