package com.example.administrator.note;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


/**
 * Created by Administrator on 2017/5/13 0013.
 */

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "CameraActivity";
    private TextView mTvTime;
    private Button mBtnPhoto, mBtnRecVideo;
    private File mRecVideoPath;
    private SurfaceView mSurfaceView;
    private Camera mCamera;
    private boolean isPreView = false;
    private boolean isRecording = true;
    private SurfaceHolder mSurfaceHolder;
    private MediaRecorder mMediaRecorder;
    private int hour = 0;
    private int minute = 0;
    private int second = 0;
    private boolean bool;
    private Handler mHandler;

    private File mRecAudioFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mSurfaceView = (SurfaceView) findViewById(R.id.id_camera_surfaceView);
        initCamera();
        initViews();


    }

    private void initCamera() {
        mRecVideoPath = new File(Environment
                .getExternalStorageDirectory().getAbsolutePath() + "/a/a/");
        if (mRecVideoPath.exists()) {
            mRecVideoPath.mkdirs();
        }
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {

                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    mCamera.setDisplayOrientation(90);
                    Camera.Parameters parameters = mCamera.getParameters();
                    parameters.setPreviewFrameRate(5);
                    parameters.setPictureFormat(ImageFormat.JPEG);
                    parameters.setJpegQuality(85);
                    mCamera.setParameters(parameters);
                    mCamera.setPreviewDisplay(mSurfaceHolder);
                    mCamera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mSurfaceHolder = holder;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceHolder = holder;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera != null) {
                    if (isPreView) {
                        mCamera.stopPreview();
                        isPreView = false;
                    }
                    mCamera.release();
                    mCamera = null;
                }
                mSurfaceView = null;
                mSurfaceHolder = null;
                mMediaRecorder = null;

            }
        });
//        mSurfaceHolder.setType(surfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void initViews() {
        mTvTime = (TextView) findViewById(R.id.id_camera_tvTime);
        mTvTime.setVisibility(View.GONE);
        mBtnPhoto = (Button) findViewById(R.id.id_camera_photo);
        mBtnRecVideo = (Button) findViewById(R.id.id_camera_recVideo);

        mBtnPhoto.setOnClickListener(new clickEvent());
        mBtnRecVideo.setOnClickListener(new clickEvent());
    }

    class clickEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.id_camera_photo:
                    Log.d(TAG, "onClick: " + Environment.getExternalStorageState().equals(
                            android.os.Environment.MEDIA_MOUNTED));

                    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/nihao/a.txt";
                    Log.d(TAG, "onClick: path"+path);
                    File f = new File(path);
                    if (f.exists()){
                        f.mkdirs();
                    }
                    mRecVideoPath = new File(Environment
                            .getExternalStorageDirectory().getAbsolutePath() + "/你好/a.txt");
                    Log.d(TAG, "onClick: " + Environment
                            .getExternalStorageDirectory().getAbsolutePath() + "/你好/a.txt");
                    mRecVideoPath.exists();
                    Log.d(TAG, "onClick: "+mRecVideoPath.exists());
                    if (mRecVideoPath.exists()) {
                        mRecVideoPath.mkdirs();
                    }
                        break;

                        case R.id.id_camera_recVideo:

                            if (isRecording) {
                                if (isPreView) {
                                    mCamera.stopPreview();
                                    mCamera.release();
                                    mCamera = null;
                                }
                                hour = 0;
                                minute = 0;
                                second = 0;
                                bool = true;

                                if (mMediaRecorder == null) {
                                    mMediaRecorder = new MediaRecorder();
                                } else {
                                    mMediaRecorder.reset();
//                        mMediaRecorder.release();
                                }
                                //表面设置显示记录媒体（视频）的预览
                                mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
                                //开始捕捉和编码数据到setOutputFile（指定的文件）
                                mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                                //设置用于录制的音源
                                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                                //设置在录制过程中产生的输出文件的格式
                                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                                //设置视频编码器，用于录制
                                mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                                //设置audio的编码格式
                                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                                //设置要捕获的视频的宽度和高度
                                mMediaRecorder.setVideoSize(320, 240);
                                // 设置要捕获的视频帧速率
                                mMediaRecorder.setVideoFrameRate(15);
                                try {
                                    mRecAudioFile = File.createTempFile("Vedio", ".3gp",
                                            mRecVideoPath);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mMediaRecorder.setOutputFile(mRecAudioFile.getAbsolutePath());
                                try {
                                    mMediaRecorder.prepare();
                                    mTvTime.setVisibility(View.VISIBLE);
                                    handler.postDelayed(task, 1000);
                                    mMediaRecorder.start();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                isRecording = !isRecording;
                                Log.e(TAG, "=====开始录制视频=====");
                            } else {
                                //点击停止录像

                                mMediaRecorder.setOnErrorListener(null);
                                mMediaRecorder.setOnInfoListener(null);
                                mMediaRecorder.setPreviewDisplay(null);
                                bool = false;
                                mMediaRecorder.stop();

                                mTvTime.setText(FormatUtil.format(hour) + ":" + FormatUtil.format(minute) + ":" + FormatUtil.format(second));
                                mMediaRecorder.release();
                                mMediaRecorder = null;
                                FormatUtil.videoRename(mRecAudioFile);
                                Log.e(TAG, "=====录制完成，已保存=====");
                                isRecording = !isRecording;
                                try {

                                    mCamera = Camera.open();
                                    Camera.Parameters parameters = mCamera.getParameters();
//						parameters.setPreviewFrameRate(5); // 每秒5帧
                                    parameters.setPictureFormat(ImageFormat.JPEG);// 设置照片的输出格式
                                    parameters.set("jpeg-quality", 85);// 照片质量
                                    mCamera.setParameters(parameters);
                                    mCamera.setPreviewDisplay(mSurfaceHolder);
                                    mCamera.startPreview();
                                    isPreView = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            break;
                    }
            }
        }

        /*
             * 定时器设置，实现计时
             */
        private Handler handler = new Handler();
        private Runnable task = new Runnable() {
            public void run() {
                if (bool) {
                    handler.postDelayed(this, 1000);
                    second++;
                    if (second >= 60) {
                        minute++;
                        second = second % 60;
                    }
                    if (minute >= 60) {
                        hour++;
                        minute = minute % 60;
                    }
                    mTvTime.setText(FormatUtil.format(hour) + ":" + FormatUtil.format(minute) + ":"
                            + FormatUtil.format(second));
                }
            }
        };


        class SavePictureTask extends AsyncTask<byte[], String, String> {
            @Override
            protected String doInBackground(byte[]... params) {
                String path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/mahc/image";
                File out = new File(path);
                if (!out.exists()) {
                    out.mkdirs();
                }
                File picture = new File(path + "/" + new Date().getTime() + ".jpg");
                try {
                    FileOutputStream fos = new FileOutputStream(picture.getPath());
                    fos.write(params[0]);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "=====照片保存完成=====");
                CameraActivity.this.finish();

                return null;
            }
        }
    }
