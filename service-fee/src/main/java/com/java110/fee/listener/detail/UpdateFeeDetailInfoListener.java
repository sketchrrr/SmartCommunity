package com.java110.fee.listener.detail;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.system.AppBusiness;
import com.java110.fee.dao.IFeeDetailServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改费用明细信息 侦听
 * <p>
 * 处理节点
 * 1、businessFeeDetail:{} 费用明细基本信息节点
 * 2、businessFeeDetailAttr:[{}] 费用明细属性信息节点
 * 3、businessFeeDetailPhoto:[{}] 费用明细照片信息节点
 * 4、businessFeeDetailCerdentials:[{}] 费用明细证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateFeeDetailInfoListener")
@Transactional
public class UpdateFeeDetailInfoListener extends AbstractFeeDetailBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateFeeDetailInfoListener.class);
    @Autowired
    private IFeeDetailServiceDao feeDetailServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FEE_DETAIL;
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

        //处理 businessFeeDetail 节点
        if (data.containsKey(PayFeeDetailPo.class.getSimpleName())) {
            Object _obj = data.get(PayFeeDetailPo.class.getSimpleName());
            JSONArray businessFeeDetails = null;
            if (_obj instanceof JSONObject) {
                businessFeeDetails = new JSONArray();
                businessFeeDetails.add(_obj);
            } else {
                businessFeeDetails = (JSONArray) _obj;
            }
            //JSONObject businessFeeDetail = data.getJSONObject("businessFeeDetail");
            for (int _feeDetailIndex = 0; _feeDetailIndex < businessFeeDetails.size(); _feeDetailIndex++) {
                JSONObject businessFeeDetail = businessFeeDetails.getJSONObject(_feeDetailIndex);
                doBusinessFeeDetail(business, businessFeeDetail);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("detailId", businessFeeDetail.getString("detailId"));
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

        //费用明细信息
        List<Map> businessFeeDetailInfos = feeDetailServiceDaoImpl.getBusinessFeeDetailInfo(info);
        if (businessFeeDetailInfos != null && businessFeeDetailInfos.size() > 0) {
            for (int _feeDetailIndex = 0; _feeDetailIndex < businessFeeDetailInfos.size(); _feeDetailIndex++) {
                Map businessFeeDetailInfo = businessFeeDetailInfos.get(_feeDetailIndex);
                flushBusinessFeeDetailInfo(businessFeeDetailInfo, StatusConstant.STATUS_CD_VALID);
                feeDetailServiceDaoImpl.updateFeeDetailInfoInstance(businessFeeDetailInfo);
                if (businessFeeDetailInfo.size() == 1) {
                    dataFlowContext.addParamOut("detailId", businessFeeDetailInfo.get("detail_id"));
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
        //费用明细信息
        List<Map> feeDetailInfo = feeDetailServiceDaoImpl.getFeeDetailInfo(info);
        if (feeDetailInfo != null && feeDetailInfo.size() > 0) {

            //费用明细信息
            List<Map> businessFeeDetailInfos = feeDetailServiceDaoImpl.getBusinessFeeDetailInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessFeeDetailInfos == null || businessFeeDetailInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（feeDetail），程序内部异常,请检查！ " + delInfo);
            }
            for (int _feeDetailIndex = 0; _feeDetailIndex < businessFeeDetailInfos.size(); _feeDetailIndex++) {
                Map businessFeeDetailInfo = businessFeeDetailInfos.get(_feeDetailIndex);
                flushBusinessFeeDetailInfo(businessFeeDetailInfo, StatusConstant.STATUS_CD_VALID);
                feeDetailServiceDaoImpl.updateFeeDetailInfoInstance(businessFeeDetailInfo);
            }
        }

    }


    /**
     * 处理 businessFeeDetail 节点
     *
     * @param business          总的数据节点
     * @param businessFeeDetail 费用明细节点
     */
    private void doBusinessFeeDetail(AppBusiness business, JSONObject businessFeeDetail) {

        Assert.jsonObjectHaveKey(businessFeeDetail, "detailId", "businessFeeDetail 节点下没有包含 detailId 节点");

        if (businessFeeDetail.getString("detailId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "detailId 错误，不能自动生成（必须已经存在的detailId）" + businessFeeDetail);
        }
        //自动保存DEL
        autoSaveDelBusinessFeeDetail(business, businessFeeDetail);

        businessFeeDetail.put("bId", business.getbId());
        businessFeeDetail.put("operate", StatusConstant.OPERATE_ADD);
        //保存费用明细信息
        feeDetailServiceDaoImpl.saveBusinessFeeDetailInfo(businessFeeDetail);

    }


    public IFeeDetailServiceDao getFeeDetailServiceDaoImpl() {
        return feeDetailServiceDaoImpl;
    }

    public void setFeeDetailServiceDaoImpl(IFeeDetailServiceDao feeDetailServiceDaoImpl) {
        this.feeDetailServiceDaoImpl = feeDetailServiceDaoImpl;
    }


}
