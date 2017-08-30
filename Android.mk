LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := tests

LOCAL_MODULE := InfoCollection
LOCAL_SRC_FILES := $(LOCAL_MODULE).apk
LOCAL_MODULE_CLASS := APPS
LOCAL_MODULE_SUFFIX := $(COMMON_ANDROID_PACKAGE_SUFFIX)
LOCAL_CERTIFICATE := platform
LOCAL_MODULE_PATH := $(TARGET_OUT_APPS)
USER := $(shell whoami)
path := $(shell pwd)

PRIVATE_LOCAL_PATH := $(LOCAL_PATH)
PRIVATE_LOCAL_MODULE := $(LOCAL_MODULE)

$(LOCAL_PATH)/$(LOCAL_MODULE).apk:
	mkdir -p /mnt/fileroot/$(USER)/AndroidSDK/licenses/
	echo "8933bad161af4178b1185d1a37fbf41ea5269c55" > /mnt/fileroot/$(USER)/AndroidSDK/licenses/android-sdk-license
	find $(PRIVATE_LOCAL_PATH)/app -name *.apk -exec rm -rf {} \;
	echo -e "org.gradle.Java.home=/opt/openjdk-1.8\nandroidNdkHome=/mnt/fileroot/$(USER)/AndroidSDK/ndk-bundle" > $(PRIVATE_LOCAL_PATH)/gradle.properties
	echo -e "sdk.dir=/mnt/fileroot/$(USER)/AndroidSDK\nndk.dir=/mnt/fileroot/$(USER)/AndroidSDK/ndk-bundle" > $(PRIVATE_LOCAL_PATH)/local.properties
	JAVA_HOME=/opt/openjdk-1.8;cd $(path)/$(LOCAL_PATH);./gradlew build;cd $(path);
	rm $(PRIVATE_LOCAL_PATH)/gradle.properties
	rm $(PRIVATE_LOCAL_PATH)/local.properties
	cp $(PRIVATE_LOCAL_PATH)/app/build/outputs/apk/app-release-unsigned.apk $(PRIVATE_LOCAL_PATH)/$(PRIVATE_LOCAL_MODULE).apk
	
include $(BUILD_PREBUILT)
