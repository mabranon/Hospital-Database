package dao;

import beans.AdmissionBean;
import beans.DiagnosisBean;
import java.util.Date;
import java.util.List;

public interface DiagnosisDAO {
    public List<DiagnosisBean> queryAdmissionDiags(AdmissionBean admission);
    public int insertDiagnosis(DiagnosisBean diagnosis);
    public List<DiagnosisBean> queryDiseaseStay();
    public List<DiagnosisBean> queryDoctorVisits(String docId, Date startDate, 
            Date endDate);
}
