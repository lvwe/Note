package com.example.administrator.note.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/5/6 0006.
 */

public class NoteOpenHelper extends SQLiteOpenHelper {
    //    public static final String COL_ID = "id";
//    public static final String COL_IMG = "img";
//    public static final String COL_TITLE = "title";
//    public static final String COL_CONTENT = "content";
//    public static final String COL_CREATE_DATE = "createDate";
//    public static final String COL_MODIFY_DATE = "modifyDate";
//    public static final String COL_IMG_STAR = "imgStar";
//    public static final String COL_IMG_CLOCK = "imgClock";
    public static final String CREATE_NOTE = "create table if not exists notes ("
//            + "id integer primary key autoincrement, "
            + "id INTEGER not null primary key AUTOINCREMENT, "
            + "img blob, "
            + "title text, "
            + "content text, "
            + "createDate varchar, "
            + "category varchar, "
            + "imgStar blob, "
            + "imgClock blob,"
            + "isDel int)";

    private Context mContext;
    public NoteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE);
        Toast.makeText(mContext, "create success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
