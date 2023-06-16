package com.java110.fee.bmo.feeReceipt;

import com.java110.po.fee.FeeReceiptPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteFeeReceiptBMO {


    /**
     * 修改收据
     * add by wuxw
     *
     * @param feeReceiptPo
     * @return
     */
    ResponseEntity<String> delete(FeeReceiptPo feeReceiptPo);


}
