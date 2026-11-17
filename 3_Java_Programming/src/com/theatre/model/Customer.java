package com.theatre.model;

/**
 * Customer entity subclassing Person using `super` constructor (Unit 2).
 */
public class Customer extends Person {

    public Customer(int id, String name, String phone, String email) {
        super(id, name, phone, email);
    }

    @Override
    public String getRoleDescription() {
        return "Movie Patron / Ticket Buyer";
    }

    @Override
    public String toString() {
        return "Customer [" + super.toString() + ", Email: " + email + "]";
    }
}
