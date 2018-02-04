package pl.mysior;

import java.util.Scanner;

public abstract class Client {
	private Scanner scan = new Scanner(System.in);

	public String command(String aCommand, String aDefault) {
		String correctInput = "";
			System.out.print("Insert " + aCommand + " : (press enter if default(" + aDefault + ")): ");
			String input = scan.nextLine();
			if (input.length() > 0) {
				correctInput = input;
			}
			else
			{
				correctInput = aDefault;
			}
		return correctInput;
	}
	public int command(String aCommand, int aDefault) {
		int correctInput = 0;
		System.out.print("Insert " + aCommand + " : (press enter if default(" + aDefault + ")): ");
		String input = scan.nextLine();
		if (input.length() > 0) {
			correctInput = Integer.parseInt(input);
		}
		else
		{
			correctInput = aDefault;
		}
	return correctInput;
}

}
