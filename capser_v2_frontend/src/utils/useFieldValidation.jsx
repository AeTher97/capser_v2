import React, {useEffect} from "react";

const useFieldValidation = (initialValue, validate, onChange = (event) => {
}, dataProcessor) => {
    const [value, setValue] = React.useState(initialValue);
    const [error, setError] = React.useState(null);

    useEffect(() => {
        if (value !== initialValue) {
            const validationError = validate(value);
            setError(validationError);
        }
    }, [value, validate, initialValue]);

    const handleChange = (event) => {
        let value = event.target.value;
        if (dataProcessor) {
            value = dataProcessor(event.target.value)
        }
        setValue(value);
        onChange(value, error === null)

    };

    const handleBlur = () => {
        const validationError = validate(value);
        setError(validationError);
    };

    const runValidation = () => {
        const validationError = validate(value);
        setError(validationError);
        return validationError;
    };

    const resetToInitialValue = () => {
        setValue(initialValue);
    };

    const clearValue = () => {
        setValue('');
        setError(null);
    };

    return {
        value,
        handleChange,
        error,
        handleBlur,
        resetToInitialValue,
        clearValue,
        validate: runValidation,
        setValue,
        validated: true
    };
};

export default useFieldValidation;
