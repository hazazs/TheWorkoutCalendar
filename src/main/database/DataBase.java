package main.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import static main.MainController.log;

public class DataBase {
    private final String URL = "jdbc:derby:TheWorkoutCalendarDataBase;create=true";
    Connection conn = null;
    Statement createStatement = null;
    DatabaseMetaData dbmd = null;
    public DataBase() {
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException ex) {
            log.error("Something went wrong while trying to create the connection with the database: " + ex);
          }
        if (conn != null) {
            try {
                createStatement = conn.createStatement();
            } catch (SQLException ex) {
                log.error("Something went wrong while trying to create the statement: " + ex);
              }
        }
        try {
            dbmd = conn.getMetaData();
        } catch (SQLException ex) {
            log.error("Something went wrong while trying to get the metadata from the database: " + ex);
          }
        try {
            ResultSet rs = dbmd.getTables(null, "APP", "WORKOUTS", null);
            if (!rs.next()) {
                createStatement.execute("CREATE TABLE WORKOUTS (ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), NAME VARCHAR(50), LENGTH INT)");
            }
        } catch (SQLException ex) {
            log.error("Something went wrong while trying to create data table: " + ex);
          }
    }
    public void Crud(String name, int length) {
        String sql = "INSERT INTO WORKOUTS (NAME, LENGTH) VALUES (?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, length);
            pstmt.execute();
        } catch (SQLException ex) {
            log.error("Something went wrong while trying to create the record: " + ex);
          }
    }
    public Map<String, Integer> cRud() {
        Map<String, Integer> workouts = null;
        String sql = "SELECT * FROM WORKOUTS ORDER BY NAME";
        try {
            workouts = new LinkedHashMap<>();
            ResultSet rs = createStatement.executeQuery(sql);
            while (rs.next())
                workouts.put(rs.getString("NAME"), rs.getInt("LENGTH"));
        } catch (SQLException ex) {
            log.error("Something went wrong while trying to read from the database: " + ex);
          }
        return workouts;
    }
    public void crUd(String oldName, String newName, int length) {
        String sql = "UPDATE WORKOUTS SET NAME = ?, LENGTH = ? WHERE NAME = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newName);
            pstmt.setInt(2, length);
            pstmt.setString(3, oldName);
            pstmt.execute();
        } catch (SQLException ex) {
            log.error("Something went wrong while trying to update the record: " + ex);
          }
    }
    public void cruD(String name) {
        String sql = "DELETE FROM WORKOUTS WHERE NAME = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.execute();
        } catch (SQLException ex) {
            log.error("Something went wrong while trying to delete the record: " + ex);
          }
    }
}