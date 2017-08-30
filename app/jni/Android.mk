LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE = libSysUtils
LOCAL_SRC_FILES = SysUtils.c
LOCAL_CFLAGS := -std=c99
LOCAL_LDLIBS+= -L$(SYSROOT)/usr/lib -llog

include $(BUILD_SHARED_LIBRARY)
