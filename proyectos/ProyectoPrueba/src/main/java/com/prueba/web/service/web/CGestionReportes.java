package com.prueba.web.service.web;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.sf.jasperreports.engine.JasperPrint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lowagie.text.pdf.codec.Base64;
import com.prueba.web.dao.ArticuloDAO;

@Component
@Path("gestionReportes")
public class CGestionReportes extends AbstractServiceWeb {

	@Autowired
	private ArticuloDAO articuloDAO;
	
	@GET
	@Path("/articulos/")
    @Produces({ "application/json; charset=UTF-8" })
	public Map<String, Object> generarReporteArticulos() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		//parametros.put("total", articuloDAO.countAll());
		//parametros.put("articulos", articuloDAO.findAll());
		//return parametros;
		
		JasperPrint jasperPrint=crearReporte(getPathProyect()+"resources/reportes/rArticulos.jrxml", null, articuloDAO.findAll());
		parametros.put("reporte", Base64.encodeBytes(toByteReport(jasperPrint)));
		return parametros;
	}
	
	
}
