package com.zlikun.open.sdk;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.IQueryResult;
import com.gexin.rp.sdk.http.IGtPush;
import com.zlikun.open.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

/**
 * http://docs.getui.com/server/java/api/
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017-11-22 17:44
 */
@Slf4j
public class OtherApiTest {

    private IGtPush client;

    @Before
    public void init() {
        client = new IGtPush(AppConstant.APP_KEY, AppConstant.MASTER_SECRET ,true);
    }

    @Test
    public void api() {

        // 获取用户状态
        IQueryResult ret = client.getClientIdStatus(AppConstant.APP_ID, AppConstant.CLIENT_ID) ;
        // {result=Offline, lastLogin=1511331288444, isblack=false}
        log.info(ret.getResponse().toString());

        // Badge设置
        // http://docs.getui.com/server/java/api/#5-badge

        // 获取推送结果
        IPushResult ret2 = client.getPushResult("OSL-1122_qxkkKDqXdk9GeXgalSSIp5") ;
        // {result=ok, msgTotal=1, clickNum=0, pushNum=1, GT={"feedback":1,"displayed":0,"result":"ok","sent":1,"clicked":0}, taskId=OSL-1122_qxkkKDqXdk9GeXgalSSIp5, msgProcess=1}
        log.info(ret2.getResponse().toString());

        // 获取单日用户数据
        IQueryResult ret3 = client.queryAppUserDataByDate(AppConstant.APP_ID, "20171122") ;
        // {result=Success, data={"activeCount":0,"date":"20171122","onlineCount":0,"registTotalCount":0,"appId":"zS8CchJL2nAPGBR4Yw7HP1","newRegistCount":0}}
        log.info(ret3.getResponse().toString());

        // 获取单日推送数据
        IQueryResult ret4 = client.queryAppPushDataByDate(AppConstant.APP_ID, "20171122") ;
        // {result=ok, data={"date":"20171122","showCount":0,"clickCount":0,"appId":"zS8CchJL2nAPGBR4Yw7HP1","receiveCount":0,"sendCount":0,"sendOnlineCount":0}}
        log.info(ret4.getResponse().toString());

        // 获取24H在线用户数
        IQueryResult ret5 = client.getLast24HoursOnlineUserStatistics(AppConstant.APP_ID) ;
        // {result=ok, onlineStatics={"1511343300012":1,"1511339100016":1,"1511332500011":1,"1511342700012":1}, appId=zS8CchJL2nAPGBR4Yw7HP1}
        log.info(ret5.getResponse().toString());

    }

}
