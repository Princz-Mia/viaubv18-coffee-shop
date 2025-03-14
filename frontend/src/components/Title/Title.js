import React from 'react'

export default function Title({title, fontSize, margin, padding}) {
  return (
    <h1 style={{ fontSize, margin, padding, color: 'wheat' }}>{title}</h1>
  );
}
