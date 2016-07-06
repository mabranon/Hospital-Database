package beans;

import dao.AdmissionDAO;
import dao.AdmissionJDBCDAO;
import dao.DiagnosisDAO;
import dao.DiagnosisJDBCDAO;
import dao.DoctorDAO;
import dao.DoctorJDBCDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

@ManagedBean
public class DoctorSearchBean {

    private DoctorDAO doctorDAO;
    private DiagnosisDAO diagDAO;

    private String searchTerm;
    private Date startDate;
    private Date endDate;

    private List<DoctorBean> docList;
    private List<String> docNameList;
    private List<DiagnosisBean> searchResults;

    @PostConstruct
    public void init() {
        this.doctorDAO = new DoctorJDBCDAO();
        this.diagDAO = new DiagnosisJDBCDAO();
        
        docNameList = new ArrayList<>();
        
        this.docList = doctorDAO.getAllDoctors();     
        for(DoctorBean doctor : this.docList){
            docNameList.add(doctor.getName());
        }
    }

    public List<DoctorBean> getDocList() {
        return this.docList;
    }
    
    public List<String> getDocNameList(){
        return this.docNameList;
    }

    public String getSearchTerm() {
        return this.searchTerm;
    }

    public void setSearchTerm(String name) {
        this.searchTerm = name;
    }

    public void setStartDate(Date date){
        this.startDate = date;
    }
    
    public Date getStartDate(){
        return this.startDate;
    }
    
    public void setEndDate(Date date){
        this.endDate = date;
    }
    
    public Date getEndDate(){
        return this.endDate;
    }
    
    public List<DiagnosisBean> getSearchResults() {
        return this.searchResults;
    }

    public void search() {
        List<DiagnosisBean> queryResult;

        queryResult = searchDoctorVisits();

        this.searchResults = queryResult;
    }

    private List<DiagnosisBean> searchDoctorVisits() {
        String docId = "";
        for(DoctorBean doctor : this.docList){
            if (doctor.getName().equals(this.searchTerm)){
                docId = doctor.getID();
            }
        }
       
        List<DiagnosisBean> queryResult
                = diagDAO.
                        queryDoctorVisits(docId, this.startDate, this.endDate);
        return queryResult;
    }

}
