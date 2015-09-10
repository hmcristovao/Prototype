package main;
public interface Constants {

		String nameConfigFile = "config.txt";
		
	    String addressBasic = "http://relationship/";
		String addressImage = "http://commons.wikimedia.org/wiki/File:";
		String markQueryReplacement = "#######";
		String originalConceptAddress = "http://dbpedia.org/resource/";
		String nameGraph = "Graph relationship";
		String nameGephiWorkspace = "workspace0";

		// ignore capital letter
		boolean ignoreCaseConcept = false;
		
		boolean edgeLabelStreamGephi = true;
		boolean nodeLabelStreamGephi = false; // original concepts always have label in Stream Gephi
		
		boolean edgeLabelFileGephi   = true;
		boolean nodeLabelFileGephi   = false; // original concepts always have label in Stream Gephi

		boolean directedStreamGraph = false; // must be false because AStar class
		boolean directedGephiGraph  = true;  // only accept true 
		
		// indicate value of concepts that do not belong to connected component (for example: original concepts)
		int withoutConnectedComponent = -1;
		
		int maxNodes = 50000;
		int minEdges = 50000;
		
		String doubleLine = "\n=============================================================================================\n";
		String singleLine = "\n---------------------------------------------------------------------------------------------\n";
		String starsLine  = "\n*********************************************************************************************\n";

}
