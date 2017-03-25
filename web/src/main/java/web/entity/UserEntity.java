package web.entity;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class UserEntity {
    private String id;
    private String name;
    private String password;
    private int age;
    private String date;
    private String time;
    private String ts;


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
    public String getDate() {
        return date;
    }

    /**
     * @param date セットする date
     */
    public void setDate(Date date) {
        this.date = null;
        if(date != null){
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            this.date = date.toLocalDate().format(f);
        }
    }

    /**
     * @return time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time セットする time
     */
    public void setTime(Time time) {
        if(time != null){
            this.time = time.toString();
        }
    }

    /**
     * @return ts
     */
    public String getTs() {
        return ts;
    }

    /**
     * @param ts セットする ts
     */
    public void setTs(Timestamp ts) {
        this.ts = null;
        if(ts != null){
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            this.ts =  ts.toLocalDateTime().format(f);
        }
    }

}
