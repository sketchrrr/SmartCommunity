package com.java110.dto.floor;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 考勤班组属性数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FloorAttrDto extends PageDto implements Serializable {

    private String floorId;
private String attrId;
private String specCd;
private String communityId;
private String value;
private String specName;
private String valueName;
private String listShow;


    private Date createTime;

    private String statusCd = "0";


    public String getFloorId() {
        return floorId;
    }
public void setFloorId(String floorId) {
        this.floorId = floorId;
    }
public String getAttrId() {
        return attrId;
    }
public void setAttrId(String attrId) {
        this.attrId = attrId;
    }
public String getSpecCd() {
        return specCd;
    }
public void setSpecCd(String specCd) {
        this.specCd = specCd;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getValue() {
        return value;
    }
public void setValue(String value) {
        this.value = value;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getListShow() {
        return listShow;
    }

    public void setListShow(String listShow) {
        this.listShow = listShow;
    }
}
