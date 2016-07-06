package dao;

import beans.PatientBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.DBConnector;

public class PatientJDBCDAO implements PatientDAO {

    @Override
    public List<PatientBean> getAllPatients() {
        Connection connection;
        PreparedStatement statement;
        ResultSet qResult;
        List<PatientBean> patientList = new ArrayList<>();

        try {
            connection = DBConnector.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Patient");
            qResult = statement.executeQuery();

            while (qResult.next()) {
                Date patientDoB = new java.sql.Date(qResult.getDate("DoB")
                        .getTime());

                PatientBean patient = new PatientBean();
                patient.setID(qResult.getInt("PatientId"));
                patient.setFirstName(qResult.getString("FirstName"));
                patient.setLastName(qResult.getString("LastName"));
                patient.setGender(qResult.getString("Gender"));
                patient.setDateOfBirth(patientDoB);
                patient.setInsuranceCo(qResult.getString("InsuranceCompany"));
                patientList.add(patient);
            }

            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return patientList;
    }

    @Override
    public PatientBean queryPatient(int id) {
        Connection connection;
        PreparedStatement statement;
        PatientBean patient = new PatientBean();
        ResultSet qResult;

        try {
            connection = DBConnector.getConnection();
            statement = connection.
                    prepareStatement("SELECT * "
                            + "FROM Patient "
                            + "WHERE Patient.PatientId = ?");
            statement.setInt(1, id);
            qResult = statement.executeQuery();

            if (qResult.next()) {
                Date patientDoB = new java.sql.Date(qResult.getDate("DoB")
                        .getTime());
                patient.setID(qResult.getInt("PatientId"));
                patient.setFirstName(qResult.getString("FirstName"));
                patient.setLastName(qResult.getString("LastName"));
                patient.setGender(qResult.getString("Gender"));
                patient.setDateOfBirth(patientDoB);
                patient.setInsuranceCo(qResult.getString("InsuranceCompany"));
            }
            statement.close();
            DBConnector.close(connection);
            return patient;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return patient;
    }

    @Override
    public List<PatientBean> listPatientByName(String name) {
        Connection connection;
        PreparedStatement statement;
        ResultSet qResult;
        List<PatientBean> patientList = new ArrayList<>();

        try {
            connection = DBConnector.getConnection();
            statement = connection.prepareStatement(
                    "SELECT Patient.PatientId, Patient.LastName, "
                    + "Patient.FirstName, Patient.Gender, Patient.DoB "
                    + "FROM Patient "
                    + "WHERE Patient.LastName LIKE ?");
            statement.setString(1, "%"+name+"%");
            qResult = statement.executeQuery();

            while (qResult.next()) {
                Date patientDoB = new java.sql.Date(qResult.getDate("DoB")
                        .getTime());

                PatientBean patient = new PatientBean();
                patient.setID(qResult.getInt("PatientId"));
                patient.setLastName(qResult.getString("LastName"));
                patient.setFirstName(qResult.getString("FirstName"));
                patient.setGender(qResult.getString("Gender"));
                patient.setDateOfBirth(patientDoB);
                patientList.add(patient);
            }
            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return patientList;
    }

    @Override
    public List<PatientBean> listPatientsByPID(String pid) {
        Connection connection;
        PreparedStatement statement;
        ResultSet qResult;
        List<PatientBean> patientList = new ArrayList<>();

        try {
            connection = DBConnector.getConnection();
            statement = connection.prepareStatement(
                    "SELECT Patient.PatientId, Patient.LastName, "
                    + "Patient.FirstName, Patient.Gender, Patient.DoB "
                    + "FROM Patient "
                    + "WHERE Patient.PatientId = ?");
            statement.setString(1, pid);
            qResult = statement.executeQuery();

            while (qResult.next()) {
                Date patientDoB = new java.sql.Date(qResult.getDate("DoB")
                        .getTime());

                PatientBean patient = new PatientBean();
                patient.setID(qResult.getInt("PatientId"));
                patient.setLastName(qResult.getString("LastName"));
                patient.setFirstName(qResult.getString("FirstName"));
                patient.setGender(qResult.getString("Gender"));
                patient.setDateOfBirth(patientDoB);
                patientList.add(patient);
            }
            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return patientList;
    }

    @Override
    public int addPatient(PatientBean patient) {
        Connection connection;
        PreparedStatement statement;
        int patientsAdded = -1;
       
        try {
            connection = DBConnector.getConnection();
            statement = connection.
                    prepareStatement("INSERT INTO "
                            + "Patient (FirstName, LastName, "
                            + "Gender, DoB, InsuranceCompany) "
                            + "VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getGender());
            statement.setDate(4,
                    new java.sql.Date(patient.getDateOfBirth().getTime()));
            statement.setString(5, patient.getInsuranceCo());
            patientsAdded = statement.executeUpdate();

            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return patientsAdded;
    }

    @Override
    public void removePatient(PatientBean patient) {
        Connection connection;
        PreparedStatement statement;

        try {
            connection = DBConnector.getConnection();
            statement = connection.
                    prepareStatement("DELETE FROM Patient "
                            + "WHERE Patient.PatientId = ?");
            statement.setInt(1, patient.getID());
            statement.executeUpdate();

            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void updatePatient(PatientBean patient, String origID) {
        Connection connection;
        PreparedStatement statement;

        try {
            connection = DBConnector.getConnection();
            statement = connection.
                    prepareStatement("UPDATE Patient "
                            + "SET FirstName = ?, LastName = ?, "
                            + "Gender = ?, DoB = ?, InsuranceCompany = ? "
                            + "WHERE Patient.PatientId = ?");
            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getGender());
            statement.setDate(4,
                    new java.sql.Date(patient.getDateOfBirth().getTime()));
            statement.setString(5, patient.getInsuranceCo());
            statement.setString(6, origID);
            statement.executeUpdate();

            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

}
