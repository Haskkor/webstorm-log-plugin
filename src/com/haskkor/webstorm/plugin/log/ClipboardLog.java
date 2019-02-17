package com.haskkor.webstorm.plugin.log;

import com.haskkor.webstorm.plugin.documentHandler.DocumentHandler;
import com.haskkor.webstorm.plugin.logFormat.LogFormatter;
import com.haskkor.webstorm.plugin.logUpdate.LogUpdater;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.editor.CaretModel;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ClipboardLog extends AnAction {

    private CopyPasteManager copyPasteManager;
    private LogFormatter logFormatter;
    private Project project;
    private Editor editor;
    private Document document;
    private CaretModel caretModel;
    private DocumentHandler documentHandler;

    @Override
    public void actionPerformed(AnActionEvent e) {
        this.copyPasteManager = CopyPasteManager.getInstance();
        this.logFormatter = new LogFormatter();
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
        String text = logFormatter.buildString(fileName, lineNumber, getClipboardContent());

        // Write in the document
        this.documentHandler.replaceText(lineEnd, lineEnd, text);

        // Move the caret
        caretModel.moveCaretRelatively(text.length(), 1, false, false, true);

        // Get new offset and update the other logs
        int newCaretOffset = caretModel.getOffset();
        LogUpdater logUpdater = new LogUpdater(project, document, newCaretOffset, e);
        logUpdater.updateConsoleLogs();
    }

    private String getClipboardContent(){
        Transferable t = this.copyPasteManager.getContents();
        try {
            if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String) t.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (HeadlessException | UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
