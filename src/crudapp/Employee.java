package crudapp;

public class Employee {
    private int ID;
    private String first_name;
    private String last_name;

    public Employee(int ID, String first_name, String last_name) {
        this.ID = ID;
        this.first_name = first_name;
        this.last_name = last_name;
    }
    
    public Employee(){
        
    }

    public int getId() {
        return ID;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    
        
    
    
}
