package dao;

import beans.PatientBean;
import java.util.List;

public interface PatientDAO {
    public List<PatientBean> getAllPatients();
    public PatientBean queryPatient(int id);
    public int addPatient(PatientBean patient);
    public void removePatient(PatientBean patient);
    public void updatePatient(PatientBean patient, String origID);
    public List<PatientBean> listPatientByName(String searchTerm);
    public List<PatientBean> listPatientsByPID(String searchTerm);
}
