package beans;

import dao.AdmissionDAO;
import dao.AdmissionJDBCDAO;
import dao.DiagnosisDAO;
import dao.DiagnosisJDBCDAO;
import dao.InsuranceDAO;
import dao.InsuranceJDBCDAO;
import dao.PatientDAO;
import dao.PatientJDBCDAO;
import dao.RoomDAO;
import dao.RoomJDBCDAO;
import dao.TreatmentDAO;
import dao.TreatmentJDBCDAO;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

@ManagedBean
public class DischargeBean {
    private static final double CHARGE_PER_DAY = 150.00;
    
    private AdmissionDAO admissionDAO;
    private DiagnosisDAO diagDAO;    
    private TreatmentDAO treatmentDAO;
    private PatientDAO patientDAO;
    private InsuranceDAO insuranceDAO;
    private RoomDAO roomDAO;

    @ManagedProperty(value = "#{param.patientChosen}")
    private String pid;
    private AdmissionBean admission;
    private PatientBean patient;
    
    private int numDaysAdmitted;
    private Date proposedDischargeDate;
    private String pDischargeDateString;
    private RoomBean room;
    private double roomCharge;
    private double subTotal;
    private double amountDue;
    private double coinsurance;
    private String formatSub;
    private String formatTotal;
    
    private List<DiagnosisBean> diagList;
    private List<TreatmentBean> treatmentList;
    
    @PostConstruct
    public void init(){
        NumberFormat formatter= new DecimalFormat("#0.00");
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        this.admissionDAO = new AdmissionJDBCDAO();
        this.diagDAO = new DiagnosisJDBCDAO();
        this.treatmentDAO = new TreatmentJDBCDAO();
        this.patientDAO = new PatientJDBCDAO();
        this.insuranceDAO = new InsuranceJDBCDAO();
        this.roomDAO = new RoomJDBCDAO();
        
        this.admission = this.admissionDAO.queryCurrentAdmission(Integer.parseInt(this.pid));
        this.patient = this.patientDAO.queryPatient(Integer.parseInt(this.pid));
        
        InsuranceBean provider= this.insuranceDAO.
                getCompany(this.patient.getInsuranceCo());
        coinsurance = provider.getPayInfo();
        
        this.proposedDischargeDate = Calendar.getInstance().getTime();
        this.pDischargeDateString = sfd.format(proposedDischargeDate);
        
        this.diagList = this.diagDAO.queryAdmissionDiags(this.admission);
        
        this.numDaysAdmitted = admissionDAO.queryDaysAdmitted(this.admission);
        
        this.treatmentList = treatmentDAO.queryAdmissionTreatments(diagList);
        
        this.room = roomDAO.queryRoom(this.admission.getRoomNo());
        this.roomCharge = Double.parseDouble(room.getPrice());
        
        this.subTotal = (this.numDaysAdmitted*CHARGE_PER_DAY) + 
                (this.numDaysAdmitted*this.roomCharge);
        for(int i=0; i<this.treatmentList.size(); i++){
            this.subTotal += this.treatmentList.get(i).getTreatmentPrice();
        }
        this.amountDue = this.subTotal*coinsurance;
        
        this.formatSub = formatter.format(this.subTotal);
        this.formatTotal = formatter.format(this.amountDue);
    }
    
    public double getCHARGE_PER_DAY(){
        return CHARGE_PER_DAY;
    }
    
    public void setPid(String id){
        this.pid = id;
    }
    
    public String getPid(){
        return this.pid;
    }
    
    public void setPDischargeDateString(String date){
        this.pDischargeDateString = date;
    }
    
    public String getPDischargeDateString(){
        return this.pDischargeDateString;
    }
    
    public void setNumDaysAdmitted(int days){
        this.numDaysAdmitted = days;
    }
    
    public int getNumDaysAdmitted(){
        return this.numDaysAdmitted;
    }
    
    public void setProposedDischargeDate(Date date){
        this.proposedDischargeDate = date;
    }
    
    public Date getProposedDischargeDate(){
        return this.proposedDischargeDate;
    }
    
    public void setRoomCharge(double price){
        this.roomCharge = price;
    }
    
    public double getRoomCharge(){
        return this.roomCharge;
    }
    
    public void setRoom(RoomBean room){
        this.room = room;
    }
    
    public RoomBean getRoom(){
        return this.room;
    }
    
    public void setAmountDue(double amnt){
        this.amountDue = amnt;
    }
    
    public double getAmountDue(){
        return this.amountDue;
    }
    
    public void setSubTotal(double amnt){
        this.subTotal = amnt;
    }
    
    public double getSubTotal(){
        return this.subTotal;
    }
    
    public void setFormatSub(String subTotal){
        this.formatSub = subTotal;
    }
    
    public String getFormatSub(){
        return this.formatSub;
    }
    
    public void setFormatTotal(String total){
        this.formatTotal = total;
    }
    
    public String getFormatTotal(){
        return this.formatTotal;
    }
    
    public List<DiagnosisBean> getDiagList(){
        return this.diagList;
    }
    
    public List<TreatmentBean> getTreatmentList(){
        return this.treatmentList;
    }
    
    public AdmissionBean getAdmission(){
        return this.admission;
    }
    
    public PatientBean getPatient(){
        return this.patient;
    }
    
    public double getCoinsurance(){
        return this.coinsurance;
    }
    
    public String dischargePatient(){
        if(this.admissionDAO.
                dischargeAdmission(this.admission, this.proposedDischargeDate)
                > 0){
            return "discharge_success.xhtml?faces-redirect=true";
        }
        return "discharge_failed.xhtml?faces-redirect=true";
    }
}
