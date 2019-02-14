package com.haskkor.webstorm.plugin.logUpdate;

import com.haskkor.webstorm.plugin.documentHandler.DocumentHandler;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogUpdater {

    private Document document;
    private Project project;
    private int startIndex;
    private Pattern patternLog;
    private DocumentHandler documentHandler;

    public LogUpdater(Project project, Document document, int startIndex, AnActionEvent anActionEvent) {
        this.project = project;
        this.document = document;
        this.startIndex = startIndex;
        this.documentHandler = new DocumentHandler(this.project, this.document, anActionEvent);
        // Find the console logs containing a line number
        this.patternLog = Pattern.compile("console.log\\([\"|'][^\"'\\s]*\\sl\\.[0-9]+(\\s[^\"']*)?[\"|'][^)]*\\);?");
    }

    public void updateConsoleLogs() {
        CharSequence text = this.document.getCharsSequence();
        Matcher matcher = this.patternLog.matcher(text);
        int position = this.startIndex;
        while (matcher.find(position)) {
            int offsetStart = matcher.start();
            int offsetEnd = matcher.end();
            documentHandler.replaceText(offsetStart, offsetEnd,
                    buildStringReplace(document.getText(new TextRange(offsetStart, offsetEnd)), offsetStart));
            position++;
        }
    }

    private String buildStringReplace(String text, int offset) {
        Pattern ptn = Pattern.compile("console.log\\([\"|'][^\"'\\s]*\\sl\\.[0-9]+");
        Matcher matcher = ptn.matcher(text);
        String replace = "console.log(\'" + this.documentHandler.getFileName() + " l." + (this.document.getLineNumber(offset) + 1);
        return matcher.replaceFirst(replace);
    }
}
