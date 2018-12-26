<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
<meta content="email=no,telephone=no" name="format-detection"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" type="text/css" href="${ctx}/mobile/css/adapt.css">
<link rel="stylesheet" type="text/css" href="${ctx}/mobile/css/global.css">
<link rel="stylesheet" type="text/css" href="${ctx}/mobile/css/style.css">
<link rel="stylesheet" type="text/css" href="${ctx}/mobile/css/common.css">
<jsp:include page="script.jsp"/>
</head>
<body>
	<form id="applicationForm" name="applicationForm" method="post"
		action="">
		<input type="hidden" name="taskId" value="${taskId}" />
		<input type="hidden" name="id" value="${id}" />
		<input type="hidden" name="inputformortaskform" value="inputform"/>
		<div class="wrap_bg"></div>
		<div class="lg_main">
			<div class="lg_title">
				<a href="#" class="lg_bkbtn mar_lgbtn"></a>
				<h3>材料承认申请表</h3>
			</div>
			<div class="lg_box">
				<label class="agreement">1.1_检验员发起申请</label>
				<div class="lg_int">
					<input type="text" name="productCode" id="productCode"
						value="${productCode}" placeholder="请输入产品代号" class="whit" />
				</div>
				<div class="lg_int">
					<input type="text" name="productNo" id="productNo"
						value="${productNo}" placeholder="请输入产品编号" class="whit" />
				</div>
				<div class="lg_int">
					<input type="text" name="contractNo" id="contractNo"
						value="${contractNo}" placeholder="请输入合同编号" class="whit" />
				</div>
				<div class="lg_int">
					<input type="text" name="submitQty" id="submitQty"
						value="${submitQty}" placeholder="请输入交验数量" class="whit" />
				</div>
				<div>
					<label class="agreement"> 请选择交验工序:</label> 
					<s:select 
						theme="simple" list="submitProcesss" 
						listKey="value" 
						listValue="name" 
						emptyOption="true" 
						id="submitProcess" 
						name="submitProcess" 
						value="submitProcess"></s:select>
				</div>
				<div>
					<label class="agreement">请选择兵种:</label>
					<s:select theme="simple" 
						list="soldierTypes" 
						listKey="value" 
						listValue="name" 
						emptyOption="true" 
						id="soldierType" 
						name="soldierType" 
						value="soldierType"></s:select>
				</div>
				<div class="lg_int">
					<input type="text" name="inspectionConclusion"
						id="inspectionConclusion"
						value="${inspectionConclusion}"
						placeholder="产品厂检的结论" class="whit" />
				</div>
				<div>
					<div class="lg_int">
						<input isUser="true" hiddenInputId="launchManLoginName" 
							treeValue="loginName" type="text" 
							id="launchMan" name="launchMan" value="${launchMan}" />
       					<input type="hidden" id="launchManLoginName" name="launchManLoginName" 
       						value="${launchManLoginName}" 
							placeholder="请输入申请人" class="whit"/>
					</div>
					<div class="lg_int">
						<div class="demo">
                            <div class="lie">
                            	<input isDate=true id="launchDate" class="kbtn datatp datatpw1 whit" name="launchDate" value="<s:date name="launchDate" format="yyyy-MM-dd"/>" placeholder="请输入日期" />
                            </div>
                        </div>
                        <div id="datePlugin"></div>
					</div>
					<div>
						<label class="agreement"> 是否发生过不合格:</label>
							<label class="agreement">
								<input type="radio" name="isHappenMRB"
										value="否" <s:if test="\"否\"==isHappenMRB">checked=checked</s:if>/>否
							</label>
							<label class="agreement">
								<input type="radio" name="isHappenMRB"
										value="是" <s:if test="\"是\"==isHappenMRB">checked=checked</s:if>/>是
							</label>
					</div>
					<div class="lg_int">
						<input isUser="true" hiddenInputId="qualityAuditorLoginName" 
							treeValue="loginName" type="text" id="qualityAuditor" 
							name="qualityAuditor" value="${qualityAuditor}" placeholder="请输入质量部审核人员" class="whit"/>
       					<input type="hidden" id="qualityAuditorLoginName" name="qualityAuditorLoginName" 
       					value="${qualityAuditorLoginName}" />
					</div>
					<div class="lg_int">
						<input isUser="true" hiddenInputId="inspectorLoginName" treeValue="loginName" 
							type="text" id="inspector" name="inspector" 
							value="${inspector}" placeholder="请输入检验员" class="whit"/>
       					<input type="hidden" id="inspectorLoginName" name="inspectorLoginName" value="${inspectorLoginName}" />
					</div>
					<div class="lg_int">
						<input type="text" name="partNumber" id="partNumber"
							 value="${partNumber}"
							 placeholder="请输入产品件号" class="whit" />
					</div>
					<div class="lg_int">
						<input type="text" name="specifications" id="specifications"
							 value="${specifications}"
							 placeholder="请输入规格/型号" class="whit" />
					</div>
					<div class="lg_int">
						<input type="text" name="workshop" id="workshop"
							 value="${workshop}"
							 placeholder="请输入车间" class="whit" />
					</div>
					<div class="lg_int">
						<input type="text" name="inspectionSituation"
							id="inspectionSituation"
							value="${inspectionSituation}"
							placeholder="请输入再提交军检产品的返工及检验情况：" class="whit" />
					</div>
					<div class="lg_box">
						<label class="agreement">2.1_质量检验部门负责人审核</label>
					</div>
					<div>
						<label class="agreement">军检条件:</label><br />
						<label class="agreement">
								<input type="radio" name="qidIsConsistent1"
										value="符合" <s:if test="\"符合\"==qidIsConsistent1">checked=checked</s:if>/>符合提交军检条件，同意提交军检
							</label><br/>
							<label class="agreement">
								<input type="radio" name="qidIsConsistent1"
										value="不符合" <s:if test="\"不符合\"==qidIsConsistent1">checked=checked</s:if>/>不符合提交军检条件，不同意提交军检
							</label>
					</div>
					<div class="lg_int">
						<input type="text" name="qidOpinion1" id="qidOpinion1"
							value="${qidOpinion1}"
							 placeholder="请输入意见" class="whit" />
					</div>
					<div class="lg_int">
						<input isUser="true" hiddenInputId="qidAuditor1LoginName" treeValue="loginName"  type="text" id="qidAuditor1" name="qidAuditor1" value="${qidAuditor1}" placeholder="请输入负责人" class="whit"/>
       					<input type="hidden" id="qidAuditor1LoginName" name="qidAuditor1LoginName" value="${qidAuditor1LoginName}" />
					</div>
					<div class="lg_int">
						<div class="demo">
                            <div class="lie">
                            	<input isDate=true id="qidDate1" name="qidDate1" value="<s:date name="qidDate1" format="yyyy-MM-dd"/>" isDate=true class="kbtn datatp datatpw1 whit" placeholder="请输入日期" />
                            </div>
                        </div>
                        <div id="datePlugin2"></div>
					</div>
					<div class="lg_box">
						<label class="agreement">2.2_质量检验部门负责人审核</label>
					</div>
					<div>
						<label class="agreement">军检条件:</label><br />
						<label class="agreement">
							<input type="radio" name="qidIsConsistent2"
									value="符合" 
									<s:if test="\"符合\"==qidIsConsistent2">checked=checked</s:if>/>不合格品已查明原因，并已采取有效解决措施，满足产品图样和技术文件的要求，再提交军检
						</label><br/>
						<label class="agreement">
							<input type="radio" name="qidIsConsistent2"
									value="不符合" <s:if test="\"不符合\"==qidIsConsistent2">checked=checked</s:if>/>不符合提交军检条件，不同意提交军检
						</label>
					</div>
					<div class="lg_int">
						<input type="text" name="qidOpinion2" id="qidOpinion2"
							value="${qidOpinion2}"
							 placeholder="请输入意见" class="whit" />
					</div>
					<div class="lg_int">
						<input isUser="true" hiddenInputId="qidAuditor2LoginName" treeValue="loginName" type="text" id="qidAuditor2" name="qidAuditor2" value="${qidAuditor2}" placeholder="请输入负责人" class="whit"/>
       					<input type="hidden" id="qidAuditor2LoginName" name="qidAuditor2LoginName" value="${qidAuditor2LoginName}" />
					</div>
					<div class="lg_int">
						<div class="demo">
                            <div class="lie">
                            	<input id="qidDate2" name="qidDate2" value="<s:date name="qidDate2" format="yyyy-MM-dd"/>" 
                            		isDate=true 
                            		class="kbtn datatp datatpw1 whit" placeholder="请输入日期" />
                            </div>
                        </div>
                        <div id="datePlugin3"></div>
					</div>
					<div class="lg_int">
						<input isUser="true" hiddenInputId="militaryAuditorLoginName" treeValue="loginName" type="text" id="militaryAuditor" name="militaryAuditor" value="${militaryAuditor}" placeholder="请输入军代表审核人员" class="whit"/>
       					<input type="hidden" id="militaryAuditorLoginName" name="militaryAuditorLoginName" value="${militaryAuditorLoginName}" />
					</div>
					<div class="lg_box">
						<label class="agreement">3.1_军代表审核</label>
					</div>
					<div class="lg_box">
						<label class="agreement">一.交验条件审查:</label><br /> <label
							class="agreement">1.工厂质量管理体系认证、认定，并保持在有效期内。</label><br />
							
							<label class="agreement">
							<input type="radio" name="scrIsConsistent1"
									value="符合" 
									<s:if test="\"符合\"==scrIsConsistent1">checked=checked</s:if>/>符合
							</label>
							<label class="agreement">
								<input type="radio" name="scrIsConsistent1"
										value="不符合" <s:if test="\"不符合\"==scrIsConsistent1">checked=checked</s:if>/>不符合
							</label>
							<br /> <label
							class="agreement">2.产品图样和技术文件现行有效。产品技术状态更改按规定办理了手续，并在产品上得到落实。</label><br />
							<label class="agreement">
							<input type="radio" name="scrIsConsistent2"
									value="符合" 
									<s:if test="\"符合\"==scrIsConsistent2">checked=checked</s:if>/>符合
							</label>
							<label class="agreement">
								<input type="radio" name="scrIsConsistent2"
										value="不符合" <s:if test="\"不符合\"==scrIsConsistent2">checked=checked</s:if>/>不符合
							</label>
						<br /> <label
							class="agreement">3.检验场所满足检测，试验和安全要求：检验的环境条件符合产品图样和产品规范的有关规定。</label><br />
						<label class="agreement">
							<input type="radio" name="scrIsConsistent3"
									value="符合" 
									<s:if test="\"符合\"==scrIsConsistent3">checked=checked</s:if>/>符合
							</label>
							<label class="agreement">
								<input type="radio" name="scrIsConsistent3"
										value="不符合" <s:if test="\"不符合\"==scrIsConsistent3">checked=checked</s:if>/>不符合
							</label>
						<br /> <label
							class="agreement">4.用于检验，试验的仪器和设备符合有关标准，产品图样和技术文件的规定，器精度及准确度满足产品检测的要求；资质(含专用)检验仪器和工装设备经鉴定合格；按规定进行了周期检定并合格。</label><br />
							<label class="agreement">
							<input type="radio" name="scrIsConsistent4"
									value="符合" 
									<s:if test="\"符合\"==scrIsConsistent4">checked=checked</s:if>/>符合
							</label>
							<label class="agreement">
								<input type="radio" name="scrIsConsistent4"
										value="不符合" <s:if test="\"不符合\"==scrIsConsistent4">checked=checked</s:if>/>不符合
							</label>
						<br /> <label
							class="agreement">5.提交军检的产品经工厂检验合格，且未处于停止验收状态。提交验收的产品应配套齐全，配用的文件资料符合要求。配套产品应有军检合格证或经军事代表认可的入场检验记录。</label><br />
							<label class="agreement">
							<input type="radio" name="scrIsConsistent5"
									value="符合" 
									<s:if test="\"符合\"==scrIsConsistent5">checked=checked</s:if>/>符合
							</label>
							<label class="agreement">
								<input type="radio" name="scrIsConsistent5"
										value="不符合" <s:if test="\"不符合\"==scrIsConsistent5">checked=checked</s:if>/>不符合
							</label>
					</div>
					<div class="lg_box">
						<label class="agreement">二.条件审查结论</label><br />
						<label class="agreement">
						<input type="radio" name="crcIsConsistent"
								value="同意" 
								<s:if test="\"同意\"==crcIsConsistent">checked=checked</s:if>/>经审查，产品提交军检条件符合规定，同意受理交验
						</label><br/>
						<label class="agreement">
							<input type="radio" name="crcIsConsistent"
									value="拒绝" <s:if test="\"拒绝\"==crcIsConsistent">checked=checked</s:if>/>经审查，产品提交军检条件不符合规定，拒绝受理交验
						</label>
					</div>
					<div class="lg_int">
						<input type="text" name="crcReason" id="crcReason" value="${crcReason}"
							 placeholder="请输入拒绝受理交验事由" class="whit" />
					</div>
					<div class="lg_int">
						<input isUser="true" hiddenInputId="crcMilitaryLoginName" treeValue="loginName" type="text" id="crcMilitaryRepresentative" name="crcMilitaryRepresentative" value="${crcMilitaryRepresentative}" placeholder="请输入军事代表" class="whit" />
       					<input type="hidden" id="crcMilitaryLoginName" name="crcMilitaryLoginName" value="${crcMilitaryLoginName}" />
					</div>
					<div class="lg_int">
						<div class="demo">
                            <div class="lie">
                            	<input id="crcDate" name="crcDate" value="<s:date name="crcDate" format="yyyy-MM-dd"/>" class="kbtn datatp datatpw1 whit" placeholder="请输入日期" />
                            </div>
                        </div>
                        <div id="datePlugin4"></div>
					</div>
					<div class="lg_box">
						<label class="agreement">三.拒绝受理交验事由</label>
					</div>
					<div class="lg_int">
						<input type="text" name="rasrRemark" id="rasrRemark"
							 value="${rasrRemark}"
							 placeholder="请输入请填写拒绝受理交验事由" class="whit" />
					</div>
					<div class="lg_int">
						<input isUser="true" hiddenInputId="rasrMilitaryLoginName" treeValue="loginName" type="text" id="rasrMilitaryRepresentative" name="rasrMilitaryRepresentative" value="${rasrMilitaryRepresentative}" placeholder="请输入军事代表" class="whit"/>
       					<input type="hidden" id="rasrMilitaryLoginName" name="rasrMilitaryLoginName" value="${rasrMilitaryLoginName}" />
					</div>
					<div class="lg_int">
						<div class="demo">
                            <div class="lie">
                            	<input id="rasrDate" name="rasrDate" value="<s:date name="rasrDate" format="yyyy-MM-dd"/>" isDate=true 
                            		class="kbtn datatp datatpw1 whit" placeholder="请输入日期" />
                            </div>
                        </div>
                        <div id="datePlugin6"></div>
					</div>
					<div class="lg_box">
						<label class="agreement">四.军检结论</label>
					</div>
					<div>
						<label class="agreement">
						<input type="radio" name="aicIsQualified"
								value="合格" 
								<s:if test="\"合格\"==aicIsQualified">checked=checked</s:if>/>合格
						</label>
						<label class="agreement">
							<input type="radio" name="aicIsQualified"
									value="不合格" <s:if test="\"不合格\"==aicIsQualified">checked=checked</s:if>/>不合格
						</label>
					</div>
					<div class="lg_int">
					
						<input isUser="true" hiddenInputId="aicMilitaryLoginName" treeValue="loginName" type="text" id="aicMilitaryRepresentative" name="aicMilitaryRepresentative" value="${aicMilitaryRepresentative}" placeholder="请输入军事代表" class="whit" />
       					<input type="hidden" id="aicMilitaryLoginName" name="aicMilitaryLoginName" value="${aicMilitaryLoginName}" />
					</div>
					<div class="lg_int">
						<div class="demo">
                            <div class="lie">
                            	<input id="aicDate" name="aicDate" value="<s:date name="aicDate" format="yyyy-MM-dd"/>" isDate=true 
                            		class="kbtn datatp datatpw1 whit" placeholder="请输入日期" />
                            </div>
                        </div>
                        <div id="datePlugin5"></div>
					</div>
					<div class="updata_img">
						<div class="dataImgBox" id="dataImgBox"></div>
						<div class="inputFile">点击上传图片<input id="updataBtn" type="file" name="inputFile" accept="image/*" /></div>
					</div>
					<div>
						<s:if test="taskId>0">
							<s:if test="task.active==0">
								<button type="button" class="lgbs_btn lg_btn" onclick="saveForm();">保存</button>
								<s:if test="task.processingMode.name()==\"TYPE_EDIT\"">
									<button type="button" class="lgbs_btn lg_btn" onclick="_completeTask('SUBMIT');">提交</button>
								</s:if>
								<s:elseif test="task.processingMode.name()==\"TYPE_APPROVAL\"">
									<button type="button" class="lgbs_btn lg_btn" onclick="_completeTask('APPROVE');">同意</button>
									<button type="button" class="lgbs_btn lg_btn" onclick="_completeTask('OPPOSE');">不同意</button>
								</s:elseif>
							</s:if>
						</s:if>
						<s:else>
							<button type="button" class="lgbs_btn lg_btn" onclick="saveForm();">保存</button>
							<button type="button" class="lgbs_btn lg_btn" onclick="submitForm();">提交</button>
						</s:else>
					</div>
				</div>
			</div>
		</div>
		<div style="margin-bottom:150px;">
		</div>
	</form>
	<div class="classification" id="userTree">
		<div class="classContent" id="tpl1">
			<h3>用户树</h3>
			<div class="classfiSrc clearfix">
				<input type="text">
				<a href="javascript:;"></a>
			</div>
			${userTreeHtml}
		</div>
		<div class="nav_hov"></div>
	</div>
</body>
</html>
