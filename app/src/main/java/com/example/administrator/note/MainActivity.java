package com.example.administrator.note;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.note.db.NoteContentProvider;
import com.example.administrator.note.db.NoteOpenHelper;
import com.example.administrator.note.db.TableNote;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private ImageView mIVSearch;
    private RecyclerView mRecyclerView;
    private RecycleViewAdapter mAdapter;
    private ImageButton mImgBtnAdd;
    private List<Note> mNoteList = new ArrayList<>();
    private NoteContentProvider mProvider;
    private NoteOpenHelper mHelper;
    private int mIsDel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        EventBus.getDefault().register(this);

        readData();
        mAdapter.notifyDataSetChanged();
//        mNoteList.add(new Note(R.mipmap.ic_launcher, "标题", "类别",
//                R.mipmap.star, R.mipmap.clock, "时间"));
//        mNoteList.add(new Note(R.mipmap.ic_launcher, "zzz", "ccc",
//                R.mipmap.star, R.mipmap.clock, "12:20"));
//        mNoteList.add(new Note(R.mipmap.ic_launcher, "www", "eeee",
//                R.mipmap.star, R.mipmap.clock, "3:22"));

    }

    private List<Note> readData() {
        Cursor cursor = getContentResolver().query(NoteContentProvider.noteUri, null, null, null, null);
        if (cursor == null) {

            Log.d(TAG, "readData: return");
            return null;
        }
        mNoteList.clear();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//            private int imgId;
//            private String title;
//            private String category;
//            private int imgStar;
//            private int imgClock;
//            private String reTime;
//            byte[] s = cursor.getBlob(0);
            Bitmap b = byteToBitmap(cursor.getBlob(1));
            String title = cursor.getString(2);
            cursor.getString(3);
            String reTime = cursor.getString(4);
            String category = cursor.getString(5);
            cursor.getBlob(6);
            cursor.getBlob(7);
            Bitmap imgStar = byteToBitmap(cursor.getBlob(6));
            Bitmap imgClock = byteToBitmap(cursor.getBlob(7));
            mIsDel = cursor.getInt(8);
            if (mIsDel == 0) {
                Log.d(TAG, "readData: " + mIsDel);
                mNoteList.add(new Note(b, title, category, imgStar, imgClock, reTime));
            }

            Log.d(TAG, "readData: query");
        }
        cursor.close();

        return mNoteList;

    }

    public Bitmap byteToBitmap(byte[] b) {
        Bitmap bitmap;
        if (b.length != 0) {

            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }

    public Drawable bitmapToDrawable(Bitmap b) {
        Drawable drawable = new BitmapDrawable(b);
        return drawable;
    }

    //    public byte[] readImage() {
//        SQLiteDatabase db = mHelper.getWritableDatabase();
//        Cursor cur = db.query("User", new String[]{"_id", "avatar"}, null, null, null, null, null);
    byte[] imgData = null;
//        if (cur.moveToNext()) {
    //将Blob数据转化为字节数组
//            imgData = cur.getBlob(cur.getColumnIndex("avatar"));
//        }
//        return imgData;
//    }

    private void initViews() {
        mIVSearch = (ImageView) findViewById(R.id.id_iv_search);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recycleView);
        mImgBtnAdd = (ImageButton) findViewById(R.id.id_imgBtn_add);
        mIVSearch.setOnClickListener(this);
        mImgBtnAdd.setOnClickListener(this);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new RecycleViewAdapter(mNoteList);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, mNoteList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            //            首先回调的方法 返回int表示是否监听该方向

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//                侧滑删除
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                滑动事件
                Collections.swap(mNoteList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                mAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//          侧滑事件
                mNoteList.remove(viewHolder.getAdapterPosition());

                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//                updateDB( viewHolder.getAdapterPosition());
                int i= mRecyclerView.getChildLayoutPosition(viewHolder.itemView);
                i+=i;
                updateDB(i);



//                updateDB();
//                Log.d(TAG, "onSwiped: "+viewHolder.getAdapterPosition());  == -1

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

        });

        helper.attachToRecyclerView(mRecyclerView);

    }


    private void updateDB(int ids) {
        ContentValues values = new ContentValues();
        values.put(TableNote.COL_IS_DEL, 1);
        getContentResolver().update(NoteContentProvider.noteUri, values, "id="+ids, null);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_iv_search:
                Toast.makeText(this, "mIVSearch", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_imgBtn_add:
                Toast.makeText(this, "mImgBtnAdd", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                break;
        }
    }


    // This method will be called when a MessageEvent is posted
    @Subscribe
    public void onMessageEvent(MessageEvent event) {
        Toast.makeText(MainActivity.this, event.message, Toast.LENGTH_SHORT).show();
        readData();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

}
