package dao;

import beans.AdmissionBean;
import beans.DiagnosisBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.DBConnector;

public class DiagnosisJDBCDAO implements DiagnosisDAO {

    @Override
    public List<DiagnosisBean> queryAdmissionDiags(AdmissionBean admission) {
        Connection connection;
        PreparedStatement statement;
        ResultSet qResult;
        List<DiagnosisBean> diagList = new ArrayList<>();
        java.sql.Date addDate = new java.sql.Date(admission.getDateOfAdmission().getTime());

        try {
            connection = DBConnector.getConnection();
            statement = connection.prepareStatement(
                    "SELECT Diagnosis.Diag_Time, Doctor.Name, "
                    + "Diagnosis.Disease_Name, "
                    + "Diagnosis.Treatment_Name "
                    + "FROM Diagnosis INNER JOIN Doctor "
                    + "ON Diagnosis.DoctorId=Doctor.DoctorId "
                    + "WHERE PatientId=? "
                    + "AND AdmissionDate=?");
            statement.setInt(1, admission.getPid());
            statement.setDate(2, addDate);
            qResult = statement.executeQuery();

            while (qResult.next()) {
                Date diagTime = new java.sql.Timestamp(qResult.
                        getTimestamp("Diagnosis.Diag_Time").getTime());
                DiagnosisBean diag = new DiagnosisBean();
                diag.setDiagTime(diagTime);
                diag.setDocName(qResult.getString("Doctor.Name"));
                diag.setDiseaseName(qResult.
                        getString("Diagnosis.Disease_Name"));
                diag.setTreatmentName(qResult.
                        getString("Diagnosis.Treatment_Name"));

                diagList.add(diag);
            }

            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return diagList;
    }

    @Override
    public int insertDiagnosis(DiagnosisBean diagnosis) {
        Connection connection;
        PreparedStatement statement;
        java.sql.Date addDate = new java.sql.Date(diagnosis.getAdmissionDate().getTime());
        int rowsInserted = -1;

        try {
            connection = DBConnector.getConnection();
            statement = connection.prepareStatement("INSERT INTO "
                    + "Diagnosis (PatientId, DoctorId, "
                    + "AdmissionDate, Disease_Name, Treatment_Name) "
                    + "VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, diagnosis.getPid());
            statement.setString(2, diagnosis.getDocId());
            statement.setDate(3, addDate);
            statement.setString(4, diagnosis.getDiseaseName());
            statement.setString(5, diagnosis.getTreatmentName());
            rowsInserted = statement.executeUpdate();

            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return rowsInserted;
    }

    @Override
    public List<DiagnosisBean> queryDiseaseStay() {
        Connection connection;
        PreparedStatement statement;
        ResultSet qResult;
        List<DiagnosisBean> diagList = new ArrayList<>();
        try {
            connection = DBConnector.getConnection();
            statement = connection.prepareStatement(
                    "SELECT DISTINCT Diagnosis.PatientId, Admission.AdmissionDate, "
                    + "Admission.DischargeDate, Diagnosis.Disease_Name FROM "
                    + "Diagnosis JOIN Admission USING (PatientId)"
                    + "WHERE cast(Diagnosis.Diag_Time as date) BETWEEN Admission.AdmissionDate AND Admission.DischargeDate");
            qResult = statement.executeQuery();

            while (qResult.next()) {
                Date AdmissionDate = new java.sql.Timestamp(qResult.
                        getTimestamp("Admission.AdmissionDate").getTime());
                Date DischargeDate = null;
                if (qResult.getTimestamp("Admission.DischargeDate") != null) {
                    DischargeDate = new java.sql.Timestamp(
                            qResult.getTimestamp("Admission.DischargeDate").getTime());
                }
                DiagnosisBean diag = new DiagnosisBean();
                diag.setAdmissionDate(AdmissionDate);
                diag.setDischargeDate(DischargeDate);
                diag.setDiseaseName(qResult.
                        getString("Diagnosis.Disease_Name"));

                diagList.add(diag);
            }

            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return diagList;
    }

    @Override
    public List<DiagnosisBean> queryDoctorVisits(String docId, Date startDate,
            Date endDate) {
        Connection connection;
        PreparedStatement statement;
        ResultSet qResult;
        List<DiagnosisBean> diagList = new ArrayList<>();
        java.sql.Timestamp searchStartDate = new java.sql.Timestamp(startDate.getTime());
        java.sql.Timestamp searchEndDate = new java.sql.Timestamp(endDate.getTime());

        try {
            connection = DBConnector.getConnection();
            statement = connection.prepareStatement(
                    "SELECT Patient.PatientId, "
                    + "Patient.LastName, "
                    + "Patient.FirstName, "
                    + "Diagnosis.Diag_Time, "
                    + "Diagnosis.Disease_Name, "
                    + "Diagnosis.Treatment_Name "
                    + "FROM Diagnosis INNER JOIN Patient "
                    + "ON Diagnosis.PatientId=Patient.PatientId "
                    + "WHERE Diagnosis.DoctorId=? "
                    + "AND Diagnosis.Diag_Time BETWEEN ? AND ?");
            statement.setString(1, docId);
            statement.setTimestamp(2, searchStartDate);
            statement.setTimestamp(3, searchEndDate);
            qResult = statement.executeQuery();

            while (qResult.next()) {
                Date diagTime = new java.sql.Timestamp(qResult.
                        getTimestamp("Diagnosis.Diag_Time").getTime());
                DiagnosisBean diag = new DiagnosisBean();
                diag.setPatientNum(qResult.getInt("Patient.PatientId"));
                diag.setPatientLName(qResult.getString("Patient.LastName"));
                diag.setPatientFName(qResult.getString("Patient.FirstName"));
                diag.setDiagTime(diagTime);
                diag.setDiseaseName(qResult.
                        getString("Diagnosis.Disease_Name"));
                diag.setTreatmentName(qResult.
                        getString("Diagnosis.Treatment_Name"));

                diagList.add(diag);
            }

            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return diagList;
    }

}
