package Projeto.java.question1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Anagram Generator Tests")
class AnagramGeneratorTest {

    private final AnagramGenerator anagramGenerator = new AnagramGenerator();

    @Test
    @DisplayName("Should generate all anagrams for 'abc'")
    void shouldGenerateAllAnagramsForAbc() {
        // Given
        String input = "abc";

        // When
        List<String> anagrams = anagramGenerator.generateAnagrams(input);

        // Then
        assertThat(anagrams, hasSize(6)); // 3! = 6 permutations
        assertThat(anagrams, containsInAnyOrder("abc", "acb", "bac", "bca", "cab", "cba"));
    }

    @Test
    @DisplayName("Should generate all anagrams for 'ab'")
    void shouldGenerateAllAnagramsForAb() {
        // Given
        String input = "ab";

        // When
        List<String> anagrams = anagramGenerator.generateAnagrams(input);

        // Then
        assertThat(anagrams, hasSize(2)); // 2! = 2 permutations
        assertThat(anagrams, containsInAnyOrder("ab", "ba"));
    }

    @Test
    @DisplayName("Should generate one anagram for a single letter")
    void shouldGenerateOneAnagramForSingleLetter() {
        // Given
        String input = "a";

        // When
        List<String> anagrams = anagramGenerator.generateAnagrams(input);

        // Then
        assertThat(anagrams, hasSize(1));
        assertThat(anagrams, contains("a"));
    }

    @Test
    @DisplayName("Should handle uppercase letters correctly")
    void shouldHandleUppercaseLettersCorrectly() {
        // Given
        String input = "ABC";

        // When
        List<String> anagrams = anagramGenerator.generateAnagrams(input);

        // Then
        assertThat(anagrams, hasSize(6)); // 3! = 6 permutations
        assertThat(anagrams, containsInAnyOrder("ABC", "ACB", "BAC", "BCA", "CAB", "CBA"));
    }

    @Test
    @DisplayName("Should handle mixed case letters correctly")
    void shouldHandleMixedCaseLettersCorrectly() {
        // Given
        String input = "AbC";

        // When
        List<String> anagrams = anagramGenerator.generateAnagrams(input);

        // Then
        assertThat(anagrams, hasSize(6)); // 3! = 6 permutations
        assertThat(anagrams, containsInAnyOrder("AbC", "ACb", "bAC", "bCA", "CAb", "CbA"));
    }

    @Test
    @DisplayName("Should throw exception for null input")
    void shouldThrowExceptionForNullInput() {
        // Given
        String input = null;

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> anagramGenerator.generateAnagrams(input)
        );

        assertThat(exception.getMessage(), is("A entrada não pode ser nula"));
    }

    @Test
    @DisplayName("Should throw exception for empty input")
    void shouldThrowExceptionForEmptyInput() {
        // Given
        String input = "";

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> anagramGenerator.generateAnagrams(input)
        );

        assertThat(exception.getMessage(), is("A entrada não pode estar vazia"));
    }

    @Test
    @DisplayName("Should throw exception for non-letter input")
    void shouldThrowExceptionForNonLetterInput() {
        // Given
        String input = "a1b";

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> anagramGenerator.generateAnagrams(input)
        );

        assertThat(exception.getMessage(), is("A entrada deve conter apenas letras"));
    }
}
