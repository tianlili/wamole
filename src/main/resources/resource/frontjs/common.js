$(function() {
	$(".tab .tDiv td div").click(function(){
		if(this.id == "details_tab"){
			window.location.href = "/project/t/files";
		}
		if(this.id == "files_tab"){
			window.location.href = "/project/tc/files";
		}	
		if(this.id == "builds_tab"){
			window.location.href = "/project/t/files";
		}
	});
}); 