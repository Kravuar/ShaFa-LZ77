import makeAnimated from "react-select/animated";
import React from "react";
import Select from "react-select";
import chroma from "chroma-js";

const c = "#1c3131";
const cda = chroma(c).darken().alpha(0.3).hex()
const cb = chroma(c).brighten(2).hex()
const cds = chroma(c).darken().saturate().hex()
const ca = chroma(c).alpha(0.3).hex()
const customStylesSingle = {
    menu: (styles, {}) => ({
        ...styles,
        backgroundColor: cda,
        padding: 20,
    }),
    control: (styles) => ({
        ...styles,
        backgroundColor: cda
    }),
    option: (styles, {isFocused, isSelected}) => ({
        ...styles,
        backgroundColor: isSelected
            ? c
            : isFocused
                ? cds
                : undefined,
        color: isSelected || isFocused
            ? cb
            : undefined,
    })
}
const customStylesMulti = {
    ...customStylesSingle,
    multiValue: (styles) => ({
        ...styles,
        backgroundColor: ca,
    }),
    multiValueLabel: (styles) => ({
        ...styles,
        color: cds,
    }),
    multiValueRemove: (styles) => ({
        ...styles,
        color: c,
        ':hover': {
            backgroundColor: cda,
            color: 'white',
        },
    }),
};

export default function MySelect({options, isMulti, placeholder, onChange, className}) {
    return (
        <Select className={className}
                closeMenuOnSelect={!isMulti}
                styles={isMulti ? customStylesMulti : customStylesSingle}
                isMulti={isMulti}
                components={makeAnimated()}
                placeholder={placeholder}
                isClearable
                options={options}
                onChange={onChange}
        />
    );
}