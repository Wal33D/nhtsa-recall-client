package io.github.nhtsarecalls;

import java.util.Map;
import java.util.Objects;

/**
 * Immutable value object representing a single NHTSA recall record.
 */
public final class RecallRecord {

    private final String reportReceivedDate;
    private final String manufacturer;
    private final String component;
    private final String summary;
    private final String consequence;
    private final String remedy;
    private final String notes;
    private final String nhtsaCampaignNumber;
    private final String recallNumber;
    private final String modelYear;
    private final String make;
    private final String model;
    private final String vin;
    private final Map<String, Object> raw;

    public RecallRecord(
            String reportReceivedDate,
            String manufacturer,
            String component,
            String summary,
            String consequence,
            String remedy,
            String notes,
            String nhtsaCampaignNumber,
            String recallNumber,
            String modelYear,
            String make,
            String model,
            String vin,
            Map<String, Object> raw) {
        this.reportReceivedDate = reportReceivedDate;
        this.manufacturer = manufacturer;
        this.component = component;
        this.summary = summary;
        this.consequence = consequence;
        this.remedy = remedy;
        this.notes = notes;
        this.nhtsaCampaignNumber = nhtsaCampaignNumber;
        this.recallNumber = recallNumber;
        this.modelYear = modelYear;
        this.make = make;
        this.model = model;
        this.vin = vin;
        this.raw = raw;
    }

    public String getReportReceivedDate() {
        return reportReceivedDate;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getComponent() {
        return component;
    }

    public String getSummary() {
        return summary;
    }

    public String getConsequence() {
        return consequence;
    }

    public String getRemedy() {
        return remedy;
    }

    public String getNotes() {
        return notes;
    }

    public String getNhtsaCampaignNumber() {
        return nhtsaCampaignNumber;
    }

    public String getRecallNumber() {
        return recallNumber;
    }

    public String getModelYear() {
        return modelYear;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getVin() {
        return vin;
    }

    public Map<String, Object> getRaw() {
        return raw;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nhtsaCampaignNumber, vin);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RecallRecord)) {
            return false;
        }
        RecallRecord other = (RecallRecord) obj;
        return Objects.equals(this.nhtsaCampaignNumber, other.nhtsaCampaignNumber)
                && Objects.equals(this.vin, other.vin);
    }

    @Override
    public String toString() {
        return "RecallRecord{" +
                "campaignNumber='" + nhtsaCampaignNumber + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", modelYear='" + modelYear + '\'' +
                '}';
    }
}
