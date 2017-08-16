package by.madcat.development.f1newsreader.classesUI;

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
import by.madcat.development.f1newsreader.data.DatabaseDescription;

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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        loadTimerViews();
        updateToolbarData("Новости", StringUtils.getImageByTitle("Новости"));
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new NewsPagesAdapter(getSupportFragmentManager());
        adapter.addFragment(NewsListFragment.newInstance(DatabaseDescription.NewsTypes.NEWS, ""), "Новости");
        adapter.addFragment(NewsListFragment.newInstance(DatabaseDescription.NewsTypes.MEMUAR, ""), "Статьи");
        adapter.addFragment(NewsListFragment.newInstance(DatabaseDescription.NewsTypes.INTERVIEW, ""), "Интервью");
        adapter.addFragment(NewsListFragment.newInstance(DatabaseDescription.NewsTypes.TECH, ""), "Техника");
        adapter.addFragment(NewsListFragment.newInstance(DatabaseDescription.NewsTypes.HISTORY, ""), "История");
        adapter.addFragment(NewsListFragment.newInstance(DatabaseDescription.NewsTypes.COLUMNS, ""), "Авторские колонки");
        adapter.addFragment(NewsListFragment.newInstance(DatabaseDescription.NewsTypes.AUTOSPORT, ""), "Автоспорт");
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
    public void sectionItemOpen(DatabaseDescription.NewsTypes type, int positionID) {

    }

    @Override
    public void setSectionItemsCount(int count) {

    }

    @Override
    public void setSectionNewsLinks(ArrayList<String> newsIDs, ArrayList<String> newsLinks) {

    }
}
