package cmpg.photoshare.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Image {

    @Id
    private String path;

    @Column(name = "title")
    private String fileName;

    @Column(name = "geolocation")
    private String GeoLocation;

    @Column(name = "capturedDate")
    private LocalDate capturedDate;

    @Column(name = "capturedBy")
    private String capturedBy;

    @Column(name = "tags")
    private String tags;

    @Column(name = "parent")
    private String imageParent;

    @Column(name = "isImage")
    private String isImage;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getGeoLocation() {
        return GeoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        GeoLocation = geoLocation;
    }

    public LocalDate getCapturedDate() {
        return capturedDate;
    }

    public void setCapturedDate(LocalDate capturedDate) {
        this.capturedDate = capturedDate;
    }

    public String getCapturedBy() {
        return capturedBy;
    }

    public void setCapturedBy(String capturedBy) {
        this.capturedBy = capturedBy;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImageParent() {
        return imageParent;
    }

    public void setImageParent(String imageParent) {
        this.imageParent = imageParent;
    }

    public String getIsImage() {
        return isImage;
    }

    public void setIsImage(String isImage) {
        this.isImage = isImage;
    }
}
