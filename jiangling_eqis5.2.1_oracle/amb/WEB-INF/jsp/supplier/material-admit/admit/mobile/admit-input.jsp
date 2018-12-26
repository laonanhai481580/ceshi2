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
<title>材料承认申请表</title>
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
	<a><span class="cause_0"><%-- <img src="${ctx}/mobile/img/comeback.png"> --%></span></a><span class="cause_1">材料承认申请表</span>
 </div>
 <form id="applicationForm" name="applicationForm" method="post">
 <input type="hidden" name="id" id="id" value="${id}"  />
 <input type="hidden" name="opinion" id="opinion">
 <input type="hidden" name="inputformortaskform" value="inputform"/>
<input name="taskId" id="taskId" value="${taskId}" type="hidden"/>
 <div class="syq">
	<ul class="syq_1">
		<li class="research">研发部</li>
		<li>
			<div class="syq_2">
				<span>规格型号/版本号</span>
			</div>
			<div><input type="text" name="productVersion" id="productVersion" value="${productVersion }"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>产品名称</span>
			</div>
			<div>
				<input type="text" name="productName" id="productName" value="${productName }">
			</div>
		</li>
		<li>
			<div class="syq_2">
				<span>物料代码</span>
			</div>
			<div><input type="text" name="materialCode" id="materialCode" value="${materialCode }"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>申请日期</span>
			</div>
			<div><input type="date" name="applyDate" id="applyDate"  value="<s:date name='applyDate' format="yyyy-MM-dd"/>" ></div>
		</li>
		<li>
			<div class="syq_2"><span>供应商</span></div>
			<div class="polling_sell_2">
				<input type="text" name="supplierName" id="supplierName" value="${supplierName }" onclick="supplierClick()" >
				<a id="polling_fdj" class="polling_fdj" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
				<%-- 	<sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('supplierName')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="syq_2">
				<span>事业群</span>
			</div>
			<div><input type="text" name="businessUnitName" id="businessUnitName" value="${businessUnitName }"></div>
		</li>
	</ul>
 </div>
 <div class="admit_infor">
	<ul>
		<li>承认资料</li>
		<li class="admit_infor_li"><input type="checkbox" name="select_down" id="select_down"><img src="${ctx}/mobile/img/ht.png"></li>
	</ul>
	 <div class="admit_infor_div" style="display:none;" id="admit_infor_div">
	    <s:iterator value="admitProjects" id="option">
				<input id="admitProject${option.id}"  control="true" type="checkbox" <s:if test="admitProject.contains(#option.value)" > checked="checked" </s:if> value="${option.value}" name="admitProject"/>
				<span for="admitProject${option.id}">${option.name}</span></br>
	    </s:iterator>
	    	<input type="checkbox" onclick="showInput()" id="p"><span>其他</span>
	        <input id="else" name="admitProject" class="checkboxLabel" value="" style="display:none;"/>
		<!-- <input type="checkbox" name="admitProject" <s:if test='%{admitProject.contans("规格书Spec")}'>checked="true"</s:if> ><span>规格书Spec</span></br>
		<input type="checkbox" name="admitProject"><span>工程图面Drawing</span></br>
		<input type="checkbox" name="admitProject"><span>样品Sample</span></br>
		<input type="checkbox" name="admitProject"><span>出货检验报告oqc &nbsp report</span></br>
		<input type="checkbox" name="admitProject"><span>CPX报告&nbsp Cpk &nbsp report</span></br>
		<input type="checkbox" name="admitProject"><span>FAI全尺寸报告&nbsp FAI &nbsp report</span></br>
		<input type="checkbox" name="admitProject"><span>可靠性测试报告&nbsp Reliability &nbsp report</span></br>
		<input type="checkbox" name="admitProject"><span>QC工程图&nbsp QCP</span></br>
		<input type="checkbox" name="admitProject"><span>性能测试数据&nbsp Limited&nbsp Sample&nbsp Test&nbsp Data</span></br>
		<input type="checkbox" name="admitProject"><span>包装方式&nbsp Packing&nbsp Content</span></br>
		<input type="checkbox" name="admitProject"><span>代码说明&nbsp Code&nbsp information</span></br>
		<input type="checkbox" name="admitProject"><span>环保资料</span></br>
		<input type="checkbox" name="admitProject"><span>FMEA</span></br>
		<input type="checkbox" name="admitProject"><span>实物写真</span></br>
		<input type="checkbox" name="admitProject"><span>其他</span></br> -->
	</div> 
	<%-- <div class="admit_infor_div" style="display:none;" id="admit_infor_div">
		<s:checkboxlist name="admitProject" id="admitProject" value="#request.admitProject" theme="simple" list="admitProjects" listKey="value" listValue="name"/>
	</div> --%>
	<script>
		$('#select_down').click(function(){
			$('#admit_infor_div').slideToggle();
		});
	</script>
 </div>
 <div class="gys_mc">
	<ul class="gys_mc_a">
		<li class="purchase">采购部</li>
		<li>
			<div class="syq_2" ><span>经办人</span></div>
			<div class="polling_sell_2">
			<input type="text" style="width: 50%;" name="purchaseProcesser" id="purchaseProcesser" value="${purchaseProcesser }">
			<input type="hidden" name="purchaseProcessLog" id="purchaseProcessLog" value="${purchaseProcessLog }">
				<a id="polling_fdj" class="polling_fdj"  onclick="searchPerson('purchaseProcesser','purchaseProcessLog')" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<!-- <sub>	
						<b>*</b>
					</sub> -->
				<%-- <a id="polling_ljt" onclick="del('purchaseProcesser','purchaseProcessLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="syq_2"><span>供应商</span></div>
			<div class="polling_sell_2">
				<input type="text" style="width: 50%;" name="supplier" id="supplier" value="${supplierName}">
				<input type="hidden" name="supplierLoginName" id="supplierLoginName" value="${supplierLoginName }">
				<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('supplier','supplierLoginName')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<!-- <sub>	
						<b>*</b>
					</sub> -->
				<%-- <a id="polling_ljt" onclick="del('supplier','supplierLoginName')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>			
		</li>
	</ul>
 </div>
 <div class="upload">
	<ul class="upload_a">
		<li class="other_li" >
				<div class="syq_2"><span>工程画图</span></div>
				<div class="upload_b" >
