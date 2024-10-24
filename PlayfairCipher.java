import java.awt.Point;
import java.util.Scanner;

public class PlayfairCipher {
    // length of digraph array
    private int length = 0;
    // creates a matrix for Playfair cipher
    private String[][] table;

    // main() method to test Playfair method
    public static void main(String args[]) {
        new PlayfairCipher();
    }

    // main run of the program, Playfair method
    // constructor of the class
    private PlayfairCipher() {
        // prompts user for the keyword to use for encoding & creates tables
        System.out.print("Enter the key for playfair cipher: ");
        Scanner sc = new Scanner(System.in);
        String key = parseString(sc);
        while (key.equals("")) key = parseString(sc);
        table = this.cipherTable(key);
        // prompts user for message to be encoded
        System.out.print("Enter the plaintext to be enciphered: ");
        String input = parseString(sc);
        while (input.equals("")) input = parseString(sc);
        // encodes and then decodes the encoded message
        String output = cipher(input);
        String decodedOutput = decode(output);
        // output the results to user
        this.keyTable(table);
        this.printResults(output, decodedOutput);
    }

    // parses an input string to remove numbers, punctuation,
    // replaces any J's with I's and makes string all caps
    private String parseString(Scanner sc) {
        String parse = sc.nextLine();
        // converts all the letters in upper case
        parse = parse.toUpperCase();
        // the string to be substituted by space for each match (A to Z)
        parse = parse.replaceAll("[^A-Z]", "");
        // replace the letter J by I
        parse = parse.replace("J", "I");
        return parse;
    }

    // creates the cipher table based on some input string (already parsed)
    private String[][] cipherTable(String key) {
        // creates a matrix of 5*5
        String[][] playfairTable = new String[5][5];
        String keyString = key + "ABCDEFGHIKLMNOPQRSTUVWXYZ";
        // fill string array with empty string
        for (int i = 0; i < 5; i++) 
            for (int j = 0; j < 5; j++) 
                playfairTable[i][j] = "";
                
        for (int k = 0; k < keyString.length(); k++) {
            boolean repeat = false;
            boolean used = false;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (playfairTable[i][j].equals("" + keyString.charAt(k))) {
                        repeat = true;
                    } else if (playfairTable[i][j].equals("") && !repeat && !used) {
                        playfairTable[i][j] = "" + keyString.charAt(k);
                        used = true;
                    }
                }
            }
        }
        return playfairTable;
    }

    // cipher: takes input (all upper-case), encodes it, and returns the output
    private String cipher(String in) {
        length = (int) in.length() / 2 + in.length() % 2;
        // insert 'X' between double-letter digraphs & redefines "length"
        for (int i = 0; i < (length - 1); i++) {
            if (in.charAt(2 * i) == in.charAt(2 * i + 1)) {
                in = new StringBuffer(in).insert(2 * i + 1, 'X').toString();
                length = (int) in.length() / 2 + in.length() % 2;
            }
        }
        // creates an array of digraphs
        String[] digraph = new String[length];
        for (int j = 0; j < length; j++) {
            if (j == (length - 1) && in.length() / 2 == (length - 1))
                in = in + "X"; // appends 'X' if plaintext length is odd
            digraph[j] = in.charAt(2 * j) + "" + in.charAt(2 * j + 1);
        }
        // encodes the digraphs and returns the output
        String out = "";
        String[] encDigraphs = encodeDigraph(digraph);
        for (int k = 0; k < length; k++)
            out = out + encDigraphs[k];
        return out;
    }

    // encodes the digraph input with the cipher's specifications
    private String[] encodeDigraph(String[] di) {
        String[] encipher = new String[length];
        for (int i = 0; i < length; i++) {
            char a = di[i].charAt(0);
            char b = di[i].charAt(1);
            int r1 = (int) getPoint(a).getX();
            int r2 = (int) getPoint(b).getX();
            int c1 = (int) getPoint(a).getY();
            int c2 = (int) getPoint(b).getY();
            // same row: shift columns to right
            if (r1 == r2) {
                c1 = (c1 + 1) % 5;
                c2 = (c2 + 1) % 5;
            } 
            // same column: shift rows down
            else if (c1 == c2) {
                r1 = (r1 + 1) % 5;
                r2 = (r2 + 1) % 5;
            } 
            // swap columns
            else {
                int temp = c1;
                c1 = c2;
                c2 = temp;
            }
            encipher[i] = table[r1][c1] + "" + table[r2][c2];
        }
        return encipher;
    }

    // decodes the output given from the cipher and decode methods
    private String decode(String out) {
        String decoded = "";
        for (int i = 0; i < out.length() / 2; i++) {
            char a = out.charAt(2 * i);
            char b = out.charAt(2 * i + 1);
            int r1 = (int) getPoint(a).getX();
            int r2 = (int) getPoint(b).getX();
            int c1 = (int) getPoint(a).getY();
            int c2 = (int) getPoint(b).getY();
            // same row: shift columns to left
            if (r1 == r2) {
                c1 = (c1 + 4) % 5;
                c2 = (c2 + 4) % 5;
            } 
            // same column: shift rows up
            else if (c1 == c2) {
                r1 = (r1 + 4) % 5;
                r2 = (r2 + 4) % 5;
            } 
            // swap columns
            else {
                int temp = c1;
                c1 = c2;
                c2 = temp;
            }
            decoded = decoded + table[r1][c1] + table[r2][c2];
        }
        return decoded;
    }

    // returns a point containing the row and column of the letter
    private Point getPoint(char c) {
        Point pt = new Point(0, 0);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (c == table[i][j].charAt(0))
                    pt = new Point(i, j);
            }
        }
        return pt;
    }

    // function prints the key-table in matrix form for playfair cipher
    private void keyTable(String[][] printTable) {
        System.out.println("Playfair Cipher Key Matrix: ");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(printTable[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // method that prints all the results
    private void printResults(String encipher, String dec) {
        System.out.println("Encrypted Message: " + encipher);
        System.out.println("Decrypted Message: " + dec);
    }
}


// Enter the key for playfair cipher: test
// Enter the plaintext to be enciphered: This is Final Exam
// Playfair Cipher Key Matrix: 
// T E S A B
// C D F G H
// I K L M N
// O P Q R U
// V W X Y Z

// Encrypted Message: BCLTLTCLMBKSYSLY
// Decrypted Message: THISISFINALEXAMX