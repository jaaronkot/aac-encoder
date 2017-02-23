package com.devzhaoyou.gezhaoyou.androidaacencoder;

/**
 * Created by gezhaoyou on 17/2/18.
 */

public class FFAacEncoder {

    private String TAG = "FFAacEncoder java";
    //load .so
    static{
        System.loadLibrary("aacEncoder");
    }

    private int mNativeContext = 0;

    public void start(){
        nativeStart();
    }

    public void setPcmData(byte[] pcm, int len){
        nativeSetPcmData(pcm, len);
    }

    public void stop(){
        nativeStop();
    }


    /***** Native Surface ****/
    //初始化编码器
    private native final void nativeStart();
    //对pcm数据进行编码
    private native final void nativeSetPcmData(byte[] pcm, int len);
    //必要的清理
    private native final void nativeStop();

}
