package processing.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PushbackInputStream;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

public class StdXMLReader
{
  private Stack<StackedReader> readers;
  private StackedReader currentReader;
  
  public static StdXMLReader stringReader(String paramString)
  {
    return new StdXMLReader(new StringReader(paramString));
  }
  
  public static StdXMLReader fileReader(String paramString)
    throws FileNotFoundException, IOException
  {
    StdXMLReader localStdXMLReader = new StdXMLReader(new FileInputStream(paramString));
    localStdXMLReader.setSystemID(paramString);
    for (int i = 0; i < readers.size(); i++)
    {
      StackedReader localStackedReader = (StackedReader)readers.elementAt(i);
      systemId = currentReader.systemId;
    }
    return localStdXMLReader;
  }
  
  public StdXMLReader(String paramString1, String paramString2)
    throws MalformedURLException, FileNotFoundException, IOException
  {
    URL localURL = null;
    try
    {
      localURL = new URL(paramString2);
    }
    catch (MalformedURLException localMalformedURLException1)
    {
      paramString2 = "file:" + paramString2;
      try
      {
        localURL = new URL(paramString2);
      }
      catch (MalformedURLException localMalformedURLException2)
      {
        throw localMalformedURLException1;
      }
    }
    currentReader = new StackedReader(null);
    readers = new Stack();
    Reader localReader = openStream(paramString1, localURL.toString());
    currentReader.lineReader = new LineNumberReader(localReader);
    currentReader.pbReader = new PushbackReader(currentReader.lineReader, 2);
  }
  
  public StdXMLReader(Reader paramReader)
  {
    currentReader = new StackedReader(null);
    readers = new Stack();
    currentReader.lineReader = new LineNumberReader(paramReader);
    currentReader.pbReader = new PushbackReader(currentReader.lineReader, 2);
    currentReader.publicId = "";
    try
    {
      currentReader.systemId = new URL("file:.");
    }
    catch (MalformedURLException localMalformedURLException) {}
  }
  
  protected void finalize()
    throws Throwable
  {
    currentReader.lineReader = null;
    currentReader.pbReader = null;
    currentReader.systemId = null;
    currentReader.publicId = null;
    currentReader = null;
    readers.clear();
    super.finalize();
  }
  
  protected String getEncoding(String paramString)
  {
    if (!paramString.startsWith("<?xml")) {
      return null;
    }
    int k;
    for (int i = 5; i < paramString.length(); i = k + 1)
    {
      StringBuffer localStringBuffer = new StringBuffer();
      while ((i < paramString.length()) && (paramString.charAt(i) <= ' ')) {
        i++;
      }
      while ((i < paramString.length()) && (paramString.charAt(i) >= 'a') && (paramString.charAt(i) <= 'z'))
      {
        localStringBuffer.append(paramString.charAt(i));
        i++;
      }
      while ((i < paramString.length()) && (paramString.charAt(i) <= ' ')) {
        i++;
      }
      if ((i >= paramString.length()) || (paramString.charAt(i) != '=')) {
        break;
      }
      while ((i < paramString.length()) && (paramString.charAt(i) != '\'') && (paramString.charAt(i) != '"')) {
        i++;
      }
      if (i >= paramString.length()) {
        break;
      }
      int j = paramString.charAt(i);
      i++;
      k = paramString.indexOf(j, i);
      if (k < 0) {
        break;
      }
      if (localStringBuffer.toString().equals("encoding")) {
        return paramString.substring(i, k);
      }
    }
    return null;
  }
  
  protected Reader stream2reader(InputStream paramInputStream, StringBuffer paramStringBuffer)
    throws IOException
  {
    PushbackInputStream localPushbackInputStream = new PushbackInputStream(paramInputStream);
    int i = localPushbackInputStream.read();
    switch (i)
    {
    case 0: 
    case 254: 
    case 255: 
      localPushbackInputStream.unread(i);
      return new InputStreamReader(localPushbackInputStream, "UTF-16");
    case 239: 
      for (int j = 0; j < 2; j++) {
        localPushbackInputStream.read();
      }
      return new InputStreamReader(localPushbackInputStream, "UTF-8");
    case 60: 
      i = localPushbackInputStream.read();
      paramStringBuffer.append('<');
      while ((i > 0) && (i != 62))
      {
        paramStringBuffer.append((char)i);
        i = localPushbackInputStream.read();
      }
      if (i > 0) {
        paramStringBuffer.append((char)i);
      }
      String str = getEncoding(paramStringBuffer.toString());
      if (str == null) {
        return new InputStreamReader(localPushbackInputStream, "UTF-8");
      }
      paramStringBuffer.setLength(0);
      try
      {
        return new InputStreamReader(localPushbackInputStream, str);
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        return new InputStreamReader(localPushbackInputStream, "UTF-8");
      }
    }
    paramStringBuffer.append((char)i);
    return new InputStreamReader(localPushbackInputStream, "UTF-8");
  }
  
