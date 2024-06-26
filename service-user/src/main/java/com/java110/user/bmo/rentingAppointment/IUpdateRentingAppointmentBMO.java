package com.java110.user.bmo.rentingAppointment;
import com.java110.po.renting.RentingAppointmentPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateRentingAppointmentBMO {


    /**
     * 修改租赁预约
     * add by wuxw
     * @param rentingAppointmentPo
     * @return
     */
    ResponseEntity<String> update(RentingAppointmentPo rentingAppointmentPo);


}
