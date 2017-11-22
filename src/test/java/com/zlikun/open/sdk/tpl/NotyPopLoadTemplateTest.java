package com.zlikun.open.sdk.tpl;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.AbstractTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.NotyPopLoadTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.zlikun.open.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * 点击通知弹框下载模板，未实际测试
 * http://docs.getui.com/server/java/template/#3
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017-11-22 16:34
 */
@Slf4j
public class NotyPopLoadTemplateTest {

    private IGtPush client;

    @Before
    public void init() {
        client = new IGtPush(AppConstant.APP_KEY, AppConstant.MASTER_SECRET ,true);
    }

    @After
    public void destroy() throws IOException {
        if (client != null) client.close();
    }

    @Test
    public void push() throws Exception {

        AbstractTemplate template = createTemplate(AppConstant.APP_ID ,
                AppConstant.APP_KEY ,
                "通知标题-3" ,
                "通知正文-3" ,
                "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png") ;

        // 单推消息体：http://docs.getui.com/server/java/interface/#2
        SingleMessage message = new SingleMessage();
        // 消息离线是否存储
        message.setOffline(true);
        // 消息离线存储多久，单位为毫秒，默认1h，3600*1000
        message.setOfflineExpireTime(24 * 3600 * 1000);
        // 推送消息消息内容
        message.setData(template);
        // 1：wifi推送；0：不限制推送方式
        message.setPushNetWorkType(0);

        // 推送目标：http://docs.getui.com/server/java/interface/#3
        Target target = new Target();
        target.setAppId(AppConstant.APP_ID);
        target.setClientId(AppConstant.CLIENT_ID);

        IPushResult ret = null;

        // 对单个用户推送消息：http://docs.getui.com/server/java/push/#1
        try {
            // 正常发送使用
            ret = client.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            log.error("推送出错，发起重试：{}", e.getRequestId(), e);
            // 异常重试发送使用
            ret = client.pushMessageToSingle(message, target, e.getRequestId());
        }

        // 推送结果：http://docs.getui.com/server/java/interface/#4
        if (ret != null) {
            // {result=ok, taskId=OSS-1122_27e72ae64bbef9f0c7b7242064bfbbed, status=successed_offline}
            // {result=ok, taskId=OSS-1122_5799a71b06eec29c597a11175dd90398, status=successed_online}
            log.info(ret.getResponse().toString());
        } else {
            log.info("服务器响应异常");
        }

    }

    /**
     * 创建消息模板
     * @param appId
     * @param appKey
     * @param title
     * @param content
     * @param logoUrl
     * @return
     * @throws Exception
     */
    private AbstractTemplate createTemplate(String appId ,String appKey ,String title ,String content,String logoUrl) throws Exception {
        // 通知模板
        NotyPopLoadTemplate template = new NotyPopLoadTemplate();
        // 设定接收的应用
        template.setAppId(appId);
        // 用于鉴定身份是否合法
        template.setAppkey(appKey);
//        // 收到消息的展示时间
//        template.setDuration("", "");
        // 弹出框标题
        template.setPopTitle(title);
        // 弹出框内容
        template.setPopContent(content);
        // 弹出框确定按钮文本显示
        template.setPopButton1("确定");
        // 弹出框取消按钮文本显示
        template.setPopButton2("取消");
        // 弹出框图标
        template.setPopImage(null);
        // 下载图标，如果是本地，则需要加入file://前缀；如果是网络图标，则直接写网络地址
        template.setLoadIcon(null);
        // 下载标题
        template.setLoadTitle("下载标题");
        // 下载地址
        template.setLoadUrl(null);
        // 是否自动安装
        template.setAutoInstall(false);
        // 安装完成后是否自动启动应用程序
        template.setActived(false);
        // 设置安卓标识
        template.setAndroidMark(null);
        // 设置塞班标识
        template.setSymbianMark(null);
        // 设置苹果标识
        template.setIphoneMark(null);

        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle(title);
        style.setText(content);
        style.setLogoUrl(logoUrl);
        // 收到通知是否响铃：true响铃，false不响铃。默认响铃。
        style.setRing(true);
        // 收到通知是否振动：true振动，false不振动。默认振动。
        style.setVibrate(true);
        // 通知是否可清除： true可清除，false不可清除。默认可清除。
        style.setClearable(true);
        // 通知栏消息布局样式(Style0 系统样式 Style1 个推样式 Style4 背景图样式 Style6 展开式通知样式)，setStyle是新方法，使用了该方法后原来的设置标题、文本等方法就不起效
        template.setStyle(style);

        return template ;
    }

}
