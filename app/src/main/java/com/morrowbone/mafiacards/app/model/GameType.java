package com.morrowbone.mafiacards.app.model;

/**
 * Created by morrow on 03.06.2014.
 */
public class GameType {
    private Integer imageId;
    private Integer titleId;
    private Integer descriptionId;

    public GameType(Integer imageId, Integer titleId, Integer descriptionId) {
        this.imageId = imageId;
        this.titleId = titleId;
        this.descriptionId = descriptionId;
    }

    public GameType(){

    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }

    public Integer getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(Integer descriptionId) {
        this.descriptionId = descriptionId;
    }
}
