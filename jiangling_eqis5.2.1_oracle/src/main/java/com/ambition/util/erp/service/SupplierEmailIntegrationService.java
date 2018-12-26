package com.ambition.util.erp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Service;

import com.ambition.supplier.archives.dao.SupplierDao;
import com.ambition.supplier.develop.dao.SupplierSurveyDao;
import com.ambition.supplier.entity.Supplier;
import com.ambition.util.common.DateUtil;
import com.ambition.util.erp.schedule.ScheduleJob;
import com.ambition.util.exception.AmbFrameException;

/**
 * 类名:供应商信息与KIS整合的服务类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：处理QIS与KIS系统供应商信息的整合</p>
 * @author  yuke
 * @version 1.00 2014-08-18 发布
 */
@Service
public class SupplierEmailIntegrationService implements IntegrationService,Runnable {
	private Logger log = Logger.getLogger(this.getClass());
	private static String ERP_VIEW_TABLE ="apps.XXCUS_PO_VENDORS_WEB_V";
	@Autowired
	private SupplierDao supplierDao;
	
	/**
     * erp1连接数据源
     */
    @Resource(name="ordbDataJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    
	
	@Override
	public void run() {
		try{
			beginIntegration(ScheduleJob.getScheduleCompanyId(),ScheduleJob.getScheduleUserName());
		}catch (Exception e) {
			log.error("自动更新供应商邮箱信息失败!",e);
		}
//		ScheduleJob
//		.addSchedule(this,ScheduleJob.getSupplierScheduleDate().getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 导入供应商信息
	 */
	public void beginIntegration(Long companyId,String userLoginName){
		if(integrationFlag){
			log.error("当前正有别的用户在执行更新供应商邮箱信息的操作!");
			return;
		}
		integrationFlag = true;
		//导入
		long time = System.currentTimeMillis();
		try {
            integration(companyId, userLoginName,ERP_VIEW_TABLE,jdbcTemplate);
		}catch (Throwable e) {
			throw new AmbFrameException("导出ERP供应商邮箱信息失败!",e);
		}
		log.debug("导入供应商邮箱信息用时:" + (System.currentTimeMillis()-time)/1000 + "秒");
	}
	
	/**
          * 方法名: 
          * <p>功能说明：</p>
          * @param calendar
          * @return
         */
    private void integration(Long companyId, String userLoginName,
            String table, JdbcTemplate jdbcTemplate) {
        Connection conn = null;
        PreparedStatement ps = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            //查询最新的供应商信息
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select vendor_code as vendor_code," +
                    "contact_email as contact_email" +
                    " from "+ERP_VIEW_TABLE);
            //删除QIS历史数据
            conn = SessionFactoryUtils.getDataSource(supplierDao.getSessionFactory()).getConnection();
            conn.setAutoCommit(false);
            //执行导入操作
            statement = conn.createStatement();
            int count=0;
            while(rowSet.next()){
                String code = rowSet.getString("vendor_code");//供应商代码
                String email = rowSet.getString("contact_email");//供应商名称
                if(email != null){
            		email = email.split(";")[0];
                }
                ps = conn.prepareStatement("select id from SUPPLIER_SUPPLIER s where s.code = ? ");
                ps.setString(1,code);
                rs = ps.executeQuery();
                
                Long supplierId = null;
                if(rs.next()){
                    supplierId = rs.getLong(1);
                }
                rs.close();
                ps.close();
                
                String sql = "";
                if(supplierId != null){
                	sql = "update SUPPLIER_SUPPLIER set supplier_Email="+(email==null?null:"'"+email+"' ")+ 
                            " where id = "+supplierId;
                	statement.addBatch(sql);
                }                                
                count++;
                if(count>=2000){
                    statement.executeBatch();
                    statement.clearBatch();
                    count=0;
                }
            }
            if(count>0){
                statement.executeBatch();
                statement.clearBatch();
            }
            
            //更新操作时间
            String tableName = "supplier_email";
            ps = conn.prepareStatement("select id from AMB_UPDATE_TIMESTAMP a where a.table_name = ? ");
            ps.setString(1,tableName);
            rs = ps.executeQuery();
            Long updateId = null;
            if(rs.next()){
                updateId = rs.getLong(1);
            }
            ps.close();
            
            if(updateId != null){
                ps = conn.prepareStatement("update AMB_UPDATE_TIMESTAMP set  last_modified_time = ? where  id = ?");
                ps.setTimestamp(1,new java.sql.Timestamp(System.currentTimeMillis()));
                ps.setLong(2,updateId);
                ps.execute();
            }else{
                ps = conn.prepareStatement("insert into AMB_UPDATE_TIMESTAMP(" +
                        "company_id,creator,created_time,last_modifier,last_modified_time,table_name,id)" +
                        " values(?,?,?,?,?,?,hibernate_sequence.nextval)");
                ps.setLong(1,companyId);
                ps.setString(2,userLoginName);
                ps.setTimestamp(3,new java.sql.Timestamp(System.currentTimeMillis()));
                ps.setString(4,userLoginName);
                ps.setTimestamp(5,new java.sql.Timestamp(System.currentTimeMillis()));
                ps.setString(6,tableName);
                ps.execute();
            }
            //提交事务
            conn.commit();
        }catch (Throwable e) {
            try {
                if(conn != null&&!conn.getAutoCommit()){
                    conn.rollback();
                }
            } catch (SQLException e1) {
                log.error("导入供应商邮箱信息失败后回滚事务时错误",e1);
            }
            throw new AmbFrameException("导出ERP供应商邮箱信息失败!",e);
        }finally{
            integrationFlag = false;
            try {
                if(rs != null){
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("导入供应商邮箱信息后关闭数据库RS时错误",e);
            }
            try {
                if(ps != null&&!ps.isClosed()){
                    ps.close();
                }
            } catch (SQLException e) {
                log.error("导入供应商邮箱信息后关闭数据库PS时错误",e);
            }
            try {
                if(statement != null&&!statement.isClosed()){
                    statement.close();
                }
            } catch (SQLException e) {
                log.error("导入供应商邮箱信息后关闭数据库statement时错误",e);
            }
            try {
                if(conn != null&&!conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException e) {
                log.error("导入供应商邮箱信息后关闭数据库CONN时错误",e);
            }
        }
        
    }

    private volatile boolean integrationFlag = false;
	@Override
	public boolean isIntegration() {
		return integrationFlag;
	}
	public int getProgressbar() {
		// TODO Auto-generated method stub
		return 0;
	}
}
