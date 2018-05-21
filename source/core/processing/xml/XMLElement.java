package processing.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Vector;
import processing.core.PApplet;

public class XMLElement
  implements Serializable
{
  public static final int NO_LINE = -1;
  private PApplet sketch;
  private XMLElement parent;
  private Vector<XMLAttribute> attributes = new Vector();
  private Vector<XMLElement> children = new Vector(8);
  private String name;
  private String fullName;
  private String namespace;
  private String content;
  private String systemID;
  private int line;
  
  public XMLElement()
  {
    this(null, null, null, -1);
  }
  
  public XMLElement(String paramString)
  {
    this(paramString, null, null, -1);
  }
  
  public XMLElement(String paramString1, String paramString2, String paramString3, int paramInt)
  {
    fullName = paramString1;
    if (paramString2 == null)
    {
      name = paramString1;
    }
    else
    {
      int i = paramString1.indexOf(':');
      if (i >= 0) {
        name = paramString1.substring(i + 1);
      } else {
        name = paramString1;
      }
    }
    namespace = paramString2;
    content = null;
    line = paramInt;
    systemID = paramString3;
    parent = null;
  }
  
  public XMLElement(PApplet paramPApplet, String paramString)
  {
    this();
    sketch = paramPApplet;
    init(paramPApplet.createReader(paramString));
  }
  
  public XMLElement(Reader paramReader)
  {
    this();
    init(paramReader);
  }
  
  public static XMLElement parse(String paramString)
  {
    return parse(new StringReader(paramString));
  }
  
  public static XMLElement parse(Reader paramReader)
  {
    try
    {
      StdXMLParser localStdXMLParser = new StdXMLParser();
      localStdXMLParser.setBuilder(new StdXMLBuilder());
      localStdXMLParser.setValidator(new XMLValidator());
      localStdXMLParser.setReader(new StdXMLReader(paramReader));
      return (XMLElement)localStdXMLParser.parse();
    }
    catch (XMLException localXMLException)
    {
      localXMLException.printStackTrace();
    }
    return null;
  }
  
  protected void init(String paramString1, String paramString2, String paramString3, int paramInt)
  {
    fullName = paramString1;
    if (paramString2 == null)
    {
      name = paramString1;
    }
    else
    {
      int i = paramString1.indexOf(':');
      if (i >= 0) {
        name = paramString1.substring(i + 1);
      } else {
        name = paramString1;
      }
    }
    namespace = paramString2;
    line = paramInt;
    systemID = paramString3;
  }
  
  protected void init(Reader paramReader)
  {
    try
    {
      StdXMLParser localStdXMLParser = new StdXMLParser();
      localStdXMLParser.setBuilder(new StdXMLBuilder(this));
      localStdXMLParser.setValidator(new XMLValidator());
      localStdXMLParser.setReader(new StdXMLReader(paramReader));
      localStdXMLParser.parse();
    }
    catch (XMLException localXMLException)
    {
      localXMLException.printStackTrace();
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    attributes.clear();
    attributes = null;
    children = null;
    fullName = null;
    name = null;
    namespace = null;
    content = null;
    systemID = null;
    parent = null;
    super.finalize();
  }
  
  public XMLElement getParent()
  {
    return parent;
  }
  
  public String getName()
  {
    return fullName;
  }
  
  public String getLocalName()
  {
    return name;
  }
  
  public String getNamespace()
  {
    return namespace;
  }
  
  public void setName(String paramString)
  {
    name = paramString;
    fullName = paramString;
    namespace = null;
  }
  
  public void setName(String paramString1, String paramString2)
  {
    int i = paramString1.indexOf(':');
    if ((paramString2 == null) || (i < 0)) {
      name = paramString1;
    } else {
      name = paramString1.substring(i + 1);
    }
    fullName = paramString1;
    namespace = paramString2;
  }
  
  public void addChild(XMLElement paramXMLElement)
  {
    if (paramXMLElement == null) {
      throw new IllegalArgumentException("child must not be null");
    }
    if ((paramXMLElement.getLocalName() == null) && (!children.isEmpty()))
    {
      XMLElement localXMLElement = (XMLElement)children.lastElement();
      if (localXMLElement.getLocalName() == null)
      {
        localXMLElement.setContent(localXMLElement.getContent() + paramXMLElement.getContent());
        return;
      }
    }
    parent = this;
    children.addElement(paramXMLElement);
  }
  
  public void insertChild(XMLElement paramXMLElement, int paramInt)
  {
    if (paramXMLElement == null) {
      throw new IllegalArgumentException("child must not be null");
    }
    if ((paramXMLElement.getLocalName() == null) && (!children.isEmpty()))
    {
      XMLElement localXMLElement = (XMLElement)children.lastElement();
      if (localXMLElement.getLocalName() == null)
      {
        localXMLElement.setContent(localXMLElement.getContent() + paramXMLElement.getContent());
        return;
      }
    }
    parent = this;
    children.insertElementAt(paramXMLElement, paramInt);
  }
  
  public void removeChild(XMLElement paramXMLElement)
  {
    if (paramXMLElement == null) {
      throw new IllegalArgumentException("child must not be null");
    }
    children.removeElement(paramXMLElement);
  }
  
  public void removeChild(int paramInt)
  {
    children.removeElementAt(paramInt);
  }
  
  public boolean isLeaf()
  {
    return children.isEmpty();
  }
  
  public boolean hasChildren()
  {
    return !children.isEmpty();
  }
  
  public int getChildCount()
  {
    return children.size();
  }
  
  public String[] listChildren()
  {
    int i = getChildCount();
    String[] arrayOfString = new String[i];
    for (int j = 0; j < i; j++) {
      arrayOfString[j] = getChild(j).getName();
    }
    return arrayOfString;
  }
  
  public XMLElement[] getChildren()
  {
    int i = getChildCount();
    XMLElement[] arrayOfXMLElement = new XMLElement[i];
    children.copyInto(arrayOfXMLElement);
    return arrayOfXMLElement;
  }
  
  public XMLElement getChild(int paramInt)
  {
    return (XMLElement)children.elementAt(paramInt);
  }
  
  public XMLElement getChild(String paramString)
  {
    if (paramString.indexOf('/') != -1) {
      return getChildRecursive(PApplet.split(paramString, '/'), 0);
    }
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      XMLElement localXMLElement = getChild(j);
      String str = localXMLElement.getName();
      if ((str != null) && (str.equals(paramString))) {
        return localXMLElement;
      }
    }
    return null;
  }
  
  protected XMLElement getChildRecursive(String[] paramArrayOfString, int paramInt)
  {
    if (Character.isDigit(paramArrayOfString[paramInt].charAt(0)))
    {
      XMLElement localXMLElement1 = getChild(Integer.parseInt(paramArrayOfString[paramInt]));
      if (paramInt == paramArrayOfString.length - 1) {
        return localXMLElement1;
      }
      return localXMLElement1.getChildRecursive(paramArrayOfString, paramInt + 1);
    }
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      XMLElement localXMLElement2 = getChild(j);
      String str = localXMLElement2.getName();
      if ((str != null) && (str.equals(paramArrayOfString[paramInt])))
      {
        if (paramInt == paramArrayOfString.length - 1) {
          return localXMLElement2;
        }
        return localXMLElement2.getChildRecursive(paramArrayOfString, paramInt + 1);
      }
    }
    return null;
  }
  
  public XMLElement[] getChildren(String paramString)
  {
    if (paramString.indexOf('/') != -1) {
      return getChildrenRecursive(PApplet.split(paramString, '/'), 0);
    }
    if (Character.isDigit(paramString.charAt(0))) {
      return new XMLElement[] { getChild(Integer.parseInt(paramString)) };
    }
    int i = getChildCount();
    XMLElement[] arrayOfXMLElement = new XMLElement[i];
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      XMLElement localXMLElement = getChild(k);
      String str = localXMLElement.getName();
      if ((str != null) && (str.equals(paramString))) {
        arrayOfXMLElement[(j++)] = localXMLElement;
      }
    }
    return (XMLElement[])PApplet.subset(arrayOfXMLElement, 0, j);
  }
  
  protected XMLElement[] getChildrenRecursive(String[] paramArrayOfString, int paramInt)
  {
    if (paramInt == paramArrayOfString.length - 1) {
      return getChildren(paramArrayOfString[paramInt]);
    }
    XMLElement[] arrayOfXMLElement1 = getChildren(paramArrayOfString[paramInt]);
    XMLElement[] arrayOfXMLElement2 = new XMLElement[0];
    for (int i = 0; i < arrayOfXMLElement1.length; i++)
    {
      XMLElement[] arrayOfXMLElement3 = arrayOfXMLElement1[i].getChildrenRecursive(paramArrayOfString, paramInt + 1);
      arrayOfXMLElement2 = (XMLElement[])PApplet.concat(arrayOfXMLElement2, arrayOfXMLElement3);
    }
    return arrayOfXMLElement2;
  }
  
  private XMLAttribute findAttribute(String paramString)
  {
    Enumeration localEnumeration = attributes.elements();
    while (localEnumeration.hasMoreElements())
    {
      XMLAttribute localXMLAttribute = (XMLAttribute)localEnumeration.nextElement();
      if (localXMLAttribute.getName().equals(paramString)) {
        return localXMLAttribute;
      }
    }
    return null;
  }
  
  public int getAttributeCount()
  {
    return attributes.size();
  }
  
  public String[] listAttributes()
  {
    String[] arrayOfString = new String[attributes.size()];
    for (int i = 0; i < attributes.size(); i++) {
      arrayOfString[i] = ((XMLAttribute)attributes.get(i)).getName();
    }
    return arrayOfString;
  }
  
  /**
   * @deprecated
   */
  public String getStringAttribute(String paramString)
  {
    return getString(paramString);
  }
  
  /**
   * @deprecated
   */
  public String getStringAttribute(String paramString1, String paramString2)
  {
    return getString(paramString1, paramString2);
  }
  
  public String getString(String paramString)
  {
    return getString(paramString, null);
  }
  
  public String getString(String paramString1, String paramString2)
  {
    XMLAttribute localXMLAttribute = findAttribute(paramString1);
    if (localXMLAttribute == null) {
      return paramString2;
    }
    return localXMLAttribute.getValue();
  }
  
  public boolean getBoolean(String paramString)
  {
    return getBoolean(paramString, false);
  }
  
  public boolean getBoolean(String paramString, boolean paramBoolean)
  {
    String str = getString(paramString);
    if (str == null) {
      return paramBoolean;
    }
    return (str.equals("1")) || (str.toLowerCase().equals("true"));
  }
  
  /**
   * @deprecated
   */
  public int getIntAttribute(String paramString)
  {
    return getInt(paramString, 0);
  }
  
  /**
   * @deprecated
   */
  public int getIntAttribute(String paramString, int paramInt)
  {
    return getInt(paramString, paramInt);
  }
  
  public int getInt(String paramString)
  {
    return getInt(paramString, 0);
  }
  
  public int getInt(String paramString, int paramInt)
  {
    String str = getString(paramString);
    return str == null ? paramInt : PApplet.parseInt(str, paramInt);
  }
  
  /**
   * @deprecated
   */
  public float getFloatAttribute(String paramString)
  {
    return getFloat(paramString, 0.0F);
  }
  
  /**
   * @deprecated
   */
  public float getFloatAttribute(String paramString, float paramFloat)
  {
    return getFloat(paramString, 0.0F);
  }
  
  public float getFloat(String paramString)
  {
    return getFloat(paramString, 0.0F);
  }
  
  public float getFloat(String paramString, float paramFloat)
  {
    String str = getString(paramString);
    if (str == null) {
      return paramFloat;
    }
    return PApplet.parseFloat(str, paramFloat);
  }
  
  public double getDouble(String paramString)
  {
    return getDouble(paramString, 0.0D);
  }
  
  public double getDouble(String paramString, double paramDouble)
  {
    String str = getString(paramString);
    return str == null ? paramDouble : Double.parseDouble(str);
  }
  
  public void setString(String paramString1, String paramString2)
  {
    XMLAttribute localXMLAttribute = findAttribute(paramString1);
    if (localXMLAttribute == null)
    {
      localXMLAttribute = new XMLAttribute(paramString1, paramString1, null, paramString2, "CDATA");
      attributes.addElement(localXMLAttribute);
    }
    else
    {
      localXMLAttribute.setValue(paramString2);
    }
  }
  
  public void setBoolean(String paramString, boolean paramBoolean)
  {
    setString(paramString, String.valueOf(paramBoolean));
  }
  
  public void setInt(String paramString, int paramInt)
  {
    setString(paramString, String.valueOf(paramInt));
  }
  
  public void setFloat(String paramString, float paramFloat)
  {
    setString(paramString, String.valueOf(paramFloat));
  }
  
  public void setDouble(String paramString, double paramDouble)
  {
    setString(paramString, String.valueOf(paramDouble));
  }
  
  public void remove(String paramString)
  {
    for (int i = 0; i < attributes.size(); i++)
    {
      XMLAttribute localXMLAttribute = (XMLAttribute)attributes.elementAt(i);
      if (localXMLAttribute.getName().equals(paramString))
      {
        attributes.removeElementAt(i);
        return;
      }
    }
  }
  
  public boolean hasAttribute(String paramString)
  {
    return findAttribute(paramString) != null;
  }
  
  public String getSystemID()
  {
    return systemID;
  }
  
  public int getLine()
  {
    return line;
  }
  
  public String getContent()
  {
    return content;
  }
  
  public void setContent(String paramString)
  {
    content = paramString;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof XMLElement)) {
      return false;
    }
    XMLElement localXMLElement1 = (XMLElement)paramObject;
    if (!name.equals(localXMLElement1.getLocalName())) {
      return false;
    }
    if (attributes.size() != localXMLElement1.getAttributeCount()) {
      return false;
    }
    Enumeration localEnumeration = attributes.elements();
    Object localObject;
    while (localEnumeration.hasMoreElements())
    {
      XMLAttribute localXMLAttribute = (XMLAttribute)localEnumeration.nextElement();
      if (!localXMLElement1.hasAttribute(localXMLAttribute.getName())) {
        return false;
      }
      localObject = localXMLElement1.getString(localXMLAttribute.getName(), null);
      if (!localXMLAttribute.getValue().equals(localObject)) {
        return false;
      }
    }
    if (children.size() != localXMLElement1.getChildCount()) {
      return false;
    }
    for (int i = 0; i < children.size(); i++)
    {
      localObject = getChild(i);
      XMLElement localXMLElement2 = localXMLElement1.getChild(i);
      if (!((XMLElement)localObject).equals(localXMLElement2)) {
        return false;
      }
    }
    return true;
  }
  
  public String toString()
  {
    return toString(true);
  }
  
  public String toString(boolean paramBoolean)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(localByteArrayOutputStream);
    XMLWriter localXMLWriter = new XMLWriter(localOutputStreamWriter);
    try
    {
      localXMLWriter.write(this, paramBoolean);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return localByteArrayOutputStream.toString();
  }
  
  private PApplet findSketch()
  {
    if (sketch != null) {
      return sketch;
    }
    if (parent != null) {
      return parent.findSketch();
    }
    return null;
  }
  
  public boolean save(String paramString)
  {
    if (sketch == null) {
      sketch = findSketch();
    }
    if (sketch == null)
    {
      System.err.println("save() can only be used on elements loaded by a sketch");
      throw new RuntimeException("no sketch found, use write(PrintWriter) instead.");
    }
    return write(sketch.createWriter(paramString));
  }
  
  public boolean write(PrintWriter paramPrintWriter)
  {
    paramPrintWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    XMLWriter localXMLWriter = new XMLWriter(paramPrintWriter);
    try
    {
      localXMLWriter.write(this, true);
      paramPrintWriter.flush();
      return true;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return false;
  }
}
