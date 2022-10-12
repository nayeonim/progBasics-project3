package model;

/**
 * Represents an answer option containing a string value of the option.
 * @author stefanieim
 */
public class AnsOptString implements AnsOpt {
  private String answerString;
  
  /**
   * Constructs an AnsOptString object with the given string, and sets this 
   * string as the answer content string.
   * @param aString the string to set the answer to
   */
  public AnsOptString(String aString) {
    this.answerString = aString;
  }
  
  @Override
  public String getAnsOptString() {
    return this.answerString;
  }

  @Override
  public String getAnsOptImageUrl() throws IllegalStateException {
    throw new IllegalStateException("AnsOptString does not have an image url");
  }
  
}
