package com.java110.api.smo.applicationKey.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.dto.system.ComponentValidateResult;
import com.java110.api.smo.applicationKey.IAddApplicationKeySMO;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("addApplicationKeySMOImpl")
public class AddApplicationKeySMOImpl extends DefaultAbstractComponentSMO implements IAddApplicationKeySMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyAndValue(paramIn, "name", "必填，请填写姓名");
        Assert.hasKeyAndValue(paramIn, "communityId", "必填，请填写小区ID");
        Assert.hasKeyAndValue(paramIn, "tel", "必填，请填写手机号");
        Assert.hasKeyAndValue(paramIn, "typeCd", "必填，请选择用户类型");
        Assert.hasKeyAndValue(paramIn, "sex", "必填，请选择性别");
        Assert.hasKeyAndValue(paramIn, "age", "必填，请填写年龄");
        Assert.hasKeyAndValue(paramIn, "idCard", "必填，请填写身份证号");
        Assert.hasKeyAndValue(paramIn, "startTime", "必填，请选择开始时间");
        Assert.hasKeyAndValue(paramIn, "endTime", "必填，请选择结束时间");
        Assert.hasKeyAndValue(paramIn, "locationTypeCd", "必填，位置不能为空");
        Assert.hasKeyAndValue(paramIn, "locationObjId", "必填，未选择位置对象");
        Assert.hasKeyAndValue(paramIn, "typeFlag", "必填，未选择钥匙类型");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.LIST_APPLICATION_KEY);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        paramIn.put("storeId", result.getStoreId());
        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "applicationKey.saveApplicationKey",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveApplicationKey(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
