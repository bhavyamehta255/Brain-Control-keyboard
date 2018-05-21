package org.json;

import java.io.IOException;
import java.io.Writer;

public class JSONWriter
{
  private static final int maxdepth = 20;
  private boolean comma = false;
  protected char mode = 'i';
  private JSONObject[] stack = new JSONObject[20];
  private int top = 0;
  protected Writer writer;
  
  public JSONWriter(Writer paramWriter)
  {
    writer = paramWriter;
  }
  
  private JSONWriter append(String paramString)
    throws JSONException
  {
    if (paramString == null) {
      throw new JSONException("Null pointer");
    }
    if ((mode == 'o') || (mode == 'a'))
    {
      try
      {
        if ((comma) && (mode == 'a')) {
          writer.write(44);
        }
        writer.write(paramString);
      }
      catch (IOException localIOException)
      {
        throw new JSONException(localIOException);
      }
      if (mode == 'o') {
        mode = 'k';
      }
      comma = true;
      return this;
    }
    throw new JSONException("Value out of sequence.");
  }
  
  public JSONWriter array()
    throws JSONException
  {
    if ((mode == 'i') || (mode == 'o') || (mode == 'a'))
    {
      push(null);
      append("[");
      comma = false;
      return this;
    }
    throw new JSONException("Misplaced array.");
  }
  
  private JSONWriter end(char paramChar1, char paramChar2)
    throws JSONException
  {
    if (mode != paramChar1) {
      throw new JSONException(paramChar1 == 'a' ? "Misplaced endArray." : "Misplaced endObject.");
    }
    pop(paramChar1);
    try
    {
      writer.write(paramChar2);
    }
    catch (IOException localIOException)
    {
      throw new JSONException(localIOException);
    }
    comma = true;
    return this;
  }
  
  public JSONWriter endArray()
    throws JSONException
  {
    return end('a', ']');
  }
  
  public JSONWriter endObject()
    throws JSONException
  {
    return end('k', '}');
  }
  
  public JSONWriter key(String paramString)
    throws JSONException
  {
    if (paramString == null) {
      throw new JSONException("Null key.");
    }
    if (mode == 'k') {
      try
      {
        stack[(top - 1)].putOnce(paramString, Boolean.TRUE);
        if (comma) {
          writer.write(44);
        }
        writer.write(JSONObject.quote(paramString));
        writer.write(58);
        comma = false;
        mode = 'o';
        return this;
      }
      catch (IOException localIOException)
      {
        throw new JSONException(localIOException);
      }
    }
    throw new JSONException("Misplaced key.");
  }
  
  public JSONWriter object()
    throws JSONException
  {
    if (mode == 'i') {
      mode = 'o';
    }
    if ((mode == 'o') || (mode == 'a'))
    {
      append("{");
      push(new JSONObject());
      comma = false;
      return this;
    }
    throw new JSONException("Misplaced object.");
  }
  
  private void pop(char paramChar)
    throws JSONException
  {
    if (top <= 0) {
      throw new JSONException("Nesting error.");
    }
    char c = stack[(top - 1)] == null ? 'a' : 'k';
    if (c != paramChar) {
      throw new JSONException("Nesting error.");
    }
    top -= 1;
    mode = (stack[(top - 1)] == null ? 'a' : top == 0 ? 'd' : 'k');
  }
  
  private void push(JSONObject paramJSONObject)
    throws JSONException
  {
    if (top >= 20) {
      throw new JSONException("Nesting too deep.");
    }
    stack[top] = paramJSONObject;
    mode = (paramJSONObject == null ? 'a' : 'k');
    top += 1;
  }
  
  public JSONWriter value(boolean paramBoolean)
    throws JSONException
  {
    return append(paramBoolean ? "true" : "false");
  }
  
  public JSONWriter value(double paramDouble)
    throws JSONException
  {
    return value(new Double(paramDouble));
  }
  
  public JSONWriter value(long paramLong)
    throws JSONException
  {
    return append(Long.toString(paramLong));
  }
  
  public JSONWriter value(Object paramObject)
    throws JSONException
  {
    return append(JSONObject.valueToString(paramObject));
  }
}
