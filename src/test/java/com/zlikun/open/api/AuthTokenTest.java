package com.zlikun.open.api;

import com.zlikun.open.AppConstant;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * 鉴权API，http://docs.getui.com/server/rest/other_if/
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017-11-17 18:22
 */
@Slf4j
public class AuthTokenTest {

    final OkHttpClient client = new OkHttpClient.Builder().build() ;

    /**
     * 请求鉴权
     * @throws IOException
     */
    @Test
    public void auth() throws IOException {

        long timestamp = System.currentTimeMillis() ;

        // 生成签名
        String sign = DigestUtils.sha256Hex(AppConstant.APP_KEY + timestamp + AppConstant.MASTER_SECRET) ;
        log.info("timestamp = {} ,sign = {}" ,timestamp ,sign);

        // 请求AUTH_TOKEN
        Request request = new Request.Builder()
                .url("https://restapi.getui.com/v1/" + AppConstant.APP_ID + "/auth_sign")
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(MediaType.parse("application/json"),
                        String.format("{\"sign\":\"%s\",\"timestamp\":\"%s\",\"appkey\":\"%s\"}",
                                sign, String.valueOf(timestamp), AppConstant.APP_KEY)))
                .build();

        // 执行请求，并输出响应
        Response response = client.newCall(request).execute() ;

        log.info("code = {} ,message = {}" ,response.code() ,response.message());
        assertTrue(response.isSuccessful()) ;

        // {"result":"ok","expire_time":"1511236638021","auth_token":"1f4282ccc76dd4e23c0e1a895ee568d75ea912e886ade5bd24ac664e58d4d365"}
        log.info("/------------\n{}\n------------------------------------------------------------/" ,
                response.body().string());

    }

    /**
     * 关闭鉴权
     * @throws IOException
     */
    @Test
    public void closeAuth() throws IOException {

        long timestamp = System.currentTimeMillis() ;

        // 生成签名
        String sign = DigestUtils.sha256Hex(AppConstant.APP_KEY + timestamp + AppConstant.MASTER_SECRET) ;
        log.info("timestamp = {} ,sign = {}" ,timestamp ,sign);

        // 请求AUTH_TOKEN
        Request request = new Request.Builder()
                .url("https://restapi.getui.com/v1/" + AppConstant.APP_ID + "/auth_close")
                .addHeader("Content-Type", "application/json")
                // POST请求不传参数还不行，R !!!
                .post(RequestBody.create(MediaType.parse("application/json"),
                        String.format("timestamp=%s", String.valueOf(timestamp))))
                .build();

        // 执行请求，并输出响应
        Response response = client.newCall(request).execute() ;

        log.info("code = {} ,message = {}" ,response.code() ,response.message());
        assertTrue(response.isSuccessful()) ;

        log.info("/------------\n{}\n------------------------------------------------------------/" ,
                response.body().string());

    }

}
