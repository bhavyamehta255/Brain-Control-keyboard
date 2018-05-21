package org.json;

import java.util.Iterator;

public class JSONML
{
  public JSONML() {}
  
  private static Object parse(XMLTokener paramXMLTokener, boolean paramBoolean, JSONArray paramJSONArray)
    throws JSONException
  {
    String str2 = null;
    JSONArray localJSONArray = null;
    JSONObject localJSONObject = null;
    String str3 = null;
    for (;;)
    {
      Object localObject = paramXMLTokener.nextContent();
      if (localObject == XML.LT)
      {
        localObject = paramXMLTokener.nextToken();
        if ((localObject instanceof Character))
        {
          if (localObject == XML.SLASH)
          {
            localObject = paramXMLTokener.nextToken();
            if (!(localObject instanceof String)) {
              throw new JSONException("Expected a closing name instead of '" + localObject + "'.");
            }
            if (paramXMLTokener.nextToken() != XML.GT) {
              throw paramXMLTokener.syntaxError("Misshaped close tag");
            }
            return localObject;
          }
          if (localObject == XML.BANG)
          {
            int i = paramXMLTokener.next();
            if (i == 45)
            {
              if (paramXMLTokener.next() == '-') {
                paramXMLTokener.skipPast("-->");
              }
              paramXMLTokener.back();
            }
            else if (i == 91)
            {
              localObject = paramXMLTokener.nextToken();
              if ((localObject.equals("CDATA")) && (paramXMLTokener.next() == '['))
              {
                if (paramJSONArray != null) {
                  paramJSONArray.put(paramXMLTokener.nextCDATA());
                }
              }
              else {
                throw paramXMLTokener.syntaxError("Expected 'CDATA['");
              }
            }
            else
            {
              int j = 1;
              do
              {
                localObject = paramXMLTokener.nextMeta();
                if (localObject == null) {
                  throw paramXMLTokener.syntaxError("Missing '>' after '<!'.");
                }
                if (localObject == XML.LT) {
                  j++;
                } else if (localObject == XML.GT) {
                  j--;
                }
              } while (j > 0);
            }
          }
          else if (localObject == XML.QUEST)
          {
            paramXMLTokener.skipPast("?>");
          }
          else
          {
            throw paramXMLTokener.syntaxError("Misshaped tag");
          }
        }
        else
        {
          if (!(localObject instanceof String)) {
            throw paramXMLTokener.syntaxError("Bad tagName '" + localObject + "'.");
          }
          str3 = (String)localObject;
          localJSONArray = new JSONArray();
          localJSONObject = new JSONObject();
          if (paramBoolean)
          {
            localJSONArray.put(str3);
            if (paramJSONArray != null) {
              paramJSONArray.put(localJSONArray);
            }
          }
          else
          {
            localJSONObject.put("tagName", str3);
            if (paramJSONArray != null) {
              paramJSONArray.put(localJSONObject);
            }
          }
          localObject = null;
          for (;;)
          {
            if (localObject == null) {
              localObject = paramXMLTokener.nextToken();
            }
            if (localObject == null) {
              throw paramXMLTokener.syntaxError("Misshaped tag");
            }
            if (!(localObject instanceof String)) {
              break;
            }
            String str1 = (String)localObject;
            if ((!paramBoolean) && ((str1 == "tagName") || (str1 == "childNode"))) {
              throw paramXMLTokener.syntaxError("Reserved attribute.");
            }
            localObject = paramXMLTokener.nextToken();
            if (localObject == XML.EQ)
            {
              localObject = paramXMLTokener.nextToken();
              if (!(localObject instanceof String)) {
                throw paramXMLTokener.syntaxError("Missing value");
              }
              localJSONObject.accumulate(str1, XML.stringToValue((String)localObject));
              localObject = null;
            }
            else
            {
              localJSONObject.accumulate(str1, "");
            }
          }
          if ((paramBoolean) && (localJSONObject.length() > 0)) {
            localJSONArray.put(localJSONObject);
          }
          if (localObject == XML.SLASH)
          {
            if (paramXMLTokener.nextToken() != XML.GT) {
              throw paramXMLTokener.syntaxError("Misshaped tag");
            }
            if (paramJSONArray == null)
            {
              if (paramBoolean) {
                return localJSONArray;
              }
              return localJSONObject;
            }
          }
          else
          {
            if (localObject != XML.GT) {
              throw paramXMLTokener.syntaxError("Misshaped tag");
            }
            str2 = (String)parse(paramXMLTokener, paramBoolean, localJSONArray);
            if (str2 != null)
            {
              if (!str2.equals(str3)) {
                throw paramXMLTokener.syntaxError("Mismatched '" + str3 + "' and '" + str2 + "'");
              }
              str3 = null;
              if ((!paramBoolean) && (localJSONArray.length() > 0)) {
                localJSONObject.put("childNodes", localJSONArray);
              }
              if (paramJSONArray == null)
              {
                if (paramBoolean) {
                  return localJSONArray;
                }
                return localJSONObject;
              }
            }
          }
        }
      }
      else if (paramJSONArray != null)
      {
        paramJSONArray.put((localObject instanceof String) ? XML.stringToValue((String)localObject) : localObject);
      }
    }
  }
  
