package by.madcat.development.f1newsreader.Utils;

import android.content.Context;
import android.database.Cursor;

import by.madcat.development.f1newsreader.data.DatabaseDescription;
import by.madcat.development.f1newsreader.data.F1NewsReaderDatabaseHelper;

public class DBUtils {
    public static boolean checkIssetNewsLinkInDB(Context context, String link){
        // check link to issue news in DB
        String selection = DatabaseDescription.News.COLUMN_LINK_NEWS + "=?";
        String[] selectionArgs = new String[]{link};

        F1NewsReaderDatabaseHelper helper = new F1NewsReaderDatabaseHelper(context);
        Cursor cursor = helper.getWritableDatabase().query(DatabaseDescription.News.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if(cursor.getCount() != 0) {
            cursor.close();
            helper.close();
            return false;
        }

        cursor.close();
        helper.close();
        return true;
    }
}
