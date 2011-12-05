$(function() {
	$(".tab .tDiv td div").click(function(){
		if(this.id == "details_tab"){
			window.location.href = "/project/t/files";
		}
		if(this.id == "files_tab"){
			window.location.href = "/project/tc/files";
		}	
		if(this.id == "builds_tab"){
			window.location.href = "/project/t/builds";
		}
	});
}); 

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
