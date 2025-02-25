import java.util.ArrayList;

public class PreferenceSolver {
    /*******************************************************************/
    // You are provided with a list of people, each who has a list of
    //  topping preferences. This takes the form of a 2d array.
    //  A pizza matrix, you might say.
    // Your goal is to find the optimal pizza order (number of pizzas 
    //  and set of toppings on each pizza). A pizza order is considered 
    //  optimal if each person can eat at least SLICES_PER_PERSON
    //  slices of pizza AND those slices match their preferred toppings
    //  as much as possible AND there is minimal pizza slices left over. 
    // Towards this end, you are implementing a genetic algorithm. The
    //  algorithm itself will be described in class.
    /**************** YOU SHOULD ONLY MODIFY THIS CLASS ****************/

    public static final int SLICES_PER_PERSON = 3;
    public static final int SLICES_PER_PIZZA = 8;

    public PizzaOrder findOptimalOrder(String[][] pizzaMatrix) {
        
        // TODO: Implement the genetic algorithm

        // data
        // TODO

        // iterate fixed number of times
        int maxIterations = 250;
        for (int i = 0; i < maxIterations; i++) {
            // TODO

            // sort (hint/sample-code provided below)
            //population.sort((pizza1, pizza2) -> Double.compare(pizza2.calcOrderFitness(pizzaMatrix), pizza1.calcOrderFitness(pizzaMatrix)));

            // TODO

            // report progress
            PizzaOrder fittestOrder = new PizzaOrder(); // TODO: replace this line with actually fittest order from the population
            double percentComplete = (double)i / (maxIterations - 1);
            System.out.print("Processing (" + String.format("%.1f", percentComplete * 100) + 
                             "%): Peak order fitness = " + String.format("%.3f", fittestOrder.calcOrderFitness(pizzaMatrix)) + " \r");
            System.out.flush();
        }
        System.out.println();

        return new PizzaOrder(); // TODO: replace this with actual solution
    }

    // app entry point
    public static void main(String[] args) {
        // test data (note that all ingrediates need to be )
        String[][] pizzaPreferenceMatrix = { 
            { "pepperoni", "black olives", "mushrooms" },
            { "pepperoni", "mushrooms" },
            { "pepperoni", "black olives", "green peppers" },
            { "tomatoes", "onions" },
            { "pineapple", "onions", "chicken" },
            { "ham", "sausage", "black olives" },
            { "sausage" },
            { "green peppers", "onions", "pineapple" },
            { "pineapple", "onions", "black olives" },
            { "broccoli", "onions" } };
       
        // TODO: Instantiate a solver, solve, then print out the result...
    }
}
