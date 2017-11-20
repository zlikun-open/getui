package com.zlikun.open.api;

import com.zlikun.open.AppConstant;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017-11-20 15:53
 */
@Slf4j
public class PushToSingleTest {

    final OkHttpClient client = new OkHttpClient.Builder().build() ;

    @Test
    public void notification() throws IOException {

        // 消息模板：http://docs.getui.com/server/rest/template/
        // transmission_type (boolean) 收到消息是否立即启动应用，true为立即启动，false则广播等待启动，默认是否
        // transmission_content (string) 透传内容
        // duration_begin (string) 设定展示开始时间，格式为yyyy-MM-dd HH:mm:ss
        // duration_end (string) 设定展示结束时间，格式为yyyy-MM-dd HH:mm:ss
        // style (style) 通知栏消息布局样式，见底下Style说明
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
                "    },\n" +
                "    \"cid\": \"%s\",\n" +
                "    \"requestid\": \"%s\"\n" +
                "}", AppConstant.APP_KEY, "通知消息-正文", "通知消息-标题", "通知消息-透传",
                AppConstant.CLIENT_ID, System.currentTimeMillis()) ;

        log.info("request body : \n{}" ,body);

        Request request = new Request.Builder()
                .url("https://restapi.getui.com/v1/" + AppConstant.APP_ID + "/push_single")
                .addHeader("Content-Type", "application/json")
                .addHeader("authtoken", AppConstant.AUTH_TOKEN)
                .post(RequestBody.create(MediaType.parse("application/json"), body))
                .build();

        // 执行请求，并输出响应
        Response response = client.newCall(request).execute() ;

        log.info("code = {} ,message = {}" ,response.code() ,response.message());
        assertTrue(response.isSuccessful()) ;

        // {"result":"invalid_param","desc":"requestid length should be 10~30"}
        // {"result":"ok","taskid":"RASS_1120_7b035ff22b3bd8cf39f1471b2ab215ba","status":"successed_online"}
        log.info("/------------\n{}\n------------------------------------------------------------/" ,
                response.body().string());

    }

    @Test
    public void transmission() throws IOException {

        String body = String.format("{\n" +
                        "    \"message\":{\n" +
                        "            \"appkey\":\"%s\",\n" +
                        "            \"is_offline\":true,\n" +
                        "            \"msgtype\":\"transmission\"\n" +
                        "    },\n" +
                        "    \"transmission\":{\n" +
                        "            \"transmission_type\":false,\n" +
                        "            \"transmission_content\":\"%s\",\n" +
                        "            \"duration_begin\":\"%s\",\n" +
                        "            \"duration_end\":\"%s\"\n" +
                        "    },\n" +
                        "    \"push_info\": {\n" +
                        "       \"aps\": {\n" +
                        "           \"alert\": {\n" +
                        "               \"title\": \"%s\",\n" +
                        "               \"body\": \"%s\"\n" +
                        "           },\n" +
                        "           \"autoBadge\": \"+1\",\n" +
                        "           \"content-available\": 1\n" +
                        "       }\n" +
                        "    },\n" +
                        "    \"cid\":\"%s\",\n" +
                        "    \"requestid\":\"%s\"\n" +
                        "}", AppConstant.APP_KEY,
                "这是一条透传消息-" + System.currentTimeMillis(),
                "2017-11-20 16:00:00", "2017-11-20 17:00:00",
                "透传消息-标题", "透传消息-正文",
                AppConstant.CLIENT_ID, System.currentTimeMillis()) ;

        log.info("request body : \n{}" ,body);

        Request request = new Request.Builder()
                .url("https://restapi.getui.com/v1/" + AppConstant.APP_ID + "/push_single")
                .addHeader("Content-Type", "application/json")
                .addHeader("authtoken", AppConstant.AUTH_TOKEN)
                .post(RequestBody.create(MediaType.parse("application/json"), body))
                .build();

        // 执行请求，并输出响应
        Response response = client.newCall(request).execute() ;

        log.info("code = {} ,message = {}" ,response.code() ,response.message());
        assertTrue(response.isSuccessful()) ;

        // {"result":"ok","taskid":"RASS_1120_718d9db8eb26b6e0479188d2bdb7c3c4","status":"successed_offline"}
        log.info("/------------\n{}\n------------------------------------------------------------/" ,
                response.body().string());

    }

}
