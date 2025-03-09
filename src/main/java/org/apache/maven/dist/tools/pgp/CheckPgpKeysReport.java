/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.dist.tools.pgp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.apache.maven.dist.tools.AbstractDistCheckReport;
import org.apache.maven.dist.tools.ConfigurationLineInfo;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.reporting.MavenReportException;

import static org.apache.maven.doxia.sink.impl.SinkEventAttributeSet.Semantics.BOLD;

/**
 * Check PGP public KEYS files.
 */
@Mojo(name = "check-pgp-keys", requiresProject = false)
public class CheckPgpKeysReport extends AbstractDistCheckReport {
    /** Constant <code>FAILURES_FILENAME="check-pgp-keys.log"</code> */
    public static final String FAILURES_FILENAME = "check-pgp-keys.log";

    public static final String EDIT_AREA_1_URL = "https://github.com/apache/maven-parent/";

    /** Constant <code>EDIT_KEYS_1_URL="<a href="https://raw.githubusercontent.com/apache/maven-parent/refs/heads/master/KEYS">...</a>"{trunked}</code> */
    public static final String EDIT_KEYS_1_URL =
            "https://raw.githubusercontent.com/apache/maven-parent/refs/heads/master/KEYS";

    public static final String PUBLISH_AREA_2_URL = "https://dist.apache.org/repos/dist/release/maven";

    /** Constant <code>PUBLISH_KEYS_2_URL="<a href="https://dist.apache.org/repos/dist/rele">...</a>"{trunked}</code> */
    public static final String PUBLISH_KEYS_2_URL = PUBLISH_AREA_2_URL + "/KEYS";

    public static final String DOWNLOAD_AREA_3_URL = "https://dlcdn.apache.org/maven";

    /** Constant <code>DOWNLOAD_KEYS_3_URL="<a href="https://dlcdn.apache.org/maven/KEYS">...</a>"{trunked}</code> */
    public static final String DOWNLOAD_KEYS_3_URL = DOWNLOAD_AREA_3_URL + "/KEYS";

    /**
     * Check PGP Keys report.
     */
    public CheckPgpKeysReport() {}

    /** {@inheritDoc} */
    @Override
    protected String getFailuresFilename() {
        return FAILURES_FILENAME;
    }

