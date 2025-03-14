import React, { useRef, forwardRef } from 'react';
import classes from './contact.module.css';
import emailjs from '@emailjs/browser';
import { toast } from 'react-toastify';

const Contact = forwardRef((props, ref) => {
  const form = useRef();

  const sendEmail = (e) => {
    e.preventDefault();

    emailjs
      .sendForm('service_dkvt37k', 'template_go6a1q7', form.current, {
        publicKey: 'YtBVwY1nDl4pFlMCi',
      })
      .then(
        () => {
          toast.success("Email sent successfully")
        },
        (error) => {
          console.log(error.text)
          toast.error("Email failed to sent")
        },
      );
  };

  return (
    <>
    <h1 className={classes.heading} ref={ref}> <span>contact</span> us </h1>
    <section className={classes.contact} id="contact">
      <div className={classes.row}>
          <form ref={form} onSubmit={sendEmail}>
              <div className={classes.inputBox}>
                  <input type="text" name='user_name' placeholder="Your Name" />
              </div>
              <div className={classes.inputBox}>
                  <input type="email" name='user_email' placeholder="Your Email" />
              </div>
              <div className={classes.inputBox}>
                  <textarea type="text" name='message' placeholder="Share your thoughts with us..." className={classes.email_content}/>
              </div>
              <input type="submit" value="Contact Now" className={classes.btn} />
          </form>
      </div>
    </section>
    </>
  )
});

export default Contact;