import React, { useEffect } from 'react'
import classes from './forgotPasswordPage.module.css';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAuth } from '../../hooks/useAuth';
import Input from '../../components/Input/Input';
import Title from '../../components/Title/Title';
import Button from '../../components/Button/Button';

import * as userService from '../../api/userService';
import { toast } from 'react-toastify';

export function ForgotPasswordPage() {
  const {
    handleSubmit,
    register,
    formState: { errors },
  } = useForm();

  const navigate = useNavigate();
  const { user } = useAuth();
  const [params] = useSearchParams();
  const returnUrl = params.get('returnUrl');

  useEffect(() => {
    if (!user) return;
    returnUrl ? navigate(returnUrl) : navigate('/');
  }, [user]);

  const submit = async (input) => {
      try {
        const response = await userService.requestPasswordReset(input.email);
        if (response) {
          toast.success(response.data.message);
        }
      } catch (error) {
        toast.error(error.message);
      }
    };

  return (
    <div className={classes.container}>
      <div className={classes.details}>
        <Title title="Forgot your password?" />
        <form onSubmit={handleSubmit(submit)} noValidate>
          <div className={classes.column_element}>
              <Input
                              type="email"
                              label="Email"
                              {...register('email', {
                                required: true,
                                pattern: {
                                  value: /^[\w-.]+@([\w-]+\.)+[\w-]{2,63}$/i,
                                  message: 'Email Is Not Valid',
                                },
                              })}
                              error={errors.email}
                            />
            </div>
          <Button width={"18rem"} type="submit" text="Request Password Reset" />

          <div className={classes.login}>
            Already have an account? &nbsp;
            <Link to={`/login${returnUrl ? '?returnUrl=' + returnUrl : ''}`}>
              Log in
            </Link>
          </div>
          <div className={classes.register}>
            New user? &nbsp;
            <Link to={`/registration${returnUrl ? '?returnUrl=' + returnUrl : ''}`}>
              Sign up here
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
}