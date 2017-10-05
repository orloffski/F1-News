package by.madcat.development.f1newsreader.adapters;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.classesUI.NewsPageFragment;
import by.madcat.development.f1newsreader.dataSQLite.DatabaseDescription.*;

public class NewsPageAdapter extends FragmentStatePagerAdapter {

    private int count;
    private Uri newsUri;
    private ArrayList<String> links;

    public NewsPageAdapter(FragmentManager fm, int itemsCount, ArrayList<String> links) {
        super(fm);
        this.count = itemsCount;
        this.links = links;
    }

    @Override
    public Fragment getItem(int position) {
        newsUri = News.buildNewsUri(Long.parseLong(links.get(position)));

        return NewsPageFragment.newInstance(newsUri);
    }

    @Override
    public int getCount() {
        return count;
    }
}