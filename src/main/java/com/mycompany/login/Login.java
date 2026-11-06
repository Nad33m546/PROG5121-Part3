package com.mycompany.login;

public class Login {
    
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;
    
    public Login(String username, String password,
                 String cellPhoneNumber,
                 String firstName, String lastName){
        
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
     public boolean checkUserName() {
        return username != null
                && username.contains("_")
                && username.length() <= 5;
    }

    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = password.matches(".*[A-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");

        return hasCapital && hasNumber && hasSpecial;
    }

    public boolean checkCellPhoneNumber() {
        return cellPhoneNumber != null
                && cellPhoneNumber.matches("^\\+27[0-9]{9}$");
    }

    public boolean loginUser(String enteredUsername,
                             String enteredPassword) {
        return username.equals(enteredUsername)
                && password.equals(enteredPassword);
    }

    public String returnLoginStatus() {
        return "Welcome " + firstName + " " + lastName
                + ", it is great to see you again.";
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }
}