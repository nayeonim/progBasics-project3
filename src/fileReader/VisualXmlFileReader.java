package fileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import main.Processing;
import model.AnsOpt;
import model.AnsOptString;
import model.AnsOptImg;
import model.QuestionSet;
import model.QuestionSetSimple;
import model.QuestionSetVisual;

/**
 * Represents a visual XML file reader, which also reads the visual information (image urls) 
 * of an XML file containing data about all the questions to be used in the Trivia Game.
 * @author stefanieim
 */
public class VisualXmlFileReader extends XmlFileReader {
  private String qImageUrl;
  private Map<Integer, String> answerImageUrls;


  /**
   * Constructs a VisualXmlFileReader, also reading the image urls, as well as 
   * the text/number-based information about the question sets to be used.
   */
  public VisualXmlFileReader() {
    super();
    this.answerImageUrls = new HashMap<>();
  }

  @Override
  public void go() {
    super.go();
    System.out.println("visual go called");
    try {
      //for all the questionSets
      for (int i = 0; i < xmlQuestionSetList.getLength(); i++) {
        Node node = xmlQuestionSetList.item(i); 
        if (node.getNodeType() == Node.ELEMENT_NODE) {  
          Element qSet = (Element) node; 

          //question image
          this.qImageUrl = qSet.getElementsByTagName("qImg").item(0).getTextContent();

          //answer images
          List<String> answerImageUrlList = new ArrayList<>();

          int numOfAnswerImages = qSet.getElementsByTagName("aImg").getLength();
          NodeList aObjectsList = qSet.getElementsByTagName("aImg");

          int aOidWeAreLookingFor = 0;
          int jj = 0;   //the answer Option we are on while parsing ()

          //sorting unordered aO elements
          while (jj <= numOfAnswerImages - 1) {
            Node aO = aObjectsList.item(jj); //starting from 0(jj)th aObject       
            String aOid = aO.getAttributes().item(0).getTextContent();
            if (aOid.equals(Integer.toString(aOidWeAreLookingFor))) {
              answerImageUrlList.add(aOidWeAreLookingFor, aO.getTextContent());
              aOidWeAreLookingFor++;//go to the next id to look for
            }
            else {
              if (jj == numOfAnswerImages - 1 && answerImageUrlList.size() < numOfAnswerImages) {
                jj = 0; //reset the index of element if we have parsed through til last element and the size has still
              }
              else {
                jj++;
              }
            }
          }
          //add all list items to a map
          Map<Integer, String> mapOfAnsImageUrls = new HashMap<>();
          for (int m = 0; m < answerImageUrlList.size(); m++) {
            String strToAdd = answerImageUrlList.get(m);
            mapOfAnsImageUrls.put(m, strToAdd);
          }
          this.answerImageUrls = mapOfAnsImageUrls;

          convertQuestionSetSimpleToVisual(i, this.qImageUrl, this.answerImageUrls);
        }
      }
    }
    catch (Exception e) {
      System.out.println("ERROR! error converting question set IMAGES");
    }
  }

  /**
   * Converts each QuestionSet object generated from the super class' go() method, 
   * from a QuestionSetSimple object to a QuestionSetVisual object, with the question image url and 
   * answer option images added. In order to add the answer option image urls, 
   * it also converts the original AnsOptString object to an AnsOptImg object.
   * @param idx the index of the question set to convert
   * @param qImageUrl the string url for the question image
   * @param answerImageUrls a map containing the answer image urls for each answer option index
   */
  private void convertQuestionSetSimpleToVisual(int idx, String qImageUrl, Map<Integer, String> answerImageUrls) {
    QuestionSet orig = super.convertedQuestionSets.get(idx); //simple

    int newId = orig.getId();
    String newQuestionString = orig.getQString();
    Map<Integer, AnsOpt> newMapOfAnsOpts = new HashMap<>();
    
    //replace old AnsOpt with new AnsOpts (image url added)
    for (int i = 0; i < orig.getMapOfAnsOpts().size(); i++) {
      AnsOpt origAO = orig.getMapOfAnsOpts().get(i);
      String aOImageUrl = answerImageUrls.get(i);
      AnsOpt newAO = new AnsOptImg(origAO.getAnsOptString(), aOImageUrl);
      newMapOfAnsOpts.put(i, newAO);
    }
    
    //new QuestionSetVisual
    QuestionSet newVisualQuestionSet = new QuestionSetVisual(newId, newQuestionString, newMapOfAnsOpts, orig.getCorrectAnsOptIdx(), qImageUrl);
    super.convertedQuestionSets.put(idx, newVisualQuestionSet);
  }



  @Override
  public void consolePrintConvertedQuestionSets() {
    System.out.println("visual consoleprint called");
    
    for (int i = 0; i < super.convertedQuestionSets.size(); i++) {
      QuestionSet qSet = super.convertedQuestionSets.get(i);

      if (qSet instanceof QuestionSetVisual) {
        QuestionSetVisual qSetVisual = (QuestionSetVisual)super.convertedQuestionSets.get(i);
        System.out.println("ID: " + qSetVisual.getId());
        System.out.println("Question: " + qSetVisual.getQString());
        System.out.println("QuestionIMAGE: " + qSetVisual.getQuestionImageUrl());


        //print out all the answerStrings in order
        for (int n = 0; n < qSetVisual.getMapOfAnsOpts().size(); n++) {
          AnsOpt aOpt = qSetVisual.getMapOfAnsOpts().get(n);
          if (aOpt instanceof AnsOptImg) {
            AnsOptImg aOptImg = (AnsOptImg) aOpt;
            System.out.print(Processing.ALPHABET[n] + ": " + aOptImg.getAnsOptString());
            System.out.println(" (image: " + aOptImg.getAnsOptImageUrl()+ ")");
          }
        }
        System.out.println("CORRECT: " + Processing.ALPHABET[qSetVisual.getCorrectAnsOptIdx()]);
        System.out.println();
      }
    }
  }

}
