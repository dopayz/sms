package com.yudu.sms.pojo;

import java.io.Serializable;
import java.util.Date;

public class SmsIn implements Serializable {
   private static final long serialVersionUID = 1L;

   /** 主键 */
   private String id;

   /** 发送人号码 */
   private String sNum;

   /** 发送人姓名, 不在通讯录中则写无 */
   private String sPerson;

   /** 发送人ID，不在通讯录中则填null */
   private String sPersonId;

   private String context;//内容

   private Date date;//接收日趋

   private Boolean isVisible;//是否可见
   
   private Boolean isRead;//是否阅读
   
   public SmsIn() {
      super();
   }

   public SmsIn(String id, String sNum, String sPerson, String sPersonId, String context, Date date,
         Boolean isVisible, Boolean isRead) {
      super();
      this.id = id;
      this.sNum = sNum;
      this.sPerson = sPerson;
      this.sPersonId = sPersonId;
      this.context = context;
      this.date = date;
      this.isVisible = isVisible;
      this.isRead = isRead;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getsNum() {
      return sNum;
   }

   public void setsNum(String sNum) {
      this.sNum = sNum;
   }

   public String getsPerson() {
      return sPerson;
   }

   public void setsPerson(String sPerson) {
      this.sPerson = sPerson;
   }

   public String getsPersonId() {
      return sPersonId;
   }

   public void setsPersonId(String sPersonId) {
      this.sPersonId = sPersonId;
   }

   public String getContext() {
      return context;
   }

   public void setContext(String context) {
      this.context = context;
   }

   public Date getDate() {
      return date;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public Boolean getIsVisible() {
      return isVisible;
   }

   public void setIsVisible(Boolean isVisible) {
      this.isVisible = isVisible;
   }

	public Boolean getIsRead() {
		return isRead;
	}
	
	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

}