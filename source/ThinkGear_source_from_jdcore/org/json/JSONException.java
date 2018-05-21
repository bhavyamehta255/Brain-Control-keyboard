package org.json;

public class JSONException
  extends Exception
{
  private static final long serialVersionUID = 0L;
  private Throwable cause;
  
  public JSONException(String paramString)
  {
    super(paramString);
  }
  
  public JSONException(Throwable paramThrowable)
  {
    super(paramThrowable.getMessage());
    cause = paramThrowable;
  }
  
  public Throwable getCause()
  {
    return cause;
  }
}
