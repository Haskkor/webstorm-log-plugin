package com.haskkor.webstorm.plugin.documentHandler;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.actionSystem.PlatformDataKeys;

public class DocumentHandler {

    private Document document;
    private Project project;
    private AnActionEvent anActionEvent;

    public DocumentHandler(Project project, Document document, AnActionEvent anActionEvent) {
        this.project = project;
        this.document = document;
        this.anActionEvent = anActionEvent;
    }

    public void replaceText(int offsetStart, int offsetEnd, String textReplace) {
        WriteCommandAction.runWriteCommandAction(this.project, () ->
                this.document.replaceString(offsetStart, offsetEnd, textReplace)
        );
    }

    public String getFileName() {
        VirtualFile vFile = this.anActionEvent.getData(PlatformDataKeys.VIRTUAL_FILE);
        return vFile != null ? vFile.getName() : null;
    }

}

