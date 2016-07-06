package dao;

import beans.InsuranceBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.DBConnector;

public class InsuranceJDBCDAO implements InsuranceDAO{

    @Override
    public List<InsuranceBean> getAllCompanies() {
        Connection connection;
        PreparedStatement statement;
        ResultSet qResult;
        List<InsuranceBean> companyList = new ArrayList<>();

        try {
            connection = DBConnector.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Insurance");
            qResult = statement.executeQuery();

            while (qResult.next()) {
                InsuranceBean company = new InsuranceBean();
                company.setCmpnyName(qResult.getString("InsuranceCompany"));
                company.setPayInfo(qResult.getDouble("PayInfo"));
                companyList.add(company);
            }

            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return companyList;
    }

    @Override
    public InsuranceBean getCompany(String cmpnyName) {
               Connection connection;
        PreparedStatement statement;
        InsuranceBean company = new InsuranceBean();
        ResultSet qResult;

        try {
            connection = DBConnector.getConnection();
            statement = connection.
                    prepareStatement("SELECT * "
                            + "FROM Insurance "
                            + "WHERE Insurance.InsuranceCompany = ?");
            statement.setString(1, cmpnyName);
            qResult = statement.executeQuery();
            qResult.next();
            company.setCmpnyName(qResult.getString("InsuranceCompany"));
            company.setPayInfo(qResult.getDouble("PayInfo"));
            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }  
        return company;
    }

    
}