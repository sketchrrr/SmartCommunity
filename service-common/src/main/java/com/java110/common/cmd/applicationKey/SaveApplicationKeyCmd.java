package com.java110.common.cmd.applicationKey;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.machine.MachineDto;
import com.java110.intf.common.IApplicationKeyV1InnerServiceSMO;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.po.accessControl.ApplicationKeyPo;
import com.java110.po.file.FileRelPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

@Java110Cmd(serviceCode = "applicationKey.saveApplicationKey")
public class SaveApplicationKeyCmd extends Cmd{


    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IApplicationKeyV1InnerServiceSMO applicationKeyV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写姓名");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写手机号");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择用户类型");
        Assert.hasKeyAndValue(reqJson, "sex", "必填，请选择性别");
        Assert.hasKeyAndValue(reqJson, "age", "必填，请填写年龄");
        Assert.hasKeyAndValue(reqJson, "idCard", "必填，请填写身份证号");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择结束时间");
        Assert.hasKeyAndValue(reqJson, "locationTypeCd", "必填，位置不能为空");
        Assert.hasKeyAndValue(reqJson, "locationObjId", "必填，未选择位置对象");
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(reqJson, "typeFlag", "必填，请选择钥匙类型");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {


        //添加单元信息
        addApplicationKey(reqJson);

        // addMsg(reqJson, context);

        if (reqJson.containsKey("photo") && !StringUtils.isEmpty(reqJson.getString("photo"))) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(reqJson.getString("photo"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(reqJson.getString("communityId"));
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);

            reqJson.put("applicationKeyPhotoId", fileDto.getFileId());
            reqJson.put("fileSaveName", fileName);

            addOwnerPhoto(reqJson);

        }
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addOwnerPhoto(JSONObject paramInJson) {
        FileRelPo fileRelPo = new FileRelPo();
        fileRelPo.setFileRelId( GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
        fileRelPo.setRelTypeCd("30000");
        fileRelPo.setSaveWay("table");
        fileRelPo.setObjId(paramInJson.getString("applicationKeyId"));
        fileRelPo.setFileRealName(paramInJson.getString("applicationKeyPhotoId"));
        fileRelPo.setFileSaveName(paramInJson.getString("fileSaveName"));
        int flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);

        if(flag < 1){
            throw new CmdException("申请钥匙失败");
        }
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addApplicationKey(JSONObject paramInJson) {

        String applicationKeyId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applicationKeyId);
        paramInJson.put("applicationKeyId", applicationKeyId);
        //根据位置id 和 位置对象查询相应 设备ID
        if (!paramInJson.containsKey("machineId")) {
            MachineDto machineDto = new MachineDto();
            machineDto.setLocationObjId(paramInJson.getString("locationObjId"));
            machineDto.setLocationTypeCd(paramInJson.getString("locationTypeCd"));
            List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
            Assert.listOnlyOne(machineDtos, "该位置还没有相应的门禁设备");
            paramInJson.put("machineId", machineDtos.get(0).getMachineId());
        }

        ApplicationKeyPo applicationKeyPo = BeanConvertUtil.covertBean(paramInJson, ApplicationKeyPo.class);
        applicationKeyPo.setApplicationKeyId(applicationKeyId);
        applicationKeyPo.setState("10002");
        applicationKeyPo.setPwd(this.getRandom());
        if ("1100103".equals(paramInJson.getString("typeFlag"))) { // 临时访问密码,只设置成24小时
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, 24);
            applicationKeyPo.setEndTime(DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        }
        int flag = applicationKeyV1InnerServiceSMOImpl.saveApplicationKey(applicationKeyPo);
        if (flag < 1) {
            throw new CmdException("保存开门记录失败");
        }
    }

    /**
     * 获取随机数
     *
     * @return
     */
    private String getRandom() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += (random.nextInt(9) + 1);;
        }
        return result;
    }
}
