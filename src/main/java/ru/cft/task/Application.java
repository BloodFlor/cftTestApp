package ru.cft.task;

import ru.cft.task.FilterLines.FilterLines;

public class Application {
    public static void main(String[] args) {
		FilterLines filterLines = new FilterLines();
		filterLines.parseOptions(args);
		filterLines.filter();
    }
}