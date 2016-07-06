package dao;

import beans.DoctorBean;
import java.util.List;

public interface DoctorDAO {
    public List<DoctorBean> getAllDoctors();
    public DoctorBean queryDoctor(String id);
}
