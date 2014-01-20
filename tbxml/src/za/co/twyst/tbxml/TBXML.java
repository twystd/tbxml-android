package za.co.twyst.tbxml;

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
         
//         + (NSString*) attributeName:(TBXMLAttribute*)aXMLAttribute {
//             if (nil == aXMLAttribute->name) return @"";
//             return [NSString stringWithCString:&aXMLAttribute->name[0] encoding:NSUTF8StringEncoding];
//         }
         
//         + (NSString*) attributeValue:(TBXMLAttribute*)aXMLAttribute {
//             if (nil == aXMLAttribute->value) return @"";
//             return [NSString stringWithCString:&aXMLAttribute->value[0] encoding:NSUTF8StringEncoding];
//         }
         
       public String textForElement(long element) 
              { return jniTextForElement(document,element);
              }

//       + (NSString*) valueOfAttributeNamed:(NSString *)aName forElement:(TBXMLElement*)aXMLElement {
//           const char * name = [aName cStringUsingEncoding:NSUTF8StringEncoding];
//           NSString * value = nil;
//           TBXMLAttribute * attribute = aXMLElement->firstAttribute;
//           while (attribute) {
//               if (strlen(attribute->name) == strlen(name) && memcmp(attribute->name,name,strlen(name)) == 0) {
//                   value = [NSString stringWithCString:&attribute->value[0] encoding:NSUTF8StringEncoding];
//                   break;
//               }
//               attribute = attribute->next;
//           }
//           return value;
//       }

//       + (TBXMLElement*) childElementNamed:(NSString*)aName parentElement:(TBXMLElement*)aParentXMLElement{
//           
//           TBXMLElement * xmlElement = aParentXMLElement->firstChild;
//           const char * name = [aName cStringUsingEncoding:NSUTF8StringEncoding];
//           while (xmlElement) {
//               if (strlen(xmlElement->name) == strlen(name) && memcmp(xmlElement->name,name,strlen(name)) == 0) {
//                   return xmlElement;
//               }
//               xmlElement = xmlElement->nextSibling;
//           }
//           return nil;
//       }

//       + (TBXMLElement*) nextSiblingNamed:(NSString*)aName searchFromElement:(TBXMLElement*)aXMLElement{
//           TBXMLElement * xmlElement = aXMLElement->nextSibling;
//           const char * name = [aName cStringUsingEncoding:NSUTF8StringEncoding];
//           while (xmlElement) {
//               if (strlen(xmlElement->name) == strlen(name) && memcmp(xmlElement->name,name,strlen(name)) == 0) {
//                   return xmlElement;
//               }
//               xmlElement = xmlElement->nextSibling;
//           }
//           return nil;
//       }

//       + (void)iterateElementsForQuery:(NSString *)query fromElement:(TBXMLElement *)anElement withBlock:(TBXMLIterateBlock)iterateBlock {
//           
//           NSArray *components = [query componentsSeparatedByString:@"."];
//           TBXMLElement *currTBXMLElement = anElement;
//           
//           // navigate down
//           for (NSInteger i=0; i < components.count; ++i) {
//               NSString *iTagName = [components objectAtIndex:i];
//               
//               if ([iTagName isEqualToString:@"*"]) {
//                   currTBXMLElement = currTBXMLElement->firstChild;
//                   
//                   // different behavior depending on if this is the end of the query or midstream
//                   if (i < (components.count - 1)) {
//                       // midstream
//                       do {
//                           NSString *restOfQuery = [[components subarrayWithRange:NSMakeRange(i + 1, components.count - i - 1)] componentsJoinedByString:@"."];
//                           [TBXML iterateElementsForQuery:restOfQuery fromElement:currTBXMLElement withBlock:iterateBlock];
//                       } while ((currTBXMLElement = currTBXMLElement->nextSibling));
//                       
//                   }
//               } else {
//                   currTBXMLElement = [TBXML childElementNamed:iTagName parentElement:currTBXMLElement];            
//               }
//               
//               if (!currTBXMLElement) {
//                   break;
//               }
//           }
//           
//           if (currTBXMLElement) {
//               // enumerate
//               NSString *childTagName = [components lastObject];
//               
//               if ([childTagName isEqualToString:@"*"]) {
//                   childTagName = nil;
//               }
//               
//               do {
//                   iterateBlock(currTBXMLElement);
//               } while (childTagName ? (currTBXMLElement = [TBXML nextSiblingNamed:childTagName searchFromElement:currTBXMLElement]) : (currTBXMLElement = currTBXMLElement->nextSibling));
//           }
//       }

//       + (void)iterateAttributesOfElement:(TBXMLElement *)anElement withBlock:(TBXMLIterateAttributeBlock)iterateAttributeBlock {
//
//           // Obtain first attribute from element
//           TBXMLAttribute * attribute = anElement->firstAttribute;
//           
//           // if attribute is valid
//           
//           while (attribute) {
//               // Call the iterateAttributeBlock with the attribute, it's name and value
//               iterateAttributeBlock(attribute, [TBXML attributeName:attribute], [TBXML attributeValue:attribute]);
//               
//               // Obtain the next attribute
//               attribute = attribute->next;
//           }
//       }
       
       public String valueOfAttributeForElement(String attribute,long element) 
              { return jniValueOfAttributeForElement(document,element,attribute);
              }

       
         // IMPLEMENTATION
         
         //TODO: \0 values are written at a bad place, spaces often appear after the texts. 
         //private void decodeBytes() throws TBXMLException
         //        { jniDecodeBytes(this);
         //        }
       }
