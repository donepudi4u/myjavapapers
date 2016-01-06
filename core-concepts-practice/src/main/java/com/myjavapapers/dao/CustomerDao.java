package com.myjavapapers.dao;

import com.myjavapapers.beans.Customers;

public interface CustomerDao {
	Customers fetchCustomers(int customerId);
	void updateCustomers(Customers customers);
}
