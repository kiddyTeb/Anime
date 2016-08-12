package com.liangdekai.anime.bean;

import java.util.List;

public class ResultBean {
    private String total ;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<CartoonBean> getBookList() {
        return bookList;
    }

    public void setBookList(List<CartoonBean> bookList) {
        this.bookList = bookList;
    }

    private List<CartoonBean> bookList ;

    private String comicName ;

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

    private List<ChapterBean> chapterList ;

    public List<ChapterBean> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<ChapterBean> chapterList) {
        this.chapterList = chapterList;
    }
}
