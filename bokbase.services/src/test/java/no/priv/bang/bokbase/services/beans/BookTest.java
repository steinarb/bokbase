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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void testCreate() {
        long bookId = 1L;
        String title = "Lord Vorpatril's Alliance";
        String subtitle = "A space romance";
        Long seriesId = 3L;
        String series = "Vorkosigan";
        Double seriesNumber = 13.0;
        Long authorId = 4L;
        String authorName = "Lois McMaster Bujold";
        Integer rating = 5;
        Integer averageRating = 4;
        Long publisherId = 5L;
        String publisherName = "Baen Books";
        Binding binding = Binding.Hardcover;
        Integer pages = 414;
        Integer yearPublished = 2013;
        Integer monthRead = 7;
        Integer yearRead = 2013;
        Bookshelf bookshelf = Bookshelf.read;
        Book bean = Book.with()
            .bookId(bookId)
            .title(title)
            .subtitle(subtitle)
            .seriesId(seriesId)
            .series(series)
            .seriesNumber(seriesNumber)
            .authorId(authorId)
            .authorName(authorName)
            .rating(rating)
            .averageRating(averageRating)
            .publisherId(publisherId)
            .publisherName(publisherName)
            .binding(binding)
            .pages(pages)
            .yearPublished(yearPublished)
            .monthRead(monthRead)
            .yearRead(yearRead)
            .bookshelf(bookshelf)
            .build();
        assertNotNull(bean);
        assertEquals(bookId, bean.getBookId());
        assertEquals(title, bean.getTitle());
        assertEquals(subtitle, bean.getSubtitle());
        assertEquals(seriesId, bean.getSeriesId());
        assertEquals(series, bean.getSeries());
        assertEquals(seriesNumber, bean.getSeriesNumber(), 0.0);
        assertEquals(authorId, bean.getAuthorId());
        assertEquals(authorName, bean.getAuthorName());
        assertEquals(rating, bean.getRating());
        assertEquals(averageRating, bean.getAverageRating());
        assertEquals(publisherId, bean.getPublisherId());
        assertEquals(publisherName, bean.getPublisherName());
        assertEquals(binding, bean.getBinding());
        assertEquals(pages, bean.getPages());
        assertEquals(yearPublished, bean.getYearPublished());
        assertEquals(monthRead, bean.getMonthRead());
        assertEquals(yearRead, bean.getYearRead());
        assertEquals(bookshelf, bean.getBookshelf());
        assertThat(bean.toString()).contains(title).contains(authorName);
    }

    @Test
    void testCreateCopy() {
        Book bean = Book.with()
            .title("Lord Vorpatril's Alliance")
            .subtitle("A space romance")
            .seriesId(3L)
            .series("Vorkosigan")
            .seriesNumber(13.0)
            .authorId(4L)
            .authorName("Lois McMaster Bujold")
            .rating(5)
            .averageRating(4)
            .publisherId(5L)
            .publisherName("Baen Books")
            .binding(Binding.Hardcover)
            .pages(414)
            .yearPublished(2013)
            .monthRead(7)
            .yearRead(2013)
            .bookshelf(Bookshelf.read)
            .build();
        Book copyOfBean = Book.with(bean).build();
        assertThat(copyOfBean).usingRecursiveComparison().isEqualTo(bean);
        assertEquals(bean, copyOfBean);
    }

}
