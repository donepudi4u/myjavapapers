package com.myjavapapers.inheritance;

public class OperationsClass {
	
	public void processCharge(ChargeableEvent chargeableEvent){
		if (chargeableEvent instanceof ChargeableEquipment){
			System.out.println("chrgeable Event is Equipment");
			ChargeableEquipment chargeableEquipment= (ChargeableEquipment)chargeableEvent;
			System.out.println(chargeableEquipment.getCustomer());
			System.out.println(chargeableEquipment.getEquipmentId());
			System.out.println("Values From super Class.");
			System.out.println(chargeableEquipment.getEventCode());
			System.out.println(chargeableEquipment.getEventDate());
			
		} else {
			if (chargeableEvent != null) {
				System.out.println("Values of Chargeable Event");
				System.out.println(chargeableEvent.getEventCode());
				System.out.println(chargeableEvent.getEventDate());
			}
		}
		
	}

}
