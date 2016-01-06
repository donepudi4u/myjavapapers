package com.myjavapaers.demo.abstractclasses;

public class MainAbstractClass {
	public static void main(String[] args) {
		AbstractClassOne abstractClassOne = new  SubClassOfAbstractSubClassTwo();
		System.out.println(abstractClassOne.getConcreteMethodName());
		System.out.println(abstractClassOne.getImplementedClassName());
		
		SubClassOfAbstractSubClassTwo subClassOfAbstractSubClassTwo = new SubClassOfAbstractSubClassTwo();
		System.out.println(subClassOfAbstractSubClassTwo.getImplementedClassName());
		System.out.println(subClassOfAbstractSubClassTwo.getConcreteMethodName());
		System.out.println(subClassOfAbstractSubClassTwo.diplayClass());
		System.out.println(subClassOfAbstractSubClassTwo.displayClass());
		
	}

}
