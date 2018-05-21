package processing.xml;

import java.io.IOException;
import java.io.Reader;

class ContentReader
  extends Reader
{
  private StdXMLReader reader;
  private String buffer;
  private int bufferIndex;
  private XMLEntityResolver resolver;
  
  ContentReader(StdXMLReader paramStdXMLReader, XMLEntityResolver paramXMLEntityResolver, String paramString)
  {
    reader = paramStdXMLReader;
    resolver = paramXMLEntityResolver;
    buffer = paramString;
    bufferIndex = 0;
  }
  
  protected void finalize()
    throws Throwable
  {
    reader = null;
    resolver = null;
    buffer = null;
    super.finalize();
  }
  
  public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    try
    {
      int i = 0;
      int j = buffer.length();
      if (paramInt1 + paramInt2 > paramArrayOfChar.length) {
        paramInt2 = paramArrayOfChar.length - paramInt1;
      }
      while (i < paramInt2)
      {
        String str = "";
        char c;
        if (bufferIndex >= j)
        {
          str = XMLUtil.read(reader, '&');
          c = str.charAt(0);
        }
        else
        {
          c = buffer.charAt(bufferIndex);
          bufferIndex += 1;
          paramArrayOfChar[i] = c;
          i++;
          continue;
        }
        if (c == '<')
        {
          reader.unread(c);
          break;
        }
        if ((c == '&') && (str.length() > 1)) {
          if (str.charAt(1) == '#')
          {
            c = XMLUtil.processCharLiteral(str);
          }
          else
          {
            XMLUtil.processEntity(str, reader, resolver);
            continue;
          }
        }
        paramArrayOfChar[i] = c;
        i++;
      }
      if (i == 0) {
        i = -1;
      }
      return i;
    }
    catch (XMLParseException localXMLParseException)
    {
      throw new IOException(localXMLParseException.getMessage());
    }
  }
  
  public void close()
    throws IOException
  {
    try
    {
      int i = buffer.length();
      for (;;)
      {
        String str = "";
        char c;
        if (bufferIndex >= i)
        {
          str = XMLUtil.read(reader, '&');
          c = str.charAt(0);
        }
        else
        {
          c = buffer.charAt(bufferIndex);
          bufferIndex += 1;
          continue;
        }
        if (c == '<')
        {
          reader.unread(c);
          break;
        }
        if ((c == '&') && (str.length() > 1) && (str.charAt(1) != '#')) {
          XMLUtil.processEntity(str, reader, resolver);
        }
      }
    }
    catch (XMLParseException localXMLParseException)
    {
      throw new IOException(localXMLParseException.getMessage());
    }
  }
}
