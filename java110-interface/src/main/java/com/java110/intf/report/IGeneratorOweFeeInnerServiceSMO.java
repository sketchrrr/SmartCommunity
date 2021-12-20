package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.po.reportFeeMonthStatistics.ReportFeeMonthStatisticsPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName IGeneratorFeeMonthStatisticsInnerServiceSMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/10/15 21:51
 * @Version 1.0
 * add by wuxw 2020/10/15
 **/
@FeignClient(name = "${java110.report-service}", configuration = {FeignConfiguration.class})
@RequestMapping("/generatorOweFeeInnerServiceSMOApi")
public interface IGeneratorOweFeeInnerServiceSMO {



    @RequestMapping(value = "/generatorOweData", method = RequestMethod.POST)
    public int generatorOweData(@RequestBody ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo);

}