<!-- 				<span class="upload_span" style="float: left;"><a class="fileClick"  onclick="_uploadFiles('showengineeringDrawing','engineeringDrawing');">上传附件</a></span> -->
				<input type="hidden" name="engineeringDrawing" id="engineeringDrawing" value="${engineeringDrawing}">
				<input type="hidden" name="hisengineeringDrawing" id="hisengineeringDrawing" value="${engineeringDrawing}">
				<p id="showengineeringDrawing"></p>
				</div>
		</li>
		<li class="other_li" >
				<div class="syq_2"><span>出货检验报告</span></div>
				<div class="upload_b" >
<!-- 				<span class="upload_span" style="float: left;"><a class="fileClick"  onclick="_uploadFiles('showengineeringDrawing','checkoutReport');">上传附件</a></span> -->
				<input type="hidden" name="checkoutReport" id="checkoutReport" value="${checkoutReport}">
				<input type="hidden" name="hischeckoutReport" id="hischeckoutReport" value="${checkoutReport}">
				<p id="showcheckoutReport"></p>
				</div>
		</li>
		<li class="other_li" >
				<div class="syq_2"><span>包装方式</span></div>
				<div class="upload_b" >
<!-- 				<span class="upload_span" style="float: left;"><a class="fileClick"  onclick="_uploadFiles('showpacking','packing');">上传附件</a></span> -->
				<input type="hidden" name="packing" id="packing" value="${packing}">
				<input type="hidden" name="hisshowpacking" id="hisshowpacking" value="${showpacking}">
				<p id="showpacking"></p>
				</div>
		</li>
		<li class="other_li" >
				<div class="syq_2"><span>代码说明</span></div>
				<div class="upload_b" >
