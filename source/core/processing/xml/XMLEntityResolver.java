package processing.xml;

import java.io.Reader;
import java.io.StringReader;
import java.util.Hashtable;

public class XMLEntityResolver
{
  private Hashtable<String, Object> entities = new Hashtable();
  
  public XMLEntityResolver()
  {
    entities.put("amp", "&#38;");
    entities.put("quot", "&#34;");
    entities.put("apos", "&#39;");
    entities.put("lt", "&#60;");
    entities.put("gt", "&#62;");
  }
  
  protected void finalize()
    throws Throwable
  {
    entities.clear();
    entities = null;
    super.finalize();
  }
  
  public void addInternalEntity(String paramString1, String paramString2)
  {
    if (!entities.containsKey(paramString1)) {
      entities.put(paramString1, paramString2);
    }
  }
  
  public void addExternalEntity(String paramString1, String paramString2, String paramString3)
  {
    if (!entities.containsKey(paramString1)) {
      entities.put(paramString1, new String[] { paramString2, paramString3 });
    }
  }
  
  public Reader getEntity(StdXMLReader paramStdXMLReader, String paramString)
    throws XMLParseException
  {
    Object localObject = entities.get(paramString);
    if (localObject == null) {
      return null;
    }
    if ((localObject instanceof String)) {
      return new StringReader((String)localObject);
    }
    String[] arrayOfString = (String[])localObject;
    return openExternalEntity(paramStdXMLReader, arrayOfString[0], arrayOfString[1]);
  }
  
  public boolean isExternalEntity(String paramString)
  {
    Object localObject = entities.get(paramString);
    return !(localObject instanceof String);
  }
  
  protected Reader openExternalEntity(StdXMLReader paramStdXMLReader, String paramString1, String paramString2)
    throws XMLParseException
  {
    String str = paramStdXMLReader.getSystemID();
    try
    {
      return paramStdXMLReader.openStream(paramString1, paramString2);
    }
    catch (Exception localException)
    {
      throw new XMLParseException(str, paramStdXMLReader.getLineNr(), "Could not open external entity at system ID: " + paramString2);
    }
  }
}
