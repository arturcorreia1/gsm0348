//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.09.09 at 04:14:20 PM MSD 
//


package ru.tapublog.lib.gsm0348.api.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CertificationAlgorithmMode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CertificationAlgorithmMode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DES_CBC"/>
 *     &lt;enumeration value="TRIPLE_DES_CBC_2_KEYS"/>
 *     &lt;enumeration value="TRIPLE_DES_CBC_3_KEYS"/>
 *     &lt;enumeration value="RESERVED"/>
 *     &lt;enumeration value="AES_CMAC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CertificationAlgorithmMode")
@XmlEnum
public enum CertificationAlgorithmMode {

    DES_CBC,
    TRIPLE_DES_CBC_2_KEYS,
    TRIPLE_DES_CBC_3_KEYS,
    RESERVED,
    AES_CMAC;

    public String value() {
        return name();
    }

    public static CertificationAlgorithmMode fromValue(String v) {
        return valueOf(v);
    }

}