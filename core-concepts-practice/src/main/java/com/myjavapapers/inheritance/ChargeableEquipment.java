package com.myjavapapers.inheritance;

public class ChargeableEquipment extends ChargeableEvent {
	private String equipmentId;
	private String customer;
	public String getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	

}
