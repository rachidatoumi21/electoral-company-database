/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JavaSQL;
import java.sql.*;

public class DbConnection {
    public static DbConnection instance;
    public Connection connection;
    
    String nomBd= "CampagneElectorale";
    String instanceServeur= "localhost:1433"; 
    
    String url = "jdbc:sqlserver://" + instanceServeur + ";" + "databaseName="+nomBd+";"  
            + "encrypt=true;" 
            + "trustServerCertificate=true;" 
            + "integratedSecurity=true;" ;
            


    private DbConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connection = DriverManager.getConnection(url, null, null);
        } catch (ClassNotFoundException ex) {
            System.out.println("SQL Server JDBC Driver non trouve: " + ex.getMessage());
        }
    }
    
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        Connection connection = this.getConnection(); 
        return connection.prepareStatement(sql);
    }
    

    public Connection getConnection() {
        return connection;
    }


    public Boolean closeConnection() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
        return true;
    }
    
    //cette m�thode statique garantit l'existence d'une seule instance de la classe DbConnection dans le programme
    //le patron singleton
    public static DbConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DbConnection();
        }
        return instance;
    }

}
