package com.devzhaoyou.gezhaoyou.androidaacencoder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MainActivity";
    private Button mStartBtn;
    private Button mStopBtn;
    public Object mLock = new Object();
    public boolean mAudioRecordGetExit;
    private FFAacEncoder mFFAacEncoderJni;
    private boolean mIsRecording = false;
    private Thread mAudioRecordGetThread;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mFFAacEncoderJni = new FFAacEncoder();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {
        mStartBtn = (Button) findViewById(R.id.bt_start);
        mStopBtn = (Button) findViewById(R.id.bt_stop);
        mStartBtn.setOnClickListener(this);
        mStopBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_start:
                startRecord();
                break;
            case R.id.bt_stop:
                stoptRecord();
                break;
        }

    }

    private void startRecord() {
        Log.i(TAG, "startRecord mIsRecording=" + mIsRecording);
        if (!mIsRecording) {
            mIsRecording = true;
            synchronized (mLock) {
                mAudioRecordGetExit = false;
            }
            //初始化ffmpeg 编码器
            mFFAacEncoderJni.start();
            //创建录音线程、开始录音
            mAudioRecordGetThread = new Thread(new AudioRecordGet());
            mAudioRecordGetThread.start();
        }
    }

    private void stoptRecord() {
        if (mIsRecording) {
            synchronized (mLock) {
                mAudioRecordGetExit = true;
            }
            mIsRecording = false;
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class AudioPCMData {
        byte[] mData;
        int mFrameSize;
        int mCapacity;

        public AudioPCMData(int capacity) {
            mCapacity = capacity;
            mData = new byte[mCapacity];
        }
    }

    ;

    private class AudioRecordGet implements Runnable {
        private AudioRecord mAudioRecord;
        private static final boolean PCM_DUMP_DEBUG = true;
        private static final boolean AAC_DUMP_DEBUG = false;
        private int mAudioSource = MediaRecorder.AudioSource.MIC;
        //采样频率，采样频率越高，音质越好。44100 、22050、 8000、4000等
        private int mSampleRateHz = 8000;
        //MONO为单声道 ，STEREO为双声道
        private int mChannelConfig = AudioFormat.CHANNEL_IN_MONO;
        //编码格式和采样大小，pcm编码；支持的采样大小16bit和8bit，采样大小越大，信息越多，音质越好。
        private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;
        //该size设置为AudioRecord.getMinBufferSize(mSampleRateHz, mChannelConfig, mAudioFormat); 编码aac时会失败。
        private int mBufferSizeInBytes = 2048;//AudioRecord.getMinBufferSize(mSampleRateHz, mChannelConfig, mAudioFormat);
        private AudioPCMData mAudioPCMData;

        public AudioRecordGet() {
            Log.i(TAG, "AudioRecordGet ");
            mAudioPCMData = new AudioPCMData(mBufferSizeInBytes);
            mAudioRecord = new AudioRecord(mAudioSource,
                    mSampleRateHz, mChannelConfig, mAudioFormat, mBufferSizeInBytes);
            Log.i(TAG, "mBufferSizeInBytes=" + mBufferSizeInBytes);
        }

        @Override
        public void run() {
            mAudioRecord.startRecording();
            FileOutputStream outPCM = null;
            try {
                if (PCM_DUMP_DEBUG) {
                    String File = "/sdcard/test.pcm";
                    outPCM = new FileOutputStream(File);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (; ; ) {
                synchronized (mLock) {
                    if (mAudioRecordGetExit) {
                        break;
                    }
                }
                //读取录音数据
                int readSize = mAudioRecord.read(mAudioPCMData.mData, 0, mBufferSizeInBytes);
                if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {
                    if (PCM_DUMP_DEBUG && null != outPCM) {
                        try {
                            outPCM.write(mAudioPCMData.mData, 0, readSize);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mAudioPCMData.mFrameSize = readSize;
                    Log.i(TAG, "audio pcm size=" + readSize);
                    //设置pcm数据，进行aac编码
                    mFFAacEncoderJni.setPcmData(mAudioPCMData.mData, readSize);
                }
            }
            if (PCM_DUMP_DEBUG && null != outPCM) {
                try {
                    outPCM.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //停止录音、释放
            mAudioRecord.stop();
            mAudioRecord.release();
            //停止音频编码
            mFFAacEncoderJni.stop();
            Log.i(TAG, "AudioRecordGet thread exit success");
        }
    }
}
