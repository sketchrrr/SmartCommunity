package com.java110.store.bmo.contractAttr;

import com.java110.po.contract.ContractAttrPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateContractAttrBMO {


    /**
     * 修改合同属性
     * add by wuxw
     *
     * @param contractAttrPo
     * @return
     */
    ResponseEntity<String> update(ContractAttrPo contractAttrPo);


}
