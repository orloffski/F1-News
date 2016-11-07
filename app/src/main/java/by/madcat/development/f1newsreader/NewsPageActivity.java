package by.madcat.development.f1newsreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.data.DatabaseDescription.*;

public class NewsPageActivity extends AppCompatActivity {

    private static final String NEWS_TYPE = "news_type";
    private static final String POSITION_ID = "position_id";
    private static final String SECTION_ITEMS_COUNT = "sections_item_count";
    private static final String SECTION_LINKS = "sections_links";

    private NewsTypes type;
    private int positionID;
    private int itemsCount;
    private ArrayList<String> links;

    private NewsPageAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);

        type = NewsTypes.valueOf(getIntent().getStringExtra(NEWS_TYPE));
        positionID = getIntent().getIntExtra(POSITION_ID, 1);
        itemsCount = getIntent().getIntExtra(SECTION_ITEMS_COUNT, 0);
        links = getIntent().getStringArrayListExtra(SECTION_LINKS);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new NewsPageAdapter(getSupportFragmentManager(), itemsCount, type, links);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(positionID);
    }

    public static Intent getIntent(Context context, NewsTypes type, int positionID, int itemsCount, ArrayList<String> links){
        Intent intent = new Intent(context, NewsPageActivity.class);
        intent.putExtra(NEWS_TYPE, type.toString());
        intent.putExtra(POSITION_ID, positionID);
        intent.putExtra(SECTION_ITEMS_COUNT, itemsCount);
        intent.putStringArrayListExtra(SECTION_LINKS, links);
        return intent;
    }
}
