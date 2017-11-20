package com.zlikun.open.api;

import com.zlikun.open.AppConstant;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * http://docs.getui.com/server/rest/push/#2-tolist
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017-11-20 16:33
 */
@Slf4j
public class PushToListTest {

    final OkHttpClient client = new OkHttpClient.Builder().build() ;

    @Test
    public void save_list_body() throws IOException {

        String body = String.format("{\n" +
                        "    \"message\": {\n" +
                        "        \"appkey\": \"%s\",\n" +
                        "        \"is_offline\": true,\n" +
                        "        \"offline_expire_time\": 10000000,\n" +
                        "        \"msgtype\": \"notification\"\n" +
                        "    },\n" +
                        "    \"notification\": {\n" +
                        "        \"style\": {\n" +
                        "            \"type\": 0,\n" +
                        "            \"text\": \"%s\",\n" +
                        "            \"title\": \"%s\"\n" +
                        "        },\n" +
                        "        \"transmission_type\": true,\n" +
                        "        \"transmission_content\": \"%s\"\n" +
                        "    }\n" +
                        "}", AppConstant.APP_KEY, "通知消息-正文", "通知消息-标题", "通知消息-透传",
                AppConstant.CLIENT_ID, System.currentTimeMillis()) ;

        log.info("request body : \n{}" ,body);

        Request request = new Request.Builder()
                .url("https://restapi.getui.com/v1/" + AppConstant.APP_ID + "/save_list_body")
                .addHeader("Content-Type", "application/json")
                .addHeader("authtoken", AppConstant.AUTH_TOKEN)
                .post(RequestBody.create(MediaType.parse("application/json"), body))
                .build();

        // 执行请求，并输出响应
        Response response = client.newCall(request).execute() ;

        log.info("code = {} ,message = {}" ,response.code() ,response.message());
        assertTrue(response.isSuccessful()) ;

        // {"result":"ok","taskid":"RASL_1120_7dfa9502eaa94d7c91c20d3e0d6f153b"}
        log.info("/------------\n{}\n------------------------------------------------------------/" ,
                response.body().string());

    }



    @Test
    public void push_list() throws IOException {

        String body = String.format("{\n" +
                        "    \"cid\": [\n" +
                        "        \"%s\"\n" +
                        "    ],\n" +
                        "    \"taskid\": \"%s\",\n" +
                        "    \"need_detail\": true\n" +
                        "}", AppConstant.CLIENT_ID, "RASL_1120_7dfa9502eaa94d7c91c20d3e0d6f153b") ;

        log.info("request body : \n{}" ,body);

        Request request = new Request.Builder()
                .url("https://restapi.getui.com/v1/" + AppConstant.APP_ID + "/push_list")
                .addHeader("Content-Type", "application/json")
                .addHeader("authtoken", AppConstant.AUTH_TOKEN)
                .post(RequestBody.create(MediaType.parse("application/json"), body))
                .build();

        // 执行请求，并输出响应
        Response response = client.newCall(request).execute() ;

        log.info("code = {} ,message = {}" ,response.code() ,response.message());
        assertTrue(response.isSuccessful()) ;

        // {"result":"ok","cid_details":{"cb9573d8251a8acc984626c19c8dbe5a":"successed_offline"},"taskid":"RASL_1120_7dfa9502eaa94d7c91c20d3e0d6f153b"}
        log.info("/------------\n{}\n------------------------------------------------------------/" ,
                response.body().string());

    }



}
