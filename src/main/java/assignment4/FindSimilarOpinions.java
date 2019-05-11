package assignment4;

import java.util.HashMap;
import java.util.HashSet;

public class FindSimilarOpinions {

	public static void main(String[] args) throws Exception {
		// You can reference the codes here to do word2vec related coding.
		FindSimilarOpinions abc = new FindSimilarOpinions(0.8, null);
		float[] tmpVec1 = abc.word2VecObject.wordMap.get("good");
		float[] tmpVec2 = abc.word2VecObject.wordMap.get("kind");
		double simUni = abc.word2VecObject.cosineSimilarity(tmpVec1, tmpVec2);
		System.out.println(simUni);
	}

	Word2VecClass word2VecObject;
	HashMap<String, HashSet<Integer>> extracted_opinions;
	double cosine_sim = 0.5;

	public FindSimilarOpinions(double input_cosine_sim, HashMap<String, HashSet<Integer>> input_extracted_ops)
			throws Exception {
		cosine_sim = input_cosine_sim;
		extracted_opinions = input_extracted_ops;
		word2VecObject = new Word2VecClass();
		word2VecObject.loadModel("data\\assign4_word2vec1_for_java.bin");
	}

	public HashMap<String, HashSet<Integer>> findSimilarOpinions(String query_opinion) {
		// example data, please remove in your real code. Only for demo.
		HashMap<String, HashSet<Integer>> similar_opinions = new HashMap<String, HashSet<Integer>>();
		
		for(HashMap.Entry<String, HashSet<Integer>> opinionMap: extracted_opinions.entrySet() ) {
			 String opinion_words[] = opinionMap.getKey().split(", ");
			 String query_words[] = query_opinion.split(", ");
			 double simUni = 0, similarity = 0;
			 for(int i=0; i<2; i++) {
				 float[] tmpVec1 = word2VecObject.wordMap.get(opinion_words[i]);
				 float[] tmpVec2 = word2VecObject.wordMap.get(query_words[i]);

				 if(tmpVec1 != null && tmpVec2 != null) {
					 similarity = word2VecObject.cosineSimilarity(tmpVec1, tmpVec2);
					 simUni = simUni + similarity;
					 if(similarity < 0.14) {
						simUni=-600; 
					 }
				 }
				 else {
					 simUni=-600;
				 }
			 }
			 simUni=simUni/2;

			 if(simUni >= cosine_sim) {
				 similar_opinions.put(opinionMap.getKey(),opinionMap.getValue());
			 }
			
		}
		return similar_opinions;

	}

}
