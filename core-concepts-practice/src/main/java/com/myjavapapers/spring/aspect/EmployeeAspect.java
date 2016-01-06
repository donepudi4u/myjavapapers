package com.myjavapapers.spring.aspect;

import org.aspectj.lang.annotation.Before;

public class EmployeeAspect {
	@Before("execution(public String getName())")
    public void getNameAdvice(){
        System.out.println("Executing Advice on getName()");
    }
     
    @Before("execution(* com.myjavapapers.spring.service.*.get*())")
    public void getAllAdvice(){
        System.out.println("Service method getter called");
    }
}
