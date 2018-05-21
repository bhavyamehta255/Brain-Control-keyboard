package processing.xml;

public class XMLParseException
  extends XMLException
{
  public XMLParseException(String paramString)
  {
    super(paramString);
  }
  
  public XMLParseException(String paramString1, int paramInt, String paramString2)
  {
    super(paramString1, paramInt, null, paramString2, true);
  }
}
