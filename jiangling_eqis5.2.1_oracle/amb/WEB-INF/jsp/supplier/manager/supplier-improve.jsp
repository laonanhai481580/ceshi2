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
		jQuery.extend($.jgrid.defaults,{
			prmNames:{
				rows:'supplierImprovePage.pageSize',
				page:'supplierImprovePage.pageNo',
				sort:'supplierImprovePage.orderBy',
				order:'supplierImprovePage.order'
			}
		});
		function isStart(cellvalue, options, rowObject){
			if(parseInt(rowObject.improveState)>=0){
				//return "<div style='background-color: #00868B;width: 100%;'> </div>";
				return "<div style='text-align:center'><img src='${ctx}/images/green.gif'/> </div>";
				
			}else{
				return "";
			}
		}
		function isFinishReason(cellvalue, options, rowObject){
			if(parseInt(rowObject.improveState)>=1){
				return "<div style='text-align:center'><img src='${ctx}/images/green.gif'/> </div>";
			}else{
				return "";
			}
			
		}
		function isFinishMeasure(cellvalue, options, rowObject){
			if(parseInt(rowObject.improveState)>=2){
				return "<div style='text-align:center'><img src='${ctx}/images/green.gif'/> </div>";
			}else{
				return "";
			}
			
		}
		function isFinishPrecautionMeasure(cellvalue, options, rowObject){
			if(parseInt(rowObject.improveState)>=3){
				return "<div style='text-align:center'><img src='${ctx}/images/green.gif'/> </div>";
			}else{
				return "";
			}
			
		}
		function isFinishVerify(cellvalue, options, rowObject){
			if(parseInt(rowObject.improveState)==4){
				return "<div style='text-align:center'><img src='${ctx}/images/green.gif'/> </div>";
			}else{
				return "";
			}
			
		}
		function submitProcess(cellvalue, options, rowObject){
			if(parseInt(rowObject.improveState)>=0){
				return "<div style='text-align:center'><img src='${ctx}/images/green.gif'/> </div>";
			}else{
				return "";
			}
		}
		function checkProcess(cellvalue, options, rowObject){
			if(parseInt(rowObject.improveState)>=1){
				return "<div style='text-align:center'><img src='${ctx}/images/green.gif'/> </div>";
			}else{
				return "";
			}
		}
		function dealProcess(cellvalue, options, rowObject){
			if(parseInt(rowObject.improveState)>=2){
				return "<div style='text-align:center'><img src='${ctx}/images/green.gif'/> </div>";
			}else{
				return "";
			}
		}
		function endProcess(cellvalue, options, rowObject){
			if(parseInt(rowObject.improveState)==3){
				return "<div style='text-align:center'><img src='${ctx}/images/green.gif'/> </div>";
			}else{
				return "";
			}
		}
		function click(cellvalue, options, rowObject){
			if(cellvalue){
				return "<a href='#' onclick='showInfo("+rowObject.id+")'> "+cellvalue+"</a>";
			}else{
				return "";
			}			
		}
		function showInfo(id){
			$.colorbox({href:'${improvectx}/correction-precaution/view-info.htm?id='+id,iframe:true, innerWidth:900, innerHeight:600,overlayClose:false,title:"查看改进详细信息"});
			
		}
		function sourceNoClick(cellvalue, options, rowObject){
			if(cellvalue&&cellvalue!="&nbsp;"&&cellvalue!=""&&cellvalue!=null){
				var info = rowObject.infoSource;
				if(info&&info!="&nbsp;"&&info=="进货检验"){
					return "<a href='#' onclick='callSource(\""+cellvalue+"\",\""+info+"\","+rowObject.id+")'>"+cellvalue+"</a>";
				}else if(info&&info!="&nbsp;"&&info=="制程管制"){
					return "<a href='#' onclick='callSource(\""+cellvalue+"\",\""+info+"\","+rowObject.id+")'>"+cellvalue+"</a>";
				}else{
					return cellvalue;
				}
			}else{
				return "";
			}
		}
		function callSource(no,info,id){
			var url = "";
			if(no&&no!=""&&no!=null&&info=="进货检验"){
				url = "${iqcctx}/inspection-report/view-info.htm?sourceNo="+no;
			}else if(id&&id!=""&&id!=null&&info=="制程管制"){
				url = '${improvectx}/alarm-setting/notice-detail.htm?noticeId='+id;
			}else{
				alert("对不起，没有该来源的数据或该数据已删除！");
			}
			if(url != ""){
				$.colorbox({href:encodeURI(url),iframe:true, 
					innerWidth:$(window).width()<900?$(window).width()-50:900, 
					innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
					overlayClose:false,
					title:"来源数据"
				});
			}
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="manager";
		var thirdMenu="_supplier_improve";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-manager-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="manager-supplier-improve-datas">
					<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
					</security:authorize>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="list" url="${supplierctx}/manager/supplier-improve-datas.htm" code="IMP_CORRECTION_PRECAUTION_MONITOR"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>

</body>

<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>