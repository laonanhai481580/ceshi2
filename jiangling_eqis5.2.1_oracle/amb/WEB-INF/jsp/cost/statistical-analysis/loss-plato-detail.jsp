<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
		function contentResize(){
			$("#grid").jqGrid("setGridHeight",$(window).height()-74);
			$("#grid").jqGrid("setGridWidth",$(window).width()-25);
		}
		//重写(在请求中附加额外的参数)
		function $getExtraParams(){
			return {searchStrs:'${params}'};
		}
		function inputLinkClick(cellvalue, options, rowObj){
			if(cellvalue&&cellvalue != '&nbsp;'&&(rowObj.formType=='不合格处理'||rowObj.formType=='客诉费用')){
				return "<a href='javascript:void(0);showViewInfo(\""+cellvalue+"\",\""+rowObj.formType+"\");'>"+cellvalue+"</a>";
			}else{
				return cellvalue;
			}
		}
		function showViewInfo(formNo,formType){
			var url='${mfgctx}/defective-goods/ledger/view-info.htm?formNo=' + formNo;
			if(formType == '客诉费用'){
				url = '${amsctx}/feesapply/view-info.htm?no='+formNo;
			}
			window.location.href = url;
		}
	</script>
</head>
<%
	/**JSONObject params = (JSONObject)ActionContext.getContext().getValueStack().findValue("params");
	String url = "";
	for(Object pro : params.keySet()){
		if(url.length()>0){
			url += "&";
		}
	}
	String url = "occurring_month_ge=" + params.getInt("inspectionDate") + "&occurring_month_le=" + params.getInt("inspectionDate");
	if(params.containsKey("businessUnitCode")){
		url += "&businessUnitCode=" + params.getString("businessUnitCode");
	}
	ActionContext.getContext().put("searchUrl",url);*/
%>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div style="margin:6px auto;text-align:center;font-size:16px;font-weight:bold;" id='titleDiv'>
			</div>
			<div id="opt-content">
				<grid:jqGrid gridId="grid"
					url="${costctx}/statistical-analysis/loss-plato-detail-datas.htm"
					code="Cost_VIEW_RECORD" pageName="costViewRecordPage"></grid:jqGrid>
			</div>
		</div>
	</div>
</body>
</html>