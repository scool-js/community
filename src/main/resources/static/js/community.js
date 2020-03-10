function post() {
    var questionId=$("#question_id").val();
    var content = $("#comment_content").val();
   comment2target(questionId,1,content);
}
function comment2target(targetId,type,content) {
    $.ajax({
        type: "POST",
        url: "/comment",
        data: JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        success: function (response) {
            if(response.code==200){
                window.location.reload();
            }
            else{
                if(response.code==2003){
                    var isAccepted =  confirm(response.message);
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=90d98e387a80739a762a&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable",true);
                    }
                    else {
                        alert(response.message);
                    }
                }

            }
        },
        dataType: "json",
        contentType:"application/json"
    });
}
function comment(e) {
    var id = e.getAttribute("data-id");
    var content = $("#input-"+id).val();
    console.log(id+content);
    comment2target(id,2,content);

}
/*
* */
function collapseComments(e){
    var id = e.getAttribute("data-id");
    var collaspe =  e.getAttribute("data-collapse");
    var comments =  $("#comment-"+id);
    if(collaspe){
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }
    else{
        var subCommentContainer = $("#comment-"+id);

        if(subCommentContainer.children().length!=1){
            comments.addClass("in");
            e.setAttribute("data-collapse","in");
            e.classList.add("active");
        }
        else{
            //展开二级评论
            $.getJSON("/comment/"+id,function (data) {
                $.each(data.data.reverse(),function (index,comment) {
                    var bodyElement = $("<div/>",{
                        class:"media-body"
                    }).append($("<h5/>", {
                        class: "media-heading",
                        text:comment.user.name
                    })).append($("<div/>", {
                        text:comment.content
                    })).append($("<div/>", {
                        class:"menu"
                    }).append($("<span/>", {
                        class:"float-right icon",
                        "html":moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));
                    var mediaLeftElement = $("<div/>",{
                        class:"media-left"
                    }).append($("<img/>", {
                        class: "media-object img-rounded",
                        style: "width: 38px",
                        src: comment.user.avatarUrl
                    }));
                    var mediaElement = $("<div/>",{
                       class: "media"
                    }).append(mediaLeftElement).append(bodyElement);
                    var commentElement = $("<div/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                // body.append($("<div/>",{
                //     "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 collapse sub-comments",
                //     "id":"comment-"+id,
                //     html:items.join("")
                // }));
                comments.addClass("in");
                e.setAttribute("data-collapse","in");
                e.classList.add("active");
            });
        }
    }
}

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var  previous = $("#tag-text").val();
    if(previous.indexOf(value)==-1){
        if(previous){
            $("#tag-text").val(previous +','+value);
        }
        else
        {
            $("#tag-text").val(value);
        }
    }
}

function showSelectTag() {
    $("#select-tag").show();
}