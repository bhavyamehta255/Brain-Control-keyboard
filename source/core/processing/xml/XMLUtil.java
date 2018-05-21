package processing.xml;

import java.io.IOException;
import java.io.Reader;

class XMLUtil
{
  XMLUtil() {}
  
  static void skipComment(StdXMLReader paramStdXMLReader)
    throws IOException, XMLParseException
  {
    if (paramStdXMLReader.read() != '-') {
      errorExpectedInput(paramStdXMLReader.getSystemID(), paramStdXMLReader.getLineNr(), "<!--");
    }
    int i = 0;
    for (;;)
    {
      int j = paramStdXMLReader.read();
      switch (j)
      {
      case 45: 
        i++;
        break;
      case 62: 
        if (i == 2) {
          return;
        }
        i = 0;
        break;
      default: 
        i = 0;
      }
    }
  }
  
  static void skipTag(StdXMLReader paramStdXMLReader)
    throws IOException, XMLParseException
  {
    int i = 1;
    while (i > 0)
    {
      int j = paramStdXMLReader.read();
      switch (j)
      {
      case 60: 
        i++;
        break;
      case 62: 
        i--;
      }
    }
  }
  
  static String scanPublicID(StringBuffer paramStringBuffer, StdXMLReader paramStdXMLReader)
    throws IOException, XMLParseException
  {
    if (!checkLiteral(paramStdXMLReader, "UBLIC")) {
      return null;
    }
    skipWhitespace(paramStdXMLReader, null);
    paramStringBuffer.append(scanString(paramStdXMLReader, '\000', null));
    skipWhitespace(paramStdXMLReader, null);
    return scanString(paramStdXMLReader, '\000', null);
  }
  
  static String scanSystemID(StdXMLReader paramStdXMLReader)
    throws IOException, XMLParseException
  {
    if (!checkLiteral(paramStdXMLReader, "YSTEM")) {
      return null;
    }
    skipWhitespace(paramStdXMLReader, null);
    return scanString(paramStdXMLReader, '\000', null);
  }
  
