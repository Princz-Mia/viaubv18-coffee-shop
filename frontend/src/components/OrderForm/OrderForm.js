import React, { useState, useEffect } from 'react'
import { Grid, } from '@material-ui/core';
import Controls from "../../components/Controls/Controls";
import { useForm, Form } from '../../components/Controls/useForm';

import * as orderService from '../../api/orderService'
import { toast } from 'react-toastify';

const initialFValues = {
    id: null,
    shopOrderStatus: '',
}

export default function OrderForm(props) {
    const { edit, recordForEdit } = props

    const [shopOrderStatuses, setShopOrderStatuses] = useState([]);

    useEffect(() => {
        const fetchProductCategories = async () => {
            try {
                const shopOrderStatusesResponse = await orderService.getShopOrderStatuses();
                setShopOrderStatuses(shopOrderStatusesResponse.data);
            } catch (error) {
                toast.error(error.message);
            }
        };
        fetchProductCategories();
    }, []);

    const validate = (fieldValues) => {
        let temp = { ...errors };
        if (!fieldValues) fieldValues = values;
    
        temp.shopOrderStatus = fieldValues.shopOrderStatus.length !== 0 ? "" : "This field is required.";
    
        setErrors({ ...temp });
        return Object.values(temp).every(x => x === "");
    }
    
    const handleInputChange = e => {
        const { name, value } = e.target;
        setValues({
            ...values,
            [name]: value
        });
    }
    
    const {
        values,
        setValues,
        errors,
        setErrors,
        resetForm
    } = useForm(initialFValues, false);
    
    const handleSubmit = async e => {
        e.preventDefault();
        if (validate()) {
            try {
                const orderData = {
                    ...values,
                };
                await edit(orderData, resetForm);
            } catch (error) {
                toast.error("Failed to update order.");
            }
        }
    }
    
    useEffect(() => {
        if (recordForEdit != null)
            setValues({
                ...recordForEdit
            })
    }, [recordForEdit])

    return (
        <Form onSubmit={handleSubmit}>
            <Grid container spacing={2}>
                <Grid item xs={12}>
                    <Controls.Select
                        label="Shop Order Status"
                        name="shopOrderStatus"
                        value={values.shopOrderStatus}
                        options={shopOrderStatuses}
                        onChange={handleInputChange}
                        error={errors.shopOrderStatus}
                        fullWidth
                        variant="outlined"
                    />
                </Grid>
                <Grid item xs={12} style={{ marginTop: '1rem' }}>
                    <div>
                        <Controls.Button
                            type="submit"
                            text="Update"
                            fullWidth
                        />
                        <Controls.Button
                            text="Reset"
                            color="default"
                            onClick={resetForm}
                            fullWidth
                        />
                    </div>
                </Grid>
            </Grid>
        </Form>
    );
};