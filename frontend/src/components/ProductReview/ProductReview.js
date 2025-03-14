import React, { useState } from "react";
import { Button, Form, FormGroup, Input, Label } from "reactstrap";
import { IoPersonCircleOutline } from "react-icons/io5";
import { toast } from "react-toastify";
import classes from './productReview.module.css';

import * as productReviewService from '../../api/productReviewService';
import { useAuth } from "../../hooks/useAuth";
import { Rating } from "@mui/material";
import DateTime from "../DateTime/DateTime";

export default function ProductReview ({ productId, productReviews, onReviewAdded })  {

  const { user } = useAuth();
  const [review, setReview] = useState({});

  const handleRating = (event) => {
    setReview(prevReview => ({ ...prevReview, ratingValue: event.target.value }));
  };

  const handleReviewComment = ({ target: { value } }) => {
    setReview({ ...review, comment: value });
  };

  const handleSubmit = async () => {
    if (!review.comment || !review.ratingValue || review.ratingValue < 0.5) {
      toast.error("Both review stars and text are required");
      return;
    }

    if (review.comment && review.ratingValue && productId) {
      try {
        await productReviewService.addProductReview(user.id, productId, review);
        if (onReviewAdded) {
          onReviewAdded();
        }
      } catch (error) {
        toast.error(error.message);
      }
    } else {
      toast.error("There is an error occured!");
    }
  };

  return (
    <div className={classes.productReviews}>
        <div className={classes.productProductCreator}>
          <h2>Create review:</h2>
          <Form>
            <FormGroup>
              <Label for="exampleText">Overall rating</Label>
              <div>
                <Rating name="half-rating-read" defaultValue={0} precision={0.5} onChange={handleRating} />
              </div>
            </FormGroup>
            <FormGroup>
              <Label for="reviewComment">Leave your review here</Label>
              <Input
                type="textarea"
                name="reviewComment"
                id="reviewComment"
                onChange={handleReviewComment}
              />
            </FormGroup>
            <Button color="info" onClick={handleSubmit}>
              Submit
            </Button>
          </Form>
        </div>

      {productReviews.length !== 0 ? (
        <div className={classes.reviews}>
          <h2>Customer reviews:</h2>
          {productReviews.map((review) => (
            <div className={classes.review}>
              <div className={classes.customer}>
                <IoPersonCircleOutline className={classes.icon} />
                <span>{review.user.firstName} {review.user.lastName}</span>
              </div>
              <Rating name="half-rating-read" value={review.ratingValue} precision={0.5} readOnly />
              <p>Reviewed at: <DateTime date={review.createdAt} /></p>
              <p>{review.comment}</p>
            </div>
          ))}
        </div>
      ) : (
        <div className={classes.placeholder}>
          <h3>Be the first who review this product!</h3>
        </div>
      )}
    </div>
  );
};