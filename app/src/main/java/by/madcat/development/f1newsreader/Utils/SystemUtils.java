package by.madcat.development.f1newsreader.Utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.service.notification.StatusBarNotification;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Services.MoveToSdTask;
import by.madcat.development.f1newsreader.Services.ReminderService;

public class SystemUtils {
    public static final String GP_DATA_COUNTRY = "gp_country";
    public static final String GP_DATA_DATE = "gp_date";
    public static final String GP_DATA_TIMESTAMP = "gp_timestamp";

    public final static String REMINDER_RINGTONE = "reminder_ringtone";

    public final static String WEEKEND_TITLE = "weekend_title";
    public final static String WEEKEND_IMAGE = "weekend_image";
    public final static String WEEKEND_1ST_DAY_TITLE = "weekend_1st_day_title";
    public final static String WEEKEND_2ND_DAY_TITLE = "weekend_2nd_day_title";
    public final static String WEEKEND_3RD_DAY_TITLE = "weekend_3rd_day_title";
    public final static String WEEKEND_1ST_DAY_TEXT = "weekend_1st_day_text";
    public final static String WEEKEND_2ND_DAY_TEXT = "weekend_2nd_day_text";
    public final static String WEEKEND_3RD_DAY_TEXT = "weekend_3rd_day_text";

    public static final String IMAGE_PATH = "F1NewsImages";
    public static final String APP_ON_SD_PATH = "F1NewsReader";

    public static final String SAVE_IMAGES_ON_SD = "move_pic_to_sd";

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static final boolean isNetworkAvailableAndConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworcConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworcConnected;
    }

    public static String getNextGpData(Context context){
        return getNextGpCountry(context)
                + "\n"
                + getNextGpDate(context);
    }

    public static String getNextGpCountry(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(GP_DATA_COUNTRY, "");
    }

    public static String getNextGpDate(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(GP_DATA_DATE, "");
    }

    public static String getNextGpTimeout(Context context){
        int timeout = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("reminder_interval", "0"));
        List<String> intervals = Arrays.asList(context.getResources().getStringArray(R.array.intervals_milliseconds));

        int index = intervals.indexOf(String.valueOf(timeout));

        String[] intervals_titles = context.getResources().getStringArray(R.array.intervals_title);

        return intervals_titles[index];
    }

    public static int getNextGpTime(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(GP_DATA_TIMESTAMP, 0);
    }

    public static void saveTimersData(String country, String date, int timestamp, Context context){
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);

        editor.putString(SystemUtils.GP_DATA_COUNTRY, country);
        editor.putString(SystemUtils.GP_DATA_DATE, date);
        editor.putInt(SystemUtils.GP_DATA_TIMESTAMP, timestamp);
        editor.commit();
    }

    public static void saveRingtoneData(String ringtonUri, Context context){
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putString(REMINDER_RINGTONE, ringtonUri);
        editor.commit();
    }

    public static String loadRingtoneData(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(REMINDER_RINGTONE, "");
    }

    public static String loadRingtoneTitle(Context context){
        String ringtoneUriString = PreferenceManager.getDefaultSharedPreferences(context).getString(REMINDER_RINGTONE, "");
        Uri ringtoneUri = Uri.parse(ringtoneUriString);
        Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
        return ringtone.getTitle(context);
    }

    public static void saveWeekendData(String weekendTitle, String weekendImageLink, Map<String, String> weekendData, Context context){
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putString(WEEKEND_TITLE, weekendTitle);
        try {
            editor.putString(WEEKEND_IMAGE, loadWeekendImage(weekendImageLink, context));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int counter = 1;

        for(Map.Entry entry : weekendData.entrySet()){
            switch (counter){
                case 1:
                    editor.putString(WEEKEND_1ST_DAY_TITLE, entry.getKey().toString());
                    editor.putString(WEEKEND_1ST_DAY_TEXT, entry.getValue().toString());
                    break;
                case 2:
                    editor.putString(WEEKEND_2ND_DAY_TITLE, entry.getKey().toString());
                    editor.putString(WEEKEND_2ND_DAY_TEXT, entry.getValue().toString());
                    break;
                case 3:
                    editor.putString(WEEKEND_3RD_DAY_TITLE, entry.getKey().toString());
                    editor.putString(WEEKEND_3RD_DAY_TEXT, entry.getValue().toString());
                    break;
            }
            counter++;
        }

        editor.commit();
    }

    public static String getWeekendTitle(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_TITLE, "");
    }

    public static String getWeekendImage(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_IMAGE, "");
    }

    public static Map<String, String> getWeekendData(Context context){
        Map<String, String> weekendData = new LinkedHashMap<>();

        weekendData.put(
                PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_1ST_DAY_TITLE, ""),
                PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_1ST_DAY_TEXT, "")
        );

        weekendData.put(
                PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_2ND_DAY_TITLE, ""),
                PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_2ND_DAY_TEXT, "")
        );

        weekendData.put(
                PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_3RD_DAY_TITLE, ""),
                PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_3RD_DAY_TEXT, "")
        );

        return weekendData;
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.edit();
    }

    private static String loadWeekendImage(String imageUrl, Context context) throws IOException {
        File sdPath = context.getFilesDir();

        sdPath = new File(sdPath.getAbsolutePath() + "/" + IMAGE_PATH);

        if(!sdPath.exists())
            sdPath.mkdirs();

        String filename = StringUtils.getImageNameFromURL(imageUrl);
        File imageOnMemory = new File(sdPath, filename);

        Bitmap image;

        OutputStream fOut;
        fOut = new FileOutputStream(imageOnMemory);

        InputStream in = new URL(imageUrl).openStream();
        if(in != null) {
            image = BitmapFactory.decodeStream(in);
            if(image != null)
                image.compress(Bitmap.CompressFormat.JPEG, 55, fOut);
            else
                filename = "";
        }

        fOut.flush();
        fOut.close();
        in.close();

        return filename;
    }

    public static boolean imagesOnSdCard(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SAVE_IMAGES_ON_SD, false);
    }

    public static String getImagesPath(Context context){
        String imagesPath = imagesPathInMemory(context);

        if(imagesOnSdCard(context)){
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
        if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("reminder_on", false)){
            int reminderInterval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("reminder_interval", "0"));
            boolean reminderVibration = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("reminder_vibration", false);
            String reminderRingtoneUri = loadRingtoneData(context);

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

    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public static void stopOldService(AlarmManager alarmManager, Intent i, Context context){
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        alarmManager.cancel(pi);
        pi.cancel();
    }

    public static int getNumberInIssetNotification(int id, NotificationManager notificationManager){
        int number = 0;
        
        if (Build.VERSION.SDK_INT >= 23) {
            StatusBarNotification[] statusBarNotifications = notificationManager.getActiveNotifications();

            for(StatusBarNotification statusBarNotification : statusBarNotifications)
                if(statusBarNotification.getId() == id){
                    Notification notification = statusBarNotification.getNotification();
                    number = notification.number;
                }
        }

        return number;
    }
}
