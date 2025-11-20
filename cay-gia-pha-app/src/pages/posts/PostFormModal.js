import React, { useState, useRef, useEffect } from 'react';
import './PostFormModal.css';
import { v4 as uuidv4 } from 'uuid';

const PostFormModal = ({ isOpen, onClose, onSubmit, postToEdit }) => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [imageFile, setImageFile] = useState(null);
  const [uploading, setUploading] = useState(false);

  const imgParentRef = useRef(null);

  // --- Khi mở modal với postToEdit thì gán dữ liệu ---
  useEffect(() => {
    if (postToEdit) {
      setTitle(postToEdit.title || '');
      setContent(postToEdit.content || '');
      setImageFile(null); // reset ảnh nếu muốn
    } else {
      setTitle('');
      setContent('');
      setImageFile(null);
    }
  }, [postToEdit]);

  if (!isOpen) return null;

  const handleImageChange = (e) => setImageFile(e.target.files[0]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!content) {
      alert('Vui lòng nhập nội dung.');
      return;
    }

    setUploading(true);
    let imageUrl = postToEdit?.imageUrl || '';

    if (imageFile) {
      try {
        const reader = new FileReader();
        reader.readAsDataURL(imageFile);
        await new Promise((resolve, reject) => {
          reader.onload = async () => {
            try {
              const base64 = reader.result;
              const formData = new FormData();
              formData.append("file", base64);
              formData.append("upload_preset", "HIHIHAHA");
              formData.append("cloud_name", "dirkik4yj");

              const res = await fetch(
                `https://api.cloudinary.com/v1_1/dirkik4yj/image/upload`,
                { method: "POST", body: formData }
              );
              const data = await res.json();
              imageUrl = data.secure_url;
              resolve();
            } catch (err) { reject(err); }
          };
          reader.onerror = (err) => reject(err);
        });
      } catch (err) {
        console.error("Upload Cloudinary failed:", err);
        setUploading(false);
        return;
      }
    }

    const postData = {
      postIdentifier: postToEdit?.postIdentifier || uuidv4().split('-').pop(),
      imageUrl,
      postDate: new Date().toISOString(),
      content,
      title,
    };

    await onSubmit(postData, !!postToEdit); // true nếu đang sửa
    setUploading(false);
    onClose();
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content" style={{ width: '600px' }}>
        <button className="modal-close" onClick={onClose}>&times;</button>
        <h3>{postToEdit ? 'Chỉnh sửa bài viết' : 'Thêm bài viết mới'}</h3>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Tiêu đề:</label>
            <input
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />
          </div>

          <div className="form-group">
            <label>Nội dung:</label>
            <textarea
              rows="6"
              value={content}
              onChange={(e) => setContent(e.target.value)}
            />
          </div>

          <div className="form-group">
            <label>Chọn ảnh (Tùy chọn):</label>
            <input type="file" accept="image/*" onChange={handleImageChange} />
          </div>

          <div className="form-actions">
            <button type="submit" disabled={uploading}>
              {uploading ? 'Đang tải ảnh...' : postToEdit ? 'Lưu thay đổi' : 'Đăng bài'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default PostFormModal;
