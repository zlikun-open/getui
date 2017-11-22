package com.zlikun.open.sdk;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.AbstractTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.zlikun.open.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 对指定列表用户推送消息
 * http://docs.getui.com/server/java/push/#2
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017-11-22 16:50
 */
@Slf4j
public class PushToListTest {

    private IGtPush client;

    @Before
    public void init() {
        client = new IGtPush(AppConstant.APP_KEY, AppConstant.MASTER_SECRET ,true);
    }

    /**
     * 返回任务ID
     * @return
     */
    private String getContentId() throws Exception {
        ListMessage lm = new ListMessage() ;
        lm.setData(createTemplate(AppConstant.APP_ID ,AppConstant.APP_KEY ,"透传消息-5"));
        lm.setOffline(true);
        lm.setOfflineExpireTime(24 * 3600 * 1000);
        String taskId = client.getContentId(lm);
        // OSL-1122_qxkkKDqXdk9GeXgalSSIp5
        log.info("batch push task : {}" ,taskId);
        return taskId ;
    }

    @Test
    public void push() throws Exception {

        String taskId = getContentId() ;

        // 配置推送目标
        List<Target> targets = new ArrayList<>();
        Target target1 = new Target();
        target1.setAppId(AppConstant.APP_ID);
        target1.setClientId(AppConstant.CLIENT_ID);
        // 推荐设置多个，最多50个(与对方技术人员沟通得到的数值)
        targets.add(target1);

        IPushResult ret = null;

        try {
            ret = client.pushMessageToList(taskId, targets);
        } catch (RequestException e) {
            log.error("推送出错!" ,e);
        }
        if (ret != null) {
            // {result=ok, contentId=OSL-1122_qxkkKDqXdk9GeXgalSSIp5}
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
