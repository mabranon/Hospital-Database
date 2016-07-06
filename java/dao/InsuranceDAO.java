
package dao;

import beans.InsuranceBean;
import java.util.List;

public interface InsuranceDAO {
    public List<InsuranceBean> getAllCompanies();
    public InsuranceBean getCompany(String cmpnyName);  
}
