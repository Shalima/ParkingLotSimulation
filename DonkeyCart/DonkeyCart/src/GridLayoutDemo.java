import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class GridLayoutDemo extends JFrame {
	//Set the buttons
	JButton query1 = new JButton("Display donkey cart");
	JButton query2 = new JButton("Parts in touch with tail");
	JButton query3 = new JButton("Calculate the area of the wheel");
	JButton query4 = new JButton("Show convex hull for tree top");
	JButton query5 = new JButton("Show difference of 2 wheels");
	JButton query6 = new JButton("Show union of cart and the log above");
	JButton query7 = new JButton("Show intersection of 2 wheels");
	JButton query8 = new JButton("The distance between birdwings");
	JButton query9 = new JButton("Show Arc densify of the tree top");
	JButton query10 = new JButton("Show MBR of the tree top");
	JButton query11 = new JButton("Calculate minimum MBR ordinate of tree top");
	JButton query12 = new JButton("Delete tail");
	JButton query13 = new JButton("Add tail");
	JTextField text1 = new JTextField();
	JTextField text2 = new JTextField();
	JTextField text3 = new JTextField();
	GridLayout Layout = new GridLayout(30,30);

	public GridLayoutDemo(String name) {
		super(name);
		setResizable(true);
	}


	public void addComponentsToPane(final Container pane) {
		final JPanel P = new JPanel();
		P.setLayout(Layout);
		JPanel controls = new JPanel();
		GridLayout layout = new GridLayout(20,20);
		controls.setLayout(layout);

		//Add controls to set up horizontal and vertical gaps
		controls.add(query1);
		controls.add(query2);
		controls.add(query3);
		controls.add(text2);
		controls.add(query4);
		controls.add(query5);
		controls.add(query6);
		controls.add(query7);
		controls.add(query8);
		controls.add(text1);
		controls.add(query9);
		controls.add(query10);
		controls.add(query11); 
		controls.add(text3);
		controls.add(query12);
		controls.add(query13);
		query13.setEnabled(false);

		//Process the button press
		query1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String query= "select * from samplecart1";
				DrawDonkeyCart.showFram(query);  
			}
		});
		query2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String query= "SELECT * FROM samplecart1 d1, samplecart1 d2 WHERE d2.NAME='tail' AND d1.NAME<>'tail' AND SDO_RELATE(d1.SHAPE,d2.SHAPE,'MASK=TOUCH')='TRUE'";
				DrawDonkeyCart.showFram(query);  
			}
		});
		query3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String query= "SELECT sdo_geom.sdo_area(shape, 0.005) FROM samplecart1 WHERE name = 'wheel'";
				text2.setText(executeQuery(query)+" units");;  
			}
		});
		query4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String query= "SELECT SDO_GEOM.SDO_CONVEXHULL(shape, 0.005) FROM samplecart1 WHERE name='tree top'";
				ShowImage.showFram(query);  
			}
		});
		query5.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String query= "SELECT sdo_geom.sdo_difference(d1.shape, d2.shape, 0.005) FROM samplecart1 d1, samplecart1 d2 WHERE d1.name = 'wheel' AND d2.name = 'wheel2'";
				ShowImage.showFram(query);  
			}
		});
		query6.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				String query= "SELECT sdo_geom.sdo_union(d1.shape, d2.shape, 0.005) FROM samplecart1 d1, samplecart1 d2 WHERE d1.name = 'log1' AND d2.name = 'log2'";
				ShowImage.showFram(query);  
			}
		});
		query7.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				String query= "SELECT sdo_geom.sdo_intersection(d1.shape, d2.shape, 0.005) FROM samplecart1 d1, samplecart1 d2 WHERE d1.name = 'wheel' AND d2.name = 'wheel2'";
				ShowImage.showFram(query);  
			}
		});
		query8.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				String query= "SELECT sdo_geom.sdo_distance(d1.shape, d2.shape, 0.005) FROM samplecart1 d1, samplecart1 d2 WHERE d1.name = 'birdwing1' AND d2.name = 'birdwing2'";
				text1.setText(executeQuery(query)+" units"); 
			}
		});
		query9.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				String query= "select sdo_geom.sdo_arc_densify(shape,  0.005, 'arc_tolerance=0.005') from samplecart1 where name = 'tree top'";
				ShowImage.showFram(query);  
			}
		});
		query10.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				String query= "SELECT SDO_GEOM.SDO_MBR(d1.shape, m.diminfo) "
						+ "FROM samplecart1 d1, user_sdo_geom_metadata m "
						+ "WHERE m.table_name = 'SAMPLECART1' AND m.column_name = 'SHAPE' "
						+ "AND d1.name = 'tree top'";
				ShowImage.showFram(query);  
			}
		});
		query11.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				String query= "SELECT SDO_GEOM.SDO_MIN_MBR_ORDINATE(d1.shape, m.diminfo, 1) "
						+ "FROM samplecart1 d1, user_sdo_geom_metadata m "
						+ "WHERE m.table_name = 'SAMPLECART1' AND m.column_name = 'SHAPE' "
						+ "AND d1.name = 'tree top'";
				text3.setText("("+executeQuery(query)+")");
			}
		});



		query12.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				String query= "delete from samplecart1 where id = '16'";
				executeUpdate(query); 
				query13.setEnabled(true);
				query= "select * from samplecart1";
				DrawDonkeyCart.showFram(query);

			}
		});
		query13.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){                 
				String query= "INSERT INTO samplecart1 VALUES (16,'tail', "
						+ "SDO_GEOMETRY (2002, null, null, "
						+ "SDO_ELEM_INFO_ARRAY(1,2,1), "
						+ "SDO_ORDINATE_ARRAY(10,2.5, 10.25,1, 10.5,2.5)))";
				executeUpdate(query);
				query13.setEnabled(false);
				query= "select * from samplecart1";
				DrawDonkeyCart.showFram(query);

			}
		});


		pane.add(controls, BorderLayout.CENTER);
	}


	private static void createAndShowGUI() {
		//Create and set up the window.
		GridLayoutDemo frame = new GridLayoutDemo("Query List");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Set up the content pane.
		frame.addComponentsToPane(frame.getContentPane());
		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * This method is used to execute function queries and return the value
	 * @param query
	 * @return
	 */
	static String executeQuery(String query){
		ResultSet rs;
		String url;
		float distance = 0;
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
			while(rs.next()){
				distance = rs.getFloat(1);
			}
			stmt.close();
			con.close();
		}
		catch (SQLException e){System.out.println("OOPS" + e.getMessage());}
		return Float.toString(distance);
	}

	/**
	 * This method executes updates in the table
	 * @param query
	 */
	static void executeUpdate(String query){

		String url = "jdbc:oracle:thin:@localhost:1522:orcl"; // jdbc is 'protocol', 
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
			stmt.executeUpdate(query); //create result object to hold
			stmt.close();
			con.close();
		}
		catch (SQLException e){System.out.println("OOPS" + e.getMessage());}
	}


	public static void main(String[] args) {
		/* Use an appropriate Look and Feel */
		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}

