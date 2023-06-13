public class row_visitor {
    row_visitor(String name, String contact){
        this.setVisitor_name(name);
        this.setVisitor_contact(contact);
    }
    public String visitor_name;
    public  String visitor_contact;

    public String getVisitor_name() {
        return visitor_name;
    }

    public void setVisitor_name(String visitor_name) {
        this.visitor_name = visitor_name;
    }

    public String getVisitor_contact() {
        return visitor_contact;
    }

    public void setVisitor_contact(String visitor_contact) {
        this.visitor_contact = visitor_contact;
    }
}