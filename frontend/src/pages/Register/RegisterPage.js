import React, {  useState, useEffect } from 'react';
import classes from './registerPage.module.css';
import { Controller, useForm } from 'react-hook-form';
import { Link, useSearchParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import Input from '../../components/Input/Input';
import Title from '../../components/Title/Title';
import Button from '../../components/Button/Button';
import "react-phone-input-2/lib/bootstrap.css";

export function RegisterPage() {

  const {
    handleSubmit,
    control,
    register,
    getValues,
    formState: { errors },
  } = useForm();

  const auth = useAuth();
  const { user } = auth;
  const navigate = useNavigate();
  const [params] = useSearchParams();
  const returnUrl = params.get('returnUrl');

  useEffect(() => {
    if (!user) {
      return;
    } else {
      returnUrl ? navigate(returnUrl) : navigate('/');
    }
  }, [user]);

  const submit = async registerRequest => {
    await auth.register(registerRequest);
  };

  return (
    <div className={classes.container}>
      <div className={classes.details}>
        <form onSubmit={handleSubmit(submit)} noValidate>
          <div className={classes.row}>
            <div className={classes.column}>
            <Title title="Sign up" />
              <div className={classes.column_element}>
              <Input
                type="text"
                label="First Name"
                {...register('firstName', {
                  required: true,
                  minLength: 1,
                })}
                error={errors.firstName}
              />
              </div>

              <div className={classes.column_element}>
              <Input
                              type="text"
                              label="Last Name"
                              {...register('lastName', {
                                required: true,
                                minLength: 1,
                              })}
                              error={errors.lastName}
                            />
              </div>

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

              <div className={classes.column_element}>
              <Input
                              type="password"
                              label="Confirm Password"
                              {...register('confirmPassword', {
                                required: true,
                                validate: value =>
                                  value !== getValues('password')
                                    ? 'Passwords Do No Match'
                                    : true,
                              })}
                              error={errors.confirmPassword}
                            />
              </div>
            </div>
          </div>

          <Button type="submit" text="Sign up" />

          <div className={classes.login}>
            Already have an account? &nbsp;
            <Link to={`/login${returnUrl ? '?returnUrl=' + returnUrl : ''}`}>
              Log in
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