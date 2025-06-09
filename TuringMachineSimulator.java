import java.util.*;

/**
 * Bu sınıf, bir Turing makinesinin davranışını simüle eder.
 * Kullanıcıdan bir PIN alır ve sistem PIN'i ile karşılaştırır.
 */
public class TuringMachineSimulator {

    // Turing alfabesi karakterleri
    private static final char BLANK = 'B';
    private static final char SEPARATOR = '#';
    private static final String SYSTEM_PIN = "1234";

    // Durumlar
    private enum State {
        START,
        READ_USER_PIN,
        MOVE_TO_SYSTEM_PIN,
        COMPARE_DIGITS,
        ACCEPT,
        REJECT
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Lütfen 4 haneli PIN kodunuzu girin: ");
        String userPIN = scanner.nextLine();

        if (!userPIN.matches("\\d{4}")) {
            System.out.println("Hatalı giriş! PIN 4 basamaklı olmalıdır.");
            return;
        }

        List<Character> tape = initializeTape(userPIN);
        boolean result = runTuringMachine(tape);

        if (result) {
            System.out.println("Şifre doğru! (KABUL EDİLDİ)");
        } else {
            System.out.println("Şifre hatalı! (REDDEDİLDİ)");
        }
    }

    /**
     * Bantı başlatır: #KULLANICI_PIN#SISTEM_PIN#
     */
    private static List<Character> initializeTape(String userPIN) {
        String fullInput = SEPARATOR + userPIN + SEPARATOR + SYSTEM_PIN + SEPARATOR;
        List<Character> tape = new ArrayList<>();
        for (char c : fullInput.toCharArray()) {
            tape.add(c);
        }
        return tape;
    }

    /**
     * Turing makinesini çalıştırır.
     */
    private static boolean runTuringMachine(List<Character> tape) {
        int head = 0;
        State state = State.START;

        int userStart = 1;              // Kullanıcı PIN'i # sonrası başlar
        int systemStart = userStart + 5; // 4 rakam + 1 # sonrası sistem PIN'i başlar
        int index = 0;

        while (true) {
            switch (state) {
                case START:
                    if (tape.get(head) == SEPARATOR) {
                        state = State.READ_USER_PIN;
                        head = userStart;
                    } else {
                        state = State.REJECT;
                    }
                    break;

                case READ_USER_PIN:
                    if (index < 4) {
                        char userDigit = tape.get(userStart + index);
                        if (!Character.isDigit(userDigit)) {
                            state = State.REJECT;
                        } else {
                            state = State.MOVE_TO_SYSTEM_PIN;
                        }
                    } else {
                        state = State.ACCEPT;
                    }
                    break;

                case MOVE_TO_SYSTEM_PIN:
                    state = State.COMPARE_DIGITS;
                    break;

                case COMPARE_DIGITS:
                    char userDigit = tape.get(userStart + index);
                    char systemDigit = tape.get(systemStart + index);

                    if (userDigit == systemDigit) {
                        index++;
                        state = State.READ_USER_PIN;
                    } else {
                        state = State.REJECT;
                    }
                    break;

                case ACCEPT:
                    return true;

                case REJECT:
                    return false;
            }
        }
    }
}
