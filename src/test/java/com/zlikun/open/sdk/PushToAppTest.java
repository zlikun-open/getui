package com.zlikun.open.sdk;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.AbstractTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.zlikun.open.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * 对指定应用群推消息
 * http://docs.getui.com/server/java/push/#3
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017-11-22 17:20
 */
@Slf4j
public class PushToAppTest {

    private IGtPush push ;

    @Before
    public void init() {
        push = new IGtPush(AppConstant.APP_KEY, AppConstant.MASTER_SECRET ,true);
    }

    @Test
    public void push() throws Exception {

        AbstractTemplate template = createTemplate(AppConstant.APP_ID, AppConstant.APP_KEY, "透传消息-6") ;

        AppMessage am = new AppMessage() ;
        am.setAppIdList(Arrays.asList(AppConstant.APP_ID));
        am.setData(template);
        am.setOffline(true);

        IPushResult ret = null;
        try {
            ret = push.pushMessageToApp(am) ;
        } catch (RequestException e) {
            log.error("推送出错!" ,e);
        }
        if (ret != null) {
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
    private AbstractTemplate createTemplate(String appId , String appKey , String transmissionContent) throws Exception {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(transmissionContent);
        template.setTransmissionType(1);
        return template ;
    }

}
