package com.java110.api.smo.auditUser.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.dto.system.ComponentValidateResult;
import com.java110.api.smo.auditUser.IListAuditAppUserBindingOwnersSMO;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 查询auditAppUserBindingOwner服务类
 */
@Service("listAuditAppUserBindingOwnersSMOImpl")
public class ListAuditAppUserBindingOwnersSMOImpl extends DefaultAbstractComponentSMO implements IListAuditAppUserBindingOwnersSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> listAuditAppUserBindingOwners(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "communityId", "未包含小区信息");

        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_AUDITAPPUSERBINDINGOWNER);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);
        paramIn.put("userId", "");

        String apiUrl = "owner.listAuditAppUserBindingOwners" + mapToUrlParam(paramIn);


        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        return responseEntity;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
