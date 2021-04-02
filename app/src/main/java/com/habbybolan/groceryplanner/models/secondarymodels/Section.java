package com.habbybolan.groceryplanner.models.secondarymodels;

import com.habbybolan.groceryplanner.database.entities.SectionEntity;

public class Section {
    private String name;

    public Section(String name) {
        this.name = name;
    }

    public Section(SectionEntity sectionEntity) {
        name = sectionEntity.getName();
    }

    public String getName() {
        return name;
    }
}
