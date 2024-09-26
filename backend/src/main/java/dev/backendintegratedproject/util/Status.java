package dev.backendintegratedproject.util;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Converter;
import jakarta.persistence.EnumType;

public enum Status {
    NO_STATUS("No Status"), TO_DO("To do"), DOING("Doing"), DONE("Done");

    private String humanReadableStats;

    private Status(String humanReadableStats){
        this.humanReadableStats = humanReadableStats;
    }



}
