public class User {
    private String id; // User ID, it is used while hashing.
    private MyHashMap<String, User> followings; // The users that is being followed.
    private MyMaxHeap<Post> posts;
    private MyHashMap<String, Post> seenPosts;
    private MyHashMap<String, Post> likedPosts;
    public User(String id) {
        this.id = id;
        followings = new MyHashMap<>();
        posts = new MyMaxHeap<>();
        seenPosts = new MyHashMap<>();
        likedPosts = new MyHashMap<>();
    }
    public String getId() {
        return this.id;
    }

    // Follows the corresponding user.
    public void follow(User user) {
        this.followings.put(user.getId(), user);
    }

    // Unfollows the corresponding user.
    public void unfollow(User user) {
        this.followings.remove(user.getId());
    }

    // Returns true if this user is following the corresponding user.
    public boolean isFollowing(User user) {
        return this.followings.containsKey(user.getId());
    }
    public void createPost(Post post) {
        posts.insert(post);
    }
    public void see(Post post) {
        seenPosts.put(post.getId(), post);
    }
    public void seeAllPosts(User user) {
        for (Post post: user.posts) {
            see(post);
        }
    }
    public boolean hasLikedPost(Post post) {
        return this.likedPosts.containsKey(post.getId());
    }
    public boolean hasSeenPost(Post post) {
        return this.seenPosts.containsKey(post.getId());
    }
    public void like(Post post) {
        see(post);
        post.incrementLike();
        likedPosts.put(post.getId(), post);
    }
    public void unlike(Post post) {
        post.decrementLike();
        likedPosts.remove(post.getId());
    }
    public MyHashMap<String, User> getFollowings() {
        return this.followings;
    }
    public MyMaxHeap<Post> getPosts() {
        return this.posts;
    }
}