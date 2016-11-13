package by.madcat.development.f1newsreader.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseDescription {
    public static final String AUTHORITY = "by.madcat.development.f1newsreader.data";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class News implements BaseColumns{
        public static final String TABLE_NAME = "news";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String COLUMN_TITLE = "news_title";
        public static final String COLUMN_NEWS = "news_full_text";
        public static final String COLUMN_NEWS_TYPE = "news_type";
        public static final String COLUMN_LINK_NEWS = "news_link";
        public static final String COLUMN_DATE = "news_date";
        public static final String COLUMN_IMAGE = "news_image";

        public static Uri buildNewsUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public enum NewsTypes{
        NEWS,
        MEMUAR,
        TECH,
        HISTORY,
        COLUMNS,
        AUTOSPORT
    }
}
