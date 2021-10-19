import java.util.*;
import SuffixTreePackage.*;

/**
 * Main class - for accessing suffix tree applications
 * David Manlove, Jan 03.  Modified by David Manlove, Jan 07 and Jan 09.
 */

public class Main {
	
	/**
	 * The main method.
	 * @param args the arguments
	 */
	public static void main(String args[]) {

		Scanner standardInput = new Scanner(System.in);
		do {   
			// display prompt for user
			System.out.println();
			System.out.print("Enter the number of the task or type 'q' to quit: ");

			// read in a line from standard input
			String line = standardInput.nextLine();
			System.out.println();

			try {
				// try to extract an integer from line if possible
				int numTask = Integer.parseInt(line);
				
				// variables used in multiple cases
				String text, searchWord;
				SuffixTree suffixTree;
				SuffixTreeAppl suffixTreeAppl;
				FileInput newInput;
				byte [] readInput;

				switch (numTask) {
				case 1:
					// get file name to be found
				    System.out.print("Enter the name of the text file: "); 
				    text = standardInput.nextLine();
				    System.out.println();
				    
					// get string to be located
					System.out.print("Enter the string to search for: "); 
				    searchWord = standardInput.nextLine();				
					
				    // read suffix tree into a byte string
					newInput = new FileInput(text);
					readInput = newInput.readFile();
					
					// turn file into suffixTreeAppl instance
					suffixTree = new SuffixTree(readInput);
					suffixTreeAppl = new SuffixTreeAppl(suffixTree);
					
				    // search for string and return result
					Task1Info task1 = suffixTreeAppl.searchSuffixTree(searchWord.getBytes());
					if (task1.getPos() == (-1)) {
						System.out.println("Search string " + '"' + searchWord + '"'
								+ " not found in " + text);
					} else {
						System.out.println("Search string " + '"' + searchWord + '"'
								+ " occurs at position " + task1.getPos() + " of " + text);
					}
					break;
					
				case 2:
					// get file name to be found
				    System.out.print("Enter the name of the text file: "); 
				    text = standardInput.nextLine();
				    System.out.println();
					
					// get string to be located
					System.out.print("Enter the string to search for: "); 
				    searchWord = standardInput.nextLine();				
					
				    // read suffix tree into a byte string
					newInput = new FileInput(text);
					readInput = newInput.readFile();
					
					// turn file into suffixTreeAppl instance
					suffixTree = new SuffixTree(readInput);
					suffixTreeAppl = new SuffixTreeAppl(suffixTree);
				    
				    //Search for occurrences and return result
				    Task2Info task2 = suffixTreeAppl.allOccurrences(searchWord.getBytes());
				    if ( task2.getPositions().isEmpty()) {
				    	System.out.println("Search string " + '"' + searchWord + '"' 
				    			+ " does not occur in " + text);
				    } else {
				    	int len = task2.getPositions().size();
				    	System.out.println("Search string " + '"' + searchWord + '"' 
				    			+ " occurs in " + text + " at positions:");
				    	
				    	// display each occurrence location
				    	int i = 0;
				    	while ( i < task2.getPositions().size() ) {
				    		System.out.println(task2.getPositions().get(i));
				    		i++;
				    	}
				    	System.out.println("The total number of occurences is " + len);
				    }
					break;
					
				case 3:
					// get file name to be found
				    System.out.print("Enter the name of the text file: "); 
				    text = standardInput.nextLine();
				    System.out.println();			
					
				    // read suffix tree into a byte string
					newInput = new FileInput(text);
					readInput = newInput.readFile();
					
					// turn file into suffixTreeAppl instance
					suffixTree = new SuffixTree(readInput);
					suffixTreeAppl = new SuffixTreeAppl(suffixTree);
				    
					// find lrs
				    Task3Info task3 = suffixTreeAppl.traverseForLrs();
				    
				    // check there was a repeated substring
				    if (task3.getLen() == 0) {
				    	System.out.println("There are no repeated substrings in " + text);
				    } else {
					    // get the longest string Found
					    String lrs = new String(readInput).substring(task3.getPos1(),
					    		task3.getPos1()+task3.getLen());
					    
					    // display results
					    System.out.println("An LRS in " + text + " is " + '"' + lrs + '"');
						System.out.println("Its Length is " + task3.getLen());
						System.out.println("Starting position of one occurence is "
								+ task3.getPos1());
						System.out.println("Starting position of another occurence is "
								+ task3.getPos2());
				    }
					break;
					
				case 4:
					// get file names to be found
				    System.out.print("Enter the name of the first text file: "); 
				    text = standardInput.nextLine();
				    System.out.println();
				    System.out.print("Enter the name of the second text file: "); 
				    String text2 = standardInput.nextLine();
				    System.out.println();
				    
				    // read suffix tree into a byte string
					newInput = new FileInput(text);
					readInput = newInput.readFile();
					
					newInput = new FileInput(text2);
					byte [] readInput2 = newInput.readFile();
					
					// turn file into suffixTreeAppl instance
					suffixTree = new SuffixTree(readInput, readInput2);
					suffixTreeAppl = new SuffixTreeAppl(suffixTree);
					
					Task4Info task4 = suffixTreeAppl.traverseForLcs(readInput.length);
					
					if (task4.getLen() == 0) {
				    	System.out.println("There are no common substrings between " 
				    			+ text + " and " + text2);
				    } else {
				    	// display results
					    String lcs = new String(readInput).substring(task4.getPos1(),
					    		task4.getPos1()+task4.getLen());
					    System.out.println("An LCS of " + text + " and " + text2 + 
					    		" is " + '"' + lcs + '"');
					    System.out.println("Its Length is " + task4.getLen());
					    System.out.println("Starting position of one occurence is " 
					    		+ task4.getPos1());
					    System.out.println("Starting position of another occurence is " 
					    		+ task4.getPos2());
				    }
					break;

				default: throw new NumberFormatException();
				}
			}
			catch (NumberFormatException e) {
				if (line.length()==0 || line.charAt(0)!='q')
					System.out.println("You must enter either '1', '2', '3', '4' or 'q'.");
				else
					break;
			}
		} while (true);
		standardInput.close();
	}
}