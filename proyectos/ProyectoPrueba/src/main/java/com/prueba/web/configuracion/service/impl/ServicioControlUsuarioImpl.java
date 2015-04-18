package com.prueba.web.configuracion.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.prueba.web.configuracion.dao.MenuDAO;
import com.prueba.web.configuracion.dao.OperacionDAO;
import com.prueba.web.configuracion.dao.UsuarioDAO;
import com.prueba.web.personas.dao.ClienteDAO;
import com.prueba.web.personas.dao.EmpleadoDAO;
import com.prueba.web.model.Group;
import com.prueba.web.model.GroupMember;
import com.prueba.web.model.Menu;
import com.prueba.web.model.Persona;
import com.prueba.web.model.Usuario;
import com.prueba.web.mvvm.BeanInjector;
import com.prueba.web.configuracion.service.ServicioControlUsuario;
import com.prueba.web.service.impl.AbstractServiceImpl;

@Service
public class ServicioControlUsuarioImpl extends AbstractServiceImpl implements ServicioControlUsuario, UserDetailsService {
	
	@Autowired
	@BeanInjector("bcryptEncoder")
	private BCryptPasswordEncoder bcryptEncoder; //Encriptador de Claves
	
	@Autowired
	@BeanInjector("authenticationManager")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	@BeanInjector("usuarioDAO")
	private UsuarioDAO usuarioDAO;
	
	@Autowired
	@BeanInjector("empleadoDAO")
	private EmpleadoDAO empleadoDAO;
	
	@Autowired
	@BeanInjector("clienteDAO")
	private ClienteDAO clienteDAO;
	
	@Autowired
	@BeanInjector("menuDAO")
	private MenuDAO menuDAO;
	
	@Autowired
	@BeanInjector("operacionDAO")
	private OperacionDAO operacionDAO;

	public ServicioControlUsuarioImpl() {
		// TODO Auto-generated constructor stub
	}
	
