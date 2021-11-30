export function emptyStringWhenFalsy(payload) {
    if (!payload) {
        return '';
    }

    return payload;
}
