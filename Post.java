public class Post implements Comparable<Post>{
    private String id; // Post ID, it is used while hashing and for comparisons between posts.
    private User author; // The user who created this post.
    private String content;
    private int likes;
    public Post(String id, User author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.likes = 0;
    }
    public String getId() { return this.id; }
    public User getAuthor() { return this.author; }
    public int getLikes() { return this.likes; }
    public void incrementLike() { this.likes++; }
    public void decrementLike() {
        if (this.likes > 0)
            this.likes--;
    }

    /**
     * The post with the greater number of likes is bigger than the other.
     * If the number of likes is equal, then the posts are compared lexicographically according to their IDs.
     * @param post the post to be compared.
     * @return 1 if this object is greater, 0 if equal, -1 if this object is smaller than the other post.
     */
    @Override
    public int compareTo(Post post) {
        if (this.likes > post.likes)
            return 1;
        else if (this.likes < post.likes)
            return -1;
        else {
            return this.id.compareTo(post.getId());
        }
    }
}
