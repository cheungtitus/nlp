package com.kenrui.nlp.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;



public class TaxonomyItem {
    protected Long id;
    protected Long selectedItemId;

    @JsonCreator
    public TaxonomyItem(@JsonProperty("id") Long id,
                        @JsonProperty("selectedItemId") Long selectedItemId) {
        this.id = id;
        this.selectedItemId = selectedItemId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSelectedItemId() {
        return selectedItemId;
    }

    public void setSelectedItemId(Long selectedItemId) {
        this.selectedItemId = selectedItemId;
    }
}
