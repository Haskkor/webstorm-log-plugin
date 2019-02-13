package com.haskkor.webstorm.plugin.logUpdate;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogUpdater {

    private Document document;
    private Project project;
    private int startIndex;
    private Pattern patternLog;
    private String fileName;

    public LogUpdater(Project project, Document document, String fileName, int startIndex) {
        this.project = project;
        this.document = document;
        this.startIndex = startIndex;
        this.fileName = fileName;
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
            WriteCommandAction.runWriteCommandAction(project, () ->
                    document.replaceString(offsetStart, offsetEnd,
                            buildStringReplace(document.getText(new TextRange(offsetStart, offsetEnd))))
            );
            position++;
        }
    }

    private String buildStringReplace(String text) {
        Pattern ptn = Pattern.compile("console.log\\([\"|'][^\"'\\s]*\\s");
        Matcher matcher = ptn.matcher(text);
        String replace = "console.log(\'" + this.fileName + " ";
        return matcher.replaceFirst(replace);
    }
}


// make a real text replace
// extract string building functions in own class
