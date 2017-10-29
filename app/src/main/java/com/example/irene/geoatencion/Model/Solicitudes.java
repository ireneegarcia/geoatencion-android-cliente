package com.example.irene.geoatencion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irene on 29/8/2017.
 */

public class Solicitudes {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("organism")
    @Expose
    private String organism;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("iconUrl")
    @Expose
    private String iconUrl;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("question4")
    @Expose
    private String question4;
    @SerializedName("question3")
    @Expose
    private String question3;
    @SerializedName("question2")
    @Expose
    private String question2;
    @SerializedName("question1")
    @Expose
    private String question1;
    @SerializedName("category")
    @Expose
    private String category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuestion4() {
        return question4;
    }

    public void setQuestion4(String question4) {
        this.question4 = question4;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Solicitudes{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", organism='" + organism + '\'' +
                ", v=" + v +
                ", created='" + created + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", status='" + status + '\'' +
                ", question4='" + question4 + '\'' +
                ", question3='" + question3 + '\'' +
                ", question2='" + question2 + '\'' +
                ", question1='" + question1 + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
