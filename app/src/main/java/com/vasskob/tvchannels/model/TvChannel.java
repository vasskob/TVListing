
package com.vasskob.tvchannels.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TvChannel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(url).append(picture).append(categoryId).toHashCode();
    }

//    @Override
//    public boolean equals(Object other) {
//        if (other == this) {
//            return true;
//        }
//        if ((other instanceof TvChannel) == false) {
//            return false;
//        }
//        TvChannel rhs = ((TvChannel) other);
//        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).append(url, rhs.url).append(picture, rhs.picture).append(categoryId, rhs.categoryId).isEquals();
//    }

}
