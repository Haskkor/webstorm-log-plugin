package com.haskkor.webstorm.plugin.log;

import com.haskkor.webstorm.plugin.logUpdate.LogUpdater;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class NewLineLog extends AnAction {

    private Project project;
    private Editor editor;
    private Document document;
    private CaretModel caretModel;

    @Override
    public void actionPerformed(AnActionEvent e) {
        project = e.getProject();
        editor = e.getData(CommonDataKeys.EDITOR);
        caretModel = editor.getCaretModel();
        document = editor.getDocument();

        //Set visibility only in case of existing project and editor and if some text in the editor is selected
        e.getPresentation().setVisible(project != null && editor != null && editor.getSelectionModel().hasSelection());

        // Gert carret offset
        int carretOffset = caretModel.getOffset();
        // Get line number
        int lineNumber = getLineNumber(carretOffset);
        // Get file name
        String fileName = getFileName(e);
        // Get line end position
        int lineEnd = document.getLineEndOffset(lineNumber);

        // Build the string
        String text = buildString(fileName, lineNumber, carretOffset);

        // Write in the document
        WriteCommandAction.runWriteCommandAction(project, () ->
                document.replaceString(lineEnd, lineEnd, text)
        );

        // Move the caret
        caretModel.moveCaretRelatively(text.length(), 1, false, false, true);

        // Get new offset and update the other logs
        int newCarretOffset = caretModel.getOffset();
        LogUpdater logUpdater = new LogUpdater(project, document, newCarretOffset);
        logUpdater.updateConsoleLogs();
    }

    private int getLineNumber(int offset) {
        return document.getLineNumber(offset);
    }

    private String getFileName(AnActionEvent e) {
        VirtualFile vFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        return vFile != null ? vFile.getName() : null;
    }

    private String buildString(String fileName, int lineNumber, int offset) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append("console.log(\'");
        sb.append(fileName);
        sb.append(" l.");
        sb.append(lineNumber + 2);
        String word = findWordUnderCarret(offset);
        if (word.length() > 0) {
            sb.append(" ");
            sb.append(word);
            sb.append("\', ");
            sb.append(word);
        } else {
            sb.append("\'");
        }
        sb.append(");");
        return sb.toString();
    }

    private String findWordUnderCarret(int offset) {
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
        for (int i = start; i > 0; i--) {
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

