package by.madcat.development.f1newsreader;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.data.DatabaseDescription.*;

public class NewsPageAdapter extends FragmentStatePagerAdapter {

    private int count;
    private int sectionID;
    private Uri newsUri;
    private ArrayList<String> links;

    public NewsPageAdapter(FragmentManager fm, int itemsCount, int sectionID, ArrayList<String> links) {
        super(fm);
        this.sectionID = sectionID;
        this.count = itemsCount;
        this.links = links;
    }

    @Override
    public Fragment getItem(int position) {
        switch (sectionID){
            case R.id.nav_news:
                newsUri = News.buildNewsUri(Long.parseLong(links.get(position)));
                break;
            case R.id.nav_memuar:
                newsUri = Memuar.buildMemuarUri(Long.parseLong(links.get(position)));
                break;
        }
        return NewsPageFragment.newInstance(newsUri, sectionID);
    }

    @Override
    public int getCount() {
        return count;
    }
}