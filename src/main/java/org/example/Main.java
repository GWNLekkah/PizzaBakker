package org.example;


public class Main {
    public static void main(String[] args) {
        int numChefs = 5;
        int numOvens = 5;
        int numObers = 5;
        String input = "Orderid 1: calzone 2 3; vega 5 4\n" +
                "Orderid 2: calzone2 4 3; vega3 4 2";

        Pizzeria pizzeria = new Pizzeria(numChefs, numOvens, numObers);
        pizzeria.placeOrders(input);
    }
}
