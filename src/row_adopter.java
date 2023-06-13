public class row_adopter {
    row_adopter(String adopter_name, String adopter_age, String adopter_contact, String animal_name, String animal_id){
        this.adopter_name = adopter_name;
        this.adopter_age = adopter_age;
        this.adopter_contact = adopter_contact;
        this.animal_name = animal_name;
        this.animal_id = animal_id;
    }

    public String getAdopter_name() {
        return adopter_name;
    }

    public void setAdopter_name(String adopter_name) {
        this.adopter_name = adopter_name;
    }

    public String getAdopter_age() {
        return adopter_age;
    }

    public void setAdopter_age(String adopter_age) {
        this.adopter_age = adopter_age;
    }

    public String getAdopter_contact() {
        return adopter_contact;
    }

    public void setAdopter_contact(String adopter_contact) {
        this.adopter_contact = adopter_contact;
    }

    public String getAnimal_name() {
        return animal_name;
    }

    public void setAnimal_name(String animal_name) {
        this.animal_name = animal_name;
    }

    public String getAnimal_id() {
        return animal_id;
    }

    public void setAnimal_id(String animal_id) {
        this.animal_id = animal_id;
    }

    public String adopter_name;
    public String adopter_age;
    public String adopter_contact;
    public String animal_name;
    public String animal_id;
}