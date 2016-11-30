package by.madcat.development.f1newsreader.classesUI;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;

import com.github.machinarius.preferencefragment.PreferenceFragment;

import by.madcat.development.f1newsreader.AnalyticsTrackers.AnalyticsTrackers;
import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Services.BackgroundLoadNewsService;

public class PreferencesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        final AnalyticsTrackers analyticsTrackers = (AnalyticsTrackers)getActivity().getApplication();

        final CheckBoxPreference hide_read_news = (CheckBoxPreference)findPreference("hide_read_news");
        hide_read_news.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ((NewsListFragment)getActivity().getSupportFragmentManager().findFragmentByTag(NewsListActivity.LIST_FRAGMENT_NAME)).updateNewsList();
                analyticsTrackers.trackEvent("Options", "change", "hide read news is " + hide_read_news.isChecked());
                return false;
            }
        });

        final CheckBoxPreference refresh_interval_on = (CheckBoxPreference)findPreference("refresh_interval_on");
        final ListPreference refresh_interval = (ListPreference)findPreference("refresh_interval");
        refresh_interval.setEnabled(refresh_interval_on.isChecked());

        refresh_interval_on.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                refresh_interval.setEnabled(refresh_interval_on.isChecked());

                analyticsTrackers.trackEvent("Options", "change", "refresh interval is " + refresh_interval_on.isChecked());

                int defaultValue = Integer.parseInt(refresh_interval.getValue());

                if(refresh_interval_on.isChecked()) {
                    BackgroundLoadNewsService.setServiceAlarm(getActivity(), true, defaultValue);
                }else{
                    BackgroundLoadNewsService.setServiceAlarm(getActivity(), false, 0);
                }

                return false;
            }
        });

        refresh_interval.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String value = o.toString();
                int timePause = Integer.parseInt(value);

                analyticsTrackers.trackEvent("Options", "change", "refresh interval: " + timePause);

                BackgroundLoadNewsService.setServiceAlarm(getActivity(), false, 0);
                BackgroundLoadNewsService.setServiceAlarm(getActivity(), true, timePause);
                return true;
            }
        });
    }
}
