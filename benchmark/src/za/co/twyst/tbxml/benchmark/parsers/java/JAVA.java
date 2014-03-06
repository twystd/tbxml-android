package za.co.twyst.tbxml.benchmark.parsers.java;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import za.co.twyst.tbxml.benchmark.db.Route;
import za.co.twyst.tbxml.benchmark.db.Section;
import za.co.twyst.tbxml.benchmark.parsers.Parser;
import za.co.twyst.tbxml.benchmark.parsers.java.TBXML.TBXMLElement;

public class JAVA implements Parser 
       { // CONSTANTS
    
		 private static final String TAG = JAVA.class.getSimpleName();
    
         // INSTANCE VARIABLES
         
         private final String xml;
         
         // CONSTRUCTOR
         
         public JAVA(String xml)
                { this.xml = xml == null ? "" : xml;
                }

         // *** Parser ***

         @Override
         public long parse(int iterations) throws Exception 
                { long start = System.currentTimeMillis();
	             
                  for (int i=0; i<iterations; i++)
                      { List<Section> sections = new ArrayList<Section>();
                        TBXML         tbxml    = new TBXML(xml);
                        TBXMLElement  root     = tbxml.rootXMLElement();
                        TBXMLElement  child    = root.firstChild;
                        
                        while (child != null)
                              { if ("section".equals(tbxml.elementName(child)))
                                   { String      id      = tbxml.valueOfAttributeForElement("id",  child);
                                     String      name    = tbxml.valueOfAttributeForElement("name",child);
                                     String      order   = tbxml.valueOfAttributeForElement("order",child);
                                     List<Route> routes  = parse(tbxml,child);
                                     Section     section = new Section(id,name,order,routes);

                                     sections.add(section);
                                   }
                            
                                child = child.nextSibling;

                              }

                        for (Section section: sections)
                            { Log.d(TAG,String.format("SECTION: %s  (%d)",section.name,section.routes.size()));
                            }
                      }
	             
                  return System.currentTimeMillis() - start;
                }
         
         private List<Route> parse(TBXML tbxml,TBXMLElement section) throws Exception 
                 { List<Route>  routes = new ArrayList<Route>();
                   TBXMLElement child  = section.firstChild;
                   
                   while (child != null)
                         { if ("route".equals(tbxml.elementName(child)))
                              { String       id          = tbxml.valueOfAttributeForElement("id",   child);
                                String       name        = tbxml.valueOfAttributeForElement("name", child);
                                String       grade       = tbxml.valueOfAttributeForElement("grade",child);
                                String       stars       = tbxml.valueOfAttributeForElement("stars",child);
                                String       bolts       = tbxml.valueOfAttributeForElement("bolts",child);
                                String       order       = tbxml.valueOfAttributeForElement("order",child);
                                String       description = tbxml.textForElement(tbxml.childElement("description",child));
                                Route        route       = new Route(id,name,grade,stars,bolts,order,description);
                                TBXMLElement optional;

                                if ((optional = tbxml.childElement("first-ascent",child)) != null)
                                   { String by   = tbxml.valueOfAttributeForElement("by",  optional);
                                     String date = tbxml.valueOfAttributeForElement("date",optional);
                                
                                     route.firstAscent = new Route.FirstAscent(by,date);
                                   }

                                if ((optional = tbxml.childElement("bolted",child)) != null)
                                   { String by   = tbxml.valueOfAttributeForElement("by",  optional);
                                     String date = tbxml.valueOfAttributeForElement("date",optional);
                                
                                     route.bolted = new Route.Bolted(by,date);
                                   }
                                
                                routes.add(route);
                              }
                         
                           child = child.nextSibling;
                         }

                   return routes;
                 }
       }
