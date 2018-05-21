package org.json;

public class HTTPTokener
  extends JSONTokener
{
  public HTTPTokener(String paramString)
  {
    super(paramString);
  }
  
  public String nextToken()
    throws JSONException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    char c1;
    do
    {
      c1 = next();
    } while (Character.isWhitespace(c1));
    if ((c1 == '"') || (c1 == '\''))
    {
      char c2 = c1;
      for (;;)
      {
        c1 = next();
        if (c1 < ' ') {
          throw syntaxError("Unterminated string.");
        }
        if (c1 == c2) {
          return localStringBuffer.toString();
        }
        localStringBuffer.append(c1);
      }
    }
    for (;;)
    {
      if ((c1 == 0) || (Character.isWhitespace(c1))) {
        return localStringBuffer.toString();
      }
      localStringBuffer.append(c1);
      c1 = next();
    }
  }
}
