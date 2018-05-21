package processing.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D.Float;
import java.awt.geom.Ellipse2D.Float;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D.Float;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

public class PGraphicsJava2D
  extends PGraphics
{
  public Graphics2D g2;
  protected BufferedImage offscreen;
  GeneralPath gpath;
  boolean breakShape;
  float[] curveCoordX;
  float[] curveCoordY;
  float[] curveDrawX;
  float[] curveDrawY;
  int transformCount;
  AffineTransform[] transformStack = new AffineTransform[32];
  double[] transform = new double[6];
  Line2D.Float line = new Line2D.Float();
  Ellipse2D.Float ellipse = new Ellipse2D.Float();
  Rectangle2D.Float rect = new Rectangle2D.Float();
  Arc2D.Float arc = new Arc2D.Float();
  protected Color tintColorObject;
  protected Color fillColorObject;
  public boolean fillGradient;
  public Paint fillGradientObject;
  protected Color strokeColorObject;
  public boolean strokeGradient;
  public Paint strokeGradientObject;
  int[] clearPixels;
  static int[] getset = new int[1];
  
  public PGraphicsJava2D() {}
  
  public void setSize(int paramInt1, int paramInt2)
  {
    width = paramInt1;
    height = paramInt2;
    width1 = (width - 1);
    height1 = (height - 1);
    allocate();
    reapplySettings();
  }
  
  protected void allocate()
  {
    image = new BufferedImage(width, height, 2);
    if (primarySurface)
    {
      offscreen = new BufferedImage(width, height, 2);
      g2 = ((Graphics2D)offscreen.getGraphics());
    }
    else
    {
      g2 = ((Graphics2D)image.getGraphics());
    }
  }
  
  public boolean canDraw()
  {
    return true;
  }
  
  public void beginDraw()
  {
    checkSettings();
    resetMatrix();
    vertexCount = 0;
  }
  
  public void endDraw()
  {
    if (primarySurface) {
      synchronized (image)
      {
        image.getGraphics().drawImage(offscreen, 0, 0, null);
      }
    } else {
      loadPixels();
    }
    modified = true;
  }
  
  public void beginShape(int paramInt)
  {
    shape = paramInt;
    vertexCount = 0;
    curveVertexCount = 0;
    gpath = null;
  }
  
  public void texture(PImage paramPImage)
  {
    showMethodWarning("texture");
  }
  
  public void vertex(float paramFloat1, float paramFloat2)
  {
    curveVertexCount = 0;
    if (vertexCount == vertices.length)
    {
      float[][] arrayOfFloat = new float[vertexCount << 1][37];
      System.arraycopy(vertices, 0, arrayOfFloat, 0, vertexCount);
      vertices = arrayOfFloat;
    }
    vertices[vertexCount][0] = paramFloat1;
    vertices[vertexCount][1] = paramFloat2;
    vertexCount += 1;
    switch (shape)
    {
    case 2: 
      point(paramFloat1, paramFloat2);
      break;
    case 4: 
      if (vertexCount % 2 == 0) {
        line(vertices[(vertexCount - 2)][0], vertices[(vertexCount - 2)][1], paramFloat1, paramFloat2);
      }
      break;
    case 9: 
      if (vertexCount % 3 == 0) {
        triangle(vertices[(vertexCount - 3)][0], vertices[(vertexCount - 3)][1], vertices[(vertexCount - 2)][0], vertices[(vertexCount - 2)][1], paramFloat1, paramFloat2);
      }
      break;
    case 10: 
      if (vertexCount >= 3) {
        triangle(vertices[(vertexCount - 2)][0], vertices[(vertexCount - 2)][1], vertices[(vertexCount - 1)][0], vertices[(vertexCount - 1)][1], vertices[(vertexCount - 3)][0], vertices[(vertexCount - 3)][1]);
      }
      break;
    case 11: 
      if (vertexCount == 3)
      {
        triangle(vertices[0][0], vertices[0][1], vertices[1][0], vertices[1][1], paramFloat1, paramFloat2);
      }
      else if (vertexCount > 3)
      {
        gpath = new GeneralPath();
        gpath.moveTo(vertices[0][0], vertices[0][1]);
        gpath.lineTo(vertices[(vertexCount - 2)][0], vertices[(vertexCount - 2)][1]);
        gpath.lineTo(paramFloat1, paramFloat2);
        drawShape(gpath);
      }
      break;
    case 16: 
      if (vertexCount % 4 == 0) {
        quad(vertices[(vertexCount - 4)][0], vertices[(vertexCount - 4)][1], vertices[(vertexCount - 3)][0], vertices[(vertexCount - 3)][1], vertices[(vertexCount - 2)][0], vertices[(vertexCount - 2)][1], paramFloat1, paramFloat2);
      }
      break;
    case 17: 
      if ((vertexCount >= 4) && (vertexCount % 2 == 0)) {
        quad(vertices[(vertexCount - 4)][0], vertices[(vertexCount - 4)][1], vertices[(vertexCount - 2)][0], vertices[(vertexCount - 2)][1], paramFloat1, paramFloat2, vertices[(vertexCount - 3)][0], vertices[(vertexCount - 3)][1]);
      }
      break;
    case 20: 
      if (gpath == null)
      {
        gpath = new GeneralPath();
        gpath.moveTo(paramFloat1, paramFloat2);
      }
      else if (breakShape)
      {
        gpath.moveTo(paramFloat1, paramFloat2);
        breakShape = false;
      }
      else
      {
        gpath.lineTo(paramFloat1, paramFloat2);
      }
      break;
    }
  }
  
  public void vertex(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showDepthWarningXYZ("vertex");
  }
  
  public void vertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    showVariationWarning("vertex(x, y, u, v)");
  }
  
  public void vertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    showDepthWarningXYZ("vertex");
  }
  
  public void breakShape()
  {
    breakShape = true;
  }
  
  public void endShape(int paramInt)
  {
    if ((gpath != null) && (shape == 20))
    {
      if (paramInt == 2) {
        gpath.closePath();
      }
      drawShape(gpath);
    }
    shape = 0;
  }
  
  public void bezierVertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    bezierVertexCheck();
    gpath.curveTo(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }
  
  public void bezierVertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9)
  {
    showDepthWarningXYZ("bezierVertex");
  }
  
  public void quadVertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    bezierVertexCheck();
    Point2D localPoint2D = gpath.getCurrentPoint();
    float f1 = (float)localPoint2D.getX();
    float f2 = (float)localPoint2D.getY();
    bezierVertex(f1 + (paramFloat1 - f1) * 2.0F / 3.0F, f2 + (paramFloat2 - f2) * 2.0F / 3.0F, paramFloat3 + (paramFloat1 - paramFloat3) * 2.0F / 3.0F, paramFloat4 + (paramFloat2 - paramFloat4) * 2.0F / 3.0F, paramFloat3, paramFloat4);
  }
  
  public void quadVertex(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    showDepthWarningXYZ("quadVertex");
  }
  
  protected void curveVertexCheck()
  {
    super.curveVertexCheck();
    if (curveCoordX == null)
    {
      curveCoordX = new float[4];
      curveCoordY = new float[4];
      curveDrawX = new float[4];
      curveDrawY = new float[4];
    }
  }
  
  protected void curveVertexSegment(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
  {
    curveCoordX[0] = paramFloat1;
    curveCoordY[0] = paramFloat2;
    curveCoordX[1] = paramFloat3;
    curveCoordY[1] = paramFloat4;
    curveCoordX[2] = paramFloat5;
    curveCoordY[2] = paramFloat6;
    curveCoordX[3] = paramFloat7;
    curveCoordY[3] = paramFloat8;
    curveToBezierMatrix.mult(curveCoordX, curveDrawX);
    curveToBezierMatrix.mult(curveCoordY, curveDrawY);
    if (gpath == null)
    {
      gpath = new GeneralPath();
      gpath.moveTo(curveDrawX[0], curveDrawY[0]);
    }
    gpath.curveTo(curveDrawX[1], curveDrawY[1], curveDrawX[2], curveDrawY[2], curveDrawX[3], curveDrawY[3]);
  }
  
  public void curveVertex(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showDepthWarningXYZ("curveVertex");
  }
  
  public void point(float paramFloat1, float paramFloat2)
  {
    if (stroke) {
      line(paramFloat1, paramFloat2, paramFloat1 + 1.0E-4F, paramFloat2 + 1.0E-4F);
    }
  }
  
  public void line(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    line.setLine(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    strokeShape(line);
  }
  
  public void triangle(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    gpath = new GeneralPath();
    gpath.moveTo(paramFloat1, paramFloat2);
    gpath.lineTo(paramFloat3, paramFloat4);
    gpath.lineTo(paramFloat5, paramFloat6);
    gpath.closePath();
    drawShape(gpath);
  }
  
  public void quad(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
  {
    GeneralPath localGeneralPath = new GeneralPath();
    localGeneralPath.moveTo(paramFloat1, paramFloat2);
    localGeneralPath.lineTo(paramFloat3, paramFloat4);
    localGeneralPath.lineTo(paramFloat5, paramFloat6);
    localGeneralPath.lineTo(paramFloat7, paramFloat8);
    localGeneralPath.closePath();
    drawShape(localGeneralPath);
  }
  
  protected void rectImpl(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    rect.setFrame(paramFloat1, paramFloat2, paramFloat3 - paramFloat1, paramFloat4 - paramFloat2);
    drawShape(rect);
  }
  
  protected void ellipseImpl(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    ellipse.setFrame(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    drawShape(ellipse);
  }
  
  protected void arcImpl(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    paramFloat5 = -paramFloat5 * 57.295776F;
    paramFloat6 = -paramFloat6 * 57.295776F;
    float f = paramFloat6 - paramFloat5;
    if (fill)
    {
      arc.setArc(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, f, 2);
      fillShape(arc);
    }
    if (stroke)
    {
      arc.setArc(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, f, 0);
      strokeShape(arc);
    }
  }
  
  protected void fillShape(Shape paramShape)
  {
    if (fillGradient)
    {
      g2.setPaint(fillGradientObject);
      g2.fill(paramShape);
    }
    else if (fill)
    {
      g2.setColor(fillColorObject);
      g2.fill(paramShape);
    }
  }
  
  protected void strokeShape(Shape paramShape)
  {
    if (strokeGradient)
    {
      g2.setPaint(strokeGradientObject);
      g2.draw(paramShape);
    }
    else if (stroke)
    {
      g2.setColor(strokeColorObject);
      g2.draw(paramShape);
    }
  }
  
  protected void drawShape(Shape paramShape)
  {
    if (fillGradient)
    {
      g2.setPaint(fillGradientObject);
      g2.fill(paramShape);
    }
    else if (fill)
    {
      g2.setColor(fillColorObject);
      g2.fill(paramShape);
    }
    if (strokeGradient)
    {
      g2.setPaint(strokeGradientObject);
      g2.draw(paramShape);
    }
    else if (stroke)
    {
      g2.setColor(strokeColorObject);
      g2.draw(paramShape);
    }
  }
  
  public void box(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showMethodWarning("box");
  }
  
  public void sphere(float paramFloat)
  {
    showMethodWarning("sphere");
  }
  
  public void bezierDetail(int paramInt) {}
  
  public void curveDetail(int paramInt) {}
  
  public void smooth()
  {
    smooth = true;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
  }
  
  public void noSmooth()
  {
    smooth = false;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
  }
  
  protected void imageImpl(PImage paramPImage, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((width <= 0) || (height <= 0)) {
      return;
    }
    if (paramPImage.getCache(this) == null)
    {
      paramPImage.setCache(this, new ImageCache(paramPImage));
      paramPImage.updatePixels();
      modified = true;
    }
    ImageCache localImageCache = (ImageCache)paramPImage.getCache(this);
    if (((tint) && (!tinted)) || ((tint) && (tintedColor != tintColor)) || ((!tint) && (tinted))) {
      paramPImage.updatePixels();
    }
    if (modified)
    {
      localImageCache.update(tint, tintColor);
      modified = false;
    }
    g2.drawImage(getCacheimage, (int)paramFloat1, (int)paramFloat2, (int)paramFloat3, (int)paramFloat4, paramInt1, paramInt2, paramInt3, paramInt4, null);
  }
  
  public float textAscent()
  {
    if (textFont == null) {
      defaultFontOrDeath("textAscent");
    }
    Font localFont = textFont.getFont();
    if (localFont != null)
    {
      FontMetrics localFontMetrics = parent.getFontMetrics(localFont);
      return localFontMetrics.getAscent();
    }
    return super.textAscent();
  }
  
  public float textDescent()
  {
    if (textFont == null) {
      defaultFontOrDeath("textAscent");
    }
    Font localFont = textFont.getFont();
    if (localFont != null)
    {
      FontMetrics localFontMetrics = parent.getFontMetrics(localFont);
      return localFontMetrics.getDescent();
    }
    return super.textDescent();
  }
  
  protected boolean textModeCheck(int paramInt)
  {
    return (paramInt == 4) || (paramInt == 256);
  }
  
  public void textSize(float paramFloat)
  {
    if (textFont == null) {
      defaultFontOrDeath("textAscent", paramFloat);
    }
    Font localFont1 = textFont.getFont();
    if (localFont1 != null)
    {
      Font localFont2 = localFont1.deriveFont(paramFloat);
      g2.setFont(localFont2);
      textFont.setFont(localFont2);
    }
    super.textSize(paramFloat);
  }
  
  protected float textWidthImpl(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    Font localFont = textFont.getFont();
    if (localFont != null)
    {
      int i = paramInt2 - paramInt1;
      FontMetrics localFontMetrics = g2.getFontMetrics(localFont);
      return localFontMetrics.charsWidth(paramArrayOfChar, paramInt1, i);
    }
    return super.textWidthImpl(paramArrayOfChar, paramInt1, paramInt2);
  }
  
  protected void textLineImpl(char[] paramArrayOfChar, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
  {
    Font localFont = textFont.getFont();
    if (localFont != null)
    {
      Object localObject = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
      if (localObject == null) {
        localObject = RenderingHints.VALUE_ANTIALIAS_DEFAULT;
      }
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, textFont.smooth ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
      g2.setColor(fillColorObject);
      int i = paramInt2 - paramInt1;
      g2.drawChars(paramArrayOfChar, paramInt1, i, (int)(paramFloat1 + 0.5F), (int)(paramFloat2 + 0.5F));
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, localObject);
      textX = (paramFloat1 + textWidthImpl(paramArrayOfChar, paramInt1, paramInt2));
      textY = paramFloat2;
      textZ = 0.0F;
    }
    else
    {
      super.textLineImpl(paramArrayOfChar, paramInt1, paramInt2, paramFloat1, paramFloat2);
    }
  }
  
  public void pushMatrix()
  {
    if (transformCount == transformStack.length) {
      throw new RuntimeException("pushMatrix() cannot use push more than " + transformStack.length + " times");
    }
    transformStack[transformCount] = g2.getTransform();
    transformCount += 1;
  }
  
  public void popMatrix()
  {
    if (transformCount == 0) {
      throw new RuntimeException("missing a popMatrix() to go with that pushMatrix()");
    }
    transformCount -= 1;
    g2.setTransform(transformStack[transformCount]);
  }
  
  public void translate(float paramFloat1, float paramFloat2)
  {
    g2.translate(paramFloat1, paramFloat2);
  }
  
  public void rotate(float paramFloat)
  {
    g2.rotate(paramFloat);
  }
  
  public void rotateX(float paramFloat)
  {
    showDepthWarning("rotateX");
  }
  
  public void rotateY(float paramFloat)
  {
    showDepthWarning("rotateY");
  }
  
  public void rotateZ(float paramFloat)
  {
    showDepthWarning("rotateZ");
  }
  
  public void rotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    showVariationWarning("rotate");
  }
  
  public void scale(float paramFloat)
  {
    g2.scale(paramFloat, paramFloat);
  }
  
  public void scale(float paramFloat1, float paramFloat2)
  {
    g2.scale(paramFloat1, paramFloat2);
  }
  
  public void scale(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showDepthWarningXYZ("scale");
  }
  
  public void skewX(float paramFloat)
  {
    g2.shear(Math.tan(paramFloat), 0.0D);
  }
  
  public void skewY(float paramFloat)
  {
    g2.shear(0.0D, Math.tan(paramFloat));
  }
  
  public void resetMatrix()
  {
    g2.setTransform(new AffineTransform());
  }
  
  public void applyMatrix(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    g2.transform(new AffineTransform(paramFloat1, paramFloat4, paramFloat2, paramFloat5, paramFloat3, paramFloat6));
  }
  
  public void applyMatrix(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16)
  {
    showVariationWarning("applyMatrix");
  }
  
  public PMatrix getMatrix()
  {
    return getMatrix((PMatrix2D)null);
  }
  
  public PMatrix2D getMatrix(PMatrix2D paramPMatrix2D)
  {
    if (paramPMatrix2D == null) {
      paramPMatrix2D = new PMatrix2D();
    }
    g2.getTransform().getMatrix(transform);
    paramPMatrix2D.set((float)transform[0], (float)transform[2], (float)transform[4], (float)transform[1], (float)transform[3], (float)transform[5]);
    return paramPMatrix2D;
  }
  
  public PMatrix3D getMatrix(PMatrix3D paramPMatrix3D)
  {
    showVariationWarning("getMatrix");
    return paramPMatrix3D;
  }
  
  public void setMatrix(PMatrix2D paramPMatrix2D)
  {
    g2.setTransform(new AffineTransform(m00, m10, m01, m11, m02, m12));
  }
  
  public void setMatrix(PMatrix3D paramPMatrix3D)
  {
    showVariationWarning("setMatrix");
  }
  
  public void printMatrix()
  {
    getMatrix((PMatrix2D)null).print();
  }
  
  public float screenX(float paramFloat1, float paramFloat2)
  {
    g2.getTransform().getMatrix(transform);
    return (float)transform[0] * paramFloat1 + (float)transform[2] * paramFloat2 + (float)transform[4];
  }
  
  public float screenY(float paramFloat1, float paramFloat2)
  {
    g2.getTransform().getMatrix(transform);
    return (float)transform[1] * paramFloat1 + (float)transform[3] * paramFloat2 + (float)transform[5];
  }
  
  public float screenX(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showDepthWarningXYZ("screenX");
    return 0.0F;
  }
  
  public float screenY(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showDepthWarningXYZ("screenY");
    return 0.0F;
  }
  
  public float screenZ(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    showDepthWarningXYZ("screenZ");
    return 0.0F;
  }
  
  public void strokeCap(int paramInt)
  {
    super.strokeCap(paramInt);
    strokeImpl();
  }
  
  public void strokeJoin(int paramInt)
  {
    super.strokeJoin(paramInt);
    strokeImpl();
  }
  
  public void strokeWeight(float paramFloat)
  {
    super.strokeWeight(paramFloat);
    strokeImpl();
  }
  
  protected void strokeImpl()
  {
    int i = 0;
    if (strokeCap == 2) {
      i = 1;
    } else if (strokeCap == 4) {
      i = 2;
    }
    int j = 2;
    if (strokeJoin == 8) {
      j = 0;
    } else if (strokeJoin == 2) {
      j = 1;
    }
    g2.setStroke(new BasicStroke(strokeWeight, i, j));
  }
  
  protected void strokeFromCalc()
  {
    super.strokeFromCalc();
    strokeColorObject = new Color(strokeColor, true);
    strokeGradient = false;
  }
  
  protected void tintFromCalc()
  {
    super.tintFromCalc();
    tintColorObject = new Color(tintColor, true);
  }
  
  protected void fillFromCalc()
  {
    super.fillFromCalc();
    fillColorObject = new Color(fillColor, true);
    fillGradient = false;
  }
  
  public void backgroundImpl()
  {
    if (backgroundAlpha)
    {
      WritableRaster localWritableRaster = ((BufferedImage)image).getRaster();
      if ((clearPixels == null) || (clearPixels.length < width)) {
        clearPixels = new int[width];
      }
      Arrays.fill(clearPixels, backgroundColor);
      for (int i = 0; i < height; i++) {
        localWritableRaster.setDataElements(0, i, width, 1, clearPixels);
      }
    }
    else
    {
      pushMatrix();
      resetMatrix();
      g2.setColor(new Color(backgroundColor));
      g2.fillRect(0, 0, width, height);
      popMatrix();
    }
  }
  
  public void beginRaw(PGraphics paramPGraphics)
  {
    showMethodWarning("beginRaw");
  }
  
  public void endRaw()
  {
    showMethodWarning("endRaw");
  }
  
  public void loadPixels()
  {
    if ((pixels == null) || (pixels.length != width * height)) {
      pixels = new int[width * height];
    }
    WritableRaster localWritableRaster = ((BufferedImage)(primarySurface ? offscreen : image)).getRaster();
    localWritableRaster.getDataElements(0, 0, width, height, pixels);
  }
  
  public void updatePixels()
  {
    WritableRaster localWritableRaster = ((BufferedImage)(primarySurface ? offscreen : image)).getRaster();
    localWritableRaster.setDataElements(0, 0, width, height, pixels);
  }
  
  public void updatePixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramInt1 != 0) || (paramInt2 != 0) || (paramInt3 != width) || (paramInt4 != height)) {
      showVariationWarning("updatePixels(x, y, w, h)");
    }
    updatePixels();
  }
  
  public int get(int paramInt1, int paramInt2)
  {
    if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= width) || (paramInt2 >= height)) {
      return 0;
    }
    WritableRaster localWritableRaster = ((BufferedImage)(primarySurface ? offscreen : image)).getRaster();
    localWritableRaster.getDataElements(paramInt1, paramInt2, getset);
    return getset[0];
  }
  
  public PImage getImpl(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    PImage localPImage = new PImage(paramInt3, paramInt4);
    parent = parent;
    WritableRaster localWritableRaster = ((BufferedImage)(primarySurface ? offscreen : image)).getRaster();
    localWritableRaster.getDataElements(paramInt1, paramInt2, paramInt3, paramInt4, pixels);
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
    getset[0] = paramInt3;
    WritableRaster localWritableRaster = ((BufferedImage)(primarySurface ? offscreen : image)).getRaster();
    localWritableRaster.setDataElements(paramInt1, paramInt2, getset);
  }
  
  protected void setImpl(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, PImage paramPImage)
  {
    WritableRaster localWritableRaster = ((BufferedImage)(primarySurface ? offscreen : image)).getRaster();
    if ((paramInt3 == 0) && (paramInt4 == 0) && (paramInt5 == width) && (paramInt6 == height))
    {
      localWritableRaster.setDataElements(paramInt1, paramInt2, width, height, pixels);
    }
    else
    {
      PImage localPImage = paramPImage.get(paramInt3, paramInt4, paramInt5, paramInt6);
      localWritableRaster.setDataElements(paramInt1, paramInt2, width, height, pixels);
    }
  }
  
  public void mask(int[] paramArrayOfInt)
  {
    showMethodWarning("mask");
  }
  
  public void mask(PImage paramPImage)
  {
    showMethodWarning("mask");
  }
  
  public void copy(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    if ((paramInt3 != paramInt7) || (paramInt4 != paramInt8))
    {
      copy(this, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8);
    }
    else
    {
      paramInt5 -= paramInt1;
      paramInt6 -= paramInt2;
      g2.copyArea(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
    }
  }
  
  class ImageCache
  {
    PImage source;
    boolean tinted;
    int tintedColor;
    int[] tintedPixels;
    BufferedImage image;
    
    public ImageCache(PImage paramPImage)
    {
      source = paramPImage;
    }
    
    public void delete() {}
    
    public void update(boolean paramBoolean, int paramInt)
    {
      int i = 2;
      int j = (paramInt & 0xFF000000) == -16777216 ? 1 : 0;
      if ((source.format == 1) && ((!paramBoolean) || ((paramBoolean) && (j != 0)))) {
        i = 1;
      }
      int k = (image != null) && (image.getType() != i) ? 1 : 0;
      if ((image == null) || (k != 0)) {
        image = new BufferedImage(source.width, source.height, i);
      }
      WritableRaster localWritableRaster = image.getRaster();
      if (paramBoolean)
      {
        if ((tintedPixels == null) || (tintedPixels.length != source.width)) {
          tintedPixels = new int[source.width];
        }
        int m = paramInt >> 24 & 0xFF;
        int n = paramInt >> 16 & 0xFF;
        int i1 = paramInt >> 8 & 0xFF;
        int i2 = paramInt & 0xFF;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        if (i == 1)
        {
          i3 = 0;
          for (i4 = 0; i4 < source.height; i4++)
          {
            for (i5 = 0; i5 < source.width; i5++)
            {
              i6 = source.pixels[(i3++)];
              i7 = i6 >> 16 & 0xFF;
              i8 = i6 >> 8 & 0xFF;
              i9 = i6 & 0xFF;
              tintedPixels[i5] = ((n * i7 & 0xFF00) << 8 | i1 * i8 & 0xFF00 | (i2 * i9 & 0xFF00) >> 8);
            }
            localWritableRaster.setDataElements(0, i4, source.width, 1, tintedPixels);
          }
        }
        else if (i == 2)
        {
          i3 = 0;
          for (i4 = 0; i4 < source.height; i4++)
          {
            int i10;
            if (source.format == 1)
            {
              i5 = paramInt & 0xFF000000;
              for (i6 = 0; i6 < source.width; i6++)
              {
                i7 = source.pixels[(i3++)];
                i8 = i7 >> 16 & 0xFF;
                i9 = i7 >> 8 & 0xFF;
                i10 = i7 & 0xFF;
                tintedPixels[i6] = (i5 | (n * i8 & 0xFF00) << 8 | i1 * i9 & 0xFF00 | (i2 * i10 & 0xFF00) >> 8);
              }
            }
            else if (source.format == 2)
            {
              for (i5 = 0; i5 < source.width; i5++)
              {
                i6 = source.pixels[(i3++)];
                i7 = i6 >> 24 & 0xFF;
                i8 = i6 >> 16 & 0xFF;
                i9 = i6 >> 8 & 0xFF;
                i10 = i6 & 0xFF;
                tintedPixels[i5] = ((m * i7 & 0xFF00) << 16 | (n * i8 & 0xFF00) << 8 | i1 * i9 & 0xFF00 | (i2 * i10 & 0xFF00) >> 8);
              }
            }
            else if (source.format == 4)
            {
              i5 = paramInt & 0xFFFFFF;
              for (i6 = 0; i6 < source.width; i6++)
              {
                i7 = source.pixels[(i3++)];
                tintedPixels[i6] = ((m * i7 & 0xFF00) << 16 | i5);
              }
            }
            localWritableRaster.setDataElements(0, i4, source.width, 1, tintedPixels);
          }
        }
      }
      else
      {
        localWritableRaster.setDataElements(0, 0, source.width, source.height, source.pixels);
      }
      tinted = paramBoolean;
      tintedColor = paramInt;
    }
  }
}
