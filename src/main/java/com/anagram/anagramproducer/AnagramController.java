package com.anagram.anagramproducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnagramController {

	@RequestMapping(value = "/{word}", method = RequestMethod.GET, produces = "application/json")
	public Set<String> getAnagrams(@PathVariable String word) throws IOException {
		//List<String> anagrams = new ArrayList<String>();
		// Reads the input file
		Set<String> fileContentSet = readFile();

		// Generates all possible combinations of a given word
		Set<String> permuteSet = findAllPermutations(word, fileContentSet);

		// Checks if the dictionary collection contains a perumuted value
		// Prints the value if it is available in the wordlist.txt
/*		for (String permutedValue : permuteSet) {
			if (fileContentSet.contains(permutedValue)) {
				anagrams.add(permutedValue);
			}
		}*/
		
		fileContentSet.clear();
		
		for(String permutedValue : permuteSet) {
			System.out.println(permutedValue);
	}
		
		return permuteSet;
	}

	/**
	 * Reads the contents of the file from the given URL and collects it into a SET
	 * object
	 * 
	 * @return
	 * @throws IOException
	 */
	private Set<String> readFile() throws IOException {
		Set<String> fileContentSet = new HashSet<String>();
		System.out.println("Started reading the input file...");
		URL wordList = new URL("http://static.abscond.org/wordlist.txt");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(wordList.openStream()))) {
			fileContentSet = br.lines().collect(Collectors.toSet());
		} catch (IOException e) {
			System.out.println("Exception while reading a file. The exception is : ");
			e.printStackTrace();
		}
		System.out.println("Completed reading the input file");
		return fileContentSet;

	}

	/**
	 * Generates all the possible combinations of a given word
	 * 
	 * @param word
	 * @param fileContentSet 
	 * @return
	 */
	public Set<String> findAllPermutations(String word, Set<String> fileContentSet) {
		System.out.println("Hello");
		if (word == null) {
			throw new NullPointerException();
		}
		if (word.length() <= 1) {
			return Stream.of(word).collect(Collectors.toSet());
		}

		Set<String> permutations = new HashSet<String>();
		for (String permutation : findAllPermutations(word.substring(1), fileContentSet)) {
			char ch = word.charAt(0);
			for (int i = 0; i <= permutation.length(); i++) {
				String prefix = permutation.substring(0, i);
				String suffix = permutation.substring(i);
				String shuffledWord = prefix + ch + suffix;
				if(fileContentSet.contains(shuffledWord)) {
					permutations.add(shuffledWord);
				}			
			}
		}
		return permutations;
	}
}
