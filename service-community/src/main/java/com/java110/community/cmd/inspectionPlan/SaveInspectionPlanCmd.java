package com.java110.community.cmd.inspectionPlan;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.IInspectionPlanV1InnerServiceSMO;
import com.java110.po.inspection.InspectionPlanPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "inspectionPlan.saveInspectionPlan")
public class SaveInspectionPlanCmd extends Cmd {

    @Autowired
    private IInspectionPlanV1InnerServiceSMO inspectionPlanV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "inspectionPlanName", "必填，请填写巡检计划名称");
        Assert.hasKeyAndValue(reqJson, "inspectionRouteId", "必填，请填写巡检路线");
        Assert.hasKeyAndValue(reqJson, "inspectionPlanPeriod", "必填，请选择执行周期");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择计划开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择结束时间");
        Assert.hasKeyAndValue(reqJson, "signType", "必填，请填写签到方式");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写签到方式");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        JSONObject businessInspectionPlan = new JSONObject();
        businessInspectionPlan.putAll(reqJson);
        businessInspectionPlan.put("inspectionPlanId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_inspectionPlanId));
        InspectionPlanPo inspectionPlanPo = BeanConvertUtil.covertBean(businessInspectionPlan, InspectionPlanPo.class);
        int flag = inspectionPlanV1InnerServiceSMOImpl.saveInspectionPlan(inspectionPlanPo);
        if (flag < 1) {
            throw new CmdException("保存巡检计划失败");
        }
    }
}