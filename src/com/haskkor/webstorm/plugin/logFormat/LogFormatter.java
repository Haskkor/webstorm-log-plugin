package com.haskkor.webstorm.plugin.logFormat;

public class LogFormatter {

    public String buildString(String fileName, int lineNumber, String word) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append("console.log(\'");
        sb.append(fileName);
        sb.append(" l.");
        sb.append(lineNumber + 2);
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
}
