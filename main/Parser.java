package main;

import format.ColouredString;
import java.awt.Color;
import java.util.ArrayList;

public class Parser {
    
    public static ArrayList<ColouredString> Parse(String input) {
        
        ArrayList<ColouredString> result = new ArrayList<ColouredString>();
        
        String keywords = "abstract continue for new switch " +
            "assert default goto package synchronized " +
            "boolean do if private this " +
            "break double implements protected throw " +
            "byte else import public throws " +
            "case enum instanceof return transient " +
            "catch extends int short try " +
            "char final interface static void " +
            "class finally long strictfp volatile " +
            "const float native super ";
        
        for (String i : input.split(" ")) {
            boolean found = false;
            for (String j : keywords.split(" ")) {
                if (i.equals(j)) 
                {
                    result.add(new ColouredString(i, Color.blue));
                    found = true;
                }
            }    
            if (!found) {
                result.add(new ColouredString(i, Color.black));
            }
        }
        
        return result;
    }
}
