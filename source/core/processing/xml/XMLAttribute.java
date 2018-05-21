package processing.xml;

class XMLAttribute
{
  private String name;
  private String localName;
  private String namespace;
  private String value;
  private String type;
  
  XMLAttribute(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    name = paramString1;
    localName = paramString2;
    namespace = paramString3;
    value = paramString4;
    type = paramString5;
  }
  
  String getName()
  {
    return name;
  }
  
  String getLocalName()
  {
    return localName;
  }
  
  String getNamespace()
  {
    return namespace;
  }
  
  String getValue()
  {
    return value;
  }
  
  void setValue(String paramString)
  {
    value = paramString;
  }
  
  String getType()
  {
    return type;
  }
}
