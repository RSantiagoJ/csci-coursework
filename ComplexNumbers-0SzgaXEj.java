/*
 * Ricardo Santiago
 * Complex Numbers
 * CSC-112
 * 2/19/2016
 */

public class Complex {

//finalize the variables to make immutable
	private final double re;
	private final double im;

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

	// I am currently looking for a faster method to calculate this, possibly using conjugate
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
		return re + " + " + im + "i";
	}

}