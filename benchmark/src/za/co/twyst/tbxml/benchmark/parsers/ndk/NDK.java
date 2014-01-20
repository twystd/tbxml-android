package za.co.twyst.tbxml.benchmark.parsers.ndk;

import java.util.ArrayList;
import java.util.List;

import za.co.twyst.tbxml.benchmark.db.Route;
import za.co.twyst.tbxml.benchmark.db.Section;
import za.co.twyst.tbxml.benchmark.parsers.Parser;
import za.co.twyst.tbxml.TBXML;

public class NDK implements Parser 
       { // CONSTANTS
    
		 @SuppressWarnings("unused")
         private static final String TAG = NDK.class.getSimpleName();
    
         // INSTANCE VARIABLES
         
         private final String xml;
         
         // CONSTRUCTOR
         
         public NDK(String xml)
                { this.xml = xml == null ? "" : xml;
                }

         // *** Parser ***

         @Override
         public long parse(int iterations) throws Exception 
                { long start = System.currentTimeMillis();
	             
                  for (int i=0; i<iterations; i++)
                      { List<Section> sections = new ArrayList<Section>();
                        TBXML         tbxml    = new TBXML();
                        
                        try
                           { tbxml.parse(xml);
                             
                             long root = tbxml.rootXMLElement();
                             
                             if (root != 0) 
                                { long child = tbxml.firstChild(root);
                                
                                  while (child != 0)
                                        { String tag = tbxml.elementName(child);
                                      
                                          if ("section".equals(tag))
                                             { String      id      = tbxml.valueOfAttributeForElement("id",child);
                                               String      name    = tbxml.valueOfAttributeForElement("name",child);
                                               String      order   = tbxml.valueOfAttributeForElement("order",child);
                                               List<Route> routes  = parse(tbxml,child);
                                               Section     section = new Section(id,name,order,routes);

                                               sections.add(section);
                                               
//                                             Log.w(TAG,"SECTION [id:" + id + "][name:" + name + "][order:" + order + "]");
                                             }

                                          child = tbxml.nextSibling(child);
                                        }
                                }
                           }
                        finally
                           { tbxml.release();
                           }
                        
//                      for (Section section: sections)
//                          { Log.d(TAG,String.format("SECTION: %s  (%d)",section.name,section.routes.size()));
//                          }
                      }
	             
                  return System.currentTimeMillis() - start;
                }
         
         private List<Route> parse(TBXML tbxml,long section) throws Exception 
                 { List<Route> routes = new ArrayList<Route>();
                   long        child  = tbxml.firstChild(section);
                   
                   while (child != 0)
                         { if ("route".equals(tbxml.elementName(child)))
                              { String id          = tbxml.valueOfAttributeForElement("id",   child);
                                String name        = tbxml.valueOfAttributeForElement("name", child);
                                String grade       = tbxml.valueOfAttributeForElement("grade",child);
                                String stars       = tbxml.valueOfAttributeForElement("stars",child);
                                String bolts       = tbxml.valueOfAttributeForElement("bolts",child);
                                String order       = tbxml.valueOfAttributeForElement("order",child);
                                String description = tbxml.textForElement(tbxml.childElement("description",child));
                                Route  route       = new Route(id,name,grade,stars,bolts,order,description);
                                long   optional;

                                if ((optional = tbxml.childElement("first-ascent",child)) != 0)
                                   { String by   = tbxml.valueOfAttributeForElement("by",  optional);
                                     String date = tbxml.valueOfAttributeForElement("date",optional);
                                
                                     route.firstAscent = new Route.FirstAscent(by,date);
                                   }

                                if ((optional = tbxml.childElement("bolted",child)) != 0)
                                   { String by   = tbxml.valueOfAttributeForElement("by",  optional);
                                     String date = tbxml.valueOfAttributeForElement("date",optional);
                                
                                     route.bolted = new Route.Bolted(by,date);
                                   }
                                
//                              Log.e(TAG,"ROUTE: [id:" + id + "][name:" + name + "][grade:" + grade + "][stars:" + stars + "][bolts:" + bolts + "][order:" + order + "][description:" + description + "]");
                                routes.add(route);
                              }
                         
                           child = tbxml.nextSibling(child);
                         }

                   return routes;
                 }
       }
