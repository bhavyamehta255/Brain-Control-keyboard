package processing.xml;

import java.io.Reader;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Stack;

public class XMLValidator
{
  protected XMLEntityResolver parameterEntityResolver = new XMLEntityResolver();
  protected Hashtable<String, Properties> attributeDefaultValues = new Hashtable();
  protected Stack<Properties> currentElements = new Stack();
  
  public XMLValidator() {}
  
  protected void finalize()
    throws Throwable
  {
    parameterEntityResolver = null;
    attributeDefaultValues.clear();
    attributeDefaultValues = null;
    currentElements.clear();
    currentElements = null;
    super.finalize();
  }
  
  public void setParameterEntityResolver(XMLEntityResolver paramXMLEntityResolver)
  {
    parameterEntityResolver = paramXMLEntityResolver;
  }
  
  public XMLEntityResolver getParameterEntityResolver()
  {
    return parameterEntityResolver;
  }
  
  public void parseDTD(String paramString, StdXMLReader paramStdXMLReader, XMLEntityResolver paramXMLEntityResolver, boolean paramBoolean)
    throws Exception
  {
    XMLUtil.skipWhitespace(paramStdXMLReader, null);
    int i = paramStdXMLReader.getStreamLevel();
    for (;;)
    {
      String str = XMLUtil.read(paramStdXMLReader, '%');
      char c = str.charAt(0);
      if (c == '%')
      {
        XMLUtil.processEntity(str, paramStdXMLReader, parameterEntityResolver);
      }
      else
      {
        if (c == '<')
        {
          processElement(paramStdXMLReader, paramXMLEntityResolver);
        }
        else
        {
          if (c == ']') {
            return;
          }
          XMLUtil.errorInvalidInput(paramStdXMLReader.getSystemID(), paramStdXMLReader.getLineNr(), str);
        }
        do
        {
          c = paramStdXMLReader.read();
          if ((paramBoolean) && (paramStdXMLReader.getStreamLevel() < i))
          {
            paramStdXMLReader.unread(c);
            return;
          }
        } while ((c == ' ') || (c == '\t') || (c == '\n') || (c == '\r'));
        paramStdXMLReader.unread(c);
      }
    }
  }
  
  protected void processElement(StdXMLReader paramStdXMLReader, XMLEntityResolver paramXMLEntityResolver)
    throws Exception
  {
    String str = XMLUtil.read(paramStdXMLReader, '%');
    int i = str.charAt(0);
    if (i != 33)
    {
      XMLUtil.skipTag(paramStdXMLReader);
      return;
    }
    str = XMLUtil.read(paramStdXMLReader, '%');
    i = str.charAt(0);
    switch (i)
    {
    case 45: 
      XMLUtil.skipComment(paramStdXMLReader);
      break;
    case 91: 
      processConditionalSection(paramStdXMLReader, paramXMLEntityResolver);
      break;
    case 69: 
      processEntity(paramStdXMLReader, paramXMLEntityResolver);
      break;
    case 65: 
      processAttList(paramStdXMLReader, paramXMLEntityResolver);
      break;
    default: 
      XMLUtil.skipTag(paramStdXMLReader);
    }
  }
  
  protected void processConditionalSection(StdXMLReader paramStdXMLReader, XMLEntityResolver paramXMLEntityResolver)
    throws Exception
  {
    XMLUtil.skipWhitespace(paramStdXMLReader, null);
    String str = XMLUtil.read(paramStdXMLReader, '%');
    int i = str.charAt(0);
    if (i != 73)
    {
      XMLUtil.skipTag(paramStdXMLReader);
      return;
    }
    str = XMLUtil.read(paramStdXMLReader, '%');
    i = str.charAt(0);
    switch (i)
    {
    case 71: 
      processIgnoreSection(paramStdXMLReader, paramXMLEntityResolver);
      return;
    case 78: 
      break;
    default: 
      XMLUtil.skipTag(paramStdXMLReader);
      return;
    }
    if (!XMLUtil.checkLiteral(paramStdXMLReader, "CLUDE"))
    {
      XMLUtil.skipTag(paramStdXMLReader);
      return;
    }
    XMLUtil.skipWhitespace(paramStdXMLReader, null);
    str = XMLUtil.read(paramStdXMLReader, '%');
    i = str.charAt(0);
    if (i != 91)
    {
      XMLUtil.skipTag(paramStdXMLReader);
      return;
    }
    CDATAReader localCDATAReader = new CDATAReader(paramStdXMLReader);
    StringBuffer localStringBuffer = new StringBuffer(1024);
    for (;;)
    {
      int j = localCDATAReader.read();
      if (j < 0) {
        break;
      }
      localStringBuffer.append((char)j);
    }
    localCDATAReader.close();
    paramStdXMLReader.startNewStream(new StringReader(localStringBuffer.toString()));
  }
  
