package com.corejsf.employee;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Qualifier;
import javax.persistence.Entity;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped; 

@Named("emp")
@RequestScoped

/**
 * A class representing a single Employee.
 *
 * @author Bruce Link
 * @version 1.1
 */
public class Employee implements Serializable {

    private static final long serialVersionUID = 11L;
    
    /** The employee's name. */
    private String name;
    
    /** The employee's employee number. */
    private int empNumber;
    
    /** The employee's login ID. */
    private String userName;
    
    private String password;


    /**
     * The no-argument constructor. Used to create new employees from within the
     * application.
     */
    public Employee() {
    }
   
    
    
    /**
     * The argument-containing constructor. Used to create the initial employees
     * who have access as well as the administrator.
     *
     * @param empName the name of the employee.
     * @param number the empNumber of the user.
     * @param id the loginID of the user.
     */
    public Employee(final String empName, final int number, final String id, final String pw) {
 
        name = empName;
        empNumber = number;
        userName = id;
        password = pw;
    }

    /**
     * name getter.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * name setter.
     * @param empName the name to set
     */
    public void setName(final String empName) {
        name = empName;
    }

    /**
     * empNumber getter.
     * @return the empNumber
     */
    public int getEmpNumber() {
        return empNumber;
    }

    /**
     * empNumber setter.
     * @param number the empNumber to set
     */
    public void setEmpNumber(final int number) {
        empNumber = number;
    }

    /**
     * userName getter.
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * userName setter.
     * @param id the userName to set
     */
    public void setUserName(final String id) {
        userName = id;
    }

    /** getter and setter for password. */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	@Override
    public String toString() {
        return name + '\t' + empNumber + '\t' + userName;
    }


}
