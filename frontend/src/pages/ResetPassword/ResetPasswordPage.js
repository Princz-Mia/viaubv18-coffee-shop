import React, { useEffect } from 'react'
import classes from './resetPasswordPage.module.css';
import { Link, useNavigate, useSearchParams, useLocation } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAuth } from '../../hooks/useAuth';
import Input from '../../components/Input/Input';
import Title from '../../components/Title/Title';
import Button from '../../components/Button/Button';

import * as userService from '../../api/userService';
import { toast } from 'react-toastify';

export function ResetPasswordPage() {
    const {
        handleSubmit,
        control,
        register,
        getValues,
        formState: { errors },
      } = useForm();
    
      const { user } = useAuth();
      const navigate = useNavigate();
      const [params] = useSearchParams();
      const returnUrl = params.get('returnUrl');

      const location = useLocation();
      const queryParams = new URLSearchParams(location.search);
      const key = queryParams.get('key');
    
    
      useEffect(() => {
        if (!user) {
          return;
        } else {
          returnUrl ? navigate(returnUrl) : navigate('/');
        }
      }, [user]);
    
      const submit = async (passwordResetRequest) => {
        try {
          const response = await userService.resetPassword(passwordResetRequest, key);
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
        <Title title="New Password" />
        <form onSubmit={handleSubmit(submit)} noValidate>
        <div className={classes.column_element}>
              <Input
                              type="password"
                              label="Password"
                              {...register('newPassword', {
                                required: true,
                                minLength: 8,
                              })}
                              error={errors.newPassword}
                            />
              </div>

              <div className={classes.column_element}>
              <Input
                              type="password"
                              label="Confirm Password"
                              {...register('confirmNewPassword', {
                                required: true,
                                validate: value =>
                                  value !== getValues('confirmNewPassword')
                                    ? 'Passwords Do No Match'
                                    : true,
                              })}
                              error={errors.confirmNewPassword}
                            />
              </div>
          <Button width={"18rem"} type="submit" text="Change Password" />

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