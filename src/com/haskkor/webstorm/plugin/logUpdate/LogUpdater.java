package com.haskkor.webstorm.plugin.logUpdate;
import com.intellij.openapi.editor.Document;

public class LogUpdater {

    private final Document document;

    public LogUpdater(Document document) {
        this.document = document;
    }

    public void findConsoleLogs() {
        CharSequence sequence = document.getCharsSequence();
        
    }
}
