package com.java110.fee.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 费用折扣组件内部之间使用，没有给外围系统提供服务能力
 * 费用折扣服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IFeeDiscountSpecServiceDao {


    /**
     * 保存 费用折扣信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveFeeDiscountSpecInfo(Map info) throws DAOException;




    /**
     * 查询费用折扣信息（instance过程）
     * 根据bId 查询费用折扣信息
     * @param info bId 信息
     * @return 费用折扣信息
     * @throws DAOException DAO异常
     */
    List<Map> getFeeDiscountSpecInfo(Map info) throws DAOException;



    /**
     * 修改费用折扣信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateFeeDiscountSpecInfo(Map info) throws DAOException;


    /**
     * 查询费用折扣总数
     *
     * @param info 费用折扣信息
     * @return 费用折扣数量
     */
    int queryFeeDiscountSpecsCount(Map info);

}
