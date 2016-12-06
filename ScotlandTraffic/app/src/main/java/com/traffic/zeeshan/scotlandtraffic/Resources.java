package com.traffic.zeeshan.scotlandtraffic;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Resources {
    public static boolean incidents = true;
    public static boolean roadworks = true;
    public static boolean future_roadworks = false;
    public static ArrayList<RssData> rssItemList = new ArrayList<>();
    public static int selected_item = -1;

    private  static String filename = "settings.sav";

    public static void LoadResources(Context context) {
        File file = new File(context.getFilesDir(), filename);
        if (file.exists()) {
            FileInputStream inputStream;
            StringBuilder sb = new StringBuilder();
            try {
                inputStream = context.openFileInput(filename);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                inputStream.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            extractDataFromString(sb.toString());
        }
        else{
            SaveResources(context);
        }
    }

    public static void SaveResources(Context context) {
        FileOutputStream outputStream;

        try{
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(CreateDataString().getBytes());
            outputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void extractDataFromString(String data)
    {
        String[] d = data.split(";");
        Log.d("myApp", d[0]);
        if (d[0].equalsIgnoreCase("true"))
            incidents = true;
        else
            incidents = false;

        if (d[1].equalsIgnoreCase("true"))
            roadworks = true;
        else
            roadworks = false;

        if (d[2].equalsIgnoreCase("true"))
            future_roadworks = true;
        else
            future_roadworks = false;
    }

    private static String CreateDataString()
    {
        String data = "";
        if (incidents)
            data += "true;";
        else
            data += "false;";

        if (roadworks)
            data += "true;";
        else
            data += "false;";

        if (future_roadworks)
            data += "true;";
        else
            data += "false;";

        return data;
    }
}
