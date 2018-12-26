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
<title>FAR解析跟踪录入表</title>
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
	<nav class="nav_head">
		<%-- <a><img src="${ctx}/mobile/img/comeback.png"></a> --%>
		<span>FAR解析跟踪录入表</span>
	</nav>
 <form id="applicationForm" name="applicationForm" method="post">
 	<input type="hidden" name="id" id="id" value="${id}"  />
 	<input type="hidden" name="opinion" id="opinion">
 	<input type="hidden" name="inputformortaskform" value="inputform"/>
	<input name="taskId" id="taskId" value="${taskId}" type="hidden"/>	
	<table class="all_tab">
		<tbody>
			<tr>
				<th style="text-align: right;">编号:${formNo}</th>
			</tr>
			<tr>
				<td class="td_left">
					<span>发生日期</span>
				</td>
				<td class="td_right">
					<input type="date" name="happenDate" id="happenDate" class="text-inp" value="<s:date name='happenDate' format="yyyy-MM-dd"/>">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>确认日期</span>
				</td>
				<td class="td_right">
					<input type="date" name="confirmDate" id="confirmDate" class="text-inp" value="<s:date name='confirmDate' format="yyyy-MM-dd"/>"> 
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>客户端工序</span>
				</td>
				<td class="td_right">
					<!-- <input type="text" name="workingProcedure" id="workingProcedure" class="text-inp"> -->
				<s:select list="workingProcedures"
					listKey="value" 
					listValue="value" 
					name="workingProcedure" 
					id="workingProcedure" 
					emptyOption="true"
					onchange=""
					cssClass="text-inp"
					theme="simple">
				</s:select>
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>客户名称</span>
				</td>
				<td class="td_right">
					<input type="text" name="customerName" id="customerName" class="text-inp" value="${customerName}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>欧非机型</span>
				</td>
				<td class="td_right">
					<input type="text" name="ofilmModel" id="ofilmModel" class="text-inp" value="${ofilmModel}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>客户机型</span>
				</td>
				<td class="td_right">
					<input type="text" name="customerModel" id="customerModel" class="text-inp" value="${customerModel}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>cs负责人</span>
				</td>
				<td class="td_right" id="select_man">
					<input type="text" name="csMan" id="csMan" value="${csMan }" >
					<input type="hidden" name="csManLogin" id="csManLogin" value="${csManLogin }">
					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('csMan','csManLogin')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<%-- <sub>	
							<b>*</b>
						</sub>
					<a id="polling_ljt" onclick="del('csMan','csManLogin')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a> --%>
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>生产事业群</span>
				</td>
				<td class="td_right">
					<!-- <input type="text" name="productionEnterpriseGroup" id="productionEnterpriseGroup" class="text-inp"> -->
					<s:select list="productionEnterpriseGroups"
					listKey="value" 
					listValue="name" 
					name="productionEnterpriseGroup" 
					id="productionEnterpriseGroup" 
					onchange=""
					cssClass="text-inp"
					theme="simple">
				</s:select>
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>事业部</span>
				</td>
				<td class="td_right">
					<!-- <input type="text" name="businessUnitCode" id="businessUnitCode" class="text-inp"> -->
				<s:select list="businessUnits"
					listKey="value" 
					listValue="name" 
					name="businessUnitCode" 
					id="businessUnitCode" 
					onchange=""
					cssClass="text-inp"
					theme="simple">
				</s:select>
				</td>
			</tr>
		</tbody>
	</table>
