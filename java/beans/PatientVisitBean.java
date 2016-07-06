package beans;

import dao.AdmissionDAO;
import dao.AdmissionJDBCDAO;
import dao.DiagnosisDAO;
import dao.DiagnosisJDBCDAO;
import dao.DiseaseDAO;
import dao.DiseaseJDBCDAO;
import dao.DoctorDAO;
import dao.DoctorJDBCDAO;
import dao.TreatmentDAO;
import dao.TreatmentJDBCDAO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.view.ViewScoped;

@ManagedBean
@ViewScoped
public class PatientVisitBean {

    private AdmissionDAO admissionDAO;
    private DiagnosisDAO diagDAO;
    private DoctorDAO docDAO;
    private DiseaseDAO diseaseDAO;
    private TreatmentDAO treatmentDAO;

    @ManagedProperty(value = "#{param.patientChosen}")
    private String pid;
    private AdmissionBean admission;

    private String diagDocName;
    private String diagDisease;
    private String diagTreatment;

    private List<DoctorBean> docList;
    private List<String> docNameList;
    private List<String> diseaseList;
    private List<String> treatmentList;

    private List<DiagnosisBean> diagList;

    @PostConstruct
    public void init() {
        this.admissionDAO = new AdmissionJDBCDAO();
        this.diagDAO = new DiagnosisJDBCDAO();
        this.docDAO = new DoctorJDBCDAO();
        this.diseaseDAO = new DiseaseJDBCDAO();
        this.treatmentDAO = new TreatmentJDBCDAO();

        docNameList = new ArrayList<>();
        treatmentList = new ArrayList<>();

        this.admission = this.admissionDAO.queryCurrentAdmission(Integer.parseInt(pid));

        this.diagList = this.diagDAO.queryAdmissionDiags(this.admission);

        this.diseaseList = this.diseaseDAO.queryDiseaseList();
        this.treatmentList= this.treatmentDAO.queryTreatmentNames();
        
        this.docList = this.docDAO.getAllDoctors();
        for (int i = 0; i < this.docList.size(); i++) {
            this.docNameList.add(this.docList.get(i).getName());
        }
    }

    public AdmissionBean getAdmission() {
        return this.admission;
    }

    public void setPid(String id) {
        this.pid = id;
    }

    public String getPid() {
        return this.pid;
    }

    public List<DiagnosisBean> getDiagList() {
        return this.diagList;
    }

    public void setDiagDocName(String name) {
        this.diagDocName = name;
    }

    public String getDiagDocName() {
        return this.diagDocName;
    }

    public void setDiagDisease(String disease) {
        this.diagDisease = disease;
    }

    public String getDiagDisease() {
        return this.diagDisease;
    }

    public void setDiagTreatment(String treatment) {
        this.diagTreatment = treatment;
    }

    public String getDiagTreatment() {
        return this.diagTreatment;
    }

    public List<String> getDiseaseList() {
        return this.diseaseList;
    }

    public List<String> getTreatmentList() {
        return this.treatmentList;
    }

    public List<String> getDocNameList() {
        return this.docNameList;
    }

    public String enterDiagnosis() {
        DiagnosisBean diag = new DiagnosisBean();

        String docID = "";
        for (int i = 0; i < docList.size(); i++) {
            if (docList.get(i).getName().equals(this.diagDocName)) {
                docID = docList.get(i).getID();
            }
        }

        diag.setPid(Integer.parseInt(this.pid));
        diag.setAdmissionDate(this.admission.getDateOfAdmission());
        diag.setDocId(docID);
        diag.setDiseaseName(this.diagDisease);
        diag.setTreatmentName(this.diagTreatment);

        if(diagDAO.insertDiagnosis(diag) > 0){
            return "visit_recorded_success.xhtml?faces-redirect=true";
        }
        return "visit_recorded_failed.xhtml?faces-redirect=true";
    }
}
