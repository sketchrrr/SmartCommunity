package com.java110.dto;

/**
 * Created by wuxw on 2017/9/15.
 */
public class DefaultBoAttrEntity extends DefaultBoEntity {

    private String attrCd; //属性编码

    private String value; //属性值

    public String getAttrCd() {
        return attrCd;
    }

    public void setAttrCd(String attrCd) {
        this.attrCd = attrCd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
