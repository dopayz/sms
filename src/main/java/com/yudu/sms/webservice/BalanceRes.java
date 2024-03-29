
package com.yudu.sms.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>balanceRes complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="balanceRes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="demo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="revStat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="revStatDes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "balanceRes", propOrder = {
    "demo",
    "revStat",
    "revStatDes"
})
public class BalanceRes {

    protected String demo;
    protected String revStat;
    protected String revStatDes;

    /**
     * 获取demo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDemo() {
        return demo;
    }

    /**
     * 设置demo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDemo(String value) {
        this.demo = value;
    }

    /**
     * 获取revStat属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRevStat() {
        return revStat;
    }

    /**
     * 设置revStat属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRevStat(String value) {
        this.revStat = value;
    }

    /**
     * 获取revStatDes属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRevStatDes() {
        return revStatDes;
    }

    /**
     * 设置revStatDes属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRevStatDes(String value) {
        this.revStatDes = value;
    }

}
