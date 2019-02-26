package com.example.mypc.dragonradar;

import android.util.Base64;
import android.util.Log;

public class Info
{
    public LocHolder[] pos;
    private String[] posNums =
            {
                    "54.717827", "25.296134",
                    "54.716598", "25.280144",
                    "54.710520", "25.261985",
                    "54.703075", "25.264140",
                    "54.686910", "25.259489",
                    "54.685783", "25.260214",
                    "54.685946", "25.259600",
                    "54.686083", "25.257166",
                    "54.712475", "25.302109",
                    "54.719181", "25.301463"
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
        pos = new LocHolder[posNums.length / 2];

        String[] encStringArr = encryptDoubles(posNums);

        /*
        for (int i = 0; i < encStringArr.length; i++)
        {
            Log.d("m9", encStringArr[i]);
        }
        */
        Positions(decDoubles(myEncNums));
        ShowDebug();
    }

    // for testing
    public String[] encryptDoubles(String[] nums)
    {
        String[] encString = new String[nums.length];

        for (int j=0; j< nums.length; j++)
        {
            encString[j] = encode(nums[j], "0");
        }

        return encString;
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
