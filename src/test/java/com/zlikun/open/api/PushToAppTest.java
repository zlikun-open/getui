package com.zlikun.open.api;

import com.zlikun.open.AppConstant;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * http://docs.getui.com/server/rest/push/#3-toapp
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017-11-20 16:43
 */
@Slf4j
public class PushToAppTest {

    final OkHttpClient client = new OkHttpClient.Builder().build() ;

    @Test
    public void push_app() throws IOException {

        // 暂未测试
        String body = String.format("") ;

        log.info("request body : \n{}" ,body);

        Request request = new Request.Builder()
                .url("https://restapi.getui.com/v1/" + AppConstant.APP_ID + "/push_app")
                .addHeader("Content-Type", "application/json")
                .addHeader("authtoken", AppConstant.AUTH_TOKEN)
                .post(RequestBody.create(MediaType.parse("application/json"), body))
                .build();

        // 执行请求，并输出响应
        Response response = client.newCall(request).execute() ;

        log.info("code = {} ,message = {}" ,response.code() ,response.message());
        assertTrue(response.isSuccessful()) ;

        log.info("/------------\n{}\n------------------------------------------------------------/" ,
                response.body().string());

    }



}
