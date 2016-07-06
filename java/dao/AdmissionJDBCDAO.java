package dao;

import beans.AdmissionBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.DBConnector;

public class AdmissionJDBCDAO implements AdmissionDAO {

    @Override
    public List<AdmissionBean> queryAllAdmissions() {
        Connection connection;
        PreparedStatement statement;
        ResultSet qResult;
        List<AdmissionBean> admissionList = new ArrayList<>();

        try {
            connection = DBConnector.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Admission");
            qResult = statement.executeQuery();

            while (qResult.next()) {
                AdmissionBean admission = new AdmissionBean();

                //deal with null discharge values
                java.sql.Date dod = qResult.getDate("DischargeDate");
                if (qResult.wasNull()) {
                    admission.setDateOfDischarge(null);
                } else {
                    admission.setDateOfDischarge(new Date(dod.getTime()));
                }

                Date doa = new java.sql.Date(qResult.getDate("AdmissionDate")
                        .getTime());

                admission.setPid(qResult.getInt("PatientId"));
                admission.setDateOfAdmission(doa);
                admission.setRoomNo(qResult.getString("RoomNo"));
                admission.setDocID(qResult.getString("DoctorId"));
                admissionList.add(admission);
            }

            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return admissionList;
    }

    @Override
    public AdmissionBean queryCurrentAdmission(int id) {
        Connection connection;
        PreparedStatement statement;
        AdmissionBean admission = new AdmissionBean();
        ResultSet qResult;

        try {
            connection = DBConnector.getConnection();
            statement = connection.
                    prepareStatement("SELECT Patient.FirstName, "
                            + "Patient.LastName, Admission.AdmissionDate, "
                            + "Admission.RoomNo, Admission.DoctorId, Doctor.Name "
                            + "FROM Admission INNER JOIN Patient "
                            + "ON Admission.PatientId=Patient.PatientId "
                            + "INNER JOIN Doctor "
                            + "ON Admission.DoctorId=Doctor.DoctorId "
                            + "WHERE Admission.PatientId = ? "
                            + "AND Admission.DischargeDate IS NULL");
            statement.setInt(1, id);
            qResult = statement.executeQuery();
            if (qResult.next()) {
                Date admissionDate = new java.sql.Date(qResult.
                        getDate("Admission.AdmissionDate").getTime());
                admission.setFName(qResult.getString("Patient.FirstName"));
                admission.setLName(qResult.getString("Patient.LastName"));
                admission.setPid(id);
                admission.setDateOfAdmission(admissionDate);
                admission.setRoomNo(qResult.getString("Admission.RoomNo"));
                admission.setDocID(qResult.getString("Admission.DoctorID"));
                admission.setDocName(qResult.getString("Doctor.Name"));
            }
            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return admission;
    }
    
    @Override
    public int queryDaysAdmitted(AdmissionBean admission){
        Connection connection;
        PreparedStatement statement;
        ResultSet qResult;
        int daysAdmitted=-1;

        try {
            connection = DBConnector.getConnection();
            statement = connection.
                    prepareStatement("SELECT "
                            + "DATEDIFF(CURDATE(), Admission.AdmissionDate) AS "
                            + "daysAdmitted "
                            + "FROM Admission "
                            + "WHERE Admission.PatientId = ? "
                            + "AND Admission.DischargeDate IS NULL");
            statement.setInt(1, admission.getPid());
            qResult = statement.executeQuery();
            if (qResult.next()) {
                daysAdmitted = qResult.getInt("daysAdmitted")+1;                
            }
            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return daysAdmitted;  
    }

    @Override
    public boolean checkIfCurrentlyAdmitted(int pid) {
        Connection connection;
        PreparedStatement statement;
        ResultSet qResult;
        boolean admitted = false;

        try {
            connection = DBConnector.getConnection();
            statement = connection.
                    prepareStatement("SELECT * "
                            + "FROM Admission "
                            + "WHERE Admission.PatientId = ? "
                            + "AND Admission.DischargeDate IS NULL");
            statement.setInt(1, pid);
            qResult = statement.executeQuery();
            admitted = qResult.next();
            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return admitted;
    }

    @Override
    public int addAdmission(AdmissionBean admission) {
        Connection connection;
        PreparedStatement statement;
        int patientsAdmitted = -1;
        try {
            connection = DBConnector.getConnection();
            statement = connection.
                    prepareStatement("INSERT INTO "
                            + "Admission (PatientId, AdmissionDate, "
                            + "DischargeDate, RoomNo, DoctorId) "
                            + "VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, admission.getPid());
            statement.setDate(2,
                    new java.sql.Date(admission.
                            getDateOfAdmission().getTime()));
            statement.setString(3, null);
            statement.setString(4, admission.getRoomNo());
            statement.setString(5, admission.getDocID());
            patientsAdmitted = statement.executeUpdate();

            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return patientsAdmitted;

    }

    @Override
    public int dischargeAdmission(AdmissionBean admission, Date dischargeDate) {
        Connection connection;
        PreparedStatement statement;
        int result = 0;
        try {
            connection = DBConnector.getConnection();
            statement = connection.
                    prepareStatement("UPDATE Admission "
                            + "SET DischargeDate = ? "
                            + "WHERE Admission.PatientId = ? "
                            + "AND Admission.AdmissionDate = ?");
            statement.setDate(1, new java.sql.Date(dischargeDate.getTime()));
            statement.setInt(2, admission.getPid());
            statement.setDate(3,
                    new java.sql.Date(admission.
                            getDateOfAdmission().getTime()));
            result = statement.executeUpdate();
            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    
}
