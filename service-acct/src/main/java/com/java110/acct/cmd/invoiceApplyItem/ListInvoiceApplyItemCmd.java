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
package com.java110.acct.cmd.invoiceApplyItem;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.acct.IInvoiceApplyItemV1InnerServiceSMO;
import com.java110.po.invoiceApplyItem.InvoiceApplyItemPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.invoiceApplyItem.InvoiceApplyItemDto;
import java.util.List;
import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：查询
 * 服务编码：invoiceApplyItem.listInvoiceApplyItem
 * 请求路劲：/app/invoiceApplyItem.ListInvoiceApplyItem
 * add by 吴学文 at 2023-10-08 18:31:57 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "invoiceApplyItem.listInvoiceApplyItem")
public class ListInvoiceApplyItemCmd extends Cmd {

  private static Logger logger = LoggerFactory.getLogger(ListInvoiceApplyItemCmd.class);
    @Autowired
    private IInvoiceApplyItemV1InnerServiceSMO invoiceApplyItemV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

           InvoiceApplyItemDto invoiceApplyItemDto = BeanConvertUtil.covertBean(reqJson, InvoiceApplyItemDto.class);

           int count = invoiceApplyItemV1InnerServiceSMOImpl.queryInvoiceApplyItemsCount(invoiceApplyItemDto);

           List<InvoiceApplyItemDto> invoiceApplyItemDtos = null;

           if (count > 0) {
               invoiceApplyItemDtos = invoiceApplyItemV1InnerServiceSMOImpl.queryInvoiceApplyItems(invoiceApplyItemDto);
           } else {
               invoiceApplyItemDtos = new ArrayList<>();
           }

           ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, invoiceApplyItemDtos);

           ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

           cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
