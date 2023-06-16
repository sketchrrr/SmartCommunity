package com.java110.user.bmo.activitiesBeautifulStaff.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.user.IActivitiesBeautifulStaffInnerServiceSMO;
import com.java110.po.activities.ActivitiesBeautifulStaffPo;
import com.java110.user.bmo.activitiesBeautifulStaff.IDeleteActivitiesBeautifulStaffBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteActivitiesBeautifulStaffBMOImpl")
public class DeleteActivitiesBeautifulStaffBMOImpl implements IDeleteActivitiesBeautifulStaffBMO {

    @Autowired
    private IActivitiesBeautifulStaffInnerServiceSMO activitiesBeautifulStaffInnerServiceSMOImpl;

    /**
     * @param activitiesBeautifulStaffPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ActivitiesBeautifulStaffPo activitiesBeautifulStaffPo) {

        int flag = activitiesBeautifulStaffInnerServiceSMOImpl.deleteActivitiesBeautifulStaff(activitiesBeautifulStaffPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
