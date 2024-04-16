package com.example.testingmajortask;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class User {
    public static String name;
    public static String address;
    public static String number;

    public String password;

    User(String name, String address, String number){
        User.name = name;
        User.address = address;
        User.number = number;
    }

    public static String getName(){
        return name;
    }

    static boolean validate(String name, String passwordHidden, String passwordShown) throws IOException {
        String password = !Objects.equals(passwordShown, "") ? passwordShown : passwordHidden;

        BufferedReader file = new BufferedReader(new FileReader("src/main/java/com/example/testingmajortask/Database/Users.txt"));

        String line;
        String[] parts;

        while((line = file.readLine()) != null){
            if(Objects.equals(line.split(":")[0], "Name")){
                continue;
            }
            parts = line.split(":");
            if(Objects.equals(parts[0], name)){
                System.out.println(parts[1]);
                if(Objects.equals(parts[1], password)){
                    System.out.println("Password correct");
                    User.name = name;
                    User.address = parts[2];
                    User.number = parts[2];
                    file.close();
                    return true;
                }
                else{
                    file.close();
                    return false;
                }
            }
        }
        file.close();
        return false;

    }
}
