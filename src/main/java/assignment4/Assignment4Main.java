package assignment4;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

public class Assignment4Main {

	public static void main(String[] args) throws Exception {
		// Step 1: extract opinions from assign4_reviews.txt
		ExtractOpinions step_1_extract_opinion = new ExtractOpinions();
		String line;
		int review_id = 1;
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("data//assign4_reviews.txt")));
		while ((line = br.readLine()) != null) {
			// Extract opinions from each reviews.
			step_1_extract_opinion.extract_pairs(review_id, line);
			review_id++;
		}
		br.close();

		// Output extracted opinions.
		HashMap<String, HashSet<Integer>> extracted_opinions = step_1_extract_opinion.extracted_opinions;
		for (String tmp_opinion : extracted_opinions.keySet()) {
			// e.g., output may have: [food, excellent] appears in review 6 13
			System.out.print("\n[" + tmp_opinion + "] appears in review ");
			HashSet<Integer> review_ids = extracted_opinions.get(tmp_opinion);
			for (int tmp_review_id : review_ids) {
				System.out.print(tmp_review_id + "\t");
			}
		}
		System.out.print("\n--------------------------------------------------------------");

		// Step 2: find similar extracted opinions
		double cosine_sim = 0.65;// ***** NT: you need to tune this threshold to
								// ***** get best performance on 20 reviews.
		FindSimilarOpinions step_2_find_similar_opinion = new FindSimilarOpinions(cosine_sim, extracted_opinions);
		String[] opinions = { "service, good", "service, bad", "atmosphere, good", "food, delicious" };
		for (String query_opinion : opinions) {
			System.out.print("\n\nquery opinion [" + query_opinion + "] has similar opinions: ");
			HashMap<String, HashSet<Integer>> similar_opinions = step_2_find_similar_opinion
					.findSimilarOpinions(query_opinion);
			for (String tmp_opinion : similar_opinions.keySet()) {
				System.out.print("\n[" + tmp_opinion + "] appears in review ");
				HashSet<Integer> review_ids = similar_opinions.get(tmp_opinion);
				for (int tmp_review_id : review_ids) {
					System.out.print(tmp_review_id + "\t");
				}
			}
		}
	}

}
