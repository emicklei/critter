package com.philemonworks.critter.action;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.philemonworks.critter.Recording;
import com.philemonworks.critter.rule.RuleContext;
import com.philemonworks.critter.ui.fixed.RecordingInput;

public class Record implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(Record.class);

    @Override
    public String explain() {
        return "record the request + response";
    }

    @Override
    public void perform(RuleContext context) {
        Recording record = new Recording();

        RecordingInput tester = new RecordingInput();
        this.copyRequestContents(context, record, tester);
        this.copyResponseData(context, record, tester);
        try {
            context.recordingDao.save(record);
        } catch (Exception ex) {
            LOG.error("Unable to save recording", ex);
        }
    }

    private void copyRequestContents(RuleContext context, Recording record, RecordingInput tester) {
        tester.contenttype = context.httpContext.getRequest().getHeaderValue("Content-Type");
        record.requestReceivedDate = context.requestReceivedDate;
        if (tester.hasTextualRequestContent()) {
            record.requestContent = context.httpContext.getRequest().getEntity(String.class);
        }
        for (Entry<String, List<String>> each : context.httpContext.getRequest().getRequestHeaders().entrySet()) {
            copyRequestHeader(record, each);
        }
    }

    private void copyResponseData(RuleContext context, Recording record, RecordingInput tester) {
        // see if a response is available
        if (context.forwardResponse != null) {
            tester.contenttype = (String) context.forwardResponse.getMetadata().getFirst("Content-Type");
            if (tester.hasTextualRequestContent()) {
                record.responseContent = this.readStringContents((InputStream) context.forwardResponse.getEntity());
            }
        }
        for (Entry<String, List<Object>> each : context.forwardResponse.getMetadata().entrySet()) {
            copyResponseHeader(record, each);
        }
    }

    private void copyResponseHeader(Recording record, Entry<String, List<Object>> each) {
        try {
            record.responseHeaders.put(each.getKey(), (String) each.getValue().get(0));
        } catch (Exception ex) {
            LOG.error("Unable to copy response header", ex);
        }
    }

    private void copyRequestHeader(Recording record, Entry<String, List<String>> each) {
        try {
            record.requestHeaders.put(each.getKey(), each.getValue().get(0));
        } catch (Exception ex) {
            LOG.error("Unable to copy request header", ex);
        }
    }

    private String readStringContents(InputStream is) {
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder(1024);
            while (buf.ready()) {
                sb.append(buf.readLine()).append("\n");
            }
            return sb.toString();
        } catch (Exception ex) {
            LOG.error("Unable to read string contents", ex);
        }
        return "";
    }
}
