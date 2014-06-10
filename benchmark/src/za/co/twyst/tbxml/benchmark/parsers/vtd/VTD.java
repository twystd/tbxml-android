package za.co.twyst.tbxml.benchmark.parsers.vtd;

import java.util.ArrayList;
import java.util.List;

import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

import android.util.Log;

import za.co.twyst.tbxml.benchmark.db.Item;
import za.co.twyst.tbxml.benchmark.db.Section;
import za.co.twyst.tbxml.benchmark.parsers.Parser;

public class VTD extends Parser 
       { // CONSTANTS
    
		 private static final String TAG = VTD.class.getSimpleName();
    
         // INSTANCE VARIABLES
         
         private final String xml;
         private final VTDGen vtd;
         
         // CLASS METHODS
         
         private static String attribute(VTDNav navigator,String attribute) throws NavException 
                 { int index = navigator.getAttrVal(attribute); 
                 
                   if (index != -1)
        	          { return navigator.toNormalizedString(index);
        	          }
                   
                   return "";
                 }
         
         private static String text(VTDNav navigator) throws NavException 
                 { int index = navigator.getText(); 
                 
                   if (index != -1)
                      { return navigator.toNormalizedString(index);
                      }
                   
                   return "";
                 }
         
         // CONSTRUCTOR
         
         public VTD(String xml)
                { this.xml = xml == null ? "" : xml;
                  this.vtd = new VTDGen();
                }

         // *** Parser ***

         @Override
         public long parse(int iterations) throws Exception 
                { long start = System.currentTimeMillis();
	             
                  for (int i=0; i<iterations; i++)
                      { vtd.setDoc(xml.getBytes());
                        vtd.parse (true);
                	  
                	    List<Section> sections  = new ArrayList<Section>();
                        VTDNav        navigator = vtd.getNav();

                        navigator.toElement(VTDNav.ROOT);
                        
                        if (navigator.toElement(VTDNav.FIRST_CHILD,SECTION))
                           { do { String     id      = attribute(navigator,ID);
                                  String     name    = attribute(navigator,NAME);
                                  String     order   = attribute(navigator,ORDER);
                                  List<Item> items   = parse(navigator);
                                  Section    section = new Section(id,name,order,items);
                              
                                  sections.add(section);
                                } while(navigator.toElement(VTDNav.NEXT_SIBLING,SECTION)); 
                           }

                        for (Section section: sections)
                            { Log.d(TAG,String.format("SECTION: %s  (%d)",section.name,section.items.size()));
                            }
                      }
	             
                  return System.currentTimeMillis() - start;
                }
         
         private List<Item> parse(VTDNav navigator) throws Exception 
                 { List<Item> items = new ArrayList<Item>();

                   navigator.push();
                   
                   if (navigator.toElement(VTDNav.FIRST_CHILD,ITEM))
                      { do { String id          = attribute(navigator,ID);
                          	 String name        = attribute(navigator,NAME);
                          	 String grade       = attribute(navigator,GRADE);
                          	 String stars       = attribute(navigator,STARS);
                          	 String rated       = attribute(navigator,RATED);
                          	 String order       = attribute(navigator,ORDER);
                          	 String description = description(navigator);
                          	 Item   item        = new Item(id,name,grade,stars,rated,order,description);

                          	 originated(navigator,item);
                          	 checked   (navigator,item);
                          
                          	 items.add (item);
                           } while(navigator.toElement(VTDNav.NEXT_SIBLING,ITEM));
                        }
                 
                   navigator.pop();
                   
                   return items;
                 }
         
         private String description(VTDNav navigator) throws Exception 
                 { navigator.push();

                   try
                      { if (navigator.toElement(VTDNav.FIRST_CHILD,DESCRIPTION))
                           { return text(navigator);
                           }
                      }
                   finally
                      { navigator.pop();
                      }

                   return "";
                 }
         
         private void originated(VTDNav navigator,Item item) throws Exception 
                 { navigator.push();
                   
                   if (navigator.toElement(VTDNav.FIRST_CHILD,ORIGINATED))
                      { String by   = attribute(navigator,BY);
                        String date = attribute(navigator,DATE);
                        
                        item.originated = new Item.Originated(by,date);
                      }
                   
                   navigator.pop();
                 }
         
         private void checked(VTDNav navigator,Item item) throws Exception 
                 { navigator.push();

                   if (navigator.toElement(VTDNav.FIRST_CHILD,CHECKED))
                      { String by   = attribute(navigator,BY);
                        String date = attribute(navigator,DATE);
                        
                        item.checked = new Item.Checked(by,date);
                      }
                   
                   navigator.pop();
                 }
       }
