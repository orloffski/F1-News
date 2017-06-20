package by.madcat.development.f1newsreader.classesUI;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.RingtonePreference;
import android.support.design.widget.Snackbar;

import com.github.machinarius.preferencefragment.PreferenceFragment;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Services.BackgroundLoadNewsService;
import by.madcat.development.f1newsreader.Services.ReminderService;
import by.madcat.development.f1newsreader.Utils.SystemUtils;
import by.madcat.development.f1newsreader.Utils.ViewCreator;
import by.madcat.development.f1newsreader.dataInet.NewsLinkListToLoad;

public class PreferencesFragment extends PreferenceFragment {
    public PreferencesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        // скрытие прочитанных новостей
        final CheckBoxPreference hide_read_news = (CheckBoxPreference)findPreference("hide_read_news");
        hide_read_news.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ((NewsListFragment)getActivity().getSupportFragmentManager().findFragmentByTag(NewsListActivity.LIST_FRAGMENT_NAME)).updateNewsList();
                return false;
            }
        });

        // видео в полноэкранном режиме
        final CheckBoxPreference fullscreen_video = (CheckBoxPreference) findPreference("fullscreen_video");

        // автообновление новостей
        final CheckBoxPreference refresh_interval_on = (CheckBoxPreference)findPreference("refresh_interval_on");
        final ListPreference refresh_interval = (ListPreference)findPreference("refresh_interval");
        refresh_interval.setEnabled(refresh_interval_on.isChecked());

        refresh_interval_on.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                refresh_interval.setEnabled(refresh_interval_on.isChecked());

                int defaultValue = Integer.parseInt(refresh_interval.getValue());

                if(refresh_interval_on.isChecked()) {
                    SystemUtils.addServiceToAlarmManager(getActivity(), true);
                }else{
                    SystemUtils.addServiceToAlarmManager(getActivity(), false);
                    NewsLinkListToLoad.getInstance(null, getContext()).setLock(false);
                }

                return false;
            }
        });

        refresh_interval.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SystemUtils.addServiceToAlarmManager(getActivity(), false);
                SystemUtils.addServiceToAlarmManager(getActivity(), true);
                return true;
            }
        });

        // напоминание о гран-при
        final CheckBoxPreference reminder_on = (CheckBoxPreference) findPreference("reminder_on");
        final ListPreference reminder_interval = (ListPreference)findPreference("reminder_interval");
        final RingtonePreference reminder_ringtone = (RingtonePreference)findPreference("reminder_ringtone");
        final CheckBoxPreference reminder_vibro = (CheckBoxPreference)findPreference("reminder_vibration");

        reminder_interval.setEnabled(reminder_on.isChecked());
        reminder_ringtone.setEnabled(reminder_on.isChecked());
        reminder_vibro.setEnabled(reminder_on.isChecked());

        reminder_ringtone.setSummary(SystemUtils.loadRingtoneTitle(getContext()));

        reminder_on.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(!reminder_on.isChecked()){
                    cancelReminder();
                    reminder_interval.setEnabled(reminder_on.isChecked());
                    reminder_ringtone.setEnabled(reminder_on.isChecked());
                    reminder_vibro.setEnabled(reminder_on.isChecked());
                }else{
                    if(SystemUtils.getNextGpTime(getContext()) == 0){
                        ViewCreator.sendSnackbarMessage(
                                ((NewsListActivity)getActivity()).getCoordinatorLayout(),
                                getString(R.string.reminder_data_null),
                                Snackbar.LENGTH_SHORT);
                        cancelReminder();
                        reminder_on.setChecked(false);
                    }else{
                        int defaultValue = Integer.parseInt(reminder_interval.getValue());
                        boolean vibroValue = reminder_vibro.isChecked();
                        String ringtoneUri = SystemUtils.loadRingtoneData(getContext());

                        ReminderService.setServiceAlarm(getActivity(), true, defaultValue, vibroValue, ringtoneUri);

                        reminder_interval.setEnabled(reminder_on.isChecked());
                        reminder_ringtone.setEnabled(reminder_on.isChecked());
                        reminder_vibro.setEnabled(reminder_on.isChecked());
                    }
                }

                return false;
            }
        });

        reminder_interval.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String value = newValue.toString();
                int timePause = Integer.parseInt(value);
                String ringtoneUri = SystemUtils.loadRingtoneData(getContext());

                ReminderService.setServiceAlarm(getActivity(), false, 0, false, null);
                ReminderService.setServiceAlarm(getActivity(), true, timePause, reminder_vibro.isChecked(), ringtoneUri);
                return true;
            }
        });

        reminder_vibro.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int timePause = Integer.parseInt(reminder_interval.getValue());
                String ringtoneUri = SystemUtils.loadRingtoneData(getContext());

                ReminderService.setServiceAlarm(getActivity(), false, 0, false, null);
                ReminderService.setServiceAlarm(getActivity(), true, timePause, reminder_vibro.isChecked(), ringtoneUri);

                return false;
            }
        });

        // перенос изображений на карту памяти
        final CheckBoxPreference move_to_sd = (CheckBoxPreference)findPreference("move_pic_to_sd");

        if(!SystemUtils.externalSdIsMounted())
            move_to_sd.setSummary(getString(R.string.move_pictures_to_sd_summary_prev));

        move_to_sd.setEnabled(SystemUtils.externalSdIsMounted());
        move_to_sd.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(NewsLinkListToLoad.getInstance(null, getContext()).isLock()){
                    ViewCreator.sendSnackbarMessage(
                            ((NewsListActivity)getActivity()).getCoordinatorLayout(),
                            getString(R.string.news_load_running),
                            Snackbar.LENGTH_LONG);
                    move_to_sd.setChecked(!move_to_sd.isChecked());
                }else{
                    if(move_to_sd.isChecked()){
                        SystemUtils.moveImages(getActivity(),
                                SystemUtils.imagesPathInMemory(getContext()),
                                SystemUtils.imagesPathOnSd(),
                                true);
                    }else{
                        SystemUtils.moveImages(getActivity(),
                                SystemUtils.imagesPathOnSd(),
                                SystemUtils.imagesPathInMemory(getContext()),
                                false);
                    }
                }

                return false;
            }
        });
    }

    private void cancelReminder(){
        ReminderService.setServiceAlarm(getActivity(), false, 0, false, null);
    }

    public void updateReminderRingtone(Intent data){
        String ringTonePath = "";
        Ringtone ringtone;
        String title = "";
        RingtonePreference reminder_ringtone = (RingtonePreference)findPreference("reminder_ringtone");

        Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

        if (uri != null) {
            ringTonePath = uri.toString();
            ringtone = RingtoneManager.getRingtone(getContext(), uri);
            title = ringtone.getTitle(getContext());

            SystemUtils.saveRingtoneData(ringTonePath, getContext());

            reminder_ringtone.setSummary(title);
        }else{
            SystemUtils.saveRingtoneData("", getContext());
            reminder_ringtone.setSummary(getString(R.string.ringtone_null));
        }

        ReminderService.setServiceAlarm(getActivity(), false, 0, false, null);
        ReminderService.setServiceAlarm(getActivity()
                , true
                , Integer.parseInt(((ListPreference)findPreference("reminder_interval")).getValue())
                , ((CheckBoxPreference)findPreference("reminder_vibration")).isChecked()
                , ringTonePath);
    }
}
