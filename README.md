## AndroidAACEncoder
* A android demo that encode pcm to aac using ffmpeg 3.2.4. 
* You can `cd` to `jni/` and run `ndk-build` gerate ffmpeg .so libs. 


### 说明
* 安卓端使用AudioRecord实时录音，并将PCM编码为AAC保存，编码使用FFmpeg 3.2.4 
* 切换端 jni目录下执行 ndk-build ,同级目录labs 生包含FFmpeg的成动态库
