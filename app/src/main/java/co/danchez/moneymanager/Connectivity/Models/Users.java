package co.danchez.moneymanager.Connectivity.Models;

import com.google.gson.annotations.SerializedName;

public class Users {

    @SerializedName("EMAIL_USER")
    private String email;
    @SerializedName("ID_TEAM_USER")
    private String idTeam;
    @SerializedName("NAME_USER")
    private String name;
    @SerializedName("PHOTO_USER")
    private String photo;
    @SerializedName("UID_USER")
    private String uid;
    private String idUser;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(String idTeam) {
        this.idTeam = idTeam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
