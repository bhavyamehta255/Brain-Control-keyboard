package processing.xml;

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

public class StdXMLBuilder
{
  private Stack<XMLElement> stack;
  private XMLElement root;
  private XMLElement parent;
  
  public StdXMLBuilder()
  {
    this(new XMLElement());
    stack = null;
    root = null;
  }
  
  public StdXMLBuilder(XMLElement paramXMLElement)
  {
    parent = paramXMLElement;
  }
  
  protected void finalize()
    throws Throwable
  {
    root = null;
    stack.clear();
    stack = null;
    super.finalize();
  }
  
  public void startBuilding(String paramString, int paramInt)
  {
    stack = new Stack();
    root = null;
  }
  
  public void newProcessingInstruction(String paramString, Reader paramReader) {}
  
  public void startElement(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt)
  {
    String str = paramString1;
    if (paramString2 != null) {
      str = paramString2 + ':' + paramString1;
    }
    if (stack.empty())
    {
      parent.init(str, paramString3, paramString4, paramInt);
      stack.push(parent);
      root = parent;
    }
    else
    {
      XMLElement localXMLElement1 = (XMLElement)stack.peek();
      XMLElement localXMLElement2 = new XMLElement(str, paramString3, paramString4, paramInt);
      localXMLElement1.addChild(localXMLElement2);
      stack.push(localXMLElement2);
    }
  }
  
  public void elementAttributesProcessed(String paramString1, String paramString2, String paramString3) {}
  
  public void endElement(String paramString1, String paramString2, String paramString3)
  {
    XMLElement localXMLElement1 = (XMLElement)stack.pop();
    if (localXMLElement1.getChildCount() == 1)
    {
      XMLElement localXMLElement2 = localXMLElement1.getChild(0);
      if (localXMLElement2.getLocalName() == null)
      {
        localXMLElement1.setContent(localXMLElement2.getContent());
        localXMLElement1.removeChild(0);
      }
    }
  }
  
  public void addAttribute(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws Exception
  {
    String str = paramString1;
    if (paramString2 != null) {
      str = paramString2 + ':' + paramString1;
    }
    XMLElement localXMLElement = (XMLElement)stack.peek();
    if (localXMLElement.hasAttribute(str)) {
      throw new XMLParseException(localXMLElement.getSystemID(), localXMLElement.getLine(), "Duplicate attribute: " + paramString1);
    }
    localXMLElement.setString(str, paramString4);
  }
  
  public void addPCData(Reader paramReader, String paramString, int paramInt)
  {
    int i = 2048;
    int j = 0;
    StringBuffer localStringBuffer = new StringBuffer(i);
    char[] arrayOfChar = new char[i];
    for (;;)
    {
      if (j >= i)
      {
        i *= 2;
        localStringBuffer.ensureCapacity(i);
      }
      int k;
      try
      {
        k = paramReader.read(arrayOfChar);
      }
      catch (IOException localIOException)
      {
        break;
      }
      if (k < 0) {
        break;
      }
      localStringBuffer.append(arrayOfChar, 0, k);
      j += k;
    }
    XMLElement localXMLElement1 = new XMLElement(null, null, paramString, paramInt);
    localXMLElement1.setContent(localStringBuffer.toString());
    if (!stack.empty())
    {
      XMLElement localXMLElement2 = (XMLElement)stack.peek();
      localXMLElement2.addChild(localXMLElement1);
    }
  }
  
  public Object getResult()
  {
    return root;
  }
}
