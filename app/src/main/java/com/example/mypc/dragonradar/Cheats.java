package com.example.mypc.dragonradar;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;


public class Cheats
{
    private Activity activity;
    private GoogleMap m_map;

    private Button btnMap0 = null;
    private Button btnMap1 = null;
    private Button btnMap2 = null;
    private Button location_butt = null;


    Cheats(Activity a, GoogleMap map)
    {
        activity = a;
        m_map = map;

        buttonFunc();
    }

    public void enableButtons()
    {

    }

    public void disableButtons()
    {
        button0();
        button1();
        button2();
        location_button();
    }

    private void buttonFunc()
    {
        // Normal button
        btnMap0 = activity.findViewById(R.id.button0);
        btnMap0.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                m_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        // Satellite button
        btnMap1 = activity.findViewById(R.id.button1);
        btnMap1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                m_map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        // Zoom button
        btnMap2 = activity.findViewById(R.id.button3);
        btnMap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                  MainActivity.maxZoomOut = 5;
            }
        });

        //Locations
        location_butt = activity.findViewById(R.id.locations_button);
        location_butt.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(activity, locations.class);
                activity.startActivity(intent);
            }
        });
    }

    private void button0()
    {
        if (btnMap0 != null)
        {
            btnMap0.setVisibility(View.INVISIBLE);
        }
    }

    private void button1()
    {
        if (btnMap1 != null)
        {
            btnMap1.setVisibility(View.INVISIBLE);
        }
    }

    private void button2()
    {
        if (btnMap2 != null)
        {
            btnMap2.setVisibility(View.INVISIBLE);
        }
    }

    private void location_button()
    {
        if (location_butt != null)
        {
            location_butt.setVisibility(View.INVISIBLE);
        }
    }
}
