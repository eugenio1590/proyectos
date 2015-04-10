package com.prueba.web.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



//import javax.persistence.EntityManager;
import org.hibernate.ejb.HibernateEntityManager;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.EntityType;




import org.hibernate.Criteria;
import org.hibernate.Session;
//import org.hibernate.annotations.common.util.impl.Log_.logger;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.web.dao.IGenericDao;

public class AbstractJpaDao<T, ID extends Serializable> implements IGenericDao<T, ID> {
	
	private static final Logger logger =  LoggerFactory.getLogger(AbstractJpaDao.class);
	
	private Class<T> persistenceClass = null;
	
	@PersistenceContext(type = PersistenceContextType.EXTENDED) //unitName="puProyectoPrueba", 
	protected HibernateEntityManager classEntityManager;
	protected CriteriaBuilder criteriaBuilder;
	protected CriteriaQuery<T> query;
	protected Root<T> entity;
	protected EntityType entityType;
	protected Selection<? extends T>[] selected;
	protected boolean distinct = false;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbstractJpaDao(Class clazz) {
		this.persistenceClass = clazz;
	}
	
	private HibernateEntityManager getClassEntityManager() {
		return this.classEntityManager;
	}
	
	public void setClassEntityManager(HibernateEntityManager classEntityManager) {
		this.classEntityManager = classEntityManager;
	}

	@Override
	public T findByPrimaryKey(ID id) {
		HibernateEntityManager entityManager = this.getClassEntityManager();
		return entityManager.find(persistenceClass, id);
	}
	
	@Override
	public List<T> findAll(int first, int numberElements) {
		HibernateEntityManager entityManager = this.getClassEntityManager();
		CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(persistenceClass);
		criteriaQuery.from(persistenceClass);
		TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
		if (numberElements > -1) {
			//Estaba al revez
			typedQuery.setFirstResult(first);
			typedQuery.setMaxResults(numberElements);
		}
		return typedQuery.getResultList();
	}
	
