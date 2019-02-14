package com.example.mypc.dragonradar;

import android.app.Activity;
import android.graphics.Interpolator;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Debug;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBallDraw implements Runnable
{
    private Activity activity;
    private android.os.Handler handler;
    private GoogleMap my_map;

    private List<Marker> positions = new ArrayList<Marker>();

    //Dragon balls blinking speed
    private long speed = 1;
    private float transperancy = 0.0f;

    private float passAlpha;
    private MediaPlayer mediaPlayer;

    private LatLng pos1 = new LatLng(54.800532,25.414260);
    private LatLng pos2 = new LatLng(54.798419,25.407121);
    private LatLng pos3 = new LatLng(54.798092,25.405475);
    private LatLng pos4 = new LatLng(54.799574,25.404955);
    private LatLng pos5 = new LatLng(54.799001,25.402865);
    private LatLng pos6 = new LatLng(54.799361,25.405003);
    private LatLng pos7 = new LatLng(54.799338,25.404736);
    private LatLng pos8 = new LatLng(54.754739, 25.26594);
    private LatLng pos9 = new LatLng(54.754163, 25.264574);
    private LatLng pos10 = new LatLng(54.754229, 25.266919);


    private boolean playing;

    DBallDraw(Activity a, GoogleMap map)
    {
        activity = a;
        handler = new Handler();
        my_map = map;

        mediaPlayer = MediaPlayer.create(activity, R.raw.demo);

        addPos();

        playing = true;
    }

    long start = SystemClock.uptimeMillis();

    @Override
    public void run()
    {
        long elapsed = SystemClock.uptimeMillis() - start;
        passAlpha = (float)elapsed / 1000;

        if (positions != null)
        {
            if (transperancy >= 1)
            {
                if (playing)
                {
                    mediaPlayer.start();
                }

                transperancy = 0.0f;
            }
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    changeAlpha(transperancy);
                }
            });

            transperancy = transperancy + passAlpha;
        }

        start = SystemClock.uptimeMillis();

        handler.postDelayed(this, speed * 100);
    }

    public void PlaySound(boolean isPlaying)
    {
        playing = isPlaying;
    }

    private void addPos()
    {
        positions.add(my_map.addMarker(new MarkerOptions().position(new LatLng(54.71800, 25.297690)).alpha(transperancy).icon(BitmapDescriptorFactory.fromResource(R.drawable.db))));
        positions.add(my_map.addMarker(new MarkerOptions().position(pos1).alpha(transperancy).icon(BitmapDescriptorFactory.fromResource(R.drawable.db))));
        positions.add(my_map.addMarker(new MarkerOptions().position(pos2).alpha(transperancy).icon(BitmapDescriptorFactory.fromResource(R.drawable.db))));
        positions.add(my_map.addMarker(new MarkerOptions().position(pos3).alpha(transperancy).icon(BitmapDescriptorFactory.fromResource(R.drawable.db))));
        positions.add(my_map.addMarker(new MarkerOptions().position(pos4).alpha(transperancy).icon(BitmapDescriptorFactory.fromResource(R.drawable.db))));
        positions.add(my_map.addMarker(new MarkerOptions().position(pos5).alpha(transperancy).icon(BitmapDescriptorFactory.fromResource(R.drawable.db))));
        positions.add(my_map.addMarker(new MarkerOptions().position(pos6).alpha(transperancy).icon(BitmapDescriptorFactory.fromResource(R.drawable.db))));
        positions.add(my_map.addMarker(new MarkerOptions().position(pos7).alpha(transperancy).icon(BitmapDescriptorFactory.fromResource(R.drawable.db))));
        positions.add(my_map.addMarker(new MarkerOptions().position(pos8).alpha(transperancy).icon(BitmapDescriptorFactory.fromResource(R.drawable.db))));
        positions.add(my_map.addMarker(new MarkerOptions().position(pos9).alpha(transperancy).icon(BitmapDescriptorFactory.fromResource(R.drawable.db))));
        positions.add(my_map.addMarker(new MarkerOptions().position(pos10).alpha(transperancy).icon(BitmapDescriptorFactory.fromResource(R.drawable.db))));

    }

    private void changeAlpha(float alpha)
    {
        for (int i = 0; i < positions.size(); i++)
        {
            positions.get(i).setAlpha(alpha);
        }
    }
}
