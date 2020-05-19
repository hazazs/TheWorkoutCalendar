package insanity;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataBase {
    private final String URL = "jdbc:derby:InsanityDataBase;create=true";
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
                createStatement.execute("CREATE TABLE Workouts (ID INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), Name varchar(50), Length INT)");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
          }
    }
    public String getLength(String fName) {
        String lengthAsString = "";
        String sql = "SELECT * FROM Workouts WHERE Name = '" + fName + "'";
        try {
            ResultSet rs = createStatement.executeQuery(sql);
            lengthAsString = rs.next() ? String.valueOf(rs.getInt("Length")) : lengthAsString;
        } catch (SQLException ex) {
            System.out.println(ex);
          }
        return lengthAsString;
    }
    public void Crud(String fName, int fLength) {
        String sql = "INSERT INTO Workouts (Name, Length) VALUES (?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fName);
            pstmt.setInt(2, fLength);
            pstmt.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
          }
    }
    public ArrayList<String> cRud() {
        ArrayList<String> workouts = null;
        String sql = "SELECT * FROM Workouts ORDER BY ID";
        try {
            workouts = new ArrayList<>();
            ResultSet rs = createStatement.executeQuery(sql);
            while (rs.next())
                workouts.add(rs.getString("Name"));
        } catch (SQLException ex) {
            System.out.println(ex);
          }
        return workouts;
    }
    public void crUd(String oName, String nName, int fLength) {
        String sql = "UPDATE Workouts SET Name = ?, Length = ? WHERE Name = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nName);
            pstmt.setInt(2, fLength);
            pstmt.setString(3, oName);
            pstmt.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
          }
    }
    public void cruD(String fName) {
        String sql = "DELETE FROM Workouts WHERE Name = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fName);
            pstmt.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
          }
    }
}