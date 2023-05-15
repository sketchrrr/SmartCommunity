package com.java110.report.statistics.impl;

import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.report.statistics.IFeeStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 基础报表统计 实现类
 */
@Service
public class FeeStatisticsImpl implements IFeeStatistics {

    @Autowired
    private IReportFeeStatisticsInnerServiceSMO reportFeeStatisticsInnerServiceSMOImpl;

    /**
     * 查询 历史欠费
     *
     * @param queryFeeStatisticsDto
     * @return
     */
    @Override
    public double getHisMonthOweFee(QueryStatisticsDto queryFeeStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getHisMonthOweFee(queryFeeStatisticsDto);
    }

    /**
     * 查询 当月欠费
     * @param queryFeeStatisticsDto
     * @return
     */
    @Override
    public double getCurMonthOweFee(QueryStatisticsDto queryFeeStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getCurMonthOweFee(queryFeeStatisticsDto);
    }


    /**
     * 查询 欠费追回
     * @param queryFeeStatisticsDto
     * @return
     */
    @Override
    public double getHisReceivedFee(QueryStatisticsDto queryFeeStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getHisReceivedFee(queryFeeStatisticsDto);
    }

    /**
     * 查询 预交费用
     * @param queryFeeStatisticsDto
     * @return
     */
    @Override
    public double getPreReceivedFee(QueryStatisticsDto queryFeeStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getPreReceivedFee(queryFeeStatisticsDto);
    }

    /**
     * 查询 实收费用
     * @param queryFeeStatisticsDto
     * @return
     */
    @Override
    public double getReceivedFee(QueryStatisticsDto queryFeeStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getReceivedFee(queryFeeStatisticsDto);
    }

    @Override
    public int getOweRoomCount(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getOweRoomCount(queryStatisticsDto);
    }
}
