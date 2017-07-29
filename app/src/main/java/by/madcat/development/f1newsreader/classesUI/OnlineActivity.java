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

        NavigationPage textOnline = new NavigationPage(
                getString(R.string.text_online),
                ContextCompat.getDrawable(this, R.drawable.ic_text_format_black_24dp),
                OnlineTextFragment.newInstance());

        NavigationPage videoOnline = new NavigationPage(
                getString(R.string.video_online),
                ContextCompat.getDrawable(this, R.drawable.ic_slow_motion_video_black_24dp),
                OnlineVideoFragment.newInstance());

        NavigationPage sessionOnline = new NavigationPage(
                getString(R.string.session_online),
                ContextCompat.getDrawable(this, R.drawable.ic_access_alarm_black_24dp),
                OnlineSessionFragment.newInstance());

        NavigationPage page4 = new NavigationPage("Profile", ContextCompat.getDrawable(this, R.drawable.ic_person_black_24dp), OnlineTextFragment.newInstance());

        List<NavigationPage> navigationPages = new ArrayList<>();
        navigationPages.add(textOnline);
        navigationPages.add(videoOnline);
        navigationPages.add(sessionOnline);
        navigationPages.add(page4);

        super.setupBottomBarHolderActivity(navigationPages);
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
