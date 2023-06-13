public class row_shelter {

    public row_shelter(String shelter_name, String number_of_animals, String number_of_employees, String number_of_visitors) {
        this.shelter_name = shelter_name;
        this.number_of_animals = number_of_animals;
        this.number_of_employees = number_of_employees;
        this.number_of_visitors = number_of_visitors;
    }
    public String shelter_name;

    public String getShelter_name() {
        return shelter_name;
    }

    public void setShelter_name(String shelter_name) {
        this.shelter_name = shelter_name;
    }

    public String getNumber_of_animals() {
        return number_of_animals;
    }

    public void setNumber_of_animals(String number_of_animals) {
        this.number_of_animals = number_of_animals;
    }

    public String getNumber_of_employees() {
        return number_of_employees;
    }

    public void setNumber_of_employees(String number_of_employees) {
        this.number_of_employees = number_of_employees;
    }

    public String getNumber_of_visitors() {
        return number_of_visitors;
    }

    public void setNumber_of_visitors(String number_of_visitors) {
        this.number_of_visitors = number_of_visitors;
    }

    public String number_of_animals;
    public String number_of_employees;
    public String number_of_visitors;

}
