package com.example.ui;

import com.example.util.HttpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
      
    }

	private void init() {
		final EditText  user_et = (EditText) findViewById(R.id.et_user);
		final EditText pw_et = (EditText) findViewById(R.id.et_pw);
		Button login_btn = (Button) findViewById(R.id.btn_login);
		
		login_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(user_et.getText().length()==0){
					user_et.setError("用户名不能为空！");
				}else if(pw_et.getText().length()==0){
					pw_et.setError("密码不能为空！");
				}else{
					//Toast toast = Toast.makeText(getApplicationContext(), user_et.getText()+""+pw_et.getText(), Toast.LENGTH_LONG);
					//toast.show();
					
					final Handler handler = new Handler(){  
					    @Override  
					    public void handleMessage(Message msg) {  
					        super.handleMessage(msg);  
					        Bundle data = msg.getData();  
					        String val = data.getString("result");  
					        if(val.equals("SUCCESS")){
					        	Intent intent=new Intent(LoginActivity.this, MainActivity.class);
								LoginActivity.this.startActivity(intent);
								//手动结束
								LoginActivity.this.finish();
					        }else{
					        	Toast toast = Toast.makeText(getApplicationContext(), "用户名或密码不正确！请重新登录！", Toast.LENGTH_LONG);
								toast.show();
					        }
					        
					        Log.i("mylog","请求结果-->" + val);  
					    }  
					};
					  
					Runnable runnable = new Runnable(){  
					    @Override  
					    public void run() {  
					       
					    	String ip = "172.20.144.0";
							String result = HttpUtil.get("http://"+ip+":8080/aaa/LoginServlet?ie=utf-8&f=8&rsv_bp=1&name="+user_et.getText()+"&password="+pw_et.getText());
							Log.i("result", result);
					        Message msg = new Message();  
					        Bundle data = new Bundle();  
					        data.putString("result",result);  
					        msg.setData(data);  
					        handler.sendMessage(msg);  
					    }  
					};
					new Thread(runnable).start();
					
				}
				
			}
			
		});
			
	
	}




}
