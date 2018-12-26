<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.norteksoft.product.api.entity.Opinion"%>
<%@page import="com.ambition.util.common.DateUtil"%>
<%@page import="com.norteksoft.task.base.enumeration.TaskProcessingResult"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>合格供应商取消申请单</title>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
<link rel="shortcut icon">
<meta name="format-detection" content="telephone=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="stylesheet" href="${ctx}/mobile/css/sm.css">
<link rel="stylesheet" href="${ctx}/mobile/css/swiper.min.css">
<link rel="stylesheet" href="${ctx}/mobile/css/style.css">
<script type="text/javascript" src="${resourcesCtx}/js/jquery-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${ctx}/mobile/js/date.js"></script>
<script type="text/javascript" src="${ctx}/mobile/js/iscroll.js"></script>
<script type="text/javascript" src  ="${resourcesCtx}/widgets/validation/validate-all-1.0.js" ></script>
<script type="text/javascript" src  ="${resourcesCtx}/widgets/validation/dynamic.validate.js" ></script>
<jsp:include page="script.jsp"/>
<%@ include file="style.jsp"%>
 </head>
 <body>
 <div class="cause">
	<a><span class="cause_0"><img src="${ctx}/mobile/img/comeback.png"></span></a><span class="cause_1">合格供应商取消申请单</span>
 </div>
 <form id="applicationForm" name="applicationForm" method="post">
<input type="hidden" name="id" id="id" value="${id}"  />
 <input type="hidden" name="opinion" id="opinion">
<input type="hidden" name="inputformortaskform" value="inputform"/>
<input name="taskId" id="taskId" value="${taskId}" type="hidden"/>
 <div class="syq">
	<ul class="syq_1">
		<li>
			<div class="syq_2">
				<span>供应商名称</span>
			</div>
			<div><input type="text" name="supplierName" id="supplierName" value="${supplierName}"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>供应商编号</span>
			</div>
			<div>
				<input type="text" name="supplierCode" id="supplierCode" value="${supplierCode }">
			</div>
		</li>
		<li>
			<div class="syq_2">
				<span>供应物料名称</span>
			</div>
			<div><input type="text" name="supplyMaterial" id="supplyMaterial" value="${supplyMaterial }"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>材料类型</span>
			</div>
			<div><input type="text" name="materialType" id="materialType" value="${materialType}" ></div>
		</li>
		<li>
			<div class="syq_2">
				<span>准入时间</span>
			</div>
			<div><input type="date" name="supplierEnterDate" id="supplierEnterDate" value="${supplierEnterDate}" ></div>
		</li>
		<li>
			<div class="syq_2">
				<span>取消申请时间</span>
			</div>
			<div><input type="date" name="cancleDate" id="cancleDate" value="${cancleDate}"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>申请部门</span>
			</div>
			<div><input type="text" name="applyDept" id="applyDept" value="${applyDept}"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>申请人</span>
			</div>
			<div><input type="text" name="applyMan" id="applyMan"  value="${applyMan }" ></div>
		</li>
	</ul>
 </div>
 <div class="admit_infor">
	<ul>
		<li>问题点描述及影响范围</li>
		<li class="admit_infor_li"><input type="checkbox" name="down_select" id="down_select"><img src="${ctx}/mobile/img/ht.png"></li>
	</ul>
 </div>
<script>
	$('#down_select').click(function(){
		$('#assess').slideToggle();
	});
