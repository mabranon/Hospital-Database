package dao;

import beans.DiagnosisBean;
import beans.TreatmentBean;
import java.util.List;

public interface TreatmentDAO {
    public List<String> queryTreatmentNames();
    public List<TreatmentBean> queryAdmissionTreatments(List<DiagnosisBean> diagList);
}
