// Ricardo Santiago
// Date: 02/26/16
// CSC-112
// Problem 1
// Hello World Example 2
// rjsantiago0001@student.stcc.edu
// Hello2.java

	// A first applet in Java
	// (More info needed in this global comment area) 

	import javax.swing.JApplet;  // import class JApplet
	import java.awt.Graphics;    // import class Graphics

	public class Hello2 extends JApplet {  
	   public void paint( Graphics g )
	   {
	      super.paint(g);
	      g.drawString( "Welcome to Java Programming!", 25, 25 );
	   }
	}