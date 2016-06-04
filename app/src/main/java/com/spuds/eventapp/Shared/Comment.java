package com.spuds.eventapp.Shared;

/**
 * Created by tina on 5/4/16.
 */
/*---------------------------------------------------------------------------
Class Name:                Comment
Description:               This class describes a Comment object
---------------------------------------------------------------------------*/
public class Comment {

    public String commentId;
    public String commentParentId;
    public String commentName;
    public String commentPicFileName;
    public String commentDate;
    public String commentText;
    public boolean commentFollow;

    /*---------------------------------------------------------------------------
    Function Name:                Comment
    Description:                  Constructor
    Input:                        String id,
                                  String parentId,
                                  String name,
                                  String picFileName,
                                  String date,
                                  String text,
                                  boolean follow
    Output:                       None
    ---------------------------------------------------------------------------*/
    public Comment(String id, String parentId, String name, String picFileName, String date, String text, boolean follow) {
        this.commentId = id;
        this.commentParentId = parentId;
        this.commentName = name;
        this.commentPicFileName = picFileName;
        this.commentDate = date;
        this.commentText = text;
        this.commentFollow = follow;
    }

}
