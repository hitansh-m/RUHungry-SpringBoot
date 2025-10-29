package com.ruhungry.core;

import java.io.InputStream;
import java.util.Scanner;

/**
 * RUHungry is a fictitious restaurant.
 * You will be running RUHungry for a day by seating guests,
 * taking orders, donation requests and restocking the pantry as necessary.
 *
 * Modified for Spring Boot to use InputStream instead of StdIn
 *
 * @author Mary Buist
 * @author Kushi Sharma
 */

public class RUHungry {

    /*
     * Instance variables
     */

    // Menu: two parallel arrays. The index in one corresponds to the same index in the other.
    private String[] categoryVar; // array where containing the name of menu categories (e.g. Appetizer, Dessert).
    private MenuNode[] menuVar; // array of lists of MenuNodes where each index is a category.

    // Stock: hashtable using chaining to resolve collisions.
    private StockNode[] stockVar; // array of linked lists of StockNodes (use hashfunction to organize Nodes: id % stockVarSize)
    private int stockVarSize;

    // Transactions: orders, donations, restock transactions are recorded
    private TransactionNode transactionVar; // refers to the first front node in linked list

    // Queue keeps track of parties that left the restaurant
    private Queue<Party> leftQueueVar;

    // Tables Information - parallel arrays
    // If tableSeats[i] has 3 seats then parties with at most 3 people can sit at tables[i]
    private Party[] tables; // Parties currently occupying the tables
    private int[] tableSeats; // The number of seats at each table

    /*
     * Default constructor
     */
    public RUHungry() {
        categoryVar = null;
        menuVar = null;
        stockVar = null;
        stockVarSize = 0;
        transactionVar = null;
        leftQueueVar = null;
        tableSeats = null;
        tables = null;
    }

    /*
     * Getter and Setter methods
     */
    public MenuNode[] getMenu() {
        return menuVar;
    }

    public String[] getCategoryArray() {
        return categoryVar;
    }

    public StockNode[] getStockVar() {
        return stockVar;
    }

    public TransactionNode getFrontTransactionNode() {
        return transactionVar;
    }

    public TransactionNode resetFrontNode() {
        return transactionVar = null;
    }

    public Queue<Party> getLeftQueueVar() {
        return leftQueueVar;
    }

    public Party[] getTables() {
        return tables;
    }

    public int[] getTableSeats() {
        return tableSeats;
    }

    /*
     * Menu methods
     */

    /**
     * This method populates the two parallel arrays menuVar and categoryVar.
     * Modified to use InputStream for Spring Boot compatibility.
     *
     * @param inputStream - InputStream containing menu data
     */
    public void menu(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        int length = scanner.nextInt();
        categoryVar = new String[length];
        menuVar = new MenuNode[length];
        scanner.nextLine();
        for (int i = 0; i < length; i++) {
            String names = scanner.nextLine();
            categoryVar[i] = names;
            int dishes = scanner.nextInt();
            scanner.nextLine();
            for (int j = 0; j < dishes; j++) {
                String dishName = scanner.nextLine();
                int l = scanner.nextInt();
                int[] ids = new int[l];
                String ings = scanner.nextLine();
                String[] stringArray = ings.trim().split("\\s+");
                for (int k = 0; k < ids.length; k++) {
                    ids[k] = Integer.parseInt(stringArray[k]);
                }
                Dish d = new Dish(categoryVar[i], dishName, ids);
                MenuNode node = new MenuNode(d, null);
                insert(node, i);
            }
        }
        scanner.close();
    }

    private void insert(MenuNode x, int i) {
        if (menuVar[i] == null) {
            menuVar[i] = x;
            return;
        }
        x.setNextMenuNode(menuVar[i]);
        menuVar[i] = x;
    }

    /**
     * Find and return the MenuNode that contains the dish with dishName in the menuVar.
     */
    public MenuNode findDish(String dishName) {
        MenuNode menuNode = null;

        for (int category = 0; category < menuVar.length; category++) {
            MenuNode ptr = menuVar[category];

            while (ptr != null) {
                if (ptr.getDish().getName().equalsIgnoreCase(dishName)) {
                    return ptr;
                } else {
                    ptr = ptr.getNextMenuNode();
                }
            }
        }
        return menuNode;
    }

    /**
     * Find integer that corresponds to the index in menuVar and categoryVar arrays
     * that has that category
     */
    public int findCategoryIndex(String category) {
        int index = 0;
        for (int i = 0; i < categoryVar.length; i++) {
            if (category.equalsIgnoreCase(categoryVar[i])) {
                index = i;
                break;
            }
        }
        return index;
    }

    /*
     * Stockroom methods
     */

    /**
     * This method adds a StockNode into the stockVar hashtable.
     */
    public void addStockNode(StockNode newNode) {
        int x = newNode.getIngredient().getID();
        int index = x % stockVarSize;
        insertF(newNode, index);
    }

