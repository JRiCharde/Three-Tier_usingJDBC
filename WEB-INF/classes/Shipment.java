/*
 Name: William Jeremy RiCharde
 Course: CNT 4714 - Summer 2014 - Project 4
 Assignment title: Developing A Three-Tier Disributed Web-Based Application
 Date: July 11, 2014
 
 */


// a class to keep track of the individual shipments
public class Shipment
{
    private String snum;
    private String pnum;
    private String jnum;
    
    // constructor
    public Shipment ( String s, String p, String j )
    {
        snum = s;
        pnum = p;
        jnum = j;
    }
    
    
    // get methods
    public String getSNum()
    {
        return snum;
    }
    
    public String getPNum()
    {
        return pnum;
    }
    
    public String getJNum()
    {
        return jnum;
    }
    
    // equals method so List methods work
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if (!(obj instanceof Shipment) )
        {
            return false;
        }
        Shipment other = (Shipment) obj;
        
        return ( other.getSNum().equals(this.snum) &&
        other.getPNum().equals(this.pnum) &&
        other.getJNum().equals(this.jnum) );
        
    }// end method equals
    
    // hashCode method so equals and List methods work
    public int hashCode()
    {
        return this.snum.hashCode() + this.pnum.hashCode()
                + this.jnum.hashCode();
    }
    
}// end class Shipment


