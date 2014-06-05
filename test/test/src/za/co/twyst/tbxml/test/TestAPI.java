package za.co.twyst.tbxml.test;

import java.io.InputStreamReader;
import java.io.Reader;

import android.content.Context;
import android.test.AndroidTestCase;

import za.co.twyst.tbxml.benchmark.R;
import za.co.twyst.tbxml.TBXML;

public class TestAPI extends AndroidTestCase
       { // CONSTANTS
	
	     @SuppressWarnings("unused")
		 private static final String TAG         = "TBXML";
	     private static final String DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec a diam lectus. Sed sit amet ipsum mauris.";
	     private static final int    TESTXML     = R.raw.test;
	     
	     // TEST VARIABLES
	     
	     private TBXML tbxml;

	     // SETUP/TEARDOWN

	     protected void setUp() throws Exception 
	               { tbxml = new TBXML();
	               }

	     protected void tearDown() throws Exception 
	               { tbxml.release();
	               }
	
	     // UNIT TESTS
	
	     public void testParse() throws Exception
	            { String xml = read(TESTXML);

	              tbxml.parse(xml);
	              
	              long root = tbxml.rootXMLElement();         
	              
	              assertTrue  ("Invalid 'root' element",root != 0);
	              assertEquals("Invalid 'root' element tag","routes",tbxml.elementName(root));
	              
	              long section = tbxml.firstChild (root);        
	              
	              assertTrue  ("Invalid 'section' element",section != 0);
	              assertEquals("Invalid 'section' element tag","section",tbxml.elementName(section));
	              
                  long item = tbxml.firstChild (section);     
                  
                  assertTrue  ("Invalid 'item' element",item != 0);
	              assertEquals("Invalid 'item' element tag","item",tbxml.elementName(item));
                  
                  long description = tbxml.firstChild (item);       
                  
                  assertTrue  ("Invalid 'description' element",description != 0);
	              assertEquals("Invalid 'description' element tag","description",tbxml.elementName(description));

                  long created = tbxml.nextSibling(description); 
                  
                  assertTrue  ("Invalid 'first-ascent' element",created != 0);
	              assertEquals("Invalid 'created' element tag","created",tbxml.elementName(created));
	            }
	     
         public void testChildElementNamed() throws Exception
                { String xml = read(TESTXML);
                  
                  tbxml.parse(xml);
                  
                  long root        = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
                  long section     = tbxml.firstChild (root);        assertTrue("Invalid 'section' element",     section     != 0);
                  long item        = tbxml.firstChild (section);     assertTrue("Invalid 'item' element",        item        != 0);
                  long description = tbxml.firstChild (item);        assertTrue("Invalid 'description' element", description != 0);
                  long created     = tbxml.nextSibling(description); assertTrue("Invalid 'created' element",     created     != 0);
                  
                  assertEquals("Invalid 'section' element",     section,    tbxml.childElementNamed("section",    root));
                  assertEquals("Invalid 'item' element",        item,       tbxml.childElementNamed("item",       section));
                  assertEquals("Invalid 'description' element", description,tbxml.childElementNamed("description",item));
                  assertEquals("Invalid 'created' element",     created,    tbxml.childElementNamed("created",    item));
                }
         
         public void testNextSiblingNamed() throws Exception
                { String xml = read(TESTXML);
           
                  tbxml.parse(xml);
           
                  long root    = tbxml.rootXMLElement();     assertTrue("Invalid 'root' element",   root    != 0);
                  long section = tbxml.firstChild (root);    assertTrue("Invalid 'section' element",section != 0);
                  long item    = tbxml.firstChild (section); assertTrue("Invalid 'item' element",   item    != 0);
                  long q       = tbxml.nextSibling(item);    assertTrue("Invalid 'q' element",      q       != 0);
                  long w       = tbxml.nextSibling(q);       assertTrue("Invalid 'w' element",      w       != 0);
                  long e       = tbxml.nextSibling(w);       assertTrue("Invalid 'e' element",      e       != 0);
                  long r       = tbxml.nextSibling(e);       assertTrue("Invalid 'r' element",      r       != 0);
                  long t       = tbxml.nextSibling(r);       assertTrue("Invalid 't' element",      t       != 0);
                  long y       = tbxml.nextSibling(t);       assertTrue("Invalid 'y' element",      y       != 0);
           
                  assertEquals("Invalid 'w' element",w,tbxml.nextSiblingNamed("item", item));
                  assertEquals("Invalid 'r' element",r,tbxml.nextSiblingNamed("item", w));
                  assertEquals("Invalid 'y' element",y,tbxml.nextSiblingNamed("item", r));

                  assertEquals("Invalid 'q' element",q,tbxml.nextSiblingNamed("itemx", item));
                  assertEquals("Invalid 'e' element",e,tbxml.nextSiblingNamed("itemx", q));
                  assertEquals("Invalid 't' element",t,tbxml.nextSiblingNamed("itemx", e));
                }
	     
         public void testElementName() throws Exception
                { String xml = read(TESTXML);
                  
                  tbxml.parse(xml);
                  
                  long root        = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
                  long section     = tbxml.firstChild (root);        assertTrue("Invalid 'section' element",     section     != 0);
                  long item        = tbxml.firstChild (section);     assertTrue("Invalid 'item' element",        item        != 0);
                  long description = tbxml.firstChild (item);        assertTrue("Invalid 'description' element", description != 0);
                  long created     = tbxml.nextSibling(description); assertTrue("Invalid 'created' element",     created     != 0);
                  
                  assertEquals("Invalid 'root' element name",        "routes",      tbxml.elementName(root));
                  assertEquals("Invalid 'section' element name",     "section",     tbxml.elementName(section));
                  assertEquals("Invalid 'item' element name",        "item",        tbxml.elementName(item));
                  assertEquals("Invalid 'description' element name", "description", tbxml.elementName(description));
                  assertEquals("Invalid 'created' element name",     "created",     tbxml.elementName(created));
                }

         public void testValueOfAttributeNamed() throws Exception
                { String xml = read(TESTXML);
                  
                  tbxml.parse(xml);
                  
                  long root        = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
                  long section     = tbxml.firstChild (root);        assertTrue("Invalid 'section' element",     section     != 0);
                  long item        = tbxml.firstChild (section);     assertTrue("Invalid 'item' element",        item        != 0);
                  long description = tbxml.firstChild (item);        assertTrue("Invalid 'description' element", description != 0);
                  long firstAscent = tbxml.nextSibling(description); assertTrue("Invalid 'first ascent' element",firstAscent != 0);
                  
                  assertEquals("Invalid section 'id' attribute","10005000",  tbxml.valueOfAttributeNamed("id",   section));
                  assertEquals("Invalid section 'id' attribute","Section1",  tbxml.valueOfAttributeNamed("name", section));
                  assertEquals("Invalid section 'id' attribute","5",         tbxml.valueOfAttributeNamed("order",section));
                  assertEquals("Invalid section 'id' attribute","4B,A",      tbxml.valueOfAttributeNamed("bolts",item));
                  assertEquals("Invalid section 'id' attribute","18",        tbxml.valueOfAttributeNamed("grade",item));
                  assertEquals("Invalid section 'id' attribute","1",         tbxml.valueOfAttributeNamed("id",   item));
                  assertEquals("Invalid section 'id' attribute","Item1",     tbxml.valueOfAttributeNamed("name", item));
                  assertEquals("Invalid section 'id' attribute","***",       tbxml.valueOfAttributeNamed("stars",item));
                  assertEquals("Invalid section 'id' attribute","Them",      tbxml.valueOfAttributeNamed("by",   firstAscent));
                  assertEquals("Invalid section 'id' attribute","2013-03-21",tbxml.valueOfAttributeNamed("date", firstAscent));
                }

         public void testTextForElement() throws Exception
                { String xml = read(TESTXML);
                  
                  tbxml.parse(xml);
                  
                  long root        = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
                  long section     = tbxml.firstChild (root);        assertTrue("Invalid 'section' element",     section     != 0);
                  long item        = tbxml.firstChild (section);     assertTrue("Invalid 'item' element",       item        != 0);
                  long description = tbxml.firstChild (item );       assertTrue("Invalid 'description' element", description != 0);
                  long firstAscent = tbxml.nextSibling(description); assertTrue("Invalid 'first ascent' element",firstAscent != 0);
                  
                  assertEquals("Invalid 'description' text",DESCRIPTION,tbxml.textForElement(description));
                }

         public void testListElementsForQuery() throws Exception
                { String xml = read(TESTXML);
           
                   tbxml.parse(xml);
           
                   long   root   = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
                   long[] items = tbxml.listElementsForQuery("section.item",root);
                   
                   assertNotNull("Invalid element list",items);
                   assertEquals ("Invalid element list size",4,items.length);
                   assertEquals ("Invalid element[0]","1",tbxml.valueOfAttributeNamed("id",items[0]));
                   assertEquals ("Invalid element[1]","W",tbxml.valueOfAttributeNamed("id",items[1]));
                   assertEquals ("Invalid element[2]","R",tbxml.valueOfAttributeNamed("id",items[2]));
                   assertEquals ("Invalid element[3]","Y",tbxml.valueOfAttributeNamed("id",items[3]));
                }

         public void testListElementsForWildcardQuery() throws Exception
                { String xml = read(TESTXML);
    
                  tbxml.parse(xml);
    
                  long   root   = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
                  long[] items = tbxml.listElementsForQuery("section.itemx.*",root);
            
                  assertNotNull("Invalid element list",items);
                  assertEquals ("Invalid element list size",3,items.length);
                  assertEquals ("Invalid element[0]","M1",tbxml.valueOfAttributeNamed("id",items[0]));
                  assertEquals ("Invalid element[1]","M3",tbxml.valueOfAttributeNamed("id",items[1]));
                  assertEquals ("Invalid element[2]","M5",tbxml.valueOfAttributeNamed("id",items[2]));
                }

         public void testListElementsForEmbeddedWildcardQuery() throws Exception
                { String xml = read(TESTXML);
           
                   tbxml.parse(xml);

                   long   root   = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
                   long[] items = tbxml.listElementsForQuery("section.*.marker",root);
                   
                   assertNotNull("Invalid element list",items);
                   assertEquals ("Invalid element list size",7,items.length);
                   assertEquals ("Invalid element[0]","M0",tbxml.valueOfAttributeNamed("id",items[0]));
                   assertEquals ("Invalid element[1]","M1",tbxml.valueOfAttributeNamed("id",items[1]));
                   assertEquals ("Invalid element[2]","M2",tbxml.valueOfAttributeNamed("id",items[2]));
                   assertEquals ("Invalid element[3]","M3",tbxml.valueOfAttributeNamed("id",items[3]));
                   assertEquals ("Invalid element[4]","M4",tbxml.valueOfAttributeNamed("id",items[4]));
                   assertEquals ("Invalid element[5]","M5",tbxml.valueOfAttributeNamed("id",items[5]));
                   assertEquals ("Invalid element[6]","M6",tbxml.valueOfAttributeNamed("id",items[6]));
                }

         public void testListAttributesForElements() throws Exception
                { String xml = read(TESTXML);
           
                   tbxml.parse(xml);
           
                   long   root    = tbxml.rootXMLElement();        assertTrue("Invalid 'root' element",   root    != 0);
                   long   section = tbxml.firstChild    (root);    assertTrue("Invalid 'section' element",section != 0);
                   long   item    = tbxml.firstChild    (section); assertTrue("Invalid 'item' element",  item    != 0);
                   long[] attributes;
                   
                   attributes = tbxml.listAttributesOfElement(section);
                   
                   assertNotNull("Invalid 'section' attribute list",attributes);
                   assertEquals ("Invalid 'section' attribute list size",3,attributes.length);
                   
                   attributes = tbxml.listAttributesOfElement(item );
                   
                   assertNotNull("Invalid 'item' attribute list",attributes);
                   assertEquals ("Invalid 'item' attribute list size",5,attributes.length);
                }

         public void testAttributeName() throws Exception
                { String xml = read(TESTXML);
           
                   tbxml.parse(xml);
           
                   long   root    = tbxml.rootXMLElement();        assertTrue("Invalid 'root' element",   root    != 0);
                   long   section = tbxml.firstChild    (root);    assertTrue("Invalid 'section' element",section != 0);
                   long   item    = tbxml.firstChild    (section); assertTrue("Invalid 'item' element",  item    != 0);
                   long[] attributes;

                   // ... section
                   
                   attributes = tbxml.listAttributesOfElement(section);
                   
                   assertNotNull("Invalid 'section' attribute list",attributes);
                   assertEquals ("Invalid 'section' attribute list size",3,attributes.length);
                   
                   assertNotNull("Invalid 'section' 'id' attribute",tbxml.attributeName(attributes[0]));
                   assertEquals ("Invalid 'section' 'id' attribute","id",tbxml.attributeName(attributes[0]));
                   assertNotNull("Invalid 'section' 'name' attribute",tbxml.attributeName(attributes[1]));
                   assertEquals ("Invalid 'section' 'name' attribute","name",tbxml.attributeName(attributes[1]));
                   assertNotNull("Invalid 'section' 'order' attribute",tbxml.attributeName(attributes[2]));
                   assertEquals ("Invalid 'section' 'order' attribute","order",tbxml.attributeName(attributes[2]));

                   // ... item 
                   
                   attributes = tbxml.listAttributesOfElement(item );

                   assertNotNull("Invalid 'item' attribute list",attributes);
                   assertEquals ("Invalid 'item' attribute list size",5,attributes.length);
                   
                   assertNotNull("Invalid 'item' 'bolts' attribute",tbxml.attributeName(attributes[0]));
                   assertEquals ("Invalid 'item' 'bolts' attribute","bolts",tbxml.attributeName(attributes[0]));
                   assertNotNull("Invalid 'item' 'grade' attribute",tbxml.attributeName(attributes[1]));
                   assertEquals ("Invalid 'item' 'grade' attribute","grade",tbxml.attributeName(attributes[1]));
                   assertNotNull("Invalid 'item' 'id' attribute",   tbxml.attributeName(attributes[2]));
                   assertEquals ("Invalid 'item' 'id' attribute",   "id",tbxml.attributeName(attributes[2]));
                   assertNotNull("Invalid 'item' 'name' attribute", tbxml.attributeName(attributes[3]));
                   assertEquals ("Invalid 'item' 'name' attribute", "name",tbxml.attributeName(attributes[3]));
                   assertNotNull("Invalid 'item' 'stars' attribute",tbxml.attributeName(attributes[4]));
                   assertEquals ("Invalid 'item' 'stars' attribute","stars",tbxml.attributeName(attributes[4]));
                }

         public void testAttributeValue() throws Exception
                { String xml = read(TESTXML);
           
                   tbxml.parse(xml);
           
                   long   root    = tbxml.rootXMLElement();        assertTrue("Invalid 'root' element",   root    != 0);
                   long   section = tbxml.firstChild    (root);    assertTrue("Invalid 'section' element",section != 0);
                   long   item    = tbxml.firstChild    (section); assertTrue("Invalid 'item' element",  item    != 0);
                   long[] attributes;

                   // ... section
                   
                   attributes = tbxml.listAttributesOfElement(section);
                   
                   assertNotNull("Invalid 'section' attribute list",attributes);
                   assertEquals ("Invalid 'section' attribute list size",3,attributes.length);
                   
                   assertNotNull("Invalid 'section' 'id' attribute",tbxml.attributeValue(attributes[0]));
                   assertEquals ("Invalid 'section' 'id' attribute","10005000",tbxml.attributeValue(attributes[0]));
                   assertNotNull("Invalid 'section' 'name' attribute",tbxml.attributeValue(attributes[1]));
                   assertEquals ("Invalid 'section' 'name' attribute","Section1",tbxml.attributeValue(attributes[1]));
                   assertNotNull("Invalid 'section' 'order' attribute",tbxml.attributeValue(attributes[2]));
                   assertEquals ("Invalid 'section' 'order' attribute","5",tbxml.attributeValue(attributes[2]));

                   // ... item 
                   
                   attributes = tbxml.listAttributesOfElement(item );

                   assertNotNull("Invalid 'item' attribute list",attributes);
                   assertEquals ("Invalid 'item' attribute list size",5,attributes.length);
                   
                   assertNotNull("Invalid 'item' 'bolts' attribute",        tbxml.attributeValue(attributes[0]));
                   assertEquals ("Invalid 'item' 'bolts' attribute","4B,A", tbxml.attributeValue(attributes[0]));
                   assertNotNull("Invalid 'item' 'grade' attribute",        tbxml.attributeValue(attributes[1]));
                   assertEquals ("Invalid 'item' 'grade' attribute","18",   tbxml.attributeValue(attributes[1]));
                   assertNotNull("Invalid 'item' 'id' attribute",           tbxml.attributeValue(attributes[2]));
                   assertEquals ("Invalid 'item' 'id' attribute",   "1",    tbxml.attributeValue(attributes[2]));
                   assertNotNull("Invalid 'item' 'name' attribute",         tbxml.attributeValue(attributes[3]));
                   assertEquals ("Invalid 'item' 'name' attribute", "Item1",tbxml.attributeValue(attributes[3]));
                   assertNotNull("Invalid 'item' 'stars' attribute",        tbxml.attributeValue(attributes[4]));
                   assertEquals ("Invalid 'item' 'stars' attribute","***",  tbxml.attributeValue(attributes[4]));
                }

	     // UTILITY FUNCTIONS

	     private String read(int rid) throws Exception
                 { Context       context = getContext();
                   StringBuilder xml     = new StringBuilder();
                   Reader        reader  = new InputStreamReader(context.getResources().openRawResource(rid));
                   char[]        buffer  = new char[4096];
                   int           N;
           
                   while ((N = reader.read(buffer)) != -1)
                         { xml.append(buffer,0,N);
                         }
           
                   reader.close();
                   
                   return xml.toString();
                 }

       }
