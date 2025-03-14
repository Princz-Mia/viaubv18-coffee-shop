import React, { useEffect, useState } from 'react'
import classes from './verificationPage.module.css';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import Title from '../../components/Title/Title';
import { toast } from 'react-toastify';

export function VerificationPage() {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const key = queryParams.get('key');

  const navigate = useNavigate();
  const { user, verify } = useAuth();
  const [verification, setVerification] = useState(null);

  useEffect(() => {
    if (user) {
      navigate('/');
      toast.error("You are already verified and logged in.");
    } else {
      callVerification(key);
    }
  }, [user]);

  const callVerification = async (key) => {
    const response = await verify(key);
    setVerification(response);
  };

  useEffect(() => {
    if (verification) {
      toast.success("Your account has been enabled. You can log in now.");
    }
  }, [verification]);

  return (
    <div className={classes.container}>
      <div className={classes.details}>
        <Title title="Verification" />
        <div>Your verification token: <br /> {JSON.stringify(key)}</div>
      </div>
    </div>
  );
}
