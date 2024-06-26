package com.java110.fee.bmo.applyRoomDiscountType.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.applyRoomDiscountType.IUpdateApplyRoomDiscountTypeBMO;
import com.java110.intf.fee.IApplyRoomDiscountTypeInnerServiceSMO;
import com.java110.po.room.ApplyRoomDiscountTypePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateApplyRoomDiscountTypeBMOImpl")
public class UpdateApplyRoomDiscountTypeBMOImpl implements IUpdateApplyRoomDiscountTypeBMO {

    @Autowired
    private IApplyRoomDiscountTypeInnerServiceSMO applyRoomDiscountTypeInnerServiceSMOImpl;

    /**
     * @param applyRoomDiscountTypePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ApplyRoomDiscountTypePo applyRoomDiscountTypePo) {

        int flag = applyRoomDiscountTypeInnerServiceSMOImpl.updateApplyRoomDiscountType(applyRoomDiscountTypePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
