package com.myjavapapers.beans;

public class EmployeeBean {

	// public static String ADDRESS = "ABC Company , Dr.No: 32-529";
	private String name;
	private int empno;
	private int sal;
	private String job;

	
	
	public EmployeeBean() {
		super();
	}

	public EmployeeBean(String name, String designation) {
		super();
		this.name = name;
		// EmployeeBean.ADDRESS = "This is new address updated by constructor";
	}

	public int getEmpno() {
		return empno;
	}

	public void setEmpno(int empno) {
		this.empno = empno;
	}

	public int getSal() {
		return sal;
	}

	public void setSal(int sal) {
		this.sal = sal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	@Override
	public String toString() {
		return "EmployeeBean [name=" + name + ", empno=" + empno + ", sal="
				+ sal + ", job=" + job + "]";
	}
	

}
