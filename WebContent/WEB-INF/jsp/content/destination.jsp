<%@ page import="domainhealth.core.jmx.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.MESSAGES_CURRENT_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.MESSAGES_PENDING_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
	</tr>
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.MESSAGES_RECEIVED_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.MESSAGES_HIGH_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
	</tr>
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.CONSUMERS_CURRENT_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.CONSUMERS_HIGH_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
	</tr>
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.CONSUMERS_TOTAL_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%">&nbsp;</td>
	</tr>