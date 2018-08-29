package com.myjavapapers.authentication.abstractclasses;

public class AbstractSubClassTwo extends AbstractClassOne {

	@Override
	public String getImplementedClassName() {
		return "This is AbstractSubClassTwo";
	}

	public String displayClass(){
		return "AbstractSubClassTwo.displayClass()";
	}
}
