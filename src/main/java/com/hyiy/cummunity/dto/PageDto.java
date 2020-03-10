package com.hyiy.cummunity.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDto<T> {
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer pageCount;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();

    public void setPages(Integer totalCount, Integer page, Integer size) {

        if(totalCount%size==0){
            this.pageCount = totalCount/size;
        }
        else this.pageCount = totalCount/size+1;
        if(page>pageCount){
            page = pageCount;
        }
        if (page<1)
            page = 1;
        this.page = page;
        pages.add(page);
        for(int i =1;i<3;i++){
            if(page-i>0){
                pages.add(0,page-i);
            }
            if(page+i<=pageCount)
                pages.add(page+i);
        }
        if(page==1){
            showPrevious=false;
        }
        else showPrevious=true;
        if(page==pageCount)
            showNext = false;
        else
            showNext = true;

        if(pages.contains(1)){
            showFirstPage = false;
        }
        else  showFirstPage =true;
        if(pages.contains(pageCount))
            showEndPage = false;
        else
            showEndPage = true;
    }
}
