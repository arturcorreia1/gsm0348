//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.09.09 at 04:14:20 PM MSD 
//

package ru.tapublog.lib.gsm0348.api.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "PoRProtocol")
@XmlEnum
public enum PoRProtocol {

    SMS_DELIVER_REPORT,
    SMS_SUBMIT;

    public String value() {
        return name();
    }

    public static PoRProtocol fromValue(String v) {
        return valueOf(v);
    }

}
