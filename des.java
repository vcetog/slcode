import java.util.*;

class Main {
    private static class DES {
        // CONSTANTS
        // Initial Permutation Table
        int[] IP = { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4,
                     62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8,
                     57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
                     61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };

        // Inverse Initial Permutation Table
        int[] IP1 = { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31,
                      38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29,
                      36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27,
                      34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25 };

        // First key permutation table
        int[] PC1 = { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18,
                      10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36,
                      63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22,
                      14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4 };

        // Second key permutation table
        int[] PC2 = { 14, 17, 11, 24, 1, 5, 3, 28,
                      15, 6, 21, 10, 23, 19, 12, 4,
                      26, 8, 16, 7, 27, 20, 13, 2,
                      41, 52, 31, 37, 47, 55, 30, 40,
                      51, 45, 33, 48, 44, 49, 39, 56,
                      34, 53, 46, 42, 50, 36, 29, 32 };

        // Expansion D-box Table
        int[] EP = { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9,
                     8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17,
                     16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25,
                     24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 };

        // Straight Permutation Table
        int[] P = { 16, 7, 20, 21, 29, 12, 28, 17,
                    1, 15, 23, 26, 5, 18, 31, 10,
                    2, 8, 24, 14, 32, 27, 3, 9,
                    19, 13, 30, 6, 22, 11, 4, 25 };

        // S-box Table
        int[][][] sbox = {
                { { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
                  { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
                  { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
                  { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } },
                
                // The rest of the S-boxes...
                { { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
                  { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
                  { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
                  { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } },

                // Add all 8 S-boxes here...
        };

        // Shift bits per round
        int[] shiftBits = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };

        // Hexadecimal to binary conversion
        String hextoBin(String input) {
            int n = input.length() * 4;
            input = Long.toBinaryString(Long.parseUnsignedLong(input, 16));
            while (input.length() < n)
                input = "0" + input;
            return input;
        }

        // Binary to hexadecimal conversion
        String binToHex(String input) {
            int n = (int) input.length() / 4;
            input = Long.toHexString(Long.parseUnsignedLong(input, 2));
            while (input.length() < n)
                input = "0" + input;
            return input;
        }

        // Permutation function according to specified sequence
        String permutation(int[] sequence, String input) {
            String output = "";
            input = hextoBin(input);
            for (int i = 0; i < sequence.length; i++)
                output += input.charAt(sequence[i] - 1);
            output = binToHex(output);
            return output;
        }

        // XOR of two hexadecimal strings
        String xor(String a, String b) {
            long t_a = Long.parseUnsignedLong(a, 16);
            long t_b = Long.parseUnsignedLong(b, 16);
            t_a = t_a ^ t_b;
            a = Long.toHexString(t_a);
            while (a.length() < b.length())
                a = "0" + a;
            return a;
        }

        // Left Circular Shifting bits
        String leftCircularShift(String input, int numBits) {
            String binaryInput = hextoBin(input);
            String shifted = binaryInput.substring(numBits) + binaryInput.substring(0, numBits);
            return binToHex(shifted);
        }

        // Preparing 16 keys for 16 rounds
        String[] getKeys(String key) {
            String keys[] = new String[16];
            key = permutation(PC1, key);
            for (int i = 0; i < 16; i++) {
                String left = leftCircularShift(key.substring(0, 7), shiftBits[i]);
                String right = leftCircularShift(key.substring(7), shiftBits[i]);
                key = left + right;
                keys[i] = permutation(PC2, key);
            }
            return keys;
        }

        // Function to perform S-box substitution
        String sBox(String input) {
            String output = "";
            input = hextoBin(input);
            for (int i = 0; i < 48; i += 6) {
                String temp = input.substring(i, i + 6);
                int num = i / 6;
                int row = Integer.parseInt(temp.charAt(0) + "" + temp.charAt(5), 2);
                int col = Integer.parseInt(temp.substring(1, 5), 2);
                output += Integer.toHexString(sbox[num][row][col]);
            }
            return output;
        }

        // Round function
        String round(String input, String key, int num) {
            String left = input.substring(0, 8);
            String temp = input.substring(8);
            String right = temp;
            temp = permutation(EP, temp);
            temp = xor(temp, key);
            temp = sBox(temp);
            temp = permutation(P, temp);
            left = xor(left, temp);
            return right + left;
        }

        // Encryption function
        String encrypt(String plainText, String key) {
            int i;
            String keys[] = getKeys(key);
            plainText = permutation(IP, plainText);
            for (i = 0; i < 16; i++)
                plainText = round(plainText, keys[i], i);
            plainText = plainText.substring(8) + plainText.substring(0, 8);
            plainText = permutation(IP1, plainText);
            return plainText;
        }
    }

    public static void main(String args[]) {
        DES des = new DES();
        String text = "123456ABCD132536";  // Example plaintext
        String key = "AABB09182736CCDD";   // Example key
        System.out.println("Encryption:\nCipher Text: " + des.encrypt(text, key));
    }
}
