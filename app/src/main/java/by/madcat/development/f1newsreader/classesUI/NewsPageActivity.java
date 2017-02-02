package by.madcat.development.f1newsreader.classesUI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.DBUtils;
import by.madcat.development.f1newsreader.adapters.NewsPageAdapter;
import by.madcat.development.f1newsreader.data.DatabaseDescription.News;
import by.madcat.development.f1newsreader.data.DatabaseDescription.NewsTypes;

public class NewsPageActivity extends AppCompatActivity {

    private static final String NEWS_TYPE = "news_type";
    private static final String POSITION_ID = "position_id";
    private static final String SECTION_ITEMS_COUNT = "sections_item_count";
    private static final String SECTION_NEWS_IDS = "sections_ids";
    private static final String SECTION_NEWS_LINKS = "sections_links";

    private int positionID;
    private int itemsCount;
    private ArrayList<String> ids;
    private ArrayList<String> links;
    private Uri openNewsUri;

    private NewsPageAdapter pagerAdapter;

    private ShareActionProvider mShareActionProvider;
    private String shareLink;

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        toolbar = (Toolbar) findViewById(R.id.toolbar_page);
        setSupportActionBar(toolbar);

        positionID = getIntent().getIntExtra(POSITION_ID, 1);
        itemsCount = getIntent().getIntExtra(SECTION_ITEMS_COUNT, 0);
        links = getIntent().getStringArrayListExtra(SECTION_NEWS_LINKS);
        ids = getIntent().getStringArrayListExtra(SECTION_NEWS_IDS);
        shareLink = String.valueOf(links.get(positionID));

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout_page);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedTextAppearance);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedTextAppearance);

        imageView = (ImageView) findViewById(R.id.toolbar_image_page);

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
                setShareIntent(createShareIntent(links.get(position)));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_page_menu, menu);

        MenuItem shareItem = menu.findItem(R.id.share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        setShareIntent(createShareIntent(shareLink));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.back_press:
                onBackPressed();
                return false;
            case R.id.share:
                return false;
        }
        return super.onOptionsItemSelected(item);
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

    public void setNewsData(Uri newsUri, String title, Bitmap image, String link, String date){
        if(newsUri.equals(openNewsUri)) {
            imageView.setImageBitmap(image);
            collapsingToolbarLayout.setTitle(title);

            setShareIntent(createShareIntent(link));
        }
    }

    private void setShareIntent(Intent shareIntent){
        if(mShareActionProvider != null)
            mShareActionProvider.setShareIntent(shareIntent);
    }

    private Intent createShareIntent(String link){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, link);
        return shareIntent;
    }
}
