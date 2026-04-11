package cz.muni.fi.pa165.productionservice.data.model;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class GrapeComponent {
    private String grapeType;
    private double kilogramsPerLiter;
    private int preferredQuality;

    public GrapeComponent() {
    }

    public GrapeComponent(String grapeType, double kilogramsPerLiter, int preferredQuality) {
        this.grapeType = grapeType;
        this.kilogramsPerLiter = kilogramsPerLiter;
        this.preferredQuality = preferredQuality;
    }

    public String getGrapeType() {
        return grapeType;
    }

    public void setGrapeType(String grapeType) {
        this.grapeType = grapeType;
    }

    public double getKilogramsPerLiter() {
        return kilogramsPerLiter;
    }

    public void setKilogramsPerLiter(double kilogramsPerLiter) {
        this.kilogramsPerLiter = kilogramsPerLiter;
    }

    public int getPreferredQuality() {
        return preferredQuality;
    }

    public void setPreferredQuality(int preferredQuality) {
        this.preferredQuality = preferredQuality;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GrapeComponent that = (GrapeComponent) o;
        return Double.compare(kilogramsPerLiter, that.kilogramsPerLiter) == 0 && preferredQuality == that.preferredQuality && Objects.equals(grapeType, that.grapeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grapeType, kilogramsPerLiter, preferredQuality);
    }

    @Override
    public String toString() {
        return "GrapeComponent{" +
            "grapeType='" + grapeType + '\'' +
            ", kilogramsPerLiter=" + kilogramsPerLiter +
            ", preferredQuality=" + preferredQuality +
            '}';
    }
}
