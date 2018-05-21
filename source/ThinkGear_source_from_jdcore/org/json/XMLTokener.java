package org.json;

import java.util.HashMap;

public class XMLTokener
  extends JSONTokener
{
  public static final HashMap entity = new HashMap(8);
  
  public XMLTokener(String paramString)
  {
    super(paramString);
  }
  
  public String nextCDATA()
    throws JSONException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i;
    do
    {
      char c = next();
      if (end()) {
        throw syntaxError("Unclosed CDATA");
      }
      localStringBuffer.append(c);
      i = localStringBuffer.length() - 3;
    } while ((i < 0) || (localStringBuffer.charAt(i) != ']') || (localStringBuffer.charAt(i + 1) != ']') || (localStringBuffer.charAt(i + 2) != '>'));
    localStringBuffer.setLength(i);
    return localStringBuffer.toString();
  }
  
  public Object nextContent()
    throws JSONException
  {
    char c;
    do
    {
      c = next();
    } while (Character.isWhitespace(c));
    if (c == 0) {
      return null;
    }
    if (c == '<') {
      return XML.LT;
    }
    StringBuffer localStringBuffer = new StringBuffer();
    for (;;)
    {
      if ((c == '<') || (c == 0))
      {
        back();
        return localStringBuffer.toString().trim();
      }
      if (c == '&') {
        localStringBuffer.append(nextEntity(c));
      } else {
        localStringBuffer.append(c);
      }
      c = next();
    }
  }
  
  public Object nextEntity(char paramChar)
    throws JSONException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (;;)
    {
      char c = next();
      if ((Character.isLetterOrDigit(c)) || (c == '#'))
      {
        localStringBuffer.append(Character.toLowerCase(c));
      }
      else
      {
        if (c == ';') {
          break;
        }
        throw syntaxError("Missing ';' in XML entity: &" + localStringBuffer);
      }
    }
    String str = localStringBuffer.toString();
    Object localObject = entity.get(str);
    return paramChar + str + ";";
  }
  
  public Object nextMeta()
    throws JSONException
  {
    char c1;
    do
    {
      c1 = next();
    } while (Character.isWhitespace(c1));
    switch (c1)
    {
    case '\000': 
      throw syntaxError("Misshaped meta tag");
    case '<': 
      return XML.LT;
    case '>': 
      return XML.GT;
    case '/': 
      return XML.SLASH;
    case '=': 
      return XML.EQ;
    case '!': 
      return XML.BANG;
    case '?': 
      return XML.QUEST;
    case '"': 
    case '\'': 
      char c2 = c1;
      do
      {
        c1 = next();
        if (c1 == 0) {
          throw syntaxError("Unterminated string");
        }
      } while (c1 != c2);
      return Boolean.TRUE;
    }
    for (;;)
    {
      c1 = next();
      if (Character.isWhitespace(c1)) {
        return Boolean.TRUE;
      }
      switch (c1)
      {
      case '\000': 
      case '!': 
      case '"': 
      case '\'': 
      case '/': 
      case '<': 
      case '=': 
      case '>': 
      case '?': 
        back();
        return Boolean.TRUE;
      }
    }
  }
  
  public Object nextToken()
    throws JSONException
  {
    char c1;
    do
    {
      c1 = next();
    } while (Character.isWhitespace(c1));
    switch (c1)
    {
    case '\000': 
      throw syntaxError("Misshaped element");
    case '<': 
      throw syntaxError("Misplaced '<'");
    case '>': 
      return XML.GT;
    case '/': 
      return XML.SLASH;
    case '=': 
      return XML.EQ;
    case '!': 
      return XML.BANG;
    case '?': 
      return XML.QUEST;
    case '"': 
    case '\'': 
      char c2 = c1;
      localStringBuffer = new StringBuffer();
      for (;;)
      {
        c1 = next();
        if (c1 == 0) {
          throw syntaxError("Unterminated string");
        }
        if (c1 == c2) {
          return localStringBuffer.toString();
        }
        if (c1 == '&') {
          localStringBuffer.append(nextEntity(c1));
        } else {
          localStringBuffer.append(c1);
        }
      }
    }
    StringBuffer localStringBuffer = new StringBuffer();
    for (;;)
    {
      localStringBuffer.append(c1);
      c1 = next();
      if (Character.isWhitespace(c1)) {
        return localStringBuffer.toString();
      }
      switch (c1)
      {
      case '\000': 
        return localStringBuffer.toString();
      case '!': 
      case '/': 
      case '=': 
      case '>': 
      case '?': 
      case '[': 
      case ']': 
        back();
        return localStringBuffer.toString();
      case '"': 
      case '\'': 
      case '<': 
        throw syntaxError("Bad character in a name");
      }
    }
  }
  
  public boolean skipPast(String paramString)
    throws JSONException
  {
    int n = 0;
    int i1 = paramString.length();
    char[] arrayOfChar = new char[i1];
    int j;
    for (int k = 0; k < i1; k++)
    {
      j = next();
      if (j == 0) {
        return false;
      }
      arrayOfChar[k] = j;
    }
    for (;;)
    {
      int m = n;
      int i = 1;
      for (k = 0; k < i1; k++)
      {
        if (arrayOfChar[m] != paramString.charAt(k))
        {
          i = 0;
          break;
        }
        m++;
        if (m >= i1) {
          m -= i1;
        }
      }
      if (i != 0) {
        return true;
      }
      j = next();
      if (j == 0) {
        return false;
      }
      arrayOfChar[n] = j;
      n++;
      if (n >= i1) {
        n -= i1;
      }
    }
  }
  
  static
  {
    entity.put("amp", XML.AMP);
    entity.put("apos", XML.APOS);
    entity.put("gt", XML.GT);
    entity.put("lt", XML.LT);
    entity.put("quot", XML.QUOT);
  }
}
