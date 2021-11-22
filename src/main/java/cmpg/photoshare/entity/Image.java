package cmpg.photoshare.entity;

import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Image {

    @Id
    private String path;

    @Column(name = "title")
    private String title;

    @Column(name="fileName")
    private String fileName;

    @Column(name = "geolocation")
    private String geolocation;

    @Column(name = "capturedDate")
    private String capturedDate;

    @Column(name = "capturedBy")
    private String capturedBy;

    @Column(name = "tags")
    private String tags;

    @Column(name = "parent")
    private String imageParent;

    @Column(name = "isImage")
    private String isImage;

    public Image(String path, String title, String fileName, String geolocation, String capturedDate, String capturedBy, String tags, String imageParent, String isImage) {
        this.path = path;
        this.title = title;
        this.fileName = fileName;
        this.geolocation = geolocation;
        this.capturedDate = capturedDate;
        this.capturedBy = capturedBy;
        this.tags = tags;
        this.imageParent = imageParent;
        this.isImage = isImage;
    }

    public Image() {

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGeoLocation() {
        return geolocation;
    }

    public void setGeoLocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public String getCapturedDate() {
        return capturedDate;
    }

    public void setCapturedDate(String capturedDate) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
