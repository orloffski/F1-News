package by.madcat.development.f1newsreader.classesUI;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.PreferencesUtils;
import me.riddhimanadib.library.BottomBarHolderActivity;
import me.riddhimanadib.library.NavigationPage;

public class OnlineActivity extends BottomBarHolderActivity {

    private boolean autoscroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavigationPage weekendInfo = new NavigationPage(
                getString(R.string.weekend_info),
                ContextCompat.getDrawable(this, R.drawable.weekend_info_black),
                WeekendInfoFragment.newInstance());

        NavigationPage textOnline = new NavigationPage(
                getString(R.string.text_online),
                ContextCompat.getDrawable(this, R.drawable.text_online_black),
                OnlineTextFragment.newInstance());

        NavigationPage sessionOnline = new NavigationPage(
                getString(R.string.session_online),
                ContextCompat.getDrawable(this, R.drawable.timings_online_black),
                OnlineSessionFragment.newInstance());

        NavigationPage onLap = new NavigationPage(
                getString(R.string.on_lap),
                ContextCompat.getDrawable(this, R.drawable.onlap_black),
                OnLapTranslationFragment.newInstance());

        List<NavigationPage> navigationPages = new ArrayList<>();
        navigationPages.add(weekendInfo);
        navigationPages.add(textOnline);
        navigationPages.add(sessionOnline);
        navigationPages.add(onLap);

        super.setupBottomBarHolderActivity(navigationPages);

        setTitle(PreferencesUtils.getNextGpCountry(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.online_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.autoscroll).setEnabled(autoscroll);
        menu.findItem(R.id.autoscroll).setChecked(PreferencesUtils.getAutoscrollingFlag(this));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.autoscroll:
                if(item.isChecked()){
                    PreferencesUtils.disableAutoScrolling(getApplicationContext());
                    item.setChecked(false);
                }else{
                    PreferencesUtils.enableAutoScrolling(getApplicationContext());
                    item.setChecked(true);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateAutoscrollMenu(boolean isNeed){
        autoscroll = isNeed;
    }
}
