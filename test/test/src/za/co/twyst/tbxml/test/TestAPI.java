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

         public void testAttributeValue() throws Exception
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
