package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user")
public class User implements Serializable {
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "realname", nullable = false)
    private String realname;

    @Column(name = "gender", nullable = false)
    private Byte gender;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "iconimg", nullable = false)
    private String iconimg;

    @Column(name = "info", nullable = false)
    private String info;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "is_vip", nullable = false)
    private Boolean isVip;

    @Column(name = "memory_size", nullable = false)
    private Long memorySize;

    @Column(name = "used_size", nullable = false)
    private Long usedSize;

    @Column(name = "private_status", nullable = false)
    private Boolean privateStatus;

    @Column(name = "private_pass", nullable = false)
    private String privatePass;

    @Column(name = "created_time", nullable = false)
    private Date createdTime;

    @Column(name = "last_login", nullable = false)
    private Date lastLogin;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getIconimg() {
        return iconimg;
    }

    public void setIconimg(String iconimg) {
        this.iconimg = iconimg == null ? null : iconimg.trim();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getIsVip() {
        return isVip;
    }

    public void setIsVip(Boolean isVip) {
        this.isVip = isVip;
    }

    public Long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(Long memorySize) {
        this.memorySize = memorySize;
    }

    public Long getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(Long usedSize) {
        this.usedSize = usedSize;
    }

    public Boolean getPrivateStatus() {
        return privateStatus;
    }

    public void setPrivateStatus(Boolean privateStatus) {
        this.privateStatus = privateStatus;
    }

    public String getPrivatePass() {
        return privatePass;
    }

    public void setPrivatePass(String privatePass) {
        this.privatePass = privatePass == null ? null : privatePass.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", realname='" + realname + '\'' +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", iconimg='" + iconimg + '\'' +
                ", info='" + info + '\'' +
                ", level=" + level +
                ", isVip=" + isVip +
                ", memorySize=" + memorySize +
                ", usedSize=" + usedSize +
                ", privateStatus=" + privateStatus +
                ", privatePass='" + privatePass + '\'' +
                ", createdTime=" + createdTime +
                ", lastLogin=" + lastLogin +
                '}';
    }
}