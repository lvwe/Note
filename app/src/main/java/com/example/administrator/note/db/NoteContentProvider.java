package com.example.administrator.note.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2017/5/6 0006.
 */

public class NoteContentProvider extends ContentProvider {
    private static final String TAG = "NoteContentProvider";
    public static Uri noteUri = Uri
            .parse("content://com.example.administrator.note/notes");
    private static String authority = "com.example.administrator.note";
    private static UriMatcher mMatcher;
    static {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mMatcher.addURI(authority,"notes",1);
    }


    private NoteOpenHelper mHelper;

    @Override
    public boolean onCreate() {
        if (mHelper == null) {
            mHelper = new NoteOpenHelper(getContext(), "notes.db", null, 1);
        }
        return false;
    }
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        switch (mMatcher.match(uri)){
            case 1:
                db.insert("notes",null,values);
                Log.d(TAG, "insert: success");
                break;
            default:
                Log.d(TAG, "insert: default");
                break;
            
        }
        db.close();
        return null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
       SQLiteDatabase db = mHelper.getWritableDatabase();
        switch (mMatcher.match(uri)){
            case 1:
                return db.query("notes",projection,selection,selectionArgs,null,null,sortOrder);

        }

        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count = db.update("notes",values,selection,null);
        return count;
    }
}
