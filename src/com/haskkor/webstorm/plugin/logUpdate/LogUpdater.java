package com.haskkor.webstorm.plugin.logUpdate;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogUpdater {

    private Document document;
    private Project project;
    private int startIndex;
//    private String regex;
    private Pattern pattern;

    public LogUpdater(Project project, Document document, int startIndex) {
        this.project = project;
        this.document = document;
        this.startIndex = startIndex;
        // Find the console logs containing a line number
        this.pattern = Pattern.compile("console.log\\([\"|'][^\"'\\s]*\\sl\\.[0-9]+(\\s[^\"']*)?[\"|'][^)]*\\);?");
    }

    // public void updateConsoleLogs() {
    // function with a range finding for the first occurence of console.log(“aaaaaaaa l.0, update it and rescursive with a smaller range.
    // have to figure out how to change the text
    // }

    public void updateConsoleLogs() {
        // TextRange textRange = new TextRange(this.startIndex, this.document.getTextLength());
        CharSequence text = this.document.getCharsSequence();
        Matcher matcher = this.pattern.matcher(text);
        int position = this.startIndex;
        while (matcher.find(position)) {
            int offsetStart = matcher.start();
            int offsetEnd = matcher.end();
            WriteCommandAction.runWriteCommandAction(project, () ->
                    document.replaceString(offsetStart, offsetEnd, "test replace")
            );
            position++;
        }
    }
}


// test this
    // skip some lines
// Start from the start of the document
// make a real text replace
// extract string building functions in own class

