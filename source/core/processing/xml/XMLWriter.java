package processing.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

public class XMLWriter
{
  static final int INDENT = 2;
  static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
  private PrintWriter writer;
  
  public XMLWriter(Writer paramWriter)
  {
    if ((paramWriter instanceof PrintWriter)) {
      writer = ((PrintWriter)paramWriter);
    } else {
      writer = new PrintWriter(paramWriter);
    }
  }
  
  public XMLWriter(OutputStream paramOutputStream)
  {
    writer = new PrintWriter(paramOutputStream);
  }
  
  protected void finalize()
    throws Throwable
  {
    writer = null;
    super.finalize();
  }
  
  public void write(XMLElement paramXMLElement)
    throws IOException
  {
    write(paramXMLElement, false, 0, 2, true);
  }
  
  public void write(XMLElement paramXMLElement, boolean paramBoolean)
    throws IOException
  {
    write(paramXMLElement, paramBoolean, 0, 2, true);
  }
  
  public void write(XMLElement paramXMLElement, boolean paramBoolean, int paramInt)
    throws IOException
  {
    write(paramXMLElement, paramBoolean, paramInt, 2, true);
  }
  
  public void write(XMLElement paramXMLElement, boolean paramBoolean1, int paramInt1, int paramInt2, boolean paramBoolean2)
    throws IOException
  {
    if (paramBoolean1) {
      for (int i = 0; i < paramInt1; i++) {
        writer.print(' ');
      }
    }
    if (paramXMLElement.getLocalName() == null)
    {
      if (paramXMLElement.getContent() != null) {
        if (paramBoolean1)
        {
          writeEncoded(paramXMLElement.getContent().trim());
          writer.println();
        }
        else
        {
          writeEncoded(paramXMLElement.getContent());
        }
      }
    }
    else
    {
      writer.print('<');
      writer.print(paramXMLElement.getName());
      for (String str1 : paramXMLElement.listAttributes())
      {
        String str2 = paramXMLElement.getString(str1, null);
        writer.print(" " + str1 + "=\"");
        writeEncoded(str2);
        writer.print('"');
      }
      if ((paramXMLElement.getContent() != null) && (paramXMLElement.getContent().length() > 0))
      {
        writer.print('>');
        writeEncoded(paramXMLElement.getContent());
        writer.print("</" + paramXMLElement.getName() + '>');
        if (paramBoolean1) {
          writer.println();
        }
      }
      else if ((paramXMLElement.hasChildren()) || (!paramBoolean2))
      {
        writer.print('>');
        if (paramBoolean1) {
          writer.println();
        }
        int j = paramXMLElement.getChildCount();
        for (??? = 0; ??? < j; ???++)
        {
          XMLElement localXMLElement = paramXMLElement.getChild(???);
          write(localXMLElement, paramBoolean1, paramInt1 + paramInt2, paramInt2, paramBoolean2);
        }
        if (paramBoolean1) {
          for (??? = 0; ??? < paramInt1; ???++) {
            writer.print(' ');
          }
        }
        writer.print("</" + paramXMLElement.getName() + ">");
        if (paramBoolean1) {
          writer.println();
        }
      }
      else
      {
        writer.print("/>");
        if (paramBoolean1) {
          writer.println();
        }
      }
    }
    writer.flush();
  }
  
  private void writeEncoded(String paramString)
  {
    for (int i = 0; i < paramString.length(); i++)
    {
      char c = paramString.charAt(i);
      switch (c)
      {
      case '\n': 
        writer.print(c);
        break;
      case '<': 
        writer.print("&lt;");
        break;
      case '>': 
        writer.print("&gt;");
        break;
      case '&': 
        writer.print("&amp;");
        break;
      case '\'': 
        writer.print("&apos;");
        break;
      case '"': 
        writer.print("&quot;");
        break;
      default: 
        if ((c < ' ') || (c > '~'))
        {
          writer.print("&#x");
          writer.print(Integer.toString(c, 16));
          writer.print(';');
        }
        else
        {
          writer.print(c);
        }
        break;
      }
    }
  }
}
