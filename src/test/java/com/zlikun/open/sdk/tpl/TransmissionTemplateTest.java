package com.zlikun.open.sdk.tpl;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.AbstractTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.zlikun.open.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * 透传消息模版<br>
 * 透传消息是指消息传递到客户端只有消息内容，展现形式由客户端自行定义。客户端可自定义通知的展现形式，也可自定义通知到达之后的动作，或者不做任何展现。IOS推送也使用该模板。<br>
 *     场景1：自定义通知栏样式不想使用默认的通知栏样式，即可使用消息透传的形式，自定义通知栏展现形式，使发送的通知更醒目，更突出。<br>
 *     场景2：自定义通知到达之后的动作希望用户点击通知后启动应用直接到和通知相关的界面，免去中间跳转的流失。如用户预订更新的某本图书有更新，点击通知直接启动应用到对应图书的页面，免去用户打开应用后的查找，节省中间环节，提高转化。<br>
 *     场景3：仅传递信息，不做任何展示推送一串代码给应用，该代码仅此app可以解析。收到透传消息时，界面不作任何展示，用户无感知，应用收到命令后按代码执行操作。<br>
 * http://docs.getui.com/server/java/template/#4
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017-11-22 16:42
 */
@Slf4j
public class TransmissionTemplateTest {

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
                "透传消息-4") ;

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
            // {result=ok, taskId=OSS-1122_1f30d59612b74f09619c65833b923605, status=successed_offline}
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
     * @param transmissionContent
     * @return
     * @throws Exception
     */
    private AbstractTemplate createTemplate(String appId ,String appKey ,String transmissionContent) throws Exception {
        // 通知模板
        TransmissionTemplate template = new TransmissionTemplate();
        // 设定接收的应用
        template.setAppId(appId);
        // 用于鉴定身份是否合法
        template.setAppkey(appKey);
//        // 收到消息的展示时间
//        template.setDuration("", "");
        // 透传内容，不支持转义字符
        template.setTransmissionContent(transmissionContent);
        // 收到消息是否立即启动应用： 1为立即启动，2则广播等待客户端自启动
        template.setTransmissionType(1);
        // IOS推送使用该字段(Payload)
        // template.setAPNInfo(null);

        return template ;
    }

}
