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
import com.corejsf.timesheet.Timesheet;
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
        
       @Resource(mappedName="java:jboss/datasources/workplace")
       private DataSource ds;
       
       public ArrayList<Timesheet> getAll(Employee e) throws SQLException {
          
          Connection conn = ds.getConnection();
          
          try {
              Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
              ResultSet result = stmt.executeQuery("SELECT * FROM Timesheet WHERE empNo = " + e.getEmpNumber());
              java.sql.ResultSetMetaData rsmd = result.getMetaData();
              int columnsNumber = rsmd.getColumnCount();
              ArrayList<Timesheet> t = new ArrayList<>();
              while (result.next()) {
                  Timesheet tSheet = new Timesheet();
                  int currentWeek = result.getInt(13);
                  int projectNum = result.getInt(2);
                  String wp = result.getString(3);
                  int totalWeekHours = result.getInt(4);
                  float satHours = result.getFloat(5);
                  float sunHours = result.getFloat(6);
                  int monHours = result.getInt(7);
                  int tueHours = result.getInt(8);
                  int wedHours = result.getInt(9);
                  int thuHours = result.getInt(10);
                  int friHours = result.getInt(11);
                  String notes = result.getString(12);
                  int weekNumber = result.getInt(13);
                  TimesheetRow r = new TimesheetRow(projectNum, wp, notes, satHours, sunHours, monHours, tueHours, wedHours, thuHours, friHours);
                  r.setWeekNumber(weekNumber);
                  tSheet.setWeekNumber(weekNumber, tSheet.getEndDate().getYear());
                  tSheet.getDetails().add(r);
                  System.out.println("week number hi 1:" + r.getNotes());
                  while (result.next() && (result.getInt(13) == tSheet.getWeekNumber())){
                      int projectNumNext = result.getInt(2);
                      String wpNext = result.getString(3);
                      int totalWeekHoursNext = result.getInt(4);
                      float satHoursNext = result.getFloat(5);
                      float sunHoursNext = result.getFloat(6);
                      int monHoursNext = result.getInt(7);
                      int tueHoursNext = result.getInt(8);
                      int wedHoursNext = result.getInt(9);
                      int thuHoursNext = result.getInt(10);
                      int friHoursNext = result.getInt(11);
                      String notesNext = result.getString(12);
                      int weekNumberNext = result.getInt(13);
                      TimesheetRow rNext = new TimesheetRow(projectNumNext, wpNext, notesNext, satHoursNext, sunHoursNext, monHoursNext, tueHoursNext, wedHoursNext, thuHoursNext, friHoursNext);
                      rNext.setWeekNumber(weekNumberNext);
                      tSheet.getDetails().add(rNext);
                      System.out.println("week number hi 1:" + rNext.getNotes());
                  }
                  result.previous();
                  t.add(tSheet);
                  
                  
                  
              }
              return t;
          } finally {
              conn.close();
          }
          
       }
       
       public ArrayList<TimesheetRow> getCurrentTimesheet(Employee e, Timesheet t) throws SQLException {
           
           Connection conn = ds.getConnection();
           
           try {
               Statement stmt = conn.createStatement();
               ResultSet result = stmt.executeQuery("SELECT * FROM Timesheet WHERE empNo = " + e.getEmpNumber() + " AND weekNumber = " + t.getWeekNumber());
               java.sql.ResultSetMetaData rsmd = result.getMetaData();
               int columnsNumber = rsmd.getColumnCount();
               ArrayList<TimesheetRow> timesheet = new ArrayList<>();
               while (result.next()) {
                   
                   int projectNum = result.getInt(2);
                   String wp = result.getString(3);
                   int totalWeekHours = result.getInt(4);
                   float satHours = result.getFloat(5);
                   float sunHours = result.getFloat(6);
                   int monHours = result.getInt(7);
                   int tueHours = result.getInt(8);
                   int wedHours = result.getInt(9);
                   int thuHours = result.getInt(10);
                   int friHours = result.getInt(11);
                   String notes = result.getString(12);
                   int weekNumber = result.getInt(13);
                   TimesheetRow r = new TimesheetRow(projectNum, wp, notes, satHours, sunHours, monHours, tueHours, wedHours, thuHours, friHours);
                   r.setWeekNumber(weekNumber);
                   timesheet.add(r);
               }
               return timesheet;
           } finally {
               conn.close();
           }
           
        }
       
         
       public String addTimesheetRow(TimesheetRow t, Employee e, int weekNumber) throws SQLException {
           Connection conn = ds.getConnection();
           
           try {
             
                String sql = "INSERT INTO Timesheet(empNo, projectNo, WP, totalWeekHours, satHours, sunHours, monHours, tuesHours, wedHours, thurHours, friHours, notes, weekNumber) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
                
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setFloat(1, e.getEmpNumber());
                statement.setLong(2, t.getProjectId());
                statement.setString(3, t.getWorkPackageId());
                statement.setFloat(4, t.getTotalWeekHours());
                statement.setFloat(5, t.getHoursPerDay()[0]);
                statement.setFloat(6, t.getHoursPerDay()[1]);
                statement.setFloat(7, t.getHoursPerDay()[2]);
                statement.setFloat(8, t.getHoursPerDay()[3]);
                statement.setFloat(9, t.getHoursPerDay()[4]);
                statement.setFloat(10, t.getHoursPerDay()[5]);
                statement.setFloat(11, t.getHoursPerDay()[6]);
                statement.setString(12, t.getNotes());
                statement.setFloat(13, weekNumber);
                
                statement.executeUpdate();
                return "addSuccess";
            }catch (Exception ex) {
                ex.printStackTrace();
                return "addFail";
            } finally {
                conn.close();
            }
       }
       
       public String removeTimesheet(Timesheet t) throws SQLException {
           Connection conn = ds.getConnection();
           
           try {
             
                String sql = "DELETE FROM Timesheet WHERE empNo = '1' AND weekNumber = " + t.getWeekNumber();
                
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
                return "deleteSuccess";
            }catch (Exception ex) {
                ex.printStackTrace();
                return "addFail";
            } finally {
                conn.close();
            }
       }
       
       

}
