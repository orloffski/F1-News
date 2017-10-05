package by.madcat.development.f1newsreader.DataSQLite;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.DataSQLite.DatabaseDescription.*;

public class F1NewsReaderContentProvider extends ContentProvider {

    private F1NewsReaderDatabaseHelper dbHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ONE_NEWS = 1;
    private static final int NEWS = 2;

    static {
        uriMatcher.addURI(DatabaseDescription.AUTHORITY, News.TABLE_NAME + "/#", ONE_NEWS);
        uriMatcher.addURI(DatabaseDescription.AUTHORITY, News.TABLE_NAME, NEWS);
    }

    public F1NewsReaderContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numberOfRowsDeleted;
        String id;

        switch (uriMatcher.match(uri)){
            case ONE_NEWS:
                id = uri.getLastPathSegment();
                numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(News.TABLE_NAME, News._ID + "=" + id, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_delete_uri) + uri);
        }

        return numberOfRowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri newUri = null;
        long rowId;

        switch (uriMatcher.match(uri)){
            case NEWS:
                rowId = dbHelper.getWritableDatabase().insert(News.TABLE_NAME, null, values);
                if(rowId > 0){
                    newUri = News.buildNewsUri(rowId);
                    getContext().getContentResolver().notifyChange(uri, null);
                }else
                    throw new SQLException(getContext().getString(R.string.insert_failed) + uri);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_insert_uri) + uri);
        }

        return newUri;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new F1NewsReaderDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)){
            case NEWS:
                queryBuilder.setTables(News.TABLE_NAME);
                break;
            case ONE_NEWS:
                queryBuilder.setTables(News.TABLE_NAME);
                queryBuilder.appendWhere(News._ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_query_uri) + uri);
        }

        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int numberOfRowsUpdated;
        String id;

        switch (uriMatcher.match(uri)){
            case ONE_NEWS:
                id = uri.getLastPathSegment();
                numberOfRowsUpdated = dbHelper.getWritableDatabase().update(News.TABLE_NAME, values, News._ID + "=" + id, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_update_uri) + uri);
        }

        if(numberOfRowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return numberOfRowsUpdated;
    }
}
