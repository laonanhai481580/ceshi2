<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.ambition.carmfg.checkinspection.web.MfgPatrolInspectionReportAction"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@page import="com.ambition.carmfg.entity.MfgCheckInspectionReport"%>
<%@page import="com.ambition.util.common.CommonUtil1"%>
<%@page import="com.norteksoft.product.util.PropUtils"%>
<%@page import="java.util.Date"%>
<%@page import="com.ambition.supplier.utils.DateUtil"%>
<%@page import="com.ambition.carmfg.entity.MfgPatrolItem"%>
<%@page import="com.ambition.carmfg.entity.MfgInspectingItem"%>
<%@page import="com.ambition.carmfg.entity.MfgCheckItem"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.ambition.iqc.entity.SampleScheme"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.ambition.iqc.entity.InspectingItem"%>
<%@page import="com.ambition.iqc.entity.SampleCodeLetter"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String sampleSchemeType = SampleScheme.ORDINARY_TYPE;
	List<MfgCheckItem> checkItems = (List<MfgCheckItem>)request.getAttribute("checkItems");
	//处理旧的统计记录
	List<MfgPatrolItem> patrolItems = (List<MfgPatrolItem>)request.getAttribute("patrolItems");
	List<String> patrolStrs = new ArrayList<String>();
	Map<String,String> patrolMap = new HashMap<String,String>();//保存对应的结果
	Map<String,String> spcSampleIdsMap = new HashMap<String,String>();//保存spcSamples
	Map<String,Long> patrolItemMap = new HashMap<String,Long>();//保存spcSamples
	if(patrolItems == null){
		patrolItems = new ArrayList<MfgPatrolItem>();
	}
	Map<String,Boolean> existMap = new HashMap<String,Boolean>();
	Map<String,String> remarkMap = new HashMap<String,String>();
	Date lastDate = null;
	String lastStr = "";
	for(MfgPatrolItem item : patrolItems){
		String dateTimeStr = DateUtil.formateTimeStr(item.getInspectionDate());
		remarkMap.put(dateTimeStr,item.getRemark());
		lastStr = dateTimeStr;
		String name = item.getCheckItemName() + "_" + dateTimeStr;
		if(!existMap.containsKey(dateTimeStr)){
	patrolStrs.add(dateTimeStr);
	existMap.put(dateTimeStr,true);
	lastDate = item.getInspectionDate();
		}
		patrolMap.put(name,item.getResult());
		spcSampleIdsMap.put(name,item.getSpcSampleIds());
		patrolItemMap.put(name,item.getId());
	}
	//判断是否需要新的添加新的巡检记录(30分钟自动添加巡检记录)
	boolean isAdd = Boolean.FALSE;
	String patrolState = (String)request.getAttribute("patrolState");
	if(StringUtils.isEmpty(patrolState)){
		patrolState = "巡检中";
	}
	if(!"巡检中".equals(patrolState)){
		isAdd = false;
	}else if(lastDate == null){
		lastDate = new Date();
		isAdd = true;
	}else{
		Date nextPatrolDate = (Date)ActionContext.getContext().getValueStack().findValue("nextPatrolDate");
		if(nextPatrolDate==null){
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(lastDate);
	calendar.add(Calendar.MINUTE,(int)(Double.valueOf(PropUtils.getProp("mfg.patrol.interval"))*60));
	nextPatrolDate = calendar.getTime();
		}
		//巡检间隔时间(单位:小时)
		if(System.currentTimeMillis()>=nextPatrolDate.getTime()){
	isAdd = true;
		}
	}
	if(isAdd){
		lastStr = DateUtil.formateTimeStr(new Date());
		patrolStrs.add(lastStr);	
	}
	//保存模式,巡检模式的单子不能修改先前的记录,历史记录模式的单子可以修改原先的记录
	String saveMode = MfgPatrolInspectionReportAction.getCurrentSaveMode();
	//检验数量
	Integer patrolNumber = 3;
	try{
		patrolNumber = Integer.valueOf(PropUtils.getProp("mfg.patrol.number"));
	}catch(Exception e){}
