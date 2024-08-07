package com.java110.common.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 批量操作日志组件内部之间使用，没有给外围系统提供服务能力
 * 批量操作日志服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IAssetImportLogServiceDao {


    /**
     * 保存 批量操作日志信息
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveAssetImportLogInfo(Map info) throws DAOException;


    /**
     * 查询批量操作日志信息（instance过程）
     * 根据bId 查询批量操作日志信息
     *
     * @param info bId 信息
     * @return 批量操作日志信息
     * @throws DAOException DAO异常
     */
    List<Map> getAssetImportLogInfo(Map info) throws DAOException;


    /**
     * 修改批量操作日志信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateAssetImportLogInfo(Map info) throws DAOException;


    /**
     * 查询批量操作日志总数
     *
     * @param info 批量操作日志信息
     * @return 批量操作日志数量
     */
    int queryAssetImportLogsCount(Map info);

    /**
     * 查询导入日志类型
     *
     * @param info
     * @return
     */
    List<Map> queryAssetImportLogType(Map info);
}
