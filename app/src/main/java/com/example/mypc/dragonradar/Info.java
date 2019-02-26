package com.example.mypc.dragonradar;

import android.util.Base64;
import android.util.Log;

public class Info
{
    public LocHolder[] pos;

    private String[] julEncNums =
            {
                    "BQQeBgcGAgEB", "AgQeCQIIAQcD",
                    "BQQeBgcGAgAI", "AgQeCQIHBwEB",
                    "BQQeBgcHAQMABg==", "AgQeCQIHAgMHAg==",
                    "BQQeBgcHBAgJCA==", "AgQeCQIHBgYHBw==",
                    "BQQeBgcHCAME", "AgQeCQIHAwgJ",
                    "BQQeBgcIAgMB", "AgQeCQIHAwMH",
                    "BQQeBgcHCQQB", "AgQeCQIHBgcJ"
            };

    private String[] myEncNums =
            {
                    "BQQeBwEHCAIH", "AgUeAgkGAQME",
                    "BQQeBwEGBQkI", "AgUeAggAAQQE",
                    "BQQeBwEABQIA", "AgUeAgYBCQgF",
                    "BQQeBwADAAcF", "AgUeAgYEAQQA",
                    "BQQeBggGCQEA", "AgUeAgUJBAgJ",
                    "BQQeBggFBwgD", "AgUeAgYAAgEE",
                    "BQQeBggFCQQG", "AgUeAgUJBgAA",
                    "BQQeBggGAAgD", "AgUeAgUHAQYG",
                    "BQQeBwECBAcF", "AgUeAwACAQAJ",
                    "BQQeBwEJAQgB", "AgUeAwABBAYD"
            };

    public Info()
    {
        pos = new LocHolder[julEncNums.length / 2];
        Positions(decDoubles(julEncNums));
    }

    public Double[] decDoubles(String[] nums)
    {
        Double[] decDoubles = new Double[nums.length];

        for (int j=0; j< nums.length; j++)
        {
            decDoubles[j] = Double.parseDouble(decode(nums[j], "0"));
        }

        return decDoubles;
    }

    public void Positions(Double[] decDoubles)
    {
        int k = 0;

        for (int i = 0; i < decDoubles.length; i++)
        {
            pos[k] = new LocHolder(decDoubles[i], decDoubles[i+1]);
            i++;
            k++;
        }
    }

    public void ShowDebug()
    {
        for (int i = 0; i < pos.length; i++)
        {
            Log.d("m9", "Latitude: " + pos[i].GetLat() + " Logtitude: " + pos[i].GetLng());
        }
    }

    public String encode(String s, String key) {
        return Base64.encodeToString(xor(s.getBytes(), key.getBytes()), 0);
    }

    public String decode(String s, String key) {
        return new String(xor(Base64.decode(s, 0), key.getBytes()));
    }

    private byte[] xor(byte[] a, byte[] key) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ key[i%key.length]);
        }
        return out;
    }
}
