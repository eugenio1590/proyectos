package com.prueba.web;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.prueba.web.configuracion.dao.UsuarioRepository;
import com.prueba.web.configuracion.service.ServicioControlUsuario;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private ServicioControlUsuario servicioControlUsuario;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping(value = "/inicioSession", method = RequestMethod.GET)
	public String  inicioSession(Locale locale, Model model){
		if(isRememberMeAuthenticated())
			return mypage(locale, model);
		
		return "inicioSession/index.zul";
	}
	
	@RequestMapping(value = "/admin/home", method = RequestMethod.GET)
	public String  mypage(Locale locale, Model model){
		logger.info("Welcome Admin home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		//return "admin/home.jsp";
		return "sistema/index.zul";
	}
	
	@RequestMapping(value="/sessionExpirada", method = RequestMethod.GET)
	public String sessionExpirada(){
		return "inicioSession/sessionExpirada.zul";
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	/**
	 * Descripcion: Verifica if el usuario al iniciar session uso remember me
	 * Parametros: Ninguno
	 * Retorno: Boolean - si el usuario uso o no el remember me 
	 */
	private Boolean isRememberMeAuthenticated() {
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user instanceof User)
			if(servicioControlUsuario.validarAutenticacion((User) user)){
				SecurityContextHolder.getContext().setAuthentication(servicioControlUsuario.consultarAutenticacion((User) user));
				return true;
			}
 
		return false;
	}
}
