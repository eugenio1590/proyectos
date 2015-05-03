package com.prueba.web.configuracion.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import com.prueba.web.configuracion.dao.MenuRepository;
import com.prueba.web.configuracion.dao.OperacionRepository;
import com.prueba.web.configuracion.dao.impl.MenuDAO;
import com.prueba.web.configuracion.dao.impl.UsuarioDAO;
import com.prueba.web.configuracion.dao.UsuarioRepository;
import com.prueba.web.personas.dao.ClienteRepository;
import com.prueba.web.personas.dao.EmpleadoRepository;
import com.prueba.web.personas.dao.impl.ClienteDAO;
import com.prueba.web.personas.dao.impl.EmpleadoDAO;
import com.prueba.web.model.Cliente;
import com.prueba.web.model.Empleado;
import com.prueba.web.model.Group;
import com.prueba.web.model.GroupMember;
import com.prueba.web.model.Menu;
import com.prueba.web.model.Operacion;
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
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private EmpleadoRepository empleadoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private OperacionRepository operacionRepository;

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
		return usuarioRepository.findById(id);
	}
	
	@Override
	public Usuario consultarUsuario(String usuario, String clave) {
		// TODO Auto-generated method stub
		usuario = (usuario==null) ? "" : usuario;
		clave = (clave==null) ? "" : clave;
		List<Usuario> listeUsuario = 
				usuarioRepository.findByUsernameContainingIgnoreCaseOrPaswordContainingIgnoreCase(usuario, clave);
		if(listeUsuario.size()>0)
			return listeUsuario.get(0);
		return null;
	}

	@Override
	public Usuario grabarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		//usuario.setPasword(this.bcryptEncoder.encode(usuario.getPasword()));
		return usuarioRepository.save(usuario);
	}
	
	@Override
	public Usuario actualizarUsuario(Usuario usuario, boolean encriptar){
		/*if(encriptar)
			usuario.setPasword(this.bcryptEncoder.encode(usuario.getPasword()));*/
		return grabarUsuario(usuario);
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
			usuarioRepository.delete(usuario);
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
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Map<String, Object> parametros = new HashMap<String, Object>();
		Page<Usuario> pageUsuario = usuarioRepository.findAll(
				usuarioDAO.consultarUsuarios(usuarioF, fieldSort, sortDirection), new PageRequest(pagina, limit));
		parametros.put("total", Long.valueOf(pageUsuario.getTotalElements()).intValue());
		parametros.put("usuarios", pageUsuario.getContent());
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
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Map<String, Object> parametros = new HashMap<String, Object>();
		Page<Usuario> pageUsuario = usuarioRepository.findAll(
				usuarioDAO.consultarUsuariosAsignadosGrupo(usuarioF, idGrupo, fieldSort, sortDirection), 
				new PageRequest(pagina, limit));
		parametros.put("total", Long.valueOf(pageUsuario.getTotalElements()).intValue());
		parametros.put("usuarios", pageUsuario.getContent());
		return parametros;
	}
	
	@Override
	public Map<String, Object> consultarUsuariosNoAsignadosGrupo(Persona usuarioF, int idGrupo,
			String fieldSort, Boolean sortDirection, int pagina, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		//parametros.put("total", usuarioDAO.consultarUsuariosNoAsignadosGrupo(usuarioF, idGrupo, fieldSort, sortDirection, 0, -1).size());
		//parametros.put("usuarios", usuarioDAO.consultarUsuariosNoAsignadosGrupo(usuarioF, idGrupo, fieldSort, sortDirection, pagina*limit, limit));
		return parametros;
	}
	
	//Usuarios Especificos:
	//1.1 Empleados
	@Override
	public Map<String, Object> consultarEmpleadosSinUsuarios(Persona empleadoF, String fieldSort, Boolean sortDirection, 
			int pagina, int limit){
		EmpleadoDAO empleadoDAO = new EmpleadoDAO();
		Map<String, Object> parametros = new HashMap<String, Object>();
		Page<Empleado> pageEmpleado = empleadoRepository.findAll(
				empleadoDAO.consultarEmpleadosSinUsuario(empleadoF, fieldSort, sortDirection), 
				new PageRequest(pagina, limit));
		parametros.put("total", Long.valueOf(pageEmpleado.getTotalElements()).intValue());
		parametros.put("empleados", pageEmpleado.getContent());
		return parametros;
	}
	
	//2.2 Clientes
	@Override
	public Map<String, Object> consultarClientesSinUsuarios(Persona clienteF, String fieldSort, Boolean sortDirection,
			int pagina, int limit){
		ClienteDAO clienteDAO = new ClienteDAO();
		Map<String, Object> parametros = new HashMap<String, Object>();
		Page<Cliente> pageCliente = clienteRepository.findAll(
				clienteDAO.consultarClientesSinUsuario(clienteF, fieldSort, sortDirection), 
				new PageRequest(pagina, limit));
		parametros.put("total", Long.valueOf(pageCliente.getTotalElements()).intValue());
		parametros.put("clientes", pageCliente.getContent());
		return parametros;
	}
	
	//2. Menus
	@Override
	public Map<String, Object> consultarRootPadres(int pagina, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		Page<Menu> pageMenu = menuRepository.findByPadreIsNull(new PageRequest(pagina, limit));
		parametros.put("total", Long.valueOf(pageMenu.getTotalElements()).intValue());
		parametros.put("menu", pageMenu.getContent());
		return parametros;
	}
	
	@Override
	public Map<String, Object> consultarSubRamas(int idPadre, int pagina, int limit){
		MenuDAO menuDAO = new MenuDAO();
		Map<String, Object> parametros = new HashMap<String, Object>();
		Page<Menu> pageMenu = menuRepository.findAll(menuDAO.consultarSubRamas(idPadre), new PageRequest(pagina, limit));
		parametros.put("total", Long.valueOf(pageMenu.getTotalElements()).intValue());
		parametros.put("menu", pageMenu.getContent());
		return parametros;
	}
	
	@Override
	public Map<String, Object> consultarHijosNoAsignadoGrupo(int idGrupo, int idPadre, int pagina, int limit){
		MenuDAO menuDAO = new MenuDAO();
		Map<String, Object> parametros = new HashMap<String, Object>();
		Page<Menu> pageMenu = menuRepository.findAll(
				menuDAO.consultarHijosNoAsignadoGrupo(idGrupo, idPadre), 
				new PageRequest(pagina, limit));
		parametros.put("total", Long.valueOf(pageMenu.getTotalElements()).intValue());
		parametros.put("menu", pageMenu.getContent());
		return parametros;
	}
	
	//Operaciones - Menu
	@Override
	public Map<String, Object> consultarOperaciones(int pagina, int limit){
		Map<String, Object> parametros = new HashMap<String, Object>();
		Page<Operacion> pageOperacion = operacionRepository.findAll(new PageRequest(pagina, limit));
		parametros.put("total", Long.valueOf(pageOperacion.getTotalElements()).intValue());
		parametros.put("operaciones", pageOperacion.getContent());
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
}
