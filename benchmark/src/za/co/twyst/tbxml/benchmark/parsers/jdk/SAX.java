package za.co.twyst.tbxml.benchmark.parsers.jdk;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import za.co.twyst.tbxml.benchmark.db.Route;
import za.co.twyst.tbxml.benchmark.db.Section;
import za.co.twyst.tbxml.benchmark.parsers.Parser;

public class SAX implements Parser 
       { // CONSTANTS
    
		 private static final String TAG          = SAX.class.getSimpleName();
         private static final String ROOT         = "routes";
         private static final String SECTION      = "section";
         private static final String ROUTE        = "route";
         private static final String DESCRIPTION  = "description";
         private static final String FIRST_ASCENT = "first-ascent";
         private static final String BOLTED       = "bolted";
    
         // INSTANCE VARIABLES
         
         private final String xml;
         
         // CONSTRUCTOR
         
         public SAX(String xml)
                { this.xml = xml == null ? "" : xml;
                }

         // *** Parser ***

         @Override
         public long parse(int iterations) throws Exception 
                { SAXParserFactory factory = SAXParserFactory.newInstance();
        	      long              start  = System.currentTimeMillis();
	             
                  for (int i=0; i<iterations; i++)
                      { SAXParser   parser  = factory.newSAXParser();
                        InputSource source  = new InputSource(new StringReader(xml));
                        XmlHandler  handler = new XmlHandler();
                        
                        parser.parse(source,handler);
                        
                        List<Section> sections = handler.sections;

                        for (Section section: sections)
                            { Log.d(TAG,String.format("SECTION: %s  (%d)",section.name,section.routes.size()));
                            }
                      }
	             
                  return System.currentTimeMillis() - start;
                }
         
         
         // INNER CLASSES
         
         private class XmlHandler extends DefaultHandler
                 { public  List<Section> sections;
                   private boolean       root;
        	       private Section       section;
        	       private Route         route;
        	       private StringBuilder description;
        	       
                   @Override
                   public void startDocument() throws SAXException 
        	              { super.startDocument();
        	        
        	                sections    = new ArrayList<Section>();
        	           	 	root        = false;
        	           	 	section     = null;
        	           	 	route       = null;
        	           	 	description = null;
        	              }

                   @Override
                   public void startElement(String uri,String localName,String qname,Attributes attributes) throws SAXException 
                          { if (localName.equalsIgnoreCase(ROOT))
        	                   { this.root        = true;
        	                     this.section     = null;
        	                     this.route       = null;
             	           	 	 this.description = null;
        	                     
        	                     return;
        	                   } 
                	        
        	                if (root && localName.equalsIgnoreCase(SECTION))
        	                   { String      id      = attributes.getValue("id");
                                 String      name    = attributes.getValue("name");
                                 String      order   = attributes.getValue("order");
                                 List<Route> routes  = new ArrayList<Route>();
                                 
                                 this.section = new Section(id,name,order,routes);
        	                     return;
        	                   } 
                	        
        	                if ((section != null) && localName.equalsIgnoreCase(ROUTE))
        	                   { String id    = attributes.getValue("id");
                                 String name  = attributes.getValue("name");
                                 String grade = attributes.getValue("grade");
                                 String stars = attributes.getValue("stars");
                                 String bolts = attributes.getValue("bolts");
                                 String order = attributes.getValue("order");
                                 
                                 this.route = new Route(id,name,grade,stars,bolts,order,"");
        	                     return;
        	                   } 
                	        
        	                if ((route != null) && localName.equalsIgnoreCase(DESCRIPTION))
        	                   { this.description = new StringBuilder();
        	                     return;
        	                   } 
                	        
        	                if ((route != null) && localName.equalsIgnoreCase(FIRST_ASCENT))
        	                   { String date = attributes.getValue("date");
                                 String by   = attributes.getValue("by");
                               
                                 route.firstAscent = new Route.FirstAscent(date,by);
        	                     return;
        	                   } 
                	        
        	                if ((route != null) && localName.equalsIgnoreCase(BOLTED))
        	                   { String date = attributes.getValue("date");
                                 String by   = attributes.getValue("by");
                               
                                 route.bolted = new Route.Bolted(date,by);
        	                     return;
        	                   } 
                          }

                   @Override
                   public void endElement(String uri,String localName,String name) throws SAXException 
        	              { if (localName.equalsIgnoreCase(ROOT))
        	                   { root = false;
        	                     return;
        	                   }
        	                
        	                if ((this.section != null) && localName.equalsIgnoreCase(SECTION))
        	                   { this.sections.add(section);
        	                	 this.section = null;
        	                     return;
        	                   } 
        	                
        	                if ((this.section != null) && (this.route != null) && localName.equalsIgnoreCase(ROUTE))
        	                   { this.section.add(route);
        	                	 this.route = null;
        	                     return;
        	                   } 
        	                
        	                if ((this.route != null) && (this.description != null) && localName.equalsIgnoreCase(ROUTE))
        	                   { this.route.description = description.toString();
        	                	 this.description = null;
        	                     return;
        	                   } 
        	              }

                   @Override
                   public void characters(char[] ch, int start, int length) throws SAXException 
                          { if (description != null)
                               { description.append(ch,start,length);
                        	   	  return;
                               }
                          }
                 }

       }
