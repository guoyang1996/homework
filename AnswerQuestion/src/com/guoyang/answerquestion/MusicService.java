package com.guoyang.answerquestion;
import java.io.IOException;

import android.app.Service;  
import android.content.Intent;  
import android.media.MediaPlayer;  
import android.os.IBinder;  
  
public class MusicService extends Service {  
  
	private MediaPlayer success_mp;  
	private MediaPlayer failure_mp;  
	private MediaPlayer amazing_mp;  
	  private String state;
    @Override  
    public void onStart(Intent intent, int startId) {  
    	state=intent.getStringExtra("state");
    	switch(state){
    	case "success":
    		success_mp.start();
    		break;
    	case "failure":
    		failure_mp.start();
    		break;
    	case "amazing":
    		amazing_mp.start();
    		break;
    	}
        super.onStart(intent, startId);  
    }  
  
    @Override  
    public void onCreate() {  
        // ��ʼ��������Դ  
        try {  
            // ����MediaPlayer����  
            success_mp = new MediaPlayer();  
           failure_mp = new MediaPlayer();  
           amazing_mp = new MediaPlayer();  
            // �����ֱ�����res/raw/xingshu.mp3,R.java���Զ�����{public static final int xingshu=0x7f040000;}  
            success_mp = MediaPlayer.create(MusicService.this, R.raw.success);  
            failure_mp = MediaPlayer.create(MusicService.this, R.raw.failure);  
            amazing_mp = MediaPlayer.create(MusicService.this, R.raw.amazing);  
            // ��MediaPlayerȡ�ò�����Դ��stop()֮��Ҫ׼��PlayBack��״̬ǰһ��Ҫʹ��MediaPlayer.prepeare()  
            success_mp.prepare();  
            failure_mp.prepare();  
           amazing_mp.prepare();  
        } catch (IllegalStateException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
        super.onCreate();  
    }  
  
    @Override  
    public void onDestroy() {  
        // ����ֹͣʱֹͣ�������ֲ��ͷ���Դ  
       failure_mp.stop();  
        failure_mp.release();  
       success_mp.stop();  
       success_mp.release();  
      amazing_mp.stop();  
      amazing_mp.release();  
        super.onDestroy();  
    }  
  
    @Override  
    public IBinder onBind(Intent intent) {  
        return null;  
    }  
}  