  public static JSONArray toJSONArray(String paramString)
    throws JSONException
  {
    return toJSONArray(new XMLTokener(paramString));
  }
  
  public static JSONArray toJSONArray(XMLTokener paramXMLTokener)
    throws JSONException
  {
    return (JSONArray)parse(paramXMLTokener, true, null);
  }
  
  public static JSONObject toJSONObject(XMLTokener paramXMLTokener)
    throws JSONException
  {
    return (JSONObject)parse(paramXMLTokener, false, null);
  }
  
  public static JSONObject toJSONObject(String paramString)
    throws JSONException
  {
    return toJSONObject(new XMLTokener(paramString));
  }
  
  public static String toString(JSONArray paramJSONArray)
    throws JSONException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str2 = paramJSONArray.getString(0);
    XML.noSpace(str2);
    str2 = XML.escape(str2);
    localStringBuffer.append('<');
    localStringBuffer.append(str2);
    Object localObject = paramJSONArray.opt(1);
    if ((localObject instanceof JSONObject))
    {
      i = 2;
      JSONObject localJSONObject = (JSONObject)localObject;
      Iterator localIterator = localJSONObject.keys();
      while (localIterator.hasNext())
      {
        String str1 = localIterator.next().toString();
        XML.noSpace(str1);
        String str3 = localJSONObject.optString(str1);
        if (str3 != null)
        {
          localStringBuffer.append(' ');
          localStringBuffer.append(XML.escape(str1));
          localStringBuffer.append('=');
          localStringBuffer.append('"');
          localStringBuffer.append(XML.escape(str3));
          localStringBuffer.append('"');
        }
      }
    }
    int i = 1;
    int j = paramJSONArray.length();
    if (i >= j)
    {
      localStringBuffer.append('/');
      localStringBuffer.append('>');
    }
    else
    {
      localStringBuffer.append('>');
      do
      {
        localObject = paramJSONArray.get(i);
        i++;
        if (localObject != null) {
          if ((localObject instanceof String)) {
            localStringBuffer.append(XML.escape(localObject.toString()));
          } else if ((localObject instanceof JSONObject)) {
            localStringBuffer.append(toString((JSONObject)localObject));
          } else if ((localObject instanceof JSONArray)) {
            localStringBuffer.append(toString((JSONArray)localObject));
          }
        }
      } while (i < j);
      localStringBuffer.append('<');
      localStringBuffer.append('/');
      localStringBuffer.append(str2);
      localStringBuffer.append('>');
    }
    return localStringBuffer.toString();
  }
  
  public static String toString(JSONObject paramJSONObject)
    throws JSONException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str2 = paramJSONObject.optString("tagName");
    if (str2 == null) {
      return XML.escape(paramJSONObject.toString());
    }
    XML.noSpace(str2);
    str2 = XML.escape(str2);
    localStringBuffer.append('<');
    localStringBuffer.append(str2);
    Iterator localIterator = paramJSONObject.keys();
    while (localIterator.hasNext())
    {
      String str1 = localIterator.next().toString();
      if ((!str1.equals("tagName")) && (!str1.equals("childNodes")))
      {
        XML.noSpace(str1);
        String str3 = paramJSONObject.optString(str1);
        if (str3 != null)
        {
          localStringBuffer.append(' ');
          localStringBuffer.append(XML.escape(str1));
          localStringBuffer.append('=');
          localStringBuffer.append('"');
          localStringBuffer.append(XML.escape(str3));
          localStringBuffer.append('"');
        }
      }
    }
    JSONArray localJSONArray = paramJSONObject.optJSONArray("childNodes");
    if (localJSONArray == null)
    {
      localStringBuffer.append('/');
      localStringBuffer.append('>');
    }
    else
    {
      localStringBuffer.append('>');
      int j = localJSONArray.length();
      for (int i = 0; i < j; i++)
      {
        Object localObject = localJSONArray.get(i);
        if (localObject != null) {
          if ((localObject instanceof String)) {
            localStringBuffer.append(XML.escape(localObject.toString()));
          } else if ((localObject instanceof JSONObject)) {
            localStringBuffer.append(toString((JSONObject)localObject));
          } else if ((localObject instanceof JSONArray)) {
            localStringBuffer.append(toString((JSONArray)localObject));
          }
        }
      }
      localStringBuffer.append('<');
      localStringBuffer.append('/');
      localStringBuffer.append(str2);
      localStringBuffer.append('>');
    }
    return localStringBuffer.toString();
  }
}
