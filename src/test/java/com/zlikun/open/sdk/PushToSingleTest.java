package com.zlikun.open.sdk;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.zlikun.open.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

/**
 * 使用个推提供SDK实现推送
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017-11-22 14:07
 */
@Slf4j
public class PushToSingleTest {

    private String host = "http://sdk.open.api.igexin.com/apiex.htm" ;

    private IGtPush push ;

    @Before
    public void init() {
        push = new IGtPush(host, AppConstant.APP_KEY, AppConstant.MASTER_SECRET);
    }

    @Test
    public void push() {

        LinkTemplate template = new LinkTemplate();
        // 设置APP_ID与APP_KEY
        template.setAppId(AppConstant.APP_ID);
        template.setAppkey(AppConstant.APP_KEY);

        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle("通知栏-标题");
        style.setText("通知栏-内容");
        // 配置通知栏图标
        style.setLogo("icon.png");
        // 配置通知栏网络图标
        style.setLogoUrl("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);

        // 设置打开的网址地址
        template.setUrl("https://zlikun.com");

        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(AppConstant.APP_ID);
        target.setClientId(AppConstant.CLIENT_ID);
        //target.setAlias(Alias);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            log.error("推送出错!" ,e);
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            log.info(ret.getResponse().toString());
        } else {
            log.info("服务器响应异常");
        }

    }

}