    private void insertF(StockNode x, int i) {
        if (stockVar[i] == null) {
            stockVar[i] = x;
            return;
        }
        x.setNextStockNode(stockVar[i]);
        stockVar[i] = x;
    }

    /**
     * This method finds an ingredient from StockVar (given the ingredientID)
     */
    public StockNode findStockNode(int ingredientID) {
        StockNode stockNode = null;

        for (int Ingindex = 0; Ingindex < stockVar.length; Ingindex++) {
            StockNode ptr = stockVar[Ingindex];

            while (ptr != null) {
                if (ptr.getIngredient().getID() == ingredientID) {
                    return ptr;
                } else {
                    ptr = ptr.getNextStockNode();
                }
            }
        }
        return stockNode;
    }

    /**
     * This method is to find an ingredient from StockVar (given the ingredient name).
     */
    public StockNode findStockNode(String ingredientName) {
        StockNode stockNode = null;

        for (int index = 0; index < stockVar.length; index++) {
            StockNode ptr = stockVar[index];

            while (ptr != null) {
                if (ptr.getIngredient().getName().equalsIgnoreCase(ingredientName)) {
                    return ptr;
                } else {
                    ptr = ptr.getNextStockNode();
                }
            }
        }
        return stockNode;
    }

    /**
     * This method updates the stock amount of an ingredient.
     */
    public void updateStock(String ingredientName, int ingredientID, int stockAmountToAdd) {
        StockNode nodeToUpdate = null;

        if (ingredientName != null) {
            nodeToUpdate = findStockNode(ingredientName);
        } else if (ingredientID != -1) {
            nodeToUpdate = findStockNode(ingredientID);
        }

        if (nodeToUpdate != null) {
            int currentStockLevel = nodeToUpdate.getIngredient().getStockLevel();
            nodeToUpdate.getIngredient().setStockLevel(currentStockLevel + stockAmountToAdd);
        }
    }

    /**
     * This method goes over menuVar to update the price and profit of each dish.
     */
    public void updatePriceAndProfit() {
        for (int i = 0; i < menuVar.length; i++) {
            MenuNode ptr = menuVar[i];
            while (ptr != null) {
                double Cost = 0;
                Dish eachDish = ptr.getDish();
                int[] ids = eachDish.getStockID();
                for (int j = 0; j < ids.length; j++) {
                    StockNode stockNode = findStockNode(ids[j]);
                    if (stockNode != null && stockNode.getIngredient() != null) {
                        Cost += stockNode.getIngredient().getCost();
                    }
                }
                Double NewPrice = Cost * (1.2);
                Double profit = NewPrice - Cost;
                eachDish.setPriceOfDish(NewPrice);
                eachDish.setProfit(profit);
                ptr = ptr.getNextMenuNode();
            }
        }
    }

