package com.leo.jtengine.window;

import com.leo.jtengine.maths.DiscreteCoordinates;
import com.leo.jtengine.utils.TerminalLogger;

public class Mouse {

    private static boolean isEscSequence(Integer[] arr) {
        return arr[0] == 27 && arr[1] == 91;
    }

    private static String[] convertToStringArr(Integer[] arr) {
        String converted = "";
        for (Integer integer : arr) {
            converted += (char) integer.intValue();
        }
        converted = converted.replaceAll("[^0-9;]", "");

        return converted.split(";");
    }

    protected static DiscreteCoordinates getClick(Integer[] input) {
        if (input.length >= 9 && isEscSequence(input)) {
            String[] strConverted = convertToStringArr(input);
            
            if (strConverted[0].equals("32")) {
                int x = Integer.parseInt(strConverted[1]) - 1;
                int y = Integer.parseInt(strConverted[2]) - 1;
                return new DiscreteCoordinates(x, y);
            }
        }
        return null;
    }

    protected static DiscreteCoordinates getHover(Integer[] input) {
        if (input.length >= 9 && isEscSequence(input)) {
            String[] strConverted = convertToStringArr(input);
            if (strConverted[0].equals("67")) {
                int x = Integer.parseInt(strConverted[1]) - 1;
                int y = Integer.parseInt(strConverted[2]) - 1;
                return new DiscreteCoordinates(x, y);
            }
        }
        return null;
    }

}
