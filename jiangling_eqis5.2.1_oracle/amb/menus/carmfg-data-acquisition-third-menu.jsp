<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script>
	$(function() {
		$("#accordion1" ).accordion({
			animated:false,
			collapsible:false,
			event:'click',
			fillSpace:true 
		});
	});
</script>
<div id="accordion1" class="basic">
	<security:authorize ifAnyGranted="carmfg-dataAcquisition-list,mfg-inspection-daliyreport">
		<h3>
			<c:set var="iscontain" value="false" />
			<s:if test="inspectionPointList.size>0||daliyReportPointList.size==0">
           		<a id="_get_datas" onclick="javascript:window.location='${carmfgctx}/data-acquisition/list.htm?workshop=${workshop}'">数据采集</a></h3>
           	</s:if>
           	<s:else>
           		<a id="_get_datas" onclick="javascript:window.location='${carmfgctx}/inspection/inprocess-inspection/list.htm?workshop=${workshop}'">数据采集</a></h3>
           	</s:else>
			<div style="padding:0px;">
				<s:if test="inspectionPointList.size>0">
      			<div id="dataAcquisition" class="west-notree" onclick="javascript:window.location=encodeURI('${carmfgctx}/data-acquisition/list.htm?workshop=${workshop}')"><span>数据采集</span></div>
      			</s:if>
				<s:if test="daliyReportPointList.size>0">
	            	<div id="inputRecord" class="west-notree"  onclick="javascript:window.location=encodeURI('${carmfgctx}/inspection/inprocess-inspection/list.htm?workshop=${workshop}')" ><span>检验信息录入台账</span></div>
	    		</s:if> 
			</div>
	</security:authorize>
	<s:if test="daliyReportProductLines.size>0"> 
	<security:authorize ifAnyGranted="mfg-daliy-inspection-all">
		<h3><a id="_get_datas_all" onclick="javascript:window.location=encodeURI('${carmfgctx}/inspection/inprocess-inspection/list-all.htm?workshop=${workshop}&productionLine=${daliyReportProductLines[0]}')">检验信息汇总台账</a></h3>
		<div style="padding:0px;">
			<c:forEach items="${daliyReportProductLines}" var="_productionLine" varStatus="base">
				<div id="${_productionLine}_a" class="west-notree" url='${carmfgctx}/inspection/inprocess-inspection/list-all.htm?productionLine=${_productionLine}&workshop=${workshop}<security:authorize ifAnyGranted="inspection-inprocess-inspection-save-remark">&showRemark=1</security:authorize>'  onclick="changeMenu(this);"><span>${_productionLine}数据台帐</span></div>
    		</c:forEach>
   		</div>
	</security:authorize>
	</s:if>
	<s:if test="inspectionPointList.size>0">
	<security:authorize ifAnyGranted="carmfg-unquality-responsiblity,mfg-defective-items-total-other">
		<h3><a id="_get_datas_items" onclick="javascript:window.location=encodeURI('<grid:authorize code="carmfg-unquality-responsiblity,mfg-defective-items-total-other" systemCode="carmfg"></grid:authorize>?workshop=${workshop}')">不良责任鉴定</a></h3>
		<div style="padding:0px;">
			<security:authorize ifAnyGranted="carmfg-unquality-responsiblity">
			<div id="defectiveItems" class="west-notree" onclick="javascript:window.location=encodeURI('${carmfgctx}/data-acquisition/data-acquisition-analyse/defective-items-workshop.htm?workshop=${workshop}')"><span>不良责任鉴定</span></div>
			</security:authorize>
			<security:authorize ifAnyGranted="mfg-defective-items-total-other">
			<div id="defectiveItemsTotal" class="west-notree" onclick="javascript:window.location=encodeURI('${carmfgctx}/data-acquisition/data-acquisition-analyse/defective-items-total-other.htm?workshop=${workshop}')"><span>数据采集总台账</span></div>
			</security:authorize>
		</div>
	</security:authorize>
	</s:if>
	<s:if test="%{#hasInspectionReport||#hasDaliyReport}">
	<security:authorize ifAnyGranted="carmfg-dataAcquisition-quality-rate-trend,carmfg-dataAcquisition-unquality-amount-trend,carmfg-dataAcquisition-general-plato,carmfg-dataAcquisition-unquality-top10,carmfg-dataAcquisition-qualityrate-trend,carmfg-dataAcquisition-daliy-general-plato">
		<s:if test="hasInspectionReport">
			<h3><a id="_data_analyse" onclick="javascript:window.location=encodeURI('<grid:authorize code="carmfg-dataAcquisition-quality-rate-trend,carmfg-dataAcquisition-unquality-amount-trend,carmfg-dataAcquisition-general-plato,carmfg-dataAcquisition-unquality-top10" systemCode="carmfg"></grid:authorize>?workshop=${workshop}')">统计分析</a></h3>
		</s:if>
		<s:else>
			<h3><a id="_data_analyse" onclick="javascript:window.location=encodeURI('<grid:authorize code="carmfg-dataAcquisition-qualityrate-trend,carmfg-dataAcquisition-daliy-general-plato" systemCode="carmfg"></grid:authorize>?workshop=${workshop}')">统计分析</a></h3>
		</s:else>
		<div style="padding:0px;">
			<s:if test="hasInspectionReport">
			<security:authorize ifAnyGranted="carmfg-dataAcquisition-quality-rate-trend">
				<div id="qualityRateTrend" class="west-notree" url='${carmfgctx}/data-acquisition/data-acquisition-analyse/quality-rate-trend.htm?workshop=${workshop}' onclick="changeMenu(this);"><span>一次合格率推移图</span></div>
			</security:authorize>
			<security:authorize ifAnyGranted="carmfg-dataAcquisition-unquality-amount-trend">
				<div id="unqualityAmountTrend" class="west-notree" url='${carmfgctx}/data-acquisition/data-acquisition-analyse/unquality-amount-trend.htm?workshop=${workshop}' onclick="changeMenu(this);"><span>千车缺陷数推移图</span></div>
			</security:authorize>
			<security:authorize ifAnyGranted="carmfg-dataAcquisition-general-plato">
				<div id="generalPlato" class="west-notree" url='${carmfgctx}/data-acquisition/data-acquisition-analyse/general-plato.htm?workshop=${workshop}' onclick="changeMenu(this);"><span>不良柏拉图分析</span></div>
			</security:authorize>
			<security:authorize ifAnyGranted="carmfg-dataAcquisition-unquality-top10">
				<c:if test="${workshop!='整车检验' }">
					<div id="unqualityTop10" class="west-notree" url='${carmfgctx}/data-acquisition/data-acquisition-analyse/unquality-top10.htm?workshop=${workshop}' onclick="changeMenu(this);"><span>不良TOP10对比</span></div>
				</c:if>
			</security:authorize>
			</s:if>
			<s:if test="hasDaliyReport">
			<security:authorize ifAnyGranted="carmfg-dataAcquisition-qualityrate-trend">
				<div id="_qualityrate_trend" class="west-notree" url='${carmfgctx}/inspection/inprocess-inspection/qualityrate-trend.htm?workshop=${workshop}' onclick="changeMenu(this);"><span>批次合格率趋势图</span></div>
			</security:authorize>
			<security:authorize ifAnyGranted="carmfg-dataAcquisition-daliy-general-plato">
				<div id="_unqualityrate_trend" class="west-notree" url='${carmfgctx}/inspection/inprocess-inspection/general-plato.htm?workshop=${workshop}' onclick="changeMenu(this);"><span>不合格项柏拉图</span></div>
			</security:authorize>
			</s:if>
		</div>
	</security:authorize>
	</s:if>
</div>
<script type="text/javascript" class="source">
	$(function () {
		var indexObj = {};
		$("#accordion1").find("h3 a[id]").each(function(num,obj){
			indexObj[obj.id] = num;
		});
		$('#'+thirdMenu).addClass('west-notree-selected');
		if(thirdMenu=="dataAcquisition" || thirdMenu=="inputRecord"){
			$("#accordion1").accordion({active:indexObj['_get_datas']});
		}else if(thirdMenu=="defectiveItems" || thirdMenu=="defectiveItemsTotal"){
			$("#accordion1").accordion({active:indexObj['_get_datas_items']});
		}else if(thirdMenu=="qualityRateTrend" || thirdMenu=="unqualityAmountTrend"|| thirdMenu=="generalPlato"|| thirdMenu=="unqualityTop10"|| thirdMenu=="_qualityrate_trend"|| thirdMenu=="_unqualityrate_trend"){
			$("#accordion1").accordion({active:indexObj['_data_analyse']});
		}else{
			$("#accordion1").accordion({active:indexObj['_get_datas_all']});
		}
	});
	function changeMenu(obj){
		window.location = encodeURI($(obj).attr('url'));
	}
</script>