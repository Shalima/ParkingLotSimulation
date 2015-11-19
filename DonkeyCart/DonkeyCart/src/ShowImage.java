
import java.awt.BasicStroke;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
public class ShowImage extends JFrame{
	static ResultSet rs;
	static String query;
	public static void showFram(String querynew)
	{
		query = querynew;
		ShowImage f = new ShowImage();
		f.setTitle("Donkey Cart");
		f.setBounds(20,20,400,400);
		f.setVisible(true);
	}
	public ShowImage() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				dispose();
			}
		} );
		Container contentPane = getContentPane();
		contentPane.add(new myPanel());
	}
	static class myPanel extends JPanel {
		@SuppressWarnings("unused")
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			String url;
			url = "jdbc:oracle:thin:@localhost:1522:orcl"; // jdbc is 'protocol', 
			//oracle is 'subprotocol',
			// and thin is the driver; cs514 is the data base
			// the 'database' format is host:port:sid 
			Statement stmt;
			Connection con;
			try { // invoke the oracle thin driver; register it with DriverManager
				Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			}   // this step 'loads' the drivers for Oracle that are 'found'
			catch (Exception e) {
				System.out.println("MR.UnitSitQueries.constructor.Exception: " +
						e);
			}

			try {
				con = DriverManager.getConnection(url,"system", "Chanchalakshi1"); // establish
				// connection to DBMS or database
				stmt = con.createStatement(); // creates object from which SQL commands
				// can be sent to the DBMS
				rs = stmt.executeQuery(query); //create result object to hold
				// information returned by the DBMS
				while (rs.next()) {
					//Get the function value
					java.sql.Struct o1 = (java.sql.Struct)rs.getObject(1);						
					oracle.sql.ARRAY oa3 = (oracle.sql.ARRAY)o1.getAttributes()[3];
					oracle.sql.ARRAY oa4 = (oracle.sql.ARRAY)o1.getAttributes()[4];
					int [] ia3 = oa3.getIntArray();
					double [] ia4 = oa4.getDoubleArray();
					int x[]= new int[ia4.length/2];
					int y[]= new int [ia4.length/2];
					int type = ia3[1];
					int interpretation = ia3[2];
					int k=0;
					int l=0;
					
					//load the coordinates
					for (int j=0;j<ia4.length;j++){
						if (j%2==0){
							x[k]=(int) ((ia4[j]*20));
							k++;
						}
						else{
							y[l]=(int) (400-ia4[j]*20);
							l++;
						}
					}

					//Draw the image
					for(int r=0; r<ia3.length; r=r+3){
						int offset = (int) Math.floor(ia3[r]/2);
						type = ia3[r+1];
						interpretation = ia3[r+2];
						int x0 = x[offset];
						int y0=y[offset];
						int x1=x[offset+1];
						int y1=y[offset+1];
						if((type==1003 || type==2003) && interpretation==3){
							drawRectangle(g, x0, y0, x1, y1);
						}
						else if((type==2 || type == 1003) && interpretation==1){
							drawLines(g, g2, ia3, ia4, x, y, r, offset, x0, y0, x1, y1);
						}
						else if(type==2 && interpretation==2){
							int x2=x[offset+2];
							int y2=y[offset+2];
							drawArcs(g2, x0, y0, x1, y1, x2, y2);
						}
						else if((type==1003 || type==2003) && interpretation==4){
							drawCircle(g, x, x0, y0);  // x,y,w,h
						}
						else if(type==1 && interpretation==1){
							g2.drawLine(x0, y0, x0, y0);
						}
						else if((type==2003 || type==1003) && interpretation==2){
							int x2=x[offset+2];
							int y2=y[offset+2];	
							for(int i=2; i<x.length; i=i+2){
								drawArcs(g2, x[i-2], y[i-2], x[i-1], y[i-1], x[i], y[i]);
							}
						}
					}
				}
				stmt.close();
				con.close();
			}
			catch (SQLException e){System.out.println("OOPS" + e.getMessage());}


		}

		/**
		 * Draws the circle
		 * 
		 * @param g
		 * @param x
		 * @param x0
		 * @param y0
		 */
		private void drawCircle(Graphics g, int[] x, int x0, int y0) {
			int width=((x[1]-x[0])>0)? 2*(x[1]-x[0]):2*(x[0]-x[1]);
			int hieght=width;
			int X =x0;
			int Y=y0-hieght;
			g.drawOval(X,Y,width,hieght);
		}

		/**
		 * Draws the arc
		 * 
		 * @param g2
		 * @param x0
		 * @param y0
		 * @param x1
		 * @param y1
		 * @param x2
		 * @param y2
		 */
		private void drawArcs(Graphics2D g2, int x0, int y0, int x1, int y1,
				int x2, int y2) {
			GeneralPath path = new GeneralPath();
			path.moveTo(x0, y0);

			Point point1 = new Point(x0, y0);
			Point point2 = new Point(x1, y1);
			Point point3 = new Point(x2, y2);

			path.curveTo(point1.x, point1.y, point2.x, point2.y, point3.x, point3.y);
			g2.draw(path);
		}

		/**
		 * Draws the lines
		 * 
		 * @param g
		 * @param g2
		 * @param ia3
		 * @param ia4
		 * @param x
		 * @param y
		 * @param r
		 * @param offset
		 * @param x0
		 * @param y0
		 * @param x1
		 * @param y1
		 */
		private void drawLines(Graphics g, Graphics2D g2, int[] ia3, double[] ia4,
				int[] x, int[] y, int r, int offset, int x0, int y0, int x1, int y1) {
			if(ia3.length == r+3 && offset == 0){
				g.drawPolyline(x, y, ia4.length/2);
			}
			else 
				g2.draw(new Line2D.Double(x0, y0, x1, y1));
		}

		/**
		 * Draws the rectangle
		 * 
		 * @param g
		 * @param x0
		 * @param y0
		 * @param x1
		 * @param y1
		 */
		private void drawRectangle(Graphics g, int x0, int y0, int x1, int y1) {
			int X=x0;
			int Y=y1; //left top corner
			int width= ((x0-x1)>0)? x0-x1:x1-x0;
			int height=((y1-y0)>0)? y1-y0: y0-y1;
			g.drawRect(X, Y, width, height);
		}

	}
}



