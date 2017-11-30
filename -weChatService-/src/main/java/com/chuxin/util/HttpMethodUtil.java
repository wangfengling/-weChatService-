package com.chuxin.util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by wangzhen on 2017/7/5.
 */
public class HttpMethodUtil {
    public static String doGet(String url){

        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK){
                return "HttpClient get方法请求失败" + getMethod.getStatusLine();
            }
            byte[] responseBody = getMethod.getResponseBody();
            String results = new String(responseBody);
            return results;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String doPost(String url,String dataParams){
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        try {
            RequestEntity requestEntity = new StringRequestEntity(dataParams, "application/json", "UTF-8");
            postMethod.setRequestEntity(requestEntity);
            postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
            //设置超时的时间
            postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000);
            int statusCode = httpClient.executeMethod(postMethod);
            if (statusCode != HttpStatus.SC_OK){
                return "HttpClient get方法请求失败" + postMethod.getStatusLine();
            }
            String result = new String(postMethod.getResponseBody());
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
