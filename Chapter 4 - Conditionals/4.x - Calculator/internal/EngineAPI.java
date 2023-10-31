package internal;

/**
 * This class provides static methods you can call from the Calculator class to
 * help you manage the calculator state across button clicks.
 * EngineAPI is statically imported into Calculator.java, which means you don't
 * need to do the usual prefixing [e.g. EngineAPI.isEntryIncomplete()]; you can
 * just call these methods directly [e.g. isEntryIncomplete()]
 */
public class EngineAPI {

    //////////////////////////////////////////////////
    /* METHODS FOR BUTTON SWAPPING: */

    /**
     * Swaps the UI text of all buttons that are modified by the "2nd" button. You
     * should use this when the "2nd" button is clicked.
     */
    public static void swapSecondaryButtons() {
        Engine.swapSecondaryButtons();
    }

    /** Swaps the UI text of the "CE"/"C" button. */
    public static void swapClearButton() {
        Engine.swapClearButton();
    }

    //////////////////////////////////////////////////
    /*
     * METHODS FOR WORKING WITH THE "ENTRY":
     * The "entry" is the big number on the bottom row of the main text display. It
     * represents either the number that the user is currently entering,
     * or the result of the previous calculation (if the user has not started
     * entering a new number).
     */

    /**
     * Returns the true or false value that was passed to the previous call to the
     * setEntryIncomplete method.
     */
    public static boolean isEntryIncomplete() {
        return Engine.isEntryIncomplete();
    }

    /**
     * Sets a true or false value that can be accessed later by calling the
     * isEntryIncomplete method.
     * You should set this while the user is entering a number. For example, for the
     * button sequence 1.2 + 3.4
     * you should setEntryIncomplete(true) when the 1, ., 2, 3, ., 4, are clicked,
     * but call setEntryIncomplete(false) when the + is clicked
     */
    public static void setEntryIncomplete(boolean isEntryIncomplete) {
        Engine.setEntryIncomplete(isEntryIncomplete);
    }

    /**
     * Returns the current value of the entry as a string. This is useful for text
     * operations.
     */
    public static String getEntryAsString() {
        return Engine.getEntryAsString();
    }

    /**
     * Returns the current value of the entry as a string. This is useful for
     * numeric operations.
     */
    public static double getEntryAsDouble() {
        return Engine.getEntryAsDouble();
    }

    /**
     * Returns true if the entry currently has a decimal point anywhere in it. You
     * should use this to avoid putting multiple decimal points
     * into the entry.
     */
    public static boolean entryHasADecimalPoint() {
        return Engine.entryHasADecimalPoint();
    }

    /**
     * Sets the value of the entry using a String. The previous value of the entry
     * is thrown away.
     */
    public static void setEntry(String entry) {
        Engine.setEntry(entry);
    }

    /**
     * Sets the value of the entry using a double. The previous value of the entry
     * is thrown away.
     */
    public static void setEntry(double entry) {
        Engine.setEntry(entry);
    }

    /**
     * Sets the value of the entry using a single char. The previous value of the
     * entry is thrown away.
     */
    public static void setEntry(char entry) {
        Engine.setEntry(entry);
    }

    /** Adds a decimal point to the end of the entry. */
    public static void appendDecimalPointToEntry() {
        Engine.appendDecimalPointToEntry();
    }

    /** Adds a single digit to the end of the entry. */
    public static void appendDigitToEntry(char digit) {
        Engine.appendDigitToEntry(digit);
    }

    /**
     * Adds a single digit to the end of the entry. The string you pass to this
     * method must have a single character.
     */
    public static void appendDigitToEntry(String digit) {
        Engine.appendDigitToEntry(digit);
    }

    /**
     * Removes the last character (digit or decimal point) from the entry. You
     * should use this for the backspace button.
     */
    public static void deleteLastDigitFromEntry() {
        Engine.deleteLastDigitFromEntry();
    }

    //////////////////////////////////////////////////
    /*
     * METHODS FOR WORKING WITH THE "EXPRESSION":
     * The "expression" is the text on the top row of the main text display. It
     * represents the previous sequence of operations/functions.
     */

    /** Returns the current value of the expression. */
    public static String getExpression() {
        return Engine.getExpression();
    }

    /**
     * Sets the value of the expression. The previous value of the expression is
     * thrown away.
     */
    public static void setExpression(String expressionText) {
        Engine.setExpression(expressionText);
    }

