#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include "com_devzhaoyou_gezhaoyou_androidaacencoder_FFAacEncoder.h"
#include "AacCodec.h"
#include "Log.h"

#undef LOG_TAG
#define LOG_TAG "jni_FFAacEncoderJni"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_devzhaoyou_gezhaoyou_androidaacencoder_FFAacEncoder_nativeStart(JNIEnv *env, jobject thiz){
	jclass clazz = env->GetObjectClass(thiz);
	jfieldID fieldId = env->GetFieldID(clazz, "mNativeContext", "I");
	AacCodec *aacCodec = (AacCodec *)env->GetIntField(thiz,fieldId);
	if(aacCodec){
		delete aacCodec;
		aacCodec = NULL;
	}
	aacCodec = new AacCodec();
	aacCodec->start();

	env->SetIntField(thiz, fieldId, (int)aacCodec);
}

JNIEXPORT void JNICALL Java_com_devzhaoyou_gezhaoyou_androidaacencoder_FFAacEncoder_nativeSetPcmData(JNIEnv *env, jobject thiz, jbyteArray pcm, jint len){
	jbyte *pcmData = env->GetByteArrayElements(pcm, 0);
	if(!pcmData){
		ALOGE("set pcm data fail");
		return;
	}
	jclass clazz = env->GetObjectClass(thiz);
	jfieldID fieldId = env->GetFieldID(clazz, "mNativeContext", "I");
	AacCodec *aacCodec = (AacCodec *)env->GetIntField(thiz,fieldId);
	if(aacCodec){
		aacCodec->encode_pcm_data(pcmData, len);
	}
	env->ReleaseByteArrayElements(pcm, pcmData, 0);
}
JNIEXPORT void JNICALL Java_com_devzhaoyou_gezhaoyou_androidaacencoder_FFAacEncoder_nativeStop(JNIEnv *env, jobject thiz){
	ALOGD("Java_com_devzhaoyou_gezhaoyou_androidaacencoder_FFAacEncoder_nativeStop");
	jclass clazz = env->GetObjectClass(thiz);
	jfieldID fieldId = env->GetFieldID(clazz, "mNativeContext", "I");
	AacCodec *aacCodec = (AacCodec *)env->GetIntField(thiz,fieldId);
	if(!aacCodec){
		aacCodec->stop();
	}
	env->SetIntField(thiz, fieldId, 0);
}
#ifdef __cplusplus
}
#endif
