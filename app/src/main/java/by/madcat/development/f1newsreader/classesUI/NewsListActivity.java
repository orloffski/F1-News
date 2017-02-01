package by.madcat.development.f1newsreader.classesUI;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.gms.analytics.ExceptionReporter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.AnalyticsTrackers.AnalyticsTrackers;
import by.madcat.development.f1newsreader.Interfaces.NewsOpenListener;
import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.data.DatabaseDescription.*;

public class NewsListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NewsOpenListener {
    public static final String LIST_FRAGMENT_NAME = "list_fragment";

    private int sectionItemsCount;
    private ArrayList<String> newsIDs;
    private ArrayList<String> newsLinks;

    private NewsListFragment fragment;
    private NavigationView navigationView;

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private ImageView imageView;

    private MaterialSearchView searchView;

    public static Intent newIntent(Context context){
        return new Intent(context, NewsListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUncaughtExceptionHandler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        appBarLayout = (AppBarLayout)findViewById(R.id.app_bar_layout);
        imageView = (ImageView) findViewById(R.id.toolbar_image);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null)
            openSectionNews(NewsTypes.NEWS);
        else{
            fragment = (NewsListFragment) getSupportFragmentManager().findFragmentById(R.id.content_news_list);
            NewsTypes type = NewsTypes.valueOf(fragment.getArguments().getString(fragment.NEWS_TYPE));
            setActivityTitle(type);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        NewsTypes type = ((NewsListFragment)getSupportFragmentManager().findFragmentByTag(LIST_FRAGMENT_NAME)).getNewsType();
        updateOnBackPressed(type);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_news:
                openSectionNews(NewsTypes.NEWS);
                break;
            case R.id.nav_memuar:
                openSectionNews(NewsTypes.MEMUAR);
                break;
            case R.id.nav_interview:
                openSectionNews(NewsTypes.INTERVIEW);
                break;
            case R.id.nav_tech:
                openSectionNews(NewsTypes.TECH);
                break;
            case R.id.nav_history:
                openSectionNews(NewsTypes.HISTORY);
                break;
            case R.id.nav_columns:
                openSectionNews(NewsTypes.COLUMNS);
                break;
            case R.id.nav_autosport:
                openSectionNews(NewsTypes.AUTOSPORT);
                break;
            case R.id.nav_settings:
                openSettings();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    private void openSectionNews(NewsTypes type){
        appBarLayout.setExpanded(true);

        fragment = NewsListFragment.newInstance(type);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_news_list, fragment, LIST_FRAGMENT_NAME).commit();

        setActivityTitle(type);
    }

    private void openSettings(){
        appBarLayout.setExpanded(false);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_news_list, new PreferencesFragment());
        transaction.addToBackStack(null);
        transaction.commit();

        setTitle(getString(R.string.settings_title));
    }

    private void setActivityTitle(NewsTypes type){
        String title = null;
        Drawable image = null;
        switch (type){
            case NEWS:
                title = getString(R.string.nav_news_title);
                image = getResources().getDrawable(R.drawable.news);
                break;
            case MEMUAR:
                title = getString(R.string.nav_memuar_title);
                image = getResources().getDrawable(R.drawable.memuar);
                break;
            case TECH:
                title = getString(R.string.nav_tech_title);
                image = getResources().getDrawable(R.drawable.tech);
                break;
            case HISTORY:
                title = getString(R.string.nav_history_title);
                image = getResources().getDrawable(R.drawable.history);
                break;
            case COLUMNS:
                title = getString(R.string.nav_columns_title);
                image = getResources().getDrawable(R.drawable.columns);
                break;
            case AUTOSPORT:
                title = getString(R.string.nav_autosport_title);
                image = getResources().getDrawable(R.drawable.autosport);
                break;
            case INTERVIEW:
                title = getString(R.string.nav_interview_title);
                image = getResources().getDrawable(R.drawable.interview);
                break;
        }
        collapsingToolbarLayout.setTitle(title);
        imageView.setImageBitmap(((BitmapDrawable)image).getBitmap());
    }

    @Override
    public void sectionItemOpen(NewsTypes type, int positionID) {
        Intent intent = NewsPageActivity.getIntent(NewsListActivity.this, type, positionID, sectionItemsCount, newsIDs, newsLinks);
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

    private void updateOnBackPressed(NewsTypes type){
        setActivityTitle(type);

        switch (type){
            case NEWS:
                navigationView.setCheckedItem(R.id.nav_news);
                break;
            case MEMUAR:
                navigationView.setCheckedItem(R.id.nav_memuar);
                break;
            case INTERVIEW:
                navigationView.setCheckedItem(R.id.nav_interview);
                break;
            case TECH:
                navigationView.setCheckedItem(R.id.nav_tech);
                break;
            case HISTORY:
                navigationView.setCheckedItem(R.id.nav_history);
                break;
            case COLUMNS:
                navigationView.setCheckedItem(R.id.nav_columns);
                break;
            case AUTOSPORT:
                navigationView.setCheckedItem(R.id.nav_autosport);
                break;
        }
    }

    private void setUncaughtExceptionHandler(){
        Thread.UncaughtExceptionHandler myHandler = new ExceptionReporter(
                ((AnalyticsTrackers)getApplication()).getDefaultTracker(),
                Thread.getDefaultUncaughtExceptionHandler(),
                getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(myHandler);
    }
}
