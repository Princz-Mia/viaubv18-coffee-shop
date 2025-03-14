import React, { useState, useEffect } from 'react'
import { Grid, } from '@material-ui/core';
import Controls from "../../components/Controls/Controls";
import { useForm, Form } from '../../components/Controls/useForm';

import { toast } from 'react-toastify';

const initialFValues = {
    id: null,
    title: '',
    content: '',
    newsImage: '',
}

export default function NewsForm(props) {
    const { addOrEdit, recordForEdit } = props

    const [uploadedImage, setUploadedImage] = useState(null);

    const validate = (fieldValues) => {
        let temp = { ...errors };
        if (!fieldValues) fieldValues = values;
    
        temp.title = fieldValues.title ? "" : "This field is required.";
        temp.content = fieldValues.content ? "" : "This field is required.";
    
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
                const newsData = {
                    ...values,
                    newsImage: uploadedImage
                };
                await addOrEdit(newsData, resetForm);
            } catch (error) {
                toast.error("Failed to save news and image.");
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
                        label="Title"
                        name="title"
                        value={values.title}
                        onChange={handleInputChange}
                        error={errors.title}
                    />
                    <Controls.Input
                        label="Content"
                        name="content"
                        value={values.content}
                        onChange={handleInputChange}
                        error={errors.content}
                    />
                    <div>
                        <Controls.Button
                            type="submit"
                            text="Publish" />
                        <Controls.Button
                            text="Reset"
                            color="default"
                            onClick={resetForm} />
                    </div>
                </Grid>
            </Grid>
        </Form>
    )
}