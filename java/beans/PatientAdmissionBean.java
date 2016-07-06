package beans;

import dao.AdmissionJDBCDAO;
import dao.DoctorJDBCDAO;
import dao.PatientJDBCDAO;
import dao.RoomJDBCDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.view.ViewScoped;

@ManagedBean
@ViewScoped
public class PatientAdmissionBean {

    private PatientJDBCDAO patientDAO;
    private DoctorJDBCDAO doctorDAO;
    private RoomJDBCDAO roomDAO;
    private AdmissionJDBCDAO admissionDAO;

    @ManagedProperty(value = "#{param.patientChosen}")
    private String pid;
    private PatientBean patient;
    private Date dateOfAdmission;
    private String docName;
    private String roomNo;

    private List<DoctorBean> doctorList;
    private List<String> doctorNameList;
    private List<String> freeRooms;

    @PostConstruct
    public void init() {
        this.patientDAO = new PatientJDBCDAO();
        this.doctorDAO = new DoctorJDBCDAO();
        this.roomDAO = new RoomJDBCDAO();
        this.admissionDAO = new AdmissionJDBCDAO();

        patient = patientDAO.queryPatient(Integer.parseInt(pid));
        
        
        this.doctorList = this.doctorDAO.getAllDoctors();
        this.doctorNameList = new ArrayList<>();
        for (int i = 0; i < this.doctorList.size(); i++) {
            this.doctorNameList.add(this.doctorList.get(i).getName());
        }

        this.freeRooms = new ArrayList<>();
        List<RoomBean> roomList = roomDAO.getAllFreeRooms();
        for (int i = 0; i < roomList.size(); i++) {
            freeRooms.add(roomList.get(i).getRoomNo());
        }
    }

    public void setPid(String id) {
        this.pid = id;
    }

    public String getPid() {
        return this.pid;
    }

    public void setDateOfAdmission(Date doa) {
        this.dateOfAdmission = doa;
    }

    public Date getDateOfAdmission() {
        return this.dateOfAdmission;
    }

    public void setDocName(String name) {
        this.docName = name;
    }

    public String getDocName() {
        return this.docName;
    }

    public void setRoomNo(String no) {
        this.roomNo = no;
    }

    public String getRoomNo() {
        return this.roomNo;
    }

    public PatientBean getPatient() {
        return this.patient;
    }

    public List<String> getDoctorNameList() {
        return this.doctorNameList;
    }

    public List<String> getFreeRooms() {
        return this.freeRooms;
    }

    public String admitPatient() {
        AdmissionBean newAdmission = new AdmissionBean();

        String docID = "";
        for (int i = 0; i < doctorList.size(); i++) {
            if (doctorList.get(i).getName().equals(this.docName)) {
                docID = doctorList.get(i).getID();
            }
        }

        newAdmission.setPid(this.patient.getID());
        newAdmission.setDateOfAdmission(this.dateOfAdmission);
        newAdmission.setRoomNo(this.roomNo);
        newAdmission.setDocID(docID);
 
        if(this.admissionDAO.addAdmission(newAdmission) > 0){
            return "admission_success.xhtml?faces-redirect=true";
        }
        return "admission_failed.xhtml?faces-redirect=true";
    }
}
