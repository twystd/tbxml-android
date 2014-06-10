package za.co.twyst.tbxml.benchmark.parsers.java;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import za.co.twyst.tbxml.benchmark.db.Item;
import za.co.twyst.tbxml.benchmark.db.Section;
import za.co.twyst.tbxml.benchmark.parsers.Parser;
import za.co.twyst.tbxml.benchmark.parsers.java.TBXML.TBXMLElement;

public class JAVA extends Parser 
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
                              { if (SECTION.equals(tbxml.elementName(child)))
                                   { String      id      = tbxml.valueOfAttributeForElement(ID,  child);
                                     String      name    = tbxml.valueOfAttributeForElement(NAME,child);
                                     String      order   = tbxml.valueOfAttributeForElement(ORDER,child);
                                     List<Item>  items   = parse(tbxml,child);
                                     Section     section = new Section(id,name,order,items);

                                     sections.add(section);
                                   }
                            
                                child = child.nextSibling;

                              }

                        for (Section section: sections)
                            { Log.d(TAG,String.format("SECTION: %s  (%d)",section.name,section.items.size()));
                            }
                      }
	             
                  return System.currentTimeMillis() - start;
                }
         
         private List<Item> parse(TBXML tbxml,TBXMLElement section) throws Exception 
                 { List<Item>  items  = new ArrayList<Item>();
                   TBXMLElement child = section.firstChild;
                   
                   while (child != null)
                         { if (ITEM.equals(tbxml.elementName(child)))
                              { String       id          = tbxml.valueOfAttributeForElement(ID,   child);
                                String       name        = tbxml.valueOfAttributeForElement(NAME, child);
                                String       grade       = tbxml.valueOfAttributeForElement(GRADE,child);
                                String       stars       = tbxml.valueOfAttributeForElement(STARS,child);
                                String       rated       = tbxml.valueOfAttributeForElement(RATED,child);
                                String       order       = tbxml.valueOfAttributeForElement(ORDER,child);
                                String       description = tbxml.textForElement(tbxml.childElement(DESCRIPTION,child));
                                Item         item        = new Item(id,name,grade,stars,rated,order,description);
                                TBXMLElement optional;

                                if ((optional = tbxml.childElement(ORIGINATED,child)) != null)
                                   { String by   = tbxml.valueOfAttributeForElement(BY,  optional);
                                     String date = tbxml.valueOfAttributeForElement(DATE,optional);
                                
                                     item.originated = new Item.Originated(by,date);
                                   }

                                if ((optional = tbxml.childElement(CHECKED,child)) != null)
                                   { String by   = tbxml.valueOfAttributeForElement(BY,  optional);
                                     String date = tbxml.valueOfAttributeForElement(DATE,optional);
                                
                                     item.checked = new Item.Checked(by,date);
                                   }
                                
                                items.add(item);
                              }
                         
                           child = child.nextSibling;
                         }

                   return items;
                 }
       }
