package assignment4;

import java.util.HashMap;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Sentence;

public class ExtractOpinions {

	// Extracted opinions and corresponding review id is saved in
	// extracted_pairs, where KEY is the opinion and VALUE is the set of
	// review_ids where the opinion is extracted from.
	// Opinion should in form of "attribute, value",
	// such as "service, good".
	HashMap<String, HashSet<Integer>> extracted_opinions;
	PrintWriter out;
	BufferedReader buff;
	public ExtractOpinions() {
		extracted_opinions = new HashMap<String, HashSet<Integer>>();
	}

	public void extract_pairs(int review_id, String review_content) throws IOException {
		
    	out = new PrintWriter("data\\output"+review_id+".txt");
		Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
		
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation annotation;
        annotation = new Annotation(review_content); 
		pipeline.annotate(annotation);
		pipeline.prettyPrint(annotation, out);
		// example data, please remove in your real code. Only for demo.pipeline.annotate(annotation);
		buff = new BufferedReader(new InputStreamReader(new FileInputStream("data\\output"+review_id+".txt")));
		String sentence = null;
		while((sentence=buff.readLine()) != null) {
			if(sentence.contains("Dependency Parse (enhanced plus plus dependencies)")) {
				sentence_pairs(review_id);
			}
		}
	
	}
	
	public void sentence_pairs(int review_id) throws IOException {
		String sentence = null;
	
		while( (sentence=buff.readLine())!= null && !sentence.contains("Extracted the following NER entity mentions:") ) {
			if(sentence.contains("amod") || sentence.contains("acl:relcl") || sentence.contains("nsubj")){
				
				String attribute = sentence.substring(sentence.indexOf('(')+1, sentence.indexOf('-')).toLowerCase();
				String value = sentence.substring(sentence.lastIndexOf(' ')+1, sentence.lastIndexOf('-')).toLowerCase();
				
				attribute = new Sentence(attribute).lemma(0);
				value = new Sentence(value).lemma(0);
				HashSet<Integer> opinion_map;
				String word_str;
				
				if(!sentence.contains("nsubj")) word_str = attribute + ", " + value;
				else word_str = value + ", " + attribute;
				
				if(extracted_opinions.containsKey(word_str)) {
					opinion_map = extracted_opinions.get(word_str);
					opinion_map.add(review_id);
					extracted_opinions.replace(word_str, opinion_map);
				}
				else {
					opinion_map = new HashSet<Integer>();
					opinion_map.add(review_id);
					extracted_opinions.put(word_str, opinion_map);
				}
				
			}
		}
	}
	
}
