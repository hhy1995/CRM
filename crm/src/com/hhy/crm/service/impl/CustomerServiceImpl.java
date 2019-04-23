package com.hhy.crm.service.impl;

import java.util.List;

import org.hibernate.Session;

import com.hhy.crm.domain.Customer;
import com.hhy.crm.service.CustomerService;
import com.hhy.crm.utils.HibernateUtils;

public class CustomerServiceImpl implements CustomerService {

	public List<Customer> findnoassociationCustomers() {
		Session session = HibernateUtils.openSession();
		session.beginTransaction();

		String hql = "from Customer where decidedzone_id is null";
		List<Customer> customers = session.createQuery(hql).list();

		session.getTransaction().commit();
		session.close();

		return customers;
	}

	public List<Customer> findhasassociationCustomers(String decidedZoneId) {
		Session session = HibernateUtils.openSession();
		session.beginTransaction();

		String hql = "from Customer where decidedzone_id = ?";
		List<Customer> customers = session.createQuery(hql).setParameter(0, decidedZoneId).list();

		session.getTransaction().commit();
		session.close();

		return customers;
	}

	public void assignCustomersToDecidedZone(Integer[] customerIds, String decidedZoneId) {
		Session session = HibernateUtils.openSession();
		session.beginTransaction();

		// 取消定区所有关联的客户
		String hql2 = "update Customer set decidedzone_id=null where decidedzone_id=?";
		session.createQuery(hql2).setParameter(0, decidedZoneId).executeUpdate();

		// 进行关联
		String hql = "update Customer set decidedzone_id=? where id =?";
		if (customerIds != null) {
			for (Integer id : customerIds) {
				session.createQuery(hql).setParameter(0, decidedZoneId).setParameter(1, id).executeUpdate();
			}
		}
		session.getTransaction().commit();
		session.close();
	}

}
