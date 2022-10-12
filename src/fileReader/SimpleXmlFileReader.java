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
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;  
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import main.Processing;
import model.AnsOpt;
import model.AnsOptString;
import model.QuestionSet;
import model.QuestionSetSimple;



/**
 * Represents a simple XML file reader, which only reads the non-visual information of an XML file 
 * containing data about all the questions to be used in the Trivia Game. 
 * @author stefanieim
 */
public class SimpleXmlFileReader extends XmlFileReader {


  /**
   * Constructs a SimpleXmlFileReader, only reading the 
   * text/number-based information about the question sets to be used.
   */
  public SimpleXmlFileReader() { 
    super();
  }

  @Override
  public void go() {
    System.out.println("simple go called");
    super.go();
  }
  
  @Override
  public void consolePrintConvertedQuestionSets() {
    for (int i = 0; i < convertedQuestionSets.size(); i++) {
      QuestionSet qSet = convertedQuestionSets.get(i);

      System.out.println("ID : " + qSet.getId());
      System.out.println("Question : " + qSet.getQString());

      //print out all the answerStrings in order
      for (int n = 0; n < qSet.getMapOfAnsOpts().size(); n++) {
        AnsOpt aOpt = qSet.getMapOfAnsOpts().get(n);
        System.out.println(Processing.ALPHABET[n] + " : " + aOpt.getAnsOptString());
      }

      System.out.println("CORRECT : " + Processing.ALPHABET[qSet.getCorrectAnsOptIdx()]);
      System.out.println();
    }
  }

}






