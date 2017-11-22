package com.zlikun.open.sdk;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.IQueryResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.TagTarget;
import com.gexin.rp.sdk.base.uitls.AppConditions;
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
 * 按Tag推送
 * http://docs.getui.com/server/java/api/#2
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017-11-22 17:25
 */
@Slf4j
public class PushToTagTest {

    private IGtPush client;

    @Before
    public void init() {
        client = new IGtPush(AppConstant.APP_KEY, AppConstant.MASTER_SECRET ,true);
    }

    @Test
    public void setTag() {
        // 设置Tag
        IQueryResult ret = client.setClientTag(AppConstant.APP_ID, AppConstant.CLIENT_ID, Arrays.asList("nginx", "netty", "jetty")) ;
        // {result=Success}
        log.info(ret.getResponse().toString());

        // 查询Tag
        IPushResult ret2 = client.getUserTags(AppConstant.APP_ID, AppConstant.CLIENT_ID) ;
        // {result=ok, clientId=cb9573d8251a8acc984626c19c8dbe5a, tags=nginx jetty netty}
        log.info(ret2.getResponse().toString());

    }

    @Test
    public void push() throws Exception {

        AbstractTemplate template = createTemplate(AppConstant.APP_ID, AppConstant.APP_KEY, "透传消息-7");

        AppMessage am = new AppMessage();
        am.setOffline(true);
        am.setOfflineExpireTime(24 * 3600 * 1000);
        am.setData(template);
        am.setAppIdList(Arrays.asList(AppConstant.APP_ID));

        AppConditions ac = new AppConditions();
        ac.addCondition(AppConditions.TAG, Arrays.asList("jetty", "kafka"));
        am.setConditions(ac);

        IPushResult ret = null;
        try {
            ret = client.pushMessageToApp(am);
        } catch (RequestException e) {
            log.error("推送出错!", e);
        }
        if (ret != null) {
            // {result=ok, contentId=OSA-1122_CCNQ2kxuLt8rbwJdddiKS6}
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
