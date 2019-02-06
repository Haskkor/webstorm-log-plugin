package com.haskkor.webstorm.plugin.log;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
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
        caretModel.moveCaretRelatively(text.length(),1,  false, false, true);
    }

    private int getLineNumber(int offset) {
        return document.getLineNumber(offset);
    }

    private String getFileName(AnActionEvent e) {
        VirtualFile vFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        return vFile != null ? vFile.getName() : null;
    }

    private String buildString(String fileName, int lineNumber, int offset) {
        StringBuilder sb  = new StringBuilder("\n");
        sb.append("console.log(\"");
        sb.append(fileName);
        sb.append(" l.");
        sb.append(lineNumber + 1);
        sb.append("\");");
        return sb.toString();
    }

    private int findWordStart(int offset) {
        CharSequence sequence = document.getCharsSequence();
        if (sequence.charAt(offset).equals(" ")) {
            return -1;
        } else {

        }
        return 0;
    }
}

