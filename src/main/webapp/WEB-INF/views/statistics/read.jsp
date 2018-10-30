<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<content tag="main">
<div id="dvStatistics" style="width: 100%">
	<div id="dvStatisticsChart" data-points='${readBookList}'></div>
	<script type="text/javascript">
	
// 	$(document).ready(function() {
		
// 		var $container = $('#dvStatisticsChart');
// 		$container.css('height', Common.getFullHeight() + 'px').css('width', '100%');
// 		$('#dvStatisticsChart2').css('height', Common.getFullHeight() + 'px').css('width', '100%');
// 		$('#dvStatisticsChart3').css('height', Common.getFullHeight() + 'px').css('width', '100%');
		
		
// 		var options = {
// 			title: {
// 				text: '도서읽은 현황'              
// 			},
// 			data: [{		
// 				type: 'column',// Change type to "doughnut", "line", "splineArea", etc.
// 				dataPoints: ${readBookList}
// 			}]
// 		};
		
	
// 		$container.CanvasJSChart(options);
// 		$('#dvStatisticsChart2').CanvasJSChart(options);
// 		$('#dvStatisticsChart3').CanvasJSChart(options);
		
// 		window.location.reload();
		
// 	});
	</script>
</div>
</content>