package blog.services.password;

public interface PasswordServices {
    String generatePassword(int length);
    String generatePassword();
    String generatePassword(int length, boolean useDigits, boolean useLower
            , boolean usePunctuation, boolean useUpper);
}