%>
<table class="form-table-border-left" style="border:0px;width:300px;table-layout:fixed;">
	<thead>
		<tr>
			<td style="width:20px;text-align:center;border-top:0px;border-left:0px;">NO</td>
			<td style="width:20px;text-align:center;border-top:0px;">分类</td>
			<td style="width:220px;text-align:center;border-top:0px;">检查项目</td>
			<td style="width:70px;text-align:center;border-top:0px;">检查方法</td>
			<td style="width:60px;text-align:center;border-top:0px;">统计类型</td>
			<!-- <td style="width:60px;text-align:center;border-top:0px;">检验类别</td>
			<td style="width:45px;text-align:center;border-top:0px;">抽检数量</td>
			<td style="width:90px;text-align:center;border-top:0px;">规格</td> -->
			<td style="width:60px;text-align:center;border-top:0px;">培训资料</td>
			<%
				for(String patrolStr : patrolStrs){
			%>
				<td style="padding:0px;width:200px;text-align:center;border-top:0px;" class="checkItemsHeader" valign="middle">
					<input type="hidden" value="<%=patrolStr%>" class="patrolDateTime"/>
					<%
						if("巡检中".equals(patrolState)&&MfgCheckInspectionReport.SAVE_MODE_HISTORY.equals(saveMode)){
					%>
					<input type="text" style="width:110px;" hisValue="<%=patrolStr%>" value="<%=patrolStr%>" patrolDateTimeShow="patrolDateTimeShow" class="{required:true,messages:{required:'巡检日期必填!'}}"/>
					<%
						}else{
					%>
					<span><%=patrolStr%></span>
					<%
						}
					%>
				</td>
			<%
				}
			%>
		</tr>
	</thead>
	<tbody>
		<%
			List<Option> inspectionLevels = (List<Option>)request.getAttribute("inspectionLevels");
			if(inspectionLevels == null){
				inspectionLevels = new ArrayList<Option>();
			}
			Map<String,String> levelMap = new HashMap<String,String>();
			for(Option option : inspectionLevels){
				levelMap.put(option.getValue(),option.getName());
			}
			int i=1,flag = 0;
			boolean isLast = false;
			String inspectionDateStr = DateUtil.formateDateStr(lastDate);
			Map<String,String> conclusionMap = new HashMap<String,String>();
			Map<String,Boolean> editMap = new HashMap<String,Boolean>();
			for(MfgCheckItem checkItem : checkItems){
				flag++;
				if(flag==checkItems.size()){
					isLast = true;
				}
				String checkItemName = checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","");
				String countType = StringUtils.isEmpty(checkItem.getCountType())?InspectingItem.COUNTTYPE_COUNT:checkItem.getCountType();
		%>
				<tr class="patrolItemTr">
				<td style="text-align: center;border-left:0px;"><%=i++%></td>
 				<%
 					if(checkItem.getParentItemName() != null && !checkItem.getParentRowSpan().equals(1)){
 				%>  
 				<td rowspan="<%=checkItem.getParentRowSpan()%>" style="text-align: center;"><%=checkItem.getParentItemName()==null?"": checkItem.getParentItemName()%></td> 
 				<%
  					} else if(checkItem.getParentItemName() != null && checkItem.getParentRowSpan().equals(1)){
  				%>
				<td style="text-align: center;"><%=checkItem.getParentItemName()==null?"": checkItem.getParentItemName()%></td>
				<%
					}
				%>
				<td style="text-align: center;" valign="middle">
					<input type="hidden" name="unit" value="<%=checkItem.getUnit()==null?"":checkItem.getUnit()%>"/>
					<input type="hidden" name="minlimit" value="<%=checkItem.getMinlimit()==null?"":checkItem.getMinlimit()%>"/>
					<input type="hidden" name="maxlimit" value="<%=checkItem.getMaxlimit()==null?"":checkItem.getMaxlimit()%>"/>
					<input type="hidden" name="parentRowSpan" value="<%=checkItem.getParentRowSpan()==null?"":checkItem.getParentRowSpan()%>"/>
					<input type="hidden" name="parentItemName" value="<%=checkItem.getParentItemName()==null?"":checkItem.getParentItemName()%>"/>
					<input type="hidden" name="checkItemName" value="<%=checkItemName%>"/>
					<input type="hidden" name="codeLetter" value="<%=checkItem.getCodeLetter()==null?"":checkItem.getCodeLetter()%>"/>
					<input type="hidden" name="inspectionType" value="<%=checkItem.getInspectionType()==null?"":checkItem.getInspectionType()%>"/>
					<input type="hidden" name="countType" value="<%=countType%>"/>
					<input type="hidden" name="aql" value="<%=checkItem.getAql()==null?"":checkItem.getAql()%>"/>
					<input type="hidden" name="aqlAc" value="<%=checkItem.getAqlAc()==null?"":checkItem.getAqlAc()%>"/>
					<input type="hidden" name="aqlRe" value="<%=checkItem.getAqlRe()==null?"":checkItem.getAqlRe()%>"/>
					<input type="hidden" name="inspectionAmount" value="<%=checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount()%>"/>
					<input type="hidden" name="inspectionLevel" value="<%=checkItem.getInspectionLevel()%>"/>
					<input type="hidden" name="attachmentFiles" value="<%=checkItem.getAttachmentFiles()==null?"":checkItem.getAttachmentFiles().replaceAll("\n","")%>"/>
					<input type="hidden" name="specifications" value="<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications()%>"/>
					<input type="hidden" name="featureId" value="<%=checkItem.getFeatureId()==null?"":checkItem.getFeatureId()%>"/>
					<%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%>
				</td>
				<td style="text-align:center;">
					<input type="hidden" name="checkMethod" value="<%=checkItem.getCheckMethod()==null?"":checkItem.getCheckMethod()%>"/>
					<%=checkItem.getCheckMethod()==null?"":checkItem.getCheckMethod()%>
				</td>
				<td style="text-align: center;">
					<%=countType%>
				</td>
				<!-- <td style="text-align:center;">
					<%=checkItem.getInspectionLevel()==null?"":(levelMap.containsKey(checkItem.getInspectionLevel())?levelMap.get(checkItem.getInspectionLevel()):checkItem.getInspectionLevel())%>
				</td>
				<td style="text-align: center;">
					<%=checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount()%>
				</td>
				<td style="text-align: center;">
					<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications()%>
				</td> -->
				<td style="text-align: center;" valign="middle" class="attachmentFiles" >
					<%
						if(StringUtils.isNotEmpty(checkItem.getAttachmentFiles())){
					%>
						<a class="small-button-bg" href="javascript:void(0);" title="查看培训资料"><span class="ui-icon ui-icon-notice" style='cursor:pointer;'></span></a>					
					<%
											}
										%>
				</td>
				<%
					for(String patrolStr : patrolStrs){
								String patrolName = checkItemName + "_" + patrolStr;
								boolean isInput = true;
								editMap.put(patrolStr,isInput);
				%>
					<td style="padding:0px;text-align:center;" class="checkItemsClass">
						<input type="hidden" value="<%=spcSampleIdsMap.containsKey(patrolName)?spcSampleIdsMap.get(patrolName):""%>" patrolSpcSampleIds="<%=patrolName%>"/>
						<input type="hidden" value="<%=patrolItemMap.containsKey(patrolName)?patrolItemMap.get(patrolName):""%>" patrolItemId="<%=patrolName%>"/>
						<%
							String str = patrolMap.get(patrolName);
											if(StringUtils.isEmpty(str)){
												str = "";
											}
											if(!"巡检中".equals(patrolState)||MfgCheckInspectionReport.SAVE_MODE_PATROL.equals(saveMode)&&!lastStr.equals(patrolStr)){
												isInput = false;
												editMap.put(patrolStr,isInput);
						%>
							<%=""%>
						<%
							}
											String values[] = str.split(",");
											Double maxlimit = checkItem.getMaxlimit(),minlimit = checkItem.getMinlimit();
											//conclusionMap.put(patrolStr,"合格");
											for(int j=0;j<patrolNumber;j++){
												String value = j<values.length?values[j]:"";
												if(InspectingItem.COUNTTYPE_METERING.equals(checkItem.getCountType())){
													String color = "black";
													if(CommonUtil1.isNumber(value)){
														Double val = Double.valueOf(value);
														if(maxlimit!=null&&minlimit!=null){
															if(val<minlimit||val>maxlimit){
																color="red";
																conclusionMap.put(patrolStr,"不合格");
															}
														}
													}else{
														value = "";
													}
													if(isInput){
						%>
								<input color="<%=color%>" style="width:55px;color:<%=color%>" title="<%=checkItem.getCheckItemName() + "样品" + (j+1) %>  min:<%=minlimit %>&max:<%=maxlimit %>" value="<%=value==null?"":value%>" class="{number:true,min:0,messages:{number:'必须是数字!'}}" patrolName="<%=patrolName%>" patrolTime="<%=patrolStr%>"></input>
							<%
							}
								}else{
									if("NG".equals(value)){
										conclusionMap.put(patrolStr,"不合格");
									}
									if(isInput){
							%>
								<select style="width:60px;"title="<%=checkItem.getCheckItemName() + "样品" + (j+1) %>" name="<%=checkItem.getCheckItemName()%>" patrolName="<%=patrolName%>" patrolTime="<%=patrolStr%>">
									<option value="OK" <%="OK".equals(value)?"selected='selected'":""%>>OK</option>
									<option value="NG" <%="NG".equals(value)?"selected='selected'":""%>>NG</option>
								</select>
							<%	
								  }
							  } 
							}
							%>
					</td>
				<%} %>
			</tr>
		<%} 
			//如果检验项目不为空,增加检验判定
			if(!checkItems.isEmpty()){
		%>
			<tr class="patrolItemConclusion">
			<td colspan="6" style="border-left:0px;text-align:center;">检验判断</td>
			<%
				for(String patrolStr : patrolStrs){
					String conclusion = conclusionMap.get(patrolStr);
					if(StringUtils.isEmpty(conclusion)){
						conclusion = "合格";
					}
			%>
				<td style="padding:0px;text-align:center;" class="checkItemConclusion">
					<input type="hidden" value="<%=conclusion %>" patrolConclusion="<%=patrolStr%>"/>
					<span patrolConclusionSpan="<%=patrolStr%>">
						<%=conclusion %>
					</span>
				</td>
			<%} %>
			</tr>
			<tr class="patrolItemRemarkTr">
			<td colspan="6" style="border-left:0px;text-align:center;">备注</td>
			<%
				for(String patrolStr : patrolStrs){
					String conclusion = conclusionMap.get(patrolStr);
					if(StringUtils.isEmpty(conclusion)){
						conclusion = "合格";
					}
			%>
				<td style="padding:0px;text-align:center;" class="patrolItemRemark">
					<%
						String remark = remarkMap.get(patrolStr);
						remark = remark==null?"":remark;
						if(editMap.get(patrolStr)){
					%>
						<textarea rows="4" style="width:99%;" patrolRemark="<%=patrolStr%>" class="{maxlen:255,messages:{maxlen:'最长不能超过255个字符!'}}"><%=remark %></textarea>						
					<%}else{ %>
						<input type="hidden" patrolRemark="<%=patrolStr%>" value="<%=remark %>"></input>
						<span><%=remark %></span>
					<%} %>
				</td>
			<%} %>
			</tr>
		<%} %>
		<script type="text/javascript">
			$(document).ready(function(){
				//产品图片
				var imgFileId = '<%=request.getAttribute("imgFileId")==null?"":request.getAttribute("imgFileId")%>';
				var imgWidth = '<%=request.getAttribute("imgFileWidth")%>';
				var imgHeight = '<%=request.getAttribute("imgFileHeight")%>';
				var hisId = $("#imgFileId").val();
				if(hisId != imgFileId){
					$("#imgFileId").val(imgFileId);
					//绑定图片事件
					if(imgFileId){
						var pHeight = $("#img").closest("td").height()-4;
						var pWidth = $("#img").closest("td").width()-20;
						var rate = imgWidth/imgHeight;
						if(imgHeight>pHeight){
							imgHeight = pHeight;
							$("#img").height(pHeight);
							imgWidth = pHeight*rate;
							$("#img").width(imgWidth);
						}else{
							$("#img").width(imgWidth).height(imgHeight);
						}
						//设置定位
						var left = ((pWidth-imgWidth)/2+2);
						var top = ((pHeight-imgHeight)/2+2);
						$("#img").closest("span").css({
							left:left+"px",
							top:top+"px"
						});
						var downloadPath = $.getDownloadPath(imgFileId);
						var zoom = $('#img').closest("a");
	                    zoom.data('zoom').destroy();
	                    zoom.attr('href',downloadPath);
	                    $('#img').attr('src',downloadPath);
	                    zoom.CloudZoom({
	                    	zoomWidth:400,
	                    	zoomHeight:400
	                    });
					}
				}
				//培训资料
				var isView = '<%=request.getAttribute("isView")==null?"":request.getAttribute("isView")%>';
				$("td.attachmentFiles a").mouseover(function(){
					var attachmentFiles = $(this).closest("tr").find(":input[name=attachmentFiles]").val();
					$(".content").html($.getDownloadHtml(attachmentFiles).replace(",","<br/>"));
					var p = $(this).offset();
					if(isView){
						p.left = p.left - 20;
						p.top = p.top + 30;
					}else{
						var west = $(".ui-layout-west:visible").length;
						var north = $(".ui-north:visible").length;
						p.left = p.left - 26 - (west>0?$(".ui-layout-west").width():0);
						p.top = p.top - (north>0?$(".ui-north").height():0);
					}
					$("div.container").css({
						left:p.left,
						top:p.top,
						visibility:"visible"
					}).show();
					$(".content").find("a").mouseover(function(){
						$("div.container").show();
					});
				});
				//补全空表格
				checkEmpty();
				//设置巡检模式
				$("#saveMode").val("<%=saveMode%>");
				//设置时间格式选择
				bindDateTimeChange($(":input[patrolDateTimeShow]"));
				//设置检验标准的信息
				var isRequestItems = '${isRequestItems}';
				if(isRequestItems){
					var is3C = '${is3C}',isStandard = '${isStandard}',iskeyComponent = '${iskeyComponent}',standardVersion = '${standardVersion}';
					$(":input[name=is3C]").val(is3C).closest("td").find("span").html(is3C);
					$(":input[name=isStandard]").val(isStandard).closest("td").find("span").html(isStandard);
					$(":input[name=iskeyComponent]").val(iskeyComponent).closest("td").find("span").html(iskeyComponent);
					$(":input[name=standardVersion]").val(standardVersion).closest("td").find("span").html(standardVersion);
				}
			});
		</script>
	</tbody>
</table>
<input type="hidden" name="patrolSettings.timeIntervalType" value="${patrolSettings.timeIntervalType}"/>
<input type="hidden" name="patrolSettings.timeIntervalValue" value="${patrolSettings.timeIntervalValue}"/>
<input type="hidden" name="patrolSettings.remindSwitch" value="${patrolSettings.remindSwitch}"/>
<input type="hidden" name="patrolSettings.remindTimeType" value="${patrolSettings.remindTimeType}"/>
<input type="hidden" name="patrolSettings.remindTimeValue" value="${patrolSettings.remindTimeValue}"/>
<input type="hidden" name="patrolSettings.receiveTypes" value="${patrolSettings.receiveTypes}"/>
<input type="hidden" name="patrolSettings.receiveUserIds" value="${patrolSettings.receiveUserIds}"/>
<input type="hidden" name="patrolSettings.triggerValue" value="${patrolSettings.triggerValue}"/>
<input type="hidden" name="patrolSettings.triggerType" value="${patrolSettings.triggerType}"/>