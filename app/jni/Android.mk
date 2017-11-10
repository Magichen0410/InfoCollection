LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE = libSysUtils
LOCAL_SRC_FILES = SysUtils.c
LOCAL_CFLAGS := -std=c99
LOCAL_LDLIBS+= -L$(SYSROOT)/usr/lib -llog
include $(BUILD_SHARED_LIBRARY)

#include $(CLEAR_VARS)
#LOCAL_MODULE := CTC_AmMediaProcessor
#LOCAL_SRC_FILES := $(LOCAL_PATH)/lib/libCTC_AmMediaProcessor.so
#LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/lib/include
#include $(PREBUILT_SHARED_LIBRARY)
#LOCAL_32_BIT_ONLY := false

#include $(CLEAR_VARS)
#WALLPAPER_ROOT_ABSOLUTE_PATH := $(LOCAL_PATH)/../../../../..
#LOCAL_MODULE = libSwcMediaProcessor
#LOCAL_SRC_FILES = SwcMediaProcessor.cpp
#APP_ALLOW_MISSING_DEPS := true
#LOCAL_LDLIBS+= -L$(SYSROOT)/usr/lib -llog
#LOCAL_C_INCLUDES := \
#                 $(LOCAL_PATH)/lib/include \
#                 $(WALLPAPER_ROOT_ABSOLUTE_PATH)/frameworks/native/include \
#                 $(WALLPAPER_ROOT_ABSOLUTE_PATH)/system/core/include \
#                 $(WALLPAPER_ROOT_ABSOLUTE_PATH)/hardware/libhardware/include \
#                 $(WALLPAPER_ROOT_ABSOLUTE_PATH)/frameworks/base/include \
#                 $(WALLPAPER_ROOT_ABSOLUTE_PATH)/libnativehelper/include
#LOCAL_SHARED_LIBRARIES := \
#        libnativehelper \
#        libCTC_AmMediaProcessor \
#        libui \
#        libandroid_runtime \
#        libnativehelper \
#        libmedia \
#        libgui
        
#include $(BUILD_SHARED_LIBRARY)