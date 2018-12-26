package com.ambition.carmfg.spccontrol.web;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.entity.MfgItemIndicator;
import com.ambition.carmfg.inspectionbase.service.MfgItemIndicatorManager;
import com.ambition.product.BaseAction;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.conversion.annotations.Conversion;

@Namespace("/carmfg/spc-control")
@ParentPackage("default")
@Results({@Result(name = CrudActionSupport.RELOAD, location = "carmfg/spc-control", type = "redirectAction")})
@Conversion
public class SpcControlAction
    extends BaseAction<MfgItemIndicator>
{
	private Logger log = Logger.getLogger(this.getClass());
    private static final long serialVersionUID = 1L;
    private Page<MfgItemIndicator> page;
    private String deleteIds;// 删除的ids
    private MfgItemIndicator itemIndicator;
    @Autowired
    private MfgItemIndicatorManager itemIndicatorManager;
    public Page<MfgItemIndicator> getPage() {
		return page;
	}

	public void setPage(Page<MfgItemIndicator> page) {
		this.page = page;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public MfgItemIndicator getItemIndicator() {
		return itemIndicator;
	}

	public void setItemIndicator(MfgItemIndicator itemIndicator) {
		this.itemIndicator = itemIndicator;
	}

	@Override
    public MfgItemIndicator getModel()
    {
        return itemIndicator;
    }

    @Override
    protected void prepareModel()
        throws Exception
    {
        
    }

    @Override
    public String input()
        throws Exception
    {
        return SUCCESS;
    }

    @Action("delete")
    @Override
    public String delete()
        throws Exception
    {
        try
        {
        }
        catch(Exception e)
        {
            log.error("删除失败!",e);
            renderText("删除失败:"+e.getMessage());
        }
        return null;
    }

    @Action("list")
    @Override
    public String list()
        throws Exception
    {
    	renderMenu();
    	try {
    		itemIndicatorManager.clearDeleteBindFeature();
		} catch (Exception e) {
			log.error("控制工艺点查询失败!",e);
		}
        return SUCCESS;
    }

    @Action("list-datas")
    public String getListDatas()
        throws Exception
    {
        try
        {
        	page = itemIndicatorManager.searchFeatures(page);
            this.renderText(PageUtils.pageToJson(page));
        }
        catch(Exception e)
        {
            log.error("工艺控制点查询失败!",e);
        }
        return null;
    }


    /**
     * 导出
     * 
     * @return
     * @throws Exception
     */
    @Action("exports")
    public String exports()
        throws Exception
    {
    	page = new Page<MfgItemIndicator>(Integer.MAX_VALUE);
        page = itemIndicatorManager.searchFeatures(page);
        this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page, "MFG_ITEM_INDICATOR"), "过程检验工艺控制点"));
        return null;
    }

	@Override
	public String save() throws Exception {
		return null;
	}
}
