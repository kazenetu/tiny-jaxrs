package web.entity;

public class PasswordChangeEntity {
    private String id;
    private String password;
    private String newPassword;

    public PasswordChangeEntity(){

    }

    public PasswordChangeEntity(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
