package com.java110.report.bmo.reportInfoBackCity;
import com.java110.po.reportInfo.ReportInfoBackCityPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteReportInfoBackCityBMO {


    /**
     * 修改返省上报
     * add by wuxw
     * @param reportInfoBackCityPo
     * @return
     */
    ResponseEntity<String> delete(ReportInfoBackCityPo reportInfoBackCityPo);


}
