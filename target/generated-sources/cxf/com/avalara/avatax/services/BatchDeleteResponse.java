
package com.avalara.avatax.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BatchDeleteResult" type="{http://avatax.avalara.com/services}DeleteResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "batchDeleteResult"
})
@XmlRootElement(name = "BatchDeleteResponse")
public class BatchDeleteResponse {

    @XmlElement(name = "BatchDeleteResult")
    protected DeleteResult batchDeleteResult;

    /**
     * Gets the value of the batchDeleteResult property.
     * 
     * @return
     *     possible object is
     *     {@link DeleteResult }
     *     
     */
    public DeleteResult getBatchDeleteResult() {
        return batchDeleteResult;
    }

    /**
     * Sets the value of the batchDeleteResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeleteResult }
     *     
     */
    public void setBatchDeleteResult(DeleteResult value) {
        this.batchDeleteResult = value;
    }

}
