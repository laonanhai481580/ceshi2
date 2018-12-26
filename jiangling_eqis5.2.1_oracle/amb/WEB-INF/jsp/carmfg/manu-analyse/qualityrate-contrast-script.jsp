<%@page import="com.ambition.carmfg.entity.InspectionPointTypeEnum"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<link href="${ctx}/widgets/multiselect/jquery.multiselect.css" rel="stylesheet" type="text/css"/>
<script src="${ctx}/widgets/multiselect/jquery.multiselect.js" type="text/javascript"></script>
<script src="${ctx}/widgets/multiselect/jquery.multiselect.zh-cn.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
<style>
<!--
	#searchUl{
		margin:0px;
		padding:0px;
	}
	#searchUl li{
		margin-top:4px;
		float:left;
		width:263px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#searchUl li select{
		width:178px;
	}
	.input{
		width:170px;
	}
	.label{
		float:left;
		width:70px;
		text-align:right;
		padding-right:2px;
	}
	#groupUl{
		margin:0px;
		padding:0px;
	}
	#groupUl li{
		float:left;
		width:95px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#groupUl li.last{
		padding:0px;
		width:280px;
		margin-bottom:2px;
		text-align:right;
	}
-->
</style>
<script type="text/javascript">
		function contentResize(){
			if(cacheResult != null){
				clearTable();
				createReport(cacheResult);
				createDetailTable(cacheResult);
			}
		}
		var chart = null,cacheResult = null;
		var multiselectIds = ['_businessUnit','_section','_productionLine','_workgroup'];
		$(document).ready(function(){
			$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
			$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
			for(var i=0;i<multiselectIds.length;i++){
				$("#" + multiselectIds[i]).multiselect({selectedList:2});
			}
			$("#check_select").each(function(index,obj){
				$($(obj).find("option")[0]).html("全选");
				if($(obj).find("option").length == 1){
					$(obj).width(100);
				}
			});
			$("#inspection_select").each(function(index,obj){
				$($(obj).find("option")[0]).html("全选");
				if($(obj).find("option").length == 1){
					$(obj).width(100);
				}
			});
			search();
		});
		function createReport(result){
			
			if(chart != null){
				chart.destroy();
				chart = null;
			}
			var width = $("#btnDiv").width()-10;
			var size = result.tableHeaderList.length;
			chart = new Highcharts.Chart({
				colors: ["#E0B56C", "#DF5353", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee",
				 		"#55BF3B", "#DF5353", "#7798BF", "#aaeeee"],
				exporting : {
					enabled : false
				},
				chart: {
					renderTo: "reportDiv",
					width : width,
					type : 'line'
				},
				credits: {
			         enabled: false
				},
				title: {
					style : {
						"font-weight":'bold',
						"color": '#3E576F',
						"font-size": '20px'
					},
					text: result.title
				},
				subtitle: {
					text: result.subtitle,
					y:33,
					x:0//center
				},
				xAxis: {
					categories: result.categories,
					gridLineDashStyle : 'ShortDashDot',
					gridLineWidth: 1
				},
				yAxis: [{
					title: {
						text: ''
					},
			        gridLineWidth : 0,
// 			        tickInterval : 10,
					labels : {
						formatter : function(){
							if(this.value>100||this.value<0){
								return "";
							}else{
								return this.value + '%';
							}
						}
					}
				}],
				legend: {
					 layout : 'vertical',
			         align: 'right',
			         verticalAlign : 'middle',
			         backgroundColor: '#FFFFFF',
			         x : 6,
			         y : 0
			    },
				plotOptions: {
			        line : {
			            shadow : false,
			            dataLabels: {
			               enabled: true,
			               formatter : function(){
			            	   return this.y + '%';
			               }
			            }
			        }
			    },
				tooltip: {
					formatter: function() {
						return "<b>" + this.series.name + ":</b>" + this.y + "%";
					}
				},
				series: result.series
			});
		}
		//确定的查询方法
		function search(){
			var params = getParams();
			var date1 = $("#datepicker1").val();
			var date2 = $("#datepicker2").val();
			if(date1>date2){
				alert("日期前后选择有误,请重新设置!");
				$("#datepicker1").focus();
			}else{
				reportByParams(params);
			}
		}
		//根据参数获取数据
		function reportByParams(params){
			$("#message").html("图表查询中,请稍候... ...");
			$.post("${mfgctx}/manu-analyse/qualityrate-contrast-datas.htm",params,function(result){
				$("#message").html("");
				if(result.error){
					alert(result.message);
				}else{
					createDetailTable(result);
					createReport(result);
					cacheResult = result;
				}
			},'json');
		}
		//显示方法
		function toggleSearchTable(){
			var display = $("#searchTable").css("display");
			if(display == 'block'){
				$("#searchTable").css("display","none");
			}else{
				$("#searchTable").css("display","block");
				window.location = "#btnDiv";
			}
		}
		//取消方法
		function hiddenSearchTable(){
			$("#searchTable").css("display","none");
		}
		//获取表单的值
		function getParams(){
			var params = {};
			var multiSelectStrs = ",";
			for(var i=0;i<multiselectIds.length;i++){
				multiSelectStrs += multiselectIds[i] + ",";
			}
			$(":input[name]","#customerSearchDiv").each(function(index,obj){
				if(obj.name.indexOf("params.") != 0){
					return;
				}
				var jObj = $(obj);
				if(obj.type=='select-one'){
					if(!$(obj).is(":disabled")){
						if(multiSelectStrs.indexOf("," + obj.id + ",")>-1){
							var values = $("#" + obj.id).multiselect("getChecked").map(function(){
							   return this.value;	
							}).get();
							params[obj.name] = values.toString();
						}else if(jObj.val()){
							params[obj.name] = jObj.val();
						}
					}
				}else if(obj.type=="radio"){
					if(obj.checked){
						params[obj.name] = jObj.val();
						if(obj.name=='params.group'&&jObj.attr("title")){
							params['params.groupName'] = jObj.attr("title");
						}
					}
				}else if(jObj.val()){
					params[obj.name] = jObj.val();
				}
			});
			return params;
		}
		
		//创建表格
		var detailTable = null;
		function createDetailTable(result){
			clearTable();
			var colModel=[{name:'name',width:80,index:'name',label:' ',align:'center',sortable:false},{name:'custom_name',width:100,index:'custom_name',label:'日期'}],datas = [];
			var tableHeaderList = result.tableHeaderList;
			for(var i=0;i<tableHeaderList.length;i++){
				colModel.push({name:'date'+i,index:'date' + i,width:50,align:'center',label : tableHeaderList[i]});
			}
			var datas = [];
			
			for(var i=0;i<result.series.length;i++){
				var serie = result.series[i];
				var p = {id:i+1,custom_name:serie.name,name:result.groupName};
				for(var j=0;j<serie.data.length;j++){
					p['date' + j] = parseFloat(serie.data[j].y).toFixed(2)+"%";
				}
				datas.push(p);
			}

			var width = $("#btnDiv").width()-30;
			detailTable = $("#detail_table").jqGrid({
				datatype: "local",
				localReader : {
					id : 'custom_name'
				},
				data: datas,
				rownumbers : true,
				rowNum:100,
				width : width,
				height: 200,
				colModel: colModel,
			    multiselect: false,
				viewrecords: true, 
				autowidth: true,
				forceFit : true,
 			   	shrinkToFit: false,
				sortorder: "desc",
				gridComplete : function(){}
			});
		}
		
		//清除表格
		function clearTable(){
			if(detailTable != null){
				detailTable.GridDestroy();
				detailTable = null;
			}
			$("#detailTableDiv_parent").html("<table id=\"detail_table\"></table>");
		}
		//统计对象改变的方法
		function typeSelect(obj,selectId){
			$(".typeSelect").attr("disabled",true);
			$("#" + selectId).attr("disabled",false);
		}
		//统计改变的方法
		function targetSelect(obj,selectId){
			$(".targerSelect").multiselect("disable");
			$("#" + selectId).multiselect("enable");
			
		}
		//重置查询条件的方法
		function formReset(){
			typeSelect($("#inspection_select_radio")[0],"inspection_select");
			targetSelect($("#_product")[0],"_product");
		}
		//查看明细的方法
		function showDetailByDate(inspectionDate){
			var params = getParams();
			params['params.inspectionDate'] = inspectionDate;
			var url = '${mfgctx}/manu-analyse/regular-right-detail.htm?1=1';
			for(var pro in params){
				url += "&" + pro + "=" + params[pro];
			}
			$.colorbox({href:encodeURI(url),iframe:true, 
				innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
	 			overlayClose:false,
	 			title:"台帐明细"
	 		});
		}
		function exportChart(){
			$.exportChart({
	    		chart:chart,
	    		grid:$("#detail_table"),
	    		message:$("#message")
	    	});
		}
		//发送邮件
		function sendChart(){
			$.sendChart({
				chart:chart,
				grid:$("#detail_table"),
				message:$("#message"),
				width:$("#reportDiv").width(),
				height:$("#reportDiv").height()
			});
		}
		function showAllMateriel(){
			var url="${mfgctx}/manu-analyse/materiel-node-load.htm";
			$.colorbox({href:encodeURI(url),iframe:true, 
				innerWidth:$(window).width()<350?$(window).width()-50:350, 
				innerHeight:$(window).height()<400?$(window).height()-50:400,
	 			overlayClose:false,
	 			title:"物料级别"
	 		});
		}
		function showCheckedMateriel(materielName,checkedId,information){
			var html='<span style="text-align: left;">物料级别信息:'+information+"</span>";
			var materielNameStr=materielName.substring(0,materielName.length-1);
			var checkedIdStr=checkedId.substring(0,checkedId.length-1);
			$("#materialLevels").val(materielNameStr);
			$("#materialLevels").next().val(checkedIdStr);
			$("#materialTypeInformation").html(html);
		}
	</script>