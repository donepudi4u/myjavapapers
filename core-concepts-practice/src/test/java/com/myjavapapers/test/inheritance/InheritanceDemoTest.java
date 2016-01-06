package com.myjavapapers.test.inheritance;

import java.util.Date;

import com.myjavapapers.inheritance.ChargeableEquipment;
import com.myjavapapers.inheritance.ChargeableEvent;
import com.myjavapapers.inheritance.OperationsClass;

import junit.framework.TestCase;

public class InheritanceDemoTest extends TestCase {

	
	public void testInheritance(){
		OperationsClass operationsClass = new OperationsClass();
		/*ChargeableEvent chargeableEvent = new ChargeableEvent();
		chargeableEvent.setEventCode("Event Code");
		chargeableEvent.setEventDate(new Date());
		operationsClass.processCharge(chargeableEvent);*/
		ChargeableEquipment chargeableEquipment = new ChargeableEquipment();
		chargeableEquipment.setCustomer("Customer");
		chargeableEquipment.setEquipmentId("TILX 12345");
		chargeableEquipment.setEventCode("Event Code");
		chargeableEquipment.setEventDate(new Date());
		operationsClass.processCharge(chargeableEquipment);
		
	}

}
