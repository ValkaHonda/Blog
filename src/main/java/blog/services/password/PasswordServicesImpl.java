package blog.services.password;

import org.springframework.stereotype.Service;

@Service
public class PasswordServicesImpl implements PasswordServices {
    private static final int PASS_LENGTH = 8;

    public String generatePassword() {
        return this.generatePassword(PASS_LENGTH);
    }
    @Override
    public String generatePassword(int length) {
       return this.generatePassword(length,true,true,true,true);
    }
    @Override
    public String generatePassword(int length, boolean useDigits, boolean useLower
            , boolean usePunctuation, boolean useUpper) {
        PasswordGeneratorImpl.PasswordGeneratorBuilder builder = new PasswordGeneratorImpl.PasswordGeneratorBuilder();
        builder.useDigits(useDigits);
        builder.useLower(useLower);
        builder.usePunctuation(usePunctuation);
        builder.useUpper(useUpper);
        PasswordGenerator passwordGenerator = new PasswordGeneratorImpl(builder);
        return passwordGenerator.generate(length);
    }
}
