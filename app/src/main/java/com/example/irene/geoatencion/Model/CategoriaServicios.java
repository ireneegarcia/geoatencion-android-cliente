package com.example.irene.geoatencion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irene on 19/8/2017.
 */

public class CategoriaServicios {


    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("iconUrl")
    @Expose
    private String iconUrl;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "CategoriaServicios{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", v=" + v +
                ", created='" + created + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

}
