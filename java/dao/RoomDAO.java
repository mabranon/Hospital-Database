package dao;

import beans.RoomBean;
import java.util.List;

public interface RoomDAO {
    public List<RoomBean> getAllFreeRooms();
    public int insertRoom(RoomBean room);
    public RoomBean queryRoom(String id);
}
