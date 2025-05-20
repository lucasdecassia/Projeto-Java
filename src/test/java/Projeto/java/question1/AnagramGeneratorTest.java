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
@DisplayName("Testes do Gerador de Anagramas")
class AnagramGeneratorTest {

    private final AnagramGenerator anagramGenerator = new AnagramGenerator();

    @Test
    @DisplayName("Deve gerar todos os anagramas para 'abc'")
    void shouldGenerateAllAnagramsForAbc() {
        String input = "abc";

        List<String> anagrams = anagramGenerator.generateAnagrams(input);

        assertThat(anagrams, hasSize(6)); // 3! = 6 permutações
        assertThat(anagrams, containsInAnyOrder("abc", "acb", "bac", "bca", "cab", "cba"));
    }

    @Test
    @DisplayName("Deve gerar todos os anagramas para 'ab'")
    void shouldGenerateAllAnagramsForAb() {
        String input = "ab";

        List<String> anagrams = anagramGenerator.generateAnagrams(input);

        assertThat(anagrams, hasSize(2)); // 2! = 2 permutações
        assertThat(anagrams, containsInAnyOrder("ab", "ba"));
    }

    @Test
    @DisplayName("Deve gerar um anagrama para uma única letra")
    void shouldGenerateOneAnagramForSingleLetter() {
        String input = "a";

        List<String> anagrams = anagramGenerator.generateAnagrams(input);

        assertThat(anagrams, hasSize(1));
        assertThat(anagrams, contains("a"));
    }

    @Test
    @DisplayName("Deve lidar corretamente com letras maiúsculas")
    void shouldHandleUppercaseLettersCorrectly() {
        String input = "ABC";

        List<String> anagrams = anagramGenerator.generateAnagrams(input);

        assertThat(anagrams, hasSize(6)); // 3! = 6 permutações
        assertThat(anagrams, containsInAnyOrder("ABC", "ACB", "BAC", "BCA", "CAB", "CBA"));
    }

    @Test
    @DisplayName("Deve lidar corretamente com letras de caso misto")
    void shouldHandleMixedCaseLettersCorrectly() {
        String input = "AbC";

        List<String> anagrams = anagramGenerator.generateAnagrams(input);

        assertThat(anagrams, hasSize(6)); // 3! = 6 permutações
        assertThat(anagrams, containsInAnyOrder("AbC", "ACb", "bAC", "bCA", "CAb", "CbA"));
    }

    @Test
    @DisplayName("Deve lançar exceção para entrada nula")
    void shouldThrowExceptionForNullInput() {
        String input = null;

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> anagramGenerator.generateAnagrams(input)
        );

        assertThat(exception.getMessage(), is("A entrada não pode ser nula"));
    }

    @Test
    @DisplayName("Deve lançar exceção para entrada vazia")
    void shouldThrowExceptionForEmptyInput() {
        String input = "";

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> anagramGenerator.generateAnagrams(input)
        );

        assertThat(exception.getMessage(), is("A entrada não pode estar vazia"));
    }

    @Test
    @DisplayName("Deve lançar exceção para entrada não-letra")
    void shouldThrowExceptionForNonLetterInput() {
        String input = "a1b";

        // Quando & Então
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> anagramGenerator.generateAnagrams(input)
        );

        assertThat(exception.getMessage(), is("A entrada deve conter apenas letras"));
    }
}
