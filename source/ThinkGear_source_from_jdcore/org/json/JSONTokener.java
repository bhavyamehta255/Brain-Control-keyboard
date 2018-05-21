package org.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

public class JSONTokener
{
  private int character;
  private boolean eof;
  private int index;
  private int line;
  private char previous;
  private Reader reader;
  private boolean usePrevious;
  
  public JSONTokener(Reader paramReader)
  {
    reader = (paramReader.markSupported() ? paramReader : new BufferedReader(paramReader));
    eof = false;
    usePrevious = false;
    previous = '\000';
    index = 0;
    character = 1;
    line = 1;
  }
  
  public JSONTokener(InputStream paramInputStream)
    throws JSONException
  {
    this(new InputStreamReader(paramInputStream));
  }
  
  public JSONTokener(String paramString)
  {
    this(new StringReader(paramString));
  }
  
  public void back()
    throws JSONException
  {
    if ((usePrevious) || (index <= 0)) {
      throw new JSONException("Stepping back two steps is not supported");
    }
    index -= 1;
    character -= 1;
    usePrevious = true;
    eof = false;
  }
  
  public static int dehexchar(char paramChar)
  {
    if ((paramChar >= '0') && (paramChar <= '9')) {
      return paramChar - '0';
    }
    if ((paramChar >= 'A') && (paramChar <= 'F')) {
      return paramChar - '7';
    }
    if ((paramChar >= 'a') && (paramChar <= 'f')) {
      return paramChar - 'W';
    }
    return -1;
  }
  
  public boolean end()
  {
    return (eof) && (!usePrevious);
  }
  
  public boolean more()
    throws JSONException
  {
    next();
    if (end()) {
      return false;
    }
    back();
    return true;
  }
  
  public char next()
    throws JSONException
  {
    int i;
    if (usePrevious)
    {
      usePrevious = false;
      i = previous;
    }
    else
    {
      try
      {
        i = reader.read();
      }
      catch (IOException localIOException)
      {
        throw new JSONException(localIOException);
      }
      if (i <= 0)
      {
        eof = true;
        i = 0;
      }
    }
    index += 1;
    if (previous == '\r')
    {
      line += 1;
      character = (i == 10 ? 0 : 1);
    }
    else if (i == 10)
    {
      line += 1;
      character = 0;
    }
    else
    {
      character += 1;
    }
    previous = ((char)i);
    return previous;
  }
  
  public char next(char paramChar)
    throws JSONException
  {
    char c = next();
    if (c != paramChar) {
      throw syntaxError("Expected '" + paramChar + "' and instead saw '" + c + "'");
    }
    return c;
  }
  
  public String next(int paramInt)
    throws JSONException
  {
    if (paramInt == 0) {
      return "";
    }
    char[] arrayOfChar = new char[paramInt];
    for (int i = 0; i < paramInt; i++)
    {
      arrayOfChar[i] = next();
      if (end()) {
        throw syntaxError("Substring bounds error");
      }
    }
    return new String(arrayOfChar);
  }
  
  public char nextClean()
    throws JSONException
  {
    for (;;)
    {
      char c = next();
      if ((c == 0) || (c > ' ')) {
        return c;
      }
    }
  }
  
  public String nextString(char paramChar)
    throws JSONException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (;;)
    {
      char c = next();
      switch (c)
      {
      case '\000': 
      case '\n': 
      case '\r': 
        throw syntaxError("Unterminated string");
      case '\\': 
        c = next();
        switch (c)
        {
        case 'b': 
          localStringBuffer.append('\b');
          break;
        case 't': 
          localStringBuffer.append('\t');
          break;
        case 'n': 
          localStringBuffer.append('\n');
          break;
        case 'f': 
          localStringBuffer.append('\f');
          break;
        case 'r': 
          localStringBuffer.append('\r');
          break;
        case 'u': 
          localStringBuffer.append((char)Integer.parseInt(next(4), 16));
          break;
        case '"': 
        case '\'': 
        case '/': 
        case '\\': 
          localStringBuffer.append(c);
          break;
        default: 
          throw syntaxError("Illegal escape.");
        }
        break;
      default: 
        if (c == paramChar) {
          return localStringBuffer.toString();
        }
        localStringBuffer.append(c);
      }
    }
  }
  
  public String nextTo(char paramChar)
    throws JSONException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (;;)
    {
      char c = next();
      if ((c == paramChar) || (c == 0) || (c == '\n') || (c == '\r'))
      {
        if (c != 0) {
          back();
        }
        return localStringBuffer.toString().trim();
      }
      localStringBuffer.append(c);
    }
  }
  
  public String nextTo(String paramString)
    throws JSONException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (;;)
    {
      char c = next();
      if ((paramString.indexOf(c) >= 0) || (c == 0) || (c == '\n') || (c == '\r'))
      {
        if (c != 0) {
          back();
        }
        return localStringBuffer.toString().trim();
      }
      localStringBuffer.append(c);
    }
  }
  
  public Object nextValue()
    throws JSONException
  {
    char c = nextClean();
    switch (c)
    {
    case '"': 
    case '\'': 
      return nextString(c);
    case '{': 
      back();
      return new JSONObject(this);
    case '[': 
      back();
      return new JSONArray(this);
    }
    StringBuffer localStringBuffer = new StringBuffer();
    while ((c >= ' ') && (",:]}/\\\"[{;=#".indexOf(c) < 0))
    {
      localStringBuffer.append(c);
      c = next();
    }
    back();
    String str = localStringBuffer.toString().trim();
    if (str.equals("")) {
      throw syntaxError("Missing value");
    }
    return JSONObject.stringToValue(str);
  }
  
  public char skipTo(char paramChar)
    throws JSONException
  {
    char c;
    try
    {
      int i = index;
      int j = character;
      int k = line;
      reader.mark(Integer.MAX_VALUE);
      do
      {
        c = next();
        if (c == 0)
        {
          reader.reset();
          index = i;
          character = j;
          line = k;
          return c;
        }
      } while (c != paramChar);
    }
    catch (IOException localIOException)
    {
      throw new JSONException(localIOException);
    }
    back();
    return c;
  }
  
  public JSONException syntaxError(String paramString)
  {
    return new JSONException(paramString + toString());
  }
  
  public String toString()
  {
    return " at " + index + " [character " + character + " line " + line + "]";
  }
}
