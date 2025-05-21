package Projeto.java.question1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * O codigo gerar todos os anagramas possíveis
 * a partir de um conjunto de letras distintas. Incluindo validação para garantir
 * que a entrada seja válida! (não vazia e sendo apenas letras).
 * Os testes estão no pacote de test do projeto
 */
public class AnagramGenerator {
    //O metodo ira receber char, pois char é mutavel e pode ser modificado... Mas se fosse String teria que ficar criando uma nova String.
    //Pois ha uma alternação no swap
    public List<String> generateAnagrams(char[] letters) {
        validateInput(letters);

        Set<String> result = new HashSet<>();
        generateAnagramsHelper(letters, 0, result);

        return new ArrayList<>(result);
    }

    // Gera todos os anagramas possíveis a partir de uma string de letras distintas.
    public List<String> generateAnagrams(String letters) {
        if (letters == null) {
            throw new IllegalArgumentException("A entrada não pode ser nula");
        }
        return generateAnagrams(letters.toCharArray());
    }

    private void validateInput(char[] letters) {
        if (letters == null || letters.length == 0) {
            throw new IllegalArgumentException("A entrada não pode estar vazia");
        }

        for (char c : letters) {
            if (!Character.isLetter(c)) {
                throw new IllegalArgumentException("A entrada deve conter apenas letras");
            }
        }
    }

   //Método auxiliar para gerar anagramas usando backtracking.
    //Aqui o fluxo funcina como se fosse -> “Tenta algo, vê no que dá, e se não der, volta atrás e tenta de novo de outro jeito.”
    private void generateAnagramsHelper(char[] letters, int start, Set<String> result) {
        if (start == letters.length - 1) {
            result.add(new String(letters));
            return;
        }

        for (int i = start; i < letters.length; i++) {
            swap(letters, start, i);
            generateAnagramsHelper(letters, start + 1, result);

            swap(letters, start, i);
        }
    }

   // Troca dois caracteres em um array.
    private void swap(char[] letters, int i, int j) {
        char temp = letters[i];
        letters[i] = letters[j];
        letters[j] = temp;
    }
}
