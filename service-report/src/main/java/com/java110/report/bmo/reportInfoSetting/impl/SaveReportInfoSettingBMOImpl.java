package com.java110.report.bmo.reportInfoSetting.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.report.IReportInfoSettingInnerServiceSMO;
import com.java110.po.reportInfo.ReportInfoSettingPo;
import com.java110.report.bmo.reportInfoSetting.ISaveReportInfoSettingBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveReportInfoSettingBMOImpl")
public class SaveReportInfoSettingBMOImpl implements ISaveReportInfoSettingBMO {

    @Autowired
    private IReportInfoSettingInnerServiceSMO reportInfoSettingInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param reportInfoSettingPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ReportInfoSettingPo reportInfoSettingPo) {

        reportInfoSettingPo.setSettingId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_settingId));
        int flag = reportInfoSettingInnerServiceSMOImpl.saveReportInfoSetting(reportInfoSettingPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