<!-- 				<span class="upload_span" style="float: left;"><a class="fileClick"  onclick="_uploadFiles('showcodeExplain','codeExplain');">上传附件</a></span> -->
				<input type="hidden" name="codeExplain" id="codeExplain" value="${codeExplain}">
				<input type="hidden" name="hiscodeExplain" id="hiscodeExplain" value="${codeExplain}">
				<p id="showcodeExplain"></p>
				</div>
		</li>
		<li class="other_li" >
				<div class="syq_2"><span>环保资料</span></div>
				<div class="upload_b" >
<!-- 				<span class="upload_span" style="float: left;"><a class="fileClick"  onclick="_uploadFiles('showenvironmentalMaterial','environmentalMaterial');">上传附件</a></span> -->
				<input type="hidden" name="environmentalMaterial" id="environmentalMaterial" value="${environmentalMaterial}">
				<input type="hidden" name="hisenvironmentalMaterial" id="hisenvironmentalMaterial" value="${environmentalMaterial}">
				<p id="showenvironmentalMaterial"></p>
				</div>
		</li>
	</ul>
 </div>
  <div class="gys_mc">
	<ul class="gys_mc_a">
		<li>
			<div class="syq_2">
				<span>承认状态</span>
			</div>
			<div >
			  <span id="gys_fl_1">
				<s:select list="admitStatuss"
					listKey="value" 
					listValue="value" 
					name="admitStatus" 
					id="admitStatus" 
					cssClass="gys_fl_2"
					emptyOption="true"
					onchange=""
					theme="simple">
				</s:select>
			</span>
			</div>
		</li>
	</ul>
 </div>
 <div class="polling">
	<ul class="polling_sell">
		<li class="company">会签单位</li>
		<li>
			<div class="syq_2"><span>PM核准</span></div>
			<div class="polling_sell_2">
				<input type="text" style="width: 50%;" name="pmChecker" id="pmChecker" value="${pmChecker }">
				<input type="hidden" name="pmCheckerLog" id="pmCheckerLog" value="${pmCheckerLog }">
				<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('pmChecker','pmCheckerLog')" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('pmChecker','pmCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		
		<li>
			<div class="syq_2"><span>研发核准</span></div>
			<div class="polling_sell_2">
				<input type="text" name="developChecker" id="developChecker" value="${developChecker }">
				<input type="hidden" name="developCheckerLog" id="developCheckerLog" value="${developCheckerLog }">
				<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('developChecker','developCheckerLog')" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('developChecker','developCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>			
		</li>
		<li>
			<div class="syq_2"><span>品保核准</span></div>
			<div class="polling_sell_2">
				<input type="text" name="qualityChecker" id="qualityChecker" value="${qualityChecker }">
				<input type="hidden" name="qualityCheckerLog" id="qualityCheckerLog" value="${qualityCheckerLog }">
				<a id="polling_fdj" class="polling_fdj"  onclick="searchPerson('qualityChecker','qualityCheckerLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('qualityChecker','qualityCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>				
		</li>
		
		<li>
			<div class="syq_2"><span>QS核准</span></div>
			<div class="polling_sell_2">
				<input type="text" name="qsChecker" id="qsChecker" value="${qsChecker }">
				<input type="hidden" name="qsCheckerLog" id="qsCheckerLog" value="${qsCheckerLog }">
				<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('qsChecker','qsCheckerLog')" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('qsChecker','qsCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>				
		</li>
	</ul>
 </div>
 <div class="admit_infor">
	<ul>
		<li>签核意见</li>
		<li class="admit_infor_li"><input type="checkbox" name="down_select" id="down_select"><img src="${ctx}/mobile/img/ht.png"></li>
	</ul>
 </div>
 <textarea id="assess"></textarea>
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
 <script>
	$('#down_select').click(function(){
		$('#assess').slideToggle();
	});
	$("#processFormId").click(function(){
		 $("#processForm").hide();
		 $("#processFormNk").hide();
	});
</script>
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
</body>
</html>
