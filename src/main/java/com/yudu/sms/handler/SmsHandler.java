package com.yudu.sms.handler;

import com.yudu.sms.config.SmsConfig;
import com.yudu.sms.pojo.SmsIn;
import com.yudu.sms.pojo.SmsOut;
import com.yudu.sms.repository.SmsInRepository;
import gnu.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class SmsHandler implements SerialPortEventListener {
   private static final Logger logger = LoggerFactory.getLogger(SmsHandler.class);
   @Autowired
   private SmsConfig smsConfig;
   @Autowired
   private SmsInRepository smsInRepository;

   private ConcurrentLinkedQueue<SmsOut> smsQueue = new ConcurrentLinkedQueue<SmsOut>();
   private ConcurrentLinkedQueue<SmsIn> smsInQueue = new ConcurrentLinkedQueue<SmsIn>();

   private InputStream inputStream;
   private OutputStream outputStream;
   private SerialPort serialPort;

   private byte[] rcvHead = new byte[5]; // 包头 "+CMS:";
   private byte[] rcvTail = new byte[4]; // 包尾 "\r\n\0\0"
   private byte[] rcvSucess = new byte[15]; // "SMS_SEND_SUCESS"
   private byte[] rcvFail = new byte[13]; // "SMS_SEND_FAIL"

   private zlyInt iHead = new zlyInt(); // 查找包头
   private zlyInt iTail = new zlyInt(); // 查找包尾
   private zlyInt iSucess = new zlyInt(); // 查找是否发送成功
   private zlyInt iFail = new zlyInt(); // 查找是否发送失败

   private int bHeadFinded; // 接收使用
   private byte[] bufSms = new byte[500]; // 接收缓冲区
   private int ibufSms; // 接收缓冲区中，当前收到的字符数
   private SmsHandler() { }

   public void init() {
      initBag();
      String port = smsConfig.portName;
      registPort(port);
      new Thread(new SendSmsTask()).start();
      new Thread(new ReceiveSmsTask()).start();
   }

   private void initBag() {
      // 接收短信的协议头
      // "+CMS:";2B 43 4D 53 3A
      rcvHead[0] = 0x2B;
      rcvHead[1] = 0x43;
      rcvHead[2] = 0x4D;
      rcvHead[3] = 0x53;
      rcvHead[4] = 0x3A;
      // 接收短信的包尾
      // "\r\n\0\0" 0D 0A 00 00
      rcvTail[0] = 0x0D;
      rcvTail[1] = 0x0A;
      rcvTail[2] = 0x00;
      rcvTail[3] = 0x00;
      // 发生成功的应答信息
      // "SMS_SEND_SUCESS" 53 4D 53 5F 53 45 4E 44 5F 53 55 43 45 53 53
      rcvSucess[0] = 0x53;
      rcvSucess[1] = 0x4D;
      rcvSucess[2] = 0x53;
      rcvSucess[3] = 0x5F;
      rcvSucess[4] = 0x53;
      rcvSucess[5] = 0x45;
      rcvSucess[6] = 0x4E;
      rcvSucess[7] = 0x44;
      rcvSucess[8] = 0x5F;
      rcvSucess[9] = 0x53;
      rcvSucess[10] = 0x55;
      rcvSucess[11] = 0x43;
      rcvSucess[12] = 0x45;
      rcvSucess[13] = 0x53;
      rcvSucess[14] = 0x53;
      // 发送失败的应答信息
      // "SMS_SEND_FAIL" 53 4D 53 5F 53 45 4E 44 5F 46 41 49 4C
      // byte[] rcvFail = new byte[13];
      rcvFail[0] = 0x53;
      rcvFail[1] = 0x4D;
      rcvFail[2] = 0x53;
      rcvFail[3] = 0x5F;
      rcvFail[4] = 0x53;
      rcvFail[5] = 0x45;
      rcvFail[6] = 0x4E;
      rcvFail[7] = 0x44;
      rcvFail[8] = 0x5F;
      rcvFail[9] = 0x46;
      rcvFail[10] = 0x41;
      rcvFail[11] = 0x49;
      rcvFail[12] = 0x4C;
   }

   private void registPort(String port) {
      Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers();// 获取所有的端口
      while (portList.hasMoreElements()) {
         CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
         if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            if (portId.getName().toUpperCase().equals(port.toUpperCase())) {
               try {
                  serialPort = (SerialPort) portId.open("sunder", 2000);
                  logger.info("打开端口："+port);
               } catch (PortInUseException e) {
                  logger.error("端口已经被占用...");
                  throw new RuntimeException("端口已经被占用...");
               }

               try {
                  inputStream = serialPort.getInputStream();
                  outputStream = serialPort.getOutputStream();
               } catch (IOException e) {
                  logger.error("获取端口的输入输出流出错...");
                  throw new RuntimeException("获取端口的输入输出流出错...");
               }

               // 设置监听
               try {
                  serialPort.addEventListener((SerialPortEventListener) this);
               } catch (TooManyListenersException e) {
                  throw new RuntimeException(e);
               }

               // 开启数据达到通知
               serialPort.notifyOnDataAvailable(true);

               try {
                  serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
               } catch (UnsupportedCommOperationException e) {
                  logger.error("打开端口失败");
                  throw new RuntimeException("打开端口失败");
               }
            }
         }
      }
   }

   public void sendSms(String smscontent, String rNum, String sPerson, String sPersonId, int type) throws Exception {
      SmsOut smsOut = new SmsOut();
      smsOut.setContext(smscontent);
      smsOut.setDate(new Date());
      smsOut.setIsVisible(true);
      smsOut.setType(type);
      smsOut.setIsSuccess(1);
      smsOut.setrNum(rNum);
      smsOut.setrPerson(sPerson);
      smsOut.setrPersonId(sPersonId);
      smsQueue.add(smsOut); // 加入队列即可

  }
   class ReceiveSmsTask implements Runnable{
       @Override
       public void run() {
           while (true) {
               SmsIn smsIn = smsInQueue.poll();
               while (smsIn != null) {
                   logger.info("接收：号码："+smsIn.getsNum()+"-内容:"+smsIn.getContext());
                   smsInRepository.insert(smsIn);
                   try {
                       Thread.sleep(1000 * 5);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   smsIn = smsInQueue.poll();
               }
               try {
                   Thread.sleep(1000 * 60 * 2);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       }
   }
    class SendSmsTask implements Runnable {
      @Override
      public void run() {
    	  while (true) {
            SmsOut smsOut = smsQueue.poll();
            while (smsOut != null) {
               String sms = smsOut.getrNum() + ":0:" + smsOut.getContext();
               try {
                  outputStream.write(sms.getBytes("GBK"), 0, sms.getBytes("GBK").length);
                  outputStream.flush();
                  logger.info("短信发送成功：" + sms);
               } catch (Exception e) {
                  e.printStackTrace();
                  if (logger.isErrorEnabled()) {
                     logger.error("短信发送失败：" + sms);
                  }
               }
               try {
                  Thread.sleep(1000 * 5);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
               smsOut = smsQueue.poll();
            }
            try {
               Thread.sleep(1000 * 60 * 2);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
      }
   }

   @Override
   public void serialEvent(SerialPortEvent event)  {
      switch (event.getEventType()) {
      case SerialPortEvent.BI:
      case SerialPortEvent.OE:
      case SerialPortEvent.FE:
      case SerialPortEvent.PE:
      case SerialPortEvent.CD:
      case SerialPortEvent.CTS:
      case SerialPortEvent.DSR:
      case SerialPortEvent.RI:
      case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
         break;
      case SerialPortEvent.DATA_AVAILABLE: // 接收到数据
         byte[] readBuffer = new byte[200];
         int numBytes = 0;
         int j;
         String s;
         try {
            while (inputStream.available() > 0) {
               numBytes = inputStream.read(readBuffer);
               for (j = 0; j < numBytes; j++) {
                  if (1 == SearchStrInStream2(rcvSucess, readBuffer[j], iSucess)) {
                	  logger.info("发送成功");
                  }
                  if (1 == SearchStrInStream2(rcvFail, readBuffer[j], iFail)) {
                      logger.info("发送失败");
                  }
                  if (bHeadFinded == 1) {// 接收
                     bufSms[ibufSms] = readBuffer[j];
                     ibufSms++;
                     if (1 == SearchStrInStream2(rcvTail, readBuffer[j], iTail)) {
                        bHeadFinded = 0;
                        s = new String(bufSms, 0, ibufSms, "gbk");
                        bufSms = new byte[500];
                        SmsIn smsIn = new SmsIn();
                        smsIn.setId(UUID.randomUUID().toString().replace("-",""));
                        smsIn.setContext(s.substring(26));
                        smsIn.setsNum(s.substring(2, 13));
                        smsIn.setDate(new Date());
                        smsIn.setIsVisible(false);
                        smsIn.setIsRead(false);
                        smsIn.setsPerson("无");
                        smsIn.setsPersonId("null");
                        smsInQueue.add(smsIn); // 加入队列即可
                     }
                  }

                  if (1 == SearchStrInStream2(rcvHead, readBuffer[j], iHead)) {
                     bHeadFinded = 1;
                     ibufSms = 0;
                  }
               }
            }
         } catch (IOException e) {
            System.out.println(e);
         }
         break;
      }
   }

   // 查询字符流的函数
   public int SearchStrInStream2(byte[] strToCmp, byte c, zlyInt i) {
      if (i.i > strToCmp.length) {
         i.i = 0;
         return 0;
      }
      if (i.i != 0) {
         if (c != strToCmp[i.i]) {
            i.i = 0;
         } else {
         }
      }
      if (c == strToCmp[i.i]) {
         (i.i)++;
      }
      if ((int) (i.i) == strToCmp.length) {
         i.i = 0;
         return 1;
      }
      return 0;
   }

   public class zlyInt // 查询字符流的函数需要获取输出参数的办法
   {
      int i = 0;
   }

}
