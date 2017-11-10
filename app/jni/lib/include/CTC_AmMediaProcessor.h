#ifndef _CTC_MEDIA_PROCESSOR_H_
#define _CTC_MEDIA_PROCESSOR_H_


#include "gui/Surface.h"
#include "android_runtime/AndroidRuntime.h"
#include "android_runtime/android_view_Surface.h"
//#include "CTsPlayer.h"
using namespace android;

enum STREAM_MEDIA_TYPE_e
{
	STREAM_MEDIA_TYPE_UNKNOW = 0,
	STREAM_MEDIA_TYPE_CHANNEL = 1, //live
	STREAM_MEDIA_TYPE_VOD,         //vod
	STREAM_MEDIA_TYPE_TVOD         //timeshift
};

enum EVENT_MEDIA_TYPE_e
{
	EVENT_MEDIA_END = 2101,
	EVENT_MEDIA_BEGINING = 2102,
	EVENT_MEDIA_PLAYMODE_CHANGE,
	EVENT_MEDIA_ERROR,
	EVENT_MEDIA_IGMP_ERROR
};

int CTC_SupportProtocol(const char* url);

typedef void (*IPTV_PLAYER_EVT_CallBack)(EVENT_MEDIA_TYPE_e evt, void *handler, void* pvParam);

class CTC_AmMediaProcessor
{
	public:
		CTC_AmMediaProcessor();
		~CTC_AmMediaProcessor();
		int  CTC_GetMediaControlVersion();//��ȡ�汾
		int  CTC_MediaProcessorInit(char* url); //player init, parser stream info
		int  CTC_GetPlayMode();//ȡ�ò���ģʽ
		int  CTC_SetVideoWindow(int x,int y,int width,int height);//������Ƶ��ʾ��λ�ã��Լ���Ƶ��ʾ�Ŀ��
		void CTC_SetSurface(Surface* pSurface);//������ʾ�õ�surface
		void CTC_GetVideoPixels(int& width, int& height);//��ȡ��Ƶ�ֱ���
		bool CTC_IsSoftFit();//�ж��Ƿ����������
		int  CTC_SetEPGSize(int w, int h);//����EPG��С
		int  CTC_VideoShow();//��ʾ��Ƶͼ��
		int  CTC_VideoHide();//������Ƶͼ��
		int  CTC_StartPlay();//begin play after init
		int  CTC_Pause();//��ͣ
		int  CTC_Resume();//��ͣ��Ļָ�
		int  CTC_Fast(int scale);//������߿���, 
		int  CTC_StopFast();//ֹͣ������߿���
		int  CTC_Stop();//ֹͣ
		int  CTC_Seek(int64_t seek_timestamp);//��λ //���ֱ��cpu clock;  �㲥:npt����
		int  CTC_SetVolume(int volume);//�趨����
		int  CTC_GetVolume();//��ȡ����
		int  CTC_SetRatio(int nRatio);//�趨��Ƶ��ʾ����
		int  CTC_GetAudioBalance();//��ȡ��ǰ����
		int  CTC_SetAudioBalance(int nAudioBalance);//��������
		int  CTC_GetCurrentPlayTime();  //get current play time
		int  CTC_SwitchSubtitle(int pid);//switch subtitle
		int  CTC_SwitchAudio(int pid);// switch audio
		void CTC_playerback_register_evt_cb(IPTV_PLAYER_EVT_CallBack pfunc, void *hander); // play info callback
		int  CTC_Release();//release all resource[similar to deconstruct]
		int  CTC_SetNetStreamType(STREAM_MEDIA_TYPE_e eStreamType);
        int  CTC_SetScale(int scale); //set play scale
		int  CTC_PlayByTime(int64_t seektimeMsec);//microsecond
		int  CTC_GetDuraion();
	private:
		void* m_pHandler;
};
#endif
