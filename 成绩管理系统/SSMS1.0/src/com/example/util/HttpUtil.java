package com.example.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpUtil {
	/** 
     * 以get方式发送请求，访问web 
     * @param uri web地址 
     * @return 响应数据 
     */  
    public static String get(String uri){  
        BufferedReader reader = null;  
        StringBuffer sb = null;  
        String result = "";  
        HttpClient client = new DefaultHttpClient();  
        HttpGet request = new HttpGet(uri);  
        try {  
            //发送请求，得到响应  
            HttpResponse response = client.execute(request);  
              
            //请求成功  
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
                reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));  
                sb = new StringBuffer();  
                String line = "";  
                String NL = System.getProperty("line.separator");  
                while((line = reader.readLine()) != null){  
                    sb.append(line);  
                }  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        finally{  
            try {  
                if (null != reader){  
                    reader.close();  
                    reader = null;  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        if (null != sb){  
            result =  sb.toString();  
        }  
        return result;  
    }  
    /** 
     * 以post方式发送请求，访问web 
     * @param uri web地址 
     * @return 响应数据 
     */  
    public static String post(String uri){  
        BufferedReader reader = null;  
        StringBuffer sb = null;  
        String result = "";  
        HttpClient client = new DefaultHttpClient();  
        HttpPost request = new HttpPost(uri);  
          
        //保存要传递的参数  
        List<NameValuePair> params = new ArrayList<NameValuePair>();  
        //添加参数  
        params.add(new BasicNameValuePair("parameter","以Post方式发送请求"));  
          
        try {  
            //设置字符集  
            HttpEntity entity = new UrlEncodedFormEntity(params,"utf-8");  
            //请求对象  
            request.setEntity(entity);  
            //发送请求  
            HttpResponse response = client.execute(request);  
              
            //请求成功  
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
                System.out.println("post success");  
                reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));  
                sb = new StringBuffer();  
                String line = "";  
                String NL = System.getProperty("line.separator");  
                while((line = reader.readLine()) != null){  
                    sb.append(line);  
                }  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        finally{  
            try {  
                //关闭流  
                if (null != reader){  
                    reader.close();  
                    reader = null;  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        if (null != sb){  
            result =  sb.toString();  
        }  
        return result;  
    }  
}
