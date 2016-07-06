package beans;

import dao.AdmissionDAO;
import dao.AdmissionJDBCDAO;
import dao.InsuranceDAO;
import dao.InsuranceJDBCDAO;
import dao.PatientDAO;
import dao.PatientJDBCDAO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class PatientBean {

    private PatientDAO patientDAO;
    private InsuranceDAO insuranceDAO;

    private int ID;
    private String firstName;
    private String lastName;
    private String gender;
    private Date dateOfBirth;
    private String insuranceCo;
    private boolean isAdmitted;

    private List<String> providerNameList;
    private List<String> genderOptionsList;
    private List<PatientBean> patientList;

    @PostConstruct
    public void initDAOs() {
        this.patientDAO = new PatientJDBCDAO();
        this.insuranceDAO = new InsuranceJDBCDAO();
    }

    public List<PatientBean> getPatientList() {
        if (this.patientList == null) {
            this.patientList = patientDAO.getAllPatients();
        }
        return this.patientList;
    }
    
    public List<String> getProviderNameList() {
        if (this.providerNameList == null) {
            this.providerNameList = new ArrayList<>();
            List<InsuranceBean> providerList = 
                    this.insuranceDAO.getAllCompanies();
            
            for(int i=0; i<providerList.size(); i++){
                this.providerNameList.add(providerList.get(i).getCmpnyName());
            }
        }
        return this.providerNameList;
    }

    public List<String> getGenderOptionsList() {
        if (this.genderOptionsList == null) {
            genderOptionsList = Arrays.asList("Male", "Female");
        }
        return this.genderOptionsList;
    }

    public String insert_patient() {
        if(this.patientDAO.addPatient(this) > 0){
            return "new_patient_success.xhtml?faces-redirect=true";
        }
        return "new_patient_failed.xhtml?faces-redirect=true";   
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String name) {
        this.lastName = name;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(Date date) {
        this.dateOfBirth = date;
    }

    public String getInsuranceCo() {
        return this.insuranceCo;
    }

    public void setInsuranceCo(String name) {
        this.insuranceCo = name;
    }
    
    public void setIsAdmitted(boolean admitted){
        this.isAdmitted = admitted;
    }
    
    public boolean getIsAdmitted(){
        return this.isAdmitted;
    }
    
    public void onDateSelect(SelectEvent event) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.
                addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Date Selected",
                        sdf.format(event.getObject())));
    }
}
