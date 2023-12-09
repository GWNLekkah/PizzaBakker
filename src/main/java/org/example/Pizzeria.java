package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class Pizzeria {
    private static final List<Order> orders = new ArrayList<>();
    private final ExecutorService chefPool;
    private final ExecutorService ovenPool;
    private final ExecutorService oberPool;


    public Pizzeria(int numChefs, int numOvens, int numObers) {
        this.chefPool = Executors.newFixedThreadPool(numChefs);
        this.ovenPool = Executors.newFixedThreadPool(numOvens);
        this.oberPool = Executors.newFixedThreadPool(numObers);
    }

    public void placeOrders(String input) {
        // Each line contains an order
        for (String order : input.split("\n")) {
            parseOrder(order);
        }
        run();
    }

    private void parseOrder(String order) {
        String[] split = order.split(":");
        Integer id = parseId(split[0]);
        List<Pizza> pizzas = parsePizzas(split[1]);
        orders.add(new Order(id, pizzas));
    }

    private Integer parseId(String input) {
        return Integer.parseInt(input.replace("Orderid ", ""));
    }

    private List<Pizza> parsePizzas(String input) {
        List<Pizza> pizzas = new ArrayList<>();
        for (String pizza : input.split(";")) {
            pizzas.add(new Pizza(pizza));
        }
        return pizzas;
    }

    public void run() {
        for (Order order: orders) {
            for (Pizza pizza : order.getPizzas()) {
                chefPool.submit(() -> preparePizza(pizza));
            }
        }
        chefPool.shutdown();
    }

    private void preparePizza(Pizza pizza) {
        try {
            sleep(pizza.getPreparationTimeInSeconds() * 1000);
            ovenPool.submit(() -> bakePizza(pizza));
            System.out.println("Prepared seconds: " + pizza.getPreparationTimeInSeconds());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void bakePizza(Pizza pizza) {
        try {
            sleep(pizza.getBakeTimeInSeconds() * 1000);
            pizza.setIsBaked();
            System.out.println("Baked seconds: " + pizza.getBakeTimeInSeconds());
            tryServeOrders();
            tryShutdownOvens();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void tryServeOrders() {
        for (Order order: orders) {
            if (order.isReadyToBeServed()) {
                order.setIsServed(true);
                oberPool.submit(() -> serveOrder(order));
                tryShutdownObers();
                return;
            }
        }
    }

    private void serveOrder(Order order) {
        try {
            sleep(3000);
            System.out.println("Served order: " + order.getId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void tryShutdownOvens() {
        for (Order order: orders) {
            for (Pizza pizza : order.getPizzas()) {
                if (!pizza.getIsBaked()) {
                    return;
                }
            }
        }
        System.out.println("Ovens shut down");
        ovenPool.shutdown();
    }

    private synchronized void tryShutdownObers() {
        for (Order order: orders) {
            if (!order.getIsServed()) {
                return;
            }
        }
        System.out.println("Obers shut down");
        ovenPool.shutdown();
    }
}
