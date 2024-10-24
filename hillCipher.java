import java.util.Scanner;

public class hillCipher {
    // 3x3 key matrix for 3 characters at once
    public static int[][] keymat = new int[][] {{1, 2, 1}, {2, 3, 2}, {2, 2, 1}};
    // Key inverse matrix
    public static int[][] invkeymat = new int[][] {{1, 0, 1}, {2, -1, 0}, {-2, 2, -1}};
    public static String key = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static String encode(char a, char b, char c) {
        String ret = "";
        int x, y, z;
        int posa = (int) a - 65;
        int posb = (int) b - 65;
        int posc = (int) c - 65;
        x = posa * keymat[0][0] + posb * keymat[1][0] + posc * keymat[2][0];
        y = posa * keymat[0][1] + posb * keymat[1][1] + posc * keymat[2][1];
        z = posa * keymat[0][2] + posb * keymat[1][2] + posc * keymat[2][2];
        a = key.charAt(x % 26);
        b = key.charAt(y % 26);
        c = key.charAt(z % 26);
        ret += a + b + c;
        return ret;
    }

    private static String decode(char a, char b, char c) {
        String ret = "";
        int x, y, z;
        int posa = (int) a - 65;
        int posb = (int) b - 65;
        int posc = (int) c - 65;
        x = posa * invkeymat[0][0] + posb * invkeymat[1][0] + posc * invkeymat[2][0];
        y = posa * invkeymat[0][1] + posb * invkeymat[1][1] + posc * invkeymat[2][1];
        z = posa * invkeymat[0][2] + posb * invkeymat[1][2] + posc * invkeymat[2][2];
        a = key.charAt((x % 26 < 0) ? (26 + x % 26) : (x % 26));
        b = key.charAt((y % 26 < 0) ? (26 + y % 26) : (y % 26));
        c = key.charAt((z % 26 < 0) ? (26 + z % 26) : (z % 26));
        ret += a + b + c;
        return ret;
    }

    public static void main(String[] args) throws java.lang.Exception {
        String msg;
        String enc = "";
        String dec = "";
        int n;
        Scanner sc = new Scanner(System.in);

        System.out.println("Simulation of Hill Cipher");
        System.out.print("Input message: ");
        msg = sc.nextLine();
        
        msg = msg.toUpperCase().replaceAll("\\s", ""); // Remove spaces
        n = msg.length() % 3;
        
        // Append padding text 'X'
        if (n != 0) {
            msg = msg + "X".repeat(3 - n);
        }

        System.out.println("Padded message: " + msg);

        char[] pdchars = msg.toCharArray();
        
        for (int i = 0; i < msg.length(); i += 3) {
            enc += encode(pdchars[i], pdchars[i + 1], pdchars[i + 2]);
        }

        System.out.println("Encoded message: " + enc);

        char[] dechats = enc.toCharArray();
        
        for (int i = 0; i < enc.length(); i += 3) {
            dec += decode(dechats[i], dechats[i + 1], dechats[i + 2]);
        }

        System.out.println("Decoded message: " + dec);
    }
}

// Simulation of Hill Cipher
// Input message: attack
// Padded message: ATTACK
// Encoded message: 241233
// Decoded message: 220243