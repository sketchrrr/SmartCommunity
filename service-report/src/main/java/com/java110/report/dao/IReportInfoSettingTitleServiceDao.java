package com.java110.report.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 进出上报题目设置组件内部之间使用，没有给外围系统提供服务能力
 * 进出上报题目设置服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IReportInfoSettingTitleServiceDao {


    /**
     * 保存 进出上报题目设置信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveReportInfoSettingTitleInfo(Map info) throws DAOException;




    /**
     * 查询进出上报题目设置信息（instance过程）
     * 根据bId 查询进出上报题目设置信息
     * @param info bId 信息
     * @return 进出上报题目设置信息
     * @throws DAOException DAO异常
     */
    List<Map> getReportInfoSettingTitleInfo(Map info) throws DAOException;



    /**
     * 修改进出上报题目设置信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateReportInfoSettingTitleInfo(Map info) throws DAOException;


    /**
     * 查询进出上报题目设置总数
     *
     * @param info 进出上报题目设置信息
     * @return 进出上报题目设置数量
     */
    int queryReportInfoSettingTitlesCount(Map info);

}
