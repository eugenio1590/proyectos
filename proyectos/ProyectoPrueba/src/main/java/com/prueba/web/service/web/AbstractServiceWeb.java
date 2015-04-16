package com.prueba.web.service.web;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public abstract class AbstractServiceWeb {
	
	private static String PATH_PROYECT=null;
	
	/**GENERAL*/
	private String getPath() {
		try {
			String path = this.getClass().getClassLoader().getResource("").getPath();
			String fullPath;

			fullPath = URLDecoder.decode(path, "UTF-8");

			String pathArr[] = fullPath.split("/WEB-INF/classes/");
			fullPath = pathArr[0];

			String reponsePath = "";
			reponsePath = new File(fullPath).getPath() + File.separatorChar;
			return reponsePath;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	protected String getPathProyect(){
		if(PATH_PROYECT==null)
			PATH_PROYECT=getPath();
		return PATH_PROYECT;
	}
	
	/**REPORTES*/
	protected JasperPrint crearReporte(String ruta, Map<String, Object> parametros, List<?> lista){
		try {
			System.out.println("Ruta: "+ruta);
			
			JasperReport jasperRepor = JasperCompileManager.compileReport(ruta);

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperRepor, parametros, new JRBeanCollectionDataSource(
							lista));
			
			return jasperPrint;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		
		return null;
	}
	
	protected byte[] toByteReport(JasperPrint jasperPrint){
		try {
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
