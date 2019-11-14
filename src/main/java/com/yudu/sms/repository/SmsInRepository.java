package com.yudu.sms.repository;

import com.yudu.sms.pojo.SmsIn;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SmsInRepository {
    @Insert("insert into SMSIN_(id,sNum,sPerson,sPersonId,context,date,isVisible,isRead) VALUES(#{id},#{sNum},#{sPerson},#{sPersonId},#{context},#{date},#{isVisible},#{isRead})")
    void insert(SmsIn smsIn);

    //update
    //page-search
    //delete-by-id
    //search-by-id
    //
    //delete-by-num
}
