/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.openapi.execute.dao;

import com.lushell.tc.dbpaas.openapi.execute.entity.EnviromentDO;
import com.lushell.tc.dbpaas.openapi.execute.entity.TestCaseDO;
import com.lushell.tc.dbpaas.utils.BaseDaoSupport;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author tangchao
 */
@Repository("TestDAO")
public class TestDAOImpl extends BaseDaoSupport implements TestDAO {

    @Override
    public List<TestCaseDO> getTestCaseByEnviroment(String enviroment) {
        return getSession().createQuery("from TestCaseDO where enviroment=:enviroment")
                .setString("enviroment", enviroment).list();
    }

    @Override
    public String getDomian(String enviroment) {
        Query query = getSession().createQuery("from EnviromentDO where enviroment=:enviroment");
        query.setString("enviroment", enviroment);
        List<EnviromentDO> list = query.list();
        for (EnviromentDO w : list) {
            return w.getUrl();
        }
        return null;
    }

    @Override
    public List<EnviromentDO> getAllEnviroment() {
        return super.getAllList(EnviromentDO.class);
    }

    @Override
    public void delete(TestCaseDO obj) {
        String hql = "delete TestCaseDO as p where p.id=:id";
        Query query = getSession().createQuery(hql);
        query.setInteger("id", obj.getId());
        query.executeUpdate();
    }

    @Override
    public TestCaseDO getTestCaseById(Integer id) {
        return (TestCaseDO) getSession().get(TestCaseDO.class, id);
    }
}
