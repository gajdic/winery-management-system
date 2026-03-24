package cz.muni.fi.pa165.productionservice.data.model;

public class GrapeComponent {
    private String grapeType;
    private double kilogramsPerLiter;
    private int preferredQuality;

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
}
