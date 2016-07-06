package dao;

import beans.AdmissionBean;
import java.util.Date;
import java.util.List;

public interface AdmissionDAO {
    public List<AdmissionBean> queryAllAdmissions();
    public AdmissionBean queryCurrentAdmission(int id);
    public int queryDaysAdmitted(AdmissionBean admission);
    public int addAdmission(AdmissionBean admission);
    public int dischargeAdmission(AdmissionBean admission, Date dischargeDate);
    public boolean checkIfCurrentlyAdmitted(int pid);
}
