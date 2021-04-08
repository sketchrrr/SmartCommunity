package com.java110.api.listener.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.allocationStorehouse.AllocationStorehouseDto;
import com.java110.entity.audit.AuditUser;
import com.java110.intf.common.IAllocationStorehouseUserInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeAuditUserConstant;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询待审核调拨单
 */
@Java110Listener("listAllocationStoreAuditHistoryOrdersListener")
public class ListAllocationStoreHisAuditOrders extends AbstractServiceApiListener {

    @Autowired
    private IAllocationStorehouseUserInnerServiceSMO allocationStorehouseUserInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeAuditUserConstant.LIST_ALLOCATION_STORE_HISTORY_AUDITORDERS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写用户ID");
        Assert.hasKeyAndValue(reqJson, "row", "必填，请填写每页显示数");
        Assert.hasKeyAndValue(reqJson, "page", "必填，请填写页数");

        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AuditUser auditUser = new AuditUser();
        auditUser.setUserId(reqJson.getString("userId"));
        auditUser.setPage(reqJson.getInteger("page"));
        auditUser.setRow(reqJson.getInteger("row"));
        auditUser.setStoreId(reqJson.getString("storeId"));

        long count = allocationStorehouseUserInnerServiceSMOImpl.getUserHistoryTaskCount(auditUser);

        List<AllocationStorehouseDto> purchaseApplyDtos = null;

        if (count > 0) {
            purchaseApplyDtos = allocationStorehouseUserInnerServiceSMOImpl.getUserHistoryTasks(auditUser);
        } else {
            purchaseApplyDtos = new ArrayList<>();
        }

        ResponseEntity responseEntity
                = ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")),
                (int) count,
                purchaseApplyDtos);
        context.setResponseEntity(responseEntity);
    }
}
