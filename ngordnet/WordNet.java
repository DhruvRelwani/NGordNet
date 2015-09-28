package ngordnet;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.algs4.Digraph;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/* @Author: Dhruv Harendra Relwani
   Project NGordnet

   Citations: 
   StackOverflow - Changing Array elements into a Set.
   Oracle Documentation - ADT descriptions. 
*/

public class WordNet  {
    private Map<Integer, Set<String>> idToSynset = new HashMap<Integer, Set<String>>(); 
    private Digraph hypIDs;
    private Set<String> allNouns = new HashSet<String>();
    /** Creates a WordNet using files for m SYNSETFILENAME and HYPONYMFILENAME */
    public WordNet(String synsetFilename, String hyponymFilename)  {
        int numberOfIDs = 0;
        // Map ID to Synsets
        In synfile = new In(synsetFilename);
        while (synfile.hasNextLine())  {
            String[] temp = synfile.readLine().split(",");
            Integer id = Integer.parseInt(temp[0]);
            // Seperating the nouns inside the synset
            String[] nounsTemp = temp[1].split(" ");
            // Add these to a synset
            Set<String> synset = new HashSet<String>(Arrays.asList(nounsTemp));
            // Add the elements (nouns) of the synset to the set of allNouns
            allNouns.addAll(synset); 
            idToSynset.put(id, synset);
            numberOfIDs += 1;
        }
        // Create Digraph using the Ids 
        In hypfile = new In(hyponymFilename);
        hypIDs = new Digraph(numberOfIDs);
        List<Integer> digraphKeys = new ArrayList<Integer>();

        while (hypfile.hasNextLine()) {
            String[] temp = hypfile.readLine().split(",");
            //Change all of those strings to integers
            Integer[] arrayIDs = new Integer[temp.length];
            for (int ctr = 0; ctr < temp.length; ctr++) {
                arrayIDs[ctr] = Integer.parseInt(temp[ctr]);
            }
            // For  all those IDs mentioned as first element in HYPONYMFILE.
            Integer synsetId = arrayIDs[0];
            digraphKeys.add(synsetId);
            // One Hypnonym is itself and it's synonyms (Same IDs)
            hypIDs.addEdge(synsetId, synsetId); 
            for (int ctr = 0; ctr < arrayIDs.length; ctr++) {
                // Adds edges to hyponyms, based on the HYPONYMFILE description (Sub IDs)
                hypIDs.addEdge(synsetId, arrayIDs[ctr]); 
            }
        }
        // For  all those IDs NOT mentioned as first element in HYPONYMFILE.
        for  (Integer key : idToSynset.keySet()) {
            // For  every key in synset, if it wasn't a Digraph key earlier,
            if (!digraphKeys.contains(key)) {   
                hypIDs.addEdge(key, key);     //add it's edge to itself and synonyms.
                digraphKeys.add(key);
            }
        }   
    } 

    /* Returns true if NOUN is a word in some synset. */
    public boolean isNoun(String noun) {
        return allNouns.contains(noun);
    }

    /* Returns the set of all nouns. */
    public Set<String> nouns() {
        return allNouns;
    }

    /** Returns the set of all hyponyms of WORD as well as all synonyms of
      * WORD. If WORD belongs to multiple synsets, return all hyponyms of
      * all of these synsets. See http://goo.gl/EGLoys for  an example.
      * Do not include hyponyms of synonyms.
      */
    public Set<String> hyponyms(String word) {
        Set<Map.Entry<Integer, Set<String>>> setOfEntries = idToSynset.entrySet();
        Set<Integer> idSet = new HashSet<Integer>();
        Set<Integer> hyponymIds;
        Set<String> hyponyms = new HashSet<String>();
        //Finding the IDs that map to a set containing 'word'
        for  (Map.Entry<Integer, Set<String>> entry : setOfEntries) {
            if (entry.getValue().contains(word)) {
                idSet.add(entry.getKey());
            }
        }
        // Finding the hyponym IDs for  our set of IDs by referring to the digraph
        hyponymIds = GraphHelper.descendants(hypIDs, idSet); 
        // Storing the nouns of those keys 
        for (Integer id : hyponymIds) {
            hyponyms.addAll(idToSynset.get(id));
        }
        return hyponyms;
    }
}
