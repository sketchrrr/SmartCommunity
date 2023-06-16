package com.java110.store.listener.resourceStoreSpecification;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.resource.ResourceStoreSpecificationPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.system.AppBusiness;
import com.java110.store.dao.IResourceStoreSpecificationServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除物品规格信息 侦听
 * <p>
 * 处理节点
 * 1、businessResourceStoreSpecification:{} 物品规格基本信息节点
 * 2、businessResourceStoreSpecificationAttr:[{}] 物品规格属性信息节点
 * 3、businessResourceStoreSpecificationPhoto:[{}] 物品规格照片信息节点
 * 4、businessResourceStoreSpecificationCerdentials:[{}] 物品规格证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteResourceStoreSpecificationInfoListener")
@Transactional
public class DeleteResourceStoreSpecificationInfoListener extends AbstractResourceStoreSpecificationBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteResourceStoreSpecificationInfoListener.class);
    @Autowired
    IResourceStoreSpecificationServiceDao resourceStoreSpecificationServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_RESOURCE_STORE_SPECIFICATION;
    }

    /**
     * 根据删除信息 查出Instance表中数据 保存至business表 （状态写DEL） 方便撤单时直接更新回去
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, AppBusiness business) {
        JSONObject data = business.getDatas();

        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessResourceStoreSpecification 节点
        if (data.containsKey(ResourceStoreSpecificationPo.class.getSimpleName())) {
            Object _obj = data.get(ResourceStoreSpecificationPo.class.getSimpleName());
            JSONArray businessResourceStoreSpecifications = null;
            if (_obj instanceof JSONObject) {
                businessResourceStoreSpecifications = new JSONArray();
                businessResourceStoreSpecifications.add(_obj);
            } else {
                businessResourceStoreSpecifications = (JSONArray) _obj;
            }
            //JSONObject businessResourceStoreSpecification = data.getJSONObject(ResourceStoreSpecificationPo.class.getSimpleName());
            for (int _resourceResourceStoreSpecificationSpecificationIndex = 0; _resourceResourceStoreSpecificationSpecificationIndex < businessResourceStoreSpecifications.size(); _resourceResourceStoreSpecificationSpecificationIndex++) {
                JSONObject businessResourceStoreSpecification = businessResourceStoreSpecifications.getJSONObject(_resourceResourceStoreSpecificationSpecificationIndex);
                doBusinessResourceStoreSpecification(business, businessResourceStoreSpecification);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("rssId", businessResourceStoreSpecification.getString("rssId"));
                }
            }

        }


    }

    /**
     * 删除 instance数据
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, AppBusiness business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");

        //物品规格信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //物品规格信息
        List<Map> businessResourceStoreSpecificationInfos = resourceStoreSpecificationServiceDaoImpl.getBusinessResourceStoreSpecificationInfo(info);
        if (businessResourceStoreSpecificationInfos != null && businessResourceStoreSpecificationInfos.size() > 0) {
            for (int _resourceResourceStoreSpecificationSpecificationIndex = 0; _resourceResourceStoreSpecificationSpecificationIndex < businessResourceStoreSpecificationInfos.size(); _resourceResourceStoreSpecificationSpecificationIndex++) {
                Map businessResourceStoreSpecificationInfo = businessResourceStoreSpecificationInfos.get(_resourceResourceStoreSpecificationSpecificationIndex);
                flushBusinessResourceStoreSpecificationInfo(businessResourceStoreSpecificationInfo, StatusConstant.STATUS_CD_INVALID);
                resourceStoreSpecificationServiceDaoImpl.updateResourceStoreSpecificationInfoInstance(businessResourceStoreSpecificationInfo);
                dataFlowContext.addParamOut("rssId", businessResourceStoreSpecificationInfo.get("rss_id"));
            }
        }

    }

    /**
     * 撤单
     * 从business表中查询到DEL的数据 将instance中的数据更新回来
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
        info.put("statusCd", StatusConstant.STATUS_CD_INVALID);

        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        //物品规格信息
        List<Map> resourceResourceStoreSpecificationSpecificationInfo = resourceStoreSpecificationServiceDaoImpl.getResourceStoreSpecificationInfo(info);
        if (resourceResourceStoreSpecificationSpecificationInfo != null && resourceResourceStoreSpecificationSpecificationInfo.size() > 0) {

            //物品规格信息
            List<Map> businessResourceStoreSpecificationInfos = resourceStoreSpecificationServiceDaoImpl.getBusinessResourceStoreSpecificationInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessResourceStoreSpecificationInfos == null || businessResourceStoreSpecificationInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（resourceResourceStoreSpecificationSpecification），程序内部异常,请检查！ " + delInfo);
            }
            for (int _resourceResourceStoreSpecificationSpecificationIndex = 0; _resourceResourceStoreSpecificationSpecificationIndex < businessResourceStoreSpecificationInfos.size(); _resourceResourceStoreSpecificationSpecificationIndex++) {
                Map businessResourceStoreSpecificationInfo = businessResourceStoreSpecificationInfos.get(_resourceResourceStoreSpecificationSpecificationIndex);
                flushBusinessResourceStoreSpecificationInfo(businessResourceStoreSpecificationInfo, StatusConstant.STATUS_CD_VALID);
                resourceStoreSpecificationServiceDaoImpl.updateResourceStoreSpecificationInfoInstance(businessResourceStoreSpecificationInfo);
            }
        }
    }


    /**
     * 处理 businessResourceStoreSpecification 节点
     *
     * @param business                           总的数据节点
     * @param businessResourceStoreSpecification 物品规格节点
     */
    private void doBusinessResourceStoreSpecification(AppBusiness business, JSONObject businessResourceStoreSpecification) {

        Assert.jsonObjectHaveKey(businessResourceStoreSpecification, "rssId", "businessResourceStoreSpecification 节点下没有包含 rssId 节点");

        if (businessResourceStoreSpecification.getString("rssId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "rssId 错误，不能自动生成（必须已经存在的rssId）" + businessResourceStoreSpecification);
        }
        //自动插入DEL
        autoSaveDelBusinessResourceStoreSpecification(business, businessResourceStoreSpecification);
    }

    @Override
    public IResourceStoreSpecificationServiceDao getResourceStoreSpecificationServiceDaoImpl() {
        return resourceStoreSpecificationServiceDaoImpl;
    }

    public void setResourceStoreSpecificationServiceDaoImpl(IResourceStoreSpecificationServiceDao resourceStoreSpecificationServiceDaoImpl) {
        this.resourceStoreSpecificationServiceDaoImpl = resourceStoreSpecificationServiceDaoImpl;
    }
}
