package za.co.twyst.xml.parsers.java;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class TBXML 
       { // CONSTANTS
	
	     private final static int MAX_ELEMENTS   = 100;
	     private final static int MAX_ATTRIBUTES = 100;
	     private final static int BUFFER_SIZE    = 1024;

	     private final static int TBXML_ATTRIBUTE_NAME_START  = 0;
	     private final static int TBXML_ATTRIBUTE_NAME_END    = 1;
	     private final static int TBXML_ATTRIBUTE_VALUE_START = 2;
	     private final static int TBXML_ATTRIBUTE_VALUE_END   = 3;
	     private final static int TBXML_ATTRIBUTE_CDATA_END   = 4;
	
	     // INNER CLASSES
	     
	     public class TBXMLAttribute  
	            { public TBXMLAttribute next;
	            
	              private int name  = -1;
	              private int value = -1;
	            }
	
	     public class TBXMLElement 
	            { public TBXMLAttribute firstAttribute;
	              public TBXMLElement   parentElement;
	              public TBXMLElement   firstChild;
	              public TBXMLElement   currentChild;
	              public TBXMLElement   nextSibling;
	              public TBXMLElement   previousSibling;
	            
	              private int name = -1;
	              private int text = -1;
	            }
	
	     @SuppressWarnings("serial")
		 public class TBXMLException extends Exception 
	            { public TBXMLException(String exception) 
	                     { super(exception);
	                     }
	            }
	
	     private class TBXMLElementBuffer 
	             { private TBXMLElement[]     elements;
	               private TBXMLElementBuffer next;
	               @SuppressWarnings("unused")
                   private TBXMLElementBuffer previous;
	             }
	     
	     private class TBXMLAttributeBuffer 
	             { private TBXMLAttribute[]     attributes;
	               private TBXMLAttributeBuffer next;
	               @SuppressWarnings("unused")
                   private TBXMLAttributeBuffer previous;
	             }
	
	     // INSTANCE VARIABLES
	     
	     private TBXMLElement         rootXMLElement;
	     private TBXMLElementBuffer   currentElementBuffer;
	     private TBXMLAttributeBuffer currentAttributeBuffer;
	     private int                  currentElement   = 0;
	     private int                  currentAttribute = 0;
	     private char[]               bytes;
	
	     // CONSTRUCTORS

	     public TBXML(String data) throws TBXMLException
                { bytes = data.toCharArray();
                  
                  decodeBytes();
                }

	     public TBXML(URI uri) throws TBXMLException,MalformedURLException,IOException
	            { this(uri.toURL());
	            }

	     public TBXML(URL url) throws IOException, TBXMLException 
	            { BufferedInputStream   in     = new BufferedInputStream  (url.openStream(),BUFFER_SIZE);
	              ByteArrayOutputStream baos   = new ByteArrayOutputStream(BUFFER_SIZE);
	              byte                  data[] = new byte[BUFFER_SIZE];
	              int                   len;
		
	              while((len=in.read(data,0,BUFFER_SIZE)) >= 0)
	                   { baos.write(data, 0, len);
	                   }
	              
	              baos.close();
	              in.close  ();
		
	              bytes = baos.toString("UTF-16LE").toCharArray();
	              
	              decodeBytes();
	            }

	     // INSTANCE METHODS

	     public TBXMLElement rootXMLElement()
	            { return rootXMLElement;
	            }
	     
	     public String elementName(TBXMLElement aXMLElement)
	            { if (aXMLElement == null || aXMLElement.name < 0) 
	                 return "";
		
	              return getText(aXMLElement.name);
	            }
	
	     public String textForElement(TBXMLElement aXMLElement)
	            { if (aXMLElement == null || aXMLElement.text < 0) 
	                 return "";
		
	              return getText(aXMLElement.text);
	            }
	
	     public String valueOfAttributeForElement(String aName, TBXMLElement aXMLElement)
	            { if (aName == null || aXMLElement == null) 
	                 return null;

	               TBXMLAttribute attribute = aXMLElement.firstAttribute;
	               
	               while (attribute != null) 
	                     { if (getText(attribute.name).equals(aName))
	                          return getText(attribute.value);
	                     
	                       attribute = attribute.next;
	                     }
	               return null;
	            }
	
	     public String attributeName(TBXMLAttribute aXMLAttribute)
	            { if (aXMLAttribute == null || aXMLAttribute.name < 0) 
	                 return "";
	               
	              return getText(aXMLAttribute.name);
	            }

	     public String attributeValue(TBXMLAttribute aXMLAttribute)
	            { if (aXMLAttribute == null || aXMLAttribute.value < 0) 
	                 return "";
		
	              return getText(aXMLAttribute.value);
	            }
	
	     public TBXMLElement nextSibling(String aName, TBXMLElement aXMLElement)
	            { if (aName == null || aXMLElement == null) 
	                 return null;

	              TBXMLElement xmlElement = aXMLElement.nextSibling;
	              
	              while (xmlElement != null) 
	                    { if (getText(xmlElement.name).equals(aName))
	                         return xmlElement;
			
	                      xmlElement = xmlElement.nextSibling;
	                    }
	              return null;
	            }
	
	     public TBXMLElement childElement(String aName, TBXMLElement aParentXMLElement)
	            { if (aName == null || aParentXMLElement == null) 
	                 return null;

	              TBXMLElement xmlElement = aParentXMLElement.firstChild;
	              
	              while (xmlElement != null) 
	                    { if (getText(xmlElement.name).equals(aName))
	                         return xmlElement;
			
	                      xmlElement = xmlElement.nextSibling;
	                    }
		
	              return null;
	            }

	     // IMPLEMENTATION
	     
	     private TBXMLElement nextAvailableElement()
	             { currentElement++;
		
	               if (currentElementBuffer == null) 
	                  { currentElementBuffer = new TBXMLElementBuffer();
	                    currentElementBuffer.elements = new TBXMLElement[MAX_ELEMENTS];
	                    currentElement = 0;
	                    currentElementBuffer.elements[currentElement] = new TBXMLElement();
	                    rootXMLElement = currentElementBuffer.elements[currentElement];
	                  }
	                  else if (currentElement >= MAX_ELEMENTS)
	                  { currentElementBuffer.next = new TBXMLElementBuffer();
	                    currentElementBuffer.next.previous = currentElementBuffer;
	                    currentElementBuffer = currentElementBuffer.next;
	                    currentElementBuffer.elements = new TBXMLElement[MAX_ELEMENTS];
	                    currentElement = 0;
	                    currentElementBuffer.elements[currentElement] = new TBXMLElement();
	                  }
	                  else
	                  { currentElementBuffer.elements[currentElement] = new TBXMLElement();
	                  }
		
	               return currentElementBuffer.elements[currentElement];
	             }
	
	     private TBXMLAttribute nextAvailableAttribute()
	             { currentAttribute++;
		
	               if (currentAttributeBuffer == null) 
	                  { currentAttributeBuffer = new TBXMLAttributeBuffer();
	                    currentAttributeBuffer.attributes = new TBXMLAttribute[MAX_ATTRIBUTES];
	                    currentAttribute = 0;
	                    currentAttributeBuffer.attributes[currentAttribute] = new TBXMLAttribute();
	                  }
	                  else if (currentAttribute >= MAX_ATTRIBUTES) 
	                  { currentAttributeBuffer.next = new TBXMLAttributeBuffer();
	                    currentAttributeBuffer.next.previous = currentAttributeBuffer;
	                    currentAttributeBuffer = currentAttributeBuffer.next;
	                    currentAttributeBuffer.attributes = new TBXMLAttribute[MAX_ATTRIBUTES];
	                    currentAttribute = 0;
	                    currentAttributeBuffer.attributes[currentAttribute] = new TBXMLAttribute();
	                  }
	                  else
	                  { currentAttributeBuffer.attributes[currentAttribute] = new TBXMLAttribute();
	                  }
		
	               return currentAttributeBuffer.attributes[currentAttribute];
	             }

	     //TODO: \0 values are written at a bad place, spaces often appear after the texts. 
	     private void decodeBytes() throws TBXMLException 
	             { int          elementStart     = 0;
	               TBXMLElement parentXMLElement = null;
		
	               while ((elementStart = strstr(elementStart,"<")) >= 0) 
	                     { // ... comment ?
			
	                       if (strncmp(elementStart,"<!--",4) == 0) 
	                          { elementStart = strstr_s(elementStart,"-->") + 3;
	                            continue;
	                          }

	                       // .... CDATA ?
			
	                       int isCDATA = strncmp(elementStart,"<![CDATA[",9);
			
	                       if (isCDATA==0) 
	                          { int CDATAEnd = strstr_s(elementStart,"]]>");
	                            int elementEnd = CDATAEnd;
				
	                            elementEnd = strstr_s(elementEnd,"<");

	                            while (strncmp(elementEnd,"<![CDATA[",9) == 0) 
	                                  { elementEnd = strstr_s(elementEnd,"]]>");
	                                    elementEnd = strstr_s(elementEnd,"<");
	                                  }
				
	                            int CDATALength = CDATAEnd-elementStart;
	                            int textLength = elementEnd-elementStart;
				
	                            memcpy(elementStart, elementStart+9, CDATAEnd-elementStart-9);
	                            memcpy(CDATAEnd-9, CDATAEnd+3, textLength-CDATALength-3);
	                            memset(elementStart+textLength-12,' ',12);
				
	                            elementStart = CDATAEnd-9;
	                            continue;
	                          }
			
	                       // find element end, skipping any cdata sections within attributes
			
	                       int elementEnd = elementStart+1;		
	                       while ((elementEnd = strpbrk_s(elementEnd, "<>")) >= 0) 
	                             { if (strncmp(elementEnd,"<![CDATA[",9) == 0) 
	                                  { elementEnd = strstr_s(elementEnd,"]]>")+3;
	                                  }
	                                  else 
	                                  { break;
	                                  }
	                             }
			
	                       // null terminate element end
	                       if (elementEnd >= 0) 
	                          bytes[elementEnd] = 0;
			
	                       // null terminate element start so previous element text doesn't overrun
	                       bytes[elementStart] = 0;
			
	                       // get element name start
	                       int elementNameStart = elementStart+1;
			
	                       // ignore tags that start with ? or ! unless cdata "<![CDATA"
	                       if (bytes[elementNameStart] == '?' || (bytes[elementNameStart] == '!' && isCDATA != 0)) 
	                          { elementStart = elementEnd+1;
	                            continue;
	                          }
			
	                       // ignore attributes/text if this is a closing element
	                       if (bytes[elementNameStart] == '/') 
	                          { elementStart = elementEnd+1;
	                            if (parentXMLElement != null) 
	                               { if (parentXMLElement.text >= 0) 
	                                    { // trim whitespace from start of text
	                                   
	                                      while (isspace(bytes[parentXMLElement.text])) 
	                                            parentXMLElement.text++;
						
	                                      // trim whitespace from end of text
	                                      
	                                      int end = parentXMLElement.text + strlen(parentXMLElement.text)-1;
	                                      
	                                      while (end > parentXMLElement.text && isspace(bytes[end])) 
	                                            bytes[end--] = 0;
	                                    }
					
	                               parentXMLElement = parentXMLElement.parentElement;
					
					// if parent element has children clear text
					if (parentXMLElement != null && parentXMLElement.firstChild != null)
						parentXMLElement.text = -1;
					
				}
				continue;
			}
			
			
			// is this element opening and closing
			boolean selfClosingElement = false;
			if (bytes[elementEnd-1] == '/') {
				selfClosingElement = true;
			}
			
			
			// create new xmlElement struct
			TBXMLElement xmlElement = nextAvailableElement();
			
			// set element name
			xmlElement.name = elementNameStart;
			
			// if there is a parent element
			if (parentXMLElement != null) {
				
				// if this is first child of parent element
				if (parentXMLElement.currentChild != null) {
					// set next child element in list
					parentXMLElement.currentChild.nextSibling = xmlElement;
					xmlElement.previousSibling = parentXMLElement.currentChild;
					
					parentXMLElement.currentChild = xmlElement;
					
					
				} else {
					// set first child element
					parentXMLElement.currentChild = xmlElement;
					parentXMLElement.firstChild = xmlElement;
				}
				
				xmlElement.parentElement = parentXMLElement;
			}
			
			
			// in the following xml the ">" is replaced with \0 by elementEnd. 
			// element may contain no attributes and would return nil while looking for element name end
			// <tile> 
			// find end of element name
			int elementNameEnd = strpbrk(xmlElement.name," /");
			
			
			// if end was found check for attributes
			if (elementNameEnd >= 0) {
				
				// null terminate end of elemenet name
				bytes[elementNameEnd] = 0;
				
				int chr = elementNameEnd;
				int name = -1;
				int value = -1;
				int CDATAStart = -1;
				int CDATAEnd = -1;
				TBXMLAttribute lastXMLAttribute = null;
				TBXMLAttribute xmlAttribute = null;
				boolean singleQuote = false;
				
				int mode = TBXML_ATTRIBUTE_NAME_START;
				
				// loop through all characters within element
				while (chr++ < elementEnd) {
					
					switch (mode) {
						// look for start of attribute name
						case TBXML_ATTRIBUTE_NAME_START:
							if (isspace(bytes[chr])) continue;
							name = chr;
							mode = TBXML_ATTRIBUTE_NAME_END;
							break;
						// look for end of attribute name
						case TBXML_ATTRIBUTE_NAME_END:
							if (isspace(bytes[chr]) || bytes[chr] == '=') {
								bytes[chr] = 0;
								mode = TBXML_ATTRIBUTE_VALUE_START;
							}
							break;
						// look for start of attribute value
						case TBXML_ATTRIBUTE_VALUE_START:
							if (isspace(bytes[chr])) continue;
							if (bytes[chr] == '"' || bytes[chr] == '\'') {
								value = chr+1;
								mode = TBXML_ATTRIBUTE_VALUE_END;
								if (bytes[chr] == '\'') 
									singleQuote = true;
								else
									singleQuote = false;
							}
							break;
						// look for end of attribute value
						case TBXML_ATTRIBUTE_VALUE_END:
							if (bytes[chr] == '<' && strncmp(chr, "<![CDATA[", 9) == 0) {
								mode = TBXML_ATTRIBUTE_CDATA_END;
							}else if ((bytes[chr] == '"' && singleQuote == false) || (bytes[chr] == '\'' && singleQuote == true)) {
								bytes[chr] = 0;
								
								// remove cdata section tags
								while ((CDATAStart = strstr(value, "<![CDATA[")) >= 0) {
									
									// remove begin cdata tag
									memcpy(CDATAStart, CDATAStart+9, strlen(CDATAStart)-8);
									
									// search for end cdata
									CDATAEnd = strstr_s(CDATAStart,"]]>");
									
									// remove end cdata tag
									memcpy(CDATAEnd, CDATAEnd+3, strlen(CDATAEnd)-2);
								}
								
								
								// create new attribute
								xmlAttribute = nextAvailableAttribute();
								
								// if this is the first attribute found, set pointer to this attribute on element
								if (xmlElement.firstAttribute == null) xmlElement.firstAttribute = xmlAttribute;
								// if previous attribute found, link this attribute to previous one
								if (lastXMLAttribute != null) lastXMLAttribute.next = xmlAttribute;
								// set last attribute to this attribute
								lastXMLAttribute = xmlAttribute;

								// set attribute name & value
								xmlAttribute.name = name;
								xmlAttribute.value = value;

								// clear name and value pointers
								name = -1;
								value = -1;
								
								// start looking for next attribute
								mode = TBXML_ATTRIBUTE_NAME_START;
							}
							break;
							// look for end of cdata
						case TBXML_ATTRIBUTE_CDATA_END:
							if (bytes[chr] == ']') {
								if (strncmp(chr, "]]>", 3) == 0) {
									mode = TBXML_ATTRIBUTE_VALUE_END;
								}
							}
							break;						
						default:
							break;
					}
				}
			}
			
			// if tag is not self closing, set parent to current element
			if (!selfClosingElement) {
				// set text on element to element end+1
				if (bytes[elementEnd+1] != '>')
					xmlElement.text = elementEnd+1;
				
				parentXMLElement = xmlElement;
			}
			
			// start looking for next element after end of current element
			elementStart = elementEnd+1;
		}
	}
	
	/******************************************************************/
	/* C internal functions implementation */
	/******************************************************************/
	
	private int strncmp(int index, String s, int length) {
        if (length == 0)
            return 0;
        int sIndex = 0;
	    do {
            if (bytes[index] != s.charAt(sIndex++))
            	return (bytes[index] - s.charAt(--sIndex));
            if (index == bytes.length || bytes[index++] == 0)
            	break;
	    } while (--length != 0);
	    return 0;
	}
	//WARNING: returns -1 if not found. Differs a lot from original C function
	private int strstr(int index, String s){
	    int l1 = strlen(index);
	    int l2 = s.length();

	    while (l1 >= l2) {
	        if (memcmp(index, s, l2) == 0) {
	            return index;
	        }
	        index++;
	        l1--;
	    }
	    return -1;
	}
	//WARNING: returns -1 if not found. Differs a lot from original C function
	private int strstr_s(int index, String s) throws TBXMLException{
		int result = strstr(index ,s);
		if (result < 0) throw new TBXMLException(s + " not found, but should be");
		return result;
	}
	//WARNING: returns -1 if not found. Differs a lot from original C function
	private int strpbrk(int index, String s){
        int scanp;
        char c, sc;

        while (index < bytes.length && (c = bytes[index++]) != 0) {
        	for (scanp = 0; scanp < s.length() && (sc = s.charAt(scanp++)) != 0;)
        		if (sc == c)
        			return index - 1;
        }
        return -1;
	}
	//WARNING: returns -1 if not found. Differs a lot from original C function
	private int strpbrk_s(int index, String s) throws TBXMLException{
		int result = strpbrk(index ,s);
		if (result < 0) throw new TBXMLException(s + " not found, but should be");
		return result;
	}
	private int memcmp(int iSrc, String s, int length){
        if (length != 0) {
        	int iCmp = 0;
            do {
            	if (bytes[iSrc] != s.charAt(iCmp++))
            		return (bytes[--iSrc] - s.charAt(--iCmp));
            } while (--length != 0);
        }
        return 0;
	}
	private int memcpy(int iDest, int iSrc, int length){
        while(length-- > 0)
                bytes[iDest++] = bytes[iSrc++];
        return iDest;
	}
	private void memset(int iDest, char value, int length){
		while (length-- > 0)
			bytes[iDest++] = value;
	}
	private boolean isspace(char c){
		return (c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == '\f' || c == '\b');
	}
	private int strlen(int index){
		int count = 0;
		while ((index + count) < bytes.length && bytes[index + count++] != 0);
		return count;
	}
	
	/* To get String from int (index of bytes) */
	private String getText(int iSrc){
		//TODO: don't do trim here, but Element name is including white spaces
		return new String(bytes, iSrc, strlen(iSrc)).trim();
	}

}
