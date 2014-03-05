package za.co.twyst.tbxml.benchmark.parsers.jdk;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import za.co.twyst.tbxml.benchmark.db.Route;
import za.co.twyst.tbxml.benchmark.db.Section;
import za.co.twyst.tbxml.benchmark.parsers.Parser;

public class SAX implements Parser 
       { // CONSTANTS
    
         @SuppressWarnings("unused")
		 private static final String TAG = SAX.class.getSimpleName();
    
         // INSTANCE VARIABLES
         
         private final String xml;
         private final XPath  xpath;
         
         // CONSTRUCTOR
         
         public SAX(String xml)
                { this.xml   = xml == null ? "" : xml;
                  this.xpath = XPathFactory.newInstance().newXPath();
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
//                        Node          root     = (Node) xpath.evaluate("/routes",source,XPathConstants.NODE);
//                        NodeList      nodes    = (NodeList) xpath.evaluate("section",root,XPathConstants.NODESET);
//                        
//                        for (int j=0; j<nodes.getLength(); j++)
//                            { Node        node    = nodes.item(j);
//                              String      id      = xpath.evaluate("@id",   node);
//                              String      name    = xpath.evaluate("@name", node);
//                              String      order   = xpath.evaluate("@order",node);
//                              List<Route> routes  = parse(node);
//                              Section     section = new Section(id,name,order,routes);
//                              
//                              sections.add(section);
//                            }

//                      for (Section section: sections)
//                          { Log.d("TRACE",String.format("SECTION: %s  (%d)",section.name,section.routes.size()));
//                          }
                      }
	             
                  return System.currentTimeMillis() - start;
                }
         
         private List<Route> parse(Node section) throws Exception 
                 { List<Route> routes = new ArrayList<Route>();
                   NodeList    nodes    = (NodeList) xpath.evaluate("route",section,XPathConstants.NODESET);
                 
                   for (int i=0; i<nodes.getLength(); i++)
                       { Node    node        = nodes.item(i);
                         String  id          = xpath.evaluate("@id",   node);
                         String  name        = xpath.evaluate("@name", node);
                         String  grade       = xpath.evaluate("@grade",node);
                         String  stars       = xpath.evaluate("@stars",node);
                         String  bolts       = xpath.evaluate("@bolts",node);
                         String  order       = xpath.evaluate("@order",node);
                         String  description = xpath.evaluate("description/text()",node);
                         Route   route       = new Route(id,name,grade,stars,bolts,order,description);
                         Node    optional;
                         
                         if ((optional = (Node) xpath.evaluate("first-ascent",node,XPathConstants.NODE)) != null)
                            { String by   = xpath.evaluate("@by", optional);
                              String date = xpath.evaluate("@date",optional);
                              
                              route.firstAscent = new Route.FirstAscent(by,date);
                            }
                         
                         if ((optional = (Node) xpath.evaluate("bolted",node,XPathConstants.NODE)) != null)
                            { String by   = xpath.evaluate("@by", optional);
                              String date = xpath.evaluate("@date",optional);
                              
                              route.bolted = new Route.Bolted(by,date);
                            }
                         
                         routes.add(route);
                       }
                   
                   return routes;
                 }
         
         // INNER CLASSES
         
         private class XmlHandler extends DefaultHandler
                 { public  List<Section> sections;
                   private StringBuilder builder;
        	    
                   @Override
                   public void startDocument() throws SAXException 
        	              { super.startDocument();
        	        
        	                sections = new ArrayList<Section>();
        	           	 	builder  = new StringBuilder();
        	              }

                   @Override
                   public void startElement(String uri,String localName,String name,Attributes attributes) throws SAXException 
                          { super.startElement(uri,localName,name,attributes);
        	        
//        	                if (localName.equalsIgnoreCase(ITEM))
//        	                { this.currentMessage = new Message();
//        	                }
                          }

                   @Override
                   public void endElement(String uri,String localName,String name) throws SAXException 
        	              { super.endElement(uri,localName,name);
        	        
//        	             if (this.currentMessage != null){
//        	            if (localName.equalsIgnoreCase(TITLE)){
//        	                currentMessage.setTitle(builder.toString());
//        	            } else if (localName.equalsIgnoreCase(LINK)){
//        	                currentMessage.setLink(builder.toString());
//        	            } else if (localName.equalsIgnoreCase(DESCRIPTION)){
//        	                currentMessage.setDescription(builder.toString());
//        	            } else if (localName.equalsIgnoreCase(PUB_DATE)){
//        	                currentMessage.setDate(builder.toString());
//        	            } else if (localName.equalsIgnoreCase(ITEM)){
//        	                messages.add(currentMessage);
//        	            }
        	                builder.setLength(0);    
        	              }

                   @Override
                   public void characters(char[] ch, int start, int length) throws SAXException 
                          { super.characters(ch, start, length);
        	            
                            builder.append(ch, start, length);
                          }
                 }

       }
