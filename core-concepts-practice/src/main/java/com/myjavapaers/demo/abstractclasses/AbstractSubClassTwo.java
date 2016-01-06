package com.myjavapaers.demo.abstractclasses;

public class AbstractSubClassTwo extends AbstractClassOne {

	@Override
	public String getImplementedClassName() {
		return "This is AbstractSubClassTwo";
	}

	public String displayClass(){
		return "AbstractSubClassTwo.displayClass()";
	}
}