  public StdXMLReader(InputStream paramInputStream)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Reader localReader = stream2reader(paramInputStream, localStringBuffer);
    currentReader = new StackedReader(null);
    readers = new Stack();
    currentReader.lineReader = new LineNumberReader(localReader);
    currentReader.pbReader = new PushbackReader(currentReader.lineReader, 2);
    currentReader.publicId = "";
    try
    {
      currentReader.systemId = new URL("file:.");
    }
    catch (MalformedURLException localMalformedURLException) {}
    startNewStream(new StringReader(localStringBuffer.toString()));
  }
  
  public char read()
    throws IOException
  {
    for (int i = currentReader.pbReader.read(); i < 0; i = currentReader.pbReader.read())
    {
      if (readers.empty()) {
        throw new IOException("Unexpected EOF");
      }
      currentReader.pbReader.close();
      currentReader = ((StackedReader)readers.pop());
    }
    return (char)i;
  }
  
  public boolean atEOFOfCurrentStream()
    throws IOException
  {
    int i = currentReader.pbReader.read();
    if (i < 0) {
      return true;
    }
    currentReader.pbReader.unread(i);
    return false;
  }
  
  public boolean atEOF()
    throws IOException
  {
    for (int i = currentReader.pbReader.read(); i < 0; i = currentReader.pbReader.read())
    {
      if (readers.empty()) {
        return true;
      }
      currentReader.pbReader.close();
      currentReader = ((StackedReader)readers.pop());
    }
    currentReader.pbReader.unread(i);
    return false;
  }
  
  public void unread(char paramChar)
    throws IOException
  {
    currentReader.pbReader.unread(paramChar);
  }
  
  public Reader openStream(String paramString1, String paramString2)
    throws MalformedURLException, FileNotFoundException, IOException
  {
    URL localURL = new URL(currentReader.systemId, paramString2);
    if (localURL.getRef() != null)
    {
      localObject = localURL.getRef();
      if (localURL.getFile().length() > 0)
      {
        localURL = new URL(localURL.getProtocol(), localURL.getHost(), localURL.getPort(), localURL.getFile());
        localURL = new URL("jar:" + localURL + '!' + (String)localObject);
      }
      else
      {
        localURL = StdXMLReader.class.getResource((String)localObject);
      }
    }
    currentReader.publicId = paramString1;
    currentReader.systemId = localURL;
    Object localObject = new StringBuffer();
    Reader localReader = stream2reader(localURL.openStream(), (StringBuffer)localObject);
    if (((StringBuffer)localObject).length() == 0) {
      return localReader;
    }
    String str = ((StringBuffer)localObject).toString();
    PushbackReader localPushbackReader = new PushbackReader(localReader, str.length());
    for (int i = str.length() - 1; i >= 0; i--) {
      localPushbackReader.unread(str.charAt(i));
    }
    return localPushbackReader;
  }
  
  public void startNewStream(Reader paramReader)
  {
    startNewStream(paramReader, false);
  }
  
  public void startNewStream(Reader paramReader, boolean paramBoolean)
  {
    StackedReader localStackedReader = currentReader;
    readers.push(currentReader);
    currentReader = new StackedReader(null);
    if (paramBoolean)
    {
      currentReader.lineReader = null;
      currentReader.pbReader = new PushbackReader(paramReader, 2);
    }
    else
    {
      currentReader.lineReader = new LineNumberReader(paramReader);
      currentReader.pbReader = new PushbackReader(currentReader.lineReader, 2);
    }
    currentReader.systemId = systemId;
    currentReader.publicId = publicId;
  }
  
  public int getStreamLevel()
  {
    return readers.size();
  }
  
  public int getLineNr()
  {
    if (currentReader.lineReader == null)
    {
      StackedReader localStackedReader = (StackedReader)readers.peek();
      if (lineReader == null) {
        return 0;
      }
      return lineReader.getLineNumber() + 1;
    }
    return currentReader.lineReader.getLineNumber() + 1;
  }
  
  public void setSystemID(String paramString)
    throws MalformedURLException
  {
    currentReader.systemId = new URL(currentReader.systemId, paramString);
  }
  
  public void setPublicID(String paramString)
  {
    currentReader.publicId = paramString;
  }
  
  public String getSystemID()
  {
    return currentReader.systemId.toString();
  }
  
  public String getPublicID()
  {
    return currentReader.publicId;
  }
  
  private class StackedReader
  {
    PushbackReader pbReader;
    LineNumberReader lineReader;
    URL systemId;
    String publicId;
    
    private StackedReader() {}
  }
}
