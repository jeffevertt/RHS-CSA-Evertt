public class AppMain {
    // The program should...
    //  Setup a series of duels (creature vs creature). In each duel, there is one winner.
    // The winner of the duel goes on to face the next random creature. 

    // All creatures have names, health (integer), strength (integer), and a probability of hitting.
    // There are three types of creatures. 
    //  StrongCreature - Has a health of 50, strength of 30, and a 50% chance to damage.
    //  FastCreature - Has a health of 50, strength of 15, and a 50% chance to damage. 
    //                 The FastCreature has a special feature: it can attack twice during each round of the duel.
    //  AccurateCreature - Has a health of 50 and a strength of 20, and a 80% chance to damage.
    //                 The AccurateCreature has a special feature: if it damages, it has a 20% chance to double its damage.

    // During the duel, creatures take turns trying to hit each other.
    //  CreatureA has a chance to hit, if it does then its opponents health is reduced by its strength.
    //  CreatureB then has a chance to hit, same rules apply.
    //  If at any point either creature has reached zero health, then they are defeated.
    // When a winner is found, that creature goes on. Its health is restored to its original before the next duel.

    // There are 8 creatures in the arena that will duel...
    //  Fluffletuft (Strong), Whiskerwhisp (Accurate), Fuzzlenook (Strong), Shagglewisp (Fast), 
    //  Puffernip (Fast), Tanglethorn (Accurate), Quillfluff (Strong), Snugglewight (Fast)

    // Requirements...
    //  - You must have the following classes: Arena, Creature, StrongCreature, FastCreature, AccurateCreature
    //  - Your app's main function should not contain any code logic. It should merely create object(s) and set them off.
    //  - Design with appropriate inheritance and good separation of concerns, encapsulation, cohesion.
    // 
    // Example output…
    //   Dueling: Fluffletuft(Strong) vs Whiskerwhisp(Accurate)!
    //     Fluffletuft(Strong) takes 20 damage.
    //     Whiskerwhisp(Accurate) takes 30 damage.
    //     Whiskerwhisp(Accurate) takes 30 damage.
    //    winner: Fluffletuft(Strong)
    //   Dueling: Fluffletuft(Strong) vs Fuzzlenook(Strong)!
    //     Fluffletuft(Strong) takes 30 damage.
    //     Fuzzlenook(Strong) takes 30 damage.
    //     Fluffletuft(Strong) takes 30 damage.
    //    winner: Fuzzlenook(Strong)
    //   Dueling: Shagglewisp(Fast) vs Fuzzlenook(Strong)!
    //     Fuzzlenook(Strong) takes 15 damage.
    //     Shagglewisp(Fast) takes 30 damage.
    //     Fuzzlenook(Strong) takes 15 damage.
    //     Fuzzlenook(Strong) takes 15 damage.
    //     Shagglewisp(Fast) takes 30 damage.
    //    winner: Fuzzlenook(Strong)
    //   Dueling: Puffernip(Fast) vs Fuzzlenook(Strong)!
    //     Fuzzlenook(Strong) takes 15 damage.
    //     Puffernip(Fast) takes 30 damage.
    //     Fuzzlenook(Strong) takes 15 damage.
    //     Fuzzlenook(Strong) takes 15 damage.
    //     Fuzzlenook(Strong) takes 15 damage.
    //     Fuzzlenook(Strong) takes 15 damage.
    //    winner: Puffernip(Fast)
    //   Dueling: Puffernip(Fast) vs Tanglethorn(Accurate)!
    //     Tanglethorn(Accurate) takes 15 damage.
    //     Puffernip(Fast) takes 20 damage.
    //     Puffernip(Fast) takes 20 damage.
    //     Puffernip(Fast) takes 20 damage.
    //     Puffernip(Fast) takes 20 damage.
    //    winner: Tanglethorn(Accurate)
    //   Dueling: Quillfluff(Strong) vs Tanglethorn(Accurate)!
    //     Tanglethorn(Accurate) takes 30 damage.
    //     Quillfluff(Strong) takes 20 damage.
    //     Quillfluff(Strong) takes 20 damage.
    //     Quillfluff(Strong) takes 20 damage.
    //    winner: Tanglethorn(Accurate)
    //   Dueling: Snugglewight(Fast) vs Tanglethorn(Accurate)!
    //     Tanglethorn(Accurate) takes 15 damage.
    //     Snugglewight(Fast) takes 20 damage.
    //     Tanglethorn(Accurate) takes 15 damage.
    //     Snugglewight(Fast) takes 20 damage.
    //     Tanglethorn(Accurate) takes 15 damage.
    //     Snugglewight(Fast) takes 20 damage.
    //    winner: Tanglethorn(Accurate)
    //   Ultimate champion: Tanglethorn(Accurate)!!!

    // Your code goes here...
}
