/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.utils;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author tangchao
 */
@Repository
public abstract class BaseDaoSupport {

    @Autowired
    protected SessionFactory sessionFactory;

    /**
     * gerCurrentSession 会自动关闭session，使用的是当前的session事务
     * 
     * @return
     */
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * openSession 需要手动关闭session 意思是打开一个新的session
     * 
     * @return
     */
    public Session getNewSession() {
        return sessionFactory.openSession();
    }

    public void flush() {
        getSession().flush();
    }

    public void clear() {
        getSession().clear();
    }

    /**
     * 根据 id 查询信息
     * 
     * @param c
     * @param id
     * @return
     */
    public Object load(Class c, String id) {
        Session session = getSession();
        return session.get(c, id);
    }

    /**
     * 获取所有信息
     * 
     * @param c 
     *        
     * @return
     */
    public List getAllList(Class c) {
        String hql = "from " + c.getName();
        Session session = getSession();
        return session.createQuery(hql).list();
    }

    /**
     * 获取总数量
     * 
     * @param c
     * @return
     */
    public Long getTotalCount(Class c) {
        Session session = getSession();
        String hql = "select count(*) from " + c.getName();
        Long count = (Long) session.createQuery(hql).uniqueResult();
        return count != null ? count : 0;
    }

    /**
     * 保存
     * 
     * @param bean 
     *            
     */
    public void save(Object bean) {
        Session session = getSession();
        session.save(bean);
    }

    /**
     * 更新
     * 
     * @param bean 
     *            
     */
    public void update(Object bean) {
        Session session = getSession();
        session.update(bean);
    }

    /**
     * 删除
     * 
     * @param bean 
     *            
     */
    public void delete(Object bean) {
        Session session = getSession();
        session.delete(bean);
    }

    /**
     * 根据ID删除
     * 
     * @param c 类
     *            
     * @param id ID
     *            
     */
    //@SuppressWarnings({ "rawtypes" })
    public void delete(Class c, Integer id) {
        Session session = getSession();
        Object obj = session.get(c, id);
        session.delete(obj);
    }

    /**
     * 批量删除
     * 
     * @param c 类
     *            
     * @param ids ID 集合
     *            
     */
    //@SuppressWarnings({ "rawtypes" })
    public void delete(Class c, String[] ids) {
        for (String id : ids) {
            Object obj = getSession().get(c, id);
            if (obj != null) {
                getSession().delete(obj);
            }
        }
    }

    public Object get(Class<?> clazz, Object id) {
        if (id instanceof Integer) {
            return getSession().get(clazz, (Integer) id);
        } else if (id instanceof String) {
            return getSession().get(clazz, (String) id);
        }
        return null;
    }

    public void createOnly(Object obj) {
        getSession().save(obj);
    }

    public void create(Object obj) {
        getSession().save(obj);
    }

    public void updateOnly(Object obj) {
        getSession().update(obj);
    }


    public void saveOrUpdate(Object obj) {
        getSession().saveOrUpdate(obj);
    }
}
