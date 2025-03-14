import React, { useState, useEffect } from 'react'
import { Grid, } from '@material-ui/core';
import Controls from "../../components/Controls/Controls";
import { useForm, Form } from '../../components/Controls/useForm';

import * as productService from '../../api/productService'
import { toast } from 'react-toastify';

const initialFValues = {
    id: null,
    name: '',
    description: '',
    qtyInStock: '',
    price: '',
    category: '',
    productImage: '',
}

export default function ProductForm(props) {
    const { addOrEdit, recordForEdit } = props

    const [categories, setCategories] = useState([]);
    const [uploadedImage, setUploadedImage] = useState(null);

    useEffect(() => {
        const fetchProductCategories = async () => {
            try {
                const categoryResponse = await productService.getAllPorductCategory();
                setCategories(categoryResponse.data);
            } catch (error) {
                toast.error(error.message);
            }
        };
        fetchProductCategories();
    }, []);

    const validate = (fieldValues) => {
        let temp = { ...errors };
        if (!fieldValues) fieldValues = values;
    
        temp.name = fieldValues.name ? "" : "This field is required.";
        temp.qtyInStock = fieldValues.qtyInStock ? "" : "This field is required.";
        temp.price = fieldValues.price ? "" : "This field is required.";
        temp.category = fieldValues.category.length !== 0 ? "" : "This field is required.";
        temp.description = fieldValues.description ? "" : "This field is required.";
    
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
    
    const handleBlur = (e, formatFunction) => {
        const { name } = e.target;
        let fieldValue = values[name];
    
        if (name === "qtyInStock" && !Number.isNaN(Number(fieldValue))) {
            fieldValue = Math.floor(Number(fieldValue)).toString();
        }
    
        if (name === "price" && !Number.isNaN(Number(fieldValue))) {
            fieldValue = Number(fieldValue).toFixed(2);
        }
    
        setValues({
            ...values,
            [name]: fieldValue
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
                const productData = {
                    ...values,
                    productImage: uploadedImage // Hozzáadva a feltöltött kép adatát a termék adatokhoz
                };
                await addOrEdit(productData, resetForm); // Hívjuk meg az addOrEdit függvényt a termék és a kép adataival
            } catch (error) {
                toast.error("Failed to save product and image."); // Hibakezelés hozzáadása
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
            <Grid container>
                <Grid item xs={6}>
                    <Controls.Input
                        label="Porduct Name"
                        name="name"
                        value={values.name}
                        onChange={handleInputChange}
                        error={errors.name}
                    />
                    <Controls.Input
                        label="Quantity In Stock"
                        name="qtyInStock"
                        value={values.qtyInStock}
                        onChange={handleInputChange}
                        onBlur={e => handleBlur(e, Math.floor)}
                        error={errors.qtyInStock}
                    />

                    <Controls.Input
                        label="Price ($)"
                        name="price"
                        value={values.price}
                        onChange={handleInputChange}
                        onBlur={e => handleBlur(e, parseFloat)}
                        error={errors.price}
                    />


                </Grid>
                <Grid item xs={6}>
                    <Controls.Select
                        label="Category"
                        name="category"
                        value={values.category}
                        options={categories}
                        onChange={handleInputChange}
                        error={errors.category}
                    />
                    <Controls.ImageUploader onImageUpload={(image) => setUploadedImage(image)} />
                </Grid>
                <Controls.Input
                    label="Description"
                    name="description"
                    value={values.description}
                    onChange={handleInputChange}
                    error={errors.description}
                />

                <div>
                    <Controls.Button
                        type="submit"
                        text="Submit" />
                    <Controls.Button
                        text="Reset"
                        color="default"
                        onClick={resetForm} />
                </div>
            </Grid>
        </Form>
    )
}