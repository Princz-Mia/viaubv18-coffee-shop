import React from 'react';

export default function DateTime({ date }) {
  if (!date || date.length !== 7) {
    return null; 
  }

  const dateTime = new Date(date[0], date[1] - 1, date[2], date[3], date[4], date[5]);
  const formattedDateTime = `${dateTime.toLocaleDateString()} ${dateTime.toLocaleTimeString()}`;

  return <>{formattedDateTime}</>;
};
