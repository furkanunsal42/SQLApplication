public class row_animals_treatment {
    row_animals_treatment(String animal_name, String animal_age, String treatment_name){
        this.animal_name = animal_name;
        this.animal_age = animal_age;
        this.treatment_name = treatment_name;
    }

    public String getAnimal_name() {
        return animal_name;
    }

    public void setAnimal_name(String animal_name) {
        this.animal_name = animal_name;
    }

    public String getAnimal_age() {
        return animal_age;
    }

    public void setAnimal_age(String animal_age) {
        this.animal_age = animal_age;
    }

    public String getTreatment_name() {
        return treatment_name;
    }

    public void setTreatment_name(String treatment_name) {
        this.treatment_name = treatment_name;
    }

    public String animal_name;
    public String animal_age;
    public String treatment_name;
}
