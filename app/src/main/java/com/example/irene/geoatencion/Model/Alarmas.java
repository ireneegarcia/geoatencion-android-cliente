package com.example.irene.geoatencion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irene on 31/8/2017.
 */

public class Alarmas {

    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("categoryService")
    @Expose
    private String categoryService;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("organism")
    @Expose
    private String organism;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("network")
    @Expose
    private String network;

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(String categoryService) {
        this.categoryService = categoryService;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }


    @Override
    public String toString() {
        return "Alarmas{" +
                "_id='" + _id + '\'' +
                ", user=" + user +
                ", categoryService='" + categoryService + '\'' +
                ", status='" + status + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", address='" + address + '\'' +
                ", created='" + created + '\'' +
                ", rating='" + rating + '\'' +
                ", organism='" + organism + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
