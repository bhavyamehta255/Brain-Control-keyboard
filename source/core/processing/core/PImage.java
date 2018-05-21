package processing.core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;
import javax.imageio.ImageIO;

public class PImage
  implements PConstants, Cloneable
{
  public int format;
  public int[] pixels;
  public int width;
  public int height;
  public PApplet parent;
  protected HashMap<PGraphics, Object> cacheMap;
  protected HashMap<PGraphics, Object> paramMap;
  protected boolean modified;
  protected int mx1;
  protected int my1;
  protected int mx2;
  protected int my2;
  private int fracU;
  private int ifU;
  private int fracV;
  private int ifV;
  private int u1;
  private int u2;
  private int v1;
  private int v2;
  private int sX;
  private int sY;
  private int iw;
  private int iw1;
  private int ih1;
  private int ul;
  private int ll;
  private int ur;
  private int lr;
  private int cUL;
  private int cLL;
  private int cUR;
  private int cLR;
  private int srcXOffset;
  private int srcYOffset;
  private int r;
  private int g;
  private int b;
  private int a;
  private int[] srcBuffer;
  static final int PRECISIONB = 15;
  static final int PRECISIONF = 32768;
  static final int PREC_MAXVAL = 32767;
  static final int PREC_ALPHA_SHIFT = 9;
  static final int PREC_RED_SHIFT = 1;
  private int blurRadius;
  private int blurKernelSize;
  private int[] blurKernel;
  private int[][] blurMult;
  static byte[] TIFF_HEADER = { 77, 77, 0, 42, 0, 0, 0, 8, 0, 9, 0, -2, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 3, 0, 0, 0, 1, 0, 0, 0, 0, 1, 2, 0, 3, 0, 0, 0, 3, 0, 0, 0, 122, 1, 6, 0, 3, 0, 0, 0, 1, 0, 2, 0, 0, 1, 17, 0, 4, 0, 0, 0, 1, 0, 0, 3, 0, 1, 21, 0, 3, 0, 0, 0, 1, 0, 3, 0, 0, 1, 22, 0, 3, 0, 0, 0, 1, 0, 0, 0, 0, 1, 23, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 8, 0, 8 };
  static final String TIFF_ERROR = "Error: Processing can only read its own TIFF files.";
  protected String[] saveImageFormats;
  
  public PImage()
  {
    format = 2;
  }
  
  public PImage(int paramInt1, int paramInt2)
  {
    init(paramInt1, paramInt2, 1);
  }
  
  public PImage(int paramInt1, int paramInt2, int paramInt3)
  {
    init(paramInt1, paramInt2, paramInt3);
  }
  
  public void init(int paramInt1, int paramInt2, int paramInt3)
  {
    width = paramInt1;
    height = paramInt2;
    pixels = new int[paramInt1 * paramInt2];
    format = paramInt3;
  }
  
  protected void checkAlpha()
  {
    if (pixels == null) {
      return;
    }
    for (int i = 0; i < pixels.length; i++) {
      if ((pixels[i] & 0xFF000000) != -16777216)
      {
        format = 2;
        break;
      }
    }
  }
  
  public PImage(Image paramImage)
  {
    format = 1;
    Object localObject;
    if ((paramImage instanceof BufferedImage))
    {
      localObject = (BufferedImage)paramImage;
      width = ((BufferedImage)localObject).getWidth();
      height = ((BufferedImage)localObject).getHeight();
      pixels = new int[width * height];
      WritableRaster localWritableRaster = ((BufferedImage)localObject).getRaster();
      localWritableRaster.getDataElements(0, 0, width, height, pixels);
      if (((BufferedImage)localObject).getType() == 2) {
        format = 2;
      }
    }
    else
    {
      width = paramImage.getWidth(null);
      height = paramImage.getHeight(null);
      pixels = new int[width * height];
      localObject = new PixelGrabber(paramImage, 0, 0, width, height, pixels, 0, width);
      try
      {
        ((PixelGrabber)localObject).grabPixels();
      }
      catch (InterruptedException localInterruptedException) {}
    }
  }
  
  public Image getImage()
  {
    loadPixels();
    int i = format == 1 ? 1 : 2;
    BufferedImage localBufferedImage = new BufferedImage(width, height, i);
    WritableRaster localWritableRaster = localBufferedImage.getRaster();
    localWritableRaster.setDataElements(0, 0, width, height, pixels);
    return localBufferedImage;
  }
  
  public void delete()
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
  
  public void setParams(PGraphics paramPGraphics, Object paramObject)
  {
    if (paramMap == null) {
      paramMap = new HashMap();
    }
    paramMap.put(paramPGraphics, paramObject);
  }
  
  public Object getParams(PGraphics paramPGraphics)
  {
    if (paramMap == null) {
      return null;
    }
    return paramMap.get(paramPGraphics);
  }
  
  public void removeParams(PGraphics paramPGraphics)
  {
    if (paramMap != null) {
      paramMap.remove(paramPGraphics);
    }
  }
  
  public boolean isModified()
  {
    return modified;
  }
  
  public void setModified()
  {
    modified = true;
  }
  
  public void setModified(boolean paramBoolean)
  {
    modified = paramBoolean;
  }
  
  public int getModifiedX1()
  {
    return mx1;
  }
  
  public int getModifiedX2()
  {
    return mx2;
  }
  
  public int getModifiedY1()
  {
    return my1;
  }
  
  public int getModifiedY2()
  {
    return my2;
  }
  
  public void loadPixels() {}
  
  public void updatePixels()
  {
    updatePixelsImpl(0, 0, width, height);
  }
  
  public void updatePixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    updatePixelsImpl(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected void updatePixelsImpl(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt1 + paramInt3;
    int j = paramInt2 + paramInt4;
    if (!modified)
    {
      mx1 = paramInt1;
      mx2 = i;
      my1 = paramInt2;
      my2 = j;
      modified = true;
    }
    else
    {
      if (paramInt1 < mx1) {
        mx1 = paramInt1;
      }
      if (paramInt1 > mx2) {
        mx2 = paramInt1;
      }
      if (paramInt2 < my1) {
        my1 = paramInt2;
      }
      if (paramInt2 > my2) {
        my2 = paramInt2;
      }
      if (i < mx1) {
        mx1 = i;
      }
      if (i > mx2) {
        mx2 = i;
      }
      if (j < my1) {
        my1 = j;
      }
      if (j > my2) {
        my2 = j;
      }
    }
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    return get();
  }
  
  public void resize(int paramInt1, int paramInt2)
  {
    loadPixels();
    if ((paramInt1 <= 0) && (paramInt2 <= 0))
    {
      width = 0;
      height = 0;
      pixels = new int[0];
    }
    else
    {
      float f;
      if (paramInt1 == 0)
      {
        f = paramInt2 / height;
        paramInt1 = (int)(width * f);
      }
      else if (paramInt2 == 0)
      {
        f = paramInt1 / width;
        paramInt2 = (int)(height * f);
      }
      PImage localPImage = new PImage(paramInt1, paramInt2, format);
      localPImage.copy(this, 0, 0, width, height, 0, 0, paramInt1, paramInt2);
      width = paramInt1;
      height = paramInt2;
      pixels = pixels;
    }
    updatePixels();
  }
  
  public int get(int paramInt1, int paramInt2)
  {
    if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= width) || (paramInt2 >= height)) {
      return 0;
    }
    switch (format)
    {
    case 1: 
      return pixels[(paramInt2 * width + paramInt1)] | 0xFF000000;
    case 2: 
      return pixels[(paramInt2 * width + paramInt1)];
    case 4: 
      return pixels[(paramInt2 * width + paramInt1)] << 24 | 0xFFFFFF;
    }
    return 0;
  }
  
  public PImage get(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (paramInt1 < 0)
    {
      paramInt3 += paramInt1;
      paramInt1 = 0;
    }
    if (paramInt2 < 0)
    {
      paramInt4 += paramInt2;
      paramInt2 = 0;
    }
    if (paramInt1 + paramInt3 > width) {
      paramInt3 = width - paramInt1;
    }
    if (paramInt2 + paramInt4 > height) {
      paramInt4 = height - paramInt2;
    }
    return getImpl(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected PImage getImpl(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    PImage localPImage = new PImage(paramInt3, paramInt4, format);
    parent = parent;
    int i = paramInt2 * width + paramInt1;
    int j = 0;
    for (int k = paramInt2; k < paramInt2 + paramInt4; k++)
    {
      System.arraycopy(pixels, i, pixels, j, paramInt3);
      i += width;
      j += paramInt3;
    }
    return localPImage;
  }
  
  public PImage get()
  {
    return get(0, 0, width, height);
  }
  
  public void set(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= width) || (paramInt2 >= height)) {
      return;
    }
    pixels[(paramInt2 * width + paramInt1)] = paramInt3;
    updatePixelsImpl(paramInt1, paramInt2, paramInt1 + 1, paramInt2 + 1);
  }
  
  public void set(int paramInt1, int paramInt2, PImage paramPImage)
  {
    int i = 0;
    int j = 0;
    int k = width;
    int m = height;
    if (paramInt1 < 0)
    {
      i -= paramInt1;
      k += paramInt1;
      paramInt1 = 0;
    }
    if (paramInt2 < 0)
    {
      j -= paramInt2;
      m += paramInt2;
      paramInt2 = 0;
    }
    if (paramInt1 + k > width) {
      k = width - paramInt1;
    }
    if (paramInt2 + m > height) {
      m = height - paramInt2;
    }
    if ((k <= 0) || (m <= 0)) {
      return;
    }
    setImpl(paramInt1, paramInt2, i, j, k, m, paramPImage);
  }
  
  protected void setImpl(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, PImage paramPImage)
  {
    int i = paramInt4 * width + paramInt3;
    int j = paramInt2 * width + paramInt1;
    for (int k = paramInt4; k < paramInt4 + paramInt6; k++)
    {
      System.arraycopy(pixels, i, pixels, j, paramInt5);
      i += width;
      j += width;
    }
    updatePixelsImpl(paramInt3, paramInt4, paramInt3 + paramInt5, paramInt4 + paramInt6);
  }
  
  public void mask(int[] paramArrayOfInt)
  {
    loadPixels();
    if (paramArrayOfInt.length != pixels.length) {
      throw new RuntimeException("The PImage used with mask() must be the same size as the applet.");
    }
    for (int i = 0; i < pixels.length; i++) {
      pixels[i] = ((paramArrayOfInt[i] & 0xFF) << 24 | pixels[i] & 0xFFFFFF);
    }
    format = 2;
    updatePixels();
  }
  
  public void mask(PImage paramPImage)
  {
    paramPImage.loadPixels();
    mask(pixels);
  }
  
  public void filter(int paramInt)
  {
    loadPixels();
    int i;
    switch (paramInt)
    {
    case 11: 
      filter(11, 1.0F);
      break;
    case 12: 
      int j;
      if (format == 4)
      {
        for (i = 0; i < pixels.length; i++)
        {
          j = 255 - pixels[i];
          pixels[i] = (0xFF000000 | j << 16 | j << 8 | j);
        }
        format = 1;
      }
      else
      {
        for (i = 0; i < pixels.length; i++)
        {
          j = pixels[i];
          int k = 77 * (j >> 16 & 0xFF) + 151 * (j >> 8 & 0xFF) + 28 * (j & 0xFF) >> 8;
          pixels[i] = (j & 0xFF000000 | k << 16 | k << 8 | k);
        }
      }
      break;
    case 13: 
      for (i = 0; i < pixels.length; i++) {
        pixels[i] ^= 0xFFFFFF;
      }
      break;
    case 15: 
      throw new RuntimeException("Use filter(POSTERIZE, int levels) instead of filter(POSTERIZE)");
    case 14: 
      for (i = 0; i < pixels.length; i++) {
        pixels[i] |= 0xFF000000;
      }
      format = 1;
      break;
    case 16: 
      filter(16, 0.5F);
      break;
    case 17: 
      dilate(true);
      break;
    case 18: 
      dilate(false);
    }
    updatePixels();
  }
  
  public void filter(int paramInt, float paramFloat)
  {
    loadPixels();
    int k;
    int m;
    int n;
    switch (paramInt)
    {
    case 11: 
      if (format == 4) {
        blurAlpha(paramFloat);
      } else if (format == 2) {
        blurARGB(paramFloat);
      } else {
        blurRGB(paramFloat);
      }
      break;
    case 12: 
      throw new RuntimeException("Use filter(GRAY) instead of filter(GRAY, param)");
    case 13: 
      throw new RuntimeException("Use filter(INVERT) instead of filter(INVERT, param)");
    case 14: 
      throw new RuntimeException("Use filter(OPAQUE) instead of filter(OPAQUE, param)");
    case 15: 
      int i = (int)paramFloat;
      if ((i < 2) || (i > 255)) {
        throw new RuntimeException("Levels must be between 2 and 255 for filter(POSTERIZE, levels)");
      }
      int j = i - 1;
      for (k = 0; k < pixels.length; k++)
      {
        m = pixels[k] >> 16 & 0xFF;
        n = pixels[k] >> 8 & 0xFF;
        int i1 = pixels[k] & 0xFF;
        m = (m * i >> 8) * 255 / j;
        n = (n * i >> 8) * 255 / j;
        i1 = (i1 * i >> 8) * 255 / j;
        pixels[k] = (0xFF000000 & pixels[k] | m << 16 | n << 8 | i1);
      }
      break;
    case 16: 
      k = (int)(paramFloat * 255.0F);
      for (m = 0; m < pixels.length; m++)
      {
        n = Math.max((pixels[m] & 0xFF0000) >> 16, Math.max((pixels[m] & 0xFF00) >> 8, pixels[m] & 0xFF));
        pixels[m] = (pixels[m] & 0xFF000000 | (n < k ? 0 : 16777215));
      }
      break;
    case 17: 
      throw new RuntimeException("Use filter(ERODE) instead of filter(ERODE, param)");
    case 18: 
      throw new RuntimeException("Use filter(DILATE) instead of filter(DILATE, param)");
    }
    updatePixels();
  }
  
  protected void buildBlurKernel(float paramFloat)
  {
    int i = (int)(paramFloat * 3.5F);
    i = i < 248 ? i : i < 1 ? 1 : 248;
    if (blurRadius != i)
    {
      blurRadius = i;
      blurKernelSize = (1 + blurRadius << 1);
      blurKernel = new int[blurKernelSize];
      blurMult = new int[blurKernelSize]['Ā'];
      int m = 1;
      int n = i - 1;
      while (m < i)
      {
        int k;
        int tmp116_114 = (k = n * n);
        blurKernel[n] = tmp116_114;
        blurKernel[(i + m)] = tmp116_114;
        arrayOfInt1 = blurMult[(i + m)];
        int[] arrayOfInt2 = blurMult[(n--)];
        for (int i1 = 0; i1 < 256; i1++)
        {
          int tmp166_165 = (k * i1);
          arrayOfInt2[i1] = tmp166_165;
          arrayOfInt1[i1] = tmp166_165;
        }
        m++;
      }
      int j = blurKernel[i] = i * i;
      int[] arrayOfInt1 = blurMult[i];
      for (m = 0; m < 256; m++) {
        arrayOfInt1[m] = (j * m);
      }
    }
  }
  
  protected void blurAlpha(float paramFloat)
  {
    int[] arrayOfInt1 = new int[pixels.length];
    int i3 = 0;
    buildBlurKernel(paramFloat);
    int i5;
    int i;
    int j;
    int k;
    int i2;
    int i6;
    int m;
    for (int i4 = 0; i4 < height; i4++)
    {
      for (i5 = 0; i5 < width; i5++)
      {
        j = i = 0;
        k = i5 - blurRadius;
        if (k < 0)
        {
          i2 = -k;
          k = 0;
        }
        else
        {
          if (k >= width) {
            break;
          }
          i2 = 0;
        }
        for (i6 = i2; (i6 < blurKernelSize) && (k < width); i6++)
        {
          int i7 = pixels[(k + i3)];
          int[] arrayOfInt3 = blurMult[i6];
          j += arrayOfInt3[(i7 & 0xFF)];
          i += blurKernel[i6];
          k++;
        }
        m = i3 + i5;
        arrayOfInt1[m] = (j / i);
      }
      i3 += width;
    }
    i3 = 0;
    int n = -blurRadius;
    int i1 = n * width;
    for (i4 = 0; i4 < height; i4++)
    {
      for (i5 = 0; i5 < width; i5++)
      {
        j = i = 0;
        if (n < 0)
        {
          i2 = m = -n;
          k = i5;
        }
        else
        {
          if (n >= height) {
            break;
          }
          i2 = 0;
          m = n;
          k = i5 + i1;
        }
        for (i6 = i2; (i6 < blurKernelSize) && (m < height); i6++)
        {
          int[] arrayOfInt2 = blurMult[i6];
          j += arrayOfInt2[arrayOfInt1[k]];
          i += blurKernel[i6];
          m++;
          k += width;
        }
        pixels[(i5 + i3)] = (j / i);
      }
      i3 += width;
      i1 += width;
      n++;
    }
  }
  
  protected void blurRGB(float paramFloat)
  {
    int[] arrayOfInt1 = new int[pixels.length];
    int[] arrayOfInt2 = new int[pixels.length];
    int[] arrayOfInt3 = new int[pixels.length];
    int i5 = 0;
    buildBlurKernel(paramFloat);
    int i7;
    int i;
    int j;
    int k;
    int m;
    int n;
    int i4;
    int i8;
    int i1;
    for (int i6 = 0; i6 < height; i6++)
    {
      for (i7 = 0; i7 < width; i7++)
      {
        m = k = j = i = 0;
        n = i7 - blurRadius;
        if (n < 0)
        {
          i4 = -n;
          n = 0;
        }
        else
        {
          if (n >= width) {
            break;
          }
          i4 = 0;
        }
        for (i8 = i4; (i8 < blurKernelSize) && (n < width); i8++)
        {
          int i9 = pixels[(n + i5)];
          int[] arrayOfInt5 = blurMult[i8];
          j += arrayOfInt5[((i9 & 0xFF0000) >> 16)];
          k += arrayOfInt5[((i9 & 0xFF00) >> 8)];
          m += arrayOfInt5[(i9 & 0xFF)];
          i += blurKernel[i8];
          n++;
        }
        i1 = i5 + i7;
        arrayOfInt1[i1] = (j / i);
        arrayOfInt2[i1] = (k / i);
        arrayOfInt3[i1] = (m / i);
      }
      i5 += width;
    }
    i5 = 0;
    int i2 = -blurRadius;
    int i3 = i2 * width;
    for (i6 = 0; i6 < height; i6++)
    {
      for (i7 = 0; i7 < width; i7++)
      {
        m = k = j = i = 0;
        if (i2 < 0)
        {
          i4 = i1 = -i2;
          n = i7;
        }
        else
        {
          if (i2 >= height) {
            break;
          }
          i4 = 0;
          i1 = i2;
          n = i7 + i3;
        }
        for (i8 = i4; (i8 < blurKernelSize) && (i1 < height); i8++)
        {
          int[] arrayOfInt4 = blurMult[i8];
          j += arrayOfInt4[arrayOfInt1[n]];
          k += arrayOfInt4[arrayOfInt2[n]];
          m += arrayOfInt4[arrayOfInt3[n]];
          i += blurKernel[i8];
          i1++;
          n += width;
        }
        pixels[(i7 + i5)] = (0xFF000000 | j / i << 16 | k / i << 8 | m / i);
      }
      i5 += width;
      i3 += width;
      i2++;
    }
  }
  
  protected void blurARGB(float paramFloat)
  {
    int i6 = pixels.length;
    int[] arrayOfInt1 = new int[i6];
    int[] arrayOfInt2 = new int[i6];
    int[] arrayOfInt3 = new int[i6];
    int[] arrayOfInt4 = new int[i6];
    int i7 = 0;
    buildBlurKernel(paramFloat);
    int i9;
    int i;
    int n;
    int j;
    int k;
    int m;
    int i1;
    int i5;
    int i10;
    int i2;
    for (int i8 = 0; i8 < height; i8++)
    {
      for (i9 = 0; i9 < width; i9++)
      {
        m = k = j = n = i = 0;
        i1 = i9 - blurRadius;
        if (i1 < 0)
        {
          i5 = -i1;
          i1 = 0;
        }
        else
        {
          if (i1 >= width) {
            break;
          }
          i5 = 0;
        }
        for (i10 = i5; (i10 < blurKernelSize) && (i1 < width); i10++)
        {
          int i11 = pixels[(i1 + i7)];
          int[] arrayOfInt6 = blurMult[i10];
          n += arrayOfInt6[((i11 & 0xFF000000) >>> 24)];
          j += arrayOfInt6[((i11 & 0xFF0000) >> 16)];
          k += arrayOfInt6[((i11 & 0xFF00) >> 8)];
          m += arrayOfInt6[(i11 & 0xFF)];
          i += blurKernel[i10];
          i1++;
        }
        i2 = i7 + i9;
        arrayOfInt4[i2] = (n / i);
        arrayOfInt1[i2] = (j / i);
        arrayOfInt2[i2] = (k / i);
        arrayOfInt3[i2] = (m / i);
      }
      i7 += width;
    }
    i7 = 0;
    int i3 = -blurRadius;
    int i4 = i3 * width;
    for (i8 = 0; i8 < height; i8++)
    {
      for (i9 = 0; i9 < width; i9++)
      {
        m = k = j = n = i = 0;
        if (i3 < 0)
        {
          i5 = i2 = -i3;
          i1 = i9;
        }
        else
        {
          if (i3 >= height) {
            break;
          }
          i5 = 0;
          i2 = i3;
          i1 = i9 + i4;
        }
        for (i10 = i5; (i10 < blurKernelSize) && (i2 < height); i10++)
        {
          int[] arrayOfInt5 = blurMult[i10];
          n += arrayOfInt5[arrayOfInt4[i1]];
          j += arrayOfInt5[arrayOfInt1[i1]];
          k += arrayOfInt5[arrayOfInt2[i1]];
          m += arrayOfInt5[arrayOfInt3[i1]];
          i += blurKernel[i10];
          i2++;
          i1 += width;
        }
        pixels[(i9 + i7)] = (n / i << 24 | j / i << 16 | k / i << 8 | m / i);
      }
      i7 += width;
      i4 += width;
      i3++;
    }
  }
  
  protected void dilate(boolean paramBoolean)
  {
    int i = 0;
    int j = pixels.length;
    int[] arrayOfInt = new int[j];
    int k;
    int m;
    int i1;
    int n;
    int i2;
    int i3;
    int i4;
    int i5;
    int i6;
    int i7;
    int i8;
    int i9;
    int i10;
    int i11;
    int i12;
    int i13;
    int i14;
    if (!paramBoolean) {
      while (i < j)
      {
        k = i;
        m = i + width;
        while (i < m)
        {
          n = i1 = pixels[i];
          i2 = i - 1;
          i3 = i + 1;
          i4 = i - width;
          i5 = i + width;
          if (i2 < k) {
            i2 = i;
          }
          if (i3 >= m) {
            i3 = i;
          }
          if (i4 < 0) {
            i4 = i;
          }
          if (i5 >= j) {
            i5 = i;
          }
          i6 = pixels[i4];
          i7 = pixels[i2];
          i8 = pixels[i5];
          i9 = pixels[i3];
          i10 = 77 * (n >> 16 & 0xFF) + 151 * (n >> 8 & 0xFF) + 28 * (n & 0xFF);
          i11 = 77 * (i7 >> 16 & 0xFF) + 151 * (i7 >> 8 & 0xFF) + 28 * (i7 & 0xFF);
          i12 = 77 * (i9 >> 16 & 0xFF) + 151 * (i9 >> 8 & 0xFF) + 28 * (i9 & 0xFF);
          i13 = 77 * (i6 >> 16 & 0xFF) + 151 * (i6 >> 8 & 0xFF) + 28 * (i6 & 0xFF);
          i14 = 77 * (i8 >> 16 & 0xFF) + 151 * (i8 >> 8 & 0xFF) + 28 * (i8 & 0xFF);
          if (i11 > i10)
          {
            i1 = i7;
            i10 = i11;
          }
          if (i12 > i10)
          {
            i1 = i9;
            i10 = i12;
          }
          if (i13 > i10)
          {
            i1 = i6;
            i10 = i13;
          }
          if (i14 > i10)
          {
            i1 = i8;
            i10 = i14;
          }
          arrayOfInt[(i++)] = i1;
        }
      }
    }
    while (i < j)
    {
      k = i;
      m = i + width;
      while (i < m)
      {
        n = i1 = pixels[i];
        i2 = i - 1;
        i3 = i + 1;
        i4 = i - width;
        i5 = i + width;
        if (i2 < k) {
          i2 = i;
        }
        if (i3 >= m) {
          i3 = i;
        }
        if (i4 < 0) {
          i4 = i;
        }
        if (i5 >= j) {
          i5 = i;
        }
        i6 = pixels[i4];
        i7 = pixels[i2];
        i8 = pixels[i5];
        i9 = pixels[i3];
        i10 = 77 * (n >> 16 & 0xFF) + 151 * (n >> 8 & 0xFF) + 28 * (n & 0xFF);
        i11 = 77 * (i7 >> 16 & 0xFF) + 151 * (i7 >> 8 & 0xFF) + 28 * (i7 & 0xFF);
        i12 = 77 * (i9 >> 16 & 0xFF) + 151 * (i9 >> 8 & 0xFF) + 28 * (i9 & 0xFF);
        i13 = 77 * (i6 >> 16 & 0xFF) + 151 * (i6 >> 8 & 0xFF) + 28 * (i6 & 0xFF);
        i14 = 77 * (i8 >> 16 & 0xFF) + 151 * (i8 >> 8 & 0xFF) + 28 * (i8 & 0xFF);
        if (i11 < i10)
        {
          i1 = i7;
          i10 = i11;
        }
        if (i12 < i10)
        {
          i1 = i9;
          i10 = i12;
        }
        if (i13 < i10)
        {
          i1 = i6;
          i10 = i13;
        }
        if (i14 < i10)
        {
          i1 = i8;
          i10 = i14;
        }
        arrayOfInt[(i++)] = i1;
      }
    }
    System.arraycopy(arrayOfInt, 0, pixels, 0, j);
  }
  
  public void copy(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    blend(this, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, 0);
  }
  
  public void copy(PImage paramPImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    blend(paramPImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, 0);
  }
  
  public static int blendColor(int paramInt1, int paramInt2, int paramInt3)
  {
    switch (paramInt3)
    {
    case 0: 
      return paramInt2;
    case 1: 
      return blend_blend(paramInt1, paramInt2);
    case 2: 
      return blend_add_pin(paramInt1, paramInt2);
    case 4: 
      return blend_sub_pin(paramInt1, paramInt2);
    case 8: 
      return blend_lightest(paramInt1, paramInt2);
    case 16: 
      return blend_darkest(paramInt1, paramInt2);
    case 32: 
      return blend_difference(paramInt1, paramInt2);
    case 64: 
      return blend_exclusion(paramInt1, paramInt2);
    case 128: 
      return blend_multiply(paramInt1, paramInt2);
    case 256: 
      return blend_screen(paramInt1, paramInt2);
    case 1024: 
      return blend_hard_light(paramInt1, paramInt2);
    case 2048: 
      return blend_soft_light(paramInt1, paramInt2);
    case 512: 
      return blend_overlay(paramInt1, paramInt2);
    case 4096: 
      return blend_dodge(paramInt1, paramInt2);
    case 8192: 
      return blend_burn(paramInt1, paramInt2);
    }
    return 0;
  }
  
  public void blend(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9)
  {
    blend(this, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9);
  }
  
  public void blend(PImage paramPImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9)
  {
    int i = paramInt1 + paramInt3;
    int j = paramInt2 + paramInt4;
    int k = paramInt5 + paramInt7;
    int m = paramInt6 + paramInt8;
    loadPixels();
    if (paramPImage == this)
    {
      if (intersect(paramInt1, paramInt2, i, j, paramInt5, paramInt6, k, m)) {
        blit_resize(get(paramInt1, paramInt2, i - paramInt1, j - paramInt2), 0, 0, i - paramInt1 - 1, j - paramInt2 - 1, pixels, width, height, paramInt5, paramInt6, k, m, paramInt9);
      } else {
        blit_resize(paramPImage, paramInt1, paramInt2, i, j, pixels, width, height, paramInt5, paramInt6, k, m, paramInt9);
      }
    }
    else
    {
      paramPImage.loadPixels();
      blit_resize(paramPImage, paramInt1, paramInt2, i, j, pixels, width, height, paramInt5, paramInt6, k, m, paramInt9);
    }
    updatePixels();
  }
  
  private boolean intersect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    int i = paramInt3 - paramInt1 + 1;
    int j = paramInt4 - paramInt2 + 1;
    int k = paramInt7 - paramInt5 + 1;
    int m = paramInt8 - paramInt6 + 1;
    int n;
    if (paramInt5 < paramInt1)
    {
      k += paramInt5 - paramInt1;
      if (k > i) {
        k = i;
      }
    }
    else
    {
      n = i + paramInt1 - paramInt5;
      if (k > n) {
        k = n;
      }
    }
    if (paramInt6 < paramInt2)
    {
      m += paramInt6 - paramInt2;
      if (m > j) {
        m = j;
      }
    }
    else
    {
      n = j + paramInt2 - paramInt6;
      if (m > n) {
        m = n;
      }
    }
    return (k > 0) && (m > 0);
  }
  
  private void blit_resize(PImage paramPImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11)
  {
    if (paramInt1 < 0) {
      paramInt1 = 0;
    }
    if (paramInt2 < 0) {
      paramInt2 = 0;
    }
    if (paramInt3 > width) {
      paramInt3 = width;
    }
    if (paramInt4 > height) {
      paramInt4 = height;
    }
    int i = paramInt3 - paramInt1;
    int j = paramInt4 - paramInt2;
    int k = paramInt9 - paramInt7;
    int m = paramInt10 - paramInt8;
    int n = 1;
    if (n == 0)
    {
      i++;
      j++;
    }
    if ((k <= 0) || (m <= 0) || (i <= 0) || (j <= 0) || (paramInt7 >= paramInt5) || (paramInt8 >= paramInt6) || (paramInt1 >= width) || (paramInt2 >= height)) {
      return;
    }
    int i1 = (int)(i / k * 32768.0F);
    int i2 = (int)(j / m * 32768.0F);
    srcXOffset = (paramInt7 < 0 ? -paramInt7 * i1 : paramInt1 * 32768);
    srcYOffset = (paramInt8 < 0 ? -paramInt8 * i2 : paramInt2 * 32768);
    if (paramInt7 < 0)
    {
      k += paramInt7;
      paramInt7 = 0;
    }
    if (paramInt8 < 0)
    {
      m += paramInt8;
      paramInt8 = 0;
    }
    k = low(k, paramInt5 - paramInt7);
    m = low(m, paramInt6 - paramInt8);
    int i3 = paramInt8 * paramInt5 + paramInt7;
    srcBuffer = pixels;
    int i4;
    int i5;
    if (n != 0)
    {
      iw = width;
      iw1 = (width - 1);
      ih1 = (height - 1);
      switch (paramInt11)
      {
      case 1: 
        for (i4 = 0; i4 < m; i4++)
        {
          filter_new_scanline();
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_blend(paramArrayOfInt[(i3 + i5)], filter_bilinear());
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 2: 
        for (i4 = 0; i4 < m; i4++)
        {
          filter_new_scanline();
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_add_pin(paramArrayOfInt[(i3 + i5)], filter_bilinear());
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 4: 
        for (i4 = 0; i4 < m; i4++)
        {
          filter_new_scanline();
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_sub_pin(paramArrayOfInt[(i3 + i5)], filter_bilinear());
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 8: 
        for (i4 = 0; i4 < m; i4++)
        {
          filter_new_scanline();
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_lightest(paramArrayOfInt[(i3 + i5)], filter_bilinear());
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 16: 
        for (i4 = 0; i4 < m; i4++)
        {
          filter_new_scanline();
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_darkest(paramArrayOfInt[(i3 + i5)], filter_bilinear());
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 0: 
        for (i4 = 0; i4 < m; i4++)
        {
          filter_new_scanline();
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = filter_bilinear();
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 32: 
        for (i4 = 0; i4 < m; i4++)
        {
          filter_new_scanline();
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_difference(paramArrayOfInt[(i3 + i5)], filter_bilinear());
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 64: 
        for (i4 = 0; i4 < m; i4++)
        {
          filter_new_scanline();
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_exclusion(paramArrayOfInt[(i3 + i5)], filter_bilinear());
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 128: 
        for (i4 = 0; i4 < m; i4++)
        {
          filter_new_scanline();
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_multiply(paramArrayOfInt[(i3 + i5)], filter_bilinear());
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 256: 
        for (i4 = 0; i4 < m; i4++)
        {
          filter_new_scanline();
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_screen(paramArrayOfInt[(i3 + i5)], filter_bilinear());
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 512: 
        for (i4 = 0; i4 < m; i4++)
        {
          filter_new_scanline();
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_overlay(paramArrayOfInt[(i3 + i5)], filter_bilinear());
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 1024: 
        for (i4 = 0; i4 < m; i4++)
        {
          filter_new_scanline();
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_hard_light(paramArrayOfInt[(i3 + i5)], filter_bilinear());
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 2048: 
        for (i4 = 0; i4 < m; i4++)
        {
          filter_new_scanline();
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_soft_light(paramArrayOfInt[(i3 + i5)], filter_bilinear());
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 4096: 
        for (i4 = 0; i4 < m; i4++)
        {
          filter_new_scanline();
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_dodge(paramArrayOfInt[(i3 + i5)], filter_bilinear());
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 8192: 
        for (i4 = 0; i4 < m; i4++)
        {
          filter_new_scanline();
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_burn(paramArrayOfInt[(i3 + i5)], filter_bilinear());
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
      }
    }
    else
    {
      switch (paramInt11)
      {
      case 1: 
        for (i4 = 0; i4 < m; i4++)
        {
          sX = srcXOffset;
          sY = ((srcYOffset >> 15) * width);
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_blend(paramArrayOfInt[(i3 + i5)], srcBuffer[(sY + (sX >> 15))]);
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 2: 
        for (i4 = 0; i4 < m; i4++)
        {
          sX = srcXOffset;
          sY = ((srcYOffset >> 15) * width);
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_add_pin(paramArrayOfInt[(i3 + i5)], srcBuffer[(sY + (sX >> 15))]);
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 4: 
        for (i4 = 0; i4 < m; i4++)
        {
          sX = srcXOffset;
          sY = ((srcYOffset >> 15) * width);
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_sub_pin(paramArrayOfInt[(i3 + i5)], srcBuffer[(sY + (sX >> 15))]);
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 8: 
        for (i4 = 0; i4 < m; i4++)
        {
          sX = srcXOffset;
          sY = ((srcYOffset >> 15) * width);
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_lightest(paramArrayOfInt[(i3 + i5)], srcBuffer[(sY + (sX >> 15))]);
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 16: 
        for (i4 = 0; i4 < m; i4++)
        {
          sX = srcXOffset;
          sY = ((srcYOffset >> 15) * width);
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_darkest(paramArrayOfInt[(i3 + i5)], srcBuffer[(sY + (sX >> 15))]);
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 0: 
        for (i4 = 0; i4 < m; i4++)
        {
          sX = srcXOffset;
          sY = ((srcYOffset >> 15) * width);
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = srcBuffer[(sY + (sX >> 15))];
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 32: 
        for (i4 = 0; i4 < m; i4++)
        {
          sX = srcXOffset;
          sY = ((srcYOffset >> 15) * width);
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_difference(paramArrayOfInt[(i3 + i5)], srcBuffer[(sY + (sX >> 15))]);
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 64: 
        for (i4 = 0; i4 < m; i4++)
        {
          sX = srcXOffset;
          sY = ((srcYOffset >> 15) * width);
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_exclusion(paramArrayOfInt[(i3 + i5)], srcBuffer[(sY + (sX >> 15))]);
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 128: 
        for (i4 = 0; i4 < m; i4++)
        {
          sX = srcXOffset;
          sY = ((srcYOffset >> 15) * width);
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_multiply(paramArrayOfInt[(i3 + i5)], srcBuffer[(sY + (sX >> 15))]);
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 256: 
        for (i4 = 0; i4 < m; i4++)
        {
          sX = srcXOffset;
          sY = ((srcYOffset >> 15) * width);
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_screen(paramArrayOfInt[(i3 + i5)], srcBuffer[(sY + (sX >> 15))]);
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 512: 
        for (i4 = 0; i4 < m; i4++)
        {
          sX = srcXOffset;
          sY = ((srcYOffset >> 15) * width);
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_overlay(paramArrayOfInt[(i3 + i5)], srcBuffer[(sY + (sX >> 15))]);
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 1024: 
        for (i4 = 0; i4 < m; i4++)
        {
          sX = srcXOffset;
          sY = ((srcYOffset >> 15) * width);
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_hard_light(paramArrayOfInt[(i3 + i5)], srcBuffer[(sY + (sX >> 15))]);
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 2048: 
        for (i4 = 0; i4 < m; i4++)
        {
          sX = srcXOffset;
          sY = ((srcYOffset >> 15) * width);
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_soft_light(paramArrayOfInt[(i3 + i5)], srcBuffer[(sY + (sX >> 15))]);
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 4096: 
        for (i4 = 0; i4 < m; i4++)
        {
          sX = srcXOffset;
          sY = ((srcYOffset >> 15) * width);
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_dodge(paramArrayOfInt[(i3 + i5)], srcBuffer[(sY + (sX >> 15))]);
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
        break;
      case 8192: 
        for (i4 = 0; i4 < m; i4++)
        {
          sX = srcXOffset;
          sY = ((srcYOffset >> 15) * width);
          for (i5 = 0; i5 < k; i5++)
          {
            paramArrayOfInt[(i3 + i5)] = blend_burn(paramArrayOfInt[(i3 + i5)], srcBuffer[(sY + (sX >> 15))]);
            sX += i1;
          }
          i3 += paramInt5;
          srcYOffset += i2;
        }
      }
    }
  }
  
  private void filter_new_scanline()
  {
    sX = srcXOffset;
    fracV = (srcYOffset & 0x7FFF);
    ifV = (32767 - fracV);
    v1 = ((srcYOffset >> 15) * iw);
    v2 = (low((srcYOffset >> 15) + 1, ih1) * iw);
  }
  
  private int filter_bilinear()
  {
    fracU = (sX & 0x7FFF);
    ifU = (32767 - fracU);
    ul = (ifU * ifV >> 15);
    ll = (ifU * fracV >> 15);
    ur = (fracU * ifV >> 15);
    lr = (fracU * fracV >> 15);
    u1 = (sX >> 15);
    u2 = low(u1 + 1, iw1);
    cUL = srcBuffer[(v1 + u1)];
    cUR = srcBuffer[(v1 + u2)];
    cLL = srcBuffer[(v2 + u1)];
    cLR = srcBuffer[(v2 + u2)];
    r = (ul * ((cUL & 0xFF0000) >> 16) + ll * ((cLL & 0xFF0000) >> 16) + ur * ((cUR & 0xFF0000) >> 16) + lr * ((cLR & 0xFF0000) >> 16) << 1 & 0xFF0000);
    g = (ul * (cUL & 0xFF00) + ll * (cLL & 0xFF00) + ur * (cUR & 0xFF00) + lr * (cLR & 0xFF00) >>> 15 & 0xFF00);
    b = (ul * (cUL & 0xFF) + ll * (cLL & 0xFF) + ur * (cUR & 0xFF) + lr * (cLR & 0xFF) >>> 15);
    a = (ul * ((cUL & 0xFF000000) >>> 24) + ll * ((cLL & 0xFF000000) >>> 24) + ur * ((cUR & 0xFF000000) >>> 24) + lr * ((cLR & 0xFF000000) >>> 24) << 9 & 0xFF000000);
    return a | r | g | b;
  }
  
  private static int low(int paramInt1, int paramInt2)
  {
    return paramInt1 < paramInt2 ? paramInt1 : paramInt2;
  }
  
  private static int high(int paramInt1, int paramInt2)
  {
    return paramInt1 > paramInt2 ? paramInt1 : paramInt2;
  }
  
  private static int peg(int paramInt)
  {
    return paramInt > 255 ? 255 : paramInt < 0 ? 0 : paramInt;
  }
  
  private static int mix(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 + ((paramInt2 - paramInt1) * paramInt3 >> 8);
  }
  
  private static int blend_blend(int paramInt1, int paramInt2)
  {
    int i = (paramInt2 & 0xFF000000) >>> 24;
    return low(((paramInt1 & 0xFF000000) >>> 24) + i, 255) << 24 | mix(paramInt1 & 0xFF0000, paramInt2 & 0xFF0000, i) & 0xFF0000 | mix(paramInt1 & 0xFF00, paramInt2 & 0xFF00, i) & 0xFF00 | mix(paramInt1 & 0xFF, paramInt2 & 0xFF, i);
  }
  
  private static int blend_add_pin(int paramInt1, int paramInt2)
  {
    int i = (paramInt2 & 0xFF000000) >>> 24;
    return low(((paramInt1 & 0xFF000000) >>> 24) + i, 255) << 24 | low((paramInt1 & 0xFF0000) + ((paramInt2 & 0xFF0000) >> 8) * i, 16711680) & 0xFF0000 | low((paramInt1 & 0xFF00) + ((paramInt2 & 0xFF00) >> 8) * i, 65280) & 0xFF00 | low((paramInt1 & 0xFF) + ((paramInt2 & 0xFF) * i >> 8), 255);
  }
  
  private static int blend_sub_pin(int paramInt1, int paramInt2)
  {
    int i = (paramInt2 & 0xFF000000) >>> 24;
    return low(((paramInt1 & 0xFF000000) >>> 24) + i, 255) << 24 | high((paramInt1 & 0xFF0000) - ((paramInt2 & 0xFF0000) >> 8) * i, 65280) & 0xFF0000 | high((paramInt1 & 0xFF00) - ((paramInt2 & 0xFF00) >> 8) * i, 255) & 0xFF00 | high((paramInt1 & 0xFF) - ((paramInt2 & 0xFF) * i >> 8), 0);
  }
  
  private static int blend_lightest(int paramInt1, int paramInt2)
  {
    int i = (paramInt2 & 0xFF000000) >>> 24;
    return low(((paramInt1 & 0xFF000000) >>> 24) + i, 255) << 24 | high(paramInt1 & 0xFF0000, ((paramInt2 & 0xFF0000) >> 8) * i) & 0xFF0000 | high(paramInt1 & 0xFF00, ((paramInt2 & 0xFF00) >> 8) * i) & 0xFF00 | high(paramInt1 & 0xFF, (paramInt2 & 0xFF) * i >> 8);
  }
  
  private static int blend_darkest(int paramInt1, int paramInt2)
  {
    int i = (paramInt2 & 0xFF000000) >>> 24;
    return low(((paramInt1 & 0xFF000000) >>> 24) + i, 255) << 24 | mix(paramInt1 & 0xFF0000, low(paramInt1 & 0xFF0000, ((paramInt2 & 0xFF0000) >> 8) * i), i) & 0xFF0000 | mix(paramInt1 & 0xFF00, low(paramInt1 & 0xFF00, ((paramInt2 & 0xFF00) >> 8) * i), i) & 0xFF00 | mix(paramInt1 & 0xFF, low(paramInt1 & 0xFF, (paramInt2 & 0xFF) * i >> 8), i);
  }
  
  private static int blend_difference(int paramInt1, int paramInt2)
  {
    int i = (paramInt2 & 0xFF000000) >>> 24;
    int j = (paramInt1 & 0xFF0000) >> 16;
    int k = (paramInt1 & 0xFF00) >> 8;
    int m = paramInt1 & 0xFF;
    int n = (paramInt2 & 0xFF0000) >> 16;
    int i1 = (paramInt2 & 0xFF00) >> 8;
    int i2 = paramInt2 & 0xFF;
    int i3 = j > n ? j - n : n - j;
    int i4 = k > i1 ? k - i1 : i1 - k;
    int i5 = m > i2 ? m - i2 : i2 - m;
    return low(((paramInt1 & 0xFF000000) >>> 24) + i, 255) << 24 | peg(j + ((i3 - j) * i >> 8)) << 16 | peg(k + ((i4 - k) * i >> 8)) << 8 | peg(m + ((i5 - m) * i >> 8));
  }
  
  private static int blend_exclusion(int paramInt1, int paramInt2)
  {
    int i = (paramInt2 & 0xFF000000) >>> 24;
    int j = (paramInt1 & 0xFF0000) >> 16;
    int k = (paramInt1 & 0xFF00) >> 8;
    int m = paramInt1 & 0xFF;
    int n = (paramInt2 & 0xFF0000) >> 16;
    int i1 = (paramInt2 & 0xFF00) >> 8;
    int i2 = paramInt2 & 0xFF;
    int i3 = j + n - (j * n >> 7);
    int i4 = k + i1 - (k * i1 >> 7);
    int i5 = m + i2 - (m * i2 >> 7);
    return low(((paramInt1 & 0xFF000000) >>> 24) + i, 255) << 24 | peg(j + ((i3 - j) * i >> 8)) << 16 | peg(k + ((i4 - k) * i >> 8)) << 8 | peg(m + ((i5 - m) * i >> 8));
  }
  
  private static int blend_multiply(int paramInt1, int paramInt2)
  {
    int i = (paramInt2 & 0xFF000000) >>> 24;
    int j = (paramInt1 & 0xFF0000) >> 16;
    int k = (paramInt1 & 0xFF00) >> 8;
    int m = paramInt1 & 0xFF;
    int n = (paramInt2 & 0xFF0000) >> 16;
    int i1 = (paramInt2 & 0xFF00) >> 8;
    int i2 = paramInt2 & 0xFF;
    int i3 = j * n >> 8;
    int i4 = k * i1 >> 8;
    int i5 = m * i2 >> 8;
    return low(((paramInt1 & 0xFF000000) >>> 24) + i, 255) << 24 | peg(j + ((i3 - j) * i >> 8)) << 16 | peg(k + ((i4 - k) * i >> 8)) << 8 | peg(m + ((i5 - m) * i >> 8));
  }
  
  private static int blend_screen(int paramInt1, int paramInt2)
  {
    int i = (paramInt2 & 0xFF000000) >>> 24;
    int j = (paramInt1 & 0xFF0000) >> 16;
    int k = (paramInt1 & 0xFF00) >> 8;
    int m = paramInt1 & 0xFF;
    int n = (paramInt2 & 0xFF0000) >> 16;
    int i1 = (paramInt2 & 0xFF00) >> 8;
    int i2 = paramInt2 & 0xFF;
    int i3 = 255 - ((255 - j) * (255 - n) >> 8);
    int i4 = 255 - ((255 - k) * (255 - i1) >> 8);
    int i5 = 255 - ((255 - m) * (255 - i2) >> 8);
    return low(((paramInt1 & 0xFF000000) >>> 24) + i, 255) << 24 | peg(j + ((i3 - j) * i >> 8)) << 16 | peg(k + ((i4 - k) * i >> 8)) << 8 | peg(m + ((i5 - m) * i >> 8));
  }
  
  private static int blend_overlay(int paramInt1, int paramInt2)
  {
    int i = (paramInt2 & 0xFF000000) >>> 24;
    int j = (paramInt1 & 0xFF0000) >> 16;
    int k = (paramInt1 & 0xFF00) >> 8;
    int m = paramInt1 & 0xFF;
    int n = (paramInt2 & 0xFF0000) >> 16;
    int i1 = (paramInt2 & 0xFF00) >> 8;
    int i2 = paramInt2 & 0xFF;
    int i3 = j < 128 ? j * n >> 7 : 255 - ((255 - j) * (255 - n) >> 7);
    int i4 = k < 128 ? k * i1 >> 7 : 255 - ((255 - k) * (255 - i1) >> 7);
    int i5 = m < 128 ? m * i2 >> 7 : 255 - ((255 - m) * (255 - i2) >> 7);
    return low(((paramInt1 & 0xFF000000) >>> 24) + i, 255) << 24 | peg(j + ((i3 - j) * i >> 8)) << 16 | peg(k + ((i4 - k) * i >> 8)) << 8 | peg(m + ((i5 - m) * i >> 8));
  }
  
  private static int blend_hard_light(int paramInt1, int paramInt2)
  {
    int i = (paramInt2 & 0xFF000000) >>> 24;
    int j = (paramInt1 & 0xFF0000) >> 16;
    int k = (paramInt1 & 0xFF00) >> 8;
    int m = paramInt1 & 0xFF;
    int n = (paramInt2 & 0xFF0000) >> 16;
    int i1 = (paramInt2 & 0xFF00) >> 8;
    int i2 = paramInt2 & 0xFF;
    int i3 = n < 128 ? j * n >> 7 : 255 - ((255 - j) * (255 - n) >> 7);
    int i4 = i1 < 128 ? k * i1 >> 7 : 255 - ((255 - k) * (255 - i1) >> 7);
    int i5 = i2 < 128 ? m * i2 >> 7 : 255 - ((255 - m) * (255 - i2) >> 7);
    return low(((paramInt1 & 0xFF000000) >>> 24) + i, 255) << 24 | peg(j + ((i3 - j) * i >> 8)) << 16 | peg(k + ((i4 - k) * i >> 8)) << 8 | peg(m + ((i5 - m) * i >> 8));
  }
  
  private static int blend_soft_light(int paramInt1, int paramInt2)
  {
    int i = (paramInt2 & 0xFF000000) >>> 24;
    int j = (paramInt1 & 0xFF0000) >> 16;
    int k = (paramInt1 & 0xFF00) >> 8;
    int m = paramInt1 & 0xFF;
    int n = (paramInt2 & 0xFF0000) >> 16;
    int i1 = (paramInt2 & 0xFF00) >> 8;
    int i2 = paramInt2 & 0xFF;
    int i3 = (j * n >> 7) + (j * j >> 8) - (j * j * n >> 15);
    int i4 = (k * i1 >> 7) + (k * k >> 8) - (k * k * i1 >> 15);
    int i5 = (m * i2 >> 7) + (m * m >> 8) - (m * m * i2 >> 15);
    return low(((paramInt1 & 0xFF000000) >>> 24) + i, 255) << 24 | peg(j + ((i3 - j) * i >> 8)) << 16 | peg(k + ((i4 - k) * i >> 8)) << 8 | peg(m + ((i5 - m) * i >> 8));
  }
  
  private static int blend_dodge(int paramInt1, int paramInt2)
  {
    int i = (paramInt2 & 0xFF000000) >>> 24;
    int j = (paramInt1 & 0xFF0000) >> 16;
    int k = (paramInt1 & 0xFF00) >> 8;
    int m = paramInt1 & 0xFF;
    int n = (paramInt2 & 0xFF0000) >> 16;
    int i1 = (paramInt2 & 0xFF00) >> 8;
    int i2 = paramInt2 & 0xFF;
    int i3 = n == 255 ? 255 : peg((j << 8) / (255 - n));
    int i4 = i1 == 255 ? 255 : peg((k << 8) / (255 - i1));
    int i5 = i2 == 255 ? 255 : peg((m << 8) / (255 - i2));
    return low(((paramInt1 & 0xFF000000) >>> 24) + i, 255) << 24 | peg(j + ((i3 - j) * i >> 8)) << 16 | peg(k + ((i4 - k) * i >> 8)) << 8 | peg(m + ((i5 - m) * i >> 8));
  }
  
  private static int blend_burn(int paramInt1, int paramInt2)
  {
    int i = (paramInt2 & 0xFF000000) >>> 24;
    int j = (paramInt1 & 0xFF0000) >> 16;
    int k = (paramInt1 & 0xFF00) >> 8;
    int m = paramInt1 & 0xFF;
    int n = (paramInt2 & 0xFF0000) >> 16;
    int i1 = (paramInt2 & 0xFF00) >> 8;
    int i2 = paramInt2 & 0xFF;
    int i3 = n == 0 ? 0 : 255 - peg((255 - j << 8) / n);
    int i4 = i1 == 0 ? 0 : 255 - peg((255 - k << 8) / i1);
    int i5 = i2 == 0 ? 0 : 255 - peg((255 - m << 8) / i2);
    return low(((paramInt1 & 0xFF000000) >>> 24) + i, 255) << 24 | peg(j + ((i3 - j) * i >> 8)) << 16 | peg(k + ((i4 - k) * i >> 8)) << 8 | peg(m + ((i5 - m) * i >> 8));
  }
  
  protected static PImage loadTIFF(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte[42] != paramArrayOfByte[102]) || (paramArrayOfByte[43] != paramArrayOfByte[103]))
    {
      System.err.println("Error: Processing can only read its own TIFF files.");
      return null;
    }
    int i = (paramArrayOfByte[30] & 0xFF) << 8 | paramArrayOfByte[31] & 0xFF;
    int j = (paramArrayOfByte[42] & 0xFF) << 8 | paramArrayOfByte[43] & 0xFF;
    int k = (paramArrayOfByte[114] & 0xFF) << 24 | (paramArrayOfByte[115] & 0xFF) << 16 | (paramArrayOfByte[116] & 0xFF) << 8 | paramArrayOfByte[117] & 0xFF;
    if (k != i * j * 3)
    {
      System.err.println("Error: Processing can only read its own TIFF files. (" + i + ", " + j + ")");
      return null;
    }
    for (int m = 0; m < TIFF_HEADER.length; m++) {
      if ((m != 30) && (m != 31) && (m != 42) && (m != 43) && (m != 102) && (m != 103) && (m != 114) && (m != 115) && (m != 116) && (m != 117) && (paramArrayOfByte[m] != TIFF_HEADER[m]))
      {
        System.err.println("Error: Processing can only read its own TIFF files. (" + m + ")");
        return null;
      }
    }
    PImage localPImage = new PImage(i, j, 1);
    int n = 768;
    k /= 3;
    for (int i1 = 0; i1 < k; i1++) {
      pixels[i1] = (0xFF000000 | (paramArrayOfByte[(n++)] & 0xFF) << 16 | (paramArrayOfByte[(n++)] & 0xFF) << 8 | paramArrayOfByte[(n++)] & 0xFF);
    }
    return localPImage;
  }
  
  protected boolean saveTIFF(OutputStream paramOutputStream)
  {
    try
    {
      byte[] arrayOfByte = new byte['̀'];
      System.arraycopy(TIFF_HEADER, 0, arrayOfByte, 0, TIFF_HEADER.length);
      arrayOfByte[30] = ((byte)(width >> 8 & 0xFF));
      arrayOfByte[31] = ((byte)(width & 0xFF));
      byte tmp66_65 = ((byte)(height >> 8 & 0xFF));
      arrayOfByte[102] = tmp66_65;
      arrayOfByte[42] = tmp66_65;
      byte tmp84_83 = ((byte)(height & 0xFF));
      arrayOfByte[103] = tmp84_83;
      arrayOfByte[43] = tmp84_83;
      int i = width * height * 3;
      arrayOfByte[114] = ((byte)(i >> 24 & 0xFF));
      arrayOfByte[115] = ((byte)(i >> 16 & 0xFF));
      arrayOfByte[116] = ((byte)(i >> 8 & 0xFF));
      arrayOfByte[117] = ((byte)(i & 0xFF));
      paramOutputStream.write(arrayOfByte);
      for (int j = 0; j < pixels.length; j++)
      {
        paramOutputStream.write(pixels[j] >> 16 & 0xFF);
        paramOutputStream.write(pixels[j] >> 8 & 0xFF);
        paramOutputStream.write(pixels[j] & 0xFF);
      }
      paramOutputStream.flush();
      return true;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return false;
  }
  
  protected boolean saveTGA(OutputStream paramOutputStream)
  {
    byte[] arrayOfByte = new byte[18];
    if (format == 4)
    {
      arrayOfByte[2] = 11;
      arrayOfByte[16] = 8;
      arrayOfByte[17] = 40;
    }
    else if (format == 1)
    {
      arrayOfByte[2] = 10;
      arrayOfByte[16] = 24;
      arrayOfByte[17] = 32;
    }
    else if (format == 2)
    {
      arrayOfByte[2] = 10;
      arrayOfByte[16] = 32;
      arrayOfByte[17] = 40;
    }
    else
    {
      throw new RuntimeException("Image format not recognized inside save()");
    }
    arrayOfByte[12] = ((byte)(width & 0xFF));
    arrayOfByte[13] = ((byte)(width >> 8));
    arrayOfByte[14] = ((byte)(height & 0xFF));
    arrayOfByte[15] = ((byte)(height >> 8));
    try
    {
      paramOutputStream.write(arrayOfByte);
      int i = height * width;
      int j = 0;
      int[] arrayOfInt = new int[''];
      int m;
      int n;
      int k;
      int i1;
      if (format == 4) {
        while (j < i)
        {
          m = 0;
          n = 1;
          int tmp208_207 = (pixels[j] & 0xFF);
          k = tmp208_207;
          arrayOfInt[0] = tmp208_207;
          while (j + n < i)
          {
            if ((k != (pixels[(j + n)] & 0xFF)) || (n == 128))
            {
              m = n > 1 ? 1 : 0;
              break;
            }
            n++;
          }
          if (m != 0)
          {
            paramOutputStream.write(0x80 | n - 1);
            paramOutputStream.write(k);
          }
          else
          {
            for (n = 1; j + n < i; n++)
            {
              i1 = pixels[(j + n)] & 0xFF;
              if (((k != i1) && (n < 128)) || (n < 3))
              {
                int tmp351_349 = i1;
                k = tmp351_349;
                arrayOfInt[n] = tmp351_349;
              }
              else
              {
                if (k != i1) {
                  break;
                }
                n -= 2;
                break;
              }
            }
            paramOutputStream.write(n - 1);
            for (i1 = 0; i1 < n; i1++) {
              paramOutputStream.write(arrayOfInt[i1]);
            }
          }
          j += n;
        }
      }
      while (j < i)
      {
        m = 0;
        int tmp439_438 = pixels[j];
        k = tmp439_438;
        arrayOfInt[0] = tmp439_438;
        for (n = 1; j + n < i; n++) {
          if ((k != pixels[(j + n)]) || (n == 128))
          {
            m = n > 1 ? 1 : 0;
            break;
          }
        }
        if (m != 0)
        {
          paramOutputStream.write(0x80 | n - 1);
          paramOutputStream.write(k & 0xFF);
          paramOutputStream.write(k >> 8 & 0xFF);
          paramOutputStream.write(k >> 16 & 0xFF);
          if (format == 2) {
            paramOutputStream.write(k >>> 24 & 0xFF);
          }
        }
        else
        {
          for (n = 1; j + n < i; n++) {
            if (((k != pixels[(j + n)]) && (n < 128)) || (n < 3))
            {
              int tmp632_631 = pixels[(j + n)];
              k = tmp632_631;
              arrayOfInt[n] = tmp632_631;
            }
            else
            {
              if (k != pixels[(j + n)]) {
                break;
              }
              n -= 2;
              break;
            }
          }
          paramOutputStream.write(n - 1);
          if (format == 2) {
            for (i1 = 0; i1 < n; i1++)
            {
              k = arrayOfInt[i1];
              paramOutputStream.write(k & 0xFF);
              paramOutputStream.write(k >> 8 & 0xFF);
              paramOutputStream.write(k >> 16 & 0xFF);
              paramOutputStream.write(k >>> 24 & 0xFF);
            }
          } else {
            for (i1 = 0; i1 < n; i1++)
            {
              k = arrayOfInt[i1];
              paramOutputStream.write(k & 0xFF);
              paramOutputStream.write(k >> 8 & 0xFF);
              paramOutputStream.write(k >> 16 & 0xFF);
            }
          }
        }
        j += n;
      }
      paramOutputStream.flush();
      return true;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return false;
  }
  
  protected boolean saveImageIO(String paramString)
    throws IOException
  {
    try
    {
      int i = format == 2 ? 2 : 1;
      String str1 = paramString.toLowerCase();
      if ((str1.endsWith("bmp")) || (str1.endsWith("jpg")) || (str1.endsWith("jpeg"))) {
        i = 1;
      }
      BufferedImage localBufferedImage = new BufferedImage(width, height, i);
      localBufferedImage.setRGB(0, 0, width, height, pixels, 0, width);
      File localFile = new File(paramString);
      String str2 = paramString.substring(paramString.lastIndexOf('.') + 1);
      return ImageIO.write(localBufferedImage, str2, localFile);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      throw new IOException("image save failed.");
    }
  }
  
  public void save(String paramString)
  {
    boolean bool = false;
    Object localObject;
    if (parent != null)
    {
      paramString = parent.savePath(paramString);
    }
    else
    {
      localObject = "PImage.save() requires an absolute path. Use createImage(), or pass savePath() to save().";
      PGraphics.showException((String)localObject);
    }
    loadPixels();
    try
    {
      localObject = null;
      if (saveImageFormats == null) {
        saveImageFormats = ImageIO.getWriterFormatNames();
      }
      if (saveImageFormats != null) {
        for (int i = 0; i < saveImageFormats.length; i++) {
          if (paramString.endsWith("." + saveImageFormats[i]))
          {
            if (!saveImageIO(paramString)) {
              throw new RuntimeException("Error while saving image.");
            }
            return;
          }
        }
      }
      if (paramString.toLowerCase().endsWith(".tga"))
      {
        localObject = new BufferedOutputStream(new FileOutputStream(paramString), 32768);
        bool = saveTGA((OutputStream)localObject);
      }
      else
      {
        if ((!paramString.toLowerCase().endsWith(".tif")) && (!paramString.toLowerCase().endsWith(".tiff"))) {
          paramString = paramString + ".tif";
        }
        localObject = new BufferedOutputStream(new FileOutputStream(paramString), 32768);
        bool = saveTIFF((OutputStream)localObject);
      }
      ((OutputStream)localObject).flush();
      ((OutputStream)localObject).close();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      bool = false;
    }
    if (!bool) {
      throw new RuntimeException("Error while saving image.");
    }
  }
}
