$(function() {
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