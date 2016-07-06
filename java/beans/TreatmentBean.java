package beans;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class TreatmentBean {
    private String treatmentName;
    private double treatmentPrice;
    
    public void setTreatmentName(String name){
        this.treatmentName = name;
    }
    
    public String getTreatmentName(){
        return this.treatmentName;
    }
    
    public void setTreatmentPrice(double price){
        this.treatmentPrice = price;
    }
    
    public double getTreatmentPrice(){
        return this.treatmentPrice;
    }
    
}
