package processing.core;

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import processing.xml.XMLElement;

public class PApplet
  extends Applet
  implements PConstants, Runnable, MouseListener, MouseMotionListener, KeyListener, FocusListener
{
  public static final String javaVersionName = System.getProperty("java.version");
  public static final float javaVersion = new Float(javaVersionName.substring(0, 3)).floatValue();
  public static int platform;
  public static boolean useQuartz = true;
  public static final int MENU_SHORTCUT = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
  public PGraphics g;
  public Frame frame;
  public int screenWidth;
  public int screenHeight;
  /**
   * @deprecated
   */
  public Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
  public PGraphics recorder;
  public String[] args;
  public String sketchPath;
  static final boolean THREAD_DEBUG = false;
  public static final int DEFAULT_WIDTH = 100;
  public static final int DEFAULT_HEIGHT = 100;
  public static final int MIN_WINDOW_WIDTH = 128;
  public static final int MIN_WINDOW_HEIGHT = 128;
  public boolean defaultSize;
  volatile boolean resizeRequest;
  volatile int resizeWidth;
  volatile int resizeHeight;
  public int[] pixels;
  public int width;
  public int height;
  public int mouseX;
  public int mouseY;
  public int pmouseX;
  public int pmouseY;
  protected int dmouseX;
  protected int dmouseY;
  protected int emouseX;
  protected int emouseY;
  public boolean firstMouse;
  public int mouseButton;
  public boolean mousePressed;
  public MouseEvent mouseEvent;
  public char key;
  public int keyCode;
  public boolean keyPressed;
  public KeyEvent keyEvent;
  public boolean focused = false;
  public boolean online = false;
  long millisOffset = System.currentTimeMillis();
  public float frameRate = 10.0F;
  protected long frameRateLastNanos = 0L;
  protected float frameRateTarget = 60.0F;
  protected long frameRatePeriod = 16666666L;
  protected boolean looping;
  protected boolean redraw;
  public int frameCount;
  public volatile boolean finished;
  public volatile boolean paused;
  protected boolean exitCalled;
  Thread thread;
  protected RegisteredMethods sizeMethods;
  protected RegisteredMethods preMethods;
  protected RegisteredMethods drawMethods;
  protected RegisteredMethods postMethods;
  protected RegisteredMethods mouseEventMethods;
  protected RegisteredMethods keyEventMethods;
  protected RegisteredMethods disposeMethods;
  public static final String ARGS_EDITOR_LOCATION = "--editor-location";
  public static final String ARGS_EXTERNAL = "--external";
  public static final String ARGS_LOCATION = "--location";
  public static final String ARGS_DISPLAY = "--display";
  public static final String ARGS_BGCOLOR = "--bgcolor";
  public static final String ARGS_PRESENT = "--present";
  public static final String ARGS_EXCLUSIVE = "--exclusive";
  public static final String ARGS_STOP_COLOR = "--stop-color";
  public static final String ARGS_HIDE_STOP = "--hide-stop";
  public static final String ARGS_SKETCH_FOLDER = "--sketch-path";
  public static final String EXTERNAL_STOP = "__STOP__";
  public static final String EXTERNAL_MOVE = "__MOVE__";
  boolean external = false;
  static final String ERROR_MIN_MAX = "Cannot use min() or max() on an empty array.";
  MouseEvent[] mouseEventQueue = new MouseEvent[10];
  int mouseEventCount;
  KeyEvent[] keyEventQueue = new KeyEvent[10];
  int keyEventCount;
  static String openLauncher;
  int cursorType = 0;
  boolean cursorVisible = true;
  PImage invisibleCursor;
  Random internalRandom;
  static final int PERLIN_YWRAPB = 4;
  static final int PERLIN_YWRAP = 16;
  static final int PERLIN_ZWRAPB = 8;
  static final int PERLIN_ZWRAP = 256;
  static final int PERLIN_SIZE = 4095;
  int perlin_octaves = 4;
  float perlin_amp_falloff = 0.5F;
  int perlin_TWOPI;
  int perlin_PI;
  float[] perlin_cosTable;
  float[] perlin;
  Random perlinRandom;
  protected String[] loadImageFormats;
  public int requestImageMax = 4;
  volatile int requestImageCount;
  protected String[] loadShapeFormats;
  public File selectedFile;
  protected Frame parentFrame;
  protected static HashMap<String, Pattern> matchPatterns;
  private static NumberFormat int_nf;
  private static int int_nf_digits;
  private static boolean int_nf_commas;
  private static NumberFormat float_nf;
  private static int float_nf_left;
  private static int float_nf_right;
  private static boolean float_nf_commas;
  public static final byte[] ICON_IMAGE = { 71, 73, 70, 56, 57, 97, 16, 0, 16, 0, -77, 0, 0, 0, 0, 0, -1, -1, -1, 12, 12, 13, -15, -15, -14, 45, 57, 74, 54, 80, 111, 47, 71, 97, 62, 88, 117, 1, 14, 27, 7, 41, 73, 15, 52, 85, 2, 31, 55, 4, 54, 94, 18, 69, 109, 37, 87, 126, -1, -1, -1, 33, -7, 4, 1, 0, 0, 15, 0, 44, 0, 0, 0, 0, 16, 0, 16, 0, 0, 4, 122, -16, -107, 114, -86, -67, 83, 30, -42, 26, -17, -100, -45, 56, -57, -108, 48, 40, 122, -90, 104, 67, -91, -51, 32, -53, 77, -78, -100, 47, -86, 12, 76, -110, -20, -74, -101, 97, -93, 27, 40, 20, -65, 65, 48, -111, 99, -20, -112, -117, -123, -47, -105, 24, 114, -112, 74, 69, 84, 25, 93, 88, -75, 9, 46, 2, 49, 88, -116, -67, 7, -19, -83, 60, 38, 3, -34, 2, 66, -95, 27, -98, 13, 4, -17, 55, 33, 109, 11, 11, -2, Byte.MIN_VALUE, 121, 123, 62, 91, 120, Byte.MIN_VALUE, Byte.MAX_VALUE, 122, 115, 102, 2, 119, 0, -116, -113, -119, 6, 102, 121, -108, -126, 5, 18, 6, 4, -102, -101, -100, 114, 15, 17, 0, 59 };
  
  public PApplet() {}
  
  public void init()
  {
    Dimension localDimension1 = Toolkit.getDefaultToolkit().getScreenSize();
    screenWidth = width;
    screenHeight = height;
    setFocusTraversalKeysEnabled(false);
    finished = false;
    looping = true;
    redraw = true;
    firstMouse = true;
    sizeMethods = new RegisteredMethods();
    preMethods = new RegisteredMethods();
    drawMethods = new RegisteredMethods();
    postMethods = new RegisteredMethods();
    mouseEventMethods = new RegisteredMethods();
    keyEventMethods = new RegisteredMethods();
    disposeMethods = new RegisteredMethods();
    try
    {
      getAppletContext();
      online = true;
    }
    catch (NullPointerException localNullPointerException)
    {
      online = false;
    }
    try
    {
      if (sketchPath == null) {
        sketchPath = System.getProperty("user.dir");
      }
    }
    catch (Exception localException) {}
    Dimension localDimension2 = getSize();
    if ((width != 0) && (height != 0))
    {
      g = makeGraphics(width, height, sketchRenderer(), null, true);
    }
    else
    {
      defaultSize = true;
      int i = sketchWidth();
      int j = sketchHeight();
      g = makeGraphics(i, j, sketchRenderer(), null, true);
      setSize(i, j);
      setPreferredSize(new Dimension(i, j));
    }
    width = g.width;
    height = g.height;
    addListeners();
    start();
  }
  
  public int sketchWidth()
  {
    return 100;
  }
  
  public int sketchHeight()
  {
    return 100;
  }
  
  public String sketchRenderer()
  {
    return "processing.core.PGraphicsJava2D";
  }
  
  public void start()
  {
    finished = false;
    paused = false;
    if (thread == null)
    {
      thread = new Thread(this, "Animation Thread");
      thread.start();
    }
  }
  
  public void stop()
  {
    paused = true;
  }
  
  public void destroy()
  {
    exit();
  }
  
  public void registerSize(Object paramObject)
  {
    Class[] arrayOfClass = { Integer.TYPE, Integer.TYPE };
    registerWithArgs(sizeMethods, "size", paramObject, arrayOfClass);
  }
  
  public void registerPre(Object paramObject)
  {
    registerNoArgs(preMethods, "pre", paramObject);
  }
  
  public void registerDraw(Object paramObject)
  {
    registerNoArgs(drawMethods, "draw", paramObject);
  }
  
  public void registerPost(Object paramObject)
  {
    registerNoArgs(postMethods, "post", paramObject);
  }
  
  public void registerMouseEvent(Object paramObject)
  {
    Class[] arrayOfClass = { MouseEvent.class };
    registerWithArgs(mouseEventMethods, "mouseEvent", paramObject, arrayOfClass);
  }
  
  public void registerKeyEvent(Object paramObject)
  {
    Class[] arrayOfClass = { KeyEvent.class };
    registerWithArgs(keyEventMethods, "keyEvent", paramObject, arrayOfClass);
  }
  
  public void registerDispose(Object paramObject)
  {
    registerNoArgs(disposeMethods, "dispose", paramObject);
  }
  
  protected void registerNoArgs(RegisteredMethods paramRegisteredMethods, String paramString, Object paramObject)
  {
    Class localClass = paramObject.getClass();
    try
    {
      Method localMethod = localClass.getMethod(paramString, new Class[0]);
      paramRegisteredMethods.add(paramObject, localMethod);
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      die("There is no public " + paramString + "() method in the class " + paramObject.getClass().getName());
    }
    catch (Exception localException)
    {
      die("Could not register " + paramString + " + () for " + paramObject, localException);
    }
  }
  
  protected void registerWithArgs(RegisteredMethods paramRegisteredMethods, String paramString, Object paramObject, Class<?>[] paramArrayOfClass)
  {
    Class localClass = paramObject.getClass();
    try
    {
      Method localMethod = localClass.getMethod(paramString, paramArrayOfClass);
      paramRegisteredMethods.add(paramObject, localMethod);
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      die("There is no public " + paramString + "() method in the class " + paramObject.getClass().getName());
    }
    catch (Exception localException)
    {
      die("Could not register " + paramString + " + () for " + paramObject, localException);
    }
  }
  
  public void unregisterSize(Object paramObject)
  {
    Class[] arrayOfClass = { Integer.TYPE, Integer.TYPE };
    unregisterWithArgs(sizeMethods, "size", paramObject, arrayOfClass);
  }
  
  public void unregisterPre(Object paramObject)
  {
    unregisterNoArgs(preMethods, "pre", paramObject);
  }
  
  public void unregisterDraw(Object paramObject)
  {
    unregisterNoArgs(drawMethods, "draw", paramObject);
  }
  
  public void unregisterPost(Object paramObject)
  {
    unregisterNoArgs(postMethods, "post", paramObject);
  }
  
  public void unregisterMouseEvent(Object paramObject)
  {
    Class[] arrayOfClass = { MouseEvent.class };
    unregisterWithArgs(mouseEventMethods, "mouseEvent", paramObject, arrayOfClass);
  }
  
  public void unregisterKeyEvent(Object paramObject)
  {
    Class[] arrayOfClass = { KeyEvent.class };
    unregisterWithArgs(keyEventMethods, "keyEvent", paramObject, arrayOfClass);
  }
  
  public void unregisterDispose(Object paramObject)
  {
    unregisterNoArgs(disposeMethods, "dispose", paramObject);
  }
  
  protected void unregisterNoArgs(RegisteredMethods paramRegisteredMethods, String paramString, Object paramObject)
  {
    Class localClass = paramObject.getClass();
    try
    {
      Method localMethod = localClass.getMethod(paramString, new Class[0]);
      paramRegisteredMethods.remove(paramObject, localMethod);
    }
    catch (Exception localException)
    {
      die("Could not unregister " + paramString + "() for " + paramObject, localException);
    }
  }
  
  protected void unregisterWithArgs(RegisteredMethods paramRegisteredMethods, String paramString, Object paramObject, Class<?>[] paramArrayOfClass)
  {
    Class localClass = paramObject.getClass();
    try
    {
      Method localMethod = localClass.getMethod(paramString, paramArrayOfClass);
      paramRegisteredMethods.remove(paramObject, localMethod);
    }
    catch (Exception localException)
    {
      die("Could not unregister " + paramString + "() for " + paramObject, localException);
    }
  }
  
  public void setup() {}
  
  public void draw()
  {
    finished = true;
  }
  
  protected void resizeRenderer(int paramInt1, int paramInt2)
  {
    if ((width != paramInt1) || (height != paramInt2))
    {
      g.setSize(paramInt1, paramInt2);
      width = paramInt1;
      height = paramInt2;
    }
  }
  
  public void size(int paramInt1, int paramInt2)
  {
    size(paramInt1, paramInt2, "processing.core.PGraphicsJava2D", null);
  }
  
  public void size(int paramInt1, int paramInt2, String paramString)
  {
    size(paramInt1, paramInt2, paramString, null);
  }
  
  public void size(final int paramInt1, final int paramInt2, String paramString1, String paramString2)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        setPreferredSize(new Dimension(paramInt1, paramInt2));
        setSize(paramInt1, paramInt2);
      }
    });
    if (paramString2 != null) {
      paramString2 = savePath(paramString2);
    }
    String str = g.getClass().getName();
    if (str.equals(paramString1))
    {
      resizeRenderer(paramInt1, paramInt2);
    }
    else
    {
      g = makeGraphics(paramInt1, paramInt2, paramString1, paramString2, true);
      width = paramInt1;
      height = paramInt2;
      defaultSize = false;
      throw new RendererChangeException();
    }
  }
  
  public PGraphics createGraphics(int paramInt1, int paramInt2, String paramString)
  {
    PGraphics localPGraphics = makeGraphics(paramInt1, paramInt2, paramString, null, false);
    return localPGraphics;
  }
  
  public PGraphics createGraphics(int paramInt1, int paramInt2, String paramString1, String paramString2)
  {
    if (paramString2 != null) {
      paramString2 = savePath(paramString2);
    }
    PGraphics localPGraphics = makeGraphics(paramInt1, paramInt2, paramString1, paramString2, false);
    parent = this;
    return localPGraphics;
  }
  
  protected PGraphics makeGraphics(int paramInt1, int paramInt2, String paramString1, String paramString2, boolean paramBoolean)
  {
    if ((paramString1.equals("processing.opengl.PGraphicsOpenGL")) && (platform == 1))
    {
      str = System.getProperty("java.version");
      if ((str != null) && (str.equals("1.5.0_10")))
      {
        System.err.println("OpenGL support is broken with Java 1.5.0_10");
        System.err.println("See http://dev.processing.org/bugs/show_bug.cgi?id=513 for more info.");
        throw new RuntimeException("Please update your Java installation (see bug #513)");
      }
    }
    String str = "Before using OpenGL, first select Import Library > opengl from the Sketch menu.";
    try
    {
      Class localClass = Thread.currentThread().getContextClassLoader().loadClass(paramString1);
      localObject1 = localClass.getConstructor(new Class[0]);
      localObject2 = (PGraphics)((Constructor)localObject1).newInstance(new Object[0]);
      ((PGraphics)localObject2).setParent(this);
      ((PGraphics)localObject2).setPrimary(paramBoolean);
      if (paramString2 != null) {
        ((PGraphics)localObject2).setPath(paramString2);
      }
      ((PGraphics)localObject2).setSize(paramInt1, paramInt2);
      return localObject2;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      localObject1 = localInvocationTargetException.getTargetException().getMessage();
      if ((localObject1 != null) && (((String)localObject1).indexOf("no jogl in java.library.path") != -1)) {
        throw new RuntimeException(str + " (The native library is missing.)");
      }
      localInvocationTargetException.getTargetException().printStackTrace();
      Object localObject2 = localInvocationTargetException.getTargetException();
      if (platform == 2) {
        ((Throwable)localObject2).printStackTrace(System.out);
      }
      throw new RuntimeException(((Throwable)localObject2).getMessage());
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      if (localClassNotFoundException.getMessage().indexOf("processing.opengl.PGraphicsGL") != -1) {
        throw new RuntimeException(str + " (The library .jar file is missing.)");
      }
      throw new RuntimeException("You need to use \"Import Library\" to add " + paramString1 + " to your sketch.");
    }
    catch (Exception localException)
    {
      Object localObject1;
      if (((localException instanceof IllegalArgumentException)) || ((localException instanceof NoSuchMethodException)) || ((localException instanceof IllegalAccessException)))
      {
        localException.printStackTrace();
        localObject1 = paramString1 + " needs to be updated " + "for the current release of Processing.";
        throw new RuntimeException((String)localObject1);
      }
      if (platform == 2) {
        localException.printStackTrace(System.out);
      }
      throw new RuntimeException(localException.getMessage());
    }
  }
  
  public PImage createImage(int paramInt1, int paramInt2, int paramInt3)
  {
    return createImage(paramInt1, paramInt2, paramInt3, null);
  }
  
  public PImage createImage(int paramInt1, int paramInt2, int paramInt3, Object paramObject)
  {
    PImage localPImage = new PImage(paramInt1, paramInt2, paramInt3);
    if (paramObject != null) {
      localPImage.setParams(g, paramObject);
    }
    parent = this;
    return localPImage;
  }
  
  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }
  
  public void paint(Graphics paramGraphics)
  {
    if (frameCount == 0) {
      return;
    }
    if ((g != null) && (g.image != null)) {
      synchronized (g.image)
      {
        paramGraphics.drawImage(g.image, 0, 0, null);
      }
    }
  }
  
  public void run()
  {
    long l1 = System.nanoTime();
    long l2 = 0L;
    int i = 0;
    while ((Thread.currentThread() == thread) && (!finished))
    {
      while (paused) {
        try
        {
          Thread.sleep(100L);
        }
        catch (InterruptedException localInterruptedException1) {}
      }
      if (resizeRequest)
      {
        resizeRenderer(resizeWidth, resizeHeight);
        resizeRequest = false;
      }
      handleDraw();
      if (frameCount == 1) {
        requestFocusInWindow();
      }
      long l3 = System.nanoTime();
      long l4 = l3 - l1;
      long l5 = frameRatePeriod - l4 - l2;
      if (l5 > 0L)
      {
        try
        {
          Thread.sleep(l5 / 1000000L, (int)(l5 % 1000000L));
          i = 0;
        }
        catch (InterruptedException localInterruptedException2) {}
        l2 = System.nanoTime() - l3 - l5;
      }
      else
      {
        l2 = 0L;
        if (i > 15)
        {
          Thread.yield();
          i = 0;
        }
      }
      l1 = System.nanoTime();
    }
    dispose();
    if (exitCalled) {
      exit2();
    }
  }
  
  public void handleDraw()
  {
    if ((g != null) && ((looping) || (redraw)))
    {
      if (!g.canDraw()) {
        return;
      }
      g.beginDraw();
      if (recorder != null) {
        recorder.beginDraw();
      }
      long l = System.nanoTime();
      if (frameCount == 0)
      {
        try
        {
          setup();
        }
        catch (RendererChangeException localRendererChangeException)
        {
          return;
        }
        defaultSize = false;
      }
      else
      {
        double d = 1000000.0D / ((l - frameRateLastNanos) / 1000000.0D);
        float f = (float)d / 1000.0F;
        frameRate = (frameRate * 0.9F + f * 0.1F);
        preMethods.handle();
        pmouseX = dmouseX;
        pmouseY = dmouseY;
        draw();
        dmouseX = mouseX;
        dmouseY = mouseY;
        dequeueMouseEvents();
        dequeueKeyEvents();
        drawMethods.handle();
        redraw = false;
      }
      g.endDraw();
      if (recorder != null) {
        recorder.endDraw();
      }
      frameRateLastNanos = l;
      frameCount += 1;
      repaint();
      getToolkit().sync();
      postMethods.handle();
    }
  }
  
  public synchronized void redraw()
  {
    if (!looping) {
      redraw = true;
    }
  }
  
  public synchronized void loop()
  {
    if (!looping) {
      looping = true;
    }
  }
  
  public synchronized void noLoop()
  {
    if (looping) {
      looping = false;
    }
  }
  
  public void addListeners()
  {
    addMouseListener(this);
    addMouseMotionListener(this);
    addKeyListener(this);
    addFocusListener(this);
    addComponentListener(new ComponentAdapter()
    {
      public void componentResized(ComponentEvent paramAnonymousComponentEvent)
      {
        Component localComponent = paramAnonymousComponentEvent.getComponent();
        Rectangle localRectangle = localComponent.getBounds();
        resizeRequest = true;
        resizeWidth = width;
        resizeHeight = height;
      }
    });
  }
  
  protected void enqueueMouseEvent(MouseEvent paramMouseEvent)
  {
    synchronized (mouseEventQueue)
    {
      if (mouseEventCount == mouseEventQueue.length)
      {
        MouseEvent[] arrayOfMouseEvent = new MouseEvent[mouseEventCount << 1];
        System.arraycopy(mouseEventQueue, 0, arrayOfMouseEvent, 0, mouseEventCount);
        mouseEventQueue = arrayOfMouseEvent;
      }
      mouseEventQueue[(mouseEventCount++)] = paramMouseEvent;
    }
  }
  
  protected void dequeueMouseEvents()
  {
    synchronized (mouseEventQueue)
    {
      for (int i = 0; i < mouseEventCount; i++)
      {
        mouseEvent = mouseEventQueue[i];
        handleMouseEvent(mouseEvent);
      }
      mouseEventCount = 0;
    }
  }
  
  protected void handleMouseEvent(MouseEvent paramMouseEvent)
  {
    int i = paramMouseEvent.getID();
    if ((i == 506) || (i == 503))
    {
      pmouseX = emouseX;
      pmouseY = emouseY;
      mouseX = paramMouseEvent.getX();
      mouseY = paramMouseEvent.getY();
    }
    mouseEvent = paramMouseEvent;
    int j = paramMouseEvent.getModifiers();
    if ((j & 0x10) != 0) {
      mouseButton = 37;
    } else if ((j & 0x8) != 0) {
      mouseButton = 3;
    } else if ((j & 0x4) != 0) {
      mouseButton = 39;
    }
    if ((platform == 2) && (mouseEvent.isPopupTrigger())) {
      mouseButton = 39;
    }
    mouseEventMethods.handle(new Object[] { paramMouseEvent });
    if (firstMouse)
    {
      pmouseX = mouseX;
      pmouseY = mouseY;
      dmouseX = mouseX;
      dmouseY = mouseY;
      firstMouse = false;
    }
    switch (i)
    {
    case 501: 
      mousePressed = true;
      mousePressed();
      break;
    case 502: 
      mousePressed = false;
      mouseReleased();
      break;
    case 500: 
      mouseClicked();
      break;
    case 506: 
      mouseDragged();
      break;
    case 503: 
      mouseMoved();
    }
    if ((i == 506) || (i == 503))
    {
      emouseX = mouseX;
      emouseY = mouseY;
    }
  }
  
  protected void checkMouseEvent(MouseEvent paramMouseEvent)
  {
    if (looping) {
      enqueueMouseEvent(paramMouseEvent);
    } else {
      handleMouseEvent(paramMouseEvent);
    }
  }
  
  public void mousePressed(MouseEvent paramMouseEvent)
  {
    checkMouseEvent(paramMouseEvent);
  }
  
  public void mouseReleased(MouseEvent paramMouseEvent)
  {
    checkMouseEvent(paramMouseEvent);
  }
  
  public void mouseClicked(MouseEvent paramMouseEvent)
  {
    checkMouseEvent(paramMouseEvent);
  }
  
  public void mouseEntered(MouseEvent paramMouseEvent)
  {
    checkMouseEvent(paramMouseEvent);
  }
  
  public void mouseExited(MouseEvent paramMouseEvent)
  {
    checkMouseEvent(paramMouseEvent);
  }
  
  public void mouseDragged(MouseEvent paramMouseEvent)
  {
    checkMouseEvent(paramMouseEvent);
  }
  
  public void mouseMoved(MouseEvent paramMouseEvent)
  {
    checkMouseEvent(paramMouseEvent);
  }
  
  public void mousePressed() {}
  
  public void mouseReleased() {}
  
  public void mouseClicked() {}
  
  public void mouseDragged() {}
  
  public void mouseMoved() {}
  
  protected void enqueueKeyEvent(KeyEvent paramKeyEvent)
  {
    synchronized (keyEventQueue)
    {
      if (keyEventCount == keyEventQueue.length)
      {
        KeyEvent[] arrayOfKeyEvent = new KeyEvent[keyEventCount << 1];
        System.arraycopy(keyEventQueue, 0, arrayOfKeyEvent, 0, keyEventCount);
        keyEventQueue = arrayOfKeyEvent;
      }
      keyEventQueue[(keyEventCount++)] = paramKeyEvent;
    }
  }
  
  protected void dequeueKeyEvents()
  {
    synchronized (keyEventQueue)
    {
      for (int i = 0; i < keyEventCount; i++)
      {
        keyEvent = keyEventQueue[i];
        handleKeyEvent(keyEvent);
      }
      keyEventCount = 0;
    }
  }
  
  protected void handleKeyEvent(KeyEvent paramKeyEvent)
  {
    keyEvent = paramKeyEvent;
    key = paramKeyEvent.getKeyChar();
    keyCode = paramKeyEvent.getKeyCode();
    keyEventMethods.handle(new Object[] { paramKeyEvent });
    switch (paramKeyEvent.getID())
    {
    case 401: 
      keyPressed = true;
      keyPressed();
      break;
    case 402: 
      keyPressed = false;
      keyReleased();
      break;
    case 400: 
      keyTyped();
    }
    if (paramKeyEvent.getID() == 401)
    {
      if (key == '\033') {
        exit();
      }
      if ((external) && (paramKeyEvent.getModifiers() == MENU_SHORTCUT) && (paramKeyEvent.getKeyCode() == 87)) {
        exit();
      }
    }
  }
  
  protected void checkKeyEvent(KeyEvent paramKeyEvent)
  {
    if (looping) {
      enqueueKeyEvent(paramKeyEvent);
    } else {
      handleKeyEvent(paramKeyEvent);
    }
  }
  
  public void keyPressed(KeyEvent paramKeyEvent)
  {
    checkKeyEvent(paramKeyEvent);
  }
  
  public void keyReleased(KeyEvent paramKeyEvent)
  {
    checkKeyEvent(paramKeyEvent);
  }
  
  public void keyTyped(KeyEvent paramKeyEvent)
  {
    checkKeyEvent(paramKeyEvent);
  }
  
  public void keyPressed() {}
  
  public void keyReleased() {}
  
  public void keyTyped() {}
  
  public void focusGained() {}
  
  public void focusGained(FocusEvent paramFocusEvent)
  {
    focused = true;
    focusGained();
  }
  
  public void focusLost() {}
  
  public void focusLost(FocusEvent paramFocusEvent)
  {
    focused = false;
    focusLost();
  }
  
  public int millis()
  {
    return (int)(System.currentTimeMillis() - millisOffset);
  }
  
  public static int second()
  {
    return Calendar.getInstance().get(13);
  }
  
  public static int minute()
  {
    return Calendar.getInstance().get(12);
  }
  
  public static int hour()
  {
    return Calendar.getInstance().get(11);
  }
  
  public static int day()
  {
    return Calendar.getInstance().get(5);
  }
  
  public static int month()
  {
    return Calendar.getInstance().get(2) + 1;
  }
  
  public static int year()
  {
    return Calendar.getInstance().get(1);
  }
  
  public void delay(int paramInt)
  {
    if ((frameCount != 0) && (paramInt > 0)) {
      try
      {
        Thread.sleep(paramInt);
      }
      catch (InterruptedException localInterruptedException) {}
    }
  }
  
  public void frameRate(float paramFloat)
  {
    frameRateTarget = paramFloat;
    frameRatePeriod = ((1.0E9D / frameRateTarget));
  }
  
  public String param(String paramString)
  {
    if (online) {
      return getParameter(paramString);
    }
    System.err.println("param() only works inside a web browser");
    return null;
  }
  
  public void status(String paramString)
  {
    if (online) {
      showStatus(paramString);
    } else {
      System.out.println(paramString);
    }
  }
  
  public void link(String paramString)
  {
    link(paramString, null);
  }
  
  public void link(String paramString1, String paramString2)
  {
    if (online) {
      try
      {
        if (paramString2 == null) {
          getAppletContext().showDocument(new URL(paramString1));
        } else {
          getAppletContext().showDocument(new URL(paramString1), paramString2);
        }
      }
      catch (Exception localException1)
      {
        localException1.printStackTrace();
        throw new RuntimeException("Could not open " + paramString1);
      }
    } else {
      try
      {
        if (platform == 1)
        {
          paramString1 = paramString1.replaceAll("&", "^&");
          Runtime.getRuntime().exec("cmd /c start " + paramString1);
        }
        else if (platform == 2)
        {
          try
          {
            Class localClass = Class.forName("com.apple.eio.FileManager");
            Method localMethod = localClass.getMethod("openURL", new Class[] { String.class });
            localMethod.invoke(null, new Object[] { paramString1 });
          }
          catch (Exception localException2)
          {
            localException2.printStackTrace();
          }
        }
        else
        {
          open(paramString1);
        }
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
        throw new RuntimeException("Could not open " + paramString1);
      }
    }
  }
  
  public static void open(String paramString)
  {
    open(new String[] { paramString });
  }
  
  public static Process open(String[] paramArrayOfString)
  {
    String[] arrayOfString = null;
    if (platform == 1)
    {
      arrayOfString = new String[] { "cmd", "/c" };
    }
    else if (platform == 2)
    {
      arrayOfString = new String[] { "open" };
    }
    else if (platform == 3)
    {
      if (openLauncher == null) {
        try
        {
          Process localProcess1 = Runtime.getRuntime().exec(new String[] { "gnome-open" });
          localProcess1.waitFor();
          openLauncher = "gnome-open";
        }
        catch (Exception localException1) {}
      }
      if (openLauncher == null) {
        try
        {
          Process localProcess2 = Runtime.getRuntime().exec(new String[] { "kde-open" });
          localProcess2.waitFor();
          openLauncher = "kde-open";
        }
        catch (Exception localException2) {}
      }
      if (openLauncher == null) {
        System.err.println("Could not find gnome-open or kde-open, the open() command may not work.");
      }
      if (openLauncher != null) {
        arrayOfString = new String[] { openLauncher };
      }
    }
    if (arrayOfString != null)
    {
      if (arrayOfString[0].equals(paramArrayOfString[0])) {
        return exec(paramArrayOfString);
      }
      arrayOfString = concat(arrayOfString, paramArrayOfString);
      return exec(arrayOfString);
    }
    return exec(paramArrayOfString);
  }
  
  public static Process exec(String[] paramArrayOfString)
  {
    try
    {
      return Runtime.getRuntime().exec(paramArrayOfString);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      throw new RuntimeException("Could not open " + join(paramArrayOfString, ' '));
    }
  }
  
  public void die(String paramString)
  {
    dispose();
    throw new RuntimeException(paramString);
  }
  
  public void die(String paramString, Exception paramException)
  {
    if (paramException != null) {
      paramException.printStackTrace();
    }
    die(paramString);
  }
  
  public void exit()
  {
    if (thread == null)
    {
      exit2();
    }
    else if (looping)
    {
      finished = true;
      exitCalled = true;
    }
    else if (!looping)
    {
      dispose();
      exit2();
    }
  }
  
  void exit2()
  {
    try
    {
      System.exit(0);
    }
    catch (SecurityException localSecurityException) {}
  }
  
  public void dispose()
  {
    finished = true;
    if (thread == null) {
      return;
    }
    thread = null;
    if (g != null) {
      g.dispose();
    }
    disposeMethods.handle();
  }
  
  public void method(String paramString)
  {
    try
    {
      Method localMethod = getClass().getMethod(paramString, new Class[0]);
      localMethod.invoke(this, new Object[0]);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      localIllegalArgumentException.printStackTrace();
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      localIllegalAccessException.printStackTrace();
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      localInvocationTargetException.getTargetException().printStackTrace();
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      System.err.println("There is no public " + paramString + "() method " + "in the class " + getClass().getName());
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void thread(final String paramString)
  {
    Thread local3 = new Thread()
    {
      public void run()
      {
        method(paramString);
      }
    };
    local3.start();
  }
  
  public void save(String paramString)
  {
    g.save(savePath(paramString));
  }
  
  public void saveFrame()
  {
    try
    {
      g.save(savePath("screen-" + nf(frameCount, 4) + ".tif"));
    }
    catch (SecurityException localSecurityException)
    {
      System.err.println("Can't use saveFrame() when running in a browser, unless using a signed applet.");
    }
  }
  
  public void saveFrame(String paramString)
  {
    try
    {
      g.save(savePath(insertFrame(paramString)));
    }
    catch (SecurityException localSecurityException)
    {
      System.err.println("Can't use saveFrame() when running in a browser, unless using a signed applet.");
    }
  }
  
  protected String insertFrame(String paramString)
  {
    int i = paramString.indexOf('#');
    int j = paramString.lastIndexOf('#');
    if ((i != -1) && (j - i > 0))
    {
      String str1 = paramString.substring(0, i);
      int k = j - i + 1;
      String str2 = paramString.substring(j + 1);
      return str1 + nf(frameCount, k) + str2;
    }
    return paramString;
  }
  
  public void cursor(int paramInt)
  {
    setCursor(Cursor.getPredefinedCursor(paramInt));
    cursorVisible = true;
    cursorType = paramInt;
  }
  
  public void cursor(PImage paramPImage)
  {
    cursor(paramPImage, width / 2, height / 2);
  }
  
  public void cursor(PImage paramPImage, int paramInt1, int paramInt2)
  {
    Image localImage = createImage(new MemoryImageSource(width, height, pixels, 0, width));
    Point localPoint = new Point(paramInt1, paramInt2);
    Toolkit localToolkit = Toolkit.getDefaultToolkit();
    Cursor localCursor = localToolkit.createCustomCursor(localImage, localPoint, "Custom Cursor");
    setCursor(localCursor);
    cursorVisible = true;
  }
  
  public void cursor()
  {
    if (!cursorVisible)
    {
      cursorVisible = true;
      setCursor(Cursor.getPredefinedCursor(cursorType));
    }
  }
  
  public void noCursor()
  {
    if (!cursorVisible) {
      return;
    }
    if (invisibleCursor == null) {
      invisibleCursor = new PImage(16, 16, 2);
    }
    cursor(invisibleCursor, 8, 8);
    cursorVisible = false;
  }
  
  public static void print(byte paramByte)
  {
    System.out.print(paramByte);
    System.out.flush();
  }
  
  public static void print(boolean paramBoolean)
  {
    System.out.print(paramBoolean);
    System.out.flush();
  }
  
  public static void print(char paramChar)
  {
    System.out.print(paramChar);
    System.out.flush();
  }
  
  public static void print(int paramInt)
  {
    System.out.print(paramInt);
    System.out.flush();
  }
  
  public static void print(float paramFloat)
  {
    System.out.print(paramFloat);
    System.out.flush();
  }
  
  public static void print(String paramString)
  {
    System.out.print(paramString);
    System.out.flush();
  }
  
  public static void print(Object paramObject)
  {
    if (paramObject == null) {
      System.out.print("null");
    } else {
      System.out.println(paramObject.toString());
    }
  }
  
  public static void println()
  {
    System.out.println();
  }
  
  public static void println(byte paramByte)
  {
    print(paramByte);
    System.out.println();
  }
  
  public static void println(boolean paramBoolean)
  {
    print(paramBoolean);
    System.out.println();
  }
  
  public static void println(char paramChar)
  {
    print(paramChar);
    System.out.println();
  }
  
  public static void println(int paramInt)
  {
    print(paramInt);
    System.out.println();
  }
  
  public static void println(float paramFloat)
  {
    print(paramFloat);
    System.out.println();
  }
  
  public static void println(String paramString)
  {
    print(paramString);
    System.out.println();
  }
  
  public static void println(Object paramObject)
  {
    if (paramObject == null)
    {
      System.out.println("null");
    }
    else
    {
      String str = paramObject.getClass().getName();
      if (str.charAt(0) == '[') {
        switch (str.charAt(1))
        {
        case '[': 
          System.out.println(paramObject);
          break;
        case 'L': 
          Object[] arrayOfObject = (Object[])paramObject;
          for (int i = 0; i < arrayOfObject.length; i++) {
            if ((arrayOfObject[i] instanceof String)) {
              System.out.println("[" + i + "] \"" + arrayOfObject[i] + "\"");
            } else {
              System.out.println("[" + i + "] " + arrayOfObject[i]);
            }
          }
          break;
        case 'Z': 
          boolean[] arrayOfBoolean = (boolean[])paramObject;
          for (int j = 0; j < arrayOfBoolean.length; j++) {
            System.out.println("[" + j + "] " + arrayOfBoolean[j]);
          }
          break;
        case 'B': 
          byte[] arrayOfByte = (byte[])paramObject;
          for (int k = 0; k < arrayOfByte.length; k++) {
            System.out.println("[" + k + "] " + arrayOfByte[k]);
          }
          break;
        case 'C': 
          char[] arrayOfChar = (char[])paramObject;
          for (int m = 0; m < arrayOfChar.length; m++) {
            System.out.println("[" + m + "] '" + arrayOfChar[m] + "'");
          }
          break;
        case 'I': 
          int[] arrayOfInt = (int[])paramObject;
          for (int n = 0; n < arrayOfInt.length; n++) {
            System.out.println("[" + n + "] " + arrayOfInt[n]);
          }
          break;
        case 'F': 
          float[] arrayOfFloat = (float[])paramObject;
          for (int i1 = 0; i1 < arrayOfFloat.length; i1++) {
            System.out.println("[" + i1 + "] " + arrayOfFloat[i1]);
          }
          break;
        case 'D': 
          double[] arrayOfDouble = (double[])paramObject;
          for (int i2 = 0; i2 < arrayOfDouble.length; i2++) {
            System.out.println("[" + i2 + "] " + arrayOfDouble[i2]);
          }
          break;
        case 'E': 
        case 'G': 
        case 'H': 
        case 'J': 
        case 'K': 
        case 'M': 
        case 'N': 
        case 'O': 
        case 'P': 
        case 'Q': 
        case 'R': 
        case 'S': 
        case 'T': 
        case 'U': 
        case 'V': 
        case 'W': 
        case 'X': 
        case 'Y': 
        default: 
          System.out.println(paramObject);
        }
      } else {
        System.out.println(paramObject);
      }
    }
  }
  
  public static final float abs(float paramFloat)
  {
    return paramFloat < 0.0F ? -paramFloat : paramFloat;
  }
  
  public static final int abs(int paramInt)
  {
    return paramInt < 0 ? -paramInt : paramInt;
  }
  
  public static final float sq(float paramFloat)
  {
    return paramFloat * paramFloat;
  }
  
  public static final float sqrt(float paramFloat)
  {
    return (float)Math.sqrt(paramFloat);
  }
  
  public static final float log(float paramFloat)
  {
    return (float)Math.log(paramFloat);
  }
  
  public static final float exp(float paramFloat)
  {
    return (float)Math.exp(paramFloat);
  }
  
  public static final float pow(float paramFloat1, float paramFloat2)
  {
    return (float)Math.pow(paramFloat1, paramFloat2);
  }
  
  public static final int max(int paramInt1, int paramInt2)
  {
    return paramInt1 > paramInt2 ? paramInt1 : paramInt2;
  }
  
  public static final float max(float paramFloat1, float paramFloat2)
  {
    return paramFloat1 > paramFloat2 ? paramFloat1 : paramFloat2;
  }
  
  public static final int max(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt2 > paramInt3 ? paramInt2 : paramInt1 > paramInt2 ? paramInt3 : paramInt1 > paramInt3 ? paramInt1 : paramInt3;
  }
  
  public static final float max(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return paramFloat2 > paramFloat3 ? paramFloat2 : paramFloat1 > paramFloat2 ? paramFloat3 : paramFloat1 > paramFloat3 ? paramFloat1 : paramFloat3;
  }
  
  public static final int max(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt.length == 0) {
      throw new ArrayIndexOutOfBoundsException("Cannot use min() or max() on an empty array.");
    }
    int i = paramArrayOfInt[0];
    for (int j = 1; j < paramArrayOfInt.length; j++) {
      if (paramArrayOfInt[j] > i) {
        i = paramArrayOfInt[j];
      }
    }
    return i;
  }
  
  public static final float max(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat.length == 0) {
      throw new ArrayIndexOutOfBoundsException("Cannot use min() or max() on an empty array.");
    }
    float f = paramArrayOfFloat[0];
    for (int i = 1; i < paramArrayOfFloat.length; i++) {
      if (paramArrayOfFloat[i] > f) {
        f = paramArrayOfFloat[i];
      }
    }
    return f;
  }
  
  public static final int min(int paramInt1, int paramInt2)
  {
    return paramInt1 < paramInt2 ? paramInt1 : paramInt2;
  }
  
  public static final float min(float paramFloat1, float paramFloat2)
  {
    return paramFloat1 < paramFloat2 ? paramFloat1 : paramFloat2;
  }
  
  public static final int min(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt2 < paramInt3 ? paramInt2 : paramInt1 < paramInt2 ? paramInt3 : paramInt1 < paramInt3 ? paramInt1 : paramInt3;
  }
  
  public static final float min(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return paramFloat2 < paramFloat3 ? paramFloat2 : paramFloat1 < paramFloat2 ? paramFloat3 : paramFloat1 < paramFloat3 ? paramFloat1 : paramFloat3;
  }
  
  public static final int min(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt.length == 0) {
      throw new ArrayIndexOutOfBoundsException("Cannot use min() or max() on an empty array.");
    }
    int i = paramArrayOfInt[0];
    for (int j = 1; j < paramArrayOfInt.length; j++) {
      if (paramArrayOfInt[j] < i) {
        i = paramArrayOfInt[j];
      }
    }
    return i;
  }
  
  public static final float min(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat.length == 0) {
      throw new ArrayIndexOutOfBoundsException("Cannot use min() or max() on an empty array.");
    }
    float f = paramArrayOfFloat[0];
    for (int i = 1; i < paramArrayOfFloat.length; i++) {
      if (paramArrayOfFloat[i] < f) {
        f = paramArrayOfFloat[i];
      }
    }
    return f;
  }
  
  public static final int constrain(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 > paramInt3 ? paramInt3 : paramInt1 < paramInt2 ? paramInt2 : paramInt1;
  }
  
  public static final float constrain(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return paramFloat1 > paramFloat3 ? paramFloat3 : paramFloat1 < paramFloat2 ? paramFloat2 : paramFloat1;
  }
  
  public static final float sin(float paramFloat)
  {
    return (float)Math.sin(paramFloat);
  }
  
  public static final float cos(float paramFloat)
  {
    return (float)Math.cos(paramFloat);
  }
  
  public static final float tan(float paramFloat)
  {
    return (float)Math.tan(paramFloat);
  }
  
  public static final float asin(float paramFloat)
  {
    return (float)Math.asin(paramFloat);
  }
  
  public static final float acos(float paramFloat)
  {
    return (float)Math.acos(paramFloat);
  }
  
  public static final float atan(float paramFloat)
  {
    return (float)Math.atan(paramFloat);
  }
  
  public static final float atan2(float paramFloat1, float paramFloat2)
  {
    return (float)Math.atan2(paramFloat1, paramFloat2);
  }
  
  public static final float degrees(float paramFloat)
  {
    return paramFloat * 57.295776F;
  }
  
  public static final float radians(float paramFloat)
  {
    return paramFloat * 0.017453292F;
  }
  
  public static final int ceil(float paramFloat)
  {
    return (int)Math.ceil(paramFloat);
  }
  
  public static final int floor(float paramFloat)
  {
    return (int)Math.floor(paramFloat);
  }
  
  public static final int round(float paramFloat)
  {
    return Math.round(paramFloat);
  }
  
  public static final float mag(float paramFloat1, float paramFloat2)
  {
    return (float)Math.sqrt(paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2);
  }
  
  public static final float mag(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return (float)Math.sqrt(paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2 + paramFloat3 * paramFloat3);
  }
  
  public static final float dist(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    return sqrt(sq(paramFloat3 - paramFloat1) + sq(paramFloat4 - paramFloat2));
  }
  
  public static final float dist(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    return sqrt(sq(paramFloat4 - paramFloat1) + sq(paramFloat5 - paramFloat2) + sq(paramFloat6 - paramFloat3));
  }
  
  public static final float lerp(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return paramFloat1 + (paramFloat2 - paramFloat1) * paramFloat3;
  }
  
  public static final float norm(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return (paramFloat1 - paramFloat2) / (paramFloat3 - paramFloat2);
  }
  
  public static final float map(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    return paramFloat4 + (paramFloat5 - paramFloat4) * ((paramFloat1 - paramFloat2) / (paramFloat3 - paramFloat2));
  }
  
  public final float random(float paramFloat)
  {
    if (paramFloat == 0.0F) {
      return 0.0F;
    }
    if (internalRandom == null) {
      internalRandom = new Random();
    }
    float f = 0.0F;
    do
    {
      f = internalRandom.nextFloat() * paramFloat;
    } while (f == paramFloat);
    return f;
  }
  
  public final float random(float paramFloat1, float paramFloat2)
  {
    if (paramFloat1 >= paramFloat2) {
      return paramFloat1;
    }
    float f = paramFloat2 - paramFloat1;
    return random(f) + paramFloat1;
  }
  
  public final void randomSeed(long paramLong)
  {
    if (internalRandom == null) {
      internalRandom = new Random();
    }
    internalRandom.setSeed(paramLong);
  }
  
  public float noise(float paramFloat)
  {
    return noise(paramFloat, 0.0F, 0.0F);
  }
  
  public float noise(float paramFloat1, float paramFloat2)
  {
    return noise(paramFloat1, paramFloat2, 0.0F);
  }
  
  public float noise(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (perlin == null)
    {
      if (perlinRandom == null) {
        perlinRandom = new Random();
      }
      perlin = new float['က'];
      for (i = 0; i < 4096; i++) {
        perlin[i] = perlinRandom.nextFloat();
      }
      perlin_cosTable = PGraphics.cosLUT;
      perlin_TWOPI = (this.perlin_PI = 'ː');
      perlin_PI >>= 1;
    }
    if (paramFloat1 < 0.0F) {
      paramFloat1 = -paramFloat1;
    }
    if (paramFloat2 < 0.0F) {
      paramFloat2 = -paramFloat2;
    }
    if (paramFloat3 < 0.0F) {
      paramFloat3 = -paramFloat3;
    }
    int i = (int)paramFloat1;
    int j = (int)paramFloat2;
    int k = (int)paramFloat3;
    float f1 = paramFloat1 - i;
    float f2 = paramFloat2 - j;
    float f3 = paramFloat3 - k;
    float f6 = 0.0F;
    float f7 = 0.5F;
    for (int m = 0; m < perlin_octaves; m++)
    {
      int n = i + (j << 4) + (k << 8);
      float f4 = noise_fsc(f1);
      float f5 = noise_fsc(f2);
      float f8 = perlin[(n & 0xFFF)];
      f8 += f4 * (perlin[(n + 1 & 0xFFF)] - f8);
      float f9 = perlin[(n + 16 & 0xFFF)];
      f9 += f4 * (perlin[(n + 16 + 1 & 0xFFF)] - f9);
      f8 += f5 * (f9 - f8);
      n += 256;
      f9 = perlin[(n & 0xFFF)];
      f9 += f4 * (perlin[(n + 1 & 0xFFF)] - f9);
      float f10 = perlin[(n + 16 & 0xFFF)];
      f10 += f4 * (perlin[(n + 16 + 1 & 0xFFF)] - f10);
      f9 += f5 * (f10 - f9);
      f8 += noise_fsc(f3) * (f9 - f8);
      f6 += f8 * f7;
      f7 *= perlin_amp_falloff;
      i <<= 1;
      f1 *= 2.0F;
      j <<= 1;
      f2 *= 2.0F;
      k <<= 1;
      f3 *= 2.0F;
      if (f1 >= 1.0F)
      {
        i++;
        f1 -= 1.0F;
      }
      if (f2 >= 1.0F)
      {
        j++;
        f2 -= 1.0F;
      }
      if (f3 >= 1.0F)
      {
        k++;
        f3 -= 1.0F;
      }
    }
    return f6;
  }
  
  private float noise_fsc(float paramFloat)
  {
    return 0.5F * (1.0F - perlin_cosTable[((int)(paramFloat * perlin_PI) % perlin_TWOPI)]);
  }
  
  public void noiseDetail(int paramInt)
  {
    if (paramInt > 0) {
      perlin_octaves = paramInt;
    }
  }
  
  public void noiseDetail(int paramInt, float paramFloat)
  {
    if (paramInt > 0) {
      perlin_octaves = paramInt;
    }
    if (paramFloat > 0.0F) {
      perlin_amp_falloff = paramFloat;
    }
  }
  
  public void noiseSeed(long paramLong)
  {
    if (perlinRandom == null) {
      perlinRandom = new Random();
    }
    perlinRandom.setSeed(paramLong);
    perlin = null;
  }
  
  public PImage loadImage(String paramString)
  {
    return loadImage(paramString, null, null);
  }
  
  public PImage loadImage(String paramString1, String paramString2)
  {
    return loadImage(paramString1, paramString2, null);
  }
  
  public PImage loadImage(String paramString, Object paramObject)
  {
    return loadImage(paramString, null, paramObject);
  }
  
  public PImage loadImage(String paramString1, String paramString2, Object paramObject)
  {
    Object localObject1;
    if (paramString2 == null)
    {
      localObject1 = paramString1.toLowerCase();
      int j = paramString1.lastIndexOf('.');
      if (j == -1) {
        paramString2 = "unknown";
      }
      paramString2 = ((String)localObject1).substring(j + 1);
      int k = paramString2.indexOf('?');
      if (k != -1) {
        paramString2 = paramString2.substring(0, k);
      }
    }
    paramString2 = paramString2.toLowerCase();
    if (paramString2.equals("tga")) {
      try
      {
        localObject1 = loadImageTGA(paramString1);
        if (paramObject != null) {
          ((PImage)localObject1).setParams(g, paramObject);
        }
        return localObject1;
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
        return null;
      }
    }
    byte[] arrayOfByte;
    Object localObject2;
    if ((paramString2.equals("tif")) || (paramString2.equals("tiff")))
    {
      arrayOfByte = loadBytes(paramString1);
      localObject2 = arrayOfByte == null ? null : PImage.loadTIFF(arrayOfByte);
      if (paramObject != null) {
        ((PImage)localObject2).setParams(g, paramObject);
      }
      return localObject2;
    }
    try
    {
      if ((paramString2.equals("jpg")) || (paramString2.equals("jpeg")) || (paramString2.equals("gif")) || (paramString2.equals("png")) || (paramString2.equals("unknown")))
      {
        arrayOfByte = loadBytes(paramString1);
        if (arrayOfByte == null) {
          return null;
        }
        localObject2 = Toolkit.getDefaultToolkit().createImage(arrayOfByte);
        PImage localPImage = loadImageMT((Image)localObject2);
        if (width == -1) {
          System.err.println("The file " + paramString1 + " contains bad image data, or may not be an image.");
        }
        if ((paramString2.equals("gif")) || (paramString2.equals("png"))) {
          localPImage.checkAlpha();
        }
        if (paramObject != null) {
          localPImage.setParams(g, paramObject);
        }
        return localPImage;
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    if (loadImageFormats == null) {
      loadImageFormats = ImageIO.getReaderFormatNames();
    }
    if (loadImageFormats != null) {
      for (int i = 0; i < loadImageFormats.length; i++) {
        if (paramString2.equals(loadImageFormats[i]))
        {
          localObject2 = loadImageIO(paramString1);
          if (paramObject != null) {
            ((PImage)localObject2).setParams(g, paramObject);
          }
          return localObject2;
        }
      }
    }
    System.err.println("Could not find a method to load " + paramString1);
    return null;
  }
  
  public PImage requestImage(String paramString)
  {
    return requestImage(paramString, null, null);
  }
  
  public PImage requestImage(String paramString1, String paramString2)
  {
    return requestImage(paramString1, paramString2, null);
  }
  
  public PImage requestImage(String paramString1, String paramString2, Object paramObject)
  {
    PImage localPImage = createImage(0, 0, 2, paramObject);
    AsyncImageLoader localAsyncImageLoader = new AsyncImageLoader(paramString1, paramString2, localPImage);
    localAsyncImageLoader.start();
    return localPImage;
  }
  
  protected PImage loadImageMT(Image paramImage)
  {
    MediaTracker localMediaTracker = new MediaTracker(this);
    localMediaTracker.addImage(paramImage, 0);
    try
    {
      localMediaTracker.waitForAll();
    }
    catch (InterruptedException localInterruptedException) {}
    PImage localPImage = new PImage(paramImage);
    parent = this;
    return localPImage;
  }
  
  protected PImage loadImageIO(String paramString)
  {
    InputStream localInputStream = createInput(paramString);
    if (localInputStream == null)
    {
      System.err.println("The image " + paramString + " could not be found.");
      return null;
    }
    try
    {
      BufferedImage localBufferedImage = ImageIO.read(localInputStream);
      PImage localPImage = new PImage(localBufferedImage.getWidth(), localBufferedImage.getHeight());
      parent = this;
      localBufferedImage.getRGB(0, 0, width, height, pixels, 0, width);
      localPImage.checkAlpha();
      return localPImage;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return null;
  }
  
  protected PImage loadImageTGA(String paramString)
    throws IOException
  {
    InputStream localInputStream = createInput(paramString);
    if (localInputStream == null) {
      return null;
    }
    byte[] arrayOfByte = new byte[18];
    int i = 0;
    do
    {
      j = localInputStream.read(arrayOfByte, i, arrayOfByte.length - i);
      if (j == -1) {
        return null;
      }
      i += j;
    } while (i < 18);
    int j = 0;
    if (((arrayOfByte[2] == 3) || (arrayOfByte[2] == 11)) && (arrayOfByte[16] == 8) && ((arrayOfByte[17] == 8) || (arrayOfByte[17] == 40))) {
      j = 4;
    } else if (((arrayOfByte[2] == 2) || (arrayOfByte[2] == 10)) && (arrayOfByte[16] == 24) && ((arrayOfByte[17] == 32) || (arrayOfByte[17] == 0))) {
      j = 1;
    } else if (((arrayOfByte[2] == 2) || (arrayOfByte[2] == 10)) && (arrayOfByte[16] == 32) && ((arrayOfByte[17] == 8) || (arrayOfByte[17] == 40))) {
      j = 2;
    }
    if (j == 0)
    {
      System.err.println("Unknown .tga file format for " + paramString);
      return null;
    }
    int k = ((arrayOfByte[13] & 0xFF) << 8) + (arrayOfByte[12] & 0xFF);
    int m = ((arrayOfByte[15] & 0xFF) << 8) + (arrayOfByte[14] & 0xFF);
    PImage localPImage = createImage(k, m, j);
    int n = (arrayOfByte[17] & 0x20) != 0 ? 1 : 0;
    int i1;
    int i3;
    if ((arrayOfByte[2] == 2) || (arrayOfByte[2] == 3))
    {
      int i2;
      if (n != 0)
      {
        i1 = (m - 1) * k;
        switch (j)
        {
        case 4: 
          for (i2 = m - 1; i2 >= 0; i2--)
          {
            for (i3 = 0; i3 < k; i3++) {
              pixels[(i1 + i3)] = localInputStream.read();
            }
            i1 -= k;
          }
          break;
        case 1: 
          for (i2 = m - 1; i2 >= 0; i2--)
          {
            for (i3 = 0; i3 < k; i3++) {
              pixels[(i1 + i3)] = (localInputStream.read() | localInputStream.read() << 8 | localInputStream.read() << 16 | 0xFF000000);
            }
            i1 -= k;
          }
          break;
        case 2: 
          for (i2 = m - 1; i2 >= 0; i2--)
          {
            for (i3 = 0; i3 < k; i3++) {
              pixels[(i1 + i3)] = (localInputStream.read() | localInputStream.read() << 8 | localInputStream.read() << 16 | localInputStream.read() << 24);
            }
            i1 -= k;
          }
        }
      }
      else
      {
        i1 = k * m;
        switch (j)
        {
        case 4: 
          for (i2 = 0; i2 < i1; i2++) {
            pixels[i2] = localInputStream.read();
          }
          break;
        case 1: 
          for (i2 = 0; i2 < i1; i2++) {
            pixels[i2] = (localInputStream.read() | localInputStream.read() << 8 | localInputStream.read() << 16 | 0xFF000000);
          }
          break;
        case 2: 
          for (i2 = 0; i2 < i1; i2++) {
            pixels[i2] = (localInputStream.read() | localInputStream.read() << 8 | localInputStream.read() << 16 | localInputStream.read() << 24);
          }
        }
      }
    }
    else
    {
      i1 = 0;
      int[] arrayOfInt1 = pixels;
      int i4;
      int i5;
      while (i1 < arrayOfInt1.length)
      {
        i3 = localInputStream.read();
        i4 = (i3 & 0x80) != 0 ? 1 : 0;
        if (i4 != 0)
        {
          i3 -= 127;
          i5 = 0;
          switch (j)
          {
          case 4: 
            i5 = localInputStream.read();
            break;
          case 1: 
            i5 = 0xFF000000 | localInputStream.read() | localInputStream.read() << 8 | localInputStream.read() << 16;
            break;
          case 2: 
            i5 = localInputStream.read() | localInputStream.read() << 8 | localInputStream.read() << 16 | localInputStream.read() << 24;
          }
          for (int i6 = 0; i6 < i3; i6++)
          {
            arrayOfInt1[(i1++)] = i5;
            if (i1 == arrayOfInt1.length) {
              break;
            }
          }
        }
        else
        {
          i3++;
          switch (j)
          {
          case 4: 
            for (i5 = 0; i5 < i3; i5++) {
              arrayOfInt1[(i1++)] = localInputStream.read();
            }
            break;
          case 1: 
            for (i5 = 0; i5 < i3; i5++) {
              arrayOfInt1[(i1++)] = (0xFF000000 | localInputStream.read() | localInputStream.read() << 8 | localInputStream.read() << 16);
            }
            break;
          case 2: 
            for (i5 = 0; i5 < i3; i5++) {
              arrayOfInt1[(i1++)] = (localInputStream.read() | localInputStream.read() << 8 | localInputStream.read() << 16 | localInputStream.read() << 24);
            }
          }
        }
      }
      if (n == 0)
      {
        int[] arrayOfInt2 = new int[k];
        for (i4 = 0; i4 < m / 2; i4++)
        {
          i5 = m - 1 - i4;
          System.arraycopy(arrayOfInt1, i4 * k, arrayOfInt2, 0, k);
          System.arraycopy(arrayOfInt1, i5 * k, arrayOfInt1, i4 * k, k);
          System.arraycopy(arrayOfInt2, 0, arrayOfInt1, i5 * k, k);
        }
      }
    }
    return localPImage;
  }
  
  public PShape loadShape(String paramString)
  {
    return loadShape(paramString, null);
  }
  
  public PShape loadShape(String paramString, Object paramObject)
  {
    String str2 = paramString.toLowerCase();
    int i = paramString.lastIndexOf('.');
    if (i == -1) {
      str1 = "unknown";
    }
    String str1 = str2.substring(i + 1);
    int j = str1.indexOf('?');
    if (j != -1) {
      str1 = str1.substring(0, j);
    }
    if (str1.equals("svg")) {
      return new PShapeSVG(this, paramString);
    }
    if (str1.equals("svgz"))
    {
      try
      {
        GZIPInputStream localGZIPInputStream = new GZIPInputStream(createInput(paramString));
        XMLElement localXMLElement = new XMLElement(createReader(localGZIPInputStream));
        return new PShapeSVG(localXMLElement);
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
    }
    else
    {
      loadShapeFormats = g.getSupportedShapeFormats();
      if (loadShapeFormats != null) {
        for (int k = 0; k < loadShapeFormats.length; k++) {
          if (str1.equals(loadShapeFormats[k])) {
            return g.loadShape(paramString, paramObject);
          }
        }
      }
    }
    return null;
  }
  
  public PShape createShape(int paramInt, Object paramObject)
  {
    return g.createShape(paramInt, paramObject);
  }
  
  public PFont loadFont(String paramString)
  {
    try
    {
      InputStream localInputStream = createInput(paramString);
      return new PFont(localInputStream);
    }
    catch (Exception localException)
    {
      die("Could not load font " + paramString + ". " + "Make sure that the font has been copied " + "to the data folder of your sketch.", localException);
    }
    return null;
  }
  
  protected PFont createDefaultFont(float paramFloat)
  {
    return createFont("Lucida Sans", paramFloat, true, null);
  }
  
  public PFont createFont(String paramString, float paramFloat)
  {
    return createFont(paramString, paramFloat, true, null);
  }
  
  public PFont createFont(String paramString, float paramFloat, boolean paramBoolean)
  {
    return createFont(paramString, paramFloat, paramBoolean, null);
  }
  
  public PFont createFont(String paramString, float paramFloat, boolean paramBoolean, char[] paramArrayOfChar)
  {
    String str = paramString.toLowerCase();
    Font localFont = null;
    try
    {
      InputStream localInputStream = null;
      if ((str.endsWith(".otf")) || (str.endsWith(".ttf")))
      {
        localInputStream = createInput(paramString);
        if (localInputStream == null)
        {
          System.err.println("The font \"" + paramString + "\" " + "is missing or inaccessible, make sure " + "the URL is valid or that the file has been " + "added to your sketch and is readable.");
          return null;
        }
        localFont = Font.createFont(0, createInput(paramString));
      }
      else
      {
        localFont = PFont.findFont(paramString);
      }
      return new PFont(localFont.deriveFont(paramFloat), paramBoolean, paramArrayOfChar, localInputStream != null);
    }
    catch (Exception localException)
    {
      System.err.println("Problem createFont(" + paramString + ")");
      localException.printStackTrace();
    }
    return null;
  }
  
  protected void checkParentFrame()
  {
    if (parentFrame == null)
    {
      for (Container localContainer = getParent(); localContainer != null; localContainer = localContainer.getParent()) {
        if ((localContainer instanceof Frame))
        {
          parentFrame = ((Frame)localContainer);
          break;
        }
      }
      if (parentFrame == null) {
        parentFrame = new Frame();
      }
    }
  }
  
  public String selectInput()
  {
    return selectInput("Select a file...");
  }
  
  public String selectInput(String paramString)
  {
    return selectFileImpl(paramString, 0);
  }
  
  public String selectOutput()
  {
    return selectOutput("Save as...");
  }
  
  public String selectOutput(String paramString)
  {
    return selectFileImpl(paramString, 1);
  }
  
  protected String selectFileImpl(final String paramString, final int paramInt)
  {
    checkParentFrame();
    try
    {
      SwingUtilities.invokeAndWait(new Runnable()
      {
        public void run()
        {
          FileDialog localFileDialog = new FileDialog(parentFrame, paramString, paramInt);
          localFileDialog.setVisible(true);
          String str1 = localFileDialog.getDirectory();
          String str2 = localFileDialog.getFile();
          selectedFile = (str2 == null ? null : new File(str1, str2));
        }
      });
      return selectedFile == null ? null : selectedFile.getAbsolutePath();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return null;
  }
  
  public String selectFolder()
  {
    return selectFolder("Select a folder...");
  }
  
  public String selectFolder(final String paramString)
  {
    checkParentFrame();
    try
    {
      SwingUtilities.invokeAndWait(new Runnable()
      {
        public void run()
        {
          Object localObject;
          if (PApplet.platform == 2)
          {
            localObject = new FileDialog(parentFrame, paramString, 0);
            System.setProperty("apple.awt.fileDialogForDirectories", "true");
            ((FileDialog)localObject).setVisible(true);
            System.setProperty("apple.awt.fileDialogForDirectories", "false");
            String str = ((FileDialog)localObject).getFile();
            selectedFile = (str == null ? null : new File(((FileDialog)localObject).getDirectory(), ((FileDialog)localObject).getFile()));
          }
          else
          {
            localObject = new JFileChooser();
            ((JFileChooser)localObject).setDialogTitle(paramString);
            ((JFileChooser)localObject).setFileSelectionMode(1);
            int i = ((JFileChooser)localObject).showOpenDialog(parentFrame);
            System.out.println(i);
            if (i == 1) {
              selectedFile = null;
            } else {
              selectedFile = ((JFileChooser)localObject).getSelectedFile();
            }
          }
        }
      });
      return selectedFile == null ? null : selectedFile.getAbsolutePath();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return null;
  }
  
  public BufferedReader createReader(String paramString)
  {
    try
    {
      InputStream localInputStream = createInput(paramString);
      if (localInputStream == null)
      {
        System.err.println(paramString + " does not exist or could not be read");
        return null;
      }
      return createReader(localInputStream);
    }
    catch (Exception localException)
    {
      if (paramString == null) {
        System.err.println("Filename passed to reader() was null");
      } else {
        System.err.println("Couldn't create a reader for " + paramString);
      }
    }
    return null;
  }
  
  public static BufferedReader createReader(File paramFile)
  {
    try
    {
      Object localObject = new FileInputStream(paramFile);
      if (paramFile.getName().toLowerCase().endsWith(".gz")) {
        localObject = new GZIPInputStream((InputStream)localObject);
      }
      return createReader((InputStream)localObject);
    }
    catch (Exception localException)
    {
      if (paramFile == null) {
        throw new RuntimeException("File passed to createReader() was null");
      }
      localException.printStackTrace();
      throw new RuntimeException("Couldn't create a reader for " + paramFile.getAbsolutePath());
    }
  }
  
  public static BufferedReader createReader(InputStream paramInputStream)
  {
    InputStreamReader localInputStreamReader = null;
    try
    {
      localInputStreamReader = new InputStreamReader(paramInputStream, "UTF-8");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
    return new BufferedReader(localInputStreamReader);
  }
  
  public PrintWriter createWriter(String paramString)
  {
    return createWriter(saveFile(paramString));
  }
  
  public static PrintWriter createWriter(File paramFile)
  {
    try
    {
      createPath(paramFile);
      Object localObject = new FileOutputStream(paramFile);
      if (paramFile.getName().toLowerCase().endsWith(".gz")) {
        localObject = new GZIPOutputStream((OutputStream)localObject);
      }
      return createWriter((OutputStream)localObject);
    }
    catch (Exception localException)
    {
      if (paramFile == null) {
        throw new RuntimeException("File passed to createWriter() was null");
      }
      localException.printStackTrace();
      throw new RuntimeException("Couldn't create a writer for " + paramFile.getAbsolutePath());
    }
  }
  
  public static PrintWriter createWriter(OutputStream paramOutputStream)
  {
    try
    {
      BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(paramOutputStream, 8192);
      OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(localBufferedOutputStream, "UTF-8");
      return new PrintWriter(localOutputStreamWriter);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
    return null;
  }
  
  /**
   * @deprecated
   */
  public InputStream openStream(String paramString)
  {
    return createInput(paramString);
  }
  
  public InputStream createInput(String paramString)
  {
    InputStream localInputStream = createInputRaw(paramString);
    if ((localInputStream != null) && (paramString.toLowerCase().endsWith(".gz"))) {
      try
      {
        return new GZIPInputStream(localInputStream);
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
        return null;
      }
    }
    return localInputStream;
  }
  
  public InputStream createInputRaw(String paramString)
  {
    Object localObject1 = null;
    if (paramString == null) {
      return null;
    }
    if (paramString.length() == 0) {
      return null;
    }
    if (paramString.indexOf(":") != -1) {
      try
      {
        URL localURL1 = new URL(paramString);
        localObject1 = localURL1.openStream();
        return localObject1;
      }
      catch (MalformedURLException localMalformedURLException) {}catch (FileNotFoundException localFileNotFoundException) {}catch (IOException localIOException1)
      {
        localIOException1.printStackTrace();
        return null;
      }
    }
    Object localObject3;
    Object localObject4;
    try
    {
      File localFile = new File(dataPath(paramString));
      if (!localFile.exists()) {
        localFile = new File(sketchPath, paramString);
      }
      if (localFile.isDirectory()) {
        return null;
      }
      if (localFile.exists()) {
        try
        {
          String str = localFile.getCanonicalPath();
          localObject3 = new File(str).getName();
          localObject4 = new File(paramString).getName();
          if (!((String)localObject3).equals(localObject4)) {
            throw new RuntimeException("This file is named " + (String)localObject3 + " not " + paramString + ". Rename the file " + "or change your code.");
          }
        }
        catch (IOException localIOException3) {}
      }
      localObject1 = new FileInputStream(localFile);
      if (localObject1 != null) {
        return localObject1;
      }
    }
    catch (IOException localIOException2) {}catch (SecurityException localSecurityException1) {}
    ClassLoader localClassLoader = getClass().getClassLoader();
    localObject1 = localClassLoader.getResourceAsStream("data/" + paramString);
    Object localObject2;
    if (localObject1 != null)
    {
      localObject2 = localObject1.getClass().getName();
      if (!((String)localObject2).equals("sun.plugin.cache.EmptyInputStream")) {
        return localObject1;
      }
    }
    localObject1 = localClassLoader.getResourceAsStream(paramString);
    if (localObject1 != null)
    {
      localObject2 = localObject1.getClass().getName();
      if (!((String)localObject2).equals("sun.plugin.cache.EmptyInputStream")) {
        return localObject1;
      }
    }
    try
    {
      localObject2 = getDocumentBase();
      if (localObject2 != null)
      {
        localObject3 = new URL((URL)localObject2, paramString);
        localObject4 = ((URL)localObject3).openConnection();
        return ((URLConnection)localObject4).getInputStream();
      }
    }
    catch (Exception localException1) {}
    try
    {
      URL localURL2 = getDocumentBase();
      if (localURL2 != null)
      {
        localObject3 = new URL(localURL2, "data/" + paramString);
        localObject4 = ((URL)localObject3).openConnection();
        return ((URLConnection)localObject4).getInputStream();
      }
    }
    catch (Exception localException2) {}
    try
    {
      try
      {
        try
        {
          localObject1 = new FileInputStream(dataPath(paramString));
          if (localObject1 != null) {
            return localObject1;
          }
        }
        catch (IOException localIOException4) {}
        try
        {
          localObject1 = new FileInputStream(sketchPath(paramString));
          if (localObject1 != null) {
            return localObject1;
          }
        }
        catch (Exception localException3) {}
        try
        {
          localObject1 = new FileInputStream(paramString);
          if (localObject1 != null) {
            return localObject1;
          }
        }
        catch (IOException localIOException5) {}
      }
      catch (SecurityException localSecurityException2) {}
    }
    catch (Exception localException4)
    {
      localException4.printStackTrace();
    }
    return null;
  }
  
  public static InputStream createInput(File paramFile)
  {
    if (paramFile == null) {
      throw new IllegalArgumentException("File passed to createInput() was null");
    }
    try
    {
      FileInputStream localFileInputStream = new FileInputStream(paramFile);
      if (paramFile.getName().toLowerCase().endsWith(".gz")) {
        return new GZIPInputStream(localFileInputStream);
      }
      return localFileInputStream;
    }
    catch (IOException localIOException)
    {
      System.err.println("Could not createInput() for " + paramFile);
      localIOException.printStackTrace();
    }
    return null;
  }
  
  public byte[] loadBytes(String paramString)
  {
    InputStream localInputStream = createInput(paramString);
    if (localInputStream != null) {
      return loadBytes(localInputStream);
    }
    System.err.println("The file \"" + paramString + "\" " + "is missing or inaccessible, make sure " + "the URL is valid or that the file has been " + "added to your sketch and is readable.");
    return null;
  }
  
  public static byte[] loadBytes(InputStream paramInputStream)
  {
    try
    {
      BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream);
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      for (int i = localBufferedInputStream.read(); i != -1; i = localBufferedInputStream.read()) {
        localByteArrayOutputStream.write(i);
      }
      return localByteArrayOutputStream.toByteArray();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return null;
  }
  
  public static byte[] loadBytes(File paramFile)
  {
    InputStream localInputStream = createInput(paramFile);
    return loadBytes(localInputStream);
  }
  
  public static String[] loadStrings(File paramFile)
  {
    InputStream localInputStream = createInput(paramFile);
    if (localInputStream != null) {
      return loadStrings(localInputStream);
    }
    return null;
  }
  
  public String[] loadStrings(String paramString)
  {
    InputStream localInputStream = createInput(paramString);
    if (localInputStream != null) {
      return loadStrings(localInputStream);
    }
    System.err.println("The file \"" + paramString + "\" " + "is missing or inaccessible, make sure " + "the URL is valid or that the file has been " + "added to your sketch and is readable.");
    return null;
  }
  
  public static String[] loadStrings(InputStream paramInputStream)
  {
    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream, "UTF-8"));
      Object localObject = new String[100];
      int i = 0;
      String str = null;
      while ((str = localBufferedReader.readLine()) != null)
      {
        if (i == localObject.length)
        {
          arrayOfString = new String[i << 1];
          System.arraycopy(localObject, 0, arrayOfString, 0, i);
          localObject = arrayOfString;
        }
        localObject[(i++)] = str;
      }
      localBufferedReader.close();
      if (i == localObject.length) {
        return localObject;
      }
      String[] arrayOfString = new String[i];
      System.arraycopy(localObject, 0, arrayOfString, 0, i);
      return arrayOfString;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return null;
  }
  
  public OutputStream createOutput(String paramString)
  {
    return createOutput(saveFile(paramString));
  }
  
  public static OutputStream createOutput(File paramFile)
  {
    try
    {
      createPath(paramFile);
      FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
      if (paramFile.getName().toLowerCase().endsWith(".gz")) {
        return new GZIPOutputStream(localFileOutputStream);
      }
      return localFileOutputStream;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return null;
  }
  
  public boolean saveStream(String paramString1, String paramString2)
  {
    return saveStream(saveFile(paramString1), paramString2);
  }
  
  public boolean saveStream(File paramFile, String paramString)
  {
    return saveStream(paramFile, createInputRaw(paramString));
  }
  
  public boolean saveStream(String paramString, InputStream paramInputStream)
  {
    return saveStream(saveFile(paramString), paramInputStream);
  }
  
  public static boolean saveStream(File paramFile, InputStream paramInputStream)
  {
    File localFile1 = null;
    try
    {
      File localFile2 = paramFile.getParentFile();
      createPath(paramFile);
      localFile1 = File.createTempFile(paramFile.getName(), null, localFile2);
      BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream, 16384);
      FileOutputStream localFileOutputStream = new FileOutputStream(localFile1);
      BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(localFileOutputStream);
      byte[] arrayOfByte = new byte[' '];
      int i;
      while ((i = localBufferedInputStream.read(arrayOfByte)) != -1) {
        localBufferedOutputStream.write(arrayOfByte, 0, i);
      }
      localBufferedOutputStream.flush();
      localBufferedOutputStream.close();
      localBufferedOutputStream = null;
      if (!localFile1.renameTo(paramFile))
      {
        System.err.println("Could not rename temporary file " + localFile1.getAbsolutePath());
        return false;
      }
      return true;
    }
    catch (IOException localIOException)
    {
      if (localFile1 != null) {
        localFile1.delete();
      }
      localIOException.printStackTrace();
    }
    return false;
  }
  
  public void saveBytes(String paramString, byte[] paramArrayOfByte)
  {
    saveBytes(saveFile(paramString), paramArrayOfByte);
  }
  
  public static void saveBytes(File paramFile, byte[] paramArrayOfByte)
  {
    File localFile1 = null;
    try
    {
      File localFile2 = paramFile.getParentFile();
      localFile1 = File.createTempFile(paramFile.getName(), null, localFile2);
      OutputStream localOutputStream = createOutput(localFile1);
      saveBytes(localOutputStream, paramArrayOfByte);
      localOutputStream.close();
      localOutputStream = null;
      if (!localFile1.renameTo(paramFile)) {
        System.err.println("Could not rename temporary file " + localFile1.getAbsolutePath());
      }
    }
    catch (IOException localIOException)
    {
      System.err.println("error saving bytes to " + paramFile);
      if (localFile1 != null) {
        localFile1.delete();
      }
      localIOException.printStackTrace();
    }
  }
  
  public static void saveBytes(OutputStream paramOutputStream, byte[] paramArrayOfByte)
  {
    try
    {
      paramOutputStream.write(paramArrayOfByte);
      paramOutputStream.flush();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }
  
  public void saveStrings(String paramString, String[] paramArrayOfString)
  {
    saveStrings(saveFile(paramString), paramArrayOfString);
  }
  
  public static void saveStrings(File paramFile, String[] paramArrayOfString)
  {
    saveStrings(createOutput(paramFile), paramArrayOfString);
  }
  
  public static void saveStrings(OutputStream paramOutputStream, String[] paramArrayOfString)
  {
    PrintWriter localPrintWriter = createWriter(paramOutputStream);
    for (int i = 0; i < paramArrayOfString.length; i++) {
      localPrintWriter.println(paramArrayOfString[i]);
    }
    localPrintWriter.flush();
    localPrintWriter.close();
  }
  
  public String sketchPath(String paramString)
  {
    if (sketchPath == null) {
      return paramString;
    }
    try
    {
      if (new File(paramString).isAbsolute()) {
        return paramString;
      }
    }
    catch (Exception localException) {}
    return sketchPath + File.separator + paramString;
  }
  
  public File sketchFile(String paramString)
  {
    return new File(sketchPath(paramString));
  }
  
  public String savePath(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    String str = sketchPath(paramString);
    createPath(str);
    return str;
  }
  
  public File saveFile(String paramString)
  {
    return new File(savePath(paramString));
  }
  
  public String dataPath(String paramString)
  {
    if (new File(paramString).isAbsolute()) {
      return paramString;
    }
    return sketchPath + File.separator + "data" + File.separator + paramString;
  }
  
  public File dataFile(String paramString)
  {
    return new File(dataPath(paramString));
  }
  
  public static void createPath(String paramString)
  {
    createPath(new File(paramString));
  }
  
  public static void createPath(File paramFile)
  {
    try
    {
      String str = paramFile.getParent();
      if (str != null)
      {
        File localFile = new File(str);
        if (!localFile.exists()) {
          localFile.mkdirs();
        }
      }
    }
    catch (SecurityException localSecurityException)
    {
      System.err.println("You don't have permissions to create " + paramFile.getAbsolutePath());
    }
  }
  
  public static byte[] sort(byte[] paramArrayOfByte)
  {
    return sort(paramArrayOfByte, paramArrayOfByte.length);
  }
  
  public static byte[] sort(byte[] paramArrayOfByte, int paramInt)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte.length);
    Arrays.sort(arrayOfByte, 0, paramInt);
    return arrayOfByte;
  }
  
  public static char[] sort(char[] paramArrayOfChar)
  {
    return sort(paramArrayOfChar, paramArrayOfChar.length);
  }
  
  public static char[] sort(char[] paramArrayOfChar, int paramInt)
  {
    char[] arrayOfChar = new char[paramArrayOfChar.length];
    System.arraycopy(paramArrayOfChar, 0, arrayOfChar, 0, paramArrayOfChar.length);
    Arrays.sort(arrayOfChar, 0, paramInt);
    return arrayOfChar;
  }
  
  public static int[] sort(int[] paramArrayOfInt)
  {
    return sort(paramArrayOfInt, paramArrayOfInt.length);
  }
  
  public static int[] sort(int[] paramArrayOfInt, int paramInt)
  {
    int[] arrayOfInt = new int[paramArrayOfInt.length];
    System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, paramArrayOfInt.length);
    Arrays.sort(arrayOfInt, 0, paramInt);
    return arrayOfInt;
  }
  
  public static float[] sort(float[] paramArrayOfFloat)
  {
    return sort(paramArrayOfFloat, paramArrayOfFloat.length);
  }
  
  public static float[] sort(float[] paramArrayOfFloat, int paramInt)
  {
    float[] arrayOfFloat = new float[paramArrayOfFloat.length];
    System.arraycopy(paramArrayOfFloat, 0, arrayOfFloat, 0, paramArrayOfFloat.length);
    Arrays.sort(arrayOfFloat, 0, paramInt);
    return arrayOfFloat;
  }
  
  public static String[] sort(String[] paramArrayOfString)
  {
    return sort(paramArrayOfString, paramArrayOfString.length);
  }
  
  public static String[] sort(String[] paramArrayOfString, int paramInt)
  {
    String[] arrayOfString = new String[paramArrayOfString.length];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramArrayOfString.length);
    Arrays.sort(arrayOfString, 0, paramInt);
    return arrayOfString;
  }
  
  public static void arrayCopy(Object paramObject1, int paramInt1, Object paramObject2, int paramInt2, int paramInt3)
  {
    System.arraycopy(paramObject1, paramInt1, paramObject2, paramInt2, paramInt3);
  }
  
  public static void arrayCopy(Object paramObject1, Object paramObject2, int paramInt)
  {
    System.arraycopy(paramObject1, 0, paramObject2, 0, paramInt);
  }
  
  public static void arrayCopy(Object paramObject1, Object paramObject2)
  {
    System.arraycopy(paramObject1, 0, paramObject2, 0, Array.getLength(paramObject1));
  }
  
  /**
   * @deprecated
   */
  public static void arraycopy(Object paramObject1, int paramInt1, Object paramObject2, int paramInt2, int paramInt3)
  {
    System.arraycopy(paramObject1, paramInt1, paramObject2, paramInt2, paramInt3);
  }
  
  /**
   * @deprecated
   */
  public static void arraycopy(Object paramObject1, Object paramObject2, int paramInt)
  {
    System.arraycopy(paramObject1, 0, paramObject2, 0, paramInt);
  }
  
  /**
   * @deprecated
   */
  public static void arraycopy(Object paramObject1, Object paramObject2)
  {
    System.arraycopy(paramObject1, 0, paramObject2, 0, Array.getLength(paramObject1));
  }
  
  public static boolean[] expand(boolean[] paramArrayOfBoolean)
  {
    return expand(paramArrayOfBoolean, paramArrayOfBoolean.length << 1);
  }
  
  public static boolean[] expand(boolean[] paramArrayOfBoolean, int paramInt)
  {
    boolean[] arrayOfBoolean = new boolean[paramInt];
    System.arraycopy(paramArrayOfBoolean, 0, arrayOfBoolean, 0, Math.min(paramInt, paramArrayOfBoolean.length));
    return arrayOfBoolean;
  }
  
  public static byte[] expand(byte[] paramArrayOfByte)
  {
    return expand(paramArrayOfByte, paramArrayOfByte.length << 1);
  }
  
  public static byte[] expand(byte[] paramArrayOfByte, int paramInt)
  {
    byte[] arrayOfByte = new byte[paramInt];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, Math.min(paramInt, paramArrayOfByte.length));
    return arrayOfByte;
  }
  
  public static char[] expand(char[] paramArrayOfChar)
  {
    return expand(paramArrayOfChar, paramArrayOfChar.length << 1);
  }
  
  public static char[] expand(char[] paramArrayOfChar, int paramInt)
  {
    char[] arrayOfChar = new char[paramInt];
    System.arraycopy(paramArrayOfChar, 0, arrayOfChar, 0, Math.min(paramInt, paramArrayOfChar.length));
    return arrayOfChar;
  }
  
  public static int[] expand(int[] paramArrayOfInt)
  {
    return expand(paramArrayOfInt, paramArrayOfInt.length << 1);
  }
  
  public static int[] expand(int[] paramArrayOfInt, int paramInt)
  {
    int[] arrayOfInt = new int[paramInt];
    System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, Math.min(paramInt, paramArrayOfInt.length));
    return arrayOfInt;
  }
  
  public static float[] expand(float[] paramArrayOfFloat)
  {
    return expand(paramArrayOfFloat, paramArrayOfFloat.length << 1);
  }
  
  public static float[] expand(float[] paramArrayOfFloat, int paramInt)
  {
    float[] arrayOfFloat = new float[paramInt];
    System.arraycopy(paramArrayOfFloat, 0, arrayOfFloat, 0, Math.min(paramInt, paramArrayOfFloat.length));
    return arrayOfFloat;
  }
  
  public static String[] expand(String[] paramArrayOfString)
  {
    return expand(paramArrayOfString, paramArrayOfString.length << 1);
  }
  
  public static String[] expand(String[] paramArrayOfString, int paramInt)
  {
    String[] arrayOfString = new String[paramInt];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, Math.min(paramInt, paramArrayOfString.length));
    return arrayOfString;
  }
  
  public static Object expand(Object paramObject)
  {
    return expand(paramObject, Array.getLength(paramObject) << 1);
  }
  
  public static Object expand(Object paramObject, int paramInt)
  {
    Class localClass = paramObject.getClass().getComponentType();
    Object localObject = Array.newInstance(localClass, paramInt);
    System.arraycopy(paramObject, 0, localObject, 0, Math.min(Array.getLength(paramObject), paramInt));
    return localObject;
  }
  
  public static byte[] append(byte[] paramArrayOfByte, byte paramByte)
  {
    paramArrayOfByte = expand(paramArrayOfByte, paramArrayOfByte.length + 1);
    paramArrayOfByte[(paramArrayOfByte.length - 1)] = paramByte;
    return paramArrayOfByte;
  }
  
  public static char[] append(char[] paramArrayOfChar, char paramChar)
  {
    paramArrayOfChar = expand(paramArrayOfChar, paramArrayOfChar.length + 1);
    paramArrayOfChar[(paramArrayOfChar.length - 1)] = paramChar;
    return paramArrayOfChar;
  }
  
  public static int[] append(int[] paramArrayOfInt, int paramInt)
  {
    paramArrayOfInt = expand(paramArrayOfInt, paramArrayOfInt.length + 1);
    paramArrayOfInt[(paramArrayOfInt.length - 1)] = paramInt;
    return paramArrayOfInt;
  }
  
  public static float[] append(float[] paramArrayOfFloat, float paramFloat)
  {
    paramArrayOfFloat = expand(paramArrayOfFloat, paramArrayOfFloat.length + 1);
    paramArrayOfFloat[(paramArrayOfFloat.length - 1)] = paramFloat;
    return paramArrayOfFloat;
  }
  
  public static String[] append(String[] paramArrayOfString, String paramString)
  {
    paramArrayOfString = expand(paramArrayOfString, paramArrayOfString.length + 1);
    paramArrayOfString[(paramArrayOfString.length - 1)] = paramString;
    return paramArrayOfString;
  }
  
  public static Object append(Object paramObject1, Object paramObject2)
  {
    int i = Array.getLength(paramObject1);
    paramObject1 = expand(paramObject1, i + 1);
    Array.set(paramObject1, i, paramObject2);
    return paramObject1;
  }
  
  public static boolean[] shorten(boolean[] paramArrayOfBoolean)
  {
    return subset(paramArrayOfBoolean, 0, paramArrayOfBoolean.length - 1);
  }
  
  public static byte[] shorten(byte[] paramArrayOfByte)
  {
    return subset(paramArrayOfByte, 0, paramArrayOfByte.length - 1);
  }
  
  public static char[] shorten(char[] paramArrayOfChar)
  {
    return subset(paramArrayOfChar, 0, paramArrayOfChar.length - 1);
  }
  
  public static int[] shorten(int[] paramArrayOfInt)
  {
    return subset(paramArrayOfInt, 0, paramArrayOfInt.length - 1);
  }
  
  public static float[] shorten(float[] paramArrayOfFloat)
  {
    return subset(paramArrayOfFloat, 0, paramArrayOfFloat.length - 1);
  }
  
  public static String[] shorten(String[] paramArrayOfString)
  {
    return subset(paramArrayOfString, 0, paramArrayOfString.length - 1);
  }
  
  public static Object shorten(Object paramObject)
  {
    int i = Array.getLength(paramObject);
    return subset(paramObject, 0, i - 1);
  }
  
  public static final boolean[] splice(boolean[] paramArrayOfBoolean, boolean paramBoolean, int paramInt)
  {
    boolean[] arrayOfBoolean = new boolean[paramArrayOfBoolean.length + 1];
    System.arraycopy(paramArrayOfBoolean, 0, arrayOfBoolean, 0, paramInt);
    arrayOfBoolean[paramInt] = paramBoolean;
    System.arraycopy(paramArrayOfBoolean, paramInt, arrayOfBoolean, paramInt + 1, paramArrayOfBoolean.length - paramInt);
    return arrayOfBoolean;
  }
  
  public static final boolean[] splice(boolean[] paramArrayOfBoolean1, boolean[] paramArrayOfBoolean2, int paramInt)
  {
    boolean[] arrayOfBoolean = new boolean[paramArrayOfBoolean1.length + paramArrayOfBoolean2.length];
    System.arraycopy(paramArrayOfBoolean1, 0, arrayOfBoolean, 0, paramInt);
    System.arraycopy(paramArrayOfBoolean2, 0, arrayOfBoolean, paramInt, paramArrayOfBoolean2.length);
    System.arraycopy(paramArrayOfBoolean1, paramInt, arrayOfBoolean, paramInt + paramArrayOfBoolean2.length, paramArrayOfBoolean1.length - paramInt);
    return arrayOfBoolean;
  }
  
  public static final byte[] splice(byte[] paramArrayOfByte, byte paramByte, int paramInt)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length + 1];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramInt);
    arrayOfByte[paramInt] = paramByte;
    System.arraycopy(paramArrayOfByte, paramInt, arrayOfByte, paramInt + 1, paramArrayOfByte.length - paramInt);
    return arrayOfByte;
  }
  
  public static final byte[] splice(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte1.length + paramArrayOfByte2.length];
    System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, paramInt);
    System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, paramInt, paramArrayOfByte2.length);
    System.arraycopy(paramArrayOfByte1, paramInt, arrayOfByte, paramInt + paramArrayOfByte2.length, paramArrayOfByte1.length - paramInt);
    return arrayOfByte;
  }
  
  public static final char[] splice(char[] paramArrayOfChar, char paramChar, int paramInt)
  {
    char[] arrayOfChar = new char[paramArrayOfChar.length + 1];
    System.arraycopy(paramArrayOfChar, 0, arrayOfChar, 0, paramInt);
    arrayOfChar[paramInt] = paramChar;
    System.arraycopy(paramArrayOfChar, paramInt, arrayOfChar, paramInt + 1, paramArrayOfChar.length - paramInt);
    return arrayOfChar;
  }
  
  public static final char[] splice(char[] paramArrayOfChar1, char[] paramArrayOfChar2, int paramInt)
  {
    char[] arrayOfChar = new char[paramArrayOfChar1.length + paramArrayOfChar2.length];
    System.arraycopy(paramArrayOfChar1, 0, arrayOfChar, 0, paramInt);
    System.arraycopy(paramArrayOfChar2, 0, arrayOfChar, paramInt, paramArrayOfChar2.length);
    System.arraycopy(paramArrayOfChar1, paramInt, arrayOfChar, paramInt + paramArrayOfChar2.length, paramArrayOfChar1.length - paramInt);
    return arrayOfChar;
  }
  
  public static final int[] splice(int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = new int[paramArrayOfInt.length + 1];
    System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, paramInt2);
    arrayOfInt[paramInt2] = paramInt1;
    System.arraycopy(paramArrayOfInt, paramInt2, arrayOfInt, paramInt2 + 1, paramArrayOfInt.length - paramInt2);
    return arrayOfInt;
  }
  
  public static final int[] splice(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
  {
    int[] arrayOfInt = new int[paramArrayOfInt1.length + paramArrayOfInt2.length];
    System.arraycopy(paramArrayOfInt1, 0, arrayOfInt, 0, paramInt);
    System.arraycopy(paramArrayOfInt2, 0, arrayOfInt, paramInt, paramArrayOfInt2.length);
    System.arraycopy(paramArrayOfInt1, paramInt, arrayOfInt, paramInt + paramArrayOfInt2.length, paramArrayOfInt1.length - paramInt);
    return arrayOfInt;
  }
  
  public static final float[] splice(float[] paramArrayOfFloat, float paramFloat, int paramInt)
  {
    float[] arrayOfFloat = new float[paramArrayOfFloat.length + 1];
    System.arraycopy(paramArrayOfFloat, 0, arrayOfFloat, 0, paramInt);
    arrayOfFloat[paramInt] = paramFloat;
    System.arraycopy(paramArrayOfFloat, paramInt, arrayOfFloat, paramInt + 1, paramArrayOfFloat.length - paramInt);
    return arrayOfFloat;
  }
  
  public static final float[] splice(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, int paramInt)
  {
    float[] arrayOfFloat = new float[paramArrayOfFloat1.length + paramArrayOfFloat2.length];
    System.arraycopy(paramArrayOfFloat1, 0, arrayOfFloat, 0, paramInt);
    System.arraycopy(paramArrayOfFloat2, 0, arrayOfFloat, paramInt, paramArrayOfFloat2.length);
    System.arraycopy(paramArrayOfFloat1, paramInt, arrayOfFloat, paramInt + paramArrayOfFloat2.length, paramArrayOfFloat1.length - paramInt);
    return arrayOfFloat;
  }
  
  public static final String[] splice(String[] paramArrayOfString, String paramString, int paramInt)
  {
    String[] arrayOfString = new String[paramArrayOfString.length + 1];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramInt);
    arrayOfString[paramInt] = paramString;
    System.arraycopy(paramArrayOfString, paramInt, arrayOfString, paramInt + 1, paramArrayOfString.length - paramInt);
    return arrayOfString;
  }
  
  public static final String[] splice(String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt)
  {
    String[] arrayOfString = new String[paramArrayOfString1.length + paramArrayOfString2.length];
    System.arraycopy(paramArrayOfString1, 0, arrayOfString, 0, paramInt);
    System.arraycopy(paramArrayOfString2, 0, arrayOfString, paramInt, paramArrayOfString2.length);
    System.arraycopy(paramArrayOfString1, paramInt, arrayOfString, paramInt + paramArrayOfString2.length, paramArrayOfString1.length - paramInt);
    return arrayOfString;
  }
  
  public static final Object splice(Object paramObject1, Object paramObject2, int paramInt)
  {
    Object[] arrayOfObject = null;
    int i = Array.getLength(paramObject1);
    if (paramObject2.getClass().getName().charAt(0) == '[')
    {
      int j = Array.getLength(paramObject2);
      arrayOfObject = new Object[i + j];
      System.arraycopy(paramObject1, 0, arrayOfObject, 0, paramInt);
      System.arraycopy(paramObject2, 0, arrayOfObject, paramInt, j);
      System.arraycopy(paramObject1, paramInt, arrayOfObject, paramInt + j, i - paramInt);
    }
    else
    {
      arrayOfObject = new Object[i + 1];
      System.arraycopy(paramObject1, 0, arrayOfObject, 0, paramInt);
      Array.set(arrayOfObject, paramInt, paramObject2);
      System.arraycopy(paramObject1, paramInt, arrayOfObject, paramInt + 1, i - paramInt);
    }
    return arrayOfObject;
  }
  
  public static boolean[] subset(boolean[] paramArrayOfBoolean, int paramInt)
  {
    return subset(paramArrayOfBoolean, paramInt, paramArrayOfBoolean.length - paramInt);
  }
  
  public static boolean[] subset(boolean[] paramArrayOfBoolean, int paramInt1, int paramInt2)
  {
    boolean[] arrayOfBoolean = new boolean[paramInt2];
    System.arraycopy(paramArrayOfBoolean, paramInt1, arrayOfBoolean, 0, paramInt2);
    return arrayOfBoolean;
  }
  
  public static byte[] subset(byte[] paramArrayOfByte, int paramInt)
  {
    return subset(paramArrayOfByte, paramInt, paramArrayOfByte.length - paramInt);
  }
  
  public static byte[] subset(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte = new byte[paramInt2];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
    return arrayOfByte;
  }
  
  public static char[] subset(char[] paramArrayOfChar, int paramInt)
  {
    return subset(paramArrayOfChar, paramInt, paramArrayOfChar.length - paramInt);
  }
  
  public static char[] subset(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    char[] arrayOfChar = new char[paramInt2];
    System.arraycopy(paramArrayOfChar, paramInt1, arrayOfChar, 0, paramInt2);
    return arrayOfChar;
  }
  
  public static int[] subset(int[] paramArrayOfInt, int paramInt)
  {
    return subset(paramArrayOfInt, paramInt, paramArrayOfInt.length - paramInt);
  }
  
  public static int[] subset(int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = new int[paramInt2];
    System.arraycopy(paramArrayOfInt, paramInt1, arrayOfInt, 0, paramInt2);
    return arrayOfInt;
  }
  
  public static float[] subset(float[] paramArrayOfFloat, int paramInt)
  {
    return subset(paramArrayOfFloat, paramInt, paramArrayOfFloat.length - paramInt);
  }
  
  public static float[] subset(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
  {
    float[] arrayOfFloat = new float[paramInt2];
    System.arraycopy(paramArrayOfFloat, paramInt1, arrayOfFloat, 0, paramInt2);
    return arrayOfFloat;
  }
  
  public static String[] subset(String[] paramArrayOfString, int paramInt)
  {
    return subset(paramArrayOfString, paramInt, paramArrayOfString.length - paramInt);
  }
  
  public static String[] subset(String[] paramArrayOfString, int paramInt1, int paramInt2)
  {
    String[] arrayOfString = new String[paramInt2];
    System.arraycopy(paramArrayOfString, paramInt1, arrayOfString, 0, paramInt2);
    return arrayOfString;
  }
  
  public static Object subset(Object paramObject, int paramInt)
  {
    int i = Array.getLength(paramObject);
    return subset(paramObject, paramInt, i - paramInt);
  }
  
  public static Object subset(Object paramObject, int paramInt1, int paramInt2)
  {
    Class localClass = paramObject.getClass().getComponentType();
    Object localObject = Array.newInstance(localClass, paramInt2);
    System.arraycopy(paramObject, paramInt1, localObject, 0, paramInt2);
    return localObject;
  }
  
  public static boolean[] concat(boolean[] paramArrayOfBoolean1, boolean[] paramArrayOfBoolean2)
  {
    boolean[] arrayOfBoolean = new boolean[paramArrayOfBoolean1.length + paramArrayOfBoolean2.length];
    System.arraycopy(paramArrayOfBoolean1, 0, arrayOfBoolean, 0, paramArrayOfBoolean1.length);
    System.arraycopy(paramArrayOfBoolean2, 0, arrayOfBoolean, paramArrayOfBoolean1.length, paramArrayOfBoolean2.length);
    return arrayOfBoolean;
  }
  
  public static byte[] concat(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte1.length + paramArrayOfByte2.length];
    System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, paramArrayOfByte1.length);
    System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, paramArrayOfByte1.length, paramArrayOfByte2.length);
    return arrayOfByte;
  }
  
  public static char[] concat(char[] paramArrayOfChar1, char[] paramArrayOfChar2)
  {
    char[] arrayOfChar = new char[paramArrayOfChar1.length + paramArrayOfChar2.length];
    System.arraycopy(paramArrayOfChar1, 0, arrayOfChar, 0, paramArrayOfChar1.length);
    System.arraycopy(paramArrayOfChar2, 0, arrayOfChar, paramArrayOfChar1.length, paramArrayOfChar2.length);
    return arrayOfChar;
  }
  
  public static int[] concat(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    int[] arrayOfInt = new int[paramArrayOfInt1.length + paramArrayOfInt2.length];
    System.arraycopy(paramArrayOfInt1, 0, arrayOfInt, 0, paramArrayOfInt1.length);
    System.arraycopy(paramArrayOfInt2, 0, arrayOfInt, paramArrayOfInt1.length, paramArrayOfInt2.length);
    return arrayOfInt;
  }
  
  public static float[] concat(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    float[] arrayOfFloat = new float[paramArrayOfFloat1.length + paramArrayOfFloat2.length];
    System.arraycopy(paramArrayOfFloat1, 0, arrayOfFloat, 0, paramArrayOfFloat1.length);
    System.arraycopy(paramArrayOfFloat2, 0, arrayOfFloat, paramArrayOfFloat1.length, paramArrayOfFloat2.length);
    return arrayOfFloat;
  }
  
  public static String[] concat(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    String[] arrayOfString = new String[paramArrayOfString1.length + paramArrayOfString2.length];
    System.arraycopy(paramArrayOfString1, 0, arrayOfString, 0, paramArrayOfString1.length);
    System.arraycopy(paramArrayOfString2, 0, arrayOfString, paramArrayOfString1.length, paramArrayOfString2.length);
    return arrayOfString;
  }
  
  public static Object concat(Object paramObject1, Object paramObject2)
  {
    Class localClass = paramObject1.getClass().getComponentType();
    int i = Array.getLength(paramObject1);
    int j = Array.getLength(paramObject2);
    Object localObject = Array.newInstance(localClass, i + j);
    System.arraycopy(paramObject1, 0, localObject, 0, i);
    System.arraycopy(paramObject2, 0, localObject, i, j);
    return localObject;
  }
  
  public static boolean[] reverse(boolean[] paramArrayOfBoolean)
  {
    boolean[] arrayOfBoolean = new boolean[paramArrayOfBoolean.length];
    int i = paramArrayOfBoolean.length - 1;
    for (int j = 0; j < paramArrayOfBoolean.length; j++) {
      arrayOfBoolean[j] = paramArrayOfBoolean[(i - j)];
    }
    return arrayOfBoolean;
  }
  
  public static byte[] reverse(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    int i = paramArrayOfByte.length - 1;
    for (int j = 0; j < paramArrayOfByte.length; j++) {
      arrayOfByte[j] = paramArrayOfByte[(i - j)];
    }
    return arrayOfByte;
  }
  
  public static char[] reverse(char[] paramArrayOfChar)
  {
    char[] arrayOfChar = new char[paramArrayOfChar.length];
    int i = paramArrayOfChar.length - 1;
    for (int j = 0; j < paramArrayOfChar.length; j++) {
      arrayOfChar[j] = paramArrayOfChar[(i - j)];
    }
    return arrayOfChar;
  }
  
  public static int[] reverse(int[] paramArrayOfInt)
  {
    int[] arrayOfInt = new int[paramArrayOfInt.length];
    int i = paramArrayOfInt.length - 1;
    for (int j = 0; j < paramArrayOfInt.length; j++) {
      arrayOfInt[j] = paramArrayOfInt[(i - j)];
    }
    return arrayOfInt;
  }
  
  public static float[] reverse(float[] paramArrayOfFloat)
  {
    float[] arrayOfFloat = new float[paramArrayOfFloat.length];
    int i = paramArrayOfFloat.length - 1;
    for (int j = 0; j < paramArrayOfFloat.length; j++) {
      arrayOfFloat[j] = paramArrayOfFloat[(i - j)];
    }
    return arrayOfFloat;
  }
  
  public static String[] reverse(String[] paramArrayOfString)
  {
    String[] arrayOfString = new String[paramArrayOfString.length];
    int i = paramArrayOfString.length - 1;
    for (int j = 0; j < paramArrayOfString.length; j++) {
      arrayOfString[j] = paramArrayOfString[(i - j)];
    }
    return arrayOfString;
  }
  
  public static Object reverse(Object paramObject)
  {
    Class localClass = paramObject.getClass().getComponentType();
    int i = Array.getLength(paramObject);
    Object localObject = Array.newInstance(localClass, i);
    for (int j = 0; j < i; j++) {
      Array.set(localObject, j, Array.get(paramObject, i - 1 - j));
    }
    return localObject;
  }
  
  public static String trim(String paramString)
  {
    return paramString.replace(' ', ' ').trim();
  }
  
  public static String[] trim(String[] paramArrayOfString)
  {
    String[] arrayOfString = new String[paramArrayOfString.length];
    for (int i = 0; i < paramArrayOfString.length; i++) {
      if (paramArrayOfString[i] != null) {
        arrayOfString[i] = paramArrayOfString[i].replace(' ', ' ').trim();
      }
    }
    return arrayOfString;
  }
  
  public static String join(String[] paramArrayOfString, char paramChar)
  {
    return join(paramArrayOfString, String.valueOf(paramChar));
  }
  
  public static String join(String[] paramArrayOfString, String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      if (i != 0) {
        localStringBuffer.append(paramString);
      }
      localStringBuffer.append(paramArrayOfString[i]);
    }
    return localStringBuffer.toString();
  }
  
  public static String[] splitTokens(String paramString)
  {
    return splitTokens(paramString, " \t\n\r\f ");
  }
  
  public static String[] splitTokens(String paramString1, String paramString2)
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString1, paramString2);
    String[] arrayOfString = new String[localStringTokenizer.countTokens()];
    int i = 0;
    while (localStringTokenizer.hasMoreTokens()) {
      arrayOfString[(i++)] = localStringTokenizer.nextToken();
    }
    return arrayOfString;
  }
  
  public static String[] split(String paramString, char paramChar)
  {
    if (paramString == null) {
      return null;
    }
    char[] arrayOfChar = paramString.toCharArray();
    int i = 0;
    for (int j = 0; j < arrayOfChar.length; j++) {
      if (arrayOfChar[j] == paramChar) {
        i++;
      }
    }
    if (i == 0)
    {
      arrayOfString = new String[1];
      arrayOfString[0] = new String(paramString);
      return arrayOfString;
    }
    String[] arrayOfString = new String[i + 1];
    int k = 0;
    int m = 0;
    for (int n = 0; n < arrayOfChar.length; n++) {
      if (arrayOfChar[n] == paramChar)
      {
        arrayOfString[(k++)] = new String(arrayOfChar, m, n - m);
        m = n + 1;
      }
    }
    arrayOfString[k] = new String(arrayOfChar, m, arrayOfChar.length - m);
    return arrayOfString;
  }
  
  public static String[] split(String paramString1, String paramString2)
  {
    ArrayList localArrayList = new ArrayList();
    int i;
    for (int j = 0; (i = paramString1.indexOf(paramString2, j)) != -1; j = i + paramString2.length()) {
      localArrayList.add(paramString1.substring(j, i));
    }
    localArrayList.add(paramString1.substring(j));
    String[] arrayOfString = new String[localArrayList.size()];
    localArrayList.toArray(arrayOfString);
    return arrayOfString;
  }
  
  static Pattern matchPattern(String paramString)
  {
    Pattern localPattern = null;
    if (matchPatterns == null) {
      matchPatterns = new HashMap();
    } else {
      localPattern = (Pattern)matchPatterns.get(paramString);
    }
    if (localPattern == null)
    {
      if (matchPatterns.size() == 10) {
        matchPatterns.clear();
      }
      localPattern = Pattern.compile(paramString, 40);
      matchPatterns.put(paramString, localPattern);
    }
    return localPattern;
  }
  
  public static String[] match(String paramString1, String paramString2)
  {
    Pattern localPattern = matchPattern(paramString2);
    Matcher localMatcher = localPattern.matcher(paramString1);
    if (localMatcher.find())
    {
      int i = localMatcher.groupCount() + 1;
      String[] arrayOfString = new String[i];
      for (int j = 0; j < i; j++) {
        arrayOfString[j] = localMatcher.group(j);
      }
      return arrayOfString;
    }
    return null;
  }
  
  public static String[][] matchAll(String paramString1, String paramString2)
  {
    Pattern localPattern = matchPattern(paramString2);
    Matcher localMatcher = localPattern.matcher(paramString1);
    ArrayList localArrayList = new ArrayList();
    int i = localMatcher.groupCount() + 1;
    while (localMatcher.find())
    {
      localObject = new String[i];
      for (j = 0; j < i; j++) {
        localObject[j] = localMatcher.group(j);
      }
      localArrayList.add(localObject);
    }
    if (localArrayList.isEmpty()) {
      return (String[][])null;
    }
    Object localObject = new String[localArrayList.size()][i];
    for (int j = 0; j < localObject.length; j++) {
      localObject[j] = ((String[])(String[])localArrayList.get(j));
    }
    return localObject;
  }
  
  public static final boolean parseBoolean(int paramInt)
  {
    return paramInt != 0;
  }
  
  public static final boolean parseBoolean(String paramString)
  {
    return new Boolean(paramString).booleanValue();
  }
  
  public static final boolean[] parseBoolean(int[] paramArrayOfInt)
  {
    boolean[] arrayOfBoolean = new boolean[paramArrayOfInt.length];
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      arrayOfBoolean[i] = (paramArrayOfInt[i] != 0 ? 1 : false);
    }
    return arrayOfBoolean;
  }
  
  public static final boolean[] parseBoolean(String[] paramArrayOfString)
  {
    boolean[] arrayOfBoolean = new boolean[paramArrayOfString.length];
    for (int i = 0; i < paramArrayOfString.length; i++) {
      arrayOfBoolean[i] = new Boolean(paramArrayOfString[i]).booleanValue();
    }
    return arrayOfBoolean;
  }
  
  public static final byte parseByte(boolean paramBoolean)
  {
    return paramBoolean ? 1 : 0;
  }
  
  public static final byte parseByte(char paramChar)
  {
    return (byte)paramChar;
  }
  
  public static final byte parseByte(int paramInt)
  {
    return (byte)paramInt;
  }
  
  public static final byte parseByte(float paramFloat)
  {
    return (byte)(int)paramFloat;
  }
  
  public static final byte[] parseByte(boolean[] paramArrayOfBoolean)
  {
    byte[] arrayOfByte = new byte[paramArrayOfBoolean.length];
    for (int i = 0; i < paramArrayOfBoolean.length; i++) {
      arrayOfByte[i] = (paramArrayOfBoolean[i] != 0 ? 1 : 0);
    }
    return arrayOfByte;
  }
  
  public static final byte[] parseByte(char[] paramArrayOfChar)
  {
    byte[] arrayOfByte = new byte[paramArrayOfChar.length];
    for (int i = 0; i < paramArrayOfChar.length; i++) {
      arrayOfByte[i] = ((byte)paramArrayOfChar[i]);
    }
    return arrayOfByte;
  }
  
  public static final byte[] parseByte(int[] paramArrayOfInt)
  {
    byte[] arrayOfByte = new byte[paramArrayOfInt.length];
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      arrayOfByte[i] = ((byte)paramArrayOfInt[i]);
    }
    return arrayOfByte;
  }
  
  public static final byte[] parseByte(float[] paramArrayOfFloat)
  {
    byte[] arrayOfByte = new byte[paramArrayOfFloat.length];
    for (int i = 0; i < paramArrayOfFloat.length; i++) {
      arrayOfByte[i] = ((byte)(int)paramArrayOfFloat[i]);
    }
    return arrayOfByte;
  }
  
  public static final char parseChar(byte paramByte)
  {
    return (char)(paramByte & 0xFF);
  }
  
  public static final char parseChar(int paramInt)
  {
    return (char)paramInt;
  }
  
  public static final char[] parseChar(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length];
    for (int i = 0; i < paramArrayOfByte.length; i++) {
      arrayOfChar[i] = ((char)(paramArrayOfByte[i] & 0xFF));
    }
    return arrayOfChar;
  }
  
  public static final char[] parseChar(int[] paramArrayOfInt)
  {
    char[] arrayOfChar = new char[paramArrayOfInt.length];
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      arrayOfChar[i] = ((char)paramArrayOfInt[i]);
    }
    return arrayOfChar;
  }
  
  public static final int parseInt(boolean paramBoolean)
  {
    return paramBoolean ? 1 : 0;
  }
  
  public static final int parseInt(byte paramByte)
  {
    return paramByte & 0xFF;
  }
  
  public static final int parseInt(char paramChar)
  {
    return paramChar;
  }
  
  public static final int parseInt(float paramFloat)
  {
    return (int)paramFloat;
  }
  
  public static final int parseInt(String paramString)
  {
    return parseInt(paramString, 0);
  }
  
  public static final int parseInt(String paramString, int paramInt)
  {
    try
    {
      int i = paramString.indexOf('.');
      if (i == -1) {
        return Integer.parseInt(paramString);
      }
      return Integer.parseInt(paramString.substring(0, i));
    }
    catch (NumberFormatException localNumberFormatException) {}
    return paramInt;
  }
  
  public static final int[] parseInt(boolean[] paramArrayOfBoolean)
  {
    int[] arrayOfInt = new int[paramArrayOfBoolean.length];
    for (int i = 0; i < paramArrayOfBoolean.length; i++) {
      arrayOfInt[i] = (paramArrayOfBoolean[i] != 0 ? 1 : 0);
    }
    return arrayOfInt;
  }
  
  public static final int[] parseInt(byte[] paramArrayOfByte)
  {
    int[] arrayOfInt = new int[paramArrayOfByte.length];
    for (int i = 0; i < paramArrayOfByte.length; i++) {
      paramArrayOfByte[i] &= 0xFF;
    }
    return arrayOfInt;
  }
  
  public static final int[] parseInt(char[] paramArrayOfChar)
  {
    int[] arrayOfInt = new int[paramArrayOfChar.length];
    for (int i = 0; i < paramArrayOfChar.length; i++) {
      arrayOfInt[i] = paramArrayOfChar[i];
    }
    return arrayOfInt;
  }
  
  public static int[] parseInt(float[] paramArrayOfFloat)
  {
    int[] arrayOfInt = new int[paramArrayOfFloat.length];
    for (int i = 0; i < paramArrayOfFloat.length; i++) {
      arrayOfInt[i] = ((int)paramArrayOfFloat[i]);
    }
    return arrayOfInt;
  }
  
  public static int[] parseInt(String[] paramArrayOfString)
  {
    return parseInt(paramArrayOfString, 0);
  }
  
  public static int[] parseInt(String[] paramArrayOfString, int paramInt)
  {
    int[] arrayOfInt = new int[paramArrayOfString.length];
    for (int i = 0; i < paramArrayOfString.length; i++) {
      try
      {
        arrayOfInt[i] = Integer.parseInt(paramArrayOfString[i]);
      }
      catch (NumberFormatException localNumberFormatException)
      {
        arrayOfInt[i] = paramInt;
      }
    }
    return arrayOfInt;
  }
  
  public static final float parseFloat(int paramInt)
  {
    return paramInt;
  }
  
  public static final float parseFloat(String paramString)
  {
    return parseFloat(paramString, NaN.0F);
  }
  
  public static final float parseFloat(String paramString, float paramFloat)
  {
    try
    {
      return new Float(paramString).floatValue();
    }
    catch (NumberFormatException localNumberFormatException) {}
    return paramFloat;
  }
  
  public static final float[] parseByte(byte[] paramArrayOfByte)
  {
    float[] arrayOfFloat = new float[paramArrayOfByte.length];
    for (int i = 0; i < paramArrayOfByte.length; i++) {
      arrayOfFloat[i] = paramArrayOfByte[i];
    }
    return arrayOfFloat;
  }
  
  public static final float[] parseFloat(int[] paramArrayOfInt)
  {
    float[] arrayOfFloat = new float[paramArrayOfInt.length];
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      arrayOfFloat[i] = paramArrayOfInt[i];
    }
    return arrayOfFloat;
  }
  
  public static final float[] parseFloat(String[] paramArrayOfString)
  {
    return parseFloat(paramArrayOfString, NaN.0F);
  }
  
  public static final float[] parseFloat(String[] paramArrayOfString, float paramFloat)
  {
    float[] arrayOfFloat = new float[paramArrayOfString.length];
    for (int i = 0; i < paramArrayOfString.length; i++) {
      try
      {
        arrayOfFloat[i] = new Float(paramArrayOfString[i]).floatValue();
      }
      catch (NumberFormatException localNumberFormatException)
      {
        arrayOfFloat[i] = paramFloat;
      }
    }
    return arrayOfFloat;
  }
  
  public static final String str(boolean paramBoolean)
  {
    return String.valueOf(paramBoolean);
  }
  
  public static final String str(byte paramByte)
  {
    return String.valueOf(paramByte);
  }
  
  public static final String str(char paramChar)
  {
    return String.valueOf(paramChar);
  }
  
  public static final String str(int paramInt)
  {
    return String.valueOf(paramInt);
  }
  
  public static final String str(float paramFloat)
  {
    return String.valueOf(paramFloat);
  }
  
  public static final String[] str(boolean[] paramArrayOfBoolean)
  {
    String[] arrayOfString = new String[paramArrayOfBoolean.length];
    for (int i = 0; i < paramArrayOfBoolean.length; i++) {
      arrayOfString[i] = String.valueOf(paramArrayOfBoolean[i]);
    }
    return arrayOfString;
  }
  
  public static final String[] str(byte[] paramArrayOfByte)
  {
    String[] arrayOfString = new String[paramArrayOfByte.length];
    for (int i = 0; i < paramArrayOfByte.length; i++) {
      arrayOfString[i] = String.valueOf(paramArrayOfByte[i]);
    }
    return arrayOfString;
  }
  
  public static final String[] str(char[] paramArrayOfChar)
  {
    String[] arrayOfString = new String[paramArrayOfChar.length];
    for (int i = 0; i < paramArrayOfChar.length; i++) {
      arrayOfString[i] = String.valueOf(paramArrayOfChar[i]);
    }
    return arrayOfString;
  }
  
  public static final String[] str(int[] paramArrayOfInt)
  {
    String[] arrayOfString = new String[paramArrayOfInt.length];
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      arrayOfString[i] = String.valueOf(paramArrayOfInt[i]);
    }
    return arrayOfString;
  }
  
  public static final String[] str(float[] paramArrayOfFloat)
  {
    String[] arrayOfString = new String[paramArrayOfFloat.length];
    for (int i = 0; i < paramArrayOfFloat.length; i++) {
      arrayOfString[i] = String.valueOf(paramArrayOfFloat[i]);
    }
    return arrayOfString;
  }
  
  public static String[] nf(int[] paramArrayOfInt, int paramInt)
  {
    String[] arrayOfString = new String[paramArrayOfInt.length];
    for (int i = 0; i < arrayOfString.length; i++) {
      arrayOfString[i] = nf(paramArrayOfInt[i], paramInt);
    }
    return arrayOfString;
  }
  
  public static String nf(int paramInt1, int paramInt2)
  {
    if ((int_nf != null) && (int_nf_digits == paramInt2) && (!int_nf_commas)) {
      return int_nf.format(paramInt1);
    }
    int_nf = NumberFormat.getInstance();
    int_nf.setGroupingUsed(false);
    int_nf_commas = false;
    int_nf.setMinimumIntegerDigits(paramInt2);
    int_nf_digits = paramInt2;
    return int_nf.format(paramInt1);
  }
  
  public static String[] nfc(int[] paramArrayOfInt)
  {
    String[] arrayOfString = new String[paramArrayOfInt.length];
    for (int i = 0; i < arrayOfString.length; i++) {
      arrayOfString[i] = nfc(paramArrayOfInt[i]);
    }
    return arrayOfString;
  }
  
  public static String nfc(int paramInt)
  {
    if ((int_nf != null) && (int_nf_digits == 0) && (int_nf_commas)) {
      return int_nf.format(paramInt);
    }
    int_nf = NumberFormat.getInstance();
    int_nf.setGroupingUsed(true);
    int_nf_commas = true;
    int_nf.setMinimumIntegerDigits(0);
    int_nf_digits = 0;
    return int_nf.format(paramInt);
  }
  
  public static String nfs(int paramInt1, int paramInt2)
  {
    return ' ' + nf(paramInt1, paramInt2);
  }
  
  public static String[] nfs(int[] paramArrayOfInt, int paramInt)
  {
    String[] arrayOfString = new String[paramArrayOfInt.length];
    for (int i = 0; i < arrayOfString.length; i++) {
      arrayOfString[i] = nfs(paramArrayOfInt[i], paramInt);
    }
    return arrayOfString;
  }
  
  public static String nfp(int paramInt1, int paramInt2)
  {
    return '+' + nf(paramInt1, paramInt2);
  }
  
  public static String[] nfp(int[] paramArrayOfInt, int paramInt)
  {
    String[] arrayOfString = new String[paramArrayOfInt.length];
    for (int i = 0; i < arrayOfString.length; i++) {
      arrayOfString[i] = nfp(paramArrayOfInt[i], paramInt);
    }
    return arrayOfString;
  }
  
  public static String[] nf(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
  {
    String[] arrayOfString = new String[paramArrayOfFloat.length];
    for (int i = 0; i < arrayOfString.length; i++) {
      arrayOfString[i] = nf(paramArrayOfFloat[i], paramInt1, paramInt2);
    }
    return arrayOfString;
  }
  
  public static String nf(float paramFloat, int paramInt1, int paramInt2)
  {
    if ((float_nf != null) && (float_nf_left == paramInt1) && (float_nf_right == paramInt2) && (!float_nf_commas)) {
      return float_nf.format(paramFloat);
    }
    float_nf = NumberFormat.getInstance();
    float_nf.setGroupingUsed(false);
    float_nf_commas = false;
    if (paramInt1 != 0) {
      float_nf.setMinimumIntegerDigits(paramInt1);
    }
    if (paramInt2 != 0)
    {
      float_nf.setMinimumFractionDigits(paramInt2);
      float_nf.setMaximumFractionDigits(paramInt2);
    }
    float_nf_left = paramInt1;
    float_nf_right = paramInt2;
    return float_nf.format(paramFloat);
  }
  
  public static String[] nfc(float[] paramArrayOfFloat, int paramInt)
  {
    String[] arrayOfString = new String[paramArrayOfFloat.length];
    for (int i = 0; i < arrayOfString.length; i++) {
      arrayOfString[i] = nfc(paramArrayOfFloat[i], paramInt);
    }
    return arrayOfString;
  }
  
  public static String nfc(float paramFloat, int paramInt)
  {
    if ((float_nf != null) && (float_nf_left == 0) && (float_nf_right == paramInt) && (float_nf_commas)) {
      return float_nf.format(paramFloat);
    }
    float_nf = NumberFormat.getInstance();
    float_nf.setGroupingUsed(true);
    float_nf_commas = true;
    if (paramInt != 0)
    {
      float_nf.setMinimumFractionDigits(paramInt);
      float_nf.setMaximumFractionDigits(paramInt);
    }
    float_nf_left = 0;
    float_nf_right = paramInt;
    return float_nf.format(paramFloat);
  }
  
  public static String[] nfs(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
  {
    String[] arrayOfString = new String[paramArrayOfFloat.length];
    for (int i = 0; i < arrayOfString.length; i++) {
      arrayOfString[i] = nfs(paramArrayOfFloat[i], paramInt1, paramInt2);
    }
    return arrayOfString;
  }
  
  public static String nfs(float paramFloat, int paramInt1, int paramInt2)
  {
    return ' ' + nf(paramFloat, paramInt1, paramInt2);
  }
  
  public static String[] nfp(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
  {
    String[] arrayOfString = new String[paramArrayOfFloat.length];
    for (int i = 0; i < arrayOfString.length; i++) {
      arrayOfString[i] = nfp(paramArrayOfFloat[i], paramInt1, paramInt2);
    }
    return arrayOfString;
  }
  
  public static String nfp(float paramFloat, int paramInt1, int paramInt2)
  {
    return '+' + nf(paramFloat, paramInt1, paramInt2);
  }
  
  public static final String hex(byte paramByte)
  {
    return hex(paramByte, 2);
  }
  
  public static final String hex(char paramChar)
  {
    return hex(paramChar, 4);
  }
  
  public static final String hex(int paramInt)
  {
    return hex(paramInt, 8);
  }
  
  public static final String hex(int paramInt1, int paramInt2)
  {
    String str = Integer.toHexString(paramInt1).toUpperCase();
    int i = str.length();
    if (i > paramInt2) {
      return str.substring(i - paramInt2);
    }
    if (i < paramInt2) {
      return "00000000".substring(8 - (paramInt2 - i)) + str;
    }
    return str;
  }
  
  public static final int unhex(String paramString)
  {
    return (int)Long.parseLong(paramString, 16);
  }
  
  public static final String binary(byte paramByte)
  {
    return binary(paramByte, 8);
  }
  
  public static final String binary(char paramChar)
  {
    return binary(paramChar, 16);
  }
  
  public static final String binary(int paramInt)
  {
    return Integer.toBinaryString(paramInt);
  }
  
  public static final String binary(int paramInt1, int paramInt2)
  {
    String str = Integer.toBinaryString(paramInt1);
    int i = str.length();
    if (i > paramInt2) {
      return str.substring(i - paramInt2);
    }
    if (i < paramInt2)
    {
      int j = 32 - (paramInt2 - i);
      return "00000000000000000000000000000000".substring(j) + str;
    }
    return str;
  }
  
  public static final int unbinary(String paramString)
  {
    return Integer.parseInt(paramString, 2);
  }
  
  public final int color(int paramInt)
  {
    if (g == null)
    {
      if (paramInt > 255) {
        paramInt = 255;
      } else if (paramInt < 0) {
        paramInt = 0;
      }
      return 0xFF000000 | paramInt << 16 | paramInt << 8 | paramInt;
    }
    return g.color(paramInt);
  }
  
  public final int color(float paramFloat)
  {
    if (g == null)
    {
      int i = (int)paramFloat;
      if (i > 255) {
        i = 255;
      } else if (i < 0) {
        i = 0;
      }
      return 0xFF000000 | i << 16 | i << 8 | i;
    }
    return g.color(paramFloat);
  }
  
  public final int color(int paramInt1, int paramInt2)
  {
    if (g == null)
    {
      if (paramInt2 > 255) {
        paramInt2 = 255;
      } else if (paramInt2 < 0) {
        paramInt2 = 0;
      }
      if (paramInt1 > 255) {
        return paramInt2 << 24 | paramInt1 & 0xFFFFFF;
      }
      return paramInt2 << 24 | paramInt1 << 16 | paramInt1 << 8 | paramInt1;
    }
    return g.color(paramInt1, paramInt2);
  }
  
  public final int color(float paramFloat1, float paramFloat2)
  {
    if (g == null)
    {
      int i = (int)paramFloat1;
      int j = (int)paramFloat2;
      if (i > 255) {
        i = 255;
      } else if (i < 0) {
        i = 0;
      }
      if (j > 255) {
        j = 255;
      } else if (j < 0) {
        j = 0;
      }
      return 0xFF000000 | i << 16 | i << 8 | i;
    }
    return g.color(paramFloat1, paramFloat2);
  }
  
  public final int color(int paramInt1, int paramInt2, int paramInt3)
  {
    if (g == null)
    {
      if (paramInt1 > 255) {
        paramInt1 = 255;
      } else if (paramInt1 < 0) {
        paramInt1 = 0;
      }
      if (paramInt2 > 255) {
        paramInt2 = 255;
      } else if (paramInt2 < 0) {
        paramInt2 = 0;
      }
      if (paramInt3 > 255) {
        paramInt3 = 255;
      } else if (paramInt3 < 0) {
        paramInt3 = 0;
      }
      return 0xFF000000 | paramInt1 << 16 | paramInt2 << 8 | paramInt3;
    }
    return g.color(paramInt1, paramInt2, paramInt3);
  }
  
  public final int color(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (g == null)
    {
      if (paramFloat1 > 255.0F) {
        paramFloat1 = 255.0F;
      } else if (paramFloat1 < 0.0F) {
        paramFloat1 = 0.0F;
      }
      if (paramFloat2 > 255.0F) {
        paramFloat2 = 255.0F;
      } else if (paramFloat2 < 0.0F) {
        paramFloat2 = 0.0F;
      }
      if (paramFloat3 > 255.0F) {
        paramFloat3 = 255.0F;
      } else if (paramFloat3 < 0.0F) {
        paramFloat3 = 0.0F;
      }
      return 0xFF000000 | (int)paramFloat1 << 16 | (int)paramFloat2 << 8 | (int)paramFloat3;
    }
    return g.color(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public final int color(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (g == null)
    {
      if (paramInt4 > 255) {
        paramInt4 = 255;
      } else if (paramInt4 < 0) {
        paramInt4 = 0;
      }
      if (paramInt1 > 255) {
        paramInt1 = 255;
      } else if (paramInt1 < 0) {
        paramInt1 = 0;
      }
      if (paramInt2 > 255) {
        paramInt2 = 255;
      } else if (paramInt2 < 0) {
        paramInt2 = 0;
      }
      if (paramInt3 > 255) {
        paramInt3 = 255;
      } else if (paramInt3 < 0) {
        paramInt3 = 0;
      }
      return paramInt4 << 24 | paramInt1 << 16 | paramInt2 << 8 | paramInt3;
    }
    return g.color(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public final int color(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (g == null)
    {
      if (paramFloat4 > 255.0F) {
        paramFloat4 = 255.0F;
      } else if (paramFloat4 < 0.0F) {
        paramFloat4 = 0.0F;
      }
      if (paramFloat1 > 255.0F) {
        paramFloat1 = 255.0F;
      } else if (paramFloat1 < 0.0F) {
        paramFloat1 = 0.0F;
      }
      if (paramFloat2 > 255.0F) {
        paramFloat2 = 255.0F;
      } else if (paramFloat2 < 0.0F) {
        paramFloat2 = 0.0F;
      }
      if (paramFloat3 > 255.0F) {
        paramFloat3 = 255.0F;
      } else if (paramFloat3 < 0.0F) {
        paramFloat3 = 0.0F;
      }
      return (int)paramFloat4 << 24 | (int)paramFloat1 << 16 | (int)paramFloat2 << 8 | (int)paramFloat3;
    }
    return g.color(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void setupExternalMessages()
  {
    frame.addComponentListener(new ComponentAdapter()
    {
      public void componentMoved(ComponentEvent paramAnonymousComponentEvent)
      {
        Point localPoint = ((Frame)paramAnonymousComponentEvent.getSource()).getLocation();
        System.err.println("__MOVE__ " + x + " " + y);
        System.err.flush();
      }
    });
    frame.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent paramAnonymousWindowEvent)
      {
        exit();
      }
    });
  }
  
  public void setupFrameResizeListener()
  {
    frame.addComponentListener(new ComponentAdapter()
    {
      public void componentResized(ComponentEvent paramAnonymousComponentEvent)
      {
        if (frame.isResizable())
        {
          Frame localFrame = (Frame)paramAnonymousComponentEvent.getComponent();
          if (localFrame.isVisible())
          {
            Insets localInsets = localFrame.getInsets();
            Dimension localDimension = localFrame.getSize();
            int i = width - left - right;
            int j = height - top - bottom;
            setBounds(left, top, i, j);
          }
        }
      }
    });
  }
  
  public static void runSketch(String[] paramArrayOfString, PApplet paramPApplet)
  {
    if (platform == 2) {
      System.setProperty("apple.awt.graphics.UseQuartz", String.valueOf(useQuartz));
    }
    if (paramArrayOfString.length < 1)
    {
      System.err.println("Usage: PApplet <appletname>");
      System.err.println("For additional options, see the Javadoc for PApplet");
      System.exit(1);
    }
    boolean bool = false;
    int[] arrayOfInt1 = null;
    int[] arrayOfInt2 = null;
    String str1 = null;
    int i = 0;
    int j = 0;
    Object localObject1 = Color.BLACK;
    Color localColor = Color.GRAY;
    GraphicsDevice localGraphicsDevice = null;
    int k = 0;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    try
    {
      str4 = System.getProperty("user.dir");
    }
    catch (Exception localException1) {}
    Object localObject3;
    Object localObject4;
    for (int m = 0; m < paramArrayOfString.length; m++)
    {
      int n = paramArrayOfString[m].indexOf('=');
      if (n != -1)
      {
        str2 = paramArrayOfString[m].substring(0, n);
        str3 = paramArrayOfString[m].substring(n + 1);
        if (str2.equals("--editor-location"))
        {
          bool = true;
          arrayOfInt2 = parseInt(split(str3, ','));
        }
        else if (str2.equals("--display"))
        {
          int i1 = Integer.parseInt(str3) - 1;
          localObject3 = GraphicsEnvironment.getLocalGraphicsEnvironment();
          localObject4 = ((GraphicsEnvironment)localObject3).getScreenDevices();
          if ((i1 >= 0) && (i1 < localObject4.length)) {
            localGraphicsDevice = localObject4[i1];
          } else {
            System.err.println("Display " + str3 + " does not exist, " + "using the default display instead.");
          }
        }
        else if (str2.equals("--bgcolor"))
        {
          if (str3.charAt(0) == '#') {
            str3 = str3.substring(1);
          }
          localObject1 = new Color(Integer.parseInt(str3, 16));
        }
        else if (str2.equals("--stop-color"))
        {
          if (str3.charAt(0) == '#') {
            str3 = str3.substring(1);
          }
          localColor = new Color(Integer.parseInt(str3, 16));
        }
        else if (str2.equals("--sketch-path"))
        {
          str4 = str3;
        }
        else if (str2.equals("--location"))
        {
          arrayOfInt1 = parseInt(split(str3, ','));
        }
      }
      else if (paramArrayOfString[m].equals("--present"))
      {
        i = 1;
      }
      else if (paramArrayOfString[m].equals("--exclusive"))
      {
        j = 1;
      }
      else if (paramArrayOfString[m].equals("--hide-stop"))
      {
        k = 1;
      }
      else if (paramArrayOfString[m].equals("--external"))
      {
        bool = true;
      }
      else
      {
        str1 = paramArrayOfString[m];
        break;
      }
    }
    if (localGraphicsDevice == null)
    {
      localObject2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
      localGraphicsDevice = ((GraphicsEnvironment)localObject2).getDefaultScreenDevice();
    }
    Object localObject2 = new Frame(localGraphicsDevice.getDefaultConfiguration());
    Image localImage = Toolkit.getDefaultToolkit().createImage(ICON_IMAGE);
    ((Frame)localObject2).setIconImage(localImage);
    ((Frame)localObject2).setTitle(str1);
    if (paramPApplet != null) {
      localObject3 = paramPApplet;
    } else {
      try
      {
        localObject4 = Thread.currentThread().getContextClassLoader().loadClass(str1);
        localObject3 = (PApplet)((Class)localObject4).newInstance();
      }
      catch (Exception localException2)
      {
        throw new RuntimeException(localException2);
      }
    }
    frame = ((Frame)localObject2);
    sketchPath = str4;
    args = subset(paramArrayOfString, 1);
    external = bool;
    Rectangle localRectangle = null;
    if (i != 0)
    {
      ((Frame)localObject2).setUndecorated(true);
      ((Frame)localObject2).setBackground((Color)localObject1);
      if (j != 0)
      {
        localGraphicsDevice.setFullScreenWindow((Window)localObject2);
        localRectangle = ((Frame)localObject2).getBounds();
      }
      else
      {
        DisplayMode localDisplayMode = localGraphicsDevice.getDisplayMode();
        localRectangle = new Rectangle(0, 0, localDisplayMode.getWidth(), localDisplayMode.getHeight());
        ((Frame)localObject2).setBounds(localRectangle);
        ((Frame)localObject2).setVisible(true);
      }
    }
    ((Frame)localObject2).setLayout(null);
    ((Frame)localObject2).add((Component)localObject3);
    if (i != 0) {
      ((Frame)localObject2).invalidate();
    } else {
      ((Frame)localObject2).pack();
    }
    ((Frame)localObject2).setResizable(false);
    ((PApplet)localObject3).init();
    while ((defaultSize) && (!finished)) {
      try
      {
        Thread.sleep(5L);
      }
      catch (InterruptedException localInterruptedException) {}
    }
    Object localObject5;
    if (i != 0)
    {
      ((Frame)localObject2).setBounds(localRectangle);
      ((PApplet)localObject3).setBounds((width - width) / 2, (height - height) / 2, width, height);
      if (k == 0)
      {
        localObject5 = new Label("stop");
        ((Label)localObject5).setForeground(localColor);
        ((Label)localObject5).addMouseListener(new MouseAdapter()
        {
          public void mousePressed(MouseEvent paramAnonymousMouseEvent)
          {
            System.exit(0);
          }
        });
        ((Frame)localObject2).add((Component)localObject5);
        Dimension localDimension = ((Label)localObject5).getPreferredSize();
        localDimension = new Dimension(100, height);
        ((Label)localObject5).setSize(localDimension);
        ((Label)localObject5).setLocation(20, height - height - 20);
      }
      if (bool) {
        ((PApplet)localObject3).setupExternalMessages();
      }
    }
    else
    {
      localObject5 = ((Frame)localObject2).getInsets();
      int i2 = Math.max(width, 128) + left + right;
      int i3 = Math.max(height, 128) + top + bottom;
      ((Frame)localObject2).setSize(i2, i3);
      if (arrayOfInt1 != null)
      {
        ((Frame)localObject2).setLocation(arrayOfInt1[0], arrayOfInt1[1]);
      }
      else if (bool)
      {
        int i4 = arrayOfInt2[0] - 20;
        i5 = arrayOfInt2[1];
        if (i4 - i2 > 10)
        {
          ((Frame)localObject2).setLocation(i4 - i2, i5);
        }
        else
        {
          i4 = arrayOfInt2[0] + 66;
          i5 = arrayOfInt2[1] + 66;
          if ((i4 + i2 > screenWidth - 33) || (i5 + i3 > screenHeight - 33))
          {
            i4 = (screenWidth - i2) / 2;
            i5 = (screenHeight - i3) / 2;
          }
          ((Frame)localObject2).setLocation(i4, i5);
        }
      }
      else
      {
        ((Frame)localObject2).setLocation((screenWidth - width) / 2, (screenHeight - height) / 2);
      }
      Point localPoint = ((Frame)localObject2).getLocation();
      if (y < 0) {
        ((Frame)localObject2).setLocation(x, 30);
      }
      if (localObject1 == Color.black) {
        localObject1 = SystemColor.control;
      }
      ((Frame)localObject2).setBackground((Color)localObject1);
      int i5 = i3 - top - bottom;
      ((PApplet)localObject3).setBounds((i2 - width) / 2, top + (i5 - height) / 2, width, height);
      if (bool) {
        ((PApplet)localObject3).setupExternalMessages();
      } else {
        ((Frame)localObject2).addWindowListener(new WindowAdapter()
        {
          public void windowClosing(WindowEvent paramAnonymousWindowEvent)
          {
            System.exit(0);
          }
        });
      }
      ((PApplet)localObject3).setupFrameResizeListener();
      if (((PApplet)localObject3).displayable()) {
        ((Frame)localObject2).setVisible(true);
      }
    }
  }
  
  public static void main(String[] paramArrayOfString)
  {
    runSketch(paramArrayOfString, null);
  }
  
  protected void runSketch(String[] paramArrayOfString)
  {
    String[] arrayOfString = new String[paramArrayOfString.length + 1];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramArrayOfString.length);
    String str1 = getClass().getSimpleName();
    String str2 = str1.replaceAll("__[^_]+__\\$", "").replaceAll("\\$\\d+", "");
    arrayOfString[paramArrayOfString.length] = str2;
    runSketch(arrayOfString, this);
  }
  
  protected void runSketch()
  {
    runSketch(new String[0]);
  }
  
  public PGraphics beginRecord(String paramString1, String paramString2)
  {
    paramString2 = insertFrame(paramString2);
    PGraphics localPGraphics = createGraphics(width, height, paramString1, paramString2);
    beginRecord(localPGraphics);
    return localPGraphics;
  }
  
  public void beginRecord(PGraphics paramPGraphics)
  {
    recorder = paramPGraphics;
    paramPGraphics.beginDraw();
  }
  
  public void endRecord()
  {
    if (recorder != null)
    {
      recorder.endDraw();
      recorder.dispose();
      recorder = null;
    }
    if (g.isRecording()) {
      g.endRecord();
    }
  }
  
  public PGraphics beginRaw(String paramString1, String paramString2)
  {
    paramString2 = insertFrame(paramString2);
    PGraphics localPGraphics = createGraphics(width, height, paramString1, paramString2);
    g.beginRaw(localPGraphics);
    return localPGraphics;
  }
  
  public void beginRaw(PGraphics paramPGraphics)
  {
    g.beginRaw(paramPGraphics);
  }
  
  public void endRaw()
  {
    g.endRaw();
  }
  
  public PShape beginRecord()
  {
    return g.beginRecord();
  }
  
  public void loadPixels()
  {
    g.loadPixels();
    pixels = g.pixels;
  }
  
  public void updatePixels()
  {
    g.updatePixels();
  }
  
  public void updatePixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    g.updatePixels(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void flush()
  {
    if (recorder != null) {
      recorder.flush();
    }
    g.flush();
  }
  
  public void hint(int paramInt)
  {
    if (recorder != null) {
      recorder.hint(paramInt);
    }
    g.hint(paramInt);
  }
  
  public boolean hintEnabled(int paramInt)
  {
    return g.hintEnabled(paramInt);
  }
  
  public void beginShape()
  {
    if (recorder != null) {
      recorder.beginShape();
    }
    g.beginShape();
  }
  
  public void beginShape(int paramInt)
  {
    if (recorder != null) {
      recorder.beginShape(paramInt);
    }
    g.beginShape(paramInt);
  }
  
  public void edge(boolean paramBoolean)
  {
    if (recorder != null) {
      recorder.edge(paramBoolean);
    }
    g.edge(paramBoolean);
  }
  
  public void normal(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.normal(paramFloat1, paramFloat2, paramFloat3);
    }
    g.normal(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void textureMode(int paramInt)
  {
    if (recorder != null) {
      recorder.textureMode(paramInt);
    }
    g.textureMode(paramInt);
  }
  
  public void texture(PImage paramPImage)
  {
    if (recorder != null) {
      recorder.texture(paramPImage);
    }
    g.texture(paramPImage);
  }
  
  public void noTexture()
  {
    if (recorder != null) {
      recorder.noTexture();
    }
    g.noTexture();
  }
  
  public void vertex(float paramFloat1, float paramFloat2)
  {
    if (recorder != null) {
      recorder.vertex(paramFloat1, paramFloat2);
    }
    g.vertex(paramFloat1, paramFloat2);
  }
  
  public void vertex(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.vertex(paramFloat1, paramFloat2, paramFloat3);
    }
    g.vertex(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void vertexFields(float[] paramArrayOfFloat)
  {
    if (recorder != null) {
      recorder.vertexFields(paramArrayOfFloat);
    }
    g.vertexFields(paramArrayOfFloat);
  }
  
  public void vertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.vertex(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.vertex(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void vertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    if (recorder != null) {
      recorder.vertex(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5);
    }
    g.vertex(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5);
  }
  
  public void breakShape()
  {
    if (recorder != null) {
      recorder.breakShape();
    }
    g.breakShape();
  }
  
  public void endShape()
  {
    if (recorder != null) {
      recorder.endShape();
    }
    g.endShape();
  }
  
  public void endShape(int paramInt)
  {
    if (recorder != null) {
      recorder.endShape(paramInt);
    }
    g.endShape(paramInt);
  }
  
  public void bezierVertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (recorder != null) {
      recorder.bezierVertex(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
    }
    g.bezierVertex(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  public void bezierVertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9)
  {
    if (recorder != null) {
      recorder.bezierVertex(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9);
    }
    g.bezierVertex(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9);
  }
  
  public void quadVertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.quadVertex(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.quadVertex(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void quadVertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (recorder != null) {
      recorder.quadVertex(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
    }
    g.quadVertex(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  public void curveVertex(float paramFloat1, float paramFloat2)
  {
    if (recorder != null) {
      recorder.curveVertex(paramFloat1, paramFloat2);
    }
    g.curveVertex(paramFloat1, paramFloat2);
  }
  
  public void curveVertex(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.curveVertex(paramFloat1, paramFloat2, paramFloat3);
    }
    g.curveVertex(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void point(float paramFloat1, float paramFloat2)
  {
    if (recorder != null) {
      recorder.point(paramFloat1, paramFloat2);
    }
    g.point(paramFloat1, paramFloat2);
  }
  
  public void point(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.point(paramFloat1, paramFloat2, paramFloat3);
    }
    g.point(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void line(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.line(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.line(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void line(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (recorder != null) {
      recorder.line(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
    }
    g.line(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  public void triangle(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (recorder != null) {
      recorder.triangle(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
    }
    g.triangle(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  public void quad(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
  {
    if (recorder != null) {
      recorder.quad(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8);
    }
    g.quad(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8);
  }
  
  public void rectMode(int paramInt)
  {
    if (recorder != null) {
      recorder.rectMode(paramInt);
    }
    g.rectMode(paramInt);
  }
  
  public void rect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.rect(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.rect(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void rect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (recorder != null) {
      recorder.rect(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
    }
    g.rect(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  public void rect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
  {
    if (recorder != null) {
      recorder.rect(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8);
    }
    g.rect(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8);
  }
  
  public void ellipseMode(int paramInt)
  {
    if (recorder != null) {
      recorder.ellipseMode(paramInt);
    }
    g.ellipseMode(paramInt);
  }
  
  public void ellipse(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.ellipse(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.ellipse(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void arc(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (recorder != null) {
      recorder.arc(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
    }
    g.arc(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  public void box(float paramFloat)
  {
    if (recorder != null) {
      recorder.box(paramFloat);
    }
    g.box(paramFloat);
  }
  
  public void box(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.box(paramFloat1, paramFloat2, paramFloat3);
    }
    g.box(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void sphereDetail(int paramInt)
  {
    if (recorder != null) {
      recorder.sphereDetail(paramInt);
    }
    g.sphereDetail(paramInt);
  }
  
  public void sphereDetail(int paramInt1, int paramInt2)
  {
    if (recorder != null) {
      recorder.sphereDetail(paramInt1, paramInt2);
    }
    g.sphereDetail(paramInt1, paramInt2);
  }
  
  public void sphere(float paramFloat)
  {
    if (recorder != null) {
      recorder.sphere(paramFloat);
    }
    g.sphere(paramFloat);
  }
  
  public float bezierPoint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    return g.bezierPoint(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5);
  }
  
  public float bezierTangent(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    return g.bezierTangent(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5);
  }
  
  public void bezierDetail(int paramInt)
  {
    if (recorder != null) {
      recorder.bezierDetail(paramInt);
    }
    g.bezierDetail(paramInt);
  }
  
  public void bezier(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
  {
    if (recorder != null) {
      recorder.bezier(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8);
    }
    g.bezier(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8);
  }
  
  public void bezier(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12)
  {
    if (recorder != null) {
      recorder.bezier(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9, paramFloat10, paramFloat11, paramFloat12);
    }
    g.bezier(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9, paramFloat10, paramFloat11, paramFloat12);
  }
  
  public float curvePoint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    return g.curvePoint(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5);
  }
  
  public float curveTangent(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    return g.curveTangent(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5);
  }
  
  public void curveDetail(int paramInt)
  {
    if (recorder != null) {
      recorder.curveDetail(paramInt);
    }
    g.curveDetail(paramInt);
  }
  
  public void curveTightness(float paramFloat)
  {
    if (recorder != null) {
      recorder.curveTightness(paramFloat);
    }
    g.curveTightness(paramFloat);
  }
  
  public void curve(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
  {
    if (recorder != null) {
      recorder.curve(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8);
    }
    g.curve(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8);
  }
  
  public void curve(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12)
  {
    if (recorder != null) {
      recorder.curve(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9, paramFloat10, paramFloat11, paramFloat12);
    }
    g.curve(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9, paramFloat10, paramFloat11, paramFloat12);
  }
  
  public void smooth()
  {
    if (recorder != null) {
      recorder.smooth();
    }
    g.smooth();
  }
  
  public void noSmooth()
  {
    if (recorder != null) {
      recorder.noSmooth();
    }
    g.noSmooth();
  }
  
  public void imageMode(int paramInt)
  {
    if (recorder != null) {
      recorder.imageMode(paramInt);
    }
    g.imageMode(paramInt);
  }
  
  public void image(PImage paramPImage, float paramFloat1, float paramFloat2)
  {
    if (recorder != null) {
      recorder.image(paramPImage, paramFloat1, paramFloat2);
    }
    g.image(paramPImage, paramFloat1, paramFloat2);
  }
  
  public void image(PImage paramPImage, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.image(paramPImage, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.image(paramPImage, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void image(PImage paramPImage, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (recorder != null) {
      recorder.image(paramPImage, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramInt1, paramInt2, paramInt3, paramInt4);
    }
    g.image(paramPImage, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void shapeMode(int paramInt)
  {
    if (recorder != null) {
      recorder.shapeMode(paramInt);
    }
    g.shapeMode(paramInt);
  }
  
  public void shape(PShape paramPShape)
  {
    if (recorder != null) {
      recorder.shape(paramPShape);
    }
    g.shape(paramPShape);
  }
  
  public void shape(PShape paramPShape, float paramFloat1, float paramFloat2)
  {
    if (recorder != null) {
      recorder.shape(paramPShape, paramFloat1, paramFloat2);
    }
    g.shape(paramPShape, paramFloat1, paramFloat2);
  }
  
  public void shape(PShape paramPShape, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.shape(paramPShape, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.shape(paramPShape, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void textAlign(int paramInt)
  {
    if (recorder != null) {
      recorder.textAlign(paramInt);
    }
    g.textAlign(paramInt);
  }
  
  public void textAlign(int paramInt1, int paramInt2)
  {
    if (recorder != null) {
      recorder.textAlign(paramInt1, paramInt2);
    }
    g.textAlign(paramInt1, paramInt2);
  }
  
  public float textAscent()
  {
    return g.textAscent();
  }
  
  public float textDescent()
  {
    return g.textDescent();
  }
  
  public void textFont(PFont paramPFont)
  {
    if (recorder != null) {
      recorder.textFont(paramPFont);
    }
    g.textFont(paramPFont);
  }
  
  public void textFont(PFont paramPFont, float paramFloat)
  {
    if (recorder != null) {
      recorder.textFont(paramPFont, paramFloat);
    }
    g.textFont(paramPFont, paramFloat);
  }
  
  public void textLeading(float paramFloat)
  {
    if (recorder != null) {
      recorder.textLeading(paramFloat);
    }
    g.textLeading(paramFloat);
  }
  
  public void textMode(int paramInt)
  {
    if (recorder != null) {
      recorder.textMode(paramInt);
    }
    g.textMode(paramInt);
  }
  
  public void textSize(float paramFloat)
  {
    if (recorder != null) {
      recorder.textSize(paramFloat);
    }
    g.textSize(paramFloat);
  }
  
  public float textWidth(char paramChar)
  {
    return g.textWidth(paramChar);
  }
  
  public float textWidth(String paramString)
  {
    return g.textWidth(paramString);
  }
  
  public float textWidth(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    return g.textWidth(paramArrayOfChar, paramInt1, paramInt2);
  }
  
  public void text(char paramChar)
  {
    if (recorder != null) {
      recorder.text(paramChar);
    }
    g.text(paramChar);
  }
  
  public void text(char paramChar, float paramFloat1, float paramFloat2)
  {
    if (recorder != null) {
      recorder.text(paramChar, paramFloat1, paramFloat2);
    }
    g.text(paramChar, paramFloat1, paramFloat2);
  }
  
  public void text(char paramChar, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.text(paramChar, paramFloat1, paramFloat2, paramFloat3);
    }
    g.text(paramChar, paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void text(String paramString)
  {
    if (recorder != null) {
      recorder.text(paramString);
    }
    g.text(paramString);
  }
  
  public void text(String paramString, float paramFloat1, float paramFloat2)
  {
    if (recorder != null) {
      recorder.text(paramString, paramFloat1, paramFloat2);
    }
    g.text(paramString, paramFloat1, paramFloat2);
  }
  
  public void text(char[] paramArrayOfChar, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
  {
    if (recorder != null) {
      recorder.text(paramArrayOfChar, paramInt1, paramInt2, paramFloat1, paramFloat2);
    }
    g.text(paramArrayOfChar, paramInt1, paramInt2, paramFloat1, paramFloat2);
  }
  
  public void text(String paramString, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.text(paramString, paramFloat1, paramFloat2, paramFloat3);
    }
    g.text(paramString, paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void text(char[] paramArrayOfChar, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.text(paramArrayOfChar, paramInt1, paramInt2, paramFloat1, paramFloat2, paramFloat3);
    }
    g.text(paramArrayOfChar, paramInt1, paramInt2, paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void text(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.text(paramString, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.text(paramString, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void text(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    if (recorder != null) {
      recorder.text(paramString, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5);
    }
    g.text(paramString, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5);
  }
  
  public void text(int paramInt, float paramFloat1, float paramFloat2)
  {
    if (recorder != null) {
      recorder.text(paramInt, paramFloat1, paramFloat2);
    }
    g.text(paramInt, paramFloat1, paramFloat2);
  }
  
  public void text(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.text(paramInt, paramFloat1, paramFloat2, paramFloat3);
    }
    g.text(paramInt, paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void text(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.text(paramFloat1, paramFloat2, paramFloat3);
    }
    g.text(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void text(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.text(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.text(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void pushMatrix()
  {
    if (recorder != null) {
      recorder.pushMatrix();
    }
    g.pushMatrix();
  }
  
  public void popMatrix()
  {
    if (recorder != null) {
      recorder.popMatrix();
    }
    g.popMatrix();
  }
  
  public void translate(float paramFloat1, float paramFloat2)
  {
    if (recorder != null) {
      recorder.translate(paramFloat1, paramFloat2);
    }
    g.translate(paramFloat1, paramFloat2);
  }
  
  public void translate(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.translate(paramFloat1, paramFloat2, paramFloat3);
    }
    g.translate(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void rotate(float paramFloat)
  {
    if (recorder != null) {
      recorder.rotate(paramFloat);
    }
    g.rotate(paramFloat);
  }
  
  public void rotateX(float paramFloat)
  {
    if (recorder != null) {
      recorder.rotateX(paramFloat);
    }
    g.rotateX(paramFloat);
  }
  
  public void rotateY(float paramFloat)
  {
    if (recorder != null) {
      recorder.rotateY(paramFloat);
    }
    g.rotateY(paramFloat);
  }
  
  public void rotateZ(float paramFloat)
  {
    if (recorder != null) {
      recorder.rotateZ(paramFloat);
    }
    g.rotateZ(paramFloat);
  }
  
  public void rotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.rotate(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.rotate(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void scale(float paramFloat)
  {
    if (recorder != null) {
      recorder.scale(paramFloat);
    }
    g.scale(paramFloat);
  }
  
  public void scale(float paramFloat1, float paramFloat2)
  {
    if (recorder != null) {
      recorder.scale(paramFloat1, paramFloat2);
    }
    g.scale(paramFloat1, paramFloat2);
  }
  
  public void scale(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.scale(paramFloat1, paramFloat2, paramFloat3);
    }
    g.scale(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void shearX(float paramFloat)
  {
    if (recorder != null) {
      recorder.shearX(paramFloat);
    }
    g.shearX(paramFloat);
  }
  
  public void shearY(float paramFloat)
  {
    if (recorder != null) {
      recorder.shearY(paramFloat);
    }
    g.shearY(paramFloat);
  }
  
  public void resetMatrix()
  {
    if (recorder != null) {
      recorder.resetMatrix();
    }
    g.resetMatrix();
  }
  
  public void applyMatrix(PMatrix paramPMatrix)
  {
    if (recorder != null) {
      recorder.applyMatrix(paramPMatrix);
    }
    g.applyMatrix(paramPMatrix);
  }
  
  public void applyMatrix(PMatrix2D paramPMatrix2D)
  {
    if (recorder != null) {
      recorder.applyMatrix(paramPMatrix2D);
    }
    g.applyMatrix(paramPMatrix2D);
  }
  
  public void applyMatrix(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (recorder != null) {
      recorder.applyMatrix(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
    }
    g.applyMatrix(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  public void applyMatrix(PMatrix3D paramPMatrix3D)
  {
    if (recorder != null) {
      recorder.applyMatrix(paramPMatrix3D);
    }
    g.applyMatrix(paramPMatrix3D);
  }
  
  public void applyMatrix(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16)
  {
    if (recorder != null) {
      recorder.applyMatrix(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9, paramFloat10, paramFloat11, paramFloat12, paramFloat13, paramFloat14, paramFloat15, paramFloat16);
    }
    g.applyMatrix(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9, paramFloat10, paramFloat11, paramFloat12, paramFloat13, paramFloat14, paramFloat15, paramFloat16);
  }
  
  public PMatrix getMatrix()
  {
    return g.getMatrix();
  }
  
  public PMatrix2D getMatrix(PMatrix2D paramPMatrix2D)
  {
    return g.getMatrix(paramPMatrix2D);
  }
  
  public PMatrix3D getMatrix(PMatrix3D paramPMatrix3D)
  {
    return g.getMatrix(paramPMatrix3D);
  }
  
  public void setMatrix(PMatrix paramPMatrix)
  {
    if (recorder != null) {
      recorder.setMatrix(paramPMatrix);
    }
    g.setMatrix(paramPMatrix);
  }
  
  public void setMatrix(PMatrix2D paramPMatrix2D)
  {
    if (recorder != null) {
      recorder.setMatrix(paramPMatrix2D);
    }
    g.setMatrix(paramPMatrix2D);
  }
  
  public void setMatrix(PMatrix3D paramPMatrix3D)
  {
    if (recorder != null) {
      recorder.setMatrix(paramPMatrix3D);
    }
    g.setMatrix(paramPMatrix3D);
  }
  
  public void printMatrix()
  {
    if (recorder != null) {
      recorder.printMatrix();
    }
    g.printMatrix();
  }
  
  public void beginCamera()
  {
    if (recorder != null) {
      recorder.beginCamera();
    }
    g.beginCamera();
  }
  
  public void endCamera()
  {
    if (recorder != null) {
      recorder.endCamera();
    }
    g.endCamera();
  }
  
  public void camera()
  {
    if (recorder != null) {
      recorder.camera();
    }
    g.camera();
  }
  
  public void camera(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9)
  {
    if (recorder != null) {
      recorder.camera(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9);
    }
    g.camera(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9);
  }
  
  public void printCamera()
  {
    if (recorder != null) {
      recorder.printCamera();
    }
    g.printCamera();
  }
  
  public void ortho()
  {
    if (recorder != null) {
      recorder.ortho();
    }
    g.ortho();
  }
  
  public void ortho(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.ortho(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.ortho(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void ortho(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (recorder != null) {
      recorder.ortho(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
    }
    g.ortho(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  public void perspective()
  {
    if (recorder != null) {
      recorder.perspective();
    }
    g.perspective();
  }
  
  public void perspective(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.perspective(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.perspective(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void frustum(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (recorder != null) {
      recorder.frustum(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
    }
    g.frustum(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  public void printProjection()
  {
    if (recorder != null) {
      recorder.printProjection();
    }
    g.printProjection();
  }
  
  public float screenX(float paramFloat1, float paramFloat2)
  {
    return g.screenX(paramFloat1, paramFloat2);
  }
  
  public float screenY(float paramFloat1, float paramFloat2)
  {
    return g.screenY(paramFloat1, paramFloat2);
  }
  
  public float screenX(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return g.screenX(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public float screenY(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return g.screenY(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public float screenZ(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return g.screenZ(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public float modelX(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return g.modelX(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public float modelY(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return g.modelY(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public float modelZ(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return g.modelZ(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void pushStyle()
  {
    if (recorder != null) {
      recorder.pushStyle();
    }
    g.pushStyle();
  }
  
  public void popStyle()
  {
    if (recorder != null) {
      recorder.popStyle();
    }
    g.popStyle();
  }
  
  public void style(PStyle paramPStyle)
  {
    if (recorder != null) {
      recorder.style(paramPStyle);
    }
    g.style(paramPStyle);
  }
  
  public void strokeWeight(float paramFloat)
  {
    if (recorder != null) {
      recorder.strokeWeight(paramFloat);
    }
    g.strokeWeight(paramFloat);
  }
  
  public void strokeJoin(int paramInt)
  {
    if (recorder != null) {
      recorder.strokeJoin(paramInt);
    }
    g.strokeJoin(paramInt);
  }
  
  public void strokeCap(int paramInt)
  {
    if (recorder != null) {
      recorder.strokeCap(paramInt);
    }
    g.strokeCap(paramInt);
  }
  
  public void noStroke()
  {
    if (recorder != null) {
      recorder.noStroke();
    }
    g.noStroke();
  }
  
  public void stroke(int paramInt)
  {
    if (recorder != null) {
      recorder.stroke(paramInt);
    }
    g.stroke(paramInt);
  }
  
  public void stroke(int paramInt, float paramFloat)
  {
    if (recorder != null) {
      recorder.stroke(paramInt, paramFloat);
    }
    g.stroke(paramInt, paramFloat);
  }
  
  public void stroke(float paramFloat)
  {
    if (recorder != null) {
      recorder.stroke(paramFloat);
    }
    g.stroke(paramFloat);
  }
  
  public void stroke(float paramFloat1, float paramFloat2)
  {
    if (recorder != null) {
      recorder.stroke(paramFloat1, paramFloat2);
    }
    g.stroke(paramFloat1, paramFloat2);
  }
  
  public void stroke(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.stroke(paramFloat1, paramFloat2, paramFloat3);
    }
    g.stroke(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void stroke(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.stroke(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.stroke(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void noTint()
  {
    if (recorder != null) {
      recorder.noTint();
    }
    g.noTint();
  }
  
  public void tint(int paramInt)
  {
    if (recorder != null) {
      recorder.tint(paramInt);
    }
    g.tint(paramInt);
  }
  
  public void tint(int paramInt, float paramFloat)
  {
    if (recorder != null) {
      recorder.tint(paramInt, paramFloat);
    }
    g.tint(paramInt, paramFloat);
  }
  
  public void tint(float paramFloat)
  {
    if (recorder != null) {
      recorder.tint(paramFloat);
    }
    g.tint(paramFloat);
  }
  
  public void tint(float paramFloat1, float paramFloat2)
  {
    if (recorder != null) {
      recorder.tint(paramFloat1, paramFloat2);
    }
    g.tint(paramFloat1, paramFloat2);
  }
  
  public void tint(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.tint(paramFloat1, paramFloat2, paramFloat3);
    }
    g.tint(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void tint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.tint(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.tint(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void noFill()
  {
    if (recorder != null) {
      recorder.noFill();
    }
    g.noFill();
  }
  
  public void fill(int paramInt)
  {
    if (recorder != null) {
      recorder.fill(paramInt);
    }
    g.fill(paramInt);
  }
  
  public void fill(int paramInt, float paramFloat)
  {
    if (recorder != null) {
      recorder.fill(paramInt, paramFloat);
    }
    g.fill(paramInt, paramFloat);
  }
  
  public void fill(float paramFloat)
  {
    if (recorder != null) {
      recorder.fill(paramFloat);
    }
    g.fill(paramFloat);
  }
  
  public void fill(float paramFloat1, float paramFloat2)
  {
    if (recorder != null) {
      recorder.fill(paramFloat1, paramFloat2);
    }
    g.fill(paramFloat1, paramFloat2);
  }
  
  public void fill(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.fill(paramFloat1, paramFloat2, paramFloat3);
    }
    g.fill(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void fill(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.fill(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.fill(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void ambient(int paramInt)
  {
    if (recorder != null) {
      recorder.ambient(paramInt);
    }
    g.ambient(paramInt);
  }
  
  public void ambient(float paramFloat)
  {
    if (recorder != null) {
      recorder.ambient(paramFloat);
    }
    g.ambient(paramFloat);
  }
  
  public void ambient(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.ambient(paramFloat1, paramFloat2, paramFloat3);
    }
    g.ambient(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void specular(int paramInt)
  {
    if (recorder != null) {
      recorder.specular(paramInt);
    }
    g.specular(paramInt);
  }
  
  public void specular(float paramFloat)
  {
    if (recorder != null) {
      recorder.specular(paramFloat);
    }
    g.specular(paramFloat);
  }
  
  public void specular(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.specular(paramFloat1, paramFloat2, paramFloat3);
    }
    g.specular(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void shininess(float paramFloat)
  {
    if (recorder != null) {
      recorder.shininess(paramFloat);
    }
    g.shininess(paramFloat);
  }
  
  public void emissive(int paramInt)
  {
    if (recorder != null) {
      recorder.emissive(paramInt);
    }
    g.emissive(paramInt);
  }
  
  public void emissive(float paramFloat)
  {
    if (recorder != null) {
      recorder.emissive(paramFloat);
    }
    g.emissive(paramFloat);
  }
  
  public void emissive(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.emissive(paramFloat1, paramFloat2, paramFloat3);
    }
    g.emissive(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void lights()
  {
    if (recorder != null) {
      recorder.lights();
    }
    g.lights();
  }
  
  public void noLights()
  {
    if (recorder != null) {
      recorder.noLights();
    }
    g.noLights();
  }
  
  public void ambientLight(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.ambientLight(paramFloat1, paramFloat2, paramFloat3);
    }
    g.ambientLight(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void ambientLight(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (recorder != null) {
      recorder.ambientLight(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
    }
    g.ambientLight(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  public void directionalLight(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (recorder != null) {
      recorder.directionalLight(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
    }
    g.directionalLight(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  public void pointLight(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (recorder != null) {
      recorder.pointLight(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
    }
    g.pointLight(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  public void spotLight(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11)
  {
    if (recorder != null) {
      recorder.spotLight(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9, paramFloat10, paramFloat11);
    }
    g.spotLight(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9, paramFloat10, paramFloat11);
  }
  
  public void lightFalloff(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.lightFalloff(paramFloat1, paramFloat2, paramFloat3);
    }
    g.lightFalloff(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void lightSpecular(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.lightSpecular(paramFloat1, paramFloat2, paramFloat3);
    }
    g.lightSpecular(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void background(int paramInt)
  {
    if (recorder != null) {
      recorder.background(paramInt);
    }
    g.background(paramInt);
  }
  
  public void background(int paramInt, float paramFloat)
  {
    if (recorder != null) {
      recorder.background(paramInt, paramFloat);
    }
    g.background(paramInt, paramFloat);
  }
  
  public void background(float paramFloat)
  {
    if (recorder != null) {
      recorder.background(paramFloat);
    }
    g.background(paramFloat);
  }
  
  public void background(float paramFloat1, float paramFloat2)
  {
    if (recorder != null) {
      recorder.background(paramFloat1, paramFloat2);
    }
    g.background(paramFloat1, paramFloat2);
  }
  
  public void background(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.background(paramFloat1, paramFloat2, paramFloat3);
    }
    g.background(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void background(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.background(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.background(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void background(PImage paramPImage)
  {
    if (recorder != null) {
      recorder.background(paramPImage);
    }
    g.background(paramPImage);
  }
  
  public void colorMode(int paramInt)
  {
    if (recorder != null) {
      recorder.colorMode(paramInt);
    }
    g.colorMode(paramInt);
  }
  
  public void colorMode(int paramInt, float paramFloat)
  {
    if (recorder != null) {
      recorder.colorMode(paramInt, paramFloat);
    }
    g.colorMode(paramInt, paramFloat);
  }
  
  public void colorMode(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (recorder != null) {
      recorder.colorMode(paramInt, paramFloat1, paramFloat2, paramFloat3);
    }
    g.colorMode(paramInt, paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void colorMode(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (recorder != null) {
      recorder.colorMode(paramInt, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }
    g.colorMode(paramInt, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public final float alpha(int paramInt)
  {
    return g.alpha(paramInt);
  }
  
  public final float red(int paramInt)
  {
    return g.red(paramInt);
  }
  
  public final float green(int paramInt)
  {
    return g.green(paramInt);
  }
  
  public final float blue(int paramInt)
  {
    return g.blue(paramInt);
  }
  
  public final float hue(int paramInt)
  {
    return g.hue(paramInt);
  }
  
  public final float saturation(int paramInt)
  {
    return g.saturation(paramInt);
  }
  
  public final float brightness(int paramInt)
  {
    return g.brightness(paramInt);
  }
  
  public int lerpColor(int paramInt1, int paramInt2, float paramFloat)
  {
    return g.lerpColor(paramInt1, paramInt2, paramFloat);
  }
  
  public static int lerpColor(int paramInt1, int paramInt2, float paramFloat, int paramInt3)
  {
    return PGraphics.lerpColor(paramInt1, paramInt2, paramFloat, paramInt3);
  }
  
  public static void showDepthWarning(String paramString)
  {
    PGraphics.showDepthWarning(paramString);
  }
  
  public static void showDepthWarningXYZ(String paramString)
  {
    PGraphics.showDepthWarningXYZ(paramString);
  }
  
  public static void showMethodWarning(String paramString)
  {
    PGraphics.showMethodWarning(paramString);
  }
  
  public static void showVariationWarning(String paramString)
  {
    PGraphics.showVariationWarning(paramString);
  }
  
  public static void showMissingWarning(String paramString)
  {
    PGraphics.showMissingWarning(paramString);
  }
  
  public boolean displayable()
  {
    return g.displayable();
  }
  
  public void screenBlend(int paramInt)
  {
    if (recorder != null) {
      recorder.screenBlend(paramInt);
    }
    g.screenBlend(paramInt);
  }
  
  public void textureBlend(int paramInt)
  {
    if (recorder != null) {
      recorder.textureBlend(paramInt);
    }
    g.textureBlend(paramInt);
  }
  
  public boolean isRecording()
  {
    return g.isRecording();
  }
  
  public void mergeShapes(boolean paramBoolean)
  {
    if (recorder != null) {
      recorder.mergeShapes(paramBoolean);
    }
    g.mergeShapes(paramBoolean);
  }
  
  public void shapeName(String paramString)
  {
    if (recorder != null) {
      recorder.shapeName(paramString);
    }
    g.shapeName(paramString);
  }
  
  public void autoNormal(boolean paramBoolean)
  {
    if (recorder != null) {
      recorder.autoNormal(paramBoolean);
    }
    g.autoNormal(paramBoolean);
  }
  
  public void matrixMode(int paramInt)
  {
    if (recorder != null) {
      recorder.matrixMode(paramInt);
    }
    g.matrixMode(paramInt);
  }
  
  public void beginText()
  {
    if (recorder != null) {
      recorder.beginText();
    }
    g.beginText();
  }
  
  public void endText()
  {
    if (recorder != null) {
      recorder.endText();
    }
    g.endText();
  }
  
  public void texture(PImage... paramVarArgs)
  {
    if (recorder != null) {
      recorder.texture(paramVarArgs);
    }
    g.texture(paramVarArgs);
  }
  
  public void vertex(float... paramVarArgs)
  {
    if (recorder != null) {
      recorder.vertex(paramVarArgs);
    }
    g.vertex(paramVarArgs);
  }
  
  public void delete()
  {
    if (recorder != null) {
      recorder.delete();
    }
    g.delete();
  }
  
  public void setCache(PGraphics paramPGraphics, Object paramObject)
  {
    if (recorder != null) {
      recorder.setCache(paramPGraphics, paramObject);
    }
    g.setCache(paramPGraphics, paramObject);
  }
  
  public Object getCache(PGraphics paramPGraphics)
  {
    return g.getCache(paramPGraphics);
  }
  
  public void removeCache(PGraphics paramPGraphics)
  {
    if (recorder != null) {
      recorder.removeCache(paramPGraphics);
    }
    g.removeCache(paramPGraphics);
  }
  
  public void setParams(PGraphics paramPGraphics, Object paramObject)
  {
    if (recorder != null) {
      recorder.setParams(paramPGraphics, paramObject);
    }
    g.setParams(paramPGraphics, paramObject);
  }
  
  public Object getParams(PGraphics paramPGraphics)
  {
    return g.getParams(paramPGraphics);
  }
  
  public void removeParams(PGraphics paramPGraphics)
  {
    if (recorder != null) {
      recorder.removeParams(paramPGraphics);
    }
    g.removeParams(paramPGraphics);
  }
  
  public int get(int paramInt1, int paramInt2)
  {
    return g.get(paramInt1, paramInt2);
  }
  
  public PImage get(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return g.get(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public PImage get()
  {
    return g.get();
  }
  
  public void set(int paramInt1, int paramInt2, int paramInt3)
  {
    if (recorder != null) {
      recorder.set(paramInt1, paramInt2, paramInt3);
    }
    g.set(paramInt1, paramInt2, paramInt3);
  }
  
  public void set(int paramInt1, int paramInt2, PImage paramPImage)
  {
    if (recorder != null) {
      recorder.set(paramInt1, paramInt2, paramPImage);
    }
    g.set(paramInt1, paramInt2, paramPImage);
  }
  
  public void mask(int[] paramArrayOfInt)
  {
    if (recorder != null) {
      recorder.mask(paramArrayOfInt);
    }
    g.mask(paramArrayOfInt);
  }
  
  public void mask(PImage paramPImage)
  {
    if (recorder != null) {
      recorder.mask(paramPImage);
    }
    g.mask(paramPImage);
  }
  
  public void filter(int paramInt)
  {
    if (recorder != null) {
      recorder.filter(paramInt);
    }
    g.filter(paramInt);
  }
  
  public void filter(int paramInt, float paramFloat)
  {
    if (recorder != null) {
      recorder.filter(paramInt, paramFloat);
    }
    g.filter(paramInt, paramFloat);
  }
  
  public void copy(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    if (recorder != null) {
      recorder.copy(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8);
    }
    g.copy(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8);
  }
  
  public void copy(PImage paramPImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    if (recorder != null) {
      recorder.copy(paramPImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8);
    }
    g.copy(paramPImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8);
  }
  
  public static int blendColor(int paramInt1, int paramInt2, int paramInt3)
  {
    return PGraphics.blendColor(paramInt1, paramInt2, paramInt3);
  }
  
  public void blend(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9)
  {
    if (recorder != null) {
      recorder.blend(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9);
    }
    g.blend(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9);
  }
  
  public void blend(PImage paramPImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9)
  {
    if (recorder != null) {
      recorder.blend(paramPImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9);
    }
    g.blend(paramPImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9);
  }
  
  static
  {
    String str = System.getProperty("os.name");
    if (str.indexOf("Mac") != -1) {
      platform = 2;
    } else if (str.indexOf("Windows") != -1) {
      platform = 1;
    } else if (str.equals("Linux")) {
      platform = 3;
    } else {
      platform = 0;
    }
  }
  
  class AsyncImageLoader
    extends Thread
  {
    String filename;
    String extension;
    PImage vessel;
    
    public AsyncImageLoader(String paramString1, String paramString2, PImage paramPImage)
    {
      filename = paramString1;
      extension = paramString2;
      vessel = paramPImage;
    }
    
    public void run()
    {
      while (requestImageCount == requestImageMax) {
        try
        {
          Thread.sleep(10L);
        }
        catch (InterruptedException localInterruptedException) {}
      }
      requestImageCount += 1;
      PImage localPImage = loadImage(filename, extension);
      if (localPImage == null)
      {
        vessel.width = -1;
        vessel.height = -1;
      }
      else
      {
        vessel.width = width;
        vessel.height = height;
        vessel.format = format;
        vessel.pixels = pixels;
      }
      requestImageCount -= 1;
    }
  }
  
  public class RegisteredMethods
  {
    int count;
    Object[] objects;
    Method[] methods;
    
    public RegisteredMethods() {}
    
    public void handle()
    {
      handle(new Object[0]);
    }
    
    public void handle(Object[] paramArrayOfObject)
    {
      for (int i = 0; i < count; i++) {
        try
        {
          methods[i].invoke(objects[i], paramArrayOfObject);
        }
        catch (Exception localException)
        {
          if ((localException instanceof InvocationTargetException))
          {
            InvocationTargetException localInvocationTargetException = (InvocationTargetException)localException;
            localInvocationTargetException.getTargetException().printStackTrace();
          }
          else
          {
            localException.printStackTrace();
          }
        }
      }
    }
    
    public void add(Object paramObject, Method paramMethod)
    {
      if (objects == null)
      {
        objects = new Object[5];
        methods = new Method[5];
      }
      if (count == objects.length)
      {
        objects = ((Object[])PApplet.expand(objects));
        methods = ((Method[])PApplet.expand(methods));
      }
      objects[count] = paramObject;
      methods[count] = paramMethod;
      count += 1;
    }
    
    public void remove(Object paramObject, Method paramMethod)
    {
      int i = findIndex(paramObject, paramMethod);
      if (i != -1)
      {
        count -= 1;
        for (int j = i; j < count; j++)
        {
          objects[j] = objects[(j + 1)];
          methods[j] = methods[(j + 1)];
        }
        objects[count] = null;
        methods[count] = null;
      }
    }
    
    protected int findIndex(Object paramObject, Method paramMethod)
    {
      for (int i = 0; i < count; i++) {
        if ((objects[i] == paramObject) && (methods[i].equals(paramMethod))) {
          return i;
        }
      }
      return -1;
    }
  }
  
  public static class RendererChangeException
    extends RuntimeException
  {
    public RendererChangeException() {}
  }
}
