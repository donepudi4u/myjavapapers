package com.myjavapapers.innerclass;

public class InnerClassDemo {
	
	public InnerClassDemo(){
		
	}
	public static void printString(){
		System.out.println("THis is out class method");
	}

	public static class NestedParentClass {
		int age = 30;
		String name = "Dileep";
	}
	
	public static class NestedStaticClass extends NestedParentClass {
		
		public void innerClassMethod(){
			printString();
			System.out.println(sayHello("Dileep"));
			System.out.println("Name : "+name);
			System.out.println("Age"+ age);
		}
		
		private String sayHello(String name){
			return "Hello :"+name;
		}
		
	}
	
	public static void main(String[] args) {
		NestedStaticClass nestedStaticClass = new NestedStaticClass();
		nestedStaticClass.innerClassMethod();
	}
}
