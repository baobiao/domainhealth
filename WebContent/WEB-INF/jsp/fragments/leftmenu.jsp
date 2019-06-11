<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="left_menu_top">
	<div class="left_menu_bottom">
		<c:if test="${resourcetype eq 'core'}">
			<h5>Help</h5>					
		</c:if>
		<c:if test="${resourcetype ne 'core'}">
			<h5><c:out value="${menutitle}"/></h5>					
		</c:if>
		<div>
			<ul class="left_menu">
				<c:forEach var="resource" items="${resources}">
					<li><a title='<c:out value="${resource}"/>' href='<c:out value="${pageurl}"/>/<c:out value="${resource}"/>?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>&direction=current'><c:out value="${fn:substring(resource,0,25)}"/></a></li>
				</c:forEach>
				<c:if test="${resourcetype ne 'core' and empty resources}">
					<li>None</li>
				</c:if>
				<c:if test="${resourcetype eq 'core'}">
					<li><a href='http://sourceforge.net/projects/domainhealth/'>Project Home Page</a></li>
					<li><a href='http://sourceforge.net/apps/mediawiki/domainhealth/index.php'>Help Documentation</a></li>
					<li><a href='http://sourceforge.net/projects/domainhealth/forums'>User Help Forums</a></li>
					<li><a href='http://sourceforge.net/tracker/?group_id=222020'>Bug Reports</a></li>
				</c:if>
			</ul>
		</div>
	</div>
</div>
