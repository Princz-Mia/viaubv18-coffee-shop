
import React, { useEffect, useState } from 'react';
import { Controller, useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import "react-phone-input-2/lib/bootstrap.css";

import { useAuth } from '../../hooks/useAuth';

import classes from './profilePage.module.css'
import Title from '../../components/Title/Title';
import Input from '../../components/Input/Input';
import Button from '../../components/Button/Button';
import CountrySelector from '../../components/CountrySelector/CountrySelector';
import InputContainer from '../../components/InputContainer/InputContainer';
import PhoneInput from "react-phone-input-2";

export function ProfilePage() {
    const { user, changeNames, changeEmail, changePhoneNumber, changePassword, setAddressToUser } = useAuth();

    const [address, setAddress] = useState(user.address);
    const [country, setCountry] = useState();
    const [phone, setPhone] = useState(user.phoneNumber);

    const {
        control,
        register,
        getValues,
        setValue,
        formState: { errors },
    } = useForm();

    useEffect(() => {
        if (user.address && user.address.country) {
            setValue('region',          user.address.region         || "");
            setValue('postalCode',      user.address.postalCode     || "");
            setValue('city',            user.address.city           || "");
            setValue('addressLine1',    user.address.addressLine1   || "");
            setValue('addressLine2',    user.address.addressLine2   || "");
            setValue('streetNumber',    user.address.streetNumber   || "");
            setValue('unitNumber',      user.address.unitNumber     || "");
            setValue('phoneNumber',     user.phoneNumber            || "");
            setValue('countryName',     user.address.country ? { value: address.country.iso, label: address.country.name } : null);
        } 
    }, [user, address, country, phone]);

    const submitNameChange = async (event) => {
        event.preventDefault();
        const firstNameInput = getValues('firstName').trim();
        const lastNameInput = getValues('lastName').trim();
        if (firstNameInput !== "" && lastNameInput !== "") {
            changeNames(firstNameInput, lastNameInput);
        } else {
            toast.warning("Invalid value in First or/and Last name field")
        }
    };

    const submitEmailChange = async (event) => {
        event.preventDefault();
        const emailInput = getValues('email').trim();
        if (emailInput !== "") {
            changeEmail(emailInput);
        } else {
            toast.warning("Invalid value in Email field.")
        }
    };

    const submitPhoneNumberChange = async (event) => {
        event.preventDefault();
        const phoneInput = getValues('phoneNumber').trim();
        if (phoneInput !== "") {
            changePhoneNumber(phoneInput);
        } else {
            toast.error("Invalid value in Phone number field")
        }
    };

    const submitAddressChange = async (event) => {
        event.preventDefault();
        const countryNameInput = getValues("countryName").label.trim();
        const regionInput = getValues("region").trim();
        const postalCodeInput = getValues("postalCode").trim();
        const cityInput = getValues("city").trim();
        const addressLine1Input = getValues("addressLine1").trim();
        const addressLine2Input = getValues("addressLine2").trim();
        const streetNumberInput = getValues("streetNumber").trim();
        const unitNumberInput = getValues("unitNumber").trim();

        const IsUserInputValid = (
            countryNameInput !== "" &&
            regionInput !== "" &&
            postalCodeInput !== "" &&
            cityInput !== "" &&
            (addressLine1Input !== "" || addressLine2Input !== "") &&
            streetNumberInput !== ""
        );
    
        if (!IsUserInputValid) {
            toast.warning("Invalid value in Address field");
            return;
        }

        const addressRequest = {
            countryName: countryNameInput,
            region: regionInput,
            postalCode: postalCodeInput,
            city: cityInput,
            addressLine1: addressLine1Input,
            addressLine2: addressLine2Input,
            streetNumber: streetNumberInput,
            unitNumber: unitNumberInput
        };

        try {
            await setAddressToUser(user.id, addressRequest);
            setAddress(user.address);
        } catch (error) {
            toast.error(error.message);
        }
    };

    const submitPasswordChange = async () => {
        const currentPasswordInput = getValues('currentPassword');
        const newPasswordInput = getValues('newPassword');
        const confirmNewPasswordInput = getValues('confirmNewPassword');
        if (currentPasswordInput !== "" && newPasswordInput !== "" && confirmNewPasswordInput !== "") {
            changePassword(
                {
                    currentPassword: currentPasswordInput,
                    newPassword: newPasswordInput,
                    confirmNewPassword: confirmNewPasswordInput,
                }
            );
        } else {
            toast.warning("Invalid value in Password fields")
        }
    };

    return (
        <div className={classes.container}>
            <div className={classes.details}>
                <Title title="Update Profile" />
                <form onSubmit={submitNameChange}>
                    <div className={classes.inputs}>
                        <Input
                            defaultValue={user.firstName}
                            label="First Name"
                            {...register('firstName')}
                            error={errors.firstName}
                        />

                        <Input
                            defaultValue={user.lastName}
                            label="Last Name"
                            {...register('lastName')}
                            error={errors.lastName}
                        />
                    </div>
                    <Button type="submit" text="Update" backgroundColor="wheat" />
                </form>

                <form className={classes.profileForm} onSubmit={submitEmailChange}>
                    <div className={classes.profileInput}>
                        <Input
                            type="email"
                            label="Email Address"
                            defaultValue={user.email}
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
                    <Button type="submit" text="Update" backgroundColor="wheat" />
                </form>

                <form onSubmit={submitPhoneNumberChange}>
                    <div className={classes.inputs}>
                        <InputContainer label="Phone Number">
                            <Controller
                                control={control}
                                name='phoneNumber'
                                rules={{ required: true }}
                                defaultValue={user.phoneNumber}
                                render={({ field: { ref, ...field } }) => (
                                <PhoneInput
                                    inputStyle={{height: '25%', width: '27rem'}}
                                    containerStyle={{height: '80%', width: '23.5rem'}}
                                    country={"us"}
                                    enableSearch={true}
                                    value={phone}
                                    onChange={(phone) => setPhone(phone)}
                                    {...field}
                                    inputExtraProps={
                                        {
                                            ref,
                                            required: true,
                                        }
                                    }
                                />
                                )}
                            />
                        </InputContainer>
                    </div>
                    <Button type="submit" text="Update" backgroundColor="wheat" />
                </form>
                        
                <Title title="Update Address" />
                <form onSubmit={submitAddressChange}>
                    <div className={classes.inputs}>
                        <InputContainer label="Country">
                            <Controller
                                name="countryName"
                                control={control}
                                render={({ field }) => (
                                <CountrySelector
                                    defaultValue={
                                        country
                                        ? { value: country.iso, label: country.countryName }
                                        : null
                                    }
                                    value={field.value}
                                    onChange={field.onChange} />
                                )}
                            />
                        </InputContainer>
    
    
                        <Input
                            defaultValue={address ? address.region : ""}
                            type="text"
                            label="Region"
                            {...register('region')}
                            error={errors.region}
                        />
    
                        <Input
                            defaultValue={address ? address.postalCode : ""}
                            type="text"
                            label="Postal Code"
                            {...register('postalCode')}
                            error={errors.postalCode}
                        />
    
                        <Input
                            defaultValue={address ? address.city : ""}
                            type="text"
                            label="City"
                            {...register('city')}
                            error={errors.city}
                        />
    
                        <Input
                            defaultValue={address ? address.addressLine1 : ""}
                            type="text"
                            label="Address Line 1"
                            {...register('addressLine1')}
                            error={errors.addressLine1}
                        />
    
                        <Input
                            defaultValue={address ? address.addressLine2 : ""}
                            type="text"
                            label="Address Line 2"
                            {...register('addressLine2')}
                            error={errors.addressLine2}
                        />
    
                        <Input
                            defaultValue={address ? address.streetNumber : ""}
                            type="text"
                            label="Street Number"
                            {...register('streetNumber')}
                            error={errors.streetNumber}
                        />
    
                        <Input
                            defaultValue={address ? address.unitNumber : ""}
                            type="text"
                            label="Unit Number"
                            {...register('unitNumber')}
                            error={errors.unitNumber}
                        />
                    </div>
                    <Button type="submit" text="Update" backgroundColor="wheat" />
                </form>
                <Title title="Change Password" />
                <form onSubmit={submitPasswordChange}>
                    <Input
                    type="password"
                    label="Current Password"
                    {...register('currentPassword', {
                        required: true,
                    })}
                    error={errors.currentPassword}
                    />

                    <Input
                    type="password"
                    label="New Password"
                    {...register('newPassword', {
                        required: true,
                        minLength: 5,
                    })}
                    error={errors.newPassword}
                    />

                    <Input
                    type="password"
                    label="Confirm Password"
                    {...register('confirmNewPassword', {
                        required: true,
                        validate: value =>
                        value != getValues('newPassword')
                            ? 'Passwords Do No Match'
                            : true,
                    })}
                    error={errors.confirmNewPassword}
                    />

                    <Button type="submit" text="Change" />
                </form>
            </div>
        </div>
      );
    }