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

import za.co.twyst.tbxml.benchmark.db.Item;
import za.co.twyst.tbxml.benchmark.db.Section;
import za.co.twyst.tbxml.benchmark.parsers.Parser;

public class SAX extends Parser 
       { // CONSTANTS
    
		 private static final String TAG = SAX.class.getSimpleName();
    
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
                            { Log.d(TAG,String.format("SECTION: %s  (%d)",section.name,section.items.size()));
                            }
                      }
	             
                  return System.currentTimeMillis() - start;
                }
         
         
         // INNER CLASSES
         
         private class XmlHandler extends DefaultHandler
                 { public  List<Section> sections;
                   private boolean       root;
        	       private Section       section;
        	       private Item          item;
        	       private StringBuilder description;
        	       
                   @Override
                   public void startDocument() throws SAXException 
        	              { super.startDocument();
        	        
        	                sections    = new ArrayList<Section>();
        	           	 	root        = false;
        	           	 	section     = null;
        	           	 	item        = null;
        	           	 	description = null;
        	              }

                   @Override
                   public void startElement(String uri,String localName,String qname,Attributes attributes) throws SAXException 
                          { if (localName.equalsIgnoreCase(DOCUMENT))
        	                   { this.root        = true;
        	                     this.section     = null;
        	                     this.item       = null;
             	           	 	 this.description = null;
        	                     
        	                     return;
        	                   } 
                	        
        	                if (root && localName.equalsIgnoreCase(SECTION))
        	                   { String     id    = attributes.getValue(ID);
                                 String     name  = attributes.getValue(NAME);
                                 String     order = attributes.getValue(ORDER);
                                 List<Item> items = new ArrayList<Item>();
                                 
                                 this.section = new Section(id,name,order,items);
        	                     return;
        	                   } 
                	        
        	                if ((section != null) && localName.equalsIgnoreCase(ITEM))
        	                   { String id    = attributes.getValue(ID);
                                 String name  = attributes.getValue(NAME);
                                 String grade = attributes.getValue(GRADE);
                                 String stars = attributes.getValue(STARS);
                                 String rated = attributes.getValue(RATED);
                                 String order = attributes.getValue(ORDER);
                                 
                                 this.item = new Item(id,name,grade,stars,rated,order,"");
        	                     return;
        	                   } 
                	        
        	                if ((item != null) && localName.equalsIgnoreCase(DESCRIPTION))
        	                   { this.description = new StringBuilder();
        	                     return;
        	                   } 
                	        
        	                if ((item != null) && localName.equalsIgnoreCase(ORIGINATED))
        	                   { String date = attributes.getValue(DATE);
                                 String by   = attributes.getValue(BY);
                               
                                 item.originated = new Item.Originated(date,by);
        	                     return;
        	                   } 
                	        
        	                if ((item != null) && localName.equalsIgnoreCase(CHECKED))
        	                   { String date = attributes.getValue(DATE);
                                 String by   = attributes.getValue(BY);
                               
                                 item.checked = new Item.Checked(date,by);
        	                     return;
        	                   } 
                          }

                   @Override
                   public void endElement(String uri,String localName,String name) throws SAXException 
        	              { if (localName.equalsIgnoreCase(DOCUMENT))
        	                   { root = false;
        	                     return;
        	                   }
        	                
        	                if ((this.section != null) && localName.equalsIgnoreCase(SECTION))
        	                   { this.sections.add(section);
        	                	 this.section = null;
        	                     return;
        	                   } 
        	                
        	                if ((this.section != null) && (this.item != null) && localName.equalsIgnoreCase(ITEM))
        	                   { this.section.add(item);
        	                	 this.item = null;
        	                     return;
        	                   } 
        	                
        	                if ((this.item != null) && (this.description != null) && localName.equalsIgnoreCase(ITEM))
        	                   { this.item.description = description.toString();
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