    /** {@inheritDoc} */
    @Override
    public String getName(Locale locale) {
        return "Dist Tool> Check PGP KEYS";
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription(Locale locale) {
        return "Verification of PGP KEYS files";
    }

    /** {@inheritDoc} */
    @Override
    protected boolean isIndexPageCheck() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    protected void executeReport(Locale locale) throws MavenReportException {
        String editKeys1 = fetchUrl(EDIT_KEYS_1_URL);
        String publishKeys2 = fetchUrl(PUBLISH_KEYS_2_URL);
        String downloadKeys3 = fetchUrl(DOWNLOAD_KEYS_3_URL);

        if (!editKeys1.equals(publishKeys2)) {
            File failure = new File(failuresDirectory, FAILURES_FILENAME);
            try (PrintWriter output = new PrintWriter(new FileWriter(failure))) {
                output.println("PGP KEYS files content is different: " + PUBLISH_KEYS_2_URL + " vs " + EDIT_KEYS_1_URL);
            } catch (Exception e) {
                getLog().error("Cannot append to " + getFailuresFilename());
            }
        }

        Sink sink = getSink();
        sink.head();
        sink.title();
        sink.text("Check PGP public KEYS files");
        sink.title_();
        sink.head_();

        sink.body();
        sink.section1();
        sink.paragraph();
        sink.rawText("Check that PGP public KEYS workflow is ok:");
        sink.paragraph_();
        sink.numberedList(Sink.NUMBERING_DECIMAL);

        // 1. committer edit
        sink.numberedListItem();
        sink.rawText("committer edits <code>KEYS</code> in <a href='" + EDIT_AREA_1_URL
                + "'><code>maven-parent.git</code></a>: ");
        sink.link(EDIT_KEYS_1_URL);
        sink.rawText(EDIT_KEYS_1_URL);
        sink.link_();
        sink.numberedListItem_();

        // 2. PMC publish
        sink.numberedListItem();
        sink.rawText("PMC publishes to <a href='" + PUBLISH_AREA_2_URL + "'>distribution area</a>: ");
        sink.link(PUBLISH_KEYS_2_URL);
        sink.rawText(PUBLISH_KEYS_2_URL);
        sink.link_();
        sink.numberedListItem_();

        // 3. INFRA distribute
        sink.numberedListItem();
        sink.rawText("INFRA syncs to <a href='" + DOWNLOAD_AREA_3_URL + "'>download area</a>: ");
        sink.link(DOWNLOAD_KEYS_3_URL);
        sink.rawText(DOWNLOAD_KEYS_3_URL);
        sink.link_();
        sink.numberedListItem_();

        sink.numberedList_();

        sink.paragraph();
        sink.rawText(
                "Committers are supposed to edit <code>maven-parent.git</code>'s <code>KEYS</code>, then ask PMC for publication, but sometimes PMC"
                        + " members directly add in distribution area, then future publication is not trivial any more.");
        sink.paragraph_();

        sink.paragraph();
        sink.rawText("INFRA distribution is then sync'ed (once per hour?).");
        sink.paragraph_();

        sink.paragraph();
        sink.paragraph_();

        sink.list();
        sink.listItem();
        sink.rawText("KEYS 2 == KEYS 1: ");
        if (publishKeys2.equals(editKeys1)) {
            iconSuccess(sink);
        } else {
            iconError(sink);
        }
        sink.listItem_();

        sink.listItem();
        sink.rawText("KEYS 3 == KEYS 2: ");
        if (downloadKeys3.equals(publishKeys2)) {
            iconSuccess(sink);
        } else {
            iconError(sink);
        }
        sink.listItem_();
        sink.list_();

        sink.numberedList(Sink.NUMBERING_DECIMAL);
        KeysIterator edit1Iterator = new KeysIterator(editKeys1);
        KeysIterator publish2Iterator = new KeysIterator(publishKeys2);
        while (edit1Iterator.hasNext() || publish2Iterator.hasNext()) {
            String editKey1 = edit1Iterator.hasNext() ? edit1Iterator.next() : "";
            String publishKey2 = publish2Iterator.hasNext() ? publish2Iterator.next() : "";

            sink.numberedListItem();
            sink.verbatim(BOLD);
            sink.rawText(editKey1);
            sink.verbatim_();
            if (!publishKey2.equals(editKey1)) {
                sink.rawText("edit (committer) ");
                iconError(sink);
                sink.rawText(" publish (PMC)");

                sink.verbatim(BOLD);
                sink.rawText(publishKey2);
                sink.verbatim_();
            }
            sink.numberedListItem_();
        }
        sink.numberedList_();

        sink.section1_();
        sink.body_();
        sink.close();
    }

    /** {@inheritDoc} */
    @Override
    protected void checkArtifact(ConfigurationLineInfo request, String repoBase) {}

    private String fetchUrl(String url) throws MavenReportException {
        try (InputStream in = new URL(url).openStream();
                Reader reader = new InputStreamReader(in, "UTF-8");
                StringWriter writer = new StringWriter()) {
            IOUtils.copy(reader, writer);
            return writer.toString();
        } catch (IOException ioe) {
            throw new MavenReportException("cannot fetch " + url, ioe);
        }
    }

    private static class KeysIterator implements Iterator<String> {
        private static final String BEGIN = "-----BEGIN PGP PUBLIC KEY BLOCK-----";
        private static final String END = "-----END PGP PUBLIC KEY BLOCK-----";

        private String content;

        KeysIterator(String content) {
            this.content = content.substring(content.indexOf("---") + 3);
        }

        @Override
        public boolean hasNext() {
            return content.length() > 0;
        }

        @Override
        public String next() {
            // get only gpg --list-sigs <ID>, not armoured content
            String id = content.substring(0, content.indexOf(BEGIN)).trim();
            // and even strip variable complex content: pub and uid are the 2 (or 3, it depends) interesting lines here
            int uid = id.indexOf("uid                  ");
            if (uid > 0) {
                int eol = id.indexOf('\n', uid);
                id = id.substring(0, eol);
            }

            content = content.substring(content.indexOf(END) + END.length()).trim();
            return id;
        }
    }
}
