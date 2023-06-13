public class row_treatment {
    row_treatment(String treatment_name, String id, String inventory, String reason){
        this.treatment_name = treatment_name;
        this.id = id;
        this.inventory = inventory;
        this.reason = reason;
    }
    public String treatment_name;
    public String id;
    public String inventory;

    public String getTreatment_name() {
        return treatment_name;
    }

    public void setTreatment_name(String treatment_name) {
        this.treatment_name = treatment_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String reason;

}