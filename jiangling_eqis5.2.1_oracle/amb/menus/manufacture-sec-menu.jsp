<%@page import="com.ambition.carmfg.entity.InspectionPoint"%>
<%@page import="com.ambition.carmfg.entity.InspectionPointTypeEnum"%>
<%@page import="java.util.*"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
	<security:authorize ifAnyGranted="inspection-made-inspection-input,made-inspection-first-list,made-inspection-patrol-list,made-inspection-end-list">
		<li  id="made_inspection">
			<span><span><a href='<grid:authorize code="inspection-made-inspection-input,made-inspection-first-list,made-inspection-patrol-list,made-inspection-end-list" systemCode="carmfg"></grid:authorize>'>制程检验</a>
			</span></span>
		</li>
	</security:authorize>
	<security:authorize ifAnyGranted="termination-inspection-first-record-list,termination-inspection-patrol-record-list,termination-inspection-end-record-list,termination-inspection-un-record-list">
		<li  id="data_list">
			<span><span>
				<a href='<grid:authorize code="termination-inspection-first-record-list,termination-inspection-patrol-record-list,termination-inspection-end-record-list,termination-inspection-un-record-list" systemCode="carmfg"></grid:authorize>'>已完成检验台帐</a>
			</span></span>
		</li>
	</security:authorize>
	<security:authorize ifAnyGranted="bom-traceability-list,product-traceability-list">
		<li  id="traceability_list">
			<span><span>
				<a href='<grid:authorize code="bom-traceability-list,product-traceability-list" systemCode="carmfg"></grid:authorize>'>追溯管理</a>
			</span></span>
		</li>
	</security:authorize>
<%-- 	<security:authorize ifAnyGranted="MFG_ORT_PLAN_LIST">
		<li  id="ort_list">
		  <span><span>
		 <a href='<grid:authorize code="MFG_ORT_PLAN_LIST" systemCode="carmfg"></grid:authorize>'>ORT检验管理</a>
			</span></span>
		</li>
	</security:authorize>	
	 --%>                             
	<security:authorize ifAnyGranted="MFG_OQC,MFG_OQC_INSPECTION_LIST,MFG_OQC_DELIVER_LIST">
		<li  id="oqc_list">
			<span><span>
				<a href='<grid:authorize code="MFG_OQC,MFG_OQC_INSPECTION_LIST,MFG_OQC_DELIVER_LIST" systemCode="carmfg"></grid:authorize>'>OQC检验管理</a>
			</span></span>
		</li>
	</security:authorize>	
	
	
	<security:authorize ifAnyGranted="MFG_IPQC_AUDIT_LIST">
		<li  id="ipqc_list">
			<span><span>
				<a href='<grid:authorize code="MFG_IPQC_AUDIT_LIST" systemCode="carmfg"></grid:authorize>'>IPQC稽核</a>
			</span></span>
		</li>
	</security:authorize>
	
   <security:authorize ifAnyGranted="MFG_IPQC_INSPECTION_MANAGEMENT_LIST,MFG_IPQC_INSPECTION_LIST,MFG_IPQC_INSPECTION_LIST_PENDING,MFG_IPQC_INSPECTION_LIST_OK">   
	<li  id="ipqc_inspection_list">
			<span><span>  <!-- MFG_IPQC_INSPECTION_LIST ,-->                  
				<a href='<grid:authorize code="MFG_IPQC_INSPECTION_MANAGEMENT_LIST,MFG_IPQC_INSPECTION_LIST,MFG_IPQC_INSPECTION_LIST_PENDING,MFG_IPQC_INSPECTION_LIST_OK" systemCode="carmfg"></grid:authorize>'>IPQC检验管理</a>
			</span></span>
	 </li>
	</security:authorize>
	<security:authorize ifAnyGranted="MFG_OQC_INSPECTION_LEDGER,
MFG_OQC_INSPECTION_LIST_OK,MFG_OQC_INSPECTION_REPORT_LIST,MFG_OQC_INSPECTION_LIST_PENDING,
MFG_OQC_VISUAL_INSPECTION_REPORT_LIST,MFG_OQC_VISUAL_INSPECTION_LIST_PENDING,MFG_OQC_VISUAL_INSPECTION_LIST_OK,
MFG_OQC_OTHER_INSPECTION_REPORT_LIST,MFG_OQC_OTHER_INSPECTION_LIST_PENDING,MFG_OQC_OTHER_INSPECTION_LIST_OK">     
	<li id="oqc_visual_list">
			<span><span>                
				<a href='<grid:authorize code="MFG_OQC_INSPECTION_LEDGER,
