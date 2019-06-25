package com.namae0Two.khmeralternativekeyboard;

import com.namae0Two.khmeralternativekeyboard.data.ButtonData;
import com.namae0Two.khmeralternativekeyboard.data.ButtonType;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void characterButtonDataTest(){
        ButtonData b = new ButtonData(ButtonType.ALPHABET_TYPE,40);
        //Initialization
        assertEquals("",b.getBottom());
        assertEquals("",b.getTop());
        assertEquals("",b.getRight());
        assertEquals("",b.getMiddle());
        assertEquals("",b.getLeft());
        //Setting Value
        b.setMiddle("A");
        b.setRight("B");
        //Test Set Value

        assertEquals("A",b.getMiddle());
        assertEquals("B",b.getRight());
        assertEquals(b.getAllChars().size(),2);





    }
}