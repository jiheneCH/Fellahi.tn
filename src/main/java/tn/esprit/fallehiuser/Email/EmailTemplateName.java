package tn.esprit.fallehiuser.Email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("ACTIVATE_ACCOUNT");
    // If you have more templates, they can be added here.

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
