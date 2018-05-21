package processing.xml;

import java.io.IOException;
import java.io.Reader;

class PIReader
  extends Reader
{
  private StdXMLReader reader;
  private boolean atEndOfData;
  
  PIReader(StdXMLReader paramStdXMLReader)
  {
    reader = paramStdXMLReader;
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
    if (atEndOfData) {
      return -1;
    }
    int i = 0;
    if (paramInt1 + paramInt2 > paramArrayOfChar.length) {
      paramInt2 = paramArrayOfChar.length - paramInt1;
    }
    while (i < paramInt2)
    {
      int j = reader.read();
      if (j == 63)
      {
        char c = reader.read();
        if (c == '>')
        {
          atEndOfData = true;
          break;
        }
        reader.unread(c);
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
      int i = reader.read();
      if (i == 63)
      {
        int j = reader.read();
        if (j == 62) {
          atEndOfData = true;
        }
      }
    }
  }
}
