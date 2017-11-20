package com.zlikun.open.api;

import com.zlikun.open.AppConstant;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * http://docs.getui.com/server/rest/other_if/
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017-11-20 13:04
 */
@Slf4j
public class AliasTest {

    final OkHttpClient client = new OkHttpClient.Builder().build() ;

    final String alias = "zlikun" ;

    /**
     * 为CID绑定别名
     * @throws IOException
     */
    @Test
    public void bind() throws IOException {

        // 一个ClientID只能绑定一个别名，若已绑定过别名的ClientID再次绑定新别名，则认为与前一个别名自动解绑，绑定新别名
        // 允许将多个ClientID和一个别名绑定，如用户使用多终端，则可将多终端对应的ClientID绑定为一个别名
        // 目前一个别名最多支持绑定10个ClientID
        // 下面测试实际是一个CID，模拟多个的情况
        Request request = new Request.Builder()
                .url("https://restapi.getui.com/v1/" + AppConstant.APP_ID + "/bind_alias")
                .addHeader("Content-Type", "application/json")
                .addHeader("authtoken", AppConstant.AUTH_TOKEN)
                .post(RequestBody.create(MediaType.parse("application/json"),
                        String.format("{\"alias_list\":[{\"cid\":\"%s\",\"alias\":\"%s\"},{\"cid\":\"%s\",\"alias\":\"%s\"}]}",
                                AppConstant.CLIENT_ID, alias, AppConstant.CLIENT_ID, alias))
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

    /**
     * 单个CID和别名解绑
     * @throws IOException
     */
    @Test
    public void unbind() throws IOException {

        Request request = new Request.Builder()
                .url("https://restapi.getui.com/v1/" + AppConstant.APP_ID + "/unbind_alias")
                .addHeader("Content-Type", "application/json")
                .addHeader("authtoken", AppConstant.AUTH_TOKEN)
                .post(RequestBody.create(MediaType.parse("application/json"),
                        String.format("{\"cid\":\"%s\",\"alias\":\"%s\"}",
                                AppConstant.CLIENT_ID, alias))
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

    /**
     * 解绑别名所有CID
     * @throws IOException
     */
    @Test
    public void unbind_all() throws IOException {

        Request request = new Request.Builder()
                .url("https://restapi.getui.com/v1/" + AppConstant.APP_ID + "/unbind_alias_all")
                .addHeader("Content-Type", "application/json")
                .addHeader("authtoken", AppConstant.AUTH_TOKEN)
                .post(RequestBody.create(MediaType.parse("application/json"),
                        String.format("{\"alias\":\"%s\"}", alias))
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

    /**
     * 查询别名绑定的CID列表
     * @throws IOException
     */
    @Test
    public void query_for_alias() throws IOException {

        Request request = new Request.Builder()
                .url("https://restapi.getui.com/v1/" + AppConstant.APP_ID + "/query_cid/" + alias)
                .addHeader("Content-Type", "application/json")
                .addHeader("authtoken", AppConstant.AUTH_TOKEN)
                .get()
                .build();

        // 执行请求，并输出响应
        Response response = client.newCall(request).execute() ;

        log.info("code = {} ,message = {}" ,response.code() ,response.message());
        assertTrue(response.isSuccessful()) ;

        // {"result":"ok","cid":["cb9573d8251a8acc984626c19c8dbe5a"]}
        log.info("/------------\n{}\n------------------------------------------------------------/" ,
                response.body().string());

    }

    /**
     * 根据CID查询别名
     * @throws IOException
     */
    @Test
    public void query_for_cid() throws IOException {

        Request request = new Request.Builder()
                .url("https://restapi.getui.com/v1/" + AppConstant.APP_ID + "/query_alias/" + AppConstant.CLIENT_ID)
                .addHeader("Content-Type", "application/json")
                .addHeader("authtoken", AppConstant.AUTH_TOKEN)
                .get()
                .build();

        // 执行请求，并输出响应
        Response response = client.newCall(request).execute() ;

        log.info("code = {} ,message = {}" ,response.code() ,response.message());
        assertTrue(response.isSuccessful()) ;

        // {"result":"ok","alias":"zlikun"}
        log.info("/------------\n{}\n------------------------------------------------------------/" ,
                response.body().string());

    }



}
