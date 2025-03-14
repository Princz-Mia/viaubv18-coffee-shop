import React, { useEffect, useState } from 'react';
import classes from './search.module.css';
import { useNavigate, useParams } from 'react-router-dom';
import { toast } from 'react-toastify';

export default function Search( {searchRoute, defaultRoute, placeholder}) {
  const [term, setTerm] = useState('');
  const navigate = useNavigate();
  const { searchTerm } = useParams();

  useEffect(() => {
    setTerm(searchTerm ?? '');
  }, [searchTerm]);

  const search = async () => {
    if (isEmptyOrSpaces(term)) {
      toast.error("Please enter a valid search term");
    } else {
      if (term) {
        const trimmedTerm = term.trim();
        trimmedTerm ? navigate(`${searchRoute}/${trimmedTerm}`) : navigate(`${defaultRoute}`);
      }
    }
  };

  function isEmptyOrSpaces(str){
    return str === null || str.match(/^ *$/) !== null;
}

  return (
    <div className={classes.container}>
        <input type='text'
        placeholder={placeholder}
        onChange={e => setTerm(e.target.value)}
        onKeyUp={e => e.key === 'Enter' && search()}
        value={term}
        />
        <button onClick={search}>Search</button>
    </div>
  )
}
