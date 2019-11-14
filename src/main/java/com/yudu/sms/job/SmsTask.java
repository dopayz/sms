package com.yudu.sms.job;
import com.yudu.sms.config.EMAConfig;
import com.yudu.sms.config.SmsConfig;
import com.yudu.sms.handler.SmsHandler;
import com.yudu.sms.pojo.SmsOut;
import com.yudu.sms.repository.SmsOutRepository;
import com.yudu.sms.util.MD5;
import com.yudu.sms.webservice.ISmsOperator;
import com.yudu.sms.webservice.MtMessage;
import com.yudu.sms.webservice.MtMessageRes;
import com.yudu.sms.webservice.SmsOperatorImpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SmsTask {
    private static final Logger logger = LoggerFactory.getLogger(SmsTask.class);
    @Autowired
    private SmsHandler smsHandler;
    @Autowired
    private SmsOutRepository smsOutRepository;
    @Autowired
    private SmsConfig smsConfig;
    @Autowired
    private EMAConfig emaConfig;

    //标示是否注册短信猫端口,默认没有
    private static boolean overPort = false;
    //job
    public void sms(){
        //判断是企信通还是短信猫发送
        if(Boolean.parseBoolean(smsConfig.isSmsCat)) {
            //判断端口是否占用，再做初始化
            if (!overPort) {
                smsHandler.init();
                overPort = true;
            }
            List<SmsOut> smsOutList = smsOutRepository.getAllNoIsVisible();
            if (smsOutList.size() != 0 && smsOutList != null) {
                for (SmsOut smsOut : smsOutList) {
                    try {
                        smsHandler.sendSms(smsOut.getContext(), smsOut.getrNum(), smsOut.getrPerson(), smsOut.getrPersonId(), smsOut.getType());
                        smsOutRepository.update(smsOut.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(Boolean.parseBoolean(smsConfig.isEMA)){
            List<SmsOut> smsOutList = smsOutRepository.getAllNoIsVisible();
            if (smsOutList.size() != 0 && smsOutList != null) {
                for (SmsOut smsOut : smsOutList) {
                    try {
                        MtMessage mtMessage = new MtMessage();
                        List<String> phoneNumber = new ArrayList<>();
                        phoneNumber.add(smsOut.getrNum());
                        mtMessage.setPhoneNumber(phoneNumber);
                        mtMessage.setContent(smsOut.getContext());
                        SmsOperatorImpService smsOperatorImpService =  new SmsOperatorImpService();
                        ISmsOperator sms = smsOperatorImpService.getSmsOperatorImpPort();
                        MtMessageRes mtMessageRes = sms.sendSms(emaConfig.EMAuserName,MD5.GetMD5Code(emaConfig.EMApassword),"",mtMessage);
                        if(mtMessageRes!=null){
                            if(mtMessageRes.getSubStat().equals("r:000")){
                                smsOutRepository.update(smsOut.getId());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
