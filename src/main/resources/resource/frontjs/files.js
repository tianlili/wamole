$(function() {

	var name = window.location.href.split("/");
	name = name[name.length-2];
	var url = location.href;
	
	function iFrameHeight(id) { 
		var ifm = parent.document.getElementById(id); 
		document.title = ifm.contentDocument.title;
		var subWeb = document.frames ? document.frames[id].document : ifm.contentDocument; 
		if(ifm != null && subWeb != null) { 
			ifm.height = subWeb.body.offsetHeight + 20; 
		} 
	};

	function getLocalTime(nS) {
	    var date = new Date(parseInt(nS));
	    Date.prototype.format = function(format){ 
	    	var o = { 
		    	"M+" : this.getMonth()+1, //month 
		    	"d+" : this.getDate(), //day 
		    	"h+" : this.getHours(), //hour 
		    	"m+" : this.getMinutes(), //minute 
		    	"s+" : this.getSeconds(), //second 
		    	"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
		    	"S" : this.getMilliseconds() //millisecond 
	    	} 

	    	if(/(y+)/.test(format)) { 
	    		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	    	} 

	    	for(var k in o) { 
		    	if(new RegExp("("+ k +")").test(format)) { 
		    		format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
		    	} 
	    	} 
	    	return format; 
	    } 

	    return date.format("yyyy-MM-dd hh:mm:ss"); 
	};
	
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
			if(data.dir == "false")
				head_path.substring(0, head_path.length - 2);
			head_html += "<span title = '" + head_path + "' class= 'ffilelink'>" + head_span[i] + "</span>" + "/";
		}
    	$(".list thead .ftitle").html(head_html);
    	$(".list thead .ftitle span").click(function(){
    		var add = this.title == "root" ? "" : this.title;
    		add = data.dir == "true" ? add : add.substring(0, add.length - 1);
    		url = url.substring(0,url.indexOf(name + '/files') + name.length + 6) + "/" + add;
    		getData(url);
    	});
	};
	
	function insertDir(data){

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
		$(".list tbody").append("<td colspan = 3><div class='filecontent'><pre></pre></div></td>");
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