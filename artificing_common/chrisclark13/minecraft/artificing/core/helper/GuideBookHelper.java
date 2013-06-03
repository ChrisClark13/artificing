package chrisclark13.minecraft.artificing.core.helper;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.minecraft.client.gui.Gui;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import chrisclark13.minecraft.artificing.client.gui.inventory.guidepages.GuiGuidePage;
import chrisclark13.minecraft.artificing.core.helper.LogHelper;

public class GuideBookHelper {
    
    private static final String JAXP_SCHEMA_LANGUAGE =
            "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_XML_SCHEMA =
            "http://www.w3.org/2001/XMLSchema";
    private static final String JAXP_SCHEMA_SOURCE =
            "http://java.sun.com/xml/jaxp/properties/schemaSource";
    
    private GuideBookHelper() {
    }
    
    public static ArrayList<GuiGuidePage> getPagesFromXML(String xmlPath) {
        ArrayList<GuiGuidePage> pages = new ArrayList<>();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setNamespaceAware(true);
//        factory.setValidating(true);
//        factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
//        factory.setAttribute(JAXP_SCHEMA_SOURCE, new File("/mods/Artificing/manuals/guideBookSchema.xsd"));
        
        try {
            InputStream stream = GuideBookHelper.class.getResourceAsStream(xmlPath);
            DocumentBuilder db = factory.newDocumentBuilder();
            db.setErrorHandler(new GuideBookErrorHandler());
            Document doc = db.parse(stream);
            
            Element bookElement = doc.getDocumentElement();
            NodeList list = bookElement.getElementsByTagName("page");
            
            for (int i = 0; i < list.getLength(); i++) {
                pages.add(parsePageElement((Element) list.item(i)));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return pages;
    }
    
    private static GuiGuidePage parsePageElement(Element pageElement) {
        GuiGuidePage page = new GuiGuidePage();
        
        NodeList list = pageElement.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;
                if (e.getTagName().equals("text")) {
                    int x = Integer.parseInt(e.getAttribute("x"));
                    int y = Integer.parseInt(e.getAttribute("y"));
                    int lineWidth = GuiGuidePage.PAGE_WIDTH;
                    if (e.hasAttribute("lineWidth")) {
                        lineWidth = Integer.parseInt(e.getAttribute("lineWidth"));
                    }
                    
                    StringBuilder sb = new StringBuilder();
                    NodeList textList = e.getChildNodes();
                    
                    for (int j = 0; j < textList.getLength(); j++) {
                        Node textNode = textList.item(j);
                        if (textNode.getNodeType() == Node.TEXT_NODE) {
                            sb.append(((Text) textNode).getData());
                        } else if (textNode.getNodeType() == Node.ELEMENT_NODE) {
                            sb.append(getFormattingCodeFromElement((Element) textNode));
                        }
                    }
                    
                    page.addTextContent(x, y, lineWidth, sb.toString());
                } else if (e.getTagName().equals("image")) {
                    int x = Integer.parseInt(e.getAttribute("x"));
                    int y = Integer.parseInt(e.getAttribute("y"));
                    int width = Integer.parseInt(e.getAttribute("width"));
                    int height = Integer.parseInt(e.getAttribute("height"));
                    String imagePath = e.getTextContent();
                    
                    page.addImageContent(x, y, width, height, imagePath);
                }
            }
        }
        
        return page;
    }
    
    private static String getFormattingCodeFromElement(Element e) {
        
        switch (e.getTagName()) {
            case "black":
                return "�0";
            case "darkBlue":
                return "�1";
            case "darkGreen":
                return "�2";
            case "darkAqua":
                return "�3";
            case "darkRed":
                return "�4";
            case "purple":
                return "�5";
            case "gold":
                return "�6";
            case "gray":
                return "�7";
            case "darkGray":
                return "�8";
            case "blue":
                return "�9";
            case "green":
                return "�a";
            case "aqua":
                return "�b";
            case "red":
                return "�c";
            case "lightPurple":
                return "�d";
            case "yellow":
                return "�e";
            case "white":
                return "�f";
            case "random":
                return "�k";
            case "bold":
                return "�l";
            case "strikethrough":
                return "�m";
            case "underline":
                return "�n";
            case "italic":
                return "�o";
            case "reset":
                return "�r";
            default:
                return "";
        }
    }
    
    private static class GuideBookErrorHandler implements ErrorHandler {

        @Override
        public void error(SAXParseException exception) throws SAXException {
            LogHelper.log(Level.WARNING, "[XML Validation Error] " + exception.getMessage());
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            LogHelper.log(Level.SEVERE, "[XML Validation Fatal Error] " + exception.getMessage());
            
        }

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            LogHelper.log(Level.WARNING, "[XML Validation Warning] " + exception.getMessage());
        }
        
    }
}
