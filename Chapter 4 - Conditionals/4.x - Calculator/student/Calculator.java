package student;

import constants.Buttons;
import static internal.EngineAPI.*;

public class Calculator {
    
    /**
     * This method is called when a single button is clicked.
     * You don't get notifications of multiple clicks at once, so you'll have to store data for later usage by using the EngineAPI methods.
     * 
     * @param buttonName The name of the button that was clicked. This should always match one String from the {@link Buttons} class in Buttons.java.
     */
    public static void handleButtonClick(String buttonName){
        if (buttonName.equals(Buttons.ONE)) {
            //TODO Any conditions to check for when '1' is clicked? Any other things you should update?
            //Use statically imported methods from the EngineAPI.java file:
            appendDigitToEntry('1');
            setEntryIncomplete(true);
            setWasPreviousButtonEquals(false);
        }
        //TODO Handle as many buttons as possible! See the list of Buttons constants in the Buttons.java class.
        //Whenever you find yourself repeating code, you may want to create other static methods in this class to help!
    }

}
