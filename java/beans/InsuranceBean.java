
package beans;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class InsuranceBean {
    
    private String cmpnyName;
    private double payInfo;
    
    public void setCmpnyName(String name){
        this.cmpnyName = name;
    }
    
    public String getCmpnyName(){
        return this.cmpnyName;
    }
    
    public void setPayInfo(double pInfo){
        this.payInfo = pInfo;
    }
    
    public double getPayInfo(){
        return this.payInfo;
    }
    
}
