/*
 * Copyright 2021 Steinar Bang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package no.priv.bang.bokbase.services.beans;

import no.priv.bang.beans.immutable.Immutable;

public class Book extends Immutable {
    private long bookId;
    private String title;
    private String subtitle;
    private Long seriesId;
    private String series;
    private Double seriesNumber;
    private Long authorId;
    private String authorName;
    private Integer rating;
    private Integer averageRating;
    private Long publisherId;
    private String publisherName;
    private Binding binding;
    private Integer pages;
    private Integer yearPublished;
    private Integer monthRead;
    private Integer yearRead;
    private Bookshelf bookshelf;
    private Book() {}

    public long getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public Long getSeriesId() {
        return seriesId;
    }

    public String getSeries() {
        return series;
    }

    public Double getSeriesNumber() {
        return seriesNumber;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Integer getRating() {
        return rating;
    }

    public Integer getAverageRating() {
        return averageRating;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public Binding getBinding() {
        return binding;
    }

    public Integer getPages() {
        return pages;
    }

    public Integer getYearPublished() {
        return yearPublished;
    }

    public Integer getMonthRead() {
        return monthRead;
    }

    public Integer getYearRead() {
        return yearRead;
    }

    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    public static Builder with() {
        return new Builder();
    }

    public static Builder with(Book originalBook) {
        Builder builder = new Builder();
        builder.bookId = originalBook.bookId;
        builder.title = originalBook.title;
        builder.subtitle = originalBook.subtitle;
        builder.seriesId = originalBook.seriesId;
        builder.series = originalBook.series;
        builder.seriesNumber = originalBook.seriesNumber;
        builder.authorId = originalBook.authorId;
        builder.authorName = originalBook.authorName;
        builder.rating = originalBook.rating;
        builder.averageRating = originalBook.averageRating;
        builder.publisherId = originalBook.publisherId;
        builder.publisherName = originalBook.publisherName;
        builder.binding = originalBook.binding;
        builder.pages = originalBook.pages;
        builder.yearPublished = originalBook.yearPublished;
        builder.monthRead = originalBook.monthRead;
        builder.yearRead = originalBook.yearRead;
        builder.bookshelf = originalBook.bookshelf;
        return builder;
    }

    public static class Builder {
        private long bookId;
        private String title;
        private String subtitle;
        private Long seriesId;
        private String series;
        private Double seriesNumber;
        private Long authorId;
        private String authorName;
        private Integer rating;
        private Integer averageRating;
        private Long publisherId;
        private String publisherName;
        private Binding binding;
        private Integer pages;
        private Integer yearPublished;
        private Integer monthRead;
        private Integer yearRead;
        private Bookshelf bookshelf;
        private Builder() {}

        public Book build() {
            Book book = new Book();
            book.bookId = bookId;
            book.title = title;
            book.subtitle = subtitle;
            book.seriesId = seriesId;
            book.series = series;
            book.seriesNumber = seriesNumber;
            book.authorId = authorId;
            book.authorName = authorName;
            book.rating = rating;
            book.averageRating = averageRating;
            book.publisherId = publisherId;
            book.publisherName = publisherName;
            book.binding = binding;
            book.pages = pages;
            book.yearPublished = yearPublished;
            book.monthRead = monthRead;
            book.yearRead = yearRead;
            book.bookshelf = bookshelf;
            return book;
        }

        public Builder bookId(long bookId) {
            this.bookId = bookId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Builder seriesId(Long seriesId) {
            this.seriesId = seriesId;
            return this;
        }

        public Builder series(String series) {
            this.series = series;
            return this;
        }

        public Builder seriesNumber(Double seriesNumber) {
            this.seriesNumber = seriesNumber;
            return this;
        }

        public Builder authorId(Long authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder authorName(String authorName) {
            this.authorName = authorName;
            return this;
        }

        public Builder rating(Integer rating) {
            this.rating = rating;
            return this;
        }

        public Builder averageRating(Integer averageRating) {
            this.averageRating = averageRating;
            return this;
        }

        public Builder publisherId(Long publisherId) {
            this.publisherId = publisherId;
            return this;
        }

        public Builder publisherName(String publisherName) {
            this.publisherName = publisherName;
            return this;
        }

        public Builder binding(Binding binding) {
            this.binding = binding;
            return this;
        }

        public Builder pages(Integer pages) {
            this.pages = pages;
            return this;
        }

        public Builder yearPublished(Integer yearPublished) {
            this.yearPublished = yearPublished;
            return this;
        }

        public Builder monthRead(Integer monthRead) {
            this.monthRead = monthRead;
            return this;
        }

        public Builder yearRead(Integer yearRead) {
            this.yearRead = yearRead;
            return this;
        }

        public Builder bookshelf(Bookshelf bookshelf) {
            this.bookshelf = bookshelf;
            return this;
        }

    }

    @Override
    public String toString() {
        return "Book [bookId=" + bookId + ", title=" + title + ", subtitle=" + subtitle + ", seriesId=" + seriesId
            + ", series=" + series + ", seriesNumber=" + seriesNumber + ", authorId=" + authorId + ", authorName="
            + authorName + ", rating=" + rating + ", averageRating=" + averageRating + ", publisherId="
            + publisherId + ", publisherName=" + publisherName + ", binding=" + binding + ", pages=" + pages
            + ", yearPublished=" + yearPublished + ", monthRead=" + monthRead + ", yearRead=" + yearRead
            + ", bookshelf=" + bookshelf + "]";
    }

}
