package com.java110.user.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 用户登录组件内部之间使用，没有给外围系统提供服务能力
 * 用户登录服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IUserLoginServiceDao {


    /**
     * 保存 用户登录信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveUserLoginInfo(Map info) throws DAOException;




    /**
     * 查询用户登录信息（instance过程）
     * 根据bId 查询用户登录信息
     * @param info bId 信息
     * @return 用户登录信息
     * @throws DAOException DAO异常
     */
    List<Map> getUserLoginInfo(Map info) throws DAOException;



    /**
     * 修改用户登录信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateUserLoginInfo(Map info) throws DAOException;


    /**
     * 查询用户登录总数
     *
     * @param info 用户登录信息
     * @return 用户登录数量
     */
    int queryUserLoginsCount(Map info);

}
