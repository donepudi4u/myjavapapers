package com.myjavapapers.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

import com.myjavapapers.beans.Country;
import com.myjavapapers.beans.Customers;
import com.myjavapapers.beans.EmployeeBean;
import com.myjavapapers.beans.Orders;

public class MainClass {
	public static void main(String[] args) {
		Configuration configuration = new Configuration();
		Configuration configure = configuration.configure("hibernate-oracle.cfg.xml");
		SessionFactory sessionFactory = configure.buildSessionFactory();
		//Session session = sessionFactory.openSession();
		//Transaction transaction = session.beginTransaction();
		//printCountryDetails(session);
		//printEmployeeDetails(session);
		MainClass mainClass = new MainClass();
		//mainClass.HibernateStatesDemo(sessionFactory);
		//mainClass.insertIntoCustomers(sessionFactory);
		//mainClass.insertIntoOrders(sessionFactory);
		mainClass.getCustomerDetailsNotExisted(sessionFactory);
	}

	private static void printEmployeeDetails(Session session) {
		List employees = session.createQuery("FROM EmployeeBean").list();
		for (Iterator iterator = employees.iterator(); iterator.hasNext();) {
			EmployeeBean employee = (EmployeeBean) iterator.next();
			System.out.println("Employee ID :" + employee.getEmpno());
			System.out.println("Emp Name :" + employee.getName());
			System.out.println("JOB : " + employee.getJob());
			System.out.println("Salary : " + employee.getSal());
		}
	}

	private static void printCountryDetails(Session session) {
		List countries = session.createQuery("FROM Country").list();
		for (Iterator iterator = countries.iterator(); iterator.hasNext();) {
			Country country = (Country) iterator.next();
			System.out.print("Code: " + country.getCode());
			System.out.print("Name: " + country.getName());
			System.out.println("Continent: " + country.getContinent());
		}
	}
	
	private void HibernateStatesDemo(SessionFactory sessionFactory){
		Session session = sessionFactory.openSession();
		EmployeeBean employeeBean = (EmployeeBean)session.get(EmployeeBean.class, Integer.valueOf(7369));
		System.out.println(employeeBean);
		employeeBean.setSal(1400);
		session.close();
		Session session2 = sessionFactory.openSession();
		Transaction transaction = session2.beginTransaction();
		session2.merge(employeeBean);
		transaction.commit();
		EmployeeBean employeeBean2 = (EmployeeBean)session2.get(EmployeeBean.class, Integer.valueOf(7369));
		System.out.println(employeeBean2);
		session2.close();
	}

	private void insertIntoCustomers(SessionFactory sessionFactory){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Customers customers = new Customers();
		customers.setCustomerId(1003);
		customers.setCustomerName("Customer 3");
		customers.setCustomerAge(29);
		customers.setCustomerAddress("Address 3");
		session.save(customers);
		transaction.commit();
		session.close();
	}
	
	private void insertIntoOrders(SessionFactory sessionFactory){
		try{
			Session session = sessionFactory.openSession();
			Transaction transaction = session.beginTransaction();
			Orders orders = new Orders();
			orders.setOrderId(3);
			orders.setOrderAmount(7000);
			orders.setOrderDate(new Date());
			orders.setCustomerId(1003);
			session.saveOrUpdate(orders);
			transaction.commit();
			session.close();
		}catch(Exception e){
			System.out.println("Exception:");
			System.out.println(e);
		}
		
	}
	
	private void getCustomerDetailsNotExisted(SessionFactory sessionFactory){
		try {
			Session session = sessionFactory.openSession();
			Transaction transaction = session.beginTransaction();
			Customers customer = (Customers) session.load(Customers.class,new Integer(1001));
			System.out.println(customer);
			customer.setCustomerAddress("Hyderbad 123");
			/*if (customer != null){
				System.out.println(customer.getCustomerName());
				System.out.println(customer.getCustomerAddress());
				System.out.println(customer.getCustomerAge());
				System.out.println(customer.getCustomerId());
			}*/
			//session.clear();
			//session.disconnect();
			//session.evict(customer);
			System.out.println(session.getEntityName(customer));
			transaction.commit();
			session.close();
			System.out.println(customer);
		}catch (Exception e){
			System.out.println(e);
		}
	}
}
