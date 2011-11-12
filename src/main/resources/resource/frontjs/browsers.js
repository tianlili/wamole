$(function() {
	function iFrameHeight(id) { 
		var ifm = parent.document.getElementById(id); 
		document.title = ifm.contentDocument.title;
		var subWeb = document.frames ? document.frames[id].document : ifm.contentDocument; 
		if(ifm != null && subWeb != null) { 
			ifm.height = subWeb.body.offsetHeight + 20; 
		} 
	} 
	
	function sortData(data, col, asc){
		var temp;
		for(var i=0; i<data.length; i++){
			for(var j=i+1; j<data.length; j++){
				if(asc && data[i][col] > data[j][col] || !asc && data[i][col] < data[j][col] ){
					temp = data[i];
					data[i] = data[j];
					data[j] = temp;
				}
			}
		}
		return data;
	};
	
	function insertData(data, col, asc){
		var data = sortData(data, col, asc);
		$(".list tbody").html("");
		for(var i=0; i<data.length; i++){
    		var c = i%2 ? "erow" : "orow";
    		var innerhtml = '<tr class="' + c +'">'
        		+'<td><div style="text-align:left;">' + data[i].ip + '</div></td>'
        		+'<td><div style="text-align:left;">' + data[i].name + '</div></td>'
        		+'<td><div style="text-align:left;">' + data[i].os + '</div></td>'
        		+'<td><div style="text-align:left;">' + data[i].id + '</div></td>'
        		+'<td><div style="text-align:left;">' + data[i].active + '</div></td>'
        		+'<td><div style="text-align:left;">' + data[i].version + '</div></td>'
        		+'<td><div style="text-align:left;"><div style="width:20px" class="fview"><img id=' + data[i].name + '_view src="../resource/frontcss/images/view.ico" width=20px height=20px/></div></div></td></tr>'
        	$(".list tbody").append(innerhtml);
    	}

		iFrameHeight("iframepage");
		
		asc ? $(".list thead #" + col).attr("class", "sasc") : $(".list thead #" + col).attr("class", "sdesc");
		
		$("[id$='_view']").click(function(){
			var name = this.id.substring(0, this.id.indexOf("_view"));
			window.location.href = "../../project/"+ name +"/view";
		});
	};
	
	function addSortEvent(el, col, data){
		var list = $(".list thead .hDiv td div");
		for(var i=0; i<list.length; i++){
			if(list[i].className != 'sort' && list[i] != el){
				list[i].className = 'sort';
			}
		}
		if(el.className == "sasc"){
			insertData(data, col, false);
			return;
		}
		if(el.className == "sdesc" || el.className == "sort"){
			insertData(data, col, true);
			return;
		}
	}
	
	$.ajax({
        url : 'data/browser',
        method : 'GET',
		dataType : 'json',
        success : function(data) {
        	insertData(data, "ip", true);
        	iFrameHeight("iframepage");
        	
        	$(".list thead #ip").click (function(el){
        		addSortEvent(el.toElement, "ip", data);
        	});
        	
        	$(".list thead #name").click (function(el){
        		addSortEvent(el.toElement, "name", data);
        	});
        }
    });
	
}); 