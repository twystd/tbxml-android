package za.co.twyst.xml.parsers.c;

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
         
         private native long   jniParse       (byte[] xml);
         private native void   jniFree        (long document);
         private native long   jniRootElement (long document);
         private native long   jniFirstChild  (long document,long element);
         private native long   jniNextSibling (long document,long element);
         private native long   jniChildElement(long document,long element,String tag);
         private native String jniElementName (long document,long element);
         private native String jniValueOfAttributeForElement(long document,long element,String attribute);
         private native String jniTextForElement(long document,long element);
         
         // CONSTRUCTORS

         public TBXML() 
                { 
                }

         // INSTANCE METHODS

         public void parse(String xml) throws TBXMLException
                { jniFree(document);
                  
                  document = jniParse(xml.getBytes());
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

         public long nextSibling(long element) 
                { return jniNextSibling(document,element);
                }

         public long childElement(String tag,long element) 
                { return jniChildElement(document,element,tag);
                }

         public String elementName(long element) 
                { return jniElementName(document,element);
                }
         
        public String valueOfAttributeForElement(String attribute,long element) 
               { return jniValueOfAttributeForElement(document,element,attribute);
               }
        
       public String textForElement(long element) 
              { return jniTextForElement(document,element);
              }

         // IMPLEMENTATION
         
         //TODO: \0 values are written at a bad place, spaces often appear after the texts. 
         //private void decodeBytes() throws TBXMLException
         //        { jniDecodeBytes(this);
         //        }
       }
