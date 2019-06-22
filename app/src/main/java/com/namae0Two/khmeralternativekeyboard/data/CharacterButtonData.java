package com.namae0Two.khmeralternativekeyboard.data;

import java.util.ArrayList;
import java.util.List;

//This class information of the character in the button
public class CharacterButtonData {

    List<String> characterList;
    String middle;
    String top;
    String right;
    String bottom;
    String left;

    public CharacterButtonData() {
        middle = "";
        top = "";
        right = "";
        bottom = "";
        left = "";
        characterList = new ArrayList<>();
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
        characterList.add(middle);
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
        characterList.add(top);

    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
        characterList.add(right);

    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
        characterList.add(bottom);

    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
        characterList.add(left);

    }

    public List<String> getAll(){

        return characterList;
    }


}
