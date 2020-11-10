package edu.mills.cs180a.wordui.model;

import java.io.IOException;
import java.util.List;
import edu.mills.cs180a.wordnik.client.ApiClientHelper;
import edu.mills.cs180a.wordnik.client.api.WordsApi;
import edu.mills.cs180a.wordnik.client.invoker.ApiClient;
import edu.mills.cs180a.wordnik.client.model.WordOfTheDay;
import javafx.collections.ObservableList;

public class SampleData {
    public static void fillSampleData(ObservableList<WordRecord> backingList) {
        try {
            ApiClient client = ApiClientHelper.getApiClient();
            WordsApi wordsApi = client.buildClient(WordsApi.class);
            WordOfTheDay word = wordsApi.getWordOfTheDay();
            List<Object> definitions = word.getDefinitions();
            String definition = definitions.isEmpty() ? "none" : definitions.get(0).toString();
            backingList.add(
                    new WordRecord(word.getWord(), 0, definition));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        backingList.add(new WordRecord("buffalo", 5153, "The North American bison."));
        backingList.add(new WordRecord("school", 23736, "A large group of aquatic animals."));
        backingList.add(new WordRecord("Java",
                179, "An island of Indonesia in the Malay Archipelago"));
        backingList.add(new WordRecord("random",
                794, "Having no specific pattern, purpose, or objective"));
    }
}