    /**
     * This method initializes and populates stockVar which is a hashtable.
     * Modified to use InputStream for Spring Boot compatibility.
     *
     * @param inputStream - InputStream containing stock data
     */
    public void createStockHashTable(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        int x = scanner.nextInt();
        stockVarSize = x;
        stockVar = new StockNode[stockVarSize];
        while (scanner.hasNextLine()) {
            if (!scanner.hasNextInt()) break;
            int stockId = scanner.nextInt();
            
            // Read the name (rest of the line after stockId)
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) break;
            
            // Read cost and amount from next line
            if (!scanner.hasNextDouble()) break;
            Double cost = scanner.nextDouble();
            
            if (!scanner.hasNextInt()) break;
            int amt = scanner.nextInt();
            
            // Move to next line for next iteration
            if (scanner.hasNextLine()) scanner.nextLine();
            
            Ingredient ingredient = new Ingredient(stockId, name, amt, cost);
            StockNode node = new StockNode(ingredient, null);
            addStockNode(node);
        }
        scanner.close();
    }

    /*
     * Transaction methods
     */

    /**
     * This method adds a TransactionNode to the END of the transactions linked list.
     */
    public void addTransactionNode(TransactionData data) {
        TransactionNode node = new TransactionNode(data, null);
        if (transactionVar == null) {
            transactionVar = node;
        } else {
            TransactionNode ptr = transactionVar;
            while (ptr.getNext() != null) {
                ptr = ptr.getNext();
            }
            ptr.setNext(node);
        }
    }

    /**
     * This method checks if there's enough in stock to prepare a dish.
     */
    public boolean checkDishAvailability(String dishName, int numberOfDishes) {
        MenuNode d = findDish(dishName);
        boolean p = true;
        Dish dish = d.getDish();
        int[] ids = dish.getStockID();
        for (int i = 0; i < ids.length; i++) {
            if (findStockNode(ids[i]).getIngredient().getStockLevel() >= numberOfDishes) {
                // sufficient stock
            } else if (findStockNode(ids[i]).getIngredient().getStockLevel() < numberOfDishes) {
                p = false;
            }
        }
        return p;
    }

    /**
     * This method simulates a customer ordering a dish.
     */
    public void order(String dishName, int quantity) {
        MenuNode original = findDish(dishName);
        TransactionData node = new TransactionData("order", dishName, quantity, original.getDish().getProfit() * quantity, true);
        int[] ids = original.getDish().getStockID();
        if (checkDishAvailability(dishName, quantity) == true) {
            addTransactionNode(node);
            for (int i = 0; i < ids.length; i++) {
                updateStock(null, ids[i], -quantity);
            }
            return;
        }
        if (checkDishAvailability(dishName, quantity) == false) {
            MenuNode dishes = findDish(dishName);
            String cats = dishes.getDish().getCategory();
            MenuNode ptr = dishes;
            while (ptr != null) {
                int[] ids2 = ptr.getDish().getStockID();
                String currName = ptr.getDish().getName();
                if (checkDishAvailability(currName, quantity) == true) {
                    TransactionData node2 = new TransactionData("order", ptr.getDish().getName(), quantity, ptr.getDish().getProfit() * quantity, true);
                    addTransactionNode(node2);
                    for (int i = 0; i < ids2.length; i++) {
                        updateStock(null, ids2[i], -quantity);
                    }
                    return;
                } else if (checkDishAvailability(currName, quantity) == false) {
                    TransactionData node22 = new TransactionData("order", ptr.getDish().getName(), quantity, 0, false);
                    addTransactionNode(node22);
                }
                ptr = ptr.getNextMenuNode();
            }
            int index = findCategoryIndex(cats);
            MenuNode dishes3 = menuVar[index];
            while (dishes3 != original && dishes3 != null) {
                int[] ids3 = dishes3.getDish().getStockID();
                if (checkDishAvailability(dishes3.getDish().getName(), quantity) == true) {
                    TransactionData node3 = new TransactionData("order", dishes3.getDish().getName(), quantity, dishes3.getDish().getProfit() * quantity, true);
                    addTransactionNode(node3);
                    for (int i = 0; i < ids3.length; i++) {
                        updateStock(null, ids3[i], -quantity);
                    }
                    return;
                } else if (checkDishAvailability(dishes3.getDish().getName(), quantity) == false) {
                    TransactionData node4 = new TransactionData("order", dishes3.getDish().getName(), quantity, 0, false);
                    addTransactionNode(node4);
                }
                dishes3 = dishes3.getNextMenuNode();
            }
        }
    }

    /**
     * This method returns the total profit for the day
     */
    public double profit() {
        double profit = 0.0;
        TransactionNode ptr = transactionVar;
        while (ptr != null) {
            profit += ptr.getData().getProfit();
            ptr = ptr.getNext();
        }
        return profit;
    }

    /**
     * This method simulates donation requests, successful or not.
     */
    public void donation(String ingredientName, int quantity) {
        StockNode original = findStockNode(ingredientName);
        TransactionData node1 = new TransactionData("donation", ingredientName, quantity, 0, false);
        TransactionData node = new TransactionData("donation", ingredientName, quantity, 0, true);
        if (profit() > 50.0 && original.getIngredient().getStockLevel() >= quantity) {
            addTransactionNode(node);
            updateStock(ingredientName, -1, -quantity);
            return;
        } else {
            addTransactionNode(node1);
        }
    }

    /**
     * This method simulates restock orders
     */
    public void restock(String ingredientName, int quantity) {
        StockNode o = findStockNode(ingredientName);
        double cost = o.getIngredient().getCost() * quantity;
        TransactionData node = new TransactionData("restock", ingredientName, quantity, 0, false);
        TransactionData node2 = new TransactionData("restock", ingredientName, quantity, -cost, true);
        if (profit() > cost) {
            addTransactionNode(node2);
            updateStock(ingredientName, -1, quantity);
            return;
        } else {
            addTransactionNode(node);
        }
    }

    /*
     * Seat guests/customers methods
     */

    /**
     * Method to populate tables based upon input
     * Modified to use InputStream for Spring Boot compatibility.
     */
    public void createTables(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        int numberOfTables = scanner.nextInt();
        tableSeats = new int[numberOfTables];
        tables = new Party[numberOfTables];

        for (int t = 0; t < numberOfTables; t++) {
            tableSeats[t] = scanner.nextInt() * scanner.nextInt();
        }
        scanner.close();
    }

    /**
     * This method simulates seating guests at tables.
     */
    public void seatAllGuests(Queue<Party> waitingQueue) {
        // WRITE YOUR CODE HERE
    }
}
