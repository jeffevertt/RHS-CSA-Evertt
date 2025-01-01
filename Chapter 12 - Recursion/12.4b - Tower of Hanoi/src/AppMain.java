public class AppMain {
    /**********************************************************/
    /********** SEE TowersGame.java FOR INSTRUCTIONS **********/
    /**************** DO NOT MODIFY THIS CLASS ****************/
    /********* EXCEPT TO TEST A ALTERNATE DISK COUNTS *********/
    /**********************************************************/
    public static void main(String[] args) {
        // create the game
        TowersGame tower = new TowersGame(5);

        // your recursive solve
        tower.solve();

        // print final state
        tower.drawFinalFrame();
    }
}
