package dao;

import beans.RoomBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.DBConnector;

public class RoomJDBCDAO implements RoomDAO {

    @Override
    public List<RoomBean> getAllFreeRooms() {
        Connection connection;
        PreparedStatement statement;
        ResultSet qResult;
        List<RoomBean> roomList = new ArrayList<>();

        try {
            connection = DBConnector.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Room "
                    + "WHERE Room.RoomNo NOT IN "
                    + "(SELECT Admission.RoomNo "
                    + "FROM Admission "
                    + "WHERE Admission.DischargeDate IS NULL)");
            qResult = statement.executeQuery();

            while (qResult.next()) {
                RoomBean room = new RoomBean();
                room.setRoomNo(qResult.getString("RoomNo"));
                room.setPrice(qResult.getString("Price"));
                roomList.add(room);
            }

            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return roomList;
    }

    @Override
    public int insertRoom(RoomBean room) {
        Connection connection;
        PreparedStatement statement;
        int roomsInserted = 0;

        try {
            connection = DBConnector.getConnection();
            statement = connection.
                    prepareStatement("INSERT INTO "
                            + "Room (RoomNo, Price) "
                            + "VALUES (?, ?)");
            statement.setString(1, room.getRoomNo());
            statement.setString(2, room.getPrice());
            roomsInserted = statement.executeUpdate();
            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return roomsInserted;
    }

    @Override
    public RoomBean queryRoom(String id) {
        Connection connection;
        PreparedStatement statement;
        ResultSet qResult;
        RoomBean room = new RoomBean();
        try {
            connection = DBConnector.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Room "
                    + "WHERE RoomNo = ?");
            statement.setString(1, id);
            qResult = statement.executeQuery();
            qResult.next();

            room.setRoomNo(qResult.getString("RoomNo"));
            room.setPrice(qResult.getString("Price"));

            statement.close();
            DBConnector.close(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return room;

    }
}
