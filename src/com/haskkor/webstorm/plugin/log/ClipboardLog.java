package com.haskkor.webstorm.plugin.log;

import com.haskkor.webstorm.plugin.logFormat.LogFormatter;

public class ClipboardLog extends AnAction {

    private CopyPasteManager copyPasteManager;
    public LogFormatter logFormatter;
    private Project project;
    private Editor editor;
    private Document document;
    private CaretModel caretModel;
    private DocumentHandler documentHandler;

    @Override
    public void actionPerformed(AnActionEvent e) {
        this.copyPasteManager = new CopyPasteManager();
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
        if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return (String) t.getTransferData(DataFlavor.stringFlavor);
        }
        return "";
    }
}
