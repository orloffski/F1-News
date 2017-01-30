package by.madcat.development.f1newsreader.classesUI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.DBUtils;
import by.madcat.development.f1newsreader.adapters.NewsPageAdapter;
import by.madcat.development.f1newsreader.data.DatabaseDescription.*;

public class NewsPageActivity extends AppCompatActivity {

    private static final String NEWS_TYPE = "news_type";
    private static final String POSITION_ID = "position_id";
    private static final String SECTION_ITEMS_COUNT = "sections_item_count";
    private static final String SECTION_NEWS_IDS = "sections_ids";
    private static final String SECTION_NEWS_LINKS = "sections_links";

    private NewsTypes type;
    private int positionID;
    private int itemsCount;
    private ArrayList<String> ids;
    private ArrayList<String> links;
    private Uri openNewsUri;

    private NewsPageAdapter pagerAdapter;

    private String shareLink;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);

        type = NewsTypes.valueOf(getIntent().getStringExtra(NEWS_TYPE));
        positionID = getIntent().getIntExtra(POSITION_ID, 1);
        itemsCount = getIntent().getIntExtra(SECTION_ITEMS_COUNT, 0);
        links = getIntent().getStringArrayListExtra(SECTION_NEWS_LINKS);
        ids = getIntent().getStringArrayListExtra(SECTION_NEWS_IDS);

        shareLink = String.valueOf(links.get(positionID));

        toolbar = (Toolbar) findViewById(R.id.newsPageToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new NewsPageAdapter(getSupportFragmentManager(), itemsCount, ids);
        pager.setAdapter(pagerAdapter);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setCurrentItem(positionID);

        openNewsUri = News.buildNewsUri(Long.valueOf(ids.get(positionID)));
        DBUtils.setNewsRead(openNewsUri, getApplicationContext());


        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                openNewsUri = News.buildNewsUri(Long.valueOf(ids.get(position)));
                DBUtils.setNewsRead(openNewsUri, getApplicationContext());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static Intent getIntent(Context context, NewsTypes type, int positionID, int itemsCount, ArrayList<String> ids, ArrayList<String> links){
        Intent intent = new Intent(context, NewsPageActivity.class);
        intent.putExtra(NEWS_TYPE, type.toString());
        intent.putExtra(POSITION_ID, positionID);
        intent.putExtra(SECTION_ITEMS_COUNT, itemsCount);
        intent.putStringArrayListExtra(SECTION_NEWS_IDS, ids);
        intent.putStringArrayListExtra(SECTION_NEWS_LINKS, links);
        return intent;
    }

    public void setNewsData(Uri newsUri, String title){
        if(newsUri.equals(openNewsUri)) {
            toolbar.setTitleTextColor(Color.BLACK);
            toolbar.setTitle(title);
        }
    }
}
