import java.util.ArrayList;
import java.util.Arrays;

public class Pizza {
    // known pizza toppings
    public static String[] knownToppings = { 
        "pepperoni", "sausage", "pineapple", "black olives", "artichokes", "chicken", 
        "mushrooms", "tomatoes", "green peppers", "onions", "ham", "broccoli" };

    // member variables
    private ArrayList<String> toppings;

    // constructors
    public Pizza() {
        this.toppings = new ArrayList<String>();
    }
    public Pizza(Pizza copyFrom) {
        this.toppings = new ArrayList<String>(copyFrom.toppings);
    }

    // basic toppings query/add/remove methods
    public boolean hasTopping(String topping) {
        return this.toppings.contains(topping);
    }
    public boolean addTopping(String topping) {
        topping = topping.toLowerCase();
        if (!hasTopping(topping) && validToppingStr(topping)) {
            this.toppings.add(topping);
            return true;
        }
        return false;
    }
    public boolean remTopping(String topping) {
        topping = topping.toLowerCase();
        if (hasTopping(topping)) {
            int index = this.toppings.indexOf(topping);
            if (index > 0 && index < this.toppings.size()) {
                this.toppings.remove(index);
                return true;
            }
        }
        return false;
    }
    public void remAllToppings() {
        this.toppings.clear();
    }
    public boolean validToppingStr(String topping) {
        topping = topping.toLowerCase();

        // check if already known
        for (String t : knownToppings) {
            if (t.equals(topping)) {
                return true;
            }
        }

        // otherwise add
        java.util.List<String> list = new java.util.ArrayList<>(Arrays.asList(knownToppings));
        list.add(topping);
        knownToppings = list.toArray(new String[0]);
        return true;
    }

    // determine a person's preference for this pizza (preferences dot toppings, ish)
    public double perfFactor(String[] preferences) {
        double result = 0;
        for (String topping : toppings) {
            boolean hasPreference = false;
            for (String pref : preferences) {
                if (pref.equals(topping)) {
                    hasPreference = true;
                    break;
                }
            }
            if (hasPreference) {
                result += 3;
            }
            else {
                result -= 1;
            }
        }
        return result;
    }

    // applyRandomToppingSwap - randomly adds or removes a topping
    public void initializeWithRandomToppings() {
        remAllToppings();
        
        int toppingCount = (int)(Math.random() * 6);
        for (int i = 0; i < toppingCount; i++) {
            int toppingIdx = (int)(Math.random() * knownToppings.length);
            addTopping(knownToppings[toppingIdx]);
        }
    }
    public boolean applyRandomToppingAddOrRemove() {
        if (Math.random() < 0.5) {
            // pick a random one (need to try multiple times, in case it's one we already have)
            final int maxAttempts = 100;
            for (int attempt = 0; attempt < maxAttempts; attempt++) {
                int toppingIdx = (int)(Math.random() * knownToppings.length);
                if (!hasTopping(knownToppings[toppingIdx])) {
                    return addTopping(knownToppings[toppingIdx]);
                }
            }
        }
        else {
            int index = (int)(Math.random() * this.toppings.size());
            if (index < this.toppings.size()) {
                return remTopping(this.toppings.get(index));
            }
        }
        return false;
    }

    // custom toString
    public String toString() {
        String ret = "(";
        for (int i = 0; i < toppings.size(); i++) {
            if (i > 0) {
                ret += ", ";
            }
            ret += toppings.get(i);
        }
        return ret + ")";
    }
}