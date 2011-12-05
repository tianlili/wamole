$(function() {

	var name = window.location.href.split("/");
	name = name[name.length-2];
	var url = location.href;
	var thead_html = $(".list thead .hDiv")[0].innerHTML;
	
	function drawArea(){
		 var chart = new Highcharts.Chart({
		      chart: {
		         renderTo: 'container',
		         defaultSeriesType: 'area'
		      },
		      title: {
		         text: ''
		      },
		      subtitle: {
		         text: ''
		      },
		      xAxis: {
		         categories: ['#1', '#2', '#3', '#4', '#5', '#6', '#7'],
		         tickmarkPlacement: 'on',
		         title: {
		            enabled: false
		         }
		      },
		      yAxis: {
		         title: {
		            text: 'Count'
		         },
		         labels: {
		            formatter: function() {
		               return this.value;
		            }
		         }
		      },
		      tooltip: {
		         formatter: function() {
		        	var c = this.series.name == "all tests" ? ' tests' : ' failures'
		            return '' + this.x +': '+ Highcharts.numberFormat(this.y, 0, ',') + c;
		         }
		      },
		      plotOptions: {
		         area: {
		            stacking: 'normal',
		            lineWidth:0,
		            marker: {
		            	 enabled: false,
		                 states: {
		                    hover: {
		                       enabled: true
		                    }
		                 }
		            }
		         }
		      },
		      series: [{
		         name: 'all tests',
		         data: [502, 635, 809, 947, 1402, 3634, 5268]
		      }, {
		         name: 'failures',
		         data: [106, 107, 111, 133, 221, 767, 1766]
		      }]
		   });
	}
	
	function insertData(data){
		for(var i=0; i<data.length; i++){
    		var c = i%2 ? "erow" : "orow";
    		var a = data[i].status == "running" ? "running" : "run";
    		var innerhtml = '<tr class="' + c +'">'
    			+'<td><div style="text-align:left;"><div style="width:16px"><img title="' + a + '" src="../../resource/frontcss/images/' + a + '.png" width=16px height=16px/></div></div></td>'
        		+'<td><div style="text-align:left;">' + data[i].id + '</div></td>'
        		+'<td><div style="text-align:left;">' + data[i].time + '</div></td>'
        		+'<td><div style="text-align:left;"><div style="width:16px" class="fresult"><img title="result" id=' + data[i].id + '_result src="../../resource/frontcss/images/reports.ico" width=16px height=16px/></div></div></td>'
        		+'</tr>';
        	$(".list tbody").append(innerhtml);
    	}

		iFrameHeight("iframepage");
	};
	
	$.getJSON('../../resource/frontjs/response.php', function(data){
		drawArea();
	});
	
	$.getJSON('../../resource/frontjs/response.php', function(data){
		insertData(data);
	});
}); 