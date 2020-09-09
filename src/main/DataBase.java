package main;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataBase {
    private final String URL = "jdbc:derby:TheWorkoutCalendarDataBase;create=true";
    Connection conn = null;
    Statement createStatement = null;
    DatabaseMetaData dbmd = null;
    public DataBase() {
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException ex) {
            System.out.println(ex);
          }
        if (conn != null) {
            try {
                createStatement = conn.createStatement();
            } catch (SQLException ex) {
                System.out.println(ex);
              }
        }
        try {
            dbmd = conn.getMetaData();
        } catch (SQLException ex) {
            System.out.println(ex);
          }
        try {
            ResultSet rs = dbmd.getTables(null, "APP", "WORKOUTS", null);
            if (!rs.next()) {
                createStatement.execute("CREATE TABLE WORKOUTS (ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), NAME VARCHAR(50), LENGTH INT)");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
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
            System.out.println(ex);
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
            System.out.println(ex);
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
            System.out.println(ex);
          }
    }
    public void cruD(String name) {
        String sql = "DELETE FROM WORKOUTS WHERE NAME = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
          }
    }
}