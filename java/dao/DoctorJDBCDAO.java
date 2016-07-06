package dao;

import beans.AdmissionBean;
import beans.DoctorBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.DBConnector;

public class DoctorJDBCDAO implements DoctorDAO{

    @Override
    public List<DoctorBean> getAllDoctors() {
        Connection connection;
        PreparedStatement statement;
        ResultSet qResult;
        List<DoctorBean> doctorList = new ArrayList<>();

        try {
            connection = DBConnector.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Doctor");
            qResult = statement.executeQuery();

            while (qResult.next()) {
                DoctorBean doctor = new DoctorBean();                
                doctor.setID(qResult.getString("DoctorId"));
                doctor.setName(qResult.getString("Name"));
                doctorList.add(doctor);
            }

            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return doctorList;
    }

    @Override
    public DoctorBean queryDoctor(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}