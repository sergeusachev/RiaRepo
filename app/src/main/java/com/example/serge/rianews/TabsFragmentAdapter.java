package com.example.serge.rianews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabsFragmentAdapter extends FragmentStatePagerAdapter {

    private String[] urls;
    private String[] categories;

    public TabsFragmentAdapter(FragmentManager fm) {
        super(fm);
        fillUrls();
        fillCategories();
    }

    @Override
    public Fragment getItem(int position) {
        return NewsListFragment.getInstance(urls[position]);
    }

    @Override
    public int getCount() {
        return urls.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categories[position];
    }

    private void fillUrls() {
        urls = new String[] {
                "http://ria.ru/export/rss2/politics/index.xml",
                "http://ria.ru/export/rss2/world/index.xml",
                "http://ria.ru/export/rss2/economy/index.xml",
                "http://ria.ru/export/rss2/society/index.xml",
                "http://ria.ru/export/rss2/education/index.xml",
                "http://ria.ru/export/rss2/moscow/index.xml",
                "http://sport.ria.ru/export/rss2/sport/index.xml",
                "http://ria.ru/export/rss2/incidents/index.xml",
                "http://ria.ru/export/rss2/defense_safety/index.xml",
                "http://ria.ru/export/rss2/science/index.xml",
                "http://eco.ria.ru/export/rss2/eco/index.xml",
                "http://ria.ru/export/rss2/tourism/index.xml",
                "http://ria.ru/export/rss2/culture/index.xml"
        };
    }

    private void fillCategories() {
        categories = new String[] {
                "Политика",
                "В мире",
                "Экономика",
                "Общество",
                "Образование",
                "Москва",
                "Спорт",
                "Происшествия",
                "Оборона",
                "Наука",
                "Экология",
                "Туризм",
                "Культура"
        };
    }
}
