package processing.xml;

import java.io.PrintStream;
import java.io.PrintWriter;

public class XMLException
  extends Exception
{
  private String msg;
  private String systemID;
  private int lineNr;
  private Exception encapsulatedException;
  
  public XMLException(String paramString)
  {
    this(null, -1, null, paramString, false);
  }
  
  public XMLException(Exception paramException)
  {
    this(null, -1, paramException, "Nested Exception", false);
  }
  
  public XMLException(String paramString, int paramInt, Exception paramException)
  {
    this(paramString, paramInt, paramException, "Nested Exception", true);
  }
  
  public XMLException(String paramString1, int paramInt, String paramString2)
  {
    this(paramString1, paramInt, null, paramString2, true);
  }
  
  public XMLException(String paramString1, int paramInt, Exception paramException, String paramString2, boolean paramBoolean)
  {
    super(buildMessage(paramString1, paramInt, paramException, paramString2, paramBoolean));
    systemID = paramString1;
    lineNr = paramInt;
    encapsulatedException = paramException;
    msg = buildMessage(paramString1, paramInt, paramException, paramString2, paramBoolean);
  }
  
  private static String buildMessage(String paramString1, int paramInt, Exception paramException, String paramString2, boolean paramBoolean)
  {
    String str = paramString2;
    if (paramBoolean)
    {
      if (paramString1 != null) {
        str = str + ", SystemID='" + paramString1 + "'";
      }
      if (paramInt >= 0) {
        str = str + ", Line=" + paramInt;
      }
      if (paramException != null) {
        str = str + ", Exception: " + paramException;
      }
    }
    return str;
  }
  
  protected void finalize()
    throws Throwable
  {
    systemID = null;
    encapsulatedException = null;
    super.finalize();
  }
  
  public String getSystemID()
  {
    return systemID;
  }
  
  public int getLineNr()
  {
    return lineNr;
  }
  
  public Exception getException()
  {
    return encapsulatedException;
  }
  
  public void printStackTrace(PrintWriter paramPrintWriter)
  {
    super.printStackTrace(paramPrintWriter);
    if (encapsulatedException != null)
    {
      paramPrintWriter.println("*** Nested Exception:");
      encapsulatedException.printStackTrace(paramPrintWriter);
    }
  }
  
  public void printStackTrace(PrintStream paramPrintStream)
  {
    super.printStackTrace(paramPrintStream);
    if (encapsulatedException != null)
    {
      paramPrintStream.println("*** Nested Exception:");
      encapsulatedException.printStackTrace(paramPrintStream);
    }
  }
  
  public void printStackTrace()
  {
    super.printStackTrace();
    if (encapsulatedException != null)
    {
      System.err.println("*** Nested Exception:");
      encapsulatedException.printStackTrace();
    }
  }
  
  public String toString()
  {
    return msg;
  }
}
