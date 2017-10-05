package by.madcat.development.f1newsreader.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.service.notification.StatusBarNotification;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.services.BackgroundLoadNewsService;
import by.madcat.development.f1newsreader.services.MoveToSdTask;
import by.madcat.development.f1newsreader.services.ReminderService;

import static by.madcat.development.f1newsreader.utils.PreferencesUtils.IMAGE_PATH;

public class SystemUtils {
    public static final String APP_ON_SD_PATH = "F1NewsReader";

    public static final boolean isNetworkAvailableAndConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworcConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworcConnected;
    }

    public static String getNextGpData(Context context){
        return PreferencesUtils.getNextGpCountry(context)
                + "\n"
                + PreferencesUtils.getNextGpDate(context);
    }

    public static String getNextGpTimeout(Context context){
        int timeout = Integer.parseInt(PreferencesUtils.getReminderInterval(context));
        List<String> intervals = Arrays.asList(context.getResources().getStringArray(R.array.intervals_milliseconds));

        int index = intervals.indexOf(String.valueOf(timeout));

        String[] intervals_titles = context.getResources().getStringArray(R.array.intervals_title);

        return intervals_titles[index];
    }

    public static String loadRingtoneTitle(Context context){
        String ringtoneUriString = PreferencesUtils.getRingtoneTitle(context);
        Uri ringtoneUri = Uri.parse(ringtoneUriString);
        Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
        return ringtone.getTitle(context);
    }

    public static String getImagesPath(Context context){
        String imagesPath = imagesPathInMemory(context);

        if(PreferencesUtils.imagesOnSdCard(context)){
            if(externalSdIsMounted()) {
                imagesPath = imagesPathOnSd();
            }
        }

        return imagesPath;
    }

    public static boolean externalSdIsMounted(){
        String s = System.getenv("SECONDARY_STORAGE");
        if(s == null)
            return false;

        return true;
    }

    public static int getResizedImageHeight(Bitmap bitmap){
        int screenWidth = getScreenWidth();
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        float scale = getScale(originalWidth, screenWidth);

        return (int)SystemUtils.getImageScaleHeight(originalHeight, scale);
    }

    public static int getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static float getScale(float original, float resized){
        return resized / original;
    }

    public static float getImageScaleHeight(float originalHeight, float scale){
        return originalHeight * scale;
    }

    public static void updateReminder(Context context){
        if(PreferencesUtils.reminderIntervalIsOn(context)){
            int reminderInterval = Integer.parseInt(PreferencesUtils.getReminderInterval(context));
            boolean reminderVibration = PreferencesUtils.reminderVibrationIsOn(context);
            String reminderRingtoneUri = PreferencesUtils.loadRingtoneData(context);

            ReminderService.setServiceAlarm(context, false, 0, false, null);
            ReminderService.setServiceAlarm(context, true, reminderInterval, reminderVibration, reminderRingtoneUri);
        }
    }

    public static String imagesPathInMemory(Context context){
        return context.getFilesDir().getAbsolutePath() + "/" + IMAGE_PATH + "/";
    }

    public static String imagesPathOnSd(){
        return Environment.getExternalStorageDirectory().toString() + "/" + APP_ON_SD_PATH + "/" + IMAGE_PATH + "/";
    }

    public static void moveImages(Context context, String pathFrom, String pathTo, boolean toSd){
        MoveToSdTask move_to_sd = new MoveToSdTask(context, pathFrom, pathTo, toSd);
        move_to_sd.execute();
    }

    public static int getFilesCountInDir(String path){
        File folder = new File(path);
        int count;

        if(!folder.exists())
            count = 0;
        else
            count = folder.listFiles().length;
        return count;
    }

    public static void copyFile(File fileFrom, File fileTo) throws IOException {
        if (!fileTo.getParentFile().exists())
            fileTo.getParentFile().mkdirs();

        if (!fileTo.exists()) {
            fileTo.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(fileFrom).getChannel();
            destination = new FileOutputStream(fileTo).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }

    }

    public static void deleteFiles(String path){
        File[] files = new File(path).listFiles();

        if(files.length > 0)
            for(int i = 0; i < files.length; i++){
                new File(path + "/" + files[i].getName()).delete();
            }

    }

    public static int getNumberInIssetNotificationsCount(int id, NotificationManager notificationManager, Context context){
        int number = 0;
        
        if (Build.VERSION.SDK_INT >= 23) {
            StatusBarNotification[] statusBarNotifications = notificationManager.getActiveNotifications();

            for(StatusBarNotification statusBarNotification : statusBarNotifications)
                if(statusBarNotification.getId() == id){
                    Notification notification = statusBarNotification.getNotification();
                    number = notification.number;
                }
        }else if(Build.VERSION.SDK_INT < 23 && Build.VERSION.SDK_INT >= 18){
            number = PreferencesUtils.getNotificationsCount(context);
        }

        return number;
    }

    public static void addServiceToAlarmManager(Context context, boolean serviceStart, int timePause){
        Intent i = BackgroundLoadNewsService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, BackgroundLoadNewsService.SERVICE_INTENT_ID, i, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if(serviceStart) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MILLISECOND, timePause);

            if(Build.VERSION.SDK_INT >= 23){
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            }else if (Build.VERSION.SDK_INT >= 19){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            }else{
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            }
        }else{
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }
}
