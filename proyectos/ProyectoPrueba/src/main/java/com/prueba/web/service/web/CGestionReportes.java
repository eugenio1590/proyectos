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
import com.prueba.web.inventario.dao.ArticuloRepository;

@Component
@Path("gestionReportes")
public class CGestionReportes extends AbstractServiceWeb {

	@Autowired
	private ArticuloRepository articuloRepository;
	
	@GET
	@Path("/articulos/")
    @Produces({ "application/json; charset=UTF-8" })
	public Map<String, Object> generarReporteArticulos() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		//parametros.put("total", articuloDAO.countAll());
		//parametros.put("articulos", articuloDAO.findAll());
		//return parametros;
		
		JasperPrint jasperPrint=crearReporte(getPathProyect()+"resources/reportes/rArticulos.jrxml", null, articuloRepository.findAll());
		parametros.put("reporte", Base64.encodeBytes(toByteReport(jasperPrint)));
		return parametros;
	}
	
	@GET
	@Path("/articulosPrueba/")
    @Produces({ "application/json; charset=UTF-8" })
	public Map<String, Object> articulosPrueba() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", articuloRepository.count());
		parametros.put("articulos", articuloRepository.findAll());
		return parametros;
	}
	
}
