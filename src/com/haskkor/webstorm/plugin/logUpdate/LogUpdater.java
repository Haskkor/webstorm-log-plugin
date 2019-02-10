package com.haskkor.webstorm.plugin.logUpdate;
import com.intellij.openapi.editor.Document;

public class LogUpdater {

    private final Document document;
    private int startIndex;
    private String regex;

    public LogUpdater(Document document, int startIndex) {
        this.document = document;
        this.startIndex = startIndex;
        // Find the console logs containing a line number
        this.regex = "console\.log\((\"|\')[^\"\'\s]*\sl\.[0-9]+\s[^\"\']*(\"|\')[^\)]*\);?";
    }

    // public void updateConsoleLogs() {
        // function with a range finding for the first occurence of console.log(“aaaaaaaa l.0, update it and rescursive with a smaller range. 
        // have to figure out how to change the text
    // }
    
    private void updateConsoleLogs() {
        // TextRange textRange = new TextRange(this.startIndex, this.document.getTextLength());
        CharSequence text = this.document.getCharSequence();
        Pattern pattern = Pattern.compile(this.regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int offsetStart = matcher.start();
            int offsetEnd = matcher.end();
            WriteCommandAction.runWriteCommandAction(project, () ->
                document.replaceString(offsetStart, offsetEnd, "test replace")
            );
        }
    }
}

// https://github.com/JetBrains/intellij-community/blob/master/platform/core-api/src/com/intellij/openapi/editor/Document.java
// https://github.com/JetBrains/intellij-community/blob/master/platform/util/src/com/intellij/openapi/util/TextRange.java
