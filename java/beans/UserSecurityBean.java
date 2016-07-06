package beans;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import util.DBConnector;

/**
 * Bean for page which creates a new user. User input password is salted and
 * hashed (SHA256) and stored to DB.
 *
 * @author AppleGrocer
 */
@ManagedBean
@SessionScoped
public class UserSecurityBean implements Serializable {

    private static final long serialVersionUID = 1L;

    //name and pass for user creation
    private String username;
    private String password;

    /**
     * Getters and setters for pass/username creation
     */
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    /**
     * creates unique salt for inputted username and hashes password. Salt and
     * hashed PW are saved in DB with plain-text username
     */
    public void uploadUser() {
        Connection connection;
        PreparedStatement statement;

        try {
            byte[] salt = genSalt();
            String passHash = SHA256Hash(this.password, salt);
            connection = DBConnector.getConnection();
            statement = connection.
                    prepareStatement("INSERT INTO Users "
                            + "(username, password, salt) "
                            + "VALUES ( ?, ?, ? )");
            statement.setString(1, getUsername());
            statement.setString(2, passHash);
            statement.setBytes(3, salt);
            statement.executeUpdate();
            statement.close();
            DBConnector.close(connection);
        } catch (NoSuchAlgorithmException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public String validateUsernamePassword() {
        Connection connection;
        PreparedStatement statement;
        boolean auth_result = false;

        try {
            connection = DBConnector.getConnection();
            statement = connection.
                    prepareStatement("SELECT Password, Salt "
                            + "FROM Users "
                            + "WHERE Username=?");
            statement.setString(1, getUsername());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                byte[] salt = result.getBytes(2);
                String passHash = SHA256Hash(this.password, salt);
                if (result.getString(1).equals(passHash)) {
                    auth_result = true;
                }
            }

            statement.close();
            DBConnector.close(connection);
        } catch (NoSuchAlgorithmException | SQLException e) {
            System.err.println(e.getMessage());
        }
        if (auth_result) {
            setSessionUserName(getUsername());
            return "search_patients.xhtml?faces-redirect=true";
        }
        logout();
        return "user_login.xhtml";
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/user_login.xhtml?faces-redirect=true";
    }

    public String getSessionUserName() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return session.getAttribute("username").toString();
    }

    public void setSessionUserName(String username) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.setAttribute("username", username);
    }

    /**
     * generates random salt for password creation
     *
     * @return base64 encoded string to append to password pre-hash
     * @throws NoSuchAlgorithmException
     */
    private byte[] genSalt() throws NoSuchAlgorithmException {
        SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");

        byte[] salt = new byte[16];
        rand.nextBytes(salt);
        return salt;
    }

    /**
     * Main PW hash function
     *
     * @param pass user plain-text password
     * @param salt unique randomly generated salt
     * @return hashed password+salt
     * @throws NoSuchAlgorithmException
     */
    private String SHA256Hash(String pass, byte[] salt)
            throws NoSuchAlgorithmException {
        MessageDigest md;
        byte[] bytePass = pass.getBytes();
        byte[] byteData;

        md = MessageDigest.getInstance("SHA-256");
        md.update(bytePass);
        md.update(salt);
        byteData = md.digest();

        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
