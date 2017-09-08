#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <unistd.h>
#include "net/if.h"
#include "arpa/inet.h"
#include "linux/sockios.h"
#include <jni.h>
#include <android/log.h>

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "com.amlogic.toolkit.infocollection", __VA_ARGS__)

static const char *classPathName = "com/amlogic/toolkit/infocollection/natives/SysUtils";
static const char *firstFrameNode = "/sys/module/amvideo/parameters/first_frame_toggled";
static const char *switchTimeInfoNode = "/sys/class/video/timedebug_info";
static const char *switchTimeDebug = "/sys/module/di/parameters/time_debug";

void callcmd(){
    FILE* fp;
    char *str;
    int size = 0;
    char buffer[1024] = {0};
    char tmp[200] = {0};

    fp = popen("busybox ifconfig eth0","r");
    while (fgets(tmp, sizeof(tmp), fp) != NULL) {
        //LOGD("%s",tmp);
        int len = strlen(tmp);
        memcpy(buffer+size,tmp,len);
        size += len;
    }
    pclose(fp);
}

jstring Java_com_amlogic_toolkit_infocollection_natives_SysUtils_getIpAddress(JNIEnv* env, jobject obj) {

    struct sockaddr_in *addr;
    struct ifreq ifr;
    char* address;
    int sockfd;
    char *name = "eth0";

    if( strlen(name) >= IFNAMSIZ) address = "";
    strcpy( ifr.ifr_name, name);
    sockfd = socket(AF_INET,SOCK_DGRAM,0);
    //get inet addr
    if( ioctl( sockfd, SIOCGIFADDR, &ifr) == 0) {
        addr = (struct sockaddr_in *)&(ifr.ifr_addr);
        address = inet_ntoa(addr->sin_addr);
    } else {
        address = "";
    }
    jstring jstr2 = (*env) -> NewStringUTF(env, address);

    close(sockfd);
    return jstr2;
}

jstring Java_com_amlogic_toolkit_infocollection_natives_SysUtils_getMask(JNIEnv* env, jobject obj) {

    struct sockaddr_in *addr;
    struct ifreq ifr;
    char* address;
    int sockfd;
    char *name = "eth0";

    if( strlen(name) >= IFNAMSIZ) exit(0);
    strcpy( ifr.ifr_name, name);
    sockfd = socket(AF_INET,SOCK_DGRAM,0);
    //get inet addr
    if( ioctl( sockfd, SIOCGIFNETMASK, &ifr) == 0){
        addr = (struct sockaddr_in *)&(ifr.ifr_addr);
        address = inet_ntoa(addr->sin_addr);
    } else {
        address = "";
    }
    jstring jstr2 = (*env) -> NewStringUTF(env, address);

    close(sockfd);
    return jstr2;
}

jstring Java_com_amlogic_toolkit_infocollection_natives_SysUtils_getMacAddr(JNIEnv* env, jobject obj) {

    struct sockaddr_in *addr;
    struct ifreq ifr;
    char address[18] = {0};
    int sockfd;
    char *name = "eth0";

    if( strlen(name) >= IFNAMSIZ) exit(0);
    strcpy( ifr.ifr_name, name);
    sockfd = socket(AF_INET,SOCK_DGRAM,0);
    //get inet addr
    u_int8_t hd[6];
    if(ioctl(sockfd, SIOCGIFHWADDR, &ifr) == 0){
        sprintf(address, "%02x:%02x:%02x:%02x:%02x:%02x",
                        (unsigned char)ifr.ifr_hwaddr.sa_data[0],
                        (unsigned char)ifr.ifr_hwaddr.sa_data[1],
                        (unsigned char)ifr.ifr_hwaddr.sa_data[2],
                        (unsigned char)ifr.ifr_hwaddr.sa_data[3],
                        (unsigned char)ifr.ifr_hwaddr.sa_data[4],
                        (unsigned char)ifr.ifr_hwaddr.sa_data[5]);
    } else {
        strcpy(address,"FF:FF:FF:FF:FF:FF");
    }
    jstring jstr2 = (*env) -> NewStringUTF(env, address);

    close(sockfd);

    return jstr2;
}

jstring Java_com_amlogic_toolkit_infocollection_natives_SysUtils_getBroadAddr(JNIEnv* env, jobject obj) {

    struct sockaddr_in *addr;
    struct ifreq ifr;
    char* address;
    int sockfd;
    char *name = "eth0";

    if( strlen(name) >= IFNAMSIZ) exit(0);
    strcpy( ifr.ifr_name, name);
    sockfd = socket(AF_INET,SOCK_DGRAM,0);
    if(ioctl(sockfd,SIOCGIFBRDADDR,&ifr) == 0) {
        addr = (struct sockaddr_in *)&(ifr.ifr_broadaddr);
        address = inet_ntoa(addr->sin_addr);
    } else {
        address = "";
    }
    jstring jstr2 = (*env) -> NewStringUTF(env, address);

    close(sockfd);

    return jstr2;
}

