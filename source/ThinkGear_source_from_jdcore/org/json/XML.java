package org.json;

import java.util.Iterator;

public class XML
{
  public static final Character AMP = new Character('&');
  public static final Character APOS = new Character('\'');
  public static final Character BANG = new Character('!');
  public static final Character EQ = new Character('=');
  public static final Character GT = new Character('>');
  public static final Character LT = new Character('<');
  public static final Character QUEST = new Character('?');
  public static final Character QUOT = new Character('"');
  public static final Character SLASH = new Character('/');
  
  public XML() {}
  
  public static String escape(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    int j = paramString.length();
    while (i < j)
    {
      char c = paramString.charAt(i);
      switch (c)
      {
      case '&': 
        localStringBuffer.append("&amp;");
        break;
      case '<': 
        localStringBuffer.append("&lt;");
        break;
      case '>': 
        localStringBuffer.append("&gt;");
        break;
      case '"': 
        localStringBuffer.append("&quot;");
        break;
      case '\'': 
        localStringBuffer.append("&apos;");
        break;
      default: 
        localStringBuffer.append(c);
      }
      i++;
    }
    return localStringBuffer.toString();
  }
  
  public static void noSpace(String paramString)
    throws JSONException
  {
    int j = paramString.length();
    if (j == 0) {
      throw new JSONException("Empty string.");
    }
    for (int i = 0; i < j; i++) {
      if (Character.isWhitespace(paramString.charAt(i))) {
        throw new JSONException("'" + paramString + "' contains a space character.");
      }
    }
  }
  
  private static boolean parse(XMLTokener paramXMLTokener, JSONObject paramJSONObject, String paramString)
    throws JSONException
  {
    JSONObject localJSONObject = null;
    Object localObject = paramXMLTokener.nextToken();
    String str1;
    if (localObject == BANG)
    {
      int i = paramXMLTokener.next();
      if (i == 45)
      {
        if (paramXMLTokener.next() == '-')
        {
          paramXMLTokener.skipPast("-->");
          return false;
        }
        paramXMLTokener.back();
      }
      else if (i == 91)
      {
        localObject = paramXMLTokener.nextToken();
        if ((localObject.equals("CDATA")) && (paramXMLTokener.next() == '['))
        {
          str1 = paramXMLTokener.nextCDATA();
          if (str1.length() > 0) {
            paramJSONObject.accumulate("content", str1);
          }
          return false;
        }
        throw paramXMLTokener.syntaxError("Expected 'CDATA['");
      }
      int j = 1;
      do
      {
        localObject = paramXMLTokener.nextMeta();
        if (localObject == null) {
          throw paramXMLTokener.syntaxError("Missing '>' after '<!'.");
        }
        if (localObject == LT) {
          j++;
        } else if (localObject == GT) {
          j--;
        }
      } while (j > 0);
      return false;
    }
    if (localObject == QUEST)
    {
      paramXMLTokener.skipPast("?>");
      return false;
    }
    if (localObject == SLASH)
    {
      localObject = paramXMLTokener.nextToken();
      if (paramString == null) {
        throw paramXMLTokener.syntaxError("Mismatched close tag " + localObject);
      }
      if (!localObject.equals(paramString)) {
        throw paramXMLTokener.syntaxError("Mismatched " + paramString + " and " + localObject);
      }
      if (paramXMLTokener.nextToken() != GT) {
        throw paramXMLTokener.syntaxError("Misshaped close tag");
      }
      return true;
    }
    if ((localObject instanceof Character)) {
      throw paramXMLTokener.syntaxError("Misshaped tag");
    }
    String str2 = (String)localObject;
    localObject = null;
    localJSONObject = new JSONObject();
    for (;;)
    {
      if (localObject == null) {
        localObject = paramXMLTokener.nextToken();
      }
      if (!(localObject instanceof String)) {
        break;
      }
      str1 = (String)localObject;
      localObject = paramXMLTokener.nextToken();
      if (localObject == EQ)
      {
        localObject = paramXMLTokener.nextToken();
        if (!(localObject instanceof String)) {
          throw paramXMLTokener.syntaxError("Missing value");
        }
        localJSONObject.accumulate(str1, stringToValue((String)localObject));
        localObject = null;
      }
      else
      {
        localJSONObject.accumulate(str1, "");
      }
    }
    if (localObject == SLASH)
    {
      if (paramXMLTokener.nextToken() != GT) {
        throw paramXMLTokener.syntaxError("Misshaped tag");
      }
      if (localJSONObject.length() > 0) {
        paramJSONObject.accumulate(str2, localJSONObject);
      } else {
        paramJSONObject.accumulate(str2, "");
      }
      return false;
    }
    if (localObject == GT)
    {
      do
      {
        for (;;)
        {
          localObject = paramXMLTokener.nextContent();
          if (localObject == null)
          {
            if (str2 != null) {
              throw paramXMLTokener.syntaxError("Unclosed tag " + str2);
            }
            return false;
          }
          if (!(localObject instanceof String)) {
            break;
          }
          str1 = (String)localObject;
          if (str1.length() > 0) {
            localJSONObject.accumulate("content", stringToValue(str1));
          }
        }
      } while ((localObject != LT) || (!parse(paramXMLTokener, localJSONObject, str2)));
      if (localJSONObject.length() == 0) {
        paramJSONObject.accumulate(str2, "");
      } else if ((localJSONObject.length() == 1) && (localJSONObject.opt("content") != null)) {
        paramJSONObject.accumulate(str2, localJSONObject.opt("content"));
      } else {
        paramJSONObject.accumulate(str2, localJSONObject);
      }
      return false;
    }
    throw paramXMLTokener.syntaxError("Misshaped tag");
  }
  
