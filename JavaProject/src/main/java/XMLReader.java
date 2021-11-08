import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader {

    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException, XPathExpressionException {

        // create the factory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try{
            // create a new document builder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // parse a document -- make sure the file is located on root level
            Document document = builder.parse("src/main/resources/Final_schema_XML.xml");
            // get elements of the document
            NodeList speciesNodeList = document.getElementsByTagName("Species");
            // parse the node

            // create species List
            List<Species> speciesList= new ArrayList<>();

            for(int i = 0; i < speciesNodeList.getLength(); i++){

                System.out.println("Node names: " + speciesNodeList.item(i).getNodeName());

                // get the child nodes
                NodeList children = speciesNodeList.item(i).getChildNodes();

                // create new species object
                Species species = new Species();

                for(int j = 0; j < children.getLength(); j++) {

                    // drop the whitespace text nodes => code one
                    if(children.item(j).getNodeType() != 3){

                        switch (children.item(j).getNodeName()) {
                            case "ID":
                                species.setID(children.item(j).getTextContent());
                                break;
                            case "Provenance":
                                species.setProvenance(children.item(j).getTextContent());
                                break;
                            case "Scientific_Names":
                                // call function and give it a NodeList which returns a List of String
                                species.setScientific_Names(getTextOfChildren(children.item(j).getChildNodes()));
                                break;
                            case "Common_Names":
                                // call function and give it a NodeList which returns a List of String
                                species.setCommon_Names(getTextOfChildren(children.item(j).getChildNodes()));
                                break;
                            case "Labels":
                                // call function and give it a NodeList which returns a List of String
                                species.setLabels(getTextOfChildren(children.item(j).getChildNodes()));
                                break;
                            case "Where_Listed_l":
                                // call function and give it a NodeList which returns a List of String
                                species.setWhere_Listed(getTextOfChildren(children.item(j).getChildNodes()));
                                break;
                            case "Different_From_l":
                                // call function and give it a NodeList which returns a List of String
                                species.setDifferent_From_l(getTextOfChildren(children.item(j).getChildNodes()));
                                break;
                            case "Endemic_To_l":
                                // call function and give it a NodeList which returns a List of String
                                species.setEndemic_To_l(getTextOfChildren(children.item(j).getChildNodes()));
                                break;
                            case "Regions":
                                // call function and give it a NodeList which returns a List of String
                                species.setRegions(getTextOfChildren(children.item(j).getChildNodes()));
                                break;
                            case "Region_Names":
                                // call function and give it a NodeList which returns a List of String
                                species.setRegion_Names(getTextOfChildren(children.item(j).getChildNodes()));
                                break;
                            case "Esa_Listing_Statuses":
                                // call function and give it a NodeList which returns a List of String
                                species.setEsa_Listing_Statuses(getTextOfChildren(children.item(j).getChildNodes()));
                                break;
                            case "Conversation_Status":
                                species.setConversation_Status(children.item(j).getTextContent());
                                break;
                            case "Categories":
                                // call function and give it a NodeList which returns a List of String
                                species.setCategories(getTextOfChildren(children.item(j).getChildNodes()));
                                break;
                            case "Orders":
                                // call function and give it a NodeList which returns a List of String
                                species.setOrders(getTextOfChildren(children.item(j).getChildNodes()));
                                break;
                            case "Families":
                                // call function and give it a NodeList which returns a List of String
                                species.setFamilies(getTextOfChildren(children.item(j).getChildNodes()));
                                break;
                        }
                    }
                }
                // add the species to the list
                speciesList.add(species);
            }
        } catch(SAXException | IOException | ParserConfigurationException exception) {
            exception.printStackTrace();
        }
    }

    // create a list to store non whitespace nodes
    public static List<String> getTextOfChildren(NodeList nList){

        // instantiate returning list
        List<String> arrayList = new ArrayList<>();

        // loop through node list
        for(int k = 1; k < nList.getLength(); k++){

            // add textContent of child to the list if it is not a whitespace node
            if(nList.item(k).getNodeType() != 3 && nList.item(k).getTextContent().isEmpty()) {
                // add item to the list if it's not a whitespace node
                arrayList.add(nList.item(k).getTextContent());
            }
        }
        if(arrayList == null || arrayList.isEmpty()){
            return null;
        } else {
            return arrayList;
        }

    }
}