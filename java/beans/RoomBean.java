package beans;

import dao.RoomDAO;
import dao.RoomJDBCDAO;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

@ManagedBean
public class RoomBean {
    private RoomDAO roomDAO;
    
    private String roomNo;
    private String price;
    
    @PostConstruct
    public void init(){
        this.roomDAO = new RoomJDBCDAO();
    }
    
    public void setRoomNo(String no){
        this.roomNo = no;
    }
    
    public String getRoomNo(){
        return this.roomNo;
    }
    
    public void setPrice(String price){
        this.price = price;
    }
    
    public String getPrice(){
        return this.price;
    }
    
    public String insertRoom(){
        if(this.roomDAO.insertRoom(this) > 0){
            return "room_insert_success.xhtml?faces-redirect=true";
        }else{
            return "room_insert_fail.xhtml?faces-redirect=true";
        }
    }
}
