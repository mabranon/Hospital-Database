package dao;

import beans.DiagnosisBean;
import beans.TreatmentBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.DBConnector;

public class TreatmentJDBCDAO implements TreatmentDAO{

    @Override
    public List<String> queryTreatmentNames() {
        Connection connection;
        PreparedStatement statement;
        ResultSet qResult;
        List<String> treatmentList = new ArrayList<>();
        try {
            connection = DBConnector.getConnection();
            statement = connection.prepareStatement(
                    "SELECT Treatment_Name FROM Treatments");
            qResult = statement.executeQuery();           
            while(qResult.next()){
                treatmentList.add(qResult.getString("Treatment_Name"));
            }
            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return treatmentList;
    }

    @Override
    public List<TreatmentBean> queryAdmissionTreatments(List<DiagnosisBean> diagList) {
        Connection connection;
        PreparedStatement statement;
        ResultSet qResult;
        List<TreatmentBean> treatmentList = new ArrayList<>();
        try {
            connection = DBConnector.getConnection();
            statement = connection.prepareStatement(
                    "SELECT * FROM Treatments");
            qResult = statement.executeQuery();           
            while(qResult.next() && (treatmentList.size() != diagList.size())){
                for(int i=0; i<diagList.size(); i++){
                    if(qResult.getString("Treatment_Name").equals(diagList.get(i).
                            getTreatmentName())){
                        TreatmentBean treatment = new TreatmentBean();
                        treatment.setTreatmentName(qResult.
                                getString("Treatment_Name"));
                        treatment.setTreatmentPrice(qResult.getDouble("Price"));
                        treatmentList.add(treatment);
                        i = diagList.size();
                    }
                }
            }
            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return treatmentList;
    }
    
}
