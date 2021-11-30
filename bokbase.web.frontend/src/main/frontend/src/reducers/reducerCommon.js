export function nullWhenFalsy(payload) {
    if (!payload) {
        return null;
    }

    return payload;
}

export function parseIntWhenThruthyOrNullWhenFalsy(payload) {
    if (!payload) {
        return null;
    }

    return parseInt(payload);
}

export function parseFloatWhenThruthyOrNullWhenFalsy(payload) {
    if (!payload) {
        return null;
    }

    if (payloadEndsWithDecimalCommaAndHasOnlyOneDecimalComma(payload)) {
        return payload;
    }

    return parseFloat(payload);
}

function payloadEndsWithDecimalCommaAndHasOnlyOneDecimalComma(payload) {
    return payload.endsWith('.') && (payload.split('.').length - 1) === 1;
}
