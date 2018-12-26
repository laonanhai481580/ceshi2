<%@page import="com.ambition.carmfg.entity.InspectionPointTypeEnum"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<link href="${ctx}/widgets/multiselect/jquery.multiselect.css" rel="stylesheet" type="text/css"/>
<script src="${ctx}/widgets/multiselect/jquery.multiselect.js" type="text/javascript"></script>
<script src="${ctx}/widgets/multiselect/jquery.multiselect.zh-cn.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/js/highcharts.src.js"></script>
<style>
<!--
	#searchUl{
		margin:0px;
		padding:0px;
		list-style:none;
	}
	
	#searchUl .li{
		float:left;
		width:260px;
		margin-top:4px;
	}
	#searchUl .liSpan{
		float:left;
		width:90px;
		text-align:right;
		padding-right:2px;
	}
-->
</style>
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
			if(cacheReports != null){
				createReport(cacheReports);
			}
			setFormWidth();
		}
		function setFormWidth(){
			var width = _getTableWidth()-16;
			var total = parseInt(width/260);
			var addWidth = parseInt(width/total);
			addWidth = addWidth < 260?260:addWidth;
			$("#searchUl .li").width(addWidth);
		}
		var cacheReports = null;
		$(document).ready(function(){
			$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
			$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
			$("#_productModel").multiselect({selectedList:2});
			$("#_modelSpecification").multiselect({selectedList:2});
			$("#_workGroupType").multiselect({selectedList:2});
			//初始化Radio
			$(":input[type=radio]","#searchDiv").each(function(index,obj){
				if(obj.checked){
					if(obj.name == "params.type"){
						typeSelect(obj,obj.value + '_select');
					}else if(obj.name=="params.group"){
						targetSelect(obj,"_" + obj.value);
					}
				}
			});
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
			setFormWidth();
		});
		var charts = [];
		function createReport(reports){
			for(var i=0;i<charts.length;i++){
				charts[i].destroy();
				charts[i] = null;
			}
			charts = [];
			var html = '<table cellpadding="0" cellspacing="0" style="margin:0px auto;width:100%;">';
			for(var i=0;i<reports.length;i++){
				if(i%2==0){
					if(i>0){
						html += "</tr>";
					}
					html += "<tr>";
				}
				var style = "";
				if(i%2>0){
					style += "padding-left:6px;padding-right:10px;";
				}
				if(i>1){
					style += "padding-top:6px;";
				}
				html += "<td width=50% style='"+style+"'><div id='reportDiv"+i+"'><font style='font-size:12px;'>图表生成中,请稍候...</font></div></td>";
			}
			if(reports.length%2>0){
				html += "<td width=50%>&nbsp;</td>";
			}
			html += "</table>";
			$("#reportDivParent").html(html);
			setTimeout(function(){
				for(var i=0;i<reports.length;i++){
					var chart = new Highcharts.Chart({
		 				chart: {
		 					renderTo: 'reportDiv' + i,
		 					type: 'bar',
		 					height : 240
		 				},
		 				title: {
		 					style: {
		 						color: 'black'
		 					},
		 					text: reports[i].title
		 				},
		 				xAxis: {
		 					categories: reports[i].categories,
		 					title: {
		 						text: null
		 					},
		 					labels: {
		 						style: {
		 							color: 'black',
		 							fontSize : '13px'
		 						}
		 					}
		 				},
		 				yAxis: {
		 					min: 0,
		 					title: {
		 						text: null
		 					},
		 					labels: {
		 						style: {
		 							color: 'black',
		 							fontSize : '13px'
		 						}
		 					}
		 				},
		 				tooltip: {
		 					formatter: function() {
		 						return this.point.name +': '+ this.y + "<br/><span style='font-size:12px;color:blue;font-weight:bold;'><font color=red>单击查看不良率推移图</font></span>";
		 					}
		 				},
		 				plotOptions: {
		 					bar: {
		 						dataLabels: {
		 							enabled: true,
		 							color: 'black',
		 							fontSize : '13px'
		 						},
		 						borderWidth:0,
		 						pointWidth : 12,
		 						shadow : false,
					            cursor : 'pointer',
					            events : {
					            	click : function(obj){
					            		var title = this.chart.options.title.text;
					            		showUnqualityTrend(title.split("_")[0],obj.point.name);
					            	}
					            }
		 					}
		 				},
		 				legend: {
		 					enabled: false
		 				},
		 				credits: {
		 					enabled: false
		 				},
		 				series: [{
		 					name: '其他',
		 					data : reports[i].datas
		 				}]
		 			});
		 			charts.push(chart);
				}
			},10);
		}
		//确定的查询方法
		function search(){
			var date1 = $("#datepicker1").val();
			var date2 = $("#datepicker2").val();
			var params = getParams();
			if(date1>date2){
				alert("日期前后选择有误，请重新设置！");
			}else{
				reportByParams(params);
			}
		}
		//根据参数获取数据
		var cacheParams = null; //缓存查询参数,转到不良率推移图时用
		function reportByParams(params){
			$.post("${mfgctx}/other-manu-analyse/qualityrate-top10-datas.htm",params,function(result){
				if(result.error){
					alert(result.message);
				}else{
					cacheParams = params;
					createReport(result);
					cacheReports = result;
				}
			},'json');
		}
		//显示方法
		function toggleSearchTable(obj){
			var display = $("#searchTable").css("display");
			if(display == 'block'){
				$("#searchTable").css("display","none");
				$(obj).html("<span><span><b class='btn-icons btn-icons-search'></b>显示查询 </span></span>");
			}else{
				$("#searchTable").css("display","block");
				$(obj).html("<span><span><b class='btn-icons btn-icons-search'></b>隐藏查询 </span></span>");
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
			$("#searchDiv :input").each(function(index,obj){
				var jObj = $(obj);
				if(obj.type=="select-one"){
					if(obj.name&&!obj.disabled){
						if(obj.id){
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
					}
				}else if(obj.name&&jObj.val()&&jObj.val()!=""){
					if(obj.type=='radio'){
						if(obj.checked){
							params[obj.name] = jObj.val();
							if(obj.name=='params.group'&&jObj.attr("title")){
								params['params.groupName'] = jObj.attr("title");
							}
						}
					}else{
						if(obj.name=='params.endDate_le_datetime'){
							params[obj.name] = jObj.val()+' 23:59:59';
						}else{
							params[obj.name] = jObj.val();
						}
					}
				}
			});
			return params;
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
		
		//查看不良率推移图
		function showUnqualityTrend(title,pointName){
			if(!cacheParams){
				alert("数据错误!");
				return;
			}
			var params = {};
			var group = cacheParams["params.group"];
			for(var pro in cacheParams){
				if(pro != 'params.group' && pro != 'params.groupValue' && pro != 'params.groupName'){
					params[pro] = cacheParams[pro];
				}
			}
			params["params." + group + "_equals"] = title;
			params["params.itemname_equals"] = pointName;
			window.location = "${mfgctx}/other-manu-analyse/unquality-trend.htm?" + $.param(params);
		}
	</script>