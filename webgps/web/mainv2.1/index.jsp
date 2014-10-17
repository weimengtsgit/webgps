<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo"%>
<%
	String path = request.getContextPath();
	UserInfo user = (UserInfo) request.getSession().getAttribute(
			"userInfo");
	if (user == null) {
		response.sendRedirect(path + "/error.jsp");
		return;
	}

	String targetTemplateType = user.getTargetTemplateType();
	int targetTemplateType_ = targetTemplateType.equals("") ? 2
			: Integer.valueOf(targetTemplateType);
	if (targetTemplateType_ == 0) {
		targetTemplateType = "��";
	} else if (targetTemplateType_ == 1) {
		targetTemplateType = "Ѯ";
	} else {
		targetTemplateType = "��";
	}
%>
<!--<!DOCTYPE html >-->
<HTML>
<HEAD>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<TITLE></TITLE>
	<script type="text/javascript">
	var targetTemplateType = '<%=targetTemplateType%>';
	</script>
	<link rel="stylesheet" type="text/css" href="<%=path%>/mainv2.1/index.css" />
	<script src="<%=path%>/js/RGraph/libraries/RGraph.common.core.js"></script>
	<script src="<%=path%>/js/RGraph/libraries/RGraph.gauge.js"></script>
	<!--[if lt IE 9]><![endif]-->
	<script src="<%=path%>/js/RGraph/excanvas/excanvas.js"></script>
	<script type="text/javascript" src="<%=path%>/js/jquery/jquery.min.js"></script>
	<script src="<%=path%>/js/Highcharts-2.2.5/js/highcharts.js"></script>
	<script src="<%=path%>/js/Highcharts-2.2.5/js/modules/exporting.js"></script>
	<script src="<%=path%>/mainv2.1/Gauge.js"></script>
	<script src="<%=path%>/mainv2.1/LineChart.js"></script>
	<script src="<%=path%>/mainv2.1/index.js"></script>
	<style>
		a:link{
			color: #0668b3;
		}
	</style>
