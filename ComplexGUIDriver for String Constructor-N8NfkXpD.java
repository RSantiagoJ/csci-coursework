/* Ricardo Santiago
* 2/20/16
* CSC-112
* ComplexGuiDriver
*/
import javax.swing.JOptionPane;

public class ComplexGuiDriver {

	public static void main(String[] args) {
		final String title = "Complex Numbers GUI";
		System.out.println("Running " + title + ". Have fun!!!");

		do {

			String temp;
			temp = JOptionPane.showInputDialog(null, "Enter 1st complex number", title,
					JOptionPane.INFORMATION_MESSAGE);
			Complex a = new Complex(temp);
			if (temp == null)
				break;

			temp = JOptionPane.showInputDialog(null, "Enter 2nd complex number", title,
					JOptionPane.INFORMATION_MESSAGE);
			Complex c = new Complex(temp);
			if (temp == null)
				break;

			Complex ans = null;
			String options[] = { "Add", "Subtract", "Multiply", "Divide", "Absolute Value", "Negate", "Conjugate",
					"Distance", "Equality", "Greater than", "Less Than", "Quit" };
			int option = JOptionPane.showOptionDialog(null, "Choose Operation", title, JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, options, 11);
			String output = "(" + a + ")";
			String out = "";
			switch (option) {
			case 0:
				ans = a.add(c);
				output += " + ";
				break;
			case 1:
				ans = a.subtract(c);
				output += " + ";
				break;
			case 2:
				ans = a.multiply(c);
				output += " * ";
				break;
			case 3:
				ans = a.divide(c);
				output += " / ";
				break;
			case 4:
				out += " |" + a + "|  = " + a.abs() + " \n |" + c + "| = " + c.abs() + "";
				break;
			case 5:
				out += "negate " + a + " = " + a.negate() + "\nnegate " + c + " = " + c.negate();
				break;
			case 6:
				out += "conjugate of " + a + " = " + a.conjugate() + "\nconjugate of " + c + " = " + c.conjugate();
				break;
			case 7:
				out += "distance between (" + a + ") and (" + c + ") = " + a.distance(c);
				break;
			case 8:
				out += a + " == " + c + " = " + a.equals(c);
				break;
			case 9:
				out += a + " > " + c + " = " + a.greaterThan(c);
				break;
			case 10:
				out += a + " < " + c + " = " + a.lessThan(c);
				break;

			default:
				break;
			}
			if (option > 11)
				break;

			if (option <= 3) {
				output += "(" + c + ")" + " = " + ans;
				JOptionPane.showMessageDialog(null, output, title, JOptionPane.PLAIN_MESSAGE);

			} else {
				if (option >= 4)
					JOptionPane.showMessageDialog(null, out, title, JOptionPane.PLAIN_MESSAGE);
			}

			option = JOptionPane.showConfirmDialog(null, "Do you want to do this again?", title,
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (option != JOptionPane.YES_OPTION)
				break;
		} while (true);
	}
}