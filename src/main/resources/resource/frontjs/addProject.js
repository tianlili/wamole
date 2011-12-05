$(function() {
	$.validator.setDefaults({       
		  submitHandler: function(form) {    
			  addProject();
		  }       
	});   
	
	jQuery.validator.addMethod("stringCheck", function(value, element) {       
		 	return this.optional(element) || value == '1';       
		}, "test");   
	
	$('#saveprojects').validate({   
	    rules: {   
	    	name: {   
	            required:true,
	            max:50,
	            stringCheck:true
	        },
	        path: {   
	            required:true,
	            max:80
	        }
	    },   
	    messages: {   
	    	name: {       
	            required: "<font style='color:red'>This field is required.</font>",
	            max: "Please enter a value less than or equal to 50."
	        },
		    path: {       
	            required: "<font style='color:red'>The Path is required.</font>",
	            max: "Please enter a value less than or equal to 80."
	        }
	    }
	});   


    function addProject(){
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
    };
}); 