	@Override
	public List<T> findAll() {
		return findAll(0, -1);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findByExample(T exampleInstance, String[] excludeProperty, 
									boolean caseSensitive, boolean enableLike) {
		HibernateEntityManager entityManager = this.getClassEntityManager();
		Session session = (Session) entityManager.getDelegate();
		Criteria criteria = session.createCriteria(persistenceClass);
		Example example = Example.create(exampleInstance);
		if (caseSensitive) {
			example.ignoreCase();
		}
		if (enableLike) {
			example.enableLike();
		}
		for (String prop : excludeProperty) {
			example.excludeProperty(prop);
		}
		criteria.add(example);
		return criteria.list();
	}
	
	@Override
	public List<T> findByExample(T exampleInstance, String[] excludeProperty) {
		return findByExample(exampleInstance, excludeProperty, false, false);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Throwable.class)
	public T save(T entity) {
		HibernateEntityManager entityManager = this.getClassEntityManager();
		entityManager.persist(entity);
		return entity;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Throwable.class)
	public T update(T entity) {
		HibernateEntityManager entityManager = this.getClassEntityManager();
		entityManager.merge(entity);
		return entity;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Throwable.class)
	public void delete(T entity) {
		HibernateEntityManager entityManager = this.getClassEntityManager();
		entityManager.remove(entity);
	}
	
	@Override
	public long countAll() {
		// TODO Auto-generated method stub
		CriteriaBuilder cb = classEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		cq.select(cb.count(cq.from(persistenceClass)));
		return classEntityManager.createQuery(cq).getSingleResult().intValue();
	}
	
	//
	@Override
	public void beginTransaction() {
		HibernateEntityManager entityManager = this.getClassEntityManager();
		entityManager.getTransaction().begin();
	}
	
	@Override
	public void commitTransaction() {
		HibernateEntityManager entityManager = this.getClassEntityManager();
		entityManager.getTransaction().commit();
	}
	
	@Override
	public T refresh(T entity) {
		HibernateEntityManager entityManager = this.getClassEntityManager();
		entityManager.refresh(entity);
		return entity;
	}
	
	@Override
	public void rollbackTransaction() {
		HibernateEntityManager entityManager = this.getClassEntityManager();
		entityManager.getTransaction().rollback();
	}
	//
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<T> excuteJPQL(String query, Map<String, Object> params, int first, int numberElements) {
		//logger.entering(this.getClass().getName(), "excuteJPQL", new Object[]{query, params, first, numberElements});
		HibernateEntityManager entityManager = this.getClassEntityManager();
		Query jqplQuery = entityManager.createQuery(query);
		if (numberElements > 0) {
			jqplQuery.setFirstResult(first).setMaxResults(numberElements).getResultList();
		}
		if (params != null) {
			for (String paramName : params.keySet()) {
				//logger.log(Level.INFO, "Binding param: {0} value: {1}", new Object[]{paramName, params.get(paramName)});
				jqplQuery.setParameter(paramName, params.get(paramName));
			}
		}
		return jqplQuery.getResultList();
	}
	
	@Override
	public Collection<T> excuteJPQL(String query) {
		return excuteJPQL(query, null, 0, 0);
	}
	
	@Override
	public void close() {
		this.getClassEntityManager().close();
	}
	
	/**METODOS PROPIOS DE LA CLASE**/
	protected CriteriaQuery<T> crearCriteria(){
		HibernateEntityManager entityManager = this.getClassEntityManager();
		criteriaBuilder = entityManager.getCriteriaBuilder();
		query = criteriaBuilder.createQuery(persistenceClass);
		return query;
	}
	
	protected Map<String, Join> crearJoins(Map<String, JoinType> entidades){
		Map<String, Join> joins = new HashMap();
		if(query==null)
			this.crearCriteria();
		entity = query.from(persistenceClass);
		entityType = entity.getModel();
		if(entidades != null){
			for (String entidad : entidades.keySet()){
				Join<Object, Object> join = entity.join(entidad, entidades.get(entidad));
				joins.put(entidad, join);
			}
		}
		this.distinct=false;
		this.selected=null;
		//query.select(entity);
		return joins;
	}
	
	protected List<T> ejecutarCriteria(Predicate[] conditions, Map<String, Boolean> orders, 
										int first, int numberElements){
		List<T> lista = null;
		HibernateEntityManager entityManager = this.getClassEntityManager();
		if(query==null)
			this.crearCriteria();
		
		if(selected!=null)
			this.query.multiselect(selected).distinct(distinct);
		else
			this.query.select(entity);

		query=(conditions!=null) ? query.where(conditions) : query;
		if(orders != null)
		for(String orden : orders.keySet()){
			Boolean ascendente = orders.get(orden);
			query=(ascendente) ? query.orderBy(criteriaBuilder.asc(entity.get(orden))) 
					: query.orderBy(criteriaBuilder.desc(entity.get(orden))) ;
		}
		TypedQuery<T> typedQuery = entityManager.createQuery(query);
		if (numberElements > -1) {
			typedQuery.setFirstResult(first);
			typedQuery.setMaxResults(numberElements);
		}
		lista = typedQuery.getResultList();
		return lista;
	}
	
	protected List<T> ejecutarCriteria(Predicate[] conditions, List<Order> orders, 
										int numberElements){
		return this.ejecutarCriteria(conditions, orders, 0, numberElements);
	}
	
	
	protected List<T> ejecutarCriteria(Predicate[] conditions, List<Order> orders, 
			int first, int numberElements){
		List<T> lista = null;
		HibernateEntityManager entityManager = this.getClassEntityManager();
		if(query==null)
			this.crearCriteria();

		if(selected!=null)
			this.query.multiselect(selected).distinct(distinct);
		else
			this.query.select(entity);

		query=(conditions!=null) ? query.where(conditions) : query;
		if(orders != null)
			query=query.orderBy(orders);
		TypedQuery<T> typedQuery = entityManager.createQuery(query);
		if (numberElements > -1) {
			typedQuery.setFirstResult(first);
			typedQuery.setMaxResults(numberElements);
		}
		lista = typedQuery.getResultList();
		return lista;
	}

	protected List<T> ejecutarCriteria(Predicate[] conditions, Map<String, Boolean> orders, 
			int numberElements){
		return this.ejecutarCriteria(conditions, orders, 0, numberElements);
	}


	protected List<T> ejecutarCriteriaParameters(Predicate[] conditions, Map<String, Object> parameters,
			Map<String, Boolean> orders, int first, int numberElements){
		List<T> lista = null;
		HibernateEntityManager entityManager = this.getClassEntityManager();
		if(query==null)
			this.crearCriteria();
		
		if(selected!=null)
			this.query.multiselect(selected).distinct(distinct);
		else
			this.query.select(entity);

		query=(conditions!=null) ? query.where(conditions) : query;
		if(orders != null)
		for(String orden : orders.keySet()){
			Boolean ascendente = orders.get(orden);
			query=(ascendente) ? query.orderBy(criteriaBuilder.asc(entity.get(orden))) 
					: query.orderBy(criteriaBuilder.desc(entity.get(orden))) ;
		}
		TypedQuery<T> typedQuery = entityManager.createQuery(query);
		if (numberElements > -1) {
			typedQuery.setFirstResult(first);
			typedQuery.setMaxResults(numberElements);
		}
		if(parameters!=null)
		for(String parametro : parameters.keySet()){
			typedQuery = typedQuery.setParameter(parametro, parameters.get(parametro));
		}
		
		lista = typedQuery.getResultList();
		return lista;
	}
	
	protected List<T> ejecutarCriteriaParameters(Predicate[] conditions, Map<String, Object> parameters,
			Map<String, Boolean> orders, int numberElements){
		return this.ejecutarCriteriaParameters(conditions, parameters, orders, 0, -1);
	}
	
	protected List<T> ejecutarCriteriaGroupBy(Predicate[] conditions, Map<String, Object> parameters, 
			List<Expression<?>> groupBy, Map<String, Boolean> orders, int first, int numberElements){
		
		List<T> lista = null;
		HibernateEntityManager entityManager = this.getClassEntityManager();
		if(query==null)
			this.crearCriteria();
		
		if(selected!=null)
			this.query.multiselect(selected).distinct(distinct);
		else
			this.query.select(entity);

		query=(conditions!=null) ? query.where(conditions) : query;
		if(groupBy!=null)
			query = query.groupBy(concatenaArrayExpression(groupBy));
		
		if(orders != null)
		for(String orden : orders.keySet()){
			Boolean ascendente = orders.get(orden);
			query=(ascendente) ? query.orderBy(criteriaBuilder.asc(entity.get(orden))) 
					: query.orderBy(criteriaBuilder.desc(entity.get(orden))) ;
		}
		TypedQuery<T> typedQuery = entityManager.createQuery(query);
		if (numberElements > -1) {
			typedQuery.setFirstResult(first);
			typedQuery.setMaxResults(numberElements);
		}
		if(parameters!=null)
		for(String parametro : parameters.keySet()){
			typedQuery = typedQuery.setParameter(parametro, parameters.get(parametro));
		}
		
		lista = typedQuery.getResultList();
		return lista;
	}
	
	protected List<T> ejecutarCriteriaGroupBy(Predicate[] conditions, Map<String, Object> parameters, 
			List<Expression<?>> groupBy, Map<String, Boolean> orders){
		return this.ejecutarCriteriaGroupBy(conditions, parameters, groupBy, orders, 0, -1);
	}
	
	protected List<T> ejecutarCriteriaOrder(Predicate[] conditions, Map<String, Object> parameters, 
			List<Expression<?>> groupBy, List<Order> orders, int first, int numberElements){
		
		List<T> lista = null;
		HibernateEntityManager entityManager = this.getClassEntityManager();
		if(query==null)
			this.crearCriteria();
		
		if(selected!=null)
			this.query.multiselect(selected).distinct(distinct);
		else
			this.query.select(entity);

		query=(conditions!=null) ? query.where(conditions) : query;
		if(groupBy!=null)
			query = query.groupBy(concatenaArrayExpression(groupBy));
		
		if(orders!=null)
		query=query.orderBy(orders); 
		
		TypedQuery<T> typedQuery = entityManager.createQuery(query);
		if (numberElements > -1) {
			typedQuery.setFirstResult(first);
			typedQuery.setMaxResults(numberElements);
		}
		if(parameters!=null)
		for(String parametro : parameters.keySet()){
			typedQuery = typedQuery.setParameter(parametro, parameters.get(parametro));
		}
		
		lista = typedQuery.getResultList();
		return lista;
	}
	
	protected List<T> ejecutarCriteriaGroupBy(Predicate[] conditions, Map<String, Object> parameters, 
			List<Expression<?>> groupBy, List<Order> orders){
		return this.ejecutarCriteriaOrder(conditions, parameters, groupBy, orders, 0, -1);
	}
	
	public static <X> Predicate[] concatenaArrayPredicate(List<X> array){
		if(array.size()==0)
			return null;
		
		return array.toArray(new Predicate[]{});
	}
	
	public static <X> Expression[] concatenaArrayExpression(List<X> array){
		if(array.size()==0)
			return null;
		
		return array.toArray(new Expression[]{});
	}
}