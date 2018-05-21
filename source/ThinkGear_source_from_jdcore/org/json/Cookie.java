package org.json;

public class Cookie
{
  public Cookie() {}
  
  public static String escape(String paramString)
  {
    String str = paramString.trim();
    StringBuffer localStringBuffer = new StringBuffer();
    int i = str.length();
    for (int j = 0; j < i; j++)
    {
      char c = str.charAt(j);
      if ((c < ' ') || (c == '+') || (c == '%') || (c == '=') || (c == ';'))
      {
        localStringBuffer.append('%');
        localStringBuffer.append(Character.forDigit((char)(c >>> '\004' & 0xF), 16));
        localStringBuffer.append(Character.forDigit((char)(c & 0xF), 16));
      }
      else
      {
        localStringBuffer.append(c);
      }
    }
    return localStringBuffer.toString();
  }
  
  public static JSONObject toJSONObject(String paramString)
    throws JSONException
  {
    JSONObject localJSONObject = new JSONObject();
    JSONTokener localJSONTokener = new JSONTokener(paramString);
    localJSONObject.put("name", localJSONTokener.nextTo('='));
    localJSONTokener.next('=');
    localJSONObject.put("value", localJSONTokener.nextTo(';'));
    localJSONTokener.next();
    while (localJSONTokener.more())
    {
      String str = unescape(localJSONTokener.nextTo("=;"));
      Object localObject;
      if (localJSONTokener.next() != '=')
      {
        if (str.equals("secure")) {
          localObject = Boolean.TRUE;
        } else {
          throw localJSONTokener.syntaxError("Missing '=' in cookie parameter.");
        }
      }
      else
      {
        localObject = unescape(localJSONTokener.nextTo(';'));
        localJSONTokener.next();
      }
      localJSONObject.put(str, localObject);
    }
    return localJSONObject;
  }
  
  public static String toString(JSONObject paramJSONObject)
    throws JSONException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(escape(paramJSONObject.getString("name")));
    localStringBuffer.append("=");
    localStringBuffer.append(escape(paramJSONObject.getString("value")));
    if (paramJSONObject.has("expires"))
    {
      localStringBuffer.append(";expires=");
      localStringBuffer.append(paramJSONObject.getString("expires"));
    }
    if (paramJSONObject.has("domain"))
    {
      localStringBuffer.append(";domain=");
      localStringBuffer.append(escape(paramJSONObject.getString("domain")));
    }
    if (paramJSONObject.has("path"))
    {
      localStringBuffer.append(";path=");
      localStringBuffer.append(escape(paramJSONObject.getString("path")));
    }
    if (paramJSONObject.optBoolean("secure")) {
      localStringBuffer.append(";secure");
    }
    return localStringBuffer.toString();
  }
  
  public static String unescape(String paramString)
  {
    int i = paramString.length();
    StringBuffer localStringBuffer = new StringBuffer();
    for (int j = 0; j < i; j++)
    {
      char c = paramString.charAt(j);
      if (c == '+')
      {
        c = ' ';
      }
      else if ((c == '%') && (j + 2 < i))
      {
        int k = JSONTokener.dehexchar(paramString.charAt(j + 1));
        int m = JSONTokener.dehexchar(paramString.charAt(j + 2));
        if ((k >= 0) && (m >= 0))
        {
          c = (char)(k * 16 + m);
          j += 2;
        }
      }
      localStringBuffer.append(c);
    }
    return localStringBuffer.toString();
  }
}