  protected void processIgnoreSection(StdXMLReader paramStdXMLReader, XMLEntityResolver paramXMLEntityResolver)
    throws Exception
  {
    if (!XMLUtil.checkLiteral(paramStdXMLReader, "NORE"))
    {
      XMLUtil.skipTag(paramStdXMLReader);
      return;
    }
    XMLUtil.skipWhitespace(paramStdXMLReader, null);
    String str = XMLUtil.read(paramStdXMLReader, '%');
    int i = str.charAt(0);
    if (i != 91)
    {
      XMLUtil.skipTag(paramStdXMLReader);
      return;
    }
    CDATAReader localCDATAReader = new CDATAReader(paramStdXMLReader);
    localCDATAReader.close();
  }
  
  protected void processAttList(StdXMLReader paramStdXMLReader, XMLEntityResolver paramXMLEntityResolver)
    throws Exception
  {
    if (!XMLUtil.checkLiteral(paramStdXMLReader, "TTLIST"))
    {
      XMLUtil.skipTag(paramStdXMLReader);
      return;
    }
    XMLUtil.skipWhitespace(paramStdXMLReader, null);
    String str1 = XMLUtil.read(paramStdXMLReader, '%');
    for (char c = str1.charAt(0); c == '%'; c = str1.charAt(0))
    {
      XMLUtil.processEntity(str1, paramStdXMLReader, parameterEntityResolver);
      str1 = XMLUtil.read(paramStdXMLReader, '%');
    }
    paramStdXMLReader.unread(c);
    String str2 = XMLUtil.scanIdentifier(paramStdXMLReader);
    XMLUtil.skipWhitespace(paramStdXMLReader, null);
    str1 = XMLUtil.read(paramStdXMLReader, '%');
    for (c = str1.charAt(0); c == '%'; c = str1.charAt(0))
    {
      XMLUtil.processEntity(str1, paramStdXMLReader, parameterEntityResolver);
      str1 = XMLUtil.read(paramStdXMLReader, '%');
    }
    Properties localProperties = new Properties();
    while (c != '>')
    {
      paramStdXMLReader.unread(c);
      String str3 = XMLUtil.scanIdentifier(paramStdXMLReader);
      XMLUtil.skipWhitespace(paramStdXMLReader, null);
      str1 = XMLUtil.read(paramStdXMLReader, '%');
      for (c = str1.charAt(0); c == '%'; c = str1.charAt(0))
      {
        XMLUtil.processEntity(str1, paramStdXMLReader, parameterEntityResolver);
        str1 = XMLUtil.read(paramStdXMLReader, '%');
      }
      if (c == '(') {
        while (c != ')')
        {
          str1 = XMLUtil.read(paramStdXMLReader, '%');
          for (c = str1.charAt(0); c == '%'; c = str1.charAt(0))
          {
            XMLUtil.processEntity(str1, paramStdXMLReader, parameterEntityResolver);
            str1 = XMLUtil.read(paramStdXMLReader, '%');
          }
        }
      }
      paramStdXMLReader.unread(c);
      XMLUtil.scanIdentifier(paramStdXMLReader);
      XMLUtil.skipWhitespace(paramStdXMLReader, null);
      str1 = XMLUtil.read(paramStdXMLReader, '%');
      for (c = str1.charAt(0); c == '%'; c = str1.charAt(0))
      {
        XMLUtil.processEntity(str1, paramStdXMLReader, parameterEntityResolver);
        str1 = XMLUtil.read(paramStdXMLReader, '%');
      }
      if (c == '#')
      {
        str1 = XMLUtil.scanIdentifier(paramStdXMLReader);
        XMLUtil.skipWhitespace(paramStdXMLReader, null);
        if (!str1.equals("FIXED"))
        {
          XMLUtil.skipWhitespace(paramStdXMLReader, null);
          str1 = XMLUtil.read(paramStdXMLReader, '%');
          for (c = str1.charAt(0); c == '%'; c = str1.charAt(0))
          {
            XMLUtil.processEntity(str1, paramStdXMLReader, parameterEntityResolver);
            str1 = XMLUtil.read(paramStdXMLReader, '%');
          }
        }
      }
      else
      {
        paramStdXMLReader.unread(c);
      }
      String str4 = XMLUtil.scanString(paramStdXMLReader, '%', parameterEntityResolver);
      localProperties.put(str3, str4);
      XMLUtil.skipWhitespace(paramStdXMLReader, null);
      str1 = XMLUtil.read(paramStdXMLReader, '%');
      for (c = str1.charAt(0); c == '%'; c = str1.charAt(0))
      {
        XMLUtil.processEntity(str1, paramStdXMLReader, parameterEntityResolver);
        str1 = XMLUtil.read(paramStdXMLReader, '%');
      }
    }
    if (!localProperties.isEmpty()) {
      attributeDefaultValues.put(str2, localProperties);
    }
  }
  
