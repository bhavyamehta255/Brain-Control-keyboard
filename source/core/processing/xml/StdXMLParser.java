package processing.xml;

import java.io.Reader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class StdXMLParser
{
  private StdXMLBuilder builder = null;
  private StdXMLReader reader = null;
  private XMLEntityResolver entityResolver = new XMLEntityResolver();
  private XMLValidator validator = null;
  
  public StdXMLParser() {}
  
  protected void finalize()
    throws Throwable
  {
    builder = null;
    reader = null;
    entityResolver = null;
    validator = null;
    super.finalize();
  }
  
  public void setBuilder(StdXMLBuilder paramStdXMLBuilder)
  {
    builder = paramStdXMLBuilder;
  }
  
  public StdXMLBuilder getBuilder()
  {
    return builder;
  }
  
  public void setValidator(XMLValidator paramXMLValidator)
  {
    validator = paramXMLValidator;
  }
  
  public XMLValidator getValidator()
  {
    return validator;
  }
  
  public void setResolver(XMLEntityResolver paramXMLEntityResolver)
  {
    entityResolver = paramXMLEntityResolver;
  }
  
  public XMLEntityResolver getResolver()
  {
    return entityResolver;
  }
  
  public void setReader(StdXMLReader paramStdXMLReader)
  {
    reader = paramStdXMLReader;
  }
  
  public StdXMLReader getReader()
  {
    return reader;
  }
  
  public Object parse()
    throws XMLException
  {
    try
    {
      builder.startBuilding(reader.getSystemID(), reader.getLineNr());
      scanData();
      return builder.getResult();
    }
    catch (XMLException localXMLException)
    {
      throw localXMLException;
    }
    catch (Exception localException)
    {
      throw new XMLException(localException);
    }
  }
  
  protected void scanData()
    throws Exception
  {
    while ((!reader.atEOF()) && (builder.getResult() == null))
    {
      String str = XMLUtil.read(reader, '&');
      char c = str.charAt(0);
      if (c == '&') {
        XMLUtil.processEntity(str, reader, entityResolver);
      } else {
        switch (c)
        {
        case '<': 
          scanSomeTag(false, null, new Properties());
          break;
        case '\t': 
        case '\n': 
        case '\r': 
        case ' ': 
          break;
        default: 
          XMLUtil.errorInvalidInput(reader.getSystemID(), reader.getLineNr(), "`" + c + "' (0x" + Integer.toHexString(c) + ')');
        }
      }
    }
  }
  
  protected void scanSomeTag(boolean paramBoolean, String paramString, Properties paramProperties)
    throws Exception
  {
    String str = XMLUtil.read(reader, '&');
    char c = str.charAt(0);
    if (c == '&') {
      XMLUtil.errorUnexpectedEntity(reader.getSystemID(), reader.getLineNr(), str);
    }
    switch (c)
    {
    case '?': 
      processPI();
      break;
    case '!': 
      processSpecialTag(paramBoolean);
      break;
    default: 
      reader.unread(c);
      processElement(paramString, paramProperties);
    }
  }
  
  protected void processPI()
    throws Exception
  {
    XMLUtil.skipWhitespace(reader, null);
    String str = XMLUtil.scanIdentifier(reader);
    XMLUtil.skipWhitespace(reader, null);
    PIReader localPIReader = new PIReader(reader);
    if (!str.equalsIgnoreCase("xml")) {
      builder.newProcessingInstruction(str, localPIReader);
    }
    localPIReader.close();
  }
  
  protected void processSpecialTag(boolean paramBoolean)
    throws Exception
  {
    String str = XMLUtil.read(reader, '&');
    int i = str.charAt(0);
    if (i == 38) {
      XMLUtil.errorUnexpectedEntity(reader.getSystemID(), reader.getLineNr(), str);
    }
    switch (i)
    {
    case 91: 
      if (paramBoolean) {
        processCDATA();
      } else {
        XMLUtil.errorUnexpectedCDATA(reader.getSystemID(), reader.getLineNr());
      }
      return;
    case 68: 
      processDocType();
      return;
    case 45: 
      XMLUtil.skipComment(reader);
      return;
    }
  }
  
  protected void processCDATA()
    throws Exception
  {
    if (!XMLUtil.checkLiteral(reader, "CDATA[")) {
      XMLUtil.errorExpectedInput(reader.getSystemID(), reader.getLineNr(), "<![[CDATA[");
    }
    validator.PCDataAdded(reader.getSystemID(), reader.getLineNr());
    CDATAReader localCDATAReader = new CDATAReader(reader);
    builder.addPCData(localCDATAReader, reader.getSystemID(), reader.getLineNr());
    localCDATAReader.close();
  }
  
  protected void processDocType()
    throws Exception
  {
    if (!XMLUtil.checkLiteral(reader, "OCTYPE"))
    {
      XMLUtil.errorExpectedInput(reader.getSystemID(), reader.getLineNr(), "<!DOCTYPE");
      return;
    }
    XMLUtil.skipWhitespace(reader, null);
    String str = null;
    StringBuffer localStringBuffer = new StringBuffer();
    XMLUtil.scanIdentifier(reader);
    XMLUtil.skipWhitespace(reader, null);
    int i = reader.read();
    if (i == 80)
    {
      str = XMLUtil.scanPublicID(localStringBuffer, reader);
      XMLUtil.skipWhitespace(reader, null);
      i = reader.read();
    }
    else if (i == 83)
    {
      str = XMLUtil.scanSystemID(reader);
      XMLUtil.skipWhitespace(reader, null);
      i = reader.read();
    }
    if (i == 91)
    {
      validator.parseDTD(localStringBuffer.toString(), reader, entityResolver, false);
      XMLUtil.skipWhitespace(reader, null);
      i = reader.read();
    }
    if (i != 62) {
      XMLUtil.errorExpectedInput(reader.getSystemID(), reader.getLineNr(), "`>'");
    }
  }
  
  protected void processElement(String paramString, Properties paramProperties)
    throws Exception
  {
    String str1 = XMLUtil.scanIdentifier(reader);
    String str2 = str1;
    XMLUtil.skipWhitespace(reader, null);
    String str3 = null;
    int i = str2.indexOf(':');
    if (i > 0)
    {
      str3 = str2.substring(0, i);
      str2 = str2.substring(i + 1);
    }
    Vector localVector1 = new Vector();
    Vector localVector2 = new Vector();
    Vector localVector3 = new Vector();
    validator.elementStarted(str1, reader.getSystemID(), reader.getLineNr());
    char c;
    for (;;)
    {
      c = reader.read();
      if ((c == '/') || (c == '>')) {
        break;
      }
      reader.unread(c);
      processAttribute(localVector1, localVector2, localVector3);
      XMLUtil.skipWhitespace(reader, null);
    }
    Properties localProperties = new Properties();
    validator.elementAttributesProcessed(str1, localProperties, reader.getSystemID(), reader.getLineNr());
    Enumeration localEnumeration = localProperties.keys();
    String str5;
    while (localEnumeration.hasMoreElements())
    {
      String str4 = (String)localEnumeration.nextElement();
      str5 = localProperties.getProperty(str4);
      localVector1.addElement(str4);
      localVector2.addElement(str5);
      localVector3.addElement("CDATA");
    }
    if (str3 == null) {
      builder.startElement(str2, str3, paramString, reader.getSystemID(), reader.getLineNr());
    } else {
      builder.startElement(str2, str3, paramProperties.getProperty(str3), reader.getSystemID(), reader.getLineNr());
    }
    Object localObject;
    for (int j = 0; j < localVector1.size(); j++)
    {
      str5 = (String)localVector1.elementAt(j);
      localObject = (String)localVector2.elementAt(j);
      String str6 = (String)localVector3.elementAt(j);
      i = str5.indexOf(':');
      if (i > 0)
      {
        String str7 = str5.substring(0, i);
        str5 = str5.substring(i + 1);
        builder.addAttribute(str5, str7, paramProperties.getProperty(str7), (String)localObject, str6);
      }
      else
      {
        builder.addAttribute(str5, null, null, (String)localObject, str6);
      }
    }
    if (str3 == null) {
      builder.elementAttributesProcessed(str2, str3, paramString);
    } else {
      builder.elementAttributesProcessed(str2, str3, paramProperties.getProperty(str3));
    }
    if (c == '/')
    {
      if (reader.read() != '>') {
        XMLUtil.errorExpectedInput(reader.getSystemID(), reader.getLineNr(), "`>'");
      }
      validator.elementEnded(str2, reader.getSystemID(), reader.getLineNr());
      if (str3 == null) {
        builder.endElement(str2, str3, paramString);
      } else {
        builder.endElement(str2, str3, paramProperties.getProperty(str3));
      }
      return;
    }
    StringBuffer localStringBuffer = new StringBuffer(16);
    for (;;)
    {
      localStringBuffer.setLength(0);
      for (;;)
      {
        XMLUtil.skipWhitespace(reader, localStringBuffer);
        str5 = XMLUtil.read(reader, '&');
        if ((str5.charAt(0) != '&') || (str5.charAt(1) == '#')) {
          break;
        }
        XMLUtil.processEntity(str5, reader, entityResolver);
      }
      if (str5.charAt(0) == '<')
      {
        str5 = XMLUtil.read(reader, '\000');
        if (str5.charAt(0) == '/')
        {
          XMLUtil.skipWhitespace(reader, null);
          str5 = XMLUtil.scanIdentifier(reader);
          if (!str5.equals(str1)) {
            XMLUtil.errorWrongClosingTag(reader.getSystemID(), reader.getLineNr(), str2, str5);
          }
          XMLUtil.skipWhitespace(reader, null);
          if (reader.read() != '>') {
            XMLUtil.errorClosingTagNotEmpty(reader.getSystemID(), reader.getLineNr());
          }
          validator.elementEnded(str1, reader.getSystemID(), reader.getLineNr());
          if (str3 == null)
          {
            builder.endElement(str2, str3, paramString);
            break;
          }
          builder.endElement(str2, str3, paramProperties.getProperty(str3));
          break;
        }
        reader.unread(str5.charAt(0));
        scanSomeTag(true, paramString, (Properties)paramProperties.clone());
      }
      else
      {
        if (str5.charAt(0) == '&')
        {
          c = XMLUtil.processCharLiteral(str5);
          localStringBuffer.append(c);
        }
        else
        {
          reader.unread(str5.charAt(0));
        }
        validator.PCDataAdded(reader.getSystemID(), reader.getLineNr());
        localObject = new ContentReader(reader, entityResolver, localStringBuffer.toString());
        builder.addPCData((Reader)localObject, reader.getSystemID(), reader.getLineNr());
        ((Reader)localObject).close();
      }
    }
  }
  
  protected void processAttribute(Vector<String> paramVector1, Vector<String> paramVector2, Vector<String> paramVector3)
    throws Exception
  {
    String str1 = XMLUtil.scanIdentifier(reader);
    XMLUtil.skipWhitespace(reader, null);
    if (!XMLUtil.read(reader, '&').equals("=")) {
      XMLUtil.errorExpectedInput(reader.getSystemID(), reader.getLineNr(), "`='");
    }
    XMLUtil.skipWhitespace(reader, null);
    String str2 = XMLUtil.scanString(reader, '&', entityResolver);
    paramVector1.addElement(str1);
    paramVector2.addElement(str2);
    paramVector3.addElement("CDATA");
    validator.attributeAdded(str1, str2, reader.getSystemID(), reader.getLineNr());
  }
}
