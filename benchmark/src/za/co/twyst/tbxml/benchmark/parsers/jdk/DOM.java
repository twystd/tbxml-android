package za.co.twyst.tbxml.benchmark.parsers.jdk;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.util.Log;

import za.co.twyst.tbxml.benchmark.db.Route;
import za.co.twyst.tbxml.benchmark.db.Section;
import za.co.twyst.tbxml.benchmark.parsers.Parser;

public class DOM implements Parser 
       { // CONSTANTS
    
		 private static final String TAG = DOM.class.getSimpleName();
    
         // INSTANCE VARIABLES
         
         private final String xml;
         
         // CLASS METHODS

         private static String attribute(NamedNodeMap attributes,String name) {
        	 Node attribute;
        	 
        	 if ((attribute = attributes.getNamedItem(name)) != null) {
        		 return attribute.getTextContent();
        	 }
        	 
        	 return "";
         }

 		private static Node child(Element element,String tag) {
         	 NodeList nodelist = element.getElementsByTagName(tag);
         	 
         	 for (int i=0; i<nodelist.getLength();) {
         		 return nodelist.item(i);
         	 }
         	 
         	 return null;
          }

		private static String description(Element element) {
        	 NodeList nodelist = element.getElementsByTagName("description");
        	 
        	 for (int i=0; i<nodelist.getLength();) {
        		 return nodelist.item(i).getTextContent();
        	 }
        	 
        	 return "";
         }
         
         // CONSTRUCTOR
         
         public DOM(String xml)
                { this.xml = xml == null ? "" : xml;
                }

         // *** Parser ***

         @Override
         public long parse(int iterations) throws Exception 
                { long start = System.currentTimeMillis();

                  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                  DocumentBuilder        builder = factory.newDocumentBuilder();
	             
                  for (int i=0; i<iterations; i++)
                      { List<Section>  sections = new ArrayList<Section>();
                        InputSource    source   = new InputSource(new StringReader(xml));
                        Document       document = builder.parse(source);
                        Element        root     = document.getDocumentElement();
                        NodeList       nodes    = root.getElementsByTagName("section");
                        
                        for (int j=0; j<nodes.getLength(); j++)
                            { Node         node       = nodes.item(j);
                              NamedNodeMap attributes = node.getAttributes();
                              String      id          = attributes.getNamedItem("id").getTextContent();
                              String      name        = attributes.getNamedItem("name").getTextContent();
                              String      order       = attributes.getNamedItem("order").getTextContent();
                              List<Route> routes      = parse((Element) node);
                              Section     section     = new Section(id,name,order,routes);
                              
                              sections.add(section);
                            }

                        for (Section section: sections)
                            { Log.d(TAG,String.format("SECTION: %s  (%d)",section.name,section.routes.size()));
                            }
                      }
	             
                  return System.currentTimeMillis() - start;
                }
         
         private List<Route> parse(Element section) throws Exception 
                 { List<Route> routes = new ArrayList<Route>();
                   NodeList    nodes  = section.getElementsByTagName("route");
                 
                   for (int i=0; i<nodes.getLength(); i++)
                       { Element      node        = (Element) nodes.item(i);
                         NamedNodeMap attributes  = node.getAttributes();
                         String       id          = attribute  (attributes,"id");
                         String       name        = attribute  (attributes,"name");
                         String       grade       = attribute  (attributes,"grade");
                         String       stars       = attribute  (attributes,"stars");
                         String       bolts       = attribute  (attributes,"bolts");
                         String       order       = attribute  (attributes,"order");
                         String       description = description(node);
                         Route        route       = new Route(id,name,grade,stars,bolts,order,description);
                         Node         optional;
                         
                         if ((optional = child(node,"first-ascent")) != null)
                            { String       by   = attribute(optional.getAttributes(),"by");
                              String       date = attribute(optional.getAttributes(),"date");
                              
                              route.firstAscent = new Route.FirstAscent(by,date);
                            }

                         if ((optional = child(node,"bolted")) != null)
                            { String       by   = attribute(optional.getAttributes(),"by");
                              String       date = attribute(optional.getAttributes(),"date");
                                   
                              route.bolted = new Route.Bolted(by,date);
                            }
                         
                         routes.add(route);
                       }
                   
                   return routes;
                 }
       }
