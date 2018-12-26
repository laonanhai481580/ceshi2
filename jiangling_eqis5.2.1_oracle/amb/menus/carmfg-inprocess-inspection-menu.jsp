<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ambition.carmfg.entity.*,com.norteksoft.acs.base.web.struts2.Struts2Utils,java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>

 <script>
	$(function() {
		$( "#accordion1" ).accordion({
			animated:false,
			collapsible:false,
			event:'click',
			fillSpace:true,
			navigation: true }
		);
		if(accordionMenu=="input"){
			$("#accordion1").accordion({active:0});
		}else if(accordionMenu=="list"){
			$("#accordion1").accordion({active:1});
		}
		
	});
 </script>
 <div id="accordion1" class="basic">
	<c:forEach items="${listInspectionPoint}" var="inspectionPoint" varStatus="base">
		<c:if test="${base.first==true}">
			<c:set value="${inspectionPoint.inspectionPointName}" var="inspection"/> 
		</c:if>
	</c:forEach>
	<%-- <security:authorize ifAnyGranted="inspection-inprocess-inspection-list,inspection-daliy-report-list,inspection-daliy-report-list-list,qualityrate-trend,inspection-general-plato,check-inspection-list"> --%>
	<h3><a id="_input" href="#" onclick="_change_menu('1');">${workshop}检验信息录入</a></h3>
	<div>
		<div id="inputRecord" class="west-notree" url="${mfgctx}/inspection/inprocess-inspection/list.htm?workshop=${workshop}"  onclick="changeMenu(this);"><span>检验信息录入台账</span></div>
		<div id="dataAcquisition" class="west-notree" onclick="javascript:window.location=encodeURI('${carmfgctx}/data-acquisition/list.htm?workshop=${workshop}')"><span>检验信息录入台账</span></div>
	</div>
	<%-- </security:authorize> --%>
	<security:authorize ifAnyGranted="inspection-inprocess-inspection-list-all">
	<h3><a id="_list" href="#" onclick="_change_menu('2');">检验数据台帐</a></h3>
	<div>
		<c:if test="${accordionMenu=='list'}">
			<c:forEach items="${listInspectionPoint}" var="inspectionPoint" varStatus="base">
				<div id="${inspectionPoint.inspectionPointName}" class="west-notree" url='${mfgctx}/inspection/inprocess-inspection/list-all.htm?inspectionPoint=${inspectionPoint.inspectionPointName}&workshop=${inspectionPoint.workshop}<security:authorize ifAnyGranted="inspection-inprocess-inspection-save-remark">&showRemark=1</security:authorize>'  onclick="changeMenu(this);"><span>${inspectionPoint.inspectionPointName}台帐</span></div>
    		</c:forEach>
   		</c:if>
	</div>
	</security:authorize>
</div>
<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = encodeURI($(obj).attr('url'));
	}
	function _change_menu(type){
		if(type=='1'){
			url = '${mfgctx}/inspection/inprocess-inspection/list.htm?workshop=${workshop}';
		}else if(type=='2'){
			url = '${mfgctx}/inspection/inprocess-inspection/list-all.htm?workshop=${workshop}&inspectionPoint=${inspection}<security:authorize ifAnyGranted="iqc-save">&showRemark=1</security:authorize>';
		}
		window.location = encodeURI(url);
	}
</script>