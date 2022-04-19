package com.java110.core.language;

import com.java110.dto.menuCatalog.MenuCatalogDto;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 藏语
 */
@Component
public class Tibetan extends DefaultLanguage {
    private static Map<String, String> menuCatalogs = new HashMap<>();
    private static Map<String, String> menus = new HashMap<>();
    private static Map<String, String> msgs = new HashMap<>();



    public List<MenuCatalogDto> getMenuCatalog(List<MenuCatalogDto> menuCatalogDtos) {
        String menuCatalogsName = "";
        for (MenuCatalogDto menuCatalogDto : menuCatalogDtos) {
            menuCatalogsName = menuCatalogs.get(menuCatalogDto.getName());
            if (!StringUtil.isEmpty(menuCatalogsName)) {
                menuCatalogDto.setName(menuCatalogsName);
            }
        }
        return menuCatalogDtos;
    }


    @Override
    public List<Map> getMenuDto(List<Map> menuDtos) {
        String menuName = "";
        for (Map menuDto : menuDtos) {
            menuName = menus.get(menuDto.get("menuGroupName"));
            if (!StringUtil.isEmpty(menuName)) {
                menuDto.put("menuGroupName", menuName);
            }

            menuName = menus.get(menuDto.get("menuName"));
            if (!StringUtil.isEmpty(menuName)) {
                menuDto.put("menuName", menuName);
            }

        }
        return menuDtos;
    }

    @Override
    public ResultVo getResultVo(ResultVo resultVo) {
        String msg = msgs.get(resultVo.getMsg());
        if (!StringUtil.isEmpty(msg)) {
            resultVo.setMsg(msg);
        }

        return resultVo;
    }

