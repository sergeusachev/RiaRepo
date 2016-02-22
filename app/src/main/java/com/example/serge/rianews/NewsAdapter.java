package com.example.serge.rianews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private ArrayList<NewsData> newsList;
    private ImageHtmlDownloader<ImageView> imageHtmlDownloader;
    private Context context;

    public NewsAdapter(ArrayList<NewsData> newsList, ImageHtmlDownloader<ImageView> downloader,
                       Context context) {
        this.newsList = newsList;
        this.imageHtmlDownloader = downloader;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView date = holder.date;
        TextView title = holder.title;
        ImageView image = holder.image;

        if(newsList.get(position).getImageUrl() == null) {
            imageHtmlDownloader.queueImage(image, newsList.get(position).getNewsUrl(), position);
        } else if(newsList.get(position).isHasImage()) {
            Glide.with(context)
                    .load(newsList.get(position).getImageUrl())
                    .into(image);
        }


        date.setText(newsList.get(position).getDate());
        title.setText(newsList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView title;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            this.date = (TextView)view.findViewById(R.id.date_news);
            this.title = (TextView)view.findViewById(R.id.title_news);
            this.image = (ImageView)view.findViewById(R.id.image_news);
        }
    }
}
