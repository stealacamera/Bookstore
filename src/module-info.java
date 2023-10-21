module BookstoreRE {
	requires javafx.base;
	requires javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.media;
    requires javafx.web;
	requires java.desktop;
	requires junit;
	
    exports views;
    exports views.employees;
    exports views.books;
    exports views.stats;
    
    exports controllers;
    exports controllers.books;
    
    exports exceptions;
    
    exports models;
    exports models.helpers;
}