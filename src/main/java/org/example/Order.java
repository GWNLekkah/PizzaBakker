package org.example;


import java.util.List;

public class Order {
    private int id;
    private List<Pizza> pizzas;
    private boolean isServed = false;

    public Order(int id, List<Pizza> pizzas) {
        this.id = id;
        this.pizzas = pizzas;
    }

    public boolean isReadyToBeServed() {
        if (isServed) {
            return false;
        }

        for (Pizza pizza : pizzas) {
            if (!pizza.getIsBaked()) {
                return false;
            }
        }
        return true;
    }

    public void setIsServed(boolean isServed) {
        this.isServed = isServed;
    }

    public boolean getIsServed() {
        return isServed;
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public int getId() {
        return id;
    }
}
