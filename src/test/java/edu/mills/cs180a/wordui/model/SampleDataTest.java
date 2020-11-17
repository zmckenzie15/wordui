package edu.mills.cs180a.wordui.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import edu.mills.cs180a.wordnik.client.api.WordApi;
import edu.mills.cs180a.wordnik.client.model.FrequencySummary;

class SampleDataTest {
    private final WordApi mockWordApi = mock(WordApi.class);
    private static List<Object> FREQ_OBJECTS_APPLE = List.of(
            // frequencies for "apple"
            makeMap(2000, 339),
            makeMap(2001, 464));
    private static final List<Object> FREQ_OBJECTS_ORANGE = List.of(
            // frequencies for "orange"
            makeMap(2000, 774),
            makeMap(2001, 941));

    @BeforeEach
    void setup() {
        FrequencySummary appleFS = makeFrequencySummary(FREQ_OBJECTS_APPLE);
        when(mockWordApi.getWordFrequency(eq("apple"), anyString(), anyInt(), anyInt()))
                .thenReturn(appleFS);
        FrequencySummary orangeFS = makeFrequencySummary(FREQ_OBJECTS_ORANGE);
        when(mockWordApi.getWordFrequency(eq("orange"), anyString(), anyInt(), anyInt()))
                .thenReturn(orangeFS);
    }

    private static Map<Object, Object> makeMap(int year, int count) {
        return Map.of(SampleData.FREQ_YEAR_KEY, String.valueOf(year),
                SampleData.FREQ_COUNT_KEY, count);
    }

    private static FrequencySummary makeFrequencySummary(List<Object> freqs) {
        FrequencySummary fs = mock(FrequencySummary.class);
        when(fs.getFrequency()).thenReturn(freqs);
        return fs;
    }

    @ParameterizedTest
    @CsvSource({"apple,2000,339", "apple,2001,464", "apple,2020,0", "orange,2000,774",
            "orange,2001,941", "orange,2050,0"})
    void testGetFrequencyFromSummary(String word, int year, int count) {
        assertEquals(count, SampleData.getFrequencyByYear(mockWordApi, word, year));
    }
}
