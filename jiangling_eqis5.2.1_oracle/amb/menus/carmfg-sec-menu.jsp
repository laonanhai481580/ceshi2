<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
	<security:authorize ifAnyGranted="carmfg-general-plato,carmfg-unquality-trend,carmfg-incomingCarInfo-list,carmfg-audit-general-plato,carmfg-audit-deduction-trend,carmfg-defective-liability-identify,carmfg-defective-liability-search,carmfg-defective-liability-count,carmfg-in-part-defective-item,carmfg-part-defective-item,carmfg-part-eliminate,carmfg-part-use,carmfg-defective-liability-items-total,defective-items-total">
		<li id="statAnalyse"><span><span><a href="<grid:authorize code="carmfg-general-plato,carmfg-unquality-trend,carmfg-incomingCarInfo-list,carmfg-audit-general-plato,carmfg-audit-deduction-trend,carmfg-defective-liability-identify,carmfg-defective-liability-search,carmfg-defective-liability-count,carmfg-in-part-defective-item,carmfg-part-defective-item,carmfg-part-eliminate,carmfg-part-use,carmfg-defective-liability-items-total,defective-items-total" systemCode="carmfg"></grid:authorize>">质量综合分析</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="carmfg-dataAcquisition-list,defective-items-total-other,carmfg-cy-check-in-list,mfg-daliy-inspection-all,carmfg-dataAcquisition-quality-rate-trend,carmfg-dataAcquisition-unquality-amount-trend,carmfg-dataAcquisition-general-plato,carmfg-dataAcquisition-unquality-top10,inspection-inprocess-inspection-list,carmfg-defective-liability-search">
		<c:forEach items="${listOption}" var="options" varStatus="base">
		 	<li id="${options.name}">
		 		<c:set var="iscontain" value="false" /> 
				<c:forEach items="${listType}" var="li"> 
               		<c:if test="${li==options.name}">       
   					 	<c:set var="iscontain" value="true" />    
 					</c:if>   
    			</c:forEach>  
    			<c:if test="${iscontain}">  
      				<span><span><a href="#" onclick="changeSecMenu('${carmfgctx}/inspection/inprocess-inspection/list.htm?workshop=${options.name}');">${options.name}</a></span></span></li>
      			</c:if>  
    			<c:if test="${!iscontain}">  
            		<span><span><a href="#" onclick="changeSecMenu('${carmfgctx}/data-acquisition/list.htm?workshop=${options.name}');">${options.name}</a></span></span></li>
    			</c:if>  
    	</c:forEach>
    </security:authorize>
	<security:authorize ifAnyGranted="carmfg-fpa-input,carmfg-fpa-list,carmfg-fpa-analysis">
		<li id="fpa"><span><span><a href="<grid:authorize code="carmfg-fpa-input,carmfg-fpa-list,carmfg-fpa-analysis" systemCode="carmfg"></grid:authorize>">FPA评价</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="carmfg-defective-goods,carmfg-defective-goods-packing,carmfg-defective-goods-report,carmfg-defective-goods-amount,carmfg-defective-vehicle-parts">
		<li id="regectManager"><span><span><a href="<grid:authorize code="carmfg-defective-goods,carmfg-defective-goods-packing,carmfg-defective-goods-report,carmfg-defective-goods-amount,carmfg-defective-vehicle-parts" systemCode="carmfg"></grid:authorize>">不合格品处理</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="carmfg-baseInfo-bom,carmfg-baseInfo-inspection-point,carmfg-baseInfo-procedure-defection,carmfg-baseInfo-position,carmfg-baseInfo-direction,carmfg-baseInfo-defection,carmfg-baseInfo-defection-attribute,carmfg-baseInfo-code-rule,carmfg-baseInfo-qualified-rate,mfg-fpa-type-list,mfg-fpa-level-list,targetLine-listEditable,mfg-base-fpa-goalline">
		<li id="baseInfo" class="last"><span><span><a href="<grid:authorize code="carmfg-baseInfo-bom,carmfg-baseInfo-inspection-point,carmfg-baseInfo-procedure-defection,carmfg-baseInfo-position,carmfg-baseInfo-direction,carmfg-baseInfo-defection,carmfg-baseInfo-defection-attribute,carmfg-baseInfo-code-rule,carmfg-baseInfo-qualified-rate,mfg-fpa-type-list,mfg-fpa-level-list,targetLine-listEditable,mfg-base-fpa-goalline" systemCode="carmfg"></grid:authorize>">基础维护</a></span></span></li>
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


