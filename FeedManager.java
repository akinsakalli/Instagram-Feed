import java.io.*;
import java.util.ArrayList;

public class FeedManager {
    // All the users and posts are stored in hash maps, in order to access them in constant time whenever needed.
    // The keys for the hash maps are the user ID's for users and the post ID's for posts.
    // The values for the hash maps are the user and post objects that contain the corresponding information.
    private static MyHashMap<String, User> users = new MyHashMap<>();
    private static MyHashMap<String, Post> posts = new MyHashMap<>();

    // Reader and writer objects are declared as static data fields in order to be used in static methods.
    private static BufferedReader reader;
    private static BufferedWriter writer;

    /**
     * This is the method where all the scanning process from the input file is done and the corresponding methods are called.
     * @param inputFileName is the name of the file to scan data from.
     * @param outputFileName is the name of the file to write data to.
     * @throws IOException if there does not exist any files with the given name.
     */
    public static void scanFile(String inputFileName, String outputFileName) throws IOException {
        // Reader and writer objects are initialised.
        reader = new BufferedReader(new FileReader(inputFileName));
        writer = new BufferedWriter(new FileWriter(outputFileName));
        String line;
        String[] input;
        String action;

        // For each line of the input file, the line is read and if the line is not empty,
        // The corresponding method is called according to the first word of that line.
        while ((line = reader.readLine()) != null) {
            input = line.split(" ");
            action = input[0];
            switch (action) {
                case ("create_user"): {
                    createUser(input);
                    break;
                }
                case ("follow_user"): {
                    followUser(input);
                    break;
                }
                case ("unfollow_user"): {
                    unfollowUser(input);
                    break;
                }
                case ("create_post"): {
                    createPost(input);
                    break;
                }
                case ("see_post"): {
                    seePost(input);
                    break;
                }
                case ("see_all_posts_from_user"): {
                    seeAllPostsFromUser(input);
                    break;
                }
                case ("toggle_like"): {
                    toggleLike(input);
                    break;
                }
                case ("generate_feed"): {
                    generateFeed(input);
                    break;
                }
                case ("scroll_through_feed"): {
                    scrollThroughFeed(input);
                    break;
                }
                case ("sort_posts"): {
                    sortPosts(input);
                    break;
                }
            }
        }
        writer.flush();
        writer.close();
    }
    public static void createUser(String[] input) throws IOException {
        // User ID is extracted.
        String userId = input[1];

        // If the userID is not appropriate or the user does not exist, error is logged to the output file.
        if (!(userId.startsWith("user")) || users.containsKey(userId)) {
            writer.write("Some error occurred in create_user.");
            writer.newLine();
        }
        // The user with the given ID is created and placed into the hash map containing the users.
        else {
            users.put(userId, new User(userId));
            writer.write("Created user with Id " + userId + ".");
            writer.newLine();
        }
    }
    public static void followUser(String[] input) throws IOException {
        // Users' IDs are extracted from the input array.
        String userId1 = input[1];
        String userId2 = input[2];

        // If any of the user IDs are not appropriate, or any of the users does not exist,
        // or the user IDs are equal or user is already following the other user, error is logged.
        if (!(userId1.startsWith("user")) ||
                !(userId2.startsWith("user")) ||
                !users.containsKey(userId1) ||
                !users.containsKey(userId2) ||
                userId1.equals(userId2) ||
                users.get(userId1).isFollowing(users.get(userId2))
        ) {
            writer.write("Some error occurred in follow_user.");
            writer.newLine();
        }
        // Users are accessed by using their IDs, via the hash map.
        // Then the first user follows the second user.
        else {
            User user1 = users.get(userId1);
            User user2 = users.get(userId2);
            user1.follow(user2);
            writer.write(userId1 + " followed " + userId2 + ".");
            writer.newLine();
        }
    }
    public static void unfollowUser(String[] input) throws IOException {
        // Users' IDs are extracted from the input array.
        String userId1 = input[1];
        String userId2 = input[2];

        // If any of the user IDs are not appropriate, or any of the users does not exist,
        // or the user IDs are equal or user is already not following the other user, error is logged.
        if (!(userId1.startsWith("user")) ||
                !(userId2.startsWith("user")) ||
                !(users.containsKey(userId1)) ||
                !(users.containsKey(userId2)) ||
                userId1.equals(userId2) ||
                !(users.get(userId1).isFollowing(users.get(userId2)))
        ) {
            writer.write("Some error occurred in unfollow_user.");
            writer.newLine();
        }
        // The first user unfollows the second user.
        else {
            User user1 = users.get(userId1);
            User user2 = users.get(userId2);
            user1.unfollow(user2);
            writer.write(userId1 + " unfollowed " + userId2 + ".");
            writer.newLine();
        }
    }
    public static void createPost(String[] input) throws IOException {
        // The needed information for creating a post is extracted.
        String userId = input[1];
        String postId = input[2];
        String content = input[3];

        // If the user with the given id is not present or there is already a post with the given id, error is logged.
        if (!users.containsKey(userId) || posts.containsKey(postId)) {
            writer.write("Some error occurred in create_post.");
            writer.newLine();
        }
        // The post is created and put into the hash map, then the post is added into the user's posts data field.
        else {
            User author = users.get(userId);
            Post post = new Post(postId, author, content);
            posts.put(postId, post);
            author.createPost(post);
            writer.write(userId + " created a post with Id " + postId + ".");
            writer.newLine();
        }
    }
    public static void seePost(String[] input) throws IOException {
        // User and post IDs are extracted from the array.
        String userId = input[1];
        String postId = input[2];

        // If there does not exist such a user or such a post, error is logged.
        if (!(users.containsKey(userId)) || !(posts.containsKey(postId))) {
            writer.write("Some error occurred in see_post.");
            writer.newLine();
        }
        // Corresponding user and post is accessed from the hash maps, then the user sees that post.
        else {
            User user = users.get(userId);
            Post post = posts.get(postId);
            user.see(post);
            writer.write(userId + " saw " + postId + ".");
            writer.newLine();
        }
    }
    public static void seeAllPostsFromUser(String[] input) throws IOException {
        // Users' IDs are extracted.
        String viewerId = input[1];
        String viewedId = input[2];

        // If any of the users does not exist, error is logged.
        if (!(users.containsKey(viewerId)) || !(users.containsKey(viewedId))) {
            writer.write("Some error occurred in see_all_posts_from_user.");
            writer.newLine();
        }
        // The first user sees all posts of the second user.
        else {
            User user1 = users.get(viewerId);
            User user2 = users.get(viewedId);
            user1.seeAllPosts(user2);
            writer.write(viewerId + " saw all posts of " + viewedId + ".");
            writer.newLine();
        }
    }
    public static void toggleLike(String[] input) throws IOException {
        // User and post IDs are extracted.
        String userId = input[1];
        String postId = input[2];

        // If the user or the post is not present, error is logged.
        if (!(users.containsKey(userId)) || !(posts.containsKey(postId))) {
            writer.write("Some error occurred in toggle_like.");
            writer.newLine();
        }
        else {
            User user = users.get(userId);
            Post post = posts.get(postId);

            // If the user has not liked the post before, the post is liked.
            if (!user.hasLikedPost(post)) {
                user.like(post);
                writer.write(userId + " liked " + postId + ".");
                writer.newLine();
            }
            // If the user has already liked the post before, the post is unliked.
            else {
                user.unlike(post);
                writer.write(userId + " unliked " + postId + ".");
                writer.newLine();
            }
        }
    }
    public static void generateFeed(String[] input) throws IOException {
        // User ID and the number of posts to be shown in the feed is extracted.
        String userId = input[1];
        int num = Integer.parseInt(input[2]);

        // If there does not exist such a user with the given ID, error is logged.
        if (!(users.containsKey(userId))) {
            writer.write("Some error occurred in generate_feed.");
            writer.newLine();
        }
        else {
            User user = users.get(userId);

            // Initially, posts to be shown in the feed is added to an arraylist.
            ArrayList<Post> postsForFeed = new ArrayList<>();

            // All the posts of all the users that is being followed are added to the arraylist if they are not seen by the present user before.
            for (User followedUser: user.getFollowings()) {
                for (Post post: followedUser.getPosts()) {
                    if (!user.hasSeenPost(post))
                        postsForFeed.add(post);
                }
            }
            // All these posts in the arraylist are transferred into an array containing posts.
            Post[] feedArray = new Post[postsForFeed.size()];
            postsForFeed.toArray(feedArray);

            // A max heap is created with these posts, so that the element with the maximum amount of likes is accessed in constant time.
            MyMaxHeap<Post> feedHeap = new MyMaxHeap<>(feedArray);

            // Logging of the feed starts.
            writer.write("Feed for " + userId + ":");
            writer.newLine();
            Post post;

            // Iterations are made as many times as the number of posts to be displayed.
            for (int i=0; i < num; i++) {
                // The post with the most likes is extracted, if there exists such a post, it is logged with its information.
                if ( (post = feedHeap.deleteMax()) != null ) {
                    writer.write("Post ID: " + post.getId() + ", Author: " + post.getAuthor().getId() + ", Likes: " + post.getLikes());
                    writer.newLine();
                }
                // If there are no more posts left, generation of feed is stopped.
                else {
                    writer.write("No more posts available for " + userId + ".");
                    writer.newLine();
                    break;
                }
            }
        }
    }
    public static void scrollThroughFeed(String[] input) throws IOException {
        // User ID and the number of scroll operations are extracted.
        String userId = input[1];
        int num = Integer.parseInt(input[2]);

        // If there does not exist such a user with the given ID, error is logged.
        if (!users.containsKey(userId)) {
            writer.write("Some error occurred in scroll_through_feed.");
            writer.newLine();
        }
        else {
            User user = users.get(userId);

            // Every unseen post of every user that is being followed is added to an arraylist.
            ArrayList<Post> postsForFeed = new ArrayList<>();
            for (User followedUser: user.getFollowings()) {
                for (Post post: followedUser.getPosts()) {
                    if (!user.hasSeenPost(post))
                        postsForFeed.add(post);
                }
            }
            // Then all these posts are transferred to an array.
            Post[] feedArray = new Post[postsForFeed.size()];
            postsForFeed.toArray(feedArray);

            // A heap with these posts is created in order to access the post with most likes in constant time.
            MyMaxHeap<Post> feedHeap = new MyMaxHeap<>(feedArray);

            writer.write(userId + " is scrolling through feed:");
            writer.newLine();
            Post post;
            String postId;
            int isLiked;

            // As many iterations as the number of scrolls is done.
            for (int i = 0; i < num; i++) {
                // The post with the most likes is extracted, if there exist such a user, it is seen and liked (if the input says so).
                if ( (post = feedHeap.deleteMax()) != null ) {
                    isLiked = Integer.parseInt(input[3 + i]);
                    postId = post.getId();
                    user.see(post);

                    if (isLiked == 1) {
                        user.like(post);
                        writer.write(userId + " saw " + postId + " while scrolling and clicked the like button.");
                        writer.newLine();
                    } else {
                        writer.write(userId + " saw " + postId + " while scrolling.");
                        writer.newLine();
                    }
                }
                // If all the posts are seen, scrolling through the feed is stopped.
                else {
                    writer.write("No more posts in feed.");
                    writer.newLine();
                    break;
                }
            }
        }
    }
    public static void sortPosts(String[] input) throws IOException {
        // User ID is extracted.
        String userId = input[1];

        // If there does not exist such a user, error is logged.
        if (!users.containsKey(userId)) {
            writer.write("Some error occurred in sort_posts.");
            writer.newLine();
        }
        else {
            User user = users.get(userId);
            // If the user has no posts, error is logged.
            if (user.getPosts().isEmpty()) {
                writer.write("No posts from " + userId + ".");
                writer.newLine();
            }
            else {
                writer.write("Sorting " + userId +"'s posts:");
                writer.newLine();
                MyMaxHeap<Post> usersPosts = user.getPosts();

                // All the posts of the user is copied into an array.
                Post[] postsCopy = new Post[usersPosts.getSize()];
                int i=0;
                for (Post post: usersPosts) {
                    postsCopy[i] = post;
                    i++;
                }

                // A max heap is created from the user's posts, in order to make sure that all the posts are sorted.
                MyMaxHeap<Post> posts = new MyMaxHeap<>(postsCopy);
                Post post;

                // Delete the post with the maximum likes and log its information, until all the posts are logged.
                while ((post = posts.deleteMax()) != null) {
                    writer.write(post.getId() + ", Likes: " + post.getLikes());
                    writer.newLine();
                }
            }
        }
    }
}
