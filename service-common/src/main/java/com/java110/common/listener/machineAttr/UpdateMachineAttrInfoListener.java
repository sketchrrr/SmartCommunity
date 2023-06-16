package com.java110.common.listener.machineAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineAttrServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.system.AppBusiness;
import com.java110.po.machine.MachineAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改设备属性信息 侦听
 * <p>
 * 处理节点
 * 1、businessMachineAttr:{} 设备属性基本信息节点
 * 2、businessMachineAttrAttr:[{}] 设备属性属性信息节点
 * 3、businessMachineAttrPhoto:[{}] 设备属性照片信息节点
 * 4、businessMachineAttrCerdentials:[{}] 设备属性证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateMachineAttrInfoListener")
@Transactional
public class UpdateMachineAttrInfoListener extends AbstractMachineAttrBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateMachineAttrInfoListener.class);
    @Autowired
    private IMachineAttrServiceDao machineAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_MACHINE;
    }

    /**
     * business过程
     *
     * @param dataFlowContext 上下文对象
     * @param business        业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, AppBusiness business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");


        //处理 businessMachineAttr 节点
        if (data.containsKey(MachineAttrPo.class.getSimpleName())) {
            Object _obj = data.get(MachineAttrPo.class.getSimpleName());
            JSONArray businessMachineAttrs = null;
            if (_obj instanceof JSONObject) {
                businessMachineAttrs = new JSONArray();
                businessMachineAttrs.add(_obj);
            } else {
                businessMachineAttrs = (JSONArray) _obj;
            }
            //JSONObject businessMachineAttr = data.getJSONObject("businessMachineAttr");
            for (int _machineAttrIndex = 0; _machineAttrIndex < businessMachineAttrs.size(); _machineAttrIndex++) {
                JSONObject businessMachineAttr = businessMachineAttrs.getJSONObject(_machineAttrIndex);
                doBusinessMachineAttr(business, businessMachineAttr);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessMachineAttr.getString("attrId"));
                }
            }

        }
    }


    /**
     * business to instance 过程
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, AppBusiness business) {

        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_ADD);

        //设备属性信息
        List<Map> businessMachineAttrInfos = machineAttrServiceDaoImpl.getBusinessMachineAttrInfo(info);
        if (businessMachineAttrInfos != null && businessMachineAttrInfos.size() > 0) {
            for (int _machineAttrIndex = 0; _machineAttrIndex < businessMachineAttrInfos.size(); _machineAttrIndex++) {
                Map businessMachineAttrInfo = businessMachineAttrInfos.get(_machineAttrIndex);
                flushBusinessMachineAttrInfo(businessMachineAttrInfo, StatusConstant.STATUS_CD_VALID);
                machineAttrServiceDaoImpl.updateMachineAttrInfoInstance(businessMachineAttrInfo);
                if (businessMachineAttrInfo.size() == 1) {
                    dataFlowContext.addParamOut("attrId", businessMachineAttrInfo.get("attr_id"));
                }
            }
        }

    }

    /**
     * 撤单
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, AppBusiness business) {

        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        //设备属性信息
        List<Map> machineAttrInfo = machineAttrServiceDaoImpl.getMachineAttrInfo(info);
        if (machineAttrInfo != null && machineAttrInfo.size() > 0) {

            //设备属性信息
            List<Map> businessMachineAttrInfos = machineAttrServiceDaoImpl.getBusinessMachineAttrInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessMachineAttrInfos == null || businessMachineAttrInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（machineAttr），程序内部异常,请检查！ " + delInfo);
            }
            for (int _machineAttrIndex = 0; _machineAttrIndex < businessMachineAttrInfos.size(); _machineAttrIndex++) {
                Map businessMachineAttrInfo = businessMachineAttrInfos.get(_machineAttrIndex);
                flushBusinessMachineAttrInfo(businessMachineAttrInfo, StatusConstant.STATUS_CD_VALID);
                machineAttrServiceDaoImpl.updateMachineAttrInfoInstance(businessMachineAttrInfo);
            }
        }

    }


    /**
     * 处理 businessMachineAttr 节点
     *
     * @param business            总的数据节点
     * @param businessMachineAttr 设备属性节点
     */
    private void doBusinessMachineAttr(AppBusiness business, JSONObject businessMachineAttr) {

        Assert.jsonObjectHaveKey(businessMachineAttr, "attrId", "businessMachineAttr 节点下没有包含 attrId 节点");

        if (businessMachineAttr.getString("attrId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "attrId 错误，不能自动生成（必须已经存在的attrId）" + businessMachineAttr);
        }
        //自动保存DEL
        autoSaveDelBusinessMachineAttr(business, businessMachineAttr);

        businessMachineAttr.put("bId", business.getbId());
        businessMachineAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存设备属性信息
        machineAttrServiceDaoImpl.saveBusinessMachineAttrInfo(businessMachineAttr);

    }


    public IMachineAttrServiceDao getMachineAttrServiceDaoImpl() {
        return machineAttrServiceDaoImpl;
    }

    public void setMachineAttrServiceDaoImpl(IMachineAttrServiceDao machineAttrServiceDaoImpl) {
        this.machineAttrServiceDaoImpl = machineAttrServiceDaoImpl;
    }


}
