package com.yudu.sms;
import com.yudu.sms.util.MD5;
import com.yudu.sms.webservice.ISmsOperator;
import com.yudu.sms.webservice.MtMessage;
import com.yudu.sms.webservice.MtMessageRes;
import com.yudu.sms.webservice.SmsOperatorImpService;
import java.util.ArrayList;
import java.util.List;

public class WebServiceCS {
    public static void main(String[] args) {
        String userName="yudu_sms01";
        String password="yudu_sms_PWD!@#";
        password = MD5.GetMD5Code(password);
        MtMessage mtMessage = new MtMessage();
        List<String> phoneNumber = new ArrayList<>();
        phoneNumber.add("19923295262");
        mtMessage.setPhoneNumber(phoneNumber);
        mtMessage.setContent("webservice短信接口测试1105");
        SmsOperatorImpService smsOperatorImpService =  new SmsOperatorImpService();
        ISmsOperator sms = smsOperatorImpService.getSmsOperatorImpPort();
        MtMessageRes mtMessageRes = sms.sendSms(userName,password,"",mtMessage);
        if(mtMessageRes!=null){
            if(mtMessageRes.getSubStat().equals("r:000")){
                // TODO
            }
        }
    }
}
