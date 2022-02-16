package model;



public class User {

    public String email;
    public String name;
    public String gender;
    public String status;

    public static int getRandom(int max){
       return (int) (Math.random()*max);
    }



    public User(String name, String gender, String status) {
        this.email = "umut"+ getRandom(1000) + "@gmail.com";
        this.name = name;
        this.gender = gender;
        this.status = status;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }





}
