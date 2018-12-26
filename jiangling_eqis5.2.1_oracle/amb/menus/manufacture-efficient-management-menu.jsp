<%@page import="com.ambition.carmfg1.ProductBomManager"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%
	Map<String,Integer> structureKeyMap = IqcProductBomManager.getStructureKeyMap();
	Integer level = structureKeyMap.get("产品型号");
	level = (level == null?1:level);
%>
<script>
	$(function() {
		$( "#accordion1" ).accordion({
			animated:false,
			collapsible:false,
			event:'click',
			fillSpace:true 
		});
		
	});
	//刷新结构树
	function refreshProductStrure(){
		$("#product-structure-tree").jstree({
			json_data : {
				data : [
					{ 
						"data" : "产品结构", 
						"state" : "closed",
						attr:{
							id:'root',
							level : 0,
							rel:'drive'
						}
					}
				],
				ajax : { 
					"url" : "${mfgctx}/base-info/bom/list-structure.htm",
					data : function(n){
						var id = n.attr("id");
						return {date:(new Date()).getTime(),selParentId:id=='root'?'':id}	
					}
				}
			},
			plugins : ["themes","json_data","ui","crrm",'types'],
			core : { "initially_open" : ["root"] },
			types : {
				valid_children:'drive',
				types:{
					drive:{
						icon:{
							image:'${mfgctx}/images/_drive.png'
						}
					}
				}
			},
			"ui" : {
				"initially_select" : [ "${productStructure.id}"]
			},
			contextmenu : {
				items : {
					create:null,
					rename : null,
					remove : null,
					ccp : null
				}
			}
		}).bind("select_node.jstree",function(e,data){
			var modelSpecificationLevel = <%=level%>;
			var level = data.rslt.obj.attr("level");
			if(level == modelSpecificationLevel){
				loadProductBomByStructureAndParent(data.rslt.obj.attr("id"));
		}else{
			alert("请选择文件夹,选择产品型号！");
		}
	//		else{
 	//			loadProductBomByStructureAndParent('','');
	//		}
		
		});
	}
	//编辑产品结构
	var hisId = '';
	function editProductStructure(id,parentId){
		var url='${mfgctx}/base-info/bom/input-structure.htm?1=1';
		var newId = '';
		if(id){
			url += '&id='+id;
			newId += id;
		}
		newId += ',';
		if(parentId){
			url += "&parentId=" + parentId;
			newId += parentId;
		}
		if(hisId == newId){
			return;
		}
		hisId = newId;
		setTimeout(function(){
			hisId = '';
		},100);
		$.colorbox({href:url,iframe:true, innerWidth:400, innerHeight:200,
			overlayClose:false,
			onClosed:function(){
				refreshProductStrure();
			},
			title:(id?"编辑":"添加") + "产品结构"
		});
	}
	//加载产品BOM 
	function loadProductBomByStructureAndParent(structureId,isreflsh){
		if(window.selStructureId == structureId&&isreflsh){
			return;
		}
		window.selStructureId = structureId || '';
		if(selStructureId){
			//生成生产线树
			$("#checkGradeTypeTree").jstree({ 
				"json_data" : {
					"data" : [
						{ 
							"attr" : { "id" : "-1"},
							"state" : "closed",
							"data" : { 
								"title" : "所有项目"
							}
						}
					],
					"ajax" : {
						"url" : "${mfgctx}/efficient-management/list-datas.htm?structureId="+selStructureId,
						data : function(node){
							return {id:node.attr("id")};
						}
					}
				},
				"contextmenu":{
					items : {
						create:{
							label : '添加',
							action:function(obj){
								$.colorbox({href:"${mfgctx}/efficient-management/list-productline.htm?structureId="+structureId,iframe:true, innerWidth:900, innerHeight:200,
									overlayClose:false,
									title:"添加生产线"
								});
							}
						},
						rename :null,
						remove : {
							label : '删除',
							action:function(obj){
								var id = obj.attr("id");
								if(id<0){
									$.jstree.rollback(data.rlbk);
									return;
								}
								$.vakata.context.hide();
								if(confirm("确定要删除吗?")){
									var params = {
										deleteIds : id
									};
									$.post("${mfgctx}/efficient-management/delete-productline.htm",params,function(result){
										if(result.error){
											alert(result.message);
											$.jstree.rollback(data.rlbk);
										}else{
											if(id == selCheckGradeTypeId){
												selCheckGradeTypeId = null;
												$("#list1").jqGrid("clearGridData");
												$("#checkGradeToolbar .btn").attr("disabled","disabled");
											}
											loadProductBomByStructureAndParent(window.selStructureId,false);
											
										}
									},'json');
								}else{
									$.jstree.rollback(data.rlbk);
								}
							}
						},
						ccp : null
					}
				},
				"dnd" : {
					"drop_finish" : function () { 
						alert("DROP"); 
					},
					"drag_check" : function (data) {
						alert("check");
						if(data.r.attr("id") == "phtml_1") {
							return false;
						}
						return { 
							after : false, 
							before : false, 
							inside : true 
						};
					},
					"drag_finish" : function (data) { 
						alert("DRAG OK"); 
					}
				},
				core : { "initially_open" : ["-1"] },
				"plugins" : [ "themes", "json_data","ui","contextmenu","dnd","crrm"]
			}).bind("select_node.jstree",function(e,data){
				if($(data.rslt.obj).hasClass("jstree-closed")){
					$.jstree._reference("#checkGradeTypeTree").open_node($(data.rslt.obj),null,false);
				}else if($(data.rslt.obj).hasClass("jstree-open")){
					$.jstree._reference("#checkGradeTypeTree").close_node($(data.rslt.obj),null,false);
				}else{
					var childCount = $.jstree._reference("#checkGradeTypeTree")._get_children($(data.rslt.obj)).length;
					if(childCount == 0){
						loadCheckGradeByCheckGradeTypeId(data.rslt.obj.attr("id"),$.jstree._reference("#checkGradeTypeTree").get_text($(data.rslt.obj)));
					}
				}
			});
			
		}
	}
</script>
	<div id="accordion1" class="basic">
		<h3><a id="_bom" onclick="_change_menu('${mfgctx}/efficient-management/list.htm');">标准工时维护</a></h3>
		<div>
			<div id="product-structure-tree" class="demo"></div>
			
			<script type="text/javascript" class="source">
			$(function () {
				if(thirdMenu=="bom"){
					refreshProductStrure();
					$("#accordion1").accordion({active:0});
				}else if(thirdMenu=="ontimeManager"){
					$("#ontimeManager").jstree({ 
						"html_data" : {
							"data" :  
								"<ul><li onclick='selectedNode(this)' id='ontimeManagerThreeMenu'><a href='${mfgctx}/efficient-management/ontime-manager/list.htm'>日出勤时间管理</a></li>"+
						       "<li onclick='selectedNode(this)' id='efficientChartThreeMenu'><a href='${mfgctx}/efficient-management/ontime-manager/efficient-chart.htm'>效率统计</a></li></ul>"
						},
						"ui" : {
							"initially_select" : [ treeMenu ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					$("#accordion1").accordion({active:1});
				}
			});
			function selectedNode(obj) {
				window.location = $(obj).children('a').attr('href');
			}
		   function _change_menu(url){
				window.location=url;
			}
			</script>
		</div>
</div>