<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/widgets/spin/jquery-spin.js"></script>
	<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
	<style>
<!--
	#searchUl{
		margin:0px;
		padding:0px;
	}
	#searchUl li{
		float:left;
		width:260px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#searchUl li select{
		width:178px;
	}
	#searchUl li input{
		width:170px;
	}
	#searchUl li span{
		float:left;
		width:75px;
		text-align:right;
		padding-right:2px;
	}
	#groupUl{
		margin:0px;
		padding:0px;
	}
	#groupUl li{
		float:left;
		width:94px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#groupUl li.last{
		width:140px;
		padding-bottom:2px;
		text-align:right;
	}
-->
</style>
	<script type="text/javascript">
	function exportChart(){
		$.exportChart({
			chart:chart,
			grid:$("#detail_table"),
			message:$("#message"),
			width:$("#reportDiv").width(),
			height:$("#reportDiv").height()
		});
	}
	
		function contentResize(){
			if(cacheResult != null){
				createReport(cacheResult);
				createDetailTable(cacheResult);
			}
			setFormWidth();
		}
		function setFormWidth(){
			var width = _getTableWidth()-16;
			var total = parseInt(width/260);
			var addWidth = parseInt(width/total);
			addWidth = addWidth < 260?260:addWidth;
			$("#searchUl li").width(addWidth);
			var num = $("li","#groupUl").length - 1;
			total = parseInt(width/92);
			addWidth = width - parseInt(num%total)*92-10;
			addWidth = addWidth<140?140:addWidth;
			$("li.last","#groupUl").width(addWidth);
		}
		//选择BOM组件
	 	function selectComponentCode(){
	 		var url = '${mfgctx}/common/product-bom-select.htm';
	 		$.colorbox({href:url,iframe:true, 
	 			innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
	 			overlayClose:false,
	 			title:"选择BOM零部件"
	 		});
	 	}
		
	 	//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setBomValue(datas){
	 		$("#partName").val(datas[0].value).focus();
	 	}
		var chart = null,searchParams = null,cacheResult = null;
		$(document).ready(function(){
			$.spin.imageBasePath = '${ctx}/widgets/spin/img/spin1/';
			$('#pageSize').spin({
				max: 100,
				min: 1
			});
			$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
			$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
			searchParams = getParams();
			reportByParams(searchParams);
			$("#partName").click(function(){
				selectComponentCode();
			});
			setFormWidth();
		});
		function createReport(result){
			if(chart != null){
				try {
					chart.destroy();
					chart = null;
				} catch (e) {
					chart = null;
				}
			}
			var width = $("#btnDiv").width()/2;
			$("#reportDiv_parent").html("<div style=\"width:"+width+"px;\" id='reportDiv'>图表生成中,请稍候... ...</div>");
			setTimeout(function(){
				var isShowMax = 0;
				var size = result.categories.length;
				var color1 = '#95B3D7',color2='#9F9F9F';
				chart = new Highcharts.Chart({
					exporting : {
						enabled : false
					},
					colors: ["#4BB2C5", "#E0B56C", "#DF5353", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee",
					 		"#55BF3B", "#DF5353", "#7798BF", "#aaeeee"],
					chart: {
						renderTo: "reportDiv",
						marginTop : 50
					},
					credits: {
				         enabled: false
					},
					title: {
						style : {
							"font-weight":'bold',
							"font-size": '20px',
							color: 'black'
						},
						text: result.title,
						y:13,
						x:-20
					},
					subtitle: {
						text: null
					},
					xAxis: {
						categories: result.categories,
						gridLineDashStyle : 'ShortDashDot',
//	 					gridLineWidth: 1,
						labels : {
							style : {
				               fontSize:'13px',
							   color: 'black'
							},
							y : 25,
							rotation:-45
						},
						title: {
							style: {
								fontWeight: 'bold',
								fontSize: '12px'
							}
						}
					},
					yAxis: [{
						title: {
							text: result.yAxisTitle1,
							style : {
								color: 'black',
				               fontWeight:'bold',
				               fontSize:15
							},
							rotation : 0,
							y : -70
						},
						plotLines: [{
							value: 12
						}],
						labels : {
							align : 'right'
						},
						gridLineWidth : 1,
//	 					tickInterval : parseInt(result.max/10),
						gridLineDashStyle : 'ShortDashDot'
					},{
						title: {
							text: result.yAxisTitle2,
							style: {
								color: 'black',
				               'font-weight':'bold',
				               fontSize:15
				            },
							rotation : 0,
							y : -70
						},
						plotLines: [{
							width: 1
						}],
				        opposite: true,
				        gridLineWidth : 0,
//	 			        tickInterval : result.max/10,
						labels : {
							y : 1,
							formatter : function(){
								if(result.max==0){
									return '';
								}else if(this.value == result.max){
									return '100%';
								}else if(this.value < result.max){
									return ((this.value / result.max *  100).toFixed(0)) + '%';
								}else{
									return '';
								}
							}
						}
					}],
					legend: {
						align: 'right',
				         verticalAlign : 'top',
				         floating: true,
				         x : -20,
				         y : 12
				    },
				    style: {
						color: 'black'
					},
					plotOptions: {
						column: {
							dataLabels: {
				               enabled: true,
					            formatter : function(){
									return '<font style="color:black;">' + this.y + '</font>';
								}
				            },
				            shadow : false,
				            borderWidth : 0,
				            pointPadding : 0,
				            pointWidth : size == 0 ? 1 : (width-160)/size,
	 			            cursor : 'pointer',
				            events : {
				            	click : function(obj){
	 			            		showDetailByArg(obj.point.arg);
				            	}
				            }
				        },
				        spline: {
				        	lineWidth : 1,
				            shadow : false,
				            dataLabels: {
				               enabled: true,
				               formatter : function(){
				            	   return ((this.y / result.max *  100).toFixed(2)) + '%';
				               },
				               color:'black'
				            }
				        }
				    },
					tooltip: {
						formatter: function() {
							var s;
							if(this.point.name == 'spline'){
								s = ''+
								'累计不良百分比 ' + ((this.y / result.max *  100).toFixed(2)) + '%';
							}else{
								s = ''+
// 								'不良数量: '+ this.y;
	 							'不良数量: '+ this.y + "<br/><span style='font-size:12px;color:blue;'>单击查看详细</span>";
							}
							return s;
						}
					},
					series: [{
						type: 'column',
						name: result.series1.name,
						data: result.series1.data
					},{
						type: 'spline',
						name: result.series2.name,
						data: result.series2.data,
						yAxis : 1
					}]
				});
				$("#detail_table").jqGrid("setGridHeight",$("#reportDiv").height()-22);
			},10);
			
		}
		//确定的查询方法
		function search(){
			searchParams = getParams();
			reportByParams(searchParams);
		}
		//根据参数获取数据
		function reportByParams(params){
			params = params || searchParams;
			$.post("${mfgctx}/manu-analyse/defective-goods-report-datas.htm",params,function(result){
				if(result.error){
					alert(result.message);
				}else{
					createReport(result);
					createDetailTable(result);
					cacheResult = result;
// 					window.location = "#reportDiv";
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
		
		//获取表单的值
		function getParams(){
			var params = {};
			$(":input","#searchDiv").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name&&jObj.val()&&jObj.val()!=""){
					if(obj.type=='radio'){
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
			if(detailTable != null){
				detailTable.GridDestroy();
				detailTable = null;
			}
			$("#detail_table_parent").html("<table id=\"detail_table\"></table>");
			//数据格式
// 			var myData0=[
// 			    {id:"1",name:"断裂",total:"392",bi1:"33%",allTotal:"392",bi2:"33%"},
// 			    {id:"2",name:"划伤",total:"200",bi1:"20%",allTotal:"592",bi2:"50%"}];
			detailTable = $("#detail_table").jqGrid({
				datatype: "local",
				data: result.tableData,
				rownumbers : true,
				colNames:[result.series1.name,'不良数量','百分比','累计','累计百分比'], 
				colModel:[ 
	               {name:'name',index:'name',width:80}, 
	               {name:'total',index:'total',width:60,align:'center'}, 
	               {name:'bi1',index:'bi1',width:80,align:'center'}, 
	               {name:'allTotal',index:'allTotal',width:70,align:'center'}, 
	               {name:'bi2',index:'bi2',width:70,align:'center'}
		        ],
			    multiselect: false,
			   	autowidth: true,
				forceFit : true,
			   	shrinkToFit: true,
				viewrecords: true, 
				sortorder: "desc",
				gridComplete : function(){
					
				}
			});
		}
		//查看明细的方法
		function showDetailByArg(arg){
			var params = getParams();
			params['params.arg'] = arg;
			var url = '${mfgctx}/manu-analyse/defective-goods-report-detail.htm?1=1';
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
		//明细详情
		function goToNewLocationById(id){
			var url='${mfgctx}';
			$.colorbox({href:url+"/defective-goods/ledger/view-info.htm?id="+id,iframe:true,
				innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"页面详情"
			});
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="regectManager";
		var thirdMenu="_defective_goods_report";
	</script>
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-defective-goods-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow-y:auto;">
			<form onsubmit="return false;">
			<div class="opt-btn" id="btnDiv"></div>
			 <div id="searchDiv" style="display:block;padding:4px;">
						<table class="form-table-outside-border" style="width:100%;display:block;" id="searchTable">
							<tr>
								<td style="padding-left:6px;padding-bottom:4px;">
									<ul id="searchUl">
									<li>
								 			<span class="label">检验日期</span>
								 			<input id="datepicker1" type="text" readonly="readonly" style="width:72px;" class="line" name="params.startDate_ge_date"/>
								 		        至<input id="datepicker2"type="text" readonly="readonly" style="width:72px;" class="line" name="params.endDate_le_date"/>
								 		</li>
										<li>
								 			<span>产品类型</span>
								 			<s:select list="productModelOptions" 
												  theme="simple"
												  listKey="name" 
												  listValue="name" 
												  name="params.productModel_equals"
												  labelSeparator=""
												  emptyOption="true"></s:select>
								 		</li>
								 		<li>
								 			<span>产品型号</span>
								 			<s:select list="modelSpecificationOptions" 
												  theme="simple"
												  listKey="name" 
												  listValue="name" 
												  name="params.modelSpecification_equals"
												  labelSeparator=""
												  emptyOption="true"></s:select>
								 		</li>
								 		<li>
								 			<span>工序</span>
								 			<s:select list="workProcedureOptions" 
												  theme="simple"
												  listKey="name" 
												  listValue="name" 
												  name="params.workProcedure_equals"
												  labelSeparator=""
												  emptyOption="true"></s:select>
								 		</li>
								 		<li>
								 			<span>产品阶段</span>
								 			<s:select list="productPhaseOptions" 
												  theme="simple"
												  listKey="name" 
												  listValue="name" 
												  name="params.productPhase_equals"
												  labelSeparator=""
												  emptyOption="true"></s:select>
								 		</li>
								 		<li>
								 			<span>处理方式</span>
								 			<s:select list="disposeMethodOptions" 
												  theme="simple"
												  listKey="name" 
												  listValue="name" 
												  name="params.disposeMethod_equals"
												  labelSeparator=""
												  emptyOption="true"></s:select>
								 		</li>
								 		<li>
								 			<span>不合格类别</span>
								 			<s:select list="unqualifiedTypeOptions" 
												  theme="simple"
												  listKey="name" 
												  listValue="name" 
												  name="params.unqualifiedType_equals"
												  labelSeparator=""
												  emptyOption="true"></s:select>
								 		</li>
								 		<li>
								 			<span>不合格来源</span>
								 			<s:select list="unqualifiedSourceOptions" 
												  theme="simple"
												  listKey="name" 
												  listValue="name" 
												  name="params.unqualifiedSource_equals"
												  labelSeparator=""
												  emptyOption="true"></s:select>
								 		</li>
								 		<li>
								 			<span>零部件</span>
								 			<input type="text" name="params.partName_like" id="partName"/>
								 		</li>
									</ul>   
								</td>
							</tr>
						</table>
						<table class="form-table-outside-border" style="width:100%;padding:4px;">
							<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;">统计对象</caption>
							<tr>
								<td><input type="radio" name="params.type" checked="checked" value="inspection"/>不合格品&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
								<td style="text-align:right">显示前&nbsp;<input type="text" size="5" value="10" style="width:40px;text-align: center;" name="params.pageSize" id="pageSize" readonly="readonly"/>&nbsp;项</td>
							</tr>
						</table>
						<table class="form-table-outside-border" style="width:100%;">
							<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;">统计分组</caption>
							<tr>
								<td style="padding:0px;margin:0px;padding-bottom:4px;">
									<ul id="groupUl">
										<li>
											<input type="radio" name="params.group" checked="checked" value="detail" title="不良项目"/>不良项目
										</li>
										<li>
											<input type="radio" name="params.group" value="workProcedure" title="工序"/>工序
										</li>
										<li>
											<input type="radio" name="params.group" value="productModel" title="产品类型"/>产品类型
										</li>
										<li>
											<input type="radio" name="params.group" value="modelSpecification" title="产品型号"/>产品型号
										</li>
										<li>
											<input type="radio" name="params.group" value="productPhase" title="产品阶段"/>产品阶段
										</li>
										<li>
											<input type="radio" name="params.group" value="unqualifiedType" title="不合格类别"/>不合格类别
										</li>
										<li>
											<input type="radio" name="params.group" value="partName" title="零部件"/>零部件
										</li>
										<li>
											<input type="radio" name="params.group" value="unqualifiedSource" title="不合格来源"/>不合格来源
										</li>
										<li>
											<input type="radio" name="params.group" value="disposeMethod" title="处理方式"/>处理方式
										</li>
										<li class="last">
										</li>
									</ul>
								</td>
							</tr>
							<tr>
							<td style="text-align:right;"> 
							<button  class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
							<button  class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
							<button  class='btn' type="button" onclick="reset();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
							<button  class='btn' onclick="toggleSearchTable(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>隐藏查询</span></span></button> 
							</td>
							</tr>
						</table>
			</div>
			<div>
				<table style="width:100%;">
					<tr>
						<td style="width:48%;" id="detail_table_parent">
							<table id="detail_table"></table>
						</td>
						<td style="width:50%;" id="reportDiv_parent">
							<div id='reportDiv'></div>
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
</body>
</html>