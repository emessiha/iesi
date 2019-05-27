package io.metadew.iesi.data.generation.configuration;

import io.metadew.iesi.data.generation.execution.GenerationComponentExecution;
import io.metadew.iesi.data.generation.execution.GenerationDataExecution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Internet extends GenerationComponentExecution {

    private static final String SAFE_EMAIL_HOST = "example.";
    private static final List<String> SAFE_EMAIL_TLDS = Arrays.asList("org", "com", "net");
    private static final List<String> DEFAULT_SEPARATORS = Arrays.asList(".", "_");
    private static final List<String> DEFAULT_SLUG_GLUE = Arrays.asList(".", "_", "-");
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 16;
    private static final boolean PASSWORD_MIX_CASE = true;
    private static final boolean PASSWORD_SPECIAL_CHARS = false;
    private static final List<String> PASSWORD_SPECIAL_CHARS_LIST
            = Arrays.asList("!", "@", "#", "$", "%", "^", "&", "*");
    private static final int DEVICE_TOKEN_LENGTH = 64;

    public Internet(GenerationDataExecution execution) {
        super(execution);
    }

    public String email() {
        return email(null);
    }

    public String email(String name) {
        return userName(name) + "@" + domainName();
    }

    public String freeEmail() {
        return freeEmail(null);
    }

    public String freeEmail(String name) {
        return userName(name) + "@" + fetch("internet.free_email");
    }

    public String safeEmail() {
        return safeEmail(null);
    }

    public String safeEmail(String name) {
        return userName(name) + "@" + SAFE_EMAIL_HOST + this.getGenerationTools().getRandomTools().sample(SAFE_EMAIL_TLDS);
    }

    public String userName() {
        return userName(null);
    }

    public String userName(String specifier) {
        return userName(specifier, DEFAULT_SEPARATORS);
    }

    public String userName(String specifier, List<String> separators) {
        String separator = this.getGenerationTools().getRandomTools().sample(separators);

        if (specifier != null) {
            List<String> words = Arrays.asList(specifier.split("\\s"));
            List<String> normalizedWords = new ArrayList<>(words.size());
            for (String word : words) {
                normalizedWords.add(this.getGenerationTools().getStringTools().normalize(word));
            }
            return this.getGenerationTools().getStringTools().join(normalizedWords, separator);

        } else if (this.getGenerationTools().getRandomTools().randBoolean()) {
            return this.getGenerationTools().getStringTools().normalize(call("Name.first_name"))
                    + separator
                    + this.getGenerationTools().getStringTools().normalize(call("Name.last_name"));

        } else {
            return this.getGenerationTools().getStringTools().normalize(call("Name.first_name"));
        }
    }

    public String password() {
        return password(PASSWORD_MIN_LENGTH);
    }

    public String password(int minLength) {
        return password(minLength, PASSWORD_MAX_LENGTH);
    }

    public String password(int minLength, int maxLength) {
        return password(minLength, maxLength, PASSWORD_MIX_CASE);
    }

    public String password(int minLength, int maxLength, boolean mixCase) {
        return password(minLength, maxLength, mixCase, PASSWORD_SPECIAL_CHARS);
    }

    public String password(int minLength, int maxLength, boolean mixCase, boolean specialChars) {
        int characterCount = this.getGenerationTools().getRandomTools().range(minLength, maxLength);
        String password = getComponent(Lorem.class).characters(characterCount);

        if (mixCase && password.length() >= 2) {
            int middlePoint = this.getGenerationTools().getRandomTools().number(password.length() - 1) + 1;
            password = password.substring(0, middlePoint).toLowerCase() +
                    password.substring(middlePoint).toUpperCase();
        }

        if (specialChars && password.length() >= 2) {
            int numSpecialChars = this.getGenerationTools().getRandomTools().number(password.length() - 1) + 1;
            for (int i = 0; i < numSpecialChars; i++) {
                String specialChar = this.getGenerationTools().getRandomTools().sample(PASSWORD_SPECIAL_CHARS_LIST);
                int index = this.getGenerationTools().getRandomTools().number(password.length());
                password = password.substring(0, index).toLowerCase() + specialChar +
                        password.substring(index + 1).toUpperCase();
            }
        }

        return password;
    }

    public String domainName() {
        return domainWord() + '.' + domainSuffix();
    }

    public String domainWord() {
        String companyName = call("Company.name");
        return this.getGenerationTools().getStringTools().normalize(companyName);
    }

    public String domainSuffix() {
        return fetch("internet.domain_suffix");
    }

    public String macAddress() {
        return macAddress("");
    }

    public String macAddress(String prefix) {
        List<String> prefixDigits;
        if (prefix.length() > 0) {
            prefixDigits = Arrays.asList(prefix.split(":"));
        } else {
            prefixDigits = new ArrayList<>();
        }

        int addressDigitsCount = (6 - prefixDigits.size());
        List<String> addressDigits = new ArrayList<>(addressDigitsCount);
        for (int i = 0; i < addressDigitsCount; i++) {
            addressDigits.add(String.format("%02x", this.getGenerationTools().getRandomTools().number(256)));
        }

        addressDigits.addAll(0, prefixDigits);
        return this.getGenerationTools().getStringTools().join(addressDigits, ":");
    }

    public String ipV4Address() {
        List<String> parts = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            parts.add(Integer.toString(this.getGenerationTools().getRandomTools().number(255)));
        }
        return this.getGenerationTools().getStringTools().join(parts, ".");
    }

    public String ipV4Cidr() {
        return ipV4Address() + "/" + this.getGenerationTools().getRandomTools().range(1, 32);
    }

    public String ipV6Address() {
        List<String> parts = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            parts.add(String.format("%x", this.getGenerationTools().getRandomTools().number(65536)));
        }
        return this.getGenerationTools().getStringTools().join(parts, ":");
    }

    public String ipV6Cidr() {
        return ipV6Address() + "/" + this.getGenerationTools().getRandomTools().range(1, 128);
    }

    public String url() {
        return url(domainName());
    }

    public String url(String host) {
        return url(host, "/" + userName());
    }

    public String url(String host, String path) {
        return "http://" + host + path;
    }

    public String slug() {
        return slug(null);
    }

    public String slug(List<String> words) {
        return slug(words, null);
    }

    public String slug(List<String> words, String glue) {
        if (glue == null) {
            glue = this.getGenerationTools().getRandomTools().sample(DEFAULT_SLUG_GLUE);
        }

        if (words == null || words.isEmpty()) {
            words = Arrays.asList(fetch("lorem.words"), fetch("lorem.words"));
        }

        return this.getGenerationTools().getStringTools().join(words, glue);
    }

    public String deviceToken() {
        StringBuilder deviceToken = new StringBuilder();
        for (int i = 0; i < DEVICE_TOKEN_LENGTH; i++) {
            deviceToken.append(String.format("%x", this.getGenerationTools().getRandomTools().number(16)));
        }
        return deviceToken.toString();
    }
}
