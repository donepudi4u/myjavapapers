package com.myjavapapers.spring.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.myjavapapers.spring.service.EmployeeService;

public class SpringMain {
	public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-aop.xml");
        EmployeeService employeeService = ctx.getBean("employeeService", EmployeeService.class);
         
        System.out.println(employeeService.getEmployee().getName());
         
        employeeService.getEmployee().setName("Dileep");
         
        employeeService.getEmployee().throwException();
         
        ctx.close();
    }
}
