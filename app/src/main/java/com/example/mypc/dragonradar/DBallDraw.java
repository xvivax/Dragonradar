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

    private Info info;

    private LatLng[] position;

    private boolean playing;

    DBallDraw(Activity a, GoogleMap map)
    {
        activity = a;
        handler = new Handler();
        my_map = map;

        mediaPlayer = MediaPlayer.create(activity, R.raw.demo);

        info = new Info();
        position = new LatLng[info.pos.length];
        SetPosition();
        AddPosOnMap();

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

    private void AddPosOnMap()
    {
        for (int i = 0; i < position.length; i++)
        {
            positions.add(my_map.addMarker(new MarkerOptions().position(position[i]).alpha(transperancy).icon(BitmapDescriptorFactory.fromResource(R.drawable.db))));
        }
    }

    private void changeAlpha(float alpha)
    {
        for (int i = 0; i < positions.size(); i++)
        {
            positions.get(i).setAlpha(alpha);
        }
    }

    private void SetPosition()
    {
        for (int i = 0; i < info.pos.length; i++)
        {
            position[i] = new LatLng(info.pos[i].GetLat(), info.pos[i].GetLng());
        }
    }
}
