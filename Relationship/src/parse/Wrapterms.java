/* Generated By:JavaCC: Do not edit this line. Wrapterms.java */
package parse;

import java.io.*;
import main.*;
import user.*;
import rdf.*;
import map.*;
import graph.*;

public class Wrapterms implements WraptermsConstants {
   public static void main(String args[])  throws Exception {
      Wrapterms firstParser = null;
          MainProcess.body(firstParser);
   }

/*
Grammar (read of user terms):
parseUserTerms        ->  ( elementUserTerm() )*  < EOF > 
element               ->  < TERM > ( < SEPARATORS > | < BLANK > | < TAB > | <  NEW_LINE > )*

Grammar (read of link vocabulary):
parseSystemVocabulary ->  ( line() )*  < EOF >
line                  ->  < TERM > ( < BLANK > | < TAB > )* < ARROW > ( < BLANK > | < TAB > )* < TERM > ( < BLANK > | < TAB > | <  NEW_LINE >  )*

Grammar (read of useless concepts):
parseUserTerms        ->  ( elementUselessConcept() )*  < EOF > 
elementUselessConcept ->  < TERM > ( < SEPARATORS > | < BLANK > | < TAB > | <  NEW_LINE >  )*

*/

// list of user terms, separate for comma or new line 
  final public void parseUserTerms(SetQuerySparql originalSetQuerySparql) throws ParseException {
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TERM:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      elementUserTerm(originalSetQuerySparql);
    }
    jj_consume_token(0);
  }

  final public void elementUserTerm(SetQuerySparql originalSetQuerySparql) throws ParseException {
                                                               Token token; Concept concept;
    token = jj_consume_token(TERM);
                concept = new Concept(token);
                originalSetQuerySparql.insertQuerySparql(concept);
                // copy the concept to static attribute in root class:
                WholeSystem.getConceptsRegister().add(concept);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NEW_LINE:
      case TAB:
      case BLANK:
      case SEPARATORS:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_2;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SEPARATORS:
        jj_consume_token(SEPARATORS);
        break;
      case BLANK:
        jj_consume_token(BLANK);
        break;
      case TAB:
        jj_consume_token(TAB);
        break;
      case NEW_LINE:
        jj_consume_token(NEW_LINE);
        break;
      default:
        jj_la1[2] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

// list of sentences, each one per line 
  final public void parseSystemVocabulary(VocabularyTable vocabularyTable) throws ParseException {
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TERM:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_3;
      }
      line(vocabularyTable);
    }
    jj_consume_token(0);
  }

  final public void line(VocabularyTable vocabularyTable) throws ParseException {
                                              Token tokenLeft; Token tokenRight;
    tokenLeft = jj_consume_token(TERM);
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TAB:
      case BLANK:
        ;
        break;
      default:
        jj_la1[4] = jj_gen;
        break label_4;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BLANK:
        jj_consume_token(BLANK);
        break;
      case TAB:
        jj_consume_token(TAB);
        break;
      default:
        jj_la1[5] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    jj_consume_token(ARROW);
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TAB:
      case BLANK:
        ;
        break;
      default:
        jj_la1[6] = jj_gen;
        break label_5;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BLANK:
        jj_consume_token(BLANK);
        break;
      case TAB:
        jj_consume_token(TAB);
        break;
      default:
        jj_la1[7] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    tokenRight = jj_consume_token(TERM);
                vocabularyTable.put(tokenLeft,tokenRight);
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NEW_LINE:
      case TAB:
      case BLANK:
        ;
        break;
      default:
        jj_la1[8] = jj_gen;
        break label_6;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BLANK:
        jj_consume_token(BLANK);
        break;
      case TAB:
        jj_consume_token(TAB);
        break;
      case NEW_LINE:
        jj_consume_token(NEW_LINE);
        break;
      default:
        jj_la1[9] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

// list of concepts, separate for comma or new line
  final public void parseUselessConcepts(UselessConceptsTable uselessConceptsTable) throws ParseException {
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TERM:
        ;
        break;
      default:
        jj_la1[10] = jj_gen;
        break label_7;
      }
      elementUselessConcept(uselessConceptsTable);
    }
    jj_consume_token(0);
  }

  final public void elementUselessConcept(UselessConceptsTable uselessConceptsTable) throws ParseException {
                                                                         Token token;
    token = jj_consume_token(TERM);
                // copy the useless concept to static attribute in root class:
                WholeSystem.getUselessConceptsTable().insert(token.image.trim());
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NEW_LINE:
      case TAB:
      case BLANK:
      case SEPARATORS:
        ;
        break;
      default:
        jj_la1[11] = jj_gen;
        break label_8;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SEPARATORS:
        jj_consume_token(SEPARATORS);
        break;
      case BLANK:
        jj_consume_token(BLANK);
        break;
      case TAB:
        jj_consume_token(TAB);
        break;
      case NEW_LINE:
        jj_consume_token(NEW_LINE);
        break;
      default:
        jj_la1[12] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

// config file
  final public void parseConfigurations(ConfigTable configTable) throws ParseException {
                                                     Token tokenVar; Token tokenValue; String numberFileUser;
    label_9:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case T_numberFileUser:
      case T_nameUserTermsFile:
      case T_minIterationToVerifyUniqueConnectedComponent:
      case T_minIterationToVerifyRelationshipBetweenOriginalConcepts:
      case T_maxIteration:
      case T_additionNewConceptWithoutCategory:
      case T_kCoreFilter:
      case T_quantityNodesToApplyKcoreFilter:
      case T_nDegreeFilter:
      case T_iterationTriggerApplyNDegreeFilterAlgorithm:
      case T_quantityNodesToApplyNdegreeFilter:
      case T_conceptsQuantityCalculationFactor:
      case T_conceptsMinMaxRange:
      case T_proporcionBetweenness:
      case T_proporcionBetweennessCloseness:
      case T_precisionBetweennessCloseness:
      case T_maxBetweennessCloseness:
      case T_proporcionEigenvector:
      case T_precisionEigenvector:
      case T_maxEigenvector:
      case T_isBetweennessCloseness:
      case T_isEigenvector:
      case T_isSelected:
      case T_isKeepNeighborsOfOriginalConcepts:
      case T_quantityNodesShortReport:
      case T_backGroundcolorOriginalConcept:
      case T_borderThicknessConceptWithHint:
      case T_nameGexfGraphFile:
      case T_nameQueryDefaultFile:
      case T_nameVocabularyFile:
      case T_nameUselessConceptsFile:
      case T_nameTxtConceptMapFile:
      case T_nameCxlConceptMapFile:
      case T_nameCompleteReportFile:
      case T_nameShortReportFile:
      case T_nameConsoleReportFile:
      case T_nameConsoleErrorFile:
      case T_dirRdfsPersistenceFiles:
      case T_dbpediaServer:
      case T_gephiVisualization:
      case T_graphStreamVisualization:
      case T_isFixBugInGephiToolKit:
      case T_originalConceptWithGephiToolKitBug:
      case T_isEnableUselessTable:
      case NEW_LINE:
      case TAB:
      case BLANK:
        ;
        break;
      default:
        jj_la1[13] = jj_gen;
        break label_9;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NEW_LINE:
        jj_consume_token(NEW_LINE);
        break;
      case TAB:
        jj_consume_token(TAB);
        break;
      case BLANK:
        jj_consume_token(BLANK);
        break;
      case T_numberFileUser:
      case T_minIterationToVerifyUniqueConnectedComponent:
      case T_minIterationToVerifyRelationshipBetweenOriginalConcepts:
      case T_maxIteration:
      case T_additionNewConceptWithoutCategory:
      case T_kCoreFilter:
      case T_quantityNodesToApplyKcoreFilter:
      case T_nDegreeFilter:
      case T_iterationTriggerApplyNDegreeFilterAlgorithm:
      case T_quantityNodesToApplyNdegreeFilter:
      case T_conceptsQuantityCalculationFactor:
      case T_conceptsMinMaxRange:
      case T_proporcionBetweenness:
      case T_proporcionBetweennessCloseness:
      case T_precisionBetweennessCloseness:
      case T_maxBetweennessCloseness:
      case T_proporcionEigenvector:
      case T_precisionEigenvector:
      case T_maxEigenvector:
      case T_isBetweennessCloseness:
      case T_isEigenvector:
      case T_isSelected:
      case T_isKeepNeighborsOfOriginalConcepts:
      case T_quantityNodesShortReport:
      case T_backGroundcolorOriginalConcept:
      case T_borderThicknessConceptWithHint:
      case T_nameQueryDefaultFile:
      case T_nameVocabularyFile:
      case T_nameUselessConceptsFile:
      case T_nameConsoleErrorFile:
      case T_dirRdfsPersistenceFiles:
      case T_dbpediaServer:
      case T_gephiVisualization:
      case T_graphStreamVisualization:
      case T_isFixBugInGephiToolKit:
      case T_originalConceptWithGephiToolKitBug:
      case T_isEnableUselessTable:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case T_numberFileUser:
          tokenVar = jj_consume_token(T_numberFileUser);
          break;
        case T_minIterationToVerifyUniqueConnectedComponent:
          tokenVar = jj_consume_token(T_minIterationToVerifyUniqueConnectedComponent);
          break;
        case T_minIterationToVerifyRelationshipBetweenOriginalConcepts:
          tokenVar = jj_consume_token(T_minIterationToVerifyRelationshipBetweenOriginalConcepts);
          break;
        case T_maxIteration:
          tokenVar = jj_consume_token(T_maxIteration);
          break;
        case T_additionNewConceptWithoutCategory:
          tokenVar = jj_consume_token(T_additionNewConceptWithoutCategory);
          break;
        case T_kCoreFilter:
          tokenVar = jj_consume_token(T_kCoreFilter);
          break;
        case T_quantityNodesToApplyKcoreFilter:
          tokenVar = jj_consume_token(T_quantityNodesToApplyKcoreFilter);
          break;
        case T_nDegreeFilter:
          tokenVar = jj_consume_token(T_nDegreeFilter);
          break;
        case T_iterationTriggerApplyNDegreeFilterAlgorithm:
          tokenVar = jj_consume_token(T_iterationTriggerApplyNDegreeFilterAlgorithm);
          break;
        case T_quantityNodesToApplyNdegreeFilter:
          tokenVar = jj_consume_token(T_quantityNodesToApplyNdegreeFilter);
          break;
        case T_conceptsQuantityCalculationFactor:
          tokenVar = jj_consume_token(T_conceptsQuantityCalculationFactor);
          break;
        case T_conceptsMinMaxRange:
          tokenVar = jj_consume_token(T_conceptsMinMaxRange);
          break;
        case T_proporcionBetweenness:
          tokenVar = jj_consume_token(T_proporcionBetweenness);
          break;
        case T_proporcionBetweennessCloseness:
          tokenVar = jj_consume_token(T_proporcionBetweennessCloseness);
          break;
        case T_precisionBetweennessCloseness:
          tokenVar = jj_consume_token(T_precisionBetweennessCloseness);
          break;
        case T_maxBetweennessCloseness:
          tokenVar = jj_consume_token(T_maxBetweennessCloseness);
          break;
        case T_proporcionEigenvector:
          tokenVar = jj_consume_token(T_proporcionEigenvector);
          break;
        case T_precisionEigenvector:
          tokenVar = jj_consume_token(T_precisionEigenvector);
          break;
        case T_maxEigenvector:
          tokenVar = jj_consume_token(T_maxEigenvector);
          break;
        case T_isBetweennessCloseness:
          tokenVar = jj_consume_token(T_isBetweennessCloseness);
          break;
        case T_isEigenvector:
          tokenVar = jj_consume_token(T_isEigenvector);
          break;
        case T_isSelected:
          tokenVar = jj_consume_token(T_isSelected);
          break;
        case T_isKeepNeighborsOfOriginalConcepts:
          tokenVar = jj_consume_token(T_isKeepNeighborsOfOriginalConcepts);
          break;
        case T_quantityNodesShortReport:
          tokenVar = jj_consume_token(T_quantityNodesShortReport);
          break;
        case T_backGroundcolorOriginalConcept:
          tokenVar = jj_consume_token(T_backGroundcolorOriginalConcept);
          break;
        case T_borderThicknessConceptWithHint:
          tokenVar = jj_consume_token(T_borderThicknessConceptWithHint);
          break;
        case T_nameQueryDefaultFile:
          tokenVar = jj_consume_token(T_nameQueryDefaultFile);
          break;
        case T_nameVocabularyFile:
          tokenVar = jj_consume_token(T_nameVocabularyFile);
          break;
        case T_nameUselessConceptsFile:
          tokenVar = jj_consume_token(T_nameUselessConceptsFile);
          break;
        case T_nameConsoleErrorFile:
          tokenVar = jj_consume_token(T_nameConsoleErrorFile);
          break;
        case T_dirRdfsPersistenceFiles:
          tokenVar = jj_consume_token(T_dirRdfsPersistenceFiles);
          break;
        case T_dbpediaServer:
          tokenVar = jj_consume_token(T_dbpediaServer);
          break;
        case T_gephiVisualization:
          tokenVar = jj_consume_token(T_gephiVisualization);
          break;
        case T_graphStreamVisualization:
          tokenVar = jj_consume_token(T_graphStreamVisualization);
          break;
        case T_isFixBugInGephiToolKit:
          tokenVar = jj_consume_token(T_isFixBugInGephiToolKit);
          break;
        case T_originalConceptWithGephiToolKitBug:
          tokenVar = jj_consume_token(T_originalConceptWithGephiToolKitBug);
          break;
        case T_isEnableUselessTable:
          tokenVar = jj_consume_token(T_isEnableUselessTable);
          break;
        default:
          jj_la1[14] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        jj_consume_token(EQUALS);
        tokenValue = jj_consume_token(TERM);
      configTable.insert(tokenVar.image, tokenValue.image.trim());
        break;
      case T_nameUserTermsFile:
      case T_nameGexfGraphFile:
      case T_nameTxtConceptMapFile:
      case T_nameCxlConceptMapFile:
      case T_nameCompleteReportFile:
      case T_nameShortReportFile:
      case T_nameConsoleReportFile:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case T_nameUserTermsFile:
          tokenVar = jj_consume_token(T_nameUserTermsFile);
          break;
        case T_nameGexfGraphFile:
          tokenVar = jj_consume_token(T_nameGexfGraphFile);
          break;
        case T_nameCompleteReportFile:
          tokenVar = jj_consume_token(T_nameCompleteReportFile);
          break;
        case T_nameShortReportFile:
          tokenVar = jj_consume_token(T_nameShortReportFile);
          break;
        case T_nameConsoleReportFile:
          tokenVar = jj_consume_token(T_nameConsoleReportFile);
          break;
        case T_nameTxtConceptMapFile:
          tokenVar = jj_consume_token(T_nameTxtConceptMapFile);
          break;
        case T_nameCxlConceptMapFile:
          tokenVar = jj_consume_token(T_nameCxlConceptMapFile);
          break;
        default:
          jj_la1[15] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        jj_consume_token(EQUALS);
        tokenValue = jj_consume_token(TERM);
          numberFileUser = WholeSystem.configTable.getString("numberFileUser");
      configTable.insert(tokenVar.image, tokenValue.image.trim().replace("##",numberFileUser));
        break;
      default:
        jj_la1[16] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  /** Generated Token Manager. */
  public WraptermsTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[17];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0xfffffff0,0x7fffffd0,0x80000020,0xfffffff0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x800000,0x10e0000,0x10e0000,0x800000,0xc0000,0xc0000,0xc0000,0xc0000,0xe0000,0xe0000,0x800000,0x10e0000,0x10e0000,0xeffff,0xff07,0xf8,0xeffff,};
   }

  /** Constructor with InputStream. */
  public Wrapterms(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Wrapterms(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new WraptermsTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public Wrapterms(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new WraptermsTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public Wrapterms(WraptermsTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(WraptermsTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[57];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 17; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 57; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