  protected void processEntity(StdXMLReader paramStdXMLReader, XMLEntityResolver paramXMLEntityResolver)
    throws Exception
  {
    if (!XMLUtil.checkLiteral(paramStdXMLReader, "NTITY"))
    {
      XMLUtil.skipTag(paramStdXMLReader);
      return;
    }
    XMLUtil.skipWhitespace(paramStdXMLReader, null);
    char c = XMLUtil.readChar(paramStdXMLReader, '\000');
    if (c == '%')
    {
      XMLUtil.skipWhitespace(paramStdXMLReader, null);
      paramXMLEntityResolver = parameterEntityResolver;
    }
    else
    {
      paramStdXMLReader.unread(c);
    }
    String str1 = XMLUtil.scanIdentifier(paramStdXMLReader);
    XMLUtil.skipWhitespace(paramStdXMLReader, null);
    c = XMLUtil.readChar(paramStdXMLReader, '%');
    String str2 = null;
    String str3 = null;
    switch (c)
    {
    case 'P': 
      if (!XMLUtil.checkLiteral(paramStdXMLReader, "UBLIC"))
      {
        XMLUtil.skipTag(paramStdXMLReader);
        return;
      }
      XMLUtil.skipWhitespace(paramStdXMLReader, null);
      str3 = XMLUtil.scanString(paramStdXMLReader, '%', parameterEntityResolver);
      XMLUtil.skipWhitespace(paramStdXMLReader, null);
      str2 = XMLUtil.scanString(paramStdXMLReader, '%', parameterEntityResolver);
      XMLUtil.skipWhitespace(paramStdXMLReader, null);
      XMLUtil.readChar(paramStdXMLReader, '%');
      break;
    case 'S': 
      if (!XMLUtil.checkLiteral(paramStdXMLReader, "YSTEM"))
      {
        XMLUtil.skipTag(paramStdXMLReader);
        return;
      }
      XMLUtil.skipWhitespace(paramStdXMLReader, null);
      str2 = XMLUtil.scanString(paramStdXMLReader, '%', parameterEntityResolver);
      XMLUtil.skipWhitespace(paramStdXMLReader, null);
      XMLUtil.readChar(paramStdXMLReader, '%');
      break;
    case '"': 
    case '\'': 
      paramStdXMLReader.unread(c);
      String str4 = XMLUtil.scanString(paramStdXMLReader, '%', parameterEntityResolver);
      paramXMLEntityResolver.addInternalEntity(str1, str4);
      XMLUtil.skipWhitespace(paramStdXMLReader, null);
      XMLUtil.readChar(paramStdXMLReader, '%');
      break;
    default: 
      XMLUtil.skipTag(paramStdXMLReader);
    }
    if (str2 != null) {
      paramXMLEntityResolver.addExternalEntity(str1, str3, str2);
    }
  }
  
  public void elementStarted(String paramString1, String paramString2, int paramInt)
  {
    Properties localProperties = (Properties)attributeDefaultValues.get(paramString1);
    if (localProperties == null) {
      localProperties = new Properties();
    } else {
      localProperties = (Properties)localProperties.clone();
    }
    currentElements.push(localProperties);
  }
  
  public void elementEnded(String paramString1, String paramString2, int paramInt) {}
  
  public void elementAttributesProcessed(String paramString1, Properties paramProperties, String paramString2, int paramInt)
  {
    Properties localProperties = (Properties)currentElements.pop();
    Enumeration localEnumeration = localProperties.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      paramProperties.put(str, localProperties.get(str));
    }
  }
  
  public void attributeAdded(String paramString1, String paramString2, String paramString3, int paramInt)
  {
    Properties localProperties = (Properties)currentElements.peek();
    if (localProperties.containsKey(paramString1)) {
      localProperties.remove(paramString1);
    }
  }
  
  public void PCDataAdded(String paramString, int paramInt) {}
}
