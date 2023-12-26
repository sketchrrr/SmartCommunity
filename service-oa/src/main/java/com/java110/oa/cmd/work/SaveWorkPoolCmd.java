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
package com.java110.oa.cmd.work;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.user.UserDto;
import com.java110.dto.workCopy.WorkCopyDto;
import com.java110.dto.workCycle.WorkCycleDto;
import com.java110.dto.workPool.WorkPoolDto;
import com.java110.dto.workPoolFile.WorkPoolFileDto;
import com.java110.dto.workTask.WorkTaskDto;
import com.java110.intf.oa.*;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.workCopy.WorkCopyPo;
import com.java110.po.workCycle.WorkCyclePo;
import com.java110.po.workPool.WorkPoolPo;
import com.java110.po.workPoolContent.WorkPoolContentPo;
import com.java110.po.workPoolFile.WorkPoolFilePo;
import com.java110.po.workTask.WorkTaskPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * 类表述：保存
 * 服务编码：workPool.saveWorkPool
 * 请求路劲：/app/workPool.SaveWorkPool
 * add by 吴学文 at 2023-12-25 15:31:02 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "work.saveWorkPool")
public class SaveWorkPoolCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveWorkPoolCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IWorkPoolV1InnerServiceSMO workPoolV1InnerServiceSMOImpl;

    @Autowired
    private IWorkPoolContentV1InnerServiceSMO workPoolContentV1InnerServiceSMOImpl;

    @Autowired
    private IWorkPoolFileV1InnerServiceSMO workPoolFileV1InnerServiceSMOImpl;

    @Autowired
    private IWorkCycleV1InnerServiceSMO workCycleV1InnerServiceSMOImpl;

    @Autowired
    private IWorkTaskV1InnerServiceSMO workTaskV1InnerServiceSMOImpl;

    @Autowired
    private IWorkCopyV1InnerServiceSMO workCopyV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    /**
     * {"workName":"关于扫雪任务","workTypes":[],"wtId":"102023122586210045",
     * "workCycle":"1001","startTime":"2023-12-26 14:20:17","endTime":"2023-12-27 14:20:17",
     * "staffs":[{"staffId":"302023071089700002","staffName":"ddysdd"}],"copyStaffs":[],"copyName":"",
     * "pathUrl":"","content":"","period":"",
     * "months":[],"days":[],"workdays":[],
     * "hours":"24","context":"<p>请大家初雪工作，积极完成</p>"}
     *
     * @param event              事件对象
     * @param cmdDataFlowContext 请求报文数据
     * @param reqJson
     */
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "wtId", "请求报文中未包含wtId");
        Assert.hasKeyAndValue(reqJson, "workName", "请求报文中未包含workName");
        Assert.hasKeyAndValue(reqJson, "workCycle", "请求报文中未包含workCycle");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        String storeId = CmdContextUtils.getStoreId(cmdDataFlowContext);
        reqJson.put("storeId", storeId);

        if (!reqJson.containsKey("staffs")) {
            throw new CmdException("未包含处理人");
        }

        JSONArray staffs = reqJson.getJSONArray("staffs");
        if (staffs == null || staffs.isEmpty()) {
            throw new CmdException("未包含处理人");
        }

        if (WorkPoolDto.WORK_CYCLE_ONE.equals(reqJson.getString("workCycle"))) {
            return;
        }
        Assert.hasKeyAndValue(reqJson, "period", "周期性工单未包含周期");
        Assert.hasKeyAndValue(reqJson, "hours", "周期性工单未包含完成小时");

        if (WorkCycleDto.PERIOD_MONTH_DAY.equals(reqJson.getString("period"))) {
            JSONArray months = reqJson.getJSONArray("months");
            JSONArray days = reqJson.getJSONArray("days");

            if (ListUtil.isNull(months) || ListUtil.isNull(days)) {
                throw new CmdException("未包含月/天");
            }
        }

        if (WorkCycleDto.PERIOD_MONTH_WORKDAY.equals(reqJson.getString("period"))) {
            JSONArray workdays = reqJson.getJSONArray("workdays");
            if (ListUtil.isNull(workdays)) {
                throw new CmdException("未包含按周");
            }
        }


    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");

        WorkPoolPo workPoolPo = BeanConvertUtil.covertBean(reqJson, WorkPoolPo.class);
        workPoolPo.setWorkId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        workPoolPo.setCreateUserId(userDtos.get(0).getUserId());
        workPoolPo.setCreateUserName(userDtos.get(0).getName());
        workPoolPo.setCreateUserTel(userDtos.get(0).getTel());
        workPoolPo.setState(WorkPoolDto.STATE_WAIT);
        int flag = workPoolV1InnerServiceSMOImpl.saveWorkPool(workPoolPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        //todo 保存 工作单内容
        saveContent(workPoolPo, reqJson, userDtos.get(0));

        //todo 保存 抄送人
        saveCopyStaff(workPoolPo, reqJson, userDtos.get(0));

        //todo 保存周期
        saveWorkCycle(workPoolPo, reqJson, userDtos.get(0));


        // todo 保存 工单任务
        saveWorkTask(workPoolPo, reqJson, userDtos.get(0));


        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    private void saveWorkTask(WorkPoolPo workPoolPo, JSONObject reqJson, UserDto userDto) {
        JSONArray staffs = reqJson.getJSONArray("staffs");
        String startTime = reqJson.getString("startTime");
        String endTime = reqJson.getString("endTime");

        if (WorkPoolDto.WORK_CYCLE_CYCLE.equals(workPoolPo.getWorkCycle())) {
            Date sTime = DateUtil.getDateFromStringA(startTime);
            endTime = DateUtil.getAddHoursStringA(sTime, reqJson.getIntValue("hours"));
        }


        for (int staffIndex = 0; staffIndex < staffs.size(); staffIndex++) {
            WorkTaskPo workTaskPo = new WorkTaskPo();
            workTaskPo.setWorkId(workPoolPo.getWorkId());
            workTaskPo.setState(WorkTaskDto.STATE_DOING);
            workTaskPo.setTaskId(GenerateCodeFactory.getGeneratorId("11"));
            workTaskPo.setStoreId(workPoolPo.getStoreId());
            workTaskPo.setCommunityId(workPoolPo.getCommunityId());
            workTaskPo.setStartTime(startTime);
            workTaskPo.setEndTime(endTime);
            workTaskPo.setStaffId(staffs.getJSONObject(staffIndex).getString("staffId"));
            workTaskPo.setStaffName(staffs.getJSONObject(staffIndex).getString("staffName"));
            int flag = workTaskV1InnerServiceSMOImpl.saveWorkTask(workTaskPo);

            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }

            if(StringUtil.isEmpty(reqJson.getString("pathUrl"))){
                continue;
            }

            WorkPoolFilePo workPoolFilePo = new WorkPoolFilePo();
            workPoolFilePo.setCommunityId(workPoolPo.getCommunityId());
            workPoolFilePo.setFileType(WorkPoolFileDto.FILE_TYPE_START);
            workPoolFilePo.setFileId(GenerateCodeFactory.getGeneratorId("11"));
            workPoolFilePo.setWorkId(workPoolPo.getWorkId());
            workPoolFilePo.setTaskId(workTaskPo.getTaskId());
            workPoolFilePo.setPathUrl(reqJson.getString("pathUrl"));
            workPoolFilePo.setStoreId(workPoolPo.getStoreId());
            workPoolFileV1InnerServiceSMOImpl.saveWorkPoolFile(workPoolFilePo);
        }
    }

    private void saveWorkCycle(WorkPoolPo workPoolPo, JSONObject reqJson, UserDto userDto) {

        JSONArray staffs = reqJson.getJSONArray("staffs");
        for (int staffIndex = 0; staffIndex < staffs.size(); staffIndex++) {
            WorkCyclePo workCyclePo = new WorkCyclePo();
            workCyclePo.setWorkCycle(workPoolPo.getWorkCycle());
            workCyclePo.setWorkId(workPoolPo.getWorkId());
            workCyclePo.setCommunityId(workPoolPo.getCommunityId());
            workCyclePo.setStoreId(workCyclePo.getStoreId());
            workCyclePo.setBeforeTime("30");
            workCyclePo.setCycleId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            workCyclePo.setPeriod(reqJson.getString("period"));
            workCyclePo.setHours(reqJson.getString("hours"));
            if (WorkCycleDto.PERIOD_MONTH_DAY.equals(reqJson.getString("period"))) {
                JSONArray months = reqJson.getJSONArray("months");
                JSONArray days = reqJson.getJSONArray("days");
                String monthStr = "";
                for (int monthIndex = 0; monthIndex < months.size(); monthIndex++) {
                    monthStr += (months.getString(monthIndex) + ",");
                }
                String dayStr = "";
                for (int dayIndex = 0; dayIndex < days.size(); dayIndex++) {
                    dayStr += (days.getString(dayIndex) + ",");
                }
                workCyclePo.setPeriodMonth(monthStr);
                workCyclePo.setPeriodDay(dayStr);
            }
            if (WorkCycleDto.PERIOD_MONTH_WORKDAY.equals(reqJson.getString("period"))) {
                JSONArray workdays = reqJson.getJSONArray("workdays");
                String workdaysStr = "";
                for (int workdaysIndex = 0; workdaysIndex < workdays.size(); workdaysIndex++) {
                    workdaysStr += (workdays.getString(workdaysIndex) + ",");
                }
                workCyclePo.setPeriodWorkday(workdaysStr);
            }
            workCyclePo.setStaffId(staffs.getJSONObject(staffIndex).getString("staffId"));
            workCyclePo.setStaffName(staffs.getJSONObject(staffIndex).getString("staffName"));
            int flag = workCycleV1InnerServiceSMOImpl.saveWorkCycle(workCyclePo);

            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
        }

    }

    /**
     * 保存抄送人
     *
     * @param workPoolPo
     * @param reqJson
     * @param userDto
     */
    private void saveCopyStaff(WorkPoolPo workPoolPo, JSONObject reqJson, UserDto userDto) {

        if (!reqJson.containsKey("copyStaffs")) {
            return;
        }

        JSONArray copyStaffs = reqJson.getJSONArray("copyStaffs");
        if (ListUtil.isNull(copyStaffs)) {
            return;
        }
        WorkCopyPo workCopyPo = null;
        for (int copyIndex = 0; copyIndex < copyStaffs.size(); copyIndex++) {
            workCopyPo = new WorkCopyPo();
            workCopyPo.setCopyId(GenerateCodeFactory.getGeneratorId("11"));
            workCopyPo.setStaffId(copyStaffs.getJSONObject(copyIndex).getString("staffId"));
            workCopyPo.setStaffName(copyStaffs.getJSONObject(copyIndex).getString("staffName"));
            workCopyPo.setStoreId(reqJson.getString("storeId"));
            workCopyPo.setWorkId(workPoolPo.getWorkId());
            workCopyPo.setCommunityId(reqJson.getString("communityId"));
            workCopyPo.setState(WorkCopyDto.STATE_DOING);
            workCopyV1InnerServiceSMOImpl.saveWorkCopy(workCopyPo);
        }
    }

    /**
     * 保存内容
     *
     * @param workPoolPo
     * @param reqJson
     * @param userDto
     */
    private void saveContent(WorkPoolPo workPoolPo, JSONObject reqJson, UserDto userDto) {

        WorkPoolContentPo workPoolContentPo = new WorkPoolContentPo();
        workPoolContentPo.setContentId(GenerateCodeFactory.getGeneratorId("11"));
        workPoolContentPo.setContent(reqJson.getString("content"));
        workPoolContentPo.setWorkId(workPoolPo.getWorkId());
        workPoolContentPo.setCommunityId(reqJson.getString("communityId"));
        workPoolContentPo.setStoreId(reqJson.getString("storeId"));
        workPoolContentV1InnerServiceSMOImpl.saveWorkPoolContent(workPoolContentPo);
    }
}
