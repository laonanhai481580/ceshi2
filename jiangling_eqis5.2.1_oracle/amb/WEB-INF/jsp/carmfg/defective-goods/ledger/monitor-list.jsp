<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
		
		function click(cellvalue, options, rowObject){
			if(cellvalue){
				return "<a href='#' onclick='callList("+rowObject.id+")'>"+cellvalue+"</a>";
			}else{
				return "";
			}
		}
		function nameFactory(cellvalue, options, rowObject){
			if(cellvalue == 'FQC'){
				return '制程';
			}else if (cellvalue == 'IQC'){
				return '进货检验';
			}
		}
		var myId = '';
		function callList(id){
			myId = id;
			$.colorbox({href:'${mfgctx}/defective-goods/ledger/monitor-detail.htm?id='+id,iframe:true,
				innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"页面详情"
			});
		}
		function goToNewLocationBySourceNo(url){
			$.colorbox({href:url+"&myId="+myId,iframe:true, 
				innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"来源数据"
			});
		}
		function inputLinkClick(cellvalue, options, rowObj){
			return "<a href='javascript:void(0);showViewInfo(\""+cellvalue+"\");'>"+cellvalue+"</a>";
		}
		function showViewInfo(formNo){
			var url='${mfgctx}/defective-goods/ledger/view-info.htm?formNo=' + formNo;
			$.colorbox({href:url,iframe:true, 
				innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"页面详情"
			});
		}
		function formateAttachmentFiles(value,o,obj){
			var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='上传附件'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
			return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
		}
		function need8DNoClick(cellvalue, options, rowObj){
			return "<a href='javascript:void(0);show8DViewInfo(\""+cellvalue+"\");'>"+cellvalue+"</a>";
		}
		function show8DViewInfo(formNo){
			var url = '${improvectx}/8d-report/view-info.htm?no='+formNo;
	        $.colorbox({href:encodeURI(url),iframe:true, 
				innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"数据详情页面"
			});
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="regectManager";
		var thirdMenu="_defective_goods_monitor";
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
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="MFG_DEFECTIVE-GOODS_MONITOR-LIST_DELETE">
					<button class='btn' onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button class="btn" onclick="iMatrix.export_Data('${mfgctx}/defective-goods/ledger/export-monitor-datas.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="list" url="${mfgctx}/defective-goods/ledger/list-datas.htm" code="MFG_DEFECTIVE_GOODS_MONITOR"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>

</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>