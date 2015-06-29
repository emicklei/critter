package com.philemonworks.critter.ui.fixed;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jruijgers on 29/06/15.
 */
public class RecordingInputTest {
    private RecordingInput subject;

    @Before
    public void setUp() throws Exception {
        subject = new RecordingInput();
    }

    @Test
    public void hasTextualContextTypeWithNoContentTypeSetShouldReturnFalse() {
        assertThat(subject.hasTextualRequestContent(), is(false));
    }

    @Test
    public void hasTextualContextTypeWithTextHtmlContentTypeShouldReturnTrue() {
        subject.setContenttype("text/html; charset: UTF-8");
        assertThat(subject.hasTextualRequestContent(), is(true));
    }

    @Test
    public void hasTextualContextTypeWithXmlContentTypeShouldReturnTrue() {
        subject.setContenttype("application/xml; charset: UTF-8");
        assertThat(subject.hasTextualRequestContent(), is(true));
    }

    @Test
    public void hasTextualContextTypeWithJsonContentTypeShouldReturnTrue() {
        subject.setContenttype("application/xml; charset: UTF-8");
        assertThat(subject.hasTextualRequestContent(), is(true));
    }

    @Test
    public void hasTextualContextTypeWithTextPlainContentTypeShouldReturnTrue() {
        subject.setContenttype("text/plain; charset: UTF-8");
        assertThat(subject.hasTextualRequestContent(), is(true));
    }
    @Test
    public void hasTextualResponseContextTypeWithTextPlainContentTypeShouldReturnTrue() {
        subject.setResponsecontenttype("text/plain; charset: UTF-8");
        assertThat(subject.hasTextualResponseContent(), is(true));
    }

    @Test
    public void hasTextualContextTypeWitBinaryContentTypeShouldReturnFalse() {
        subject.setContenttype("application/octet-stream");
        assertThat(subject.hasTextualRequestContent(), is(false));
    }
}
