package com.example.firebase;

public class User
{
    private String No_of_copies;
    private String Email;

    public User(){

    }

    public User(String no_of_copies, String email) {
        No_of_copies = no_of_copies;
        Email=email;


    }

    public String getNo_of_copies() {
        return No_of_copies;
    }

    public void setNo_of_copies(String no_of_copies) {
        No_of_copies = no_of_copies;
    }

    public String getEmail(){
        return Email;
    }

    public void setEmail(String email){
        Email=email;
    }
}
