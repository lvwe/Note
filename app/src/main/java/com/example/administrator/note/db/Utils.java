package com.example.administrator.note.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2017/5/6 0006.
 */

public class Utils {
    private NoteOpenHelper mHelper;

    public void update(int id){

        ContentValues values = new ContentValues();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.update("notes", values,"id="+id,null);
        db.close();



    }

}
