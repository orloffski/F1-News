package by.madcat.development.f1newsreader.DataSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import by.madcat.development.f1newsreader.DataSQLite.DatabaseDescription.*;

public class F1NewsReaderDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "F1NewsReader.db";
    private static final int DATABASE_VERSION = 1;

    public F1NewsReaderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_NEWS_TABLE =
                "CREATE TABLE " + News.TABLE_NAME + "(" +
                        News._ID + " integer primary key, " +
                        News.COLUMN_TITLE + " TEXT, " +
                        News.COLUMN_NEWS + " TEXT, " +
                        News.COLUMN_NEWS_TYPE + " TEXT, " +
                        News.COLUMN_LINK_NEWS + " TEXT, " +
                        News.COLUMN_DATE + " TEXT, " +
                        News.COLUMN_IMAGE + " TEXT, " +
                        News.COLUMN_READ_FLAG + " integer);";

        sqLiteDatabase.execSQL(CREATE_NEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
