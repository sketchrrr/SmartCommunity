package com.java110.store.bmo.contractTypeSpec;

import com.java110.po.contract.ContractTypeSpecPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteContractTypeSpecBMO {


    /**
     * 修改合同类型规格
     * add by wuxw
     *
     * @param contractTypeSpecPo
     * @return
     */
    ResponseEntity<String> delete(ContractTypeSpecPo contractTypeSpecPo);


}
