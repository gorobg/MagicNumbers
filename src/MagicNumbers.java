
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MagicNumbers {
    static boolean exit = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Amazing Numbers!\n" +
                "\nSupported requests:\n" +
                "- enter a natural number to know its properties;\n" +
                "- enter two natural numbers to obtain the properties of the list:\n" +
                "  * the first parameter represents a starting number;\n" +
                "  * the second parameter shows how many consecutive numbers are to be processed;\n" +
                "- two natural numbers and properties to search for;\n" +
                "- a property preceded by minus must not be present in numbers;\n" +
                "- separate the parameters with one space;\n" +
                "- enter 0 to exit.");
        while (!exit) {
            Number number = new Number();
            System.out.printf("%nEnter a request: ");
            inputInterpreter(scanner.nextLine(), number);
        }
    }

    static void inputInterpreter(String input, Number n) {

        switch (input) {
            case "0":
                exit = true;
                System.out.println("\nGoodbye!");
                break;
            default:
                ArrayList<String> inpList = new ArrayList<>(Arrays.asList(input.split(" ")));
                inpList.replaceAll(str -> str.toUpperCase());

                try {
                    n.setNumber(Long.parseLong(inpList.get(0)));
                    if (!n.isNatural()) {
                        throw new Exception();
                    }
                } catch (Exception ex) {
                    System.out.println("\nThe first parameter should be a natural number or zero.");
                    break;
                }

                if (inpList.size() > 1) {
                    Number length = new Number();
                    try {
                        length.setNumber(Long.parseLong(inpList.get(1)));
                        if (!length.isNatural() || length.getNumber() == 0) {
                            throw new Exception();
                        }
                    } catch (Exception ex) {
                        System.out.println("\nThe second parameter should be a natural number.");
                        break;
                    }
                    if (inpList.size() > 2) {
                        if (!Number.availableProps.containsAll(inpList.subList(2, inpList.size()))) {
                            ArrayList<String> invalidProps = new ArrayList<>();
                            for (int i = 2; i < inpList.size(); i++) {
                                if (!Number.availableProps.contains(inpList.get(i).toUpperCase())) {
                                    invalidProps.add(inpList.get(i).toUpperCase());
                                }
                            }
                            System.out.printf("%nThe %s %s %s%n%s%n", invalidProps.size() > 1 ? "properties are" : "property", invalidProps,
                                    invalidProps.size() > 1 ? "are wrong." : "is wrong.", "Available properties:" + n.availableProps.subList(0, 12));
                            break;
                        }
                    }
                    if (inpList.size() > 3) {
                        List<String> exclusives = new ArrayList<>();
                        for (String str : inpList.subList(2, inpList.size() - 1)) {
                            if (inpList.contains("-" + str)) {
                                exclusives.add(str);
                                exclusives.add("-" + str);
                            }
                            if (inpList.contains(str.substring(1))) {
                                exclusives.add(str);
                                exclusives.add(str.substring(1));
                            }
                        }
                        exclusives.addAll(inpList.contains("ODD") && inpList.contains("EVEN") ? Arrays.asList("ODD EVEN".split(" ")) :
                                inpList.contains("-ODD") && inpList.contains("-EVEN") ? Arrays.asList("-ODD -EVEN".split(" ")) :
                                        inpList.contains("SUNNY") && inpList.contains("SQUARE") ? Arrays.asList("SUNNY SQUARE".split(" ")) :
                                                inpList.contains("SPY") && inpList.contains("DUCK") ? Arrays.asList("SPY DUCK".split(" ")) :
                                                        inpList.contains("-SAD") && inpList.contains("-HAPPY") ? Arrays.asList("-SAD -HAPPY".split(" ")) :
                                                                inpList.contains("SAD") && inpList.contains("HAPPY") ? Arrays.asList("SAD HAPPY".split(" ")) : Arrays.asList("".split("")));
                        exclusives.remove("");
                        if (!exclusives.isEmpty()) {
                            System.out.println("\nThe request contains mutually exclusive properties: " + exclusives +
                                    "\nThere are no numbers with these properties.");
                            break;
                        }
                    }
                    n.printProperties(length, inpList.subList(2, inpList.size()));
                } else {
                    n.printProperties();
                }
                break;
        }
    }
}

