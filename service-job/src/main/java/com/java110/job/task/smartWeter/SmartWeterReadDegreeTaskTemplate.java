package com.java110.job.task.smartWeter;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.attendanceClasses.AttendanceClassesDto;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDetailDto;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDto;
import com.java110.dto.attendanceClassesStaff.AttendanceClassesStaffDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.meterMachine.MeterMachineDto;
import com.java110.dto.scheduleClassesStaff.ScheduleClassesStaffDto;
import com.java110.dto.scheduleClassesTime.ScheduleClassesTimeDto;
import com.java110.dto.store.StoreDto;
import com.java110.dto.task.TaskDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IAttendanceClassesInnerServiceSMO;
import com.java110.intf.common.IAttendanceClassesTaskDetailInnerServiceSMO;
import com.java110.intf.common.IAttendanceClassesTaskInnerServiceSMO;
import com.java110.intf.common.IMeterMachineV1InnerServiceSMO;
import com.java110.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.java110.intf.store.IScheduleClassesStaffV1InnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.IAttendanceClassesStaffV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.attendanceClassesTask.AttendanceClassesTaskPo;
import com.java110.po.attendanceClassesTaskDetail.AttendanceClassesTaskDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import io.micrometer.core.instrument.Meter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 智能电表远程抄表
 * <p>
 * 执行最小单位不能小于一个小时
 */
@Component
public class SmartWeterReadDegreeTaskTemplate extends TaskSystemQuartz {

    @Autowired
    private IMeterMachineV1InnerServiceSMO meterMachineV1InnerServiceSMOImpl;


    @Override
    protected void process(TaskDto taskDto) throws Exception {
        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            try {
                doReadDegree(taskDto, communityDto);
            } catch (Exception e) {
                logger.error("生成月报表 失败", e);
            }
        }

    }

    /**
     * 考勤任务
     *
     * @param taskDto
     */
    private void doReadDegree(TaskDto taskDto, CommunityDto communityDto) {

        MeterMachineDto meterMachineDto = new MeterMachineDto();
        meterMachineDto.setCommunityId(communityDto.getCommunityId());
        List<MeterMachineDto> meterMachineDtos = meterMachineV1InnerServiceSMOImpl.queryMeterMachines(meterMachineDto);

        if (meterMachineDtos == null || meterMachineDtos.size() < 1) {
            return;
        }

        List<MeterMachineDto> tmpMeterMachineDtos = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hours = calendar.get(Calendar.HOUR);

        for (MeterMachineDto tmpMeterMachineDto : meterMachineDtos) {
            if (MeterMachineDto.MACHINE_MODEL_READ.equals(tmpMeterMachineDto.getMachineModel())) {
                tmpMeterMachineDtos.add(tmpMeterMachineDto);
                continue;
            }

            if (day == tmpMeterMachineDto.getReadDay() && hours == tmpMeterMachineDto.getReadHours()) {
                tmpMeterMachineDtos.add(tmpMeterMachineDto);
            }
        }

        if (tmpMeterMachineDtos.size() < 1) {
            return;
        }

        try {
            meterMachineV1InnerServiceSMOImpl.requestReads(tmpMeterMachineDtos);
        } catch (Exception e) {
            logger.error("抄表失败", e);

        }
    }
}
