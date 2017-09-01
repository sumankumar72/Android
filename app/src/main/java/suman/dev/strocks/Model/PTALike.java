package suman.dev.strocks.Model;

/**
 * Created by suman on 1/8/17.
 */

public class PTALike {

    public int Id;
    public int AuthorId;
    public int ForumId;

    public PTALike(int Id, int AuthorId, int ForumId) {
        this.Id = Id;
        this.AuthorId = AuthorId;
        this.ForumId = ForumId;
    }
}
