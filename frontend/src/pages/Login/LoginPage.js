import React, { useEffect } from 'react'
import classes from './loginPage.module.css';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAuth } from '../../hooks/useAuth';
import Input from '../../components/Input/Input';
import Title from '../../components/Title/Title';
import Button from '../../components/Button/Button';

export function LoginPage() {
  const {
    handleSubmit,
    register,
    formState: { errors },
  } = useForm();

  const navigate = useNavigate();
  const { user, login } = useAuth();
  const [params] = useSearchParams();
  const returnUrl = params.get('returnUrl');

  useEffect(() => {
    if (!user) return;
    returnUrl ? navigate(returnUrl) : navigate('/');
  }, [user]);

  const submit = (loginRequest) => {
    login(loginRequest);
  };

  return (
    <div className={classes.container}>
      <div className={classes.details}>
        <Title title="Login" />
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

            <div className={classes.column_element}>
              <Input
                              type="password"
                              label="Password"
                              {...register('password', {
                                required: true,
                                minLength: 8,
                              })}
                              error={errors.password}
                            />
            </div>

          <Button type="submit" text="Login" />

          <div className={classes.register}>
            New user? &nbsp;
            <Link to={`/registration${returnUrl ? '?returnUrl=' + returnUrl : ''}`}>
              Sign up here
            </Link>
          </div>
          <div className={classes.forgot_password}>
            <Link to={`/forgot-password${returnUrl ? '?returnUrl=' + returnUrl : ''}`}>
              Can't log in? &nbsp;
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
}