package za.co.twyst.tbxml.benchmark.parsers.ndk;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import za.co.twyst.tbxml.benchmark.db.Item;
import za.co.twyst.tbxml.benchmark.db.Section;
import za.co.twyst.tbxml.benchmark.parsers.Parser;
import za.co.twyst.tbxml.TBXML;

public class NDK extends Parser 
       { // CONSTANTS
    
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
                                      
                                          if (SECTION.equals(tag))
                                             { String     id      = tbxml.valueOfAttributeNamed(ID,   child);
                                               String     name    = tbxml.valueOfAttributeNamed(NAME, child);
                                               String     order   = tbxml.valueOfAttributeNamed(ORDER,child);
                                               List<Item> items   = parse(tbxml,child);
                                               Section    section = new Section(id,name,order,items);

                                               sections.add(section);
                                             }

                                          child = tbxml.nextSibling(child);
                                        }
                                }
                           }
                        finally
                           { tbxml.release();
                           }
                        
                      // ... validate
                        
                      for (Section section: sections)
                          { Log.d(TAG,String.format("SECTION: %s  (%d)",section.name,section.items.size()));
                          }
                      }
	             
                  return System.currentTimeMillis() - start;
                }
         
         private List<Item> parse(TBXML tbxml,long section) throws Exception 
                 { List<Item> items = new ArrayList<Item>();
                   long       child = tbxml.firstChild(section);
                   
                   while (child != 0)
                         { if (ITEM.equals(tbxml.elementName(child)))
                              { String id          = tbxml.valueOfAttributeNamed(ID,   child);
                                String name        = tbxml.valueOfAttributeNamed(NAME, child);
                                String grade       = tbxml.valueOfAttributeNamed(GRADE,child);
                                String stars       = tbxml.valueOfAttributeNamed(STARS,child);
                                String rated       = tbxml.valueOfAttributeNamed(RATED,child);
                                String order       = tbxml.valueOfAttributeNamed(ORDER,child);
                                String description = tbxml.textForElement(tbxml.childElementNamed(DESCRIPTION,child));
                                Item   item        = new Item(id,name,grade,stars,rated,order,description);
                                long   optional;

                                if ((optional = tbxml.childElementNamed(ORIGINATED,child)) != 0)
                                   { String by   = tbxml.valueOfAttributeNamed(BY,  optional);
                                     String date = tbxml.valueOfAttributeNamed(DATE,optional);
                                
                                     item.originated = new Item.Originated(by,date);
                                   }

                                if ((optional = tbxml.childElementNamed(CHECKED,child)) != 0)
                                   { String by   = tbxml.valueOfAttributeNamed(BY,  optional);
                                     String date = tbxml.valueOfAttributeNamed(DATE,optional);
                                
                                     item.checked = new Item.Checked(by,date);
                                   }
                                
                                items.add(item);
                              }
                         
                           child = tbxml.nextSibling(child);
                         }

                   return items;
                 }
       }
