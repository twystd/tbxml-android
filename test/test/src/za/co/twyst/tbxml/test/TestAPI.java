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
		 private static final String TAG = "TBXML";
	     
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
