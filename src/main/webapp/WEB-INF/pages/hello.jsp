<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<body>
	<h1>${message}</h1>
    <a href="javascript:remoteControlRequest()">Remote Control</a>
	<script type="text/javascript" src="js/frameworks/jquery-1.11.1.min.js"></script>
	<script type="text/javascript" src="js/message.js"></script>
</body>
</html>