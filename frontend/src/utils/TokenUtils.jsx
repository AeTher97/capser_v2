import * as jwt from "jsonwebtoken";

const errorString = 'Nastąpiło wylogowanie, zaloguj się ponownie';
export const decodeToken = (accessToken) => {
    try {
        const {sub, rol, exp, email} = jwt.decode(accessToken);

        return {
            userId: sub,
            roles: rol,
            expirationTime: exp,
            email: email,
        };
    } catch (e) {
        return {
            error: errorString
        };
    }
}

export const saveTokenInStorage = (accessToken, refreshToken, email, username) => {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
    localStorage.setItem('email', email);
    localStorage.setItem('username', username);
}

export const saveUserParametersInStorage = (email, username) => {
    localStorage.setItem('email', email);
    localStorage.setItem('username', username);
}

export const isTokenOutdated = (exp) => {
    return exp - 10 < (new Date().getTime() / 1000);
}

export const getHeaders = (token) => {
    return `Bearer ${token}`;

}

