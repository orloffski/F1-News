package by.madcat.development.f1newsreader.classesUI;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.data.DatabaseDescription.*;
import by.madcat.development.f1newsreader.dataInet.InternetDataRouting;
import by.madcat.development.f1newsreader.dataInet.LoadLinkListTask;

public class NewsListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NewsListFragment.NewsOpenListener{

    private int sectionItemsCount;
    private ArrayList<String> links;
    private FloatingActionButton fabLoadNews;

    private InternetDataRouting dataRouting;
    private LoadLinkListTask loadLinksTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fabLoadNews = (FloatingActionButton) findViewById(R.id.fabLoadNews);
        fabLoadNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkAvailable()) {
                    Toast.makeText(NewsListActivity.this, getString(R.string.network_not_available), Toast.LENGTH_SHORT).show();
                }else {
                    dataRouting = InternetDataRouting.getInstance();
                    loadLinksTask = new LoadLinkListTask(dataRouting.getRoutingMap(), getApplicationContext());
                    loadLinksTask.execute();
                }
            }
        });

        if(savedInstanceState == null)
            openSectionNews(NewsTypes.NEWS);
        else{
            NewsListFragment fragment = (NewsListFragment) getSupportFragmentManager().findFragmentById(R.id.content_news_list);
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openSectionNews(NewsTypes type){
        NewsListFragment fragment = NewsListFragment.newInstance(type);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_news_list, fragment).commit();

        setActivityTitle(type);
    }

    private void setActivityTitle(NewsTypes type){
        String title = null;
        switch (type){
            case NEWS:
                title = getString(R.string.nav_news_title);
                break;
            case MEMUAR:
                title = getString(R.string.nav_memuar_title);
                break;
        }
        setTitle(title);
    }

    @Override
    public void sectionItemOpen(NewsTypes type, int positionID) {
        Intent intent = NewsPageActivity.getIntent(NewsListActivity.this, type, positionID, sectionItemsCount, links);
        startActivity(intent);
    }

    @Override
    public void setSectionItemsCount(int count) {
        this.sectionItemsCount = count;
    }

    @Override
    public void setSectionNewsLinks(ArrayList<String> links) {
        this.links = links;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
