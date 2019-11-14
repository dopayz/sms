
package com.yudu.sms.webservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>mtMessageRes complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="mtMessageRes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resDetail" type="{http://sms.jwsserver.server.ema.ctc.com/}mtMessageResDetail" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="smsId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subStat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subStatDes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mtMessageRes", propOrder = {
    "resDetail",
    "smsId",
    "subStat",
    "subStatDes"
})
public class MtMessageRes {

    @XmlElement(nillable = true)
    protected List<MtMessageResDetail> resDetail;
    protected String smsId;
    protected String subStat;
    protected String subStatDes;

    /**
     * Gets the value of the resDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MtMessageResDetail }
     * 
     * 
     */
    public List<MtMessageResDetail> getResDetail() {
        if (resDetail == null) {
            resDetail = new ArrayList<MtMessageResDetail>();
        }
        return this.resDetail;
    }

    /**
     * ��ȡsmsId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmsId() {
        return smsId;
    }

    /**
     * ����smsId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmsId(String value) {
        this.smsId = value;
    }

    /**
     * ��ȡsubStat���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubStat() {
        return subStat;
    }

    /**
     * ����subStat���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubStat(String value) {
        this.subStat = value;
    }

    /**
     * ��ȡsubStatDes���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubStatDes() {
        return subStatDes;
    }

    /**
     * ����subStatDes���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubStatDes(String value) {
        this.subStatDes = value;
    }

}
