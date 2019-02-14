package com.haskkor.webstorm.plugin.log;

import com.haskkor.webstorm.plugin.documentHandler.DocumentHandler;
import com.haskkor.webstorm.plugin.logFormat.LogFormatter;
import com.haskkor.webstorm.plugin.logUpdate.LogUpdater;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

public class NewLineLog extends AnAction {

    private Project project;
    private Editor editor;
    private Document document;
    private CaretModel caretModel;
    private DocumentHandler documentHandler;

    @Override
    public void actionPerformed(AnActionEvent e) {
        this.project = e.getProject();
        this.editor = e.getData(CommonDataKeys.EDITOR);
        this.caretModel = editor.getCaretModel();
        this.document = editor.getDocument();
        this.documentHandler = new DocumentHandler(this.project, this.document, e);

        //Set visibility only in case of existing project and editor and if some text in the editor is selected
        e.getPresentation().setVisible(project != null && editor != null && editor.getSelectionModel().hasSelection());

        // Get caret offset
        int caretOffset = caretModel.getOffset();
        // Get line number
        int lineNumber = document.getLineNumber(caretOffset);
        // Get file name
        String fileName = this.documentHandler.getFileName();
        // Get line end position
        int lineEnd = document.getLineEndOffset(lineNumber);

        // Build the string
        LogFormatter logFormatter = new LogFormatter();
        String text = logFormatter.buildString(fileName, lineNumber, findWordUnderCaret(caretOffset));

        // Write in the document
        this.documentHandler.replaceText(lineEnd, lineEnd, text);

        // Move the caret
        caretModel.moveCaretRelatively(text.length(), 1, false, false, true);

        // Get new offset and update the other logs
        int newCaretOffset = caretModel.getOffset();
        LogUpdater logUpdater = new LogUpdater(project, document, newCaretOffset, e);
        logUpdater.updateConsoleLogs();
    }

    private String findWordUnderCaret(int offset) {
        CharSequence sequence = document.getCharsSequence();
        char firstLetter = sequence.charAt(offset);
        if (!Character.isLetter(firstLetter) && !Character.isDigit(firstLetter)) {
            return "";
        } else {
            return sequence.subSequence(findWordStart(sequence, offset), findWordEnd(sequence, offset)).toString();
        }
    }

    private int findWordStart(CharSequence seq, int start) {
        int val = start;
        for (int i = start; i > 1; i--) {
            char c = seq.charAt(i);
            if (!Character.isLetter(c) && !Character.isDigit(c) && c != '_' && c != '.') {
                val = i + 1;
                break;
            }
            val = i - 1;
        }
        return val;
    }

    private int findWordEnd(CharSequence seq, int start) {
        int val = start;
        for (int i = start; i <= seq.length() - 1; i++) {
            char c = seq.charAt(i);
            if (c == '(') {
                for (int j = i; j <= seq.length(); j++) {
                    char d = seq.charAt(j);
                    if (d == ')') {
                        val = j + 1;
                        break;
                    }
                }
                break;
            }
            if (!Character.isLetter(c) && !Character.isDigit(c) && c != '.' || c == ';') {
                val = i;
                break;
            }
        }
        return val;
    }
}

