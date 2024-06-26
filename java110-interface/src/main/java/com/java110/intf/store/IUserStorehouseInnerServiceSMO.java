package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.user.UserStorehouseDto;
import com.java110.po.user.UserStorehousePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IUserStorehouseInnerServiceSMO
 * @Description 个人物品接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/userUserStorehouseApi")
public interface IUserStorehouseInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param userUserStorehousehouseDto 数据对象分享
     * @return UserStorehouseDto 对象数据
     */
    @RequestMapping(value = "/queryUserStorehouses", method = RequestMethod.POST)
    List<UserStorehouseDto> queryUserStorehouses(@RequestBody UserStorehouseDto userUserStorehousehouseDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param userUserStorehousehouseDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryUserStorehousesCount", method = RequestMethod.POST)
    int queryUserStorehousesCount(@RequestBody UserStorehouseDto userUserStorehousehouseDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param userStorehousePo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/saveUserStorehouses", method = RequestMethod.POST)
    int saveUserStorehouses(@RequestBody UserStorehousePo userStorehousePo);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param userStorehousePo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/updateUserStorehouses", method = RequestMethod.POST)
    int updateUserStorehouses(@RequestBody UserStorehousePo userStorehousePo);
}
