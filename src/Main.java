import java.io.*;
import java.util.Scanner;

/**
 * Main class for RedBlackTree
 * 
 * This class is used to read commands from an input file and perform the
 * corresponding operations on an instance of RedBlackTree.
 * The result of each operation is printed on a separate output file.
 * When run, this program accepts two command line arguments. The first
 * argument is the name of the input file listing the commands to perform,
 * and the second argument is the name of the output file to write the
 * results to. The first line of the input file must have the word "Integer"
 * or the word "String" to indicate the type parameter of the RedBlackTree.
 * If any data type other than Integer or String is used, the output file will
 * display an error message.
 * 
 * @author John Dixon
 *
 */
public class Main {
	
	/**
	 * The main method of the program
	 * 
	 * @param args - an array containing the arguments passed to the
	 * program from the command line; args[0] holds the name of the
	 * input file and args[1] holds the name of the output file.
	 */
	public static void main(String[] args) {
		
		if (args.length < 2) {
			System.err.println("Error: Invalid argument(s)");
		} else {
			Scanner sc = null;
			PrintWriter pw = null;
			String token = null;
			String[] cmd = null;
			
			try {
				sc = new Scanner(new BufferedReader(new FileReader(args[0])));
				pw = new PrintWriter(new BufferedWriter(new FileWriter(args[1])));
				
				if (sc.hasNext()) {
					
					String object = sc.next();
					
					if (object.equalsIgnoreCase("Integer")) {				// if object is Integer
						
						RedBlackTree<Integer> intTree = new RedBlackTree<>();
						
						while (sc.hasNext()) {
							try {
								token = sc.next();
								cmd = token.split(":");
								
								if (cmd.length == 0) {				
									
									pw.println("Error in Line: " + token);		
									
								} else if (cmd.length < 2) {	
									
									switch (cmd[0].toLowerCase()) {
										case "printtree":
											pw.println(intTree.toString());
											break;
										default:
											pw.println("Error in Line: " + token);
									}									
									
								} else if (cmd[1].matches("-?\\d+")) {	// if element can be parsed as an Integer
									
									switch (cmd[0].toLowerCase()) {
										case "insert":
											if (intTree.insert(Integer.parseInt(cmd[1])))
												pw.println("True");
											else
												pw.println("False");
											break;
										case "contains":
											if (intTree.contains(Integer.parseInt(cmd[1])))
												pw.println("True");
											else
												pw.println("False");
											break;
										default:
											pw.println("Error in Line: " + token);
									}
									
								} else {
									
									pw.println("Error in Line: " + token);
									
								}
							} catch (NullPointerException e) {
								pw.println(e.getMessage());
							}
						}
						
					} else if (object.equalsIgnoreCase("String")) {				// if object is String
						
						RedBlackTree<String> stringTree = new RedBlackTree<>();
						
						while (sc.hasNext()) {
							try {
								token = sc.next();
								cmd = token.split(":");
								
								if (cmd.length == 0) {				
									
									pw.println("Error in Line: " + token);		
									
								} else if (cmd.length < 2) {	
									
									switch (cmd[0].toLowerCase()) {
										case "printtree":
											pw.println(stringTree.toString());
											break;
										default:
											pw.println("Error in Line: " + token);
									}									
									
								} else {
									
									switch (cmd[0].toLowerCase()) {
										case "insert":
											if (stringTree.insert(cmd[1]))
												pw.println("True");
											else
												pw.println("False");
											break;
										case "contains":
											if (stringTree.contains(cmd[1]))
												pw.println("True");
											else
												pw.println("False");
											break;
										default:
											pw.println("Error in Line: " + token);
									}
									
								}
							} catch (NullPointerException e) {
								pw.println(e.getMessage());
							}
						}
						
					} else {						// if object is neither Integer nor String
						pw.println("Only works for objects Integers and Strings");
					}
				}
				
			} catch (FileNotFoundException e) {
				System.err.println("Error: " + args[0] + " could not be opened");
			} catch (IOException e) {
				System.err.println("Error: " + args[1] + " could not be opened");
			} finally {
				if (sc != null)
					sc.close();
				if (pw != null)
					pw.close();
			}
		}
	}
}