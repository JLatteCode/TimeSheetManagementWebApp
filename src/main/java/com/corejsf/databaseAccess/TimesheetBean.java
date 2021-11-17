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
       
       /** gets all the timesheet rows for the current employee from the database.
        * @param e current employee
        * @return list of timesheets.
        * @throws SQLException
        */
       public ArrayList<Timesheet> getAll(Employee e) throws SQLException {
          
          Connection conn = ds.getConnection();
          
          try {
              Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
              ResultSet result = stmt.executeQuery("SELECT * FROM Timesheet WHERE empNo = " + e.getEmpNumber());
              
              
              ArrayList<Timesheet> t = new ArrayList<>();
              while (result.next()) {
                  Timesheet tSheet = new Timesheet();
                  
                  int projectNum = result.getInt(2);
                  String wp = result.getString(3);
                  
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
                  
                  while (result.next() && (result.getInt(13) == tSheet.getWeekNumber())){
                      int projectNumNext = result.getInt(2);
                      String wpNext = result.getString(3);
                      
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
                      
                  }
                  result.previous();
                  t.add(tSheet);
                  
                  
                  
              }
              return t;
          } finally {
              conn.close();
          }
          
       }
       
       /** gets current timesheet for the current employee.
        * @param e current employee
        * @param t current timesheet
        * @return list of timesheet rows
        * @throws SQLException
        */
       public ArrayList<TimesheetRow> getCurrentTimesheet(Employee e, Timesheet t) throws SQLException {
           
           Connection conn = ds.getConnection();
           
           try {
               Statement stmt = conn.createStatement();
               ResultSet result = stmt.executeQuery("SELECT * FROM Timesheet WHERE empNo = " + e.getEmpNumber() + " AND weekNumber = " + t.getWeekNumber());
               
               ArrayList<TimesheetRow> timesheet = new ArrayList<>();
               while (result.next()) {
                   
                   int projectNum = result.getInt(2);
                   String wp = result.getString(3);
                   
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
       
       /** adds the timesheet row to the database.
        * @param t timesheet row to add to database
        * @param e employee who timesheet row it is.
        * @param weekNumber weeknumber of the timesheet row.
        * @return success if added successfully, else fail
        * @throws SQLException
        */
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
       
       /** Deletes the timesheet from the database.
        * @param t timesheet to delete
        * @param empNumber employee who owns timesheet
        * @return success if delete successful, else fail
        * @throws SQLException
        */
       public String removeTimesheet(Timesheet t, int empNumber) throws SQLException {
           Connection conn = ds.getConnection();
           
           try {
                String sql = "DELETE FROM Timesheet WHERE empNo = " + empNumber + " AND weekNumber = " + t.getWeekNumber();
                
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
                return "deleteSuccess";
            }catch (Exception ex) {
                ex.printStackTrace();
                return "deleteFail";
            } finally {
                conn.close();
            }
       }
       
       

}
