package org.json;

public class CDL
{
  public CDL() {}
  
  private static String getValue(JSONTokener paramJSONTokener)
    throws JSONException
  {
    char c1;
    do
    {
      c1 = paramJSONTokener.next();
    } while ((c1 == ' ') || (c1 == '\t'));
    switch (c1)
    {
    case '\000': 
      return null;
    case '"': 
    case '\'': 
      char c2 = c1;
      StringBuffer localStringBuffer = new StringBuffer();
      for (;;)
      {
        c1 = paramJSONTokener.next();
        if (c1 == c2) {
          break;
        }
        if ((c1 == 0) || (c1 == '\n') || (c1 == '\r')) {
          throw paramJSONTokener.syntaxError("Missing close quote '" + c2 + "'.");
        }
        localStringBuffer.append(c1);
      }
      return localStringBuffer.toString();
    case ',': 
      paramJSONTokener.back();
      return "";
    }
    paramJSONTokener.back();
    return paramJSONTokener.nextTo(',');
  }
  
  public static JSONArray rowToJSONArray(JSONTokener paramJSONTokener)
    throws JSONException
  {
    JSONArray localJSONArray = new JSONArray();
    for (;;)
    {
      String str = getValue(paramJSONTokener);
      char c = paramJSONTokener.next();
      if ((str == null) || ((localJSONArray.length() == 0) && (str.length() == 0) && (c != ','))) {
        return null;
      }
      localJSONArray.put(str);
      while (c != ',')
      {
        if (c != ' ')
        {
          if ((c == '\n') || (c == '\r') || (c == 0)) {
            return localJSONArray;
          }
          throw paramJSONTokener.syntaxError("Bad character '" + c + "' (" + c + ").");
        }
        c = paramJSONTokener.next();
      }
    }
  }
  
  public static JSONObject rowToJSONObject(JSONArray paramJSONArray, JSONTokener paramJSONTokener)
    throws JSONException
  {
    JSONArray localJSONArray = rowToJSONArray(paramJSONTokener);
    return localJSONArray != null ? localJSONArray.toJSONObject(paramJSONArray) : null;
  }
  
  public static String rowToString(JSONArray paramJSONArray)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramJSONArray.length(); i++)
    {
      if (i > 0) {
        localStringBuffer.append(',');
      }
      Object localObject = paramJSONArray.opt(i);
      if (localObject != null)
      {
        String str = localObject.toString();
        if ((str.length() > 0) && ((str.indexOf(',') >= 0) || (str.indexOf('\n') >= 0) || (str.indexOf('\r') >= 0) || (str.indexOf(0) >= 0) || (str.charAt(0) == '"')))
        {
          localStringBuffer.append('"');
          int j = str.length();
          for (int k = 0; k < j; k++)
          {
            char c = str.charAt(k);
            if ((c >= ' ') && (c != '"')) {
              localStringBuffer.append(c);
            }
          }
          localStringBuffer.append('"');
        }
        else
        {
          localStringBuffer.append(str);
        }
      }
    }
    localStringBuffer.append('\n');
    return localStringBuffer.toString();
  }
  
  public static JSONArray toJSONArray(String paramString)
    throws JSONException
  {
    return toJSONArray(new JSONTokener(paramString));
  }
  
  public static JSONArray toJSONArray(JSONTokener paramJSONTokener)
    throws JSONException
  {
    return toJSONArray(rowToJSONArray(paramJSONTokener), paramJSONTokener);
  }
  
  public static JSONArray toJSONArray(JSONArray paramJSONArray, String paramString)
    throws JSONException
  {
    return toJSONArray(paramJSONArray, new JSONTokener(paramString));
  }
  
  public static JSONArray toJSONArray(JSONArray paramJSONArray, JSONTokener paramJSONTokener)
    throws JSONException
  {
    if ((paramJSONArray == null) || (paramJSONArray.length() == 0)) {
      return null;
    }
    JSONArray localJSONArray = new JSONArray();
    for (;;)
    {
      JSONObject localJSONObject = rowToJSONObject(paramJSONArray, paramJSONTokener);
      if (localJSONObject == null) {
        break;
      }
      localJSONArray.put(localJSONObject);
    }
    if (localJSONArray.length() == 0) {
      return null;
    }
    return localJSONArray;
  }
  
  public static String toString(JSONArray paramJSONArray)
    throws JSONException
  {
    JSONObject localJSONObject = paramJSONArray.optJSONObject(0);
    if (localJSONObject != null)
    {
      JSONArray localJSONArray = localJSONObject.names();
      if (localJSONArray != null) {
        return rowToString(localJSONArray) + toString(localJSONArray, paramJSONArray);
      }
    }
    return null;
  }
  
  public static String toString(JSONArray paramJSONArray1, JSONArray paramJSONArray2)
    throws JSONException
  {
    if ((paramJSONArray1 == null) || (paramJSONArray1.length() == 0)) {
      return null;
    }
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramJSONArray2.length(); i++)
    {
      JSONObject localJSONObject = paramJSONArray2.optJSONObject(i);
      if (localJSONObject != null) {
        localStringBuffer.append(rowToString(localJSONObject.toJSONArray(paramJSONArray1)));
      }
    }
    return localStringBuffer.toString();
  }
}
