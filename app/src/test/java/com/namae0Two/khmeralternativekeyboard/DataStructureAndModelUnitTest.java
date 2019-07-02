package com.namae0Two.khmeralternativekeyboard;

import com.namae0Two.khmeralternativekeyboard.data.ButtonData;
import com.namae0Two.khmeralternativekeyboard.data.ButtonType;
import com.namae0Two.khmeralternativekeyboard.data.Trie;
import com.namae0Two.khmeralternativekeyboard.khmer.CharacterType;
import com.namae0Two.khmeralternativekeyboard.khmer.KhmerLang;
import com.namae0Two.khmeralternativekeyboard.khmer.KhmerWord;
import com.namae0Two.khmeralternativekeyboard.khmer.KhmerWordCluster;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DataStructureAndModelUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void characterButtonDataTest() {
        ButtonData b = new ButtonData(ButtonType.ALPHABET_TYPE, 40);
        //Initialization
        assertEquals("", b.getBottom());
        assertEquals("", b.getTop());
        assertEquals("", b.getRight());
        assertEquals("", b.getMiddle());
        assertEquals("", b.getLeft());
        //Setting Value
        b.setMiddle("A");
        b.setRight("B");
        //Test Set Value

        assertEquals("A", b.getMiddle());
        assertEquals("B", b.getRight());
        assertEquals(b.getAllChars().size(), 2);
    }

    @Test
    public void testKhmerLangMap() {
        Map<String, CharacterType> map = KhmerLang.Companion.getCharacter_to_type_map();

        //Consonant and Independent Vowels

        List<String> consonant = KhmerLang.Companion.getConsonant();
        List<String> independent_vowels = KhmerLang.Companion.getIndependent_vowels();

        for (int i = 0; i < consonant.size(); i++) {
            assertEquals(map.get(consonant.get(i)), CharacterType.INDEPENDENT_ALPHABET);
        }
        for (String vowel : independent_vowels) {
            assertEquals(map.get(vowel), CharacterType.INDEPENDENT_ALPHABET);

        }
        //Vowel

        List<String> vowel = KhmerLang.Companion.getBase_vowels();

        for (String s : vowel) {
            assertEquals(map.get(s), CharacterType.DEPENDENT_VOWEL);

        }

        ///Diacritic

        List<String> above_diacritic = KhmerLang.Companion.getDependent_above_diacritic();
        List<String> after_diacritic = KhmerLang.Companion.getDependent_after_diacritic();

        for (String s : above_diacritic) {
            assertEquals(map.get(s), CharacterType.ABOVE_DIACRITIC);
        }

        for (String s : after_diacritic) {
            assertEquals(map.get(s), CharacterType.AFTER_DIACRITIC);
        }

        ///Other sign and digits

        List<String> khmer_digits = KhmerLang.Companion.getKhmer_digits();
        List<String> latin_digits = KhmerLang.Companion.getLatin_digits();

        List<String> other_sign = KhmerLang.Companion.getOther_sign();

        for (String s : khmer_digits) {
            assertEquals(map.get(s), CharacterType.DIGITS_AND_SIGN);

        }
        for (String s : latin_digits) {
            assertEquals(map.get(s), CharacterType.DIGITS_AND_SIGN);

        }
        for (String s : other_sign) {
            assertEquals(map.get(s), CharacterType.DIGITS_AND_SIGN);

        }


    }

    @Test
    public void testKhmerWord() {

        String word1 = "ធានាអះអាង";
        String word2 = "កិច្ចសិក្សា";

        KhmerWord khmerWord = new KhmerWord(word1);
        KhmerWord khmerWord1 = new KhmerWord(word2);

        KhmerWord khmerWord3 = khmerWord.addKhmerCharacterToWord(khmerWord1.getKhmerWord());


        assertEquals(khmerWord.getKhmerWord(), "ធានាអះអាង");
        assertEquals(khmerWord1.getKhmerWord(), "កិច្ចសិក្សា");
        assertEquals(khmerWord3.getKhmerWord(), "ធានាអះអាងកិច្ចសិក្សា");


    }

    @Test
    public void testTrie() {

        String word1 = "apple";
        String word2 = "apple2";
        String word3 = "bag";
        String word4 = "baggage";
        String word5 = "beautiful";
        String word6 = "beat";
        String word7 = "come";
        String word8 = "comet";
        String word9 = "baka";
        String word0 = "bakajanai";

        Trie trie = new Trie();

        trie.addWord(word0, 1);
        trie.addWord(word1, 1);
        trie.addWord(word2, 1);
        trie.addWord(word3, 1);
        trie.addWord(word4, 1);
        trie.addWord(word5, 1);
        trie.addWord(word6, 1);
        trie.addWord(word7, 1);
        trie.addWord(word8, 1);
        trie.addWord(word9, 1);

        ///Allword
        assertEquals(trie.getAllWords().size(), 10);

        assertTrue(trie.isPrefix(word3));
        assertTrue(trie.isWord(word1));
        assertFalse(trie.isWord("apple2"));
        assertEquals(Objects.requireNonNull(trie.searchWord(word1)).getAllWords().size(), 2);

//        for (String com :trie.getAllWords()) {
//            System.out.println(com);
//
//        }

    }
}