$(function() {

	var name = window.location.href.split("/");
	name = name[name.length-2];
	var url = location.href;
	var thead_html = $(".list thead .hDiv")[0].innerHTML;
	
	function splitPaths(data){
		var paths = data.path.split('/');
		for(var l=0; l<paths.length; l++){
			if(paths[l].length == 0){
				paths.splice(l, 1);
				l --;
			}
		}
		return paths;
	};
	
	function insertHead(data){
		var head_span = splitPaths(data);
		var head_html = "<span title = 'root' class= 'ffilelink'>" + name + "</span>" + "/";
		var head_path = "";
		for(var i = 0; i < head_span.length; i ++){
			head_path += head_span[i] + "/";
			head_html += "<span title = '" + head_path + "' class= 'ffilelink'>" + head_span[i] + "</span>" + "/";
		}
		if(data.dir == "false")
			head_html = head_html.substring(0, head_html.length - 1);
		
		var exec_html = '';
    	if(data.exe == "true")
    		exec_html = '<img title="run the case" src="../../resource/frontcss/images/exec.ico" width="16px" height="16px">';
    	
		
    	$(".list thead .ftitle").html(head_html);
    	$(".list thead .fbutton").html(exec_html);
    	
    	$(".list thead .ftitle span").click(function(){
    		var add = this.title == "root" ? "" : "/" + this.title;
    		add = data.dir == "true" ? add : add.substring(0, add.length - 1);
    		url = url.substring(0,url.indexOf(name + '/files') + name.length + 6) + add;
    		getData(url);
    	});
	};
	
	function insertDir(data){
		$(".list thead .hDiv").html(thead_html);
		
    	data = data.children;
    	sortData(data);
    	
		for(var i=0; i<data.length; i++){
			var c = i%2 ? "erow" : "orow";
			var p = data[i].dir == "true" ? "dir.png" : "txt.png";
			var f = data[i].dir == "true" ? "/" : "";
			var filenames = splitPaths(data[i]);
			data[i].filename = filenames[filenames.length - 1];
			data[i].path = decodeURIComponent(data[i].path);
			
			var innerhtml = '<tr class="' + c +'">'
	    		+'<td><div><img style="margin-right:5px" src="../../resource/frontcss/images/' + p +'" /><span class="ffile" title="' + data[i].path + '">' + data[i].filename + f + '</span></div></td>'
	    		+'<td><div>' + getLocalTime(data[i].lastModified) + '</div></td>'
	    		+'<td><div>' + data[i].size + '</div></td>';
			
	    	$(".list tbody").append(innerhtml);
	    	
	    	$(".list tbody tr:eq(" + i +") td:first div span").click(function(){
	    		for(var j = 0;j < data.length; j++){
	    			if(data[j].path ==  this.title){
	            		url += '/' + data[j].filename; 
	            		getData(url);
	    			}
	    		}
	    	});
		}
	};
	
	function insertFile(data){
		$(".list thead .hDiv").html('<td colspan=3><div>&nbsp;</div></td>');
		
		$(".list tbody").append("<tr class='fcontent'><td colspan=3><div><pre></pre></div></td></tr>");
		$(".list tbody pre").append(data.content);
	}
	
	function sortData(data){
		var temp;
		for(var r=0; r<data.length; r++){
			for(var s=r+1; s<data.length; s++){
				if(data[r].dir < data[s].dir){
					temp = data[s];
					data[s] = data[r];
					data[r] = temp;
				}
			}
		}
	};
	
	function insertData(data){
		insertHead(data);
		
    	$(".list tbody").html("");
		
    	if(data.dir == 'true'){
    		insertDir(data);
    	}
    	else{
    		insertFile(data);
    	}
		iFrameHeight("iframepage");
	};
	
	function getData(url){
		$.getJSON(url,function(data) {
        	$.each(data.children, function(i, item){
        		data.children[i].path = Base64.decode(data.children[i].path);
        	});
        	data.path = Base64.decode(data.path);
        	data.content = Base64.decode(data.content);
        	insertData(data);
        });
	};
	
	//获取第一级目录下的数据
	getData(url);
}); 