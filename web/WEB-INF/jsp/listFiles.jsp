<%@ page language="java" contentType="text/html"%>
<%@ taglib prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>Spring MVC + Dropzone.js Example</title>
    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-heading text-center">
				<h3>Spring MVC + Dropzone.js Example</h3>
			</div>
			<div class="panel-body">

				<a class="btn btn-primary" href="${pageContext.request.contextPath}">
					<span class="glyphicon glyphicon-chevron-left"></span> Go Back</a> <br>
				<br>
				<h4>List of All Uploaded Files</h4>

			</div>
			<table class="table table-hover table-condensed">
				<thead>
					<tr>
						<th width="5%">S.N</th>
						<th width="40%">File Name</th>
						<th width="20%">File Type</th>
						<th width="15%">File Size</th>
						<th width="10%">Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${fileList}" var="dataFile" varStatus="loopCounter">
						<tr>
							<td><c:out value="${loopCounter.count}" /></td>
							<td><c:out value="${dataFile.name}" /></td>
							<td><c:out value="${dataFile.type}" /></td>
							
							<td>
								<c:choose>
									<c:when test="${(dataFile.size < 1024)}">
										${dataFile.size/(1024 * 1.0)} Bytes
									</c:when>
									<c:when
										test="${(dataFile.size >= 1024) && (dataFile.size < 1024*1024)}">
										<fmt:formatNumber value="${dataFile.size/(1024 * 1.0)}"
											maxFractionDigits="2" /> KB
									</c:when>
									<c:when test="${(dataFile.size >= 1024*1024)}">
										<fmt:formatNumber value="${dataFile.size/(1024 * 1024 * 1.0)}"
											maxFractionDigits="2" /> MB
									</c:when>
								</c:choose>
							</td>
							
							<td>
								<a class="btn btn-primary" href="${pageContext.request.contextPath}/get/${dataFile.id}">
									<span class="glyphicon glyphicon-download"></span> Download
							</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>

</html>