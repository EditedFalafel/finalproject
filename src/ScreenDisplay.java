import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ScreenDisplay {
    // change this if off-center
    public static int SCREEN_WIDTH = detectWidth();
    public static final int INNER = 58; // box width
    private static final Scanner in = new Scanner(System.in);

    private static int detectWidth() {
        try {
            String cols = System.getenv("COLUMNS");

            if (cols != null) {
                return Integer.parseInt(cols.trim());
            }

        } catch (Exception ignored) {
        }
        return 100;
    }

    public static String center(String s) {
        int pad = Math.max(0, (SCREEN_WIDTH - s.length()) / 2);
        return " ".repeat(pad) + s;
    }

    public static void print(String s) {
        System.out.println(center(s));
    }

    public static void blank() {
        System.out.println();
    }

    // clears the console
    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static String fit(String s, int width) {
        if (s.length() > width) {
            return s.substring(0, Math.max(0, width - 3)) + "...";
        }
        return s + " ".repeat(width - s.length());
    }

    private static void boxLine(String content) {
        print("| " + fit(content, INNER - 2) + " |");
    }

    public static void boxTop(String title) {
        print("+" + "=".repeat(INNER) + "+");
        boxCentered(title);
        print("+" + "=".repeat(INNER) + "+");
    }

    public static void boxBottom() {
        print("+" + "=".repeat(INNER) + "+");
    }

    public static void boxSeparator() {
        print("|" + "-".repeat(INNER) + "|");
    }

    public static void boxGap() {
        boxLine("");
    }

    public static void boxText(String text) {
        boxLine(text);
    }

    // wraps long text
    public static void boxWrapped(String prefix, String text) {
        int width = INNER - 2 - prefix.length();
        List<String> lines = wrap(text, width);
        for (int i = 0; i < lines.size(); i++) {
            String lead = (i == 0) ? prefix : " ".repeat(prefix.length());
            boxLine(lead + lines.get(i));
        }

    }

    public static void boxCentered(String text) {
        int inner = INNER - 2;
        if (text.length() > inner) {
            text = text.substring(0, inner);
        }
        int left = (inner - text.length()) / 2;
        boxLine(" ".repeat(left) + text);
    }

    // left and right text
    public static void boxRow(String left, String right) {
        int inner = INNER - 2;
        int space = inner - left.length() - right.length();
        if (space < 1) {
            left = left.substring(0, Math.max(0, inner - right.length() - 4)) + "..";
            space = Math.max(1, inner - left.length() - right.length());
        }
        boxLine(left + " ".repeat(space) + right);
    }

    private static List<String> wrap(String text, int width) {
        List<String> lines = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (String word : text.split(" ")) {
            if (current.length() > 0 && current.length() + 1 + word.length() > width) {
                lines.add(current.toString());
                current = new StringBuilder();
            }

            if (current.length() > 0) {
                current.append(" ");
            }
            current.append(word);
        }

        if (current.length() > 0) {
            lines.add(current.toString());
        }
        return lines;
    }

    // centers lines as one block
    public static void block(List<String> lines) {
        int longest = 0;
        for (String l : lines) {
            longest = Math.max(longest, l.length());
        }
        int pad = Math.max(0, (SCREEN_WIDTH - longest) / 2);
        for (String l : lines) {
            System.out.println(" ".repeat(pad) + l);
        }
    }

    public static String money(double amount) {
        return String.format("Php %.2f", amount);
    }

    public static String time(double minutes) {
        if (minutes == Math.floor(minutes)) {
            return (int) minutes + " min";
        }
        return String.format("%.1f min", minutes);
    }

    // asks until input is valid
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(center(prompt + " "));
            String line = in.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }


            } catch (NumberFormatException ignored) {
            }


            print("Invalid input. Please enter a number from " + min + " to " + max + ".");
        }

    }

    public static void pause() {
        System.out.print(center("Press ENTER to continue..."));
        in.nextLine();
    }
}