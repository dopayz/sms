# 短信猫串口通信实战

## 1.环境搭建

​    1.1 .RXTXcomm.jar 串口通信包，放到JAVA_HOME/jre/lib/ext 

​    1.2.rxtxSerial.dll 实现串口操作,有32位和64位，放到JAVA_HOME/bin

## 2.项目说明

​	2.1.本项目还集成了企通信webservice接口短信通信模块，短信猫串口通信与企通信的切换通过application.properties中isSmsCat与isEMA变量控制。

​	2.2.短信猫的收发短信与业务隔离，通过数据库表字段标示做切割。该程序定时从收发表中查询数据，通过字段标示位做发送短信，收发短信各开一个线程独自运行。

​	3.3.短信猫设备：JYC311A6

## 3.表结构（sqlserver）

​	3.1.发送表：SMSOUT_

```mssql
`CREATE TABLE [dbo].[SMSOUT_](
    [id] [varchar](255) NOT NULL,
    [rNum] [varchar](255) NULL,
    [rPerson] [varchar](255) NULL,
    [rPersonId] [varchar](255) NULL,
    [context] [varchar](255) NULL,
    [date] [datetime] NULL,
    [isSuccess] [int] NULL,
    [type] [int] NULL,
    [isVisible] [tinyint] NULL
)`
```
​	3.2.接收表：SMSIN_

```mssql
`CREATE TABLE [dbo].[SMSIN_](
    [id] [varchar](255) NOT NULL,
    [sNum] [varchar](255) NULL,
    [sPerson] [varchar](255) NULL,
    [sPersonId] [varchar](255) NULL,
    [context] [varchar](255) NULL,
    [date] [datetime] NULL,
    [isVisible] [tinyint] NULL,
    [isRead] [tinyint] NULL
)`
```
​	
## 4.资源下载
    https://share.weiyun.com/5ND78bz

