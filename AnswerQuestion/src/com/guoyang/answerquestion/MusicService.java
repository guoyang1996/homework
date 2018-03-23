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
        // 初始化音乐资源  
        try {  
            // 创建MediaPlayer对象  
            success_mp = new MediaPlayer();  
           failure_mp = new MediaPlayer();  
           amazing_mp = new MediaPlayer();  
            // 将音乐保存在res/raw/xingshu.mp3,R.java中自动生成{public static final int xingshu=0x7f040000;}  
            success_mp = MediaPlayer.create(MusicService.this, R.raw.success);  
            failure_mp = MediaPlayer.create(MusicService.this, R.raw.failure);  
            amazing_mp = MediaPlayer.create(MusicService.this, R.raw.amazing);  
            // 在MediaPlayer取得播放资源与stop()之后要准备PlayBack的状态前一定要使用MediaPlayer.prepeare()  
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
        // 服务停止时停止播放音乐并释放资源  
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