MFG_OQC_INSPECTION_LIST_OK,MFG_OQC_INSPECTION_REPORT_LIST,MFG_OQC_INSPECTION_LIST_PENDING,
MFG_OQC_VISUAL_INSPECTION_REPORT_LIST,MFG_OQC_VISUAL_INSPECTION_LIST_PENDING,MFG_OQC_VISUAL_INSPECTION_LIST_OK,
MFG_OQC_OTHER_INSPECTION_REPORT_LIST,MFG_OQC_OTHER_INSPECTION_LIST_PENDING,MFG_OQC_OTHER_INSPECTION_LIST_OK" systemCode="carmfg"></grid:authorize>'>OQC检验管理台账</a>
			</span></span>
	 </li>
	</security:authorize>
	
	
	
	<security:authorize ifAnyGranted="MFG_ANALYSE,MFG_YIELD_LIST,MFG_IPQC_SAMPLING_BAD_LIST,MFG_IPQC_BATCH_PASS_LIST,MFG_IPQC_LOSS_LIST,MFG_IPQC_CLOSE_LIST,MFG_OQC_SAMPLING_PASS_LIST,MFG_OQC_SAMPLING_BAD_LIST,MFG_OQC_PASS_LIST">
		<li  id="analyse">
			<span><span>
				<a href='<grid:authorize code="MFG_ANALYSE,MFG_YIELD_LIST,MFG_IPQC_SAMPLING_BAD_LIST,MFG_IPQC_BATCH_PASS_LIST,MFG_IPQC_LOSS_LIST,MFG_IPQC_CLOSE_LIST,MFG_OQC_SAMPLING_PASS_LIST,MFG_OQC_SAMPLING_BAD_LIST,MFG_OQC_PASS_LIST" systemCode="carmfg"></grid:authorize>'>统计分析</a>
			</span></span>
		</li>
	</security:authorize>
<%-- 	<security:authorize ifAnyGranted="mfg-manu-analyse-regular-right,mfg-manu-analyse-qualityrate-contrast,mfg-manu-analyse-general-plato,mfg-manu-analyse-unquality-trend,mfg-manu-analyse-qualityrate-top,mfg-spc-control-list"> --%>
<%-- 	<li  id="statAnalyse"><span><span><a href="<grid:authorize code="mfg-manu-analyse-regular-right,mfg-manu-analyse-qualityrate-contrast,mfg-manu-analyse-general-plato,mfg-manu-analyse-unquality-trend,mfg-manu-analyse-qualityrate-top,mfg-spc-control-list" systemCode="carmfg"></grid:authorize>">工序/成品质量统计分析</a></span></span></li> --%>
<%-- 	</security:authorize> --%>
	<security:authorize ifAnyGranted="MFG_DEFECTIVE-GOODS_LIST_LIST,MFG_DEFECTIVE-GOODS_FORM_FORM,MFG_DEFECTIVE-GOODS_MONITOR-LIST_LIST,MFG_MANU-ANALYSE_DEFECTIVE-GOODS-REPORT_PAGE,MFG_DEFECTIVE-GOODS_AMOUNT_PAGE,MFG_DEFECTIVE-GOODS_INTERNAL-TOTAL_PAGE">
	<li  id="regectManager"><span><span><a href="<grid:authorize code="MFG_DEFECTIVE-GOODS_LIST_LIST,MFG_DEFECTIVE-GOODS_FORM_FORM,MFG_DEFECTIVE-GOODS_MONITOR-LIST_LIST,MFG_MANU-ANALYSE_DEFECTIVE-GOODS-REPORT_PAGE,MFG_DEFECTIVE-GOODS_AMOUNT_PAGE,MFG_DEFECTIVE-GOODS_INTERNAL-TOTAL_PAGE" systemCode="carmfg"></grid:authorize>">不合格品处理</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="mfg_base_info_bom_list,carmfg-baseInfo-procedure-defection-list,carmfg-baseInfo-defection,carmfg-baseInfo-defection-attribute,carmfg-baseInfo-inspection-base-indicator-list,carmfg-baseInfo-inspection-base-item-list,carmfg-plant-parameter-list,carmfg-ort-inspection-list,carmfg-ipqc-warming-list">
	<li  id="baseInfo" class="last"><span><span><a href="<grid:authorize code="mfg_base_info_bom_list,carmfg-baseInfo-procedure-defection-list,carmfg-baseInfo-defection,carmfg-baseInfo-defection-attribute,carmfg-baseInfo-inspection-base-indicator-list,carmfg-baseInfo-inspection-base-item-list,carmfg-plant-parameter-list,carmfg-ort-inspection-list,carmfg-ipqc-warming-list" systemCode="carmfg"></grid:authorize>">基础设置</a></span></span></li>
	</security:authorize>
</ul>
<div class="hid-header" onclick=headerChange(this); title="折叠/展开"></div>
<script>
	var topMenu='sysInprocess';
	$('#'+secMenu).addClass('sec-selected');
	function changeSecMenu(url){
		window.location = encodeURI(url);
	}
</script>


