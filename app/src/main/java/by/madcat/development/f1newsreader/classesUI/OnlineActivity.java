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

        NavigationPage page1 = new NavigationPage(
                getString(R.string.text_online),
                ContextCompat.getDrawable(this, R.drawable.ic_text_format_black_24dp),
                OnlineTextFragment.newInstance());

        NavigationPage page2 = new NavigationPage("Support", ContextCompat.getDrawable(this, R.drawable.ic_mail_black_24dp), OnlineTextFragment.newInstance());
        NavigationPage page3 = new NavigationPage("Billing", ContextCompat.getDrawable(this, R.drawable.ic_assessment_black_24dp), OnlineTextFragment.newInstance());
        NavigationPage page4 = new NavigationPage("Profile", ContextCompat.getDrawable(this, R.drawable.ic_person_black_24dp), OnlineTextFragment.newInstance());

        List<NavigationPage> navigationPages = new ArrayList<>();
        navigationPages.add(page1);
        navigationPages.add(page2);
        navigationPages.add(page3);
        navigationPages.add(page4);

        super.setupBottomBarHolderActivity(navigationPages);
    }

}
