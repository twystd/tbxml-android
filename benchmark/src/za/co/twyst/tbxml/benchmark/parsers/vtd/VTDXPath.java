package za.co.twyst.tbxml.benchmark.parsers.vtd;

import java.util.ArrayList;
import java.util.List;

import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

import android.util.Log;

import za.co.twyst.tbxml.benchmark.db.Route;
import za.co.twyst.tbxml.benchmark.db.Section;
import za.co.twyst.tbxml.benchmark.parsers.Parser;

public class VTDXPath implements Parser 
       { // CONSTANTS
    
		 private static final String TAG = VTDXPath.class.getSimpleName();
    
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
         
         public VTDXPath(String xml)
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
                        AutoPilot     pilot     = new AutoPilot(navigator);
                        
                        pilot.selectXPath("/routes/section");
                        
                        while (pilot.evalXPath() != -1)
                              { String      id      = attribute(navigator,"id");
                                String      name    = attribute(navigator,"name");
                                String      order   = attribute(navigator,"order");
                                List<Route> routes  = parse(navigator);
                                Section     section = new Section(id,name,order,routes);
                              
                                sections.add(section);
                              }

                        for (Section section: sections)
                            { Log.d(TAG,String.format("SECTION: %s  (%d)",section.name,section.routes.size()));
                            }
                      }
	             
                  return System.currentTimeMillis() - start;
                }
         
         private List<Route> parse(VTDNav navigator) throws Exception 
                 { List<Route> routes = new ArrayList<Route>();
                   AutoPilot   pilot  = new AutoPilot(navigator);
                   
                   pilot.selectXPath("route");
                   
                   while(pilot.evalXPath() != -1)
                        { String id          = attribute(navigator,"id");
                          String name        = attribute(navigator,"name");
                          String grade       = attribute(navigator,"grade");
                          String stars       = attribute(navigator,"stars");
                          String bolts       = attribute(navigator,"bolts");
                          String order       = attribute(navigator,"order");
                          String description = description(navigator);
                          Route  route       = new Route(id,name,grade,stars,bolts,order,description);

                          firstAscent(navigator,route);
                          boltedBy   (navigator,route);
                          
                          routes.add (route);
                        }
                 
                   return routes;
                 }
         
         private String description(VTDNav navigator) throws Exception 
                 { AutoPilot pilot = new AutoPilot(navigator);
           
                   navigator.push();
                   
                   try
                      { pilot.selectXPath("description");
                   
                        if (pilot.evalXPath() != -1)
                           { return text(navigator);
                           }
                      }
                   finally
                      { navigator.pop();
                      }

                   return "";
                 }
         
         private void firstAscent(VTDNav navigator,Route route) throws Exception 
                 { AutoPilot pilot = new AutoPilot(navigator);
           
                   navigator.push();
                   pilot.selectXPath("first-ascent");

                   if (pilot.evalXPath() != -1)
                      { String by   = attribute(navigator,"by");
                        String date = attribute(navigator,"date");
                        
                        route.firstAscent = new Route.FirstAscent(by,date);
                      }
                   
                   navigator.pop();
                 }
         
         private void boltedBy(VTDNav navigator,Route route) throws Exception 
                 { AutoPilot pilot = new AutoPilot(navigator);
           
                   navigator.push();
                   pilot.selectXPath("bolted");

                   if (pilot.evalXPath() != -1)
                      { String by   = attribute(navigator,"by");
                        String date = attribute(navigator,"date");
                        
                        route.bolted = new Route.Bolted(by,date);
                      }
                   
                   navigator.pop();
                 }
       }
