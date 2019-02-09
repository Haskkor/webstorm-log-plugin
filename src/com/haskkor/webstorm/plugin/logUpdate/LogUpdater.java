package com.haskkor.webstorm.plugin.logUpdate;
import com.intellij.openapi.editor.Document;

public class LogUpdater {

    private final Document document;

    public LogUpdater(Document document) {
        this.document = document;
    }

    public void findConsoleLogs() {
        CharSequence sequence = document.getCharsSequence();
        // function with a range finding for the first occurence of console.log(“aaaaaaaa l.0, update it and rescursive with a smaller range. 
        // have to figure out how to change the text
        
    }
}
