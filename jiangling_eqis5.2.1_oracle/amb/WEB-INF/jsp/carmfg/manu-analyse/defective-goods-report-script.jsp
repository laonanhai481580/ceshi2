	<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
	<script type="text/javascript" src="${ctx}/widgets/spin/jquery-spin.js"></script>
	<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
	<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<style>
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
			width:118px;
		}
		#searchUl li input{
			width:128px;
		}
		#searchUl li span{
			float:left;
			text-align:right;
			padding-right:2px;
		}
		#groupUl{
			margin:0px;
			padding:0px;
		}
		#groupUl li{
			float:left;
			padding-right:20px;
			height:24px;
			line-height:24px;
			list-style:none;
		}
		#groupUl li.last{
			width:140px;
			padding-bottom:2px;
			text-align:right;
		}
	</style>

	<script type="text/javascript">
		var chart = null,searchParams = null,cacheResult = null;
		$(document).ready(function(){
			$.spin.imageBasePath = '${ctx}/widgets/spin/img/spin1/';
			$('#pageSize').spin({
				max: 100,
				min: 1
			});
			$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
			$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
			$("#partName").click(function(){
				selectComponentCode();
			});
			setFormWidth();
			
			//绑定选项组变更事件
			$(":input[name=params\.businessUnitCode_equals]").attr("target","all");
			groupChange("defectNum");
			$(":input[name=params\.type]").click(function(){
				groupChange($(this).val());
			});
			
			//初始化查询
			searchParams = getParams();
			reportByParams(searchParams);
		});
		
		function groupChange(groupName){
			$(":input[name=params\.group]").closest("li").hide();
			$(":input[name=params\.group][target=all]").closest("li").show();
			$(":input[name=params\.group][target="+groupName+"]").closest("li").show();
			$(":input[name=params\.group][target=all]").first().attr("checked","checked");
			//查询条件
			$("#searchUl :input").attr("disabled","disabled").closest("li").hide();
			$("#searchUl :input[target=all]").removeAttr("disabled").closest("li").show();
			$("#searchUl :input[target="+groupName+"]").removeAttr("disabled").closest("li").show();
		}
		
		function exportChart(){
			$.exportChart({
				chart:chart,
				grid:$("#detail_table"),
				message:$("#message"),
				width:$("#reportDiv").width(),
				height:$("#reportDiv").height()
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
	 		$("#searchModel").val(datas[0].value).focus();
	 	}

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
	 							result.columnName+': '+ this.y + "<br/><span style='font-size:12px;color:blue;'>单击查看详细</span>";
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
			$(":input[name]","#searchDiv").each(function(index,obj){
				var jObj = $(obj);
				if(jObj.is(":disabled")){
					return;
				}
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
				colNames:[result.series1.name,result.columnName,'百分比','累计','累计百分比'], 
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
			
			var url = '${mfgctx}/manu-analyse/defective-goods-report-detail.htm?a=1';
			for(var pro in params){
				url += "&" + pro + "=" + params[pro];
			}
			$.colorbox({href:encodeURI(url),iframe:true, 
				innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
	 			overlayClose:false,
	 			title:"台帐明细"
	 		});
		}
		//明细详情
		function goToNewLocationById(id){
			var url='${mfgctx}';
			$.colorbox({href:url+"/defective-goods/ledger/view-info.htm?id="+id,iframe:true,
				innerWidth:$(window).width()<1000?$(window).width()-50:1000, 
				innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"页面详情"
			});
		}
		
		function selectObj(title,obj,treeType){
			var acsSystemUrl = "${ctx}";
			popTree({ title :title,
				innerWidth:'400',
				treeType:treeType,
				defaultTreeValue:'id',
				leafPage:'false',
				multiple:'false',
				hiddenInputId:obj.id,
				showInputId:obj.id,
				acsSystemUrl:acsSystemUrl,
				callBack:function(){}});
		}
		function selectComponentCode(){
	 		var url = '${mfgctx}/common/product-bom-select.htm';
	 		$.colorbox({href:url,iframe:true, innerWidth:650, innerHeight:400,
	 			overlayClose:false,
	 			title:"选择BOM零部件"
	 		});
	 	}
	 	//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setBomValue(datas){
	 		$("input[name='params.modelSpecification_equals']").val(datas[0].model);
	 	}
	</script>