</script>
 <textarea id="assess" name="problemDesc">${problemDesc}</textarea>
 <div class="gys_mc">
	<ul class="gys_mc_a">
		<li class="purchase">采购中心</li>
		<li>
			<div class="syq_2" ><span>经办人</span></div>
			<div class="polling_sell_2">
			<input type="text" style="width: 50%;" name="purchaseProcesser" id="purchaseProcesser" value="${purchaseProcesser }">
			<input type="hidden" name="purchaseProcessLog" id="purchaseProcessLog" value="${purchaseProcessLog }">
				<a id="polling_fdj" class="polling_fdj"  onclick="searchPerson('purchaseProcesser','purchaseProcessLog')" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('purchaseProcesser','purchaseProcessLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a>
			</div>
		</li>
		<li>
			<div class="syq_2" ><span>审核人</span></div>
			<div class="polling_sell_2">
			<input type="text" style="width: 50%;" name="purchaseChecker" id="purchaseChecker" value="${purchaseChecker }">
			<input type="hidden" name="purchaseCheckerLog" id="purchaseCheckerLog" value="${purchaseCheckerLog }">
				<a id="polling_fdj" class="polling_fdj"  onclick="searchPerson('purchaseChecker','purchaseCheckerLog')" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('purchaseChecker','purchaseCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
	</ul>
 </div>
 <div class="gys_mc">
	<ul class="gys_mc_a">
		<li class="purchase">财务部</li>
		<li>
			<div class="syq_2"><span>应付账款余额</span></div>
			<div><input type="text" name="payMoney" id="payMoney"  value="${payMoney}"></div>
		</li>
		<li>
			<div class="syq_2"><span>经办人</span></div>
			<div class="polling_sell_2">
				<input type="text" name="financeProcesser" id="financeProcesser" value="${financeProcesser }">
				<input type="hidden" name="financeProcesserLog" id="financeProcesserLog" value="${financeProcesserLog }">
				<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('financeProcesser','financeProcesserLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('financeProcesser','financeProcesserLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="syq_2" ><span>审核人</span></div>
			<div class="polling_sell_2">
			<input type="text" style="width: 50%;" name="financeChecker" id="financeChecker" value="${financeChecker }">
			<input type="hidden" name="financeCheckerLog" id="financeCheckerLog" value="${financeCheckerLog }">
				<a id="polling_fdj" class="polling_fdj"  onclick="searchPerson('financeChecker','financeCheckerLog')" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('financeChecker','financeCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
	</ul>
 </div>
 <div class="gys_mc">
	<ul class="gys_mc_a">
		<li class="purchase">物控</li>
		<li>
			<div class="syq_2"><span>在库库存数</span></div>
			<div><input type="text" name="pmcStockAmount" id="pmcStockAmount" value="${pmcStockAmount}"></div>
		</li>
		<li>
			<div class="syq_2"><span>在线库存数</span></div>
			<div><input type="text" name="pmcLineAmount" id="pmcLineAmount" value="${pmcLineAmount}"></div>
		</li>
		<li>
			<div class="syq_2"><span>经办人</span></div>
			<div class="polling_sell_2">
				<input type="text" name="pmcProcesser" id="pmcProcesser" value="${pmcProcesser }">
				<input type="hidden" name="pmcProcesserLog" id="pmcProcesserLog" value="${pmcProcesserLog }">
				<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('pmcProcesser','pmcProcesserLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('pmcProcesser','pmcProcesserLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="syq_2" ><span>审核人</span></div>
			<div class="polling_sell_2">
			<input type="text" style="width: 50%;" name="pmcChecker" id="pmcChecker" value="${pmcChecker }">
			<input type="hidden" name="pmcCheckerLog" id="pmcCheckerLog" value="${pmcCheckerLog }">
				<a id="polling_fdj" class="polling_fdj"  onclick="searchPerson('pmcChecker','pmcCheckerLog')" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('pmcChecker','pmcCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
	</ul>
 </div>
 <div class="gys_mc">
	<ul class="gys_mc_a">
		<li class="purchase">SQM</li>
		<li >
		 <div class="syq_2"><span>IQC库存品检验状态</span></div>
		 <div><input type="text" name="stockInsepctionState" id="stockInsepctionState" value="${stockInsepctionState}"></div>
		 </li>
		<li>
			<div class="syq_2"><span>经办人</span></div>
			<div class="polling_sell_2">
				<input type="text" name="sqmProcesser" id="sqmProcesser" value="${sqmProcesser }">
				<input type="hidden" name="sqmProcesserLog" id="sqmProcesserLog" value="${sqmProcesserLog }">
				<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('sqmProcesser','sqmProcesserLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('sqmProcesser','sqmProcesserLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="syq_2" ><span>审核人</span></div>
			<div class="polling_sell_2">
			<input type="text" style="width: 50%;" name="sqmChecker" id="sqmChecker" value="${sqmChecker }">
			<input type="hidden" name="sqmCheckerLog" id="sqmCheckerLog" value="${sqmCheckerLog }">
				<a id="polling_fdj" class="polling_fdj"  onclick="searchPerson('sqmChecker','sqmCheckerLog')" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('sqmChecker','sqmCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li class="purchase">SQE处理结果</li>
		<li>
			<div class="syq_2"><span>经办人</span></div>
			<div class="polling_sell_2">
				<input type="text" style="width: 50%;" name="sqeProcesser" id="sqeProcesser" value="${sqeProcesser }">
				<input type="hidden" name="sqeProcesserLog" id="sqeProcesserLog" value="${sqeProcesserLog }">
				<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('sqeProcesser','sqeProcesserLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('sqeProcesser','sqeProcesserLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="syq_2" ><span>审核人</span></div>
			<div class="polling_sell_2">
			<input type="text" style="width: 50%;" name="sqeChecker" id="sqeChecker" value="${sqeChecker }">
			<input type="hidden" name="sqeCheckerLog" id="sqeCheckerLog" value="${sqeCheckerLog }">
				<a id="polling_fdj" class="polling_fdj"  onclick="searchPerson('sqeChecker','sqeCheckerLog')" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('sqeChecker','sqeCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
	</ul>
 </div>
 <div class="admit_infor">
	<ul>
		<li>最终结果及意见</li>
		<li class="admit_infor_li"><input type="checkbox" name="select_down" id="select_down" "><img src="${ctx}/mobile/img/ht.png"></li>
	</ul>
 </div>
	<script>
		$('#select_down').click(function(){
			$('#opin').slideToggle();
		});
	</script>
 <textarea id="opin" name="managerIdeal" >${managerIdeal}</textarea>
 <div class="gys_mc">
	<ul class="gys_mc_a">
		<li>
			<div class="syq_2" ><span>总经理</span></div>
			<div class="polling_sell_2">
			<input type="text" style="width: 50%;" name="managerName" id="managerName" value="${managerName }">
			<input type="hidden" name="managerLog" id="managerLog" value="${managerLog }">
				<a id="polling_fdj" class="polling_fdj"  onclick="searchPerson('managerName','managerLog')" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('managerName','managerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
	</ul>
 </div>
 </form>
 <div class="endding" >
 <div style="color:red;" id="message"><s:actionmessage theme="mytheme"/></div>
	 <s:if test="taskId>0">
		<div class="give">
			<a class="polling_fdj" onclick="searchPerson('copyPersonUser','copyPerson');">抄送</a>
			<input type="text" name="copyPersonUser"  placeholder="选择抄送人" id="copyPersonUser">
			<input type="hidden" name="copyPerson"   id="copyPerson">
			<i onclick="del('copyPersonUser','copyPerson')"></i>
		</div>
		<div class="give">
			<a class="polling_fdj" onclick="searchPerson('assigneeUser','assignee');">指派</a>
			<input type="text" name="assigneeUser" value="" placeholder="选择指派人" id="assigneeUser">
			<input type="hidden" name="assignee" value=""  id="assignee">
			<i onclick="del('assigneeUser','assignee')"></i>
		</div>
	</s:if>
	 <div class="buttos">
	<s:if test="taskId>0">
	        <a onclick="showProcessForm();">查看意见</a>
        <s:if test="isComplete==false">
			   <s:if test="task.active==0">
				 <a onclick="saveForm();">保存</a>
				<s:if test="task.processingMode.name()==\"TYPE_EDIT\"">
					<a id="sub_tj" onclick="_completeTask('SUBMIT');">提交</a>
				</s:if>
				<s:elseif test="task.processingMode.name()==\"TYPE_APPROVAL\"">
					  <a onclick="_completeTask('APPROVE');">同意</a><a onclick="_completeTask('REFUSE');">不同意</a>
				</s:elseif>
	    </s:if>
	</s:if>
	</s:if>
	<s:else>
	   <a onclick="showProcessForm();">查看意见</a>
	   <s:if test="isComplete==false">
			<a onclick="saveForm();">保存</a>
			<s:if test="taskId>0">
			<a id="sub_tj" onclick="_completeTask('SUBMIT');">提交</a>
			</s:if>
			<s:else><a id="sub_tj" onclick="submitForm();">提交</a></s:else>
		</s:if>
	</s:else>
	</div>
	<div class="opinion">意见</div>
		<textarea id="opinionStr" onchange="setOpinionValue(this);" style="height: 3rem;  width: 90%;margin-left: 5%;" name="opinionStr" class="writetextarea"></textarea>
 </div>
 <div style="height:11rem;width: 100%"></div>
 <div id="zzc" style="display:none"></div>
  <div id="nk" style="display:none">
	<div id="ym"><p><span>用户树</span><a id="closeId"><img src="${ctx}/mobile/img/cha.png" /></a></p></div>
	<div id="navigation"><button type="button" onclick="setInputValue();">确定</button><input type="text" name="searchTag" id="searchTag"><a href="javascript:;" onclick="searchElement()"><img src="${ctx}/mobile/img/fdj.png"></a></div>
	<div class="dsb" style="overflow-y:scroll; overflow-x:scroll;">
		<ul id="root" style="margin:0 6px;">
			<li>
				<label><a href="javascript:;" >欧菲光-CCM</a></label>
				${userTreeHtml}
			</li>
		</ul>
	</div>
 </div>
   <div id="file" style="display:none">
  <div id="ym"><p><span>上传附件</span><a id="filecloseId"><img src="${ctx}/mobile/img/cha.png" /></a></p></div>
  <div class="dsb" style="overflow-y:scroll; overflow-x:scroll;">
	<form id="form1" enctype="multipart/form-data" method="post" action="${carmfgctx}/common/upload.htm">
	  <ul id="root" style="margin:0 6px;">
	      <li style="margin-left:20px;">
				 <div class="row">
				      <label for="fileToUpload">选择文件</label><br/>
				      <input type="file" name="uploadFile" id="uploadFile" onchange="fileSelected();"/>
				 </div>
			 </li>
			 <li style="margin-left:5px;">
			    <div id="fileNameDiv"><input style="width:150px;" name="uploadFileName" readonly=readonly id="uploadFileName"/></div>
			 </li>
			 <li style="margin-left:20px;">
				<div class="row">
				  <input type="button" onclick="uploadFiles()" value="Upload" />
		       </div>
	       </li>
			 <li style="margin-left:20px;">
			   <div id="fileSize"></div>
			</li>
			<li style="margin-left:20px;">
			   <div id="fileType"></div>
			</li>
			
	       <li style="margin-left:20px;">
	          <div id="progressNumber"></div>
	       </li>
      </ul>
   </form>
   </div>
 </div>
 <div id="processForm" style="display:none"></div>
 <div id="processFormNk" style="display:none">
 <div id="ym"><p><span>查看意见</span><a id="processFormId"><img src="${ctx}/mobile/img/cha.png" /></a></p></div>
	<div class="dsb" style="overflow-y:scroll; overflow-x:scroll;">
	   <%
		List<Opinion> opinionParameters = (List<Opinion>)request.getAttribute("opinionParameters");
		if(opinionParameters==null){
			opinionParameters = new ArrayList<Opinion>();
		}
	%>
	<table class="form-table-border-left" id="history-table" style="width:100%;margin: auto;">
		<tr height=28>
			<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:100px;text-align: center">
				办理人
			</td>
			<td style="background:#99CCFF;font-weight: bold;font-size:14px;text-align: center">
				意见
			</td>
		</tr>
		<%
			int index = 1;
			for(Opinion param : opinionParameters){
		%>
			<tr height=24>
				<td>
					<%=param.getTransactorName() %>
				</td>
				<td>
					<%=param.getOpinion()==null?"":param.getOpinion() %>
				</td>
			</tr>
		<%} %>
	</table>
	</div>
 </div>
<script type="text/javascript" >
	function addEvent(el,name,fn){
		if(el.addEventListener) return el.addEventListener(name,fn,false);
		return el.attachEvent('on'+name,fn);
	}
	
	function nextnode(node){
		if(!node)return ;
		if(node.nodeType == 1)
			return node;
		if(node.nextSibling)
			return nextnode(node.nextSibling);
	} 
	function prevnode(node){
		if(!node)return ;
		if(node.nodeType == 1)
			return node;
		if(node.previousSibling)
			return prevnode(node.previousSibling);
	} 
	function parcheck(self,checked){
		var par =  prevnode(self.parentNode.parentNode.parentNode.previousSibling),parspar;
		if(par&&par.getElementsByTagName('input')[0]){
			par.getElementsByTagName('input')[0].checked = checked;
			parcheck(par.getElementsByTagName('input')[0],sibcheck(par.getElementsByTagName('input')[0]));
		}			
	}
	function sibcheck(self){
		var sbi = self.parentNode.parentNode.parentNode.childNodes,n=0;
		for(var i=0;i<sbi.length;i++){
			if(sbi[i].nodeType != 1)
				n++;
			else if(sbi[i].getElementsByTagName('input')[0].checked)
				n++;
		}
		return n==sbi.length?true:false;
	}
	addEvent(document.getElementById('root'),'click',function(e){
		e = e||window.event;
		var target = e.target||e.srcElement;
		var tp = nextnode(target.parentNode.nextSibling);
		switch(target.nodeName){
			case 'A':
				if(tp&&tp.nodeName == 'UL'){
					if(tp.style.display != 'block' ){
						tp.style.display = 'block';
						prevnode(target.parentNode.previousSibling).className = 'ren'
					}else{
						tp.style.display = 'none';
						prevnode(target.parentNode.previousSibling).className = 'add'
					}	
				}
				break;
			case 'SPAN':
				var ap = nextnode(nextnode(target.nextSibling).nextSibling);
				if(ap.style.display != 'block' ){
					ap.style.display = 'block';
					target.className = 'ren'
				}else{
					ap.style.display = 'none';
					target.className = 'add'
				}
				break;
			case 'INPUT':
				if(target.checked){
					if(tp){
						var checkbox = tp.getElementsByTagName('input');
						for(var i=0;i<checkbox.length;i++)
							checkbox[i].checked = true;
					} 
				}else{
					if(tp){
						var checkbox = tp.getElementsByTagName('input');
						for(var i=0;i<checkbox.length;i++)
							checkbox[i].checked = false;
					}
				}
				parcheck(target,sibcheck(target));
				break;
		}
	});
	window.onload = function(){
		var labels = document.getElementById('root').getElementsByTagName('label');
		for(var i=0;i<labels.length;i++){
			var span = document.createElement('span');
			span.style.cssText ='display:inline-block;height:24px;vertical-align:middle;width:21px;cursor:pointer;';
			span.innerHTML = ' '
			span.className = 'add';
			if(nextnode(labels[i].nextSibling)&&nextnode(labels[i].nextSibling).nodeName == 'UL')
				labels[i].parentNode.insertBefore(span,labels[i]);
			else
				labels[i].className = 'rem'
		}
	}
</script>

 <script>
	function del(id){
		document.getElementById(id).value="";
	} 
	$("#processFormId").click(function(){
		 $("#processForm").hide();
		 $("#processFormNk").hide();
	});
 </script>

<script>
</script>
</body>
</html>
