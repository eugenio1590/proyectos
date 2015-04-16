function loadReport (ruta, frame) {
	jq.ajax({
		url: ruta,
		type: "get",
		// callback handler that will be called on success
		success: function(response, textStatus, jqXHR){
			var data = response;
			if(data.reporte!=null){
				var obj = jq("$"+frame);
				obj.empty();
				obj.append("<iframe src='" + "data:application/pdf;base64,"+data.reporte + "' />");
			}
		}
	});
}