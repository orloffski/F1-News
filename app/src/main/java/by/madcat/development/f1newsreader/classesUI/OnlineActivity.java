package by.madcat.development.f1newsreader.classesUI;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import by.madcat.development.f1newsreader.R;
import me.riddhimanadib.library.BottomBarHolderActivity;
import me.riddhimanadib.library.NavigationPage;

public class OnlineActivity extends BottomBarHolderActivity {

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

        NavigationPage page3 = new NavigationPage("Billing", ContextCompat.getDrawable(this, R.drawable.ic_assessment_black_24dp), OnlineTextFragment.newInstance());
        NavigationPage page4 = new NavigationPage("Profile", ContextCompat.getDrawable(this, R.drawable.ic_person_black_24dp), OnlineTextFragment.newInstance());

        List<NavigationPage> navigationPages = new ArrayList<>();
        navigationPages.add(textOnline);
        navigationPages.add(videoOnline);
        navigationPages.add(page3);
        navigationPages.add(page4);

        super.setupBottomBarHolderActivity(navigationPages);
    }

}