<%-- 	<div class="operation">
		<ul id="operation_ul">
			<li>不良现象&数量</li>
			<li class="operation_lifir">
				<div class="divspan"><span>操作</span></div>
				<div>
					<i style="background-image:url(${ctx}/mobile/img/add.png)" onclick="addoperation()"></i>
					<i style="background-image:url(${ctx}/mobile/img/jh.png)" onclick="deleteoperation()"></i>
				</div>
			</li>
			<li class="operation_lisec">
				<div class="divspan"><span>不良现象</span></div>
				<div class="divspan"><span>数量</span></div>
			</li>
			<li class="operation_lithr">
				<div><input type=text class="text-inp" name="defectionItem" value="${defectionItem}"></div>
				<div><input type=text class="text-inp" name="defectionItemValue" value="${defectionItemValue}"></div>
			</li>
		</ul>
	</div> --%>	
	 <input type="hidden" name="_childrenInfos" id="_childrenInfos" />
	<div class="operation">
		<ul id="operation_ul">
			<li>不良现象&数量</li>
			<li class="operation_lifir">
				<div class="divspan"><span>操作</span></div>
				<div>
					<i style="background-image:url(${ctx}/mobile/img/add.png)" onclick="addoperation()"></i>
					<i style="background-image:url(${ctx}/mobile/img/jh.png)" onclick="deleteoperation()"></i>
				</div>
			</li>
			<li class="operation_lisec">
				<div class="divspan"><span>不良现象</span></div>
				<div class="divspan"><span>数量</span></div>
			</li>
			<%int bcount=0; %>
			<s:iterator value="_farAnalysisItems" id="item" var="item">
			<li class="operation_lithr">
				<div><input type=text class="text-inp" name="defectionItem" value="${defectionItem}"></div>
				<div><input type=text class="text-inp" name="defectionItemValue" value="${defectionItemValue}"></div>
			</li>
			<% bcount++; %>
			</s:iterator>			
		</ul>
		 <input type="hidden" name="ingredientItemTotal" id="ingredientItemTotal" value="<%=bcount%>"/>
	</div>	
	<table class="all_tab" style="margin-top:0.5rem;">
		<tbody>
			<tr>
				<td class="td_left">
					<span>快递公司</span>
				</td>
				<td class="td_right">
					<input type="text" name="courierCompany" id="courierCompany" class="text-inp" value="${courierCompany}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>快递单号</span>
				</td>
				<td class="td_right">
					<input type="text" name="courierNumber" id="courierNumber" class="text-inp" value="${courierNumber}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>寄件日期</span>
				</td>
				<td class="td_right">
					<input type="date" name="sendDate" id="sendDate" class="text-inp" value="<s:date name='sendDate' format="yyyy-MM-dd"/>">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>接受者</span>
				</td>
				<%-- <td class="td_right">
					<input type="text" name="receiver" id="receiver" class="text-inp" value="${receiverLogin}">
				</td> --%>
				<td class="td_right" id="select_man">
					<input type="text" name="receiver" id="receiver" value="${receiver }" >
					<input type="hidden" name="receiverLogin" id="receiverLogin" value="${receiverLogin }">
					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('receiver','receiverLogin')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<%-- <sub>	
							<b>*</b>
						</sub>
					<a id="polling_ljt" onclick="del('receiver','receiverLogin')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a> --%>
				</td>				
			</tr>
			<tr>
				<td class="td_left">
					<span>接收日期</span>
				</td>
				<td class="td_right">
					<input type="date" name="receiptDate" id="receiptDate" class="text-inp" value="<s:date name='receiptDate' format="yyyy-MM-dd"/>">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>样品转交接收人</span>
				</td>
				<td class="td_right" id="select_man">
					<input type="text" name="transferMan" id="transferMan" value="${transferMan }" >
					<input type="hidden" name="transferManLogin" id="transferManLogin" value="${transferManLogin }">
					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('transferMan','transferManLogin')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<%-- <sub>	
							<b>*</b>
						</sub>
					<a id="polling_ljt" onclick="del('transferMan','transferManLogin')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a> --%>
				</td>					
			</tr>
			<tr>
				<td class="td_left">
					<span>转交日期</span>
				</td>
				<td class="td_right">
					<input type="date" name="transferDate" id="transferDate" class="text-inp" value="<s:date name='transferDate' format="yyyy-MM-dd"/>">
				</td>
			</tr>
		</tbody>
	</table>
	<div class="problem">
		<ul>
			<li class="other_li" style="background-color:#00ffe5;">识别问题根本</li>
			<li >
				<div>方法</div>
				<div style="margin-top: 0.5rem;">
					&nbsp;&nbsp;&nbsp;<s:checkboxlist
						theme="simple" list="methods" listKey="value" listValue="name"
						name="method" value="method">
					</s:checkboxlist>
				</div>
			</li>
			<li>
				<div>原因分类</div>
				<div>
				<s:select list="reasons"
					listKey="value" 
					listValue="name" 
					name="reason" 
					id="reason" 
					cssStyle="width:140px;"
					onchange=""
					cssClass="text-inp"
					theme="simple">
				</s:select>
				</div>
			</li>
			<li>
				<div>责任部门</div>
				<div><!-- <input type="text" name="department" id="department" class="text-inp"> -->
				<s:select list="departments"
					listKey="value" 
					listValue="name" 
					name="department" 
					id="department" 
					cssStyle="width:140px;"
					onchange=""
					cssClass="text-inp"
					theme="simple">
				</s:select>
				</div>
			</li>
		</ul>
	</div>
	 <div class="admit_infor">
		<ul>
			<li>意见</li>
			<li class="admit_infor_li"><input type="checkbox" name="down_select1" id="down_select1"><img src="${ctx}/mobile/img/ht.png"></li>
		</ul>
	 </div>
	<script>
		$('#down_select1').click(function(){
			$('#remark1').slideToggle();
		});
	</script>
	 <textarea id="remark1" name="remark1">${remark1}</textarea>
 	<div class="problem">
		<ul>
 			<li class="other_li" id="upload">
				<div class="syq_2"><span>附件</span></div>
				<div class="upload_b" >
				<span class="upload_span" style="float: left;"><a class="fileClick"  onclick="_uploadFiles('showattachment1','attachment1');">上传附件</a></span>
				<input type="hidden" name="attachment1" id="attachment1" value="${attachment1}">
				<input type="hidden" name="hisattachment1" id="hisattachment1" value="${hisattachment1}"><br/>
				<p id="showattachment1"></p>
				</div>
			</li>
		</ul>
	</div>
	<div class="problem">
		<ul>
			<li class="other_li" style="background-color:#00ffe5;text-align: center;">制定纠正措施</li>
		</ul>
	</div>			
	 <div class="admit_infor">
		<ul>
			<li>意见</li>
			<li class="admit_infor_li"><input type="checkbox" name="down_select2" id="down_select2"><img src="${ctx}/mobile/img/ht.png"></li>
		</ul>
	 </div>
	<script>
		$('#down_select2').click(function(){
			$('#remark2').slideToggle();
		});
	</script>
	 <textarea id="remark2" name="remark2">${remark2}</textarea>
	<div class="problem">
		<ul>			
			<li class="other_li" id="upload">
				<div class="syq_2"><span>附件</span></div>
				<div class="upload_b" >
				<span class="upload_span" style="float: left;"><a class="fileClick"  onclick="_uploadFiles('showattachment2','attachment2');">上传附件</a></span>
				<input type="hidden" name="attachment2" id="attachment2" value="${attachment2}">
				<input type="hidden" name="hisattachment2" id="hisattachment2" value="${hisattachment2}"><br/>
				<p id="showattachment2"></p>
				</div>
			</li>
		</ul>
	</div>
	<div class="problem">
		<ul>
			<li class="other_li" style="background-color:#00ffe5;">回传客户接收状况</li>
			<li>
				<div>回复日期</div>
				<div><input type="date" name="replyDate" id="replyDate" class="text-inp" value="<s:date name='replyDate' format="yyyy-MM-dd"/>"></div>
			</li>
			<li>
				<div>关闭状态</div>
				<div><!-- <input type="text" name="closeState" id="closeState" class="text-inp"> -->
					<s:select list="closeStates"
						listKey="value" 
						listValue="name" 
						name="closeState" 
						id="closeState" 
						cssStyle="width:140px;"
						onchange=""
						cssClass="text-inp"
						theme="simple">
					</s:select>								
				</div>
			</li>
			<li>
				<div>关闭确认人</div>
				<div id="select_man">
					<input type="text" name="closeMan" id="closeMan" value="${closeMan }">
					<input type="hidden" name="closeManLogin" id="closeManLogin" value="${closeManLogin }">
					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('closeMan','closeManLogin')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<sub>	
							<b>*</b>
						</sub>
					<a id="polling_ljt" onclick="del('closeMan','closeManLogin')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a>
				</div>				
			</li>
			<li class="other_li" id="upload">
				<div class="syq_2"><span>附件</span></div>
				<div class="upload_b" >
				<span class="upload_span" style="float: left;"><a class="fileClick"  onclick="_uploadFiles('showattachment3','attachment3');">上传附件</a></span>
				<input type="hidden" name="attachment3" id="attachment3" value="${attachment3}">
				<input type="hidden" name="hisattachment3" id="hisattachment3" value="${hisattachment3}"><br/>
				<p id="showattachment3"></p>
				</div>
			</li>
			<li>
				<div>回复日期</div>
				<div><input type="date" name="closeDate" id="closeDate" class="text-inp" value="<s:date name='closeDate' format="yyyy-MM-dd"/>" ></div>
			</li>
		</ul>
	</div>
	 <div class="admit_infor">
		<ul>
			<li>回复意见</li>
			<li class="admit_infor_li"><input type="checkbox" name="down_select3" id="down_select3"><img src="${ctx}/mobile/img/ht.png"></li>
		</ul>
	 </div>
	<script>
		$('#down_select3').click(function(){
			$('#remark3').slideToggle();
		});
	</script>
	 <textarea id="remark3" name="remark3">${remark3}</textarea>	
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
		var par =  prevnode(self.parentNode.parentNode.parentNode.previousSibling);
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
						prevnode(target.parentNode.previousSibling).className = 'ren';
					}else{
						tp.style.display = 'none';
						prevnode(target.parentNode.previousSibling).className = 'add';
					}	
				}
				break;
			case 'SPAN':
				var ap = nextnode(nextnode(target.nextSibling).nextSibling);
				if(ap.style.display != 'block' ){
					ap.style.display = 'block';
					target.className = 'ren';
				}else{
					ap.style.display = 'none';
					target.className = 'add';
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
			span.innerHTML = ' ';
			span.className = 'add';
			if(nextnode(labels[i].nextSibling)&&nextnode(labels[i].nextSibling).nodeName == 'UL')
				labels[i].parentNode.insertBefore(span,labels[i]);
			else
				labels[i].className = 'rem';
		}
	};
