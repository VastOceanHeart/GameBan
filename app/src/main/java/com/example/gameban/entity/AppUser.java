package com.example.gameban.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class AppUser {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String email;

    @NotNull
    public String name;

    @NotNull
    public int age;

    @NotNull
    public String address;

    @NotNull
    public String gender;

    public String histories;

    public AppUser(@NonNull String email, @NotNull String name, @NotNull int age, @NotNull String address, @NotNull String gender, String histories) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.address = address;
        this.gender = gender;
        this.histories = histories;
    }
}
