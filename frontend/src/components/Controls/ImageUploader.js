import React from 'react';
import ImageUploading from 'react-images-uploading';
import Button from './Button';
import ActionButton from './ActionButton';
import EditOutlined from '@material-ui/icons/EditOutlined';
import CloseIcon from '@material-ui/icons/Close';

export function ImageUploader() {
  const [images, setImages] = React.useState([]);
  const maxNumber = 1;

  const onChange = (imageList, addUpdateIndex) => {
    setImages(imageList);
  };

  return (
    <div className="App">
      <ImageUploading
        multiple
        value={images}
        onChange={onChange}
        maxNumber={maxNumber}
        dataURLKey="data_url"
      >
        {({
          imageList,
          onImageUpload,
          onImageRemoveAll,
          onImageUpdate,
          onImageRemove,
          isDragging,
          dragProps,
        }) => (
          <div className="upload__image-wrapper">
            <ActionButton
              onClick={onImageUpload}
              {...dragProps}
            >
              Upload Product Picture
            </ActionButton>
            &nbsp;
            {imageList.map((image, index) => (
              <div key={index} className="image-item">
                <img src={image['data_url']} alt="" width="100" />
                <div className="image-item__btn-wrapper">
                  <ActionButton color="primary" onClick={() => onImageUpdate(index)}><EditOutlined fontSize="small" /></ActionButton>
                  <ActionButton color="secondary" onClick={() => onImageRemove(index)}><CloseIcon fontSize="small" /></ActionButton>
                </div>
              </div>
            ))}
          </div>
        )}
      </ImageUploading>
    </div>
  );
}