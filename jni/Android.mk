LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    :=  avcodec
LOCAL_SRC_FILES := 	libs/libavcodec.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    :=  avdevice
LOCAL_SRC_FILES := 	libs/libavdevice.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    :=  avfilter
LOCAL_SRC_FILES := 	libs/libavfilter.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    :=  avformat
LOCAL_SRC_FILES := 	libs/libavformat.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    :=  avutil
LOCAL_SRC_FILES := 	libs/libavutil.a
include $(PREBUILT_STATIC_LIBRARY)

# include $(CLEAR_VARS)
# LOCAL_MODULE    :=  postproc
# LOCAL_SRC_FILES := 	libs/libpostproc.a
# include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    :=  swresample
LOCAL_SRC_FILES := 	libs/libswresample.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    :=  swscale
LOCAL_SRC_FILES := 	libs/libswscale.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE    := 	aacEncoder
LOCAL_SRC_FILES :=	com_devzhaoyou_gezhaoyou_androidaacencoder_FFAacEncoder.cpp \
					AacCodec.cpp

LOCAL_LDLIBS    +=  -lm -llog -landroid -lz -ljnigraphics
LOCAL_CFLAGS	+=  -D__STDC_CONSTANT_MACROS

LOCAL_C_INCLUDES	:= 	$(LOCAL_PATH) \
						$(LOCAL_PATH)/include
#LOCAL_PROGUARED_ENABLDE := disabled 
						
LOCAL_STATIC_LIBRARIES	:=	avformat \
							avcodec	\
							avdevice \
							avfilter \
							swresample \
							swscale \
							avutil \

						
include $(BUILD_SHARED_LIBRARY)
#include $(call all-makefiles-under,$(LOCAL_PATH))