    public String getLangMsg(String msg){
        String msgStr = msgs.get(msg);
        if (!StringUtil.isEmpty(msg)) {
            return msgStr;
        }

        return msg;
    }
    static {
        menuCatalogs.put("设备", "སྒྲིག་ཆས། ");
        menuCatalogs.put("首页", "ཁྱིམ། ");
        menuCatalogs.put("业务受理", "ལས་སྒོ་བདག་སྤྲོད། ");
        menuCatalogs.put("费用报表", "འགྲོ་གྲོན་སྙན་ཐོའི་རེའུ་མིག། ");
        menuCatalogs.put("物业服务", "གཞིས་ཀ་འི་ཞབས་ཞུ། ");
        menuCatalogs.put("设备停车", "སྒྲིག་ཆས་རླངས་འཁོར། ");
        menuCatalogs.put("常用菜单", "ཉེར་མཁོ་འི་ཡོ་བྱད། ");
        menuCatalogs.put("设置", "བཀོད་སྒྲིག། ");

        menus.put("小区管理", "ཆུང་གྲས་ཀྱི་དོ་དམ། ");
        menus.put("我的小区", "ང་འི་སྡོད་ཁུལ། ");
        menus.put("小区大屏", "གཞིས་ཁུལ་ཆེན་པོ་འི་མཐའ་སྐོར། ");
        menus.put("业务受理", "ལས་སྒོ་བདག་སྤྲོད། ");
        menus.put("房屋装修", "ཁང་བ་སྒྲིག་སྦྱོར། ");
        menus.put("结构图", "གྲུབ་ཚུལ། ");
        menus.put("车位结构图", "རླངས་འཁོར་འཇོག་ས་འི་གྲུབ་ཚུལ། ");
        menus.put("产权登记", "བདག་དབང་ཐོ་འགོད། ");
        menus.put("视频监控", "བརྙན་འཕྲིན་དང་ཚོད་འཛིན། ");
        menus.put("资产管理", "རྒྱུ་ནོར་དོ་དམ། ");
        menus.put("楼栋信息", "ཐོག་ཁང་ཆ་འཕྲིན། ");
        menus.put("单元信息", "སྡེ་ཚན་གྱི་ཆ་འཕྲིན། ");
        menus.put("房屋管理", "ཁང་ཁྱིམ་དོ་དམ། ");
        menus.put("商铺管理", "ཚོང་ཁང་དོ་དམ། ");
        menus.put("业主管理", "བདག་པོ་དང་དོ་དམ། ");
        menus.put("业主信息", "བདག་པོ་འི་ཆ་འཕྲིན། ");
        menus.put("业主成员", "བདག་པོ་འི་ཁོངས་མི། ");
        menus.put("绑定业主", "གནམ་བདག་པོ། ");
        menus.put("业主账号", "བདག་པོ་རྩིས་ཐོའི་ཨང་གྲངས། ");
        menus.put("费用管理", "འགྲོ་གྲོན་དོ་དམ། ");
        menus.put("费用项设置", "གྲོན་དངུལ་རྣམ་གྲངས་བཀོད་སྒྲིག། ");
        menus.put("房屋收费", "ཁང་བའི་གླ་རིན། ");
        menus.put("车辆收费", "འཁོར་ལོ་འི་རིན་བསྡུ་འི། ");
        menus.put("合同收费", "གན་རྒྱ་འི་རིན་བསྡུ་འི། ");
        menus.put("账单催缴", "བསྡུ། ");
        menus.put("费用导入", "དབུགས་འདྲེན་པའི་འགྲོ་གྲོན། ");
        menus.put("员工收费", "ལས་བཟོ་པའི་རིན་བསྡུ་འི། ");
        menus.put("费用汇总表", "འགྲོ་གྲོན་ཕྱོགས་བསྡུས་རེའུ་མིག། ");
        menus.put("退费审核", "ཞིང་གྲོན་དངུལ་ཞིབ་བཤེར། ");
        menus.put("欠费信息", "ཆད་དངུལ་ཆ་འཕྲིན། ");
        menus.put("抄表类型", "བཤུ་རེའུ་མིག་རིགས་དབྱིབས། ");
        menus.put("水电抄表", "ཆུ་གློག་བཤུ་རེའུ་མིག། ");
        menus.put("补打收据", "ཁ་གསབ་རྒྱག་པ་འབྱོར་ཐོ། ");
        menus.put("公摊公式", "ཆ་གསེས་སྤྱི་འགྲོས། ");
        menus.put("缴费审核", "གྲོན་དངུལ་སྤྲོད་པ་ཞིབ་བཤེར། ");
        menus.put("折扣设置", "ཕབ་ཆ་བཀོད་སྒྲིག། ");
        menus.put("优惠申请", "གཟིགས་སྐྱོང་བྱེད་པའི་རེ་ཞུ། ");
        menus.put("优惠类型", "གཟིགས་སྐྱོང་གི་རིགས་དབྱིབས། ");
        menus.put("临时车收费", "གནས་སྐབས་ཀྱི་རླངས་འཁོར་རིན་བསྡུ་འི། ");
        menus.put("临时车缴费清单", "གནས་སྐབས་ཀྱི་རླངས་འཁོར་གྱི་འཇལ་གྲོན་རྩིས་ཁྲ། ");
        menus.put("取消费用", "ཕྱིར་འཐེན་བྱེད་པའི་འགྲོ་གྲོན། ");
        menus.put("智慧停车", "བློ་གྲོས་རླངས་འཁོར། ");
        menus.put("停车场管理", "རླངས་འཁོར་འཇོག་ས་དོ་དམ། ");
        menus.put("车位信息", "རླངས་འཁོར་འཇོག་ས་སྒྲིག་འཇོག་བྱ་ཆ་འཕྲིན། ");
        menus.put("岗亭管理", "སོ་སྲུང་ཁང་བུ་དོ་དམ། ");
        menus.put("业主车辆", "བདག་པོ་འཁོར་ལོ། ");
        menus.put("进场记录", "ཉོ་དགོས་ཟིན་ཐོ། ");
        menus.put("在场车辆", "དེ་ག་ར་རླངས་འཁོར། ");
        menus.put("黑白名单", "དཀར་ནག་མིང་ཐོ། ");
        menus.put("剩余车位", "དེ་ལས་ལྷག་པའི་རླངས་འཁོར་འཇོག་ས་སྒྲིག་འཇོག་བྱ། ");
        menus.put("车位申请", "རླངས་འཁོར་འཇོག་ས་སྒྲིག་འཇོག་བྱ་རེ་ཞུ། ");
        menus.put("报修管理", "ཞིག་གསོ་དོ་དམ། ");
        menus.put("报修设置", "སྐྱ་ར་བཀོད་སྒྲིག། ");
        menus.put("电话报修", "ཁ་པར་ཉམས་གསོ། ");
        menus.put("工单池", "ལས་གཅིག་པུ་གསང། ");
        menus.put("报修待办", "རེ་སྒུག་ཉམས་གསོ། ");
        menus.put("报修已办", "སྐྱ་མཇུག་རྫོགས། ");
        menus.put("报修回访", "སྐྱ་ར་ལན་འདེབས། ");
        menus.put("强制回单", "སྐྱ་ར་བཙན་ཤེད་ཀྱིས་ཕྱིར། ");
        menus.put("疫情管控", "ནད་ཡམས་ཚོད་འཛིན། ");
        menus.put("疫情设置", "ནད་ཡམས་བཀོད་སྒྲིག། ");
        menus.put("返省上报", "ཡ་ཐོག་ཞིང་ཆེན་ཞུ། ");
        menus.put("疫情上报", "ནད་ཡམས་ཞུ། ");
        menus.put("合同管理", "གན་རྒྱ་འི་དོ་དམ། ");
        menus.put("合同类型", "གན་རྒྱ་འི་དབྱིབས། ");
        menus.put("合同甲方", "གན་རྒྱ་Aཕྱོགས་ཀྱིས། ");
        menus.put("起草合同", "Drafting contracts");
        menus.put("合同查询", "Contract query");
        menus.put("合同变更", "Contract changes");
        menus.put("到期合同", "Expired contract");
        menus.put("报表管理", "Report management");
        menus.put("报表专家", "Report Specialist");
        menus.put("楼栋费用表", "Building fee schedule");
        menus.put("费用分项表", "Fee breakdown");
        menus.put("费用明细表", "Schedule of Fees");
        menus.put("缴费清单", "Payment list");
        menus.put("欠费明细表", "Arrears Schedule");
        menus.put("预缴费提醒", "Prepayment reminder");
        menus.put("费用到期提醒", "Fee due reminder");
        menus.put("缴费明细表", "Payment Schedule");
        menus.put("报修汇总表", "Repair report summary");
        menus.put("未收费房屋", "Unpaid housing");
        menus.put("问卷明细表", "Questionnaire Schedule");
        menus.put("业主缴费明细", "Owner's payment details");
        menus.put("华宁物业报表", "Huaning Property Report");
        menus.put("押金报表", "Deposit Statement");
        menus.put("报修报表", "Repair report");
        menus.put("营业报表", "Business statement");
        menus.put("协同办公", "Work together");
        menus.put("流程实例", "Process instance");
        menus.put("我的流程", "My process");
        menus.put("工作办理", "Work");
        menus.put("发布公告", "Annouce");
        menus.put("电话投诉", "Phone complaint");
        menus.put("访客登记", "Visitor Registration");
        menus.put("信息发布", "Information Release");
        menus.put("信息大类", "Information categories");
        menus.put("智慧考勤", "Smart Attendance");
        menus.put("今日考勤", "Attendance today");
        menus.put("月考勤", "Monthly attendance");
        menus.put("考勤记录", "Attendance Record");
        menus.put("问卷投票", "Poll");
        menus.put("我的问卷", "My questionnaire");
        menus.put("活动规则", "Activity Rules");
        menus.put("采购管理", "Purchasing management");
        menus.put("仓库信息", "Warehouse information");
        menus.put("物品信息", "Item information");
        menus.put("物品类型", "Item type");
        menus.put("物品供应商", "Item supplier");
        menus.put("物品规格", "Item Specifications");
        menus.put("采购申请", "Purchase Requisition");
        menus.put("调拨记录", "Call record");
        menus.put("出入库明细", "Inbound and outbound details");
        menus.put("调拨明细", "Transfer details");
        menus.put("物品领用", "Items received");
        menus.put("我的物品", "My items");
        menus.put("转赠记录", "Transfer record");
        menus.put("物品使用记录", "Item usage record");
        menus.put("巡检管理", "Inspection management");
        menus.put("巡检项目", "Inspection items");
        menus.put("巡检点", "Inspection point");
        menus.put("巡检路线", "Inspection route");
        menus.put("巡检计划", "Inspection plan");
        menus.put("巡检任务", "Inspection tasks");
        menus.put("巡检明细", "Inspection details");
        menus.put("设备管理", "Device management");
        menus.put("设备信息", "Device Information");
        menus.put("设备类型", "Device type");
        menus.put("设备数据同步", "Device data synchronization");
        menus.put("开门记录", "Open door record");
        menus.put("访客留影", "Visitor's photo");
        menus.put("申请钥匙", "Request key");
        menus.put("钥匙审核", "Key audit");
        menus.put("员工门禁授权", "Employee access control authorization");
        menus.put("组织管理", "Organizational management");
        menus.put("组织信息", "Organization Information");
        menus.put("员工信息", "Employee information");
        menus.put("员工认证", "Employee Certification");
        menus.put("系统管理", "System Management");
        menus.put("权限组", "Rights Groups");
        menus.put("员工权限", "Employee permissions");
        menus.put("小区配置", "Cell configuration");
        menus.put("流程管理", "Process management");
        menus.put("修改密码", "Change password");
        menus.put("商户信息", "Business information");
        menus.put("公众号", "Wechat");
        menus.put("小程序配置", "Wechat mini");
        menus.put("短信配置", "Message configure");
        menus.put("位置信息", "Location");
        menus.put("资产导入导出", "Assets import&export");
        menus.put("历史缴费导入", "Import history fee");
        menus.put("打印配置", "Print configuration");
        menus.put("收据模板", "Receipt template");
    }
}
