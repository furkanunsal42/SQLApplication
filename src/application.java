import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.*;

class Database{
    public Connection conn = null;

    Database(String username, String password, String server_url) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        init_connection(username, password, server_url);
    }

    Database(String username, String password) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        init_connection(username, password, "jdbc:mysql://localhost/db");
    }

    private void init_connection(String username, String password, String server_url){
                try {
                    conn = DriverManager.getConnection(
                            server_url,username,password);

                    System.out.println("Connection Successful");
                    // Do something with the Connection

                } catch (SQLException ex) {
                    // handle any errors
                    System.out.println("Connection Failed");
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public ResultSet read_query(String commend_string){
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(commend_string);
        }
        catch (SQLException ex){
            // handle any errors
            System.out.println("query failed");
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return rs;
    }

    public int write_query(String commend_string){
        int result = 0;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            result = stmt.executeUpdate(commend_string);
        }
        catch (SQLException ex){
            // handle any errors
            System.out.println("query failed");
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return result;
    }
}

public class application extends Application {

    static Database database;
    static Scanner input;
    boolean should_halt = false;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        database = new Database("root", "Fu2227014");
        input = new Scanner(System.in);
        launch();
    }

    int screen_width = 1200;
    int screen_height = 800;

    int selectors_padding = 50;
    int selectors_height  = 30;
    int selectors_seperation = 10;
    float selector_persentage = 0.75f;
    int selectors_y = 20;
    String[] object_selector_names = {"Visitors", "Treatments", "Adopters", "Animals", "Employees", "Shelter"};

    ArrayList<Node> dynamic_objets = new ArrayList<>();

    ComboBox<String> object_selector;
    ComboBox<String> operation_selector;

    Label result_label = new Label("");

    @Override
    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        Scene scene = new Scene(root, screen_width, screen_height);

        // object selector
        object_selector = new ComboBox<>(FXCollections.observableArrayList(object_selector_names));
        object_selector.getSelectionModel().selectFirst();
        object_selector.setTranslateX(selectors_padding);
        object_selector.setTranslateY(selectors_y);
        object_selector.setPrefSize((screen_width - 2 * selectors_padding) * selector_persentage -  selectors_seperation/2, selectors_height);
        root.getChildren().add(object_selector);

        HashMap<String, ArrayList <String>> object_operations_table = new HashMap<>();
        object_operations_table.put("Visitors", new ArrayList <String>(Arrays.asList(
                "Insert", "Delete")));
        object_operations_table.put("Treatments", new ArrayList <String>(Arrays.asList(
                "Insert", "Delete", "Use")));
        object_operations_table.put("Adopters", new ArrayList <String>(Arrays.asList(
                "Display")));
        object_operations_table.put("Animals", new ArrayList <String>(Arrays.asList(
                "Apply Treatment", "Adopt", "Display Treatments", "Display Adoptable", "Display All")));
        object_operations_table.put("Employees", new ArrayList <String>(Arrays.asList(
                "Delete", "Change Salary")));
        object_operations_table.put("Shelter", new ArrayList <String>(Arrays.asList(
                "Change Name")));

        // operation selector
        operation_selector = new ComboBox();
        operation_selector.setItems(FXCollections.observableArrayList(object_operations_table.get(object_selector.getValue().toString())));
        operation_selector.getSelectionModel().selectFirst();
        operation_selector.setPrefSize((screen_width - 2 * selectors_padding) * (1-selector_persentage) - selectors_seperation/2, selectors_height);
        operation_selector.setTranslateX((screen_width - 2 * selectors_padding) * (selector_persentage) + selectors_padding);
        operation_selector.setTranslateY(selectors_y);
        root.getChildren().add(operation_selector);

        root.getChildren().add(result_label);

        // update functions
        object_selector.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                operation_selector.setItems(FXCollections.observableArrayList(object_operations_table.get(object_selector.getValue().toString())));
                operation_selector.getSelectionModel().selectFirst();
                operation_selector.fireEvent(new ActionEvent());
            }
        });

        operation_selector.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if ((operation_selector.getValue() == null))
                    return;

                switch (object_selector.getValue().toString()){
                    case "Visitors" -> {
                        switch (operation_selector.getValue().toString()) {
                            case "Insert" -> {try { layout_visitor_insert(root);} catch (SQLException e) { e.printStackTrace(); }}
                            case "Delete" -> {try { layout_visitor_delete(root);} catch (SQLException e) { e.printStackTrace(); }}
                        }
                    }
                    case "Treatments" -> {
                        switch (operation_selector.getValue().toString()) {
                            case "Insert" -> {try { layout_treatment_insert(root);} catch (SQLException e) { e.printStackTrace(); }}
                            case "Delete" -> {try { layout_treatment_delete(root);} catch (SQLException e) { e.printStackTrace(); }}
                            case "Use" -> {try { layout_treatment_use(root);} catch (SQLException e) { e.printStackTrace(); }}
                            //case "Display Cats" -> {try { layout_treatment_use(root);} catch (SQLException e) { e.printStackTrace(); }}
                            //case "Display Dogs" -> {try { layout_treatment_use(root);} catch (SQLException e) { e.printStackTrace(); }}
                            //case "Display All" -> {try { layout_treatment_use(root);} catch (SQLException e) { e.printStackTrace(); }}
                        }
                    }
                    case "Adopters" -> {
                        switch (operation_selector.getValue().toString()) {
                            case "Display" -> {try { layout_adopters_display(root);} catch (SQLException e) { e.printStackTrace(); }}
                        }
                    }
                    case "Animals" -> {
                        switch (operation_selector.getValue().toString()) {
                            case "Apply Treatment" -> {try { layout_animals_apply_treatment(root);} catch (SQLException e) { e.printStackTrace(); }}
                            case "Adopt" -> {try { layout_animals_adopt(root);} catch (SQLException e) { e.printStackTrace(); }}
                            case "Display Treatments" -> {try { layout_animals_display_treatment(root);} catch (SQLException e) { e.printStackTrace(); }}
                            case "Display Adoptable" -> {try { layout_animals_display_adoptable(root);} catch (SQLException e) { e.printStackTrace(); }}
                            case "Display All" -> {try { layout_animals_display_all(root);} catch (SQLException e) { e.printStackTrace(); }}
                        }
                    }
                    case "Employees" -> {
                        switch (operation_selector.getValue().toString()) {
                            case "Delete" -> {try { layout_employee_delete(root);} catch (SQLException e) { e.printStackTrace(); }}
                            case "Change Salary" -> {try { layout_employee_change_salary(root);} catch (SQLException e) { e.printStackTrace(); }}
                        }
                    }
                    case "Shelter" -> {
                        switch (operation_selector.getValue().toString()) {
                            case "Change Name" -> {try { layout_shelter_change_name(root);} catch (SQLException e) { e.printStackTrace(); }}
                        }
                    }


                }
            }
        });

        object_selector.fireEvent(new ActionEvent());
        stage.setScene(scene);
        stage.show();
    }

    int table_height = 500;

    public void layout_visitor_insert(Pane root) throws SQLException {
        root.getChildren().removeAll(dynamic_objets);

        String[] input_parameter_names = new String[]{"Visitor Name", "Visitor Contact"};

        String[] table_parameter_names = new String[]{"Visitor Name", "Visitor Contact"};
        String[] table_variable_name = new String[]{"visitor_name", "visitor_contact"};

        TableView<row_visitor> table = new TableView<>();
        root.getChildren().add(table);
        dynamic_objets.add(table);

        int table_y = selectors_y + selectors_height + 20;
        table.setTranslateX(selectors_padding);
        table.setTranslateY(table_y);
        table.setPrefSize(screen_width - 2 * selectors_padding, table_height);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (String parameter : table_parameter_names){
            TableColumn<row_visitor, String> column = new TableColumn<row_visitor, String>(parameter);
            table.getColumns().add(column);
        }

        for (int i = 0; i < table_variable_name.length; i++){
            ((TableColumn<row_visitor, String>)table.getColumns().get(i)).setCellValueFactory(new PropertyValueFactory<row_visitor, String>(table_variable_name[i]));
        }

        ResultSet result = database.read_query(String.format("SELECT * FROM Visitors; "));
        while (result.next()) {
            table.getItems().add(new row_visitor(result.getString(1), result.getString(2)));
        }

        ArrayList<TextField> inputs = new ArrayList<>();
        int text_area_width = 100;
        int text_area_height = 10;
        int text_area_seperation = 5;
        int text_area_y = table_y + table_height + 10;
        for (int i = 0; i < input_parameter_names.length; i++){
            TextField text_field = new TextField();
            text_field.setPromptText(input_parameter_names[i]);
            text_field.setTranslateX(selectors_padding + i * (text_area_width + text_area_seperation));
            text_field.setTranslateY(text_area_y);
            text_field.setPrefSize(text_area_width, text_area_height);
            root.getChildren().add(text_field);
            dynamic_objets.add(text_field);
            inputs.add(text_field);
        }

        int button_width = 60;
        int button_height = 20;
        int button_y = text_area_y + text_area_height + 20;
        Button submit = new Button("Apply");
        submit.setTranslateX(selectors_padding);
        submit.setTranslateY(button_y);
        submit.setPrefSize(button_width, button_height);
        root.getChildren().add(submit);
        dynamic_objets.add(submit);

        int result_label_x = (selectors_padding + button_width + 10);
        int result_label_y = button_y + button_height/4;
        result_label.setTranslateX(result_label_x);
        result_label.setTranslateY(result_label_y);


        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String visitor_name = inputs.get(0).getText();
                String visitor_contact = inputs.get(1).getText();
                int result = database.write_query(String.format("INSERT INTO Visitors (Visitor_Name, Visitor_Contact) \n" +
                        "VALUES ('%s', %s); ", visitor_name, visitor_contact));
                result_label.setText(String.format("%d rows have changed", result));
                System.out.println(result);
                operation_selector.fireEvent(new ActionEvent());
            }
        });
    }

    public void layout_visitor_delete(Pane root) throws SQLException {
        root.getChildren().removeAll(dynamic_objets);

        String[] input_parameter_names = new String[]{"Visitor Name"};

        String[] table_parameter_names = new String[]{"Visitor Name", "Visitor Contact"};
        String[] table_variable_name = new String[]{"visitor_name", "visitor_contact"};

        TableView<row_visitor> table = new TableView<>();
        root.getChildren().add(table);
        dynamic_objets.add(table);

        int table_y = selectors_y + selectors_height + 20;
        table.setTranslateX(selectors_padding);
        table.setTranslateY(table_y);
        table.setPrefSize(screen_width - 2 * selectors_padding, table_height);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (String parameter : table_parameter_names){
            TableColumn<row_visitor, String> column = new TableColumn<row_visitor, String>(parameter);
            table.getColumns().add(column);
        }

        for (int i = 0; i < table_variable_name.length; i++){
            ((TableColumn<row_visitor, String>)table.getColumns().get(i)).setCellValueFactory(new PropertyValueFactory<row_visitor, String>(table_variable_name[i]));
        }

        ResultSet result = database.read_query(String.format("SELECT * FROM Visitors; "));
        while (result.next()) {
            table.getItems().add(new row_visitor(result.getString(1), result.getString(2)));
        }

        ArrayList<TextField> inputs = new ArrayList<>();
        int text_area_width = 100;
        int text_area_height = 10;
        int text_area_seperation = 5;
        int text_area_y = table_y + table_height + 10;
        for (int i = 0; i < input_parameter_names.length; i++){
            TextField text_field = new TextField();
            text_field.setPromptText(input_parameter_names[i]);
            text_field.setTranslateX(selectors_padding + i * (text_area_width + text_area_seperation));
            text_field.setTranslateY(text_area_y);
            text_field.setPrefSize(text_area_width, text_area_height);
            root.getChildren().add(text_field);
            dynamic_objets.add(text_field);
            inputs.add(text_field);
        }

        int button_width = 60;
        int button_height = 20;
        int button_y = text_area_y + text_area_height + 20;
        Button submit = new Button("Apply");
        submit.setTranslateX(selectors_padding);
        submit.setTranslateY(button_y);
        submit.setPrefSize(button_width, button_height);
        root.getChildren().add(submit);
        dynamic_objets.add(submit);

        int result_label_x = (selectors_padding + button_width + 10);
        int result_label_y = button_y + button_height/4;
        result_label.setTranslateX(result_label_x);
        result_label.setTranslateY(result_label_y);


        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String visitor_name = inputs.get(0).getText();
                int result = database.write_query(String.format("DELETE FROM Visitors WHERE Visitor_Name = '%s'; ", visitor_name));
                result_label.setText(String.format("%d rows have changed", result));
                System.out.println(result);
                operation_selector.fireEvent(new ActionEvent());
            }
        });
    }

    public void layout_treatment_insert(Pane root) throws SQLException {
        root.getChildren().removeAll(dynamic_objets);

        String table_name = "Treatments";
        String[] input_parameter_names = new String[]{"Treatment Name", "ID", "Inventory", "Reason"};

        String[] table_parameter_names = new String[]{"Treatment Name", "ID", "Inventory", "Reason"};
        String[] table_variable_name = new String[]{"treatment_name", "id", "inventory", "reason"};

        TableView<row_treatment> table = new TableView<>();
        root.getChildren().add(table);
        dynamic_objets.add(table);

        int table_y = selectors_y + selectors_height + 20;
        table.setTranslateX(selectors_padding);
        table.setTranslateY(table_y);
        table.setPrefSize(screen_width - 2 * selectors_padding, table_height);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (String parameter : table_parameter_names){
            TableColumn<row_treatment, String> column = new TableColumn<row_treatment, String>(parameter);
            table.getColumns().add(column);
        }

        for (int i = 0; i < table_variable_name.length; i++){
            ((TableColumn<row_treatment, String>)table.getColumns().get(i)).setCellValueFactory(new PropertyValueFactory<row_treatment, String>(table_variable_name[i]));
        }

        ResultSet result = database.read_query(String.format("SELECT Treatment_Name, Treatment_ID, Treatment_Inventory, Treatment_Reason FROM "+table_name));
        while (result.next()) {
            table.getItems().add(new row_treatment(result.getString(1), result.getString(2), result.getString(3), result.getString(4)));
        }

        ArrayList<TextField> inputs = new ArrayList<>();
        int text_area_width = 100;
        int text_area_height = 10;
        int text_area_seperation = 5;
        int text_area_y = table_y + table_height + 10;
        for (int i = 0; i < input_parameter_names.length; i++){
            TextField text_field = new TextField();
            text_field.setPromptText(input_parameter_names[i]);
            text_field.setTranslateX(selectors_padding + i * (text_area_width + text_area_seperation));
            text_field.setTranslateY(text_area_y);
            text_field.setPrefSize(text_area_width, text_area_height);
            root.getChildren().add(text_field);
            dynamic_objets.add(text_field);
            inputs.add(text_field);
        }

        int button_width = 60;
        int button_height = 20;
        int button_y = text_area_y + text_area_height + 20;
        Button submit = new Button("Apply");
        submit.setTranslateX(selectors_padding);
        submit.setTranslateY(button_y);
        submit.setPrefSize(button_width, button_height);
        root.getChildren().add(submit);
        dynamic_objets.add(submit);

        int result_label_x = (selectors_padding + button_width + 10);
        int result_label_y = button_y + button_height/4;
        result_label.setTranslateX(result_label_x);
        result_label.setTranslateY(result_label_y);


        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String treatment_name = inputs.get(0).getText();
                String id = inputs.get(1).getText();
                String inventory = inputs.get(2).getText();
                String reason = inputs.get(3).getText();
                int result = database.write_query(String.format("INSERT INTO Treatments(Treatment_Name, Treatment_ID, Treatment_Inventory, Treatment_Reason) Values(%s, %s, %s, %s);", treatment_name, id, inventory, reason));
                result_label.setText(String.format("%d rows have changed", result));
                System.out.println(result);
                operation_selector.fireEvent(new ActionEvent());
            }
        });
    }

    public void layout_treatment_delete(Pane root) throws SQLException {
        root.getChildren().removeAll(dynamic_objets);

        String table_name = "Treatments";
        String[] input_parameter_names = new String[]{"ID"};

        String[] table_parameter_names = new String[]{"Treatment Name", "ID", "Inventory", "Reason"};
        String[] table_variable_name = new String[]{"treatment_name", "id", "inventory", "reason"};

        TableView<row_treatment> table = new TableView<>();
        root.getChildren().add(table);
        dynamic_objets.add(table);

        int table_y = selectors_y + selectors_height + 20;
        table.setTranslateX(selectors_padding);
        table.setTranslateY(table_y);
        table.setPrefSize(screen_width - 2 * selectors_padding, table_height);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (String parameter : table_parameter_names){
            TableColumn<row_treatment, String> column = new TableColumn<row_treatment, String>(parameter);
            table.getColumns().add(column);
        }

        for (int i = 0; i < table_variable_name.length; i++){
            ((TableColumn<row_treatment, String>)table.getColumns().get(i)).setCellValueFactory(new PropertyValueFactory<row_treatment, String>(table_variable_name[i]));
        }

        ResultSet result = database.read_query(String.format("SELECT Treatment_Name, Treatment_ID, Treatment_Inventory, Treatment_Reason FROM "+table_name));
        while (result.next()) {
            table.getItems().add(new row_treatment(result.getString(1), result.getString(2), result.getString(3), result.getString(4)));
        }

        ArrayList<TextField> inputs = new ArrayList<>();
        int text_area_width = 100;
        int text_area_height = 10;
        int text_area_seperation = 5;
        int text_area_y = table_y + table_height + 10;
        for (int i = 0; i < input_parameter_names.length; i++){
            TextField text_field = new TextField();
            text_field.setPromptText(input_parameter_names[i]);
            text_field.setTranslateX(selectors_padding + i * (text_area_width + text_area_seperation));
            text_field.setTranslateY(text_area_y);
            text_field.setPrefSize(text_area_width, text_area_height);
            root.getChildren().add(text_field);
            dynamic_objets.add(text_field);
            inputs.add(text_field);
        }

        int button_width = 60;
        int button_height = 20;
        int button_y = text_area_y + text_area_height + 20;
        Button submit = new Button("Apply");
        submit.setTranslateX(selectors_padding);
        submit.setTranslateY(button_y);
        submit.setPrefSize(button_width, button_height);
        root.getChildren().add(submit);
        dynamic_objets.add(submit);

        int result_label_x = (selectors_padding + button_width + 10);
        int result_label_y = button_y + button_height/4;
        result_label.setTranslateX(result_label_x);
        result_label.setTranslateY(result_label_y);


        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String id = inputs.get(0).getText();
                int result = database.write_query(String.format("DELETE FROM Treatments WHERE Treatment_ID = %s;", id));
                result_label.setText(String.format("%d rows have changed", result));
                System.out.println(result);
                operation_selector.fireEvent(new ActionEvent());
            }
        });
    }

    public void layout_treatment_use(Pane root) throws SQLException {
        root.getChildren().removeAll(dynamic_objets);

        String table_name = "Treatments";
        String[] input_parameter_names = new String[]{"ID", "Amount"};

        String[] table_parameter_names = new String[]{"Treatment Name", "ID", "Inventory", "Reason"};
        String[] table_variable_name = new String[]{"treatment_name", "id", "inventory", "reason"};

        TableView<row_treatment> table = new TableView<>();
        root.getChildren().add(table);
        dynamic_objets.add(table);

        int table_y = selectors_y + selectors_height + 20;
        table.setTranslateX(selectors_padding);
        table.setTranslateY(table_y);
        table.setPrefSize(screen_width - 2 * selectors_padding, table_height);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (String parameter : table_parameter_names){
            TableColumn<row_treatment, String> column = new TableColumn<row_treatment, String>(parameter);
            table.getColumns().add(column);
        }

        for (int i = 0; i < table_variable_name.length; i++){
            ((TableColumn<row_treatment, String>)table.getColumns().get(i)).setCellValueFactory(new PropertyValueFactory<row_treatment, String>(table_variable_name[i]));
        }

        ResultSet result = database.read_query(String.format("SELECT Treatment_Name, Treatment_ID, Treatment_Inventory, Treatment_Reason FROM "+table_name));
        while (result.next()) {
            table.getItems().add(new row_treatment(result.getString(1), result.getString(2), result.getString(3), result.getString(4)));
        }

        ArrayList<TextField> inputs = new ArrayList<>();
        int text_area_width = 100;
        int text_area_height = 10;
        int text_area_seperation = 5;
        int text_area_y = table_y + table_height + 10;
        for (int i = 0; i < input_parameter_names.length; i++){
            TextField text_field = new TextField();
            text_field.setPromptText(input_parameter_names[i]);
            text_field.setTranslateX(selectors_padding + i * (text_area_width + text_area_seperation));
            text_field.setTranslateY(text_area_y);
            text_field.setPrefSize(text_area_width, text_area_height);
            root.getChildren().add(text_field);
            dynamic_objets.add(text_field);
            inputs.add(text_field);
        }

        int button_width = 60;
        int button_height = 20;
        int button_y = text_area_y + text_area_height + 20;
        Button submit = new Button("Apply");
        submit.setTranslateX(selectors_padding);
        submit.setTranslateY(button_y);
        submit.setPrefSize(button_width, button_height);
        root.getChildren().add(submit);
        dynamic_objets.add(submit);

        int result_label_x = (selectors_padding + button_width + 10);
        int result_label_y = button_y + button_height/4;
        result_label.setTranslateX(result_label_x);
        result_label.setTranslateY(result_label_y);


        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String id = inputs.get(0).getText();
                String amount = inputs.get(1).getText();
                int result = database.write_query(String.format("UPDATE Treatments SET Treatment_Inventory = Treatment_Inventory -%s WHERE Treatment_ID = %s;", amount, id));
                result_label.setText(String.format("%d rows have changed", result));
                System.out.println(result);
                operation_selector.fireEvent(new ActionEvent());
            }
        });
    }

    public void layout_adopters_display(Pane root) throws SQLException {
        root.getChildren().removeAll(dynamic_objets);

        String table_name = "Adopters";
        String[] input_parameter_names = new String[]{};

        String[] table_parameter_names = new String[]{"Adopter Name", "Adopter Age", "Adopter Contact", "Animal Name", "Animal ID"};
        String[] table_variable_name = new String[]{"adopter_name", "adopter_age", "adopter_contact", "animal_name", "animal_id"};

        TableView<row_adopter> table = new TableView<>();
        root.getChildren().add(table);
        dynamic_objets.add(table);

        int table_y = selectors_y + selectors_height + 20;
        table.setTranslateX(selectors_padding);
        table.setTranslateY(table_y);
        table.setPrefSize(screen_width - 2 * selectors_padding, table_height);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (String parameter : table_parameter_names){
            TableColumn<row_adopter, String> column = new TableColumn<row_adopter, String>(parameter);
            table.getColumns().add(column);
        }

        for (int i = 0; i < table_variable_name.length; i++){
            ((TableColumn<row_adopter, String>)table.getColumns().get(i)).setCellValueFactory(new PropertyValueFactory<row_adopter, String>(table_variable_name[i]));
        }

        ResultSet result = database.read_query(String.format("SELECT A.Adopter_Name, A.Adopter_Age, A.Adopter_Contact, AN.Animal_Name,AN.Animal_ID FROM Adopters A JOIN Animals AN ON A.Adopted_ID = AN.Animal_ID;"));
        while (result.next()) {
            table.getItems().add(new row_adopter(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5)));
        }
    }

    public void layout_animals_apply_treatment(Pane root) throws SQLException {
        root.getChildren().removeAll(dynamic_objets);

        String table_name = "Animals";
        String[] input_parameter_names = new String[]{"Animal ID", "Treatment ID"};

        String[] table_parameter_names = new String[]{"Animal Name", "Animal ID", "Treatment ID"};
        String[] table_variable_name = new String[]{"animal_name", "animal_age", "treatment_name"};

        TableView<row_animals_treatment> table = new TableView<>();
        root.getChildren().add(table);
        dynamic_objets.add(table);

        int table_y = selectors_y + selectors_height + 20;
        table.setTranslateX(selectors_padding);
        table.setTranslateY(table_y);
        table.setPrefSize(screen_width - 2 * selectors_padding, table_height);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (String parameter : table_parameter_names){
            TableColumn<row_animals_treatment, String> column = new TableColumn<row_animals_treatment, String>(parameter);
            table.getColumns().add(column);
        }

        for (int i = 0; i < table_variable_name.length; i++){
            ((TableColumn<row_animals_treatment, String>)table.getColumns().get(i)).setCellValueFactory(new PropertyValueFactory<row_animals_treatment, String>(table_variable_name[i]));
        }

        ResultSet result = database.read_query(String.format("SELECT Animal_Name, Animal_ID, Animal_Treatment_ID from %s;", table_name));
        while (result.next()) {
            table.getItems().add(new row_animals_treatment(result.getString(1), result.getString(2), result.getString(3)));
        }

        ArrayList<TextField> inputs = new ArrayList<>();
        int text_area_width = 88;
        int text_area_height = 10;
        int text_area_seperation = 0;
        int text_area_y = table_y + table_height + 10;
        for (int i = 0; i < input_parameter_names.length; i++){
            TextField text_field = new TextField();
            text_field.setPromptText(input_parameter_names[i]);
            text_field.setTranslateX(selectors_padding + i * (text_area_width + text_area_seperation));
            text_field.setTranslateY(text_area_y);
            text_field.setPrefSize(text_area_width, text_area_height);
            root.getChildren().add(text_field);
            dynamic_objets.add(text_field);
            inputs.add(text_field);
        }

        int button_width = 60;
        int button_height = 20;
        int button_y = text_area_y + text_area_height + 20;
        Button submit = new Button("Apply");
        submit.setTranslateX(selectors_padding);
        submit.setTranslateY(button_y);
        submit.setPrefSize(button_width, button_height);
        root.getChildren().add(submit);
        dynamic_objets.add(submit);

        int result_label_x = (selectors_padding + button_width + 10);
        int result_label_y = button_y + button_height/4;
        result_label.setTranslateX(result_label_x);
        result_label.setTranslateY(result_label_y);


        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String animal_id = inputs.get(0).getText();
                String treatment_id = inputs.get(1).getText();
                int result = database.write_query(String.format("UPDATE Animals SET Animal_Last_Treatment_Date = CURDATE(), Animal_Treatment_ID = %s WHERE Animal_ID = %s;", treatment_id, animal_id));
                result_label.setText(String.format("%d rows have changed", result));
                System.out.println(result);
                operation_selector.fireEvent(new ActionEvent());
            }
        });
    }

    public void layout_animals_adopt(Pane root) throws SQLException {
        root.getChildren().removeAll(dynamic_objets);

        String table_name = "Animals";
        String[] input_parameter_names = new String[]{"adopter_name", "adopter_id", "adopter_age", "adopter_contact", "date", "animal_name"};

        String[] table_parameter_names = new String[]{"Animal ID", "Animal Name", "Animal Breed", "Animal Age", "Animal Registration Date", "Animal Leaving Date", " Aimal Adoptable", "Animal Treatment ID", "Animal Last Treatment Date", "Animal Bringing Person ID"};
        String[] table_variable_names = new String[]{"animal_id", "animal_name", "animal_breed", "animal_age", "animal_registration_date", "animal_leaving_date", "animal_adoptable", "animal_treatment_id", "animal_last_treatment_date", "animal_bringing_person_id"};

        TableView<row_animals_all> table = new TableView<>();
        root.getChildren().add(table);
        dynamic_objets.add(table);

        int table_y = selectors_y + selectors_height + 20;
        table.setTranslateX(selectors_padding);
        table.setTranslateY(table_y);
        table.setPrefSize(screen_width - 2 * selectors_padding, table_height);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (String parameter : table_parameter_names){
            TableColumn<row_animals_all, String> column = new TableColumn<row_animals_all, String>(parameter);
            table.getColumns().add(column);
        }

        for (int i = 0; i < table_variable_names.length; i++){
            ((TableColumn<row_animals_all, String>)table.getColumns().get(i)).setCellValueFactory(new PropertyValueFactory<row_animals_all, String>(table_variable_names[i]));
        }

        ResultSet result = database.read_query(String.format("SELECT * FROM Animals Where Animal_Adoptable = 'yes';"));
        while (result.next()) {
            table.getItems().add(new row_animals_all(result.getString(1), result.getString(2), result.getString(3),result.getString(4),result.getString(5),result.getString(6),result.getString(7),result.getString(8),result.getString(9),result.getString(10)));
        }

        ArrayList<TextField> inputs = new ArrayList<>();
        int text_area_width = 100;
        int text_area_height = 10;
        int text_area_seperation = 10;
        int text_area_y = table_y + table_height + 10;
        for (int i = 0; i < input_parameter_names.length; i++){
            TextField text_field = new TextField();
            text_field.setPromptText(input_parameter_names[i]);
            text_field.setTranslateX(selectors_padding + i * (text_area_width + text_area_seperation));
            text_field.setTranslateY(text_area_y);
            text_field.setPrefSize(text_area_width, text_area_height);
            root.getChildren().add(text_field);
            dynamic_objets.add(text_field);
            inputs.add(text_field);
        }

        int button_width = 60;
        int button_height = 20;
        int button_y = text_area_y + text_area_height + 20;
        Button submit = new Button("Apply");
        submit.setTranslateX(selectors_padding);
        submit.setTranslateY(button_y);
        submit.setPrefSize(button_width, button_height);
        root.getChildren().add(submit);
        dynamic_objets.add(submit);

        int result_label_x = (selectors_padding + button_width + 10);
        int result_label_y = button_y + button_height/4;
        result_label.setTranslateX(result_label_x);
        result_label.setTranslateY(result_label_y);


        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String adopter_name = inputs.get(0).getText();
                String adopter_id = inputs.get(1).getText();
                String adopter_age = inputs.get(2).getText();
                String adopter_contact = inputs.get(3).getText();
                String date = inputs.get(4).getText();
                String animal_name = inputs.get(5).getText();
                int result = database.write_query(String.format("INSERT INTO Adopters (Adopter_Name, Adopter_ID, Adopter_Age, Adopter_Contact, Adopted_ID) \n" +
                        "VALUES ('John Doe', 123145627890, 30, 5551234567, (SELECT Animal_ID FROM Animals WHERE Animal_Name = 'Lucky')); \n" +
                        "UPDATE Animals \n" +
                        "SET Animal_Adoptable = 'No' \n" +
                        "WHERE Animal_Name = 'Lucky'; \n" +
                        "UPDATE Animals \n" +
                        "SET Animal_Leaving_Date = '2023-06-06' \n" +
                        "WHERE Animal_Name = ''; "));
                result_label.setText(String.format("%d rows have changed", result));
                System.out.println(result);
                operation_selector.fireEvent(new ActionEvent());
            }
        });
    }

    public void layout_animals_display_treatment(Pane root) throws SQLException {
        root.getChildren().removeAll(dynamic_objets);

        String table_name = "Animals";
        String[] input_parameter_names = new String[]{};

        String[] table_parameter_names = new String[]{"Animal Name", "Animal Age", "Treatment Name"};
        String[] table_variable_name = new String[]{"animal_name", "animal_age", "treatment_name"};

        TableView<row_animals_treatment> table = new TableView<>();
        root.getChildren().add(table);
        dynamic_objets.add(table);

        int table_y = selectors_y + selectors_height + 20;
        table.setTranslateX(selectors_padding);
        table.setTranslateY(table_y);
        table.setPrefSize(screen_width - 2 * selectors_padding, table_height);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (String parameter : table_parameter_names){
            TableColumn<row_animals_treatment, String> column = new TableColumn<row_animals_treatment, String>(parameter);
            table.getColumns().add(column);
        }

        for (int i = 0; i < table_variable_name.length; i++){
            ((TableColumn<row_animals_treatment, String>)table.getColumns().get(i)).setCellValueFactory(new PropertyValueFactory<row_animals_treatment, String>(table_variable_name[i]));
        }

        ResultSet result = database.read_query(String.format("SELECT AN.Animal_Name, AN.Animal_Age, T.Treatment_Name FROM Animals AN JOIN Treatments T ON AN.Animal_Treatment_ID = T.Treatment_ID WHERE AN.Animal_Adoptable = 'yes' GROUP BY AN.Animal_Name, AN.Animal_Age, T.Treatment_Name;"));
        while (result.next()) {
            table.getItems().add(new row_animals_treatment(result.getString(1), result.getString(2), result.getString(3)));
        }
    }

    public void layout_animals_display_adoptable(Pane root) throws SQLException {
        root.getChildren().removeAll(dynamic_objets);

        String table_name = "Animals";
        String[] input_parameter_names = new String[]{};

        String[] table_parameter_names = new String[]{"Animal ID", "Animal Name", "Animal Breed", "Animal Age", "Animal Registration Date", "Animal Leaving Date", " Aimal Adoptable", "Animal Treatment ID", "Animal Last Treatment Date", "Animal Bringing Person ID"};
        String[] table_variable_names = new String[]{"animal_id", "animal_name", "animal_breed", "animal_age", "animal_registration_date", "animal_leaving_date", "animal_adoptable", "animal_treatment_id", "animal_last_treatment_date", "animal_bringing_person_id"};

        TableView<row_animals_all> table = new TableView<>();
        root.getChildren().add(table);
        dynamic_objets.add(table);

        int table_y = selectors_y + selectors_height + 20;
        table.setTranslateX(selectors_padding);
        table.setTranslateY(table_y);
        table.setPrefSize(screen_width - 2 * selectors_padding, table_height);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (String parameter : table_parameter_names){
            TableColumn<row_animals_all, String> column = new TableColumn<row_animals_all, String>(parameter);
            table.getColumns().add(column);
        }

        for (int i = 0; i < table_variable_names.length; i++){
            ((TableColumn<row_animals_all, String>)table.getColumns().get(i)).setCellValueFactory(new PropertyValueFactory<row_animals_all, String>(table_variable_names[i]));
        }

        ResultSet result = database.read_query(String.format("SELECT * FROM Animals Where Animal_Adoptable = 'yes';"));
        while (result.next()) {
            table.getItems().add(new row_animals_all(result.getString(1), result.getString(2), result.getString(3),result.getString(4),result.getString(5),result.getString(6),result.getString(7),result.getString(8),result.getString(9),result.getString(10)));
        }
    }

    public void layout_animals_display_all(Pane root) throws SQLException {
        root.getChildren().removeAll(dynamic_objets);

        String table_name = "Animals";
        String[] input_parameter_names = new String[]{};

        String[] table_parameter_names = new String[]{"Animal ID", "Animal Name", "Animal Breed", "Animal Age", "Animal Registration Date", "Animal Leaving Date", " Aimal Adoptable", "Animal Treatment ID", "Animal Last Treatment Date", "Animal Bringing Person ID"};
        String[] table_variable_names = new String[]{"animal_id", "animal_name", "animal_breed", "animal_age", "animal_registration_date", "animal_leaving_date", "animal_adoptable", "animal_treatment_id", "animal_last_treatment_date", "animal_bringing_person_id"};

        TableView<row_animals_all> table = new TableView<>();
        root.getChildren().add(table);
        dynamic_objets.add(table);

        int table_y = selectors_y + selectors_height + 20;
        table.setTranslateX(selectors_padding);
        table.setTranslateY(table_y);
        table.setPrefSize(screen_width - 2 * selectors_padding, table_height);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (String parameter : table_parameter_names){
            TableColumn<row_animals_all, String> column = new TableColumn<row_animals_all, String>(parameter);
            table.getColumns().add(column);
        }

        for (int i = 0; i < table_variable_names.length; i++){
            ((TableColumn<row_animals_all, String>)table.getColumns().get(i)).setCellValueFactory(new PropertyValueFactory<row_animals_all, String>(table_variable_names[i]));
        }

        ResultSet result = database.read_query(String.format("SELECT * FROM Animals"));
        while (result.next()) {
            table.getItems().add(new row_animals_all(result.getString(1), result.getString(2), result.getString(3),result.getString(4),result.getString(5),result.getString(6),result.getString(7),result.getString(8),result.getString(9),result.getString(10)));
        }
    }

    public void layout_employee_delete(Pane root) throws SQLException {
        root.getChildren().removeAll(dynamic_objets);

        String[] input_parameter_names = new String[]{"Employee ID"};

        String[] table_parameter_names = new String[]{"Employee Salary", "Employee Contact", "Employee ID", "Employee Working Hours", "Employe Name", "Employee Starting Date"};
        String[] table_variable_name = new String[]{"employee_salary", "employee_contact", "employee_id", "employee_working_hours", "employee_name", "employee_starting_date"};

        TableView<row_employee> table = new TableView<>();
        root.getChildren().add(table);
        dynamic_objets.add(table);

        int table_y = selectors_y + selectors_height + 20;
        table.setTranslateX(selectors_padding);
        table.setTranslateY(table_y);
        table.setPrefSize(screen_width - 2 * selectors_padding, table_height);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (String parameter : table_parameter_names){
            TableColumn<row_employee, String> column = new TableColumn<row_employee, String>(parameter);
            table.getColumns().add(column);
        }

        for (int i = 0; i < table_variable_name.length; i++){
            ((TableColumn<row_employee, String>)table.getColumns().get(i)).setCellValueFactory(new PropertyValueFactory<row_employee, String>(table_variable_name[i]));
        }

        ResultSet result = database.read_query(String.format("SELECT * FROM Employees;"));
        while (result.next()) {
            table.getItems().add(new row_employee(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6)));
        }

        ArrayList<TextField> inputs = new ArrayList<>();
        int text_area_width = 100;
        int text_area_height = 10;
        int text_area_seperation = 5;
        int text_area_y = table_y + table_height + 10;
        for (int i = 0; i < input_parameter_names.length; i++){
            TextField text_field = new TextField();
            text_field.setPromptText(input_parameter_names[i]);
            text_field.setTranslateX(selectors_padding + i * (text_area_width + text_area_seperation));
            text_field.setTranslateY(text_area_y);
            text_field.setPrefSize(text_area_width, text_area_height);
            root.getChildren().add(text_field);
            dynamic_objets.add(text_field);
            inputs.add(text_field);
        }

        int button_width = 60;
        int button_height = 20;
        int button_y = text_area_y + text_area_height + 20;
        Button submit = new Button("Apply");
        submit.setTranslateX(selectors_padding);
        submit.setTranslateY(button_y);
        submit.setPrefSize(button_width, button_height);
        root.getChildren().add(submit);
        dynamic_objets.add(submit);

        int result_label_x = (selectors_padding + button_width + 10);
        int result_label_y = button_y + button_height/4;
        result_label.setTranslateX(result_label_x);
        result_label.setTranslateY(result_label_y);


        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String id = inputs.get(0).getText();
                int result = database.write_query(String.format("DELETE FROM Employees WHERE Employee_ID = %s;", id));
                result_label.setText(String.format("%d rows have changed", result));
                System.out.println(result);
                operation_selector.fireEvent(new ActionEvent());
            }
        });
    }

    public void layout_employee_change_salary(Pane root) throws SQLException {
        root.getChildren().removeAll(dynamic_objets);

        String[] input_parameter_names = new String[]{"Employee ID", "New Salary"};

        String[] table_parameter_names = new String[]{"Employee Salary", "Employee Contact", "Employee ID", "Employee Working Hours", "Employe Name", "Employee Starting Date"};
        String[] table_variable_name = new String[]{"employee_salary", "employee_contact", "employee_id", "employee_working_hours", "employee_name", "employee_starting_date"};

        TableView<row_employee> table = new TableView<>();
        root.getChildren().add(table);
        dynamic_objets.add(table);

        int table_y = selectors_y + selectors_height + 20;
        table.setTranslateX(selectors_padding);
        table.setTranslateY(table_y);
        table.setPrefSize(screen_width - 2 * selectors_padding, table_height);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (String parameter : table_parameter_names){
            TableColumn<row_employee, String> column = new TableColumn<row_employee, String>(parameter);
            table.getColumns().add(column);
        }

        for (int i = 0; i < table_variable_name.length; i++){
            ((TableColumn<row_employee, String>)table.getColumns().get(i)).setCellValueFactory(new PropertyValueFactory<row_employee, String>(table_variable_name[i]));
        }

        ResultSet result = database.read_query(String.format("SELECT * FROM Employees;"));
        while (result.next()) {
            table.getItems().add(new row_employee(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6)));
        }

        ArrayList<TextField> inputs = new ArrayList<>();
        int text_area_width = 100;
        int text_area_height = 10;
        int text_area_seperation = 5;
        int text_area_y = table_y + table_height + 10;
        for (int i = 0; i < input_parameter_names.length; i++){
            TextField text_field = new TextField();
            text_field.setPromptText(input_parameter_names[i]);
            text_field.setTranslateX(selectors_padding + i * (text_area_width + text_area_seperation));
            text_field.setTranslateY(text_area_y);
            text_field.setPrefSize(text_area_width, text_area_height);
            root.getChildren().add(text_field);
            dynamic_objets.add(text_field);
            inputs.add(text_field);
        }

        int button_width = 60;
        int button_height = 20;
        int button_y = text_area_y + text_area_height + 20;
        Button submit = new Button("Apply");
        submit.setTranslateX(selectors_padding);
        submit.setTranslateY(button_y);
        submit.setPrefSize(button_width, button_height);
        root.getChildren().add(submit);
        dynamic_objets.add(submit);

        int result_label_x = (selectors_padding + button_width + 10);
        int result_label_y = button_y + button_height/4;
        result_label.setTranslateX(result_label_x);
        result_label.setTranslateY(result_label_y);


        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String id = inputs.get(0).getText();
                String salary = inputs.get(1).getText();
                int result = database.write_query(String.format("UPDATE Employees SET Employee_Salary = '%s' WHERE Employee_ID = '%s'; \n", salary, id));
                result_label.setText(String.format("%d rows have changed", result));
                System.out.println(result);
                operation_selector.fireEvent(new ActionEvent());
            }
        });
    }

    public void layout_shelter_change_name(Pane root) throws SQLException {
        root.getChildren().removeAll(dynamic_objets);

        String[] input_parameter_names = new String[]{"New Shelter Name"};

        String[] table_parameter_names = new String[]{"Shelter Name", "Number of Animals", "Number of Employees", "Number of Visitors"};
        String[] table_variable_name = new String[]{"shelter_name", "number_of_animals", "number_of_employees", "number_of_visitors"};

        TableView<row_shelter> table = new TableView<>();
        root.getChildren().add(table);
        dynamic_objets.add(table);

        int table_y = selectors_y + selectors_height + 20;
        table.setTranslateX(selectors_padding);
        table.setTranslateY(table_y);
        table.setPrefSize(screen_width - 2 * selectors_padding, table_height);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (String parameter : table_parameter_names){
            TableColumn<row_shelter, String> column = new TableColumn<row_shelter, String>(parameter);
            table.getColumns().add(column);
        }

        for (int i = 0; i < table_variable_name.length; i++){
            ((TableColumn<row_shelter, String>)table.getColumns().get(i)).setCellValueFactory(new PropertyValueFactory<row_shelter, String>(table_variable_name[i]));
        }

        ResultSet result = database.read_query(String.format("SELECT Shelter_Name, Shelter_Number_Of_Animals, Shelter_Number_Of_Employees, Shelter_Number_Of_Visitors FROM db.shelter;"));
        while (result.next()) {
            table.getItems().add(new row_shelter(result.getString(1), result.getString(2), result.getString(3), result.getString(4)));
        }

        ArrayList<TextField> inputs = new ArrayList<>();
        int text_area_width = 100;
        int text_area_height = 10;
        int text_area_seperation = 5;
        int text_area_y = table_y + table_height + 10;
        for (int i = 0; i < input_parameter_names.length; i++){
            TextField text_field = new TextField();
            text_field.setPromptText(input_parameter_names[i]);
            text_field.setTranslateX(selectors_padding + i * (text_area_width + text_area_seperation));
            text_field.setTranslateY(text_area_y);
            text_field.setPrefSize(text_area_width, text_area_height);
            root.getChildren().add(text_field);
            dynamic_objets.add(text_field);
            inputs.add(text_field);
        }

        int button_width = 60;
        int button_height = 20;
        int button_y = text_area_y + text_area_height + 20;
        Button submit = new Button("Apply");
        submit.setTranslateX(selectors_padding);
        submit.setTranslateY(button_y);
        submit.setPrefSize(button_width, button_height);
        root.getChildren().add(submit);
        dynamic_objets.add(submit);

        int result_label_x = (selectors_padding + button_width + 10);
        int result_label_y = button_y + button_height/4;
        result_label.setTranslateX(result_label_x);
        result_label.setTranslateY(result_label_y);


        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name = inputs.get(0).getText();
                int result = database.write_query(String.format("UPDATE Shelter SET Shelter_Name = '%s' WHERE Shelter_ID = 143; \n", name));
                result_label.setText(String.format("%d rows have changed", result));
                System.out.println(result);
                operation_selector.fireEvent(new ActionEvent());
            }
        });
    }
}