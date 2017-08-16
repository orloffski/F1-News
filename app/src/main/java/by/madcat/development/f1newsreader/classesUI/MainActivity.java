package by.madcat.development.f1newsreader.classesUI;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.Interfaces.NewsOpenListener;
import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.adapters.NewsPagesAdapter;
import by.madcat.development.f1newsreader.data.DatabaseDescription;

public class MainActivity extends AppCompatActivity implements NewsOpenListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView timerText;
    private TextView timer;
    private CoordinatorLayout coordinatorLayout;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        loadTimerViews();
    }

    private void setupViewPager(ViewPager viewPager) {
        NewsPagesAdapter adapter = new NewsPagesAdapter(getSupportFragmentManager());
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
