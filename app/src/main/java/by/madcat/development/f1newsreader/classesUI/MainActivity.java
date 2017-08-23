package by.madcat.development.f1newsreader.classesUI;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.Interfaces.NewsOpenListener;
import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.StringUtils;
import by.madcat.development.f1newsreader.adapters.NewsPagesAdapter;
import by.madcat.development.f1newsreader.data.DatabaseDescription.NewsTypes;

import static by.madcat.development.f1newsreader.data.DatabaseDescription.NewsTypes.AUTOSPORT;
import static by.madcat.development.f1newsreader.data.DatabaseDescription.NewsTypes.COLUMNS;
import static by.madcat.development.f1newsreader.data.DatabaseDescription.NewsTypes.HISTORY;
import static by.madcat.development.f1newsreader.data.DatabaseDescription.NewsTypes.INTERVIEW;
import static by.madcat.development.f1newsreader.data.DatabaseDescription.NewsTypes.MEMUAR;
import static by.madcat.development.f1newsreader.data.DatabaseDescription.NewsTypes.NEWS;
import static by.madcat.development.f1newsreader.data.DatabaseDescription.NewsTypes.TECH;

public class MainActivity extends AppCompatActivity implements NewsOpenListener{

    private NewsPagesAdapter adapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView backdrop;
    private TextView timerText;
    private TextView timer;
    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Toolbar toolbar;

    private NewsListFragment fragment;
    private ArrayList<String> newsIDs;
    private ArrayList<String> newsLinks;
    private int sectionItemsCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_list);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedTextAppearance);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedTextAppearance);
        backdrop = (ImageView) findViewById(R.id.backdrop);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String title = String.valueOf(adapter.getPageTitle(position));
                updateToolbarData(title, StringUtils.getImageByTitle(title));

                initFragmentData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        loadTimerViews();
        updateToolbarData(NEWS.toString(), StringUtils.getImageByTitle("Новости"));
    }

    public void initFragmentData(int pageID){
        if(pageID == viewPager.getCurrentItem()) {

            fragment = ((NewsListFragment) viewPager.getAdapter().instantiateItem(viewPager, pageID));

            if (fragment != null) {
                sectionItemsCount = fragment.getSectionItemsCount();
                newsIDs = fragment.getNewsIDs();
                newsLinks = fragment.getNewsLinks();
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new NewsPagesAdapter(getSupportFragmentManager());
        adapter.addFragment(NewsListFragment.newInstance(NEWS.toString(), ""), "Новости");
        adapter.addFragment(NewsListFragment.newInstance(MEMUAR.toString(), ""), "Статьи");
        adapter.addFragment(NewsListFragment.newInstance(INTERVIEW.toString(), ""), "Интервью");
        adapter.addFragment(NewsListFragment.newInstance(TECH.toString(), ""), "Техника");
        adapter.addFragment(NewsListFragment.newInstance(HISTORY.toString(), ""), "История");
        adapter.addFragment(NewsListFragment.newInstance(COLUMNS.toString(), ""), "Авторские колонки");
        adapter.addFragment(NewsListFragment.newInstance(AUTOSPORT.toString(), ""), "Автоспорт");
        viewPager.setAdapter(adapter);
    }

    public View getTimerLink(){
        return timer;
    }

    public View getTimerTextLink(){
        return timerText;
    }

    public CoordinatorLayout getCoordinatorLayout(){
        return this.coordinatorLayout;
    }

    private void loadTimerViews(){
        timerText = new TextView(this);
        timer = new TextView(this);
    }

    private void updateToolbarData(String title, int imageR){
        collapsingToolbarLayout.setTitle(title);
        Glide.with(MainActivity.this).load(imageR).into(backdrop);

    }

    @Override
    public void sectionItemOpen(NewsTypes type, int positionID) {
        Intent intent = NewsPageActivity.getIntent(MainActivity.this, type, positionID, sectionItemsCount, newsIDs, newsLinks);
        startActivity(intent);
    }
}