</script>

 <script>
	function del(id){
		document.getElementById(id).value="";
	} 
 </script>

<script>
 

 $(function(){
	$("#closeId").click(function(){
		 $("#nk").hide();
		 $("#zzc").hide();
	});

	$(".polling_fdj").click(function(){
		 $("#nk").show();
		 $("#zzc").show();
	});
	$("#processFormId").click(function(){
		 $("#processForm").hide();
		 $("#processFormNk").hide();
	});
 });
</script>
<script>
	/* function addoperation(){
		var ulnode=document.getElementById('operation_ul');
		var linode=document.createElement('li');
		linode.class="operation_lithr";
		linode.innerHTML="<div><input type=text class='text-inp' name='bad_a'></div><div><input type=text class='text-inp' name='num_a'></div>";
		ulnode.insertBefore(linode, ulnode.children[3]);
	}
	function deleteoperation(){
		var li=document.getElementById('operation_ul').getElementsByTagName('li');
		var x=li.length;
		if(x > 3){
			document.getElementById('operation_ul').removeChild(li[3]); 
		}
	} */
	 function addoperation(){
		var ulnode=document.getElementById('operation_ul');
		var linode=document.createElement('li');
		var total=parseInt($("#ingredientItemTotal").val())+1;
// 		linode.class="operation_lithr";
		linode.setAttribute('class','operation_lithr');
		linode.innerHTML="<div><input type=text class='text-inp' name='defectionItem' id='defectionItem_<%=bcount%>'></div><div><input type=text class='text-inp' name='defectionItemValue' id='defectionItemValue_<%=bcount%>'></div>";
		ulnode.insertBefore(linode, ulnode.children[linode.length-1]);
		$("#ingredientItemTotal").val(total);
	}
	function deleteoperation(){
		var li=document.getElementById('operation_ul').getElementsByClassName('operation_lithr');
		var x=li.length;
// 		document.getElementById('operation_ul').getElementsByClassName('operation_lithr').removeChild(li[x-1]);
		if(x > 1){
			document.getElementById('operation_ul').removeChild(li[x-1]); 
			var total=parseInt($("#ingredientItemTotal").val());
			$("#ingredientItemTotal").val(total-1);
		}else{
			alert("至少保留一行");
		}
		
	}
</script>
 </body>
</html>
