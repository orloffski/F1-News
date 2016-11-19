package by.madcat.development.f1newsreader.classesUI;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;

import com.github.machinarius.preferencefragment.PreferenceFragment;

import by.madcat.development.f1newsreader.R;

public class PreferencesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        final CheckBoxPreference hide_read_news = (CheckBoxPreference)findPreference("hide_read_news");
        hide_read_news.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ((NewsListFragment)getActivity().getSupportFragmentManager().findFragmentByTag(NewsListActivity.LIST_FRAGMENT_NAME)).updateNewsList();
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
                return false;
            }
        });
    }
}
