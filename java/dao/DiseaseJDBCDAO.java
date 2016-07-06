package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.DBConnector;

public class DiseaseJDBCDAO implements DiseaseDAO{

    @Override
    public List<String> queryDiseaseList() {
        Connection connection;
        PreparedStatement statement;
        ResultSet qResult;
        List<String> diseaseList = new ArrayList<>();
        try {
            connection = DBConnector.getConnection();
            statement = connection.prepareStatement(
                    "SELECT Disease_Name FROM Disease");
            qResult = statement.executeQuery();           
            while(qResult.next()){
                diseaseList.add(qResult.getString("Disease_Name"));
            }
            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return diseaseList;
    }
    
}
