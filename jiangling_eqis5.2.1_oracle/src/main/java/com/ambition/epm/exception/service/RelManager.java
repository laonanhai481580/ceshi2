package com.ambition.epm.exception.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.epm.entity.Rel;
import com.ambition.epm.exception.dao.RelDao;
import com.ambition.gp.entity.GpAverageMaterial;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 
 * 类名:体系维护Manager
 * <p>
 * amb
 * </p>
 * <p>
 * 厦门安必兴信息科技有限公司
 * </p>
 * <p>
 * 功能说明：
 * </p>
 * 
 * @author LPF
 * @version 1.00 2016年9月26日 发布
 */
@Service
@Transactional
public class RelManager {
	@Autowired
	private RelDao relDao;

	public Rel getRel(Long id) {
		return relDao.get(id);
	}

	public void deleteRel(Rel Rel) {
		relDao.delete(Rel);
	}

	public Page<Rel> search(Page<Rel> page) {
		return relDao.search(page);
	}

	public List<Rel> listAll() {
		return relDao.getAllRel();
	}

	public void deleteRel(Long id) {
		relDao.delete(id);
	}

	public void deleteRel(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			Rel Rel = relDao.get(Long.valueOf(id));
			if (Rel.getId() != null) {
				relDao.delete(Rel);
			}
		}
	}

	public void saveRel(Rel Rel) {
		relDao.save(Rel);
	}

	public Page<Rel> listState(Page<Rel> page, String type, String code,
			String subName) {
		String hql = " from Rel s where hiddenState='N' ";
		List<Object> searchParams = new ArrayList<Object>();
		if (type != null) {
			if ("0".equals(type)) {
				type = GpAverageMaterial.STATE_SUBMIT;
			}
			if ("1".equals(type)) {
				type = GpAverageMaterial.STATE_PENDING;
			}
			if ("2".equals(type)) {
				type = GpAverageMaterial.STATE_QUALIFIED;
			}
			hql = hql + " and s.isHarmful=?";
			searchParams.add(type);
		}
		if (code != null) {
			hql = hql + " and s.supplierCode=?";
			searchParams.add(code);
		}
		if (subName != null) {
			hql = hql + " and s.factoryClassify=?";
			searchParams.add(subName);
		}
		return relDao.searchPageByHql(page, hql, searchParams.toArray());

	}

	public void harmful(String eid, String type) {
		String[] ids = eid.split(",");
		for (String id : ids) {
			Rel rel = relDao.get(Long.valueOf(id));
			if ("U".equals(type)) {
				rel.setIsHarmful(GpAverageMaterial.STATE_PENDING);
				rel.setTaskProgress(GpAverageMaterial.STATE_PENDING);
			} else if ("Y".equals(type)) {
				rel.setIsHarmful(GpAverageMaterial.STATE_QUALIFIED);
				rel.setTaskProgress(GpAverageMaterial.STATE_QUALIFIED);
			} else if ("N".equals(type)) {
				rel.setIsHarmful(GpAverageMaterial.STATE_SUBMIT);
				rel.setTaskProgress(GpAverageMaterial.STATE_SUBMIT);
			} else if ("O".equals(type)) {
				rel.setIsHarmful(GpAverageMaterial.STATE_OVERDUE);
				rel.setTaskProgress(GpAverageMaterial.STATE_OVERDUE);
			}
			if (!"未更新".equals(rel.getUpdateStatus())) {
				rel.setUpdateStatus("未更新");
			}
			relDao.save(rel);
		}
	}

	public void isHarmfulDate(String eid) {
		String[] ids = eid.split(",");
		for (String id : ids) {
			Rel rel = relDao.get(Long.valueOf(id));
			String auditMan = rel.getAuditMan();// 审核人
			String businessUnitName = rel.getBusinessUnitName();//厂区
			String processSection = rel.getProcessSection();//制程区段
			String customer = rel.getCustomer();//客户
			Date reportDate = rel.getReportDate();//日期
			String sampleItemCode=rel.getSampleItemCode();//样品代码
			String sampleCategory=rel.getSampleCategory();//样品类别
			String prototypePhase=rel.getPrototypePhase();//样品阶段
			String volumeNumOrConfiguration =rel.getVolumeNumOrConfiguration();//卷号或配置
			String testPurpose=rel.getVolumeNumOrConfiguration();//测试目的
			String attachment = rel.getAttachment();//附件
			String remark = rel.getRemark();//备注
			if ("".equals(auditMan) || auditMan == null) {
				throw new RuntimeException("审核人不能为空!");
			}
			if ("".equals(businessUnitName) || businessUnitName == null) {
				throw new RuntimeException("厂区不能为空!");
			}
			if ("".equals(processSection) || processSection == null) {
				throw new RuntimeException("制程区段不能为空!");
			}
			if ("".equals(customer) || customer == null) {
				throw new RuntimeException("客户不能为空!");
			}
			if ("".equals(sampleItemCode) || sampleItemCode == null) {
				throw new RuntimeException("样品代码不能为空!");
			}
			if ("".equals(sampleCategory) || sampleCategory == null) {
				throw new RuntimeException("样品类别不能为空!");
			}
			if ("".equals(prototypePhase) || prototypePhase == null) {
				throw new RuntimeException("样品阶段不能为空!");
			}
			if ("".equals(volumeNumOrConfiguration) || volumeNumOrConfiguration == null) {
				throw new RuntimeException("卷号或配置不能为空!");
			}
			if ("".equals(testPurpose) || testPurpose == null) {
				throw new RuntimeException("测试目的不能为空!");
			}
			if (attachment == null || "".equals(attachment)
					|| attachment.indexOf("|~|") < 0) {
				throw new RuntimeException("附件不能为空!");
			}
			if ("".equals(reportDate) || reportDate == null) {
				throw new RuntimeException("时间不能为空!");
			}
		
			if ("".equals(remark) || remark == null) {
				throw new RuntimeException("备注不能为空!");
			}

		}

	}
	public String importDatas(File file) throws Exception {
		StringBuffer sb = new StringBuffer("");
		// 表单字段
		Map<String, String> fieldMap = this
				.getFieldMap("EPM_REL");// MFG_IPQC_INSPECTION
		Workbook book = WorkbookFactory.create(new FileInputStream(file));
		Sheet sheet = book.getSheetAt(0);
		Row row = sheet.getRow(0);
		if (row == null) {
			throw new RuntimeException("第一行不能为空!");
		}
		Map<String, Integer> columnMap = new HashMap<String, Integer>();
		for (int i = 0;; i++) {
			Cell cell = row.getCell(i);
			if (cell == null) {
				break;
			}
			String value = cell.getStringCellValue();
			if (fieldMap.containsKey(value)) {
				columnMap.put(value, i);
			}
		}
		DecimalFormat df = new DecimalFormat("#.##");
		Iterator<Row> rows = sheet.rowIterator();
		rows.next();// 标题行
		int i = 0;
		while (rows.hasNext()) {
			row = rows.next();
			try {
				Map<String, Object> objMap = new HashMap<String, Object>();
				for (String columnName : columnMap.keySet()) {
					Cell cell = row.getCell(columnMap.get(columnName));
					if (cell != null) {
						Object value = null;
						if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
							value = cell.getStringCellValue();
						} else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								value = cell.getDateCellValue();
							} else {
								value = df.format(cell.getNumericCellValue());
							}
						} else if (Cell.CELL_TYPE_FORMULA == cell.getCellType()) {
							value = cell.getCellFormula();
						}
						objMap.put(fieldMap.get(columnName), value);
					}
				}
				Rel Rel = new Rel();
				Rel.setCompanyId(ContextUtils.getCompanyId());
				Rel.setCreatedTime(new Date());
				Rel.setCreator(ContextUtils.getUserName());
				Rel.setModifiedTime(new Date());
				Rel.setModifier(ContextUtils.getUserName());
				for (String key : objMap.keySet()) {
					CommonUtil1.setProperty(Rel, key, objMap.get(key));
				}
				relDao.save(Rel);
				sb.append("第" + (i + 1) + "行导入成功!<br/>");
			} catch (Exception e) {
				e.printStackTrace();
				sb.append("第" + (i + 1) + "行导入失败:<font color=red>"
						+ e.getMessage() + "</font><br/>");
			}
			i++;
		}
		file.delete();
		return sb.toString();
	}

	public Map<String, String> getFieldMap(String listCode) {
		Map<String, String> fieldMap = new HashMap<String, String>();
		ListView columns = ApiFactory.getMmsService().getListViewByCode(
				listCode);
		for (ListColumn column : columns.getColumns()) {
			if (column.getVisible()) {
				fieldMap.put(column.getHeaderName(), column.getTableColumn()
						.getName());
			}
		}
		return fieldMap;
	}

}
