package com.java110.user.bmo.rentingPoolAttr;
import com.java110.po.renting.RentingPoolAttrPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteRentingPoolAttrBMO {


    /**
     * 修改出租房屋属性
     * add by wuxw
     * @param rentingPoolAttrPo
     * @return
     */
    ResponseEntity<String> delete(RentingPoolAttrPo rentingPoolAttrPo);


}
