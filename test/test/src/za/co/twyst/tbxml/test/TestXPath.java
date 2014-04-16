package za.co.twyst.tbxml.test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import android.content.Context;
import android.test.AndroidTestCase;

import za.co.twyst.tbxml.dummy.R;

public class TestXPath extends AndroidTestCase
       { // CONSTANTS
	
	     @SuppressWarnings("unused")
		 private static final String TAG         = "TBXML";
	     private static final String DESCRIPTION = "Takes the vague slightly right-slanting break about 3m left of the large chimney filled with chockstones.";
	     
	     // TEST VARIABLES

	     private XPath           xpath;
	     
	     // SETUP/TEARDOWN

	     protected void setUp() throws Exception 
	               { xpath   = XPathFactory.newInstance().newXPath();
	               }

	     protected void tearDown() throws Exception 
	               { 
	               }
	
	     // UNIT TESTS
	     
         public void testChildElementNamed() throws Exception
                { String      xml    = read(R.raw.routesx);
	              InputSource source = new InputSource(new StringReader(xml));
	              Node        root   = (Node) xpath.evaluate("/routes",source,XPathConstants.NODE);
	              
                  assertNotNull("Invalid 'routes' element",root);
                  assertEquals ("Invalid 'routes' element","routes",root.getNodeName());
	              
	              Node section = (Node) xpath.evaluate("section",root,XPathConstants.NODE);
	              
	              assertNotNull("Invalid 'section' element",section);
                  assertEquals ("Invalid 'section' element","section",section.getNodeName());

	              Node route = (Node) xpath.evaluate("route",section,XPathConstants.NODE);

	              assertNotNull("Invalid 'route' element",route);
                  assertEquals ("Invalid 'route' element","route",route.getNodeName());

	              Node description = (Node) xpath.evaluate("description",route,XPathConstants.NODE);

	              assertNotNull("Invalid 'description' element",description);
                  assertEquals ("Invalid 'description' element","description",description.getNodeName());

	              Node firstAscent = (Node) xpath.evaluate("first-ascent",route,XPathConstants.NODE);

	              assertNotNull("Invalid 'first-ascent' element",firstAscent);
                  assertEquals ("Invalid 'first-ascent' element","first-ascent",firstAscent.getNodeName());
                }
         
         public void testNextSiblingNamed() throws Exception
                { String      xml     = read(R.raw.routesx);
	              InputSource source  = new InputSource(new StringReader(xml));
	              Node        root    = (Node) xpath.evaluate("/routes",source,XPathConstants.NODE);
	              Node        section = (Node) xpath.evaluate("section",root,XPathConstants.NODE);
	              Node        route   = (Node) xpath.evaluate("route",section,XPathConstants.NODE);

                  assertEquals ("Invalid 'route' element","route",route.getNodeName());
                  assertEquals ("Invalid 'route' element","route",route.getNodeName());
                  assertEquals ("Invalid 'route' element","1",route.getAttributes().getNamedItem("id").getNodeValue());

	              Node w = (Node) xpath.evaluate("following-sibling::route",route,XPathConstants.NODE);

	              assertNotNull("Invalid 'w' element",w);
                  assertEquals ("Invalid 'w' element","route",w.getNodeName());
                  assertEquals ("Invalid 'w' element","W",w.getAttributes().getNamedItem("id").getNodeValue());

	              Node r = (Node) xpath.evaluate("following-sibling::route",w,XPathConstants.NODE);

	              assertNotNull("Invalid 'r' element",r);
                  assertEquals ("Invalid 'r' element","route",r.getNodeName());
                  assertEquals ("Invalid 'r' element","R",r.getAttributes().getNamedItem("id").getNodeValue());

	              Node y = (Node) xpath.evaluate("following-sibling::route",r,XPathConstants.NODE);

	              assertNotNull("Invalid 'y' element",y);
                  assertEquals ("Invalid 'y' element","route",y.getNodeName());
                  assertEquals ("Invalid 'y' element","Y",y.getAttributes().getNamedItem("id").getNodeValue());

                  Node last = (Node) xpath.evaluate("following-sibling::route",y,XPathConstants.NODE);

	              assertNull("Invalid 'last' element",last);
                }
	     
         public void testElementName() throws Exception
                { String      xml     = read(R.raw.routesx);
	              InputSource source  = new InputSource(new StringReader(xml));
	              Node        root    = (Node) xpath.evaluate("/routes",source,XPathConstants.NODE);
	              Node        section = (Node) xpath.evaluate("section",root,XPathConstants.NODE);
	              Node        route   = (Node) xpath.evaluate("route",section,XPathConstants.NODE);
	              Node        description = (Node) xpath.evaluate("description",route,XPathConstants.NODE);
	              Node        firstAscent = (Node) xpath.evaluate("first-ascent",route,XPathConstants.NODE);

                  assertEquals("Invalid 'root' element name",        "routes",      root.getNodeName());
                  assertEquals("Invalid 'section' element name",     "section",     section.getNodeName());
                  assertEquals("Invalid 'route' element name",       "route",       route.getNodeName());
                  assertEquals("Invalid 'description' element name", "description", description.getNodeName());
                  assertEquals("Invalid 'first-ascent' element name","first-ascent",firstAscent.getNodeName());
                }

//         public void testValueOfAttributeNamed() throws Exception
//                { String xml = read(R.raw.routesx);
//                  
//                  tbxml.parse(xml);
//                  
//                  long root        = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
//                  long section     = tbxml.firstChild (root);        assertTrue("Invalid 'section' element",     section     != 0);
//                  long route       = tbxml.firstChild (section);     assertTrue("Invalid 'route' element",       route       != 0);
//                  long description = tbxml.firstChild (route);       assertTrue("Invalid 'description' element", description != 0);
//                  long firstAscent = tbxml.nextSibling(description); assertTrue("Invalid 'first ascent' element",firstAscent != 0);
//                  
//                  assertEquals("Invalid section 'id' attribute","10005000",                  tbxml.valueOfAttributeNamed("id",   section));
//                  assertEquals("Invalid section 'id' attribute","Far Right",                 tbxml.valueOfAttributeNamed("name", section));
//                  assertEquals("Invalid section 'id' attribute","5",                         tbxml.valueOfAttributeNamed("order",section));
//                  assertEquals("Invalid section 'id' attribute","4B,A",                      tbxml.valueOfAttributeNamed("bolts",route));
//                  assertEquals("Invalid section 'id' attribute","18",                        tbxml.valueOfAttributeNamed("grade",route));
//                  assertEquals("Invalid section 'id' attribute","1",                         tbxml.valueOfAttributeNamed("id",   route));
//                  assertEquals("Invalid section 'id' attribute","Pixilated",                 tbxml.valueOfAttributeNamed("name", route));
//                  assertEquals("Invalid section 'id' attribute","***",                       tbxml.valueOfAttributeNamed("stars",route));
//                  assertEquals("Invalid section 'id' attribute","Peter Speed,Tony Seebregts",tbxml.valueOfAttributeNamed("by",   firstAscent));
//                  assertEquals("Invalid section 'id' attribute","2013-03-21",                tbxml.valueOfAttributeNamed("date", firstAscent));
//                }
//
//         public void testTextForElement() throws Exception
//                { String xml = read(R.raw.routesx);
//                  
//                  tbxml.parse(xml);
//                  
//                  long root        = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
//                  long section     = tbxml.firstChild (root);        assertTrue("Invalid 'section' element",     section     != 0);
//                  long route       = tbxml.firstChild (section);     assertTrue("Invalid 'route' element",       route       != 0);
//                  long description = tbxml.firstChild (route);       assertTrue("Invalid 'description' element", description != 0);
//                  long firstAscent = tbxml.nextSibling(description); assertTrue("Invalid 'first ascent' element",firstAscent != 0);
//                  
//                  assertEquals("Invalid 'description' text",DESCRIPTION,tbxml.textForElement(description));
//                }
//
//         public void testListElementsForQuery() throws Exception
//                { String xml = read(R.raw.routesx);
//           
//                   tbxml.parse(xml);
//           
//                   long   root   = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
//                   long[] routes = tbxml.listElementsForQuery("section.route",root);
//                   
//                   assertNotNull("Invalid element list",routes);
//                   assertEquals ("Invalid element list size",4,routes.length);
//                   assertEquals ("Invalid element[0]","1",tbxml.valueOfAttributeNamed("id",routes[0]));
//                   assertEquals ("Invalid element[1]","W",tbxml.valueOfAttributeNamed("id",routes[1]));
//                   assertEquals ("Invalid element[2]","R",tbxml.valueOfAttributeNamed("id",routes[2]));
//                   assertEquals ("Invalid element[3]","Y",tbxml.valueOfAttributeNamed("id",routes[3]));
//                }
//
//         public void testListElementsForWildcardQuery() throws Exception
//                { String xml = read(R.raw.routesx);
//    
//                  tbxml.parse(xml);
//    
//                  long   root   = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
//                  long[] routes = tbxml.listElementsForQuery("section.routex.*",root);
//            
//                  assertNotNull("Invalid element list",routes);
//                  assertEquals ("Invalid element list size",3,routes.length);
//                  assertEquals ("Invalid element[0]","M1",tbxml.valueOfAttributeNamed("id",routes[0]));
//                  assertEquals ("Invalid element[1]","M3",tbxml.valueOfAttributeNamed("id",routes[1]));
//                  assertEquals ("Invalid element[2]","M5",tbxml.valueOfAttributeNamed("id",routes[2]));
//                }
//
//         public void testListElementsForEmbeddedWildcardQuery() throws Exception
//                { String xml = read(R.raw.routesx);
//           
//                   tbxml.parse(xml);
//
//                   long   root   = tbxml.rootXMLElement();         assertTrue("Invalid 'root' element",        root        != 0);
//                   long[] routes = tbxml.listElementsForQuery("section.*.marker",root);
//                   
//                   assertNotNull("Invalid element list",routes);
//                   assertEquals ("Invalid element list size",7,routes.length);
//                   assertEquals ("Invalid element[0]","M0",tbxml.valueOfAttributeNamed("id",routes[0]));
//                   assertEquals ("Invalid element[1]","M1",tbxml.valueOfAttributeNamed("id",routes[1]));
//                   assertEquals ("Invalid element[2]","M2",tbxml.valueOfAttributeNamed("id",routes[2]));
//                   assertEquals ("Invalid element[3]","M3",tbxml.valueOfAttributeNamed("id",routes[3]));
//                   assertEquals ("Invalid element[4]","M4",tbxml.valueOfAttributeNamed("id",routes[4]));
//                   assertEquals ("Invalid element[5]","M5",tbxml.valueOfAttributeNamed("id",routes[5]));
//                   assertEquals ("Invalid element[6]","M6",tbxml.valueOfAttributeNamed("id",routes[6]));
//                }
//
//         public void testListAttributesForElements() throws Exception
//                { String xml = read(R.raw.routesx);
//           
//                   tbxml.parse(xml);
//           
//                   long   root    = tbxml.rootXMLElement();        assertTrue("Invalid 'root' element",   root    != 0);
//                   long   section = tbxml.firstChild    (root);    assertTrue("Invalid 'section' element",section != 0);
//                   long   route   = tbxml.firstChild    (section); assertTrue("Invalid 'route' element",  route   != 0);
//                   long[] attributes;
//                   
//                   attributes = tbxml.listAttributesOfElement(section);
//                   
//                   assertNotNull("Invalid 'section' attribute list",attributes);
//                   assertEquals ("Invalid 'section' attribute list size",3,attributes.length);
//                   
//                   attributes = tbxml.listAttributesOfElement(route);
//                   
//                   assertNotNull("Invalid 'route' attribute list",attributes);
//                   assertEquals ("Invalid 'route' attribute list size",5,attributes.length);
//                }
//
//         public void testAttributeName() throws Exception
//                { String xml = read(R.raw.routesx);
//           
//                   tbxml.parse(xml);
//           
//                   long   root    = tbxml.rootXMLElement();        assertTrue("Invalid 'root' element",   root    != 0);
//                   long   section = tbxml.firstChild    (root);    assertTrue("Invalid 'section' element",section != 0);
//                   long   route   = tbxml.firstChild    (section); assertTrue("Invalid 'route' element",  route   != 0);
//                   long[] attributes;
//
//                   // ... section
//                   
//                   attributes = tbxml.listAttributesOfElement(section);
//                   
//                   assertNotNull("Invalid 'section' attribute list",attributes);
//                   assertEquals ("Invalid 'section' attribute list size",3,attributes.length);
//                   
//                   assertNotNull("Invalid 'section' 'id' attribute",tbxml.attributeName(attributes[0]));
//                   assertEquals ("Invalid 'section' 'id' attribute","id",tbxml.attributeName(attributes[0]));
//                   assertNotNull("Invalid 'section' 'name' attribute",tbxml.attributeName(attributes[1]));
//                   assertEquals ("Invalid 'section' 'name' attribute","name",tbxml.attributeName(attributes[1]));
//                   assertNotNull("Invalid 'section' 'order' attribute",tbxml.attributeName(attributes[2]));
//                   assertEquals ("Invalid 'section' 'order' attribute","order",tbxml.attributeName(attributes[2]));
//
//                   // ... route
//                   
//                   attributes = tbxml.listAttributesOfElement(route);
//
//                   assertNotNull("Invalid 'route' attribute list",attributes);
//                   assertEquals ("Invalid 'route' attribute list size",5,attributes.length);
//                   
//                   assertNotNull("Invalid 'route' 'bolts' attribute",tbxml.attributeName(attributes[0]));
//                   assertEquals ("Invalid 'route' 'bolts' attribute","bolts",tbxml.attributeName(attributes[0]));
//                   assertNotNull("Invalid 'route' 'grade' attribute",tbxml.attributeName(attributes[1]));
//                   assertEquals ("Invalid 'route' 'grade' attribute","grade",tbxml.attributeName(attributes[1]));
//                   assertNotNull("Invalid 'route' 'id' attribute",   tbxml.attributeName(attributes[2]));
//                   assertEquals ("Invalid 'route' 'id' attribute",   "id",tbxml.attributeName(attributes[2]));
//                   assertNotNull("Invalid 'route' 'name' attribute", tbxml.attributeName(attributes[3]));
//                   assertEquals ("Invalid 'route' 'name' attribute", "name",tbxml.attributeName(attributes[3]));
//                   assertNotNull("Invalid 'route' 'stars' attribute",tbxml.attributeName(attributes[4]));
//                   assertEquals ("Invalid 'route' 'stars' attribute","stars",tbxml.attributeName(attributes[4]));
//                }
//
//         public void testAttributeValue() throws Exception
//                { String xml = read(R.raw.routesx);
//           
//                   tbxml.parse(xml);
//           
//                   long   root    = tbxml.rootXMLElement();        assertTrue("Invalid 'root' element",   root    != 0);
//                   long   section = tbxml.firstChild    (root);    assertTrue("Invalid 'section' element",section != 0);
//                   long   route   = tbxml.firstChild    (section); assertTrue("Invalid 'route' element",  route   != 0);
//                   long[] attributes;
//
//                   // ... section
//                   
//                   attributes = tbxml.listAttributesOfElement(section);
//                   
//                   assertNotNull("Invalid 'section' attribute list",attributes);
//                   assertEquals ("Invalid 'section' attribute list size",3,attributes.length);
//                   
//                   assertNotNull("Invalid 'section' 'id' attribute",tbxml.attributeValue(attributes[0]));
//                   assertEquals ("Invalid 'section' 'id' attribute","10005000",tbxml.attributeValue(attributes[0]));
//                   assertNotNull("Invalid 'section' 'name' attribute",tbxml.attributeValue(attributes[1]));
//                   assertEquals ("Invalid 'section' 'name' attribute","Far Right",tbxml.attributeValue(attributes[1]));
//                   assertNotNull("Invalid 'section' 'order' attribute",tbxml.attributeValue(attributes[2]));
//                   assertEquals ("Invalid 'section' 'order' attribute","5",tbxml.attributeValue(attributes[2]));
//
//                   // ... route
//                   
//                   attributes = tbxml.listAttributesOfElement(route);
//
//                   assertNotNull("Invalid 'route' attribute list",attributes);
//                   assertEquals ("Invalid 'route' attribute list size",5,attributes.length);
//                   
//                   assertNotNull("Invalid 'route' 'bolts' attribute",tbxml.attributeValue(attributes[0]));
//                   assertEquals ("Invalid 'route' 'bolts' attribute","4B,A",tbxml.attributeValue(attributes[0]));
//                   assertNotNull("Invalid 'route' 'grade' attribute",tbxml.attributeValue(attributes[1]));
//                   assertEquals ("Invalid 'route' 'grade' attribute","18",tbxml.attributeValue(attributes[1]));
//                   assertNotNull("Invalid 'route' 'id' attribute",   tbxml.attributeValue(attributes[2]));
//                   assertEquals ("Invalid 'route' 'id' attribute",   "1",tbxml.attributeValue(attributes[2]));
//                   assertNotNull("Invalid 'route' 'name' attribute", tbxml.attributeValue(attributes[3]));
//                   assertEquals ("Invalid 'route' 'name' attribute", "Pixilated",tbxml.attributeValue(attributes[3]));
//                   assertNotNull("Invalid 'route' 'stars' attribute",tbxml.attributeValue(attributes[4]));
//                   assertEquals ("Invalid 'route' 'stars' attribute","***",tbxml.attributeValue(attributes[4]));
//                }

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
