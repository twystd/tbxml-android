package za.co.twyst.tbxml.benchmark.parsers.jdk;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.util.Log;

import za.co.twyst.tbxml.benchmark.db.Item;
import za.co.twyst.tbxml.benchmark.db.Section;
import za.co.twyst.tbxml.benchmark.parsers.Parser;

public class XPATH extends Parser 
       { // CONSTANTS
    
		 private static final String TAG = XPATH.class.getSimpleName();
    
         // INSTANCE VARIABLES
         
         private final String xml;
         private final XPath  xpath;
         
         // CONSTRUCTOR
         
         public XPATH(String xml)
                { this.xml   = xml == null ? "" : xml;
                  this.xpath = XPathFactory.newInstance().newXPath();
                }

         // *** Parser ***

         @Override
         public long parse(int iterations) throws Exception 
                { long start = System.currentTimeMillis();
	             
                  for (int i=0; i<iterations; i++)
                      { List<Section> sections = new ArrayList<Section>();
                        InputSource   source   = new InputSource(new StringReader(xml));
                        Node          root     = (Node)     xpath.evaluate(DOCUMENT,source,XPathConstants.NODE);
                        NodeList      nodes    = (NodeList) xpath.evaluate(SECTION, root,  XPathConstants.NODESET);
                        
                        for (int j=0; j<nodes.getLength(); j++)
                            { Node       node    = nodes.item(j);
                              String     id      = xpath.evaluate(ATTR_ID,   node);
                              String     name    = xpath.evaluate(ATTR_NAME, node);
                              String     order   = xpath.evaluate(ATTR_ORDER,node);
                              List<Item> items   = parse(node);
                              Section    section = new Section(id,name,order,items);
                              
                              sections.add(section);
                            }

                        for (Section section: sections)
                            { Log.d(TAG,String.format("SECTION: %s  (%d)",section.name,section.items.size()));
                            }
                      }
	             
                  return System.currentTimeMillis() - start;
                }
         
         private List<Item> parse(Node section) throws Exception 
                 { List<Item> items = new ArrayList<Item>();
                   NodeList   nodes = (NodeList) xpath.evaluate(ITEM,section,XPathConstants.NODESET);
                 
                   for (int i=0; i<nodes.getLength(); i++)
                       { Node    node        = nodes.item(i);
                         String  id          = xpath.evaluate(ATTR_ID,   node);
                         String  name        = xpath.evaluate(ATTR_NAME, node);
                         String  grade       = xpath.evaluate(ATTR_GRADE,node);
                         String  stars       = xpath.evaluate(ATTR_STARS,node);
                         String  rated       = xpath.evaluate(ATTR_RATED,node);
                         String  order       = xpath.evaluate(ATTR_ORDER,node);
                         String  description = xpath.evaluate(TEXT_DESCRIPTION,node);
                         Item    item        = new Item(id,name,grade,stars,rated,order,description);
                         Node    optional;
                         
                         if ((optional = (Node) xpath.evaluate(ORIGINATED,node,XPathConstants.NODE)) != null)
                            { String by   = xpath.evaluate(ATTR_BY,  optional);
                              String date = xpath.evaluate(ATTR_DATE,optional);
                              
                              item.originated = new Item.Originated(by,date);
                            }
                         
                         if ((optional = (Node) xpath.evaluate(CHECKED,node,XPathConstants.NODE)) != null)
                            { String by   = xpath.evaluate(ATTR_BY,  optional);
                              String date = xpath.evaluate(ATTR_DATE,optional);
                              
                              item.checked = new Item.Checked(by,date);
                            }
                         
                         items.add(item);
                       }
                   
                   return items;
                 }
       }
