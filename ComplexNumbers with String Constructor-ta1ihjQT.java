
/*
 * Ricardo Santiago
 * Complex Numbers with String Constructor
 * CSC-112
 * 2/29/2016
 */

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Complex {

	// finalize the variables to make immutable
	private final double re;
	private final double im;

	// Bonus string Constructor using regex
	public Complex(String str) {

		ArrayList<Double> list = new ArrayList<Double>();
		

		
		Pattern p = Pattern.compile("[-+]?[0-9]*\\.?[0-9]");
		// toString() handles the letter i

		// find positive and negative doubles in string and add to list
		Matcher m = p.matcher(str);
		while (m.find()) {
			double num = Double.parseDouble(m.group());
			list.add(num);
		}
		this.re = list.get(0);
		this.im = list.get(1);
	}

	public Complex(double real, double imag) {
		this.re = real;
		this.im = imag;
	}

	public Complex(double real) {
		this.re = real;
		this.im = 0;
	}

	public Complex() {
		this.re = 0;
		this.im = 0;
	}

	public Complex add(Complex c) {
		Complex a = this;
		double real = a.re + c.re;
		double imag = a.im + c.im;
		return new Complex(real, imag);
	}

	public Complex subtract(Complex c) {
		Complex a = this;
		return a.add(c.negate());
	}

	public Complex multiply(Complex c) {
		Complex a = this;
		double real = (a.re * c.re) - (a.im * c.im);
		double imag = (a.re * c.im) + (a.im * c.re);
		return new Complex(real, imag);
	}

	// I am currently looking for a faster method to calculate this, possibly
	// using conjugate
	public Complex divide(Complex c) {
		Complex a = this;
		double real = ((a.re * c.re) + (a.im * c.im)) / ((c.re * c.re) + (c.im * c.im));
		double imag = ((a.im * c.re) - (a.re * c.im)) / ((c.re * c.re) + (c.im * c.im));
		return new Complex(real, imag);
	}

	public double abs() {
		return Math.hypot(re, im);
	}

	public Complex negate() {
		return new Complex(-re, -im);
	}

	public Complex conjugate() {
		return new Complex(re, -im);
	}

	public double distance(Complex c) {
		return this.subtract(c).abs();

	}

	public boolean equals(Complex c) {
		double diff;
		if (this.greaterThan(c) == true)
			diff = (this.abs() - c.abs()) / this.abs();
		else
			diff = (c.abs() - this.abs()) / c.abs();

		if (diff < 1E-6)
			return true;
		else
			return false;
	}

	public boolean greaterThan(Complex c) {
		if (this.abs() > c.abs())
			return true;
		else
			return false;
	}

	public boolean lessThan(Complex c) {
		if (this.abs() < c.abs())
			return true;
		else
			return false;
	}

	public String toString() {
		if (im == 0)
			return re + "";
		if (re == 0)
			return im + "i";
		if (im < 0)
			return re + " - " + (-im) + "i";
		else
			return re + " + " + im + "i";
	}

}