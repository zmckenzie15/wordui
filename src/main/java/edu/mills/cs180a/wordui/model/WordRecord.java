package edu.mills.cs180a.wordui.model;

import java.util.Comparator;
import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class WordRecord {
    private static Comparator<WordRecord> azComparator =
            ((w1, w2) -> w1.getWord().compareToIgnoreCase(w2.getWord()));
    private static Comparator<WordRecord> zaComparator =
            ((w1, w2) -> w2.getWord().compareToIgnoreCase(w1.getWord()));

    public enum SortOrder {
        ALPHABETICALLY_FORWARD("alphabetically (A-Z)", azComparator),
        ALPHABETICALLY_BACKWARD("alphabetically (Z-A)", zaComparator);

        private final String name;
        private final Comparator<WordRecord> comparator;

        private SortOrder(String name, Comparator<WordRecord> comparator) {
            this.name = name;
            this.comparator = comparator;
        }

        @Override
        public String toString() {
            return name;
        }

        public Comparator<WordRecord> getComparator() {
            return comparator;
        }
    }

    private final StringProperty word = new SimpleStringProperty(this, "word", "");
    private final IntegerProperty frequency = new SimpleIntegerProperty(this, "frequency");
    private final StringProperty definition =
            new SimpleStringProperty(this, "definition", "sample definition");

    public WordRecord() {}

    public WordRecord(String word, int frequency, String definition) {
        this.word.set(word);
        this.frequency.set(frequency);
        this.definition.set(definition);
    }

    public String getWord() {
        return word.get();
    }

    public StringProperty wordProperty() {
        return word;
    }

    public void setWord(String word) {
        this.word.set(word);
    }

    public Integer getFrequency() {
        return frequency.get();
    }

    public IntegerProperty frequencyProperty() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency.set(frequency);
    }

    public String getDefinition() {
        return definition.get();
    }

    public StringProperty definitionProperty() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition.set(definition);
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", word.get(), frequency.get());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        WordRecord record = (WordRecord) obj;
        return Objects.equals(word, record.word) && Objects.equals(frequency, record.frequency)
                && Objects.equals(definition, record.definition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, frequency, definition);
    }

    /*
     * public static Callback<Person, Observable[]> extractor = p -> new Observable[]
     * {p.lastnameProperty(), p.firstnameProperty()};
     */
}
