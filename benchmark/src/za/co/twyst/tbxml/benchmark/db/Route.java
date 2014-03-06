package za.co.twyst.tbxml.benchmark.db;

public class Route 
       { // CONSTANTS
    
         // INSTANCE VARIABLES
    
         public final String id;
         public final String name;
         public final String grade;
         public final String stars;
         public final String bolts;
         public final String order;
         public       String description;

         public FirstAscent firstAscent;
         public Bolted      bolted;

         // CONSTRUCTOR
         
         public Route(String id,String name,String grade,String stars,String bolts,String order,String description)
                { this.id          = id;
                  this.name        = name;
                  this.grade       = grade;
                  this.stars       = stars;
                  this.bolts       = bolts;
                  this.order       = order;
                  this.description = description;
                }
         
         // INSTANCE METHODS
         
         // INNER CLASSES

         public static class FirstAscent
                { public final String date;
                  public final String by;
                  
                  public FirstAscent(String date,String by)
                         { this.date = date == null ? "" : date;
                           this.by   = by   == null ? "" : by;
                         }
                }

         public static class Bolted
                { public final String date;
                  public final String by;
           
                  public Bolted(String date,String by)
                         { this.date = date == null ? "" : date;
                           this.by   = by   == null ? "" : by;
                         }
                }
       }