jstring Java_com_amlogic_toolkit_infocollection_natives_NodeInfo_getFirstFrameInfo(JNIEnv* env, jobject obj) {
    FILE* pFile;
    char *pBuf;

    if (access(firstFrameNode,F_OK) != 0)
    {
        pBuf = "";
        jstring jstr2 = (*env) -> NewStringUTF(env, pBuf);
        return jstr2;
    }
    pFile = fopen(firstFrameNode,"r");

    fseek(pFile,0,SEEK_END);
    int len = ftell(pFile);
    pBuf = (char*)malloc(len+1);
    memset(pBuf,0x0,len+1);
    rewind(pFile);
    fread(pBuf,1,len,pFile);

    jstring jstr2 = (*env) -> NewStringUTF(env, pBuf);
    free(pBuf);
    pclose(pFile);

    return jstr2;
}

jstring Java_com_amlogic_toolkit_infocollection_natives_NodeInfo_getSwitchTimeInfo(JNIEnv* env, jobject obj) {
    FILE* pFile;
    char *pBuf;

    if (access(switchTimeInfoNode,F_OK) != 0)
    {
        pBuf = "";
        jstring jstr2 = (*env) -> NewStringUTF(env, pBuf);
        return jstr2;
    }
    pFile = fopen(switchTimeInfoNode,"r");

    fseek(pFile,0,SEEK_END);
    int len = ftell(pFile);
    pBuf = (char*)malloc(len+1);
    memset(pBuf,0x0,len+1);
    rewind(pFile);
    fread(pBuf,1,len,pFile);

    jstring jstr2 = (*env) -> NewStringUTF(env, pBuf);
    free(pBuf);
    pclose(pFile);

    return jstr2;
}

void Java_com_amlogic_toolkit_infocollection_natives_NodeInfo_startDbg(JNIEnv* env, jobject obj) {
    FILE* pFile;
    char *pBuf;

    pFile = fopen(switchTimeDebug,"w");
    fwrite("1", 1, 1, pFile);

    pclose(pFile);
}

void Java_com_amlogic_toolkit_infocollection_natives_NodeInfo_stopDbg(JNIEnv* env, jobject obj) {
    FILE* pFile;
    char *pBuf;

    pFile = fopen(switchTimeDebug,"w");
    fwrite("0", 1, 1, pFile);

    pclose(pFile);
}

static JNINativeMethod gMethods[] = {
        {"getIpAddress", "()Ljava/lang/String;",
            (void*)Java_com_amlogic_toolkit_infocollection_natives_SysUtils_getIpAddress},
        {"getMask", "()Ljava/lang/String;",
            (void*)Java_com_amlogic_toolkit_infocollection_natives_SysUtils_getMask},
        {"getMacAddr", "()Ljava/lang/String;",
            (void*)Java_com_amlogic_toolkit_infocollection_natives_SysUtils_getMacAddr},
        {"getBroadAddr", "()Ljava/lang/String;",
            (void*)Java_com_amlogic_toolkit_infocollection_natives_SysUtils_getBroadAddr},
        {"getFirstFrameInfo", "()Ljava/lang/String;",
            (void*)Java_com_amlogic_toolkit_infocollection_natives_NodeInfo_getFirstFrameInfo},
        {"getSwitchTimeInfo", "()Ljava/lang/String;",
            (void*)Java_com_amlogic_toolkit_infocollection_natives_NodeInfo_getSwitchTimeInfo},
        {"startDbg", "()V", (void*)Java_com_amlogic_toolkit_infocollection_natives_NodeInfo_startDbg},
        {"stopDbg", "()V", (void*)Java_com_amlogic_toolkit_infocollection_natives_NodeInfo_stopDbg},
};

//注册native方法到java中
static int registerNativeMethods(JNIEnv* env, const char* className,JNINativeMethod* gMethods,
                                 int numMethods)
{
    jclass clazz;
    clazz = (*env)->FindClass(env, className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if ((*env)->RegisterNatives(env, clazz, gMethods,numMethods) < 0){
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

int register_ndk_load(JNIEnv *env)
{
    return registerNativeMethods(env, classPathName,gMethods,sizeof(gMethods)/sizeof(gMethods[0]));
    //NELEM(gMethods));
}

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    JNIEnv* env = NULL;
    jint result = -1;

    if ((*vm)->GetEnv(vm, (void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return result;
    }

    register_ndk_load(env);

    // 返回jni的版本
    return JNI_VERSION_1_6;
}