</HEAD>
<BODY leftmargin="0" topmargin="0" style="background-color: #D6E3F3;">
<table width="99%" border="0" style="TABLE-LAYOUT: fixed; margin-left: 10px;">
	<tr>
		<td width="5%" rowspan="3" class="tdstyle" align="center" style="border-style: solid; border-width: 2; border-color: #99BBE8;" width="20%">
			<p class="STYLE1">��</p>
			<p class="STYLE1">��</p>
			<p class="STYLE1">ͳ</p>
			<p class="STYLE1">��</p>
		</td>
		<td width="95%" class="tdstyle" cellpadding="0">
			<table width="100%" height="100%" border="0" cellspacing="0" style="margin-top: -2; margin-left: -1;">
				<tr>
					<td align="center" nowrap="nowrap" style="border-style: solid; border-width: 2; border-color: #99BBE8;" width="25%">
						<br/>
						<span id="signNeedle" class="STYLE5">ǩ��������</span><br/>
						<span style="text-align: center">
							<canvas id="signBillCanvas" width="210" height="210">[No canvas support]</canvas>
						</span>
						<table>
							<tr>
								<td align="left">
									<font style="font-size: 13px; font-weight: bold; font-family: '����';">��<%=targetTemplateType%>�ۼ�ǩ����:</font>
								</td>
								<td>
									<font id="bill" style="font-size: 13px; font-weight: bold; font-family: '����';">0 Ԫ</font>
								</td>
							</tr>
							<tr>
								<td align="left">
									<font style="font-size: 13px; font-weight: bold; font-family: '����';">��<%=targetTemplateType%>Ŀ��ֵ:</font>
								</td>
								<td>
									<font id="targetBill" style="font-size: 13px; font-weight: bold; font-family: '����';">0 Ԫ</font>
								</td>
							</tr>
						</table>
					</td>
					<td  width="75%" nowrap="nowrap" colspan="2" style="border-top-style: solid; border-top-width: 2; border-right-style: solid; border-right-width: 2; border-right-style: solid; border-bottom-width: 2; border-bottom-style: solid; border-bottom-width: 2; border-color: #99BBE8;">
						<table width="100%">
							<tr>
								<td width="100%">
									<table width="100%">
										<tr>
											<td width="50%">
												<input type="radio" name="signBillRadio" 
												onclick="javascript:getLineChart('../signBill/signBill.do?method=getSignBillsByTime', 'signBillChartDiv', '#signBillUnauditedSpan');"
												checked><span class="STYLE2"><font style="font-size: 13px;">��<%=targetTemplateType%>ǩ��������ͼ</font></span>
												&nbsp;&nbsp;
												<input type="radio" name="signBillRadio"
												onclick="javascript:getLineChart('../signBill/signBill.do?method=getSignBillHisByTime', 'signBillChartDiv', '#signBillUnauditedSpan');">
												<span class="STYLE2"><font style="font-size: 13px;">��ʷǩ��������ͼ</font></span>
											</td>
											<td width="50%" align="right">
												<a href="javascript:mainJumpToDetail('signBillChartDiv')" class="STYLE4">
													<font style="font-size: 13px;">����鿴��ϸ</font>
												</a>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td width="100%">
									<table width="100%">
										<tr>
											<td width="100%">
												<table width="100%">
													<tr>
														<td width="3%">
															<table cellspacing="0" align="right">
																<tr>
																	<td>
																		<font style="font-size: 13px;">��</font>		
																	</td>
																</tr>
																<tr>
																	<td>
																		<font style="font-size: 13px;">λ</font>		
																	</td>
																</tr>
																<tr>
																	<td align="right">
																		<font style="font-size: 13px;">:</font>		
																	</td>
																</tr>
																<tr>
																	<td>
																		<font style="font-size: 13px;">��</font>		
																	</td>
																</tr>
																<tr>
																	<td>
																		<font style="font-size: 13px;">Ԫ</font>		
																	</td>
																</tr>
															</table>
														</td>
														<td width="97%">
															<div id="signBillChartDiv" style="height: 210px; margin: 0 auto"></div>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdstyle">
			<table width="100%" height="100%" border="0" cellspacing="0" style="margin-top: -2; margin-left: -1;">
				<tr>
					<td align="center" nowrap="nowrap" style="border-style: solid; border-width: 2; border-color: #99BBE8;" width="25%">
						<br/>
						<span id="cashNeedle" class="STYLE5">�ؿ������</span><br/>
						<span style="text-align: center">
							<canvas id="cashCanvas" width="210" height="210">[No canvas support]</canvas>
						</span>
						<table>
							<tr>
								<td align="left">
									<font style="font-size: 13px; font-weight: bold; font-family: '����';">��<%=targetTemplateType%>�ۼƻؿ��:</font>
								</td>
								<td>
									<font id="cash" style="font-size: 13px; font-weight: bold; font-family: '����';">0 Ԫ</font>
								</td>
							</tr>
							<tr>
								<td align="left">
									<font style="font-size: 13px; font-weight: bold; font-family: '����';">��<%=targetTemplateType%>Ŀ��ֵ:</font>
								</td>
								<td>
									<font id="targetCash" style="font-size: 13px; font-weight: bold; font-family: '����';">0 Ԫ</font>
								</td>
							</tr>
						</table>
					</td>
					<td  width="75%" nowrap="nowrap" colspan="2" style="border-top-style: solid; border-top-width: 2; border-right-style: solid; border-right-width: 2; border-right-style: solid; border-bottom-width: 2; border-bottom-style: solid; border-bottom-width: 2; border-color: #99BBE8;">
						<table width="100%">
							<tr>
								<td width="100%">
									<table width="100%">
										<tr>
											<td width="50%">
												<input type="radio" name="cashRadio" 
												onclick="javascript:getLineChart('../cash/cash.do?method=getCashsByTime', 'cashChartDiv', '#cashUnauditedSpan');"
												checked><span class="STYLE2"><font style="font-size: 13px;">��<%=targetTemplateType%>�ؿ������ͼ</font></span>
												&nbsp;&nbsp;
												<input type="radio" name="cashRadio"
												onclick="javascript:getLineChart('../cash/cash.do?method=getCashHisByTime', 'cashChartDiv', '#cashUnauditedSpan');">
												<span class="STYLE2"><font style="font-size: 13px;">��ʷ�ؿ������ͼ</font></span>
											</td>
											<td width="50%" align="right">
												<a href="javascript:mainJumpToDetail('cashChartDiv')" class="STYLE4">
													<font style="font-size: 13px;">����鿴��ϸ</font>
												</a>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td width="100%">
									<table width="100%">
										<tr>
											<td width="100%">
												<table width="100%">
													<tr>
														<td width="3%">
															<table cellspacing="0" align="right">
																<tr>
																	<td>
																		<font style="font-size: 13px;">��</font>		
																	</td>
																</tr>
																<tr>
																	<td>
																		<font style="font-size: 13px;">λ</font>		
																	</td>
																</tr>
																<tr>
																	<td align="right">
																		<font style="font-size: 13px;">:</font>		
																	</td>
																</tr>
																<tr>
																	<td>
																		<font style="font-size: 13px;">��</font>		
																	</td>
																</tr>
																<tr>
																	<td>
																		<font style="font-size: 13px;">Ԫ</font>		
																	</td>
																</tr>
															</table>
														</td>
														<td width="97%">
															<div id="cashChartDiv" style="min-width: 500px; height: 210px; margin: 0 auto"></div>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdstyle">
			<table width="100%" height="100%" border="0" cellspacing="0" style="margin-bottom: -2; margin-left: -1;">
				<tr>
					<td align="center" nowrap="nowrap" style="border-style: solid; border-width: 2; border-color: #99BBE8;" width="25%">
						<br/>
						<span id="costNeedle" class="STYLE5">���ö�ʹ����</span><br/>
						<span style="text-align: center">
							<canvas id="costCanvas" width="210" height="210">[No canvas support]</canvas>
						</span>
						<table>
							<tr>
								<td align="left">
									<font style="font-size: 13px; font-weight: bold; font-family: '����';">��<%=targetTemplateType%>�ۼƷ��ö�:</font>
								</td>
								<td>
									<font id="cost" style="font-size: 13px; font-weight: bold; font-family: '����';">0 Ԫ</font>
								</td>
							</tr>
							<tr>
								<td align="left">
									<font style="font-size: 13px; font-weight: bold; font-family: '����';">��<%=targetTemplateType%>Ŀ��ֵ:</font>
								</td>
								<td>
									<font id="targetCost" style="font-size: 13px; font-weight: bold; font-family: '����';">0 Ԫ</font>
								</td>
							</tr>
						</table>
					</td>
					<td  width="75%" nowrap="nowrap" colspan="2" style="border-top-style: solid; border-top-width: 2; border-right-style: solid; border-right-width: 2; border-right-style: solid; border-bottom-width: 2; border-bottom-style: solid; border-bottom-width: 2; border-color: #99BBE8;">
						<table width="100%">
							<tr>
								<td width="100%">
									<table width="100%">
										<tr>
											<td width="50%">
												<input type="radio" name="costRadio" 
												onclick="javascript:getLineChart('../cost/cost.do?method=getCostsByTime', 'costChartDiv', '#costUnauditedSpan');"
												checked><span class="STYLE2"><font style="font-size: 13px;">��<%=targetTemplateType%>���ö�����ͼ</font></span>
												&nbsp;&nbsp;
												<input type="radio" name="costRadio"
												onclick="javascript:getLineChart('../cost/cost.do?method=getCostHisByTime', 'costChartDiv', '#costUnauditedSpan');">
												<span class="STYLE2"><font style="font-size: 13px;">��ʷ���ö�����ͼ</font></span>
											</td>
											<td width="50%" align="right">
												<a href="javascript:mainJumpToDetail('costChartDiv')" class="STYLE4">
													<font style="font-size: 13px;">����鿴��ϸ</font>
												</a>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td width="100%">
									<table width="100%">
										<tr>
											<td width="100%">
												<table width="100%">
													<tr>
														<td width="3%">
															<table cellspacing="0" align="right">
																<tr>
																	<td>
																		<font style="font-size: 13px;">��</font>		
																	</td>
																</tr>
																<tr>
																	<td>
																		<font style="font-size: 13px;">λ</font>		
																	</td>
																</tr>
																<tr>
																	<td align="right">
																		<font style="font-size: 13px;">:</font>		
																	</td>
																</tr>
																<tr>
																	<td>
																		<font style="font-size: 13px;">��</font>		
																	</td>
																</tr>
																<tr>
																	<td>
																		<font style="font-size: 13px;">Ԫ</font>		
																	</td>
																</tr>
															</table>
														</td>
														<td width="97%">
															<div id="costChartDiv" style="min-width: 500px; height: 210px; margin: 0 auto"></div>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td rowspan="2" class="tdstyle" align="center" style="border-style: solid; border-width: 2; border-color: #99BBE8;">
			<p class="STYLE1">��</p>
			<p class="STYLE1">��</p>
			<p class="STYLE1">ͳ</p>
			<p class="STYLE1">��</p>
		</td>
		<td class="tdstyle">
			<table width="100%" height="100%" border="0" cellspacing="0" style="margin-top: -2; margin-left: -1;">
				<tr>
					<td width="25%" align="center" nowrap="nowrap" style="border-style: solid; border-width: 2; border-color: #99BBE8;">
						<br/>
						<span id="visitNeedle" class="STYLE5">ҵ��Ա���ô����</span><br/>
						<span style="text-align: center">
							<canvas id="visitCanvas" width="210" height="210">[No canvas support]</canvas>
						</span>
						<table>
							<tr>
								<td align="left">
									<font style="font-size: 13px; font-weight: bold; font-family: '����';">��<%=targetTemplateType%>�ۼưݷÿͻ���:</font>
								</td>
								<td>
									<font id="visit" style="font-size: 13px; font-weight: bold; font-family: '����';">0 ��</font>
								</td>
							</tr>
							<tr>
								<td align="left">
									<font style="font-size: 13px; font-weight: bold; font-family: '����';">��<%=targetTemplateType%>Ŀ��ֵ:</font>
								</td>
								<td>
									<font id="targetVisit" style="font-size: 13px; font-weight: bold; font-family: '����';">0 ��</font>
								</td>
							</tr>
						</table>
					</td>
					<td  width="75%" nowrap="nowrap" colspan="2" style="border-top-style: solid; border-top-width: 2; border-right-style: solid; border-right-width: 2; border-right-style: solid; border-bottom-width: 2; border-bottom-style: solid; border-bottom-width: 2; border-color: #99BBE8;">
						<table width="100%">
							<tr>
								<td width="100%">
									<table width="100%">
										<tr>
											<td width="50%">
												<input type="radio" name="visitRadio" 
												onclick="javascript:getLineChart('../visit/visit.do?method=getVisitsByTime', 'visitChartDiv', '#visitUnauditedSpan');"
												checked><span class="STYLE2"><font style="font-size: 13px;">��<%=targetTemplateType%>ҵ��Ա����ͳ������ͼ</font></span>
												&nbsp;&nbsp;
												<input type="radio" name="visitRadio"
												onclick="javascript:getLineChart('../visit/visit.do?method=getVisitHisByTime', 'visitChartDiv', '#visitUnauditedSpan');">
												<span class="STYLE2"><font style="font-size: 13px;">��ʷҵ��Ա����ͳ������ͼ</font></span>
											</td>
											<td width="50%" align="right">
												<a href="javascript:mainJumpToDetail('visitChartDiv')" class="STYLE4">
													<font style="font-size: 13px;">����鿴��ϸ</font>
												</a>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td width="100%">
									<table width="100%">
										<tr>
											<td width="100%">
												<table width="100%">
													<tr>
														<td width="3%">
															<table cellspacing="0" align="right">
																<tr>
																	<td>
																		<font style="font-size: 13px;">��</font>		
																	</td>
																</tr>
																<tr>
																	<td>
																		<font style="font-size: 13px;">λ</font>		
																	</td>
																</tr>
																<tr>
																	<td align="right">
																		<font style="font-size: 13px;">:</font>		
																	</td>
																</tr>
																<tr>
																	<td>
																		<font style="font-size: 13px;">��</font>		
																	</td>
																</tr>
															</table>
														</td>
														<td width="97%">
															<div id="visitChartDiv" style="min-width: 500px; height: 210px; margin: 0 auto"></div>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdstyle">
			<table width="100%" height="100%" border="0" cellspacing="0" style="margin-bottom: -2; margin-left: -1;">
				<tr>
					<td align="center" nowrap="nowrap" style="border-style: solid; border-width: 2; border-color: #99BBE8;" width="25%">
						<br/>
						<span id="cusVisitNeedle" class="STYLE5">�ͻ����ݷô����</span><br/>
						<span style="text-align: center">
							<canvas id="cusVisitCanvas" width="210" height="210">[No canvas support]</canvas>
						</span>
						<table>
							<tr>
								<td align="left">
									<font style="font-size: 13px; font-weight: bold; font-family: '����';">��<%=targetTemplateType%>�ۼưݷÿͻ���:</font>
								</td>
								<td>
									<font id="cusVisit" style="font-size: 13px; font-weight: bold; font-family: '����';">0  ��</font>
								</td>
							</tr>
							<tr>
								<td align="left">
									<font style="font-size: 13px; font-weight: bold; font-family: '����';">�ͻ�����Ŀ:</font>
								</td>
								<td>
									<font id="targetCusVisit" style="font-size: 13px; font-weight: bold; font-family: '����';">0  ��</font>
								</td>
							</tr>
						</table>
					</td>
					<td  width="75%" nowrap="nowrap" colspan="2" style="border-top-style: solid; border-top-width: 2; border-right-style: solid; border-right-width: 2; border-right-style: solid; border-bottom-width: 2; border-bottom-style: solid; border-bottom-width: 2; border-color: #99BBE8;">
						<table width="100%">
							<tr>
								<td width="100%">
									<table width="100%">
										<tr>
											<td width="50%">
												<input type="radio" name="cusVisitRadio" 
												onclick="javascript:getLineChart('../visit/visit.do?method=getCusVisitsByTime', 'cusVisitChartDiv', '#cusVisitUnauditedSpan');"
												checked><span class="STYLE2"><font style="font-size: 13px;">��<%=targetTemplateType%>�ͻ����ݷ�ͳ������ͼ</font></span>
												&nbsp;&nbsp;
												<input type="radio" name="cusVisitRadio"
												onclick="javascript:getLineChart('../visit/visit.do?method=getCusVisitHisByTime', 'cusVisitChartDiv', '#cusVisitUnauditedSpan');">
												<span class="STYLE2"><font style="font-size: 13px;">��ʷ�ͻ����ݷ�ͳ������ͼ</font></span>
											</td>
											<td width="50%" align="right">
												<a href="javascript:mainJumpToDetail('cusVisitChartDiv')" class="STYLE4">
													<font style="font-size: 13px;">����鿴��ϸ</font>
												</a>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td width="100%">
									<table width="100%">
										<tr>
											<td width="100%">
												<table width="100%">
													<tr>
														<td width="3%">
															<table cellspacing="0" align="right">
																<tr>
																	<td>
																		<font style="font-size: 13px;">��</font>		
																	</td>
																</tr>
																<tr>
																	<td>
																		<font style="font-size: 13px;">λ</font>		
																	</td>
																</tr>
																<tr>
																	<td align="right">
																		<font style="font-size: 13px;">:</font>		
																	</td>
																</tr>
																<tr>
																	<td>
																		<font style="font-size: 13px;">��</font>		
																	</td>
																</tr>
															</table>
														</td>
														<td width="97%">
															<div id="cusVisitChartDiv" style="min-width: 500px; height: 210px; margin: 0 auto"></div>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdstyle" align="center" style="border-style: solid; border-width: 2; border-color: #99BBE8;">
			<p class="STYLE1">��</p>
			<p class="STYLE1">λ</p>
			<p class="STYLE1">��</p>
			<p class="STYLE1">ѯ</p>
		</td>
		<td class="tdstyle">
		    <table width="100%" height="100%" style="border-style: solid; border-width: 2; border-color: #99BBE8; margin-top: -2; margin-left: -1;">
		    <tr>
				<td colspan="2" align="center" nowrap="nowrap"><span
					class="STYLE5">
					<a href="javascript:jumpToPoiPlat()" >��ʱ��λ</a>
					</span>
				</td>
			    <td colspan="2" align="center" nowrap="nowrap"><span
					class="STYLE5">
                     <a href="javascript:jumpToPoiPlat()" >�켣��ѯ</a>
					</span>
				</td>
				<td align="center" nowrap="nowrap"></td>
			</tr>
			<tr>
				<td colspan="2" align="center" nowrap="nowrap">
				<span>
					<a href="javascript:jumpToPoiPlat()" >
					<img class=img height=210  src="../images/mainV2.1/anyTimePoi.gif"  
					width=400></img>
					</a>
			    </span>
				</td>
			    <td colspan="2" align="center" nowrap="nowrap"><span
					class="STYLE5">
					<a href="javascript:jumpToPoiPlat()">
						<img class=img height=210  src="../images/mainV2.1/trackQuery.gif"  
						width=400>
						</img>
					</a>
					</span>
				</td>
				<td align="center" nowrap="nowrap"></td>
			</tr>
			
		    </table>
		</td>
	</tr>
</table>

</BODY>
</HTML>
