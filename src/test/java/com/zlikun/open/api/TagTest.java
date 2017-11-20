package com.zlikun.open.api;

import com.zlikun.open.AppConstant;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * http://docs.getui.com/server/rest/other_if/
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017-11-20 15:17
 */
@Slf4j
public class TagTest {

    final OkHttpClient client = new OkHttpClient.Builder().build() ;

    final String tag0 = "zlikun_0" ;
    final String tag2 = "zlikun_2" ;

    @Test
    public void set_tags() throws IOException {

        Request request = new Request.Builder()
                .url("https://restapi.getui.com/v1/" + AppConstant.APP_ID + "/set_tags")
                .addHeader("Content-Type", "application/json")
                .addHeader("authtoken", AppConstant.AUTH_TOKEN)
                .post(RequestBody.create(MediaType.parse("application/json"),
                        String.format("{\"cid\":\"%s\", \"tag_list\":[\"%s\", \"%s\"]}",
                                AppConstant.CLIENT_ID, tag0, tag2))
                )
                .build();

        // 执行请求，并输出响应
        Response response = client.newCall(request).execute() ;

        log.info("code = {} ,message = {}" ,response.code() ,response.message());
        assertTrue(response.isSuccessful()) ;

        // {"result":"ok"}
        log.info("/------------\n{}\n------------------------------------------------------------/" ,
                response.body().string());

    }

    @Test
    public void get_tags() throws IOException {

        Request request = new Request.Builder()
                .url("https://restapi.getui.com/v1/" + AppConstant.APP_ID + "/get_tags/" + AppConstant.CLIENT_ID)
                .addHeader("Content-Type", "application/json")
                .addHeader("authtoken", AppConstant.AUTH_TOKEN)
                .get()
                .build();

        // 执行请求，并输出响应
        Response response = client.newCall(request).execute() ;

        log.info("code = {} ,message = {}" ,response.code() ,response.message());
        assertTrue(response.isSuccessful()) ;

        // {"result":"ok","cid":"cb9573d8251a8acc984626c19c8dbe5a","tags":"zlikun_2 zlikun_0"}
        log.info("/------------\n{}\n------------------------------------------------------------/" ,
                response.body().string());

    }

}
