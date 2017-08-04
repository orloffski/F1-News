package by.madcat.development.f1newsreader.classesUI;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.Interfaces.NewsOpenListener;
import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.adapters.MainPagerAdapter;
import by.madcat.development.f1newsreader.data.DatabaseDescription;
import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;

public class MainActivity extends AppCompatActivity implements NewsOpenListener{

    private CoordinatorTabLayout mCoordinatorTabLayout;
    private int[] mImageArray, mColorArray;
    private ArrayList<Fragment> mFragments;
    private final String[] mTitles = {"Новости", "Статьи", "Интервью", "Техника", "История", "Колонки", "Автоспорт"};
    private ViewPager mViewPager;

    private int sectionItemsCount;
    private ArrayList<String> newsIDs;
    private ArrayList<String> newsLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initFragments();
        initViewPager();
        mImageArray = new int[]{
                R.drawable.drawerimage_news,
                R.drawable.drawerimage_memuar,
                R.drawable.drawerimage_interview,
                R.drawable.drawerimage_tech,
                R.drawable.drawerimage_history,
                R.drawable.drawerimage_columns,
                R.drawable.drawerimage_autosport};
        mColorArray = new int[]{
                android.R.color.holo_red_light,
                android.R.color.holo_red_light,
                android.R.color.holo_red_light,
                android.R.color.holo_red_light,
                android.R.color.holo_red_light,
                android.R.color.holo_red_light,
                android.R.color.holo_red_light};

        mCoordinatorTabLayout = (CoordinatorTabLayout) findViewById(R.id.coordinatortablayout);
        mCoordinatorTabLayout.setTitle("Demo")
                .setBackEnable(true)
                .setImageArray(mImageArray, mColorArray)
                .setupWithViewPager(mViewPager);
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        for (String title : mTitles) {
            mFragments.add(NewsListFragment.newInstance(title, ""));
        }
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
    }

    @Override
    public void sectionItemOpen(DatabaseDescription.NewsTypes type, int positionID) {
        Intent intent = NewsPageActivity.getIntent(MainActivity.this, type, positionID, sectionItemsCount, newsIDs, newsLinks);
        startActivity(intent);
    }

    @Override
    public void setSectionItemsCount(int count) {
        this.sectionItemsCount = count;
    }

    @Override
    public void setSectionNewsLinks(ArrayList<String> newsIDs, ArrayList<String> newsLinks) {
        this.newsIDs = newsIDs;
        this.newsLinks = newsLinks;
    }
}
