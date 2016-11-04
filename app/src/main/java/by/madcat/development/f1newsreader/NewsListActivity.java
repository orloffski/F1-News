package by.madcat.development.f1newsreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class NewsListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NewsListFragment.NewsOpenListener{

        private int sectionItemsCount;

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

        if(savedInstanceState == null)
            openSectionNews(R.id.nav_news);
        else{
            NewsListFragment fragment = (NewsListFragment) getSupportFragmentManager().findFragmentById(R.id.content_news_list);
            int sectionID = fragment.getArguments().getInt(fragment.SECTION_ID);
            setActivityTitle(sectionID);
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
                openSectionNews(R.id.nav_news);
                break;
            case R.id.nav_memuar:
                openSectionNews(R.id.nav_memuar);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openSectionNews(int sectionID){
        NewsListFragment fragment = NewsListFragment.newInstance(sectionID);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_news_list, fragment).commit();

        setActivityTitle(sectionID);
    }

    private void setActivityTitle(int sectionID){
        String title = null;
        switch (sectionID){
            case R.id.nav_news:
                title = getString(R.string.nav_news_title);
                break;
            case R.id.nav_memuar:
                title = getString(R.string.nav_memuar_title);
                break;
        }
        setTitle(title);
    }

    @Override
    public void sectionItemOpen(int sectionID, int positionID) {
        Intent intent = NewsPageActivity.getIntent(NewsListActivity.this, sectionID, positionID, sectionItemsCount);
        startActivity(intent);
    }

    @Override
    public void setSectionItemsCount(int count) {
        this.sectionItemsCount = count;
    }
}
