/* Generated By:JavaCC: Do not edit this line. WraptermsConstants.java */
package parse;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface WraptermsConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int T_numberFileUser = 4;
  /** RegularExpression Id. */
  int T_nameUserTermsFile = 5;
  /** RegularExpression Id. */
  int T_minIterationToVerifyUniqueConnectedComponent = 6;
  /** RegularExpression Id. */
  int T_minIterationToVerifyRelationshipBetweenOriginalConcepts = 7;
  /** RegularExpression Id. */
  int T_maxIteration = 8;
  /** RegularExpression Id. */
  int T_additionNewConceptWithoutCategory = 9;
  /** RegularExpression Id. */
  int T_kCoreFilter = 10;
  /** RegularExpression Id. */
  int T_iterationTriggerApplyKCoreFilterAlgorithm = 11;
  /** RegularExpression Id. */
  int T_quantityNodesToApplyKcoreFilter = 12;
  /** RegularExpression Id. */
  int T_nDegreeFilter = 13;
  /** RegularExpression Id. */
  int T_iterationTriggerApplyNDegreeFilterAlgorithm = 14;
  /** RegularExpression Id. */
  int T_quantityNodesToApplyNdegreeFilter = 15;
  /** RegularExpression Id. */
  int T_conceptsQuantityCalulationFactor = 16;
  /** RegularExpression Id. */
  int T_conceptsMinMaxRange = 17;
  /** RegularExpression Id. */
  int T_proporcionBetweenness = 18;
  /** RegularExpression Id. */
  int T_proporcionBetweennessCloseness = 19;
  /** RegularExpression Id. */
  int T_precisionBetweennessCloseness = 20;
  /** RegularExpression Id. */
  int T_maxBetweennessCloseness = 21;
  /** RegularExpression Id. */
  int T_proporcionEigenvector = 22;
  /** RegularExpression Id. */
  int T_precisionEigenvector = 23;
  /** RegularExpression Id. */
  int T_maxEigenvector = 24;
  /** RegularExpression Id. */
  int T_isBetweennessCloseness = 25;
  /** RegularExpression Id. */
  int T_isEigenvector = 26;
  /** RegularExpression Id. */
  int T_isSelected = 27;
  /** RegularExpression Id. */
  int T_isKeepNeighborsOfOriginalConcepts = 28;
  /** RegularExpression Id. */
  int T_quantityNodesShortReport = 29;
  /** RegularExpression Id. */
  int T_backGroundcolorOriginalConcept = 30;
  /** RegularExpression Id. */
  int T_borderThicknessConceptWithHint = 31;
  /** RegularExpression Id. */
  int T_nameGexfGraphFile = 32;
  /** RegularExpression Id. */
  int T_nameQueryDefaultFile = 33;
  /** RegularExpression Id. */
  int T_nameVocabularyFile = 34;
  /** RegularExpression Id. */
  int T_nameUselessConceptsFile = 35;
  /** RegularExpression Id. */
  int T_nameTxtConceptMapFile = 36;
  /** RegularExpression Id. */
  int T_nameCxlConceptMapFile = 37;
  /** RegularExpression Id. */
  int T_nameCompleteReportFile = 38;
  /** RegularExpression Id. */
  int T_nameShortReportFile = 39;
  /** RegularExpression Id. */
  int T_nameConsoleReportFile = 40;
  /** RegularExpression Id. */
  int T_nameConsoleErrorFile = 41;
  /** RegularExpression Id. */
  int T_dirRdfsPersistenceFiles = 42;
  /** RegularExpression Id. */
  int T_dbpediaServer = 43;
  /** RegularExpression Id. */
  int T_gephiVisualization = 44;
  /** RegularExpression Id. */
  int T_graphStreamVisualization = 45;
  /** RegularExpression Id. */
  int T_isFixBugInGephiToolKit = 46;
  /** RegularExpression Id. */
  int T_originalConceptWithGephiToolKitBug = 47;
  /** RegularExpression Id. */
  int EQUALS = 48;
  /** RegularExpression Id. */
  int NEW_LINE = 49;
  /** RegularExpression Id. */
  int TAB = 50;
  /** RegularExpression Id. */
  int BLANK = 51;
  /** RegularExpression Id. */
  int ARROW = 52;
  /** RegularExpression Id. */
  int COMMON_CHARACTER = 53;
  /** RegularExpression Id. */
  int SPECIAL_CHARACTER = 54;
  /** RegularExpression Id. */
  int TERM = 55;
  /** RegularExpression Id. */
  int SEPARATORS = 56;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int comentario_final = 1;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\"//\"",
    "\"\\n\"",
    "<token of kind 3>",
    "\"numberFileUser\"",
    "\"nameUserTermsFile\"",
    "\"minIterationToVerifyUniqueConnectedComponent\"",
    "\"minIterationToVerifyRelationshipBetweenOriginalConcepts\"",
    "\"maxIteration\"",
    "\"additionNewConceptWithoutCategory\"",
    "\"kCoreFilter\"",
    "\"iterationTriggerApplyKCoreFilterAlgorithm\"",
    "\"quantityNodesToApplyKcoreFilter\"",
    "\"nDegreeFilter\"",
    "\"iterationTriggerApplyNDegreeFilterAlgorithm\"",
    "\"quantityNodesToApplyNdegreeFilter\"",
    "\"conceptsQuantityCalulationFactor\"",
    "\"conceptsMinMaxRange\"",
    "\"proporcionBetweenness\"",
    "\"proporcionBetweennessCloseness\"",
    "\"precisionBetweennessCloseness\"",
    "\"maxBetweennessCloseness\"",
    "\"proporcionEigenvector\"",
    "\"precisionEigenvector\"",
    "\"maxEigenvector\"",
    "\"isBetweennessCloseness\"",
    "\"isEigenvector\"",
    "\"isSelected\"",
    "\"isKeepNeighborsOfOriginalConcepts\"",
    "\"quantityNodesShortReport\"",
    "\"backGroundcolorOriginalConcept\"",
    "\"borderThicknessConceptWithHint\"",
    "\"nameGexfGraphFile\"",
    "\"nameQueryDefaultFile\"",
    "\"nameVocabularyFile\"",
    "\"nameUselessConceptsFile\"",
    "\"nameTxtConceptMapFile\"",
    "\"nameCxlConceptMapFile\"",
    "\"nameCompleteReportFile\"",
    "\"nameShortReportFile\"",
    "\"nameConsoleReportFile\"",
    "\"nameConsoleErrorFile\"",
    "\"dirRdfsPersistenceFiles\"",
    "\"dbpediaServer\"",
    "\"gephiVisualization\"",
    "\"graphStreamVisualization\"",
    "\"isFixBugInGephiToolKit\"",
    "\"originalConceptWithGephiToolKitBug\"",
    "<EQUALS>",
    "<NEW_LINE>",
    "<TAB>",
    "\" \"",
    "\"->\"",
    "<COMMON_CHARACTER>",
    "<SPECIAL_CHARACTER>",
    "<TERM>",
    "<SEPARATORS>",
  };

}
