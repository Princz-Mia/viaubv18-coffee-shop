import React, { useEffect } from 'react';
import { useCart } from '../../hooks/useCart';
import { useAuth } from '../../hooks/useAuth';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { Controller, useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import Select from 'react-select'
import PhoneInput from "react-phone-input-2";
import "react-phone-input-2/lib/bootstrap.css";

import classes from './checkoutPage.module.css';
import Title from '../../components/Title/Title';
import Input from '../../components/Input/Input';
import Button from '../../components/Button/Button';
import OrderItemsList from '../../components/OrderItemsList/OrderItemsList';
import CountrySelector from '../../components/CountrySelector/CountrySelector';
import InputContainer from '../../components/InputContainer/InputContainer';

import * as orderService from '../../api/orderService';
import * as addressService from '../../api/addressService';

export function CheckoutPage() {
    const { cart, clearCart } = useCart();
    const { user, fetchPendingOrdersOfUser } = useAuth();

    const navigate = useNavigate();

    const [order, setOrder] = useState({ ...cart });
    const [shopOrder, setShopOrder] = useState(null);

    const [address, setAddress] = useState(null);
    const [country, setCountry] = useState(null);
    const [phone, setPhone] = useState(user.phoneNumber);

    const [shippingMethods, setShippingMethods] = useState([]);
    const [selectedShippingMethod, setSelectedShippingMethod] = useState(null);

    const {
        control,
        register,
        getValues,
        setValue,
        formState: { errors },
    } = useForm();

    useEffect(() => {
        const hasPendingOrder = fetchPendingOrdersOfUser();
        if (hasPendingOrder.data === true) {
            toast.warning("You have already an Order that is in Pending state");
            navigate('/track/orders');
        }
    }, [user]);

    useEffect(() => {
        setOrder({ ...cart });
    }, [cart]);

    useEffect(() => {
        if (user.address && user.address.country) {
            setValue('region',          user.address.region         || "");
            setValue('postalCode',      user.address.postalCode     || "");
            setValue('city',            user.address.city           || "");
            setValue('addressLine1',    user.address.addressLine1   || "");
            setValue('addressLine2',    user.address.addressLine2   || "");
            setValue('streetNumber',    user.address.streetNumber   || "");
            setValue('unitNumber',      user.address.unitNumber     || "");
            setValue('phoneNumber',     user.phone                  || "");
            setValue('countryName',     user.address.country ? { value: user.address.country.iso, label: user.address.country.countryName } : null);
        }

        const fetchShippingMethods = async () => {
            try {
                const responseShippingMethods = await orderService.getShippingMethods();
                setShippingMethods(responseShippingMethods.data);
            } catch(error) {
                toast.error(error.response.data.message)
            }
        };
        fetchShippingMethods();
        
    }, []);

    const shippingMethodHandler = (selectedOption) => {
        setSelectedShippingMethod(selectedOption);
        if (selectedOption) {
            const selectedMethod = shippingMethods.find(method => method.id === selectedOption.value.id);
            if (selectedMethod) {
                const newShippingPrice = selectedMethod.price;
                let totalPriceWithoutShipping = order.totalPrice - (order.shippingMethod ? order.shippingMethod.price : 0);
                const totalPrice = totalPriceWithoutShipping + newShippingPrice;
                setOrder(prevOrder => ({ ...prevOrder, totalPrice, shippingMethod: selectedMethod }));
            }
        }
    };
    
    const submitHandler = async (event) => {
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
            getValues("phoneNumber").trim() !== "" &&
            countryNameInput !== "" &&
            regionInput !== "" &&
            postalCodeInput !== "" &&
            cityInput !== "" &&
            (addressLine1Input !== "" || addressLine2Input !== "") &&
            streetNumberInput !== "" &&
            getValues("firstName").trim() !== "" &&
            getValues("lastName").trim() !== ""
        );
    
        if (!IsUserInputValid) {
            toast.warning('Please fill the Shipping Address details');
            return;
        }
    
        if (!selectedShippingMethod) {
            toast.warning('Please select a Shipping Method');
            return;
        }
    
        try {
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
    
            const responseAddress = await addressService.getOrCreateAddress(addressRequest);
            const address = responseAddress.data;
    
            const shopOrderRequest = {
                userId: user.id,
                addressId: address.id,
                shippingMethodId: selectedShippingMethod.value.id,
                orderDate: new Date().toISOString(),
                orderTotal: order.totalPrice.toFixed(2),
                firstName: getValues("firstName").trim(),
                lastName: getValues("lastName").trim(),
                phoneNumber: getValues("phoneNumber").trim()
            };
    
            const responseShopOrder = await orderService.createShopOrder(shopOrderRequest);
            const shopOrder = responseShopOrder.data;
    
            if (shopOrder) {
                for (const item of order.products) {
                    const shopOrderItemRequest = {
                        productId: item.product.id,
                        shopOrderId: shopOrder.id,
                        price: item.qty * item.product.price,
                        qty: item.qty
                    };
                    await orderService.createOrderItem(shopOrderItemRequest);
                }
    
                navigate('/payment');
                clearCart();
            } else {
                toast.error("We were unable to process your order. Please try again later.");
            }
        } catch (error) {
            toast.error(error.response.data.message);
        }
    };
    

  return (
    <div className={classes.page}>
        <Title title="Order Form" margin="1.5rem 0 0 2.5rem" />
        <form onSubmit={submitHandler} className={classes.container}>
            <div className={classes.content}>
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

                    <InputContainer label="Phone Number">
                        <Controller
                            control={control}
                            name='phoneNumber'
                            rules={{ required: true }}
                            defaultValue={phone}
                            render={({ field: { ref, ...field } }) => (
                            <PhoneInput
                                inputStyle={{height: '25%', width: '27rem'}}
                                containerStyle={{height: '80%', width: '23.5rem'}}
                                country={"us"}
                                enableSearch={true}
                                value={phone}
                                onChange={(phone) => setPhone(phone)}
                                {...field}
                                inputExtraProps={{
                                ref,
                                required: true,
                                }}
                            />
                            )}
                        />
                    </InputContainer>
                    
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

                <div>
                    <div className={classes.order}>
                        <OrderItemsList  order={order} totalPrice={order.totalPrice} shipping={selectedShippingMethod} />
                    </div>

                    <div className={classes.shipping_method_container}>
                        <label className={classes.shipping_method_label}>Shipping Method</label>
                        <div className={classes.shipping_method_content}>                            
                            <p className={classes.shipping_method_desc}>{selectedShippingMethod ? `${selectedShippingMethod.value.description}` : "Please select a shipping method for delivering your items."}</p>
                            <Select
                                className={classes.shipping_method_selector}
                                options={shippingMethods.map(method => ({ value: method, label: `${method.name} - $` + `${(Math.round(method.price * 100) / 100).toFixed(2)}` }))}
                                onChange={shippingMethodHandler}
                                value={selectedShippingMethod}
                            />
                        </div>
                    </div>
                </div>
            </div>
            <div className={classes.buttons_container}>
                <Button
                    type="submit"
                    text="Go to Payment"
                    width="25%"
                    height="3rem"
                />
            </div>
        </form>
    </div>
  )
}
