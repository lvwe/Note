package com.example.administrator.note;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.note.db.NoteContentProvider;
import com.example.administrator.note.db.NoteOpenHelper;
import com.example.administrator.note.db.TableNote;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddActivity";
    private ImageView mImgAdd;
    private EditText mEdtTitle;
    private TextView mTvTime;
    private EditText mEdtContext;
    private Button mBtnDel;
    private Button mBtnSave;
    private String currentTime;
    private Spinner mSpinner;
    private Button mBtnSelImg, mBtnRecorder, mBtnVideo;


    final static int PHOTO_RESULT_CODE = 1001;
    final static int PHOTO_REQUEST_CAMERA = 1002;
    final static int PHOTO_REQUEST_GALLERY = 1003;
    final static int PHOTO_RESULT_CUT = 1004;

    static Uri imgSelectUri;
    static String imgFilePath;
    private List<Note> mNoteList = new ArrayList<>();
    private Bitmap mBitmap;
    private String mCategory;
    private NoteOpenHelper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mHelper = new NoteOpenHelper(this, "note.db", null, 1);
        initViews();
    }

    private void initViews() {
        mImgAdd = (ImageView) findViewById(R.id.id_add_imgBtn);
        mEdtTitle = (EditText) findViewById(R.id.id_add_edtTitle);
        mTvTime = (TextView) findViewById(R.id.id_add_time);
        mEdtContext = (EditText) findViewById(R.id.id_add_context);
        mBtnDel = (Button) findViewById(R.id.id_add_btnDel);
        mBtnSave = (Button) findViewById(R.id.id_add_btnSave);

//        private Button mBtnSelImg,mBtnRecorder,mBtnVideo;
        mBtnSelImg = (Button) findViewById(R.id.id_add_btnSelImg);
        mBtnRecorder = (Button) findViewById(R.id.id_add_btnRecorder);
        mBtnVideo = (Button) findViewById(R.id.id_add_btnVideo);
        mSpinner = (Spinner) findViewById(R.id.id_add_spinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] type = getResources().getStringArray(R.array.type);
                Toast.makeText(AddActivity.this, "你点击的是" + type[position], Toast.LENGTH_SHORT).show();
                mCategory = type[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mImgAdd.setOnClickListener(this);
        mBtnDel.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
        mBtnSelImg.setOnClickListener(this);
        mBtnRecorder.setOnClickListener(this);
        mBtnVideo.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mTvTime.setText(getCurrentTime());

    }

    private String getCurrentTime() {
        Log.d(TAG, "onStart: ");
        Calendar c = Calendar.getInstance();
        c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
        String currentTime = format.format(c.getTime());
        Log.d(TAG, "onStart: " + currentTime);
        return currentTime;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_add_imgBtn:
                Toast.makeText(this, "mImgBtnAdd", Toast.LENGTH_SHORT).show();
                selectPicture();
                break;
            case R.id.id_add_btnDel:
                Toast.makeText(this, "mBtnDel", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.id_add_btnSave:
                Toast.makeText(this, "mBtnSave", Toast.LENGTH_SHORT).show();
                saveNoteToDB();
                EventBus.getDefault().post(new MessageEvent("post"));
                finish();
                break;
            case R.id.id_add_btnSelImg:
                selectPicture();
                Log.d(TAG, "onClick: +selImg");
                break;
            case R.id.id_add_btnRecorder:

                break;
            case R.id.id_add_btnVideo:
                Intent intent = new Intent(this,CameraActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void saveNoteToDB() {
        String addTitle = mEdtTitle.getText().toString();
        String addContent = mEdtContext.getText().toString();
        String createDate = mTvTime.getText().toString();
        String category = mCategory;
//                int addImgStar = R.id.id_star;
//                int addImgClock = R.id.id_clock;
//                bitmapToByte(this,addImgStar);
//                bitmapToByte(this,addImgClock);
        if (addTitle != null && addContent != null) {
            ContentValues values = new ContentValues();
            values.put(TableNote.COL_IMG, bitmapToByte(this));
            values.put(TableNote.COL_TITLE, addTitle);
            values.put(TableNote.COL_CONTENT, addContent);
            values.put(TableNote.COL_CREATE_DATE, createDate);
            values.put(TableNote.COL_CATEGORY, category);
            values.put(TableNote.COL_IMG_STAR, bitmapToByte(this, R.mipmap.star));
            values.put(TableNote.COL_IMG_CLOCK, bitmapToByte(this, R.mipmap.clock));
            values.put(TableNote.COL_IS_DEL, 0);
            Log.d(TAG, "onClick: ");
            getContentResolver().insert(NoteContentProvider.noteUri, values);
        }
    }


    private byte[] bitmapToByte(Context context, int imgId) {
        //将图片转化为位图
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgId);
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        //创建一个字节数组输出流,流的大小为size
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
        try {
            //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            //将字节数组输出流转化为字节数组byte[]
            byte[] imageData = baos.toByteArray();
            return imageData;
        } catch (Exception e) {
        } finally {
            try {
                bitmap.recycle();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    private byte[] bitmapToByte(Context context) {
        //将图片转化为位图
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);



        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }

        int size = mBitmap.getWidth() * mBitmap.getHeight() * 4;
        //创建一个字节数组输出流,流的大小为size
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
        try {
            //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            //将字节数组输出流转化为字节数组byte[]
            byte[] imageData = baos.toByteArray();
            return imageData;
        } catch (Exception e) {
        } finally {
            try {
                if(mBitmap != null && !mBitmap.isRecycled()){
//                    mBitmap.recycle();
                    mBitmap = null;
                }
//                mBitmap.recycle();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }


    private void selectPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTO_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (PHOTO_RESULT_CODE == requestCode && data != null) {
            imgSelectUri = data.getData();
            String[] filePathColumn = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imgSelectUri, filePathColumn, null, null, null);
            imgFilePath = null;
            while (cursor.moveToNext()) {
                imgFilePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            }
            Toast.makeText(AddActivity.this, "图片路径" + imgFilePath, Toast.LENGTH_LONG).show();
            cursor.close();
            crop(imgSelectUri);
        } else if (PHOTO_RESULT_CUT == requestCode) {
            Toast.makeText(AddActivity.this, "crop(imgSelectUri);", Toast.LENGTH_SHORT).show();
            mBitmap = null;
            //bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgSelectUri);
            mBitmap = data.getParcelableExtra("data");
            mImgAdd.setImageBitmap(mBitmap);

//            Bitmap bitmap = getOriginalBitmap(uri);
            SpannableString ss = getBitmapMime(mBitmap, imgSelectUri);
            insertIntoEditText(ss);


            Log.d("------", "onActivityResult: -----" + mBitmap);

        }



            /*try {
                *//*
                *//**//*错误Bitmap too large to be uploaded into a texture (4208x3120, max=4096x4096)
                * http://www.cnblogs.com/Ringer/p/4096050.html
                * 一个解决的方法是禁止硬件加速
                    <application android:hardwareAccelerated="false" ...>
                * *//*
                Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgSelectUri);
                Log.d("------", "onActivityResult: -----"+bmp);
                imgHead.setImageBitmap(bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(AddActivity.this,"设置图片",Toast.LENGTH_SHORT).show();*/


            /*if(requestCode == PHOTO_RESULT_CUT){
                Bundle bundle = data.getExtras();

                Bitmap bitmap = bundle.getParcelable("data");
                imgHead.setImageBitmap(bitmap);
            }*/

        else {
            Toast.makeText(AddActivity.this, "选择图片失败", Toast.LENGTH_LONG).show();

        }
    }

    private SpannableString getBitmapMime(Bitmap pic, Uri uri) {
        int imgWidth = pic.getWidth();
        int imgHeight = pic.getHeight();
        // 只对大尺寸图片进行下面的压缩，小尺寸图片使用原图
//        if (imgWidth >= mInsertedImgWidth) {
//            float scale = (float) mInsertedImgWidth / imgWidth;
//            Matrix mx = new Matrix();
//            mx.setScale(scale, scale);
//            pic = Bitmap.createBitmap(pic, 0, 0, imgWidth, imgHeight, mx, true);
//
//        }
        String smile = uri.getPath();
        SpannableString ss = new SpannableString(smile);
        ImageSpan span = new ImageSpan(this, pic);
        ss.setSpan(span, 0, smile.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
    private void insertIntoEditText(SpannableString ss) {
        // 先获取Edittext中原有的内容
        Editable et = mEdtContext.getText();
        int start = mEdtContext.getSelectionStart();
        // 设置ss要添加的位置
        et.insert(start, ss);
        // 把et添加到Edittext中
        mEdtContext.setSingleLine(false);
        mEdtContext.setText(et);

//        tv.setText(shipAddress.getFullAddress().replace("\n", ""));
        // 设置Edittext光标在最后显示

        mEdtContext.setSelection(start + ss.length());



//        mEdtContext.setText("\n");
    }
    private Bitmap getOriginalBitmap(Uri photoUri) {
        if (photoUri == null) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            ContentResolver conReslv = getContentResolver();
            // 得到选择图片的Bitmap对象
            bitmap = MediaStore.Images.Media.getBitmap(conReslv, photoUri);
        } catch (Exception e) {
            Log.e(TAG, "Media.getBitmap failed", e);
        }
        return bitmap;
    }

    public void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESULT_CUT);
        Toast.makeText(AddActivity.this, "PHOTO_RESULT_CUT", Toast.LENGTH_SHORT).show();

    }
}

