package com.example.myapplication.data

import kotlinx.coroutines.flow.Flow

class PostRepository(private val dao: PostDao) {

    fun observePosts(): Flow<List<Post>> = dao.observeAll()

    suspend fun addPost(content: String) = dao.insert(Post(content = content))

    suspend fun editPost(post: Post, newContent: String) =
        dao.update(post.copy(content = newContent))

    suspend fun removePost(post: Post) = dao.delete(post)
}
