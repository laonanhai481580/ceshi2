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
			if("区段名称"==labelName){
				$(obj.nextElementSibling).change(function(){
					var process = obj.nextElementSibling.value;
					var url = "${mfgctx}/common/select-by-process-section.htm?process="+process;
					$.post(encodeURI(url),{},function(result){
			 			if(result.error){
			 				alert(result.message);
			 			}else{
			 				$(".label").each(function(index,obj2){
			 					var labelName2 = obj2.innerHTML;
			 					var processName = result.processName;
			 					var productLine = result.productLine;
			 					var processArr = processName.split(",");
			 					var productLineArr = productLine.split(",");
			 					if("工序"==labelName2){
			 						var selectProcessName = obj2.nextElementSibling;
			 						selectProcessName.options.length=0;
			 						var opp1 = new Option("","");
			 						selectProcessName.add(opp1);
			 		 				for(var i=0;i<processArr.length;i++){
			 		 					var opp = new Option(processArr[i],processArr[i]);
			 		 					selectProcessName.add(opp);
			 		 				}
			 					}  
			 					if("生产线"==labelName2){
			 						var selectProcessName = obj2.nextElementSibling;
			 						selectProcessName.options.length=0;
			 						var opp1 = new Option("","");
			 						selectProcessName.add(opp1);
			 		 				for(var i=0;i<productLineArr.length;i++){
			 		 					var opp = new Option(productLineArr[i],productLineArr[i]);
			 		 					selectProcessName.add(opp);
			 		 				}
			 					}  
			 				});
			 			}
			 		},'json');
				});
			}
		});
	};
	function addSelect(obj){
		$(".label").each(function(index,obj2){
				var labelName2 = obj2.innerHTML;
				var processName = obj.processName;
				var processArr = processName.split(",");
				if("工序"==labelName2){
					var selectProcessName = obj2.nextElementSibling;
					var opp1 = new Option("","");
					selectProcessName.add(opp1);
	 				var names = processArr.split(',');
	 				for(var i=0;i<names.length;i++){
	 					var opp = new Option(names[i],names[i]);
	 					selectProcessName.add(opp);
	 				}
				}  
			});
	}
	</script>
</head>
<!-- IPQC不良项目柏拉图-->
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<chart:view 
		chartDefinitionCode="mfg-ipqc-sampling-bad"
		secMenu="analyse"
		secMenuJspPath="/menus/manufacture-sec-menu.jsp"
		thirdMenu="_ipqc_sampling_bad"
		thirdMenuJspPath="/menus/manufacture-data-acquisition-menu.jsp"/>
</body>
</html>	

