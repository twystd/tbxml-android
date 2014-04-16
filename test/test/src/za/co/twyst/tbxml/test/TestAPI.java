package za.co.twyst.tbxml.test;

import java.io.InputStreamReader;
import java.io.Reader;

import android.content.Context;
import android.test.AndroidTestCase;

import za.co.twyst.tbxml.dummy.R;
import za.co.twyst.tbxml.TBXML;

public class TestAPI extends AndroidTestCase
       { // CONSTANTS
	
	     @SuppressWarnings("unused")
		 private static final String TAG         = "TBXML";
	     private static final String DESCRIPTION = "Takes the vague slightly right-slanting break about 3m left of the large chimney filled with chockstones.";
	     
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
	            { String xml = read(R.raw.routesx);
	              
	              tbxml.parse(xml);
	              
	              long root        = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
	              long section     = tbxml.firstChild (root);        assertTrue("Invalid 'section' element",     section     != 0);
                  long route       = tbxml.firstChild (section);     assertTrue("Invalid 'route' element",       route       != 0);
                  long description = tbxml.firstChild (route);       assertTrue("Invalid 'description' element", description != 0);
                  long firstAscent = tbxml.nextSibling(description); assertTrue("Invalid 'first ascent' element",firstAscent != 0);
	            }
	     
         public void testChildElementNamed() throws Exception
                { String xml = read(R.raw.routesx);
                  
                  tbxml.parse(xml);
                  
                  long root        = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
                  long section     = tbxml.firstChild (root);        assertTrue("Invalid 'section' element",     section     != 0);
                  long route       = tbxml.firstChild (section);     assertTrue("Invalid 'route' element",       route       != 0);
                  long description = tbxml.firstChild (route);       assertTrue("Invalid 'description' element", description != 0);
                  long firstAscent = tbxml.nextSibling(description); assertTrue("Invalid 'first ascent' element",firstAscent != 0);
                  
                  assertEquals("Invalid 'section' element",    section,     tbxml.childElementNamed("section",     root));
                  assertEquals("Invalid 'route' element",      route,       tbxml.childElementNamed("route",       section));
                  assertEquals("Invalid 'description' element", description,tbxml.childElementNamed("description", route));
                  assertEquals("Invalid 'first-ascent' element",firstAscent,tbxml.childElementNamed("first-ascent",route));
                }
         
         public void testNextSiblingNamed() throws Exception
                { String xml = read(R.raw.routesx);
           
                  tbxml.parse(xml);
           
                  long root    = tbxml.rootXMLElement();     assertTrue("Invalid 'root' element",   root    != 0);
                  long section = tbxml.firstChild (root);    assertTrue("Invalid 'section' element",section != 0);
                  long route   = tbxml.firstChild (section); assertTrue("Invalid 'route' element",  route   != 0);
                  long q       = tbxml.nextSibling(route);   assertTrue("Invalid 'q' element",      q       != 0);
                  long w       = tbxml.nextSibling(q);       assertTrue("Invalid 'w' element",      w       != 0);
                  long e       = tbxml.nextSibling(w);       assertTrue("Invalid 'e' element",      e       != 0);
                  long r       = tbxml.nextSibling(e);       assertTrue("Invalid 'r' element",      r       != 0);
                  long t       = tbxml.nextSibling(r);       assertTrue("Invalid 't' element",      t       != 0);
                  long y       = tbxml.nextSibling(t);       assertTrue("Invalid 'y' element",      y       != 0);
           
                  assertEquals("Invalid 'w' element",w,tbxml.nextSiblingNamed("route", route));
                  assertEquals("Invalid 'r' element",r,tbxml.nextSiblingNamed("route", w));
                  assertEquals("Invalid 'y' element",y,tbxml.nextSiblingNamed("route", r));

                  assertEquals("Invalid 'q' element",q,tbxml.nextSiblingNamed("routex", route));
                  assertEquals("Invalid 'e' element",e,tbxml.nextSiblingNamed("routex", q));
                  assertEquals("Invalid 't' element",t,tbxml.nextSiblingNamed("routex", e));
                }
	     
         public void testElementName() throws Exception
                { String xml = read(R.raw.routesx);
                  
                  tbxml.parse(xml);
                  
                  long root        = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
                  long section     = tbxml.firstChild (root);        assertTrue("Invalid 'section' element",     section     != 0);
                  long route       = tbxml.firstChild (section);     assertTrue("Invalid 'route' element",       route       != 0);
                  long description = tbxml.firstChild (route);       assertTrue("Invalid 'description' element", description != 0);
                  long firstAscent = tbxml.nextSibling(description); assertTrue("Invalid 'first ascent' element",firstAscent != 0);
                  
                  assertEquals("Invalid 'root' element name",        "routes",      tbxml.elementName(root));
                  assertEquals("Invalid 'section' element name",     "section",     tbxml.elementName(section));
                  assertEquals("Invalid 'route' element name",       "route",       tbxml.elementName(route));
                  assertEquals("Invalid 'description' element name", "description", tbxml.elementName(description));
                  assertEquals("Invalid 'first-ascent' element name","first-ascent",tbxml.elementName(firstAscent));
                }

         public void testValueOfAttributeNamed() throws Exception
                { String xml = read(R.raw.routesx);
                  
                  tbxml.parse(xml);
                  
                  long root        = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
                  long section     = tbxml.firstChild (root);        assertTrue("Invalid 'section' element",     section     != 0);
                  long route       = tbxml.firstChild (section);     assertTrue("Invalid 'route' element",       route       != 0);
                  long description = tbxml.firstChild (route);       assertTrue("Invalid 'description' element", description != 0);
                  long firstAscent = tbxml.nextSibling(description); assertTrue("Invalid 'first ascent' element",firstAscent != 0);
                  
                  assertEquals("Invalid section 'id' attribute","10005000",                  tbxml.valueOfAttributeNamed("id",   section));
                  assertEquals("Invalid section 'id' attribute","Far Right",                 tbxml.valueOfAttributeNamed("name", section));
                  assertEquals("Invalid section 'id' attribute","5",                         tbxml.valueOfAttributeNamed("order",section));
                  assertEquals("Invalid section 'id' attribute","4B,A",                      tbxml.valueOfAttributeNamed("bolts",route));
                  assertEquals("Invalid section 'id' attribute","18",                        tbxml.valueOfAttributeNamed("grade",route));
                  assertEquals("Invalid section 'id' attribute","1",                         tbxml.valueOfAttributeNamed("id",   route));
                  assertEquals("Invalid section 'id' attribute","Pixilated",                 tbxml.valueOfAttributeNamed("name", route));
                  assertEquals("Invalid section 'id' attribute","***",                       tbxml.valueOfAttributeNamed("stars",route));
                  assertEquals("Invalid section 'id' attribute","Peter Speed,Tony Seebregts",tbxml.valueOfAttributeNamed("by",   firstAscent));
                  assertEquals("Invalid section 'id' attribute","2013-03-21",                tbxml.valueOfAttributeNamed("date", firstAscent));
                }

         public void testTextForElement() throws Exception
                { String xml = read(R.raw.routesx);
                  
                  tbxml.parse(xml);
                  
                  long root        = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
                  long section     = tbxml.firstChild (root);        assertTrue("Invalid 'section' element",     section     != 0);
                  long route       = tbxml.firstChild (section);     assertTrue("Invalid 'route' element",       route       != 0);
                  long description = tbxml.firstChild (route);       assertTrue("Invalid 'description' element", description != 0);
                  long firstAscent = tbxml.nextSibling(description); assertTrue("Invalid 'first ascent' element",firstAscent != 0);
                  
                  assertEquals("Invalid 'description' text",DESCRIPTION,tbxml.textForElement(description));
                }

         public void testListElementsForQuery() throws Exception
                { String xml = read(R.raw.routesx);
           
                   tbxml.parse(xml);
           
                   long   root   = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
                   long[] routes = tbxml.listElementsForQuery("section.route",root);
                   
                   assertNotNull("Invalid element list",routes);
                   assertEquals ("Invalid element list size",4,routes.length);
                   assertEquals ("Invalid element[0]","1",tbxml.valueOfAttributeNamed("id",routes[0]));
                   assertEquals ("Invalid element[1]","W",tbxml.valueOfAttributeNamed("id",routes[1]));
                   assertEquals ("Invalid element[2]","R",tbxml.valueOfAttributeNamed("id",routes[2]));
                   assertEquals ("Invalid element[3]","Y",tbxml.valueOfAttributeNamed("id",routes[3]));
                }

         public void testListElementsForWildcardQuery() throws Exception
                { String xml = read(R.raw.routesx);
    
                  tbxml.parse(xml);
    
                  long   root   = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
                  long[] routes = tbxml.listElementsForQuery("section.routex.*",root);
            
                  assertNotNull("Invalid element list",routes);
                  assertEquals ("Invalid element list size",4,routes.length);
                  assertEquals ("Invalid element[0]","1",tbxml.valueOfAttributeNamed("id",routes[0]));
                  assertEquals ("Invalid element[1]","W",tbxml.valueOfAttributeNamed("id",routes[1]));
                  assertEquals ("Invalid element[2]","R",tbxml.valueOfAttributeNamed("id",routes[2]));
                  assertEquals ("Invalid element[3]","Y",tbxml.valueOfAttributeNamed("id",routes[3]));
                }

         public void testListElementsForEmbeddedWildcardQuery() throws Exception
                { String xml = read(R.raw.routesx);
           
                   tbxml.parse(xml);

                   long   root   = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
                   long[] routes = tbxml.listElementsForQuery("section.*.marker",root);
                   
                   assertNotNull("Invalid element list",routes);
                   assertEquals ("Invalid element list size",7,routes.length);
                   assertEquals ("Invalid element[0]","1",tbxml.valueOfAttributeNamed("id",routes[0]));
                   assertEquals ("Invalid element[1]","W",tbxml.valueOfAttributeNamed("id",routes[1]));
                   assertEquals ("Invalid element[2]","R",tbxml.valueOfAttributeNamed("id",routes[2]));
                   assertEquals ("Invalid element[3]","Y",tbxml.valueOfAttributeNamed("id",routes[3]));
                   fail("NOT IMPLEMENTED YET");
                }

         public void testListAttributesForElements() throws Exception
                { String xml = read(R.raw.routesx);
           
                   tbxml.parse(xml);
           
                   long   root    = tbxml.rootXMLElement();        assertTrue("Invalid 'root' element",   root    != 0);
                   long   section = tbxml.firstChild    (root);    assertTrue("Invalid 'section' element",section != 0);
                   long   route   = tbxml.firstChild    (section); assertTrue("Invalid 'route' element",  route   != 0);
                   long[] attributes;
                   
                   attributes = tbxml.listAttributesOfElement(section);
                   
                   assertNotNull("Invalid 'section' attribute list",attributes);
                   assertEquals ("Invalid 'section' attribute list size",3,attributes.length);
                   
                   attributes = tbxml.listAttributesOfElement(route);
                   
                   assertNotNull("Invalid 'route' attribute list",attributes);
                   assertEquals ("Invalid 'route' attribute list size",5,attributes.length);
                }

         public void testAttributeName() throws Exception
                { String xml = read(R.raw.routesx);
           
                   tbxml.parse(xml);
           
                   long   root    = tbxml.rootXMLElement();        assertTrue("Invalid 'root' element",   root    != 0);
                   long   section = tbxml.firstChild    (root);    assertTrue("Invalid 'section' element",section != 0);
                   long   route   = tbxml.firstChild    (section); assertTrue("Invalid 'route' element",  route   != 0);
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

                   // ... route
                   
                   attributes = tbxml.listAttributesOfElement(route);

                   assertNotNull("Invalid 'route' attribute list",attributes);
                   assertEquals ("Invalid 'route' attribute list size",5,attributes.length);
                   
                   assertNotNull("Invalid 'route' 'bolts' attribute",tbxml.attributeName(attributes[0]));
                   assertEquals ("Invalid 'route' 'bolts' attribute","bolts",tbxml.attributeName(attributes[0]));
                   assertNotNull("Invalid 'route' 'grade' attribute",tbxml.attributeName(attributes[1]));
                   assertEquals ("Invalid 'route' 'grade' attribute","grade",tbxml.attributeName(attributes[1]));
                   assertNotNull("Invalid 'route' 'id' attribute",   tbxml.attributeName(attributes[2]));
                   assertEquals ("Invalid 'route' 'id' attribute",   "id",tbxml.attributeName(attributes[2]));
                   assertNotNull("Invalid 'route' 'name' attribute", tbxml.attributeName(attributes[3]));
                   assertEquals ("Invalid 'route' 'name' attribute", "name",tbxml.attributeName(attributes[3]));
                   assertNotNull("Invalid 'route' 'stars' attribute",tbxml.attributeName(attributes[4]));
                   assertEquals ("Invalid 'route' 'stars' attribute","stars",tbxml.attributeName(attributes[4]));
                }

         public void testAttributeValue() throws Exception
                { String xml = read(R.raw.routesx);
           
                   tbxml.parse(xml);
           
                   long   root    = tbxml.rootXMLElement();        assertTrue("Invalid 'root' element",   root    != 0);
                   long   section = tbxml.firstChild    (root);    assertTrue("Invalid 'section' element",section != 0);
                   long   route   = tbxml.firstChild    (section); assertTrue("Invalid 'route' element",  route   != 0);
                   long[] attributes;

                   // ... section
                   
                   attributes = tbxml.listAttributesOfElement(section);
                   
                   assertNotNull("Invalid 'section' attribute list",attributes);
                   assertEquals ("Invalid 'section' attribute list size",3,attributes.length);
                   
                   assertNotNull("Invalid 'section' 'id' attribute",tbxml.attributeValue(attributes[0]));
                   assertEquals ("Invalid 'section' 'id' attribute","10005000",tbxml.attributeValue(attributes[0]));
                   assertNotNull("Invalid 'section' 'name' attribute",tbxml.attributeValue(attributes[1]));
                   assertEquals ("Invalid 'section' 'name' attribute","Far Right",tbxml.attributeValue(attributes[1]));
                   assertNotNull("Invalid 'section' 'order' attribute",tbxml.attributeValue(attributes[2]));
                   assertEquals ("Invalid 'section' 'order' attribute","5",tbxml.attributeValue(attributes[2]));

                   // ... route
                   
                   attributes = tbxml.listAttributesOfElement(route);

                   assertNotNull("Invalid 'route' attribute list",attributes);
                   assertEquals ("Invalid 'route' attribute list size",5,attributes.length);
                   
                   assertNotNull("Invalid 'route' 'bolts' attribute",tbxml.attributeValue(attributes[0]));
                   assertEquals ("Invalid 'route' 'bolts' attribute","4B,A",tbxml.attributeValue(attributes[0]));
                   assertNotNull("Invalid 'route' 'grade' attribute",tbxml.attributeValue(attributes[1]));
                   assertEquals ("Invalid 'route' 'grade' attribute","18",tbxml.attributeValue(attributes[1]));
                   assertNotNull("Invalid 'route' 'id' attribute",   tbxml.attributeValue(attributes[2]));
                   assertEquals ("Invalid 'route' 'id' attribute",   "1",tbxml.attributeValue(attributes[2]));
                   assertNotNull("Invalid 'route' 'name' attribute", tbxml.attributeValue(attributes[3]));
                   assertEquals ("Invalid 'route' 'name' attribute", "Pixilated",tbxml.attributeValue(attributes[3]));
                   assertNotNull("Invalid 'route' 'stars' attribute",tbxml.attributeValue(attributes[4]));
                   assertEquals ("Invalid 'route' 'stars' attribute","***",tbxml.attributeValue(attributes[4]));
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
