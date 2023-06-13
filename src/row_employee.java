public class row_employee {

    public row_employee(String employee_salary, String employee_contact, String employee_id, String employee_working_hours, String employee_name, String employee_starting_date) {
        this.employee_salary = employee_salary;
        this.employee_contact = employee_contact;
        this.employee_id = employee_id;
        this.employee_working_hours = employee_working_hours;
        this.employee_name = employee_name;
        this.employee_starting_date = employee_starting_date;
    }
    public String employee_salary;
    public String employee_contact;
    public String employee_id;
    public String employee_working_hours;
    public String employee_name;

    public String getEmployee_salary() {
        return employee_salary;
    }

    public void setEmployee_salary(String employee_salary) {
        this.employee_salary = employee_salary;
    }

    public String getEmployee_contact() {
        return employee_contact;
    }

    public void setEmployee_contact(String employee_contact) {
        this.employee_contact = employee_contact;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_working_hours() {
        return employee_working_hours;
    }

    public void setEmployee_working_hours(String employee_working_hours) {
        this.employee_working_hours = employee_working_hours;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getEmployee_starting_date() {
        return employee_starting_date;
    }

    public void setEmployee_starting_date(String employee_starting_date) {
        this.employee_starting_date = employee_starting_date;
    }

    public String employee_starting_date;
}
