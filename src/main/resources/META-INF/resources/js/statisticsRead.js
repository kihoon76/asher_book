$(document).ready(function() {
	
	var $container = $('#dvStatisticsRead');
	$container.css('height', Common.getFullHeight() + 'px');
	
	
	var options = {
			title: {
				text: "도서읽은 현황"              
			},
			data: [              
			{
				// Change type to "doughnut", "line", "splineArea", etc.
				type: "column",
				dataPoints: [
					{ label: "apple",  y: 10  },
					{ label: "orange", y: 15  },
					{ label: "banana", y: 25  },
					{ label: "mango",  y: 30  },
					{ label: "grape",  y: 28  },
					{ label: "grape2",  y: 28  }
				]
			}
			]
		};

	$container.CanvasJSChart(options);
	
});