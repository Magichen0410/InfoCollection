#include <string.h>
#include <jni.h>
#include <android/log.h>

#include "CTC_AmMediaProcessor.h"

#ifndef NELEM
#define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))
#endif

using namespace android;

#ifdef  __cplusplus
extern "C" {
#endif

static CTC_AmMediaProcessor *sCTC_AmMediaProcessor;
static void playHandlerEvent(EVENT_MEDIA_TYPE_e evt, void *handler, void* sParam){
	//ALOGE("playHandlerEvent  evt= %d",evt);
}

static const char *classPathName = "com/droidlogic/test/CtcAmPlayer";

static CTC_AmMediaProcessor* getInstance(){
    if(sCTC_AmMediaProcessor==NULL){
        sCTC_AmMediaProcessor = new CTC_AmMediaProcessor();
        sCTC_AmMediaProcessor->CTC_GetMediaControlVersion();
        sCTC_AmMediaProcessor->CTC_SetNetStreamType(STREAM_MEDIA_TYPE_VOD);
        sCTC_AmMediaProcessor->CTC_playerback_register_evt_cb(playHandlerEvent, NULL);
        sCTC_AmMediaProcessor->CTC_SetEPGSize(1080,720);
        sCTC_AmMediaProcessor->CTC_SetVideoWindow(10,10,1080,720);
    }
    return sCTC_AmMediaProcessor;
}

JNIEXPORT void JNICALL 
ctc_setEpgSize(JNIEnv *env, jobject thiz, int width, int height) {
     getInstance()->CTC_SetEPGSize(width,height);
     ALOGE("ctc_player_setEpgSize width= '%d' height=%d", width,height);
}

JNIEXPORT void JNICALL ctc_setDataSource(JNIEnv* env, jobject thiz, jstring url) {
	   const char *request_url = env->GetStringUTFChars(url, 0);
	   int size = strlen(request_url);
	   char *tmp = new char[size + 1];
	   memset(tmp,0,size + 1);
	   strncpy(tmp, request_url, size);
	   getInstance()->CTC_MediaProcessorInit(tmp);
	   ALOGE("ctc_player_setDataSource url= '%s',,tmp='%s'", request_url,tmp);
}

JNIEXPORT void JNICALL
ctc_prepareAsync(JNIEnv *env, jobject thiz) {
	  ALOGE("ctc_player_prepareAsync "); 
}

JNIEXPORT void JNICALL
ctc_setDisplaySurface(JNIEnv *env, jobject  thiz, jobject jsurface) {
	 ALOGE("ctc_player_setDisplaySurface ");
	 //sp<Surface> surface(android_view_Surface_getSurface(env, jsurface));
//	getInstance()->CTC_SetSurface(jsurface);
}


JNIEXPORT void JNICALL
ctc_setPlayWindow(JNIEnv* env, jobject thiz, int left, int top, int right, int bottom) {
    getInstance()->CTC_SetVideoWindow(left,top,right,bottom);
}


JNIEXPORT void JNICALL
ctc_start(JNIEnv *env, jobject thiz) {
  getInstance()->CTC_StartPlay();
  ALOGE("ctc_player_start ");
}

JNIEXPORT void JNICALL 
ctc_resume(JNIEnv *env, jobject thiz) {
  getInstance()->CTC_Resume();
  ALOGE("ctc_player_resume ");
}

JNIEXPORT void JNICALL 
ctc_resumePause(JNIEnv *env, jobject thiz) {
  getInstance()->CTC_StopFast();
  ALOGE("ctc_player_resumePause ");
}

JNIEXPORT void JNICALL
ctc_pause(JNIEnv *env, jobject thiz) {
getInstance()->CTC_Pause();
ALOGE("ctc_player_Pause ");
}


JNIEXPORT void JNICALL
ctc_stop(JNIEnv *env, jobject thiz) {
getInstance()->CTC_Stop();
ALOGE("ctc_player_Stop ");
}

JNIEXPORT void JNICALL
ctc_release(JNIEnv *env, jobject thiz) {
getInstance()->CTC_Release();
ALOGE("ctc_player_Release");
}

JNIEXPORT void JNICALL
ctc_begin(JNIEnv *env, jobject thiz) {
getInstance()->CTC_Seek(0);
ALOGE("ctc_player_begin");
}

JNIEXPORT void JNICALL 
ctc_end(JNIEnv *env, jobject thiz) {
getInstance()->CTC_Stop();
ALOGE("ctc_player_end");
}

JNIEXPORT void JNICALL
    ctc_setDisplayMode(JNIEnv* env, jobject thiz, int mode) {
}

JNIEXPORT void JNICALL
ctc_setPlaySpeed(JNIEnv* env, jobject thiz, int speed) {
    getInstance()->CTC_Fast(speed);
    ALOGE("ctc_player_setPlaySpeed  speed=%d",speed);
}

JNIEXPORT jint JNICALL
ctc_getDuration(JNIEnv* env, jobject thiz) {
   ALOGE("ctc_player_getDuration11  Duration=%d", 10);
   ALOGE("ctc_player_getDuration33  Duration=%d", getInstance()->CTC_GetDuraion());
   return getInstance()->CTC_GetDuraion();
   // return 100;
}

JNIEXPORT jint JNICALL
ctc_getCurrentPosition(JNIEnv* env, jobject thiz) {
    ALOGE("ctc_player_getCurrentPosition");
    ALOGE("ctc_player_getCurrentPosition  CurrentPosition=%d", getInstance()->CTC_GetCurrentPlayTime());
    return 10;
}

static JNINativeMethod methods[] = {
    {"nativeCtcSetEpgSize", "(II)V", (void*)ctc_setEpgSize},
    {"nativeCtcSetPlayWindow", "(IIII)V", (void*)ctc_setPlayWindow},
    {"nativeCtcPrepareAsync", "()V", (void *)ctc_prepareAsync},
    {"nativeCtcPause", "()V", (void*)ctc_pause},
    {"nativeCtcResume", "()V", (void*)ctc_resume},
    {"nativeCtcBegin", "()V", (void*)ctc_begin},
    {"nativeCtcEnd", "()V", (void*)ctc_end},
    {"nativeCtcStop", "()V", (void*)ctc_stop},
    {"nativeCtcStopFast", "()V", (void*)ctc_resumePause},
    {"nativeCtcRelease", "()V", (void*)ctc_release},
    {"nativeCtcSetDisplayMode", "(I)V", (void*)ctc_setDisplayMode},
    {"nativeCtcSetDataSource", "(Ljava/lang/String;)V", (void*)ctc_setDataSource},
    {"nativeCtcSetPlaySpeed", "(I)V", (void*)ctc_setPlaySpeed},
    {"nativeCtcGetDuration", "()I", (void*)ctc_getDuration},
    {"nativeCtcGetCurrentPosition", "()I", (void*)ctc_getCurrentPosition},
    {"nativeCtcSetDisplaySurface","(Landroid/view/Surface;)V",(void*)ctc_setDisplaySurface},
    {"nativeCtcStart","()V",(void*)ctc_start},
};



/*
 * Register several native methods for one class.
 */
static int registerNativeMethods(JNIEnv* env, const char* className,
        JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;

    clazz = env->FindClass(className);
    if (clazz == NULL) {
        ALOGE("Native registration unable to find class '%s'", className);
        return JNI_FALSE;
    }

    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        ALOGE("RegisterNatives failed for '%s'", className);
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

/*
 * Register native methods for all classes we know about.
 *
 * returns JNI_TRUE on success.
 */
static int registerNatives(JNIEnv* env)
{

    if (!registerNativeMethods(env, classPathName,
                methods, sizeof(methods) / sizeof(methods[0]))) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

int JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env = NULL;
    jint result = -1;
    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        ALOGE("ERROR: GetEnv failed\n");
        goto bail;
    }
    registerNatives(env);
    result = JNI_VERSION_1_4;
bail:
    return result;
}
}
