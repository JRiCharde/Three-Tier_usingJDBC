/*
 Name: William Jeremy RiCharde
 Course: CNT 4714 - Summer 2014 - Project 4
 Assignment title: Developing A Three-Tier Disributed Web-Based Application
 Date: July 11, 2014
 
 */



// DBservlet.java
//

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import java.util.*; // for Lists

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


// servlet that connects to database and returns an html result page
public class DBservlet extends HttpServlet {
    
    private Connection connection;
    private Statement statement1;
    private Statement statement2;
    
    // set up database connection and create SQL statement
    public void init( ServletConfig config ) throws ServletException
    {
        // attempt database connection and create Statement
        try
        {
            Class.forName( config.getInitParameter( "databaseDriver" ) );
            connection = DriverManager.getConnection(
            config.getInitParameter( "databaseName" ),
            config.getInitParameter( "username" ),
            config.getInitParameter( "password" ) );
     
            
            // create Statement to query database
            statement1 = connection.createStatement();
            statement2 = connection.createStatement();
            
        } // end try
        // for any exception throw an UnavailableException to
        // indicate that the servlet is not currently available
        catch ( Exception exception )
        {
            exception.printStackTrace();
            throw new UnavailableException( exception.getMessage() );
        } // end catch
    }  // end method init
    
    
    // retrieve info from form and create new webpage displaying results
    protected void doPost( HttpServletRequest request,
                          HttpServletResponse response )
    throws ServletException, IOException
    {
        // get query from form
        String clientQuery = request.getParameter( "queryArea" );
        // put default query if none
        String sql = clientQuery;
        if ( sql.length() == 0 )
        {
            sql = "Select * From suppliers";
        }
        
        // set up response to client
        response.setContentType( "text/html" );
        PrintWriter out = response.getWriter();
        
        // start XHTML document
        out.println( "<?xml version = \"1.0\"?>" );
        out.println( "<!DOCTYPE html>" );
        out.println("<html xmlns = \"http://www.w3.org/1999/xhtml\">" );
        
        // head section of document
        out.println( "<head>" );
        out.println(" <link rel='stylesheet' type='text/css' " +
                    "href='css/form.css' />");
        
        out.println( "<form action = '/Project4/' class = 'return' > " );
        out.println( "<p>" );
        out.println( "<input type = 'submit' value = 'Return to Query' />" );
        out.println( "</p>" );
        out.println( "</form>" );
        
        
        // determine first character to see which SQL query to executy
        char firstLetter = sql.charAt(0);
        if ( firstLetter == 's' || firstLetter == 'S' )
        {
            selectQuery(sql, out);  // for select querys
        }
        else
        {
            updateQuery( sql, out );    // for updates, inserts and deletes
        }
        
        out.println( "</pre></body></html>" );
        out.close();
        
    } // end method doPost
    
    
    // executy Select querys and display results in html
    public void selectQuery( String sql, PrintWriter out )
    {
        // process query
        try
        {
            // create resultSet for query
            ResultSet resultsRS = statement1.executeQuery( sql );
            
            // retrieve column names
            ResultSetMetaData rsmd = resultsRS.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++ ) {
                String name = rsmd.getColumnName(i);
            }
            
            // more html
            out.println( "<pre><title>Database Results</title>" );
            out.println( "</head>" );
            
            out.println( "<body>" );
            out.println ("<font size = 4>");
            out.println ("</font>");
            out.println ("<div class='container'>");
            out.println ("<label>Database Results:</label>");
            out.println ("</p><pre>" );
            out.println ("<table>");
            out.println ("<thead>");;
            out.println ("<tr>");
            
            // print column head names
            for (int i = 1; i <= columnCount; i++ )
            {
                out.println ("<th>" + rsmd.getColumnName(i) + "</th>");
            }
            
            // more html
            out.println ("</tr>");
            out.println ("</thead>");
            out.println("<tbody>");
            
            // fill in table with values
            while ( resultsRS.next() )
            {
                out.println("<tr>");
                for (int i = 1; i <= columnCount; i++ )
                {
                    out.println("<td>");
                    out.print( resultsRS.getString( i ) );
                    out.println("</td>");
                }
                out.print( "</tr>" );
            } // end while
            
            out.println("</tbody>");
            out.println("</table>");
            out.println ("</div>");
            resultsRS.close();
            // end XHTML document
            
        } // end try
        // if database exception occurs, return error page
        catch ( SQLException e )
        {
            e.printStackTrace();
            
            out.println( "<pre><title>Error</title>" );
            out.println( "</head>" );
            
            out.println( "<body>" );
            out.println ("<body>");
            out.println ("<font size = 4> <b>");
            out.println ("</font>");
            out.println ("<div class='container'>");
            out.println ("<label>Database Results:</label>");
            out.println ("<table>");
            out.println ("<thead>");;
            out.println ("<tr>");
            out.println ("<th>" );
            
            out.println( "Error executing the SQL statement: <br/>" );
            out.println( e.getMessage() + "<br/></th></tr>");
            out.println("</tbody>");
            
        } // end catch
    }// end method select query
    
    
    // execute all other SQL querys and display results in html
    public void updateQuery( String sql, PrintWriter out )
    {
        // process query
        try
        {
            // create string to query for business logic
            String blSQL = "Select snum, pnum, jnum From shipments where"
            + " quantity >= 100";

            // create result set to query for business logic before SQL
            //  is executed
            ResultSet updateRS = statement2.executeQuery( blSQL );
            ResultSetMetaData updateRSMD = updateRS.getMetaData();
            
            // creates a list of shipments where quantity >= 100 before
            //  SQL executed
            List<Shipment> shipList = new ArrayList<Shipment>();
            while(updateRS.next())
            {
                String snum = updateRS.getString("snum");
                String pnum = updateRS.getString("pnum");
                String jnum = updateRS.getString("jnum");
                Shipment nextShip = new Shipment(snum, pnum, jnum );
                shipList.add( nextShip );
                
            }
            updateRS.close();
            
            
            // execute the initial client query
            int affected = statement1.executeUpdate( sql );
            
            // get result set again to query for business logic after
            // the SQL is executed
            updateRS = statement2.executeQuery( blSQL );
            updateRSMD = updateRS.getMetaData();
            
            // creates a list of shipments where quantity >= 100 after
            //  SQL executed
            List<Shipment> newShipList = new ArrayList<Shipment>();
            while(updateRS.next())
            {
                String snum = updateRS.getString("snum");
                String pnum = updateRS.getString("pnum");
                String jnum = updateRS.getString("jnum");
                Shipment nextShip = new Shipment(snum, pnum, jnum );
                newShipList.add( nextShip );
                
            }
            updateRS.close();
            
            
            // remove from the after list, any shipments that were already
            // in the before list.  The result will be all shipments that
            // increased to at least 100 quantity as a result of the SQL execution
            for (Shipment before : shipList )
            {
                newShipList.remove( before );
            }
            
            // more html
            out.println( "<pre><title>Database Results</title>" );
            out.println( "</head>" );
            out.println ("<body >");
            out.println ("<font size = 4> <b>");
            out.println ("</b><br>");
			out.println ("</font>");
            out.println ("<div class='container'>");
            out.println ("<label>Database Results:</label>");
            out.println ("<table>");
            out.println ("<thead>");;
            out.println ("<tr>");
            out.println ("<th class = 'update'>");
            // print out the # of rows affected by the client query
            out.println ("The statement executed successfully. <br>" );
            out.println (affected + " row(s) affected. <br>");
            out.println ("</th>");
            out.println ("</tr>");
            out.println ("</thead>");
            out.println("<tbody>");
            // print out the results of the business logic
            printBusinesLogic( newShipList, out );
            out.println("</tbody>");
            out.println("</table>");
            out.println ("</div>");
            
		} // end try
        // if database exception occurs, return error page
        catch ( SQLException e )
        {
            e.printStackTrace();
            
            out.println( "<pre><title>Error</title>" );
            out.println( "</head>" );
            out.println( "<body>" );
            out.println ("<body >");
            out.println ("<font size = 4> <b>");
            out.println ("</b><br>");
			out.println ("</font>");
            out.println ("<div class='container'>");
            out.println ("<label>Database Results:</label>");
            out.println ("<table>");
            out.println ("<thead>");;
            out.println ("<tr>");
            out.println ("<th>" );
            out.println( "Error executing the SQL statement: <br/>" );
            out.println( e.getMessage() + "<br/></th></tr>");
            out.println("</tbody>");
            out.println("</table>");
            
        } // end catch
    }
    
    // prints out the results of the business logic
    public void printBusinesLogic( List<Shipment> shipList, PrintWriter out )
    {
        try
        {
            out.println("<tr><td>");
            out.println("Business Logic Detected!  Updating Supplier Status");
            out.println("</td></tr>");
            
            // initialze count of suppliers affected by business logic
            int count = 0;
            for (Shipment shipment : shipList) {
                
                count++;
                
                // get the snum for each shipment affected by logic
                String snum = shipment.getSNum();
                
                // following used for error checking - comment out
                /*
                out.println("<tr><td>");
                out.println(snum);
                out.println("</tr></td>");
                */
                
                // execute update to increase status of affected suppliers
                String updateBLSQL = "Update suppliers Set status = status + 5"
                                    + " Where snum = '" + snum + "'";
                statement1.executeUpdate( updateBLSQL );
                
            }
            
            out.println("<tr><td>");
            out.println("Business Logic updated " + count + " supplier status "
                        + "marks." );
            out.println("</td></tr>");
            
        }
        catch ( SQLException e )
        {
            
            out.println( "Error executing the SQL statement: <br/>" );
            out.println( e.getMessage() + "<br/></th></tr>");
        }
    }// end method rintBusinessLogic
    
    
    
    // close SQL statements and database when servlet terminates
    public void destroy()
    {
        // attempt to close statements and database connection
        try
        {
            statement1.close();
            statement2.close();
            connection.close();
        } // end try
        // handle database exceptions by returning error to client
        catch( SQLException sqlException )
        {
            sqlException.printStackTrace();
        } // end catch
    } // end method destroy
    

}// end class DBservlet