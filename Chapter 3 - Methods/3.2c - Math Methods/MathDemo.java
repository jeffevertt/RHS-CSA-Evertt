// Demos the creation of simple math methods (pow, sumToN, ...)
public class MathDemo {
    public static void main(String[] args) {

        // Add code the exercises (run & test) all the code you wrote in this class. 
        // Pick good examples (non-trivial but also something you can hand-verify).  
        // Be sure to identify the output For example: System.out.println("pow(2, 3) == " + pow(2, 3));
        // TODO: FINISH CALLING ALL METHODS. 

        System.out.println("---- MathDemo: Done ----"); 
    }
    
    // ------------------------------------------------------
    // computes 'num' to the 'exponent'  Thus pow(2,3) == 8
    // ‘exponent’ is required to be non-negative integer.  
    // num and exponent are both integers. returns an integer.  
    // TODO: implement a power method
    
    // ------------------------------------------------------
    // computes n! that is n * (n-1) * (n-2) ... 3 * 2 * 1
    // factorial(0) == 1.  
    // takes an integer n and returns an integer; 
    // TODO implement a factorial method 

    // ------------------------------------------------------
    // computes the sum of all integers from 1 to maxNum inclusive.  
    // Thus sumToN(0) == 0  sumToN(3) == 6
    // takes an integer maxNum and returns an integer; 
    // TODO implement a sumToN method
    
    // ------------------------------------------------------
    // computes the sum of the squares to n.   
    // that is N*N + (N-1)*(N-1) ... 3*3 + 2*2 + 1*1
    // takes an integer maxNum and returns an integer     
    // TODO implement a sumSquares method
    
    // ------------------------------------------------------
    // returns a string that is 'str' repeated 'count' times 
    // takes a str and count argument and returns a string. 
    // TODO implement a repeat method  
    
    // ------------------------------------------------------
    // returns a string that is 'str' padded with spaces 
    // so that it has a total of 'width' characters
    // Callers should insure that the length of str <= width
    // Takes a str and width argument and returns a string.
    // TODO implement a padLeft method  
    
    // ------------------------------------------------------
    // print a table of square that starts at 1 and goes up to and includes maxN
    // +-----+-------+
    // |  N  |  N*N  |
    // +-----+-------+
    // |   1 |     1 |
    // |   2 |     4 |
    //      ... omitted ...
    // |  10 |   100 |
    //      ... omitted ...
    // | 100 | 10000 | 
    // +-----+-------+
    // You can assert that maxN*maxN < 100000 and maxN < 1000
    // Thus N will be at moat a  digit number and maxN will be
    // at most a 5 digit number.   
    public void printTableOfSquares(int maxN) {
        // TODO: Implement.
        // NOTE YOU MUST PAD INTEGERS so that the table looks pretty.  
        // Thus you immediately can use your padLeft method.  
        assert(false);   
        
        // HINT: To convert an integer to a strings use the method
        // Integer.toString(anInteger) (TODO remove this comment before submitting)
    }    
}  
