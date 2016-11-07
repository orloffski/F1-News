package by.madcat.development.f1newsreader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class NewsPageActivity extends AppCompatActivity {

    private static final String SECTION_ID = "section_id";
    private static final String POSITION_ID = "position_id";
    private static final String SECTION_ITEMS_COUNT = "sections_item_count";
    private static final String SECTION_LINKS = "sections_links";

    private int sectionID;
    private int positionID;
    private int itemsCount;
    private ArrayList<String> links;

    private NewsPageAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);

        sectionID = getIntent().getIntExtra(SECTION_ID, R.id.nav_news);
        positionID = getIntent().getIntExtra(POSITION_ID, 1);
        itemsCount = getIntent().getIntExtra(SECTION_ITEMS_COUNT, 0);
        links = getIntent().getStringArrayListExtra(SECTION_LINKS);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new NewsPageAdapter(getSupportFragmentManager(), itemsCount, sectionID, links);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(positionID);
    }

    public static Intent getIntent(Context context, int sectionID, int positionID, int itemsCount, ArrayList<String> links){
        Intent intent = new Intent(context, NewsPageActivity.class);
        intent.putExtra(SECTION_ID, sectionID);
        intent.putExtra(POSITION_ID, positionID);
        intent.putExtra(SECTION_ITEMS_COUNT, itemsCount);
        intent.putStringArrayListExtra(SECTION_LINKS, links);
        return intent;
    }
}
