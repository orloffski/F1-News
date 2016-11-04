package by.madcat.development.f1newsreader.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import by.madcat.development.f1newsreader.data.DatabaseDescription.*;

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
                        News.COLUMN_LINK_NEWS + " TEXT, " +
                        News.COLUMN_DATE + " TEXT, " +
                        News.COLUMN_IMAGE + " TEXT);";

        final String CREATE_MEMUAR_TABLE =
                "CREATE TABLE " + Memuar.TABLE_NAME + "(" +
                        Memuar._ID + " integer primary key, " +
                        Memuar.COLUMN_TITLE + " TEXT, " +
                        Memuar.COLUMN_NEWS + " TEXT, " +
                        Memuar.COLUMN_LINK_NEWS + " TEXT, " +
                        Memuar.COLUMN_DATE + " TEXT, " +
                        Memuar.COLUMN_IMAGE + " TEXT);";

        sqLiteDatabase.execSQL(CREATE_NEWS_TABLE);
        sqLiteDatabase.execSQL(CREATE_MEMUAR_TABLE);

        // фэйковые данные для теста
        ContentValues cv = new ContentValues();

        for(int i = 1; i <= 31; i++){
            cv.clear();
            cv.put(News.COLUMN_TITLE, "news title " + i);
            cv.put(News.COLUMN_NEWS, "full news text " + i);
            cv.put(News.COLUMN_LINK_NEWS, "news link " + i);
            cv.put(News.COLUMN_DATE, String.valueOf(i) + ".01.2016");
            cv.put(News.COLUMN_IMAGE, "news image " + i);

            sqLiteDatabase.insert(News.TABLE_NAME, null, cv);
        }

        for(int i = 1; i <= 31; i++){
            cv.clear();
            cv.put(Memuar.COLUMN_TITLE, "Memuar title " + i);
            cv.put(Memuar.COLUMN_NEWS, "full Memuar text " + i);
            cv.put(Memuar.COLUMN_LINK_NEWS, "Memuar link " + i);
            cv.put(Memuar.COLUMN_DATE, String.valueOf(i) + ".01.2016");
            cv.put(Memuar.COLUMN_IMAGE, "Memuar image " + i);

            sqLiteDatabase.insert(Memuar.TABLE_NAME, null, cv);
        }
        // конец фэйковых данных
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
