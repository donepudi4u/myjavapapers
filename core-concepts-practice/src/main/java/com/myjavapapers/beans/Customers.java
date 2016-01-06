package com.myjavapapers.beans;

public class Customers {

	private Integer customerId;
	private String customerName;
	private Integer customerAge;
	private String customerAddress;

	/**
	 * Default Constructor
	 */
	public Customers() {
	}

	public Customers(Integer customerId, String customerName) {
		this.customerId = customerId;
		this.customerName = customerName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getCustomerAge() {
		return customerAge;
	}

	public void setCustomerAge(Integer customerAge) {
		this.customerAge = customerAge;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	@Override
	public String toString() {
		return "Customers [customerId=" + customerId + ", customerName="
				+ customerName + ", customerAge=" + customerAge
				+ ", customerAddress=" + customerAddress + "]";
	}

}
