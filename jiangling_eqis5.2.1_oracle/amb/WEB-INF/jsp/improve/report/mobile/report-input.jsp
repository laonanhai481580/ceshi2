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
<title>8D纠正及预防措施报告</title>
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
		<span>8D纠正及预防措施报告</span>
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
					<input type="date" name="happenDate" id="happenDate" class="text-inp" value="<s:date name='happenDate' format="yyyy-MM-dd"/>"/>
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>8D分析引导者</span>
				</td>
				<td class="td_right">
					<input type="text" name="sponsor" id="sponsor" class="text-inp" value="${sponsor}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>产品型号</span>
				</td>
				<td class="td_right">
					<input type="text" name="productModel" id="productModel" class="text-inp"  value="${productModel}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>产品阶段</span>
				</td>
				<td class="td_right">
					<s:select list="productPhases"
						listKey="value" 
						listValue="value" 
						name="productPhase" 
						id="productPhase" 
						emptyOption="true"
						onchange=""
						cssClass="text-inp"
						theme="simple">
					</s:select>				
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>发生区</span>
				</td>
				<td class="td_right">
					<input type="text" name="hanppenArea" id="hanppenArea" class="text-inp"  value="${hanppenArea}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>客户名称</span>
				</td>
				<td class="td_right">
					<input type="text" name="customerName" id="customerName" class="text-inp"  value="${customerName}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>问题归属</span>
				</td>
				<td class="td_right">
					<s:select list="problemBelongs"
						listKey="value" 
						listValue="value" 
						name="problemBelong" 
						id="problemBelong" 
						emptyOption="true"
						onchange=""
						cssClass="text-inp"
						theme="simple">
					</s:select>		
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>问题来源</span>
				</td>
				<td class="td_right">
					<select name="problemSource" id="problemSource" class="method"  value="${productModel}">
						<option value=""></option>
						<option ty="1" value="客户端IQC">客户端IQC</option>
						<option ty="1" value="客户端上线">客户端上线</option>
						<option ty="1" value="客户端OQC">客户端OQC</option>
						<option ty="1" value="客户端市场退机">客户端市场退机</option>
						<option ty="1" value="客户端RA实验">客户端RA实验</option>
						
						<option ty="2" value="厂内设计评审">厂内设计评审</option>
						<option ty="2" value="厂内IQC异常">厂内IQC异常</option>					
						<option ty="2" value="厂内制程异常">厂内制程异常</option>
						<option ty="2" value="厂内RA验证异常">厂内RA验证异常</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>lot no.</span>
				</td>
				<td class="td_right">
					<input type="text" name="lotNo" id="lotNo" class="text-inp"  value="${lotNo}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>问题类型</span>
				</td>
				<td class="td_right">
					<s:select list="problemTypes"
						listKey="value" 
						listValue="value" 
						name="problemType" 
						id="problemType" 
						emptyOption="true"
						onchange=""
						cssClass="text-inp"
						theme="simple">
					</s:select>					
					</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>问题严重度</span>
				</td>
				<td class="td_right">
					<s:select list="problemDegrees"
						listKey="value" 
						listValue="value" 
						name="problemDegree" 
						id="problemDegree" 
						emptyOption="true"
						onchange=""
						cssClass="text-inp"
						theme="simple">
					</s:select>					
					</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>不良率</span>
				</td>
				<td class="td_right">
					<input type="text" name="unqualifiedRate" id="unqualifiedRate" class="text-inp"  value="${unqualifiedRate}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>生产事业群</span>
				</td>
				<td class="td_right">
					<s:select list="productionEnterpriseGroups"
						listKey="value" 
						listValue="value" 
						name="productionEnterpriseGroup" 
						id="productionEnterpriseGroup" 
						emptyOption="true"
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
					<s:select list="businessUnits"
						listKey="value" 
						listValue="value" 
						name="businessUnitCode" 
						id="businessUnitCode" 
						emptyOption="true"
						onchange=""
						cssClass="text-inp"
						theme="simple">
					</s:select>					
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>问题点</span>
				</td>
				<td class="td_right">
					<input type="text" name="problemPoint" id="problemPoint" class="text-inp"  value="${problemPoint}">
				</td>
			</tr>		
		</tbody>
	</table>
	<div class="problem">
		<ul>
			<li class="purchase">
				<p class="topdescribe">D1 Problem/Defect Desoription（问题/缺陷确认描述）</p>
			</li>
			<li class="opinion">
				<div class="opinion_div_a">描述</div>
				<div class="opinion_div_b">
					<input type="checkbox" name="selec_tdown" class="teatareadown" onclick="selectdown('problemDescrible')">
					<img src="${ctx}/mobile/img/ht.png">
				</div>
			</li>
		</ul>
	</div>
	 <textarea id="problemDescrible" name="problemDescrible" class="writetextarea">${problemDescrible}</textarea>		
	<div class="problem">
		<ul>
			<li class="purchase">
				<p class="topdescribe">D2 From the team(建立团队)</p>
			</li>
			<li>
				<div >负责人</div>
				<div id="select_man">
					<input type="text" name="dutyManD2" id="dutyManD2" value="${dutyManD2 }">
					<input type="hidden" name="dutyManD2Login" id="dutyManD2Login" value="${dutyManD2Login }">
					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('dutyManD2','dutyManD2Login')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<sub>	
							<b>*</b>
						</sub>
					<a id="polling_ljt" onclick="del('dutyManD2','dutyManD2Login')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a>
				</div>				
			</li>			
			<li>
				<div class="opinion_div_1">部门</div>
				<div class="opinion_div_2"><input type="text" name="deprtmentD2" id="deprtmentD2" class="text-inp"  value="${deprtmentD2}"></div>
			</li>
			<li>
				<div class="opinion_div_1">预计完成时间</div>
				<div class="opinion_div_2"><input type="date" name="planDateD2" id="planDateD2" class="text-inp"  value="<s:date name='planDateD2' format="yyyy-MM-dd"/>"></div>
			</li>
		</ul>
	</div>
	<div class="testoperation">
		<div class="number">
			<p>操作</p>
			<p>
				<i style="background-image:url(${ctx}/mobile/img/add.png);" id="addnum"></i>
				<i style="background-image:url(${ctx}/mobile/img/jh.png);" id="delnum"></i>
			</p>
		</div>
		<div class="textcontent" id="addtextcontent">
			<ul class="ulcontent">
				<li id="addcontent_a"><span>部门</span><input type="text" name="content_fir" id="content_fir" class="textwidth" ></li>
				<li id="addcontent_b"><span>责任人</span><input type="text" name="content_sec" id="content_sec" class="textwidth"></li>
				<li id="addcontent_c"><span>备注</span><input type="text" name="content_thir" id="content_thir" class="textwidth"></li>
			</ul>
		</div>
		<script>
			
			$('#addnum').click(function(){
				var node1=document.getElementById('addtextcontent');
				var node2=document.createElement('ul');
				node2.class="ulcontent";
				node2.innerHTML="<li id='addcontent_a'><span>部门</span><input type='text' class='textwidth'></li><li id='addcontent_b'><span>责任人</span><input type='text' class='textwidth'></li><li id='addcontent_b'><span>备注</span><input type='text' class='textwidth'></li>"
				node1.insertBefore(node2,node1.children[0]);
			});
			$('#delnum').click(function(){
				var y=document.getElementById('addtextcontent').getElementsByTagName('ul');
				var z=y.length;
				if(z > 1){
					document.getElementById('addtextcontent').removeChild(y[1]);
				}
			});
		</script>
	</div>
	<div class="problem">
		<ul>
			<li class="purchase">
				<p class="topdescribe">D3 Contaiment Action(s)(应急措施)</p>
			</li>
			<li>
				<div class="opinion_div_1">客户端库存</div>
				<div class="opinion_div_2"><input type="text" name="clientStore" id="clientStore" class="text-inp"  value="${clientStore}"></div>
			</li>
			<li>
				<div class="opinion_div_1">在途数量</div>
				<div class="opinion_div_2"><input type="text" name="onOrder" id="onOrder" class="text-inp"  value="${onOrder}"></div>
			</li>
			<li>
				<div class="opinion_div_1">内部成品仓库存数量</div>
				<div class="opinion_div_2"><input type="text" name="innerStore" id="innerStore" class="text-inp"  value="${innerStore}"></div>
			</li>
			<li>
				<div class="opinion_div_1">内部在线数量</div>
				<div class="opinion_div_2"><input type="text" name="innerOnOrder" id="innerOnOrder" class="text-inp"  value="${innerOnOrder}"></div>
			</li>
			<li>
				<div class="opinion_div_1">RMA仓库存数量</div>
				<div class="opinion_div_2"><input type="text" name="rmaStore" id="rmaStore" class="text-inp"  value="${rmaStore}"></div>
			</li>
			<li>
				<div class="opinion_div_1">原材料仓</div>
				<div class="opinion_div_2"><input type="text" name="materialStore" id="materialStore" class="text-inp"  value="${materialStore}"></div>
			</li>
			<li class="opinion">
				<div class="opinion_div_a">应急措施</div>
				<div class="opinion_div_b">
					<input type="checkbox" name="selec_tdown" class="teatareadown" onclick="selectdown('emergencyMeasures')" >
					<img src="${ctx}/mobile/img/ht.png">
				</div>
			</li>
		</ul>
	</div>
	<textarea id="emergencyMeasures" name="emergencyMeasures" class="writetextarea">${emergencyMeasures}</textarea>
	<div class="problem">
		<ul>
			<li class="purchase">
				<p class="topdescribe">D4 Tdentify Rause of the Problem（识别问题根源）</p>
			</li>
			<li>
				<div class="opinion_div_1">负责人</div>
				<div class="opinion_div_2"><input type="text" name="dutyManD4" id="dutyManD4" class="text-inp"  value="${productModel}"></div>
			</li>
			<li>
				<div>负责人</div>
				<div id="select_man">
					<input type="text" name="dutyManD4" id="dutyManD4" value="${dutyManD4 }">
					<input type="hidden" name="dutyManD4Login" id="dutyManD4Login" value="${dutyManD4Login }">
					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('dutyManD4','dutyManD4Login')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<sub>	
							<b>*</b>
						</sub>
					<a id="polling_ljt" onclick="del('dutyManD4','dutyManD4Login')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a>
				</div>				
			</li>			
			<li>
				<div class="opinion_div_1">部门</div>
				<div class="opinion_div_2"><input type="text" name="deprtmentD4" id="deprtmentD4" class="text-inp"  value="${deprtmentD4}"></div>
			</li>
			<li>
				<div class="opinion_div_1">预计完成时间</div>
				<div class="opinion_div_2"><input type="date" name="planDateD4" id="planDateD4" class="text-inp"  value="<s:date name='planDateD4' format="yyyy-MM-dd"/>"></div>
			</li>
			<li >
				<div >方法</div>
				<div >
					&nbsp;&nbsp;&nbsp;<s:checkboxlist
						theme="simple" list="methods" listKey="value" listValue="name"
						name="method" value="method">
					</s:checkboxlist>
				</div>
			</li>
			<li>
				<div class="opinion_div_1">原因分类</div>
				<div class="opinion_div_2">
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
				<div class="opinion_div_1">责任部门</div>
				<div class="opinion_div_2">
					<s:select list="departments"
						listKey="value" 
						listValue="name" 
						name="dutyDept" 
						id="dutyDept" 
						cssStyle="width:140px;"
						onchange=""
						cssClass="text-inp"
						theme="simple">
					</s:select>	
				</div>
			</li>
 			<li class="other_li" id="upload">
				<div class="syq_2"><span>附件</span></div>
				<div class="upload_b" >
				<span class="upload_span" style="float: left;"><a class="fileClick"  onclick="_uploadFiles('showattachmentD4','attachmentD4');">上传附件</a></span>
				<input type="hidden" name="attachmentD4" id="attachmentD4" value="${attachmentD4}">
				<input type="hidden" name="hisattachmentD4" id="hisattachmentD4" value="${hisattachmentD4}"><br/>
				<p id="showattachmentD4"></p>
				</div>
			</li>			
			<li class="opinion">
				<div class="opinion_div_a">原因</div>
				<div class="opinion_div_b">
					<input type="checkbox" name="selec_tdown" class="teatareadown" onclick="selectdown('remarkD4')">
					<img src="${ctx}/mobile/img/ht.png">
				</div>
			</li>
		</ul>
	</div>
	<textarea id="remarkD4" name="remarkD4" class="writetextarea"></textarea>
	<div class="problem">
		<ul>
			<li class="purchase">
				<p class="topdescribe">D5  Formulate Corrective Actions（制定纠正措施）</p>
			</li>
 			<li class="other_li" id="upload">
				<div class="syq_2"><span>附件</span></div>
				<div class="upload_b" >
				<span class="upload_span" style="float: left;"><a class="fileClick"  onclick="_uploadFiles('showattachmentD5','attachmentD5');">上传附件</a></span>
				<input type="hidden" name="attachmentD5" id="attachmentD5" value="${attachmentD5}">
				<input type="hidden" name="hisattachmentD5" id="hisattachmentD5" value="${hisattachmentD5}"><br/>
				<p id="showattachmentD5"></p>
				</div>
			</li>				
			<li class="opinion">
				<div class="opinion_div_a">应急措施</div>
				<div class="opinion_div_b">
					<input type="checkbox" name="selec_tdown" class="teatareadown" onclick="selectdown('remarkD5')">
					<img src="${ctx}/mobile/img/ht.png">
				</div>
			</li>
		</ul>
	</div>
	<textarea id="remarkD5" name="remarkD5" class="writetextarea"></textarea>
	<div class="problem">
		<ul>
			<li class="purchase">
				<p class="topdescribe">D6  Verify Corrective Actions（验证纠正措施）</p>
			</li>
			<li>
				<div>负责人</div>
				<div id="select_man">
					<input type="text" name="dutyManD6" id="dutyManD6" value="${dutyManD6 }">
					<input type="hidden" name="dutyManD6Login" id="dutyManD6Login" value="${dutyManD6Login }">
					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('dutyManD6','dutyManD6Login')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<sub>	
							<b>*</b>
						</sub>
					<a id="polling_ljt" onclick="del('dutyManD6','dutyManD6Login')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a>
				</div>				
			</li>			
			<li>
				<div class="opinion_div_1">部门</div>
				<div class="opinion_div_2"><input type="text" name="deprtmentD6" id="deprtmentD6" class="text-inp" value="${deprtmentD6 }"></div>
			</li>
			<li>
				<div class="opinion_div_1">预计完成时间</div>
				<div class="opinion_div_2"><input type="date" name="planDateD6" id="planDateD6" class="text-inp" value="<s:date name='planDateD6' format="yyyy-MM-dd"/>"></div>
			</li>
 			<li class="other_li" id="upload">
				<div class="syq_2"><span>附件</span></div>
				<div class="upload_b" >
				<span class="upload_span" style="float: left;"><a class="fileClick"  onclick="_uploadFiles('showattachmentD6','attachmentD6');">上传附件</a></span>
				<input type="hidden" name="attachmentD6" id="attachmentD6" value="${attachmentD6}">
				<input type="hidden" name="hisattachmentD6" id="hisattachmentD6" value="${hisattachmentD6}"><br/>
				<p id="showattachmentD6"></p>
				</div>
			</li>			
			<li class="opinion">
				<div class="opinion_div_a">验证纠正措施</div>
				<div class="opinion_div_b">
					<input type="checkbox" name="selec_tdown" class="teatareadown" onclick="selectdown('remarkD6')">
					<img src="${ctx}/mobile/img/ht.png">
				</div>
			</li>
		</ul>
	</div>
	<textarea id="remarkD6" name="remarkD6" class="writetextarea"></textarea>
	<div class="problem">
		<ul>
			<li class="purchase">
				<p class="topdescribe">D7  Preventive Action(s)（预防措施）</p>
			</li>
 			<li class="other_li" id="upload">
				<div class="syq_2"><span>附件</span></div>
				<div class="upload_b" >
				<span class="upload_span" style="float: left;"><a class="fileClick"  onclick="_uploadFiles('showattachmentD7','attachmentD7');">上传附件</a></span>
				<input type="hidden" name="attachmentD7" id="attachmentD7" value="${attachmentD7}">
				<input type="hidden" name="hisattachmentD7" id="hisattachmentD7" value="${hisattachmentD7}"><br/>
				<p id="showattachmentD7"></p>
				</div>
			</li>				
			<li class="opinion">
				<div class="opinion_div_a">预防措施</div>
				<div class="opinion_div_b">
					<input type="checkbox" name="selec_tdown" class="teatareadown" onclick="selectdown('remarkD7')">
					<img src="${ctx}/mobile/img/ht.png">
				</div>
			</li>
		</ul>
	</div>
	<textarea id="remarkD7" name="remarkD7" class="writetextarea"></textarea>
	<div class="problem">
		<ul>
			<li class="purchase">
				<p class="topdescribe">D8  效果追踪</p>
			</li>
			<li>
				<div class="opinion_div_1">确认日期</div>
				<div class="opinion_div_2"><input type="date" name="confirmDate" id="confirmDate" class="text-inp" value="<s:date name='confirmDate' format="yyyy-MM-dd"/>"></div>
			</li>
			<li>
				<div class="opinion_div_1">关闭状态</div>
				<div class="opinion_div_2">
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
				<span class="upload_span" style="float: left;"><a class="fileClick"  onclick="_uploadFiles('showattachmentD8','attachmentD8');">上传附件</a></span>
				<input type="hidden" name="attachmentD8" id="attachmentD8" value="${attachmentD8}">
				<input type="hidden" name="hisattachmentD8" id="hisattachmentD8" value="${hisattachmentD8}"><br/>
				<p id="showattachmentD8"></p>
				</div>
			</li>				
			<li>
				<div class="opinion_div_1">结案日期</div>
				<div class="opinion_div_2"><input type="date" name="closeDate" id="closeDate" class="text-inp" value="<s:date name='closeDate' format="yyyy-MM-dd"/>"></div>
			</li>
			<li class="opinion">
				<div class="opinion_div_a">效果追踪</div>
				<div class="opinion_div_b">
					<input type="checkbox" name="selec_tdown" class="teatareadown" onclick="selectdown('remarkD8')">
					<img src="${ctx}/mobile/img/ht.png">
				</div>
			</li>
		</ul>
		<textarea id="remarkD8" name="remarkD8" class="writetextarea"></textarea>
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
	function addoperation(){
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
	}
</script>	
 </body>
</html>
