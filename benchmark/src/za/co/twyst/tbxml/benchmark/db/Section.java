package za.co.twyst.tbxml.benchmark.db;

import java.util.List;

public class Section 
       { // CONSTANTS
    
         // INSTANCE VARIABLES
    
         public final String     id;
         public final String     name;
         public final String     order;
         public final List<Item> items;
         
         // CONSTRUCTOR
         
         public Section(String id,String name,String order,List<Item> items)
                { this.id    = id;
                  this.name  = name;
                  this.order = order;
                  this.items = items;
                }

         // INSTANCE METHODS
         
		public void add(Item item)
		       { if (item != null)
		            { items.add(item);
		            }
		       }
       }
