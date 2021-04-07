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
        return "Type in a correct email address";
    } else {
        return null
    }
}


export const validateLength = (word, lengthMin, lengthMax = Infinity) => {
    if (word.length < lengthMin) {
        return "Lenght of " + lengthMin + " required"
    } else if (word.length > lengthMax) {
        return "Text cannot be longer than " + lengthMax + " characters."
    }
    {
        return null;
    }
}

export const phoneNumberProcessor = (word) => {
    return word.replace(/[^\d+]/g, '');
}

export const floatNumberProcessor = (word) => {
    return word.replace(/[^\d+.]/g, '');
}