  public static Object stringToValue(String paramString)
  {
    if (paramString.equals("")) {
      return paramString;
    }
    if (paramString.equalsIgnoreCase("true")) {
      return Boolean.TRUE;
    }
    if (paramString.equalsIgnoreCase("false")) {
      return Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("null")) {
      return JSONObject.NULL;
    }
    if (paramString.equals("0")) {
      return new Integer(0);
    }
    try
    {
      int i = paramString.charAt(0);
      int j = 0;
      if (i == 45)
      {
        i = paramString.charAt(1);
        j = 1;
      }
      if (i == 48) {
        if (paramString.charAt(j != 0 ? 2 : 1) == '0') {
          return paramString;
        }
      }
      if ((i >= 48) && (i <= 57))
      {
        if (paramString.indexOf('.') >= 0) {
          return Double.valueOf(paramString);
        }
        if ((paramString.indexOf('e') < 0) && (paramString.indexOf('E') < 0))
        {
          Long localLong = new Long(paramString);
          if (localLong.longValue() == localLong.intValue()) {
            return new Integer(localLong.intValue());
          }
          return localLong;
        }
      }
    }
    catch (Exception localException) {}
    return paramString;
  }
  
  public static JSONObject toJSONObject(String paramString)
    throws JSONException
  {
    JSONObject localJSONObject = new JSONObject();
    XMLTokener localXMLTokener = new XMLTokener(paramString);
    while ((localXMLTokener.more()) && (localXMLTokener.skipPast("<"))) {
      parse(localXMLTokener, localJSONObject, null);
    }
    return localJSONObject;
  }
  
  public static String toString(Object paramObject)
    throws JSONException
  {
    return toString(paramObject, null);
  }
  
  public static String toString(Object paramObject, String paramString)
    throws JSONException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    JSONArray localJSONArray;
    int j;
    int i;
    if ((paramObject instanceof JSONObject))
    {
      if (paramString != null)
      {
        localStringBuffer.append('<');
        localStringBuffer.append(paramString);
        localStringBuffer.append('>');
      }
      JSONObject localJSONObject = (JSONObject)paramObject;
      Iterator localIterator = localJSONObject.keys();
      while (localIterator.hasNext())
      {
        String str1 = localIterator.next().toString();
        Object localObject = localJSONObject.opt(str1);
        if (localObject == null) {
          localObject = "";
        }
        if ((localObject instanceof String)) {
          str2 = (String)localObject;
        } else {
          str2 = null;
        }
        if (str1.equals("content"))
        {
          if ((localObject instanceof JSONArray))
          {
            localJSONArray = (JSONArray)localObject;
            j = localJSONArray.length();
            for (i = 0; i < j; i++)
            {
              if (i > 0) {
                localStringBuffer.append('\n');
              }
              localStringBuffer.append(escape(localJSONArray.get(i).toString()));
            }
          }
          else
          {
            localStringBuffer.append(escape(localObject.toString()));
          }
        }
        else if ((localObject instanceof JSONArray))
        {
          localJSONArray = (JSONArray)localObject;
          j = localJSONArray.length();
          for (i = 0; i < j; i++)
          {
            localObject = localJSONArray.get(i);
            if ((localObject instanceof JSONArray))
            {
              localStringBuffer.append('<');
              localStringBuffer.append(str1);
              localStringBuffer.append('>');
              localStringBuffer.append(toString(localObject));
              localStringBuffer.append("</");
              localStringBuffer.append(str1);
              localStringBuffer.append('>');
            }
            else
            {
              localStringBuffer.append(toString(localObject, str1));
            }
          }
        }
        else if (localObject.equals(""))
        {
          localStringBuffer.append('<');
          localStringBuffer.append(str1);
          localStringBuffer.append("/>");
        }
        else
        {
          localStringBuffer.append(toString(localObject, str1));
        }
      }
      if (paramString != null)
      {
        localStringBuffer.append("</");
        localStringBuffer.append(paramString);
        localStringBuffer.append('>');
      }
      return localStringBuffer.toString();
    }
    if (paramObject.getClass().isArray()) {
      paramObject = new JSONArray(paramObject);
    }
    if ((paramObject instanceof JSONArray))
    {
      localJSONArray = (JSONArray)paramObject;
      j = localJSONArray.length();
      for (i = 0; i < j; i++) {
        localStringBuffer.append(toString(localJSONArray.opt(i), paramString == null ? "array" : paramString));
      }
      return localStringBuffer.toString();
    }
    String str2 = paramObject == null ? "null" : escape(paramObject.toString());
    return "<" + paramString + ">" + str2 + "</" + paramString + ">";
  }
}
