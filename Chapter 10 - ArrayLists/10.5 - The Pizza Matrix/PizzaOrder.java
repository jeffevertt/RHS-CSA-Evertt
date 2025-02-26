import java.util.ArrayList;

public class PizzaOrder {
    private ArrayList<Pizza> pizzas = new ArrayList<Pizza>();

    public PizzaOrder() {
    }
    public PizzaOrder(PizzaOrder copyFrom) {
        for (Pizza pizza : copyFrom.pizzas) {
            Pizza copy = new Pizza(pizza);
            this.pizzas.add(copy);
        }
    }

    // basic get/set/add methods
    public int getPizzaCount() {
        return pizzas.size();
    }
    public Pizza addEmptyPizza() {
        Pizza pizza = new Pizza();
        pizzas.add(pizza);
        return pizza;
    }
    public void addPizza(Pizza pizza) {
        pizzas.add(pizza);
    }
    public Pizza getPizza(int index) {
        if (index < 0 || index >= pizzas.size()) {
            return null;
        }
        return pizzas.get(index);
    }
    public void clearOrder() {
        pizzas.clear();
    }

    // methods you might find useful
    public void initializeWithRandomPizzas(int numPizzas) {
        clearOrder();
        for (int i = 0; i < numPizzas; i++) {
            Pizza pizza = new Pizza();
            pizza.initializeWithRandomToppings();
            this.pizzas.add(pizza);
        }
    }
    public boolean applyRandomToppingAddOrRemoveToRandomPizza() {
        int pizzaIdx = (int)(Math.random() * this.pizzas.size());
        if (pizzaIdx < this.pizzas.size()) {
            return this.pizzas.get(pizzaIdx).applyRandomToppingAddOrRemove();
        }
        return false;
    }

    // computing order fitness
    public double calcOrderFitness(String[][] pizzaMatrix) {
        // pizzaMatrix should be all lowercase
        for (int r = 0; r < pizzaMatrix.length; r++) {
            for (int c = 0; c < pizzaMatrix[r].length; c++) {
                pizzaMatrix[r][c] = pizzaMatrix[r][c].toLowerCase();
            }
        }

        // we're going to calculate the "fitness" or satifaction score for this order
        double fitness = 0;

        // keep track of slice count
        int[] sliceCount = new int[pizzas.size()];
        for (int i = 0; i < sliceCount.length; i++) {
            sliceCount[i] = PreferenceSolver.SLICES_PER_PIZZA;
        }

        // walk the rows, letting each eat their preferred slice
        int numPeople = pizzaMatrix.length; // each row corresponds to a person
        for (int s = 0; s < PreferenceSolver.SLICES_PER_PERSON; s++) {
            for (int p = 0; p < numPeople; p++) {
                // figure out which pizza this person would prefer
                String[] preferences = pizzaMatrix[p];
                int pizza = -1;
                for (int i = 0; i < pizzas.size(); i++) {
                    if (sliceCount[i] > 0) {
                        if ((pizza < 0) || (pizzas.get(i).perfFactor(preferences) > pizzas.get(pizza).perfFactor(preferences))) {
                            pizza = i;
                        }
                    }
                }
                if (pizza < 0) {
                    // out of slices, this is just no good
                    return 0.0;
                }

                // eat a slice & count it
                sliceCount[pizza] -= 1;
                fitness += pizzas.get(pizza).perfFactor(preferences);
            }
        }

        // If there is more than slices remaining, then not fit at all
        int remainingSliceCount = 0;
        for (int i = 0; i < sliceCount.length; i++) {
            remainingSliceCount += sliceCount[i];
        }
        if (remainingSliceCount > PreferenceSolver.SLICES_PER_PIZZA) {
            return 0;
        }

        // Use the computed fitness from the pseudo dot products above
        return fitness;
    }

    // custom toString
    public String toString() {
        String ret = Integer.toString(pizzas.size()) + " pizza" + (pizzas.size() > 1 ? "s" : "") + "\n";
        for (int i = 0; i < pizzas.size(); i++) {
            if (i > 0) {
                ret += ",\n";
            }
            ret += " " + pizzas.get(i).toString();
        }
        return ret;
    }
}
