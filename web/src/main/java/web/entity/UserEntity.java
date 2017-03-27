package web.entity;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class UserEntity {
    private String id;
    private String name;
    private String password;
    private int age;
    private Date date;
    private Time time;
    private Timestamp ts;


    public UserEntity() {
    }

    public UserEntity(String id, String name, String password, int age) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date セットする date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return time
     */
    public Time getTime() {
        return time;
    }

    /**
     * @param time セットする time
     */
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * @return ts
     */
    public Timestamp getTs() {
        return ts;
    }

    /**
     * @param ts セットする ts
     */
    public void setTs(Timestamp ts) {
       this.ts = ts;
    }

}
