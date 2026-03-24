package cz.muni.fi.pa165.harvestservice.api.dto;

public class HarvestSummaryDto {

    private String groupByValue;
    private double totalQuantity;

    public HarvestSummaryDto() {
    }

    public HarvestSummaryDto(String groupByValue, double totalQuantity) {
        this.groupByValue = groupByValue;
        this.totalQuantity = totalQuantity;
    }

    public String getGroupByValue() {
        return groupByValue;
    }

    public void setGroupByValue(String groupByValue) {
        this.groupByValue = groupByValue;
    }

    public double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
