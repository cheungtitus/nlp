package com.kenrui.nlp.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ItemSubmitted {
    protected String text;
    protected String postedBy;
    protected Boolean isComplaint;
    protected ArrayList<TaxonomyItem> details;

    @JsonCreator
    public ItemSubmitted(@JsonProperty(value="text", required = true) String text,
                         @JsonProperty(value="postedBy", required = true) String postedBy,
                         @JsonProperty(value="isComplaint", required = true) Boolean isComplaint,
                         @JsonProperty("details") ArrayList<TaxonomyItem> details) {
        this.text = text;
        this.postedBy = postedBy;
        this.isComplaint = isComplaint;
        this.details = details;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public Boolean getComplaint() {
        return isComplaint;
    }

    public void setComplaint(Boolean isComplaint) {
        this.isComplaint = isComplaint;
    }

    public List<TaxonomyItem> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<TaxonomyItem> details) {
        this.details = details;
    }
}
