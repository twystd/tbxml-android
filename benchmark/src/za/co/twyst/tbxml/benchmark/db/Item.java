package za.co.twyst.tbxml.benchmark.db;

public class Item 
       { // CONSTANTS
    
         // INSTANCE VARIABLES
    
         public final String id;
         public final String name;
         public final String grade;
         public final String stars;
         public final String rated;
         public final String order;
         public       String description;

         public Originated originated;
         public Checked      checked;

         // CONSTRUCTOR
         
         public Item(String id,String name,String grade,String stars,String rated,String order,String description)
                { this.id          = id;
                  this.name        = name;
                  this.grade       = grade;
                  this.stars       = stars;
                  this.rated       = rated;
                  this.order       = order;
                  this.description = description;
                }
         
         // INSTANCE METHODS
         
         // INNER CLASSES

         public static class Originated
                { public final String date;
                  public final String by;
                  
                  public Originated(String date,String by)
                         { this.date = date == null ? "" : date;
                           this.by   = by   == null ? "" : by;
                         }
                }

         public static class Checked
                { public final String date;
                  public final String by;
           
                  public Checked(String date,String by)
                         { this.date = date == null ? "" : date;
                           this.by   = by   == null ? "" : by;
                         }
                }
       }
