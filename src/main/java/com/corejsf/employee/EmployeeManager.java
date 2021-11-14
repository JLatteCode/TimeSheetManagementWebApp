package com.corejsf.employee;

import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 * EmployeeManager class contain some methods to handle
 * user login and edit users.
 * @author Thi Hong Phuc Le (Katherine) & Jung Jae Lee
 * @version 1.0
 */
@Named("empManager")
@SessionScoped
public class EmployeeManager implements Serializable {

	private static final long serialVersionUID = 11L;

	/**
	 *  Four fields for to set value of Employee variable.
	 */
	private String name;
	private int empNumber;
	private String userName;
	private String password;


	/** Declare regular employee object.*/
	Employee employee;
	/** Declare employee object that is admin and put it into Array. */
	Employee admin = new Employee("admin", 1, "admin", "admin");
	private ArrayList<Employee> employees = new ArrayList<>(Arrays.asList(admin));



	@Inject
	private Credentials credential;
	/** default constructor for EmployeeManager class */
	public EmployeeManager(){ }
    /** returns List that contains Employee objects */
	public List<Employee> getEmployees() {return employees;}

	/** Create and Initialize employee object and add it into List. */
	public void init() {
employees.add(new Employee(getName(),getEmpNumber(),getUserName(),getPassword()));
	}

		/**  Return Employee List. */
	public Employee getEmployee(int empNo) {

		Iterator<Employee> iter = employees.iterator();
		/** Iterator that loops through into Employee List. */
		while (iter.hasNext()) {
			Employee e = iter.next();

			if (e.getEmpNumber() == empNo) {
				return e;
			}
		} return null; }

      /** Returns current Employee. Current employee is already
	   *  set automatically when users login. */
	public Employee getCurrentEmployee() {

		 
		return employee;
	}

    /** return admin */
	public Employee getAdministrator() {

		return admin;
	}

 /**
  * @param credential receives credential object by injection
  * Verify user by comparing credential's
  * ID and Password with that of users from List. */
	public boolean verifyUser(Credentials credential) {
		Iterator<Employee> iter = employees.iterator();

		while (iter.hasNext()) {
			Employee e = iter.next();

				if(e.getUserName().equals(credential.getUserName()) &&
					e.getPassword().equals(credential.getPassword())) {
					employee = e;
					return true;
				}
		} 
		return false;
	}

	/**
	 * @login login returns this if user is not verified.
	 * @return homePage returns this if user is verified.
	 */
	public String login(){
		boolean verified = verifyUser(credential);
		System.out.println(credential);
		System.out.println(verified);
		System.out.println("login(), current user: " + employee);

		if(verified){
			return "homePage";
		} else {
			return "login";
		}



	}

	 /** Logout a user. */
	public void logout() {
		((HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext()
		         .getSession(false)).invalidate();
		

	}

   /**
	* deletes employee
	@param empNo Receives empNo to delete that user */
	public void deleteEmployee(int empNo) {

		Iterator<Employee> iter = employees.iterator();

		while (iter.hasNext()) {
			Employee e = iter.next();

			if (e.getEmpNumber() == empNo) {
				iter.remove();
			}
		}
	}


   /** add employee into Employee type List. */
	public void addEmployee() {

		init();
		Iterator<Employee> iter = employees.iterator();
		while (iter.hasNext()) {
			Employee e = iter.next();

			System.out.println(e);
	}}

	  /** getter and setter for credential injectin object .*/
	public Credentials getCredential() {
		return credential;
	}
	public void setCredential(Credentials newCredential) {
		credential = newCredential;
	}

	/** getter and setter for employee .*/
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee newEmployee) {employee = newEmployee;}

	/** @param empNo receives employee's number
	 *  to decide which user to edit.   */
	public void editEmployee(int empNo) {

		Iterator<Employee> iter = employees.iterator();

		while (iter.hasNext()) {
			Employee e = iter.next();

			if (e.getEmpNumber() == empNo) {

				e.setName(getName());
				e.setPassword(getPassword());
				e.setUserName(getUserName());

			}
		}

	}

	/**
	 * getter and setters for Employee's name field
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * getter and setters for Employee's number field
	 */

	public int getEmpNumber() {
		return empNumber;
	}
	public void setEmpNumber(int empNumber) {
		this.empNumber = empNumber;
	}

	/**
	 * getter and setters for Employee's userName field
	 */
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * getter and setters for Employee's password field
	 */
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
