package za.co.twyst.tbxml.benchmark.db;

import java.util.List;

public class Section 
       { // CONSTANTS
    
         // INSTANCE VARIABLES
    
         public final String      id;
         public final String      name;
         public final String      order;
         public final List<Route> routes;
         
         // CONSTRUCTOR
         
         public Section(String id,String name,String order,List<Route> routes)
                { this.id     = id;
                  this.name   = name;
                  this.order  = order;
                  this.routes = routes;
                }
       }
