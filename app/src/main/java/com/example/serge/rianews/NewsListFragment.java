package com.example.serge.rianews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NewsListFragment extends Fragment {

    public static final int LAYOUT = R.layout.fragment_news_list;

    private ArrayList<NewsData> newsList;
    private String url;
    private String saveFile;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ImageHtmlDownloader<ImageView> imageHtmlDownloader;
    private RiaJSONSerializer serializer;

    public static NewsListFragment getInstance(String url) {
        Bundle args = new Bundle();
        args.putString("URL", url);
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        url = getArguments().getString("URL");
        saveFile = url.hashCode() + ".json";
        prepareLoadData();
        loadData();
        prepareImageHtmlDownloader();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_news_list);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ListDividerDecorator(getActivity()));
        setupAdapter();

        recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                NewsData data = newsList.get(position);
                Intent i = new Intent(getActivity(), NewsActivity.class);
                i.putExtra("DATA_NEWS", data);
                i.putExtra("DATA_POSITION", position);
                getActivity().startActivityForResult(i, 0);
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        //serializer.saveNews(newsList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imageHtmlDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imageHtmlDownloader.quit();
        serializer.saveNews(newsList);
    }

    private void loadData() {
        if(isCacheExist()) {
            Toast.makeText(getActivity(), "Loading from cache",
                    Toast.LENGTH_SHORT).show();
            setupAdapter();
        } else {
            if(isNetworkAvailable()) {
                Toast.makeText(getActivity(), "Loading from net",
                        Toast.LENGTH_SHORT).show();
                new FetchNewsTask().execute();
            } else {
                Toast.makeText(getActivity(), "Network is unavailable",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isCacheExist() {
        return serializer.loadNews(newsList);
    }

    private void prepareLoadData() {
        serializer = new RiaJSONSerializer(getActivity(), saveFile);
        newsList = new ArrayList<>();
    }

    private void prepareImageHtmlDownloader() {
        imageHtmlDownloader = new ImageHtmlDownloader<>(new Handler());
        imageHtmlDownloader.setListener(new ImageHtmlDownloader.Listener<ImageView>() {
            @Override
            public void onGetImageLink(ImageView imageView, String imageLink, int position) {
                newsList.get(position).setImageWasLoaded(true);
                if(isVisible()) {
                    if(imageLink != null) {
                        newsList.get(position).setImageUrl(imageLink);
                        newsList.get(position).setHasImage(true);
                        Glide.with(NewsListFragment.this)
                                .load(imageLink)
                                .into(imageView);
                    } else {
                        newsList.get(position).setImageUrl(NewsData.LOAD_NONE);
                        newsList.get(position).setHasImage(false);
                    }
                }
            }
        });
        imageHtmlDownloader.start();
        imageHtmlDownloader.getLooper();
    }

    private void setupAdapter() {
        if(getActivity() == null || recyclerView == null) return;

        if(newsList != null) {
            recyclerView.setAdapter(new NewsAdapter(newsList, imageHtmlDownloader, getActivity()));
        } else {
            recyclerView.setAdapter(null);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo infoWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConnected = infoWifi.isConnected();

        NetworkInfo infoMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConnected = infoMobile.isConnected();

        return (isMobileConnected || isWifiConnected);
    }

    private class FetchNewsTask extends AsyncTask<Void, Void, ArrayList<NewsData>> {

        @Override
        protected ArrayList<NewsData> doInBackground(Void... params) {
            return new RiaRssParser(newsList).fetchNews(url);
        }

        @Override
        protected void onPostExecute(ArrayList<NewsData> newsData) {
            newsList.addAll(newsData);
            setupAdapter();
        }
    }
}