  static String scanIdentifier(StdXMLReader paramStdXMLReader)
    throws IOException, XMLParseException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (;;)
    {
      char c = paramStdXMLReader.read();
      if ((c == '_') || (c == ':') || (c == '-') || (c == '.') || ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) || ((c >= '0') && (c <= '9')) || (c > '~'))
      {
        localStringBuffer.append(c);
      }
      else
      {
        paramStdXMLReader.unread(c);
        break;
      }
    }
    return localStringBuffer.toString();
  }
  
  static String scanString(StdXMLReader paramStdXMLReader, char paramChar, XMLEntityResolver paramXMLEntityResolver)
    throws IOException, XMLParseException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = paramStdXMLReader.getStreamLevel();
    char c1 = paramStdXMLReader.read();
    if ((c1 != '\'') && (c1 != '"')) {
      errorExpectedInput(paramStdXMLReader.getSystemID(), paramStdXMLReader.getLineNr(), "delimited string");
    }
    for (;;)
    {
      String str = read(paramStdXMLReader, paramChar);
      char c2 = str.charAt(0);
      if (c2 == paramChar)
      {
        if (str.charAt(1) == '#') {
          localStringBuffer.append(processCharLiteral(str));
        } else {
          processEntity(str, paramStdXMLReader, paramXMLEntityResolver);
        }
      }
      else if (c2 == '&')
      {
        paramStdXMLReader.unread(c2);
        str = read(paramStdXMLReader, '&');
        if (str.charAt(1) == '#') {
          localStringBuffer.append(processCharLiteral(str));
        } else {
          localStringBuffer.append(str);
        }
      }
      else if (paramStdXMLReader.getStreamLevel() == i)
      {
        if (c2 == c1) {
          break;
        }
        if ((c2 == '\t') || (c2 == '\n') || (c2 == '\r')) {
          localStringBuffer.append(' ');
        } else {
          localStringBuffer.append(c2);
        }
      }
      else
      {
        localStringBuffer.append(c2);
      }
    }
    return localStringBuffer.toString();
  }
  
  static void processEntity(String paramString, StdXMLReader paramStdXMLReader, XMLEntityResolver paramXMLEntityResolver)
    throws IOException, XMLParseException
  {
    paramString = paramString.substring(1, paramString.length() - 1);
    Reader localReader = paramXMLEntityResolver.getEntity(paramStdXMLReader, paramString);
    if (localReader == null) {
      errorInvalidEntity(paramStdXMLReader.getSystemID(), paramStdXMLReader.getLineNr(), paramString);
    }
    boolean bool = paramXMLEntityResolver.isExternalEntity(paramString);
    paramStdXMLReader.startNewStream(localReader, !bool);
  }
  
  static char processCharLiteral(String paramString)
    throws IOException, XMLParseException
  {
    if (paramString.charAt(2) == 'x')
    {
      paramString = paramString.substring(3, paramString.length() - 1);
      return (char)Integer.parseInt(paramString, 16);
    }
    paramString = paramString.substring(2, paramString.length() - 1);
    return (char)Integer.parseInt(paramString, 10);
  }
  
  static void skipWhitespace(StdXMLReader paramStdXMLReader, StringBuffer paramStringBuffer)
    throws IOException
  {
    char c;
    if (paramStringBuffer == null) {
      do
      {
        c = paramStdXMLReader.read();
      } while ((c == ' ') || (c == '\t') || (c == '\n'));
    } else {
      for (;;)
      {
        c = paramStdXMLReader.read();
        if ((c != ' ') && (c != '\t') && (c != '\n')) {
          break;
        }
        if (c == '\n') {
          paramStringBuffer.append('\n');
        } else {
          paramStringBuffer.append(' ');
        }
      }
    }
    paramStdXMLReader.unread(c);
  }
  
  static String read(StdXMLReader paramStdXMLReader, char paramChar)
    throws IOException, XMLParseException
  {
    char c = paramStdXMLReader.read();
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(c);
    if (c == paramChar) {
      while (c != ';')
      {
        c = paramStdXMLReader.read();
        localStringBuffer.append(c);
      }
    }
    return localStringBuffer.toString();
  }
  
  static char readChar(StdXMLReader paramStdXMLReader, char paramChar)
    throws IOException, XMLParseException
  {
    String str = read(paramStdXMLReader, paramChar);
    char c = str.charAt(0);
    if (c == paramChar) {
      errorUnexpectedEntity(paramStdXMLReader.getSystemID(), paramStdXMLReader.getLineNr(), str);
    }
    return c;
  }
  
  static boolean checkLiteral(StdXMLReader paramStdXMLReader, String paramString)
    throws IOException, XMLParseException
  {
    for (int i = 0; i < paramString.length(); i++) {
      if (paramStdXMLReader.read() != paramString.charAt(i)) {
        return false;
      }
    }
    return true;
  }
  
  static void errorExpectedInput(String paramString1, int paramInt, String paramString2)
    throws XMLParseException
  {
    throw new XMLParseException(paramString1, paramInt, "Expected: " + paramString2);
  }
  
  static void errorInvalidEntity(String paramString1, int paramInt, String paramString2)
    throws XMLParseException
  {
    throw new XMLParseException(paramString1, paramInt, "Invalid entity: `&" + paramString2 + ";'");
  }
  
  static void errorUnexpectedEntity(String paramString1, int paramInt, String paramString2)
    throws XMLParseException
  {
    throw new XMLParseException(paramString1, paramInt, "No entity reference is expected here (" + paramString2 + ")");
  }
  
  static void errorUnexpectedCDATA(String paramString, int paramInt)
    throws XMLParseException
  {
    throw new XMLParseException(paramString, paramInt, "No CDATA section is expected here");
  }
  
  static void errorInvalidInput(String paramString1, int paramInt, String paramString2)
    throws XMLParseException
  {
    throw new XMLParseException(paramString1, paramInt, "Invalid input: " + paramString2);
  }
  
  static void errorWrongClosingTag(String paramString1, int paramInt, String paramString2, String paramString3)
    throws XMLParseException
  {
    throw new XMLParseException(paramString1, paramInt, "Closing tag does not match opening tag: `" + paramString3 + "' != `" + paramString2 + "'");
  }
  
  static void errorClosingTagNotEmpty(String paramString, int paramInt)
    throws XMLParseException
  {
    throw new XMLParseException(paramString, paramInt, "Closing tag must be empty");
  }
  
  static void errorMissingElement(String paramString1, int paramInt, String paramString2, String paramString3)
    throws XMLValidationException
  {
    throw new XMLValidationException(1, paramString1, paramInt, paramString3, null, null, "Element " + paramString2 + " expects to have a " + paramString3);
  }
  
  static void errorUnexpectedElement(String paramString1, int paramInt, String paramString2, String paramString3)
    throws XMLValidationException
  {
    throw new XMLValidationException(2, paramString1, paramInt, paramString3, null, null, "Unexpected " + paramString3 + " in a " + paramString2);
  }
  
  static void errorMissingAttribute(String paramString1, int paramInt, String paramString2, String paramString3)
    throws XMLValidationException
  {
    throw new XMLValidationException(3, paramString1, paramInt, paramString2, paramString3, null, "Element " + paramString2 + " expects an attribute named " + paramString3);
  }
  
  static void errorUnexpectedAttribute(String paramString1, int paramInt, String paramString2, String paramString3)
    throws XMLValidationException
  {
    throw new XMLValidationException(4, paramString1, paramInt, paramString2, paramString3, null, "Element " + paramString2 + " did not expect an attribute " + "named " + paramString3);
  }
  
  static void errorInvalidAttributeValue(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4)
    throws XMLValidationException
  {
    throw new XMLValidationException(5, paramString1, paramInt, paramString2, paramString3, paramString4, "Invalid value for attribute " + paramString3);
  }
  
  static void errorMissingPCData(String paramString1, int paramInt, String paramString2)
    throws XMLValidationException
  {
    throw new XMLValidationException(6, paramString1, paramInt, null, null, null, "Missing #PCDATA in element " + paramString2);
  }
  
  static void errorUnexpectedPCData(String paramString1, int paramInt, String paramString2)
    throws XMLValidationException
  {
    throw new XMLValidationException(7, paramString1, paramInt, null, null, null, "Unexpected #PCDATA in element " + paramString2);
  }
  
  static void validationError(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4, String paramString5)
    throws XMLValidationException
  {
    throw new XMLValidationException(0, paramString1, paramInt, paramString3, paramString4, paramString5, paramString2);
  }
}
