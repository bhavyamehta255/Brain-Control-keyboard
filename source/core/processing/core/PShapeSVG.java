package processing.core;

import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.PrintStream;
import java.util.HashMap;
import processing.xml.XMLElement;

public class PShapeSVG
  extends PShape
{
  XMLElement element;
  float opacity;
  float strokeOpacity;
  float fillOpacity;
  Gradient strokeGradient;
  Paint strokeGradientPaint;
  String strokeName;
  Gradient fillGradient;
  Paint fillGradientPaint;
  String fillName;
  
  public PShapeSVG(PApplet paramPApplet, String paramString)
  {
    this(new XMLElement(paramPApplet, paramString));
  }
  
  public PShapeSVG(XMLElement paramXMLElement)
  {
    this(null, paramXMLElement, true);
    if (!paramXMLElement.getName().equals("svg")) {
      throw new RuntimeException("root is not <svg>, it's <" + paramXMLElement.getName() + ">");
    }
    String str1 = paramXMLElement.getString("viewBox");
    if (str1 != null)
    {
      localObject = PApplet.parseInt(PApplet.splitTokens(str1));
      width = localObject[2];
      height = localObject[3];
    }
    Object localObject = paramXMLElement.getString("width");
    String str2 = paramXMLElement.getString("height");
    if (localObject != null)
    {
      width = parseUnitSize((String)localObject);
      height = parseUnitSize(str2);
    }
    else if ((width == 0.0F) || (height == 0.0F))
    {
      PGraphics.showWarning("The width and/or height is not readable in the <svg> tag of this file.");
      width = 1.0F;
      height = 1.0F;
    }
  }
  
  public PShapeSVG(PShapeSVG paramPShapeSVG, XMLElement paramXMLElement, boolean paramBoolean)
  {
    parent = paramPShapeSVG;
    if (paramPShapeSVG == null)
    {
      stroke = false;
      strokeColor = -16777216;
      strokeWeight = 1.0F;
      strokeCap = 1;
      strokeJoin = 8;
      strokeGradient = null;
      strokeGradientPaint = null;
      strokeName = null;
      fill = true;
      fillColor = -16777216;
      fillGradient = null;
      fillGradientPaint = null;
      fillName = null;
      strokeOpacity = 1.0F;
      fillOpacity = 1.0F;
      opacity = 1.0F;
    }
    else
    {
      stroke = stroke;
      strokeColor = strokeColor;
      strokeWeight = strokeWeight;
      strokeCap = strokeCap;
      strokeJoin = strokeJoin;
      strokeGradient = strokeGradient;
      strokeGradientPaint = strokeGradientPaint;
      strokeName = strokeName;
      fill = fill;
      fillColor = fillColor;
      fillGradient = fillGradient;
      fillGradientPaint = fillGradientPaint;
      fillName = fillName;
      opacity = opacity;
    }
    element = paramXMLElement;
    name = paramXMLElement.getString("id");
    if (name != null) {
      for (;;)
      {
        localObject = PApplet.match(name, "_x([A-Za-z0-9]{2})_");
        if (localObject == null) {
          break;
        }
        char c = (char)PApplet.unhex(localObject[1]);
        name = name.replace(localObject[0], "" + c);
      }
    }
    Object localObject = paramXMLElement.getString("display", "inline");
    visible = (!((String)localObject).equals("none"));
    String str = paramXMLElement.getString("transform");
    if (str != null) {
      matrix = parseTransform(str);
    }
    if (paramBoolean)
    {
      parseColors(paramXMLElement);
      parseChildren(paramXMLElement);
    }
  }
  
  protected void parseChildren(XMLElement paramXMLElement)
  {
    XMLElement[] arrayOfXMLElement1 = paramXMLElement.getChildren();
    children = new PShape[arrayOfXMLElement1.length];
    childCount = 0;
    for (XMLElement localXMLElement : arrayOfXMLElement1)
    {
      PShape localPShape = parseChild(localXMLElement);
      if (localPShape != null) {
        addChild(localPShape);
      }
    }
    children = ((PShape[])PApplet.subset(children, 0, childCount));
  }
  
  protected PShape parseChild(XMLElement paramXMLElement)
  {
    String str = paramXMLElement.getName();
    PShapeSVG localPShapeSVG = null;
    if (str.equals("g"))
    {
      localPShapeSVG = new PShapeSVG(this, paramXMLElement, true);
    }
    else if (str.equals("defs"))
    {
      localPShapeSVG = new PShapeSVG(this, paramXMLElement, true);
    }
    else if (str.equals("line"))
    {
      localPShapeSVG = new PShapeSVG(this, paramXMLElement, true);
      localPShapeSVG.parseLine();
    }
    else if (str.equals("circle"))
    {
      localPShapeSVG = new PShapeSVG(this, paramXMLElement, true);
      localPShapeSVG.parseEllipse(true);
    }
    else if (str.equals("ellipse"))
    {
      localPShapeSVG = new PShapeSVG(this, paramXMLElement, true);
      localPShapeSVG.parseEllipse(false);
    }
    else if (str.equals("rect"))
    {
      localPShapeSVG = new PShapeSVG(this, paramXMLElement, true);
      localPShapeSVG.parseRect();
    }
    else if (str.equals("polygon"))
    {
      localPShapeSVG = new PShapeSVG(this, paramXMLElement, true);
      localPShapeSVG.parsePoly(true);
    }
    else if (str.equals("polyline"))
    {
      localPShapeSVG = new PShapeSVG(this, paramXMLElement, true);
      localPShapeSVG.parsePoly(false);
    }
    else if (str.equals("path"))
    {
      localPShapeSVG = new PShapeSVG(this, paramXMLElement, true);
      localPShapeSVG.parsePath();
    }
    else
    {
      if (str.equals("radialGradient")) {
        return new RadialGradient(this, paramXMLElement);
      }
      if (str.equals("linearGradient")) {
        return new LinearGradient(this, paramXMLElement);
      }
      if (str.equals("font")) {
        return new Font(this, paramXMLElement);
      }
      if (str.equals("metadata")) {
        return null;
      }
      if (str.equals("text")) {
        PGraphics.showWarning("Text and fonts in SVG files are not currently supported, convert text to outlines instead.");
      } else if (str.equals("filter")) {
        PGraphics.showWarning("Filters are not supported.");
      } else if (str.equals("mask")) {
        PGraphics.showWarning("Masks are not supported.");
      } else if (str.equals("pattern")) {
        PGraphics.showWarning("Patterns are not supported.");
      } else if ((!str.equals("stop")) && (!str.equals("sodipodi:namedview"))) {
        PGraphics.showWarning("Ignoring <" + str + "> tag.");
      }
    }
    return localPShapeSVG;
  }
  
  protected void parseLine()
  {
    primitive = 4;
    family = 1;
    params = new float[] { getFloatWithUnit(element, "x1"), getFloatWithUnit(element, "y1"), getFloatWithUnit(element, "x2"), getFloatWithUnit(element, "y2") };
  }
  
  protected void parseEllipse(boolean paramBoolean)
  {
    primitive = 31;
    family = 1;
    params = new float[4];
    params[0] = getFloatWithUnit(element, "cx");
    params[1] = getFloatWithUnit(element, "cy");
    float f2;
    float f1;
    if (paramBoolean)
    {
      f1 = f2 = getFloatWithUnit(element, "r");
    }
    else
    {
      f1 = getFloatWithUnit(element, "rx");
      f2 = getFloatWithUnit(element, "ry");
    }
    params[0] -= f1;
    params[1] -= f2;
    params[2] = (f1 * 2.0F);
    params[3] = (f2 * 2.0F);
  }
  
  protected void parseRect()
  {
    primitive = 30;
    family = 1;
    params = new float[] { getFloatWithUnit(element, "x"), getFloatWithUnit(element, "y"), getFloatWithUnit(element, "width"), getFloatWithUnit(element, "height") };
  }
  
  protected void parsePoly(boolean paramBoolean)
  {
    family = 2;
    close = paramBoolean;
    String str = element.getString("points");
    if (str != null)
    {
      String[] arrayOfString1 = PApplet.splitTokens(str);
      vertexCount = arrayOfString1.length;
      vertices = new float[vertexCount][2];
      for (int i = 0; i < vertexCount; i++)
      {
        String[] arrayOfString2 = PApplet.split(arrayOfString1[i], ',');
        vertices[i][0] = Float.valueOf(arrayOfString2[0]).floatValue();
        vertices[i][1] = Float.valueOf(arrayOfString2[1]).floatValue();
      }
    }
  }
  
  protected void parsePath()
  {
    family = 2;
    primitive = 0;
    String str1 = element.getString("d");
    if ((str1 == null) || (PApplet.trim(str1).length() == 0)) {
      return;
    }
    char[] arrayOfChar = str1.toCharArray();
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    for (int j = 0; j < arrayOfChar.length; j++)
    {
      char c = arrayOfChar[j];
      int k = 0;
      if ((c == 'M') || (c == 'm') || (c == 'L') || (c == 'l') || (c == 'H') || (c == 'h') || (c == 'V') || (c == 'v') || (c == 'C') || (c == 'c') || (c == 'S') || (c == 's') || (c == 'Q') || (c == 'q') || (c == 'T') || (c == 't') || (c == 'Z') || (c == 'z') || (c == ','))
      {
        k = 1;
        if (j != 0) {
          localStringBuffer.append("|");
        }
      }
      if ((c == 'Z') || (c == 'z')) {
        k = 0;
      }
      if ((c == '-') && (i == 0) && ((j == 0) || (arrayOfChar[(j - 1)] != 'e'))) {
        localStringBuffer.append("|");
      }
      if (c != ',') {
        localStringBuffer.append(c);
      }
      if ((k != 0) && (c != ',') && (c != '-')) {
        localStringBuffer.append("|");
      }
      i = k;
    }
    String[] arrayOfString = PApplet.splitTokens(localStringBuffer.toString(), "| \t\n\r\f ");
    vertices = new float[arrayOfString.length][2];
    vertexCodes = new int[arrayOfString.length];
    float f1 = 0.0F;
    float f2 = 0.0F;
    int m = 0;
    int n = 0;
    int i1 = 0;
    while (m < arrayOfString.length)
    {
      int i2 = arrayOfString[m].charAt(0);
      if (((i2 >= 48) && (i2 <= 57)) || ((i2 == 45) && (n != 0)))
      {
        i2 = n;
        m--;
      }
      else
      {
        n = i2;
      }
      float f5;
      float f6;
      float f7;
      float f8;
      float f9;
      float f10;
      float f3;
      float f4;
      switch (i2)
      {
      case 77: 
        f1 = PApplet.parseFloat(arrayOfString[(m + 1)]);
        f2 = PApplet.parseFloat(arrayOfString[(m + 2)]);
        parsePathMoveto(f1, f2);
        n = 76;
        m += 3;
        break;
      case 109: 
        f1 += PApplet.parseFloat(arrayOfString[(m + 1)]);
        f2 += PApplet.parseFloat(arrayOfString[(m + 2)]);
        parsePathMoveto(f1, f2);
        n = 108;
        m += 3;
        break;
      case 76: 
        f1 = PApplet.parseFloat(arrayOfString[(m + 1)]);
        f2 = PApplet.parseFloat(arrayOfString[(m + 2)]);
        parsePathLineto(f1, f2);
        m += 3;
        break;
      case 108: 
        f1 += PApplet.parseFloat(arrayOfString[(m + 1)]);
        f2 += PApplet.parseFloat(arrayOfString[(m + 2)]);
        parsePathLineto(f1, f2);
        m += 3;
        break;
      case 72: 
        f1 = PApplet.parseFloat(arrayOfString[(m + 1)]);
        parsePathLineto(f1, f2);
        m += 2;
        break;
      case 104: 
        f1 += PApplet.parseFloat(arrayOfString[(m + 1)]);
        parsePathLineto(f1, f2);
        m += 2;
        break;
      case 86: 
        f2 = PApplet.parseFloat(arrayOfString[(m + 1)]);
        parsePathLineto(f1, f2);
        m += 2;
        break;
      case 118: 
        f2 += PApplet.parseFloat(arrayOfString[(m + 1)]);
        parsePathLineto(f1, f2);
        m += 2;
        break;
      case 67: 
        f5 = PApplet.parseFloat(arrayOfString[(m + 1)]);
        f6 = PApplet.parseFloat(arrayOfString[(m + 2)]);
        f7 = PApplet.parseFloat(arrayOfString[(m + 3)]);
        f8 = PApplet.parseFloat(arrayOfString[(m + 4)]);
        f9 = PApplet.parseFloat(arrayOfString[(m + 5)]);
        f10 = PApplet.parseFloat(arrayOfString[(m + 6)]);
        parsePathCurveto(f5, f6, f7, f8, f9, f10);
        f1 = f9;
        f2 = f10;
        m += 7;
        i1 = 1;
        break;
      case 99: 
        f5 = f1 + PApplet.parseFloat(arrayOfString[(m + 1)]);
        f6 = f2 + PApplet.parseFloat(arrayOfString[(m + 2)]);
        f7 = f1 + PApplet.parseFloat(arrayOfString[(m + 3)]);
        f8 = f2 + PApplet.parseFloat(arrayOfString[(m + 4)]);
        f9 = f1 + PApplet.parseFloat(arrayOfString[(m + 5)]);
        f10 = f2 + PApplet.parseFloat(arrayOfString[(m + 6)]);
        parsePathCurveto(f5, f6, f7, f8, f9, f10);
        f1 = f9;
        f2 = f10;
        m += 7;
        i1 = 1;
        break;
      case 83: 
        if (i1 == 0)
        {
          f3 = f1;
          f4 = f2;
        }
        else
        {
          f5 = vertices[(vertexCount - 2)][0];
          f6 = vertices[(vertexCount - 2)][1];
          f7 = vertices[(vertexCount - 1)][0];
          f8 = vertices[(vertexCount - 1)][1];
          f3 = f7 + (f7 - f5);
          f4 = f8 + (f8 - f6);
        }
        f5 = PApplet.parseFloat(arrayOfString[(m + 1)]);
        f6 = PApplet.parseFloat(arrayOfString[(m + 2)]);
        f7 = PApplet.parseFloat(arrayOfString[(m + 3)]);
        f8 = PApplet.parseFloat(arrayOfString[(m + 4)]);
        parsePathCurveto(f3, f4, f5, f6, f7, f8);
        f1 = f7;
        f2 = f8;
        m += 5;
        i1 = 1;
        break;
      case 115: 
        if (i1 == 0)
        {
          f3 = f1;
          f4 = f2;
        }
        else
        {
          f5 = vertices[(vertexCount - 2)][0];
          f6 = vertices[(vertexCount - 2)][1];
          f7 = vertices[(vertexCount - 1)][0];
          f8 = vertices[(vertexCount - 1)][1];
          f3 = f7 + (f7 - f5);
          f4 = f8 + (f8 - f6);
        }
        f5 = f1 + PApplet.parseFloat(arrayOfString[(m + 1)]);
        f6 = f2 + PApplet.parseFloat(arrayOfString[(m + 2)]);
        f7 = f1 + PApplet.parseFloat(arrayOfString[(m + 3)]);
        f8 = f2 + PApplet.parseFloat(arrayOfString[(m + 4)]);
        parsePathCurveto(f3, f4, f5, f6, f7, f8);
        f1 = f7;
        f2 = f8;
        m += 5;
        i1 = 1;
        break;
      case 81: 
        f3 = PApplet.parseFloat(arrayOfString[(m + 1)]);
        f4 = PApplet.parseFloat(arrayOfString[(m + 2)]);
        f5 = PApplet.parseFloat(arrayOfString[(m + 3)]);
        f6 = PApplet.parseFloat(arrayOfString[(m + 4)]);
        parsePathQuadto(f3, f4, f5, f6);
        f1 = f5;
        f2 = f6;
        m += 5;
        i1 = 1;
        break;
      case 113: 
        f3 = f1 + PApplet.parseFloat(arrayOfString[(m + 1)]);
        f4 = f2 + PApplet.parseFloat(arrayOfString[(m + 2)]);
        f5 = f1 + PApplet.parseFloat(arrayOfString[(m + 3)]);
        f6 = f2 + PApplet.parseFloat(arrayOfString[(m + 4)]);
        parsePathQuadto(f3, f4, f5, f6);
        f1 = f5;
        f2 = f6;
        m += 5;
        i1 = 1;
        break;
      case 84: 
        if (i1 == 0)
        {
          f3 = f1;
          f4 = f2;
        }
        else
        {
          f5 = vertices[(vertexCount - 2)][0];
          f6 = vertices[(vertexCount - 2)][1];
          f7 = vertices[(vertexCount - 1)][0];
          f8 = vertices[(vertexCount - 1)][1];
          f3 = f7 + (f7 - f5);
          f4 = f8 + (f8 - f6);
        }
        f5 = PApplet.parseFloat(arrayOfString[(m + 1)]);
        f6 = PApplet.parseFloat(arrayOfString[(m + 2)]);
        parsePathQuadto(f3, f4, f5, f6);
        f1 = f5;
        f2 = f6;
        m += 3;
        i1 = 1;
        break;
      case 116: 
        if (i1 == 0)
        {
          f3 = f1;
          f4 = f2;
        }
        else
        {
          f5 = vertices[(vertexCount - 2)][0];
          f6 = vertices[(vertexCount - 2)][1];
          f7 = vertices[(vertexCount - 1)][0];
          f8 = vertices[(vertexCount - 1)][1];
          f3 = f7 + (f7 - f5);
          f4 = f8 + (f8 - f6);
        }
        f5 = f1 + PApplet.parseFloat(arrayOfString[(m + 1)]);
        f6 = f2 + PApplet.parseFloat(arrayOfString[(m + 2)]);
        parsePathQuadto(f3, f4, f5, f6);
        f1 = f5;
        f2 = f6;
        m += 3;
        i1 = 1;
        break;
      case 90: 
      case 122: 
        close = true;
        m++;
        break;
      case 68: 
      case 69: 
      case 70: 
      case 71: 
      case 73: 
      case 74: 
      case 75: 
      case 78: 
      case 79: 
      case 80: 
      case 82: 
      case 85: 
      case 87: 
      case 88: 
      case 89: 
      case 91: 
      case 92: 
      case 93: 
      case 94: 
      case 95: 
      case 96: 
      case 97: 
      case 98: 
      case 100: 
      case 101: 
      case 102: 
      case 103: 
      case 105: 
      case 106: 
      case 107: 
      case 110: 
      case 111: 
      case 112: 
      case 114: 
      case 117: 
      case 119: 
      case 120: 
      case 121: 
      default: 
        String str2 = PApplet.join(PApplet.subset(arrayOfString, 0, m), ",");
        String str3 = PApplet.join(PApplet.subset(arrayOfString, m), ",");
        System.err.println("parsed: " + str2);
        System.err.println("unparsed: " + str3);
        if ((arrayOfString[m].equals("a")) || (arrayOfString[m].equals("A")))
        {
          String str4 = "Sorry, elliptical arc support for SVG files is not yet implemented (See issue #130 for updates)";
          throw new RuntimeException(str4);
        }
        throw new RuntimeException("shape command not handled: " + arrayOfString[m]);
      }
    }
  }
  
  private void parsePathVertex(float paramFloat1, float paramFloat2)
  {
    if (vertexCount == vertices.length)
    {
      float[][] arrayOfFloat = new float[vertexCount << 1][2];
      System.arraycopy(vertices, 0, arrayOfFloat, 0, vertexCount);
      vertices = arrayOfFloat;
    }
    vertices[vertexCount][0] = paramFloat1;
    vertices[vertexCount][1] = paramFloat2;
    vertexCount += 1;
  }
  
  private void parsePathCode(int paramInt)
  {
    if (vertexCodeCount == vertexCodes.length) {
      vertexCodes = PApplet.expand(vertexCodes);
    }
    vertexCodes[(vertexCodeCount++)] = paramInt;
  }
  
  private void parsePathMoveto(float paramFloat1, float paramFloat2)
  {
    if (vertexCount > 0) {
      parsePathCode(4);
    }
    parsePathCode(0);
    parsePathVertex(paramFloat1, paramFloat2);
  }
  
  private void parsePathLineto(float paramFloat1, float paramFloat2)
  {
    parsePathCode(0);
    parsePathVertex(paramFloat1, paramFloat2);
  }
  
  private void parsePathCurveto(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    parsePathCode(1);
    parsePathVertex(paramFloat1, paramFloat2);
    parsePathVertex(paramFloat3, paramFloat4);
    parsePathVertex(paramFloat5, paramFloat6);
  }
  
  private void parsePathQuadto(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    parsePathCode(2);
    parsePathVertex(paramFloat1, paramFloat2);
    parsePathVertex(paramFloat3, paramFloat4);
  }
  
  protected static PMatrix2D parseTransform(String paramString)
  {
    paramString = paramString.trim();
    Object localObject = null;
    int i = 0;
    int j = -1;
    while ((j = paramString.indexOf(')', i)) != -1)
    {
      PMatrix2D localPMatrix2D = parseSingleTransform(paramString.substring(i, j + 1));
      if (localObject == null) {
        localObject = localPMatrix2D;
      } else {
        localObject.apply(localPMatrix2D);
      }
      i = j + 1;
    }
    return localObject;
  }
  
  protected static PMatrix2D parseSingleTransform(String paramString)
  {
    String[] arrayOfString = PApplet.match(paramString, "[,\\s]*(\\w+)\\((.*)\\)");
    if (arrayOfString == null)
    {
      System.err.println("Could not parse transform " + paramString);
      return null;
    }
    float[] arrayOfFloat = PApplet.parseFloat(PApplet.splitTokens(arrayOfString[2], ", "));
    if (arrayOfString[1].equals("matrix")) {
      return new PMatrix2D(arrayOfFloat[0], arrayOfFloat[2], arrayOfFloat[4], arrayOfFloat[1], arrayOfFloat[3], arrayOfFloat[5]);
    }
    float f1;
    float f2;
    if (arrayOfString[1].equals("translate"))
    {
      f1 = arrayOfFloat[0];
      f2 = arrayOfFloat.length == 2 ? arrayOfFloat[1] : arrayOfFloat[0];
      return new PMatrix2D(1.0F, 0.0F, f1, 0.0F, 1.0F, f2);
    }
    if (arrayOfString[1].equals("scale"))
    {
      f1 = arrayOfFloat[0];
      f2 = arrayOfFloat.length == 2 ? arrayOfFloat[1] : arrayOfFloat[0];
      return new PMatrix2D(f1, 0.0F, 0.0F, 0.0F, f2, 0.0F);
    }
    if (arrayOfString[1].equals("rotate"))
    {
      f1 = arrayOfFloat[0];
      if (arrayOfFloat.length == 1)
      {
        f2 = PApplet.cos(f1);
        float f3 = PApplet.sin(f1);
        return new PMatrix2D(f2, -f3, 0.0F, f3, f2, 0.0F);
      }
      if (arrayOfFloat.length == 3)
      {
        PMatrix2D localPMatrix2D = new PMatrix2D(0.0F, 1.0F, arrayOfFloat[1], 1.0F, 0.0F, arrayOfFloat[2]);
        localPMatrix2D.rotate(arrayOfFloat[0]);
        localPMatrix2D.translate(-arrayOfFloat[1], -arrayOfFloat[2]);
        return localPMatrix2D;
      }
    }
    else
    {
      if (arrayOfString[1].equals("skewX")) {
        return new PMatrix2D(1.0F, 0.0F, 1.0F, PApplet.tan(arrayOfFloat[0]), 0.0F, 0.0F);
      }
      if (arrayOfString[1].equals("skewY")) {
        return new PMatrix2D(1.0F, 0.0F, 1.0F, 0.0F, PApplet.tan(arrayOfFloat[0]), 0.0F);
      }
    }
    return null;
  }
  
  protected void parseColors(XMLElement paramXMLElement)
  {
    String str;
    if (paramXMLElement.hasAttribute("opacity"))
    {
      str = paramXMLElement.getString("opacity");
      setOpacity(str);
    }
    if (paramXMLElement.hasAttribute("stroke"))
    {
      str = paramXMLElement.getString("stroke");
      setColor(str, false);
    }
    if (paramXMLElement.hasAttribute("stroke-opacity"))
    {
      str = paramXMLElement.getString("stroke-opacity");
      setStrokeOpacity(str);
    }
    if (paramXMLElement.hasAttribute("stroke-width"))
    {
      str = paramXMLElement.getString("stroke-width");
      setStrokeWeight(str);
    }
    if (paramXMLElement.hasAttribute("stroke-linejoin"))
    {
      str = paramXMLElement.getString("stroke-linejoin");
      setStrokeJoin(str);
    }
    if (paramXMLElement.hasAttribute("stroke-linecap"))
    {
      str = paramXMLElement.getString("stroke-linecap");
      setStrokeCap(str);
    }
    if (paramXMLElement.hasAttribute("fill"))
    {
      str = paramXMLElement.getString("fill");
      setColor(str, true);
    }
    if (paramXMLElement.hasAttribute("fill-opacity"))
    {
      str = paramXMLElement.getString("fill-opacity");
      setFillOpacity(str);
    }
    if (paramXMLElement.hasAttribute("style"))
    {
      str = paramXMLElement.getString("style");
      String[] arrayOfString1 = PApplet.splitTokens(str, ";");
      for (int i = 0; i < arrayOfString1.length; i++)
      {
        String[] arrayOfString2 = PApplet.splitTokens(arrayOfString1[i], ":");
        arrayOfString2[0] = PApplet.trim(arrayOfString2[0]);
        if (arrayOfString2[0].equals("fill")) {
          setColor(arrayOfString2[1], true);
        } else if (arrayOfString2[0].equals("fill-opacity")) {
          setFillOpacity(arrayOfString2[1]);
        } else if (arrayOfString2[0].equals("stroke")) {
          setColor(arrayOfString2[1], false);
        } else if (arrayOfString2[0].equals("stroke-width")) {
          setStrokeWeight(arrayOfString2[1]);
        } else if (arrayOfString2[0].equals("stroke-linecap")) {
          setStrokeCap(arrayOfString2[1]);
        } else if (arrayOfString2[0].equals("stroke-linejoin")) {
          setStrokeJoin(arrayOfString2[1]);
        } else if (arrayOfString2[0].equals("stroke-opacity")) {
          setStrokeOpacity(arrayOfString2[1]);
        } else if (arrayOfString2[0].equals("opacity")) {
          setOpacity(arrayOfString2[1]);
        }
      }
    }
  }
  
  void setOpacity(String paramString)
  {
    opacity = PApplet.parseFloat(paramString);
    strokeColor = ((int)(opacity * 255.0F) << 24 | strokeColor & 0xFFFFFF);
    fillColor = ((int)(opacity * 255.0F) << 24 | fillColor & 0xFFFFFF);
  }
  
  void setStrokeWeight(String paramString)
  {
    strokeWeight = parseUnitSize(paramString);
  }
  
  void setStrokeOpacity(String paramString)
  {
    strokeOpacity = PApplet.parseFloat(paramString);
    strokeColor = ((int)(strokeOpacity * 255.0F) << 24 | strokeColor & 0xFFFFFF);
  }
  
  void setStrokeJoin(String paramString)
  {
    if (!paramString.equals("inherit")) {
      if (paramString.equals("miter")) {
        strokeJoin = 8;
      } else if (paramString.equals("round")) {
        strokeJoin = 2;
      } else if (paramString.equals("bevel")) {
        strokeJoin = 32;
      }
    }
  }
  
  void setStrokeCap(String paramString)
  {
    if (!paramString.equals("inherit")) {
      if (paramString.equals("butt")) {
        strokeCap = 1;
      } else if (paramString.equals("round")) {
        strokeCap = 2;
      } else if (paramString.equals("square")) {
        strokeCap = 4;
      }
    }
  }
  
  void setFillOpacity(String paramString)
  {
    fillOpacity = PApplet.parseFloat(paramString);
    fillColor = ((int)(fillOpacity * 255.0F) << 24 | fillColor & 0xFFFFFF);
  }
  
  void setColor(String paramString, boolean paramBoolean)
  {
    int i = fillColor & 0xFF000000;
    boolean bool = true;
    int j = 0;
    String str = "";
    Gradient localGradient = null;
    Paint localPaint = null;
    if (paramString.equals("none"))
    {
      bool = false;
    }
    else if (paramString.equals("black"))
    {
      j = i;
    }
    else if (paramString.equals("white"))
    {
      j = i | 0xFFFFFF;
    }
    else if (paramString.startsWith("#"))
    {
      if (paramString.length() == 4) {
        paramString = paramString.replaceAll("^#(.)(.)(.)$", "#$1$1$2$2$3$3");
      }
      j = i | Integer.parseInt(paramString.substring(1), 16) & 0xFFFFFF;
    }
    else if (paramString.startsWith("rgb"))
    {
      j = i | parseRGB(paramString);
    }
    else if (paramString.startsWith("url(#"))
    {
      str = paramString.substring(5, paramString.length() - 1);
      PShape localPShape = findChild(str);
      if ((localPShape instanceof Gradient))
      {
        localGradient = (Gradient)localPShape;
        localPaint = calcGradientPaint(localGradient);
      }
      else
      {
        System.err.println("url " + str + " refers to unexpected data: " + localPShape);
      }
    }
    if (paramBoolean)
    {
      fill = bool;
      fillColor = j;
      fillName = str;
      fillGradient = localGradient;
      fillGradientPaint = localPaint;
    }
    else
    {
      stroke = bool;
      strokeColor = j;
      strokeName = str;
      strokeGradient = localGradient;
      strokeGradientPaint = localPaint;
    }
  }
  
  protected static int parseRGB(String paramString)
  {
    int i = paramString.indexOf('(') + 1;
    int j = paramString.indexOf(')');
    String str = paramString.substring(i, j);
    int[] arrayOfInt = PApplet.parseInt(PApplet.splitTokens(str, ", "));
    return arrayOfInt[0] << 16 | arrayOfInt[1] << 8 | arrayOfInt[2];
  }
  
  protected static HashMap<String, String> parseStyleAttributes(String paramString)
  {
    HashMap localHashMap = new HashMap();
    String[] arrayOfString1 = paramString.split(";");
    for (int i = 0; i < arrayOfString1.length; i++)
    {
      String[] arrayOfString2 = arrayOfString1[i].split(":");
      localHashMap.put(arrayOfString2[0], arrayOfString2[1]);
    }
    return localHashMap;
  }
  
  protected static float getFloatWithUnit(XMLElement paramXMLElement, String paramString)
  {
    String str = paramXMLElement.getString(paramString);
    return str == null ? 0.0F : parseUnitSize(str);
  }
  
  protected static float parseUnitSize(String paramString)
  {
    int i = paramString.length() - 2;
    if (paramString.endsWith("pt")) {
      return PApplet.parseFloat(paramString.substring(0, i)) * 1.25F;
    }
    if (paramString.endsWith("pc")) {
      return PApplet.parseFloat(paramString.substring(0, i)) * 15.0F;
    }
    if (paramString.endsWith("mm")) {
      return PApplet.parseFloat(paramString.substring(0, i)) * 3.543307F;
    }
    if (paramString.endsWith("cm")) {
      return PApplet.parseFloat(paramString.substring(0, i)) * 35.43307F;
    }
    if (paramString.endsWith("in")) {
      return PApplet.parseFloat(paramString.substring(0, i)) * 90.0F;
    }
    if (paramString.endsWith("px")) {
      return PApplet.parseFloat(paramString.substring(0, i));
    }
    return PApplet.parseFloat(paramString);
  }
  
  protected Paint calcGradientPaint(Gradient paramGradient)
  {
    Object localObject;
    if ((paramGradient instanceof LinearGradient))
    {
      localObject = (LinearGradient)paramGradient;
      return new LinearGradientPaint(x1, y1, x2, y2, offset, color, count, opacity);
    }
    if ((paramGradient instanceof RadialGradient))
    {
      localObject = (RadialGradient)paramGradient;
      return new RadialGradientPaint(cx, cy, r, offset, color, count, opacity);
    }
    return null;
  }
  
  protected void styles(PGraphics paramPGraphics)
  {
    super.styles(paramPGraphics);
    if ((paramPGraphics instanceof PGraphicsJava2D))
    {
      PGraphicsJava2D localPGraphicsJava2D = (PGraphicsJava2D)paramPGraphics;
      if (strokeGradient != null)
      {
        strokeGradient = true;
        strokeGradientObject = strokeGradientPaint;
      }
      if (fillGradient != null)
      {
        fillGradient = true;
        fillGradientObject = fillGradientPaint;
      }
    }
  }
  
  public PShape getChild(String paramString)
  {
    PShape localPShape = super.getChild(paramString);
    if (localPShape == null) {
      localPShape = super.getChild(paramString.replace(' ', '_'));
    }
    if (localPShape != null)
    {
      width = width;
      height = height;
    }
    return localPShape;
  }
  
  public void print()
  {
    PApplet.println(element.toString());
  }
  
  public class FontGlyph
    extends PShapeSVG
  {
    public String name;
    char unicode;
    int horizAdvX;
    
    public FontGlyph(PShapeSVG paramPShapeSVG, XMLElement paramXMLElement, PShapeSVG.Font paramFont)
    {
      super(paramXMLElement, true);
      super.parsePath();
      name = paramXMLElement.getString("glyph-name");
      String str = paramXMLElement.getString("unicode");
      unicode = '\000';
      if (str != null) {
        if (str.length() == 1) {
          unicode = str.charAt(0);
        } else {
          System.err.println("unicode for " + name + " is more than one char: " + str);
        }
      }
      if (paramXMLElement.hasAttribute("horiz-adv-x")) {
        horizAdvX = paramXMLElement.getInt("horiz-adv-x");
      } else {
        horizAdvX = horizAdvX;
      }
    }
    
    protected boolean isLegit()
    {
      return vertexCount != 0;
    }
  }
  
  class FontFace
    extends PShapeSVG
  {
    int horizOriginX;
    int horizOriginY;
    int vertOriginX;
    int vertOriginY;
    int vertAdvY;
    String fontFamily;
    int fontWeight;
    String fontStretch;
    int unitsPerEm;
    int[] panose1;
    int ascent;
    int descent;
    int[] bbox;
    int underlineThickness;
    int underlinePosition;
    
    public FontFace(PShapeSVG paramPShapeSVG, XMLElement paramXMLElement)
    {
      super(paramXMLElement, true);
      unitsPerEm = paramXMLElement.getInt("units-per-em", 1000);
    }
    
    protected void drawShape() {}
  }
  
  public class Font
    extends PShapeSVG
  {
    public PShapeSVG.FontFace face;
    public HashMap<String, PShapeSVG.FontGlyph> namedGlyphs;
    public HashMap<Character, PShapeSVG.FontGlyph> unicodeGlyphs;
    public int glyphCount;
    public PShapeSVG.FontGlyph[] glyphs;
    public PShapeSVG.FontGlyph missingGlyph;
    int horizAdvX;
    
    public Font(PShapeSVG paramPShapeSVG, XMLElement paramXMLElement)
    {
      super(paramXMLElement, false);
      XMLElement[] arrayOfXMLElement = paramXMLElement.getChildren();
      horizAdvX = paramXMLElement.getInt("horiz-adv-x", 0);
      namedGlyphs = new HashMap();
      unicodeGlyphs = new HashMap();
      glyphCount = 0;
      glyphs = new PShapeSVG.FontGlyph[arrayOfXMLElement.length];
      for (int i = 0; i < arrayOfXMLElement.length; i++)
      {
        String str = arrayOfXMLElement[i].getName();
        XMLElement localXMLElement = arrayOfXMLElement[i];
        if (str.equals("glyph"))
        {
          PShapeSVG.FontGlyph localFontGlyph = new PShapeSVG.FontGlyph(this, this, localXMLElement, this);
          if (localFontGlyph.isLegit())
          {
            if (name != null) {
              namedGlyphs.put(name, localFontGlyph);
            }
            if (unicode != 0) {
              unicodeGlyphs.put(new Character(unicode), localFontGlyph);
            }
          }
          glyphs[(glyphCount++)] = localFontGlyph;
        }
        else if (str.equals("missing-glyph"))
        {
          missingGlyph = new PShapeSVG.FontGlyph(this, this, localXMLElement, this);
        }
        else if (str.equals("font-face"))
        {
          face = new PShapeSVG.FontFace(this, this, localXMLElement);
        }
        else
        {
          System.err.println("Ignoring " + str + " inside <font>");
        }
      }
    }
    
    protected void drawShape() {}
    
    public void drawString(PGraphics paramPGraphics, String paramString, float paramFloat1, float paramFloat2, float paramFloat3)
    {
      paramPGraphics.pushMatrix();
      float f = paramFloat3 / face.unitsPerEm;
      paramPGraphics.translate(paramFloat1, paramFloat2);
      paramPGraphics.scale(f, -f);
      char[] arrayOfChar = paramString.toCharArray();
      for (int i = 0; i < arrayOfChar.length; i++)
      {
        PShapeSVG.FontGlyph localFontGlyph = (PShapeSVG.FontGlyph)unicodeGlyphs.get(new Character(arrayOfChar[i]));
        if (localFontGlyph != null)
        {
          localFontGlyph.draw(paramPGraphics);
          paramPGraphics.translate(horizAdvX, 0.0F);
        }
        else
        {
          System.err.println("'" + arrayOfChar[i] + "' not available.");
        }
      }
      paramPGraphics.popMatrix();
    }
    
    public void drawChar(PGraphics paramPGraphics, char paramChar, float paramFloat1, float paramFloat2, float paramFloat3)
    {
      paramPGraphics.pushMatrix();
      float f = paramFloat3 / face.unitsPerEm;
      paramPGraphics.translate(paramFloat1, paramFloat2);
      paramPGraphics.scale(f, -f);
      PShapeSVG.FontGlyph localFontGlyph = (PShapeSVG.FontGlyph)unicodeGlyphs.get(new Character(paramChar));
      if (localFontGlyph != null) {
        paramPGraphics.shape(localFontGlyph);
      }
      paramPGraphics.popMatrix();
    }
    
    public float textWidth(String paramString, float paramFloat)
    {
      float f = 0.0F;
      char[] arrayOfChar = paramString.toCharArray();
      for (int i = 0; i < arrayOfChar.length; i++)
      {
        PShapeSVG.FontGlyph localFontGlyph = (PShapeSVG.FontGlyph)unicodeGlyphs.get(new Character(arrayOfChar[i]));
        if (localFontGlyph != null) {
          f += horizAdvX / face.unitsPerEm;
        }
      }
      return f * paramFloat;
    }
  }
  
  class RadialGradientPaint
    implements Paint
  {
    float cx;
    float cy;
    float radius;
    float[] offset;
    int[] color;
    int count;
    float opacity;
    
    public RadialGradientPaint(float paramFloat1, float paramFloat2, float paramFloat3, float[] paramArrayOfFloat, int[] paramArrayOfInt, int paramInt, float paramFloat4)
    {
      cx = paramFloat1;
      cy = paramFloat2;
      radius = paramFloat3;
      offset = paramArrayOfFloat;
      color = paramArrayOfInt;
      count = paramInt;
      opacity = paramFloat4;
    }
    
    public PaintContext createContext(ColorModel paramColorModel, Rectangle paramRectangle, Rectangle2D paramRectangle2D, AffineTransform paramAffineTransform, RenderingHints paramRenderingHints)
    {
      return new RadialGradientContext();
    }
    
    public int getTransparency()
    {
      return 3;
    }
    
    public class RadialGradientContext
      implements PaintContext
    {
      int ACCURACY = 5;
      
      public RadialGradientContext() {}
      
      public void dispose() {}
      
      public ColorModel getColorModel()
      {
        return ColorModel.getRGBdefault();
      }
      
      public Raster getRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
      {
        WritableRaster localWritableRaster = getColorModel().createCompatibleWritableRaster(paramInt3, paramInt4);
        int i = (int)radius * ACCURACY;
        int[][] arrayOfInt = new int[i][4];
        int j = 0;
        int i1;
        for (int k = 1; k < count; k++)
        {
          m = color[(k - 1)];
          n = color[k];
          i1 = (int)(offset[k] * (i - 1));
          for (int i2 = j; i2 <= i1; i2++)
          {
            float f2 = PApplet.norm(i2, j, i1);
            arrayOfInt[i2][0] = ((int)PApplet.lerp(m >> 16 & 0xFF, n >> 16 & 0xFF, f2));
            arrayOfInt[i2][1] = ((int)PApplet.lerp(m >> 8 & 0xFF, n >> 8 & 0xFF, f2));
            arrayOfInt[i2][2] = ((int)PApplet.lerp(m & 0xFF, n & 0xFF, f2));
            arrayOfInt[i2][3] = ((int)(PApplet.lerp(m >> 24 & 0xFF, n >> 24 & 0xFF, f2) * opacity));
          }
          j = i1;
        }
        int[] arrayOfInt1 = new int[paramInt3 * paramInt4 * 4];
        int m = 0;
        for (int n = 0; n < paramInt4; n++) {
          for (i1 = 0; i1 < paramInt3; i1++)
          {
            float f1 = PApplet.dist(cx, cy, paramInt1 + i1, paramInt2 + n);
            int i3 = PApplet.min((int)(f1 * ACCURACY), arrayOfInt.length - 1);
            arrayOfInt1[(m++)] = arrayOfInt[i3][0];
            arrayOfInt1[(m++)] = arrayOfInt[i3][1];
            arrayOfInt1[(m++)] = arrayOfInt[i3][2];
            arrayOfInt1[(m++)] = arrayOfInt[i3][3];
          }
        }
        localWritableRaster.setPixels(0, 0, paramInt3, paramInt4, arrayOfInt1);
        return localWritableRaster;
      }
    }
  }
  
  class LinearGradientPaint
    implements Paint
  {
    float x1;
    float y1;
    float x2;
    float y2;
    float[] offset;
    int[] color;
    int count;
    float opacity;
    
    public LinearGradientPaint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float[] paramArrayOfFloat, int[] paramArrayOfInt, int paramInt, float paramFloat5)
    {
      x1 = paramFloat1;
      y1 = paramFloat2;
      x2 = paramFloat3;
      y2 = paramFloat4;
      offset = paramArrayOfFloat;
      color = paramArrayOfInt;
      count = paramInt;
      opacity = paramFloat5;
    }
    
    public PaintContext createContext(ColorModel paramColorModel, Rectangle paramRectangle, Rectangle2D paramRectangle2D, AffineTransform paramAffineTransform, RenderingHints paramRenderingHints)
    {
      Point2D localPoint2D1 = paramAffineTransform.transform(new Point2D.Float(x1, y1), null);
      Point2D localPoint2D2 = paramAffineTransform.transform(new Point2D.Float(x2, y2), null);
      return new LinearGradientContext((float)localPoint2D1.getX(), (float)localPoint2D1.getY(), (float)localPoint2D2.getX(), (float)localPoint2D2.getY());
    }
    
    public int getTransparency()
    {
      return 3;
    }
    
    public class LinearGradientContext
      implements PaintContext
    {
      int ACCURACY = 2;
      float tx1;
      float ty1;
      float tx2;
      float ty2;
      
      public LinearGradientContext(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
      {
        tx1 = paramFloat1;
        ty1 = paramFloat2;
        tx2 = paramFloat3;
        ty2 = paramFloat4;
      }
      
      public void dispose() {}
      
      public ColorModel getColorModel()
      {
        return ColorModel.getRGBdefault();
      }
      
      public Raster getRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
      {
        WritableRaster localWritableRaster = getColorModel().createCompatibleWritableRaster(paramInt3, paramInt4);
        int[] arrayOfInt = new int[paramInt3 * paramInt4 * 4];
        float f1 = tx2 - tx1;
        float f2 = ty2 - ty1;
        float f3 = (float)Math.sqrt(f1 * f1 + f2 * f2);
        if (f3 != 0.0F)
        {
          f1 /= f3;
          f2 /= f3;
        }
        int i = (int)PApplet.dist(tx1, ty1, tx2, ty2) * ACCURACY;
        int k;
        int m;
        if (i <= 0)
        {
          int j = 0;
          for (k = 0; k < paramInt4; k++) {
            for (m = 0; m < paramInt3; m++)
            {
              arrayOfInt[(j++)] = 0;
              arrayOfInt[(j++)] = 0;
              arrayOfInt[(j++)] = 0;
              arrayOfInt[(j++)] = 255;
            }
          }
        }
        else
        {
          int[][] arrayOfInt1 = new int[i][4];
          k = 0;
          int i1;
          for (m = 1; m < count; m++)
          {
            n = color[(m - 1)];
            i1 = color[m];
            int i2 = (int)(offset[m] * (i - 1));
            for (int i3 = k; i3 <= i2; i3++)
            {
              float f6 = PApplet.norm(i3, k, i2);
              arrayOfInt1[i3][0] = ((int)PApplet.lerp(n >> 16 & 0xFF, i1 >> 16 & 0xFF, f6));
              arrayOfInt1[i3][1] = ((int)PApplet.lerp(n >> 8 & 0xFF, i1 >> 8 & 0xFF, f6));
              arrayOfInt1[i3][2] = ((int)PApplet.lerp(n & 0xFF, i1 & 0xFF, f6));
              arrayOfInt1[i3][3] = ((int)(PApplet.lerp(n >> 24 & 0xFF, i1 >> 24 & 0xFF, f6) * opacity));
            }
            k = i2;
          }
          m = 0;
          for (int n = 0; n < paramInt4; n++) {
            for (i1 = 0; i1 < paramInt3; i1++)
            {
              float f4 = paramInt1 + i1 - tx1;
              float f5 = paramInt2 + n - ty1;
              int i4 = (int)((f4 * f1 + f5 * f2) * ACCURACY);
              if (i4 < 0) {
                i4 = 0;
              }
              if (i4 > arrayOfInt1.length - 1) {
                i4 = arrayOfInt1.length - 1;
              }
              arrayOfInt[(m++)] = arrayOfInt1[i4][0];
              arrayOfInt[(m++)] = arrayOfInt1[i4][1];
              arrayOfInt[(m++)] = arrayOfInt1[i4][2];
              arrayOfInt[(m++)] = arrayOfInt1[i4][3];
            }
          }
        }
        localWritableRaster.setPixels(0, 0, paramInt3, paramInt4, arrayOfInt);
        return localWritableRaster;
      }
    }
  }
  
  class RadialGradient
    extends PShapeSVG.Gradient
  {
    float cx;
    float cy;
    float r;
    
    public RadialGradient(PShapeSVG paramPShapeSVG, XMLElement paramXMLElement)
    {
      super(paramXMLElement);
      cx = getFloatWithUnit(paramXMLElement, "cx");
      cy = getFloatWithUnit(paramXMLElement, "cy");
      r = getFloatWithUnit(paramXMLElement, "r");
      String str = paramXMLElement.getString("gradientTransform");
      if (str != null)
      {
        float[] arrayOfFloat = parseTransform(str).get(null);
        transform = new AffineTransform(arrayOfFloat[0], arrayOfFloat[3], arrayOfFloat[1], arrayOfFloat[4], arrayOfFloat[2], arrayOfFloat[5]);
        Point2D localPoint2D1 = transform.transform(new Point2D.Float(cx, cy), null);
        Point2D localPoint2D2 = transform.transform(new Point2D.Float(cx + r, cy), null);
        cx = ((float)localPoint2D1.getX());
        cy = ((float)localPoint2D1.getY());
        r = ((float)(localPoint2D2.getX() - localPoint2D1.getX()));
      }
    }
  }
  
  class LinearGradient
    extends PShapeSVG.Gradient
  {
    float x1;
    float y1;
    float x2;
    float y2;
    
    public LinearGradient(PShapeSVG paramPShapeSVG, XMLElement paramXMLElement)
    {
      super(paramXMLElement);
      x1 = getFloatWithUnit(paramXMLElement, "x1");
      y1 = getFloatWithUnit(paramXMLElement, "y1");
      x2 = getFloatWithUnit(paramXMLElement, "x2");
      y2 = getFloatWithUnit(paramXMLElement, "y2");
      String str = paramXMLElement.getString("gradientTransform");
      if (str != null)
      {
        float[] arrayOfFloat = parseTransform(str).get(null);
        transform = new AffineTransform(arrayOfFloat[0], arrayOfFloat[3], arrayOfFloat[1], arrayOfFloat[4], arrayOfFloat[2], arrayOfFloat[5]);
        Point2D localPoint2D1 = transform.transform(new Point2D.Float(x1, y1), null);
        Point2D localPoint2D2 = transform.transform(new Point2D.Float(x2, y2), null);
        x1 = ((float)localPoint2D1.getX());
        y1 = ((float)localPoint2D1.getY());
        x2 = ((float)localPoint2D2.getX());
        y2 = ((float)localPoint2D2.getY());
      }
    }
  }
  
  static class Gradient
    extends PShapeSVG
  {
    AffineTransform transform;
    float[] offset;
    int[] color;
    int count;
    
    public Gradient(PShapeSVG paramPShapeSVG, XMLElement paramXMLElement)
    {
      super(paramXMLElement, true);
      XMLElement[] arrayOfXMLElement = paramXMLElement.getChildren();
      offset = new float[arrayOfXMLElement.length];
      color = new int[arrayOfXMLElement.length];
      for (int i = 0; i < arrayOfXMLElement.length; i++)
      {
        XMLElement localXMLElement = arrayOfXMLElement[i];
        String str1 = localXMLElement.getName();
        if (str1.equals("stop"))
        {
          String str2 = localXMLElement.getString("offset");
          float f = 1.0F;
          if (str2.endsWith("%"))
          {
            f = 100.0F;
            str2 = str2.substring(0, str2.length() - 1);
          }
          offset[count] = (PApplet.parseFloat(str2) / f);
          String str3 = localXMLElement.getString("style");
          HashMap localHashMap = parseStyleAttributes(str3);
          String str4 = (String)localHashMap.get("stop-color");
          if (str4 == null) {
            str4 = "#000000";
          }
          String str5 = (String)localHashMap.get("stop-opacity");
          if (str5 == null) {
            str5 = "1";
          }
          int j = (int)(PApplet.parseFloat(str5) * 255.0F);
          color[count] = (j << 24 | Integer.parseInt(str4.substring(1), 16));
          count += 1;
        }
      }
      offset = PApplet.subset(offset, 0, count);
      color = PApplet.subset(color, 0, count);
    }
  }
}
