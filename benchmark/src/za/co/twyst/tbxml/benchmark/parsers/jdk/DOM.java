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

import za.co.twyst.tbxml.benchmark.db.Item;
import za.co.twyst.tbxml.benchmark.db.Section;
import za.co.twyst.tbxml.benchmark.parsers.Parser;

public class DOM extends Parser 
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
        	 NodeList nodelist = element.getElementsByTagName(DESCRIPTION);
        	 
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
                        NodeList       nodes    = root.getElementsByTagName(SECTION);
                        
                        for (int j=0; j<nodes.getLength(); j++)
                            { Node         node       = nodes.item(j);
                              NamedNodeMap attributes = node.getAttributes();
                              String      id          = attributes.getNamedItem(ID).getTextContent();
                              String      name        = attributes.getNamedItem(NAME).getTextContent();
                              String      order       = attributes.getNamedItem(ORDER).getTextContent();
                              List<Item>  items       = parse((Element) node);
                              Section     section     = new Section(id,name,order,items);
                              
                              sections.add(section);
                            }

                        for (Section section: sections)
                            { Log.d(TAG,String.format("SECTION: %s  (%d)",section.name,section.items.size()));
                            }
                      }
	             
                  return System.currentTimeMillis() - start;
                }
         
         private List<Item> parse(Element section) throws Exception 
                 { List<Item> items = new ArrayList<Item>();
                   NodeList   nodes = section.getElementsByTagName(ITEM);
                 
                   for (int i=0; i<nodes.getLength(); i++)
                       { Element      node        = (Element) nodes.item(i);
                         NamedNodeMap attributes  = node.getAttributes();
                         String       id          = attribute  (attributes,ID);
                         String       name        = attribute  (attributes,NAME);
                         String       grade       = attribute  (attributes,GRADE);
                         String       stars       = attribute  (attributes,STARS);
                         String       rated       = attribute  (attributes,RATED);
                         String       order       = attribute  (attributes,ORDER);
                         String       description = description(node);
                         Item         item        = new Item(id,name,grade,stars,rated,order,description);
                         Node         optional;
                         
                         if ((optional = child(node,ORIGINATED)) != null)
                            { String       by   = attribute(optional.getAttributes(),BY);
                              String       date = attribute(optional.getAttributes(),DATE);
                              
                              item.originated = new Item.Originated(by,date);
                            }

                         if ((optional = child(node,CHECKED)) != null)
                            { String       by   = attribute(optional.getAttributes(),BY);
                              String       date = attribute(optional.getAttributes(),DATE);
                                   
                              item.checked = new Item.Checked(by,date);
                            }
                         
                         items.add(item);
                       }
                   
                   return items;
                 }
       }
