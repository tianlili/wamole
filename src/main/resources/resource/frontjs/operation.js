$(function() {
	$(".flexme").flexigrid({
		url : 'data/project',
		method : 'GET',
		dataType : 'json',
		colModel : [{
			display : 'Name',
			name : 'name',
			width : 280,
			sortable : true,
			align : 'left'
		}, {
			display : 'Path',
			name : 'path',
			width : 420,
			sortable : true,
			align : 'left'
		}, {
			display : 'View',
			name : 'view',
			width : 280,
			sortable : true,
			align : 'left'
		}],
		buttons : [ {
			name : '<img src="../resource/frontcss/images/add.ico" width="20px" height="20px">',
			bclass : 'add',
			onpress : operation
		},{
			name : '<img src="../resource/frontcss/images/edit.ico" width="20px" height="20px">',
			bclass : 'edit',
			onpress : operation
		}, {
			name : '<img src="../resource/frontcss/images/delete.ico" width="20px" height="20px">',
			bclass : 'delete',
			onpress : operation
		}],
		searchitems : [{
			display : 'Name',
			name : 'name',
			isdefault : true
		}, {
			display : 'Path',
			name : 'path',
			isdefault : false
		}],
		sortname : "name",
		sortorder : "asc",
		usepager : true,
		title : 'Projects',
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 1020
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