class Number {
    static ArrayList<String> availableProps = new ArrayList<>(Arrays.asList(
            new String[]{"BUZZ", "DUCK", "PALINDROMIC", "GAPFUL", "SPY", "SQUARE", "SUNNY", "EVEN", "HAPPY", "SAD", "ODD", "JUMPING",
                    "-BUZZ", "-DUCK", "-PALINDROMIC", "-GAPFUL", "-SPY", "-SQUARE", "-SUNNY", "-EVEN", "-HAPPY", "-SAD", "-ODD", "-JUMPING"}));
    private long number;
    private ArrayList<String> properties = new ArrayList<>();

    boolean checkEven() {
        return this.number % 2 == 0;
    }

    boolean isDivBySeven() {
        return this.number % 7 == 0;
    }

    boolean endWithSeven() {
        return this.number % 10 == 7;
    }

    boolean isNatural() {
        return this.number > 0;
    }

    boolean isGapful() {
        String numberAsString = String.valueOf(this.number);
        String firstAndLastDigit = numberAsString.substring(0, 1) + numberAsString.charAt(numberAsString.length() - 1);
        return numberAsString.length() > 2 ? this.number % Integer.valueOf(firstAndLastDigit) == 0 ? true : false : false;
    }

    //a number is spy, if the sum of all its digits equal the product of all its digits
    boolean isSpy() {
        long sumDgits = 0;
        long productDigits = 1;
        long temp = this.number;
        while (temp > 0) {
            sumDgits += temp % 10;
            productDigits *= temp % 10;
            temp /= 10;
        }
        return sumDgits == productDigits;
    }

    //a duck number  is one that contains zeroes. Leading zeroes don't make it a duck number
    boolean isDuck() {
        String numberAsString = String.valueOf(this.number);
        return numberAsString.contains("0") && numberAsString.charAt(0) != '0';
    }

    //palindromic is a number that reads the same form lef to right and from right to left
    boolean isPalindromic() {
        String numAsString = String.valueOf(this.number);
        for (int i = 0; i <= numAsString.length() / 2; i++) {
            if (numAsString.charAt(i) != numAsString.charAt(numAsString.length() - 1 - i)) {
                return false;
            }
        }
        return true;
    }

    //if the suqre root of the number results in a real number
    boolean isSquare() {
        return Math.sqrt(this.getNumber()) % 1 == 0;
    }

    //if the suqre root of the number + 1 results in a real number
    boolean isSunny() {
        return Math.sqrt(this.getNumber() + 1) % 1 == 0;
    }

    // a number with neighbouring digits differing with no more or less than 1
    boolean isJumping() {
        long temp = this.number;
        for (int i = 1; i < String.valueOf(this.number).length(); i++) {
            if (Math.abs(temp % 10 - temp / 10 % 10) != 1) {
                return false;
            }
            temp /= 10;
        }
        return true;
    }

    boolean isHappy() {
        long temp = this.number;
        while (temp > 9) {
            short numOfDigits = (short) String.valueOf(temp).length();
            long sumSquaredDigits = 0;
            for (int i = 0; i < numOfDigits; i++) {
                long lastDigit = temp % 10;
                sumSquaredDigits += lastDigit * lastDigit;
                temp /= 10;
            }
            temp = sumSquaredDigits;
        }
        return temp == 1 || temp == 7 ? true : false;
    }

    //Method to set the properties of the number
    ArrayList<String> numberPropertiesList() {
        this.properties.add(isDivBySeven() || endWithSeven() ? "BUZZ" : "-BUZZ");
        this.properties.add(isDuck() ? "DUCK" : "-DUCK");
        this.properties.add(isPalindromic() ? "PALINDROMIC" : "-PALINDROMIC");
        this.properties.add(isGapful() ? "GAPFUL" : "-GAPFUL");
        this.properties.add(isSpy() ? "SPY" : "-SPY");
        this.properties.add(isSunny() ? "SUNNY" : "-SUNNY");
        this.properties.add(isSquare() ? "SQUARE" : "-SQUARE");
        this.properties.add(isJumping() ? "JUMPING" : "-JUMPING");
        if (isHappy()) {
            this.properties.add("HAPPY");
            this.properties.add("-SAD");
        } else {
            this.properties.add("SAD");
            this.properties.add("-HAPPY");
        }
        if (checkEven()) {
            this.properties.add("EVEN");
            this.properties.add("-ODD");
        } else {
            this.properties.add("ODD");
            this.properties.add("-EVEN");
        }
        return this.properties;
    }

