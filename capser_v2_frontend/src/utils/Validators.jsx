const EMAIL_PATTERN = /^[\w!#$%&’*+/=?`{|}~^-]+(?:\.[\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,6}$/;


export const validatePassword = (password) => {



    if (password.length < 8) {
        return "Password has to be at least 8 characters long"
    }

    return null;

}

export const validateRepeatedPassword = (password) => (repeatedPassword) => {
    if (repeatedPassword !== password) {
        return "Passwords don't match";
    } else {
        return null;
    }
};

export const validateEmail = (email) => {
    if (!EMAIL_PATTERN.test(email)) {
        return "Podaj poprawny adres email";
    } else {
        return null
    }
}

export const validateLength = (word, length) => {
    if (word.length < length) {
        return "Lenght of " + length + " required"
    } else {
        return null;
    }
}

export const phoneNumberProcessor = (word) => {
    return word.replace(/[^\d+]/g, '');
}

export const floatNumberProcessor = (word) => {
    return word.replace(/[^\d+.]/g, '');
}
