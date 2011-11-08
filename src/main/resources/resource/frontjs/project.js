$(function() {
	function iFrameHeight(id) { 
		var ifm = parent.document.getElementById(id); 
		document.title = ifm.contentDocument.title;
		var subWeb = document.frames ? document.frames[id].document : ifm.contentDocument; 
		if(ifm != null && subWeb != null) { 
			ifm.height = subWeb.body.scrollHeight; 
		} 
	} 
	
	$.ajax({
        url : 'data/project',
        method : 'GET',
		dataType : 'json',
        success : function(data) {
        	for(var i=0; i<data.length; i++){
        		var c = i%2 ? "erow" : "orow";
        		var innerhtml = '<tr class="' + c +'">'
            		+'<td><div style="text-align:left;">' + data[i].name + '</div></td>'
            		+'<td><div style="text-align:left;">' + data[i].path + '</div></td>'
            		+'<td><div style="text-align:left;"><div style="width:20px" class="fview"><img id=' + data[i].name + '_view src="../resource/frontcss/images/view.ico" width=20px height=20px/></div></div></td></tr>'
            	$(".projects tbody").append(innerhtml);
        	}
        	iFrameHeight("iframepage");
        	
        	$("[id$='_view']").click(function(){
				var name = this.id.substring(0, this.id.indexOf("_view"));
				window.location.href = "../../project/"+ name +"/view";
			});
        }
    });
		
    function operation(com, grid) {
	    if(com.indexOf('add') > 0){
	    	$("#projects")[0].style.display = "";
		    $("#saveprojects input[type='text']").each(function() {
			        $(this).val("");
		        });
	    }
	    if(com.indexOf('edit') > 0){
	    	selected_count = $('.trSelected', grid).length;
		    if (selected_count == 0) {
			    alert('请选择一条记录!');
			    return;
		    }
		    if (selected_count > 1) {
			    alert('抱歉只能同时修改一条记录!');
			    return;
		    }
		    data = new Array();
		    $('.trSelected td', grid).each(function(i) {
			        data[i] = $(this).children('div').text();
		        });
		    
		    $("#projects")[0].style.display = "";
		    $('#saveprojects input[name="name"]').val(data[0]);
		    $('#saveprojects input[name="path"]').val(data[1]);
	    }
	    if(com.indexOf('delete') > 0){
	    	 selected_count = $('.trSelected', grid).length;
			    if (selected_count == 0) {
				    alert('请选择一条记录!');
				    return;
			    }
			    names = '';
			    $('.trSelected td:nth-child(1) div', grid).each(function(i) {
				        if (i)
					        names += ',';
				        names += $(this).text();
			        });
			    if (confirm("确定删除项目[" + names + "]?")) {
				    delUser(names);
			    }
	    }
    };
	
    function delUser(names) {
	    $.ajax({
		        url : '',
		        data : {
			        names : names
		        },
		        type : 'POST',
		        dataType : 'json',
		        success : function() {
			        $('#flex').flexReload();//表格重载
		        }
	        });
    }
    $("#submit").click(function(){
    	 $.ajax({
		        url : "",
		        data : $("#saveprojects").serialize(),
		        type : 'POST',
		        dataType : 'json',
		        success : function(data) {
		        	$("#projects")[0].style.display = "none";
			        $('#flex').flexReload();
		        }
	        });
    });
    
}); 