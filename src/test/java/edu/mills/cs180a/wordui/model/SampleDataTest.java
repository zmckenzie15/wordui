package edu.mills.cs180a.wordui.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.mills.cs180a.wordnik.client.model.FrequencySummary;

class SampleDataTest {
    private final FrequencySummary mockFS = mock(FrequencySummary.class);

    @BeforeEach
    void setup() {
        List<Object> freqObjects = List.of(
                // frequencies for "apple"
                makeMap(2000, 339),
                makeMap(2001, 464));
        when(mockFS.getFrequency())
                .thenReturn(freqObjects);
    }

    private static Map<Object, Object> makeMap(int year, int count) {
        return Map.of(SampleData.FREQ_YEAR_KEY, String.valueOf(year),
                SampleData.FREQ_COUNT_KEY, count);
    }

    @Test
    void testGetFrequencyFromSummary() {
        assertEquals(339, SampleData.getFrequencyFromSummary(mockFS, 2000));
        assertEquals(464, SampleData.getFrequencyFromSummary(mockFS, 2001));
        assertEquals(0, SampleData.getFrequencyFromSummary(mockFS, 2020));
    }
}
