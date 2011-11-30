$(function() {
	function iFrameHeight(id) { 
		var ifm = parent.document.getElementById(id); 
		document.title = ifm.contentDocument.title;
		var subWeb = document.frames ? document.frames[id].document : ifm.contentDocument; 
		if(ifm != null && subWeb != null) { 
			ifm.height = subWeb.body.offsetHeight + 20; 
		} 
	};
	
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
        		+'<td><div style="text-align:left;">' + data[i].name + '</div></td>'
        		+'<td><div style="text-align:left;">' + decodeURIComponent(data[i].path) + '</div></td>'
        		+'<td><div style="text-align:left;"><div style="width:16px" class="fview"><img title="detail" id=' + data[i].name + '_view src="../resource/frontcss/images/view.ico" width=16px height=16px/></div></div></td>'
        		+'<td><div style="text-align:left;"><div style="width:16px" class="fexec"><img title="exec" id=' + data[i].name + '_exec src="../resource/frontcss/images/exec.ico" width=16px height=16px/></div></div></td>'
        		+'</tr>';
            $(".list tbody").append(innerhtml);
		}

		iFrameHeight("iframepage");
		
		asc ? $(".list thead #name").attr("class", "sasc") : $(".list thead #name").attr("class", "sdesc");
		
		$("[id$='_view']").click(function(){
			var name = this.id.substring(0, this.id.indexOf("_view"));
			window.location.href = "../../project/"+ name +"/files";
		});
	};
	
	$.ajax({
        url : location.href,
        method : 'GET',
		dataType : 'json',
        success : function(data) {
        	$.each(data, function(i, item){
        		data[i].path = Base64.decode(data[i].path);
        	});
        	insertData(data, "name", true);
        	$(".list thead #name").click (function(){
        		if(this.className == "sasc"){
        			insertData(data, "name", false);
        			return;
        		}
        		if(this.className == "sdesc"){
        			insertData(data, "name", true);
        			return;
        		}
        	});
        }
    });
}); 

function add(){
	window.location.href = "/addProject";
};