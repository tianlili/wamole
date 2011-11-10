$(function() {

	var name = window.location.href.split("/");
	name = name[name.length-2];
	
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
	
	function insertHead(data){
		var path = data.path.split("/");
		var apath = "";
		var bpath = "";
		for(var l=0; l< path.length; l++){
			if(path[l].length != 0){
				bpath += path[l] + "/";
				path[l] = "<span title='" + bpath + "' class= 'ffilelink'>" + path[l] + "</span>";
				apath += path[l] + " / ";
			}
		}
    	$(".list thead .ftitle").html("<span title = 'root' class= 'ffilelink'>" + name + "</span>" + ' / ' + apath);
    	$(".list thead .ftitle span").click(function(){
    		data = all_data;
    		paths = this.title.split("/");
    		for(var q=0; q<paths.length; q++){
				if(paths[q].length == 0)
					paths.splice(q, 1);
			}
    		for(var m=0; m<paths.length; m++){
    			if(paths[m] == "root")
    				break;
    			data = data.children;
				for(var n=0; n<data.length; n++){
        			var npath = data[n].path.split("/");
        			for(var o=0; o<npath.length; o++){
        				if(npath[o].length == 0)
        					npath.splice(o, 1);
        			}
        			if(npath[npath.length - 1] == paths[m]){
        				data = data[n];
        				break;
        			}
        		}
			}
			insertData(data);
    	});
	};
	
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
		
    	data = data.children;
    	
    	sortData(data);
    	
    	$(".list tbody").html("");
		for(var i=0; i<data.length; i++){
    		var c = i%2 ? "erow" : "orow";
    		var p = data[i].dir == "true" ? "dir.png" : "txt.png";
    		var filenames = data[i].path.split("/");
    		if(filenames[filenames.length - 1].length == 0)
    			filenames.splice(filenames.length - 1, 1);
    		var filename = filenames[filenames.length - 1];
    		
    		var innerhtml = '<tr class="' + c +'">'
        		+'<td><div><img style="margin-right:5px" src="../../resource/frontcss/images/' + p +'" /><span class="ffile" title="' + data[i].path + '">' + filename + '</span></div></td>'
        		+'<td><div>' + getLocalTime(data[i].lastModified) + '</div></td>'
        		+'<td><div>' + data[i].size + '</div></td>';
    		
        	$(".list tbody").append(innerhtml);
        	
        	$(".list tbody tr:eq(" + i +") td:first div span").click(function(){
        		for(var j = 0;j < data.length; j++){
        			if(data[j].path ==  this.title){
        				data = data[j];
        				insertData(data);
        			}
        		}
        	});
		}
		iFrameHeight("iframepage");
	};
	
	$.ajax({
        url : '../../../data/project/' + name + '/view',
        method : 'GET',
		dataType : 'json',
        success : function(data) {
        	all_data = data;
        	insertData(data);
        }
    });
	
}); 