/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import util.DBConnector;

/**
 *
 * @author applegrocer
 */
@ManagedBean
public class DoctorBean {

    private String ID;
    private String name;

    public CachedRowSet select_doctors() {
        Connection connection;
        PreparedStatement statement;
        CachedRowSet rowSet = null;

        try {
            connection = DBConnector.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Doctor");
            rowSet = RowSetProvider.newFactory().createCachedRowSet();
            rowSet.populate(statement.executeQuery());
            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return rowSet;
    }

    public String insert_doctor() {
        Connection connection;
        PreparedStatement statement;
        int doctorsInserted = 0;
        try {
            connection = DBConnector.getConnection();
            statement = connection.
                    prepareStatement("INSERT INTO "
                            + "Doctor (DoctorId, Name) "
                            + "VALUES (?, ?)");
            statement.setString(1, getID());
            statement.setString(2, getName());
            doctorsInserted = statement.executeUpdate();
            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        if(doctorsInserted > 0){
            return "doctor_insert_success.xhtml?faces-redirect=true";
        }else{
            return "doctor_insert_fail.xhtml?faces-redirect=true";
        }
    }

    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
