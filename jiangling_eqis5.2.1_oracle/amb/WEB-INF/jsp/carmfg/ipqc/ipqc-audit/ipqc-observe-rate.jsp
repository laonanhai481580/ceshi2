<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/widgets/highcharts-4.0.3/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
	<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
	<script type="text/javascript" src="${ctx}/js/chart-view1.0.js"></script>
	<script type="text/javascript" src="${ctx}/js/chart-view-search-format1.0.js"></script>	
		<script type="text/javascript">
	var chart = null,searchParams = null,cacheResult = null;
	var multiselectIds = ['businessUnit'];
	window.chartView.initInputEditType = function(){
		$(":input[isDate]").each(function(index,obj){
			var formatStr = $(obj).attr("dateFormat");
			if(!formatStr){
				formatStr = "yy-mm-dd";
			}
			$(obj).datepicker({changeYear:true,changeMonth:true,showButtonPanel: true,dateFormat:formatStr});
		});
		$(".label").each(function(index,obj){
			var labelName = obj.innerHTML;									
			if("工厂"==labelName){
				var url = "${mfgctx}/base-info/factory-procedure/factory-select.htm";
				$.post(encodeURI(url),{},function(result){
		 			if(result.error){
		 				alert(result.message);
		 			}else{
	 					var factorys = result.factorys;
	 					var factoryArr = factorys.split(",");
 						var selectFactory = obj.nextElementSibling;
 						selectFactory.options.length=0;
 						var opp1 = new Option("","");
 						selectFactory.add(opp1);
 		 				for(var i=0;i<factoryArr.length;i++){
 		 					var opp = new Option(factoryArr[i],factoryArr[i]);
 		 					selectFactory.add(opp);
 		 				}
		 			}
		 		},'json');
				$(obj.nextElementSibling).change(function(){
					var factory = obj.nextElementSibling.value;
					var url = "${mfgctx}/base-info/factory-procedure/procedure-select.htm?factory="+factory;
					$.post(encodeURI(url),{},function(result){
			 			if(result.error){
			 				alert(result.message);
			 			}else{
			 				$(".label").each(function(index,obj2){
			 					var labelName2 = obj2.innerHTML;
			 					var procedures = result.procedures;
			 					var procedureArr = procedures.split(",");
			 					if("工序"==labelName2){
			 						var selectProcedure = obj2.nextElementSibling;
			 						selectProcedure.options.length=0;
			 						var opp1 = new Option("","");
			 						selectProcedure.add(opp1);
			 		 				for(var i=0;i<procedureArr.length;i++){
			 		 					var opp = new Option(procedureArr[i],procedureArr[i]);
			 		 					selectProcedure.add(opp);
			 		 				}
			 					}  
			 				});
			 			}
			 		},'json');
				});
			}
			if("工序"==labelName){
				var url = "${mfgctx}/base-info/factory-procedure/procedure-select.htm";
				$.post(encodeURI(url),{},function(result){
		 			if(result.error){
		 				alert(result.message);
		 			}else{
	 					var procedures = result.procedures;
	 					var procedureArr = procedures.split(",");
 						var selectProcedure = obj.nextElementSibling;
 						selectProcedure.options.length=0;
 						var opp1 = new Option("","");
 						selectProcedure.add(opp1);
 		 				for(var i=0;i<procedureArr.length;i++){
 		 					var opp = new Option(procedureArr[i],procedureArr[i]);
 		 					selectProcedure.add(opp);
 		 				}
		 			}
		 		},'json');
			}
		});
	};
	</script>
</head>
<!-- IPQC遵守度-->
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<chart:view 
		chartDefinitionCode="mfg-ipqc-observe-rate"
		secMenu="ipqc_list"
		secMenuJspPath="/menus/manufacture-sec-menu.jsp"
		thirdMenu="_ipqc_observe"
		thirdMenuJspPath="/menus/manufacture-ipqc-menu.jsp"/>
</body>
</html>	

