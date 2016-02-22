package com.example.serge.rianews;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

public class NewsActivity extends AppCompatActivity {

    private NewsData newsData;
    private int position_news;
    private TextView text_of_news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        position_news = getIntent().getIntExtra("DATA_POSITION", 0);

        TextView titleView = (TextView) findViewById(R.id.text_title);
        TextView dateView = (TextView) findViewById(R.id.text_date);
        text_of_news = (TextView) findViewById(R.id.text_of_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        newsData = (NewsData)getIntent().getSerializableExtra("DATA_NEWS");
        toolbar.setTitle(newsData.getTitle());
        titleView.setText(newsData.getTitle());
        dateView.setText(newsData.getDate());

        if(newsData.getTextNews() == null) {
            new LoadNewsText().execute();
            sendResultBack();
            Toast.makeText(this, "Loading from net",
                    Toast.LENGTH_SHORT).show();
        } else {
            text_of_news.setText(newsData.getTextNews());
            Toast.makeText(this, "Loading from cache",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void sendResultBack() {
        Intent data = new Intent();
        data.putExtra("DATA_UPDATE", newsData);
        data.putExtra("DATA_POSITION_UPDATE", position_news);
        setResult(RESULT_OK, data);
    }

    private class LoadNewsText extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return new RiaHtmlParser().getTextNews(newsData.getNewsUrl());
        }

        @Override
        protected void onPostExecute(String text) {
            newsData.setTextNews(text);
            newsData.setHasBody(true);
            text_of_news.setText(text);
        }
    }
}
