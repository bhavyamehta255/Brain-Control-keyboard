package neurosky;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;
import processing.core.PApplet;

public class ThinkGearSocket
  implements Runnable
{
  public PApplet parent;
  public Socket neuroSocket;
  public OutputStream outStream;
  public InputStream inStream;
  public BufferedReader stdIn;
  private Method attentionEventMethod = null;
  private Method meditationEventMethod = null;
  private Method poorSignalEventMethod = null;
  private Method blinkEventMethod = null;
  private Method eegEventMethod = null;
  private Method rawEventMethod = null;
  public String appName = "";
  public String appKey = "";
  private Thread t;
  private int[] raw = new int['Ȁ'];
  private int index = 0;
  public static final String VERSION = "1.0";
  private boolean running = true;
  
  public ThinkGearSocket(PApplet paramPApplet, String paramString1, String paramString2)
  {
    this(paramPApplet);
    appName = paramString1;
    appKey = paramString2;
  }
  
  public ThinkGearSocket(PApplet paramPApplet)
  {
    parent = paramPApplet;
    try
    {
      attentionEventMethod = parent.getClass().getMethod("attentionEvent", new Class[] { Integer.TYPE });
    }
    catch (Exception localException1)
    {
      System.err.println("attentionEvent() method not defined. ");
    }
    try
    {
      meditationEventMethod = parent.getClass().getMethod("meditationEvent", new Class[] { Integer.TYPE });
    }
    catch (Exception localException2)
    {
      System.err.println("meditationEvent() method not defined. ");
    }
    try
    {
      poorSignalEventMethod = parent.getClass().getMethod("poorSignalEvent", new Class[] { Integer.TYPE });
    }
    catch (Exception localException3)
    {
      System.err.println("poorSignalEvent() method not defined. ");
    }
    try
    {
      blinkEventMethod = parent.getClass().getMethod("blinkEvent", new Class[] { Integer.TYPE });
    }
    catch (Exception localException4)
    {
      System.err.println("blinkEvent() method not defined. ");
    }
    try
    {
      eegEventMethod = parent.getClass().getMethod("eegEvent", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE });
    }
    catch (Exception localException5)
    {
      System.err.println("eegEvent() method not defined. ");
    }
    try
    {
      rawEventMethod = parent.getClass().getMethod("rawEvent", new Class[] { [I.class });
    }
    catch (Exception localException6)
    {
      System.err.println("rawEvent() method not defined. ");
    }
  }
  
  public boolean isRunning()
  {
    return running;
  }
  
  public static String version()
  {
    return "1.0";
  }
  
  public void start()
    throws ConnectException
  {
    try
    {
      neuroSocket = new Socket("127.0.0.1", 13854);
    }
    catch (ConnectException localConnectException)
    {
      System.out.println("Oi plonker! Is ThinkkGear running?");
      running = false;
      throw localConnectException;
    }
    catch (UnknownHostException localUnknownHostException)
    {
      localUnknownHostException.printStackTrace();
    }
    catch (IOException localIOException1)
    {
      localIOException1.printStackTrace();
    }
    try
    {
      inStream = neuroSocket.getInputStream();
      outStream = neuroSocket.getOutputStream();
      stdIn = new BufferedReader(new InputStreamReader(neuroSocket.getInputStream()));
      running = true;
    }
    catch (IOException localIOException2)
    {
      localIOException2.printStackTrace();
    }
    if ((appName != "") && (appKey != ""))
    {
      localJSONObject = new JSONObject();
      try
      {
        localJSONObject.put("appName", appName);
      }
      catch (JSONException localJSONException1)
      {
        localJSONException1.printStackTrace();
      }
      try
      {
        localJSONObject.put("appKey", appKey);
      }
      catch (JSONException localJSONException2)
      {
        localJSONException2.printStackTrace();
      }
      sendMessage(localJSONObject.toString());
      System.out.println("appAuth" + localJSONObject);
    }
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("enableRawOutput", true);
    }
    catch (JSONException localJSONException3)
    {
      System.out.println("raw error");
      localJSONException3.printStackTrace();
    }
    try
    {
      localJSONObject.put("format", "Json");
    }
    catch (JSONException localJSONException4)
    {
      System.out.println("Json error");
      localJSONException4.printStackTrace();
    }
    sendMessage(localJSONObject.toString());
    t = new Thread(this);
    t.start();
  }
  
  public void stop()
  {
    if (running)
    {
      t.interrupt();
      try
      {
        neuroSocket.close();
        inStream.close();
        outStream.close();
        stdIn.close();
        stdIn = null;
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
    }
    running = false;
  }
  
  public void sendMessage(String paramString)
  {
    PrintWriter localPrintWriter = new PrintWriter(outStream, true);
    localPrintWriter.println(paramString);
  }
  
  public void run()
  {
    if ((running) && (neuroSocket.isConnected()))
    {
      try
      {
        String str;
        while ((str = stdIn.readLine()) != null)
        {
          String[] arrayOfString = str.split("/\r/");
          for (int i = 0; i < arrayOfString.length; i++) {
            if (arrayOfString[i].indexOf("{") > -1)
            {
              JSONObject localJSONObject = new JSONObject(arrayOfString[i]);
              parsePacket(localJSONObject);
            }
          }
        }
      }
      catch (SocketException localSocketException) {}catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
      catch (JSONException localJSONException) {}
      parent.delay(50);
    }
    else
    {
      running = false;
    }
  }
  
  private void triggerAttentionEvent(int paramInt)
  {
    if (attentionEventMethod != null) {
      try
      {
        attentionEventMethod.invoke(parent, new Object[] { Integer.valueOf(paramInt) });
      }
      catch (Exception localException)
      {
        System.err.println("Disabling attentionEvent()  because of an error.");
        localException.printStackTrace();
        attentionEventMethod = null;
      }
    }
  }
  
  private void triggerMeditationEvent(int paramInt)
  {
    if (meditationEventMethod != null) {
      try
      {
        meditationEventMethod.invoke(parent, new Object[] { Integer.valueOf(paramInt) });
      }
      catch (Exception localException)
      {
        System.err.println("Disabling meditationEvent()  because of an error.");
        localException.printStackTrace();
        meditationEventMethod = null;
      }
    }
  }
  
  private void triggerPoorSignalEvent(int paramInt)
  {
    if (poorSignalEventMethod != null) {
      try
      {
        poorSignalEventMethod.invoke(parent, new Object[] { Integer.valueOf(paramInt) });
      }
      catch (Exception localException)
      {
        System.err.println("Disabling meditationEvent()  because of an error.");
        localException.printStackTrace();
        poorSignalEventMethod = null;
      }
    }
  }
  
  private void triggerBlinkEvent(int paramInt)
  {
    if (blinkEventMethod != null) {
      try
      {
        blinkEventMethod.invoke(parent, new Object[] { Integer.valueOf(paramInt) });
      }
      catch (Exception localException)
      {
        System.err.println("Disabling blinkEvent()  because of an error.");
        localException.printStackTrace();
        blinkEventMethod = null;
      }
    }
  }
  
  private void triggerEEGEvent(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    if (eegEventMethod != null) {
      try
      {
        eegEventMethod.invoke(parent, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3), Integer.valueOf(paramInt4), Integer.valueOf(paramInt5), Integer.valueOf(paramInt6), Integer.valueOf(paramInt7), Integer.valueOf(paramInt8) });
      }
      catch (Exception localException)
      {
        System.err.println("Disabling eegEvent()  because of an error.");
        localException.printStackTrace();
        eegEventMethod = null;
      }
    }
  }
  
  private void triggerRawEvent(int[] paramArrayOfInt)
  {
    if (rawEventMethod != null) {
      try
      {
        rawEventMethod.invoke(parent, new Object[] { paramArrayOfInt });
      }
      catch (Exception localException)
      {
        System.err.println("Disabling rawEvent()  because of an error.");
        localException.printStackTrace();
        rawEventMethod = null;
      }
    }
  }
  
  private void parsePacket(JSONObject paramJSONObject)
  {
    Iterator localIterator = paramJSONObject.keys();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      String str = localObject.toString();
      try
      {
        if (str.matches("poorSignalLevel")) {
          triggerPoorSignalEvent(paramJSONObject.getInt(localObject.toString()));
        }
        if (str.matches("rawEeg"))
        {
          int i = ((Integer)paramJSONObject.get("rawEeg")).intValue();
          raw[index] = i;
          index += 1;
          if (index == 512)
          {
            index = 0;
            int[] arrayOfInt = new int['Ȁ'];
            PApplet.arrayCopy(raw, arrayOfInt);
            triggerRawEvent(arrayOfInt);
          }
        }
        if (str.matches("blinkStrength")) {
          triggerBlinkEvent(paramJSONObject.getInt(localObject.toString()));
        }
        JSONObject localJSONObject;
        if (str.matches("eSense"))
        {
          localJSONObject = paramJSONObject.getJSONObject("eSense");
          triggerAttentionEvent(localJSONObject.getInt("attention"));
          triggerMeditationEvent(localJSONObject.getInt("meditation"));
        }
        if (str.matches("eegPower"))
        {
          localJSONObject = paramJSONObject.getJSONObject("eegPower");
          triggerEEGEvent(localJSONObject.getInt("delta"), localJSONObject.getInt("theta"), localJSONObject.getInt("lowAlpha"), localJSONObject.getInt("highAlpha"), localJSONObject.getInt("lowBeta"), localJSONObject.getInt("highBeta"), localJSONObject.getInt("lowGamma"), localJSONObject.getInt("highGamma"));
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }
}
