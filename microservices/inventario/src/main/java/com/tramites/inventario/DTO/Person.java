package com.tramites.inventario.DTO;

public class Person {
    private int id;
    private String name;
    private int edad;

    public Person() {
    }

    public Person(int id, String name, int edad) {
        this.id = id;
        this.name = name;
        this.edad = edad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", edad=" + edad +
                '}';
    }
}
