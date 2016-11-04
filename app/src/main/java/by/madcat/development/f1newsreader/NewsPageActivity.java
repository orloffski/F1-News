package by.madcat.development.f1newsreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class NewsPageActivity extends AppCompatActivity {

    private static final String SECTION_ID = "section_id";
    private static final String POSITION_ID = "position_id";
    private static final String SECTION_ITEMS_COUNT = "sections_item_count";

    private int sectionID;
    private int positionID;
    private int itemsCount;

    private NewsPageAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);

        sectionID = getIntent().getIntExtra(SECTION_ID, 0);
        positionID = getIntent().getIntExtra(POSITION_ID, 0);
        itemsCount = getIntent().getIntExtra(SECTION_ITEMS_COUNT, 0);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new NewsPageAdapter(getSupportFragmentManager(), itemsCount);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(positionID);


    }

    public static Intent getIntent(Context context, int sectionID, int positionID, int itemsCount){
        Intent intent = new Intent(context, NewsPageActivity.class);
        intent.putExtra(SECTION_ID, sectionID);
        intent.putExtra(POSITION_ID, positionID);
        intent.putExtra(SECTION_ITEMS_COUNT, itemsCount);
        return intent;
    }

    public void setActivityTitle(int sectionID, int position) {
        String sectionName = "";
        switch (sectionID){
            case R.id.nav_news:
                sectionName = getString(R.string.nav_news_title);
                break;
            case R.id.nav_memuar:
                sectionName = getString(R.string.nav_memuar_title);
                break;
        }
        setTitle(sectionName + " " + position);
    }

    private class NewsPageAdapter extends FragmentStatePagerAdapter {

        private int count;

        public NewsPageAdapter(FragmentManager fm, int itemsCount) {
            super(fm);
            this.count = itemsCount;
        }

        @Override
        public Fragment getItem(int position) {
            return NewsPageFragment.newInstance(sectionID, position);
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            switch (sectionID){
                case R.id.nav_news:
                    title = getString(R.string.nav_news_title);
                    break;
                case R.id.nav_memuar:
                    title = getString(R.string.nav_memuar_title);
                    break;
            }
            return title + " " + position;
        }
    }
}
