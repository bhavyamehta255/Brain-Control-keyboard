package processing.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class PFont
  implements PConstants
{
  protected int glyphCount;
  protected Glyph[] glyphs;
  protected String name;
  protected String psname;
  protected int size;
  protected boolean smooth;
  protected int ascent;
  protected int descent;
  protected int[] ascii;
  protected boolean lazy;
  protected Font font;
  protected boolean stream;
  protected boolean subsetting;
  protected boolean fontSearched;
  protected static Font[] fonts;
  protected static HashMap<String, Font> fontDifferent;
  protected BufferedImage lazyImage;
  protected Graphics2D lazyGraphics;
  protected FontMetrics lazyMetrics;
  protected int[] lazySamples;
  protected HashMap<PGraphics, Object> cacheMap;
  static final char[] EXTRA_CHARS = { '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ' ', '¡', '¢', '£', '¤', '¥', '¦', '§', '¨', '©', 'ª', '«', '¬', '­', '®', '¯', '°', '±', '´', 'µ', '¶', '·', '¸', 'º', '»', '¿', 'À', 'Á', 'Â', 'Ã', 'Ä', 'Å', 'Æ', 'Ç', 'È', 'É', 'Ê', 'Ë', 'Ì', 'Í', 'Î', 'Ï', 'Ñ', 'Ò', 'Ó', 'Ô', 'Õ', 'Ö', '×', 'Ø', 'Ù', 'Ú', 'Û', 'Ü', 'Ý', 'ß', 'à', 'á', 'â', 'ã', 'ä', 'å', 'æ', 'ç', 'è', 'é', 'ê', 'ë', 'ì', 'í', 'î', 'ï', 'ñ', 'ò', 'ó', 'ô', 'õ', 'ö', '÷', 'ø', 'ù', 'ú', 'û', 'ü', 'ý', 'ÿ', 'Ă', 'ă', 'Ą', 'ą', 'Ć', 'ć', 'Č', 'č', 'Ď', 'ď', 'Đ', 'đ', 'Ę', 'ę', 'Ě', 'ě', 'ı', 'Ĺ', 'ĺ', 'Ľ', 'ľ', 'Ł', 'ł', 'Ń', 'ń', 'Ň', 'ň', 'Ő', 'ő', 'Œ', 'œ', 'Ŕ', 'ŕ', 'Ř', 'ř', 'Ś', 'ś', 'Ş', 'ş', 'Š', 'š', 'Ţ', 'ţ', 'Ť', 'ť', 'Ů', 'ů', 'Ű', 'ű', 'Ÿ', 'Ź', 'ź', 'Ż', 'ż', 'Ž', 'ž', 'ƒ', 'ˆ', 'ˇ', '˘', '˙', '˚', '˛', '˜', '˝', 'Ω', 'π', '–', '—', '‘', '’', '‚', '“', '”', '„', '†', '‡', '•', '…', '‰', '‹', '›', '⁄', '€', '™', '∂', '∆', '∏', '∑', '√', '∞', '∫', '≈', '≠', '≤', '≥', '◊', 63743, 64257, 64258 };
  public static char[] CHARSET = new char[94 + EXTRA_CHARS.length];
  
  public PFont() {}
  
  public PFont(Font paramFont, boolean paramBoolean)
  {
    this(paramFont, paramBoolean, null);
  }
  
  public PFont(Font paramFont, boolean paramBoolean, char[] paramArrayOfChar)
  {
    font = paramFont;
    smooth = paramBoolean;
    name = paramFont.getName();
    psname = paramFont.getPSName();
    size = paramFont.getSize();
    int i = 10;
    glyphs = new Glyph[i];
    ascii = new int[''];
    Arrays.fill(ascii, -1);
    int j = size * 3;
    lazyImage = new BufferedImage(j, j, 1);
    lazyGraphics = ((Graphics2D)lazyImage.getGraphics());
    lazyGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, paramBoolean ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
    lazyGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, paramBoolean ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    lazyGraphics.setFont(paramFont);
    lazyMetrics = lazyGraphics.getFontMetrics();
    lazySamples = new int[j * j];
    if (paramArrayOfChar == null)
    {
      lazy = true;
    }
    else
    {
      Arrays.sort(paramArrayOfChar);
      glyphs = new Glyph[paramArrayOfChar.length];
      glyphCount = 0;
      for (char c : paramArrayOfChar) {
        if (paramFont.canDisplay(c))
        {
          Glyph localGlyph = new Glyph(c);
          if (value < 128) {
            ascii[value] = glyphCount;
          }
          index = glyphCount;
          glyphs[(glyphCount++)] = localGlyph;
        }
      }
      if (glyphCount != paramArrayOfChar.length) {
        glyphs = ((Glyph[])PApplet.subset(glyphs, 0, glyphCount));
      }
    }
    if (ascent == 0) {
      if (paramFont.canDisplay('d')) {
        new Glyph('d');
      } else {
        ascent = lazyMetrics.getAscent();
      }
    }
    if (descent == 0) {
      if (paramFont.canDisplay('p')) {
        new Glyph('p');
      } else {
        descent = lazyMetrics.getDescent();
      }
    }
  }
  
  public PFont(Font paramFont, boolean paramBoolean1, char[] paramArrayOfChar, boolean paramBoolean2)
  {
    this(paramFont, paramBoolean1, paramArrayOfChar);
    stream = paramBoolean2;
  }
  
  public PFont(InputStream paramInputStream)
    throws IOException
  {
    DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
    glyphCount = localDataInputStream.readInt();
    int i = localDataInputStream.readInt();
    size = localDataInputStream.readInt();
    localDataInputStream.readInt();
    ascent = localDataInputStream.readInt();
    descent = localDataInputStream.readInt();
    glyphs = new Glyph[glyphCount];
    ascii = new int[''];
    Arrays.fill(ascii, -1);
    for (int j = 0; j < glyphCount; j++)
    {
      Glyph localGlyph1 = new Glyph(localDataInputStream);
      if (value < 128) {
        ascii[value] = j;
      }
      index = j;
      glyphs[j] = localGlyph1;
    }
    if ((ascent == 0) && (descent == 0)) {
      throw new RuntimeException("Please use \"Create Font\" to re-create this font.");
    }
    for (Glyph localGlyph2 : glyphs) {
      localGlyph2.readBitmap(localDataInputStream);
    }
    if (i >= 10)
    {
      name = localDataInputStream.readUTF();
      psname = localDataInputStream.readUTF();
    }
    if (i == 11) {
      smooth = localDataInputStream.readBoolean();
    }
    findFont();
  }
  
  void delete()
  {
    if (cacheMap != null)
    {
      Set localSet = cacheMap.keySet();
      if (!localSet.isEmpty())
      {
        Object[] arrayOfObject = localSet.toArray();
        for (int i = 0; i < arrayOfObject.length; i++)
        {
          Object localObject = getCache((PGraphics)arrayOfObject[i]);
          Method localMethod = null;
          try
          {
            Class localClass = localObject.getClass();
            localMethod = localClass.getMethod("delete", new Class[0]);
          }
          catch (Exception localException1) {}
          if (localMethod != null) {
            try
            {
              localMethod.invoke(localObject, new Object[0]);
            }
            catch (Exception localException2) {}
          }
        }
      }
    }
  }
  
  public void save(OutputStream paramOutputStream)
    throws IOException
  {
    DataOutputStream localDataOutputStream = new DataOutputStream(paramOutputStream);
    localDataOutputStream.writeInt(glyphCount);
    if ((name == null) || (psname == null))
    {
      name = "";
      psname = "";
    }
    localDataOutputStream.writeInt(11);
    localDataOutputStream.writeInt(size);
    localDataOutputStream.writeInt(0);
    localDataOutputStream.writeInt(ascent);
    localDataOutputStream.writeInt(descent);
    for (int i = 0; i < glyphCount; i++) {
      glyphs[i].writeHeader(localDataOutputStream);
    }
    for (i = 0; i < glyphCount; i++) {
      glyphs[i].writeBitmap(localDataOutputStream);
    }
    localDataOutputStream.writeUTF(name);
    localDataOutputStream.writeUTF(psname);
    localDataOutputStream.writeBoolean(smooth);
    localDataOutputStream.flush();
  }
  
  protected void addGlyph(char paramChar)
  {
    Glyph localGlyph = new Glyph(paramChar);
    if (glyphCount == glyphs.length) {
      glyphs = ((Glyph[])PApplet.expand(glyphs));
    }
    if (glyphCount == 0)
    {
      index = 0;
      glyphs[glyphCount] = localGlyph;
      if (value < 128) {
        ascii[value] = 0;
      }
    }
    else if (glyphs[(glyphCount - 1)].value < value)
    {
      glyphs[glyphCount] = localGlyph;
      if (value < 128) {
        ascii[value] = glyphCount;
      }
    }
    else
    {
      for (int i = 0; i < glyphCount; i++) {
        if (glyphs[i].value > paramChar)
        {
          for (int j = glyphCount; j > i; j--)
          {
            glyphs[j] = glyphs[(j - 1)];
            if (glyphs[j].value < 128) {
              ascii[glyphs[j].value] = j;
            }
          }
          index = i;
          glyphs[i] = localGlyph;
          if (paramChar >= '') {
            break;
          }
          ascii[paramChar] = i;
          break;
        }
      }
    }
    glyphCount += 1;
  }
  
  public String getName()
  {
    return name;
  }
  
  public String getPostScriptName()
  {
    return psname;
  }
  
  public void setFont(Font paramFont)
  {
    font = paramFont;
  }
  
  public Font getFont()
  {
    if (subsetting) {
      return null;
    }
    return font;
  }
  
  public int getSize()
  {
    return size;
  }
  
  public boolean isStream()
  {
    return stream;
  }
  
  public void setSubsetting()
  {
    subsetting = true;
  }
  
  public Font findFont()
  {
    if ((font == null) && (!fontSearched))
    {
      font = new Font(name, 0, size);
      if (!font.getPSName().equals(psname)) {
        font = new Font(psname, 0, size);
      }
      if (!font.getPSName().equals(psname)) {
        font = null;
      }
      fontSearched = true;
    }
    return font;
  }
  
  public Glyph getGlyph(char paramChar)
  {
    int i = index(paramChar);
    return i == -1 ? null : glyphs[i];
  }
  
  protected int index(char paramChar)
  {
    if (lazy)
    {
      int i = indexActual(paramChar);
      if (i != -1) {
        return i;
      }
      if ((font != null) && (font.canDisplay(paramChar)))
      {
        addGlyph(paramChar);
        return indexActual(paramChar);
      }
      return -1;
    }
    return indexActual(paramChar);
  }
  
  protected int indexActual(char paramChar)
  {
    if (glyphCount == 0) {
      return -1;
    }
    if (paramChar < '') {
      return ascii[paramChar];
    }
    return indexHunt(paramChar, 0, glyphCount - 1);
  }
  
  protected int indexHunt(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = (paramInt2 + paramInt3) / 2;
    if (paramInt1 == glyphs[i].value) {
      return i;
    }
    if (paramInt2 >= paramInt3) {
      return -1;
    }
    if (paramInt1 < glyphs[i].value) {
      return indexHunt(paramInt1, paramInt2, i - 1);
    }
    return indexHunt(paramInt1, i + 1, paramInt3);
  }
  
  public float kern(char paramChar1, char paramChar2)
  {
    return 0.0F;
  }
  
  public float ascent()
  {
    return ascent / size;
  }
  
  public float descent()
  {
    return descent / size;
  }
  
  public float width(char paramChar)
  {
    if (paramChar == ' ') {
      return width('i');
    }
    int i = index(paramChar);
    if (i == -1) {
      return 0.0F;
    }
    return glyphs[i].setWidth / size;
  }
  
  public void setCache(PGraphics paramPGraphics, Object paramObject)
  {
    if (cacheMap == null) {
      cacheMap = new HashMap();
    }
    cacheMap.put(paramPGraphics, paramObject);
  }
  
  public Object getCache(PGraphics paramPGraphics)
  {
    if (cacheMap == null) {
      return null;
    }
    return cacheMap.get(paramPGraphics);
  }
  
  public void removeCache(PGraphics paramPGraphics)
  {
    if (cacheMap != null) {
      cacheMap.remove(paramPGraphics);
    }
  }
  
  public int getGlyphCount()
  {
    return glyphCount;
  }
  
  public Glyph getGlyph(int paramInt)
  {
    return glyphs[paramInt];
  }
  
  public static String[] list()
  {
    loadFonts();
    String[] arrayOfString = new String[fonts.length];
    for (int i = 0; i < arrayOfString.length; i++) {
      arrayOfString[i] = fonts[i].getName();
    }
    return arrayOfString;
  }
  
  public static void loadFonts()
  {
    if (fonts == null)
    {
      GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
      fonts = localGraphicsEnvironment.getAllFonts();
      if (PApplet.platform == 2)
      {
        fontDifferent = new HashMap();
        for (Font localFont : fonts) {
          fontDifferent.put(localFont.getName(), localFont);
        }
      }
    }
  }
  
  public static Font findFont(String paramString)
  {
    
    if (PApplet.platform == 2)
    {
      Font localFont = (Font)fontDifferent.get(paramString);
      if (localFont != null) {
        return localFont;
      }
    }
    return new Font(paramString, 0, 1);
  }
  
  static
  {
    int i = 0;
    for (int j = 33; j <= 126; j++) {
      CHARSET[(i++)] = ((char)j);
    }
    for (j = 0; j < EXTRA_CHARS.length; j++) {
      CHARSET[(i++)] = EXTRA_CHARS[j];
    }
  }
  
  public class Glyph
  {
    public PImage image;
    public int value;
    public int height;
    public int width;
    public int index;
    public int setWidth;
    public int topExtent;
    public int leftExtent;
    
    public Glyph()
    {
      index = -1;
    }
    
    public Glyph(DataInputStream paramDataInputStream)
      throws IOException
    {
      index = -1;
      readHeader(paramDataInputStream);
    }
    
    protected void readHeader(DataInputStream paramDataInputStream)
      throws IOException
    {
      value = paramDataInputStream.readInt();
      height = paramDataInputStream.readInt();
      width = paramDataInputStream.readInt();
      setWidth = paramDataInputStream.readInt();
      topExtent = paramDataInputStream.readInt();
      leftExtent = paramDataInputStream.readInt();
      paramDataInputStream.readInt();
      if ((value == 100) && (ascent == 0)) {
        ascent = topExtent;
      }
      if ((value == 112) && (descent == 0)) {
        descent = (-topExtent + height);
      }
    }
    
    protected void writeHeader(DataOutputStream paramDataOutputStream)
      throws IOException
    {
      paramDataOutputStream.writeInt(value);
      paramDataOutputStream.writeInt(height);
      paramDataOutputStream.writeInt(width);
      paramDataOutputStream.writeInt(setWidth);
      paramDataOutputStream.writeInt(topExtent);
      paramDataOutputStream.writeInt(leftExtent);
      paramDataOutputStream.writeInt(0);
    }
    
    protected void readBitmap(DataInputStream paramDataInputStream)
      throws IOException
    {
      image = new PImage(width, height, 4);
      int i = width * height;
      byte[] arrayOfByte = new byte[i];
      paramDataInputStream.readFully(arrayOfByte);
      int j = width;
      int k = height;
      int[] arrayOfInt = image.pixels;
      for (int m = 0; m < k; m++) {
        for (int n = 0; n < j; n++) {
          arrayOfInt[(m * width + n)] = (arrayOfByte[(m * j + n)] & 0xFF);
        }
      }
    }
    
    protected void writeBitmap(DataOutputStream paramDataOutputStream)
      throws IOException
    {
      int[] arrayOfInt = image.pixels;
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          paramDataOutputStream.write(arrayOfInt[(i * width + j)] & 0xFF);
        }
      }
    }
    
    protected Glyph(char paramChar)
    {
      int i = size * 3;
      lazyGraphics.setColor(Color.white);
      lazyGraphics.fillRect(0, 0, i, i);
      lazyGraphics.setColor(Color.black);
      lazyGraphics.drawString(String.valueOf(paramChar), size, size * 2);
      WritableRaster localWritableRaster = lazyImage.getRaster();
      localWritableRaster.getDataElements(0, 0, i, i, lazySamples);
      int j = 1000;
      int k = 0;
      int m = 1000;
      int n = 0;
      int i1 = 0;
      int i4;
      for (int i2 = 0; i2 < i; i2++) {
        for (i3 = 0; i3 < i; i3++)
        {
          i4 = lazySamples[(i2 * i + i3)] & 0xFF;
          if (i4 != 255)
          {
            if (i3 < j) {
              j = i3;
            }
            if (i2 < m) {
              m = i2;
            }
            if (i3 > k) {
              k = i3;
            }
            if (i2 > n) {
              n = i2;
            }
            i1 = 1;
          }
        }
      }
      if (i1 == 0)
      {
        j = m = 0;
        k = n = 0;
      }
      value = paramChar;
      height = (n - m + 1);
      width = (k - j + 1);
      setWidth = lazyMetrics.charWidth(paramChar);
      topExtent = (size * 2 - m);
      leftExtent = (j - size);
      image = new PImage(width, height, 4);
      int[] arrayOfInt = image.pixels;
      for (int i3 = m; i3 <= n; i3++) {
        for (i4 = j; i4 <= k; i4++)
        {
          int i5 = 255 - (lazySamples[(i3 * i + i4)] & 0xFF);
          int i6 = (i3 - m) * width + (i4 - j);
          arrayOfInt[i6] = i5;
        }
      }
      if ((value == 100) && (ascent == 0)) {
        ascent = topExtent;
      }
      if ((value == 112) && (descent == 0)) {
        descent = (-topExtent + height);
      }
    }
  }
}
