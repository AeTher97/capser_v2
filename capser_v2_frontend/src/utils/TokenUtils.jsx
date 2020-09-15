import * as jwt from "jsonwebtoken";

const errorString = 'Nastąpiło wylogowanie, zaloguj się ponownie';
export const decodeToken = (accessToken) => {
    try {
        const {sub, rol, exp} = jwt.decode(accessToken);

        return {
            userId: sub,
            roles: rol,
            expirationTime: exp
        };
    } catch (e) {
        return {
            error: errorString
        };
    }
}

export const saveTokenInStorage = (accessToken, refreshToken, email) => {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
    localStorage.setItem('email', email);
}

export const isTokenOutdated = (exp) => {
    return exp - 10< (new Date().getTime() / 1000) ;
}

export const getHeaders = (token) => {
    return `Bearer ${token}`;

}