	/**Interface: UserDetailsService*/
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Usuario usuario = consultarUsuario(username, null);
		if(usuario!=null){
			if(usuario.getActivo()){
				//Depurar las Autoridades
				List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
				//authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
				if(usuario.getGroupMembers()!=null)
					for(GroupMember grupoMiembro : usuario.getGroupMembers()){
						Group grupo = grupoMiembro.getGroup();
						if(grupo.getAuthority()!=null)
							authorities.add(new GrantedAuthorityImpl(grupo.getAuthority()));

					}
				User securityUser = new User(username, usuario.getPasword(), true, true, true, true,  authorities);
				return securityUser;
			}
			else
				throw new UsernameNotFoundException("Usuario No Activo!!!");
		}
		else
			throw new UsernameNotFoundException("Usuario No Encontrado!!!");
	}
	

	/**Interface: ServicioControlUsuario*/
	//1. Usuarios
	@Override
	public Usuario consultarUsuario(Integer id){
		return usuarioDAO.findByPrimaryKey(id);
	}
	
	@Override
	public Usuario consultarUsuario(String usuario, String clave) {
		// TODO Auto-generated method stub
		return usuarioDAO.consultarUsuario(usuario, clave);
	}

	@Override
	public Usuario grabarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		//usuario.setPasword(this.bcryptEncoder.encode(usuario.getPasword()));
		return usuarioDAO.save(usuario);
	}
	
	@Override
	public Usuario actualizarUsuario(Usuario usuario, boolean encriptar){
		/*if(encriptar)
			usuario.setPasword(this.bcryptEncoder.encode(usuario.getPasword()));*/
		return usuarioDAO.update(usuario);
	}
	
	@Override
	public Boolean cambiarEstadoUsuario(Usuario usuario, boolean estado){
		if((usuario=consultarUsuario(usuario.getId()))!=null) {
			usuario.setActivo(estado);
			actualizarUsuario(usuario, false);
			return true;
		}
		
		return false;
	}
	
	@Override
	public Boolean eliminarUsuario(Usuario usuario){
		if((usuario=consultarUsuario(usuario.getId()))!=null) {
			usuarioDAO.delete(usuario);
			return true;
		}
		
		return false;
	}
	
	@Override
	public Boolean validarAutenticacion(User user){
		try{
			UsernamePasswordAuthenticationToken auth = consultarAutenticacion(user);
			authenticationManager.authenticate(auth);
			return auth.isAuthenticated();
		}
		catch(Exception e){
			System.out.println("Error en Autenticar: "+e.getMessage());
		}
		
		return false;
	}
	
	@Override
	public UsernamePasswordAuthenticationToken consultarAutenticacion(User user){
		UserDetails userDetails = loadUserByUsername(user.getUsername());
		return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
	}
	
	@Override
	public Map<String, Object> consultarUsuarios(Usuario usuarioF, String fieldSort, Boolean sortDirection, 
			int pagina, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", usuarioDAO.consultarUsuarios(usuarioF, fieldSort, sortDirection, 0, -1).size());
		parametros.put("usuarios", usuarioDAO.consultarUsuarios(usuarioF, fieldSort, sortDirection, pagina*limit, limit));
		return parametros;
	}
	
	@Override
	public boolean verificarUsername(String username){
		Usuario usuario = consultarUsuario(username, null);
		return (usuario!=null);
	}
	
	@Override
	public Map<String, Object> consultarUsuariosAsignadosGrupo(Persona usuarioF, int idGrupo,
			String fieldSort, Boolean sortDirection, int pagina, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", usuarioDAO.consultarUsuariosAsignadosGrupo(usuarioF, idGrupo, fieldSort, sortDirection, 0, -1).size());
		parametros.put("usuarios", usuarioDAO.consultarUsuariosAsignadosGrupo(usuarioF, idGrupo, fieldSort, sortDirection, pagina*limit, limit));
		return parametros;
	}
	
	@Override
	public Map<String, Object> consultarUsuariosNoAsignadosGrupo(Persona usuarioF, int idGrupo,
			String fieldSort, Boolean sortDirection, int pagina, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", usuarioDAO.consultarUsuariosNoAsignadosGrupo(usuarioF, idGrupo, fieldSort, sortDirection, 0, -1).size());
		parametros.put("usuarios", usuarioDAO.consultarUsuariosNoAsignadosGrupo(usuarioF, idGrupo, fieldSort, sortDirection, pagina*limit, limit));
		return parametros;
	}
	
	//Usuarios Especificos:
	//1.1 Empleados
	@Override
	public Map<String, Object> consultarEmpleadosSinUsuarios(Persona empleadoF, String fieldSort, Boolean sortDirection, int pagina, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", empleadoDAO.consultarEmpleadosSinUsuario(empleadoF, fieldSort, sortDirection, 0, -1).size());
		parametros.put("empleados", empleadoDAO.consultarEmpleadosSinUsuario(empleadoF, fieldSort, sortDirection, pagina*limit, limit));
		return parametros;
	}
	
	//2.2 Clientes
	@Override
	public Map<String, Object> consultarClientesSinUsuarios(Persona clienteF, String fieldSort, Boolean sortDirection,
			int pagina, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", clienteDAO.consultarClientesSinUsuario(clienteF, fieldSort, sortDirection, 0, -1).size());
		parametros.put("clientes", clienteDAO.consultarClientesSinUsuario(clienteF, fieldSort, sortDirection, pagina*limit, limit));
		return parametros;
	}
	
	//2. Menus
	@Override
	public Map<String, Object> consultarRootPadres(int pagina, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", menuDAO.consultarRootPadres(0, -1).size());
		parametros.put("menu", menuDAO.consultarRootPadres(pagina*limit, limit));
		return parametros;
	}
	
	@Override
	public Map<String, Object> consultarSubRamas(int idPadre, int pagina, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", menuDAO.consultarSubRamas(idPadre, 0, -1).size());
		parametros.put("menu", menuDAO.consultarSubRamas(idPadre, pagina*limit, limit));
		return parametros;
	}
	
	@Override
	public Map<String, Object> consultarHijosNoAsignadoGrupo(int idGrupo, int idPadre, int pagina, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", menuDAO.consultarHijosNoAsignadoGrupo(idGrupo, idPadre, 0, -1).size());
		parametros.put("menu", menuDAO.consultarHijosNoAsignadoGrupo(idGrupo, idPadre, pagina*limit, limit));
		return parametros;
	}
	
	//Operaciones - Menu
	@Override
	public Map<String, Object> consultarOperaciones(int pagina, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("total", operacionDAO.countAll());
		parametros.put("operaciones", operacionDAO.findAll(pagina*limit, limit));
		return parametros;
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	

	/**SETTERS Y GETTERS*/
	public BCryptPasswordEncoder getBcryptEncoder() {
		return bcryptEncoder;
	}

	public void setBcryptEncoder(BCryptPasswordEncoder bcryptEncoder) {
		this.bcryptEncoder = bcryptEncoder;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public UsuarioDAO getUsuarioDAO() {
		return usuarioDAO;
	}

	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}

	public EmpleadoDAO getEmpleadoDAO() {
		return empleadoDAO;
	}

	public void setEmpleadoDAO(EmpleadoDAO empleadoDAO) {
		this.empleadoDAO = empleadoDAO;
	}

	public ClienteDAO getClienteDAO() {
		return clienteDAO;
	}

	public void setClienteDAO(ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}

	public MenuDAO getMenuDAO() {
		return menuDAO;
	}

	public void setMenuDAO(MenuDAO menuDAO) {
		this.menuDAO = menuDAO;
	}

	public OperacionDAO getOperacionDAO() {
		return operacionDAO;
	}

	public void setOperacionDAO(OperacionDAO operacionDAO) {
		this.operacionDAO = operacionDAO;
	}
}
