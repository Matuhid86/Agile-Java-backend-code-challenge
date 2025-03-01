package com.agiletv.user.context.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum Gender {
    FEMALE("female"),
    MALE("male"),
    OTHER("other");

    private final String name;

    Gender(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static Gender fromName(final String name) {
        if (!StringUtils.isEmpty(name)) {
            for (final Gender enumEntry : values()) {
                if (enumEntry.getName().equalsIgnoreCase(name)) {
                    return enumEntry;
                }
            }
        }
        throw new IllegalArgumentException(String.format("No gender with [%s] name", name));
    }
}
