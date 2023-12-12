public class Arena {
    private final int CREATURE_COUNT = 8;
    private int creaturesRemaining;

    public Arena() {
        creaturesRemaining = CREATURE_COUNT;
    }

    private Creature nextCreature() {
        // Decrease our number remaining...
        int creatureIndex = creaturesRemaining;
        creaturesRemaining--;

        /*  Create each requested create. Use creatureIndex and the list specified
               in AppMain's app description to determine which one to create/return. */
        // TODO
        
        return null;
    }

    // Runs a creature vs creature dual, returns the winner
    private Creature runDuel(Creature a, Creature b) {
        // Report
        System.out.println("Dueling: " + a + " vs " + b + "!");

        // Keep fighting till someone wins
        while (!a.isDefeated() && !b.isDefeated()) {
            a.performAttack(b);
            if (!b.isDefeated()) {
                b.performAttack(a);
            }
        }
        if (a.isDefeated()) {
            return b;
        }
        return a;
    }

    // Runs all the duels until an ultimate winner is found
    public void runDuels() {
        Creature a = nextCreature();
        Creature b = nextCreature();
        Creature winner = null;
        while (a != null && b != null) {
            // Run the dual
            winner = runDuel(a, b);

            // Report
            System.out.println(" winner: " + winner);

            if (winner == a) {
                b = nextCreature();
            } else {
                a = nextCreature();
            }
            winner.resetHealth();
        }

        // Report the final winner...
        if (winner != null) {
            System.out.println("Ultimate champion: " + winner + "!!!");
        }
        else {
            System.out.println("No winner found :(");
        }
    }
}
