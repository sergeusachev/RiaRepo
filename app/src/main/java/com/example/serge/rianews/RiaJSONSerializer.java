package com.example.serge.rianews;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class RiaJSONSerializer {

    private String path;
    private String saveFile;

    public RiaJSONSerializer(Context context, String saveFile) {
        this.saveFile = saveFile;
        path = context.getExternalCacheDir() + "/";
    }

    public void saveNews(ArrayList<NewsData> newsList) {
        try {
            JSONArray array = new JSONArray();
            for(NewsData news : newsList) {
                array.put(news.toJSON());
            }

            Writer writer = null;
            try {
                File file = new File(path, saveFile);
                writer = new BufferedWriter(new FileWriter(file, true));
                writer.write(array.toString());
            } finally {
                if(writer != null)
                    writer.close();
            }
        } catch (Exception e) {
            Log.e("load", "Ex in saveNews");
        }
    }

    public boolean loadNews(ArrayList<NewsData> list) {
        File file = new File(path, saveFile);
        if(!file.exists()) {
            return false;
        }
        BufferedReader reader = null;
        try {
            try {
                InputStream in = new FileInputStream(path + saveFile);
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder jsonString = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    jsonString.append(line);
                }
                JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(new NewsData(jsonArray.getJSONObject(i)));
                }
            } catch (FileNotFoundException e) {
                Log.e("load", "File Not Found");
            } catch (JSONException e) {
                Log.e("load", "JSONEx");
                e.printStackTrace();
            } finally {
                if(reader != null)
                    reader.close();
            }
        } catch (IOException e) {
            Log.e("load", "IOEx");
        }

        return true;
    }
}
