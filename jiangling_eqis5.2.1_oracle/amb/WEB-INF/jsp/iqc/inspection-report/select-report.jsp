<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
<script type="text/javascript">
		//确定
		function realSelect(id){
			var ids = [];
			if(id){
				ids.push(id);
			}else{
				ids = $("#gridList").jqGrid("getGridParam","selarrrow");
				if(ids.length == 0){
					alert("请选择检验报告!");
					return;
				}
				if(ids.length > 1){
					alert("只能选择一条检验报告!");
					return;
				}
			}
			if($.isFunction(window.parent.setReportValue)){
				var objs = [];
				for(var i=0;i<ids.length;i++){
					var data = $("#gridList").jqGrid('getRowData',ids[i]);
					if(data){
						data.id = ids[i];
						objs.push(data);
					}
				}
				if(objs.length>0){
					window.parent.setReportValue(objs);
					window.parent.$.colorbox.close();
				}else{
					alert("选择的值不存在!");
				}
			}else{
				alert("页面还没有 setReportValue 方法!");
			}
		}
		//取消
		function cancel(){
			window.parent.$.colorbox.close();
		}
		var isFirst = true;
		function setInitParams(){
			if(isFirst){
				isFirst = false;
				<%
					String searchParameters = (String)session.getAttribute("searchParameters");
				%>
				var searchParameters = '<%=searchParameters==null?"":searchParameters%>';
				if(searchParameters){
					searchParameters = eval("(" + searchParameters + ")");
					var paramMap = {};
					for(var i=0;i<searchParameters.length;i++){
						var name = searchParameters[i].propName;
						var val = searchParameters[i].propValue;
						if(paramMap[name]){
							paramMap[name].push(val);
						}else{
							paramMap[name] = [val];
						}
					}
					for(var i=0;i<searchParameters.length;i++){
						var name = searchParameters[i].propName;
						var vals = paramMap[name];
						if(vals){
							$("#parameter_Table :input[name="+name+"]").each(function(index,obj){
								if(vals.length>index){
									$(obj).val(vals[index]);
								}
							});						
						}
					}
				}
			}
		}
		function click(cellvalue,options,rowObject){	
			return "<a href='javascript:void(0);realSelect(\""+rowObject.id+"\");'>"+cellvalue+"</a>";
		}
		
		function formatRate(cellvalue, options, obj){
			if(cellvalue){
				var rate = cellvalue*100;
				if(obj.inspectionAmount != undefined && obj.qualifiedAmount != undefined){
					rate = obj.qualifiedAmount*100.0/obj.inspectionAmount;
				}
				if(rate.toFixed){
					rate = rate.toFixed(2);
				}
				var operations = "<div style='text-align:left;' class='rate'>"+rate+"%</div>";
				return operations;
			}else{
				return "<div style='text-align:left;' class='rate'></div>";
			}
		}
		
		function formateAttachmentFiles(value,o,obj){
			var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='上传附件'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
			return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
		}
	</script>
</head>

<body>
<div class="ui-layout-center">
	<div class="opt-body">
		<div class="opt-btn" id="btnDiv">
			<button class='btn' type="button" onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>	
			<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			<button class='btn' onclick="javascript:iMatrix.showSearchDIV(this);setInitParams();" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
		</div>
		<div id="opt-content">
			<form id="contentForm" name="contentForm" method="post"  action="">
				<grid:jqGrid gridId="gridList"  url="${iqcctx}/inspection-report/select-report-datas.htm" code="IQC_SELECT_REPORT" pageName="page"></grid:jqGrid>
			</form>
		</div>
	</div>
</div>
</body>
</html>