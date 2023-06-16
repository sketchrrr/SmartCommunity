package com.java110.user.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 组织组件内部之间使用，没有给外围系统提供服务能力
 * 组织服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IOrgServiceDao {

    /**
     * 保存 组织信息
     * @param businessOrgInfo 组织信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessOrgInfo(Map businessOrgInfo) throws DAOException;



    /**
     * 查询组织信息（business过程）
     * 根据bId 查询组织信息
     * @param info bId 信息
     * @return 组织信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessOrgInfo(Map info) throws DAOException;




    /**
     * 保存 组织信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveOrgInfoInstance(Map info) throws DAOException;




    /**
     * 查询组织信息（instance过程）
     * 根据bId 查询组织信息
     * @param info bId 信息
     * @return 组织信息
     * @throws DAOException DAO异常
     */
    List<Map> getOrgInfo(Map info) throws DAOException;

    /**
     * 查询上级组织信息（instance过程）
     * 根据bId 查询组织信息
     * @param info bId 信息
     * @return 组织信息
     * @throws DAOException DAO异常
     */
    List<Map> getParentOrgInfo(Map info) throws DAOException;



    /**
     * 修改组织信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateOrgInfoInstance(Map info) throws DAOException;


    /**
     * 查询组织总数
     *
     * @param info 组织信息
     * @return 组织数量
     */
    int queryOrgsCount(Map info);

}
