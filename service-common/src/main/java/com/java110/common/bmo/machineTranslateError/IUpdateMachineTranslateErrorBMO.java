package com.java110.common.bmo.machineTranslateError;

import com.java110.po.machine.MachineTranslateErrorPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateMachineTranslateErrorBMO {


    /**
     * 修改IOT同步错误日志记录
     * add by wuxw
     *
     * @param machineTranslateErrorPo
     * @return
     */
    ResponseEntity<String> update(MachineTranslateErrorPo machineTranslateErrorPo);


}
