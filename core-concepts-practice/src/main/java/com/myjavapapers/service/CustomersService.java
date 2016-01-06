package com.myjavapapers.service;

import com.myjavapapers.beans.Customers;
import com.myjavapapers.dao.CustomerDao;

public class CustomersService {
	CustomerDao customerDao;

	public CustomersService(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	public boolean update(Integer CustomersId, String name) {
		Customers Customers = customerDao.fetchCustomers(CustomersId);
		if (Customers != null) {
			Customers updatedCustomers = new Customers(Customers.getCustomerId(), name);
			customerDao.updateCustomers(updatedCustomers);
			return true;
		} else {
			return false;
		}
	}

}
