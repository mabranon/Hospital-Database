package beans;

import dao.AdmissionDAO;
import dao.AdmissionJDBCDAO;
import dao.PatientDAO;
import dao.PatientJDBCDAO;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

@ManagedBean
@ViewScoped
public class PatientSearchBean {

    private PatientDAO patientDAO;
    private AdmissionDAO admissionDAO;

    private String searchTerm;
    private String searchDomain;

    private List<String> domainList;
    private List<PatientBean> searchResults;

    @PostConstruct
    public void init() {
        this.patientDAO = new PatientJDBCDAO();
        this.admissionDAO = new AdmissionJDBCDAO();
        this.searchDomain = "Last Name";
        domainList = Arrays.asList(
                "Last Name",
                "Patient ID");
    }

    public List<String> getDomainList() {
        return this.domainList;
    }

    public String getSearchTerm() {
        return this.searchTerm;
    }

    public void setSearchTerm(String name) {
        this.searchTerm = name;
    }

    public String getSearchDomain() {
        return this.searchDomain;
    }

    public void setSearchDomain(String domain) {
        this.searchDomain = domain;
    }

    public List<PatientBean> getSearchResults() {
        return this.searchResults;
    }

    public void search() {
        List<PatientBean> queryResult;
        if (this.searchTerm.equals("*")) {
            queryResult = searchAllPatients();
        } else if (this.searchDomain.equals("Last Name")) {
            queryResult = searchByLastName();
        } else {
            queryResult = searchByPID();
        }

        this.searchResults = queryResult;
    }
    
    private List<PatientBean> searchAllPatients(){
        List<PatientBean> queryResult =
                patientDAO.getAllPatients();
        
        //queries DB to assign boolean admitted variable
        for (int i = 0; i < queryResult.size(); i++) {
            queryResult.get(i).setIsAdmitted(admissionDAO.
                    checkIfCurrentlyAdmitted(queryResult.get(i).getID()));
        }
        
        return queryResult;
    }

    private List<PatientBean> searchByLastName() {

        List<PatientBean> queryResult
                = patientDAO.listPatientByName(this.getSearchTerm());

        //queries DB to assign boolean admitted variable
        for (int i = 0; i < queryResult.size(); i++) {
            queryResult.get(i).setIsAdmitted(admissionDAO.
                    checkIfCurrentlyAdmitted(queryResult.get(i).getID()));
        }

        return queryResult;
    }

    private List<PatientBean> searchByPID() {
        List<PatientBean> queryResult
                = patientDAO.listPatientsByPID(this.getSearchTerm());

        //queries DB to assign boolean admitted variable
        for (int i = 0; i < queryResult.size(); i++) {
            queryResult.get(i).setIsAdmitted(admissionDAO.
                    checkIfCurrentlyAdmitted(queryResult.get(i).getID()));
        }
        return queryResult;
    }
}
