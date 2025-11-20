import React, { useState, useEffect } from 'react';
import useAuth from '../../hooks/useAuth';
import PostFormModal from './PostFormModal';
import './PostListPage.css';
import { createPost, deletePost, getPosts, updatePost } from '../../api/apiConfig';

const PostListPage = () => {
        const [posts, setPosts] = useState([]);
        const [loading, setLoading] = useState(true);
        const [isModalOpen, setIsModalOpen] = useState(false);
        const { user } = useAuth();
        const [postToEdit, setPostToEdit] = useState(null);
        const handleEdit = (post) => {
                window.scrollTo({ top: 0, behavior: 'smooth' });
                setPostToEdit(post);
                setIsModalOpen(true);
        };
        useEffect(() => {
                const fetchPosts = async () => {
                        setLoading(true);
                        try {
                                const data = await getPosts();
                                setPosts(data.result);
                        } catch (err) {
                                console.error("Lỗi khi tải bài viết:", err);
                                alert("Không thể tải danh sách bài viết.");
                        } finally {
                                setLoading(false);
                        }
                };
                fetchPosts();
        }, []);

        const handleDelete = async (postId) => {
                if (window.confirm("Bạn có chắc muốn xóa bài viết này?")) {
                        try {
                                await deletePost(postId);
                                setPosts(posts.filter(p => p.postIdentifier !== postId));
                        } catch (err) {
                                console.error("Lỗi khi xóa bài viết:", err);
                                alert("Lỗi khi xóa bài viết.");
                        }
                }
        };

        const handleCreatePost = async (postData, isEdit = false) => {
                try {
                        const dataToSend = { ...postData, userId: user?.id };
                        let result;
                        if (isEdit) {
                                console.log(dataToSend)
                                result = await updatePost(dataToSend); // API sửa bài viết
                                console.log("res",result.result)
                                setPosts((pre) => pre.map(p => p.postIdentifier === result.result.postIdentifier ? result.result : p));
                        } else {
                                result = await createPost(dataToSend);
                                setPosts((pre) => [result.result, ...pre]);
                        }
                        setIsModalOpen(false);
                        setPostToEdit(null);
                } catch (err) {
                        console.error("Lỗi khi tạo bài viết:", err);
                        alert("Lỗi khi tạo bài viết.");
                }
        };

        // ✅ Hàm mở modal và scroll lên đầu trang
        const handleOpenModal = () => {
                window.scrollTo({ top: 0, behavior: 'smooth' });
                setPostToEdit(false);
                setIsModalOpen(true);
        };

        return (
                <div className="post-list-container">
                        <h2>Bài viết của gia đình</h2>
                        <button className="btn-add-post" onClick={handleOpenModal}>
                                Thêm bài viết
                        </button>

                        <div className="posts">
                                {loading && <p>Đang tải bài viết...</p>}
                                {!loading && posts.length === 0 && <p>Chưa có bài viết nào.</p>}
                                {posts.map(post => (
                                        <article key={post.postIdentifier} className="post-item">
                                                <div className="post-header">
                                                        <h3>{post.title}</h3>

                                                        <div className="post-actions">
                                                                <button className="btn-edit"
                                                                        onClick={() => handleEdit(post)}>
                                                                        ✏️
                                                                </button>
                                                                <button
                                                                        className="btn-delete"
                                                                        onClick={() => handleDelete(post.postIdentifier)}
                                                                >
                                                                        🗑️
                                                                </button>
                                                        </div>
                                                </div>

                                                {post.imageUrl && (
                                                        <img src={post.imageUrl} alt="post" className="post-image" />
                                                )}

                                                <p>{post.content}</p>

                                                <footer className="post-footer">
                                                        <span>Đăng bởi: {post.name || 'Ẩn danh'}</span>
                                                        <span>Ngày: {new Date(post.postDate).toLocaleDateString('vi-VN')}</span>
                                                </footer>
                                        </article>
                                ))}
                        </div>

                        <PostFormModal
                                isOpen={isModalOpen}
                                onClose={() => setIsModalOpen(false)}
                                onSubmit={handleCreatePost}
                                postToEdit={postToEdit}
                        />
                </div>
        );
};

export default PostListPage;