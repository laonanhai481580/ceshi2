<%@page import="com.ambition.carmfg.entity.InspectionPointTypeEnum"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<link href="${ctx}/widgets/multiselect/jquery.multiselect.css" rel="stylesheet" type="text/css"/>
<script src="${ctx}/widgets/multiselect/jquery.multiselect.js" type="text/javascript"></script>
<script src="${ctx}/widgets/multiselect/jquery.multiselect.zh-cn.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
<%
	StringBuffer sb = new StringBuffer("");
	InspectionPointTypeEnum[] enums = InspectionPointTypeEnum.values();
	for(int i=0;i<enums.length;i++){
		InspectionPointTypeEnum e = enums[i];
		if(e != InspectionPointTypeEnum.PATROLINSPECTION){
			if(sb.length()>0){
				sb.append(",");
			}
			sb.append(e.ordinal());
		}
	}
	request.setAttribute("inspectionPointType",sb.toString());
%>
<script type="text/javascript">
		function contentResize(){
			if(cacheResult != null){
				clearTable();
				createReport(cacheResult);
				createDetailTable(cacheResult);
			}
		}
		var chart = null,cacheResult = null;
		$(document).ready(function(){
			$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
			$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
			$("#_workgroup").multiselect({selectedList:2});
			$("#_product").multiselect({selectedList:2});
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
							if(this.value>100){
								return "";
							}else{
								return this.value + '%';
							}
						}
					},
					min:50,
					max:100
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
			            lineWidth : 2,
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
			$.post("${mfgctx}/manu-analyse/qualityrate-contrast-datas.htm",params,function(result){
				if(result.error){
					alert(result.message);
				}else{
					createDetailTable(result);
					createReport(result);
					cacheResult = result;
// 					window.location = "#typeTable";
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
			$(":input","#customerSearchDiv").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name&&obj.type=='select-one'){
					if(obj.id&&!obj.disabled){
						var values = $("#" + obj.id).multiselect("getChecked").map(function(){
						   return this.value;	
						}).get();
						if(values){
							var val = values.toString();
							if(val){
								params[obj.name] = val;
							}
						}else{
							if(jObj.val()){
								params[obj.name] = jObj.val();
							}
						}
					}
				}else if(obj.name&&jObj.val()&&jObj.val()!=""){
					if(obj.type=="radio"){
						if(obj.checked){
							params[obj.name] = jObj.val();
							if(obj.name=='params.group'&&jObj.attr("title")){
								params['params.groupName'] = jObj.attr("title");
							}
						}
					}else{
						params[obj.name] = jObj.val();
					}
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
// 					p['date' + j] = serie.data[j].y + "%";
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
	</script>