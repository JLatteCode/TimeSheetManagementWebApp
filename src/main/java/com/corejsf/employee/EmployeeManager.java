package com.corejsf.employee;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.validation.constraints.Size;

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
	 * Database source.
	 */
	@Resource (mappedName = "java:/jboss/datasources/workplace")
	private DataSource dataSource;

	/**
	 *  Four fields for to set value of Employee variable.
	 */
	@Size(min=1, message="{com.corejsf.Length}")
	private String name;
	private int empNumber;
	private String userName;
	private String password;


	/** Declare regular employee object.*/
	Employee employee;
	
//	/** Declare employee object that is admin and put it into Array. */
//	Employee admin = new Employee("admin", 1, "admin", "admin");
//	private ArrayList<Employee> employees = new ArrayList<>(Arrays.asList(admin));
	private ArrayList<Employee> employees = new ArrayList<>();


	@Inject
	private Credentials credential;
	/** default constructor for EmployeeManager class */
	public EmployeeManager(){ }
    /** returns List that contains Employee objects */
	public List<Employee> getEmployees() {
		return employees;
		}

	/** Create and Initialize employee object and add it into List. */
	@PostConstruct
	public void onCreate() {

		Connection connection = null;
        PreparedStatement stmt = null;

        try {
        	
            try {
            	 connection = dataSource.getConnection();
            	
                try {
                	stmt = connection.prepareStatement("SELECT * FROM Users ORDER BY empNo");
                    ResultSet result = stmt.executeQuery( );
                    while (result.next()) {
                    	
                    	employees.add(new Employee(
                    			result.getString("empName"),
                    			result.getInt("empNo"),
                    			result.getString("empID"),
                    			result.getString("empPW")			
                    			));	                    
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in finding User Database");
            ex.printStackTrace();
        }
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

		
		
		return null;
	}
	
	 /** return the current user is admin */
		public boolean isAdmin() {

			if(getEmployee().getUserName().equals("admin")) {
				System.out.println("name from isadmin: " + getEmployee().getUserName());
				System.out.println("is admin");
				return true;
			}
			System.out.println("name from isadmin: " + getEmployee().getUserName());
			System.out.println("not admin");
			return false;
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
		
		if(isAdmin() == true) {
			
		
		
		 Connection connection = null;
	        PreparedStatement stmt = null;
	        try {
	            try {
	                connection = dataSource.getConnection();
	                try {
	                    stmt = connection.prepareStatement(
	                            "DELETE FROM Users WHERE empNo =  ?");
	                    stmt.setInt(1, empNo);
	                    stmt.executeUpdate();
	                } finally {
	                    if (stmt != null) {
	                        stmt.close();
	                    }
	                }
	            } finally {
	                if (connection != null) {
	                    connection.close();
	                }
	            }
	        } catch (SQLException ex) {
	            System.out.println("Error in remove empNo: " + empNo);
	            ex.printStackTrace();
	        }

 /** Delete locally .*/
		Iterator<Employee> iter = employees.iterator();

		while (iter.hasNext()) {
			Employee e = iter.next();

			if (e.getEmpNumber() == empNo) {
				iter.remove();
			}
		}
	}
	}


   /** add employee into Employee type List. */
	public void addEmployee() {
		
		boolean userExist = false;
		
		Iterator<Employee> iter = employees.iterator();
		
		while (iter.hasNext()) {
			Employee e = iter.next();

			if (e.getEmpNumber() == getEmpNumber()) {
				
				userExist = true;
			} 
			
			
		}
		
		if(isAdmin() == true && userExist == false) {
		
		
		employees.add(new Employee(getName(),getEmpNumber(),getUserName(),getPassword()));
				
		 Connection connection = null;
	        PreparedStatement stmt = null;
	        try {
	            try {
	                connection = dataSource.getConnection();
	                try {
	                    stmt = connection.prepareStatement(
	                            "INSERT INTO Users "
	                         + "VALUES (?, ?, ?, ?)");	                  
	                    stmt.setString(1, getName());
	                    stmt.setInt(2, getEmpNumber());
	                    stmt.setString(3, getUserName());
	                    stmt.setString(4, getPassword());	                   
	                    stmt.executeUpdate();
	                } finally {
	                    if (stmt != null) {
	                        stmt.close();
	                    }
	                }
	            } finally {
	                if (connection != null) {
	                    connection.close();
	                }
	            }
	        } catch (SQLException ex) {
	            System.out.println("Error in persist Employee, EmpNo: " + getEmpNumber());
	            ex.printStackTrace();
	        }
	}
	}
	
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

		if(isAdmin() == true) {
		
		Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "UPDATE Users SET empName = ?, empID = ?, empPW = ? WHERE empNo =  ?");
                    stmt.setString(1, getName());
        			stmt.setString(2, getPassword());
        			stmt.setString(3, getUserName());
                    stmt.setInt(4, empNo);
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in editing empNo: " + empNo);
            ex.printStackTrace();
        }
		
		
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
