package processing.xml;

public class XMLValidationException
  extends XMLException
{
  public static final int MISSING_ELEMENT = 1;
  public static final int UNEXPECTED_ELEMENT = 2;
  public static final int MISSING_ATTRIBUTE = 3;
  public static final int UNEXPECTED_ATTRIBUTE = 4;
  public static final int ATTRIBUTE_WITH_INVALID_VALUE = 5;
  public static final int MISSING_PCDATA = 6;
  public static final int UNEXPECTED_PCDATA = 7;
  public static final int MISC_ERROR = 0;
  private String elementName;
  private String attributeName;
  private String attributeValue;
  
  public XMLValidationException(int paramInt1, String paramString1, int paramInt2, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    super(paramString1, paramInt2, null, paramString5 + (paramString2 == null ? "" : new StringBuilder().append(", element=").append(paramString2).toString()) + (paramString3 == null ? "" : new StringBuilder().append(", attribute=").append(paramString3).toString()) + (paramString4 == null ? "" : new StringBuilder().append(", value='").append(paramString4).append("'").toString()), false);
    elementName = paramString2;
    attributeName = paramString3;
    attributeValue = paramString4;
  }
  
  protected void finalize()
    throws Throwable
  {
    elementName = null;
    attributeName = null;
    attributeValue = null;
    super.finalize();
  }
  
  public String getElementName()
  {
    return elementName;
  }
  
  public String getAttributeName()
  {
    return attributeName;
  }
  
  public String getAttributeValue()
  {
    return attributeValue;
  }
}
