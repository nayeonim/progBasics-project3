package model;

/**
 * Represents an answer option containing a string value of the option.
 * @author stefanieim
 */
public class AnsOptImg extends AnsOptString implements AnsOpt {
  private String answerImageUrl;
  
  /**
   * Constructs an AnsOptImg object with the given answer option string, and the image url.
   * @param aString the string value for the answer content
   * @param imageUrl the string value for the image's url.
   */
  public AnsOptImg(String aString, String imageUrl) {
    super(aString);
    this.answerImageUrl = imageUrl;
  }
  
  @Override
  public String getAnsOptImageUrl() throws IllegalStateException {
    return this.answerImageUrl;
  }

}
