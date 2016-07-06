package beans;

import java.util.Date;
import javax.faces.bean.ManagedBean;

@ManagedBean
public class AdmissionBean {
    private int pid;
    private String fName;
    private String lName;
    private Date dateOfAdmission;
    private Date dateOfDischarge;
    private String roomNo;
    private String docID;
    private String docName;
    
    public void setPid(int id){
        this.pid = id;
    }
    
    public int getPid(){
        return this.pid;
    }
    
    public void setFName(String name){
        this.fName=name;
    }
    
    public String getFName(){
        return this.fName;
    }
    
    public void setLName(String name){
        this.lName=name;
    }
    
    public String getLName(){
        return this.lName;
    }
    
    public void setDateOfAdmission(Date doa){
        this.dateOfAdmission = doa;
    }
    
    public Date getDateOfAdmission(){
        return this.dateOfAdmission;
    }
    
    public void setDateOfDischarge(Date dod){
        this.dateOfDischarge = dod;
    }
    
    public Date getDateOfDischarge(){
        return this.dateOfDischarge;
    }
    
    public void setRoomNo(String rmNo){
        this.roomNo = rmNo;
    }
    
    public String getRoomNo(){
        return this.roomNo;
    }
    public void setDocID(String id){
        this.docID = id;
    }
    
    public String getDocID(){
        return this.docID;
    }
    
    public void setDocName(String name){
        this.docName=name;
    }
    
    public String getDocName(){
        return this.docName;
    }
}
