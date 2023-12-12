public class Creature {
    private static int MAX_HEALTH = 50;

    private String name;
    private int health;
    private int strength;
    private int chanceToDamage;

    public Creature(String name, int strength, int chanceToDamage) {
        this.name = name;
        this.strength = strength;
        this.chanceToDamage = chanceToDamage;
        resetHealth();
    }
    public boolean isDefeated() {
        return (health <= 0);
    }
    public void resetHealth() {
        this.health = MAX_HEALTH;
    }
    public void performAttack(Creature target) {
        if (doesAttackDamage(target)) {
            damageTargetCreature(target);
        }
    }
    public void takeDamage(int damage) {
        // Report...
        System.out.println("  " + this + " takes " + damage + " damage.");

        // Apply it...
        health -= damage;
    }
    public String toString() {
        return name;
    }

    public boolean doesAttackDamage(Creature target) {
        return (Math.random() * 100 < chanceToDamage);
    }
    public void damageTargetCreature(Creature target) {
        target.takeDamage(strength);
    }
}