    /**
     * Sets the expression to be blank. The previous value of the expression is
     * thrown away.
     */
    public static void clearExpression() {
        Engine.clearExpression();
    }

    /** Adds text to the end of the expression. */
    public static void appendToExpression(String textToAppendToExpression) {
        Engine.appendToExpression(textToAppendToExpression);
    }

    /** Adds text to the beginning of the expression. */
    public static void prependToExpression(String textToPrependToExpression) {
        Engine.prependToExpression(textToPrependToExpression);
    }

    /**
     * Checks if the current value of the expression ends with the = symbol. If it
     * does, the = symbol is deleted; otherwise, nothing happens.
     */
    public static void deleteTrailingEqualsFromExpression() {
        Engine.deleteTrailingEqualsFromExpression();
    }

    //////////////////////////////////////////////////
    /*
     * METHODS FOR WORKING WITH THE PREVIOUS INPUTS:
     * Storing input data for later usage can be helpful for determining what to do
     * next.
     * 
     * - The "previous result" is the value returned by the previous
     * operation/function (example: 2+4 should produce 6 as the previous result), or
     * the number entered by the user if there was no previous operation
     * - The "previous operation button" is the last button to be clicked that was
     * an operation/function (such as + or log).
     * - Boolean value "wasPreviousButtonEquals" indicates whether the last button
     * clicked was the = symbol.
     */

    /**
     * Returns the "previous result" that is currently stored.
     * Before calling this, you should ensure that hasPreviousResult() returns true.
     */
    public static double getPreviousResult() {
        return Engine.getPreviousResult();
    }

    /** Returns whether there is currently a "previous result" stored. */
    public static boolean hasPreviousResult() {
        return Engine.hasPreviousResult();
    }

    /**
     * Stores the value of the "previous result". If a previous result was already
     * stored, it is thrown away.
     */
    public static void setPreviousResult(double previousResult) {
        Engine.setPreviousResult(previousResult);
    }

    /** Removes the stored value of the "previous result". */
    public static void clearPreviousResult() {
        Engine.clearPreviousResult();
    }

    /**
     * Returns the "previous operation button" that is currently stored.
     * Before calling this, you should ensure that hasPreviousOperationButton()
     * returns true.
     */
    public static String getPreviousOperationButton() {
        return Engine.getPreviousOperationButton();
    }

    /** Returns whether there is currently a "previous operation button" stored. */
    public static boolean hasPreviousOperationButton() {
        return Engine.hasPreviousOperationButton();
    }

    /**
     * Stores the value of the "previous operation button". If a previous result was
     * already stored, it is thrown away.
     */
    public static void setPreviousOperationButton(String previousOperationButton) {
        Engine.setPreviousOperationButton(previousOperationButton);
    }

    /** Removes the stored value of the "previous operation button". */
    public static void clearPreviousOperationButton() {
        Engine.clearPreviousOperationButton();
    }

    /**
     * Returns the true or false value that was set by the previous call to the
     * setWasPreviousButtonEquals method.
     */
    public static boolean wasPreviousButtonEquals() {
        return Engine.wasPreviousButtonEquals();
    }

    /**
     * Sets a true or false value that can be accessed later by calling the
     * wasPreviousButtonEquals method.
     * After every button click, you should call this with either true or false.
     */
    public static void setWasPreviousButtonEquals(boolean wasPreviousButtonEquals) {
        Engine.setWasPreviousButtonEquals(wasPreviousButtonEquals);
    }

    //////////////////////////////////////////////////
    /*
     * METHODS FOR WORKING WITH "MEMORY":
     * These are for the MC/MR/M+/M-/MS buttons on the memory row. There's no
     * documentation. You'll have to experiment.
     */

    public static double getLatestMemoryValue() {
        return Engine.getLatestMemoryValue();
    }

    public static void replaceLatestMemoryValue(double valueToStore) {
        Engine.replaceLatestMemoryValue(valueToStore);
    }

    public static void storeNewMemoryValue(double valueToStore) {
        Engine.storeNewMemoryValue(valueToStore);
    }

    public static void clearMemoryValues() {
        Engine.clearMemoryValues();
    }

    public static void toggleMemoryHistoryMenu() {
        Engine.toggleMemoryHistoryMenu();
    }

}