    //method for printing the properties of single number
    void printProperties() {
        System.out.printf("%nProperties of %d%n%12s: " +
                        "%b%n%12s: %b%n%12s: %b%n%12s: %b%n%12s: %b%n%12s: %b%n%12s: " +
                        "%b%n%12s: %b%n%12s: %b%n%12s: %b%n%12s: %b%n%12s: %b%n",
                this.number,
                "buzz", this.properties.contains("BUZZ"),
                "duck", this.properties.contains("DUCK"),
                "palindromic", this.properties.contains("PALINDROMIC"),
                "gapful", this.properties.contains("GAPFUL"),
                "spy", this.properties.contains("SPY"),
                "square", this.properties.contains("SQUARE"),
                "sunny", this.properties.contains("SUNNY"),
                "jumping", this.properties.contains("JUMPING"),
                "happy", this.properties.contains("HAPPY"),
                "sad", this.properties.contains("SAD"),
                "even", this.properties.contains("EVEN"),
                "odd", this.properties.contains("ODD"));
    }

    //method to print the properties of a list of numbers
    void printProperties(Number length, List<String> requiredProps) {
        System.out.println();
        long temp = this.getNumber();
        for (long i = 0, count = 0; count < length.getNumber(); i++) {
            Number n = new Number();
            if (requiredProps.contains("jUmPiNG".toUpperCase())) {
                n.setNumber(nextJumping(temp));             // for reducing complexity of finding a jumping number
                temp = nextJumping(n.getNumber() + 1);
            } else {
                n.setNumber(temp + i);
            }
            if (n.properties.containsAll(requiredProps)) {   //print the result if all requirements are met
                StringBuilder sbProperties = new StringBuilder();
                n.properties.removeIf(s -> s.startsWith("-")); //remove all props containing "-" from the print
                sbProperties.append(n.properties).deleteCharAt(0).deleteCharAt(sbProperties.length() - 1); //formatting the sb
                System.out.printf("%12d is %s%n", n.getNumber(), sbProperties.toString().toLowerCase());   //formatting the print
                count++;   //increment the counter for found number the have met all requirements
            }
        }
    }

    public long getNumber() {
        return number;
    }

    void setNumber(long number) {
        this.number = number;
        numberPropertiesList();
    }

    /*find next Jumping Number algorithm O_o. Big reduction of program complexity in comparison to checking every
     next number for being jumping*/
    static long nextJumping(Long number) {
        ArrayList<Integer> numAsList = new ArrayList<>(); //add each digit of the number to a list for ease of manipulation
        while (number > 0) {
            numAsList.add((int) (number % 10));
            number /= 10;
        }
        //check each neighbouring digits and set them according to the algorithm
        for (int i = numAsList.size() - 1; i > 0; i--) {
            if (Math.abs(numAsList.get(i) - numAsList.get(i - 1)) != 1) {
                if (numAsList.get(i) - numAsList.get(i - 1) > 1) {
                    numAsList.set(i - 1, numAsList.get(i) - 1);
                    listNullifyHelper(numAsList, i);
                } else {
                    if (numAsList.get(i) == numAsList.get(i - 1)) {
                        if (numAsList.get(i) == 9) {
                            if (i == numAsList.size() - 1) {
                                numAsList.set(i, 0);
                                numAsList.set(i - 1, 1);
                                numAsList.add(1);
                                listNullifyHelper(numAsList, i);
                                i = numAsList.size();
                            } else {
                                numAsList.set(i + 1, numAsList.get(i + 1) + 1);
                                numAsList.set(i, 0);
                                listNullifyHelper(numAsList, i);
                                i = numAsList.size();
                            }
                        } else {
                            numAsList.set(i - 1, numAsList.get(i - 1) + 1);
                            listNullifyHelper(numAsList, i);
                            i = numAsList.size() - 1;
                        }
                    } else {
                        numAsList.set(i, numAsList.get(i) + 1);
                        numAsList.set(i - 1, numAsList.get(i) - 1);
                        listNullifyHelper(numAsList, i);
                        i = numAsList.size();
                    }
                }
            }
        }
        //transform the list of digits, back to the long multi-digit JUMPING number
        for (int i = numAsList.size() - 1; i >= 0; i--) {
            number += numAsList.get(i) * (long) Math.pow(10, i);
        }
        return number;
    }

    //set all values in a list to 0 from the start up to the index parameter
    static void listNullifyHelper(ArrayList<Integer> list, int toIndex) {
        for (int j = 0; j < toIndex - 1; j++) {
            list.set(j, 0);
        }
    }
}
