package edu.mills.cs180a.wordui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.io.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import edu.mills.cs180a.wordnik.client.api.*;
import edu.mills.cs180a.wordnik.client.model.*;

class WorduiWordnikClientTest {
    private static WorduiWordnikClient client;
    private static WordApi mockWordApi = mock(WordApi.class);
    private static FrequencySummary mockFrequencySummaryApple = mock(FrequencySummary.class);
    private static FrequencySummary mockFrequencySummaryOrange = mock(FrequencySummary.class);
    // These should be returned by getFrequency() for "apple" and "orange"
    private static final List<Object> APPLE_FREQUENCIES =
            List.of(makeMap(2000, 339), makeMap(2001, 464), makeMap(2020, 0));
    private static final List<Object> ORANGE_FREQUENCIES =
            List.of(makeMap(2000, 774), makeMap(2001, 941));

    private static Map<Object, Object> makeMap(int year, int count) {
        return Map.of(WorduiWordnikClient.FREQ_YEAR_KEY, String.valueOf(year),
                WorduiWordnikClient.FREQ_COUNT_KEY, count);
    }

    @BeforeAll
    public static void setup() throws IOException {
        when(mockWordApi.getWordFrequency(eq("apple"), anyString(), anyInt(), anyInt()))
                .thenReturn(mockFrequencySummaryApple);
        when(mockWordApi.getWordFrequency(eq("orange"), anyString(), anyInt(), anyInt()))
                .thenReturn(mockFrequencySummaryOrange);
        when(mockFrequencySummaryApple.getFrequency()).thenReturn(APPLE_FREQUENCIES);
        when(mockFrequencySummaryOrange.getFrequency()).thenReturn(ORANGE_FREQUENCIES);
        client = WorduiWordnikClient.getMockInstance(mockWordApi);
    }

    @ParameterizedTest
    @CsvSource({"apple,2000,339", "apple,2001,464", "apple,2020,0", "orange,2000,774",
            "orange,2001,941"})
    void testGetFrequencyFromSummary(String word, int year, int count) {
        assertEquals(count, client.getFrequencyByYear(word, year));
    }
}
