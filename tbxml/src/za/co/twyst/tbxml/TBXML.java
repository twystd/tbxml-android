package za.co.twyst.tbxml;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class TBXML
       { // CONSTANTS
         
         // INNER CLASSES

         @SuppressWarnings("serial")
         public class TBXMLException extends Exception 
                { public TBXMLException(String exception) 
                         { super(exception);
                         }
                }
    
         // INSTANCE VARIABLES
         
         private long document = 0;
    
         // CLASS METHODS
         
         static 
             { System.loadLibrary("tbxml");
             }
         
         // NATIVE METHODS
         
         private native long   jniParse                   (byte[] xml);
         private native void   jniFree                    (long document);
         private native long   jniRootElement             (long document);
         private native long   jniFirstChild              (long document,long element);
         private native long   jniChildElementNamed       (long document,long element,String tag);
         private native long   jniNextSibling             (long document,long element);
         private native long   jniNextSiblingNamed        (long document,long element,String tag);
         private native String jniElementName             (long document,long element);
         private native long[] jniListElementsForQuery    (long document,long element,String query);
         private native long[] jniListAttributesForElement(long document,long element);
         private native String jniAttributeName           (long document,long attribute);
         private native String jniAttributeValue          (long document,long attribute);
         private native String jniValueOfAttributeNamed   (long document,long element,String attribute);
         private native String jniTextForElement          (long document,long element);
         
         // CONSTRUCTORS

         public TBXML() 
                { 
                }

         // INSTANCE METHODS

         public void parse(String xml) throws TBXMLException
                { jniFree(document);
                  
                  if ((document = jniParse(xml.getBytes())) == 0)
                     { throw new TBXMLException("Invalid document handle");
                     }
                }

         public void release()
                { jniFree(document);
                }

         public long rootXMLElement() 
                { return jniRootElement(document);
                }

         public long firstChild(long element) 
                { return jniFirstChild(document,element);
                }

         public long childElementNamed(String name,long element)
                { return jniChildElementNamed(document,element,name);
                }

         public long nextSibling(long element) 
                { return jniNextSibling(document,element);
                }

         public long nextSiblingNamed(String tag,long element)
                { return jniNextSiblingNamed(document,element,tag);
                }

         public String elementName(long element) 
                { return jniElementName(document,element);
                }
         
         public String attributeName(long attribute) 
                { return jniAttributeName(document,attribute);
                }
         
         public String attributeValue(long attribute) 
                { return jniAttributeValue(document,attribute);
                }
         
         public String valueOfAttributeNamed(String attribute,long element) 
                { return jniValueOfAttributeNamed(document,element,attribute);
                }

         public String textForElement(long element) 
                { return jniTextForElement(document,element);
                }

         public long[] listElementsForQuery(String query,long element)
                { Log.w("TRACE","listElementsForQuery [" + query + "]");
                
        	      List<String> segments = new ArrayList<String>();
        	      String[]     tokens   = query == null ? new String[0] : query.split("\\.");
        	      String       segment  = null;
        	      long[]       elements = new long[0];
                
        	      for (String token: tokens)
        	          { segment = segment == null ? token : segment + "." + token;
        	          
        	            if (token.equals("*"))
        	               { segments.add(segment);
        	                 segment = null;
        	               }
        	          }
        	 
        	      if (segment != null)
        	         { segments.add(segment);
        	         }
        	      
//        	      for (String string: segments)
//        	    	  { elements = jniListElementsForQuery(document,element,string);
//        	    	  }
        	      
        	      Log.w("TRACE","listElementsForQuery/X [" + query + "]");
        	      return jniListElementsForQuery(document,element,query);
                }

         public long[] listAttributesOfElement(long element)
                { return jniListAttributesForElement(document,element);
                }
       }

