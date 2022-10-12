package fileReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import main.Processing;
import model.AnsOpt;
import model.AnsOptString;
import model.QuestionSet;
import model.QuestionSetSimple;

/**
 * Represents a file reader for xml documents that contain the questions for playing the Trivia game.
 * This is an abstract class that contians methods applicable to both Simple and Visual game modes,
 * reading all information except for images.
 * @author stefanieim
 *
 */
public abstract class XmlFileReader {
  private File file;
  private DocumentBuilderFactory dbf;
  private DocumentBuilder db;
  private Document doc;
  protected NodeList xmlQuestionSetList;
  
  //the resulting map of ALL question sets
  protected Map<Integer, QuestionSet> convertedQuestionSets;
 
  
  /**
   * Constructs an XmlFileReader that finds the file and sets up the document elements 
   * ready for parsing. If the file cannot be found or set up, it prints an error message 
   * to the console. Also instantiates the map that will contain the converted question sets
   * (convertedQuestionSets) with a new hashmap.
   */
  public XmlFileReader() { 
    try {
      this.findFile();
      this.setUpDocElements();
    }
    catch (Exception e) {
      System.out.println("ERROR! File could not be set up");
      return;
    } 
    this.convertedQuestionSets = new HashMap<>();
  }

  /**
   * Finds a file in the src/resources package.
   */
  public void findFile() {
    try {
      file = new File("src/resources/" + Processing.FILE_NAME + ".xml"); 
    }
    catch (Exception e) {
      System.out.println("ERROR! File not found");
      return;
    }
  }
  
  /**
   * Sets up the documents, ready for parsing, by creating a new Document builder and a Document
   * with the found file. Then filters all the questionSet elements and saves it to the 
   * xmlQuestionSetList variable.
   */
  public void setUpDocElements() {
    dbf = DocumentBuilderFactory.newInstance();  
    try {
      db = dbf.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      System.out.println("ERROR! Document Builder could not be created");
      return;
    } 
    try {
      doc = db.parse(file);
      doc.getDocumentElement().normalize(); 
    } catch (SAXException | IOException e) {
      System.out.println("ERROR! Document could not be created");
      return;
    }  
    try {
      xmlQuestionSetList = doc.getElementsByTagName("questionSet");
    }
    catch (Exception e) {
      System.out.println("ERROR! questionSet elements could not be found (xml)");
    }
  }
  
  /**
   * Extracts the data from the xml file, by parsing through each of the questionSet elements
   * and looking for the question id, question string, answer options, and the correct answer option.
   * When extracting the information on answer options, this method is able to sort them in order, based on the
   * id attribute given inside the a tag.
   */
  public void go() {
    Map<Integer, QuestionSet> allQuestionSets = new HashMap<>();

    try {
      
      //-----------the following 4 lines of code were referenced from https://mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
      //for all the questionSets..
      for (int i = 0; i < xmlQuestionSetList.getLength(); i++) {
        //type check/casting each node (which resents a questionSet
        Node node = xmlQuestionSetList.item(i); 
        if (node.getNodeType() == Node.ELEMENT_NODE) {  
          Element qSet = (Element) node; 
          //-------
          
          
          int qSetID;
          String questionString;
          List<String> answerStringList = new ArrayList<>();
          int correctAnswer;

          //save the questionSet's id, question string, and the correct answer index.
          String qSetIDStr = qSet.getAttribute("id");
          qSetID = Integer.parseInt(qSetIDStr);
          questionString = qSet.getElementsByTagName("q").item(0).getTextContent();
          String correctAnswerStr = qSet.getElementsByTagName("correct").item(0).getTextContent();
          correctAnswer = Integer.parseInt(correctAnswerStr);
        
          //find all the answer options
          NodeList aObjectsList = qSet.getElementsByTagName("a");
          int numOfAnswerOptions = aObjectsList.getLength();

          //indexes
          int aOidWeAreLookingFor = 0; //the id of the answer option we are looking for
          int jj = 0;   //the answer Option we are on while parsing
          
          //sorting unordered aO elements
          while (jj <= numOfAnswerOptions - 1) {
            Node aO = aObjectsList.item(jj); //starting from 0(jj)th aObject       
            String aOid = aO.getAttributes().item(0).getTextContent(); //get the attribute (id specified inside the a tag)
            if (aOid.equals(Integer.toString(aOidWeAreLookingFor))) {  //if this is the element we are looking for
              answerStringList.add(aOidWeAreLookingFor, aO.getTextContent()); //add this to the list of answer strings
              aOidWeAreLookingFor++; //look for the next id
            }
            else {
              //if we have parsed through all answer options till the last element, and haven't found the matching id
              //(can happen since we don't reset jj after finding the previous id)
              if (jj == numOfAnswerOptions - 1 && answerStringList.size() < numOfAnswerOptions) {
                jj = 0; //go back to the beginning of the answer options list
              }
              else { //we are not on the last element
                jj++; //look at the next element
              }
            }
          }
          //add all list items to a map
          Map<Integer, AnsOpt> mapOfAnsOpts = new HashMap<>();
          for (int m = 0; m < answerStringList.size(); m++) {
            String strToAdd = answerStringList.get(m);
            AnsOpt aOToAdd = new AnsOptString(strToAdd);
            mapOfAnsOpts.put(m, aOToAdd);
          }
          //construct a QuestionSet object with all the necessary information, converted to the right data type/object
          QuestionSet qSetToAdd = new QuestionSetSimple(qSetID, questionString, mapOfAnsOpts, correctAnswer);
          allQuestionSets.put(qSetID, qSetToAdd);
        }
      }
    }
    catch (Exception e) {
      System.out.println("ERROR! error converting question sets");
    }
    //save the resulting map to the final list
    this.convertedQuestionSets = allQuestionSets;
  }
  
  /**
   * Returns the map of converted question sets.
   * If go() has been called during the program, it will be a map of QuestionSet objects that have 
   * extracted data from the XML file.
   * If go() has NOT been called, it will simply be an empty hash map.
   * @return
   */
  public Map<Integer, QuestionSet> getResultingQuestionSetList() {
    return this.convertedQuestionSets;
  }


  /**
   * Prints elements of the converted question sets to the console,
   * in a human readable form.
   */
  public abstract void consolePrintConvertedQuestionSets();
  
}
