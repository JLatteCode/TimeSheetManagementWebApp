package com.corejsf.databaseAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.inject.Named; 
import javax.enterprise.context.SessionScoped; 
import javax.sql.DataSource;

import com.corejsf.employee.Employee;
import com.corejsf.timesheet.TimesheetRow;
import com.mysql.cj.jdbc.result.ResultSetMetaData;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import java.io.Serializable;

@Named
@SessionScoped
public class TimesheetBean implements Serializable {
    

    
       /**
         * 
         */
        private static final long serialVersionUID = 1L;
        
       @Resource(mappedName="java:jboss/datasources/timesheet")
       private DataSource ds;
       
       public ArrayList<TimesheetRow> getAll(Employee e) throws SQLException {
          
          Connection conn = ds.getConnection();
          
          try {
              Statement stmt = conn.createStatement();
              ResultSet result = stmt.executeQuery("SELECT * FROM TimesheetData WHERE empNum = " + e.getEmpNumber());
              java.sql.ResultSetMetaData rsmd = result.getMetaData();
              int columnsNumber = rsmd.getColumnCount();
              ArrayList<TimesheetRow> t = new ArrayList<>();
              while (result.next()) {
                  
                  int projectNum = result.getInt(1);
                  String wp = result.getString(2);
                  int totalWeekHours = result.getInt(3);
                  float satHours = result.getFloat(4);
                  float sunHours = result.getFloat(5);
                  int monHours = result.getInt(6);
                  int tueHours = result.getInt(7);
                  int wedHours = result.getInt(8);
                  int thuHours = result.getInt(9);
                  int friHours = result.getInt(10);
                  String notes = result.getString(11);
                  TimesheetRow r = new TimesheetRow(projectNum, wp, notes, satHours, sunHours, monHours, tueHours, wedHours, thuHours, friHours);
                  t.add(r);

              }
              return t;
          } finally {
              conn.close();
          }
          
       }
       
//       public Result searchForSupplier(String supplierName) throws SQLException {
//           Connection conn = ds.getConnection();
//           try {
//               String sql = "SELECT * FROM Suppliers WHERE SupplierName like ?;";
//               PreparedStatement stmt = conn.prepareStatement(sql);
//               stmt.setString(1, supplierName + "%");
//               ResultSet result = stmt.executeQuery();
//               return ResultSupport.toResult(result);
//           } finally {
//               conn.close();
//           }
//       }
//       
//       public String addSupplier(Supplier s) throws SQLException {
//           Connection conn = ds.getConnection();
//           
//           try {
//            
//               String sql = "insert into Suppliers(SupplierID, SupplierName, ContactName, ContactTitle, Address, City, PostalCode, StateOrProvince, Country, PhoneNumber, FaxNumber, PaymentTerms, EmailAddress, Notes) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//               
//               PreparedStatement statement = conn.prepareStatement(sql);
//               
//               statement.setLong(1, s.getSupplierID());
//               statement.setString(2, s.getSupplierName());
//               statement.setString(3, s.getContactName());
//               statement.setString(4, s.getContactTitle());
//               statement.setString(5, s.getAddress());
//               statement.setString(6, s.getCity());
//               statement.setString(7, s.getPostalCode());
//               statement.setString(8, s.getStateOrProvince());
//               statement.setString(9, s.getCountry());
//               statement.setString(10, s.getPhoneNumber());
//               statement.setString(11, s.getFaxNumber());
//               statement.setString(12, s.getPaymentTerms());
//               statement.setString(13, s.getEmail());
//               statement.setString(14, s.getNotes());
//               
//               statement.executeUpdate();
//               return "success";
//           }catch (Exception ex) {
//               ex.printStackTrace();
//               return "fail";
//           } finally {
//               conn.close();
//           }
//       }
//       
//       public String deleteSupplier(Supplier s) throws SQLException {
//           Connection conn = ds.getConnection();
//           
//           try {
//               String sql = "DELETE FROM Suppliers WHERE SupplierID LIKE ?;";
//               PreparedStatement stmt = conn.prepareStatement(sql);
//               int searchName = s.getSupplierID();
//               stmt.setLong(1, s.getSupplierID());
//               stmt.executeQuery();
//               return "successDelete";
//           } catch (Exception ex) {
//               ex.printStackTrace();
//               return "fail";
//           } finally {
//               conn.close();
//           }
//       }
         
       public String addTimesheetRow(TimesheetRow t, Employee e) throws SQLException {
           Connection conn = ds.getConnection();
         
           try {
             
                String sql = "INSERT INTO TimesheetData(projectNum, WP, totalWeekHours, satHours, sunHours, monHours, tuesHours, wedHours, thurHours, friHours, notes, empNum) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
                PreparedStatement statement = conn.prepareStatement(sql);
                
                statement.setLong(1, t.getProjectId());
                statement.setString(2, t.getWorkPackageId());
                statement.setFloat(3, t.getTotalWeekHours());
                statement.setFloat(4, t.getHoursPerDay()[0]);
                statement.setFloat(5, t.getHoursPerDay()[1]);
                statement.setFloat(6, t.getHoursPerDay()[2]);
                statement.setFloat(7, t.getHoursPerDay()[3]);
                statement.setFloat(8, t.getHoursPerDay()[4]);
                statement.setFloat(9, t.getHoursPerDay()[5]);
                statement.setFloat(10, t.getHoursPerDay()[6]);
                statement.setString(11, t.getNotes());
                statement.setFloat(12, 1);
                
                statement.executeUpdate();
                return "addSuccess";
            }catch (Exception ex) {
                ex.printStackTrace();
                return "addFail";
            } finally {
                conn.close();
            }
       }

}
