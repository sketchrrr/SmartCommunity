/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.common.cmd.attendanceClasses;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IPhotoSMO;
import com.java110.dto.attendance.AttendanceClassesStaffDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IAttendanceClassesStaffV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.attendance.AttendanceClassesStaffPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：attendanceClassesStaff.saveAttendanceClassesStaff
 * 请求路劲：/app/attendanceClassesStaff.SaveAttendanceClassesStaff
 * add by 吴学文 at 2023-02-21 23:48:34 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "attendanceClasses.saveAttendanceClassesStaff")
public class SaveAttendanceClassesStaffCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveAttendanceClassesStaffCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IAttendanceClassesStaffV1InnerServiceSMO attendanceClassesStaffV1InnerServiceSMOImpl;

    @Autowired
    private IPhotoSMO photoSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "classesId", "请求报文中未包含classesId");
        Assert.hasKeyAndValue(reqJson, "staffId", "请求报文中未包含staffId");
        Assert.hasKeyAndValue(reqJson, "photo", "请求报文中未包含人脸");

        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");
        Assert.hasLength(storeId, "未包含商户信息");

        AttendanceClassesStaffDto attendanceClassesStaffDto = new AttendanceClassesStaffDto();
        attendanceClassesStaffDto.setClassesId(reqJson.getString("classesId"));
        attendanceClassesStaffDto.setStaffId(reqJson.getString("staffId"));
        int count = attendanceClassesStaffV1InnerServiceSMOImpl.queryAttendanceClassesStaffsCount(attendanceClassesStaffDto);

        if (count > 0) {
            throw new CmdException("考勤员工已经存在");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");

        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("staffId"));
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "员工不存在");

        AttendanceClassesStaffPo attendanceClassesStaffPo = BeanConvertUtil.covertBean(reqJson, AttendanceClassesStaffPo.class);
        attendanceClassesStaffPo.setStoreId(storeId);
        attendanceClassesStaffPo.setStaffName(userDtos.get(0).getName());
        attendanceClassesStaffPo.setCsId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = attendanceClassesStaffV1InnerServiceSMOImpl.saveAttendanceClassesStaff(attendanceClassesStaffPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        photoSMOImpl.savePhoto(reqJson, attendanceClassesStaffPo.getCsId(), reqJson.getString("communityId"));

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
