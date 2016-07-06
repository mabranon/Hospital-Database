package beans;

import dao.DiagnosisDAO;
import dao.DiagnosisJDBCDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

@ManagedBean
public class DiagnosisBean {
    private String patientFName;
    private String patientLName;
    private int pid;
    private Date diagTime;
    private String docID;
    private String docName;
    private Date admissionDate;
    private Date DischargeDate;
    private String diseaseName;
    private String treatmentName;
    private int patientNum;
    private int stayAverage;
    private DiagnosisDAO diagnosisDAO;
    
    @PostConstruct
    public void initDAOs() {
        this.diagnosisDAO = new DiagnosisJDBCDAO();
    }
    
    public void setPid(int id){
        this.pid=id;
    }
    
    public int getPid(){
        return this.pid;
    }
    
    public void setPatientFName(String fName){
        this.patientFName = fName;
    }
    
    public String getPatientFName(){
        return this.patientFName;
    }
    
    public void setPatientLName(String lName){
        this.patientLName=lName;
    }
    
    public String getPatientLName(){
        return this.patientLName;
    }
    
    public void setDocId(String id){
        this.docID=id;
    }
    
    public String getDocId(){
        return this.docID;
    }
    
    public void setAdmissionDate(Date date){
        this.admissionDate=date;
    }
    
    public Date getAdmissionDate(){
        return this.admissionDate;
    }
    
    public void setDiseaseName(String name){
        this.diseaseName=name;
    }
    
    public String getDiseaseName(){
        return this.diseaseName;
    }
    
    public void setDiagTime(Date time){
        this.diagTime=time;
    }
    
    public Date getDiagTime(){
        return this.diagTime;
    }
    
    public void setDocName(String name){
        this.docName=name;
    }
    
    public String getDocName(){
        return this.docName;
    }
    
    public void setTreatmentName(String name){
        this.treatmentName=name;
    }
    
    public String getTreatmentName(){
        return this.treatmentName;
    }

    public Date getDischargeDate() {
        return DischargeDate;
    }

    public void setDischargeDate(Date DischargeDate) {
        this.DischargeDate = DischargeDate;
    }
    
       public int getPatientNum() {
        return patientNum;
    }

    public void setPatientNum(int patientNum) {
        this.patientNum = patientNum;
    }

    public int getStayAverage() {
        return stayAverage;
    }

    public void setStayAverage(int stayAverage) {
        this.stayAverage = stayAverage;
    }
    
    public List<DiagnosisBean> getDiseaseStay()
    {
        List<DiagnosisBean> diagnosis = diagnosisDAO.queryDiseaseStay();
        List<DiagnosisBean> average = new ArrayList<>();
        boolean found;
        for (DiagnosisBean currentDiagnosis : diagnosis) {
            found = false;
            Date admission = currentDiagnosis.getAdmissionDate();
            Date discharge = currentDiagnosis.getDischargeDate();
            if(discharge == null)
                discharge = new Date();
            int days = (int)((discharge.getTime() - admission.getTime())/(24 * 60 * 60 * 1000));
            days++;
            for(DiagnosisBean currentAverage : average) {
                if(currentAverage.getDiseaseName().equals(currentDiagnosis.getDiseaseName())) {
                    currentAverage.setPatientNum(currentAverage.getPatientNum() + 1);
                    currentAverage.setStayAverage((currentAverage.getStayAverage() + days)/currentAverage.getPatientNum());
                    found = true;
                }
            }
            if(!found) {
                DiagnosisBean d = new DiagnosisBean();
                d.setDiseaseName(currentDiagnosis.getDiseaseName());
                d.setStayAverage(days);
                d.setPatientNum(1);
                average.add(d);
            }
        }
        return average;
    }
    
}
