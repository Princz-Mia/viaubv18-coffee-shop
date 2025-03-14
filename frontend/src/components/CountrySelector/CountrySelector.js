import React, { useState, useEffect, useMemo } from 'react';
import Select from 'react-select';
import countryList from 'react-select-country-list';
import classes from './countrySelector.module.css';

export default function CountrySelector({ onChange, value, defaultValue }) {
  const [selectedValue, setSelectedValue] = useState(value || defaultValue);
  const options = countryList().getData();

  useEffect(() => {
    setSelectedValue(value || defaultValue);
  }, [value, defaultValue]);

  const changeHandler = selectedOption => {
    setSelectedValue(selectedOption);
    if (onChange) {
      onChange(selectedOption);
    }
  };

  return (
    <div style={{ width: '27rem'  }}>
      <Select
        options={options}
        value={selectedValue}
        onChange={changeHandler}
      />
    </div>
  );
}