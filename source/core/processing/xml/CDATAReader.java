package processing.xml;

import java.io.IOException;
import java.io.Reader;

class CDATAReader
  extends Reader
{
  private StdXMLReader reader;
  private char savedChar;
  private boolean atEndOfData;
  
  CDATAReader(StdXMLReader paramStdXMLReader)
  {
    reader = paramStdXMLReader;
    savedChar = '\000';
    atEndOfData = false;
  }
  
  protected void finalize()
    throws Throwable
  {
    reader = null;
    super.finalize();
  }
  
  public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = 0;
    if (atEndOfData) {
      return -1;
    }
    if (paramInt1 + paramInt2 > paramArrayOfChar.length) {
      paramInt2 = paramArrayOfChar.length - paramInt1;
    }
    while (i < paramInt2)
    {
      int j = savedChar;
      if (j == 0) {
        j = reader.read();
      } else {
        savedChar = '\000';
      }
      if (j == 93)
      {
        char c1 = reader.read();
        if (c1 == ']')
        {
          char c2 = reader.read();
          if (c2 == '>')
          {
            atEndOfData = true;
            break;
          }
          savedChar = c1;
          reader.unread(c2);
        }
        else
        {
          reader.unread(c1);
        }
      }
      paramArrayOfChar[i] = j;
      i++;
    }
    if (i == 0) {
      i = -1;
    }
    return i;
  }
  
  public void close()
    throws IOException
  {
    while (!atEndOfData)
    {
      int i = savedChar;
      if (i == 0) {
        i = reader.read();
      } else {
        savedChar = '\000';
      }
      if (i == 93)
      {
        char c1 = reader.read();
        if (c1 == ']')
        {
          char c2 = reader.read();
          if (c2 == '>') {
            break;
          }
          savedChar = c1;
          reader.unread(c2);
        }
        else
        {
          reader.unread(c1);
        }
      }
    }
    atEndOfData = true;
  }
}
